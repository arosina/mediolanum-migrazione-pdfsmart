package prgm.pdfwebforms.drivers.io.squadra;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ElementoSquadra implements Serializable{
	
	public static class Ruoli {
		private Ruoli() {}
		public static String SOTTOSCRITTORE 					= "29";
		public static String CONTRAENTE						 	= "21";
		public static String COSOTTOSCRITTORE 					= "13";
		public static String LEGALERAPPRESENTANTE 				= "30";
		public static String ASSICURATO 						= "11";
		public static String TERZOPAGATORE 						= "31";
		public static String BENEFICIARIO 						= "28";
		public static String TITOLARE_EFFETTIVO_BENEFICIARIO 	= "32";
	}	
	
	private String nomeCampoNdg = "";
	private String nomeCampoIdCensimento = "";
	private String nomiCampoErrore = ""; 	// separati da &. Se vuoto viene usato "nomeCampoNdg"
	private String ruoli = ""; 				// se più di uno separati da &.
	
	public String getNomeCampoNdg() {
		return nomeCampoNdg;
	}
	public void setNomeCampoNdg(String nomeCampoNdg) {
		this.nomeCampoNdg = nomeCampoNdg;
	}
	public String getNomeCampoIdCensimento() {
		return nomeCampoIdCensimento;
	}
	public void setNomeCampoIdCensimento(String nomeCampoIdCensimento) {
		this.nomeCampoIdCensimento = nomeCampoIdCensimento;
	}
	public String getNomiCampoErrore() {
		return nomiCampoErrore;
	}
	public void setNomiCampoErrore(String nomiCampoErrore) {
		this.nomiCampoErrore = nomiCampoErrore;
	}
	public String getRuoli() {
		return ruoli;
	}
	public void setRuoli(String ruoli) {
		this.ruoli = ruoli;
	}
	
}
