package prgm.pdfwebforms.drivers.io.squadra;

import java.util.ArrayList;
import java.util.List;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSquadraDataResponse{

	private List<ElementoSquadra> squadra = new ArrayList<ElementoSquadra>();

	public List<ElementoSquadra> getSquadra() {
		return squadra;
	}

	public void setSquadra(List<ElementoSquadra> squadra) {
		this.squadra = squadra;
	}
	
}
