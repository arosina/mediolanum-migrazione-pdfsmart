package prgm.pdfwebforms.copernicoprocess.accettazione;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.copernicoprocess.backend.PdfCopernicoProcessFacade;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.signprocess.business.GoOnSignProcess;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnCopernicoSignBasketProcess extends GoOnCopernicoSignProcess {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfModel pdf = (PdfModel)dataModel;

		if(!Basket.verifyMvcConsistency(this, pdf))
        	return pdf;
        
		Basket basket = pdf.getBasket();

		try{
			
 			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
 			
        	pdf.resetCommandErrors();
        	
        	if(!PdfUtil.testConcorrenzaCopernicoIsOk(csc, pdf)){
    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
    			return pdf;
    		}
        	
        	pdf.getPdfData().initPdfsFromData();
        	
        	setNextCommandClass(PdfCopernicoPersonSign.class);
    	
        	if(!pdf.getPersonaCorrente().getSignData().isStepFlagSignSuperato()){
        		
				if(!GoOnSignProcess.checkSign(pdf, pdf.getPersonaCorrente()))
					return pdf;

				pdf.setFirstSignPage(false);

				// cerco il prossimo pdf che la persona corrente deve firmare
				int nextPdfIndex = -1;
				for(int i=pdf.getPdfData().getPdfIndex().intValue()+1;i<pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos().length;i++){
					PdfPersonSignProcessInfo pi = pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos()[i];
					if(pi.isHasSignOnPdf()){
						nextPdfIndex = i;
						break;
					}
				}
				if(nextPdfIndex >= 0){
    				PdfPersonSignProcessInfo pi = pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos()[nextPdfIndex];
    				pdf.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
    	   			pdf = facade.gotoPdf(csc, pdf, nextPdfIndex, false);
    	    		return pdf;
				}else{
					PdfModel nextDispo = Basket.findNextDispoToSignCurrentPerson(csc, facade, pdf);
					if(nextDispo != null)
						return nextDispo;
				}
       		
				pdf.getPersonaCorrente().getSignData().setStepFlagSignSuperato(true);
				return pdf;
        	}
        	
			if(!pdf.getPersonaCorrente().getSignData().isStepOtpSuperato()){

				if(!checkPinOtp(csc, pdf, pdf.getPersonaCorrente()))
					return pdf;
				
				basket.getPersonaCorrente().getSignData().setStepOtpSuperato(true);
				basket.getPersoneGestite().add(basket.getPersonaCorrente());

				PdfModel nextDispo = Basket.findFirstDispoToSignNextPerson(csc, facade, pdf, false);
				if(nextDispo != null) {
					pdf = nextDispo;					
					
		    		SignUtility.generaOTP(csc,pdf,pdf.getPersonaCorrente());
		    		if(pdf.hasCommandErrors()){
		    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
		    			return pdf;
		    		}
		    		
            		SignUtility.getDigitDaChiedere(csc, pdf, pdf.getPersonaCorrente());
            		if(pdf.hasCommandErrors()){
            			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
                		return pdf;            			
            		}
					return pdf;
				}
					
				pdf = acceptBasket(csc, pdf);
					
			}
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			pdf.addCommandError(StartAccettaPropostaCopernicoProcess.ERRORE_TECNICO_MSG);
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel acceptBasket(ClientSessionContext csc, PdfModel currentPdf) throws Exception{

		ArrayList<byte[]> pdfSignedContents = new ArrayList<byte[]>();
		StringBuilder msgFineOperazione = new StringBuilder();
		
		Basket basket = currentPdf.getBasket();
		for(BasketElement be : basket.getBasketElements()) {
			
			be.setHasFreezeError(false);
			
			PdfModel pdf = be.getDispoPdf();
				
			pdf.setErrorOnSign(false);
			
			byte[] pdfSignedContent = SignUtility.firmaPdf(csc, pdf);
			if(pdf.hasCommandErrors() || pdfSignedContent == null){
				if(pdf.hasCommandErrors())
					pdf.setEndErrorTraceMsg(pdf.getCommandErrors().get(0).toString().replaceAll("\\n", ""));
				else if(pdfSignedContent == null)
					pdf.setEndErrorTraceMsg("pdfSignedContent is null");
				pdf.setErrorOnSign(true);
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
			}
			
			pdfSignedContents.add(pdfSignedContent);
		}
			
		PdfCopernicoProcessFacade signFacade = (PdfCopernicoProcessFacade)FacadeLoader.getFacade(csc, PdfCopernicoProcessFacade.class);
		for(int i=0;i<basket.getBasketElements().size();i++) {

			BasketElement be = basket.getBasketElements().get(i);
			PdfModel pdf = be.getDispoPdf();
			
			pdf = signFacade.acceptPdfInstance(csc, pdf, pdfSignedContents.get(i));
			
			if(pdf.hasCommandErrors()){
				be.setHasFreezeError(true);
				String title = pdf.getPdfData().getPdfTitle().isNull() ? pdf.mainPdfAnag().getPdfDescr().toString() : pdf.getPdfData().getPdfTitle().toString();
				msgFineOperazione.append("<br>"+title+": "+pdf.getCommandErrors().get(0));
			}
		}

		if(msgFineOperazione.length() > 0)
			currentPdf.setMessaggioFineOperazione("Alcune operazioni non sono andate a buon fine."+msgFineOperazione);

		for(BasketElement be : basket.getBasketElements()) {
			if(!be.hasFreezeError()) {
				setNextCommandClass(PdfCopernicoAccettazioneEndProcess.class);
				return currentPdf;
			}
		}
		
		setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
    	return currentPdf;
		
	}
	
}
