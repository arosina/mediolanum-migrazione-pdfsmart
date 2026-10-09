package prgm.ita.anagraficaclienti.facade;

import java.util.Calendar;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dbjavaclasses.DBJavaClassLoader;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeWarning;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.model.AdempimentiNormativiModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DocumentoModel;
import prgm.ita.anagraficaclienti.model.DomicilioModel;
import prgm.ita.anagraficaclienti.model.InfoDittaModel;
import prgm.ita.anagraficaclienti.model.InfoPersonaliModel;
import prgm.ita.anagraficaclienti.model.ResidenzaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliAnagraficaSalvataggio implements Controlli{

    protected static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	
    private static final String DAO_XML_VERIFICHE = "ItaAnagraficaClienti.Verifiche";
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		/* *************************************************** */
		/* GESTIONE DEL CONTROLLO SULLA VALIDITA DELLE TENDINE */
		/* *************************************************** */
		ClienteModel clienteOriginale = model.getDatiApplicativi().getClienteOriginale(); 
		model.getDatiApplicativi().setClienteOriginale(null);

		ClienteModel clienteCaricato = model.getDatiApplicativi().getClienteCaricato(); 
		model.getDatiApplicativi().setClienteCaricato(null);
		
		ClienteModel clienteTitolare = model.getClienteTitolare(); 
		model.setClienteTitolare(null);
		
		// Nelle ditte questi campi non sono visibili e non vanno controllati
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();
		if(model.getIsDitta().booleanValue()){
			infoPersonali.getCodProfessione().setVisible(false);
		}

		// Annullo i campi per i quali non deve essere effattuato il controllo di validità
		// poichè legati a dati storici
		StringType savCodNazioneNascita = model.getComuneNascita().getCodNazione();
		if(model.getIsEffettivo().booleanValue())
			model.getComuneNascita().setCodNazione(new StringType());
		
		/* *********     CONTROLLO TENDINE           ********* */
		Tools.checkCodDescFieldsValidity(model);
		/* *************************************************** */

		/* *************************************************** */
		// Ripristino i dati salvati
		/* *************************************************** */
		model.getDatiApplicativi().setClienteOriginale(clienteOriginale);
		model.getDatiApplicativi().setClienteCaricato(clienteCaricato);
		model.setClienteTitolare(clienteTitolare);
		model.setInfoPersonali(infoPersonali);
		model.getComuneNascita().setCodNazione(savCodNazioneNascita);
		
		infoPersonali.getCodProfessione().setVisible(true);
		/* *************************************************** */
		/* *************************************************** */
		/* *************************************************** */
		
		try{
			
			// Per messaggio generale sulle province scadute
			model.setAlmenoUnaProvinciaScaduta(false);

			// Datore di lavoro
			if(model.getIsDatoreDiLavoro().booleanValue()){
				if(model.getCognome().isNull())
					model.getCognome().addTypeError("err.ragioneSocialeObbl");
				
				if(!model.getPartitaIva().isNull() && !model.getPartitaIva().isSkippable()){
					isSocietaDelPromotore(csc,model);
				}
				
				controllaEmail(model.getRecapiti().getEmail());
				
				ResidenzaModel residenza = model.getResidenza();
				VerificheAnagrafica.verificaComuneScaduto(csc,residenza.getIndirizzo(),model);
				return;
			}
			
			// Dati cogesione
			controllaCogestione(csc, model);
			
			// Dati minimi
			controllaDatiMinimi(csc,model);
			
			// Dati generali			
			controllaDatiGenerali(csc,model);
			
			// Residenza
			controllaResidenza(csc,model);
			
			// Domicilio
			controllaDomicilio(csc,model);
			
			// Residenza diverso da domicilio? (solo se "presso" nel domicilio non valorizzato)
			String nomeCognome = model.getNome().toString()+model.getCognome().toString();
			String cognomeNome = model.getCognome().toString()+model.getNome().toString();
			String trimPresso = model.getDomicilio().getIndirizzo().getPresso().toString().replaceAll("\\s", "");
			boolean pressoUgualeNome = trimPresso.equalsIgnoreCase(nomeCognome) || trimPresso.equalsIgnoreCase(cognomeNome);
			if((model.getDomicilio().getIndirizzo().getPresso().isNull() || pressoUgualeNome) &&
			   !Tools.containsTypeErrors(model.getResidenza().getIndirizzo()) &&
			   !Tools.containsTypeErrors(model.getDomicilio().getIndirizzo()) &&
			   !model.getResidenza().getIndirizzo().isEmpty() &&
			   !model.getDomicilio().getIndirizzo().isEmpty() &&
			    model.getDomicilio().getIndirizzo().isEqual(model.getResidenza().getIndirizzo())) {
				model.getDomicilio().getIndirizzo().getDescrizioneIndirizzo().addTypeError("err.domicilioUgualeResidenza");				
			}
			
			// Recapiti
			controllaRecapiti(csc, model);
			
			// Info personali
			controllaInfoPersonali(model);
			
			// Info ditta
			if(model.getIsDitta().booleanValue())
				controllaInfoDitta(csc,model);
			
			// Adempimenti normativi
			controllaAdempimentiNormativi(model);
			
			// Impostazione del campo origine per i vari cantieri (CLI_C_CLIE_SEGN con i dati relativi al cantiere/origine)
			controllaImpostaOrigine(csc,model);
			
			// Documento (Solo se non derivato da un cliente quando ditta)
			if(!model.getIsDitta().booleanValue() || model.getClienteTitolare() == null)
				controllaDocumento(csc,model);				
			
			// Privacy
			controllaDatiPrivacy(csc,model);

			return;
			
		}catch(Exception e){
			String errorMsg = "Eccezione: ["+e+"] nel controllo dell'anagrafica al salvataggio";
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/***********************************************************************************************/
	// Attenzione: usato anche nei censimenti light (HomepageTools)
	/***********************************************************************************************/
	public static void controllaCogestione(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{
		if(model.getIsEffettivo().booleanValue()) {
			model.setIsClienteInCogestione(new BooleanType(false));
			return;
		}
		
		if(model.getIsClienteInCogestione().booleanValue()) {
			
			CogestioneDataModel cogestioneData = model.getCogestioneData();
			
			if(!cogestioneData.getIsUtenteCogestore().booleanValue())
				model.getCodFiscale().addTypeError("err.cogestioneUtenteNonBC");
			
			if(cogestioneData.getIsUtenteCogestore().booleanValue() && !cogestioneData.isContrattoCogestioneAttivo())
				model.getCodFiscale().addTypeError("err.cogestioneContrattoNonAttivo");
			
			if(!model.getCodFiscale().isNull()) {
				try {
					String daoAccessName = "verificaSeFalsoProspect";
					if(model.getIsDitta().booleanValue())
						daoAccessName = "verificaSeFalsoProspectDitta";
					StringType codEffettivo = (StringType)new DAOObject(csc, DAO_XML_VERIFICHE).executeQueryAccess(daoAccessName, model).getSingleResult();
					if(codEffettivo != null && !codEffettivo.isNull()) {
						if(model.getIsDitta().booleanValue())
							model.getPartitaIva().addTypeError("err.cogestioneSuFalsoProspectDitta");
						else
							model.getCodFiscale().addTypeError("err.cogestioneSuFalsoProspect");
					}
				} catch(DAOException de) {
					String errorMsg = "Eccezione DAO: ["+ de +"] nel controllo della cogestione";
					throw new AnagraficaClientiException(errorMsg);				
				}
			}
	
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDatiMinimi(ClientSessionContext csc, ClienteModel model) throws Exception{

		// Tipo persona obbligatorio			
		if(model.getTipoPersona().isNull() && model.getSesso().isNull())
			model.getTipoPersona().addTypeError("err.tipoPersonaObbl");

		// Sesso obbligatorio			
		if(model.getSesso().isNull())
			model.getSesso().addTypeError("err.sessoObbl");

		// Cognome obbligatorio			
		if(model.getCognome().isNull())
			model.getCognome().addTypeError("err.cognomeObbl");
		
		// Nome obbligatorio (Solo se persona fisica)
		if(model.getIsPersonaFisica().booleanValue() && model.getNome().isNull())
			model.getNome().addTypeError("err.nomeObbl");
		
		// ********************* TOOL PREVIDENZA ****************************************************
		// Nel calcolo pensionistico controllo l'obbligatorietà dei dati minimi per il calcolo stesso		
		if(model.getCallingAppl().toString().equals(Costanti.CALLING_APPL_TOOLPREVIDENZA)){
			
			if(model.getDataNascita().isNull())
				model.getDataNascita().addTypeError("err.dataNascitaObbl");
			
			if(model.getInfoPersonali().getCodStatoCivile().isNull())
				model.getInfoPersonali().getCodStatoCivile().addTypeError("err.statoCivileObbl");

			if(!model.getIsDitta().booleanValue()){
				if(model.getInfoPersonali().getCodProfessione().isNull())
					model.getInfoPersonali().getCodProfessione().addTypeError("err.professioneObbl");
			}
			
			if(model.getInfoPersonali().getCodSettoreEconomico().isNull())
				model.getInfoPersonali().getCodSettoreEconomico().addTypeError("err.taeObbl");			

			if(model.getInfoPersonali().getCodTitoloStudio().isNull())
				model.getInfoPersonali().getCodTitoloStudio().addTypeError("err.titoloStudioObbl");
			
			if(model.getInfoPersonali().getHaFigli().isNull())
				model.getInfoPersonali().getHaFigli().addTypeError("err.flagHaFigliObbl");

			ControlloIndirizzoIntf controlloIndirizzo = (ControlloIndirizzoIntf)DBJavaClassLoader.loadDbJavaObject(csc,ControlloIndirizzoImpl.class);
			controlloIndirizzo.eseguiControllo(csc,model.getResidenza().getIndirizzo(),true);

		}
		// ********************* TOOL PREVIDENZA ****************************************************
		
		// ********************* TOOL PROTEZIONE ****************************************************
		// Nel calcolo protezione controllo l'obbligatorietà dei dati minimi per il calcolo stesso
		if(model.getCallingAppl().toString().equals(Costanti.CALLING_APPL_TOOLPROTEZIONE)){
			
			if(model.getDataNascita().isNull())
				model.getDataNascita().addTypeError("err.dataNascitaObbl");
			
			if(model.getInfoPersonali().getCodStatoCivile().isNull())
				model.getInfoPersonali().getCodStatoCivile().addTypeError("err.statoCivileObbl");

			if(model.getInfoPersonali().getNumeroFamiliari().isNull())
				model.getInfoPersonali().getNumeroFamiliari().addTypeError("err.numFamigliariObbl");

			ControlloIndirizzoIntf controlloIndirizzo = (ControlloIndirizzoIntf)DBJavaClassLoader.loadDbJavaObject(csc,ControlloIndirizzoImpl.class);
			controlloIndirizzo.eseguiControllo(csc,model.getResidenza().getIndirizzo(),true);

		}
		// ********************* TOOL PREVIDENZA ****************************************************
		
		return;
				
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDatiGenerali(ClientSessionContext csc, ClienteModel model) throws Exception{

		// Comune di nascita
		if(model.getIsPotenziale().booleanValue()){// Censimento
			VerificheAnagrafica.verificaComuneScaduto(csc,model.getComuneNascita(),model);		
		}else{ //Variazione
			if(!model.getComuneNascita().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)){
				model.getComuneNascita().setComune(model.getComuneNascita().getComuneEstero());
				model.getComuneNascita().setCodComune(new StringType());
				model.getComuneNascita().setProvincia(new StringType("EE"));
			}
		}
		
		// Controllo che non esista già il codice fiscale / Partita IVA nel DB (Solo per i potenziali)
		if(model.getIsPotenziale().booleanValue()){
			
			if(model.getIsPersonaFisica().booleanValue()){
				
				if(!model.getCodFiscale().isNull() && !model.getCodFiscale().isSkippable()){
					if(!isPersonaDelPromotore(csc,model))
						isPersonaCensitaAltroAgente(csc,model);
				}
				
			}else if(model.getIsDitta().booleanValue()){

				if(!model.getPartitaIva().isNull() && !model.getPartitaIva().isSkippable()){
					if(!isSocietaDelPromotore(csc,model))
						isSocietaCensitaAltroAgente(csc,model);
				}
			}
		}
				
		return;
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaImpostaOrigine(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();
		
		if(!model.getIsPotenziale().booleanValue()){
			infoPersonali.setCodOrigineECodCliente(new StringType());
			infoPersonali.setSegnalatore(new ClienteKeyModel());				
			return;
		}

		//controllo che il segnalatore non sia un FB ticket 1007748
		if( infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER) &&
		   !infoPersonali.getSegnalatore().getCodMediolanum().isNull() &&
		    VerificheAnagrafica.verificaSeClienteIsAgente(csc, infoPersonali.getSegnalatore().getCodMediolanum())) {
			infoPersonali.getSegnalatore().getCodMediolanum().addTypeError("err.clienteFbAsPresentatore");
			return;	
		}
		
		
		// Pulisco il referral, ormai obsoleto (prima veniva pulito solo se "infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_LINEABLU)"
		infoPersonali.setReferral(new StringType());

		// Gestione della concatenazione di codOrigine e Cliente / Agente nei vari casi
		if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER) ||
		   infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_MFORYOU) ||
		   infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_IMF)){
			// Se è potenziale costruisco il codice segnalatore
			if(model.getIsPotenziale().booleanValue()){	
				ClienteKeyModel segnalatore = infoPersonali.getSegnalatore(); 
				if(!segnalatore.getCodMediolanum().isNull())
					infoPersonali.setCodOrigineECodCliente(new StringType(infoPersonali.getCodOrigine()+"-"+segnalatore.getCodMediolanum()));
				else if(!segnalatore.getCodInforete().isNull())
					infoPersonali.setCodOrigineECodCliente(new StringType(infoPersonali.getCodOrigine()+"-"+segnalatore.getCodInforete()));
				else
					infoPersonali.setCodOrigineECodCliente(new StringType());
			}
		}else if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_GLOBAL_SPECIALIST)){
			// Se è potenziale costruisco il codice segnalatore GBS
			if(model.getIsPotenziale().booleanValue()){
				ClienteKeyModel segnalatore = infoPersonali.getSegnalatore(); // In realtà in questo caso rappresenta un agente 
				if(!segnalatore.getCodAgente().isNull())
					infoPersonali.setCodOrigineECodCliente(new StringType(infoPersonali.getCodOrigine()+"-"+segnalatore.getCodAgente()));
				else
					infoPersonali.setCodOrigineECodCliente(new StringType());
			}
		}else{
			infoPersonali.setCodOrigineECodCliente(new StringType());
			infoPersonali.setSegnalatore(new ClienteKeyModel());				
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaResidenza(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{
		
		ResidenzaModel residenza = model.getResidenza();
		VerificheAnagrafica.verificaComuneScaduto(csc,residenza.getIndirizzo(),model);
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDomicilio(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{
		
		DomicilioModel domicilio = model.getDomicilio();
		VerificheAnagrafica.verificaComuneScaduto(csc,domicilio.getIndirizzo(),model);
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaRecapiti(ClientSessionContext csc, ClienteModel model){
		controllaEmail(model.getRecapiti().getEmail());
		controllaIsEmailDiAltroCliente(csc, model);
		controllaIsCellulareDiAltroCliente(csc, model);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaEmail(StringType inpEmail){
		String email = inpEmail.toString(); 
		if(email == null || email.length() == 0)
			return;
		
		int idx = email.indexOf('@');
		if(idx < 0){
			inpEmail.addTypeError("err.emailNoAtChar");
		}else if(email.equals("@")){
			inpEmail.addTypeError("err.emailErrata");
		}else if(idx == 0 || idx == (email.length()-1)){
			inpEmail.addTypeError("err.emailErrata");
		}else{
			int chiocciolaCount = 0;
			int caratteriNonValidiCount = 0;
			char[] chemail = email.toCharArray();
			for(int i=0;i<chemail.length;i++){
				if(chemail[i] == '@'){
					chiocciolaCount++;
				}else{
					if(chemail[i] != '.' && chemail[i] != '/' && chemail[i] != '_' && 
					   chemail[i] != '-' && chemail[i] != '\'' && 
					   !(chemail[i] >= '0' && chemail[i] <= '9') &&
					   !(chemail[i] >= 'A' && chemail[i] <= 'Z') &&
					   !(chemail[i] >= 'a' && chemail[i] <= 'z'))
						caratteriNonValidiCount++;
				}
			}
			if(caratteriNonValidiCount > 0)
				inpEmail.addTypeError("err.emailErrata");					
			else if(chiocciolaCount != 1)
				inpEmail.addTypeError("err.emailTooManyAtChar");
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaIsEmailDiAltroCliente(ClientSessionContext csc, ClienteModel model){
		StringType email = model.getRecapiti().getEmail();
		if(email.isNull() || email.hasTypeErrors() || model.getCodFiscale().isNull())
			return;

		// Se in censimento controlliamo sempre 
		// Se in variazione e l'email viene modificata rispetto all'originale controlliamo l'eventuale duplicazione
		boolean doControllo = model.getIsPotenziale().booleanValue() || 
							(model.getIsEffettivo().booleanValue() && model.getDatiApplicativi().getClienteCaricato() != null && 
							!model.getDatiApplicativi().getClienteCaricato().getRecapiti().getEmail().toString().equalsIgnoreCase(email.toString()));
		if(doControllo) {
			try {
				DAOObject dao = new DAOObject(csc, DAO_XML_VERIFICHE);
				BooleanType isEmailDiAltroCliente = (BooleanType)dao.executeQueryAccess("isEmailDiAltroCliente", model).getSingleResult();
				if(isEmailDiAltroCliente != null && isEmailDiAltroCliente.booleanValue())
					email.addTypeError("err.emailDiAltroCliente");
			}catch(DAOException daoe) {
				email.addTypeError(daoe.toString());
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaIsCellulareDiAltroCliente(ClientSessionContext csc, ClienteModel model){
		StringType numeroCellulare = model.getRecapiti().getTelefonoCellulare().getNumeroTelefonoCompleto();
		if(numeroCellulare.isNull() || numeroCellulare.hasTypeErrors() || model.getCodFiscale().isNull())
			return;

		// Se in censimento controlliamo sempre 
		// Se in variazione e il cellulare viene modificato rispetto all'originale controlliamo l'eventuale duplicazione
		boolean doControllo = model.getIsPotenziale().booleanValue() || 
							(model.getIsEffettivo().booleanValue() && model.getDatiApplicativi().getClienteCaricato() != null && 
							!model.getDatiApplicativi().getClienteCaricato().getRecapiti().getTelefonoCellulare().getNumeroTelefonoCompleto().toString().equalsIgnoreCase(numeroCellulare.toString()));
		if(doControllo) {
			try {
				DAOObject dao = new DAOObject(csc, DAO_XML_VERIFICHE);
				BooleanType isCellulareDiAltroCliente = (BooleanType)dao.executeQueryAccess("isCellulareDiAltroCliente", model).getSingleResult();
				if(isCellulareDiAltroCliente != null && isCellulareDiAltroCliente.booleanValue())
					model.getRecapiti().getTelefonoCellulare().getNumeroTelefono().addTypeError("err.cellulareDiAltroCliente");
			}catch(DAOException daoe) {
				model.getRecapiti().getTelefonoCellulare().getNumeroTelefono().addTypeError(daoe.toString());
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaInfoPersonali(ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();

		if(infoPersonali.getNumeroFamiliari().intValue() > 99)
			infoPersonali.getNumeroFamiliari().addTypeError("err.numeroFamiliariSuperioreAlMassimo");
		
		// Numero percettori reddito <= numero famigliari
		if(!infoPersonali.getPercettoriDiReddito().isNull() && 
		   !infoPersonali.getNumeroFamiliari().isNull() &&
			infoPersonali.getNumeroFamiliari().compareTo(infoPersonali.getPercettoriDiReddito()) < 0)
			infoPersonali.getPercettoriDiReddito().addTypeError("err.percettoriSuperioreAFamigliari");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaInfoDitta(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{

		InfoDittaModel infoDitta = model.getInfoDitta();
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();
		
		// #71803: Provincia CCIAA, se non in variazione 
		if(model.getIsPotenziale().booleanValue())
			VerificheAnagrafica.verificaProvincia(csc,infoDitta.getLuogoRilascioCCIAA().getProvincia());
				
		// controllo codice Ateco , se presente e se non in variazione (#71803: in fd non viene richiesto) 
		// prima della RFC #284520 doveva essere lungo esattamente 5 caratteri, ora nessuna verifica sulla lunghezza
		// deve esistere il codice digitato sulla tabella dominio
		// inoltre deve esistere un legame tra i primi 4 caratteri ateco e il sottogruppo
		if(model.getIsPotenziale().booleanValue()){
			if(!infoPersonali.getCodAteco().isNull()) {
				try {
					// RFC #284520: piccola modifica per evitare l'utilizzo dei codDesc
					DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
					DAOQueryResultModel qRes = dao.executeQueryAccess("controllaCodiceAteco", model);
					if(qRes.getResult().size() == 0) {
					   	infoPersonali.getCodAteco().addTypeError("err.atecoInesistente");				
					}else {
						qRes = dao.executeQueryAccess("loadCodiciAtecoDitta", model);
						if(qRes.getResult().size() == 0)
						   	infoPersonali.getCodAteco().addTypeError("err.atecoNonValido");				
					}
				} catch(DAOException de) {
					String errorMsg = "Eccezione: ["+ de +"] nel controllo del codice ATECO";
					throw new AnagraficaClientiException(errorMsg);				
				}
			}
		}
	}
	
	/***********************************************************************************************/
	/* Con la rfc 119648 molti dati si sono spostati da InfoPersonali ad AdempimentiNormativi 	   */
	/***********************************************************************************************/
	private void controllaAdempimentiNormativi(ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();

		// Se non è uno studente pulisco il codice universita
		if(!infoPersonali.getCodProfessione().equals(Costanti.CODICE_PROFESSIONE_STUDENTE))
			infoPersonali.getUniversita().clear();
		
		AdempimentiNormativiModel ade = model.getAdempimentiNormativi();
		StringType p1 = ade.getPaeseLegameAffariDiversoDaAttivitaPrincipale1();
		StringType p2 = ade.getPaeseLegameAffariDiversoDaAttivitaPrincipale2();
		StringType p3 = ade.getPaeseLegameAffariDiversoDaAttivitaPrincipale3();
		
		addErrorPaesiLegameAffariDiversoDaAttivitaPrincipaleUguali(p1,p2,p3);		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static final String ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI = "err.paesiLegameAffariDiversoDaAttivitaPrincipaleUguali";
	private void addErrorPaesiLegameAffariDiversoDaAttivitaPrincipaleUguali(StringType p1, StringType p2,StringType p3) {
		if(!p1.isNull() && p1.equals(p2)){
			p1.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
			p2.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
		}

		if(!p1.isNull() && p1.equals(p3)){
			p1.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
			p3.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
		}
		
		if(!p2.isNull() && p2.equals(p3)){
			p2.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
			p3.addTypeError(ERR_PAESILEGAMEAFFARIDIVERSODAATTIVITAPRINCIPALEUGUALI);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDocumento(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		DocumentoModel documento = model.getDocumento();
		
		// Comune di rilascio documento (solo se italiano)
		if(!model.getDocumento().getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_CARTA_IDENTITA_ESTERA) &&
		   !model.getDocumento().getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_PASSAPORTO_ESTERO)){
			if(model.getDocumento().getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_PASSAPORTO_ITALIANO))
				VerificheAnagrafica.verificaComuneDocumentoItaliano(csc,documento.getLuogoRilascio(),model);
			else	
				VerificheAnagrafica.verificaComuneDocumento(csc,documento.getLuogoRilascio(),model);			
		}

		// La data di scadenza deve essere maggiore di quella di rilascio
		if(!documento.getDataScadenza().isNull() &&
		   !documento.getDataRilascio().isNull()){
			
		   	if(documento.getDataScadenza().compareTo(documento.getDataRilascio()) <= 0)
		   		documento.getDataScadenza().addTypeError("err.scadenzaDocMinoreRilascio");
		   	
		}
		
		// La data di rilascio del documento deve essere maggiore di oggi
		DateType today = Tools.today();
	   	if(!documento.getDataRilascio().isNull() && documento.getDataRilascio().compareTo(today) > 0)
	   		documento.getDataRilascio().addTypeError("err.rilascioDocMaggioreOggi");
	   	
		// La data di scadenza deve essere maggiore di oggi (Solo per i potenziali o per gli effettivi
		// ai quali viene variato il documento) 
		DocumentoModel docOriginale = null;
		if(model.getDatiApplicativi().getClienteOriginale() != null)
			docOriginale = model.getDatiApplicativi().getClienteOriginale().getDocumento();
		if( model.getIsPotenziale().booleanValue() ||
		   (docOriginale!= null && !documento.getTipoDocumento().isNull() && !documento.getTipoDocumento().equals(docOriginale.getTipoDocumento())) ||
		   (docOriginale!= null && !documento.getNumeroDocumento().isNull() && !documento.getNumeroDocumento().equals(docOriginale.getNumeroDocumento())) ||
		   (docOriginale!= null && !documento.getDataScadenza().isNull() && !documento.getDataScadenza().equals(docOriginale.getDataScadenza()))){
			DateType oggi = Tools.today();
		   	if(!documento.getDataScadenza().isNull() &&
			   	documento.getDataScadenza().compareTo(oggi) < 0)
			   	documento.getDataScadenza().addTypeError("err.scadenzaDocMinoreOggi");
		}

		// #133848 - Si avvisa che la data di scadenza è prossima, solo se non minore di oggi
		DateType scadenzaMeno4 = documento.getDataScadenza().addMonthsOnNew(-4);
		if(today.compareTo(documento.getDataScadenza()) <= 0 && today.compareTo(scadenzaMeno4) >= 0) {
			documento.getDataScadenza().addTypeWarning("war.scadenzaDocProssima");
		}
		
		StringType numeroDocumentoOriginale = null;
		if(model.getDatiApplicativi().getClienteOriginale() != null){
			numeroDocumentoOriginale = model.getDatiApplicativi().getClienteOriginale().getDocumento().getNumeroDocumento();
		}
		
		if( numeroDocumentoOriginale == null || !numeroDocumentoOriginale.equals(model.getDocumento().getNumeroDocumento())){
			//Controllo che il documento non sia appartente ad altro cliente
			if(!model.getDocumento().getTipoDocumento().isNull() && !model.getDocumento().getNumeroDocumento().isNull()){
				isDocumentoDelPromotore(csc, model);			
			}
		}

		// Il Numero documento deve essere lungo almeno tre caratteri
		if(!documento.getNumeroDocumento().isNull()){
			String numDoc = documento.getNumeroDocumento().toString();
			if(numDoc.length() < 3)
				documento.getNumeroDocumento().addTypeError("err.numDocAlmenoTreChar");
			else{ // Non 3 char uguali consecutivi
				for(int i=0;i<numDoc.length()-3;i++){
					char c = numDoc.charAt(i);
					if(c == numDoc.charAt(i+1) &&
					   c == numDoc.charAt(i+2) &&
					   c == numDoc.charAt(i+3)){
						documento.getNumeroDocumento().addTypeWarning("war.numDocAnomalo");
						break;
					}
				}
			}
		}
				
		//Controlla Giorni festivi
		controllaGiorniFestivi(csc,model);
		
		//Controlla Data Rilascio,Rinnovo,Scadenza
		controllaDataDocumento(csc,model);		
				
		return;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDatiPrivacy(ClientSessionContext csc, ClienteModel model) throws Exception{
		return;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isPersonaDelPromotore(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel clienteEsistente = new ClienteModel();
			clienteEsistente.setCodAgente(model.getCodAgente());
			clienteEsistente.setSesso(new StringType(Costanti.SESSO_MASCHIO));
			clienteEsistente.setCodFiscale(model.getCodFiscale());
			DAOQueryResultModel qres = 	dao.executeQueryAccess("verificaSeClienteDiAgente",clienteEsistente);
			if(qres.getResult().size() == 0)
				return false;

			clienteEsistente = (ClienteModel)qres.getResult().get(0);
			String codMediolanum = clienteEsistente.getCodMediolanum().toString();
			String nome = clienteEsistente.getCognome().toString()+" "+clienteEsistente.getNome().toString();
			String dataNascita = clienteEsistente.getDataNascita().toString();
			TypeWarning te;
			if(clienteEsistente.getIsAssegnatoAdAltroAgente().booleanValue()){
				te = new TypeWarning("war.codFiscAltroAgente",codMediolanum,nome,dataNascita);
				model.getCodFiscale().addTypeWarning(te);
			}else if(clienteEsistente.getIsCointestatarioNonAssegnato().booleanValue()){
				te = new TypeWarning("war.codFiscClienteCoint",codMediolanum,nome,dataNascita);
				model.getCodFiscale().addTypeWarning(te);
			}else if(clienteEsistente.getIsEffettivo().booleanValue()){
				te = new TypeWarning("war.codFiscAltroCliente",codMediolanum,nome,dataNascita);
				model.getCodFiscale().addTypeWarning(te);
			}else{
				te = new TypeWarning("war.codFiscAltroProspect",nome,dataNascita);
				model.getCodFiscale().addTypeWarning(te);
			}
			return true;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isSocietaDelPromotore(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		try{
			String filledPIva = Tools.fillSx(model.getPartitaIva().toString(),'0',11);
			if(Integer.parseInt(filledPIva) == 0)
				return false;
		}catch(Exception e){}
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel clienteEsistente = new ClienteModel();
			clienteEsistente.setCodAgente(model.getCodAgente());
			clienteEsistente.setSesso(new StringType(Costanti.SESSO_SOCIETA));
			clienteEsistente.setPartitaIva(model.getPartitaIva());
			DAOQueryResultModel qres = 	dao.executeQueryAccess("verificaSeClienteDiAgente",clienteEsistente);
			if(qres.getResult().size() == 0)
				return false;

			clienteEsistente = (ClienteModel)qres.getResult().get(0);
			String codMediolanum = clienteEsistente.getCodMediolanum().toString();
			String nome = clienteEsistente.getCognome().toString()+" "+clienteEsistente.getNome().toString();
			String secondaIntestazione = clienteEsistente.getSecondaIntestazione().toString();
			if(secondaIntestazione.length() == 0)
				secondaIntestazione = nome;
			String dataNascita = clienteEsistente.getDataNascita().toString();
			TypeWarning te;
			if(clienteEsistente.getIsAssegnatoAdAltroAgente().booleanValue()){
				if(clienteEsistente.getIsDitta().booleanValue())
					te = new TypeWarning("war.pIvaAltroAgente",codMediolanum,secondaIntestazione,nome,dataNascita);
				else
					te = new TypeWarning("war.pIvaSocietaAltroAgente",codMediolanum,nome,dataNascita);
				model.getPartitaIva().addTypeWarning(te);
			}else if(clienteEsistente.getIsCointestatarioNonAssegnato().booleanValue()){
				if(clienteEsistente.getIsDitta().booleanValue())
					te = new TypeWarning("war.pIvaDittaCoint",codMediolanum,secondaIntestazione,nome,dataNascita);
				else
					te = new TypeWarning("war.pIvaSocietaCoint",codMediolanum,nome,dataNascita);
				model.getPartitaIva().addTypeWarning(te);
			}else if(clienteEsistente.getIsEffettivo().booleanValue()){
				if(clienteEsistente.getIsDitta().booleanValue())
					te = new TypeWarning("war.pIvaAltraDitta",codMediolanum,secondaIntestazione,nome,dataNascita);
				else
					te = new TypeWarning("war.pIvaAltraSocieta",codMediolanum,nome,dataNascita);
				model.getPartitaIva().addTypeWarning(te);
			}else{
				if(clienteEsistente.getIsDitta().booleanValue())
					te = new TypeWarning("war.pIvaDittaProspect",secondaIntestazione,nome,dataNascita);
				else
					te = new TypeWarning("war.pIvaSocietaProspect",nome,dataNascita);
				model.getPartitaIva().addTypeWarning(te);
			}
			return true;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isPersonaCensitaAltroAgente(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel clienteEsistente = new ClienteModel();
			clienteEsistente.setSesso(new StringType(Costanti.SESSO_MASCHIO));
			clienteEsistente.setCodFiscale(model.getCodFiscale());
			DAOQueryResultModel qres = 	dao.executeQueryAccess("verificaSeClienteCensito",clienteEsistente);
			if(qres.getResult().size() == 0)
				return false;

			clienteEsistente = (ClienteModel)qres.getResult().get(0);
			String nome = clienteEsistente.getCognome().toString()+" "+clienteEsistente.getNome().toString();
			String dataNascita = clienteEsistente.getDataNascita().toString();
			String luogoNascita = clienteEsistente.getComuneNascita().getComune().toString();
			
			BooleanType isAssegnatoAdAltroAgenteImpersonale = (BooleanType)dao.executeQueryAccess("isAssegnatoAdAltroAgenteImpersonale",clienteEsistente).getSingleResult();
			BooleanType isDisinvestito = (BooleanType)dao.executeQueryAccess("isClienteDisinvestito",clienteEsistente).getSingleResult();

			TypeWarning tw = null;
			if(isDisinvestito != null && isDisinvestito.booleanValue()) {
				if(isAssegnatoAdAltroAgenteImpersonale != null && isAssegnatoAdAltroAgenteImpersonale.booleanValue()){
					tw = new TypeWarning("war.codFiscDisinvestitoFBImpersonale",nome,dataNascita,luogoNascita);
				} else {
					tw = new TypeWarning("war.codFiscDisinvestitoFBNonImpersonale",nome,dataNascita,luogoNascita);
				}
			}else{
				if(isAssegnatoAdAltroAgenteImpersonale != null && isAssegnatoAdAltroAgenteImpersonale.booleanValue()){
					tw = new TypeWarning("war.codFiscNonDisinvestitoFBImpersonale",nome,dataNascita,luogoNascita);
				} else {
					tw = new TypeWarning("war.codFiscNonDisinvestitoFBNonImpersonale",nome,dataNascita,luogoNascita);
				}
			}

			model.getCodFiscale().addTypeWarning(tw);
			return true;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isSocietaCensitaAltroAgente(ClientSessionContext csc, ClienteModel model) throws Exception{

		try{
			String filledPIva = Tools.fillSx(model.getPartitaIva().toString(),'0',11);
			if(Integer.parseInt(filledPIva) == 0)
				return false;
		}catch(Exception e){}
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel clienteEsistente = new ClienteModel();
			clienteEsistente.setSesso(new StringType(Costanti.SESSO_SOCIETA));
			clienteEsistente.setPartitaIva(model.getPartitaIva());
			DAOQueryResultModel qres = 	dao.executeQueryAccess("verificaSeClienteCensito",clienteEsistente);
			if(qres.getResult().size() == 0)
				return false;

			clienteEsistente = (ClienteModel)qres.getResult().get(0);
			String nome = clienteEsistente.getCognome().toString()+" "+clienteEsistente.getNome().toString();
			String secondaIntestazione = clienteEsistente.getSecondaIntestazione().toString();
			if(secondaIntestazione.length() == 0)
				secondaIntestazione = nome;
			String dataNascita = clienteEsistente.getDataNascita().toString();
			String luogoNascita = clienteEsistente.getComuneNascita().getComune().toString();
			
			BooleanType isAssegnatoAdAltroAgenteImpersonale = (BooleanType)dao.executeQueryAccess("isAssegnatoAdAltroAgenteImpersonale",clienteEsistente).getSingleResult();
			BooleanType isDisinvestito = (BooleanType)dao.executeQueryAccess("isClienteDisinvestito",clienteEsistente).getSingleResult();
			
			TypeWarning tw = null;
			if(isDisinvestito != null && isDisinvestito.booleanValue()) {
				if(isAssegnatoAdAltroAgenteImpersonale != null && isAssegnatoAdAltroAgenteImpersonale.booleanValue()){
					if(clienteEsistente.getIsDitta().booleanValue()) {
						tw = new TypeWarning("war.pIvaDittaDisinvestitaFBImpersonale",secondaIntestazione,nome,dataNascita,luogoNascita);
					} else {
						tw = new TypeWarning("war.pIvaSocietaDisinvestitaFBImpersonale",nome,dataNascita);
					}
				} else {
					if(clienteEsistente.getIsDitta().booleanValue()) {
						tw = new TypeWarning("war.pIvaDittaDisinvestitaFBNonImpersonale",secondaIntestazione,nome,dataNascita,luogoNascita);
					} else {
						tw = new TypeWarning("war.pIvaSocietaDisinvestitaFBNonImpersonale",nome,dataNascita);
					}
				}
			}else{
				if(isAssegnatoAdAltroAgenteImpersonale != null && isAssegnatoAdAltroAgenteImpersonale.booleanValue()){
					if(clienteEsistente.getIsDitta().booleanValue()) {
						tw = new TypeWarning("war.pIvaDittaNonDisinvestitaFBImpersonale",secondaIntestazione,nome,dataNascita,luogoNascita);
					} else {
						tw = new TypeWarning("war.pIvaSocietaNonDisinvestitaFBImpersonale",nome,dataNascita);
					}
				} else {
					if(clienteEsistente.getIsDitta().booleanValue()) {
						tw = new TypeWarning("war.pIvaDittaNonDisinvestitaFBNonImpersonale",secondaIntestazione,nome,dataNascita,luogoNascita);
					} else {
						tw = new TypeWarning("war.pIvaSocietaNonDisinvestitaFBNonImpersonale",nome,dataNascita);
					}
				}
			}

			model.getPartitaIva().addTypeWarning(tw);
			return true;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isDocumentoDelPromotore(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel docuemntoClienteEsistente = new ClienteModel();
			docuemntoClienteEsistente.setCodAgente(model.getCodAgente());
			docuemntoClienteEsistente.getDocumento().setTipoDocumento(model.getDocumento().getTipoDocumento());
			docuemntoClienteEsistente.getDocumento().setNumeroDocumento(model.getDocumento().getNumeroDocumento());
			
			DAOQueryResultModel qres = 	dao.executeQueryAccess("verificaSeDocumentoDiAgente",docuemntoClienteEsistente);
			if(qres.getResult().size() == 0)
				return false;

			docuemntoClienteEsistente = (ClienteModel)qres.getResult().get(0);
			String codMediolanum = docuemntoClienteEsistente.getCodMediolanum().toString();
			String codPotenziale = docuemntoClienteEsistente.getCodPotenziale().toString();
			String nome = docuemntoClienteEsistente.getCognome().toString()+" "+docuemntoClienteEsistente.getNome().toString();
			String dataNascita = docuemntoClienteEsistente.getDataNascita().toString();
			String codFiscale = docuemntoClienteEsistente.getCodFiscale().toString();
			
			//Se il documento trovato appartiene al cliente in variazione esco
			if(!codMediolanum.equals("")){
				if(model.getCodMediolanum().equals(codMediolanum))
					return false;
			}else{
				if(model.getCodPotenziale().equals(codPotenziale))
					return false;
			}
			
			//oppure il codice fiscale coincide esco 
			if(!codFiscale.equals("")){
				if(model.getCodFiscale().equals(codFiscale))
					return false;
			}			
			
			TypeError te;
			if(docuemntoClienteEsistente.getIsAssegnatoAdAltroAgente().booleanValue()){
				te = new TypeError("err.docCliAltroAgente",codMediolanum,nome,dataNascita);
				model.getDocumento().getNumeroDocumento().addTypeError(te);
			}else if(docuemntoClienteEsistente.getIsCointestatarioNonAssegnato().booleanValue()){
				te = new TypeError("err.docAltroCliCoint",codMediolanum,nome,dataNascita);
				model.getDocumento().getNumeroDocumento().addTypeError(te);
			}else if(docuemntoClienteEsistente.getIsEffettivo().booleanValue()){
				te = new TypeError("err.docAltroCli",codMediolanum,nome,dataNascita);
				model.getDocumento().getNumeroDocumento().addTypeError(te);
			}else{
				te = new TypeError("err.docAltroCliProspect",nome,dataNascita);
				model.getDocumento().getNumeroDocumento().addTypeError(te);
			}
			return true;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaGiorniFestivi(ClientSessionContext csc,ClienteModel model) throws Exception{
				
		DocumentoModel documento = model.getDocumento();
		
		DateType dataRilascioOriginale = null;
		
		if(model.getDatiApplicativi().getClienteOriginale() != null){
			
			if(model.getDatiApplicativi().getClienteOriginale().getDocumento().getDataRilascio() != null)
				dataRilascioOriginale = model.getDatiApplicativi().getClienteOriginale().getDocumento().getDataRilascio();
			
		}
			
		if( dataRilascioOriginale == null || !dataRilascioOriginale.equals(model.getDocumento().getDataRilascio())){
				
				if(!documento.getDataRilascio().isNull()){
					if (isDataRilascioGiornoFestivo(csc,documento)){
						TypeWarning tw = new TypeWarning("war.dataRilascioDocFestiva");
						documento.getDataRilascio().addTypeWarning(tw);
					}
				}
		}
		return;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isDataRilascioGiornoFestivo(ClientSessionContext csc,DocumentoModel documento) {
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
						
			DAOQueryResultModel qRes = dao.executeQueryAccess("verificaDataRilascioFestivo", documento);
			BooleanType isGiornoFestivita =(BooleanType)qRes.getSingleResult();
			
			BooleanType isGiornoDomenica = new BooleanType();
			
			Calendar dataRilascioCalendar = Calendar.getInstance();
			dataRilascioCalendar.setTime(documento.getDataRilascio().dateValue());

			int dayofweek = dataRilascioCalendar.get(Calendar.DAY_OF_WEEK);
		    if (dayofweek == Calendar.SUNDAY)
		    	isGiornoDomenica = new BooleanType(true);
			
		    if(isGiornoFestivita.booleanValue() || isGiornoDomenica.booleanValue())
		    	return true;
		    else
		    	return false;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDataDocumento(ClientSessionContext csc,ClienteModel model) throws Exception{
		
		DocumentoModel documento = model.getDocumento();
		
		// Le tre date devono essere maggiori della data di nascita (se valorizzata)
		DateType dataNascita = model.getDataNascita();
		if(!dataNascita.isNull()){

			DateType dataRilascioOriginale = null;
			if(model.getDatiApplicativi().getClienteOriginale() != null)
		
				dataRilascioOriginale = model.getDatiApplicativi().getClienteOriginale().getDocumento().getDataRilascio();
			
				if( dataRilascioOriginale == null || !dataRilascioOriginale.equals(model.getDocumento().getDataRilascio())){
					
					if(documento.getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_LIBRETTO_PENSIONE))
						if(!documento.getDataRilascio().isNull() &&
							    documento.getDataRilascio().yearsBetween(dataNascita).intValue() < 60)
								documento.getTipoDocumento().addTypeWarning("war.etaNonIdoneaInPensione");
					
					
					if(documento.getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_CERTIFICATO_NASCITA))
						if(!documento.getDataRilascio().isNull() &&
							    documento.getDataRilascio().yearsBetween(dataNascita).intValue() >= 15)
								documento.getTipoDocumento().addTypeWarning("war.etaNonIdoneaCertNascita");
			}
				
			// Data scadenza
			if(!documento.getDataScadenza().isNull() &&
			    documento.getDataScadenza().compareTo(dataNascita) <= 0)
				documento.getDataScadenza().addTypeError("err.scadenzaDocMinoreNascita");
			
		}
	
	}

}
