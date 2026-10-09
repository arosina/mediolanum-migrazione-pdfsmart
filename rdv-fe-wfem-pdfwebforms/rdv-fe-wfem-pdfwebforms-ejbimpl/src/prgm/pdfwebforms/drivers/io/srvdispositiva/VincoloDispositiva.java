package prgm.pdfwebforms.drivers.io.srvdispositiva;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class VincoloDispositiva{
	
	public static int INDICE_DISPOSITIVA_PRINCIPALE = -1;
	
	// Gli indici sono riferiti all'elenco aggiuntivo di dispositive "dispoAggiuntive" (0=la prima, 1=la seconda etc.)
	// Utilizzare INDICE_DISPOSITIVA_PRINCIPALE per riferirsi alla dispositiva reale, ossia al pdf
	private int indiceDispositivaVincolante; 
	private int indiceDispositivaVincolata;
	
	public int getIndiceDispositivaVincolante() {
		return indiceDispositivaVincolante;
	}
	public void setIndiceDispositivaVincolante(int indiceDispositivaVincolante) {
		this.indiceDispositivaVincolante = indiceDispositivaVincolante;
	}
	public int getIndiceDispositivaVincolata() {
		return indiceDispositivaVincolata;
	}
	public void setIndiceDispositivaVincolata(int indiceDispositivaVincolata) {
		this.indiceDispositivaVincolata = indiceDispositivaVincolata;
	}
}
