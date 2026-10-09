package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class InrDispTerzoPagatoreInput extends CommandDataModel {

	private StringType pdfInstanceId = new StringType();
	private StringType codAgente = new StringType();
	private StringType ndgCliente = new StringType();
	private StringType codPotenzialeCliente = new StringType();
	private StringType relazioneContraenteTerzoPagatore = new StringType();
	private StringType altraRelazione = new StringType();

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}
	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getNdgCliente() {
		return ndgCliente;
	}
	public void setNdgCliente(StringType ndgCliente) {
		this.ndgCliente = ndgCliente;
	}
	public StringType getCodPotenzialeCliente() {
		return codPotenzialeCliente;
	}
	public void setCodPotenzialeCliente(StringType codPotenzialeCliente) {
		this.codPotenzialeCliente = codPotenzialeCliente;
	}
	public StringType getRelazioneContraenteTerzoPagatore() {
		return relazioneContraenteTerzoPagatore;
	}
	public void setRelazioneContraenteTerzoPagatore(StringType relazioneContraenteTerzoPagatore) {
		this.relazioneContraenteTerzoPagatore = relazioneContraenteTerzoPagatore;
	}
	public StringType getAltraRelazione() {
		return altraRelazione;
	}
	public void setAltraRelazione(StringType altraRelazione) {
		this.altraRelazione = altraRelazione;
	}
}
