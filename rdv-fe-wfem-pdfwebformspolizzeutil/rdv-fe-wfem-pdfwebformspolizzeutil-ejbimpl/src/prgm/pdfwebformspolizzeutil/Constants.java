package prgm.pdfwebformspolizzeutil;

import com.atosorigin.wfem.types.StringType;

public class Constants {
	private Constants() {
		throw new IllegalStateException("Utility class");
	}
	
	public static final String TIPO_CASO_BENEFICARIO_VITA = "VIT";
	public static final String TIPO_CASO_BENEFICARIO_DECESSO = "DEC";
	
	/*
	 * Prefissi campi PDF
	 */
	public static final String TITOLARE = "Titolare";
	public static final String PREFISSO_BENEFICIARIO_VITA = "BeneficiarioVita";
	public static final String PREFISSO_BENEFICIARIO_DECESSO = "Beneficiario";
	public static final String REFERENTE_TERZO = "ReferenteTerzo";
	public static final String PEP = "Pep";
	public static final String LEGALE_RAPPR_PROCURATORE = "LegaleRapprProcuratore";

	
	/*
	 * Varie
	 */
	public static final String TIPO_BENEFICIARIO_ALTRO = "031";
    public static final String TIPO_RELAZIONE_ALTRO = "004";
    
    public static final String TIPO_SOGGETTO_CLIENTE = "2";
    public static final String TIPO_SOGGETTO_PROSPECT = "3";

    public static final String CODICE_RUOLO_SOGGETTO_TITOLARE = "28";
    public static final String CODICE_RUOLO_SOGGETTO_BENEFICIARIO = "28";

    public static final String MOM_CODE_BARCODE = "GE14";
    public static final String MOM_VERSION_BARCODE = "L19";

    public static final String NAZIONE_ITALIA = "ITALIA";

    /*
     * Messaggi di errore
     */
	public static final String MSG_ERRORE_CAMPO_OBBLIGATORIO = "Campo obbligatorio.";
	public static final String MSG_ERRORE_VALORE_NON_AMMESSO = "Valore non ammesso.";
    public static final String MSG_ERRORE_FORMATO_NON_CORRETTO        		    = "Formato non corretto";
	public static final String MSG_ERRORE_LUNGHEZZA_DESCRIZIONE_RELAZIONE_ERRATA 				= "Il campo può contenere al massimo 50 caratteri.";
	public static final String MSG_ERRORE_BENEFICIARIO_GIA_PRESENTE = "E' presente un altro beneficiario con lo stesso codice fiscale/partita IVA.";
	public static final String MSG_ERRORE_DATA_ISCRIZIONE_CCIAA = "La data di iscrizione inserita non può essere successiva alla data odierna.";
	public static final String MSG_ERRORE_DATA_NASCITA = "La data di nascita inserita deve essere antecedente alla data odierna.";

	public static final String MSG_ERRORE_CENSIMENTO_ANAGRAFICA = "L'anagrafica del cliente è incompleta, è necessario procedere con un aggiornamento della stessa prima di censirlo come beneficiario della polizza.";
	public static final String MSG_ERRORE_COERENZA_REFERENTE_TERZO = "Il referente terzo non può coincidere con il contraente, con l'assicurando o eventuali beneficiari censiti in forma nominativa.";
	public static final String MSG_ERRORE_PERCENTUALE_NON_AMMESSA = "La percentuale dev'essere maggiore di zero e minore o uguale a 100.";


	
	 /*
     * Messaggi di warning
     */
	public static final String MSG_WARNING_RELAZIONE_ALTRO = "Ti ricordiamo che in caso di relazione ALTRO tra beneficiario e contraente, è obbligatorio procedere al censimento completo del beneficiario tramite scheda anagrafica debitamente firmata. La presenza della scheda anagrafica non è vincolante alla sottoscrizione della presente polizza, ma potrà essere inviata in Sede anche in un momento successivo";
	/*
	 * Nuovi warning per MOP
	 */
	public static final String MSG_WARNING_RELAZIONE_ALTRO_NO_CENSITO_MODULO_AV = "Ti ricordiamo che in caso di relazione ALTRO tra beneficiario e contraente, è obbligatorio procedere al censimento completo del beneficiario tramite scheda anagrafica debitamente firmata. La presenza della scheda anagrafica non è vincolante in caso di sottoscrizione polizza, ma potrà essere inviata in Sede anche in un momento successivo.  Inoltre ti ricordiamo che il campo riferito alla relazione tra contraente e beneficiario è valorizzato con la relazione '4 - altro'. Ricordati di allegare il modulo AV Operazione.";
	public static final String MSG_WARNING_RELAZIONE_ALTRO_MODULO_AV = "Il campo riferito alla relazione tra contraente e beneficiario è valorizzato '4 - altro'. Ricordati che è richiesta l'identificazione ed il censimento anagrafico completo del beneficiario.";

