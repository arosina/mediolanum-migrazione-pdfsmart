package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class AdempimentiNormativiModel extends AbstractSectionModel{
	
	private static final long serialVersionUID = 1L;
	
	private static final String S_NAZIONIUIC = "NAZIONIUIC";
	private static final String S_FUNZIONI_PUBBLICHE = "FUNZIONI_PUBBLICHE";
	
	private StringType formaGiuridicaSocietaAppartenenza = new StringType();				// 10303
	
	private StringType haCarichePubbliche = new StringType(); 								// Non persistente
	private StringType caricaPubblicaRicoperta = new StringType();							// 10304
	private StringType dettaglioCaricaPubblicaRicoperta = new StringType();					// 10305
	
	private StringType haLegamiAffariDiversiDaAttivitaPrincipale = new StringType(); 		// Non persistente
	private StringType tipologiaLegameAffariDiversoDaAttivitaPrincipale = new StringType(); // 10306
	private StringType paeseLegameAffariDiversoDaAttivitaPrincipale1 = new StringType();	// 10307
	private StringType paeseLegameAffariDiversoDaAttivitaPrincipale2 = new StringType();	// 10308
	private StringType paeseLegameAffariDiversoDaAttivitaPrincipale3 = new StringType();	// 10309
	
	private StringType haLegamiParentelaConPep = new StringType();							// 10314
	private StringType tipologiaLegameParentelaConPep = new StringType();					// 10310
	private StringType tipologiaFunzionePubblicaLegameParentelaConPep = new StringType();	// 10311
	
	private StringType haLegamiAffariConPep = new StringType();								// 10315
	private StringType tipologiaLegameAffariConPep = new StringType();						// 10316  
	private StringType tipologiaFunzionePubblicaLegameAffariConPep = new StringType();		// 10312
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AdempimentiNormativiModel() {
		
		frontendPropName.add("adempimentiNormativi_formaGiuridicaSocietaAppartenenza");
		frontendPropName.add("adempimentiNormativi_haCarichePubbliche");
		frontendPropName.add("adempimentiNormativi_caricaPubblicaRicoperta");
		frontendPropName.add("adempimentiNormativi_dettaglioCaricaPubblicaRicoperta");
		frontendPropName.add("adempimentiNormativi_haLegamiAffariDiversiDaAttivitaPrincipale");
		frontendPropName.add("adempimentiNormativi_tipologiaLegameAffariDiversoDaAttivitaPrincipale");
		frontendPropName.add("adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale1");
		frontendPropName.add("adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale2");
		frontendPropName.add("adempimentiNormativi_paeseLegameAffariDiversoDaAttivitaPrincipale3");
		frontendPropName.add("adempimentiNormativi_haLegamiParentelaConPep");
		frontendPropName.add("adempimentiNormativi_tipologiaLegameParentelaConPep");
		frontendPropName.add("adempimentiNormativi_tipologiaFunzionePubblicaLegameParentelaConPep");
		frontendPropName.add("adempimentiNormativi_haLegamiAffariConPep");
		frontendPropName.add("adempimentiNormativi_tipologiaLegameAffariConPep");
		frontendPropName.add("adempimentiNormativi_tipologiaFunzionePubblicaLegameAffariConPep");
			
		frontendPropName.add("infoPersonali_fasciaRedditoAnnuale");
		frontendPropName.add("infoPersonali_fasciaPatrimonioComplessivo");
		frontendPropName.add("infoPersonali_combinazioneProvenienzaPatrimonio");
		frontendPropName.add("infoPersonali_codProfessionePrecedente");
		frontendPropName.add("infoPersonali_codProfessione");
		frontendPropName.add("infoPersonali_codSettoreEconomico");
		frontendPropName.add("infoPersonali_nazioneSvolgimentoProfessione");
		frontendPropName.add("infoPersonali_provinciaSvolgimentoProfessione");
		
		frontendPropName.add("residenza_flagPep");
		frontendPropName.add("residenza_motivazionePep");

		addCodDescField("formaGiuridicaSocietaAppartenenza","FORME_GIURIDICHE");
		addCodDescField("caricaPubblicaRicoperta","CARICHE_PUBBLICHE");
		addCodDescField("dettaglioCaricaPubblicaRicoperta","DETTAGLIO_CARICHE_PUBBLICHE");
		
		addCodDescField("tipologiaLegameAffariDiversoDaAttivitaPrincipale","LEGAMI_AFFARI");
		addCodDescField("paeseLegameAffariDiversoDaAttivitaPrincipale1",S_NAZIONIUIC);
		addCodDescField("paeseLegameAffariDiversoDaAttivitaPrincipale2",S_NAZIONIUIC);
		addCodDescField("paeseLegameAffariDiversoDaAttivitaPrincipale3",S_NAZIONIUIC);
		
		addCodDescField("tipologiaLegameParentelaConPep","LEGAMI_PARENTELA_PEP");
		addCodDescField("tipologiaFunzionePubblicaLegameParentelaConPep",S_FUNZIONI_PUBBLICHE);
		
		addCodDescField("tipologiaLegameAffariConPep","LEGAMI_AFFARI_PEP");
		addCodDescField("tipologiaFunzionePubblicaLegameAffariConPep",S_FUNZIONI_PUBBLICHE);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String linkElencoPaesiAltoRischio() {
		return "https://www.bmednet.it/static-assets/uploaded/allegati/20200226_elenco_paesi_alto_rischio.pdf";
	}
	
	public StringType getFormaGiuridicaSocietaAppartenenza() {
		return formaGiuridicaSocietaAppartenenza;
	}

	public void setFormaGiuridicaSocietaAppartenenza(StringType formaGiuridicaSocietaAppartenenza) {
		this.formaGiuridicaSocietaAppartenenza = formaGiuridicaSocietaAppartenenza;
	}

	public StringType getHaCarichePubbliche() {
		return haCarichePubbliche;
	}

	public void setHaCarichePubbliche(StringType haCarichePubbliche) {
		this.haCarichePubbliche = haCarichePubbliche;
	}

	public StringType getCaricaPubblicaRicoperta() {
		return caricaPubblicaRicoperta;
	}

	public void setCaricaPubblicaRicoperta(StringType caricaPubblicaRicoperta) {
		this.caricaPubblicaRicoperta = caricaPubblicaRicoperta;
	}

	public StringType getDettaglioCaricaPubblicaRicoperta() {
		return dettaglioCaricaPubblicaRicoperta;
	}

	public void setDettaglioCaricaPubblicaRicoperta(StringType dettaglioCaricaPubblicaRicoperta) {
		this.dettaglioCaricaPubblicaRicoperta = dettaglioCaricaPubblicaRicoperta;
	}

	public StringType getHaLegamiAffariDiversiDaAttivitaPrincipale() {
		return haLegamiAffariDiversiDaAttivitaPrincipale;
	}

	public void setHaLegamiAffariDiversiDaAttivitaPrincipale(StringType haLegamiAffariDiversiDaAttivitaPrincipale) {
		this.haLegamiAffariDiversiDaAttivitaPrincipale = haLegamiAffariDiversiDaAttivitaPrincipale;
	}

	public StringType getTipologiaLegameAffariDiversoDaAttivitaPrincipale() {
		return tipologiaLegameAffariDiversoDaAttivitaPrincipale;
	}

	public void setTipologiaLegameAffariDiversoDaAttivitaPrincipale(
			StringType tipologiaLegameAffariDiversoDaAttivitaPrincipale) {
		this.tipologiaLegameAffariDiversoDaAttivitaPrincipale = tipologiaLegameAffariDiversoDaAttivitaPrincipale;
	}

	public StringType getPaeseLegameAffariDiversoDaAttivitaPrincipale1() {
		return paeseLegameAffariDiversoDaAttivitaPrincipale1;
	}

	public void setPaeseLegameAffariDiversoDaAttivitaPrincipale1(StringType paeseLegameAffariDiversoDaAttivitaPrincipale1) {
		this.paeseLegameAffariDiversoDaAttivitaPrincipale1 = paeseLegameAffariDiversoDaAttivitaPrincipale1;
	}

	public StringType getPaeseLegameAffariDiversoDaAttivitaPrincipale2() {
		return paeseLegameAffariDiversoDaAttivitaPrincipale2;
	}

	public void setPaeseLegameAffariDiversoDaAttivitaPrincipale2(StringType paeseLegameAffariDiversoDaAttivitaPrincipale2) {
		this.paeseLegameAffariDiversoDaAttivitaPrincipale2 = paeseLegameAffariDiversoDaAttivitaPrincipale2;
	}

	public StringType getPaeseLegameAffariDiversoDaAttivitaPrincipale3() {
		return paeseLegameAffariDiversoDaAttivitaPrincipale3;
	}

	public void setPaeseLegameAffariDiversoDaAttivitaPrincipale3(StringType paeseLegameAffariDiversoDaAttivitaPrincipale3) {
		this.paeseLegameAffariDiversoDaAttivitaPrincipale3 = paeseLegameAffariDiversoDaAttivitaPrincipale3;
	}

	public StringType getHaLegamiParentelaConPep() {
		return haLegamiParentelaConPep;
	}

	public void setHaLegamiParentelaConPep(StringType haLegamiParentelaConPep) {
		this.haLegamiParentelaConPep = haLegamiParentelaConPep;
	}

	public StringType getTipologiaLegameParentelaConPep() {
		return tipologiaLegameParentelaConPep;
	}

	public void setTipologiaLegameParentelaConPep(StringType tipologiaLegameParentelaConPep) {
		this.tipologiaLegameParentelaConPep = tipologiaLegameParentelaConPep;
	}

	public StringType getTipologiaFunzionePubblicaLegameParentelaConPep() {
		return tipologiaFunzionePubblicaLegameParentelaConPep;
	}

	public void setTipologiaFunzionePubblicaLegameParentelaConPep(
			StringType tipologiaFunzionePubblicaLegameParentelaConPep) {
		this.tipologiaFunzionePubblicaLegameParentelaConPep = tipologiaFunzionePubblicaLegameParentelaConPep;
	}

	public StringType getHaLegamiAffariConPep() {
		return haLegamiAffariConPep;
	}

	public void setHaLegamiAffariConPep(StringType haLegamiAffariConPep) {
		this.haLegamiAffariConPep = haLegamiAffariConPep;
	}

	public StringType getTipologiaLegameAffariConPep() {
		return tipologiaLegameAffariConPep;
	}

	public void setTipologiaLegameAffariConPep(StringType tipologiaLegameAffariConPep) {
		this.tipologiaLegameAffariConPep = tipologiaLegameAffariConPep;
	}

	public StringType getTipologiaFunzionePubblicaLegameAffariConPep() {
		return tipologiaFunzionePubblicaLegameAffariConPep;
	}

	public void setTipologiaFunzionePubblicaLegameAffariConPep(StringType tipologiaFunzionePubblicaLegameAffariConPep) {
		this.tipologiaFunzionePubblicaLegameAffariConPep = tipologiaFunzionePubblicaLegameAffariConPep;
	}

}
