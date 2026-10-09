package prgm.ita.p.dac.popup.model;

import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class PopupContrattiModel extends ParamsModel {
	
	private boolean primaVolta = true;
	
	private AgenteModel agenteCollegato = new AgenteModel();
	
	private IntegerType tipoRicerca = new IntegerType(PopupClientiModel.RICERCA_MIEI_CLIENTI);
	
	private StringType 	numeroContratto = new StringType();
	private ListType	elencoContratti = new ListType(PopupContrattoModel.class);
	
	/********************************************************************************/
	/********************************************************************************/
	public boolean esisteContrattoChiuso(){
		for(int i=0;i<getElencoContratti().size();i++){
			PopupContrattoModel c = (PopupContrattoModel)getElencoContratti().get(i);
			if(c.getIsChiuso().intValue() > 0)
				return true;
		}
		return false;
	}
	
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public IntegerType getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(IntegerType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public boolean isPrimaVolta() {
		return primaVolta;
	}
	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}
	public ListType getElencoContratti() {
		return elencoContratti;
	}
	public void setElencoContratti(ListType elencoContratti) {
		this.elencoContratti = elencoContratti;
	}
	public AgenteModel getAgenteCollegato() {
		return agenteCollegato;
	}
	public void setAgenteCollegato(AgenteModel agenteCollegato) {
		this.agenteCollegato = agenteCollegato;
	}
	
}
