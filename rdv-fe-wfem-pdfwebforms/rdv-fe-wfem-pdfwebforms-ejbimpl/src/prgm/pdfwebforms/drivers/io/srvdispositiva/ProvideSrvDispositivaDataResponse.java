package prgm.pdfwebforms.drivers.io.srvdispositiva;

import java.util.ArrayList;
import java.util.List;

import prgm.pdfwebforms.mom.SoggettoDispositivaCallModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSrvDispositivaDataResponse{
	
	// Elenco di soggetti "agente", esplicitati dal driver, che sostituisce l'elemento agente
	// gestito a livello centralizzato
	private ArrayList<SoggettoDispositivaCallModel> agentiDispositiva = new ArrayList<SoggettoDispositivaCallModel>();
	// Elenco di soggetti "cliente", esplicitati dal driver, che vengono aggiunti a quelli gestiti a livello centralizzato
	private List<SoggettoDispositivaCallModel> 		clientiAggiuntiviDispositiva = new ArrayList<SoggettoDispositivaCallModel>();
		
	private List<DispoAggiuntiva> 			   		dispoAggiuntive = new ArrayList<DispoAggiuntiva>();			// Dispositive aggiuntive oltre quella principale rappresentata dal pdf stesso
	private List<VincoloDispositiva> 			   	vincoliDispositive = new ArrayList<VincoloDispositiva>();	// Vincoli tra le dispositive

	public ArrayList<SoggettoDispositivaCallModel> getAgentiDispositiva() {
		return agentiDispositiva;
	}

	public void setAgentiDispositiva(ArrayList<SoggettoDispositivaCallModel> agentiDispositiva) {
		this.agentiDispositiva = agentiDispositiva;
	}

	public List<DispoAggiuntiva> getDispoAggiuntive() {
		return dispoAggiuntive;
	}

	public void setDispoAggiuntive(List<DispoAggiuntiva> dispoAggiuntive) {
		this.dispoAggiuntive = dispoAggiuntive;
	}

	public List<VincoloDispositiva> getVincoliDispositive() {
		return vincoliDispositive;
	}

	public void setVincoliDispositive(List<VincoloDispositiva> vincoliDispositive) {
		this.vincoliDispositive = vincoliDispositive;
	}

	public List<SoggettoDispositivaCallModel> getClientiAggiuntiviDispositiva() {
		return clientiAggiuntiviDispositiva;
	}

	public void setClientiAggiuntiviDispositiva(List<SoggettoDispositivaCallModel> clientiAggiuntiviDispositiva) {
		this.clientiAggiuntiviDispositiva = clientiAggiuntiviDispositiva;
	}


}
