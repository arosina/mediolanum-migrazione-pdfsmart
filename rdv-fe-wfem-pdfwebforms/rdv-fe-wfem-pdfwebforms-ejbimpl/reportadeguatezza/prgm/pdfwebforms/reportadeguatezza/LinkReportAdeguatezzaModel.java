package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class LinkReportAdeguatezzaModel extends CommandDataModel {
	
	private StringType  idReportPDF = new StringType();
	private StringType  codiceCliente = new StringType();
	private StringType  link = new StringType();
	private StringType  guid = new StringType();
	private StringType  idECM = new StringType();
	
	public StringType getIdReportPDF() {
		return idReportPDF;
	}
	public void setIdReportPDF(StringType idReportPDF) {
		this.idReportPDF = idReportPDF;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public StringType getLink() {
		return link;
	}
	public void setLink(StringType link) {
		this.link = link;
	}
	public StringType getGuid() {
		return guid;
	}
	public void setGuid(StringType guid) {
		this.guid = guid;
	}
	public StringType getIdECM() {
		return idECM;
	}
	public void setIdECM(StringType idECM) {
		this.idECM = idECM;
	}
	
}
