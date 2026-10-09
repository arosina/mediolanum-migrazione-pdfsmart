package prgm.pdfwebforms.copernicoprocess.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.pdf.PdfTools;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCallData;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CopernicoUtility {
	
	private static String DAO_XML = "PdfWebForms.PdfSignProcess";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private CopernicoUtility() {}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadDatiCopernicoPersone(ClientSessionContext csc, PdfModel pdf){
		
		if(pdf.isTestMode())
			return;
		
		ArrayList<String> allEvidence = new ArrayList<String>();
		
		int count = 0;
		for(PdfPersonModel persona : pdf.getFullProcessPersons()){
			persona.resetCommandErrors();
			
			if(persona.isAgente())
				continue;
				
			if(!persona.getIsEffettivo().booleanValue()){
				
				pdf.addCommandError(""+persona.readCognomeNome()+" e' un cliente prospect.");
				
			}else{
				
				if(pdf.isProdottoCopernicoSmart()){
					allEvidence.addAll(CopernicoSmartUtility.loadDatiFirmaDigitalePersonaPerInvioCopernico(csc, persona, count));
				}else{
					String error = loadDatiCopernicoPersona(csc, persona);
					if(error != null)
						pdf.addCommandError(error);
				}
				
			}
			count++;
		}
		
		if(allEvidence.size() > 0){
			ArrayList<String> errors = new ArrayList<String>();
			ArrayList<String> warnings = new ArrayList<String>();
			for(String evid : allEvidence){
				if(evid.startsWith(MifidCaller.WARNING_INDICATOR))
					warnings.add(evid.substring(MifidCaller.WARNING_INDICATOR.length()));
				else
					errors.add(evid);
			}
			if(errors.size() > 0){
				for(String err : errors)
					pdf.addCommandError(err);
			}else{
				for(String war : warnings)
					pdf.addCommandWarning(war);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String loadDatiCopernicoPersona(ClientSessionContext csc, PdfPersonModel persona){

		String error = null;
		
		try{
			
			// Banca diretta
			if(persona.getIsEffettivo().booleanValue()){
				
				error = hasSoggettoContiBancaDiretta(csc, persona.getNdg());
				if(error != null)
					return error;
				
				error = verificaCodici(csc, persona.getNdg(), persona.readCognomeNome());
				if(error != null)
					return error;
				
			}
			
			return error;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return "Errore DAO nel recuperare le informazioni relative a copernico per "+persona.readCognomeNome()+": "+daoe.toString();
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public static byte[] firmaPdf(ClientSessionContext csc, PdfModel pdf){
		
		try{

			// Put customer name on selected sign
			ArrayList<String> fieldsToRemove = putPersonNameOnSigns(pdf);

			// Create pdf
			byte[] pdfContent = PdfEngine.compilePdfFields(csc, pdf, false, false, "COPERNICO");

			// Get sign list to remove from pdf
			fieldsToRemove.addAll(signFieldsToRemove(pdf));
			if(!fieldsToRemove.isEmpty())
				pdfContent = PdfEngine.removePdfFields(csc, pdfContent, fieldsToRemove, false);
			
			String title = pdf.getPdfData().getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfCode()+" "+pdf.mainPdfAnag().getPdfDescr():pdf.getPdfData().getPdfTitle().toString();
			pdfContent = PdfTools.rendiPdfAccessibile(csc, pdfContent, title, pdf.getPdfData().getPdfInstanceId().toString());
			return pdfContent;
			
		}catch(Exception e){
			e.printStackTrace();
			pdf.addCommandError("Errore nel firmare il modulo: "+e.toString());
			return null;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> putPersonNameOnSigns(PdfModel pdf){
		PdfDataModel pdfData = pdf.getPdfData();
		ArrayList<String> personNameFieldToRemove = new ArrayList<String>();
		if(!pdf.isMultiPdf()){
			personNameFieldToRemove = putPersonNameOnSignsForSinglePdf(pdfData);
		}else{
			for(int i=0;i< pdfData.getPdfs().size();i++)
				personNameFieldToRemove.addAll(putPersonNameOnSignsForSinglePdf((PdfDataModel)pdfData.getPdfs().get(i)));
		}
		return personNameFieldToRemove;
	}

	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> putPersonNameOnSignsForSinglePdf(PdfDataModel pdfData){
		
		ArrayList<String> personNameFieldToRemove = new ArrayList<String>();
		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove));
		
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			
			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName)){
				personNameFieldToRemove.add("nome"+fi.pdfFieldName);
				continue;
			}

			AbstractType nomefirma = pdfData.readProperty("nome"+fi.htmlFieldName);
			if(nomefirma == null)
				continue;

			Matcher mat = PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(fi.htmlFieldName);
			if(mat.matches()){
				pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
			}else{
				mat = PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
				if(mat.matches())
					pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
			}
		}
		return personNameFieldToRemove;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> signFieldsToRemove(PdfModel pdf) throws Exception{
		PdfDataModel pdfData = pdf.getPdfData();
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		if(!pdf.isMultiPdf()){
			signFieldToRemove = signFieldsToRemoveFromSinglePdf(pdfData,"");
		}else{
			for(int i=0;i< pdfData.getPdfs().size();i++)
				signFieldToRemove.addAll(signFieldsToRemoveFromSinglePdf((PdfDataModel)pdfData.getPdfs().get(i),PdfEngine.multiPdfPrefix(i)));
		}
		return signFieldToRemove;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> signFieldsToRemoveFromSinglePdf(PdfDataModel pdfData, String fieldNamePrefix) throws Exception{ 
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			BooleanType firma = (BooleanType)pdfData.readProperty(fi.htmlFieldName);
			if(firma != null)
				pdfData.addProperty(fi.htmlFieldName,new BooleanType());
			signFieldToRemove.add(fieldNamePrefix+fi.pdfFieldName);
		}
		return signFieldToRemove;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static String hasSoggettoContiBancaDiretta(ClientSessionContext csc, StringType ndg) throws DAOException{
		
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("ndg", new StringType(ndg.toString()));	
		BooleanType hasConti = (BooleanType)new DAOObject(csc, DAO_XML).executeQueryAccess("hasSoggettoContiBancaDiretta",input).getSingleResult();
		if(hasConti == null || !hasConti.booleanValue())
			return "Il sevizio di Banca Diretta non è attivo per questo cliente, non è possibile inviare la proposta con Copernico";
		return null;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static String verificaCodici(ClientSessionContext csc, StringType ndg, String cognomeNome) throws DAOException{
		
		try{
			
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("ndg", new StringType(ndg.toString()));				
			DAOQASResultModel qasRes = new DAOObject(csc, DAO_XML).executeQASAccess("getUserProfile",input);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK)
				return "Errore di sistema nel recuperare lo UserProfile: "+qasRes.getQasCallData().getMessage();
			
			MapCommandDataModel srvRes = (MapCommandDataModel)qasRes.getResult();
			String blocked1 = srvRes.readProperty("blocked1") == null ? "" : srvRes.readProperty("blocked1").toString();
			String errNo = srvRes.readProperty("errNo") == null ? "" : srvRes.readProperty("errNo").toString();
			
			if("0".equals(errNo) && "0".equals(blocked1))
				return null;	// Tutto ok
			
			if("15".equals(errNo) && "1".equals(blocked1))
				return "Il sevizio di Banca Diretta non è attivo per questo cliente, non è possibile inviare la proposta con Copernico";
			else
				return "Il servizio Copernico non può essere utilizzato da questo cliente in quanto il suo primo codice segreto è in stato di blocco";
			
		}catch(DAOException daoe){
			return "Errore DAO nel verificare i codici di "+cognomeNome+" per banca diretta: "+daoe.toString();
		}
	}

}
