package prgm.pdfwebforms.idd;

/***********************************************************************************************/
/***********************************************************************************************/
public class FlagControlloTargetMarketBackupBean {
	
	private String tariffa = "";
	private String codiceCliente = "";
	private String flagControlloTMPostvendita = "";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FlagControlloTargetMarketBackupBean(IddCallModel callModel) {
		setCodiceCliente(callModel.getCodiceCliente().toString());
		setTariffa(callModel.getTariffa().toString());
		setFlagControlloTMPostvendita(callModel.getFlagControlloTMPostvendita().toString());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasSameData(IddCallModel callModel) {
		return callModel.getCodiceCliente().equals(getCodiceCliente()) && callModel.getTariffa().equals(getTariffa());
	}

	public String getTariffa() {
		return tariffa;
	}
	public void setTariffa(String tariffa) {
		this.tariffa = tariffa;
	}
	public String getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(String codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public String getFlagControlloTMPostvendita() {
		return flagControlloTMPostvendita;
	}
	public void setFlagControlloTMPostvendita(String flagControlloTMPostvendita) {
		this.flagControlloTMPostvendita = flagControlloTMPostvendita;
	}
}
