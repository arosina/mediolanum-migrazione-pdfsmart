package prgm.pdfwebforms.aml.model;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SoggettoRelazioneModel extends CommandDataModel{
	
	public static String COD_TIPO_RELAZIONE_AZIENDALE 	= "003";
	public static String COD_TIPO_RELAZIONE_ALTRO 	 	= "004";
		
	public static String TIPO_MOTIVAZIONE_TERZO_PAGATORE_DONAZIONE = "DONAZIONE";
	public static String TIPO_MOTIVAZIONE_TERZO_PAGATORE_CORRISPETTIVO = "CORRISPETTIVO";
	
	private StringType 	codiceFiscalePartitaIva = new StringType();
	private StringType 	idCensimento = new StringType();
	private StringType 	ndg = new StringType();
	private StringType 	nome = new StringType();
	private StringType 	cognome = new StringType();
	
	private StringType  isGiaCliente = new StringType();
	private BooleanType isPersonaFisica = new BooleanType();
	
	private BooleanType isCosottoscrittore = new BooleanType();	
	private BooleanType isTerzoPagatore = new BooleanType();		
	private BooleanType isAssicurato = new BooleanType();	
	private BooleanType isBeneficiario = new BooleanType();	
	private BooleanType isBeneficiarioPG = new BooleanType();
	private BooleanType isTitolareBeneficiarioPG = new BooleanType();
	
	private boolean		relazioneContraentePreselezionata;
	private StringType 	codTipoRelazioneContraente = new StringType();
	private StringType 	descrTipoRelazioneContraente = new StringType();
	private StringType 	informazioniRelazioneContraente = new StringType();
	
	private StringType 	codTipoRelazioneAssicurando = new StringType();
	private StringType 	descrTipoRelazioneAssicurando = new StringType();
	private StringType 	informazioniRelazioneAssicurando = new StringType();
	
	private StringType 	tipoMotivazionePagamentoTerzoPagatore = new StringType();
	private StringType 	dettagliMotivazionePagamentoTerzoPagatore = new StringType();
	private StringType 	motivazioneAssicuratoDiversoDaContraente = new StringType();
	
	private List<SoggettoRelazioneModel> titolariBeneficiarioPG = new ArrayList<>();

	// Relazioni assicurati/beneficiari
	private ListType 	elencoBeneficiariAssicurato = new ListType(SoggettoRelazioneModel.class);
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void impostaCodTipoRelazioneContraente(RelazioniModel relazioni, String codTipoRelazione, String descrTipoRelazione) {
		if(codTipoRelazione.isEmpty())
			return;
		if(!codTipoRelazione.isEmpty() && !this.codTipoRelazioneContraente.isNull() && !codTipoRelazione.equals(this.codTipoRelazioneContraente.toString()))
			relazioni.setRelazioniContrastanti(true);
		this.codTipoRelazioneContraente = new StringType(codTipoRelazione);
		this.relazioneContraentePreselezionata = true;
		if(codTipoRelazione.equals(COD_TIPO_RELAZIONE_ALTRO))
			this.descrTipoRelazioneContraente = new StringType(descrTipoRelazione);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void impostaCodTipoRelazioneAssicurando(RelazioniModel relazioni, String codTipoRelazione, String descrTipoRelazione) {
		if(codTipoRelazione.isEmpty())
			return;
		this.codTipoRelazioneAssicurando = new StringType(codTipoRelazione);
		if(codTipoRelazione.equals(COD_TIPO_RELAZIONE_ALTRO))
			this.descrTipoRelazioneAssicurando = new StringType(descrTipoRelazione);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean iSoggettoVisibileInAML() {
		return  getIsCosottoscrittore().booleanValue() || getIsTerzoPagatore().booleanValue() || 
				getIsAssicurato().booleanValue() ||	getIsBeneficiario().booleanValue() || getIsTitolareBeneficiarioPG().booleanValue();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public SoggettoRelazioneModel findBeneficiarioAssicurato(SoggettoRelazioneModel beneficiario) {
		for(int i=0;i<getElencoBeneficiariAssicurato().size();i++) {
			SoggettoRelazioneModel s = (SoggettoRelazioneModel)getElencoBeneficiariAssicurato().get(i);
			if(beneficiario.sameAs(s))
				return s;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean sameAs(SoggettoRelazioneModel otherSogg) {
		return (!getNdg().isNull() && !otherSogg.getNdg().isNull() && getNdg().equals(otherSogg.getNdg())) || 
				getCodiceFiscalePartitaIva().equals(otherSogg.getCodiceFiscalePartitaIva());
	}
	
	public StringType getCodiceFiscalePartitaIva() {
		return codiceFiscalePartitaIva;
	}

	public void setCodiceFiscalePartitaIva(StringType codiceFiscalePartitaIva) {
		this.codiceFiscalePartitaIva = codiceFiscalePartitaIva;
	}

	public StringType getIdCensimento() {
		return idCensimento;
	}

	public void setIdCensimento(StringType idCensimento) {
		this.idCensimento = idCensimento;
	}

	public StringType getNdg() {
		return ndg;
	}

	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}

	public StringType getNome() {
		return nome;
	}

	public void setNome(StringType nome) {
		this.nome = nome;
	}

	public StringType getCognome() {
		return cognome;
	}

	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}

	public StringType getIsGiaCliente() {
		return isGiaCliente;
	}

	public void setIsGiaCliente(StringType isGiaCliente) {
		this.isGiaCliente = isGiaCliente;
	}

	public BooleanType getIsPersonaFisica() {
		return isPersonaFisica;
	}

	public void setIsPersonaFisica(BooleanType isPersonaFisica) {
		this.isPersonaFisica = isPersonaFisica;
	}

	public BooleanType getIsCosottoscrittore() {
		return isCosottoscrittore;
	}

	public void setIsCosottoscrittore(BooleanType isCosottoscrittore) {
		this.isCosottoscrittore = isCosottoscrittore;
	}

	public BooleanType getIsTerzoPagatore() {
		return isTerzoPagatore;
	}

	public void setIsTerzoPagatore(BooleanType isTerzoPagatore) {
		this.isTerzoPagatore = isTerzoPagatore;
	}

	public BooleanType getIsAssicurato() {
		return isAssicurato;
	}

	public void setIsAssicurato(BooleanType isAssicurato) {
		this.isAssicurato = isAssicurato;
	}

	public BooleanType getIsBeneficiario() {
		return isBeneficiario;
	}

	public void setIsBeneficiario(BooleanType isBeneficiario) {
		this.isBeneficiario = isBeneficiario;
	}

	public BooleanType getIsBeneficiarioPG() {
		return isBeneficiarioPG;
	}

	public void setIsBeneficiarioPG(BooleanType isBeneficiarioPG) {
		this.isBeneficiarioPG = isBeneficiarioPG;
	}

	public BooleanType getIsTitolareBeneficiarioPG() {
		return isTitolareBeneficiarioPG;
	}

	public void setIsTitolareBeneficiarioPG(BooleanType isTitolareBeneficiarioPG) {
		this.isTitolareBeneficiarioPG = isTitolareBeneficiarioPG;
	}

	public boolean isRelazioneContraentePreselezionata() {
		return relazioneContraentePreselezionata;
	}

	public void setRelazioneContraentePreselezionata(boolean relazioneContraentePreselezionata) {
		this.relazioneContraentePreselezionata = relazioneContraentePreselezionata;
	}

	public StringType getCodTipoRelazioneContraente() {
		return codTipoRelazioneContraente;
	}

	public void setCodTipoRelazioneContraente(StringType codTipoRelazioneContraente) {
		this.codTipoRelazioneContraente = codTipoRelazioneContraente;
	}

	public StringType getDescrTipoRelazioneContraente() {
		return descrTipoRelazioneContraente;
	}

	public void setDescrTipoRelazioneContraente(StringType descrTipoRelazioneContraente) {
		this.descrTipoRelazioneContraente = descrTipoRelazioneContraente;
	}

	public StringType getInformazioniRelazioneContraente() {
		return informazioniRelazioneContraente;
	}

	public void setInformazioniRelazioneContraente(StringType informazioniRelazioneContraente) {
		this.informazioniRelazioneContraente = informazioniRelazioneContraente;
	}

	public StringType getCodTipoRelazioneAssicurando() {
		return codTipoRelazioneAssicurando;
	}

	public void setCodTipoRelazioneAssicurando(StringType codTipoRelazioneAssicurando) {
		this.codTipoRelazioneAssicurando = codTipoRelazioneAssicurando;
	}

	public StringType getDescrTipoRelazioneAssicurando() {
		return descrTipoRelazioneAssicurando;
	}

	public void setDescrTipoRelazioneAssicurando(StringType descrTipoRelazioneAssicurando) {
		this.descrTipoRelazioneAssicurando = descrTipoRelazioneAssicurando;
	}

	public StringType getInformazioniRelazioneAssicurando() {
		return informazioniRelazioneAssicurando;
	}

	public void setInformazioniRelazioneAssicurando(StringType informazioniRelazioneAssicurando) {
		this.informazioniRelazioneAssicurando = informazioniRelazioneAssicurando;
	}

	public StringType getTipoMotivazionePagamentoTerzoPagatore() {
		return tipoMotivazionePagamentoTerzoPagatore;
	}

	public void setTipoMotivazionePagamentoTerzoPagatore(StringType tipoMotivazionePagamentoTerzoPagatore) {
		this.tipoMotivazionePagamentoTerzoPagatore = tipoMotivazionePagamentoTerzoPagatore;
	}

	public StringType getDettagliMotivazionePagamentoTerzoPagatore() {
		return dettagliMotivazionePagamentoTerzoPagatore;
	}

	public void setDettagliMotivazionePagamentoTerzoPagatore(StringType dettagliMotivazionePagamentoTerzoPagatore) {
		this.dettagliMotivazionePagamentoTerzoPagatore = dettagliMotivazionePagamentoTerzoPagatore;
	}

	public List<SoggettoRelazioneModel> getTitolariBeneficiarioPG() {
		return titolariBeneficiarioPG;
	}

	public void setTitolariBeneficiarioPG(List<SoggettoRelazioneModel> titolariBeneficiarioPG) {
		this.titolariBeneficiarioPG = titolariBeneficiarioPG;
	}

	public ListType getElencoBeneficiariAssicurato() {
		return elencoBeneficiariAssicurato;
	}

	public void setElencoBeneficiariAssicurato(ListType elencoBeneficiariAssicurato) {
		this.elencoBeneficiariAssicurato = elencoBeneficiariAssicurato;
	}

	public StringType getMotivazioneAssicuratoDiversoDaContraente() {
		return motivazioneAssicuratoDiversoDaContraente;
	}

	public void setMotivazioneAssicuratoDiversoDaContraente(StringType motivazioneAssicuratoDiversoDaContraente) {
		this.motivazioneAssicuratoDiversoDaContraente = motivazioneAssicuratoDiversoDaContraente;
	}

}