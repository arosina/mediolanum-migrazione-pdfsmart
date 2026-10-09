package prgm.ita.anagraficaclienti.facade;

import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dbjavaclasses.DBJavaClassLoader;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.model.AdempimentiNormativiModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiPrivacyModel;
import prgm.ita.anagraficaclienti.model.DocumentoModel;
import prgm.ita.anagraficaclienti.model.DomicilioModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;
import prgm.ita.anagraficaclienti.model.InfoPersonaliModel;
import prgm.ita.anagraficaclienti.model.ResidenzaModel;
import prgm.ita.anagraficaclienti.model.TelefonoModel;
import prgm.ita.anagraficaclienti.model.UniversitaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliAnagraficaInvio implements Controlli{

    protected static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	

    private static final String DAO_XML_VERIFICHE = "ItaAnagraficaClienti.Verifiche";
    private static final String S_ERR_DATOOBBLIGATORIO = "err.datoObbligatorio";
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		try{
			ControlloIndirizzoIntf controlloreIndirizzo = (ControlloIndirizzoIntf)DBJavaClassLoader.loadDbJavaObject(csc,ControlloIndirizzoImpl.class);
			
			// Datore di lavoro
			if(model.getIsDatoreDiLavoro().booleanValue()){
				if(model.getCognome().isNull())
					model.getCognome().addTypeError("err.ragioneSocialeObbl");

				if(model.getPartitaIva().isNull())
					model.getPartitaIva().addTypeError("err.partitaIvaObbl");
				
				// Indirizzo residenza obbligatorio
				IndirizzoModel indirizzo = model.getResidenza().getIndirizzo();		
				controlloreIndirizzo.eseguiControllo(csc,indirizzo,true);
				
				// Telefono sede legale obbligatorio
				TelefonoModel telefono = model.getRecapiti().getTelefonoResidenza();
				if(telefono.getNumeroTelefono().isNull())
					telefono.getNumeroTelefono().addTypeError("err.numTelObbl");

				if(!telefono.getNumeroTelefono().isNull() && telefono.getPrefisso().isNull())
					telefono.getPrefisso().addTypeError("err.prefTelObbl");
				
				if(model.getRecapiti().getEmail().isNull()) // RFC #129196
					model.getRecapiti().getEmail().addTypeError("err.emailObbl");
				return;
			}
			
			// Dati generali			
			controllaDatiGenerali(csc,model);
				
			// Residenza
			controllaResidenza(csc,model,controlloreIndirizzo);

			// Domicilio
			controllaDomicilio(csc,model,controlloreIndirizzo);

			// Domicilio diverso da residenza obbligatorio
			if(model.getDomicilioDiversoDaResidenza().isNull())
				model.getDomicilioDiversoDaResidenza().addTypeError(S_ERR_DATOOBBLIGATORIO);
			
			// Recapiti
			controllaRecapiti(csc,model);
			
			// Info personali
			controllaInfoPersonali(csc, model);
				
			// Info ditta
			if(model.getIsDitta().booleanValue())
				controllaInfoDitta(model);
			
			// Adempimenti normativi
			controllaAdempimentiNormativi(model);
			
			// Controllo dell'origine
			controllaOrigine(csc,model);
			
			// Documento
			controllaDocumento(csc,model);
				
			// Privacy
			controllaDatiPrivacy(csc,model);
			
			// Prefissi
			controllaPrefissi(csc,model);
			
			return;
			
		}catch(Exception e){
			String errorMsg = "Eccezione: ["+e+"] nel controllo dell'anagrafica all'invio";
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDatiGenerali(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		// Numero Ordine solo in censimento
		if(model.getIsPotenziale().booleanValue()){
			if(model.getNumeroOrdine().isNull()){
				model.getNumeroOrdine().addTypeWarning("war.numeroCartaChimicaNullo");
			}else{
				try{
					String sNumOrd = Tools.fillSx(model.getNumeroOrdine().toString(),'0',12); 
					if(Integer.parseInt(sNumOrd) <= 0){
						model.getNumeroOrdine().addTypeWarning("war.numeroCartaChimicaErrato");						
					}else{
						int sNumOrdLen = sNumOrd.length(); 
						if(sNumOrdLen < 4){
							model.getNumeroOrdine().addTypeWarning("war.numeroCartaChimicaErrato");
						}else{
								int cin = Integer.parseInt(sNumOrd.substring(sNumOrdLen-2));
								int num = Integer.parseInt(sNumOrd.substring(0,sNumOrdLen-2));
								if((num % 13) != cin)
									model.getNumeroOrdine().addTypeWarning("war.numeroCartaChimicaErrato");						
						}
					}
				}catch(Exception ne){
					model.getNumeroOrdine().addTypeWarning("war.numeroCartaChimicaErrato");						
				}
			}
		}
		
		// Data di nascita con un valore futuro
		DateType rightNow = Tools.today();
		if(rightNow.compareTo(model.getDataNascita()) < 0)
			model.getDataNascita().addTypeError("err.dataNascitaFutura");
		
		// Data di nascita obbligatoria
		if(model.getDataNascita().isNull())
			model.getDataNascita().addTypeError("err.dataNascitaObbl");

		// Comune di nascita: se italiano lo controllo altrimenti gestisco l'estero (Solo persone fisiche)
		if(model.getComuneNascita().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)){
			if(model.getComuneNascita().getComune().isNull())
				model.getComuneNascita().getComune().addTypeError("err.luogoNascitaObbl");
		}else{ // Se estero obbligatorio il comune estero
			// Comune nascita estero obbligatorio
			if(model.getComuneNascita().getComuneEstero().isNull())
				model.getComuneNascita().getComuneEstero().addTypeError("err.luogoNascitaEsteroObbl");
		}

		// Controllo codice fiscale
		if(model.getCodFiscale().isNull())
			model.getCodFiscale().addTypeError("err.codFiscObbl");
		
		// Se ditta la pIva è obbligatoria
		if(model.getIsDitta().booleanValue() && model.getPartitaIva().isNull())
			model.getPartitaIva().addTypeError("err.partitaIvaObbl");
		
		// Se ditta il tipo ditta è obbligatorio (solo in censimento
		if(model.getIsPotenziale().booleanValue()){
			if(model.getIsDitta().booleanValue() && model.getTipoDitta().isNull())
				model.getTipoDitta().addTypeError("err.tipoDittaObbl");
		}
		
		// Cittadinanza Obbligatoria
		if(model.getCittadinanza().isNull())
			model.getCittadinanza().addTypeError("err.cittadinanzaObbl");
		
		return;
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaOrigine(ClientSessionContext csc, ClienteModel model) throws AnagraficaClientiException{
		
		if(!model.getIsPotenziale().booleanValue())
			return;
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();

		if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER)){			
			if(infoPersonali.getSegnalatore().getCodMediolanum().isNull() && infoPersonali.getSegnalatore().getCodPotenziale().isNull())
				infoPersonali.getSegnalatore().getCodMediolanum().addTypeError("err.presentatoreObbl");
		}else if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_MFORYOU)){ // Il flagClienteSegnalato è not null se il cliente è segnalato		
			if(!model.getDatiApplicativi().getFlagClienteSegnalato().isNull() && infoPersonali.getSegnalatore().getCodMediolanum().isNull())
				infoPersonali.getSegnalatore().getCodMediolanum().addTypeError("err.presentatoreObbl");
		}else if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_GLOBAL_SPECIALIST)){			
			if(infoPersonali.getSegnalatore().getCodAgente().isNull())
				infoPersonali.getSegnalatore().getCodMediolanum().addTypeError("err.segnalatoreGlobalSpecialistObbl");
		}else if(infoPersonali.getCodOrigine().equals(Costanti.CODICE_ORIGINE_IMF)){			
			if(infoPersonali.getSegnalatore().getCodMediolanum().isNull() && infoPersonali.getSegnalatore().getCodPotenziale().isNull())
				infoPersonali.getSegnalatore().getCodMediolanum().addTypeError("err.presentatoreObbl");
			else
				if(!VerificheAnagrafica.verificaPresentatoreIMF(csc,infoPersonali.getSegnalatore()))
					infoPersonali.getSegnalatore().getCodMediolanum().addTypeError(new TypeError("err.erratoPresentatoreIMF"));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaResidenza(ClientSessionContext csc, ClienteModel model, 
								    ControlloIndirizzoIntf controlloreIndirizzo) throws Exception{
		
		ResidenzaModel residenza = model.getResidenza();
		
		// Indirizzo residenza obbligatorio
		IndirizzoModel indirizzo = residenza.getIndirizzo();		
		controlloreIndirizzo.eseguiControllo(csc,indirizzo,true);
		
		// Almeno una residenza fiscale
		if(residenza.getCodNazioneResidenzaFiscale1().isNull() && residenza.getCodNazioneResidenzaFiscale2().isNull()){
			residenza.getCodNazioneResidenzaFiscale1().addTypeError("err.resFis1OResfis2Obbl");
			residenza.getCodNazioneResidenzaFiscale2().addTypeError("err.resFis2OResfis1Obbl");			
		}
		
		// Se specificata la terza residenza e non la seconda
		if(residenza.getCodNazioneResidenzaFiscale2().isNull() && ! residenza.getCodNazioneResidenzaFiscale3().isNull()){
			residenza.getCodNazioneResidenzaFiscale2().addTypeError("err.primaESecondaResFiscObblig");						
		}
		
		// Se la terza residenza è uguale alla seconda
		if( ! residenza.getCodNazioneResidenzaFiscale3().isNull() &&
				residenza.getCodNazioneResidenzaFiscale2().equals(residenza.getCodNazioneResidenzaFiscale3())){
			residenza.getCodNazioneResidenzaFiscale3().addTypeError("err.secondaETerzaResFiscUguali");						
		}
		
		// Controllo codice fiscale seconda residenza fiscale
		if(residenza.getCodFiscaleResidenza2Released().booleanValue()) {
			/* se non è stato digitato nulla e il cf è obbligatorio errore */
			if(residenza.getCodFiscaleResidenzaFiscale2().isNull()) {
				if(residenza.getCodFiscaleResidenza2Required().booleanValue()) {
					residenza.getCodFiscaleResidenzaFiscale2().addTypeError("err.codFiscSecondaResFiscObbl");
				}
			/* se è stato digitato qualcosa la lunghezza non deve superare il max */
			} else {
				if(residenza.getCodFiscaleResidenzaFiscale2().toString().length() > residenza.getCodFiscaleResidenza2Digits().intValue()) {
					residenza.getCodFiscaleResidenzaFiscale2().addTypeError("err.codFiscSecondaResFiscErrato");
				}
			}
		}
		
		// Controllo codice fiscale terza residenza fiscale
		if(residenza.getCodFiscaleResidenza3Released().booleanValue()) {
			/* se non è stato digitato nulla e il cf è obbligatorio errore */
			if(residenza.getCodFiscaleResidenzaFiscale3().isNull()) {
				if(residenza.getCodFiscaleResidenza3Required().booleanValue()) {
					residenza.getCodFiscaleResidenzaFiscale3().addTypeError("err.codFiscTerzaResFiscObbl");
				}
			} else {
				/* se è stato digitato qualcosa la lunghezza non deve superare il max */
				if(residenza.getCodFiscaleResidenzaFiscale3().toString().length() > residenza.getCodFiscaleResidenza3Digits().intValue()) {
					residenza.getCodFiscaleResidenzaFiscale3().addTypeError("err.codFiscTerzaResFiscErrato");
				}
			}
		}
		
		return;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDomicilio(ClientSessionContext csc, ClienteModel model, ControlloIndirizzoIntf controlloreIndirizzo) throws Exception{
		
		DomicilioModel domicilio = model.getDomicilio();		
		IndirizzoModel indirizzo = domicilio.getIndirizzo();		
		controlloreIndirizzo.eseguiControllo(csc,indirizzo,model.getDomicilioDiversoDaResidenza().equals("S"));
		return;
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaRecapiti(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		// Numero telefono residenza o cell obbligatorio
		TelefonoModel telefono = model.getRecapiti().getTelefonoResidenza();
		TelefonoModel cellulare = model.getRecapiti().getTelefonoCellulare();
		TelefonoModel fax = model.getRecapiti().getTelefonoFax();
		
		if(telefono.getNumeroTelefono().isNull() && cellulare.getNumeroTelefono().isNull()){
			telefono.getNumeroTelefono().addTypeError("err.telOCellObbl");
			cellulare.getNumeroTelefono().addTypeError("err.cellOTelObbl");			
		}

		
		ClienteModel clienteCaricato = null;
		if (model.getDatiApplicativi().getClienteCaricato() == null)
			clienteCaricato = new ClienteModel();
		else	
			clienteCaricato = model.getDatiApplicativi().getClienteCaricato();
			
		TelefonoModel telefonoCellulareCaricato = clienteCaricato.getRecapiti().getTelefonoCellulare();
			
		//Se il prefisso internazionale è stato cambiato e non in quello italiano allora blocco
		if(!cellulare.getPrefissoInternazionale().equals(Costanti.PREFIX_INTERNAZIONALE_ITALIA) &&
		   !cellulare.getPrefissoInternazionale().equals(telefonoCellulareCaricato.getPrefissoInternazionale())){
			cellulare.getPrefissoInternazionale().addTypeError(new TypeError("err.prefissoTelInternErrato",Costanti.PREFIX_INTERNAZIONALE_ITALIA));
		}

		//Per il censimento se il prefisso inizia con '0' allora blocco
		//Per le variazioni se il prefisso è stato cambiato e inizia con '0' allora blocco
		if( cellulare.getPrefisso().toString().startsWith("0") &&
		   (telefonoCellulareCaricato.getPrefisso().isNull() || !cellulare.getPrefisso().equals(telefonoCellulareCaricato.getPrefisso()))){
			cellulare.getPrefisso().addTypeError("err.prefissoTelErrato");
		}
		
		TelefonoModel faxCaricato = clienteCaricato.getRecapiti().getTelefonoFax();
		
		//Se il prefisso internazionale è stato cambiato e non in quello italiano allora blocco
		if(!fax.getPrefissoInternazionale().equals(Costanti.PREFIX_INTERNAZIONALE_ITALIA) &&
		   !fax.getPrefissoInternazionale().equals(faxCaricato.getPrefissoInternazionale())){
			fax.getPrefissoInternazionale().addTypeError(new TypeError("err.prefissoTelInternErrato",Costanti.PREFIX_INTERNAZIONALE_ITALIA));
		}
		
		return;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaInfoPersonaliUniversita(InfoPersonaliModel infoPersonali){
		UniversitaModel uni = infoPersonali.getUniversita();
		int v1 = uni.getProvincia().isNull() ? 0 : 1;
		int v2 = uni.getFacolta().isNull() ? 0 : 1;
		int v3 = uni.getCodUniversita().isNull() ? 0 : 1;		
		if ((v1+v2+v3) != 0 && (v1+v2+v3) != 3) {
			if(v1 == 0)
				uni.getProvincia().addTypeError("err.provinciaUniversitaObbl");
			if(v2 == 0)
				uni.getFacolta().addTypeError("err.facoltaUniversitaObbl");
			if(v3 == 0) {
				uni.getCodUniversita().addTypeError("err.ateneoUniversitaObbl"); // In variazione è una tendina
				uni.getAteneo().addTypeError("err.ateneoUniversitaObbl");
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaInfoPersonali(ClientSessionContext csc, ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();

		// Titolo di studio obbligatorio
		if(infoPersonali.getCodTitoloStudio().isNull())
			infoPersonali.getCodTitoloStudio().addTypeError("err.titoloStudioObbl");

		// Fasce di reddito e patrimonio
		if(infoPersonali.getFasciaPatrimonioComplessivo().isNull())
			infoPersonali.getFasciaPatrimonioComplessivo().addTypeError("err.fasciaPatrimonioComplessivoObbligatoria");		
		else if(model.getIsEffettivo().booleanValue())
			controllaFasciaPatrimonio(csc, model);
		
		if(infoPersonali.getFasciaRedditoAnnuale().isNull())			
			infoPersonali.getFasciaRedditoAnnuale().addTypeError("err.fasciaRedditoAnnualeObbligatoria");			
		
		if(infoPersonali.getCombinazioneProvenienzaPatrimonio().isNull())
			infoPersonali.getCombinazioneProvenienzaPatrimonio().addTypeError("err.almenoUnaProvenienzaPatrimonio");

		// Professioni
		if(infoPersonali.getCodProfessione().isNull())
			infoPersonali.getCodProfessione().addTypeError("err.professioneObbl");
		
		if(infoPersonali.getCodProfessionePrecedente().isNull())
			infoPersonali.getCodProfessionePrecedente().addTypeError("err.professionePrecedenteObbl");

		if(infoPersonali.getNazioneSvolgimentoProfessione().isNull())
			infoPersonali.getNazioneSvolgimentoProfessione().addTypeError("err.luogoProfessioneObbl");
		
		// Se la professione è "studente" verifico che l'universita' sia compilata tutta o vuota 
		if(infoPersonali.getCodProfessione().equals(Costanti.CODICE_PROFESSIONE_STUDENTE))
			controllaInfoPersonaliUniversita(infoPersonali);
		
		// TAE			
		if(infoPersonali.getCodSettoreEconomico().isNull())
			infoPersonali.getCodSettoreEconomico().addTypeError("err.taeObbl");			

		// Provincia svolgimento attività obbligatoria (se Italia)
		if(infoPersonali.getNazioneSvolgimentoProfessione().equals(Costanti.COD_UIC_NAZIONE_ITALIA) &&
		   infoPersonali.getProvinciaSvolgimentoProfessione().isNull())
			infoPersonali.getProvinciaSvolgimentoProfessione().addTypeError("err.provProfessioneObbl");
	}

	/***********************************************************************************************/
	private static final Map<String, Double> IMPORTI_MASSIMI_FASCE_PATRIMONIALI = new HashMap<String, Double>();
	static {
		IMPORTI_MASSIMI_FASCE_PATRIMONIALI.put("00001",15000D);
		IMPORTI_MASSIMI_FASCE_PATRIMONIALI.put("00002",30000D);
		IMPORTI_MASSIMI_FASCE_PATRIMONIALI.put("00003",100000D);
		IMPORTI_MASSIMI_FASCE_PATRIMONIALI.put("00004",500000D);
		IMPORTI_MASSIMI_FASCE_PATRIMONIALI.put("00005",2000000D);
	};
	/***********************************************************************************************/
	private void controllaFasciaPatrimonio(ClientSessionContext csc, ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();
		Double importoMassimoFascia = IMPORTI_MASSIMI_FASCE_PATRIMONIALI.get(infoPersonali.getFasciaPatrimonioComplessivo().toString());
		if(importoMassimoFascia == null)
			return;
			
		try {
			MapCommandDataModel wsModel = new MapCommandDataModel();
			wsModel.addProperty("utente", new StringType(csc.getUserCode()));
			wsModel.addProperty("ndgCliente", model.getCodMediolanum());
			new DAOObject(csc, DAO_XML_VERIFICHE).executeOSBAccess("getPatrimonioCliente", wsModel);
			StringType totalePatrimonioMediolanum = (StringType)wsModel.readProperty("totalePatrimonioMediolanum");
			if(totalePatrimonioMediolanum == null || totalePatrimonioMediolanum.isNull()) {
				infoPersonali.getFasciaPatrimonioComplessivo().addTypeWarning("war.erroreRecuperoPatrimonioCliente");		
				return;
			}		
			double totalePatrimonioMediolanumAsDouble = Double.parseDouble(totalePatrimonioMediolanum.toString());
			if(importoMassimoFascia < totalePatrimonioMediolanumAsDouble)
				infoPersonali.getFasciaPatrimonioComplessivo().addTypeWarning("war.fasciaPatrimonioComplessivoFuoriRange");	
			
		}catch(Exception | DAOException e) {
			infoPersonali.getFasciaPatrimonioComplessivo().addTypeWarning("Eccezione nel recupero del patrimonio: "+e.toString());					
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaInfoDitta(ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();

		// Andrebbe controllato per le ditte e non per i lib prof. ma non l'hanno chiesto
		/*
		xif infoDitta.getNumeroIscrizioneREA().isNull())
			infoDitta.getNumeroIscrizioneREA().addTypeError("Specificare il numero di iscrizione REA")
		
		xif infoDitta.getLuogoRilascioCCIAA().getProvincia().isNull())
			infoDitta.getLuogoRilascioCCIAA().getProvincia().addTypeError("Specificare la provincia CCIAA")
		
		xif infoDitta.getDataIscrizione().isNull())
			infoDitta.getDataIscrizione().addTypeError("Specificare la data di iscrizione")
		*/

		if(infoPersonali.getCodSottogruppoAttivita().isNull())
			infoPersonali.getCodSottogruppoAttivita().addTypeError("err.sottogruppoAttivitaObbl");

		// Codice ATECO (solo in censimento)
		if (model.getIsPotenziale().booleanValue()) {
			if(infoPersonali.getCodAteco().isNull())
				infoPersonali.getCodAteco().addTypeError("err.atecoObbl");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiFormaGiuridica(InfoPersonaliModel infoPersonali, AdempimentiNormativiModel adempimentiNormativi){
		if((infoPersonali.getCodProfessione().equals(Costanti.CODICE_PROFESSIONE_DIPENDENTE_DIRIGENTE) ||
			infoPersonali.getCodProfessione().equals(Costanti.CODICE_PROFESSIONE_AUTONOMO_IMPRENDITORE) ||
			infoPersonali.getCodProfessione().equals(Costanti.CODICE_PROFESSIONE_SOGGETTO_APICALE)) && 
			adempimentiNormativi.getFormaGiuridicaSocietaAppartenenza().isNull())
			adempimentiNormativi.getFormaGiuridicaSocietaAppartenenza().addTypeError(S_ERR_DATOOBBLIGATORIO);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiCarichePubbliche(AdempimentiNormativiModel adempimentiNormativi){
		if(adempimentiNormativi.getHaCarichePubbliche().isNull()) {
			adempimentiNormativi.getHaCarichePubbliche().addTypeError(S_ERR_DATOOBBLIGATORIO);
		}else if(adempimentiNormativi.getHaCarichePubbliche().equals("S")) {
			if(adempimentiNormativi.getCaricaPubblicaRicoperta().isNull()) {
				adempimentiNormativi.getCaricaPubblicaRicoperta().addTypeError(S_ERR_DATOOBBLIGATORIO);
			}else{
				if(adempimentiNormativi.getCaricaPubblicaRicoperta().equals(Costanti.CODICE_CARICA_PUBBLICA_RICOPERTA_POLITICO_ISTITUZIONALE) &&
				   adempimentiNormativi.getDettaglioCaricaPubblicaRicoperta().isNull())
					adempimentiNormativi.getDettaglioCaricaPubblicaRicoperta().addTypeError(S_ERR_DATOOBBLIGATORIO);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiPEP(ClienteModel model, ResidenzaModel residenza){
		
		if(model.getIsPotenziale().booleanValue()){
			if(residenza.getFlagPep().isNull()){
				residenza.getFlagPep().addTypeError("err.flagPepObbl");
			}else if(!residenza.getFlagPep().equals("N") && residenza.getMotivazionePep().isNull()){
				residenza.getMotivazionePep().addTypeError("err.motivazionePepObbl");
			}
			return;
		}
		
		if(residenza.getFlagPep().isNull()){
			residenza.getFlagPep().addTypeError("err.flagPepObbl");
		}else{
			// In variazione controlliamo l'obbligatorietà della motivazione PEP
			// solo se il flag da "N", o "null", diventa "Si" (i valori di "Si" sono enne)
			if(model.getDatiApplicativi().getClienteCaricato() != null){
				StringType originalFlagPep = model.getDatiApplicativi().getClienteCaricato().getResidenza().getFlagPep();
				if( (originalFlagPep.isNull() || originalFlagPep.equalsIgnoreCase("N")) &&
					(residenza.getMotivazionePep().isNull() && !residenza.getFlagPep().equals("N"))) {
					residenza.getMotivazionePep().addTypeError("err.motivazionePepObbl");
				}
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiRelazioniAffari(AdempimentiNormativiModel adempimentiNormativi){
		if(adempimentiNormativi.getHaLegamiAffariDiversiDaAttivitaPrincipale().isNull()){
			adempimentiNormativi.getHaLegamiAffariDiversiDaAttivitaPrincipale().addTypeError(S_ERR_DATOOBBLIGATORIO);
		}else if(adempimentiNormativi.getHaLegamiAffariDiversiDaAttivitaPrincipale().equals("S")){
			if(adempimentiNormativi.getTipologiaLegameAffariDiversoDaAttivitaPrincipale().isNull())
				adempimentiNormativi.getTipologiaLegameAffariDiversoDaAttivitaPrincipale().addTypeError(S_ERR_DATOOBBLIGATORIO);
			if( adempimentiNormativi.getPaeseLegameAffariDiversoDaAttivitaPrincipale1().isNull() &&
				adempimentiNormativi.getPaeseLegameAffariDiversoDaAttivitaPrincipale2().isNull() &&
				adempimentiNormativi.getPaeseLegameAffariDiversoDaAttivitaPrincipale3().isNull())
				adempimentiNormativi.getPaeseLegameAffariDiversoDaAttivitaPrincipale1().addTypeError("err.almenounPaeseLegameAffariDiversoDaAttivitaPrincipale");
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiLegamiParentelaPEP(AdempimentiNormativiModel adempimentiNormativi){
		if(adempimentiNormativi.getHaLegamiParentelaConPep().isNull()){
			adempimentiNormativi.getHaLegamiParentelaConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
		}else if(!adempimentiNormativi.getHaLegamiParentelaConPep().equals("N")){		
			if( adempimentiNormativi.getTipologiaLegameParentelaConPep().isNull() && adempimentiNormativi.getTipologiaFunzionePubblicaLegameParentelaConPep().isNull()){
				adempimentiNormativi.getTipologiaLegameParentelaConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
				adempimentiNormativi.getTipologiaFunzionePubblicaLegameParentelaConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
			}else{
				// Sulla riga, se compilato uno lo deve essere anche l'altro 
				if(!adempimentiNormativi.getTipologiaLegameParentelaConPep().isNull() && adempimentiNormativi.getTipologiaFunzionePubblicaLegameParentelaConPep().isNull())
					adempimentiNormativi.getTipologiaFunzionePubblicaLegameParentelaConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
				if(adempimentiNormativi.getTipologiaLegameParentelaConPep().isNull() && !adempimentiNormativi.getTipologiaFunzionePubblicaLegameParentelaConPep().isNull())
					adempimentiNormativi.getTipologiaLegameParentelaConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
					
			}		
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativiLegamiAffariPEP(AdempimentiNormativiModel adempimentiNormativi){
		if(adempimentiNormativi.getHaLegamiAffariConPep().isNull()){
			adempimentiNormativi.getHaLegamiAffariConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
		}else if(!adempimentiNormativi.getHaLegamiAffariConPep().equals("N")){		
			if( adempimentiNormativi.getTipologiaLegameAffariConPep().isNull() && adempimentiNormativi.getTipologiaFunzionePubblicaLegameAffariConPep().isNull()){
				adempimentiNormativi.getTipologiaLegameAffariConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
				adempimentiNormativi.getTipologiaFunzionePubblicaLegameAffariConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
			}else{
				// Sulla riga, se compilato uno lo deve essere anche l'altro 
				if(!adempimentiNormativi.getTipologiaLegameAffariConPep().isNull() && adempimentiNormativi.getTipologiaFunzionePubblicaLegameAffariConPep().isNull())
					adempimentiNormativi.getTipologiaFunzionePubblicaLegameAffariConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
				if(adempimentiNormativi.getTipologiaLegameAffariConPep().isNull() && !adempimentiNormativi.getTipologiaFunzionePubblicaLegameAffariConPep().isNull())
					adempimentiNormativi.getTipologiaLegameAffariConPep().addTypeError(S_ERR_DATOOBBLIGATORIO);
			}		
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaAdempimentiNormativi(ClienteModel model){
		
		InfoPersonaliModel infoPersonali = model.getInfoPersonali();
		AdempimentiNormativiModel adempimentiNormativi = model.getAdempimentiNormativi();
		ResidenzaModel residenza = model.getResidenza();

		// Forma giuridica
		controllaAdempimentiNormativiFormaGiuridica(infoPersonali, adempimentiNormativi);
					
		// Cariche pubbliche
		controllaAdempimentiNormativiCarichePubbliche(adempimentiNormativi);
		
		// PEP 
		controllaAdempimentiNormativiPEP(model, residenza);
		
		// Relazioni d'affari
		controllaAdempimentiNormativiRelazioniAffari(adempimentiNormativi);
		
		// Legami di parentela PEP
		controllaAdempimentiNormativiLegamiParentelaPEP(adempimentiNormativi);

		// Legami d'affari PEP
		controllaAdempimentiNormativiLegamiAffariPEP(adempimentiNormativi);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDocumento(ClientSessionContext csc, ClienteModel model) throws Exception{
		
	    DocumentoModel documento = model.getDocumento();
			
		// Tipo documento obbligatorio
		if(documento.getTipoDocumento().isNull())
			documento.getTipoDocumento().addTypeError("err.tipoDocObbl");

		// Numero documento obbligatorio
		if(documento.getNumeroDocumento().isNull())
			documento.getNumeroDocumento().addTypeError("err.numeroDocObbl");

		// Luogo rilascio obbligatorio
		if(documento.getLuogoRilascio().getComune().isNull())
			documento.getLuogoRilascio().getComune().addTypeError("err.luogoRilascioDocObbl");

		// Data rilascio obbligatoria
		if(documento.getDataRilascio().isNull())
			documento.getDataRilascio().addTypeError("err.dataRilascioDocObbl");
		
		// Data scadenza obbligatoria
		if(documento.getDataScadenza().isNull())
			documento.getDataScadenza().addTypeError("err.dataScadenzaDocObbl");
		
		return;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaDatiPrivacy(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		if(model.isDigitale()) // In digitale la privacy non viene chiesta a front-end
			return;
		
		// #119648 - Tutti i flag sono sempre obbligatori in censimento e variazione in FO
		DatiPrivacyModel datiPrivacy = model.getDatiPrivacy();
		if(datiPrivacy.getFlagCarte().isNull())
			datiPrivacy.getFlagCarte().addTypeError("err.flagCarteObbl");
		if(datiPrivacy.getFlagLiberatoria().isNull())
			datiPrivacy.getFlagLiberatoria().addTypeError("err.flagLiberatoriaObbl");
		if(datiPrivacy.getFlagExtraUE().isNull())
			datiPrivacy.getFlagExtraUE().addTypeError("err.flagExtraUEObbl");
		if(datiPrivacy.getFlagProfilazione().isNull())
			datiPrivacy.getFlagProfilazione().addTypeError("err.flagProfilazioneObbl");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaPrefissi(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		TelefonoModel telefonoResidenza = model.getRecapiti().getTelefonoResidenza();
		TelefonoModel telefonoCellulare = model.getRecapiti().getTelefonoCellulare();
		TelefonoModel fax = model.getRecapiti().getTelefonoFax();
	
		if(!telefonoResidenza.getNumeroTelefono().isNull() && telefonoResidenza.getPrefisso().isNull())
			telefonoResidenza.getPrefisso().addTypeError("err.prefTelObbl");
		
		if(!telefonoCellulare.getNumeroTelefono().isNull() && telefonoCellulare.getPrefisso().isNull())
			telefonoCellulare.getPrefisso().addTypeError("err.prefTelObbl");
		
		if(!fax.getNumeroTelefono().isNull() && fax.getPrefisso().isNull())
			fax.getPrefisso().addTypeError("err.prefTelObbl");
		
		return;
		
	}
	
}
