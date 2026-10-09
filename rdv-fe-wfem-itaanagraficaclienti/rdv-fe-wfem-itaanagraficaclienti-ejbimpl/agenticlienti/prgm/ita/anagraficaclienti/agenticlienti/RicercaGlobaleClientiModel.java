package prgm.ita.anagraficaclienti.agenticlienti;

import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaGlobaleClientiModel extends PopupClientiModel implements GridDecorator{

	private boolean primaAttivazione = true;	
	private IntegerType idxClienteSelezionato = new IntegerType();
	
	private ListType elencoAgentiCliente = new ListType(AgenteModel.class); 
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName, CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		FieldStyle fs = new FieldStyle();
		fs.textAlign = "center";

		if(listPropertyName.equals("elenco")){
			if(cellPropertyName.equals("cognome") ||
			   cellPropertyName.equals("nome") ||
			   cellPropertyName.equals("comuneNascita_comune"))
				fs.textAlign = "left";
		}else if(listPropertyName.equals("elencoAgentiCliente")){
			if(cellPropertyName.equals("nominativoAgente"))
				fs.textAlign = "left";
			AgenteModel agente = (AgenteModel)row;
			if(agente.getDataFineAssegnazione().isNull())
				fs.backgroundColor = "lightgreen";
		}
		
		cell.setStyle(fs);
	}
	
	public boolean isPrimaAttivazione() {
		return primaAttivazione;
	}
	
	public void setPrimaAttivazione(boolean primaAttivazione) {
		this.primaAttivazione = primaAttivazione;
	}
	
	public IntegerType getIdxClienteSelezionato() {
		return idxClienteSelezionato;
	}

	public void setIdxClienteSelezionato(IntegerType idxClienteSelezionato) {
		this.idxClienteSelezionato = idxClienteSelezionato;
	}

	public ListType getElencoAgentiCliente() {
		return elencoAgentiCliente;
	}

	public void setElencoAgentiCliente(ListType elencoAgentiCliente) {
		this.elencoAgentiCliente = elencoAgentiCliente;
	}
}
