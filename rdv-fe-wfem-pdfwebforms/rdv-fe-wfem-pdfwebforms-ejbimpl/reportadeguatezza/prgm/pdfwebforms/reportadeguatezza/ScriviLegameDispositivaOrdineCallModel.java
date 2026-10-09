package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class ScriviLegameDispositivaOrdineCallModel extends CommandDataModel {

	// Input
	private StringType userId = new StringType();
	private StringType codOperzDispOrig = new StringType();
	private StringType tipoSistemaOrig = new StringType();
	private StringType idReport = new StringType();
	private StringType progr = new StringType();
	
	// Output
	private StringType idReportOut = new StringType(); // Per verifica chiamata
	
	private ListType ordini = new ListType(LegameDispositivaOrdineModel.class);
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
	
	public StringType getCodOperzDispOrig() {
		return codOperzDispOrig;
	}

	public void setCodOperzDispOrig(StringType codOperzDispOrig) {
		this.codOperzDispOrig = codOperzDispOrig;
	}

	public StringType getTipoSistemaOrig() {
		return tipoSistemaOrig;
	}

	public void setTipoSistemaOrig(StringType tipoSistemaOrig) {
		this.tipoSistemaOrig = tipoSistemaOrig;
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

	public StringType getIdReportOut() {
		return idReportOut;
	}

	public void setIdReportOut(StringType idReportOut) {
		this.idReportOut = idReportOut;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public ListType getOrdini() {
		return ordini;
	}

	public void setOrdini(ListType ordini) {
		this.ordini = ordini;
	}
	
}
