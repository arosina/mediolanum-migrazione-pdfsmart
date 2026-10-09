package prgm.pdfwebformsutil.drivers.materialeprecontrattuale;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.ExternalLinkOnSignDataModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsutil.drivers.service.materialeprecontrattuale.MaterialePreContrattualeCarrelloService;
import prgm.pdfwebformsutil.drivers.service.materialeprecontrattuale.MaterialePreContrattualeDisposizioneService;

public class MaterialePreContrattuale<T extends PdfBaseDriver>  {
	private T pdfDriver;
	private MaterialePreContrattualeCarrelloService<T> matPrecontrCarrelloService = null;
	private MaterialePreContrattualeDisposizioneService<T> matPrecontrDisposizioneService = null;
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.MaterialeContrattuale";
	
	public MaterialePreContrattuale(T driver) {
		setPdfDriver(driver);
		matPrecontrCarrelloService = new MaterialePreContrattualeCarrelloService<>(driver, true);
		matPrecontrDisposizioneService = new MaterialePreContrattualeDisposizioneService<>(driver, true);
	}
	
	public T getPdfDriver() {
		return pdfDriver;
	}

	public void setPdfDriver(T pdfDriver) {
		this.pdfDriver = pdfDriver;
	}
	
	public void clearCache() {
		matPrecontrCarrelloService.clearCache();
		matPrecontrDisposizioneService.clearCache();
	}
	
	public MaterialePreContrattualeModel readMaterialePrecontrattualeCarrello(ClientSessionContext csc, PdfDataModel pdfData, MaterialePreContrattualeModel model) throws Exception {
		return matPrecontrCarrelloService.read(csc, pdfData, model);		
	}
	
	public MaterialePreContrattualeModel readMaterialePrecontrattualeDisposizione(ClientSessionContext csc, PdfDataModel pdfData, MaterialePreContrattualeModel model) throws Exception {
		return matPrecontrDisposizioneService.read(csc, pdfData, model);
	}
	
	public void recuperaMaterialePrecontrattualeCarrello(ClientSessionContext csc, PdfModel pdfModel, PdfDataModel pdfData)
			throws Exception {
		MaterialePreContrattualeModel matPrecontrModel = new MaterialePreContrattualeModel();
		matPrecontrModel.setCodiceFb(pdfModel.getCodAgeFiltro());			
		matPrecontrModel.setIdProposta(pdfData.getIdCarrello());
		matPrecontrModel.setIdOrdine(pdfData.getIdDispCarrello());
		
		matPrecontrModel = readMaterialePrecontrattualeCarrello(csc, pdfData, matPrecontrModel);
		if (matPrecontrModel != null) {
			ExternalLinkOnSignDataModel externalLinkOnSignData = new ExternalLinkOnSignDataModel();
			externalLinkOnSignData.setExternalLinkOnSignLabel(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeObbligatorio()));
			externalLinkOnSignData.setExternalLinkOnSignUrl(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeObbligatorio()));
			
