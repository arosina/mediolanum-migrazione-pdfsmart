package prgm.pdfwebforms.drivers.io.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SoggettoReportAdeguatezzaModel extends CommandDataModel {

	public static class Ruoli{
		public static String INSTESTATARIO 			= "6";
		public static String COINSTESTATARIO 		= "13";
		public static String PRIMO_INTESTATARIO  	= "2";	
		public static String SECONDO_INTESTATARIO 	= "3";
		public static String TERZO_INTESTATARIO 	= "4";
		public static String QUARTO_INTESTATARIO 	= "5";
	}
	
	private StringType codice = new StringType();
	private StringType ruolo = new StringType(Ruoli.INSTESTATARIO);
	
	public StringType getCodice() {
		return codice;
	}
	public void setCodice(StringType codice) {
		this.codice = codice;
	}
	public StringType getRuolo() {
		return ruolo;
	}
	public void setRuolo(StringType ruolo) {
		this.ruolo = ruolo;
	}

}
