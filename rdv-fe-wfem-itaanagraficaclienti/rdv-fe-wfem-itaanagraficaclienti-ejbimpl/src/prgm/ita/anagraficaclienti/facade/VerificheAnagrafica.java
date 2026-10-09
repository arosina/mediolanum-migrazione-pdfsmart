package prgm.ita.anagraficaclienti.facade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.model.BancaModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.ComuneModel;
import prgm.ita.anagraficaclienti.model.VerificaCodicePromoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VerificheAnagrafica{

	private static final String THISCLASSNAME = "VerificheAnagrafica";
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private VerificheAnagrafica() {
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaComune(ClientSessionContext csc, ComuneModel model, ClienteModel cliente) throws AnagraficaClientiException{
		return innerVerificaComune(csc,model,cliente,false,false,false);
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaComuneScaduto(ClientSessionContext csc, ComuneModel model, ClienteModel cliente) throws AnagraficaClientiException{
		return innerVerificaComune(csc,model,cliente,true,true,false);
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaComuneDocumentoItaliano(ClientSessionContext csc, ComuneModel model, ClienteModel cliente) throws AnagraficaClientiException{
		return innerVerificaComune(csc,model,cliente,true,false,true);
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaComuneDocumento(ClientSessionContext csc, ComuneModel model, ClienteModel cliente) throws AnagraficaClientiException{
		return innerVerificaComune(csc,model,cliente,true,false,false);
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaProvincia(ClientSessionContext csc, StringType provincia) throws AnagraficaClientiException{
		try{
			
			if(provincia.isNull())
				return true;
			
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			ComuneModel input = new ComuneModel();
			input.setProvincia(provincia);
			DateType scadenzaProvincia = (DateType)dao.executeQueryAccess("verificaProvincia", input).getSingleResult();
			if(scadenzaProvincia == null || !scadenzaProvincia.isNull()) {
				provincia.addTypeError("err.provinciaInesistente");
				return false;
			}
			return true;
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare la provincia: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare la provincia: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}		
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private static boolean innerVerificaComune(ClientSessionContext csc, ComuneModel model, ClienteModel cliente, 
											   boolean gestisciScadenzaComune, boolean gestisciScadenzaProvincia, 
											   boolean messaggioComuneEstero) throws AnagraficaClientiException{
		
		try{
			
			if(!model.getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)){
				model.setComune(model.getComuneEstero());
				model.setCodComune(new StringType());
				model.setProvincia(new StringType("EE"));
				return true;
			}
			
			if(model.getComune().isNull())
			   return true;
			
			boolean result = true;
			
			StringType savComune = model.getComune();
			StringType savCap = model.getCap();
			StringType savProvincia = model.getProvincia();

			String accessName = "verificaComune";
			if(model.getIsIscrittoAlCatasto().booleanValue())
				accessName = "verificaComuneIscrittoAlCatasto";

			ComuneModel comuneTrovato = null;
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			DAOQueryResultModel queryResult = dao.executeQueryAccess(accessName,model);
			if(queryResult.getResult().size() == 1){
				
				comuneTrovato = (ComuneModel)queryResult.getResult().get(0);
				if(gestisciScadenzaProvincia && !comuneTrovato.getScadenzaProvincia().isNull() && !comuneTrovato.getProvincia().equals("EE")){
					model.setProvincia(new StringType());
					queryResult = dao.executeQueryAccess(accessName,model);
					if(queryResult.getResult().size() == 1){
						comuneTrovato = (ComuneModel)queryResult.getResult().get(0);
						cliente.setAlmenoUnaProvinciaScaduta(true);
					}else{
						if(model.getIsIscrittoAlCatasto().booleanValue())
							savComune.addTypeError(new TypeError("err.comuneNonIscrittoAlCatasto"));
						else
							savComune.addTypeError(new TypeError("err.comuneInesistente"));
						result = false;						
					}
				}
				
				if((!comuneTrovato.getScadenzaComune().isNull() && gestisciScadenzaComune)){
					savComune.addTypeError(new TypeError("err.comuneScaduto"));
					result = false;	
				}
				
			}else{
				
				if(model.getIsIscrittoAlCatasto().booleanValue()){
					savComune.addTypeError(new TypeError("err.comuneNonIscrittoAlCatasto"));
				}else{
					if(messaggioComuneEstero)
						savComune.addTypeWarning("war.localitaNonItaliana");
					else
						savComune.addTypeError(new TypeError("err.comuneInesistente"));
				}				
				result = false;
				
			}
			
			if(result){			
				model.setComune(comuneTrovato.getComune());
				model.setCap(comuneTrovato.getCap());
				model.setCodComune(comuneTrovato.getCodComune());
				model.setProvincia(comuneTrovato.getProvincia());
			}else{
				model.setCodComune(new StringType());
				model.setComune(savComune);
				model.setCap(savCap);
				model.setProvincia(savProvincia);				
			}
			return result;
			
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare il comune: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare il comune: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaBanca(ClientSessionContext csc, BancaModel model) throws AnagraficaClientiException{

		try{
			
			boolean result = true;
			
			if(model.getAbi().isNull() &&
			   model.getCab().isNull()){
				return result;
			}
			
			BancaModel bancaTrovata = null;
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			DAOQueryResultModel queryResult = dao.executeQueryAccess("verificaBanca",model);
			if(queryResult.getResult().size() == 1){
				bancaTrovata = (BancaModel)queryResult.getResult().get(0);
			}else if(queryResult.getResult().size() > 1){
				model.getAbi().addTypeError(new TypeError("err.bancaNonUnivoca"));
				result = false;
			}else{
				model.getAbi().addTypeError(new TypeError("err.bancaInesistente"));
				result = false;
			}
			
			if(result){
				model.setAbi(bancaTrovata.getAbi());
				model.setCab(bancaTrovata.getCab());				
				model.setSportello(bancaTrovata.getSportello());
				model.setRagioneSociale(bancaTrovata.getRagioneSociale());
				model.setProvincia(bancaTrovata.getProvincia());
				model.setLocalita(bancaTrovata.getLocalita());
				model.setIndirizzo(bancaTrovata.getIndirizzo());
			}
			return result;
			
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare la banca: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare la banca: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaPresentatoreIMF(ClientSessionContext csc, ClienteKeyModel segnalatore) throws AnagraficaClientiException{

		try{
			
			boolean result = true;
			
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			if (segnalatore.getCodMediolanum().isNull()){
				//Verifica il Potenziale
				DateType ultimi30gg = Tools.today();
				ultimi30gg.addDays(-30);
				DAOQueryResultModel queryResult = dao.executeQueryAccess("selezionaDataInvioContoDepositoPotenziale",segnalatore);
				DateType dataInvioCD = (DateType)queryResult.getSingleResult();
				if (dataInvioCD.compareTo(ultimi30gg) < 0) {
					result = false;
				}
			}else{
				//Verifica l'effettivo
				//Conto deposito attivo portafoglio
				boolean intestCointINR = false;
				DAOQueryResultModel queryResult = dao.executeQueryAccess("verificaPresentatoreIntestCointestContoDepositoAttivoINR",segnalatore);
				if (!queryResult.getSingleResult().isNull()){
					intestCointINR = ((BooleanType)queryResult.getSingleResult()).booleanValue();
				}
				if(!intestCointINR) {
					//Conto deposito contratti elettronici
					queryResult = dao.executeQueryAccess("verificaPresentatoreIntestCointestContoDepositoCEPE",segnalatore);
					IntegerType contiDepositoPresentatore = (IntegerType)queryResult.getSingleResult();
					if(contiDepositoPresentatore.intValue() < 1) {
						result = false;
					}
				}
			}
			return result;
			
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare il Presentatore per l'iniziativa IMF: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare il Presentatore per l'iniziativa IMF: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaCodPromo(ClientSessionContext csc, StringType codicePromo) throws AnagraficaClientiException{

		try{
			
			boolean result = true;
			
			String country = csc.getCountryCode();
			String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
			xmlName += "AnagraficaClienti.VERIFICA_CODPROMO";

			VerificaCodicePromoModel verificaCodicePromo = new VerificaCodicePromoModel();
			verificaCodicePromo.setCodicePromo(new StringType(codicePromo.getStringValue()));
			
			DAOObject dao = new DAOObject(csc,xmlName);
			DAOQASResultModel qRes = dao.executeQASAccess("QARC_VERFICA_CODPROMO",verificaCodicePromo);
			if (qRes.getQasCallData().getStatus() != QASCallData.STATUS_OK) {
				result = false;
			}
			if (!verificaCodicePromo.getFlagCodiceValido().booleanValue())
				result = false;
			
			return result;
			
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare il codice promozionale per l'iniziativa MGM: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare il codice promozionale per l'iniziativa MGM: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static boolean verificaSeClienteIsAgente(ClientSessionContext csc, StringType codiceCliente) throws AnagraficaClientiException{

		try{
			
			boolean result = true;
			
			String country = csc.getCountryCode();
			String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
			xmlName += "AnagraficaClienti.AnagraficaClienti";

			ClienteKeyModel cliente = new ClienteKeyModel();
			cliente.setCodMediolanum(new StringType(codiceCliente.toString()));
			
			DAOObject dao = new DAOObject(csc,xmlName);
			DAOQueryResultModel qRes = dao.executeQueryAccess("clienteSegnalatoreFB",cliente);
			StringType codiceAgente = (StringType) qRes.getSingleResult();
			if(codiceAgente == null || codiceAgente.isNull())
				result = false;
			
			return result;
			
		}catch(DAOException daoe){
			String errorMsg = THISCLASSNAME+" - Eccezione DAO nel verificare se il clietne è un agente: "+daoe;
			throw new AnagraficaClientiException(errorMsg);
		}catch(Exception e){
			String errorMsg = THISCLASSNAME+" - Eccezione nel verificare se il clietne è un agente: "+e;
			throw new AnagraficaClientiException(errorMsg);
		}
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String allXmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		allXmlName += "AnagraficaClienti.Verifiche";
	  	return allXmlName;
	}	

	
}
