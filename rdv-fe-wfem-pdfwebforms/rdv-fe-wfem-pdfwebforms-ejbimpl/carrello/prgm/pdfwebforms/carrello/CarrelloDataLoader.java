package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CarrelloDataLoader {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static DispositivaCarrelloModel loadCarrello(ClientSessionContext csc, PdfDataModel pdfData) throws Exception{
		try{

			// Il nuovo carrello "semplt" imposta nella dispositiva anche il codice di ciascun modulo 
			if(!pdfData.getIdDispModuloCarrello().isNull())	
				return loadCampiCarrelloSemplt(csc, pdfData);
			
			// Carrello 5D
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfCarrello");
			
			DispositivaCarrelloModel dispositivaCarrello = new DispositivaCarrelloModel();
			dispositivaCarrello.setIdCarrello(pdfData.getIdCarrello());
			dispositivaCarrello.setIdDispCarrello(pdfData.getIdDispCarrello());
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadDispositivaCarrello", dispositivaCarrello);
			if(qRes.getResult().size() < 1){
				throw new Exception("Dispositiva "+pdfData.getIdDispCarrello()+" non trovata nel carrello "+pdfData.getIdCarrello());
			}
			
			ListType sottoscrittori = dao.executeQueryAccess("loadSottoscrittoriCarrello", pdfData).getResult();
			if(sottoscrittori.size() == 0){
				throw new Exception("Nessun sottoscrittore per la dispositiva "+pdfData.getIdDispCarrello()+" nel carrello "+pdfData.getIdCarrello());
			}
			
			pdfData.startPdfInitialInputDataInitialization();
			int idx = 0;
			for(int i=0;i<sottoscrittori.size();i++){
				SottoscrittoreCarrelloModel sottoscrittore = (SottoscrittoreCarrelloModel)sottoscrittori.get(i);
				// Il tipo variazione indica il tipo di variazione che andrà effettuata sul cointestatario del contratto (INS=Inserimento/CAN=Cancellazione)
				// Quando impostato la gestione del popolamento dei campi sul pdf viene delegata al driver in quanto specifica
				if (sottoscrittore.getTipoVariazione().isNull()) {
					idx++;
					pdfData.write("ndgCliente"+idx, sottoscrittore.getCodiceCliente());
				}
			}
			pdfData.stopPdfInitialInputDataInitialization();
			dispositivaCarrello.setSottoscrittori(sottoscrittori);
			
			dispositivaCarrello.setFondi(dao.executeQueryAccess("loadFondiDispositivaCarrello", dispositivaCarrello).getResult());
			
			// Carico i dati della eventuale collegata (switch)
			if(!dispositivaCarrello.getIdDispCollegataCarrello().isNull()){
				DispositivaCarrelloModel dispositivaCarrelloCollegata = new DispositivaCarrelloModel();
				dispositivaCarrelloCollegata.setIdCarrello(pdfData.getIdCarrello());
				dispositivaCarrelloCollegata.setIdDispCarrello(new IntegerType(dispositivaCarrello.getIdDispCollegataCarrello().intValue()));
				qRes = dao.executeQueryAccess("loadDispositivaCarrello", dispositivaCarrelloCollegata);
				if(qRes.getResult().size() < 1){
					throw new Exception("Dispositiva collegata "+dispositivaCarrelloCollegata.getIdDispCarrello()+" non trovata nel carrello "+pdfData.getIdCarrello());
				}
				dispositivaCarrelloCollegata.setSottoscrittori(sottoscrittori);
				dispositivaCarrelloCollegata.setFondi(dao.executeQueryAccess("loadFondiDispositivaCarrello", dispositivaCarrelloCollegata).getResult());
				dispositivaCarrello.setDispositivaCarrelloCollegata(dispositivaCarrelloCollegata);
			}
			
			return dispositivaCarrello;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static DispositivaCarrelloModel loadCampiCarrelloSemplt(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		
		pdfData.startPdfInitialInputDataInitialization();
		ListType elencoCampiModulo = new DAOObject(csc, "PdfWebForms.PdfBasketSemplt").executeQueryAccess("loadCampiModuloDispoBasket", pdfData).getResult();
		for(int i=0;i<elencoCampiModulo.size();i++) {
			CampoDispositivaCarrelloModel c = (CampoDispositivaCarrelloModel)elencoCampiModulo.get(i);
			if(!initModelProperty(pdfData, c))
				pdfData.write(c.getFieldName().toString(), c.getFieldValue());
		}
		pdfData.stopPdfInitialInputDataInitialization();

		DispositivaCarrelloModel dispositivaCarrello = new DispositivaCarrelloModel();
		dispositivaCarrello.setIdCarrello(pdfData.getIdCarrello());
		dispositivaCarrello.setIdDispCarrello(pdfData.getIdDispCarrello());
		return dispositivaCarrello;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean initModelProperty(PdfDataModel pdfData, CampoDispositivaCarrelloModel c) {
		try {
			if(Tools.getPropertyType(pdfData, c.getFieldName().toString()) != null) {
				AbstractType prop = (AbstractType)Tools.getPropertyValue(pdfData, c.getFieldName().toString());
				if(prop != null) {
					prop.setStringValue(c.getFieldValue().toString());
					pdfData.getPdfInitialInputDataArray().add(c.getFieldName().toString());
					return true;
				}				
			}		
		}catch(Exception e){ /* Do nothing */ }
		return false;
	}
}
