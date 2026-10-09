package prgm.pdfwebforms.sendprocess.business;

import java.util.ArrayList;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnCompilationModeSelection;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.reportadeguatezza.Costanti;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;
import prgm.pdfwebforms.sendprocess.backend.PdfSendProcessFacade;
import prgm.pdfwebforms.sendprocess.display.PdfEndSendProcess;
import prgm.pdfwebforms.sendprocess.display.PdfEndVolatileSendProcess;
import prgm.pdfwebforms.sendprocess.display.PdfErrorsOnSend;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartSendProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;			
        	pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();
        	
        	if(Basket.isFirstDispoPdf(pdf) && !pdf.isTestMode() &&
        	   (Basket.globalBasketPdfCompilationModes(pdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA)  < 0 && 
			    Basket.globalBasketPdfCompilationModes(pdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA) < 0)){
    			pdf.setInitialErrorMsg("Attenzione, la modalità di sottoscrizione selezionata non è abilitata, non è possibile procedere");
    			setNextCommandClass(PdfInitialError.class);
    			return pdf;
        	}

        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
    		if(pdf.globalPdfCompilationModes().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)){
    			if(!pdf.pdfIsInCartaChimica()){
        			pdf.addCommandError("Non risulta valorizzato il numero riportato sulla carta chimica");
        			setNextCommandClass(PdfErrorsOnSend.class);
        			return pdf;
    			}
    		}
    		
			if(!PdfBarcodeUtils.initBarcodesCartaceo(csc, pdf)){
				setNextCommandClass(PdfErrorsOnSend.class);
				return pdf;
			}
			
        	if(!pdf.getSkipCommandWarnings().booleanValue()){
				PdfDriverCaller.callCompilationModeSelection(csc, pdf, (pdf.pdfIsInCartaChimica() ? PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA : PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA));
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
			
			String[] erroriAdeguatezza = MifidCaller.callOnSend(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
			
			if(pdf.pdfIsInCartaChimica())
				pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA));
			else
				pdf.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA));

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
			
        	// Invio il pdf, solo se non siamo in un basket, dove viene fatto a monte
			if(!pdf.isInBasket()) {
				byte[] pdfContent = doSend(csc, pdf, true, false); // Se siamo qui il processo è quello "virtuoso"
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
	public static byte[] doSend(ClientSessionContext csc, PdfModel pdf, boolean doMultipleCopies, boolean isProcessoNonVirtuoso) throws Exception{
		
		// In modo da pilotare correttamente la valorizzazione dei dati nell'eventuale chiamata al servizio "dispositiva"
		pdf.setProcessoNonVirtuoso(isProcessoNonVirtuoso);
		
    	// Inizializzo i clienti coinvolti
		ArrayList<PdfPersonModel> fullSendProcessPersons = createFullSendProcessPersons(pdf);
		pdf.setFullProcessPersons(fullSendProcessPersons);
		
		clearSignFields(pdf);
		byte[] pdfContent = PdfEngine.compilePdfFields(csc, pdf, true, doMultipleCopies, "SEND");
		
		PdfSendProcessFacade facade = (PdfSendProcessFacade)FacadeLoader.getFacade(csc, PdfSendProcessFacade.class);
		facade.sendPdfInstance(csc, pdf, pdfContent);
		
		return pdfContent;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static ArrayList<PdfPersonModel> createFullSendProcessPersons(PdfModel pdf) throws Exception{
		
		ArrayList<PdfPersonModel> clienti = new ArrayList<PdfPersonModel>();
		if(pdf.isMultiPdf()){
			
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){ // for each pdf
				
				PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				
				for(PdfPersonModel pdfCli : pdfDataElement.getClienti()){ // for each person of pdf
					
					if(pdfCli.isEmty())
						continue;
					
					boolean found = false;
					for(int j=0;j<clienti.size();j++){ // for each person managed
						PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
						if(managedCli.isEqual(pdfCli)){
							found = true;
							break;
						}
					}
					if(!found){
						PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
						clienti.add(cli);
					}
					
				}
			}
		}else{
			
			for(PdfPersonModel pdfCli : pdf.getPdfData().getClienti()){
				
				if(pdfCli.isEmty())
					continue;
				
				PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
				clienti.add(cli);
			}
			
		}
		return clienti;
	}
		
	/*******************************************************************/
	/*******************************************************************/
	protected static ArrayList<String> clearSignFields(PdfModel pdf) throws Exception{
		PdfDataModel pdfData = pdf.getPdfData();
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		if(!pdf.isMultiPdf()){
			signFieldToRemove = clearSignFieldsFromSinglePdf(pdfData,"");
		}else{
			for(int i=0;i< pdfData.getPdfs().size();i++)
				signFieldToRemove.addAll(clearSignFieldsFromSinglePdf((PdfDataModel)pdfData.getPdfs().get(i),PdfEngine.multiPdfPrefix(i)));
		}
		return signFieldToRemove;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	protected static ArrayList<String> clearSignFieldsFromSinglePdf(PdfDataModel pdfData, String fieldNamePrefix) throws Exception{ 
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			BooleanType firma = (BooleanType)pdfData.readProperty(fi.htmlFieldName);
			if(firma != null){
				pdfData.addProperty(fi.htmlFieldName,new BooleanType());
				pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
			}
			signFieldToRemove.add(fieldNamePrefix+fi.pdfFieldName);
			signFieldToRemove.add(fieldNamePrefix+"nome"+fi.pdfFieldName);
		}
		return signFieldToRemove;
	}
	
}
