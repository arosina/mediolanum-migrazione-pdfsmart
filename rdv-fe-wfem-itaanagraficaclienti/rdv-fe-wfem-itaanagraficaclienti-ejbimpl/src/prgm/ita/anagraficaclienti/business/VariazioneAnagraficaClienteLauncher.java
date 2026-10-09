package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.display.ErroriOnStartUp;
import prgm.ita.anagraficaclienti.display.VariazioneAnagraficaClienteDispatcher;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;

/*******************************************************************/
/*******************************************************************/
public class VariazioneAnagraficaClienteLauncher extends BusinessCommand implements MenuCommand{

	private static final long serialVersionUID = 1L;
	
	private static final String S_STATOCELLUAREPRIMARIO = "statoCelluarePrimario";
	private static final String S_STATOFIRMADIGITALE = "statoFirmaDigitale";
	private static final String S_HABANCADIRETTA = "haBancaDiretta";
	private static final String S_NATURAGIURIDICA = "naturaGiuridica";
	private static final String S_SESSO = "sesso";
	private static final String S_ESITOSTATOFIRMADIGITALE = "esitoStatoFirmaDigitale";
	private static final String S_IDCERTIFICATIONAUTHORITYFIRMADIGITALE = "idCertificationAuthorityFirmaDigitale";
	private static final String S_OLOGRAFA = "OLOGRAFA";	
	private static final String S_SRVERRORS = "srvErrors";
		
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		
		try {

			if(chiave.getCodMediolanum().isNull())
				return forwardToErroriOnStartUp(chiave, "Nessun ndg ricevuto in input");
			
			if(chiave.getCodMediolanum().toString().startsWith("S")){
				chiave.setCodPotenziale(new StringType(chiave.getCodMediolanum().toString()));
				chiave.setCodMediolanum(new StringType());
				setNextCommandClass(ApriAnagraficaCliente.class);
				return chiave;
			}
			
			DAOObject dao = new DAOObject(csc, "ItaAnagraficaClienti.VariazioneAnagraficaClienteLauncher");			
			
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadDatiCliente", chiave);			
			if(qRes.getResult().size() == 0)
				return forwardToErroriOnStartUp(chiave, "Variazione anagrafica: il cliente con ndg "+chiave.getCodMediolanum()+" non è stato trovato");

			MapCommandDataModel cliData = (MapCommandDataModel)qRes.getResult().get(0);
			initMapData(cliData);

			String layout = "";
			String modalita = "";
			if(isPersonaGiuridica(cliData)){
				layout = "GIURIDICA";
			}else if(isDitta(cliData)){
				modalita = S_OLOGRAFA;
			}else if(isPersonaFisica(cliData)){
				layout = "FISICA";
				modalita = modalitaPersonaFisica(csc, dao, cliData);
			}else {
				return forwardToErroriOnStartUp(chiave, "Variazione anagrafica: cliente di natura sconosiuta");
			}
			
			StringType srvErrors = (StringType)cliData.readProperty(S_SRVERRORS);
			if(srvErrors.isNull() && modalita.equals(S_OLOGRAFA)){ // Solo firma olografa
				ClienteKeyModel key = new ClienteKeyModel();
				key.setCodMediolanum(chiave.getCodMediolanum());
				setNextCommandClass(ApriAnagraficaCliente.class);
				return key;
			}
			
			cliData.addProperty("layout", new StringType(layout));
			cliData.addProperty("modalita", new StringType(modalita));
			
			setNextCommandClass(VariazioneAnagraficaClienteDispatcher.class);
			return cliData;
			
		} catch(DAOException daoe) {
			LOG.error(daoe);
			String errorMsg = getClass() + " Eccezione DAO nel lanciare la variazione anagrafica codMediolanum=["+chiave.getCodMediolanum()+"]: " + daoe;
			throw new CommandException(errorMsg);
		} catch(Exception e) {
			LOG.error(e);
			String errorMsg = getClass() + " Eccezione nel lanciare la variazione anagrafica codMediolanum=["+chiave.getCodMediolanum()+"]: " + e;
			throw new CommandException(errorMsg);
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	private CommandDataModel forwardToErroriOnStartUp(CommandDataModel model, String commandError) {
		model.addCommandError(commandError);
		setNextCommandClass(ErroriOnStartUp.class);
		return model;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	private void initMapData(MapCommandDataModel m) {
		m.addProperty(S_STATOCELLUAREPRIMARIO, new StringType());
		m.addProperty(S_ESITOSTATOFIRMADIGITALE, new StringType());
		m.addProperty(S_STATOFIRMADIGITALE, new StringType());
		m.addProperty(S_IDCERTIFICATIONAUTHORITYFIRMADIGITALE, new StringType());
		m.addProperty(S_HABANCADIRETTA, new StringType());
		m.addProperty(S_SRVERRORS, new StringType());
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private boolean isPersonaGiuridica(MapCommandDataModel cliData) {
		StringType sesso = (StringType)cliData.readProperty(S_SESSO);
		return !sesso.equals(Costanti.SESSO_MASCHIO) && !sesso.equals(Costanti.SESSO_FEMMINA); 
	}

	/*******************************************************************/
	/*******************************************************************/
	private boolean isPersonaFisica(MapCommandDataModel cliData) {
		StringType naturaGiuridica = (StringType)cliData.readProperty(S_NATURAGIURIDICA);
		return naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_PERSONA_MASCHIO) || naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_PERSONA_FEMMINA); 
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private boolean isDitta(MapCommandDataModel cliData) {
		StringType naturaGiuridica = (StringType)cliData.readProperty(S_NATURAGIURIDICA);
		return naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_DITTA_MASCHIO) || naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_DITTA_FEMMINA); 
	}

