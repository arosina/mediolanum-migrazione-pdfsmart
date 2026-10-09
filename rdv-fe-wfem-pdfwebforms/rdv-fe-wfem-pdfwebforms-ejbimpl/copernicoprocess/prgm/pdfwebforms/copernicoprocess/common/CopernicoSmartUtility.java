package prgm.pdfwebforms.copernicoprocess.common;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCallData;

import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.model.PdfPersonSignDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CopernicoSmartUtility {
	
	private static String DAO_XML = "PdfWebForms.PdfSignProcess";
	
	public static String CELLULARE_KO_SERVIZIO				= "K";
	public static String CELLULARE_OK 						= "C";

	public static final String 	FIRMA_DIGITALE_ATTIVA	 	= "A";
	public static final String 	FIRMA_DIGITALE_DISATTIVA	= "D";
	public static final String 	FIRMA_DIGITALE_SOSPESA	 	= "S";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ArrayList<String> loadDatiFirmaDigitalePersonaPerInvioCopernico(ClientSessionContext csc, PdfPersonModel persona, int count){

		 ArrayList<String> errors = new  ArrayList<String>();
		
		try{
			
			if(persona.readCodiceFiscale().length() == 0){
				errors.add("Per "+persona.readCognomeNome()+" non risulta valorizzato il codice fiscale");
				return errors;
			}
			
			DAOObject dao = new DAOObject(csc,DAO_XML);			
			PdfPersonSignDataModel signData = persona.getSignData();
			
			// Cellulare
			signData.setStatoCelluarePrimario(new StringType());
			signData.setNumeroCelluarePrimario(new StringType());
			signData.setPrefissoCellulareAnag(new StringType());
			signData.setNumeroCellulareAnag(new StringType());
			DAOQASResultModel qasRes = dao.executeQASAccess("loadStatoCellulare",persona);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				signData.setStatoCelluarePrimario(new StringType(CELLULARE_KO_SERVIZIO));
				errors.add("Errore di sistema nel recuperare la coerenza del cellulare di "+persona.readCognomeNome());
			}else{
				if(!signData.getStatoCelluarePrimario().equals(CELLULARE_OK)){
					errors.add("Contattare Banking Center e modificare numero di cellulare di "+persona.readCognomeNome());
				}else{
					dao.executeQueryAccess("loadCellulareAnagEffettivo", persona);
				}
			}
			
			if(persona.getSignData().getNumeroCelluarePrimario().isNull()){
				errors.add("Per "+persona.readCognomeNome()+" non risulta valorizzato il numero di cellulare");
			}
			
			// Stato firma
			qasRes = dao.executeQASAccess("readStatoFirmaDigitale",persona);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				
				errors.add("Errore di servizio nel recuperare lo stato della firma digitale per "+persona.readCognomeNome()+": "+qasRes.getQasCallData().getMessage());
				
			}else{
				
				if(!"OK".equals(signData.getEsitoChiamataServizio().toString())){
					
					errors.add("KO dal servizio di verifica stato firma digitale per "+persona.readCognomeNome());
					
				}else if(FIRMA_DIGITALE_SOSPESA.equals(signData.getStatoFirmaDigitale().toString())){
					
					errors.add(MifidCaller.WARNING_INDICATOR+persona.readCognomeNome()+" ha sospeso la firma digitale");
					
				}else if("B".equals(signData.getStatoFirmaDigitale().toString())){
					
					errors.add(MifidCaller.WARNING_INDICATOR+"Per "+persona.readCognomeNome()+" la firma digitale è bloccata");
					
				}else if(FIRMA_DIGITALE_DISATTIVA.equals(signData.getStatoFirmaDigitale().toString())){
					
					errors.add(MifidCaller.WARNING_INDICATOR+"Il cliente "+persona.readCognomeNome()+" non risulta abilitato alla firma digitale, necessaria per accettare questa proposta, se decidi di proseguire accertati che proceda all'abilitazione");
					
				}else if(FIRMA_DIGITALE_ATTIVA.equals(signData.getStatoFirmaDigitale().toString())){
					
					if(signData.getIdCertificationAuthority().isNull()){
						errors.add("Per "+persona.readCognomeNome()+" non risulta l'identificativo della CA");
					}
					
				}
			}

			// Banca diretta
			String errBD = hasSoggettoContiBancaDiretta(csc, persona.getNdg());
			if(errBD != null)
				errors.add(errBD);
			
			String errCODICI = verificaCodici(csc, persona.getNdg(), persona.readCognomeNome());
			if(errCODICI != null)
				errors.add(errCODICI);
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			errors.add("Errore DAO nel recuperare le informazioni relative alla firma digitale per "+persona.readCognomeNome()+": "+daoe.toString());
		}
		return errors;
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
				return "Il servizio di firma digitale non può essere utilizzato da "+cognomeNome+" in quanto non attivo il servizio di Banca Diretta";
			else
				return "Il servizio di firma digitale non può essere utilizzato da "+cognomeNome+" in quanto il suo primo codice segreto è in stato di blocco";
			
		}catch(DAOException daoe){
			return "Errore DAO nel verificare i codici di "+cognomeNome+" per banca diretta: "+daoe.toString();
		}
	}
	
}
