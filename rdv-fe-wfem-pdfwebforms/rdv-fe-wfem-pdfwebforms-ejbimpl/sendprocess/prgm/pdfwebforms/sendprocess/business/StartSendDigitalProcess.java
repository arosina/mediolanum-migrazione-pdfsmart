package prgm.pdfwebforms.sendprocess.business;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnCompilationModeSelection;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.reportadeguatezza.Costanti;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;
import prgm.pdfwebforms.sendprocess.display.PdfEndSendProcess;
import prgm.pdfwebforms.sendprocess.display.PdfEndVolatileSendProcess;
import prgm.pdfwebforms.sendprocess.display.PdfErrorsOnSend;
import prgm.pdfwebforms.signprocess.backend.PdfSignProcessFacade;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartSendDigitalProcess extends StartSendProcess {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;
        	pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();

        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	if(!pdf.getPdfData().getIsVolatile().booleanValue()){
	    		if(pdf.mainPdfAnag().getPdfCodProdottoPrit().isNull() || pdf.mainPdfAnag().getPdfCodOperazionePrit().isNull()){
	    			pdf.addCommandError("Il modulo non risulta disponibile per l'invio in formato digitale in quanto non risultano configurati i codici per la pratica di sede");
	    			setNextCommandClass(PdfErrorsOnSend.class);
	    			return pdf;
	    		}
        	}
        	
			if(!PdfBarcodeUtils.initBarcodeFirmaDigitale(csc, pdf)){
				setNextCommandClass(PdfErrorsOnSend.class);
				return pdf;
			}
			
        	if(!pdf.getSkipCommandWarnings().booleanValue()){
				PdfDriverCaller.callCompilationModeSelection(csc, pdf, PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE);
				if(pdf.hasCommandErrors()){
	    			setNextCommandClass(PdfErrorsOnCompilationModeSelection.class);
	    			return pdf;
				}
				if(pdf.hasCommandWarnings()){
					pdf.setSkipWarningCommandName(this.getClass().getName());
	    			setNextCommandClass(PdfErrorsOnCompilationModeSelection.class);
	    			return pdf;
				}
	    	}
	    	pdf.setSkipCommandWarnings(new BooleanType(false));
			
			String[] erroriAdeguatezza = MifidCaller.callOnSign(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
			
			pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE));

    		// Richiamo la generazione del report adeguatezza, se non già fatto o passato in input
			String erroriReportAdeguatezza = ReportAdeguatezzaCaller.callGeneraReportAdeguatezzaOnSend(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(erroriReportAdeguatezza);
				setNextCommandClass(PdfErrorsOnSend.class);
				return pdf;
			}      	
    		
			// Richiamo in polling il recupero del report adeguatezza
			if(pdf.getIdReportAdeguatezza().length() > 0 && !pdf.reportAdeguatezzaPassatoDalChiamate()){
				boolean reportOk = false;
				for(int i=0; i<pdf.getMaxRecuperaReportAdeguatezzaRetryCount(); i++){
					erroriReportAdeguatezza = ReportAdeguatezzaCaller.callRecuperaReportAdeguatezza(csc, pdf);
					if(erroriReportAdeguatezza != null){
						pdf.addCommandError(Costanti.MESSAGGIO_ERRORE);
						setNextCommandClass(PdfErrorsOnSend.class);
						return pdf;
					}else if(pdf.getRecuperaReportAdeguatezzaCallModel() != null){
						if(pdf.getStatoRecueroReportAdeguatezza().equals("END")){
							reportOk = true;
							break;
						}
					}
					Thread.sleep(1000);
				}
				if(!reportOk){
					pdf.addCommandError("La generazione del report di adeguatezza non è stata effettuata nei tempi prestabiliti. Non è possibile proseguire.");
					setNextCommandClass(PdfErrorsOnSend.class);
					return pdf;
				}
			}

			if(pdf.hasDataSottoscrizioneOggi())
				pdf.saveAndSetDataSottoscrizione();
			
			// Chiamo il completamento sul driver del primo pdf
			String errorMsg = PdfDriverCaller.callPdfCompleted(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnSend.class);
    			return pdf;
			}
			
			byte[] pdfContent = doSendDigital(csc, pdf);
			
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfErrorsOnSend.class);
				return pdf;
			}
    		
			if(pdf.getCallbackData() != null){
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingContent", new ByteArrayType(pdfContent));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingCompilationMode", new StringType(pdf.getPdfCompilationMode().toString()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingBarcodes", new StringType(pdf.getPdfGeneratedBarcodes().toString()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingFlagWayout", new BooleanType(pdf.getFlagWayout().booleanValue()));
				Tools.setPropertyValue(pdf.getCallbackData(),"pdfWebFormsResultingNoteFBCopernico", new StringType(""));
			}
			
			if(pdf.getPdfData().getIsVolatile().booleanValue()){
				setNextCommandClass(PdfEndVolatileSendProcess.class);
			}else{
				setNextCommandClass(PdfEndSendProcess.class);
			}
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] doSendDigital(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
    	// Inizializzo i clienti coinvolti
		ArrayList<PdfPersonModel> fullSendProcessPersons = createFullSendProcessPersons(pdf);
		pdf.setFullProcessPersons(fullSendProcessPersons);
		
		clearSignFields(pdf);
		byte[] pdfContent = PdfEngine.compilePdfFields(csc, pdf, true, false, "SEND");
		
		PdfSignProcessFacade facade = (PdfSignProcessFacade)FacadeLoader.getFacade(csc, PdfSignProcessFacade.class);
		pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE));
		pdf = facade.signPdfInstance(csc, pdf, pdfContent);
		
		return pdfContent;
	}
	
}
