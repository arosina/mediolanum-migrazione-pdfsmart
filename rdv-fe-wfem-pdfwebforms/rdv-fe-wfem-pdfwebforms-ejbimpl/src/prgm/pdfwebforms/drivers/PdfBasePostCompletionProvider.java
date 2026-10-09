package prgm.pdfwebforms.drivers;

import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado

 * Richiamato per avere il nome del post completion manager dello specifico prodotto che il/i pdf rappresentano 
 * 
 * "isCompilationModeManaged": viene dapprima richiamato per esplicitare se la specifica modalità di sottoscrizione viene gestita nella fase
 * di post compilazione
 * 
 * "providePostCompletionManagerName": viene richiamato per indicare il nome logico della classe di post compilazione. Può tornare anche stringa vuota, ma non null
 * Il nome della classe "PostCompletion" verrà composta nel seguente modo:
 * 		prgm.pdfwebformsdrivers."+driverClassName+".postcompletion.PdfPostCompletion"+pdfPostCompletionName+compilationType
 * dove
 * 		driverClassName: 		nome del driver così come configurato in anagrafica moduli
 * 		pdfPostCompletionName:	stringa ritornata dal metodo "providePostCompletionManagerName". Rappresenta il prodotto gestito dallo specifico PostCompletion
 * 		compilationType:			tipo di compilazione. "Send" -> Cartacea. "Sign" -> Firma digitale. "Copernico" -> Copernico 
**************************************************************************************************/
public abstract class PdfBasePostCompletionProvider extends PdfBasePostCompletion {
	public abstract boolean isCompilationModeManaged(String compilationMode); 
	public abstract String 	providePostCompletionManagerName(PdfModel pdf) throws Exception;
}
