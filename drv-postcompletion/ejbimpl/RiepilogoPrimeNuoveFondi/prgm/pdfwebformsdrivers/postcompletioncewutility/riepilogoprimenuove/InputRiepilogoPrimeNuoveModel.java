package prgm.pdfwebformsdrivers.postcompletioncewutility.riepilogoprimenuove;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.model.AdeguatezzaClienteModel;

/****************************************************************/
/****************************************************************/
public class InputRiepilogoPrimeNuoveModel extends CommandDataModel {
	
	
	private StringType 	idUtente 			= new StringType();
	private StringType 	idUtentePrimoCosott = new StringType();
	private StringType 	idUtenteSecondoCosott = new StringType();
	private StringType 	flagPrimaSott		= new StringType();
	private StringType 	flagSwitch 			= new StringType();
	private StringType 	idContoOrdinante 	= new StringType();
	private StringType 	idAgente 			= new StringType();
	private StringType 	idContratto 		= new StringType();
	private StringType 	idMandato 			= new StringType();
	
	private StringType 	umbrellaFund 		= new StringType();
	private StringType 	classe 				= new StringType();
	private StringType 	modalita 			= new StringType();
	private StringType 	modalitaComunic 	= new StringType();
	private StringType 	recapitoVia 		= new StringType();
	private StringType 	recapitoNum 		= new StringType();
	private StringType 	recapitoLoc 		= new StringType();
	private StringType 	recapitoCap 		= new StringType();
	private StringType 	recapitoPro 		= new StringType();
	private StringType 	recapitoNaz 		= new StringType();
	
	private StringType 	regimeFiscale 		= new StringType();
	private StringType 	flagServizio		= new StringType();	
	
	private DoubleType 	importoRataTotale 	= new DoubleType();
	private StringType 	numRate 			= new StringType();
	
	/* FREQUENZARATE	Frequenza delle rate (è valorizzato se PAC, altrimenti 0)
	1 Mensile 	2 Bimestrale	3 Trimestrale	6 Semestrale  	*/
	private IntegerType frequenzaRate 		= new IntegerType();

	// SCADENZARATE	Scadenza delle Rate (è valorizzato se PAC con 8 o 28, altrimenti 0)
	private IntegerType scadenzaRate 		= new IntegerType();
	private DoubleType 	loiTotale 			= new DoubleType();
	private StringType 	flagIstat 			= new StringType();
	private StringType 	tipoDistribuzioneProventi 	= new StringType();
	private StringType 	distribuzioneProventi 		= new StringType();
	private StringType 	flagDipendente 		= new StringType();
	
	private ListType	fondiScelti		= new ListType(CompartoModel.class); 
	
	private StringType 	contrattoProdDisinv	= new StringType();
	private StringType 	tipoDisiv	 		= new StringType();
	
	private ListType	elencoFondiDisinv	= new ListType(CompartoModel.class);
	private ListType	elencoFondiInv	= new ListType(CompartoModel.class);
	private ListType 	elencoFondiDisinvInv = new ListType(CompartoModel.class);
	
	private DoubleType 	totaleImportoSrv	= new DoubleType();
	
	private StringType	tipologiaRichiesta	= new StringType();
	private StringType	descRichiesta		= new StringType();
	
	private StringType	stato				= new StringType();
	private StringType	idEsitoAdeguatezza	= new StringType();
	private StringType	adeguatezza			= new StringType();
	private StringType	descrAdeguatezza	= new StringType();
	private StringType	manlevaAdeguatezza	= new StringType();
	
	private StringType	cliobbInv			= new StringType();
	private StringType	cliobbTemp			= new StringType();
	private StringType	cliSitFinanz		= new StringType();
	private StringType	clioModVers			= new StringType();
	
	// Valorizzato solo in presenza di agevolazione commissionale.
	private StringType	agevolazione			= new StringType();
		
	
	// PER TICKET 1007008 - Gestione tag StrinfAgg
	private StringType strinfAgg1 = new StringType();
	private StringType strinfAgg2 = new StringType();
	private StringType strinfAgg3 = new StringType();

	private StringType opzioni 						= new StringType();
	
