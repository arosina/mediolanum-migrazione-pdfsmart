package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class DettaglioDocRicevutiDacModel extends CommandDataModel{
   /**
    * dettagli documenti ricevuti nelle DAC
    */
	private StringType service=new StringType();
	private StringType codPrit=new StringType();
	private TimestampType dataSpunta=new TimestampType();
	private StringType barCode=new StringType();
	private StringType codCliente=new StringType();
	private StringType cognomeCliente=new StringType();
	private StringType nomeCliente=new StringType();
	private StringType codAgente=new StringType();
	private StringType nominativoAgente=new StringType();
	private StringType codAgenzia	=new StringType();
	private StringType codProdotto=new StringType();
	private StringType descProdotto=new StringType();
	private StringType codOperazione=new  StringType();
	private StringType descOperazione=new StringType();
	//Ticket: 1008920
	private TimestampType dataInvioInSede = new TimestampType();
	
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
	public TimestampType getDataSpunta() {
		return dataSpunta;
	}
	public void setDataSpunta(TimestampType dataSpunta) {
		this.dataSpunta = dataSpunta;
	}
	public StringType getBarCode() {
		return barCode;
	}
	public void setBarCode(StringType barCode) {
		this.barCode = barCode;
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
	public StringType getNominativoAgente() {
		return nominativoAgente;
	}
	public void setNominativoAgente(StringType nominativoAgente) {
		this.nominativoAgente = nominativoAgente;
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
	public StringType getDescProdotto() {
		return descProdotto;
	}
	public void setDescProdotto(StringType descProdotto) {
		this.descProdotto = descProdotto;
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
	public TimestampType getDataInvioInSede() {
		return dataInvioInSede;
	}
	public void setDataInvioInSede(TimestampType dataInvioInSede) {
		this.dataInvioInSede = dataInvioInSede;
	}
}
