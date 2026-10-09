package prgm.pdfwebforms.drivers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.carrello.DispositivaCarrelloModel;
import prgm.pdfwebforms.drivers.io.AfterSavedPdfInputData;
import prgm.pdfwebforms.drivers.io.AfterSavedPdfOutputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewInputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewOutputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventInputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventOutputData;
import prgm.pdfwebforms.drivers.io.FinalEventInputData;
import prgm.pdfwebforms.drivers.io.FinalEventOutputData;
import prgm.pdfwebforms.drivers.io.FreezeEventInputData;
import prgm.pdfwebforms.drivers.io.FreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.InitPdfInputData;
import prgm.pdfwebforms.drivers.io.InitPdfOutputData;
import prgm.pdfwebforms.drivers.io.LoadPdfInputData;
import prgm.pdfwebforms.drivers.io.LoadPdfOutputData;
import prgm.pdfwebforms.drivers.io.MomEventInputData;
import prgm.pdfwebforms.drivers.io.MomEventOutputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedInputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedOutputData;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesRequest;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesResponse;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataResponse;
import prgm.pdfwebforms.drivers.io.SendToCliEventInputData;
import prgm.pdfwebforms.drivers.io.SendToCliEventOutputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfInputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventInputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfOutputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfOutputData;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataRequest;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataResponse;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentInputData;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentOutputData;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataRequest;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataResponse;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataRequest;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataRequest;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataRequest;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataRequest;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataRequest;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataResponse;
import prgm.pdfwebforms.drivers.io.squadra.ElementoSquadra;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataRequest;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataResponse;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataRequest;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataResponse;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventInputData;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventOutputData;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfDriverCaller {

	private static String EX_MESSAGE_PREFIX = "Si sono verificati problemi nei controlli ";
	private static String sDRVMESSAGEPREFIX = " on driver method ";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static InitPdfOutputData callInitPdf(ClientSessionContext csc, PdfModel pdf, DispositivaCarrelloModel dispositivaCarrello, AgevolazioneModel agevolazione){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		InitPdfInputData eventInput = new InitPdfInputData(pdf, dispositivaCarrello, agevolazione);
		InitPdfOutputData eventOutput = null;
		try{
			eventOutput = pdfData.getPdfDriver().onInitPdf(csc, eventInput);
			manageEventResult(eventOutput, pdf, false);
			return eventOutput; 
		}catch(Throwable t){
			eventOutput = new InitPdfOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onInitPdf)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void callSubmitPdf(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return;
		
		SubmitPdfInputData eventInput = new SubmitPdfInputData(pdf);
		try{
			pdfData.getPdfDriver().onSubmitPdf(csc, eventInput);
			return; 
		}catch(Throwable t){
			pdf.addCommandWarning(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onSubmitPdf). Proseguendo la compilazione potrebbe risultare incoerente<br><br>"+t.toString());
			return;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callAfterSavedPdf(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		AfterSavedPdfInputData eventInput = new AfterSavedPdfInputData(pdf);
		try{
			AfterSavedPdfOutputData eventOutput = pdfData.getPdfDriver().onAfterSavedPdf(csc, eventInput); 
			if(eventOutput != null){
				if(eventOutput.getErrorMessage() != null)
					return eventOutput.getErrorMessage();
			}
			return null;
		}catch(Throwable t){
			return EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onAfterSavedPdf)<br><br>"+t.toString();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static LoadPdfOutputData callLoadPdf(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		LoadPdfInputData eventInput = new LoadPdfInputData(pdf);
		LoadPdfOutputData eventOutput = null;
		try{
			eventOutput = pdfData.getPdfDriver().onLoadPdf(csc, eventInput);
			manageEventResult(eventOutput, pdf, false);
			return eventOutput; 
		}catch(Throwable t){
			eventOutput = new LoadPdfOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onLoadPdf)<br><br>"+t.toString());
			return eventOutput;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean callVerifyPdf(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return false;
		
		VerifyPdfInputData eventInput = new VerifyPdfInputData(pdf, pdf.getPdfDataBeforeImage());
		VerifyPdfOutputData eventOutput = null;
		try{
			pdf.getErrorsCodes().clear();
			pdf.getWarningsCodes().clear();
			eventOutput = pdfData.getPdfDriver().onVerifyPdf(csc, eventInput);
			manageEventResult(eventOutput, pdf, true);
			if(eventOutput != null){
				pdf.setErrorsCodes(eventOutput.getErrorsCodes());
				for(String error : eventOutput.getErrors())
					pdf.addCommandError(new CommandError(error));
				pdf.setWarningsCodes(eventOutput.getWarningsCodes());
				for(String warning : eventOutput.getWarnings())
					pdf.addCommandWarning(new CommandWarning(warning));
			}
			return eventOutput == null ? false: eventOutput.isReloadPersons(); 
		}catch(Throwable t){
			pdf.addCommandError(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onVerifyPdf)<br><br>"+t.toString());
			return false;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void callPdfVerified(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return;
		
		PdfVerifiedInputData eventInput = new PdfVerifiedInputData(pdf);
		PdfVerifiedOutputData eventOutput = null; 
		try{
			eventOutput = pdfData.getPdfDriver().onPdfVerified(csc, eventInput);
			manageEventResult(eventOutput, pdf, true);
			if(eventOutput != null){
				pdf.getErrorsCodes().putAll(eventOutput.getErrorsCodes());
				for(String error : eventOutput.getErrors())
					pdf.addCommandError(new CommandError(error));
				pdf.getWarningsCodes().putAll(eventOutput.getWarningsCodes());
				for(String warning : eventOutput.getWarnings())
					pdf.addCommandWarning(new CommandWarning(warning));
			}
		}catch(Throwable t){
			pdf.addCommandError(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onPdfVerified)<br><br>"+t.toString());
			return;
		}
		try {
			new PdfModuliAggiuntiviManager().addQuestionarioAML(csc, pdfData.getPdfDriver(), eventInput);
		}catch(Throwable t){
			pdf.addCommandError(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".addQuestionarioAML)<br><br>"+t.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callBeforePreview(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		BeforePreviewInputData eventInput = new BeforePreviewInputData(pdf);
		BeforePreviewOutputData eventOutput = null;
		try{
			eventOutput = pdfData.getPdfDriver().onPdfPreview(csc, eventInput);
			if(eventOutput != null){
				if(eventOutput.getErrorMessage() != null)
					return eventOutput.getErrorMessage();
				if(eventOutput.getFacSimileOnPreview() != null)
					pdf.getPdfData().setFacSimileOnPreview(new BooleanType(eventOutput.getFacSimileOnPreview().booleanValue()));
				if(eventOutput.getInviaInSedeButtonLabel() != null)
					pdf.getPdfData().setInviaInSedeButtonLabel(new StringType(eventOutput.getInviaInSedeButtonLabel()));
				if(eventOutput.getFirmaDigitaleButtonLabel() != null)
					pdf.getPdfData().setFirmaDigitaleButtonLabel(new StringType(eventOutput.getFirmaDigitaleButtonLabel()));
				if(eventOutput.getCopernicoButtonLabel() != null)
					pdf.getPdfData().setCopernicoButtonLabel(new StringType(eventOutput.getCopernicoButtonLabel()));
				return null;
			}
			return null;
		}catch(Throwable t){
			pdf.addCommandMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onPdfPreview)<br><br>"+t.toString());
			return null;
		}
	}


	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void callCompilationModeSelection(ClientSessionContext csc, PdfModel pdf, String selectedComilationMode){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return;
		
		CompilationModeSelectionEventInputData eventInput = new CompilationModeSelectionEventInputData(pdf, selectedComilationMode);
		CompilationModeSelectionEventOutputData eventOutput = null;
		try{
			eventOutput = pdfData.getPdfDriver().onCompilationModeSelection(csc, eventInput);
			if(eventOutput != null){
				if(pdf.getPdfData().getIdCarrello().isNull() && eventOutput.getErrorMessage() != null && eventOutput.getErrorMessage().length() > 0){
					pdf.addCommandError(eventOutput.getErrorMessage());
					return;
				}
				if(pdf.getPdfData().getIdCarrello().isNull() && eventOutput.getWarningMessage() != null && eventOutput.getWarningMessage().length() > 0){
					pdf.addCommandWarning(eventOutput.getWarningMessage());
					return;
				}
			}
			return;
		}catch(Throwable t){
			pdf.addCommandError(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onCompilationModeSelection)<br><br>"+t.toString());
			return;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callPdfCompleted(ClientSessionContext csc, PdfModel pdf){

		pdf.setPutOnSignedProcessBatchQueue(null);
		pdf.setPutOnPostCompletionProcessBatchQueue(null);

		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		FinalEventInputData eventInput = new FinalEventInputData(pdf);
		FinalEventOutputData eventOutput = null;
		try{
			eventOutput = pdfData.getPdfDriver().onPdfCompleted(csc, eventInput);
			if(eventOutput != null){
				pdf.setPutOnSignedProcessBatchQueue(eventOutput.getPutOnSignedProcessBatchQueue());
				pdf.setPutOnPostCompletionProcessBatchQueue(eventOutput.getPutOnPostCompletionProcessBatchQueue());
				return eventOutput.getErrorMessage();
			}
			return null;
		}catch(Throwable t){
			return EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onPdfCompleted)<br><br>"+t.toString();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static VerifyCopernicoPdfOutputData callVerifyCopernicoPdf(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		VerifyCopernicoPdfInputData eventInput = new VerifyCopernicoPdfInputData(pdf);
		VerifyCopernicoPdfOutputData eventOutput = null;
		try{
			return pdfData.getPdfDriver().onVerifyCopernicoPdf(csc, eventInput);
		}catch(Throwable t){
			eventOutput = new VerifyCopernicoPdfOutputData();
			eventOutput.addError(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onVerifyCopernicoPdf)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static SendToCliEventOutputData callSendToCli(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		SendToCliEventInputData eventInput = new SendToCliEventInputData(pdf);
		try{
			return pdfData.getPdfDriver().onSendToCli(csc, eventInput);
		}catch(Throwable t){
			SendToCliEventOutputData eventOutput = new SendToCliEventOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onSendToCli)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static FreezeEventOutputData callPdfFreeze(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		FreezeEventInputData eventInput = new FreezeEventInputData(pdf, pdfContent);
		try{
			return pdfData.getPdfDriver().onPdfFreeze(csc, eventInput);
		}catch(Throwable t){
			FreezeEventOutputData eventOutput = new FreezeEventOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onPdfFreeze)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static UnfreezeEventOutputData callPdfUnfreeze(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		UnfreezeEventInputData eventInput = new UnfreezeEventInputData(pdf);
		try{
			return pdfData.getPdfDriver().onPdfUnfreeze(csc, eventInput);
		}catch(Throwable t){
			UnfreezeEventOutputData eventOutput = new UnfreezeEventOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onPdfUnfreeze): "+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvidePritDataResponse callProvideMom2PritData(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws Exception{
			
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		ProvidePritDataRequest eventInput = new ProvidePritDataRequest(pdf, pdfData);
		try{
			return pdfData.getPdfDriver().providePritData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".providePritData on MOM2)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvidePritDataResponse callProvidePritData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		ProvidePritDataRequest eventInput = new ProvidePritDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().providePritData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".providePritData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideMifidDataResponse callProvideMifidData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideMifidDataRequest eventInput = new ProvideMifidDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideMifidData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideMifidData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideIddDataResponse callProvideIddData(ClientSessionContext csc, PdfModel pdf, boolean onload) throws Exception{
			
		PdfDataModel pdfData = onload ? pdf.getPdfData() : pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideIddDataRequest eventInput = new ProvideIddDataRequest(pdf, pdfData);
		try{
			return pdfData.getPdfDriver().provideIddData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideIddData)");
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideSostituzioniDataResponse callProvideSostituzioniData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideSostituzioniDataRequest eventInput = new ProvideSostituzioniDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideSostituzioniData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideSostituzioniData)");
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideReportAdeguatezzaDataResponse callProvideReportAdeguatezzaData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideReportAdeguatezzaDataRequest eventInput = new ProvideReportAdeguatezzaDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideReportAdeguatezzaData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideReportAdeguatezzaData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideAttachmentsDataResponse callProvideAttachmentsData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideAttachmentsDataRequest eventInput = new ProvideAttachmentsDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideAttachmentsData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideAttachmentsData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static VerifyAttachmentOutputData callVerifyAttachment(ClientSessionContext csc, PdfModel pdf, PdfAttachModel pdfAttach){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		VerifyAttachmentInputData eventInput = new VerifyAttachmentInputData(pdf, pdfAttach);
		try{
			return pdfData.getPdfDriver().onVerifyAttachment(csc, eventInput);
		}catch(Throwable t){
			VerifyAttachmentOutputData eventOutput = new VerifyAttachmentOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onVerifyAttachment)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideCompilationModesResponse callProvideCompilationModes(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideCompilationModesRequest eventInput = new ProvideCompilationModesRequest(pdf);
		try{
			ProvideCompilationModesResponse eventOutput = pdfData.getPdfDriver().provideCompilationModes(csc, eventInput);
			if(eventOutput != null && eventOutput.getCompilationModes() > 0 && pdf.isInBasket())
				throw new Exception("("+pdfData.getPdfDriver().getClass().getName()+".provideCompilationModes: non utilizzabile in un basket)");
			return eventOutput;
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideCompilationModes)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideAgevolazioneDipendentiDataResponse callProvideAgevolazioneDipendentiData(ClientSessionContext csc, PdfDataModel pdfData){
			
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideAgevolazioneDipendentiDataRequest eventInput = new ProvideAgevolazioneDipendentiDataRequest(pdfData);
		try{
			return pdfData.getPdfDriver().provideAgevolazioneDipendentiData(csc, eventInput);
		}catch(Throwable t){
			t.printStackTrace();
			return null;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideSrvDispositivaDataResponse callProvideSrvDispositivaData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		ProvideSrvDispositivaDataRequest eventInput = new ProvideSrvDispositivaDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideSrvDispositivaData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideSrvDispositivaData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideBasketDataResponse callProvideBasketData(ClientSessionContext csc, PdfModel pdf) throws Exception{
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideBasketDataRequest eventInput = new ProvideBasketDataRequest(pdf);
		try{
			return pdfData.getPdfDriver().provideBasketData(csc, eventInput);
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideBasketData)");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ProvideCrossFBCustomersDataResponse callProvideCrossFBCustomersData(ClientSessionContext csc, PdfDataModel pdfData) throws Exception{
			
		if(pdfData.getPdfDriver() == null)
			return null;
		
		ProvideCrossFBCustomersDataRequest eventInput = new ProvideCrossFBCustomersDataRequest(pdfData);
		try{
			ProvideCrossFBCustomersDataResponse res = pdfData.getPdfDriver().provideCrossFBCustomersData(csc, eventInput);
			if(res == null) {
				String crossFBCustomerIndexes = pdfData.readAsString("crossFBCustomerIndexes");
				if(!crossFBCustomerIndexes.isEmpty()) {
					res = new ProvideCrossFBCustomersDataResponse();
					res.setCrossFBCustomerIndexes(Arrays.asList(crossFBCustomerIndexes.split("\\,")));
				}
			}
			return res;
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideCrossFBCustomersData)");
		}
	}

	/***********************************************************************************************/
	/* Ad uso delle chiamate sul pdf corrente */
	/***********************************************************************************************/
	public static List<ElementoSquadra> callProvideSquadraData(ClientSessionContext csc, PdfModel pdf, String[] ruoli) throws Exception{
		return callProvideSquadraData(csc, pdf, pdf.getPdfData(), ruoli);
	}
	
	/***********************************************************************************************/
	/* Ad uso delle chiamate su tutti pdf */
	/***********************************************************************************************/
	public static List<ElementoSquadra> callProvideSquadraData(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, String[] ruoli) throws Exception{
		
		try{
			
			ProvideSquadraDataRequest eventInput = new ProvideSquadraDataRequest(pdf, pdfData);
			ProvideSquadraDataResponse output = null;
			if(pdfData.getPdfDriver() != null)
				output = pdfData.getPdfDriver().provideSquadraData(csc, eventInput);
			if(output == null || output.getSquadra().isEmpty()) { // La squadra è fatta almeno da ndgCliente1
				output = new ProvideSquadraDataResponse();
				ElementoSquadra elSq = new ElementoSquadra();
				elSq.setNomeCampoNdg("ndgCliente1");
				elSq.setNomeCampoIdCensimento("idCensimentoCliente1");
				elSq.setNomiCampoErrore(elSq.getNomeCampoNdg());
				elSq.setRuoli(ElementoSquadra.Ruoli.SOTTOSCRITTORE);
				output.getSquadra().add(elSq);
			}
			
			List<String> ruoliAsList = Arrays.asList(ruoli);
			List<ElementoSquadra> res = new ArrayList<ElementoSquadra>();
			List<ElementoSquadra> squadra = output.getSquadra();
			for(ElementoSquadra elSq : squadra) {
				StringType codCli = (StringType)pdfData.read(elSq.getNomeCampoNdg());
				if(codCli == null || codCli.isNull())
					codCli = (StringType)pdfData.read(elSq.getNomeCampoIdCensimento());
				if(codCli == null || codCli.isNull())
					continue;
				
				List<String> elSqRuoli = Arrays.asList(elSq.getRuoli().split("\\&"));
				for(String elSqRuolo : elSqRuoli) {
					if(ruoliAsList.contains(elSqRuolo)) {
						ElementoSquadra elSqCopy = (ElementoSquadra)Tools.cloneObject(elSq);
						elSqCopy.setRuoli(elSqRuolo);
						res.add(elSqCopy);				
					}
				}
			}
			return res;
		}catch(Throwable t){
			throw new Exception(t+sDRVMESSAGEPREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".provideSquadraData)");
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static MomEventOutputData callMomEvent(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		MomEventInputData eventInput = new MomEventInputData(pdf);
		try{
			return pdfData.getPdfDriver().onMomEvent(csc, eventInput);
		}catch(Throwable t){
			MomEventOutputData eventOutput = new MomEventOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onMomEvent)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ValidationEventOutputData callValidationEvent(ClientSessionContext csc, PdfModel pdf){
			
		PdfDataModel pdfData = pdf.mainPdfData();
		if(pdfData.getPdfDriver() == null || pdf.isTestMode())
			return null;
		
		ValidationEventInputData eventInput = new ValidationEventInputData(pdf);
		try{
			return pdfData.getPdfDriver().onValidationEvent(csc, eventInput);
		}catch(Exception t){
			ValidationEventOutputData eventOutput = new ValidationEventOutputData();
			eventOutput.setErrorMessage(EX_MESSAGE_PREFIX+"("+pdfData.getPdfDriver().getClass().getName()+".onValidationEvent)<br><br>"+t.toString());
			return eventOutput;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void manageEventResult(AbstractEventOutputData eventOutput, PdfModel pdf, boolean manageCompilationMode){
		
		if(eventOutput == null)
			return;
		
		PdfDataModel pdfData = pdf.getPdfData();
		
		if(manageCompilationMode)
			manageCompilationModes(pdf, pdfData, eventOutput.getInvalidCompilationModes());
		
		PdfDataModel pdfElement = null;
		if(pdf.isMultiPdf())
			pdfElement = (PdfDataModel)pdfData.getPdfs().get(pdfData.getPdfIndex().intValue());
		
		if(eventOutput.getFieldsToRemove().size() > 0 || eventOutput.isFieldsToRemoveManagedByDriver()){
			pdfData.setFieldsToRemove(arrayAsString(eventOutput.getFieldsToRemove()));
			if(pdfElement != null) 
				pdfElement.setFieldsToRemove(new StringType(pdfData.getFieldsToRemove().toString()));
		}
		if(eventOutput.getEditableFields().size() > 0 || eventOutput.isEditableFieldsManagedByDriver()){
			pdfData.setEditableFields(arrayAsString(eventOutput.getEditableFields()));
			if(pdfElement != null) 
				pdfElement.setEditableFields(new StringType(pdfData.getEditableFields().toString()));
		}
		if(eventOutput.getUneditableFields().size() > 0 || eventOutput.isUneditableFieldsManagedByDriver()){
			pdfData.setUneditableFields(arrayAsString(eventOutput.getUneditableFields()));
			if(pdfElement != null) 
				pdfElement.setUneditableFields(new StringType(pdfData.getUneditableFields().toString()));
		}
		if(eventOutput.getExtraMandatoryFields().size() > 0 || eventOutput.isExtraMandatoryFieldsManagedByDriver()){
			pdfData.setExtraMandatoryFields(arrayAsString(eventOutput.getExtraMandatoryFields()));
			if(pdfElement != null) 
				pdfElement.setExtraMandatoryFields(new StringType(pdfData.getExtraMandatoryFields().toString()));
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static StringType arrayAsString(ArrayList<String> array){
		ArrayList<String> managed = new ArrayList<String>(); 
		String arrayAsString = "";
		for(int i=0;i<array.size();i++){
			String f = array.get(i);
			if(managed.contains(f))
				continue;
			arrayAsString += f+",";
			managed.add(f);
		}
		if(arrayAsString.length() > 0)
			arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1);
		return new StringType(arrayAsString);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void manageCompilationModes(PdfModel pdf, PdfDataModel pdfData, int invalidCompilationModes){
		
		PdfAnagModel pdfMainAnag = pdf.mainPdfAnag();
		PdfDataModel pdfMainData = pdf.mainPdfData();
		
		String compilationModes = "";
		if(invalidCompilationModes >= 0){
			if(!pdfMainData.getInputCompilationModes().isNull()){
				String icm = pdfMainData.getInputCompilationModes().toString();
				if(icm.indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA) >= 0 && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_LIBERA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA+",";
				if(icm.indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA) >= 0 && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_CHIMICA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA+",";
				if(icm.indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) >= 0 && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.FIRMA_DIGITALE) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE+",";
				if(icm.indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) >= 0 && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.COPERNICO) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO+",";
				if(icm.indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA) >= 0 && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.STAMPA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA+",";
			}else{
				if(pdfMainAnag.getPdfIsCartaLiberaEnabled().booleanValue() && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_LIBERA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA+",";
				if(pdfMainAnag.getPdfIsCartaChimicaEnabled().booleanValue() && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_CHIMICA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA+",";
				if(pdfMainAnag.getPdfIsFirmaDigitaleEnabled().booleanValue() && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.FIRMA_DIGITALE) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE+",";
				if(pdfMainAnag.getPdfIsCopernicoEnabled().booleanValue() && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.COPERNICO) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO+",";
				if(pdfMainAnag.getPdfIsStampaEnabled().booleanValue() && (invalidCompilationModes & PdfBaseDriver.ModalitaDiSottoscrizione.STAMPA) == 0)
					compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA+",";
			}
			if(compilationModes.length() > 0)
				compilationModes = compilationModes.substring(0,compilationModes.length()-1);
			else
				compilationModes = PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_NESSUNA;
		}
		
		if(compilationModes.length() > 0){
			if(pdf.isMultiPdf()){
				PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(pdfData.getPdfIndex().intValue());			
				pdfElement.setCompilationModes(new StringType(compilationModes));
			}
			pdfData.setCompilationModes(new StringType(compilationModes));
		}

	}
}
