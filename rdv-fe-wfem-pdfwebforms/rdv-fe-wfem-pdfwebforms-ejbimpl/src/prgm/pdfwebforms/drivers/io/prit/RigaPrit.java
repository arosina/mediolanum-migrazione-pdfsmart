package prgm.pdfwebforms.drivers.io.prit;

import java.util.ArrayList;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class RigaPrit{
	
	private String 	numeroContratto;
	private String 	barcode;				// Barcode del documento, quando generato o conosciuto
	private String 	chiavePrit;				// Quando valorizzata i codici prodotto/operazione vengono letti, tramite CODE o MOM_CODE, dalla PDF_INFO_PRIT
											// Se non trovata con CODE o MOM_CODE, chiavePrit viene cercata con CODE='*', ossia "tutti i moduli"
	private int 	codProdotto;			// Utilizzato solo in caso di "chiavePrit" non valorizzata o non ritrovata sulla PDF_INFO_PRIT
	private int 	codOperazione;			// Utilizzato solo in caso di "chiavePrit" non valorizzata o non ritrovata sulla PDF_INFO_PRIT
	
	private ArrayList<MezzoPagamentoRigaPrit> mezziDiPagamento = new ArrayList<MezzoPagamentoRigaPrit>();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addMezzoDiPagamento(MezzoPagamentoRigaPrit mezzoPg){
		getMezziDiPagamento().add(mezzoPg);
	}
	

	public int getCodProdotto() {
		return codProdotto;
	}

	public void setCodProdotto(int codProdotto) {
		this.codProdotto = codProdotto;
	}

	public int getCodOperazione() {
		return codOperazione;
	}

	public void setCodOperazione(int codOperazione) {
		this.codOperazione = codOperazione;
	}

	public ArrayList<MezzoPagamentoRigaPrit> getMezziDiPagamento() {
		return mezziDiPagamento;
	}

	public void setMezziDiPagamento(
			ArrayList<MezzoPagamentoRigaPrit> mezziDiPagamento) {
		this.mezziDiPagamento = mezziDiPagamento;
	}


	public String getNumeroContratto() {
		return numeroContratto;
	}


	public void setNumeroContratto(String numeroContratto) {
		this.numeroContratto = numeroContratto;
	}


	public String getChiavePrit() {
		return chiavePrit;
	}


	public void setChiavePrit(String chiavePrit) {
		this.chiavePrit = chiavePrit;
	}


	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
