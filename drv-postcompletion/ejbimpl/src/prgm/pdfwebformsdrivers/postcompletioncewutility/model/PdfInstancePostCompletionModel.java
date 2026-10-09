package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class PdfInstancePostCompletionModel extends CommandDataModel {
	
	private StringType codRete = new StringType();
	private StringType stato = new StringType();
	
	private StringType codPotenziale1 = new StringType();
	private StringType codPotenziale2 = new StringType();
	private StringType codPotenziale3 = new StringType();
	
	private StringType cognome1 = new StringType();
	private StringType nome1 = new StringType();
	private TimestampType completionTime = new TimestampType();
	
	private StringType codAgente = new StringType();
	private StringType codAgenteImpersonato = new StringType();
	private StringType codRuoloImpersonato = new StringType();
	
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getCodPotenziale1() {
		return codPotenziale1;
	}
	public void setCodPotenziale1(StringType codPotenziale1) {
		this.codPotenziale1 = codPotenziale1;
	}
	public StringType getCodPotenziale2() {
		return codPotenziale2;
	}
	public void setCodPotenziale2(StringType codPotenziale2) {
		this.codPotenziale2 = codPotenziale2;
	}
	public StringType getCodPotenziale3() {
		return codPotenziale3;
	}
	public void setCodPotenziale3(StringType codPotenziale3) {
		this.codPotenziale3 = codPotenziale3;
	}
	public StringType getStato() {
		return stato;
	}
	public void setStato(StringType stato) {
		this.stato = stato;
	}
	public StringType getCognome1() {
		return cognome1;
	}
	public void setCognome1(StringType cognome1) {
		this.cognome1 = cognome1;
	}
	public StringType getNome1() {
		return nome1;
	}
	public void setNome1(StringType nome1) {
		this.nome1 = nome1;
	}
	public TimestampType getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(TimestampType completionTime) {
		this.completionTime = completionTime;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodAgenteImpersonato() {
		return codAgenteImpersonato;
	}
	public void setCodAgenteImpersonato(StringType codAgenteImpersonato) {
		this.codAgenteImpersonato = codAgenteImpersonato;
	}
	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}
	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}
	
}
