package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.model.AdeguatezzaClienteModel;

public class InputServizioCopernicoModel extends CommandDataModel {

	/**
	 * Dati generali
	 */
	private StringType codDisposizione = new StringType();
	private StringType nomePropostaCopernico = new StringType();
	private StringType dataFineValidita = new StringType();
	private StringType noteCopernico = new StringType();
	private StringType pdfCopernico = new StringType();

	private StringType idUtente = new StringType();
	private StringType idUtentePrimoCosott = new StringType();
	private StringType idUtenteSecondoCosott = new StringType();
	private StringType flagSwitch = new StringType();
	private StringType idContoOrdinante = new StringType();
	private StringType idAgente = new StringType();
	private StringType idContratto = new StringType();
	private StringType flagServizio = new StringType();
	private StringType flagPacPiu = new StringType();
	private StringType codiceUfficio = new StringType();
	

	/*
	 * Dati per iniziale e aggiuntivo
	 */
	private StringType umbrellaFund = new StringType();
	private StringType classe = new StringType();
	private StringType modalita = new StringType();
	private StringType modalitaComunic = new StringType();
	private StringType recapitoVia = new StringType();
	private StringType recapitoNum = new StringType();
	private StringType recapitoLoc = new StringType();
	private StringType recapitoCap = new StringType();
	private StringType recapitoPro = new StringType();
	private StringType recapitoNaz = new StringType();

	private StringType regimeFiscale = new StringType();

	private DoubleType importoRataTotale = new DoubleType();
	private StringType numRate = new StringType();

	/*
	 * FREQUENZARATE Frequenza delle rate (è valorizzato se PAC, altrimenti 0) 1
	 * Mensile 2 Bimestrale 3 Trimestrale 6 Semestrale
	 */
	private IntegerType frequenzaRate = new IntegerType();

	private IntegerType scadenzaRate = new IntegerType();
	private DoubleType loiTotale = new DoubleType();
	private StringType flagIstat = new StringType();
	private StringType tipoDistribuzioneProventi = new StringType();
	private StringType distribuzioneProventi = new StringType();
	private StringType flagDipendente = new StringType();

	private ListType fondiScelti = new ListType(prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.CompartoCopernicoModel.class);

	private DoubleType totaleImportoSrv = new DoubleType();
	private StringType strinfAgg1 = new StringType();
	private StringType strinfAgg2 = new StringType();
	private StringType strinfAgg3 = new StringType();
	
	private StringType idEsitoAdeguatezza = new StringType();
	private StringType adeguatezza = new StringType();
	private StringType descrAdeguatezza = new StringType();
	private StringType manlevaAdeguatezza = new StringType();

	private StringType cliobbInv = new StringType();
	private StringType cliobbTemp = new StringType();
	private StringType clioModVers = new StringType();
	private StringType cliSitFinanz = new StringType();
	private ListType elencoAdeguatezza = new ListType(AdeguatezzaClienteModel.class);
	
	private StringType agevolazione = new StringType();
	private StringType isRadarForzato = new StringType();

	/**
	 * Dati rimborso
	 */
	private StringType nominativoIntestatarioContoOrdinante = new StringType();
	private StringType tipologiaRichiesta = new StringType();
	private StringType descRichiesta = new StringType();
	private StringType contrattoProdDisinv = new StringType();
	private StringType tipoDisiv = new StringType();
	private ListType elencoFondiDisinv = new ListType(prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.CompartoCopernicoModel.class);
	private StringType codServizio = new StringType();
	private StringType flagRevocaAddebitoRid = new StringType();
	private StringType flagRevocaReinvProgr = new StringType();
	private StringType intestatarioBonifico = new StringType();
	private StringType ibanBonifico = new StringType();
	private StringType cabBonifico = new StringType();
	private StringType abiBonifico = new StringType();
	private StringType bancaBonifico = new StringType();
	private StringType filialeBonifico = new StringType();
	private StringType tipoRimborsoProgr = new StringType();
	private StringType codiceTipoFrequenza = new StringType();
	private IntegerType quantitaRateRimborsoProgrammato = new IntegerType();
	private StringType codProdottoFondo = new StringType();
	private StringType dataInizioDecorrenza = new StringType();
	private StringType dataFineDecorrenza = new StringType();
	private StringType giornoOperazione = new StringType();
	private StringType tipoDisp = new StringType();
	private IntegerType totaleQuoteSrv = new IntegerType();	
	private StringType intestatarioAssegno = new StringType();
	private StringType indirizzoResidenzaAssegno = new StringType();
	private StringType civicoResidenzaAssegno = new StringType();
	private StringType luogoResidenzaAssegno = new StringType();
	private StringType provinciaResidenzaAssegno = new StringType();
	private StringType capResidenzaAssegno = new StringType();
	
	

	
	/*
	 * Dati Mifid II
	 */
	private StringType prgSuitability = new StringType();
	private StringType chiaveEK = new StringType();
	private StringType idSuitability = new StringType();
	
