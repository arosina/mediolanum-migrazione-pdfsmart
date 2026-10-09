package prgm.ita.anagraficaclienti.facade;

import java.util.HashMap;
import java.util.Map;

/********************************************************************************************************/
/********************************************************************************************************/
public class MappaturaTabelleOracle {

	public static final String CLI_ELETTR 			= "INR_CLI_ELETTR_REP";
	public static final String CLI_PROSPECT_RETE 	= "CEPE_CLI_PROSPECT_RETE_REP";
	
	private static final String INDCLI_ELETTR 			= "INR_INDCLI_ELETTR_REP";
	public static final String INDCLI_PROSPECT_RETE		= "CEPE_INDCLI_PROSPECT_RETE_REP";

	private static final String TELCLI_ELETTR 			= "INR_RECAPTELCLI_ELETTR_REP";
	public static final String TELCLI_PROSPECT_RETE 	= "CEPE_RECAPCLI_PROS_RETE_REP";

	private static final Map tabelleIndirizzi = new HashMap();
	private static final Map tabelleTelefoni = new HashMap();
	static{
		
		tabelleIndirizzi.put(CLI_ELETTR,INDCLI_ELETTR);
		tabelleIndirizzi.put(CLI_PROSPECT_RETE,INDCLI_PROSPECT_RETE);

		tabelleTelefoni.put(CLI_ELETTR,TELCLI_ELETTR);
		tabelleTelefoni.put(CLI_PROSPECT_RETE,TELCLI_PROSPECT_RETE);
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static String getNomeTabellaIndirizzi(String nomeTabellaCli){
		return (String)tabelleIndirizzi.get(nomeTabellaCli);
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public static String getNomeTabellaTelefoni(String nomeTabellaCli){
		return (String)tabelleTelefoni.get(nomeTabellaCli);
	}
}