			externalLinkOnSignData.setExternalLinkOnSignLabelAggiuntivo(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeFacoltativo()));
			externalLinkOnSignData.setExternalLinkOnSignUrlAggiuntivo(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeFacoltativo()));					
			pdfData.setExternalLinkOnSignData(externalLinkOnSignData);
		}
	}
	
	public void recuperaMaterialePrecontrattualeDisposizione(ClientSessionContext csc, PdfDataModel pdfData, String codiceProdotto, String codiceSicav, boolean doubleChance, ListType listaOperazioni)
			throws Exception {	
		MaterialePreContrattualeModel matPrecontrModel = new MaterialePreContrattualeModel();
		matPrecontrModel.setCodiceProdotto(new StringType(codiceProdotto));
		matPrecontrModel.setCodiceSicav(new StringType(codiceSicav));
		matPrecontrModel.setDoubleChance(new BooleanType(doubleChance));
		matPrecontrModel.setTariffa(new StringType());
		matPrecontrModel.setTipoAdesioneFondo(new StringType());
		matPrecontrModel.setListaOperazioni(listaOperazioni);
		recuperaMaterialePrecontrattualeDisposizione(csc, pdfData, matPrecontrModel);
	}
	
	public void recuperaMaterialePrecontrattualeDisposizione(ClientSessionContext csc, PdfDataModel pdfData, MaterialePreContrattualeModel matPrecontrModel)
			throws Exception {		
		
		matPrecontrModel = readMaterialePrecontrattualeDisposizione(csc, pdfData, matPrecontrModel);
		if (matPrecontrModel != null) {
			ExternalLinkOnSignDataModel externalLinkOnSignData = new ExternalLinkOnSignDataModel();			
			externalLinkOnSignData.setExternalLinkOnSignLabel(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeObbligatorio()));
			externalLinkOnSignData.setExternalLinkOnSignUrl(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeObbligatorio()));
			
			externalLinkOnSignData.setExternalLinkOnSignLabelAggiuntivo(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeFacoltativo()));
			externalLinkOnSignData.setExternalLinkOnSignUrlAggiuntivo(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeFacoltativo()));			
			pdfData.setExternalLinkOnSignData(externalLinkOnSignData);
		}
	}
	
	public static void mergeMaterialePrecontrattuale(PdfDataModel pdfData, PdfDataModel otherPdfData) {
		MaterialePreContrattualeModel matPrecontrModel = new MaterialePreContrattualeModel();
		
		if (pdfData.getExternalLinkOnSignData() != null) {			
			String extLinkLabel = pdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabel().toString();
			String extLinkUrl = pdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrl().toString();
			
			addMaterialePrecontrattuale(matPrecontrModel.getMaterialeObbligatorio(), extLinkLabel, extLinkUrl);
			
			extLinkLabel = pdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabelAggiuntivo().toString();
			extLinkUrl = pdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrlAggiuntivo().toString();
			
			addMaterialePrecontrattuale(matPrecontrModel.getMaterialeFacoltativo(), extLinkLabel, extLinkUrl);
		}	
		
		if (otherPdfData.getExternalLinkOnSignData() != null) {
			String otherExtLinkLabel = otherPdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabel().toString();
			String otherExtLinkUrl = otherPdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrl().toString();
			
			addMaterialePrecontrattuale(matPrecontrModel.getMaterialeObbligatorio(), otherExtLinkLabel, otherExtLinkUrl);
			
			otherExtLinkLabel = otherPdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabelAggiuntivo().toString();
			otherExtLinkUrl = otherPdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrlAggiuntivo().toString();
			
			addMaterialePrecontrattuale(matPrecontrModel.getMaterialeFacoltativo(), otherExtLinkLabel, otherExtLinkUrl);
		}						
					
		if (matPrecontrModel.getMaterialeObbligatorio().size() > 0 || matPrecontrModel.getMaterialeFacoltativo().size() > 0) { 
			if (pdfData.getExternalLinkOnSignData() == null) {
				pdfData.setExternalLinkOnSignData(new ExternalLinkOnSignDataModel());
			}
			
			if (matPrecontrModel.getMaterialeObbligatorio().size() > 0) {
				pdfData.getExternalLinkOnSignData().setExternalLinkOnSignLabel(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeObbligatorio()));
				pdfData.getExternalLinkOnSignData().setExternalLinkOnSignUrl(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeObbligatorio()));
			}
			
			if (matPrecontrModel.getMaterialeFacoltativo().size() > 0) {
				pdfData.getExternalLinkOnSignData().setExternalLinkOnSignLabelAggiuntivo(new StringType(matPrecontrModel.formattaDescrizioneMaterialePreContrattualeFacoltativo()));
				pdfData.getExternalLinkOnSignData().setExternalLinkOnSignUrlAggiuntivo(new StringType(matPrecontrModel.formattaLinkMaterialePreContrattualeFacoltativo()));
			}
		}
	}

	public static void addMaterialePrecontrattuale(ListType listMateriale, String extLinkLabel, String extLinkUrl) {
		String[] itemLabels = extLinkLabel.split("\\|");
		String[] itemLinks = extLinkUrl.split("\\|");
		
		for(int i = 0; i < itemLabels.length; i++) {
			String label = itemLabels[i];
			String link = itemLinks[i];			
			DocumentoMaterialePreContrattualeModel docModel = new DocumentoMaterialePreContrattualeModel();
			docModel.setDescrizione(new StringType(label));
			docModel.setLink(new StringType(link));
			
			addLink(listMateriale, docModel);
		}
	}
		
	public static void addLink(ListType listMateriale, DocumentoMaterialePreContrattualeModel linkModel) {	
		if (!linkModel.getLink().isNull() && !isMaterialePrecontrattualeEsistente(listMateriale, linkModel)) {						
			listMateriale.add(linkModel);			
		}
	}
	
	public static boolean isMaterialePrecontrattualeEsistente(ListType materiale, DocumentoMaterialePreContrattualeModel linkSearch) {
		for (int i = 0; i < materiale.size(); i++) {
			DocumentoMaterialePreContrattualeModel linkModel = (DocumentoMaterialePreContrattualeModel) materiale.get(i);
			if (linkModel.getTipoDocumento().equalsIgnoreCase(linkSearch.getTipoDocumento().toString()) && linkModel.getLink().equalsIgnoreCase(linkSearch.getLink().toString()) && linkModel.getDescrizione().equalsIgnoreCase(linkSearch.getDescrizione().toString())) {
				return true;
			}
		}			
		return false;
	}
	
	public ListType recuperaIsinByCodProdotto(ClientSessionContext csc, String formaContrattuale, String listCodProdotto)
			throws DAOException {
		ListType listaOperazioni = new ListType(OperazioneMaterialePreContrattualeModel.class);
		if (!listCodProdotto.isEmpty()) {
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("listaCodiciProdotto", new StringType(listCodProdotto));
			input.addProperty("formaContrattuale", new StringType(formaContrattuale));		
			DAOQueryResultModel qRes = new DAOObject(csc, DAO_FILE_NAME).executeQueryAccess("loadIsin", input);
			for (int i = 0; i < qRes.getResult().size(); i++) {
				MapCommandDataModel map = (MapCommandDataModel)qRes.getResult().get(i);
				StringType isin = (StringType)map.readProperty("isin");
				
				OperazioneMaterialePreContrattualeModel opModel = new OperazioneMaterialePreContrattualeModel();
				opModel.setIsin(isin);
				listaOperazioni.add(opModel);
			}
		}
		return listaOperazioni;
	}
}
