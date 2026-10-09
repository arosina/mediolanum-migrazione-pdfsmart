package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.model;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

public class InviaMailInputModel {
	StringType codiceProdotto = new StringType();
	StringType nomeCliente = new StringType();
	StringType cognomeCliente = new StringType();
	DateType dataSottoscrizione = new DateType();
	StringType codiceAgente = new StringType();
	StringType pdfInstaceId = new StringType();
	StringType oggettoEmail = new StringType();
	StringType testoEmail = new StringType();
	StringType isSwitch = new StringType();
	StringType switchFrom = new StringType();
	StringType switchTo = new StringType();
	
	public StringType getCodiceProdotto() {
		return codiceProdotto;
	}
	public void setCodiceProdotto(StringType codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}
	public StringType getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(StringType nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public StringType getCognomeCliente() {
		return cognomeCliente;
	}
	public void setCognomeCliente(StringType cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}
	public DateType getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(DateType dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	public StringType getPdfInstaceId() {
		return pdfInstaceId;
	}
	public void setPdfInstaceId(StringType pdfInstaceId) {
		this.pdfInstaceId = pdfInstaceId;
	}
	public StringType getOggettoEmail() {
		return oggettoEmail;
	}
	public void setOggettoEmail(StringType oggettoEmail) {
		this.oggettoEmail = oggettoEmail;
	}
	public StringType getTestoEmail() {
		return testoEmail;
	}
	public void setTestoEmail(StringType testoEmail) {
		this.testoEmail = testoEmail;
	}
	public StringType getIsSwitch() {
		return isSwitch;
	}
	public void setIsSwitch(StringType isSwitch) {
		this.isSwitch = isSwitch;
	}
	public StringType getSwitchFrom() {
		return switchFrom;
	}
	public void setSwitchFrom(StringType switchFrom) {
		this.switchFrom = switchFrom;
	}
	public StringType getSwitchTo() {
		return switchTo;
	}
	public void setSwitchTo(StringType switchTo) {
		this.switchTo = switchTo;
	}
	
	
	
}
