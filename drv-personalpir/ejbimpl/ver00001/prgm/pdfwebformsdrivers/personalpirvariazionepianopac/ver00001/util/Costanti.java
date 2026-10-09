package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util;

public final class Costanti {
	public static final String PREFIX_MESSAGE = "@#@#@personalpirvariazionepianopac_";
	public static final String PERSONALPIR_VARIAZIONE_MOMCODE = "IAA0";
	public static final String LINEA_FONDO_MED = "MED";
	public static final String LINEA_FONDO_TERZI = "ALT";

	public static final String REPORTADEGUATEZZA_OPERAZIONE_VARIAZIONE = "46"; 
	
	public static final String TARIFFA_PIC = "ULFUPIR";
	public static final String TARIFFA_PAC = "ULFAPIR";
	
	public static final String ORIGINE_MOP = "MOP";
	public static final String ORIGINE_RDV = "RDV";
	
	public static final String MSG_DAO_EXCEPTION_STATO_FONDO = "Eccezione DAO nel recuperare lo stato del fondo: ";

	public static final String DAO_OBJECT_NAME = "PdfWebFormsDrivers.PersonalPirVariazionePianoPac.ver00001.PersonalPirVariazionePianoPac";
	public static final String DAO_OBJECT_NAME_EF = "PdfWebFormsDrivers.PersonalPirVariazionePianoPac.ver00001.EquivalentiFreeze";
	
	public static final String QUERY_FONDO_IS_COLLOC ="isFondoCollocabile";
	
	public static final String QUERY_RICERCA_COMPARTI = "ricercaComparti";
	// costanti aggiunte per MOP 2
	public static final String SAN_MARINO = "SAN MARINO";
	public static final String LUOGO = "luogo";
	public static final String NDG_CLIENTE_1 = "ndgCliente1";
	public static final String DAO_FILE_XML = "PdfWebFormsDrivers.PersonalPirVariazionePianoPac.ver00001.PersonalPirVariazionePianoPac";
	public static final int ALTO_RISCHIO_1 = 1;	
	public static final String NDG_CLIENTE = "ndgCliente";
	public static final String ID_CENSIMENTO_CLIENTE_1 = "idCensimentoCliente1";
	public static final String COGNOME_CLIENTE_1 = "cognomeCliente1";
	public static final String NOME_CLIENTE_1 = "nomeCliente1";
	public static final int ID_CLIENTE = 1;
	public static final String COLLOCAMENTO_PRODOTTI_GESTITO = "COLLOCAMENTO_PRODOTTI_GESTITO";
	public static final String TIPO_RELAZIONE_ALTRO = "004";
	public static final String ERR_DATA_FUTURA = "La data di sottoscrizione non può essere futura.";
	public static final String TIPO_CONTO_CONTO_CORRENTE_SCUDATO = "CONTO_CORRENTE_SCUDATO";
	public static final String TIPO_CONTO = "tipoConto";
	public static final String RUOLI_AMMESSI = "ruoliAmmessi";
	public static final String RUOLO_P = "P";
	public static final String RUOLO_I = "I";
	public static final String RUOLO_C = "C";
	public static final String STRINGA_RUOLI_AMMESSI = Costanti.RUOLI_AMMESSI+": \"'"+Costanti.RUOLO_P+"', '"+Costanti.RUOLO_I+"', '"+Costanti.RUOLO_C+"'\", ";
	public static final String CATEGORIA_CONTO = "categoriaConto";
	public static final String CONTR_C_CAT_SCUDATO = "0099";
	public static final String POLIZZA_SCUDATA_S = "S";
		
