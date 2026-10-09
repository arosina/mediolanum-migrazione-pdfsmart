package prgm.pdfwebforms.publisher.crafter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;
import prgm.pdfwebforms.publisher.model.PdfModuloModel;
import prgm.pdfwebforms.publisher.model.PdfRuoloUtilizzatoreModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class CrafterPdfUtils{
	
	private static String sCLIENTIPROMOTORI = "CLIENTIPROMOTORI";
	private static String sCatalogoOperazioni =  "CatalogoOperazioni";

	/***********************************************************************************************/
	/***********************************************************************************************/
	private CrafterPdfUtils() {	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType loadAreeCrafter(ClientSessionContext csc, PdfConfigurationModel confModel) throws Exception{
		confModel.setAreeCrafter(new ArrayList<String>());
		StringType areeCrafterFromDB = PdfConfig.getParamAsString(csc, "PUBLISHER", "AREE_CRAFTER");
		if(areeCrafterFromDB == null || areeCrafterFromDB.isNull())
			areeCrafterFromDB = new StringType("CATALOGO_MODULI,CATALOGO_OPERAZIONI");
		ArrayList<String> areeCrafterAsArray = new ArrayList<String>((Arrays.asList(areeCrafterFromDB.toString().split("\\,"))));
		confModel.setAreeCrafter(areeCrafterAsArray);
		return areeCrafterFromDB;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String moveToCrafter(ClientSessionContext csc, PdfConfigurationModel confModel){
		return moveToCrafter(csc, confModel, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String moveToCrafter(ClientSessionContext csc, PdfConfigurationModel confModel, boolean checkAree){
		try{
			
			String result = "";
			
			if(checkAree && !confModel.isAreaCrafter())
				return result;
			
			PdfAnagModel pdfAnag = confModel.getPdfAnag();			
			CrafterPdfAnagModel crafterPdfAnag = new CrafterPdfAnagModel();
			Tools.copyCommandDataModel(pdfAnag, crafterPdfAnag);
			
			crafterPdfAnag.getElencoRuoliUtilizzatori().clear();
			for(int i=0;i<pdfAnag.getPdfElencoRuoliUtilizzatori().size();i++) {
				PdfRuoloUtilizzatoreModel r = (PdfRuoloUtilizzatoreModel)pdfAnag.getPdfElencoRuoliUtilizzatori().get(i);
				if(r.getIsSelezionato().booleanValue()) {
					CrafterPdfAnagRuoloUtilizzatoreModel cr = new CrafterPdfAnagRuoloUtilizzatoreModel();
					cr.setCodiceRuoloUtilizzatore(r.getCodiceRuoloUtilizzatore());
					crafterPdfAnag.getElencoRuoliUtilizzatori().add(cr);
				}
			}
			
			// Temporaneamente impostiamo il flag per la sottoscrivibilità in carta chimica con il flag che indica l'esisteza della carta chimica
			crafterPdfAnag.setPdfIsCartaChimicaEnabled(new BooleanType(pdfAnag.getExistInCartaChimica().booleanValue()));
			// **********************************************************************************************************************************
			
			if(pdfAnag.getIsFromCatalogoModuli().booleanValue()){
				PdfModuloModel modulo = pdfAnag.getPdfModulo();
				crafterPdfAnag.setPdfStartDate(modulo.getDataInizioValidita());
				crafterPdfAnag.setPdfEndDate(modulo.getDataFineValidita());
			}
			
			ListType pubs = confModel.getPdfPubblicationList();
			
			for(int i=0;i<pubs.size();i++){
				PdfAnagModel pdfPublication = (PdfAnagModel)pubs.get(i);
				CrafterPdfPublicationModel crafterPdfPublication = new CrafterPdfPublicationModel();
				Tools.copyCommandDataModel(pdfPublication, crafterPdfPublication);
				crafterPdfAnag.getPdfPubblicationList().add(crafterPdfPublication);
			}
			
			String xmlTemplate = Tools.xmlFromModel(crafterPdfAnag);
			
			String fname = pdfAnag.getPdfId().toString();
			result = savePublications(csc, pdfAnag, pubs, fname);
			NasStorage.saveSimpleFile(csc, sCLIENTIPROMOTORI, sCatalogoOperazioni, 
										fname+".xml", new ByteArrayInputStream(xmlTemplate.getBytes("UTF-8")));
			return result;
			
		}catch(Exception e){
			return "WARNING: Errore sistemistico nel popolamento crafter: "+e.toString();
		}
		
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String savePublications(ClientSessionContext csc, PdfAnagModel pdfAnag, ListType pubs, String fname) {
		StringBuilder result = new StringBuilder();
		DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfAsStream");
		for(int i=0;i<pubs.size();i++){					
			PdfAnagModel pdfPublication = (PdfAnagModel)pubs.get(i);
			if(!savePdf(csc, pdfAnag, pdfPublication, dao, fname)){
				if(result.length() == 0)
					result.append("WARNING: errore nel popolamento crafter del/i pdf ");
				result.append("["+pdfAnag.getPdfId()+"-"+pdfPublication.getPdfPublicationId()+"]");
			}
			if(pdfPublication.getPdfPublicationId().intValue() == pdfPublication.getPdfPubIdCorrente().intValue())
				break;
		}
		return result.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean savePdf(ClientSessionContext csc, PdfAnagModel pdfAnag, PdfAnagModel pdfPublication, DAOObject dao, String fname){
		try{
			
			boolean fileExist = true;
			String title = null;
			byte[] fileContent = null;
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfAnag.setPdfPublicationId(pdfPublication.getPdfPublicationId());
			pdfInstance.setPdfAnag(pdfAnag);
			
			DAOQueryResultModel qRes = dao.executeQueryAccess("blobPdfPubblicato",pdfInstance);
			if(qRes.getResult().size() != 1){
				fileExist = false;
			}else{
				title = pdfAnag.getTitle();
				fileContent = pdfInstance.getPdfContent().byteArrayValue();
				if(fileContent == null)
					fileContent = PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdfInstance.getPdfAnag().getPdfId(), pdfInstance.getPdfAnag().getPdfPublicationId());
				fileContent = PdfEngine.compilePdfForDownload(fileContent, pdfAnag);
			}	
			if(!fileExist)
				return false;

			if(title != null && title.length() > 0)
				fileContent = PdfEngine.putTitle(title, fileContent, false);
			NasStorage.saveSimpleFile(csc, sCLIENTIPROMOTORI, sCatalogoOperazioni, 
										fname+"-"+pdfPublication.getPdfPublicationId()+".pdf", new ByteArrayInputStream(fileContent));
			
			fileContent = PdfEngine.generateAsFacsimile(fileContent);
			if(title != null && title.length() > 0)
				fileContent = PdfEngine.putTitle(title, fileContent, true);			
			NasStorage.saveSimpleFile(csc, sCLIENTIPROMOTORI, sCatalogoOperazioni, 
										fname+"-"+pdfPublication.getPdfPublicationId()+"-facsimile.pdf", new ByteArrayInputStream(fileContent));
			return true;
			
		}catch(DAOException daoe){
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean deleteFromCrafter(ClientSessionContext csc, PdfAnagModel pdfAnag){
		try{
			String fname = pdfAnag.getPdfId().toString();
			NasStorage.saveSimpleFile(csc, sCLIENTIPROMOTORI, sCatalogoOperazioni, 
										fname+".delete", new ByteArrayInputStream(new byte[0]));
			return true;
		}catch(Exception e){
			return false;
		}
	}	
}
