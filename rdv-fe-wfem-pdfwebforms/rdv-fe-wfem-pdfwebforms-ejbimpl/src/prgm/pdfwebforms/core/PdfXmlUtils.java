package prgm.pdfwebforms.core;

import java.io.ByteArrayInputStream;

import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.aml.model.CoraModel;
import prgm.pdfwebforms.idd.IddCallModel;
import prgm.pdfwebforms.mifid.MifidCallModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceDataListModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PritMomInfoModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCallModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCallModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfXmlUtils{

	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModel(PdfDataModel pdfData, PdfModel pdf, boolean onEndProcess) throws Exception{
		PdfInstanceDataListModel dataList = new PdfInstanceDataListModel();
		
		// Global data
		dataList.setPdfTitle(pdfData.getPdfTitle());
		if(pdfData.getHideSaveButton().booleanValue())
			dataList.setHideSaveButton(pdfData.getHideSaveButton());
		else
			dataList.setHideSaveButton(null);
		dataList.setInviaInSedeButtonLabel(pdfData.getInviaInSedeButtonLabel());
		dataList.setFirmaDigitaleButtonLabel(pdfData.getFirmaDigitaleButtonLabel());
		dataList.setCopernicoButtonLabel(pdfData.getCopernicoButtonLabel());
		dataList.setPdfCompilationMode(pdf.getPdfCompilationMode());
		
		if(onEndProcess){
			if(pdf.getIsMifidSkipped().booleanValue())
				dataList.setIsMifidSkipped(pdf.getIsMifidSkipped());
			if(pdf.getMifidCallModel() != null)
				dataList.setMifidCallData(MifidCallModel.modelForXmlFormat(pdf.getMifidCallModel()));
			if(pdf.getMifidManlevaData() != null)
				dataList.setMifidManlevaData(pdf.getMifidManlevaData());
			if(pdf.getIddCallModel() != null && pdf.getIddCallModel().getInput() != null){
				IddCallModel iddCallData = (IddCallModel)Tools.cloneObject(pdf.getIddCallModel());
				dataList.setIddCallData(iddCallData);
			}
			if(pdf.getSostituzioniCallModel() != null && pdf.getSostituzioniCallModel().getInput() != null){
				SostituzioniCallModel sostituzioniCallData = (SostituzioniCallModel)Tools.cloneObject(pdf.getSostituzioniCallModel());
				dataList.setSostituzioniCallData(sostituzioniCallData);
			}
			if(pdf.getReportAdeguatezzaCallModel() != null){
				ReportAdeguatezzaCallModel repAde = (ReportAdeguatezzaCallModel)Tools.cloneObject(pdf.getReportAdeguatezzaCallModel());
				dataList.setReportAdeguatezzaCallData(repAde);
			}
			if(pdf.getPritMomInfoModel() != null){
				PritMomInfoModel pim = (PritMomInfoModel)Tools.cloneObject(pdf.getPritMomInfoModel());
				dataList.setPritMomInfo(pim);
			}
			dataList.setPdfInstanceAttachments(pdf.getPdfInstanceAttachments());
			dataList.setPdfInstanceOriginalAttachments(pdf.getPdfInstanceOriginalAttachments());
			dataList.setTipoProcessoSedeMOM(pdf.getTipoProcessoSede());
			
			// Il cora lo aggiungiamo solo per le dispo del basket che sono presenti nella lista del modello 
			// In caso di basket il modello CoraModel viene impostato in tutte le dispo a prescindere. 
			// In caso di accettazione copernico viene impostato negli xml delle specifiche dispo
			CoraModel cm = pdf.getCoraModel();
			if(cm != null && (cm.getDispoHasCora() != null || pdf == cm.findPdf(pdf))){
				cm = (CoraModel)Tools.cloneObject(cm);
				cm.setDispoHasCora(new StringType("S"));
				dataList.setCoraModel(cm);
			}
			
		}
		
		if(pdfData.getPdfs().size() > 0){
			for(int i=0;i<pdfData.getPdfs().size();i++){
				
				PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
				PdfAnagModel pdfAnag = (PdfAnagModel)pdf.getPdfAnags().get(i);
				pdfDataElement.initPdfInitialInputDataStringFromArray();
				
				PdfDataModel dataListElement = (PdfDataModel)Tools.cloneObject(pdfDataElement);
				insertPersistentData(dataListElement, pdfAnag);
				clearNonPersistentData(dataListElement, onEndProcess);
				dataList.getPdfData().add(dataListElement);
			}
		}else{
			PdfAnagModel pdfAnag = (PdfAnagModel)pdf.getPdfAnags().get(0);
			pdfData.initPdfInitialInputDataStringFromArray();
			
			PdfDataModel dataListElement = (PdfDataModel)Tools.cloneObject(pdfData);
			insertPersistentData(dataListElement, pdfAnag);
			clearNonPersistentData(dataListElement, onEndProcess);
			dataList.getPdfData().add(dataListElement);
		}
		
		return Tools.xmlFromModel(dataList);
	}

	/********************************************************************************/
	/********************************************************************************/
	private static void insertPersistentData(PdfDataModel pdfData, PdfAnagModel pdfAnag){
		// In questo metodo inseriamo gli eventuali dati aggiuntivi sul singolo pdfData
//		pdfData.addProperty("codProdottoPrit", pdfAnag.getPdfCodProdottoPrit());
//		pdfData.addProperty("codOperazionePrit", pdfAnag.getPdfCodOperazionePrit());
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void clearNonPersistentData(PdfDataModel pdfData, boolean onEndProcess){
		pdfData.setGobackLabel(null);
		pdfData.setGobackUrl(null);
		pdfData.setGobackUrlOnTop(null);
		pdfData.setGobackEndLabel(null);
		pdfData.setGobackEndUrl(null);
		pdfData.setGobackEndUrlOnTop(null);
		pdfData.setHideSaveButton(null);
		pdfData.setInviaInSedeButtonLabel(null);
		pdfData.setFirmaDigitaleButtonLabel(null);
		pdfData.setCopernicoButtonLabel(null);
		pdfData.setSkipDataentry(null);
		pdfData.setIsVolatile(null);
		pdfData.setAgente(null);
		pdfData.setAgenteImpersonato(null);
		pdfData.setPdfEnvironment(null); 
		pdfData.setPdfInstanceId(null);
		pdfData.setIsOnSameStack(null);
		pdfData.setPdfTitle(null);
		pdfData.setExternalEntityAppl(null);
		pdfData.setExternalEntityName(null);
		pdfData.setExternalEntityKey(null);
		pdfData.setSignAll(null);
		pdfData.setFacSimileOnPreview(null);
		pdfData.setDoMultipleCopiesOnPrintPdf(null);
		pdfData.setFacSimileLabelOnPrintPdf(null);
		pdfData.setSistemaClient(null);
		pdfData.setReplaceExternalPdfInstance(null);
		pdfData.setCallerSelectedCompilationMode(null);
		pdfData.setIdAdeguatezzaMifidPadre(null);
		pdfData.setFlagManlevaMifidKOESG(null);
		pdfData.setReadonly(null);
		pdfData.setReadonlyDataentry(null);
		pdfData.setProcessDriverReference(null);
		pdfData.setIndiceLegaleRappresentante(null);
		pdfData.setCodiceLegaleRappresentante(null);
		
		if(pdfData.getPdfInfos() != null){
	    	for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
	    		if(fi.mainField != null)
					continue;
	
	    		if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
	    			continue;
	    		
				if(onEndProcess){
					pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
				}else{
		    		pdfData.addProperty(fi.htmlFieldName,null);
					pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
				}
	    	}
			
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static PdfDataModel modelFromXml(PdfDataModel pdfDataKey, String xml) throws Exception{
		PdfInstanceDataListModel dataList = (PdfInstanceDataListModel)Tools.modelFromXml(new ByteArrayInputStream(xml.getBytes()));
		return modelFromXml(pdfDataKey, dataList, xml);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static PdfDataModel modelFromXml(PdfDataModel pdfDataKey, PdfInstanceDataListModel dataList, String xml) throws Exception{
		if(dataList.getPdfData().size() == 0)
			throw new Exception("No pdf instance data found. XML data on db may be corrupted.");
		
		PdfDataModel res = (PdfDataModel)Tools.cloneObject(dataList.getPdfData().get(0));
		res.initPdfInitialInputDataArrayFromString();

		// Global data
		res.setPdfTitle(dataList.getPdfTitle());
		res.setHideSaveButton(dataList.getHideSaveButton());
		res.setInviaInSedeButtonLabel(dataList.getInviaInSedeButtonLabel());
		res.setFirmaDigitaleButtonLabel(dataList.getFirmaDigitaleButtonLabel());
		res.setCopernicoButtonLabel(dataList.getCopernicoButtonLabel());
		
		if(dataList.getPdfData().size() > 1){
			for(int i=0;i<dataList.getPdfData().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)dataList.getPdfData().get(i);
				pdfDataElement.initPdfInitialInputDataArrayFromString();
				res.getPdfs().add(pdfDataElement);
			}
		}
		
		// Imposto i dati globali e volatili a partire da quelli in input
		if(pdfDataKey != null){
			res.setGobackLabel(pdfDataKey.getGobackLabel());
			res.setGobackUrl(pdfDataKey.getGobackUrl());
			res.setGobackUrlOnTop(pdfDataKey.getGobackUrlOnTop());
			res.setGobackEndLabel(pdfDataKey.getGobackEndLabel());
			res.setGobackEndUrl(pdfDataKey.getGobackEndUrl());
			res.setGobackEndUrlOnTop(pdfDataKey.getGobackEndUrlOnTop());
			res.setIsOnSameStack(pdfDataKey.getIsOnSameStack());
			res.setCallerSelectedCompilationMode(pdfDataKey.getCallerSelectedCompilationMode());
			res.setIdAdeguatezzaMifidPadre(pdfDataKey.getIdAdeguatezzaMifidPadre());
			res.setFlagManlevaMifidKOESG(pdfDataKey.getFlagManlevaMifidKOESG());
			res.setReadonlyDataentry(pdfDataKey.getReadonlyDataentry());
		}
		return res;
	}

}