	// TYPE ERROR
	public static final String TE_CAMPO_OBBLIGATORIO = "00001";
	public static final String TE_PROBLEMA_TECNICO_VERIFICA_POLIZZA = "00003";
	public static final String TE_AGENTE_NON_ABILITATO = "00004";
	public static final String TE_CLIENTE_COINCIDE_CONTRAENTE = "00005";
	public static final String TE_CLIENTE_INESISTENTE = "00006";
	public static final String TE_PROBLEMA_TECNICO_RECUPERO_SECONDO_CLIENTE = "00007";
	public static final String TE_NUOVO_CONTO_CORRISPONDE_VECCHIO = "00008";
	public static final String TE_VALORE_NON_AMMESSO = "00010";
	public static final String TE_COMPILA_IBAN = "00012";
	public static final String TE_INCOERENZA_SCUDO_FISCALE_POLIZZA = "00013";
	public static final String TE_DATA_SOTTOSCRIZIONE_FUTURA = "00015";
	public static final String TE_SOSPENSIONE_MAGGIORE_12_MESI = "00016";
	public static final String TE_FORMATO_ERRATO = "00017";
	public static final String TE_INDICARE_PERCENTUALE = "00018";
	public static final String TE_INDICARE_IMPORTO = "00019";
	public static final String TE_SELEZIONARE_UN_FONDO = "00020";
	public static final String TE_IMPORTO_INFERIORE_MINIMO = "00021";
	public static final String TE_LUOGO_NON_ITALIANO = "00022";
	public static final String TE_SELEZIONARE_POLIZZA = "00023";
	public static final String TE_RESIDENZA_CLIENTE_ALTO_RISCHIO = "00024";	
	public static final String TE_IMPORTO_NON_VALIDO = "00025";
	public static final String TE_PERCENTUALE_NON_VALIDA = "00026";
	public static final String TE_IMPORTO_INFERIORE_MINIMO_PAR = "00027";
	public static final String TE_NESSUN_SDD_ATTIVO = "00028";
	public static final String TE_NESSUN_SDD_SOSPESO = "00029";
	public static final String TE_SELEZIONARE_OPERAZIONE = "00030";
	public static final String TE_SELEZIONARE_OPZIONE= "00031";
	public static final String TE_OPERAZIONE_NON_VALIDA_CON_REVOCA = "00032";
	public static final String TE_OICR_OGGETTO_SALVAGUARDIA_E_MONITORAGGIO = "00033";
	public static final String TE_PRODOTTO_NON_SOTTOSCRIVIBILE = "00034";
	public static final String TE_IMPORTO_MAGGIORE_MASSIMO = "00036";	
	public static final String TE_IMPORTO_MAGGIORE_MASSIMO_PAR = "00037";
	public static final String TE_SOMMA_PERCENTUALI_DIVERSA_100 = "00039";
	public static final String TE_NO_FORMA_CONTR = "00040";
	
	// ERROR
	public static final String E_IMPORTO_NON_CORRISPONDE_AL_PREMIO_UNICO = "00001";
	public static final String E_NUMERO_MAX_OICR_ERRATO = "00003";
	public static final String E_RANGE_DISTRIBUZIONE_ERRATO = "00004";
	public static final String E_PROBLEMI_NEL_DETERMINARE_SALDO_CONTO = "00005";
	public static final String E_IMPORTO_MAGGIORE_SALDO = "00006";	
	public static final String E_OICR_OGGETTO_SALVAGUARDIA_E_MONITORAGGIO = "00007";
	public static final String E_PRODOTTO_NON_SOTTOSCRIVIBILE = "00008";
	public static final String E_OPERAZIONE_NON_SOTTOSCRIVIBILE = "00009";

	
	// WARNING
	public static final String W_DISPOSIZIONE_PERMANENTE_SDD_BANCA_ESTERNA = "00001";
	public static final String W_CC_IN_APERTURA = "00002";
	public static final String W_ASSICURANDO_MINORENNE = "00003";
	public static final String W_CONTRAENTE_PERSONA_GIURIDICA = "00004";
	public static final String W_RICHIESTA_SOSPENSIONE = "00005";
	public static final String W_CONTRAENTE_NON_INTESTATARIO_O_COINTESTATARIO = "00006";
	public static final String W_INTESTATARIO_UNICAMENTE_DELEGATO = "00007";
	public static final String W_CONTRAENTE_MINORENNE = "00008";
	public static final String W_RELAZIONE_TERZO_PAGATORE_ALTRO = "00009";
	
	private Costanti() {}
}