	private ListType	elencoAdeguatezza	= new ListType(AdeguatezzaClienteModel.class); 
	
	private OutputRiepilogoPrimeNuoveModel	output				= new OutputRiepilogoPrimeNuoveModel();
	
	//INIZIO ATTRIBUTI PER SERVIZIO RiepilogoConferma chiamato per i RIMBORSI
	private IntegerType totaleQuoteSrv      = new IntegerType();
	private StringType  tipoDisp            = new StringType();
	private StringType  codiceUfficio       = new StringType();
	private StringType 	descUtente 			= new StringType();
	private StringType 	ibanBonifico        = new StringType();
	private StringType 	abiBonifico         = new StringType();
	private StringType 	cabBonifico         = new StringType();
	private StringType 	flagRevocaReinvProgr = new StringType();
	private StringType 	flagRevocaAddebitoRid= new StringType();
	private StringType  codServizio         = new StringType();
	//FINE ATTRIBUTI PER PER SERVIZIO RiepilogoConferma chiamato per i RIMBORSI
	
	
	
	private StringType isRadarForzato	 	= new StringType();
	private StringType codDisposizione		= new StringType();
	private StringType flagPACPiu 			= new StringType();
	
	private StringType nominativoIntestatarioContoOrdinante 			= new StringType();
	
	/*
	 * Dati Mifid II
	 */
	private StringType prgSuitability = new StringType();
	private StringType chiaveEK = new StringType();
	private StringType idSuitability = new StringType();
	
	// INTEGRAZIONE SWITCH
	private StringType codiceSwitch = new StringType();
	private StringType codiceProdInv = new StringType();
	
	private StringType idVendente = new StringType();
	
