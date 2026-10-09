package prgm.pdfwebforms.copernicoprocess.accettazione;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartCopernicoSignProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
 
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfModel pdf = (PdfModel)dataModel;

		try{
			pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();
        	
        	if(!PdfUtil.testConcorrenzaCopernicoIsOk(csc, pdf)){
    			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
    			return pdf;
    		}
        	
			// Cerco il primo cliente che deve firmare
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdf.setPersonaCorrente(null);
			for(PdfPersonModel personaCorrente : pdf.getFullProcessPersons()){
				
				for(int i=0;i<personaCorrente.getPdfPersonsSignProcessInfos().length;i++){
    				PdfPersonSignProcessInfo pi = personaCorrente.getPdfPersonsSignProcessInfos()[i];
					if(pi.isHasSignOnPdf()){
	    	       		personaCorrente.setIndexInFieldName(pi.getIndexInFieldName());
	   					pdf = facade.gotoPdf(csc, pdf, i, false);
						pdf.setPersonaCorrente(personaCorrente);
						break;
					}
				}
				if(pdf.getPersonaCorrente() != null)
					break;
			}
			
    		if(pdf.getPersonaCorrente() == null){ // Non dovrebbe mai accadere!
				pdf.addCommandError("Nessun pdf per il soggetto previsto in firma");
				setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
				return pdf;
    		}
    		
    		if(!pdf.isInBasket()) {
    			
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
    		}
    		
			setNextCommandClass(PdfCopernicoPersonSign.class);
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
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
