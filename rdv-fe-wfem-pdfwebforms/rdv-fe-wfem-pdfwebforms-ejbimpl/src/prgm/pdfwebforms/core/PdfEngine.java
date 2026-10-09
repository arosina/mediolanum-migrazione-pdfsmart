package prgm.pdfwebforms.core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.FieldFormatException;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.FieldPosition;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfEngine {

	private static final String S_SETFFLAGS = "setfflags";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String multiPdfPrefix(int pdfIndex){
		return "p"+pdfIndex+"#";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] removePdfFields(ClientSessionContext csc, byte[] pdfContent,
										 ArrayList<String> fieldNames, boolean flattening) throws Exception{
		
		InputStream pdf = new ByteArrayInputStream(pdfContent);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	    AcroFields form = stamp.getAcroFields();
	    for(String fieldName : fieldNames)
	    	form.removeField(fieldName);
		stamp.setFormFlattening(flattening);
	    stamp.close();
	    reader.close();
	    pdfOut.close();
	    pdf.close();
	    return pdfOut.toByteArray();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] prefixPdfFields(byte[] pdfContent, String prefix, boolean flattening) throws Exception{
		
		InputStream pdf = new ByteArrayInputStream(pdfContent);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	    AcroFields acroForm = stamp.getAcroFields();
	    ArrayList<String> fieldNames = new ArrayList<String>();
    	Iterator<String> fields = acroForm.getFields().keySet().iterator();
    	while(fields.hasNext())
    		fieldNames.add((String)fields.next());
    	for(String pdfFieldName : fieldNames)
	    	acroForm.renameField(pdfFieldName,prefix+pdfFieldName);
		stamp.setFormFlattening(flattening);
	    stamp.close();
	    reader.close();
	    pdfOut.close();
	    return pdfOut.toByteArray();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] compilePdfFields(ClientSessionContext csc, PdfModel pdfModel, boolean flattening, 
										  boolean doMultipleCopies, String caller) throws Exception{
		
		pdfModel.setPdfTitle(pdfModel.mainPdfAnag().getTitle());
		if(!pdfModel.getPdfData().getPdfTitle().isNull())
			pdfModel.setPdfTitle(pdfModel.getPdfData().getPdfTitle().toString());
		
		byte[] pdfResult = null;
		if(pdfModel.isMultiPdf())
			pdfResult = compileMultiPdfFields(csc, pdfModel, flattening, doMultipleCopies,caller);
		else
			pdfResult = compileSinglePdfFields(csc, pdfModel, flattening, doMultipleCopies,caller);
		if(pdfModel.pdfIsInCartaChimica())
			pdfResult = putNotPrintableString(pdfResult, flattening);

		if(pdfModel.getPdfAttachments() != null && pdfModel.getPdfAttachments().size() > 0){
			pdfResult = addPdfAttachments(pdfModel, pdfResult);
		}
		
		String title = pdfModel.getPdfData().getPdfTitle().isNull()?pdfModel.mainPdfAnag().getPdfCode()+" "+pdfModel.mainPdfAnag().getPdfDescr():pdfModel.getPdfData().getPdfTitle().toString();
		return putTitle(title, pdfResult, flattening);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] addPdfAttachments(PdfModel pdfModel, byte[] mainPdf) throws Exception{
		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(allPdfOut);
		copy.addDocument(new PdfReader(mainPdf));

		for(int i=0; i < pdfModel.getPdfAttachments().size(); i++){
			PdfAttachModel pdfAttach = (PdfAttachModel)pdfModel.getPdfAttachments().get(i);
			if(pdfAttach.getFile().isNull() && pdfAttach.getDriverAttachRef().getImplicitContent() == null)
				continue;
			if(pdfAttach.getDriverAttachRef().getImplicitContent() != null)
				copy.addDocument(new PdfReader(pdfAttach.getDriverAttachRef().getImplicitContent()));
			else
				copy.addDocument(new PdfReader(pdfAttach.getFile().getFileContent()));
		}
		
		copy.close();
		allPdfOut.close();
		return allPdfOut.toByteArray();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] putTitle(String title, byte[] pdfByteArray, boolean flattening){
	    try{
			InputStream pdf = new ByteArrayInputStream(pdfByteArray);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    Map<String, String> info = reader.getInfo();
	    	info.put("Title", title);
	    	stamp.setMoreInfo(info);
			stamp.setFormFlattening(flattening);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			return pdfOut.toByteArray();
	    }catch(Throwable t){
			return pdfByteArray;
	    }
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] putNotPrintableString(byte[] pdfByteArray, boolean flattening) throws Exception{

		InputStream pdf = new ByteArrayInputStream(pdfByteArray);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		
	    int numPages = reader.getNumberOfPages();
	    for(int i=1; i<=numPages; i++){
	    	PdfContentByte over = stamp.getOverContent(i);
	        Font f = new Font(FontFamily.HELVETICA, 16);
	        f.setColor(BaseColor.RED);
	        Phrase p = new Phrase("COPIA NON VALIDA PER L'INVIO IN SEDE", f);
	        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, reader.getBoxSize(i,"media").getHeight()-18, 0);	    	
	    }
	    
		stamp.setFormFlattening(flattening);
	    stamp.close();
	    reader.close();
	    pdfOut.close();
	    return pdfOut.toByteArray();
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] generateAsFacsimile(byte[] pdfByteArray) throws Exception{
		return generateAsFacsimile(pdfByteArray, "fac simile");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] generateAsFacsimile(byte[] pdfByteArray, String label) throws Exception{

		InputStream pdf = new ByteArrayInputStream(pdfByteArray);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		
	    int numPages = reader.getNumberOfPages();
	    for(int i=1; i<=numPages; i++){
	    	PdfContentByte over = stamp.getOverContent(i);
	        Font f = new Font(FontFamily.HELVETICA, 60);
	        f.setColor(BaseColor.RED);
	        Phrase p = new Phrase(label, f);
	        ColumnText.showTextAligned(over, Element.ALIGN_CENTER, p, 297, 400, 45);	    	
	    }
	    
		stamp.setFormFlattening(true);
	    stamp.close();
	    reader.close();
	    pdfOut.close();
	    return pdfOut.toByteArray();
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] compileMultiPdfFields(ClientSessionContext csc, PdfModel pdfModel, boolean flattening, 
												boolean doMultipleCopies, String caller) throws Exception{

		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(allPdfOut);
		
		PdfDataModel pdfData = (PdfDataModel)pdfModel.getPdfData();
		for(int i=0;i<pdfModel.getPdfData().getPdfs().size();i++){
			pdfData.initDataFromPdfs(i);
			byte[] singlePdf = compileSinglePdfFields(csc,pdfModel,flattening,doMultipleCopies,caller);
			singlePdf = PdfEngine.prefixPdfFields(singlePdf, multiPdfPrefix(i), flattening);
			copy.addDocument(new PdfReader(singlePdf));
		}
		
		copy.close();
		allPdfOut.close();
		return allPdfOut.toByteArray();
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static ArrayList<String> getFieldsToBeRemovedFromPdf(PdfModel pdfModel){
		ArrayList<String> fieldsToBeRemovedFromPdf = new ArrayList<String>(); 
		if(!pdfModel.getPdfData().getFieldsToRemove().isNull()){
			String[] fieldsToRemoveArray = pdfModel.getPdfData().getFieldsToRemove().toString().split("\\,");
			ArrayList<String> fieldsToRemove = new ArrayList<String>(Arrays.asList(fieldsToRemoveArray)); 
			fieldsToBeRemovedFromPdf.addAll(fieldsToRemove);				
		}
		if(!pdfModel.getPdfData().getHidedFields().isNull()){
			String[] hidedFieldsArray = pdfModel.getPdfData().getHidedFields().toString().split("\\,");
			ArrayList<String> hidedFields = new ArrayList<String>(Arrays.asList(hidedFieldsArray)); 
			fieldsToBeRemovedFromPdf.addAll(hidedFields);				
		}
		return fieldsToBeRemovedFromPdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] compileSinglePdfFields(ClientSessionContext csc, PdfModel pdfModel, boolean flattening, 
												 boolean doMultipleCopies, String caller) throws Exception{
		
		try{
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfWebForms");
			String daoAccess = pdfModel.isPdfOnWork() ? "loadPdfOnWork" : "loadPdf";
			byte[] pdfContent = ((ByteArrayType)dao.executeQueryAccess(daoAccess,pdfModel.getPdfData()).getSingleResult()).byteArrayValue();
			if(pdfContent == null)
				pdfContent = pdfModel.isPdfOnWork() ? PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdfModel.getPdfData().getPdfId()) : PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdfModel.getPdfData().getPdfId(), pdfModel.getPdfData().getPdfPublicationId()); 			
			
			ArrayList<String> fieldsToBeRemovedFromPdf = getFieldsToBeRemovedFromPdf(pdfModel);
			if(!fieldsToBeRemovedFromPdf.isEmpty())
				pdfContent = PdfEngine.removePdfFields(csc, pdfContent, fieldsToBeRemovedFromPdf, false);
			
			if(!doMultipleCopies)
				return compileOnePdfFields(csc,pdfModel,pdfContent,flattening,"",caller);
			
			PdfDataModel pdfData = pdfModel.getPdfData();
			
			ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
			PdfCopyFields copy = new PdfCopyFields(allPdfOut);
			int numCopie = pdfData.getPdfNumCopie().intValue();
			if(numCopie == 0)
				numCopie = 1;
			for(int i=1; i<=numCopie; i++){
				String testoCopia = (String)Tools.getPropertyValue(pdfData, "pdfTestoCopia"+i);
				copy.addDocument(new PdfReader(compileOnePdfFields(csc,pdfModel,pdfContent,flattening,testoCopia,caller)));
			}

			copy.close();
			allPdfOut.close();
			return allPdfOut.toByteArray();
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] compileOnePdfFields(ClientSessionContext csc, PdfModel pdfModel, 
											  byte[] pdfByteArray, boolean flattening, String copiaPer, String caller) throws Exception{
		
		PdfDataModel pdfData = pdfModel.getPdfData();
		PdfAnagModel pdfAnag = pdfModel.getPdfAnag();
		
		try{
						
			InputStream pdf = new ByteArrayInputStream(pdfByteArray);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	
		    AcroFields acroForm = stamp.getAcroFields();
	    	Iterator fields = acroForm.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		
	    		String pdfFieldName = (String)fields.next();
	    		
	    		int fieldType = acroForm.getFieldType(pdfFieldName);
	    		
	    		if(fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
	    			continue;
	    		
	    		acroForm.setFieldProperty(pdfFieldName, S_SETFFLAGS, PdfFormField.FF_READ_ONLY, null); 

	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.COPIA_PER)){
	    			if(caller.equals("LEGGIILCONTRATTO")) {
	    				acroForm.setField(pdfFieldName,"Modulo non valido ai fini di sottoscrizione");
	    				continue;
	    			}
	    			acroForm.setField(pdfFieldName,copiaPer);
	    			continue;
	    		}
	    		
	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.CODICE_MODULO)){
	    			acroForm.setField(pdfFieldName,pdfAnag.getPdfCode().toString());
	    			continue;
	    		}

	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.DESCRIZIONE_MODULO)){
	    			acroForm.setField(pdfFieldName,pdfAnag.getPdfDescr().toString());
	    			continue;
	    		}

	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.NUMERO_ISTANZA_MODULO)){
	    			acroForm.setField(pdfFieldName,pdfModel.getPdfData().getPdfInstanceId().toString());
	    			continue;
	    		}
	    		
	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA)){
	    			if(pdfModel.reportAdeguatezzaPassatoDalChiamate() || !caller.equals("PREVIEW"))
	    				acroForm.setField(pdfFieldName,pdfModel.getIdReportAdeguatezza().toString());
	    			continue;
	    		}
	    		
	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_QUESTIONARIO_IDD)){
    				acroForm.setField(pdfFieldName,pdfModel.getIdQuestionarioIdd().toString());
	    			continue;
	    		}
	    		
	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD)){
					if(pdfModel.getIddCallModel() != null && !pdfModel.getIddCallModel().getIdRaccomandazioneIdd().isNull())
	    				acroForm.setField(pdfFieldName,pdfModel.getIddCallModel().getIdRaccomandazioneIdd().toString());
					else
						acroForm.setField(pdfFieldName,pdfModel.getIdRaccomandazioneIdd());
	    			continue;
	    		}
	    		
	    		// Campi relativi alla vendita come Banca Mediolanum
	    		if(manageCampiVenditaComeBmed(pdfModel, acroForm, pdfFieldName))
	    			continue;

	    		PdfFieldInfos fi = pdfModel.getPdfData().getPdfInfos().findFieldInfoByPdfName(pdfFieldName);
	    		if(fi == null)
	    			continue;
	    		
	    		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE) ||
			       pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_IMAGE) ||
			       pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_VALUE)){
	    			if(caller.equals("LEGGIILCONTRATTO")) {
	    				acroForm.setFieldProperty(pdfFieldName, "bgcolor", BaseColor.WHITE, null);
	    				acroForm.setFieldProperty(pdfFieldName, S_SETFFLAGS,PdfFormField.FF_READ_ONLY, null);
	    				acroForm.setField(pdfFieldName,"Modulo non utilizzabile per la sottoscrizione");
	    				continue;
	    			}
	    			if(!pdfData.getPdfBarcode().isNull()){
		    			createBarcode(pdfModel,stamp,acroForm,pdfFieldName,pdfData.getPdfBarcode());
		    			continue;
		    		}else{
		    			StringType barcodeValue = (StringType)pdfData.readProperty(PdfPredefinedFields.BARCODE);
		    			if(barcodeValue != null && !barcodeValue.isNull()){
			    			createBarcode(pdfModel,stamp,acroForm,pdfFieldName,barcodeValue);
			    			continue;
		    			}
		    		}
	    		}
	    		
	    		AbstractType field = pdfData.readProperty(fi.htmlFieldName);
	    		if(field == null)
	    			continue;
	    		
	    		CodDescDataList dl = pdfData.getCodDescDataList(fi.htmlFieldName);
	    		if(dl != null && fieldType != AcroFields.FIELD_TYPE_CHECKBOX && fieldType != AcroFields.FIELD_TYPE_RADIOBUTTON){
	    			String dlVal = pdfData.getDescValue(fi.htmlFieldName);
	    			pdfData.addProperty(fi.htmlFieldName+"Descr",new StringType(dlVal));
	    			setAcroformField(acroForm,pdfFieldName,dlVal);
	    			continue;
	    		}
	    		
	    		dl = fi.codDescDataList;
	    		if(dl != null){
	    			String dlVal = "";
	    			CodDescData d = dl.getCodDesc(field.toString());
	    			if(d != null)
	    				dlVal = d.getDescr();
	    			pdfData.addProperty(fi.htmlFieldName+"Descr",new StringType(dlVal));
	    		}
	    		
	    		if(field instanceof BooleanType){
	    			acroForm.setField(pdfFieldName,((BooleanType)field).booleanValue()?fi.expValue:"");
	    		}else if(field instanceof DateType){
	    			DateType dt = (DateType)field;
	    			if(fi.maxLen == 8)
	    				acroForm.setField(pdfFieldName,dt.getGG()+dt.getMM()+dt.getAA());
	    			else
	    				acroForm.setField(pdfFieldName,dt.toString());
	    		}else if(field instanceof DoubleType){
	    			String currency = fi.currency;
	    			if(field.isNull())
	    				currency = "";
	    			if(fi.currencyPrepend)
	    				acroForm.setField(pdfFieldName,currency+((DoubleType)field).toScaledString(fi.doubleScale));
	    			else
	    				acroForm.setField(pdfFieldName,((DoubleType)field).toScaledString(fi.doubleScale)+currency);
	    		}else if(field instanceof IntegerType){
	    			String currency = fi.currency;
	    			if(field.isNull())
	    				currency = "";
	    			if(fi.currencyPrepend)
	    				acroForm.setField(currency+pdfFieldName,field.toString());
	    			else
	    				acroForm.setField(pdfFieldName,field.toString()+currency);
	    		}else{
	    			setAcroformField(acroForm,pdfFieldName,field.toString());
	    		}
	    	}
	    	
			stamp.setFormFlattening(flattening);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			pdf.close();
			
			pdfByteArray = pdfOut.toByteArray();
			return removePdfPages(pdfByteArray, pdfData);
			
		}catch(Exception daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	private static final String ETICHETTA_BANCA_MEDIOLANUM  = "Banca Mediolanum";
	private static final String ETICHETTA_BANCA  = "Banca";
	private static final String ETICHETTA_MEDIOLANUM  = "Mediolanum";
	/***********************************************************************************************/
	private static boolean manageCampiVenditaComeBmed(PdfModel pdf, AcroFields acroForm, String pdfFieldName) throws Exception {
		if(!pdf.isVenditaComeBmed())
			return false;
		if(pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_CONSULENTE_FINANZIARIO)) {
			acroForm.setField(pdfFieldName, ETICHETTA_BANCA_MEDIOLANUM);
			return true;
		}
		if(pdfFieldName.equalsIgnoreCase("nomeAgente")) {
			acroForm.setField(pdfFieldName, ETICHETTA_BANCA);
			return true;
		}
		if(pdfFieldName.equalsIgnoreCase("cognomeAgente")) {
			acroForm.setField(pdfFieldName, ETICHETTA_MEDIOLANUM);
			return true;
		}
		if(pdfFieldName.equalsIgnoreCase("cognomeNomeAgente")) {
			acroForm.setField(pdfFieldName, ETICHETTA_BANCA_MEDIOLANUM);
			return true;
		}
		if(pdfFieldName.equalsIgnoreCase("nomeCognomeAgente")) {
			acroForm.setField(pdfFieldName, ETICHETTA_BANCA_MEDIOLANUM);
			return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] compilePdfForDownload(byte[] pdfContent, PdfAnagModel pdfAnag) throws Exception{

		boolean doWork = false;
		int numCopie = pdfAnag.getPdfNumCopie().intValue();
		for(int i=1; i<=numCopie; i++){
			StringType testoCopia = (StringType)Tools.getPropertyValue(pdfAnag, "pdfTestoCopia"+i);
			if(testoCopia != null && !testoCopia.isNull()){
				doWork = true;
				break;
			}
		}
		if(!doWork)
			return enableAllFields(pdfContent);
		
		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(allPdfOut);
		for(int i=1; i<=numCopie; i++){
			
			StringType testoCopia = (StringType)Tools.getPropertyValue(pdfAnag, "pdfTestoCopia"+i);
			if(testoCopia == null)
				testoCopia = new StringType();
			
			InputStream pdf = new ByteArrayInputStream(pdfContent);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    AcroFields acroForm = stamp.getAcroFields();
		    
		    acroForm.renameField(PdfPredefinedFields.COPIA_PER,PdfPredefinedFields.COPIA_PER+i);
		    acroForm.setField(PdfPredefinedFields.COPIA_PER+i,testoCopia.toString());
		    
			stamp.setFormFlattening(false);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			pdf.close();
			
			copy.addDocument(new PdfReader(pdfOut.toByteArray()));
		}

		copy.close();
		allPdfOut.close();
		return enableAllFields(allPdfOut.toByteArray());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] enableAllFields(byte[] fileContent) throws DocumentException{
		try {
			InputStream pdf = new ByteArrayInputStream(fileContent);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	
		    AcroFields acroForm = stamp.getAcroFields();
	    	Iterator<String> fields = acroForm.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		
	    		String pdfFieldName = fields.next();
	    		int fieldType = acroForm.getFieldType(pdfFieldName);
	    		
	    		if( fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
	    			continue;
	    		
	    		boolean isCopiaPer = pdfFieldName.equals(PdfPredefinedFields.COPIA_PER) || pdfFieldName.matches(PdfPredefinedFields.COPIA_PER+"(\\d+)");
	    		if( pdfFieldName.equals(PdfPredefinedFields.BARCODE) || isCopiaPer){
	    			acroForm.setFieldProperty(pdfFieldName, S_SETFFLAGS, PdfFormField.FF_READ_ONLY, null); 
	    		}else{
	    			acroForm.setFieldProperty(pdfFieldName, "clrfflags", PdfFormField.FF_READ_ONLY, null);
	    		}
	    		if(!isCopiaPer)
	    			setAcroformField(acroForm,pdfFieldName, "");
	    	}
			stamp.setFormFlattening(false);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			pdf.close();
			return pdfOut.toByteArray();
		}catch(Exception e) {
			throw new DocumentException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfInfos inspectPdfInfos(ClientSessionContext csc, PdfDataModel pdfData, boolean pdfOnWork) throws Exception{
		
		try{
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfWebForms");
			String daoAccess = pdfOnWork ? "loadPdfOnWork" : "loadPdf";
			byte[] pdfContent = ((ByteArrayType)dao.executeQueryAccess(daoAccess,pdfData).getSingleResult()).byteArrayValue();
			if(pdfContent == null)
				pdfContent = pdfOnWork ? PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdfData.getPdfId()) : PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdfData.getPdfId(), pdfData.getPdfPublicationId()); 			
			try{
				PdfInfos pdfInfos = inspectPdfInfos(pdfContent);
				return pdfInfos;
			}catch(Throwable t){
				throw new Exception("Pdf is corrupted or protected");
			}
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfInfos inspectPdfInfos(byte[] pdf) throws Throwable{

		PdfReader reader = null;
	    ByteArrayOutputStream pdfOut = null;
	    PdfStamper stamp = null;
	    
		try{
			PdfInfos result = new PdfInfos();
		    reader = new PdfReader(pdf);
		    int numPages = reader.getNumberOfPages();
		    result.pageWidths = new float[numPages];
		    result.pageHeights = new float[numPages];
		    for(int i=0;i<numPages;i++){
		    	result.pageWidths[i] = reader.getBoxSize((i+1),"media").getWidth();
		    	result.pageHeights[i] = reader.getBoxSize((i+1),"media").getHeight();
		    }
		    pdfOut = new ByteArrayOutputStream();
		    stamp = new PdfStamper(reader, pdfOut);
		    AcroFields acroForm = stamp.getAcroFields();
	    	Iterator<String> fields = acroForm.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		String pdfFieldName = (String)fields.next();
	    		int fieldType = acroForm.getFieldType(pdfFieldName);
                if(fieldType == AcroFields.FIELD_TYPE_PUSHBUTTON)
                	result.actionInfos.addAll(getActionInfo(acroForm, pdfFieldName));
                else
                	result.fieldInfos.addAll(getFieldInfos(acroForm, pdfFieldName, fieldType));
	    	}
	    	
	    	Collections.sort(result.fieldInfos);
	    	
			// Imposto i campi duplicati
			ArrayList<PdfFieldInfos> mainFields = new ArrayList<PdfFieldInfos>();
			ArrayList<PdfFieldInfos> slaveFields = new ArrayList<PdfFieldInfos>();
			for(PdfFieldInfos fi : result.fieldInfos){
				if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON)
					continue;
				if(!mainFields.contains(fi))
					mainFields.add(fi);
				else
					slaveFields.add(fi);
			}
			if(slaveFields.size() > 0){
				for(PdfFieldInfos mainField : mainFields){
					for(PdfFieldInfos slaveField : slaveFields){
						if(mainField.equals(slaveField)){
							slaveField.mainField = mainField;
							mainField.hasClone = true;
						}
					}
				}
			}

			return result;
			
		}catch(Throwable t){
			t.printStackTrace();
			throw t;
		}finally{
			if(stamp != null) stamp.close();
		    if(pdfOut != null) pdfOut.close();
		    if(reader != null) reader.close();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static List<PdfActionInfos> getActionInfo(AcroFields acroForm, String pdfFieldName){
        ArrayList<PdfActionInfos> ret = new ArrayList<PdfActionInfos>();
        Item item = acroForm.getFieldItem(pdfFieldName);
        if (item == null)
            return ret;
        for(int i=0;i<item.size();i++){
            try{
            	
                PdfDictionary mergedDict = item.getMerged(i);
                
                PdfArray rect = mergedDict.getAsArray(PdfName.RECT);
                if (rect == null)
                    continue;
                
                PdfActionInfos ai = new PdfActionInfos();
                ai.pdfFieldName = pdfFieldName;
                ai.htmlFieldName = getHtmlFieldName(pdfFieldName);
                ai.page = item.getPage(i).intValue();
                Rectangle r = PdfReader.getNormalizedRectangle(rect);
                ai.top = r.getTop();
                ai.left = r.getLeft();
                ai.bottom = r.getBottom();
                ai.right = r.getRight();
                ai.height = r.getHeight();
                ai.width = r.getWidth();
                
           		PdfString ss = mergedDict.getAsString(PdfName.TU);
           		if(ss != null)
           			ai.helpText = ss.toString();
           		
                ret.add(ai);
            }catch(Exception e){}
        }
        return ret;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
    private static List<PdfFieldInfos> getFieldInfos(AcroFields acroForm, String pdfFieldName, int fieldType) {
    	
        ArrayList<PdfFieldInfos> ret = new ArrayList<PdfFieldInfos>();
        Item item = acroForm.getFieldItem(pdfFieldName);
        if (item == null)
            return ret;
        for(int i=0;i<item.size();i++){
            try{
            	
                PdfDictionary mergedDict = item.getMerged(i);
                
                PdfArray rect = mergedDict.getAsArray(PdfName.RECT);
                if (rect == null)
                    continue;
                
                PdfFieldInfos fi = new PdfFieldInfos();
                fi.fieldType = fieldType;
                fi.pdfFieldName = pdfFieldName;
                fi.htmlFieldName = getHtmlFieldName(pdfFieldName);
                fi.page = item.getPage(i).intValue();
               	fi.expValue = getExpValue(mergedDict);
                Rectangle r = PdfReader.getNormalizedRectangle(rect);
                fi.top = r.getTop();
                fi.left = r.getLeft();
                fi.bottom = r.getBottom();
                fi.right = r.getRight();
                fi.height = r.getHeight();
                fi.width = r.getWidth();
               	
           		PdfNumber nn = mergedDict.getAsNumber(PdfName.F);
           		if(nn != null){
           			int flags = nn.intValue();
           			fi.hidden = ((flags & PdfFormField.FLAGS_PRINT) != 0 && ((flags & PdfFormField.FLAGS_HIDDEN) != 0) || ((flags & PdfFormField.FLAGS_NOVIEW) != 0)) ? true : false;
           		}

           		PdfString ss = mergedDict.getAsString(PdfName.TU);
           		if(ss != null)
           			fi.helpText = ss.toString();
           		
           		nn = mergedDict.getAsNumber(PdfName.MAXLEN);
           		if(nn != null)
           			fi.maxLen = nn.intValue();

           		nn = mergedDict.getAsNumber(PdfName.Q);
           		if(nn != null){
           			if(nn.intValue() == PdfFormField.Q_CENTER)
           				fi.align = "center";
           			else if(nn.intValue() == PdfFormField.Q_RIGHT)
           				fi.align = "right";
           		}

           		PdfDictionary mk = mergedDict.getAsDict(PdfName.MK);
           		if(mk != null){
           			PdfArray bc = mk.getAsArray(PdfName.BC);
           			if(bc != null)
           				fi.border = true;
           		}
           		
           		PdfNumber ffo = mergedDict.getAsNumber(PdfName.FF);
           		if(ffo != null){
           			int ff = ffo.intValue();
           			fi.readonly =  (ff & PdfFormField.FF_READ_ONLY) != 0 ? true : false;
           			fi.monospaced = (ff & PdfFormField.FF_COMB) != 0 ? true : false;
           			fi.mandatory = (ff & PdfFormField.FF_REQUIRED) != 0 ? true : false;
           			fi.multiline = (ff & PdfFormField.FF_MULTILINE) != 0 ? true : false;
           		}               		
               	
               	initDataType(fi, mergedDict, fieldType);

                PdfString defValue = mergedDict.getAsString(PdfName.V);
           		if(defValue != null){
           			fi.defValue = defValue.toString();
           			if("DoubleType".equals(fi.dataType))
           				fi.defValue = fi.defValue.replace('.',',');
           		}
           		
               	if(fieldType == AcroFields.FIELD_TYPE_CHECKBOX || fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON){
	           		if(mk != null){
	               		PdfString ca = mk.getAsString(PdfName.CA);
	               		if(ca != null){ // ca-> 1=CHECK_O (not used), 8=CHECK_X, null || 4=CHECK_V 
	               			fi.checkType = PdfFieldInfos.CHECK_X; // default to X
	               			if("4".equals(ca.toString()))
	               				fi.checkType = PdfFieldInfos.CHECK_V;	
	               		}
	           		}
               	}
               	
               	if(fieldType == AcroFields.FIELD_TYPE_COMBO || fieldType == AcroFields.FIELD_TYPE_LIST){
               		try{
	               		CodDescDataList dl = new CodDescDataList();
	        		    String[] ops = acroForm.getListOptionExport(pdfFieldName);
	        		    String[] opsDesc = acroForm.getListOptionDisplay(pdfFieldName);
	               		for(int j=0;j<ops.length;j++){
	               			CodDescData d = new CodDescData();
	               			d.setCod(ops[j]);
	               			try{ d.setDescr(opsDesc[j]); }catch(Exception e){ d.setDescr(ops[j]); }
	               			dl.addCodDescData(d);
	               		}
	               		fi.codDescDataList = dl;
               		}catch(Exception e){}
               	}

                ret.add(fi);
            }catch(Exception e){}
        }
        return ret;
    }
	
	/***********************************************************************************************/
	private static String NOMI_CAMPO_CHARS = "a-zA-Z0-9";
	public static Pattern NOMI_CAMPO_PATTERN = Pattern.compile("["+NOMI_CAMPO_CHARS+"]*");
	/***********************************************************************************************/
    private static String getHtmlFieldName(String pdfFieldName){
    	String htmlFieldName = pdfFieldName;
        htmlFieldName = pdfFieldName.replaceAll("[^"+NOMI_CAMPO_CHARS+"]*","");
        if(htmlFieldName.length() > 0){
			char firstChar = pdfFieldName.charAt(0);
			if(firstChar >= '0' && firstChar <= '9')
				htmlFieldName = "field"+htmlFieldName;
        }
		return htmlFieldName;
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initPdfFieldInfosPropertyValues(PdfDataModel pdfData, boolean onInit) throws Exception{
		
    	for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
    		
    		if(fi.mainField != null)
				continue;

    		// Skippo i campi firma e il loro testo (rfc #255474 e poi rfc #262162)
    		if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE || fi.pdfFieldName.startsWith("testofirma"))
    			continue;
    		
    		if(fi.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.COPIA_PER) ||
    		   fi.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.CODICE_MODULO) ||
    		   fi.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.DESCRIZIONE_MODULO))
    			continue;
    		
    		String propValue = "";
    		AbstractType prop = pdfData.readProperty(fi.htmlFieldName);
    		if(prop != null){
    			propValue = prop.toString();
    		}else{
        		prop = pdfData.readProperty(fi.pdfFieldName);
        		if(prop != null)
        			propValue = prop.toString();
    		}
    		
    		if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
    			propValue = "false";
    		
    		boolean setDefValue = fi.fieldType == AcroFields.FIELD_TYPE_TEXT && fi.defValue.length() > 0;
   			if(setDefValue && ((onInit && propValue.length() == 0 && !pdfData.getPdfInitialInputDataArray().contains(fi.pdfFieldName)) || 
   							   (pdfData.isPublicationIdChanged() && fi.hidden)))
   				propValue = fi.defValue;   		
   			
    		List errs = null;
    		if(prop != null)
    			errs = prop.getTypeErrors();
   			prop = new StringType();
    		try{
   				prop = AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+fi.dataType),propValue);
    		}catch(FieldFormatException ffe){
    			ffe.printStackTrace();
    		}
    		prop.setTypeErrors(errs);
    		
    		pdfData.addProperty(fi.htmlFieldName,prop);
    		
    	}
    	pdfData.setPublicationIdChanged(false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initPdfSignFieldInfosPropertyValues(PdfDataModel pdfData) throws Exception{
		
    	for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
    		
    		if(fi.mainField != null)
				continue;

    		if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
    			continue;
    		
    		pdfData.addProperty(fi.htmlFieldName,new BooleanType());
    		AbstractType fname = pdfData.readProperty("nome"+fi.htmlFieldName);
    		if(fname != null)
    			pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
    	}
    	
	}
	
    
	/***********************************************************************************************/
	/***********************************************************************************************/
    private static String getExpValue(PdfDictionary mergedDict){
		PdfDictionary appearanceDict = mergedDict.getAsDict(PdfName.AP); 
		if(appearanceDict == null)
			return "";
    	PdfDictionary expValsDict = appearanceDict.getAsDict(PdfName.N); 
    	if(expValsDict == null)
    		return "";
    	
		Set<PdfName> expValKeys = expValsDict.getKeys();
		for(PdfName expVal : expValKeys){
			String ev = PdfName.decodeName(expVal.toString());
			if(!ev.equals("Off"))
				return ev;
		}
		return "";
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
    private static byte[] removePdfPages(byte[] pdfByteArray, PdfDataModel pdfData) throws Exception{
    	if(pdfData.getVisiblePages().isNull())
    		return pdfByteArray;
		InputStream pdf = new ByteArrayInputStream(pdfByteArray);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    reader.selectPages(pdfData.getVisiblePages().toString());
	    PdfStamper pdfStamper = new PdfStamper(reader, pdfOut);
	    pdfStamper.close();
	    reader.close();
	    pdfOut.close();
	    return pdfOut.toByteArray();
    }    

	/***********************************************************************************************/
	/***********************************************************************************************/
    private static void initDataType(PdfFieldInfos fi, PdfDictionary mergedDict, int fieldType){
    	
    	fi.dataType = "StringType";
    	
    	if(fieldType == AcroFields.FIELD_TYPE_CHECKBOX)
    		fi.dataType = "BooleanType";
    	else if(fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON)
    		;
    	else if(fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
    		fi.dataType = "BooleanType";
    	else{
    		
    		PdfDictionary localMergedDict = mergedDict.getAsDict(PdfName.PARENT);
        	if(localMergedDict == null)
        		localMergedDict = mergedDict;
        		
        	PdfDictionary dict = localMergedDict.getAsDict(PdfName.AA);
        	if(dict == null)
        		return;
        	
        	try{
        	PdfDictionary rangeDict = dict.getAsDict(PdfName.V);
        	if(rangeDict != null){
            	PdfString rangePdfType = rangeDict.getAsString(PdfName.JS);
            	if(rangePdfType != null){
            		String rangeType = rangePdfType.toString();
            		if(rangeType.startsWith("AFRange_Validate")){
    	        		int idx1 = rangeType.indexOf("(")+1;
    	        		String strRangeValues = rangeType.substring(idx1);
    	        		strRangeValues = strRangeValues.substring(0,strRangeValues.indexOf(")"));
    	        		String[] rangeValues = strRangeValues.split("\\,");
    	        		if("true".equals(rangeValues[0].trim()))
    	        			fi.minValue = new Double(rangeValues[1].trim());
    	        		if("true".equals(rangeValues[2].trim()))
    	        			fi.maxValue = new Double(rangeValues[3].trim());
            		}
            	}
        	}
        	}catch(Throwable t){}
        	
    		dict = dict.getAsDict(PdfName.F);
        	if(dict == null)
        		return;
        	
        	PdfString pdfType = dict.getAsString(PdfName.JS);
        	if(pdfType == null)
        		return;
        	
        	String type = pdfType.toString();
        	if(type.startsWith("AFSpecial_Format")){
        		try{
	        		int idx1 = type.indexOf("(")+1;
	        		String strFormatType = type.substring(idx1);
	        		strFormatType = strFormatType.substring(0,strFormatType.indexOf(")"));
	        		if("0".equals(strFormatType)){ // CAP format
	        			fi.maxLen = 5;
	        			fi.onlynum = true;
	        		}
        		}catch(Throwable t){}
        		
        	}else if(type.startsWith("AFDate_FormatEx")){
        		fi.dataType = "DateType";
        	}else if(type.startsWith("AFNumber_Format")){
        		fi.dataType = "DoubleType";
        		try{
	        		int idx1 = type.indexOf("(")+1;
	        		String strFormatValues = type.substring(idx1);
	        		strFormatValues = strFormatValues.substring(0,strFormatValues.indexOf(")"));
	        		String[] formatValues = strFormatValues.split("\\,");
	        		fi.doubleScale = Integer.parseInt(formatValues[0].trim());
	        		String separator = formatValues[1].trim();
	        		if(fi.doubleScale == 0 && (Integer.parseInt(separator) == 1 || Integer.parseInt(separator) == 3))
	        			fi.dataType = "IntegerType";
	        		try{
	        			String v = formatValues[4].replaceAll("\"","").replaceAll("\\\\u","").trim();
	        			int c = Integer.parseInt(v,16);
		        		fi.currency = ""+Character.toString((char)c);
		        		fi.currencyPrepend = Boolean.parseBoolean(formatValues[5].trim());
	        		}catch(Throwable t){}
        		}catch(Throwable t){}
        	}else if(type.startsWith("AFPercent_Format")){
        		fi.dataType = "DoubleType";
        		try{
	        		int idx1 = type.indexOf("(")+1;
	        		String strFormatValues = type.substring(idx1);
	        		strFormatValues = strFormatValues.substring(0,strFormatValues.indexOf(")"));
	        		String[] formatValues = strFormatValues.split("\\,");
	        		fi.doubleScale = Integer.parseInt(formatValues[0].trim());
	        		String separator = formatValues[1].trim();
	        		if(fi.doubleScale == 0 && (Integer.parseInt(separator) == 1 || Integer.parseInt(separator) == 3))
	        			fi.dataType = "IntegerType";
	        		try{
		        		fi.currency = "%";
		        		fi.currencyPrepend = false;
	        		}catch(Throwable t){}
        		}catch(Throwable t){}
        	}
    	}
    }
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void createBarcode(PdfModel pdf, PdfStamper stamp, AcroFields form, 
									  String pdfFieldName, StringType barcodeValue){
		
		try{
			String code = barcodeValue.toString();

			if(pdfFieldName.equals(PdfPredefinedFields.BARCODE_VALUE)){
				form.setFieldProperty(PdfPredefinedFields.BARCODE_VALUE, "bgcolor", BaseColor.WHITE, null);
				form.setFieldProperty(PdfPredefinedFields.BARCODE_VALUE, S_SETFFLAGS,PdfFormField.FF_READ_ONLY, null);
				if(code.length() > 0)
					form.setField(PdfPredefinedFields.BARCODE_VALUE,code);
				return;
			}
			
			if(code.equals(""))
				return;

			InputStream is = pdf.getClass().getResourceAsStream("/prgm/pdfwebforms/core/WhiteBarcodeBackground.png");
			java.awt.Image backAwtImage = ImageIO.read(is);

			Barcode128 barcode = new Barcode128();
		    barcode.setCode(code);
		    barcode.setBarHeight(50);
		    barcode.setX(1.5f);		
		    
		    List<FieldPosition> fps = form.getFieldPositions(pdfFieldName);
		    for(FieldPosition fp : fps){
		    	int   pageNum = fp.page;
			    float x = fp.position.getLeft();
			    float y = fp.position.getBottom();
			    float h = fp.position.getHeight();
			    float w = fp.position.getWidth();

			    PdfContentByte cb = stamp.getOverContent(pageNum);
		    	
				java.awt.Image backScaledImage = backAwtImage.getScaledInstance((int)w,(int)h,BufferedImage.SCALE_DEFAULT);
				backAwtImage = new BufferedImage(backScaledImage.getWidth(null),backScaledImage.getHeight(null),BufferedImage.TYPE_INT_RGB);
				Graphics g = backAwtImage.getGraphics();
				g.drawImage(backScaledImage, 0, 0, null);
				g.dispose();
		    	Image backImage = Image.getInstance(backAwtImage,null);
		    	backImage.scaleToFit(fp.position);
		    	backImage.setAbsolutePosition(x,y);
		        cb.addImage(backImage);
			    
			    Image barcodeImage = null;
			    if(pdfFieldName.equals(PdfPredefinedFields.BARCODE_IMAGE)){
			    	barcodeImage = barcode.createImageWithBarcode(cb,BaseColor.BLACK,BaseColor.WHITE);
			    }else{
			    	barcodeImage = barcode.createImageWithBarcode(cb,null,null);
			    }
			    float dx = x;
			    try{
			    	if(backImage.getWidth() > barcodeImage.getWidth()){
					    dx = x + ((backImage.getWidth()-barcodeImage.getWidth()) / 2);
			    	}
			    }catch(Throwable t){}
			    
		        barcodeImage.scaleToFit(fp.position);
		        barcodeImage.setAbsolutePosition(dx,y);
		        cb.addImage(barcodeImage);
		    }

			form.setFieldProperty(pdfFieldName,S_SETFFLAGS,PdfFormField.FF_READ_ONLY, null);
		    
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static byte[] extractPdfPages(byte[] pdfContent, List<Integer> pagesToExtract) throws PdfWebFormsException{     
		try {
			InputStream pdf = new ByteArrayInputStream(pdfContent);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
			PdfReader reader = new PdfReader(pdf);
			Document document = new Document(reader.getPageSizeWithRotation(1));
			PdfCopy writer = new PdfCopy(document, pdfOut);
			document.open();
	        for (int i=0;i<pagesToExtract.size();i++){
			    PdfImportedPage page = writer.getImportedPage(reader,pagesToExtract.get(i));
			    writer.addPage(page);
	        }
			document.close();
			writer.close();
			reader.close();
			return pdfOut.toByteArray();
		}catch(Exception e) {
			throw new PdfWebFormsException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] compileBasketPreviewPdf(ClientSessionContext csc, Basket basket, boolean doMultipleCopies, String caller) throws Exception{
		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(allPdfOut);

		for(BasketElement be : basket.getBasketElements()) {
			byte[] pdfOut = PdfEngine.compilePdfFields(csc, be.getDispoPdf(), true, doMultipleCopies, caller);
			copy.addDocument(new PdfReader(pdfOut));
		}
		
		copy.close();
		allPdfOut.close();		
		return PdfEngine.putTitle("Modulo", allPdfOut.toByteArray(), true);
	}	
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void setAcroformField(AcroFields acroForm, String pdfFieldName, String val) throws Exception{
		int fieldType = acroForm.getFieldType(pdfFieldName);
    	if(fieldType == AcroFields.FIELD_TYPE_COMBO) {
	    	String[] combovals = acroForm.getListOptionExport(pdfFieldName);
	    	String[] combotexts = acroForm.getListOptionDisplay(pdfFieldName);
	    	acroForm.setListOption(pdfFieldName, combovals, combotexts);
    	}
		acroForm.setField(pdfFieldName,val);
	}
}
