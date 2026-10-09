package prgm.pdfwebforms.drivers.io.srvdispositiva;

import java.util.ArrayList;
import java.util.List;

import prgm.pdfwebforms.mom.SoggettoDispositivaCallModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DispoAggiuntiva{

	/**************************************************************************************************
	 * Informazioni per accedere alla tabella PDF_INFO_PRIT per le dispositive aggiuntive
	**************************************************************************************************/
	public class PritRetrieveInfo{
		private String 		pdfCode = "";
		private String 		chiave = "";
		public String getPdfCode() { return pdfCode; }
		public void setPdfCode(String pdfCode) { this.pdfCode = pdfCode; }
		public String getChiave() { return chiave; }
		public void setChiave(String chiave) { this.chiave = chiave; }
	}
	
	private String		tipoDispositiva = "";
	private String 		descrizione = "";
	private String 		barcode = "";
	private String 		numeroContratto = "";
	private Double 		valoreDispositiva = null;
	private String 		numeroProposta = "";
	private String 		codicePropostaCorrelata = "";
	private PritRetrieveInfo pritRetrieveInfo = new PritRetrieveInfo();
	private List<SoggettoDispositivaCallModel> soggetti = new ArrayList<SoggettoDispositivaCallModel>();
	
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(String numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public Double getValoreDispositiva() {
		return valoreDispositiva;
	}
	public void setValoreDispositiva(Double valoreDispositiva) {
		this.valoreDispositiva = valoreDispositiva;
	}
	public String getNumeroProposta() {
		return numeroProposta;
	}
	public void setNumeroProposta(String numeroProposta) {
		this.numeroProposta = numeroProposta;
	}
	public String getCodicePropostaCorrelata() {
		return codicePropostaCorrelata;
	}
	public void setCodicePropostaCorrelata(String codicePropostaCorrelata) {
		this.codicePropostaCorrelata = codicePropostaCorrelata;
	}
	public PritRetrieveInfo getPritRetrieveInfo() {
		return pritRetrieveInfo;
	}
	public List<SoggettoDispositivaCallModel> getSoggetti() {
		return soggetti;
	}
	public String getTipoDispositiva() {
		return tipoDispositiva;
	}
	public void setTipoDispositiva(String tipoDispositiva) {
		this.tipoDispositiva = tipoDispositiva;
	}

}
