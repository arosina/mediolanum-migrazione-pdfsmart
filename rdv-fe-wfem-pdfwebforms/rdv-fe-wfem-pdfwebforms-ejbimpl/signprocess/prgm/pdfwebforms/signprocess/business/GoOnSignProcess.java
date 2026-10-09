package prgm.pdfwebforms.signprocess.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.signprocess.backend.PdfSignProcessFacade;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;
import prgm.pdfwebforms.signprocess.display.PdfEndSignProcess;
import prgm.pdfwebforms.signprocess.display.PdfEndVolatileSignProcess;
import prgm.pdfwebforms.signprocess.display.PdfErrorsOnSign;
import prgm.pdfwebforms.signprocess.display.PdfPersonSign;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOnSignProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);

			PdfModel pdf = (PdfModel)dataModel;
			
            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;
            
        	pdf.resetCommandErrors();
        	
        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	pdf.getPdfData().initPdfsFromData();
        	
        	setNextCommandClass(PdfPersonSign.class);
    	
			if(!pdf.getPersonaCorrente().getSignData().isStepPinSuperato()){
			
				if(!checkPin(csc, pdf, pdf.getPersonaCorrente()))
					return pdf;
				
				pdf.getPersonaCorrente().getSignData().setStepPinSuperato(true);
				return pdf;
				
			}
			
			if(!pdf.getPersonaCorrente().getSignData().isStepOtpSuperato()){

				if(!checkSign(pdf, pdf.getPersonaCorrente()))
					return pdf;

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
						if(!checkOtp(csc, pdf, pdf.getPersonaCorrente()))
							return pdf;						
						pdf.setFirstSignPage(false);
						pdf.getPersonaCorrente().getSignData().setOtpDigitato(new StringType());
					}
    				PdfPersonSignProcessInfo pi = pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos()[nextPdfIndex];
    				pdf.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
    	   			pdf = facade.gotoPdf(csc, pdf, nextPdfIndex, false);
    	    		return pdf;
				}
    			
				if(!checkOtp(csc, pdf, pdf.getPersonaCorrente()))
					return pdf;
				
				pdf.setFirstSignPage(false);
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
					
    				if(!PdfBarcodeUtils.initBarcodeFirmaDigitale(csc, pdf)){
    					setNextCommandClass(PdfErrorsOnSign.class);
    					return pdf;
    				}
    				
					pdf = signPdf(csc, pdf);
					
				}else{
					
            		SignUtility.getDigitDaChiedere(csc, pdf, pdf.getPersonaCorrente());
            		if(pdf.hasCommandErrors()){
            			setNextCommandClass(PdfErrorsOnSign.class);
                		return pdf;            			
            		}
 				}
				
			}
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected boolean checkPin(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		
		if(!SignUtility.verificaPin(csc,pdf,person))
			return false;
		
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfErrorsOnSign.class);
			return false;
		}
		
		SignUtility.generaOTP(csc,pdf,person);
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfErrorsOnSign.class);
			return false;
		}
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected boolean checkOtp(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		if(!SignUtility.verificaOtp(csc,pdf,person))
			return false;
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfErrorsOnSign.class);
			return false;
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean checkSign(PdfModel pdf, PdfPersonModel person){
		
		PdfDataModel pdfData = pdf.getPdfData();
		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove)); 
		
		PdfPersonSignProcessInfo pi = person.getPdfPersonsSignProcessInfos()[pdf.getPdfData().getPdfIndex().intValue()];
		pi.getSignedFieldNames().clear();
		
		// To skip sign on non visible pages
		ArrayList<String> visiblePagesAsArray = null; 
		if(!pdfData.getVisiblePages().isNull()){
			String[] visiblePages = pdfData.getVisiblePages().toString().split("\\,");
			visiblePagesAsArray = new ArrayList<String>(Arrays.asList(visiblePages)); 
		}
		
		int numFirmate = 0;
		int numNonFirmate = 0;
		List<PdfFieldInfos> personSignFields = pdf.getPersonSignFields(person);
		for(PdfFieldInfos fi : personSignFields){
			
			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName))
				continue;
			
			if(visiblePagesAsArray != null && !visiblePagesAsArray.contains(""+fi.page))
				continue;
			
			AbstractType field = (AbstractType)pdf.getPdfData().readProperty(fi.htmlFieldName);
			if(field == null)
				continue;
			if(field.toString().equals("true")){
				pi.getSignedFieldNames().add(fi.htmlFieldName);
				numFirmate++;
			}else{
				numNonFirmate++;
			}
		}
		
		boolean accettate = true;
		List<PdfFieldInfos> personClauseFields = pdf.getPersonClauseFields(person);
		for(PdfFieldInfos fi : personClauseFields){
			
			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName))
				continue;
			
			AbstractType field = (AbstractType)pdf.getPdfData().readProperty(fi.htmlFieldName);
			if(field == null)
				continue;
			if(!field.toString().equals("true")){
				accettate = false;
				break;
			}
		}
		
		String clausoleMsg = "";
		if(!accettate)
			clausoleMsg = " e tutte le clausole";
		
		if(pdf.getPdfData().getSignAll().booleanValue()){
			if(numNonFirmate > 0){
				pdf.addCommandError("Selezionare tutte le firme"+clausoleMsg+" di "+(pdf.isInAccettazioneCopernico()?person.readNomeCognome():person.readCognomeNome()));
				return false;
			}
		}else if(pdf.getPdfAnag().getSignAll().booleanValue()){
				if(numNonFirmate > 0){
					pdf.addCommandError("Selezionare tutte le firme"+clausoleMsg+" di "+(pdf.isInAccettazioneCopernico()?person.readNomeCognome():person.readCognomeNome()));
					return false;
				}
		}else{
			if(numFirmate == 0){
				pdf.addCommandError("Selezionare le firme"+clausoleMsg+" di "+(pdf.isInAccettazioneCopernico()?person.readNomeCognome():person.readCognomeNome()));
				return false;
			}
		}
		
		if(!accettate){
			pdf.addCommandError("Selezionare tutte le clausole di "+(pdf.isInAccettazioneCopernico()?person.readNomeCognome():person.readCognomeNome()));
			return false;
		}
		
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel signPdf(ClientSessionContext csc, PdfModel pdf) throws Exception{

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
			
		PdfSignProcessFacade signFacade = (PdfSignProcessFacade)FacadeLoader.getFacade(csc, PdfSignProcessFacade.class);
		pdf = signFacade.signPdfInstance(csc, pdf, pdfSignedContent);
		
		if(pdf.hasCommandErrors()){
			setNextCommandClass(PdfErrorsOnSign.class);
			return pdf;
		}
		
		if(pdf.getCallbackData() != null){
			Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingContent", new ByteArrayType(pdfSignedContent));
			Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingCompilationMode", new StringType(pdf.getPdfCompilationMode().toString()));
			Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingBarcodes", new StringType(pdf.getPdfGeneratedBarcodes().toString()));
			Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingFlagWayout", new BooleanType(false));
			Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingNoteFBCopernico", new StringType(""));
		}
		
		if(pdf.getPdfData().getIsVolatile().booleanValue()){
			setNextCommandClass(PdfEndVolatileSignProcess.class);
		}else{
			setNextCommandClass(PdfEndSignProcess.class);
		}
    	return pdf;
		
	}
	
}