	public static final String MSG_WARNING_RELAZIONE_ASSICURANDO_ALTRO_NO_CENSITO_MODULO_AV = "Ti ricordiamo che in caso di relazione ALTRO tra beneficiario e assicurando, è obbligatorio procedere al censimento completo del beneficiario tramite scheda anagrafica debitamente firmata. La presenza della scheda anagrafica non è vincolante alla sottoscrizione della presente polizza, ma potrà essere inviata in Sede anche in un momento successivo. Inoltre ti ricordiamo che il campo riferito alla relazione tra assicurando  e beneficiario è valorizzato con la relazione '4 - altro'. Ricordati di allegare il modulo AV Operazione.";
	public static final String MSG_WARNING_RELAZIONE_ASSICURANDO_ALTRO_MODULO_AV = "Il campo riferito alla relazione tra assicurando e beneficiario è valorizzato con la relazione '4 - altro'. Ricordati di allegare il modulo AV Operazione.";
	
	
	/*
	 * Base name campi PDF
	 */
	public static final String TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF = "tipo";
	public static final String NDG_CLIENTE_BASE_FIELD_NAME_PDF = "ndgCliente";
	public static final String IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF = "isPersonaFisica";
	public static final String IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF = "isGiaCliente";
	public static final String PERCENTUALE_BASE_FIELD_NAME_PDF = "percentuale";
	public static final String CODICE_PROSPECT_BASE_FIELD_NAME_PDF = "codiceProspect";
	public static final String BAR_CODE_FIELD_NAME_PDF = "censimentoAnagraficoBr"; //barcode : non usiamo il termine bar code per evitare possibili conflitti con terminologie del motore
	public static final String IS_SOCIETA_FIDUCIARIA_CLIENTE_BASE_FIELD_NAME_PDF = "isSocietaFiduciariaCliente";



	public static final String NOME_BASE_FIELD_NAME_PDF = "nome";
	public static final String COGNOME_BASE_FIELD_NAME_PDF = "cognome";
	public static final String CODICE_CLIENTE_BASE_FIELD_NAME_PDF = "codiceCliente";


	public static final String CODICE_FISCALE_BASE_FIELD_NAME_PDF = "codiceFiscale";
	public static final String SESSO_BASE_FIELD_NAME_PDF = "sesso";
	public static final String DATA_NASCITA_BASE_FIELD_NAME_PDF = "dataNascita";
	public static final String COMUNE_NASCITA_BASE_FIELD_NAME_PDF = "comuneNascita";
	public static final String PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF = "provinciaComuneNascita";
	public static final String NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF = "nazioneComuneNascita";

	public static final String COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF = "codToponimoIndirizzo";
	public static final String TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF = "toponimoIndirizzo";
	public static final String INDIRIZZO_BASE_FIELD_NAME_PDF = "indirizzo";
	public static final String NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF = "numeroCivicoIndirizzo";
	public static final String CAP_COMUNE_BASE_FIELD_NAME_PDF = "capComune";
	public static final String COMUNE_BASE_FIELD_NAME_PDF = "comune";
	public static final String PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF = "provinciaComune";
	public static final String NAZIONE_COMUNE_BASE_FIELD_NAME_PDF = "nazioneComune";

	public static final String CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF = "codiceFiscalePartitaIva";
	public static final String RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF = "ragioneSociale";
	public static final String NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF = "numeroIscrizioneCCIAA";
	public static final String DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF = "dataIscrizioneCCIAA";
	public static final String PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF = "provinciaIscrizioneCCIAA";

	public static final String EMAIL_BASE_FIELD_NAME_PDF = "email";

	public static final String PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF = "prefissoInternazionaleTelefono";
	public static final String PREFISSO_TEL_BASE_FIELD_NAME_PDF = "prefissoTelefono";
	public static final String NUMERO_TEL_BASE_FIELD_NAME_PDF = "telefono";
	public static final String TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF = "tipoTelefono";
	
	public static final String TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF = "tipoRelazioneContraente";
	public static final String DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF = "descrizioneTipoRelazioneContraente";
	public static final String TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF = "tipoRelazioneAssicurando";
	public static final String DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF = "descrizioneTipoRelazioneAssicurando";
	
