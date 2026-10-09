package prgm.pdfwebforms.model;

/***********************************************************************************************/
/***********************************************************************************************/
public class CodRuoliImpersonati {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private CodRuoliImpersonati() {
		throw new IllegalStateException("CodRuoliImpersonati class");
	}

	public static String FB  = "FB";	// Family Banker
	public static String BC  = "BC";	// Banker Consultant
	public static String FPS = "FPS";	// Family Protection Specialist
	public static String CS  = "CS";	// Credit Specialist
	public static String OS  = "OS";	// Operatore di Sede (pdf_config)
	public static String SA  = "SA";	// Self Advisor (pdf_config)
	public static String TWP = "TWP";	// Team Wealth Advisor
}
