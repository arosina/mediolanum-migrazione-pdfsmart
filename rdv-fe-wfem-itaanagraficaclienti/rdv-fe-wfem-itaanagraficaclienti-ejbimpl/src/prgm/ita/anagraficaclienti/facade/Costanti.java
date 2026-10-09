package prgm.ita.anagraficaclienti.facade;

/***********************************************************************************************/
/***********************************************************************************************/
public class Costanti {
	public static final String 	CALLING_APPL_MHD = "MHD";
	public static final String 	CALLING_APPL_TOOLPREVIDENZA = "TOOLPREVIDENZA";
	public static final String 	CALLING_APPL_TOOLPROTEZIONE = "TOOLPROTEZIONE";
	public static final String 	CALLING_APPL_TOOLDOSSIERTITOLI = "TOOLDOSSIERTITOLI";

	public static final String 	NOME_RISORSA = "ANAGRAFICA_CLIENTI";
	public static final String 	NOME_RISORSA_FOTO_CLIENTE = "FOTOGRAFIE_CLIENTI";
	
	public static final String 	VALORE_FLAG_STATO_CONFERMATO = "P";

	public static final String 	PARAM_RUNTIME_MODALITY_SIMPLE = "simple";
	public static final String 	PARAM_RUNTIME_MODALITY_FULL = "full";

	public static final String 	TIPO_PERSONA_FISICA 	= "F";
	public static final String 	TIPO_PERSONA_GIURIDICA 	= "G";

	public static final String 	NATURA_GIURIDICA_PERSONA_MASCHIO 	= "FMM";
	public static final String 	NATURA_GIURIDICA_PERSONA_FEMMINA 	= "FFF";
	public static final String 	NATURA_GIURIDICA_DITTA_MASCHIO 		= "SMM";
	public static final String 	NATURA_GIURIDICA_DITTA_FEMMINA 		= "SFF";
	public static final String 	NATURA_GIURIDICA_DATORE_DI_LAVORO 	= "ESP";
	
	public static final String 	SESSO_MASCHIO = "M";
	public static final String 	SESSO_FEMMINA = "F";
	public static final String 	SESSO_SOCIETA = "S";
	
	public static final int 	PROGRESSIVO_DI_SEDE = 1000;

	public static final int 	STAMPA_FACSIMILE_CENSIMENTO = 1;
	public static final int 	STAMPA_CENSIMENTO 			= 2;
	public static final int 	STAMPA_VARIAZIONE 			= 3;

	public static final String 	SERVER_REPLICA_NULLO = "0";

	public static final String 	COD_INDIRIZZO_RESIDENZA = "01";
	public static final String 	COD_INDIRIZZO_DOMICILIO = "02";
	
	public static final String 	COD_TELEFONO_RESIDENZA 	= "01";
	public static final String 	COD_TELEFONO_DOMICILIO 	= "02";
	public static final String 	COD_TELEFONO_FAX 		= "13";
	public static final String 	COD_TELEFONO_CELLULARE 	= "14";

	public static final String 	PREFIX_INTERNAZIONALE_ITALIA = "0039";
	public static final String 	COD_NAZIONE_ITALIA = "I";
	public static final String 	COD_UIC_NAZIONE_ITALIA = "000";
	
	public static final String 	COD_NAZIONE_US = "USA";
	public static final String 	COD_NAZIONE_US_UIC = "069";

	public static final String 	  TIMESTAMP_PRE = "01-01-2000 ";
	public static final String 	  TIMESTAMP_SUF = ":00.000000000";
	
	public static final String[]  ORE = new String[]{  "00:00","00:30","01:00","01:30","02:00","02:30","03:00","03:30",
													   "04:00","04:30","05:00","05:30","06:00","06:30","07:00","07:30",
													   "08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30",
													   "12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30",
													   "16:00","16:30","17:00","17:30","18:00","18:30","19:00","19:30",
													   "20:00","20:30","21:00","21:30","22:00","22:30","23:00","23:30" };
	
	public static final String[]  TIPI_PATENTE = new String[]{"A","B","C","D","E","F"};
	public static final String    TIPO_DOCUMENTO_PATENTE = "P";
	public static final String    TIPO_DOCUMENTO_CARTA_IDENTITA = "C";
	public static final String    TIPO_DOCUMENTO_CARTA_IDENTITA_ESTERA = "H";
	public static final String    TIPO_DOCUMENTO_PASSAPORTO_ITALIANO = "T";
	public static final String    TIPO_DOCUMENTO_PASSAPORTO_ESTERO = "K";
	public static final String    TIPO_DOCUMENTO_LIBRETTO_PENSIONE = "L";
	public static final String    TIPO_DOCUMENTO_CERTIFICATO_NASCITA = "Y";

	
	public static final String    TIPO_TELEFONO_UFFICIO 	= "03";
	public static final String    TIPO_TELEFONO_CELLULARE 	= "07";
	public static final String    TIPO_TELEFONO_FAX_CASA	= "08";
	public static final String    TIPO_TELEFONO_FAX_UFFICIO = "09";