	/****************************************************************/
	/****************************************************************/

	
	public StringType getCodiceSwitch() {
		return codiceSwitch;
	}
	public StringType getCodiceProdInv() {
		return codiceProdInv;
	}
	public void setCodiceProdInv(StringType codiceProdInv) {
		this.codiceProdInv = codiceProdInv;
	}
	public void setCodiceSwitch(StringType codiceSwitch) {
		this.codiceSwitch = codiceSwitch;
	}
	public StringType getIdUtente() {
		return idUtente;
	}
	public void setIdUtente(StringType idUtente) {
		this.idUtente = idUtente;
	}
	public StringType getFlagSwitch() {
		return flagSwitch;
	}
	public void setFlagSwitch(StringType flagSwitch) {
		this.flagSwitch = flagSwitch;
	}
	public StringType getIdContoOrdinante() {
		return idContoOrdinante;
	}
	public void setIdContoOrdinante(StringType idContoOrdinante) {
		this.idContoOrdinante = idContoOrdinante;
	}
	public StringType getIdAgente() {
		return idAgente;
	}
	public void setIdAgente(StringType idAgente) {
		this.idAgente = idAgente;
	}
	public StringType getIdContratto() {
		return idContratto;
	}
	public void setIdContratto(StringType idContratto) {
		this.idContratto = idContratto;
	}
	public StringType getUmbrellaFund() {
		return umbrellaFund;
	}
	public void setUmbrellaFund(StringType umbrellaFund) {
		this.umbrellaFund = umbrellaFund;
	}
	public StringType getClasse() {
		return classe;
	}
	public void setClasse(StringType classe) {
		this.classe = classe;
	}
	public StringType getModalita() {
		return modalita;
	}
	public void setModalita(StringType modalita) {
		this.modalita = modalita;
	}
	public StringType getModalitaComunic() {
		return modalitaComunic;
	}
	public void setModalitaComunic(StringType modalitaComunic) {
		this.modalitaComunic = modalitaComunic;
	}
	public StringType getRecapitoVia() {
		return recapitoVia;
	}
	public void setRecapitoVia(StringType recapitoVia) {
		this.recapitoVia = recapitoVia;
	}
	public StringType getRecapitoNum() {
		return recapitoNum;
	}
	public void setRecapitoNum(StringType recapitoNum) {
		this.recapitoNum = recapitoNum;
	}
	public StringType getRecapitoCap() {
		return recapitoCap;
	}
	public void setRecapitoCap(StringType recapitoCap) {
		this.recapitoCap = recapitoCap;
	}
	public StringType getRecapitoPro() {
		return recapitoPro;
	}
	public void setRecapitoPro(StringType recapitoPro) {
		this.recapitoPro = recapitoPro;
	}
	public StringType getRecapitoNaz() {
		return recapitoNaz;
	}
	public void setRecapitoNaz(StringType recapitoNaz) {
		this.recapitoNaz = recapitoNaz;
	}
	public StringType getRegimeFiscale() {
		return regimeFiscale;
	}
	public void setRegimeFiscale(StringType regimeFiscale) {
		this.regimeFiscale = regimeFiscale;
	}
	public DoubleType getImportoRataTotale() {
		return importoRataTotale;
	}
	public void setImportoRataTotale(DoubleType importoRataTotale) {
		this.importoRataTotale = importoRataTotale;
	}
	public StringType getNumRate() {
		return numRate;
	}
	public void setNumRate(StringType numRate) {
		this.numRate = numRate;
	}
	public IntegerType getFrequenzaRate() {
		return frequenzaRate;
	}
	public void setFrequenzaRate(IntegerType frequenzaRate) {
		this.frequenzaRate = frequenzaRate;
	}
	public IntegerType getScadenzaRate() {
		return scadenzaRate;
	}
	public void setScadenzaRate(IntegerType scadenzaRate) {
		this.scadenzaRate = scadenzaRate;
	}
	public DoubleType getLoiTotale() {
		return loiTotale;
	}
	public void setLoiTotale(DoubleType loiTotale) {
		this.loiTotale = loiTotale;
	}
	public StringType getFlagIstat() {
		return flagIstat;
	}
	public void setFlagIstat(StringType flagIstat) {
		this.flagIstat = flagIstat;
	}
	public StringType getTipoDistribuzioneProventi() {
		return tipoDistribuzioneProventi;
	}
	public void setTipoDistribuzioneProventi(StringType tipoDistribuzioneProventi) {
		this.tipoDistribuzioneProventi = tipoDistribuzioneProventi;
	}
	public StringType getDistribuzioneProventi() {
		return distribuzioneProventi;
	}
	public void setDistribuzioneProventi(StringType distribuzioneProventi) {
		this.distribuzioneProventi = distribuzioneProventi;
	}
	public StringType getFlagDipendente() {
		return flagDipendente;
	}
	public void setFlagDipendente(StringType flagDipendente) {
		this.flagDipendente = flagDipendente;
	}
	public ListType getFondiScelti() {
		return fondiScelti;
	}
	public void setFondiScelti(ListType fondiScelti) {
		this.fondiScelti = fondiScelti;
	}
	public StringType getContrattoProdDisinv() {
		return contrattoProdDisinv;
	}
	public void setContrattoProdDisinv(StringType contrattoProdDisinv) {
		this.contrattoProdDisinv = contrattoProdDisinv;
	}
	public StringType getTipoDisiv() {
		return tipoDisiv;
	}
	public void setTipoDisiv(StringType tipoDisiv) {
		this.tipoDisiv = tipoDisiv;
	}
	public void setIdUtentePrimoCosott(StringType idUtentePrimoCosott) {
		this.idUtentePrimoCosott = idUtentePrimoCosott;
	}
	public StringType getIdUtentePrimoCosott() {
		return idUtentePrimoCosott;
	}
	public void setIdUtenteSecondoCosott(StringType idUtenteSecondoCosott) {
		this.idUtenteSecondoCosott = idUtenteSecondoCosott;
	}
	public StringType getIdUtenteSecondoCosott() {
		return idUtenteSecondoCosott;
	}
	public void setRecapitoLoc(StringType recapitoLoc) {
		this.recapitoLoc = recapitoLoc;
	}
	public StringType getRecapitoLoc() {
		return recapitoLoc;
	}
	public void setElencoFondiDisinv(ListType elencoFondiDisinv) {
		this.elencoFondiDisinv = elencoFondiDisinv;
	}
	public ListType getElencoFondiDisinv() {
		return elencoFondiDisinv;
	}
	public void setElencoFondiInv(ListType elencoFondiInv) {
		this.elencoFondiInv = elencoFondiInv;
	}
	public ListType getElencoFondiInv() {
		return elencoFondiInv;
	}
	public DoubleType getTotaleImportoSrv() {
		return totaleImportoSrv;
	}
	public void setTotaleImportoSrv(DoubleType totaleImportoSrv) {
		this.totaleImportoSrv = totaleImportoSrv;
	}
	public void setTipologiaRichiesta(StringType tipologiaRichiesta) {
		this.tipologiaRichiesta = tipologiaRichiesta;
	}
	public StringType getTipologiaRichiesta() {
		return tipologiaRichiesta;
	}
	public void setDescRichiesta(StringType descRichiesta) {
		this.descRichiesta = descRichiesta;
	}
	public StringType getDescRichiesta() {
		return descRichiesta;
	}
	public StringType getIdEsitoAdeguatezza() {
		return idEsitoAdeguatezza;
	}
	public void setIdEsitoAdeguatezza(StringType idEsitoAdeguatezza) {
		this.idEsitoAdeguatezza = idEsitoAdeguatezza;
	}
	public StringType getDescrAdeguatezza() {
		return descrAdeguatezza;
	}
	public void setDescrAdeguatezza(StringType descrAdeguatezza) {
		this.descrAdeguatezza = descrAdeguatezza;
	}
	public StringType getManlevaAdeguatezza() {
		return manlevaAdeguatezza;
	}
	public void setManlevaAdeguatezza(StringType manlevaAdeguatezza) {
		this.manlevaAdeguatezza = manlevaAdeguatezza;
	}
	public StringType getCliobbInv() {
		return cliobbInv;
	}
	public void setCliobbInv(StringType cliobbInv) {
		this.cliobbInv = cliobbInv;
	}
	public StringType getCliobbTemp() {
		return cliobbTemp;
	}
	public void setCliobbTemp(StringType cliobbTemp) {
		this.cliobbTemp = cliobbTemp;
	}
	public StringType getClioModVers() {
		return clioModVers;
	}
	public void setClioModVers(StringType clioModVers) {
		this.clioModVers = clioModVers;
	}
	public StringType getCliSitFinanz() {
		return cliSitFinanz;
	}
	public void setCliSitFinanz(StringType cliSitFinanz) {
		this.cliSitFinanz = cliSitFinanz;
	}
	public void setAdeguatezza(StringType adeguatezza) {
		this.adeguatezza = adeguatezza;
	}
	public StringType getAdeguatezza() {
		return adeguatezza;
	}
	public void setStato(StringType stato) {
		this.stato = stato;
	}
	public StringType getStato() {
		return stato;
	}
	public ListType getElencoAdeguatezza() {
		return elencoAdeguatezza;
	}
	public void setElencoAdeguatezza(ListType elencoAdeguatezza) {
		this.elencoAdeguatezza = elencoAdeguatezza;
	}
	public OutputRiepilogoPrimeNuoveModel getOutput() {
		return output;
	}
	public void setOutput(OutputRiepilogoPrimeNuoveModel output) {
		this.output = output;
	}
	public StringType getStrinfAgg1() {
		return strinfAgg1;
	}
	public void setStrinfAgg1(StringType strinfAgg1) {
		this.strinfAgg1 = strinfAgg1;
	}
	public StringType getStrinfAgg2() {
		return strinfAgg2;
	}
	public void setStrinfAgg2(StringType strinfAgg2) {
		this.strinfAgg2 = strinfAgg2;
	}
	public StringType getStrinfAgg3() {
		return strinfAgg3;
	}
	public void setStrinfAgg3(StringType strinfAgg3) {
		this.strinfAgg3 = strinfAgg3;
	}
	public StringType getFlagServizio() {
		return flagServizio;
	}
	public void setFlagServizio(StringType flagServizio) {
		this.flagServizio = flagServizio;
	}
	public StringType getOpzioni() {
		return opzioni;
	}
	public void setOpzioni(StringType opzioni) {
		this.opzioni = opzioni;
	}
	public StringType getIdMandato() {
		return idMandato;
	}
	public void setIdMandato(StringType idMandato) {
		this.idMandato = idMandato;
	}
	public StringType getFlagPrimaSott() {
		return flagPrimaSott;
	}
	public void setFlagPrimaSott(StringType flagPrimaSott) {
		this.flagPrimaSott = flagPrimaSott;
	}
	public void setAgevolazione(StringType agevolazione) {
		this.agevolazione = agevolazione;
	}
	public StringType getAgevolazione() {
		return agevolazione;
	}
	public IntegerType getTotaleQuoteSrv() {
		return totaleQuoteSrv;
	}
	public void setTotaleQuoteSrv(IntegerType totaleQuoteSrv) {
		this.totaleQuoteSrv = totaleQuoteSrv;
	}
	public StringType getTipoDisp() {
		return tipoDisp;
	}
	public void setTipoDisp(StringType tipoDisp) {
		this.tipoDisp = tipoDisp;
	}
	public StringType getCodiceUfficio() {
		return codiceUfficio;
	}
	public void setCodiceUfficio(StringType codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}
	public StringType getDescUtente() {
		return descUtente;
	}
	public void setDescUtente(StringType descUtente) {
		this.descUtente = descUtente;
	}
	public StringType getIbanBonifico() {
		return ibanBonifico;
	}
	public void setIbanBonifico(StringType ibanBonifico) {
		this.ibanBonifico = ibanBonifico;
	}
	public StringType getAbiBonifico() {
		return abiBonifico;
	}
	public void setAbiBonifico(StringType abiBonifico) {
		this.abiBonifico = abiBonifico;
	}
	public StringType getCabBonifico() {
		return cabBonifico;
	}
	public void setCabBonifico(StringType cabBonifico) {
		this.cabBonifico = cabBonifico;
	}
	public StringType getFlagRevocaReinvProgr() {
		return flagRevocaReinvProgr;
	}
	public void setFlagRevocaReinvProgr(StringType flagRevocaReinvProgr) {
		this.flagRevocaReinvProgr = flagRevocaReinvProgr;
	}
	public StringType getFlagRevocaAddebitoRid() {
		return flagRevocaAddebitoRid;
	}
	public void setFlagRevocaAddebitoRid(StringType flagRevocaAddebitoRid) {
		this.flagRevocaAddebitoRid = flagRevocaAddebitoRid;
	}
	public void setIsRadarForzato(StringType isRadarForzato) {
		this.isRadarForzato = isRadarForzato;
	}
	public StringType getIsRadarForzato() {
		return isRadarForzato;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public StringType getCodServizio() {
		return codServizio;
	}
	public void setCodServizio(StringType codServizio) {
		this.codServizio = codServizio;
	}
	public StringType getFlagPACPiu() {
		return flagPACPiu;
	}
	public void setFlagPACPiu(StringType flagPACPiu) {
		this.flagPACPiu = flagPACPiu;
	}
	public ListType getElencoFondiDisinvInv() {
		return elencoFondiDisinvInv;
	}
	public void setElencoFondiDisinvInv(ListType elencoFondiDisinvInv) {
		this.elencoFondiDisinvInv = elencoFondiDisinvInv;
	}
	public StringType getNominativoIntestatarioContoOrdinante() {
		return nominativoIntestatarioContoOrdinante;
	}
	public void setNominativoIntestatarioContoOrdinante(StringType nominativoIntestatarioContoOrdinante) {
		this.nominativoIntestatarioContoOrdinante = nominativoIntestatarioContoOrdinante;
	}
	public StringType getPrgSuitability() {
		return prgSuitability;
	}
	public void setPrgSuitability(StringType prgSuitability) {
		this.prgSuitability = prgSuitability;
	}
	public StringType getChiaveEK() {
		return chiaveEK;
	}
	public void setChiaveEK(StringType chiaveEK) {
		this.chiaveEK = chiaveEK;
	}
	public StringType getIdSuitability() {
		return idSuitability;
	}
	public void setIdSuitability(StringType idSuitability) {
		this.idSuitability = idSuitability;
	}
	public StringType getIdVendente() {
		return idVendente;
	}
	public void setIdVendente(StringType idVendente) {
		this.idVendente = idVendente;
	}
	
}