	public static final String MOTIVAZIONE_BASE_FIELD_NAME_PDF = "motivazione";
	public static final String INVIO_COMUNICAZIONE_BASE_FIELD_NAME_PDF = "invioComunicazione";

	public static final String IS_PEP_BASE_FIELD_NAME_PDF = "isPep";
	public static final String INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF = "indiceMotivazionePep";

	/*
	 * Campi modello database
	 */
	public static final String NOME = "nome";
	public static final String COGNOME = "cognome";
	public static final String CODICE_FISCALE = "codiceFiscale";
	public static final String CODICE_CLIENTE = "codiceCliente";

	public static final String SESSO  = "sesso";
	public static final String DATA_NASCITA  = "dataNascita";
	public static final String COMUNE_NASCITA  = "comuneNascita";
	public static final String PROVINCIA_COMUNE_NASCITA  = "provinciaComuneNascita";
	public static final String NAZIONE_COMUNE_NASCITA  = "nazioneComuneNascita";

	public static final String COD_TOPONIMO_INDIRIZZO  = "codToponimoIndirizzo";
	public static final String TOPONIMO_INDIRIZZO  = "toponimoIndirizzo";
	public static final String INDIRIZZO  = "indirizzo";
	public static final String NUMERO_CIVICO_INDIRIZZO  = "numeroCivicoIndirizzo";
	public static final String CAP_COMUNE  = "capComune";
	public static final String COMUNE  = "comune";
	public static final String PROVINCIA_COMUNE  = "provinciaComune";
	public static final String NAZIONE_COMUNE  = "nazioneComune";

	public static final String RAGIONE_SOCIALE  = "ragioneSociale";
	public static final String NUMERO_ISCRIZIONE_CCIAA  = "numeroIscrizioneCCIAA";
	public static final String DATA_ISCRIZIONE_CCIAA  = "dataIscrizioneCCIAA";
	public static final String PROVINCIA_ISCRIZIONE_CCIAA  = "provinciaIscrizioneCCIAA";

	public static final String EMAIL  = "email";

	public static final String PREFISSO_INTERNAZIONALE_TEL  = "prefissoInternazionaleTelefono";
	public static final String PREFISSO_TEL  = "prefissoTelefono";
	public static final String NUMERO_TEL  = "telefono";
	public static final String TIPO_RECAPITO_TEL  = "tipoTelefono";


	public static final String[] BENEFICIARI_BASE_FIELD_NAME_PDF_PF = { NOME_BASE_FIELD_NAME_PDF, COGNOME_BASE_FIELD_NAME_PDF, CODICE_FISCALE_BASE_FIELD_NAME_PDF, CODICE_CLIENTE_BASE_FIELD_NAME_PDF,
			SESSO_BASE_FIELD_NAME_PDF, DATA_NASCITA_BASE_FIELD_NAME_PDF, COMUNE_NASCITA_BASE_FIELD_NAME_PDF, PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF, NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF, COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF, TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			INDIRIZZO_BASE_FIELD_NAME_PDF, NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF, CAP_COMUNE_BASE_FIELD_NAME_PDF, COMUNE_BASE_FIELD_NAME_PDF, PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF, NAZIONE_COMUNE_BASE_FIELD_NAME_PDF, EMAIL_BASE_FIELD_NAME_PDF,
			PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF, PREFISSO_TEL_BASE_FIELD_NAME_PDF, NUMERO_TEL_BASE_FIELD_NAME_PDF, TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF };

	public static final String[] BENEFICIARI_FIELD_DB_MODEL_PF = { NOME, COGNOME, CODICE_FISCALE, CODICE_CLIENTE, SESSO,
			DATA_NASCITA, COMUNE_NASCITA, PROVINCIA_COMUNE_NASCITA, NAZIONE_COMUNE_NASCITA, COD_TOPONIMO_INDIRIZZO, TOPONIMO_INDIRIZZO,
			INDIRIZZO, NUMERO_CIVICO_INDIRIZZO, CAP_COMUNE, COMUNE, PROVINCIA_COMUNE, NAZIONE_COMUNE, EMAIL,
			PREFISSO_INTERNAZIONALE_TEL, PREFISSO_TEL, NUMERO_TEL, TIPO_RECAPITO_TEL };

