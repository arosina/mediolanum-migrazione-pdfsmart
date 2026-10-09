package prgm.pdfwebforms.drivers.io.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideReportAdeguatezzaDataResponse extends CommandDataModel{
	
	private BooleanType 		calcolaSost = new BooleanType();
	private DispositivaReportAdeguatezzaModel	dispositiva = new DispositivaReportAdeguatezzaModel();
	private ListType			listaDispositive = new ListType(DispositivaReportAdeguatezzaModel.class);
	
	public BooleanType getCalcolaSost() {
		return calcolaSost;
	}
	public void setCalcolaSost(BooleanType calcolaSost) {
		this.calcolaSost = calcolaSost;
	}
	public DispositivaReportAdeguatezzaModel getDispositiva() {
		return dispositiva;
	}
	public void setDispositiva(DispositivaReportAdeguatezzaModel dispositiva) {
		this.dispositiva = dispositiva;
	}
	public ListType getListaDispositive() {
		return listaDispositive;
	}
	public void setListaDispositive(ListType listaDispositive) {
		this.listaDispositive = listaDispositive;
	}

}
