package prgm.pdfwebforms.pritmom;

import java.util.ArrayList;

/********************************************************************************/
/********************************************************************************/
public class PritMomInfo {
	
	private String 	chiave;					// Quando valorizzata i codici prodotto/operazione vengono letti, tramite CODE o MOM_CODE, dalla PDF_INFO_PRIT
											// Se non trovata con CODE o MOM_CODE, "chiave" viene cercata con CODE='*', ossia "tutti i moduli"
	private int 	codProdotto = 0;		// Utilizzato solo in caso di "chiavePrit" non valorizzata o non ritrovata sulla PDF_INFO_PRIT
	private int 	codOperazione = 0;		// Utilizzato solo in caso di "chiavePrit" non valorizzata o non ritrovata sulla PDF_INFO_PRIT
	
	// 	Elenco dei "prodotti attivati", dati utili alla sede la cui valorizzazione deve rientrare nelle specifiche di ciascuna singola operazione
	private ArrayList<PritMomInfoProdottoAttivato> prodottiAttivati = new ArrayList<PritMomInfoProdottoAttivato>();

	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
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
	public ArrayList<PritMomInfoProdottoAttivato> getProdottiAttivati() {
		return prodottiAttivati;
	}
	public void setProdottiAttivati(ArrayList<PritMomInfoProdottoAttivato> prodottiAttivati) {
		this.prodottiAttivati = prodottiAttivati;
	}
	
}
