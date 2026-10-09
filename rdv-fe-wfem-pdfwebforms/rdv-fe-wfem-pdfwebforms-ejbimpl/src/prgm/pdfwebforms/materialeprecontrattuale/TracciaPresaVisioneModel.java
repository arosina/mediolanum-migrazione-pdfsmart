package prgm.pdfwebforms.materialeprecontrattuale;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class TracciaPresaVisioneModel extends CommandDataModel {

	private StringType url = new StringType();
	private StringType title = new StringType();
	private StringType ndg = new StringType();
	private StringType idCarrello = new StringType();
	private StringType pdfId = new StringType();
	private StringType clickType = new StringType();
	
	private StringType esitoCode = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getActionID() {
		return getIdCarrello().isNull() ? getPdfId() : getIdCarrello();
	}
	
	public StringType getUrl() {
		return url;
	}
	public void setUrl(StringType url) {
		this.url = url;
	}
	public StringType getTitle() {
		return title;
	}
	public void setTitle(StringType title) {
		this.title = title;
	}
	public StringType getNdg() {
		return ndg;
	}
	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}
	public StringType getIdCarrello() {
		return idCarrello;
	}
	public void setIdCarrello(StringType idCarrello) {
		this.idCarrello = idCarrello;
	}
	public StringType getPdfId() {
		return pdfId;
	}
	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}
	public StringType getClickType() {
		return clickType;
	}
	public void setClickType(StringType clickType) {
		this.clickType = clickType;
	}
	public StringType getEsitoCode() {
		return esitoCode;
	}
	public void setEsitoCode(StringType esitoCode) {
		this.esitoCode = esitoCode;
	}

}
