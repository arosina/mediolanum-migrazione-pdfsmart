package prgm.ita.p.dac.facade;

/***********************************************************************************************/
/***********************************************************************************************/
public class Costanti {
	
	public static String DAO_XML_NAME_MOM = "ItaPDac.PritMOM";
	
	public static String FNC_RICERCA 	 = "ricerca";
	public static String FNC_GESTIONE 	 = "gestione";
	public static String FNC_SPUNTA		 = "spunta";
	public static String FNC_AUTORIZZA	 = "autorizza";
	
	public static int MEZZO_SPEDIZIONE_CORRIERE = 1;
	public static int MEZZO_PAGAMENTO_ASSEGNO = 1;
	public static int MEZZO_PAGAMENTO_ASSEGNO_ESTERO = 11;
	public static int DOC_ASSEGNO = 0;
	
	public static String GET_PROGRESSIVI_RISORSA_DAC 		= "CEPE_PT_TESTATA";
	public static String GET_PROGRESSIVI_RISORSA_DOCUMENTO 	= "CEPE_PT_DETTAGLIO";
	
	public static int  TIPO_DAC_STANDARD 		= 1;
	public static int  TIPO_DAC_PERCONTORETE 	= 2;
	public static int  TIPO_DAC_RACCOMANDATE 	= 3;
	public static int  TIPO_DAC_CARTOLINE 		= 4;
	public static int  TIPO_DAC_SEDE	 		= 5;
	
	public static int STATO_INCORSO 	= 1;	//Bozza
	public static int STATO_SPEDITA 	= 2;
	public static int STATO_APERTA 		= 3;
	public static int STATO_LAVORATA 	= 4;
	
	public static String FLAG_DA_REPLICARE_ONLINE 	= "S";
	public static String FLAG_DA_REPLICARE_OFFLINE 	= "D";
	
	public static int UFFICIO_RETE 				= 1;
	public static int UFFICIO_CODING_SPUNTA 	= 2; 
	public static int UFFICIO_COMDATA_SPUNTA 	= 5;
	public static int UFFICIO_C_GLOBAL_SPUNTA 	= 6;
	
	public static int AZIONE_CREA 	= 1;
	public static int AZIONE_EMETTI = 2;
	public static int AZIONE_APRI 	= 3;
	public static int AZIONE_CHIUDI = 4;

	public static int ESITO_DAC_TUTTI_ACCETTATI 		= 1;
	public static int ESITO_DAC_MODIFICATI_AGGIUNTI 	= 2;
	public static int ESITO_DAC_MANCANTI 				= 3;
	public static int ESITO_DAC_TUTTI_MANCANTI 			= 4;
	
	public static int ESITO_DOC_ACCETTATO 				= 1;
	public static int ESITO_DOC_MODIFICATO 				= 2;
	public static int ESITO_DOC_AGGIUNTO 				= 3;
	public static int ESITO_DOC_DOCMANCANTE 			= 4;
	public static int ESITO_DOC_DOCMANCANTESEGN 		= 5;
	public static int ESITO_DOC_ASSEGNOMANCANTE 		= 6;
	public static int ESITO_DOC_RIGADOPPIAANNULLATA 	= 7;
	
	public static String ID_DAC_X_DOC_FUORI_DAC = "00000000000000000000";
	public static int UBICAZIONE_IN_VIAGGIO 	= -1;
	
	public static String ORA_SPEDIZIONE_8 		= "01";
	public static String ORA_SPEDIZIONE_12 		= "02";
	public static String ORA_SPEDIZIONE_14_30	= "03";
	public static String ORA_SPEDIZIONE_16_30	= "04";
	public static String ORA_SPEDIZIONE_19_30	= "05";
	
	public static String CONTROLLO_FIRME_NORMALE 	= "S";
	public static String CONTROLLO_FIRME_CONGIUNTO 	= "C";
	
	public static String CF_NON_ESEGUITO		= "";
	public static String CF_CONFORME 			= "S";
	public static String CF_NONCONFORME 		= "N";
	public static String CF_FIRMAASSENTE 		= "A";
	public static String CF_FUORIPROCEDURA 		= "F";
	public static String CF_NONDISPONIBILE 		= "D";
	public static String CF_NONTROVATA 			= "T";
	public static String CF_ERRATA 				= "E";
	public static String CF_CLI_SENZA_CONTI		= "X";
	public static String CF_DOC_MANCANTE		= "M";
	public static String CF_NON_ATTIVO			= "Z";
	public static String CF_FIRMAINCOPIA		= "C";
	
	public static String CONTESTO_PLICO_DAC	= "DACGUI";
	public static String CONTESTO_PLICO_ASS	= "DACASS";
	
	public static int ERR_DOC_NON_TROVATO					= 1;
	public static int ERR_BOX_CASSETTA_NON_COMPATIBILI		= 2;
	public static int ERR_DOC_NON_DISPONIBILE_IN_UFF_CORR	= 3;
	public static int ERR_DOC_NON_LAVORATO					= 4;
	public static int ERR_DOC_IN_ALTRA_DAC					= 5;
	public static int ERR_DOC_IN_VIAGGIO					= 6;
	public static int ERR_DOC_GIA_RICEVUTO					= 7;
	public static int ERR_DOC_IN_DAC_NON_CHIUSA				= 8;
	
	public static String COD_DIVISA_EURO		= "EUR";
	public static String COD_DIVISA_LIRE		= "ITL";
	public static String COD_DIVISA_STERLINA	= "GBP";
	public static String COD_DIVISA_DOLLARO		= "USD";
	public static String COD_DIVISA_ALTRO		= "*";
	
	public static String TIPO_AGENTE_AAF	= "AAF";
	public static String TIPO_AGENTE_PF		= "PF";
}