	public static final String    TIPO_DITTA_DITTA 		= "D";
	public static final String    TIPO_DITTA_LIBPROF 	= "L";

	public static final String    FLAG_CODICE_FISCALE_FORZATO = "001";

	public static final String 	  STATO_CIVILE_CELIBE = "1";
	public static final String 	  STATO_CIVILE_VEDOVO = "3";

	public static final String    CODICE_ORIGINE_LINEABLU 			= "006";	
	public static final String    CODICE_ORIGINE_MEMBER_GET_MEMBER 	= "MGM";	
	public static final String    CODICE_ORIGINE_MFORYOU		 	= "M4U";	
	public static final String    CODICE_ORIGINE_GLOBAL_SPECIALIST 	= "GBS";	
	public static final String    CODICE_ORIGINE_IMF		 		= "IMF";	

	public static final String    CODICE_SOTTOGRUPPO_ATTIVITA_DATORI_DI_LAVORO			= "430";
	public static final String    CODICE_SOTTOGRUPPO_ATTIVITA_CONSUMATORI 				= "600";
	public static final String    CODICE_SOTTOGRUPPO_FAMIGLIE_CONSUMATRICI_PAESI_UE 	= "773";	
	public static final String    CODICE_SOTTOGRUPPO_FAMIGLIE_CONSUMATRICI_MEMBRI_U 	= "774";	
	public static final String    CODICE_SOTTOGRUPPO_FAMIGLIE_CONSUMATRICI_PAESI_NO 	= "775";

	public static final String    CODICE_GRUPPO_ATTIVITA_DATORI_DI_LAVORO				= "984";
	
	public static final String    CODICE_ATECO_NESSUN_CODICE							= "0";
		
	public static final String    CODICE_PROFESSIONE_CASALINGA	 = "24";
	public static final String    CODICE_PROFESSIONE_PENSIONATO	 = "25";
	public static final String    CODICE_PROFESSIONE_STUDENTE	 = "57";
	public static final String    CODICE_PROFESSIONE_DISOCCUPATO = "48";
	public static final String    CODICE_PROFESSIONE_BENESTANTE  = "83";
	public static String    CODICE_PROFESSIONE_DIPENDENTE_DIRIGENTE = "27";
	public static String    CODICE_PROFESSIONE_AUTONOMO_IMPRENDITORE= "29";
	public static String    CODICE_PROFESSIONE_SOGGETTO_APICALE   	= "84";

	public static String  	CODICE_CARICA_PUBBLICA_RICOPERTA_POLITICO_ISTITUZIONALE = "4";
	public static String	CODICE_CARICA_PUBBLICA_RICOPERTA_NO = "5";
	public static String 	CODICE_TIPOLOGIA_LEGAME_AFFARI_DIVERSO_ATTIVITA_PRINCIPALE_NO = "1";
	
	public static final int   	LUNGHEZZA_CODICE_FISCALE_ITALIANO 	= 16;
	public static final int   	LUNGHEZZA_CODICE_FISCALE_ESTERO 	= 16;
	public static final int   	LUNGHEZZA_PARTITA_IVA_ITALIANA 		= 11;
	public static final int   	LUNGHEZZA_PARTITA_IVA_ESTERA 		= 11;

	public static final String CODICE_PRODOTTO						= "ANAGRAFICA";
	public static final String CHIAVE_PRIT_CENSIMENTO				= "CENSIMENTO";
	public static final String CHIAVE_PRIT_PRIVACY					= "PRIVACY";
	public static final String CHIAVE_PRIT_CCIAA					= "CCIAA";
	public static final String CHIAVE_PRIT_COUPON_PRESENTA_UN_AMICO	= "COUPON PRESENTA UN AMICO";
	public static final String CHIAVE_PRIT_VARIAZIONE				= "VARIAZIONE";
	public static final String CHIAVE_PRIT_PERSONAL_PROFILE			= "PERSONAL PROFILE";
	public static final String CHIAVE_PRIT_VARIAZIONE_PATIMONIO		= "VARIAZIONE PATRIMONIO";
	public static final String CHIAVE_PRIT_PRIVACY_PERSONAL_PROFILE	= "PRIVACY PERSONAL PROFILE";
	public static final String CHIAVE_PRIT_W9_FATCA					= "MOD W8 BEN W9";
	public static final String CHIAVE_PRIT_COUPON_PROMOZIONE_MGM	= "COUPON PROMOZIONE MGM";
	public static final String CHIAVE_PRIT_AUTOCERTIFICAZIONE_FATCA	= "AUTOCERTIFICAZIONE FATCA";

	public static String MODULO_W9_FATCA		 			= "W9";
	public static String MODULO_AUTOCERTIFICAZIONE_FATCA	= "AUTOCERTIFICAZIONE_NON_US_PERSON";
	
	public static int 	PROGRESSIVO_INIZIALE_SEDE = 51;
}
