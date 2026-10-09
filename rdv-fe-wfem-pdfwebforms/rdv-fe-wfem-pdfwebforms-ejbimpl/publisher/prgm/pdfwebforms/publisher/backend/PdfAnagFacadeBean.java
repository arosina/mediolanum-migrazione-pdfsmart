package prgm.pdfwebforms.publisher.backend;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.imageio.ImageIO;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.VersionPrinter;
import com.atosorigin.wfem.util.XmlServiceCallData;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import prgm.pdfwebforms.catalog.PdfCatalogModel;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.publisher.common.CostantiPublisher;
import prgm.pdfwebforms.publisher.crafter.CrafterPdfUtils;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;
import prgm.pdfwebforms.publisher.model.PdfPageAnagModel;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;
import prgm.pdfwebforms.publisher.mom.SrvAggiornaModuloModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfAnagFacadeBean extends FacadeObject implements PdfAnagFacade {
  
	{VersionPrinter.getInstance().print("PdfWebForms",this);}
	
	public static final String DAO_XML_NAME = "PdfWebForms.PdfPublisher";
	public static final String DAO_XML_MOM_NAME = "PdfWebForms.PdfMomPublisher";
	
	public static int 		PRIT_MILTIOPERAZIONE_CODE = -1;
	public static String 	PRIT_MILTIOPERAZIONE_DESCR = "multioperazione";

	private static final String S_CARIARE_IL_FILE = "Caricare il file";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel fillCodDesc(ClientSessionContext csc, CommandDataModel model, boolean includeInnerModels) throws EJBException {
		try{
			
			new DAOObject(csc,DAO_XML_NAME).fillCodDesc(model,includeInnerModels);
			return model;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPublisherModel searchPdf(ClientSessionContext csc, PdfPublisherModel model) throws EJBException {
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			if(model.getArea().equals(PdfCatalogModel.AREA_CATALOGO_MODULI))
				model.setPdfList(dao.executeQueryAccess("searchPdfAnagCatalogo",model).getResult());
			else
				model.setPdfList(dao.executeQueryAccess("searchPdfAnag",model).getResult());
			return model;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel newPdfConf(ClientSessionContext csc) throws EJBException {
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			PdfConfigurationModel confModel = new PdfConfigurationModel();
			CrafterPdfUtils.loadAreeCrafter(csc, confModel);
			confModel.getPdfAnag().setPdfElencoRuoliUtilizzatori(dao.executeQueryAccess("loadAnagRuoliUtilizzatori", confModel.getPdfAnag()).getResult());
			dao.fillCodDesc(confModel.getPdfAnag(),false);
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception on new pdf conf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception on new pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel openPdfConf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException {
		return openPdfConf(csc, pdfAnagKey, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel openPdfConf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey, boolean light) throws EJBException {
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			PdfConfigurationModel confModel = new PdfConfigurationModel(pdfAnagKey);
			confModel.setPdfPubblicationList(dao.executeQueryAccess("pdfPubblicationList",pdfAnagKey).getResult());
			if(!light){
				CrafterPdfUtils.loadAreeCrafter(csc, confModel);
				confModel.setPdfNumArchiviazioni((IntegerType)dao.executeQueryAccess("pdfNumArchiviazioni",pdfAnagKey).getSingleResult());
			}
			
			PdfAnagModel pdfAnag = new PdfAnagModel(pdfAnagKey);
			if(!light){
				pdfAnag = readPdfOnWork(csc,confModel,pdfAnagKey);
			}else{
				dao.executeTableLoadAccess("pdfAnag",pdfAnag);
				pdfAnag.setPdfElencoRuoliUtilizzatori(dao.executeQueryAccess("loadAnagRuoliUtilizzatori", pdfAnag).getResult());
			}
			confModel.setPdfAnag(pdfAnag);
			if(light)
				return confModel;

			if(confModel.getPdfPubblicationList().size() == 0 && 
			   confModel.getPdfNumArchiviazioni().intValue() == 0 &&
			   pdfAnag.getPdfContent().isNull()){
				PdfAnagModel pdf = new PdfAnagModel(pdfAnagKey);
				DAOTableResultModel tRes = dao.executeTableLoadAccess("pdfPublication_WORK",pdf);
				if(tRes.getResult().intValue() == 0){
					tRes = dao.executeTableLoadAccess("allegatoOriginaleModulo",pdfAnag);
					if(tRes.getResult().intValue() > 0 && pdfAnag.getPdfContent().getContentType().toString().indexOf("application/pdf") >= 0)
						confModel.getPdfAnag().setFileIsChanged(new BooleanType(true));
					else
						pdfAnag.getPdfContent().clear();
				}else{
					PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdfAnag);
				}
				
			}
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception reading pdf conf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception reading pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel savePdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		
		try{
			confModel.resetCommandErrors(); 
			confModel.resetCommandMessages();
			
			PdfAnagModel pdf = confModel.getPdfAnag();
			Tools.resetTypesWarningAndErrors(pdf);
			
			if(pdf.getIsFromCatalogoModuli().booleanValue() || confModel.getPdfPubblicationList().size() > 0){
				if(confModel.getPdfAnag().getPdfContent().isNull())
					confModel.getPdfAnag().getPdfContent().addTypeError(S_CARIARE_IL_FILE);
			}else{
				if(pdf.getPdfCode().isNull()){
					pdf.getPdfCode().addTypeError("Codice pdf obbligatorio");
				}else if(!pdf.getPdfCode().equals(pdf.getPdfOriginalCode())){
					BooleanType pdfCodeExist = (BooleanType)new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("pdfCodeExist",pdf).getSingleResult();
					if(pdfCodeExist.booleanValue())
						pdf.getPdfCode().addTypeError("Codice modulo gia' utilizzato");
				}
			}
			
			if(!pdf.getPdfMomCode().isNull()){
				BooleanType pdfMomCodeExist = (BooleanType)new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("pdfMomCodeExist",pdf).getSingleResult();
				if(pdfMomCodeExist.booleanValue())
					pdf.getPdfMomCode().addTypeError("Codice MOM gia' utilizzato");
			}
			
			if(pdf.getPdfDescr().isNull())
				pdf.getPdfDescr().addTypeError("Descrizione pdf obbligatoria");
			
			if(confModel.getPdfAnag().getPdfContent().isNull())
				confModel.getPdfAnag().getPdfContent().addTypeError(S_CARIARE_IL_FILE);
			
			if(Tools.containsTypeWarningOrErrors(pdf)){
				confModel.addCommandError("errori");
				return confModel;				
			}

			if(pdf.getFileIsChanged().booleanValue()){
				validaPdf(csc,confModel);
				if(confModel.getPdfValidationErrorMessage().length() > 0)
					return confModel;
			}
			
			pdf.setPagesToSave(null);
			try {
				if((pdf.getFileIsChanged().booleanValue() && pdf.getGeneratePageImages().booleanValue()) ||
				   (pdf.getPdfNumPages().intValue() == 0 && pdf.getGeneratePageImages().booleanValue())){
						pdf.setPagesToSave(createPdfPageImages(pdf));
					if(pdf.getFileIsChanged().booleanValue())
						pdf.setFileIsChanged(new BooleanType(false));
				}
			}catch(Exception e) {
				confModel.setPdfValidationWarningMessage("");
				confModel.setPdfValidationErrorMessage("<font color='red'><b>Attenzione: caricamento annullato.</b></font><br><br>"+
														"Il pdf presenta caratteristiche non compatibili con lo standard");
				return confModel;
			}
			
			pdf.setPdfPublishTime(new TimestampType());
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			pdf = writer.savePdfConf(csc,pdf);
			confModel.setPdfAnag(pdf);
			pdf.setPagesToSave(null);
			
			new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("loadOtherPdfDataOnWork",pdf);
			pdf.setPdfOriginalCode(new StringType(pdf.getPdfCode().toString()));
			
			confModel.addCommandMessage("operazioneOk");
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception saving pdf conf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception saving pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel publishPdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		
		try{
			confModel.resetCommandErrors(); 
			confModel.resetCommandMessages();
			
			PdfAnagModel pdf = confModel.getPdfAnag();
			Tools.resetTypesWarningAndErrors(pdf);
			
			verificaDatiAnagrafici(csc, pdf);
			verificaDatiPubblicazione(csc,confModel,false);

			if(Tools.containsTypeWarningOrErrors(pdf)){
				confModel.addCommandError("errori");
				return confModel;				
			}

			validaPdf(csc,confModel);
			if(confModel.getPdfValidationErrorMessage().length() > 0)
				return confModel;				

			// Se integrato con pratiche digitali e no ndevo "skippare" la chiamata chiamo MOM per l'aggiornamento modulo
			if(!pdf.getCallSrvDispositivaBMED().isNull() && !pdf.getSkipCallToAggiornaModuloMom().booleanValue()) {
				SrvAggiornaModuloModel callModel = new SrvAggiornaModuloModel();
				callModel.setUserId(new StringType(csc.getUserCode()));
				callModel.setPdfAnag(pdf);				
				DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_MOM_NAME).executeOSBAccess("aggiornaVersioneModulo", callModel);
				if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){
					confModel.addCommandError("Errore di comunicazione con il servizio MOM di aggiornamento modulo: "+wsRes.getWsCallData().getMessage());
					return confModel;	
				}
			}
			

			confModel.setPdfValidationWarningMessage("");
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			pdf.setPdfPublishUser(new StringType(csc.getUserCode()));
			pdf.setPdfPublishTime(Tools.now());
			if(confModel.getPubblicaComeNuovaPubblicazione().booleanValue())
				dao.executeQueryAccess("loadNextPubblicationId",pdf);
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			pdf = writer.publishPdfConf(csc,pdf);
			confModel.setPdfAnag(pdf);
			
			confModel.setPdfPubblicationList(dao.executeQueryAccess("pdfPubblicationList",pdf).getResult());
			confModel.setPdfNumArchiviazioni((IntegerType)dao.executeQueryAccess("pdfNumArchiviazioni",pdf).getSingleResult());
			
			confModel.setCrafterErrorMessage(CrafterPdfUtils.moveToCrafter(csc, confModel));	
			
			confModel.setWorkingAreaHidden(true);
			confModel.addCommandMessage("operazioneOk");
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception publishing pdf conf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception publishing pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verificaDatiAnagrafici(ClientSessionContext csc, PdfAnagModel pdf) throws Exception{
		
		try{
			
			pdf.initFlagsFromInvioInSede();
			
			if(!pdf.getIsFromCatalogoModuli().booleanValue()){
				if(pdf.getPdfCode().isNull()){
					pdf.getPdfCode().addTypeError("Codice pdf obbligatorio");
				}else if(!pdf.getPdfCode().equals(pdf.getPdfOriginalCode())){
					BooleanType pdfCodeExist = (BooleanType)new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("pdfCodeExist",pdf).getSingleResult();
					if(pdfCodeExist.booleanValue())
						pdf.getPdfCode().addTypeError("Codice modulo gia' utilizzato");
				}
			}
			
			if(!pdf.getPdfMomCode().isNull()){
				BooleanType pdfMomCodeExist = (BooleanType)new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("pdfMomCodeExist",pdf).getSingleResult();
				if(pdfMomCodeExist.booleanValue())
					pdf.getPdfMomCode().addTypeError("Codice MOM gia' utilizzato");
			}else if(!pdf.getCallSrvDispositivaBMED().isNull()) {
				pdf.getPdfMomCode().addTypeError("Modulo integrato con pratiche digitali: specificare il codice MOM");
			}
			
			if(pdf.getPdfDescr().isNull())
				pdf.getPdfDescr().addTypeError("Descrizione pdf obbligatoria");

			String prodPritObbl = "Prodotto Prit obbligatorio";
			String operPritObbl = "Operazione Prit obbligatoria";
			if(pdf.getIsFromCatalogoModuli().booleanValue() || pdf.getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_OPERAZIONI)){
				
				if(pdf.getPdfIsCartaLiberaEnabled().booleanValue()  ||
				   pdf.getPdfIsCartaChimicaEnabled().booleanValue() ||
				   pdf.getPdfIsFirmaDigitaleEnabled().booleanValue()||
				   pdf.getPdfIsCopernicoEnabled().booleanValue()){
							
					if(pdf.getPdfCodProdottoPrit().isNull())
						pdf.getPdfCodProdottoPrit().addTypeError(prodPritObbl);
					
					if(pdf.getPdfCodOperazionePrit().isNull())
						pdf.getPdfCodOperazionePrit().addTypeError(operPritObbl);
					
				}
				
			}else{

				if(pdf.getPdfIsFirmaDigitaleEnabled().booleanValue() ||
				   pdf.getPdfIsCopernicoEnabled().booleanValue()){
					
					if(pdf.getPdfCodProdottoPrit().isNull())
						pdf.getPdfCodProdottoPrit().addTypeError(prodPritObbl);
					
					if(pdf.getPdfCodOperazionePrit().isNull())
						pdf.getPdfCodOperazionePrit().addTypeError(operPritObbl);

				}
				
			}
			
			if(pdf.getPdfCodProdottoPrit().isNull() && !pdf.getPdfCodOperazionePrit().isNull()) 
				pdf.getPdfCodProdottoPrit().addTypeError(prodPritObbl);
			if(!pdf.getPdfCodProdottoPrit().isNull() && pdf.getPdfCodOperazionePrit().isNull()) 
				pdf.getPdfCodOperazionePrit().addTypeError(operPritObbl);

			if(!pdf.getPdfStartDate().isNull() && !pdf.getPdfEndDate().isNull()){
				if(pdf.getPdfStartDate().compareTo(pdf.getPdfEndDate()) > 0)
					pdf.getPdfStartDate().addTypeError("La data inizio non può essere successiva alla data di fine validità");
			}
			
			if(pdf.getSendCliSmsOnSign().booleanValue() && pdf.getCliSmsOnSignText().isNull()){
				pdf.getCliSmsOnSignText().addTypeError("Specificare il testo dell'SMS");
			}
			
			if(!pdf.getExternalLinkOnSignLabel().isNull() && pdf.getExternalLinkOnSignUrl().isNull()){
				pdf.getExternalLinkOnSignLabel().addTypeError("L'etichetta del link esterno in fase di firma va valorizzata solo se valorizzata anche la URL");
			}
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verificaDatiPubblicazione(ClientSessionContext csc, PdfConfigurationModel confModel, boolean fromUpdateAnag){
		
		PdfAnagModel pdf = confModel.getPdfAnag();

		if(pdf.getPdfNumPages().intValue() == 0)
			pdf.getPdfNumPages().addTypeError("Generare/uploadare le immagini delle pagine");

		if(pdf.getPdfPubStartDate().isNull())
			pdf.getPdfPubStartDate().addTypeError("Specificare la data di entrata in vigore della pubblicazione");
		else if(pdf.getPdfPubStartDate().compareTo(Tools.today()) < 0)
			pdf.getPdfPubStartDate().addTypeError("Data inizio compilabilità non ammissibile");

		if(pdf.getPdfContent().isNull())
			pdf.getPdfContent().addTypeError(S_CARIARE_IL_FILE);

		if(!pdf.getCallSrvDispositivaBMED().isNull()) {
			if(confModel.getPdfPubblicationList().size() > 0) {
				if(pdf.getPdfDataFineAccettazionePubblPrec().isNull())
					pdf.getPdfDataFineAccettazionePubblPrec().addTypeError("Specificare la data fine accettazione old");
				else if(!pdf.getPdfPubStartDate().isNull() && pdf.getPdfDataFineAccettazionePubblPrec().compareTo(pdf.getPdfPubStartDate()) < 0)
					pdf.getPdfDataFineAccettazionePubblPrec().addTypeError("La data indicata non può essere precedente alla data di entrata in vigore");
			}
			if(pdf.getPdfMomVersion().isNull()) {
				pdf.getPdfMomVersion().addTypeError("Specificare la versione MOM");
			}else if(pdf.getPdfMomVersion().toString().length() != 3){
				pdf.getPdfMomVersion().addTypeError("La versione MOM deve essere di 3 caratteri");
			}else {
				String codMese = pdf.getPdfMomVersion().toString().substring(0,1);
				if(CostantiPublisher.CODICI_MESE.indexOf(codMese.toUpperCase()) < 0)
					pdf.getPdfMomVersion().addTypeError("Il codice del mese deve essere la lettera corrispondente al suo numero ["+CostantiPublisher.CODICI_MESE+"]");
			}
		}
			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel editPdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			
			int selectedPublicationId = confModel.getPdfAnag().getPdfPublicationId().intValue();
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.editPdfPublication(csc,(PdfAnagKeyModel)confModel.getPdfAnag());
			
			PdfAnagModel pdf = readPdfOnWork(csc,confModel,confModel.getPdfAnag());
			
			// Per far si che il controllo del file venga attivato
			pdf.setFileIsChanged(new BooleanType(false));
			if(selectedPublicationId < pdf.getPdfPubIdCorrente().intValue())
				pdf.setFileIsChanged(new BooleanType(true));
			
			pdf.setPdfValidationWarningMessage(new StringType());
			confModel.setPdfAnag(pdf);
			return confModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Exception reading pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel cancelChangesPdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.deletePdfOnWork(csc, confModel.getPdfAnag());
			
			PdfAnagModel pdf = readPdfOnWork(csc,confModel,confModel.getPdfAnag());
			confModel.setPdfAnag(pdf);
			
			return confModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Exception reading pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel updatePdfAnag(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		
		try{
			confModel.resetCommandErrors(); 
			confModel.resetCommandMessages();
			
			PdfAnagModel pdf = confModel.getPdfAnag();
			Tools.resetTypesWarningAndErrors(pdf);
			
			verificaDatiAnagrafici(csc, pdf);
			
			if(Tools.containsTypeWarningOrErrors(pdf)){
				confModel.addCommandError("errori");
				return confModel;				
			}

			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			pdf = writer.updatePdfAnag(csc,pdf);
			confModel.setPdfAnag(pdf);
			
			confModel.setCrafterErrorMessage(CrafterPdfUtils.moveToCrafter(csc, confModel));
			
			confModel.addCommandMessage("operazioneOk");
			return confModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Exception saving pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagKeyModel deletePdf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException{
		try{
			
			PdfAnagModel pdfAnag = new PdfAnagModel(pdfAnagKey);
			DAOTableResultModel tRes = new DAOObject(csc,DAO_XML_NAME).executeTableLoadAccess("pdfAnag",pdfAnag);
			if(tRes.getResult().intValue() != 1)
				return pdfAnagKey;
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.deletePdf(csc, pdfAnagKey);
			
			if(!CrafterPdfUtils.deleteFromCrafter(csc, pdfAnag))
				pdfAnagKey.setCrafterErrorMessage("WARNING: allineamento crafter non riuscito");
			
			return pdfAnagKey;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception deleting pdf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel deletePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.deletePdfPublication(csc,(PdfAnagKeyModel)confModel.getPdfAnag());

			if(confModel.getPdfAnag().getPdfPublicationId().intValue() == confModel.getPdfAnag().getPdfOriginalPubId().intValue())
				writer.deletePdfOnWork(csc, (PdfAnagKeyModel)confModel.getPdfAnag());
			
			callCancellaVersioneModuloMom(csc, confModel);
			
			String profiloUtente = confModel.getProfiloUtente();
			confModel = openPdfConf(csc,(PdfAnagKeyModel)confModel.getPdfAnag());
			confModel.setProfiloUtente(profiloUtente);
			
			confModel.setCrafterErrorMessage(CrafterPdfUtils.moveToCrafter(csc, confModel));
			
			return confModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void callCancellaVersioneModuloMom(ClientSessionContext csc, PdfConfigurationModel confModel) throws Exception {
		if(confModel.getPdfAnag().getPdfMomCode().isNull() || confModel.getPdfAnag().getPdfMomVersion().isNull())
			return;
		try {
			SrvAggiornaModuloModel callModel = new SrvAggiornaModuloModel();
			callModel.setUserId(new StringType(csc.getUserCode()));
			callModel.setPdfAnag(confModel.getPdfAnag());				
			new DAOObject(csc, DAO_XML_MOM_NAME).executeOSBAccess("cancellaVersioneModulo", callModel);
		}catch(DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel archivePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.archivePdfPublication(csc,(PdfAnagKeyModel)confModel.getPdfAnag());
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			confModel.setPdfPubblicationList(dao.executeQueryAccess("pdfPubblicationList",confModel.getPdfAnag()).getResult());
			confModel.setPdfNumArchiviazioni((IntegerType)dao.executeQueryAccess("pdfNumArchiviazioni",confModel.getPdfAnag()).getSingleResult());
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception deleting pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel restorePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			
			PdfAnagManager writer = (PdfAnagManager)ROF.getManager(csc,PdfAnagManager.class);
			writer.restorePdfPublication(csc,(PdfAnagKeyModel)confModel.getPdfAnag());
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			confModel.setPdfPubblicationList(dao.executeQueryAccess("pdfPubblicationList",confModel.getPdfAnag()).getResult());
			confModel.setPdfNumArchiviazioni((IntegerType)dao.executeQueryAccess("pdfNumArchiviazioni",confModel.getPdfAnag()).getSingleResult());
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception deleting pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigurationModel savePageImage(ClientSessionContext csc, PdfConfigurationModel confModel) throws EJBException {
		try{
			

			PdfAnagModel pdfAnag = confModel.getPdfAnag();
			
			PdfPageAnagModel page = new PdfPageAnagModel();
			page.setPdfId(new StringType(pdfAnag.getPdfId().toString()));
			page.setPdfCode(new StringType(pdfAnag.getPdfCode().toString()));
			page.setPdfPublicationId(new IntegerType(pdfAnag.getPdfPublicationId().intValue()));
			page.setPdfPageNum(new IntegerType(pdfAnag.getPageImageNum().intValue()));
			page.setPdfPageImg(new ByteArrayType(pdfAnag.getPageImageContent().getFileContent()));
			
			boolean blobSuDB = PdfNasUtil.blobPublisherSuDB(csc);
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			PdfNasUtil.PDF_PAGE_WORK.writePdfPageWorkContent(csc, page, blobSuDB);
			try{
				dao.executeTableUpdateAccess("pdfPage_WORK",page);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("pdfPage_WORK",page);
			}
			
			pdfAnag.getPageImageContent().clear();
			pdfAnag.setPageImageNum(new IntegerType());
			return confModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception deleting pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfAnagModel readPdfOnWork(ClientSessionContext csc, PdfConfigurationModel confModel, PdfAnagKeyModel pdfAnagKey) throws EJBException{
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			PdfAnagModel pdf = new PdfAnagModel(pdfAnagKey);
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadModuloCatalogoAnag",pdf);
			dao.executeTableLoadAccess("pdfAnag",pdf);
			pdf.setPdfElencoRuoliUtilizzatori(dao.executeQueryAccess("loadAnagRuoliUtilizzatori", pdf).getResult());
			if((pdf.getPdfArea().isNull() || pdf.getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_MODULI)) && qRes.getResult().size() > 0){
				pdf.setIsFromCatalogoModuli(new BooleanType(true));
				pdf.setPdfArea(new StringType(PdfCatalogModel.AREA_CATALOGO_MODULI));
			}
			confModel.setWorkingAreaHidden(false);
			DAOTableResultModel tRes = dao.executeTableLoadAccess("pdfPublication_WORK",pdf);
			if(tRes.getResult().intValue() <= 0)
				confModel.setWorkingAreaHidden(true);
			else
				PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdf);
			dao.executeQueryAccess("loadOtherPdfDataOnWork",pdf);
			pdf.setPdfOriginalCode(new StringType(pdf.getPdfCode().toString()));
			new DAOObject(csc,DAO_XML_NAME).fillCodDesc(pdf,false);
			return pdf;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO Exception reading pdf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception reading pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void validaPdf(ClientSessionContext csc, PdfConfigurationModel confModel) throws DAOException, Exception{

		confModel.getPdfAnag().setPdfValidationWarningMessage(new StringType());
		confModel.setPdfValidationWarningMessage("");
		confModel.setPdfValidationErrorMessage("");
		
		if(confModel.getPdfAnag().getPdfContent().isNull())
			return;

		// ERRORI BLOCCANTI
		ArrayList<String> errors = new ArrayList<>();
		PdfInfos pdfInfos = null;
		try{
			pdfInfos = PdfEngine.inspectPdfInfos(confModel.getPdfAnag().getPdfContent().getFileContent());
		}catch(Throwable t){
			confModel.setPdfValidationErrorMessage("<font color='red'><b>Attenzione: caricamento annullato.</b><br></font>Il pdf risulta illeggibile, corrotto o protetto");
			return;
		}
		
		List<PdfFieldInfos> fieldInfos = pdfInfos.getFieldInfos();
		
		// Verifico (e conto) i nomi dei campi firma
		int numSignFields = 0;
		confModel.getPdfAnag().setPdfNumSignFields(new IntegerType(numSignFields));
		ArrayList<String> nomiFirmaErrati = new ArrayList<String>();
		ArrayList<String> nomiFirmaMancanti = new ArrayList<String>();
		for(PdfFieldInfos fi : fieldInfos){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			numSignFields++;
			String pdfFieldName = fi.pdfFieldName;
			if(!PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(pdfFieldName).matches() &&
			   !PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(pdfFieldName).matches()){
				nomiFirmaErrati.add(pdfFieldName);
			}else{
				PdfFieldInfos nomefirma = pdfInfos.findFieldInfoByPdfName("nome"+pdfFieldName);
				if(nomefirma == null)
					nomiFirmaMancanti.add("nome"+pdfFieldName);
			}
		}
		confModel.getPdfAnag().setPdfNumSignFields(new IntegerType(numSignFields));
		if(!nomiFirmaErrati.isEmpty())
			errors.add(fieldReportList("Campi firma con nome errato.", nomiFirmaErrati));
		
		if(!nomiFirmaMancanti.isEmpty())
			errors.add(fieldReportList("Campi, utili a contenere il nome del firmatario, mancanti.", nomiFirmaMancanti));

		if(!errors.isEmpty()){
			confModel.getPdfAnag().getPdfContent().clear();
			StringBuilder sberrors = new StringBuilder("<font color='red'><b>Attenzione: caricamento annullato.</b><br></font>");
			for(String e : errors)
				sberrors.append(e+"<br><br>");
			confModel.setPdfValidationErrorMessage(sberrors.toString());
			return;
		}
		
		// Costruisco l'acroform version confrontando la form del pdf attuale con quella dell'ultima pubblicazione
		String acroformChanges = "";
		try{
			acroformChanges = initAcroformVersion(csc, confModel, pdfInfos);
		}catch(PdfReadException pre){
			confModel.setPdfValidationErrorMessage("<font color='red'>"+
														"<b>Attenzione: caricamento annullato.</b><br>"+
													"</font>"+
													"Il pdf attualmente pubblicato risulta illeggibile, corrotto o protetto");
			return;			
		}
		
		// WARNING
		ArrayList<String> warnings = new ArrayList<String>();

		// Verifico la scrittura dei campi 
		ArrayList<String> nomiSetFieldErrati = verifySetAcroformField(fieldInfos, confModel.getPdfAnag().getPdfContent().getFileContent());
		if(!nomiSetFieldErrati.isEmpty())
			warnings.add(fieldReportList("<br>Campi in errore nella impostazione del valore.", nomiSetFieldErrati));
		
		// Cambio acroformversion
		if(acroformChanges.length() > 0){
			warnings.add("<b>I campi nel pdf sono cambiati.</b><br>"+
						 "Verificare eventuali impatti sui controlli, se esistenti, prima della pubblicazione"+acroformChanges);
		}
		
		// Verifico i nomi dei campi
		ArrayList<String> nomiErrati = new ArrayList<String>();
		for(PdfFieldInfos fi : fieldInfos){
			String pdfFieldName = fi.pdfFieldName;
			if(nomiErrati.contains(pdfFieldName))
				continue;
			if(!PdfEngine.NOMI_CAMPO_PATTERN.matcher(pdfFieldName).matches()){
				nomiErrati.add(pdfFieldName);
			}else if(pdfFieldName.length() > 0){
				char firstChar = pdfFieldName.charAt(0);
				if(firstChar >= '0' && firstChar <= '9')
					nomiErrati.add(pdfFieldName);
			}
		}
		if(!nomiErrati.isEmpty()){
			StringBuilder er = new StringBuilder("<b>Il pdf contiene nomi di campo errati.</b><br>"+
											   		"Sono stati opportunamente adeguati ma "+
											   		"sarebbe utile modificarli utilizzando solo lettere (a-z, A-Z) e numeri (0-9), senza spazi "+
					   						   		"e senza che inizino con un carattere numerico.");
			warnings.add(er.toString());
		}
		
		if(!warnings.isEmpty()){
			StringBuilder sbwarnings = new StringBuilder("<font color='DarkGoldenRod'>"+
															"<b>Caricamento effettuato con i seguenti punti di attenzione:</b><br>"+
					 									"</font>");
			for(String w : warnings)
				sbwarnings.append(w+"<br>");
			confModel.setPdfValidationWarningMessage(sbwarnings.toString());
			confModel.getPdfAnag().setPdfValidationWarningMessage(new StringType(sbwarnings.toString()));
		}

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private ArrayList<String> verifySetAcroformField(List<PdfFieldInfos> fieldInfos, byte[] pdf){
		ArrayList<String> res = new ArrayList<>();
		PdfReader reader = null;
	    PdfStamper stamp = null;
		try(ByteArrayOutputStream pdfOut = new ByteArrayOutputStream()){
			reader = new PdfReader(pdf);
			stamp = new PdfStamper(reader, pdfOut);
		    AcroFields acroForm = stamp.getAcroFields();
			for(PdfFieldInfos fi : fieldInfos){
				if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
					continue;
				String fieldVal = "0";
				if(fi.expValue != null)
					fieldVal = fi.expValue;
				else if(fi.defValue != null)
					fieldVal = fi.defValue;
				try {
					PdfEngine.setAcroformField(acroForm, fi.pdfFieldName, fieldVal);
				}catch(Exception e) {
					res.add(fi.pdfFieldName);
				}
			}			
		}catch(Exception e){
			res.add(e.toString());
		}finally {
			try {
				if(stamp != null) stamp.close();
				if(reader != null) reader.close();
			}catch(Exception e) {
				res.add(e.toString());
			}
		}
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String initAcroformVersion(ClientSessionContext csc, PdfConfigurationModel confModel, 
									 	PdfInfos newPdfInfos) throws Exception, DAOException{
		
		List<PdfFieldInfos> newFieldInfos = newPdfInfos.getFieldInfos();
		
		// Costruisco l'acroform version confrontando la form del pdf attuale con quella dell'ultima pubblicazione
		boolean newAcroformVersion = false;
		ArrayList<String> fieldAddeded = new ArrayList<String>();
		ArrayList<String> fieldRemoved = new ArrayList<String>();
		ArrayList<String> fieldChanged = new ArrayList<String>();
		ArrayList<String> autoManagedFieldAddeded = new ArrayList<String>();
		ArrayList<String> autoManagedFieldRemoved = new ArrayList<String>();
		ArrayList<String> autoManagedFieldChanged = new ArrayList<String>();
		
		IntegerType lastPublishedPdfAcroformVersion = (IntegerType)new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("loadLastPublishedPdfAcroformVersion",confModel.getPdfAnag()).getSingleResult();
		if(lastPublishedPdfAcroformVersion == null || lastPublishedPdfAcroformVersion.isNull())
			lastPublishedPdfAcroformVersion = new IntegerType(1);
		
		PdfAnagModel tmpAnag = (PdfAnagModel)Tools.cloneObject(confModel.getPdfAnag());
		new DAOObject(csc,DAO_XML_NAME).executeQueryAccess("loadLastPublishedPdfContent",tmpAnag);
		byte[] lastPublishedPdfContent = tmpAnag.getPdfContent().getFileContent();
		if(lastPublishedPdfContent == null)
			PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, tmpAnag);
		lastPublishedPdfContent = tmpAnag.getPdfContent().getFileContent();
		if(lastPublishedPdfContent != null){
			
			PdfInfos lastPdfInfos = null;
			try{
				lastPdfInfos = PdfEngine.inspectPdfInfos(lastPublishedPdfContent);
			}catch(Throwable t){
				throw new PdfReadException();
			}
			
			List<PdfFieldInfos> lastFieldInfos = lastPdfInfos.getFieldInfos();
			for(PdfFieldInfos lastFieldInfo : lastFieldInfos){
				
				PdfFieldInfos matchedField = newPdfInfos.findFieldInfoByPdfName(lastFieldInfo.pdfFieldName); 
				if(matchedField == null){
					if(fieldIsAutoManaged(lastFieldInfo)){
						if(!autoManagedFieldRemoved.contains(lastFieldInfo.pdfFieldName))
							autoManagedFieldRemoved.add(lastFieldInfo.pdfFieldName);
					}else{
						if(!fieldRemoved.contains(lastFieldInfo.pdfFieldName))
							fieldRemoved.add(lastFieldInfo.pdfFieldName);
						newAcroformVersion = true;
					}
					continue;
				}
				
				PdfFieldInfos matchedInfo = newPdfInfos.findFieldInfoByFieldInfo(lastFieldInfo); 
				if(matchedInfo == null){
					if(fieldIsAutoManaged(lastFieldInfo)){
						if(!autoManagedFieldChanged.contains(lastFieldInfo.pdfFieldName))
							autoManagedFieldChanged.add(lastFieldInfo.pdfFieldName);
					}else{
						if(!fieldChanged.contains(lastFieldInfo.pdfFieldName))
							fieldChanged.add(lastFieldInfo.pdfFieldName);
						newAcroformVersion = true;
					}
				}else if(!matchedInfo.dataType.equals(lastFieldInfo.dataType) || !matchedInfo.defValue.equals(lastFieldInfo.defValue)) {
					if(!fieldChanged.contains(lastFieldInfo.pdfFieldName))
						fieldChanged.add(lastFieldInfo.pdfFieldName);
					newAcroformVersion = true;
				}
			}
			
			for(PdfFieldInfos newFieldInfo : newFieldInfos){
				
				PdfFieldInfos matchedField = lastPdfInfos.findFieldInfoByPdfName(newFieldInfo.pdfFieldName); 
				if(matchedField == null){
					if(fieldIsAutoManaged(newFieldInfo)){
						if(!autoManagedFieldAddeded.contains(newFieldInfo.pdfFieldName))
							autoManagedFieldAddeded.add(newFieldInfo.pdfFieldName);
					}else{
						if(!fieldAddeded.contains(newFieldInfo.pdfFieldName))
							fieldAddeded.add(newFieldInfo.pdfFieldName);
						newAcroformVersion = true;
					}
				}
				
			}
			
			if(newAcroformVersion)
				lastPublishedPdfAcroformVersion = lastPublishedPdfAcroformVersion.add(new IntegerType(1));
		}
		
		confModel.getPdfAnag().setPdfAcroformVersion(lastPublishedPdfAcroformVersion);
		
		StringBuilder sfieldAddeded = new StringBuilder();
		StringBuilder sfieldRemoved = new StringBuilder();
		StringBuilder sfieldChanged = new StringBuilder();
		
		if(!autoManagedFieldAddeded.isEmpty())
			sfieldAddeded.append(fieldReportList("Campi che non modificano la vers. dell'acroform aggiunti:", autoManagedFieldAddeded));

		if(!autoManagedFieldRemoved.isEmpty())
			sfieldRemoved.append(fieldReportList("Campi che non modificano la vers. dell'acroform eliminati:", autoManagedFieldRemoved));

		if(!autoManagedFieldChanged.isEmpty())
			sfieldChanged.append(fieldReportList("Campi che non modificano la vers. dell'acroform modificati:", autoManagedFieldChanged));

		if(!fieldAddeded.isEmpty())
			sfieldAddeded.append(fieldReportList("Campi aggiunti:", fieldAddeded));

		if(!fieldRemoved.isEmpty())
			sfieldRemoved.append(fieldReportList("Campi eliminati:", fieldRemoved));

		if(!fieldChanged.isEmpty())
			sfieldChanged.append(fieldReportList("Campi modificati:", fieldChanged));

		if(sfieldAddeded.length() == 0 && sfieldRemoved.length() == 0 && sfieldChanged.length() == 0)
			return "";
		return "<br><br>"+sfieldAddeded+sfieldRemoved+sfieldChanged;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String fieldReportList(String title, ArrayList<String> fields) {
		StringBuilder res = new StringBuilder();
		res.append("<b>"+title+"</b><br>");
		res.append("<ul style='margin-top:3;'>");
		for(String f : fields)
			res.append("<li>"+f+"</li>");
		res.append("</ul>");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean fieldIsAutoManaged(PdfFieldInfos fieldInfo){
		if(fieldInfo.fieldType == AcroFields.FIELD_TYPE_SIGNATURE ||
   		   PdfPredefinedFields.AGENTE_NOME_FIRMA_N_PATTERN.matcher(fieldInfo.htmlFieldName).matches() ||
	       PdfPredefinedFields.AGENTE_CLAUSOLA_N_PATTERN.matcher(fieldInfo.htmlFieldName).matches() ||
	       PdfPredefinedFields.CLIENTE_NOME_FIRMA_N_DI_M_PATTERN.matcher(fieldInfo.htmlFieldName).matches() ||
	       PdfPredefinedFields.CLIENTE_CLAUSOLA_N_DI_M_PATTERN.matcher(fieldInfo.htmlFieldName).matches() ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.COPIA_PER) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_IMAGE) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_VALUE) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.CODICE_MODULO) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.DESCRIZIONE_MODULO) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.NUMERO_ISTANZA_MODULO) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA) ||
	       fieldInfo.htmlFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_QUESTIONARIO_IDD))
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ArrayList<PdfPageAnagModel> createPdfPageImages(PdfAnagModel pdf) throws Exception, DAOException{
		
		if(pdf.getPdfContent().isNull())
			return new ArrayList<PdfPageAnagModel>();
		
		InputStream pdfIs = pdf.getPdfContent().getInputStream();
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdfIs);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	    AcroFields form = stamp.getAcroFields();
	    int numPages = reader.getNumberOfPages();
	    for(int i=1;i<=numPages;i++)
	    	form.removeFieldsFromPage(i);
	    stamp.setFormFlattening(true);
	    stamp.close();
	    reader.close();
	    pdfOut.close();
	    pdfIs.close();

	    try{
	    	Class.forName("org.apache.pdfbox.Loader");
	    	return createPdfPageImagesPdfBox(pdf, pdfOut.toByteArray());
	    }catch(ClassNotFoundException cnfe){
	    	return createPdfPageImagesPdfBox_2_0_14(pdf, pdfOut.toByteArray());
	    }

	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ArrayList<PdfPageAnagModel> createPdfPageImagesPdfBox(PdfAnagModel pdf, byte[] pdfContent) throws Exception{
		
		ArrayList<PdfPageAnagModel> result = new ArrayList<PdfPageAnagModel>();
		PDDocument document = Loader.loadPDF(pdfContent);
		PDFRenderer pdfRenderer = new PDFRenderer(document);
		int numPdfPages = document.getNumberOfPages();		
	    for(int i=0;i<numPdfPages;i++){
	    	
			BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", bos);
			bos.close();
			
			PdfPageAnagModel page = new PdfPageAnagModel();
			page.setPdfId(new StringType(pdf.getPdfId().toString()));
			page.setPdfCode(new StringType(pdf.getPdfCode().toString()));
			page.setPdfPublicationId(new IntegerType(pdf.getPdfPublicationId().intValue()));
			page.setPdfPageNum(new IntegerType(i+1));
			page.setPdfPageImg(new ByteArrayType(bos.toByteArray()));
			result.add(page);
	    }
	    document.close();
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ArrayList<PdfPageAnagModel> createPdfPageImagesPdfBox_2_0_14(PdfAnagModel pdf, byte[] pdfContent) throws Exception{
		
		ArrayList<PdfPageAnagModel> result = new ArrayList<PdfPageAnagModel>();
	    
		InputStream is = new ByteArrayInputStream(pdfContent);
	    
	    Class<?> documentClass = Class.forName("org.apache.pdfbox.pdmodel.PDDocument");
	    Class<?> pdfrendererClass = Class.forName("org.apache.pdfbox.rendering.PDFRenderer");
	    
	    Method documentMethodLoad = documentClass.getDeclaredMethod("load", new Class[]{InputStream.class});
	    Method documentMethodGetNumberOfPages = documentClass.getMethod("getNumberOfPages");
	    Method documentMethodClose = documentClass.getMethod("close");
	    
	    Constructor<?> pdfrendererConstructor = pdfrendererClass.getConstructor(documentClass);
	    Method pdfrendererMethodRenderImageWithDPI = pdfrendererClass.getMethod("renderImageWithDPI", new Class[]{int.class, float.class});
	    
	    Object document = documentMethodLoad.invoke(null, is);
	    Object pdfRenderer = pdfrendererConstructor.newInstance(document);
	    Integer numPdfPages = (Integer)documentMethodGetNumberOfPages.invoke(document);
	    for(int i=0;i<numPdfPages;i++){
			BufferedImage image = (BufferedImage)pdfrendererMethodRenderImageWithDPI.invoke(pdfRenderer, i, 300);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", bos);
			bos.close();
			
			PdfPageAnagModel page = new PdfPageAnagModel();
			page.setPdfId(new StringType(pdf.getPdfId().toString()));
			page.setPdfCode(new StringType(pdf.getPdfCode().toString()));
			page.setPdfPublicationId(new IntegerType(pdf.getPdfPublicationId().intValue()));
			page.setPdfPageNum(new IntegerType(i+1));
			page.setPdfPageImg(new ByteArrayType(bos.toByteArray()));
			result.add(page);
	    }
	    documentMethodClose.invoke(document);
		return result;
	}

}