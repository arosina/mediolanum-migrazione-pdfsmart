package prgm.ita.anagraficaclienti.facade;

import java.util.HashMap;
import java.util.Map;

/********************************************************************************************************/
/********************************************************************************************************/
public class MappaturaTabelle {

	public static final String CLI 					= "INR_CLI";
	public static final String CLI_COINT 			= "INR_CLI_COINT";
	public static final String CLI_PROSPECT 		= "INR_CLI_PROSPECT";
	public static final String CLI_ELETTR 			= "INR_CLI_ELETTR";
	public static final String CLI_PROSPECT_RETE 	= "CEPE_CLI_PROSPECT_RETE";
	
	public static final String INDCLI 				= "INR_INDCLI";
	public static final String INDCLI_COINT 		= "INR_INDCLI_COINT";
	public static final String INDCLI_PROSPECT 		= "INR_INDCLI_PROSPECT";
	public static final String INDCLI_ELETTR 		= "INR_INDCLI_ELETTR";
	public static final String INDCLI_PROSPECT_RETE = "CEPE_INDCLI_PROSPECT_RETE";

	public static final String TELCLI 				= "INR_RECAPTELCLI";
	public static final String TELCLI_COINT 		= "INR_RECAPTELCLI_COINT";
	public static final String TELCLI_PROSPECT 		= "INR_RECAPTELCLI_PROSPECT";
	public static final String TELCLI_ELETTR 		= "INR_RECAPTELCLI_ELETTR";
	public static final String TELCLI_PROSPECT_RETE = "CEPE_RECAPTELCLI_PROSPECT_RETE";

	private static final Map tabelleIndirizzi = new HashMap();
	private static final Map tabelleTelefoni = new HashMap();
	static{
		
		tabelleIndirizzi.put(CLI_ELETTR,"INR_INDCLI_ELETTR");
		tabelleIndirizzi.put(CLI_PROSPECT_RETE,"CEPE_INDCLI_PROSPECT_RETE");

		tabelleTelefoni.put(CLI_ELETTR,"INR_RECAPTELCLI_ELETTR");
		tabelleTelefoni.put(CLI_PROSPECT_RETE,"CEPE_RECAPTELCLI_PROSPECT_RETE");
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