	/*
	 * Dati Conversione
	 */
	private ListType elencoFondiDisInvConversioni = new ListType(prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.CompartoCopernicoConvertitoModel.class);
	private ListType elencoFondiInv = new ListType(prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.CompartoCopernicoModel.class);
	private ListType elencoFondiDis = new ListType(prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.CompartoCopernicoModel.class);

	

	// INTEGRAZIONE SWITCH
	private StringType codiceSwitch = new StringType();
	private StringType idMandato = new StringType();
	private StringType tipoReinvestimento = new StringType();
	private StringType flagPrimaSott = new StringType();
	
	
	/*
	 * Dati output
	 */
	
	private StringType esitoChiamata = new StringType();
	private StringType messaggioOutputChiamata = new StringType();

	


	public StringType getCodDisposizione() {
		return codDisposizione;
	}


	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}


	public StringType getNomePropostaCopernico() {
		return nomePropostaCopernico;
	}


	public void setNomePropostaCopernico(StringType nomePropostaCopernico) {
		this.nomePropostaCopernico = nomePropostaCopernico;
	}


	public StringType getDataFineValidita() {
		return dataFineValidita;
	}


	public void setDataFineValidita(StringType dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}


	public StringType getNoteCopernico() {
		return noteCopernico;
	}


	public void setNoteCopernico(StringType noteCopernico) {
		this.noteCopernico = noteCopernico;
	}


	public StringType getPdfCopernico() {
		return pdfCopernico;
	}


	public void setPdfCopernico(StringType pdfCopernico) {
		this.pdfCopernico = pdfCopernico;
	}


	public StringType getIdUtente() {
		return idUtente;
	}


	public void setIdUtente(StringType idUtente) {
		this.idUtente = idUtente;
	}


	public StringType getIdUtentePrimoCosott() {
		return idUtentePrimoCosott;
	}


	public void setIdUtentePrimoCosott(StringType idUtentePrimoCosott) {
		this.idUtentePrimoCosott = idUtentePrimoCosott;
	}


	public StringType getIdUtenteSecondoCosott() {
		return idUtenteSecondoCosott;
	}


	public void setIdUtenteSecondoCosott(StringType idUtenteSecondoCosott) {
		this.idUtenteSecondoCosott = idUtenteSecondoCosott;
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


	public StringType getFlagServizio() {
		return flagServizio;
	}


	public void setFlagServizio(StringType flagServizio) {
		this.flagServizio = flagServizio;
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


	public StringType getRecapitoLoc() {
		return recapitoLoc;
	}


	public void setRecapitoLoc(StringType recapitoLoc) {
		this.recapitoLoc = recapitoLoc;
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


	public DoubleType getTotaleImportoSrv() {
		return totaleImportoSrv;
	}


	public void setTotaleImportoSrv(DoubleType totaleImportoSrv) {
		this.totaleImportoSrv = totaleImportoSrv;
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


	public StringType getIdEsitoAdeguatezza() {
		return idEsitoAdeguatezza;
	}


	public void setIdEsitoAdeguatezza(StringType idEsitoAdeguatezza) {
		this.idEsitoAdeguatezza = idEsitoAdeguatezza;
	}


	public StringType getAdeguatezza() {
		return adeguatezza;
	}


	public void setAdeguatezza(StringType adeguatezza) {
		this.adeguatezza = adeguatezza;
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


	public ListType getElencoAdeguatezza() {
		return elencoAdeguatezza;
	}


	public void setElencoAdeguatezza(ListType elencoAdeguatezza) {
		this.elencoAdeguatezza = elencoAdeguatezza;
	}


	public StringType getAgevolazione() {
		return agevolazione;
	}


	public void setAgevolazione(StringType agevolazione) {
		this.agevolazione = agevolazione;
	}


	public StringType getIsRadarForzato() {
		return isRadarForzato;
	}


	public void setIsRadarForzato(StringType isRadarForzato) {
		this.isRadarForzato = isRadarForzato;
	}


	public StringType getNominativoIntestatarioContoOrdinante() {
		return nominativoIntestatarioContoOrdinante;
	}


	public void setNominativoIntestatarioContoOrdinante(StringType nominativoIntestatarioContoOrdinante) {
		this.nominativoIntestatarioContoOrdinante = nominativoIntestatarioContoOrdinante;
	}


	public StringType getTipologiaRichiesta() {
		return tipologiaRichiesta;
	}


	public void setTipologiaRichiesta(StringType tipologiaRichiesta) {
		this.tipologiaRichiesta = tipologiaRichiesta;
	}


	public StringType getDescRichiesta() {
		return descRichiesta;
	}


	public void setDescRichiesta(StringType descRichiesta) {
		this.descRichiesta = descRichiesta;
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


	public ListType getElencoFondiDisinv() {
		return elencoFondiDisinv;
	}


	public void setElencoFondiDisinv(ListType elencoFondiDisinv) {
		this.elencoFondiDisinv = elencoFondiDisinv;
	}


	public StringType getCodServizio() {
		return codServizio;
	}


	public void setCodServizio(StringType codServizio) {
		this.codServizio = codServizio;
	}


	public StringType getFlagRevocaAddebitoRid() {
		return flagRevocaAddebitoRid;
	}


	public void setFlagRevocaAddebitoRid(StringType flagRevocaAddebitoRid) {
		this.flagRevocaAddebitoRid = flagRevocaAddebitoRid;
	}


	public StringType getFlagRevocaReinvProgr() {
		return flagRevocaReinvProgr;
	}


	public void setFlagRevocaReinvProgr(StringType flagRevocaReinvProgr) {
		this.flagRevocaReinvProgr = flagRevocaReinvProgr;
	}


	public StringType getIntestatarioBonifico() {
		return intestatarioBonifico;
	}


	public void setIntestatarioBonifico(StringType intestatarioBonifico) {
		this.intestatarioBonifico = intestatarioBonifico;
	}


	public StringType getIbanBonifico() {
		return ibanBonifico;
	}


	public void setIbanBonifico(StringType ibanBonifico) {
		this.ibanBonifico = ibanBonifico;
	}


	public StringType getCabBonifico() {
		return cabBonifico;
	}


	public void setCabBonifico(StringType cabBonifico) {
		this.cabBonifico = cabBonifico;
	}


	public StringType getAbiBonifico() {
		return abiBonifico;
	}


	public void setAbiBonifico(StringType abiBonifico) {
		this.abiBonifico = abiBonifico;
	}


	public StringType getBancaBonifico() {
		return bancaBonifico;
	}


	public void setBancaBonifico(StringType bancaBonifico) {
		this.bancaBonifico = bancaBonifico;
	}


	public StringType getFilialeBonifico() {
		return filialeBonifico;
	}


	public void setFilialeBonifico(StringType filialeBonifico) {
		this.filialeBonifico = filialeBonifico;
	}


	public StringType getTipoRimborsoProgr() {
		return tipoRimborsoProgr;
	}


	public void setTipoRimborsoProgr(StringType tipoRimborsoProgr) {
		this.tipoRimborsoProgr = tipoRimborsoProgr;
	}


	public StringType getCodiceTipoFrequenza() {
		return codiceTipoFrequenza;
	}


	public void setCodiceTipoFrequenza(StringType codiceTipoFrequenza) {
		this.codiceTipoFrequenza = codiceTipoFrequenza;
	}


	public IntegerType getQuantitaRateRimborsoProgrammato() {
		return quantitaRateRimborsoProgrammato;
	}


	public void setQuantitaRateRimborsoProgrammato(IntegerType quantitaRateRimborsoProgrammato) {
		this.quantitaRateRimborsoProgrammato = quantitaRateRimborsoProgrammato;
	}


	public StringType getCodProdottoFondo() {
		return codProdottoFondo;
	}


	public void setCodProdottoFondo(StringType codProdottoFondo) {
		this.codProdottoFondo = codProdottoFondo;
	}


	public StringType getDataInizioDecorrenza() {
		return dataInizioDecorrenza;
	}


	public void setDataInizioDecorrenza(StringType dataInizioDecorrenza) {
		this.dataInizioDecorrenza = dataInizioDecorrenza;
	}


	public StringType getDataFineDecorrenza() {
		return dataFineDecorrenza;
	}


	public void setDataFineDecorrenza(StringType dataFineDecorrenza) {
		this.dataFineDecorrenza = dataFineDecorrenza;
	}


	public StringType getGiornoOperazione() {
		return giornoOperazione;
	}


	public void setGiornoOperazione(StringType giornoOperazione) {
		this.giornoOperazione = giornoOperazione;
	}


	public StringType getTipoDisp() {
		return tipoDisp;
	}


	public void setTipoDisp(StringType tipoDisp) {
		this.tipoDisp = tipoDisp;
	}


	public IntegerType getTotaleQuoteSrv() {
		return totaleQuoteSrv;
	}


	public void setTotaleQuoteSrv(IntegerType totaleQuoteSrv) {
		this.totaleQuoteSrv = totaleQuoteSrv;
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


	public StringType getCodiceSwitch() {
		return codiceSwitch;
	}


	public void setCodiceSwitch(StringType codiceSwitch) {
		this.codiceSwitch = codiceSwitch;
	}

	public StringType getIdMandato() {
		return idMandato;
	}


	public void setIdMandato(StringType idMandato) {
		this.idMandato = idMandato;
	}


	public StringType getTipoReinvestimento() {
		return tipoReinvestimento;
	}


	public void setTipoReinvestimento(StringType tipoReinvestimento) {
		this.tipoReinvestimento = tipoReinvestimento;
	}


	public StringType getFlagPrimaSott() {
		return flagPrimaSott;
	}


	public void setFlagPrimaSott(StringType flagPrimaSott) {
		this.flagPrimaSott = flagPrimaSott;
	}


	public StringType getEsitoChiamata() {
		return esitoChiamata;
	}


	public void setEsitoChiamata(StringType esitoChiamata) {
		this.esitoChiamata = esitoChiamata;
	}


	public StringType getMessaggioOutputChiamata() {
		return messaggioOutputChiamata;
	}


	public void setMessaggioOutputChiamata(StringType messaggioOutputChiamata) {
		this.messaggioOutputChiamata = messaggioOutputChiamata;
	}


	public StringType getCodiceUfficio() {
		return codiceUfficio;
	}


	public void setCodiceUfficio(StringType codiceUfficio) {
		this.codiceUfficio = codiceUfficio;
	}


	public ListType getElencoFondiDisInvConversioni() {
		return elencoFondiDisInvConversioni;
	}


	public void setElencoFondiDisInvConversioni(ListType elencoFondiDisInvConversioni) {
		this.elencoFondiDisInvConversioni = elencoFondiDisInvConversioni;
	}


	public ListType getElencoFondiInv() {
		return elencoFondiInv;
	}


	public void setElencoFondiInv(ListType elencoFondiInv) {
		this.elencoFondiInv = elencoFondiInv;
	}


	public ListType getElencoFondiDis() {
		return elencoFondiDis;
	}


	public void setElencoFondiDis(ListType elencoFondiDis) {
		this.elencoFondiDis = elencoFondiDis;
	}


	public StringType getIntestatarioAssegno() {
		return intestatarioAssegno;
	}


	public void setIntestatarioAssegno(StringType intestatarioAssegno) {
		this.intestatarioAssegno = intestatarioAssegno;
	}


	public StringType getIndirizzoResidenzaAssegno() {
		return indirizzoResidenzaAssegno;
	}


	public void setIndirizzoResidenzaAssegno(StringType indirizzoResidenzaAssegno) {
		this.indirizzoResidenzaAssegno = indirizzoResidenzaAssegno;
	}


	public StringType getCivicoResidenzaAssegno() {
		return civicoResidenzaAssegno;
	}


	public void setCivicoResidenzaAssegno(StringType civicoResidenzaAssegno) {
		this.civicoResidenzaAssegno = civicoResidenzaAssegno;
	}


	public StringType getLuogoResidenzaAssegno() {
		return luogoResidenzaAssegno;
	}


	public void setLuogoResidenzaAssegno(StringType luogoResidenzaAssegno) {
		this.luogoResidenzaAssegno = luogoResidenzaAssegno;
	}


	public StringType getProvinciaResidenzaAssegno() {
		return provinciaResidenzaAssegno;
	}


	public void setProvinciaResidenzaAssegno(StringType provinciaResidenzaAssegno) {
		this.provinciaResidenzaAssegno = provinciaResidenzaAssegno;
	}


	public StringType getCapResidenzaAssegno() {
		return capResidenzaAssegno;
	}


	public void setCapResidenzaAssegno(StringType capResidenzaAssegno) {
		this.capResidenzaAssegno = capResidenzaAssegno;
	}


	public StringType getFlagPacPiu() {
		return flagPacPiu;
	}


	public void setFlagPacPiu(StringType flagPacPiu) {
		this.flagPacPiu = flagPacPiu;
	}



	

	


}
