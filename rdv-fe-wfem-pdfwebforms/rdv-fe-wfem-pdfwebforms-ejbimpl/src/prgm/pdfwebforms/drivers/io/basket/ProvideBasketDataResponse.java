package prgm.pdfwebforms.drivers.io.basket;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideBasketDataResponse extends CommandDataModel{
	
	public static String TIPO_OPERAZIONE_RIMBORSO 	= "RIMBORSO";
	public static String TIPO_OPERAZIONE_RISCATTO 	= "RISCATTO";
	
	public static String TIPO_CONTROLLO_SALDO_DISPONIBILE 	= "DISPONIBILE";
	public static String TIPO_CONTROLLO_SALDO_RADAR 		= "RADAR";
	public static String TIPO_CONTROLLO_SALDO_NESSUNO 		= "NESSUNO";

	private String 		tipoOperazione = "";
	private String 		tipoControlloSaldo = TIPO_CONTROLLO_SALDO_DISPONIBILE;
	private StringType 	numeroContoControlloSaldo = new StringType();
	private DoubleType 	importoControlloSaldo = new DoubleType();
	
	public String getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(String tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public String getTipoControlloSaldo() {
		return tipoControlloSaldo;
	}
	public void setTipoControlloSaldo(String tipoControlloSaldo) {
		this.tipoControlloSaldo = tipoControlloSaldo;
	}
	public StringType getNumeroContoControlloSaldo() {
		return numeroContoControlloSaldo;
	}
	public void setNumeroContoControlloSaldo(StringType numeroContoControlloSaldo) {
		this.numeroContoControlloSaldo = numeroContoControlloSaldo;
	}
	public DoubleType getImportoControlloSaldo() {
		return importoControlloSaldo;
	}
	public void setImportoControlloSaldo(DoubleType importoControlloSaldo) {
		this.importoControlloSaldo = importoControlloSaldo;
	}
	
}
