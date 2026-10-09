package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CertificazioneFbModel extends CommandDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6801668373205132701L;
	private StringType 	codAgente = new StringType();	
	private StringType certificazione = new StringType();
	private DateType dataCompletamento = new DateType();
	private StringType codRete = new StringType();
	private StringType userIns = new StringType();
	private DateType dataInserimento = new DateType();
	private StringType userMod = new StringType();
	private DateType dataModifica = new DateType();
	private StringType flagAnnullato = new StringType();
	
	
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	
	public StringType getCertificazione() {
		return certificazione;
	}
	public void setCertificazione(StringType certificazione) {
		this.certificazione = certificazione;
	}
	public DateType getDataCompletamento() {
		return dataCompletamento;
	}
	public void setDataCompletamento(DateType dataCompletamento) {
		this.dataCompletamento = dataCompletamento;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getUserIns() {
		return userIns;
	}
	public void setUserIns(StringType userIns) {
		this.userIns = userIns;
	}
	public DateType getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(DateType dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public StringType getUserMod() {
		return userMod;
	}
	public void setUserMod(StringType userMod) {
		this.userMod = userMod;
	}
	public DateType getDataModifica() {
		return dataModifica;
	}
	public void setDataModifica(DateType dataModifica) {
		this.dataModifica = dataModifica;
	}
	public StringType getFlagAnnullato() {
		return flagAnnullato;
	}
	public void setFlagAnnullato(StringType flagAnnullato) {
		this.flagAnnullato = flagAnnullato;
	}
	
	
}
