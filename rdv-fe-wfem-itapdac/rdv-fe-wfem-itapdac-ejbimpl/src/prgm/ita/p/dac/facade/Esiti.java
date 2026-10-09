package prgm.ita.p.dac.facade;

import java.util.HashMap;
import java.util.Map;

/***********************************************************************************************/
/***********************************************************************************************/
public class Esiti {

	private static Map esitiDocumento = new HashMap();
	private static Map esitiFirme = new HashMap();
	
	static{
		esitiDocumento.put(""+Costanti.ESITO_DOC_ACCETTATO,"Accettato");
		esitiDocumento.put(""+Costanti.ESITO_DOC_DOCMANCANTE,"Doc. Mancante");
		esitiDocumento.put(""+Costanti.ESITO_DOC_ASSEGNOMANCANTE,"Assegno Mancante");
		esitiDocumento.put(""+Costanti.ESITO_DOC_DOCMANCANTESEGN,"Doc. Mancante Segn.");
		esitiDocumento.put(""+Costanti.ESITO_DOC_RIGADOPPIAANNULLATA,"Riga doppia ann.");
		
		esitiFirme.put(Costanti.CF_CONFORME,"Conforme");
		esitiFirme.put(Costanti.CF_NONCONFORME,"Non Conforme");
		esitiFirme.put(Costanti.CF_FIRMAASSENTE,"Firma Assente");
		esitiFirme.put(Costanti.CF_FUORIPROCEDURA,"Fuori Procedura");
		esitiFirme.put(Costanti.CF_NONDISPONIBILE,"Firma Non Disp.");
		esitiFirme.put(Costanti.CF_NONTROVATA,"Firma Non Trovata");
		esitiFirme.put(Costanti.CF_ERRATA,"Firma Errata");
		esitiFirme.put(Costanti.CF_CLI_SENZA_CONTI,"Cliente senza conti");
		esitiFirme.put(Costanti.CF_DOC_MANCANTE,"Documento mancante");
		esitiFirme.put(Costanti.CF_NON_ATTIVO,"Controllo firme disattivato");
		esitiFirme.put(Costanti.CF_FIRMAINCOPIA,"Firma in copia");
	}
	
	public static String getDescrEsitoDocumento(int esito){
		return (String)esitiDocumento.get(""+esito);
	}
	public static String getDescrEsitoFirme(String esito){
		return (String)esitiFirme.get(esito);
	}
	public static String getColoredDescrEsitoFirme(String esito){
		String s1 ="";
		String s2="";
		if(esito.equals(Costanti.CF_CONFORME)){
			s1 = "<span style='color:green;'>";
			s2 = "</span>";
		}else if(esito.equals(Costanti.CF_NONCONFORME)){
			s1 = "<span style='color:red;'>";
			s2 = "</span>";
		}
		return s1+(String)esitiFirme.get(esito)+s2;
	}

}
