package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class LegameDispositivaOrdineModel extends CommandDataModel {

	// Input
	private StringType idReport = new StringType();
	private StringType progr = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
		
	public StringType getIdReport() {
		return idReport;
	}

	public void setIdReport(StringType idReport) {
		this.idReport = idReport;
	}

	public StringType getProgr() {
		return progr;
	}

	public void setProgr(StringType progr) {
		this.progr = progr;
	}
	
}
