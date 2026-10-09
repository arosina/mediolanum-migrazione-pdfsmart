package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;


public class DettaglioModel extends CommandDataModel{
	
	private StringType service=		  	new StringType();
	private StringType codPrit=		  	new StringType();
	private DateType   dataSpunta=	  	new DateType();
	private StringType barcode=		  	new StringType();
	private StringType codCliente=	  	new StringType();
	private StringType cognomeCliente=	new StringType();
	private StringType nomeCliente=	 	new StringType();
	private StringType codAgente=		new StringType();
	private StringType cognomeAgente=	new StringType();
	private StringType nomeAgente=		new StringType();
	private StringType codAgenzia=		new StringType();
	private StringType codProdotto=		new StringType();
	private StringType descPrdotto=		new StringType();
	private StringType codOperazione=	new StringType();
	private StringType descOperazione=	new StringType();
	private StringType codDivision=		new StringType();
	private StringType cognomeDivision=	new StringType();
	private StringType nomeDivision=	new StringType();
	private StringType codRegion=		new StringType();
	private StringType cognomeRegion=	new StringType();
	private StringType nomeRegion=		new StringType();
	
	
	public StringType getService() {
		return service;
	}
	public void setService(StringType service) {
		this.service = service;
	}
	public StringType getCodPrit() {
		return codPrit;
	}
	public void setCodPrit(StringType codPrit) {
		this.codPrit = codPrit;
	}
	public DateType getDataSpunta() {
		return dataSpunta;
	}
	public void setDataSpunta(DateType dataSpunta) {
		this.dataSpunta = dataSpunta;
	}
	public StringType getBarcode() {
		return barcode;
	}
	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}
	public StringType getCodCliente() {
		return codCliente;
	}
	public void setCodCliente(StringType codCliente) {
		this.codCliente = codCliente;
	}
	public StringType getCognomeCliente() {
		return cognomeCliente;
	}
	public void setCognomeCliente(StringType cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}
	public StringType getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(StringType nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCognomeAgente() {
		return cognomeAgente;
	}
	public void setCognomeAgente(StringType cognomeAgente) {
		this.cognomeAgente = cognomeAgente;
	}
	public StringType getNomeAgente() {
		return nomeAgente;
	}
	public void setNomeAgente(StringType nomeAgente) {
		this.nomeAgente = nomeAgente;
	}
	public StringType getCodAgenzia() {
		return codAgenzia;
	}
	public void setCodAgenzia(StringType codAgenzia) {
		this.codAgenzia = codAgenzia;
	}
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public StringType getDescPrdotto() {
		return descPrdotto;
	}
	public void setDescPrdotto(StringType descPrdotto) {
		this.descPrdotto = descPrdotto;
	}
	public StringType getCodOperazione() {
		return codOperazione;
	}
	public void setCodOperazione(StringType codOperazione) {
		this.codOperazione = codOperazione;
	}
	public StringType getDescOperazione() {
		return descOperazione;
	}
	public void setDescOperazione(StringType descOperazione) {
		this.descOperazione = descOperazione;
	}
	public StringType getCodDivision() {
		return codDivision;
	}
	public void setCodDivision(StringType codDivision) {
		this.codDivision = codDivision;
	}
	public StringType getCognomeDivision() {
		return cognomeDivision;
	}
	public void setCognomeDivision(StringType cognomeDivision) {
		this.cognomeDivision = cognomeDivision;
	}
	public StringType getNomeDivision() {
		return nomeDivision;
	}
	public void setNomeDivision(StringType nomeDivision) {
		this.nomeDivision = nomeDivision;
	}
	public StringType getCodRegion() {
		return codRegion;
	}
	public void setCodRegion(StringType codRegion) {
		this.codRegion = codRegion;
	}
	public StringType getCognomeRegion() {
		return cognomeRegion;
	}
	public void setCognomeRegion(StringType cognomeRegion) {
		this.cognomeRegion = cognomeRegion;
	}
	public StringType getNomeRegion() {
		return nomeRegion;
	}
	public void setNomeRegion(StringType nomeRegion) {
		this.nomeRegion = nomeRegion;
	}
	
	
	

}
