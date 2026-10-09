package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;

/***********************************************************************************************/
/***********************************************************************************************/
public class FondoModel extends CommandDataModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3304382674494055927L;
	private StringType	codiceFondo 				= new StringType();
	private StringType 	descFondo 					= new StringType();
	private StringType 	classe 						= new StringType();
	private StringType 	denominazione 				= new StringType();
	private StringType 	isin 						= new StringType();
	private StringType 	sicav 						= new StringType();
	private StringType 	tipologia 					= new StringType();
	private StringType 	lineaFondo 					= new StringType();
	private StringType 	codiceDivisa 				= new StringType();
	private IntegerType	profiloRischioMIFID 		= new IntegerType();
	private IntegerType orizzonteTempMIFID 			= new IntegerType();
	private StringType 	bancaDepositaria 			= new StringType();
	private DateType 	dataSottoscrizione 			= new DateType();
	private DateType 	dataChiusura 				= new DateType();
	private StringType 	causaleChiusura 			= new StringType();
	private StringType 	isinFusione 				= new StringType();
	private StringType 	flagConsolidaAz 			= new StringType();
	private StringType 	flagConsolidaOb 			= new StringType();
	private StringType 	flagPicProgrammato 			= new StringType();
	private StringType 	tipoQuota 					= new StringType();
	private StringType 	bloccoOperazioni 			= new StringType();	
	private StringType 	linkKIID 					= new StringType();
	private DoubleType 	importoMinimo 				= new DoubleType();
	private StringType 	flagPicProgrInEssere 		= new StringType();
	private StringType 	numeroQuote 				= new StringType();
	private DoubleType 	valoreQuota					= new DoubleType();
	private DoubleType 	controvaloreComparto		= new DoubleType();
	
	private BooleanType selected 					= new BooleanType(false);
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompletionLabel(){
		return getIsin()+" "+getDescFondo()+ (getControvaloreComparto().doubleValue()>0?" Controvalore: "+Utility.formattaImporto(getControvaloreComparto().doubleValue())+"&euro;":"");
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FondoModel(){
		selected.setEditable(true);
	}
	public StringType getCodiceFondo() {
		return codiceFondo;
	}
	public void setCodiceFondo(StringType codiceFondo) {
		this.codiceFondo = codiceFondo;
	}
	public StringType getDescFondo() {
		return descFondo;
	}
	public void setDescFondo(StringType descFondo) {
		this.descFondo = descFondo;
	}
	public StringType getClasse() {
		return classe;
	}
	public void setClasse(StringType classe) {
		this.classe = classe;
	}
	public StringType getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(StringType denominazione) {
		this.denominazione = denominazione;
	}
	public StringType getIsin() {
		return isin;
	}
	public void setIsin(StringType isin) {
		this.isin = isin;
	}
	public StringType getSicav() {
		return sicav;
	}
	public void setSicav(StringType sicav) {
		this.sicav = sicav;
	}
	public StringType getTipologia() {
		return tipologia;
	}
	public void setTipologia(StringType tipologia) {
		this.tipologia = tipologia;
	}
	public StringType getLineaFondo() {
		return lineaFondo;
	}
	public void setLineaFondo(StringType lineaFondo) {
		this.lineaFondo = lineaFondo;
	}
	public StringType getCodiceDivisa() {
		return codiceDivisa;
	}
	public void setCodiceDivisa(StringType codiceDivisa) {
		this.codiceDivisa = codiceDivisa;
	}
	public IntegerType getProfiloRischioMIFID() {
		return profiloRischioMIFID;
	}
	public void setProfiloRischioMIFID(IntegerType profiloRischioMIFID) {
		this.profiloRischioMIFID = profiloRischioMIFID;
	}
	public IntegerType getOrizzonteTempMIFID() {
		return orizzonteTempMIFID;
	}
	public void setOrizzonteTempMIFID(IntegerType orizzonteTempMIFID) {
		this.orizzonteTempMIFID = orizzonteTempMIFID;
	}
	public StringType getBancaDepositaria() {
		return bancaDepositaria;
	}
	public void setBancaDepositaria(StringType bancaDepositaria) {
		this.bancaDepositaria = bancaDepositaria;
	}
	public DateType getDataSottoscrizione() {
		return dataSottoscrizione;
	}
	public void setDataSottoscrizione(DateType dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}
	public DateType getDataChiusura() {
		return dataChiusura;
	}
	public void setDataChiusura(DateType dataChiusura) {
		this.dataChiusura = dataChiusura;
	}
	public StringType getCausaleChiusura() {
		return causaleChiusura;
	}
	public void setCausaleChiusura(StringType causaleChiusura) {
		this.causaleChiusura = causaleChiusura;
	}
	public StringType getIsinFusione() {
		return isinFusione;
	}
	public void setIsinFusione(StringType isinFusione) {
		this.isinFusione = isinFusione;
	}
	public StringType getFlagConsolidaAz() {
		return flagConsolidaAz;
	}
	public void setFlagConsolidaAz(StringType flagConsolidaAz) {
		this.flagConsolidaAz = flagConsolidaAz;
	}
	public StringType getFlagConsolidaOb() {
		return flagConsolidaOb;
	}
	public void setFlagConsolidaOb(StringType flagConsolidaOb) {
		this.flagConsolidaOb = flagConsolidaOb;
	}
	public StringType getFlagPicProgrammato() {
		return flagPicProgrammato;
	}
	public void setFlagPicProgrammato(StringType flagPicProgrammato) {
		this.flagPicProgrammato = flagPicProgrammato;
	}
	public StringType getTipoQuota() {
		return tipoQuota;
	}
	public void setTipoQuota(StringType tipoQuota) {
		this.tipoQuota = tipoQuota;
	}
	public StringType getBloccoOperazioni() {
		return bloccoOperazioni;
	}
	public void setBloccoOperazioni(StringType bloccoOperazioni) {
		this.bloccoOperazioni = bloccoOperazioni;
	}
	public StringType getLinkKIID() {
		return linkKIID;
	}
	public void setLinkKIID(StringType linkKIID) {
		this.linkKIID = linkKIID;
	}
	public DoubleType getImportoMinimo() {
		return importoMinimo;
	}
	public void setImportoMinimo(DoubleType importoMinimo) {
		this.importoMinimo = importoMinimo;
	}
	public BooleanType getSelected() {
		return selected;
	}
	public void setSelected(BooleanType selected) {
		this.selected = selected;
	}
	public StringType getFlagPicProgrInEssere() {
		return flagPicProgrInEssere;
	}
	public void setFlagPicProgrInEssere(StringType flagPicProgrInEssere) {
		this.flagPicProgrInEssere = flagPicProgrInEssere;
	}
	public StringType getNumeroQuote() {
		return numeroQuote;
	}
	public void setNumeroQuote(StringType numeroQuote) {
		this.numeroQuote = numeroQuote;
	}
	public DoubleType getValoreQuota() {
		return valoreQuota;
	}
	public void setValoreQuota(DoubleType valoreQuota) {
		this.valoreQuota = valoreQuota;
	}
	public DoubleType getControvaloreComparto() {
		return controvaloreComparto;
	}
	public void setControvaloreComparto(DoubleType controvaloreComparto) {
		this.controvaloreComparto = controvaloreComparto;
	}	
}
