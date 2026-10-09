package prgm.ita.anagraficaclienti.flussofatca;

import java.util.HashMap;
import java.util.Map;

import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractNavigatore{

	public static final int INDIZI_FORTI_NON_SUPERABILI 	= 3;
	public static final int INDIZI_FORTI_SUPERABILI 		= 2;
	public static final int INDIZI_DEBOLI 				= 1;
	public static final int NESSUN_INDIZIO 				= 0;
	
	public static String NO_POPUP				 		= null;
	public static String POPUP_DICHIARAZIONE_US_PERSON 	= "DichiarazioneUsPerson";
	public static String POPUP_RACCOLTA_TIN 			= "RaccoltaTin";
	public static String POPUP_CONFERMA_VERFICA 		= "ConfermaVerifica";
	public static String FINE			 				= "Fine";
	
	public static String FATCA_ALERT_W9 				= "Attenzione! Il cliente è una US person e quindi verrà stampato il modulo W9";
	public static String FATCA_ALERT_ALLEGATI 			= "Attenzione! Il cliente presenta indizi di americanità e pertanto, al termine del caricamento, dovrà fornire gli allegati previsti";
	public static String FATCA_ALERT_SI_DICHIARA_US 	= "Attenzione! Il cliente si dichiara US person e quindi verrà stampato il modulo W9";
	
	public static Map<String, String> statiFatca = new HashMap<String, String>();
	static{
		statiFatca.put("O","FuoriAmbito");
		statiFatca.put("V","ValutazioneInCorso");
		statiFatca.put("P","ValutazioneInCorso");
		statiFatca.put("K","ValutazioneInCorso");
		statiFatca.put("R","ValutazioneInCorso");
		statiFatca.put("S","UsPerson");
		statiFatca.put("N","NonUsPerson");
		statiFatca.put("X","VerificatoSenzaIndizi");
	}
	
	public abstract String 	init(ClienteModel model);
	public abstract void 	doStep(ClienteModel model);
	
}
