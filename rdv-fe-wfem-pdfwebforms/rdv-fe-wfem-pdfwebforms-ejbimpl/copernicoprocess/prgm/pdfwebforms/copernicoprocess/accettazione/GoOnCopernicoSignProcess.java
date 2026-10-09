package prgm.pdfwebforms.copernicoprocess.accettazione;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.QASCallData;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.copernicoprocess.backend.PdfCopernicoProcessFacade;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.model.PdfPersonSignDataModel;
import prgm.pdfwebforms.signprocess.business.GoOnSignProcess;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnCopernicoSignProcess extends BusinessCommand {

	private static final String DAO_XML_NAME = "PdfWebForms.PdfCopernicoProcess";
	private static final String MSG_CODICI = "Sembra che ci sia qualcosa che non va con i codici che hai inserito.<br>Per favore prova ad inserirli nuovamente";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfModel pdf = (PdfModel)dataModel;

		if(!Basket.verifyMvcConsistency(this, pdf))
        	return pdf;
        
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
					if(pdf.getPdfData().getIsSwitch().booleanValue()){
						if(!checkPinOtp(csc, pdf, pdf.getPersonaCorrente()))
							return pdf;						
						pdf.getPersonaCorrente().getSignData().setDigit1Digitato(new StringType());
						pdf.getPersonaCorrente().getSignData().setDigit2Digitato(new StringType());
						pdf.getPersonaCorrente().getSignData().setOtpDigitato(new StringType());
					}
    				PdfPersonSignProcessInfo pi = pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos()[nextPdfIndex];
    				pdf.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
    	   			pdf = facade.gotoPdf(csc, pdf, nextPdfIndex, false);
    	    		return pdf;
				}
				
				pdf.getPersonaCorrente().getSignData().setStepFlagSignSuperato(true);
				return pdf;
			}
			
			if(!pdf.getPersonaCorrente().getSignData().isStepOtpSuperato()){

				if(!checkPinOtp(csc, pdf, pdf.getPersonaCorrente()))
					return pdf;
				
				pdf.getPersonaCorrente().getSignData().setStepOtpSuperato(true);
    			
				// Cerco il prossimo cliente/pdf da firmare
				pdf.setPersonaCorrente(null);
    			for(PdfPersonModel personaCorrente : pdf.getFullProcessPersons()){
    				
    				if(personaCorrente.getSignData().isStepOtpSuperato())
    					continue;
    				
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
				
    			if(pdf.getPersonaCorrente() == null){
					
					pdf = acceptPdf(csc, pdf);
					
				}else{
					
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
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected boolean checkPinOtp(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){

		if(!verificaPin2(csc,pdf,person))
			return false;
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return false;
		}
		
		if(!verificaOtp(csc,pdf,person))
			return false;
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return false;
		}
		return true;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	protected static boolean verificaPin2(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){

		PdfPersonSignDataModel signData = person.getSignData();
		signData.getDigit1DaChiedere().resetTypeErrors();

		if(pdf.isTestMode())
			return true;
		
		try{
			if(signData.getDigit1Digitato().isNull() || signData.getDigit2Digitato().isNull()){
				signData.getDigit1DaChiedere().addTypeError("Digitare le cifre del Codice Segreto");
				return false;
			}
			
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML_NAME).executeQASAccess("verificaPin2",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				signData.getDigit1DaChiedere().addTypeError("Probabilmente si è verificato un errore tecnico.<br>Contatta il Servizio Clienti al numero verde 800.107.107");
			}else{
				if(!"0".equals(signData.getEsitoChiamataServizio().toString()))
					signData.getDigit1DaChiedere().addTypeError(MSG_CODICI);
			}
			
			if(signData.getDigit1DaChiedere().hasTypeErrors())
				return false;
			return true;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel verificare il pin per "+person.readCognomeNome()+": "+daoe.toString());
			return true;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	protected static boolean verificaOtp(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		
		PdfPersonSignDataModel signData = person.getSignData();
		signData.getOtpDigitato().resetTypeErrors();
		
		if(pdf.isTestMode())
			return true;
		
		try{
			
			if(signData.getOtpDigitato().isNull()){
				signData.getOtpDigitato().addTypeError("Digitare il codice OTP");
				return false;
			}
			
			if(!signData.getSavOtpDigitato().isNull()){
				if(!signData.getOtpDigitato().equals(signData.getSavOtpDigitato())){
					signData.getOtpDigitato().addTypeError("Inserire lo stesso codice OTP ricevuto in precedenza");
					return false;
				}else{
					return true;
				}
			}
			
			signData.setSavOtpDigitato(new StringType());
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML_NAME).executeQASAccess("verificaOtp",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				signData.getOtpDigitato().addTypeError("Probabilmente si è verificato un errore tecnico.<br>Contatta il Servizio Clienti al numero verde 800.107.107");
			}else{
				if(!"OK".equals(signData.getEsitoChiamataServizio().toString())){
					String msg = signData.getMessaggioChiamataServizio().toString();
					if(msg.equalsIgnoreCase("VALIDATION FAILED") || msg.equalsIgnoreCase("OTP BRUCIATA"))
						signData.getOtpDigitato().addTypeError(MSG_CODICI);
					else if(msg.equalsIgnoreCase("TERZO CODICE BLOCCATO"))
						signData.getOtpDigitato().addTypeError("Per motivi di sicurezza l'accesso è stato bloccato.<br>Contatta il Servizio Clienti al numero verde 800.107.107");
					else
						signData.getOtpDigitato().addTypeError(MSG_CODICI);
				}
			}
			if(signData.getOtpDigitato().hasTypeErrors())
				return false;
			
			signData.setSavOtpDigitato(new StringType(signData.getOtpDigitato().toString()));
			return true;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel verificare l'OTP per "+person.readCognomeNome()+": "+daoe.toString());
			return true;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel acceptPdf(ClientSessionContext csc, PdfModel pdf) throws Exception{

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
			
		PdfCopernicoProcessFacade signFacade = (PdfCopernicoProcessFacade)FacadeLoader.getFacade(csc, PdfCopernicoProcessFacade.class);
		pdf = signFacade.acceptPdfInstance(csc, pdf, pdfSignedContent);
		
		if(pdf.hasCommandErrors()){
			pdf.setEndErrorTraceMsg(pdf.getCommandErrors().get(0).toString().replaceAll("\\n", ""));
			setNextCommandClass(PdfCopernicoAccettazioneErrors.class);
			return pdf;
		}
		
		setNextCommandClass(PdfCopernicoAccettazioneEndProcess.class);
    	return pdf;
		
	}
	
}
