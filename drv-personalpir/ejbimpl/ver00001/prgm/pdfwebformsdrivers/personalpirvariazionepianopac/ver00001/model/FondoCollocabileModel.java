package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutoCompleteModel;


public class FondoCollocabileModel extends CommandDataModel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2486428097882839010L;
	private StringType campoData            = new StringType();
	private StringType tariffa 				= new StringType();
	private StringType origine 				= new StringType();
	private StringType isin 				= new StringType();
	private boolean isFondoPartenza         = false;
	NumeroPolizzaAutoCompleteModel polizzaModel = new NumeroPolizzaAutoCompleteModel();
	
	public StringType getTariffa() {
		return tariffa;
	}
	public StringType getOrigine() {
		return origine;
	}
	public StringType getIsin() {
		return isin;
	}
	public StringType getCampoData() {
		return campoData;
	}
	public NumeroPolizzaAutoCompleteModel getPolizzaModel() {
		return polizzaModel;
	}
	public void setTariffa(StringType tariffa) {
		this.tariffa = tariffa;
	}
	public void setOrigine(StringType origine) {
		this.origine = origine;
	}
	public void setIsin(StringType isin) {
		this.isin = isin;
	}
	public void setCampoData(StringType campoData) {
		this.campoData = campoData;
	}
	public void setPolizzaModel(NumeroPolizzaAutoCompleteModel polizzaModel) {
		this.polizzaModel = polizzaModel;
	}
	public boolean isFondoPartenza() {
		return isFondoPartenza;
	}
	public void setFondoPartenza(boolean isFondoPartenza) {
		this.isFondoPartenza = isFondoPartenza;
	}

	
	
}