	/*******************************************************************/
	/*******************************************************************/
	private String modalitaPersonaFisica(ClientSessionContext csc, DAOObject dao, MapCommandDataModel cliData) throws DAOException {
		StringBuilder srvErrors = new StringBuilder();
		MapCommandDataModel ageData = new MapCommandDataModel();
		ageData.addProperty("codAgente", new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
		initMapData(ageData);
		
		dao.executeQueryAccess("loadNdgAgente", ageData);
		StringType ndgAgente = (StringType)ageData.readProperty("codMediolanum");
		if(ndgAgente != null && !ndgAgente.isNull()){
			if(!callLoadStatoCellulare(dao, ageData))
				srvErrors.append("00001,");
			if(!callReadStatoFirmaDigitale(dao, ageData))
				srvErrors.append("00002,");
		}else{
			srvErrors.append("00000,");
		}
		
		dao.executeQueryAccess("loadHaBancaDiretta", cliData);
		if(!callLoadStatoCellulare(dao, cliData))
			srvErrors.append("00003,");
		if(!callReadStatoFirmaDigitale(dao, cliData))
			srvErrors.append("00004,");
		
		String modalita = S_OLOGRAFA;		
		if(srvErrors.length() > 0) {
			cliData.addProperty(S_SRVERRORS, new StringType(srvErrors.substring(0, srvErrors.length()-1)));
		}else{
			if(isFirmaAttivabile(ageData, cliData))
				modalita += "&DIGITALE";
			if(isCopernicoAttivabile(csc, dao, cliData))
				modalita += "&COPERNICO";
		}
		return modalita;
	}

	/*******************************************************************/
	/*******************************************************************/
	private boolean callLoadStatoCellulare(DAOObject dao, MapCommandDataModel m){
		try{
			DAOQASResultModel qasRes = dao.executeQASAccess("loadStatoCellulare",m);
			return qasRes.getQasCallData().getStatus() == QASCallData.STATUS_OK;
		}catch(DAOException daoe){
			return false;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	private boolean callReadStatoFirmaDigitale(DAOObject dao, MapCommandDataModel m){
		try{
			DAOQASResultModel qasRes = dao.executeQASAccess("readStatoFirmaDigitale",m);
			return qasRes.getQasCallData().getStatus() == QASCallData.STATUS_OK && m.readProperty(S_ESITOSTATOFIRMADIGITALE).equals("OK");
		}catch(DAOException daoe){
			return false;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private boolean isFirmaAttivabile(MapCommandDataModel ageData, MapCommandDataModel cliData) {
		return  ageData.readProperty(S_STATOCELLUAREPRIMARIO).equals("C") 	&& 
				ageData.readProperty(S_STATOFIRMADIGITALE).equals("A") 		&& 
				cliData.readProperty(S_HABANCADIRETTA).equals("S") 			&& 
				cliData.readProperty(S_STATOCELLUAREPRIMARIO).equals("C") 	&& 
				cliData.readProperty(S_STATOFIRMADIGITALE).equals("A");
	}

	/*******************************************************************/
	/*******************************************************************/
	private boolean isCopernicoAttivabile(ClientSessionContext csc, DAOObject dao, MapCommandDataModel cliData) {
		cliData.addProperty("isClienteAgente", new StringType());
		try{dao.executeQueryAccess("loadIsClienteAgente", cliData);}catch(DAOException daoe) { /* do nothing */ }
		return  cliData.readProperty(S_HABANCADIRETTA).equals("S") && 
				cliData.readProperty(S_STATOCELLUAREPRIMARIO).equals("C") &&
				!cliData.readProperty("codAgenteAssegnatario").toString().equals("0000000000") &&	// Aggiunto rispetto alla rfc #126123 con la rfc #150718
				cliData.readProperty("codAgenteAssegnatario").toString().equals(csc.getCurrentLinkedUserCode()) &&	// Cogestione #157558
				!cliData.readProperty("isClienteAgente").toString().equals("S"); 					// Aggiunto rispetto alla rfc #126123 con la rfc #150718
	}
		
}
