package prgm.pdfwebforms.basket;

import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BasketContoInfo{
	
	private String 		numeroContoControlloSaldo = "";
	private DoubleType 	importoNecessario = new DoubleType();
	private DoubleType 	importoRadar = new DoubleType();
	
	public String getNumeroContoControlloSaldo() {
		return numeroContoControlloSaldo;
	}
	public void setNumeroContoControlloSaldo(String numeroContoControlloSaldo) {
		this.numeroContoControlloSaldo = numeroContoControlloSaldo;
	}
	public DoubleType getImportoNecessario() {
		return importoNecessario;
	}
	public void setImportoNecessario(DoubleType importoNecessario) {
		this.importoNecessario = importoNecessario;
	}
	public DoubleType getImportoRadar() {
		return importoRadar;
	}
	public void setImportoRadar(DoubleType importoRadar) {
		this.importoRadar = importoRadar;
	}
	
}
