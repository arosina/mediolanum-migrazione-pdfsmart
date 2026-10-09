package prgm.pdfwebforms.signprocess.business;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.signprocess.backend.PdfSignProcessFacade;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;
import prgm.pdfwebforms.signprocess.display.PdfEndSignProcess;
import prgm.pdfwebforms.signprocess.display.PdfErrorsOnSign;
import prgm.pdfwebforms.signprocess.display.PdfPersonSign;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnBasketSignProcess extends GoOnSignProcess {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);

			PdfModel pdf = (PdfModel)dataModel;

            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;
            
            Basket basket = pdf.getBasket();
			
        	pdf.resetCommandErrors();
        	
        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	pdf.getPdfData().initPdfsFromData();
        	
        	setNextCommandClass(PdfPersonSign.class);
    	
			if(!basket.getPersonaCorrente().getSignData().isStepPinSuperato()){
			
				if(!checkPin(csc, pdf, basket.getPersonaCorrente()))
					return pdf;
				
				basket.getPersonaCorrente().getSignData().setStepPinSuperato(true);
				return pdf;
				
			}
			
			if(!basket.getPersonaCorrente().getSignData().isStepFlagSignSuperato()){

				if(!checkSign(pdf, basket.getPersonaCorrente()))
					return pdf;

				pdf.setFirstSignPage(false);

				// cerco il prossimo pdf che la persona corrente deve firmare
				int nextPdfIndex = -1;
				for(int i=pdf.getPdfData().getPdfIndex().intValue()+1;i<basket.getPersonaCorrente().getPdfPersonsSignProcessInfos().length;i++){
					PdfPersonSignProcessInfo pi = basket.getPersonaCorrente().getPdfPersonsSignProcessInfos()[i];
					if(pi.isHasSignOnPdf()){
						nextPdfIndex = i;
						break;
					}
				}
				if(nextPdfIndex >= 0){
    				PdfPersonSignProcessInfo pi = basket.getPersonaCorrente().getPdfPersonsSignProcessInfos()[nextPdfIndex];
    				basket.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
    	   			pdf = facade.gotoPdf(csc, pdf, nextPdfIndex, false);
    	    		return pdf;
				}else{
					PdfModel nextDispo = Basket.findNextDispoToSignCurrentPerson(csc, facade, pdf);
					if(nextDispo != null) {
						basket.getPersonaCorrente().getSignData().setLeggiIlContrattoClicked(new BooleanType());
						return nextDispo;
					}
				}
    			
				basket.getPersonaCorrente().getSignData().setStepFlagSignSuperato(true);
				return pdf;
			}

			if(!basket.getPersonaCorrente().getSignData().isStepOtpSuperato()){
				
				if(!checkOtp(csc, pdf, basket.getPersonaCorrente()))
					return pdf;
				
				basket.getPersonaCorrente().getSignData().setStepOtpSuperato(true);
				basket.getPersoneGestite().add(basket.getPersonaCorrente());
		
				PdfModel nextDispo = Basket.findFirstDispoToSignNextPerson(csc, facade, pdf, false);
				if(nextDispo == null)
					nextDispo = Basket.findFirstDispoToSignNextPerson(csc, facade, pdf, true);
				if(nextDispo != null) {
					pdf = nextDispo;					
					
            		SignUtility.getDigitDaChiedere(csc, pdf, pdf.getPersonaCorrente());
            		if(pdf.hasCommandErrors()){
            			setNextCommandClass(PdfErrorsOnSign.class);
                		return pdf;            			
            		}
            		
					return pdf;
				}

				pdf = signBasket(csc, pdf);
				
			}			
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel signBasket(ClientSessionContext csc, PdfModel currentPdf) throws Exception{

		ArrayList<byte[]> pdfSignedContents = new ArrayList<byte[]>();
		StringBuilder msgFineOperazione = new StringBuilder();
		
		Basket basket = currentPdf.getBasket();
		for(BasketElement be : basket.getBasketElements()) {
			
			be.setHasFreezeError(false);
			
			PdfModel pdf = be.getDispoPdf();
			
			if(!PdfBarcodeUtils.initBarcodeFirmaDigitale(csc, pdf)){
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
			}
			
			pdf.setErrorOnSign(false);
			
			pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE));
			
			// Chiamo il completamento sul driver del primo pdf
			String errorMsg = PdfDriverCaller.callPdfCompleted(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
				pdf.addCommandError(errorMsg);
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
			}
			
			byte[] pdfSignedContent = SignUtility.firmaPdf(csc, pdf);
			if(pdf.hasCommandErrors() || pdfSignedContent == null){
				pdf.setErrorOnSign(true);
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
			}
			
			pdfSignedContents.add(pdfSignedContent);
		}

		PdfSignProcessFacade signFacade = (PdfSignProcessFacade)FacadeLoader.getFacade(csc, PdfSignProcessFacade.class);
		for(int i=0;i<basket.getBasketElements().size();i++) {
			
			BasketElement be = basket.getBasketElements().get(i);
			PdfModel pdf = be.getDispoPdf();
			pdf = signFacade.signPdfInstance(csc, pdf, pdfSignedContents.get(i));
			if(pdf.hasCommandErrors()) {
				be.setHasFreezeError(true);
				String title = pdf.getPdfData().getPdfTitle().isNull() ? pdf.mainPdfAnag().getPdfDescr().toString() : pdf.getPdfData().getPdfTitle().toString();
				msgFineOperazione.append("<br>"+title+": "+pdf.getCommandErrors().get(0));
			}
		}
		
		if(msgFineOperazione.length() > 0)
			currentPdf.setMessaggioFineOperazione("<br>Alcune operazioni non sono andate a buon fine, torna nel 5D per riproporle."+msgFineOperazione);

		for(BasketElement be : basket.getBasketElements()) {
			if(!be.hasFreezeError()) {
				setNextCommandClass(PdfEndSignProcess.class);
				return currentPdf;
			}
		}

		setNextCommandClass(PdfErrorsOnSign.class);
    	return currentPdf;
		
	}
	
}
