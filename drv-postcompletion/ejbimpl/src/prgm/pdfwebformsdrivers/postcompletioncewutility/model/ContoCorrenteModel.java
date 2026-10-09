package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.utils.PostCompletionCewUtils;

public class ContoCorrenteModel extends BancaModel {

	private StringType cognome 	      = new StringType();
	private StringType nome 		  = new StringType();
	private StringType numeroConto    = new StringType();
	private StringType paese			= new StringType();
    private StringType cin				= new StringType();
    private StringType cinIban			= new StringType();    
	
	public StringType getCognome() {
		return cognome;
	}
	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}
	
	public StringType getNumeroConto() {
		return numeroConto;
	}
	public void setNumeroConto(StringType numeroConto) {
		this.numeroConto = numeroConto;
	}

	public StringType getPaese() {
		return paese;
	}
	public void setPaese(StringType paese) {
		this.paese = paese;
	}
	public StringType getCin() {
		return cin;
	}
	public void setCin(StringType cin) {
		this.cin = cin;
	}
	public StringType getCinIban() {
		return cinIban;
	}
	public void setCinIban(StringType cinIban) {
		this.cinIban = cinIban;
	}
	public StringType getPrefissoIban() {
		return new StringType(paese.toString() + cinIban.toString() + cin.toString())  ;
	}
	public StringType getNumeroContoFormatoMediolanum() {
		if (numeroConto.stringValue().length() >= 8) {
			return new StringType(numeroConto.stringValue().substring(numeroConto.stringValue().length()  - 8));
		} else {
			return new StringType(PostCompletionCewUtils.left0Fill(numeroConto, 8).stringValue())  ;
		}
		
	}
}