	public static final String[] BENEFICIARI_BASE_FIELD_NAME_PDF_PG = { CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF, CODICE_CLIENTE_BASE_FIELD_NAME_PDF, RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF,
			NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF, DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF, PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF, COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF, TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF, INDIRIZZO_BASE_FIELD_NAME_PDF,
			NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF, CAP_COMUNE_BASE_FIELD_NAME_PDF, COMUNE_BASE_FIELD_NAME_PDF, PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF, NAZIONE_COMUNE_BASE_FIELD_NAME_PDF, EMAIL_BASE_FIELD_NAME_PDF,
			PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF, PREFISSO_TEL_BASE_FIELD_NAME_PDF, NUMERO_TEL_BASE_FIELD_NAME_PDF, TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF };

	public static final String[] BENEFICIARI_FIELD_DB_MODEL_PG = { CODICE_FISCALE, CODICE_CLIENTE, RAGIONE_SOCIALE,
			NUMERO_ISCRIZIONE_CCIAA, DATA_ISCRIZIONE_CCIAA, PROVINCIA_ISCRIZIONE_CCIAA, COD_TOPONIMO_INDIRIZZO, TOPONIMO_INDIRIZZO, INDIRIZZO,
			NUMERO_CIVICO_INDIRIZZO, CAP_COMUNE, COMUNE, PROVINCIA_COMUNE, NAZIONE_COMUNE, EMAIL,
			PREFISSO_INTERNAZIONALE_TEL, PREFISSO_TEL, NUMERO_TEL, TIPO_RECAPITO_TEL };
	
	public static final String[] BENEFICIARI_BASE_FIELD_NAME_PDF_TIT = { NOME_BASE_FIELD_NAME_PDF, COGNOME_BASE_FIELD_NAME_PDF, CODICE_FISCALE_BASE_FIELD_NAME_PDF, CODICE_CLIENTE_BASE_FIELD_NAME_PDF,
			SESSO_BASE_FIELD_NAME_PDF, DATA_NASCITA_BASE_FIELD_NAME_PDF, COMUNE_NASCITA_BASE_FIELD_NAME_PDF, PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF, NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF, COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF, TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			INDIRIZZO_BASE_FIELD_NAME_PDF, NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF, CAP_COMUNE_BASE_FIELD_NAME_PDF, COMUNE_BASE_FIELD_NAME_PDF, PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF, NAZIONE_COMUNE_BASE_FIELD_NAME_PDF, EMAIL_BASE_FIELD_NAME_PDF,
			PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF, PREFISSO_TEL_BASE_FIELD_NAME_PDF, NUMERO_TEL_BASE_FIELD_NAME_PDF, TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF };

	public static final String[] BENEFICIARI_FIELD_DB_MODEL_TIT = { NOME, COGNOME, CODICE_FISCALE, CODICE_CLIENTE, SESSO,
			DATA_NASCITA, COMUNE_NASCITA, PROVINCIA_COMUNE_NASCITA, NAZIONE_COMUNE_NASCITA, COD_TOPONIMO_INDIRIZZO, TOPONIMO_INDIRIZZO,
			INDIRIZZO, NUMERO_CIVICO_INDIRIZZO, CAP_COMUNE, COMUNE, PROVINCIA_COMUNE, NAZIONE_COMUNE, EMAIL,
			PREFISSO_INTERNAZIONALE_TEL, PREFISSO_TEL, NUMERO_TEL, TIPO_RECAPITO_TEL  };

	public static final String[] DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PF = {NOME, COGNOME, CODICE_FISCALE};
	public static final String[] DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PG = {RAGIONE_SOCIALE, CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF};
	
	/**
	 * Costanti censimento anagrafico
	 */
	
	public static final StringType CA_TIPOLOGIA_PERSONA_FISICA = new StringType("F");
	public static final StringType CA_TIPOLOGIA_PERSONA_GIURIDICA = new StringType("G");
	
	public static final StringType CA_SESSO_MASCHIO = new StringType("M");
	public static final StringType CA_SESSO_FEMMINA = new StringType("F");

	public static final StringType CA_TIPO_SCHEDA_ANAGRAFICA_BENEFICIARIO = new StringType("B");
	public static final StringType CA_TIPO_SCHEDA_ANAGRAFICA_TITOLARE = new StringType("T");

	public static final StringType CA_CANALE_CENSIMENTO_RETE = new StringType("INF");
	public static final StringType CA_SISTEMAORIGINE_CENSIMENTO_RETE = new StringType("INF");
	
	public static final StringType CA_PROVINCIA_ESTERA = new StringType("EE");
	
	public static final String TIPO_SOGGETTO_PF = "PF";
	public static final String TIPO_SOGGETTO_PG = "PG";

}
