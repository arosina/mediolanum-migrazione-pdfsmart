package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ExternalLinkOnSignDataModel extends CommandDataModel {

	// In caso di più elementi impostare label e url con la lista, di numero sempre coerente di valori, separati da pipe (|)
	private StringType externalLinkOnSignLabel = new StringType();
	private StringType externalLinkOnSignUrl = new StringType();
	private StringType externalLinkOnSignLabelAggiuntivo = new StringType();
	private StringType externalLinkOnSignUrlAggiuntivo = new StringType();
	
	public StringType getExternalLinkOnSignLabel() {
		return externalLinkOnSignLabel;
	}
	public void setExternalLinkOnSignLabel(StringType externalLinkOnSignLabel) {
		this.externalLinkOnSignLabel = externalLinkOnSignLabel;
	}
	public StringType getExternalLinkOnSignUrl() {
		return externalLinkOnSignUrl;
	}
	public void setExternalLinkOnSignUrl(StringType externalLinkOnSignUrl) {
		this.externalLinkOnSignUrl = externalLinkOnSignUrl;
	}
	public StringType getExternalLinkOnSignLabelAggiuntivo() {
		return externalLinkOnSignLabelAggiuntivo;
	}
	public void setExternalLinkOnSignLabelAggiuntivo(StringType externalLinkOnSignLabelAggiuntivo) {
		this.externalLinkOnSignLabelAggiuntivo = externalLinkOnSignLabelAggiuntivo;
	}
	public StringType getExternalLinkOnSignUrlAggiuntivo() {
		return externalLinkOnSignUrlAggiuntivo;
	}
	public void setExternalLinkOnSignUrlAggiuntivo(StringType externalLinkOnSignUrlAggiuntivo) {
		this.externalLinkOnSignUrlAggiuntivo = externalLinkOnSignUrlAggiuntivo;
	}

}
