package prgm.pdfwebforms.core;

import java.util.regex.Pattern;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfPredefinedFields {

	public static String INPUT_COD_CLIENTE_N 	= "codCliente";

	public static String BARCODE 								= "barcode";
	public static String BARCODE_IMAGE							= "barcodeImage";
	public static String BARCODE_VALUE							= "barcodeValue";
	public static String LUOGO 									= "luogo";
	public static String DESCR_TOPONIMO							= "descrToponimo";
	public static String DATA									= "data";
	public static String NUMERO_CARTA_CHIMICA					= "numeroCartaChimica";
	public static String COPIA_PER								= "copiaPer";
	public static String CODICE_MODULO							= "codiceModulo";
	public static String DESCRIZIONE_MODULO						= "descrizioneModulo";
	public static String DATA_SOTTOSCRIZIONE					= "dataSottoscrizione";
	public static String ORA_SOTTOSCRIZIONE						= "oraSottoscrizione";
	public static String NUMERO_ISTANZA_MODULO					= "numeroIstanzaModulo";
	public static String TIPO_SUPPORTO_MATERIALE_CONTRATTUALE 	= "tipoSupportoMaterialeContrattuale";
	public static String ORIZZONTE_TEMPORALE				 	= "orizzonteTemporale";
	public static String TOLLERANZA_VOLATILITA				 	= "tolleranzaVolatilita";
	public static String ID_REPORT_ADEGUATEZZA					= "idReportAdeguatezza";
	public static String ID_QUESTIONARIO_IDD					= "idQuestionarioIdd";
	public static String ID_RACCOMANDAZIONE_IDD					= "idRaccomandazioneIdd";
	public static String TIPO_AGEVOLAZIONE						= "tipoAgevolazione";
	public static String TIPO_AGEVOLAZIONE_ALTRO				= "ALTRO";
	public static String TIPO_AGEVOLAZIONE_DIPENDENTI			= "DIPENDENTI";
	public static String PAGINAZIONE							= "paginazione";
	
	public static String TIPOLOGIA_AGEVOLAZIONE					= "tipologiaAgevolazione";
	public static String DESCR_AGEVOLAZIONE 					= "descrizioneAgevolazione";
	public static String MOD_VERSAMENTO_AGEVOLAZIONE			= "modalitaVersamentoAgevolazione";
	public static String PERCENTUALE_AGEVOLAZIONE				= "percentualeAgevolazione";
	public static String IMPORTO_AGEVOLAZIONE					= "importoAgevolazione";
	public static String DESCR_AGEVOLAZIONE_DIPENDENTI			= "descrizioneAgevolazioneDipendenti";
	public static String SCOPO_RAPPORTO							= "scopoRapporto";
	public static String TIPO_DISTANZA_COLLOCAMENTO				= "tipoDistanzaCollocamento";
	public static String PRAT_DIG_MOM_COD_TIPO_DOCUM			= "praticheDigitaliMomCodTipoDocumValue";
	
	// Target market
	public static String ID_PCA									= "idPCA";
	public static String ID_QLTM								= "idQLTM";
	public static String LINK_ID_QLTM							= "linkIdQLTM";
	
	// Campi strutturati
	public static String ID_AGEVOLAZIONE		= "idAgevolazione";
	public static String CODICE_AGEVOLAZIONE	= "codiceAgevolazione";
	public static String IMPORTO				= "importo";
	public static String IBAN					= "iban";
	public static String NUMERO_CONTRATTO		= "numeroContratto";
	public static String NUMERO_PROPOSTA		= "numeroProposta";
	public static String NUMERO_PROPOSTA_CORRELATA	= "numeroPropostaCorrelata";	// Utile solo a MOM
	public static String COD_FPS_SUPPORTANTE	= "codiceSpecialistaProtezione";
	
	public static String 	AGENTE_CODICE 		= "codiceAgente";
	public static String 	AGENTE_NDG 			= "ndgAgente";
	
	// Con suffisso 1,2.....n 
	public static String 	CLIENTE_NDG_PREFIX 				= "ndgCliente";
	public static String 	CLIENTE_ID_CENSIMENTO_PREFIX 	= "idCensimentoCliente";
	// Per ricerca clienti: parte web
	public static String 	CLIENTE_NOME_PREFIX 			= "nomeCliente";
	public static String 	CLIENTE_COGNOME_PREFIX 			= "cognomeCliente";
	public static String 	CLIENTE_COGNOME_NOME_PREFIX 	= "cognomeNomeCliente";
	public static String 	CLIENTE_NOME_COGNOME_PREFIX 	= "nomeCognomeCliente";

	// Campi per impostare sul pdf le info dei protection specialist
	public static String 	PROTECTION_SPECIALIST_COGNOME_NOME_CONSULENTE_FINANZIARIO 	= "cognomeNomeConsulenteFinanziario";
	public static String 	PROTECTION_SPECIALIST_CODICE_CONSULENTE_FINANZIARIO 		= "codiceConsulenteFinanziario";
	public static String 	PROTECTION_SPECIALIST_CODICE_AREA_CONSULENTE_FINANZIARIO 	= "codiceAreaConsulenteFinanziario";
	
	public static String 	PROTECTION_SPECIALIST_COGNOME_NOME_SPECIALIST 	= "cognomeNomeFamilyProtectionSpecialist";
	public static String 	PROTECTION_SPECIALIST_CODICE_SPECIALIST 		= "codiceFamilyProtectionSpecialist";
	public static String 	PROTECTION_SPECIALIST_CODICE_AREA_SPECIALIST 	= "codiceAreaFamilyProtectionSpecialist";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static Pattern 	AGENTE_FIRMA_N_PATTERN;
	public static Pattern 	AGENTE_NOME_FIRMA_N_PATTERN;
	public static Pattern 	AGENTE_CLAUSOLA_N_PATTERN;
	public static Pattern 	CLIENTE_FIRMA_N_DI_M_PATTERN;
	public static Pattern 	CLIENTE_NOME_FIRMA_N_DI_M_PATTERN;
	public static Pattern 	CLIENTE_CLAUSOLA_N_DI_M_PATTERN;
	public static Pattern 	AGENTE_PATTERN;
	public static Pattern 	SPLIT_PATTERN;
	public static Pattern 	CLIENTE_PATTERN;
	public static Pattern 	CLIENTE_CONTO_PATTERN;
	public static Pattern 	CLIENTE_CONTOCORRENTE_PATTERN;
	public static Pattern 	CLIENTE_NDG_PATTERN;
	public static Pattern 	CLIENTE_PRESTITO_PATTERN;
	static{
		AGENTE_FIRMA_N_PATTERN 				= Pattern.compile("firma(\\d+)Agente",Pattern.CASE_INSENSITIVE);
		AGENTE_NOME_FIRMA_N_PATTERN 		= Pattern.compile("nomefirma(\\d+)Agente",Pattern.CASE_INSENSITIVE);
		AGENTE_CLAUSOLA_N_PATTERN 			= Pattern.compile("clausola(\\d+)Agente",Pattern.CASE_INSENSITIVE);
		CLIENTE_FIRMA_N_DI_M_PATTERN 		= Pattern.compile("firma(\\d+)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_NOME_FIRMA_N_DI_M_PATTERN 	= Pattern.compile("nomefirma(\\d+)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_CLAUSOLA_N_DI_M_PATTERN 	= Pattern.compile("clausola(\\d+)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		AGENTE_PATTERN 						= Pattern.compile("(codice|ndg|nome|cognome|cognomeNome|nomeCognome|codiceFiscale|partitaIva|codiceFiscalePartitaIva)Agente",Pattern.CASE_INSENSITIVE);
		SPLIT_PATTERN 						= Pattern.compile("(codice|ndg|nome|cognome|cognomeNome|nomeCognome|codiceFiscale|partitaIva|codiceFiscalePartitaIva)Split",Pattern.CASE_INSENSITIVE);
		CLIENTE_PATTERN 					= Pattern.compile("(ndg|idCensimento|nome|cognome|secondaIntestazione|cognomeNome|nomeCognome|codiceFiscale|partitaIva|codiceFiscalePartitaIva)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_CONTO_PATTERN 				= Pattern.compile("(numeroConto|ibanConto|paeseIbanConto|cinEuropeoIbanConto|cinControlloIbanConto|abiIbanConto|cabIbanConto|numeroIbanConto)(.*)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_CONTOCORRENTE_PATTERN 		= Pattern.compile("(numeroConto|ibanConto|paeseIbanConto|cinEuropeoIbanConto|cinControlloIbanConto|abiIbanConto|cabIbanConto|numeroIbanConto)Corrente(.*)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_NDG_PATTERN 				= Pattern.compile("ndgCliente(\\d+)",Pattern.CASE_INSENSITIVE);
		CLIENTE_PRESTITO_PATTERN			= Pattern.compile("(numero)Prestito(.*)Cliente(\\d+)",Pattern.CASE_INSENSITIVE);
	}
}
