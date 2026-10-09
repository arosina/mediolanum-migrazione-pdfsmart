package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispVitaInput extends CommandDataModel{

    private StringType codDisposizione			= new StringType();
    private StringType codRete					= new StringType();
    private StringType codAgente				= new StringType();
    private StringType tipoStatoDisposizione	= new StringType();
    private StringType codTipoBeneficiario		= new StringType();
    private StringType descrBeneficiario		= new StringType();    
    private DateType   dataDecorrenza			= new DateType();
    private StringType codTipoBeneficiarioMorte	= new StringType();
    private StringType descrBeneficiarioMorte	= new StringType();
    private StringType serverReplica			= new StringType();
    private StringType flagDivisioneImp			= new StringType();
    private StringType codConvenzione			= new StringType();
    private StringType propostaAbbinata1		= new StringType();
    private StringType propostaAbbinata2		= new StringType();
    private StringType codDispAbb1				= new StringType();
    private DoubleType importo					= new DoubleType();
    private StringType codDivisa				= new StringType();
    private StringType flag1					= new StringType();
    private StringType descr1					= new StringType();    
    private StringType codAgevolazione			= new StringType();
    private StringType fondo1					= new StringType();
    private StringType fondo2					= new StringType();
    private StringType fondo3					= new StringType();
    private StringType fondo4					= new StringType();
    private StringType fondoPartenza1			= new StringType();
    private StringType fondoDestinazione1		= new StringType();
    private StringType fondoDestinazione7		= new StringType();
    private StringType flagBC					= new StringType();    
    private StringType flag2					= new StringType();
    private DoubleType percentuale1				= new DoubleType();
    private DoubleType importoDistr				= new DoubleType();
    private DoubleType importoDistr1			= new DoubleType();
    private StringType descrPianoPens			= new StringType();
    private StringType numContrPens				= new StringType();
    private StringType societaPensione			= new StringType();
	private StringType flagIntenz				= new StringType();
	private DoubleType impIntenz				= new DoubleType();    
	
	private DateType   pensioneDataIstituzione  = new DateType();
	private DateType   pensioneDataIscrizione   = new DateType();
	private StringType anniContribuzione		= new StringType();
	private StringType flagGaranziaCompl		= new StringType();
	private StringType periodicitaTfr			= new StringType();
	private StringType flagTfr					= new StringType();
	private StringType flagContrAzienda			= new StringType();
	private StringType formaPensionistica		= new StringType();
	
	private StringType flagSchedaCosti			= new StringType();
	private IntegerType ratingQuestionario		= new IntegerType();
	private IntegerType alboCovip				= new IntegerType();
	private StringType denomAltroFondo			= new StringType();
	private StringType flagProfiloCoerente		= new StringType();
	private StringType risposteQuestionario		= new StringType();
	private StringType flagManleva				= new StringType();
	
	
	private StringType beneficiarioTerzo = new StringType();
	private StringType noComunicazioneBeneficiario = new StringType();
	
	public StringType getNoComunicazioneBeneficiario() {
		return noComunicazioneBeneficiario;
	}

	public void setNoComunicazioneBeneficiario(StringType noComunicazioneBeneficiario) {
		this.noComunicazioneBeneficiario = noComunicazioneBeneficiario;
	}

	public StringType getBeneficiarioTerzo() {
		return beneficiarioTerzo;
	}

	public void setBeneficiarioTerzo(StringType beneficiarioTerzo) {
		this.beneficiarioTerzo = beneficiarioTerzo;
	}

	/*********************************************************************************/
	/*********************************************************************************/
	public IntegerType getAlboCovip() {
		return alboCovip;
	}

	public void setAlboCovip(IntegerType alboCovip) {
		this.alboCovip = alboCovip;
	}
	
	public StringType getFlagSchedaCosti() {
		return flagSchedaCosti;
	}

	public void setFlagSchedaCosti(StringType flagSchedaCosti) {
		this.flagSchedaCosti = flagSchedaCosti;
	}

	public IntegerType getRatingQuestionario() {
		return ratingQuestionario;
	}

	public void setRatingQuestionario(IntegerType ratingQuestionario) {
		this.ratingQuestionario = ratingQuestionario;
	}

	public StringType getDenomAltroFondo() {
		return denomAltroFondo;
	}

	public void setDenomAltroFondo(StringType denomAltroFondo) {
		this.denomAltroFondo = denomAltroFondo;
	}

	public StringType getFlagProfiloCoerente() {
		return flagProfiloCoerente;
	}

	public void setFlagProfiloCoerente(StringType flagProfiloCoerente) {
		this.flagProfiloCoerente = flagProfiloCoerente;
	}

	public StringType getRisposteQuestionario() {
		return risposteQuestionario;
	}

	public void setRisposteQuestionario(StringType risposteQuestionario) {
		this.risposteQuestionario = risposteQuestionario;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodAgevolazione() {
		return codAgevolazione;
	}

	public StringType getCodConvenzione() {
		return codConvenzione;
	}

	public StringType getCodDispAbb1() {
		return codDispAbb1;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodDivisa() {
		return codDivisa;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodTipoBeneficiario() {
		return codTipoBeneficiario;
	}

	public StringType getCodTipoBeneficiarioMorte() {
		return codTipoBeneficiarioMorte;
	}

	public DateType getDataDecorrenza() {
		return dataDecorrenza;
	}

	public StringType getDescr1() {
		return descr1;
	}

	public StringType getDescrBeneficiario() {
		return descrBeneficiario;
	}

	public StringType getDescrBeneficiarioMorte() {
		return descrBeneficiarioMorte;
	}

	public StringType getDescrPianoPens() {
		return descrPianoPens;
	}

	public StringType getFlag1() {
		return flag1;
	}

	public StringType getFlag2() {
		return flag2;
	}

	public StringType getFlagBC() {
		return flagBC;
	}

	public StringType getFlagDivisioneImp() {
		return flagDivisioneImp;
	}

	public StringType getFondo1() {
		return fondo1;
	}

	public StringType getFondo2() {
		return fondo2;
	}

	public StringType getFondo3() {
		return fondo3;
	}

	public StringType getFondoDestinazione1() {
		return fondoDestinazione1;
	}

	public StringType getFondoDestinazione7() {
		return fondoDestinazione7;
	}

	public StringType getFondoPartenza1() {
		return fondoPartenza1;
	}

	public DoubleType getImporto() {
		return importo;
	}

	public DoubleType getImportoDistr() {
		return importoDistr;
	}

	public DoubleType getImportoDistr1() {
		return importoDistr1;
	}

	public StringType getNumContrPens() {
		return numContrPens;
	}

	public DoubleType getPercentuale1() {
		return percentuale1;
	}

	public StringType getPropostaAbbinata1() {
		return propostaAbbinata1;
	}

	public StringType getPropostaAbbinata2() {
		return propostaAbbinata2;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getSocietaPensione() {
		return societaPensione;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodAgevolazione(StringType codAgevolazione) {
		this.codAgevolazione = codAgevolazione;
	}

	public void setCodConvenzione(StringType codConvenzione) {
		this.codConvenzione = codConvenzione;
	}

	public void setCodDispAbb1(StringType codDispAbb1) {
		this.codDispAbb1 = codDispAbb1;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodDivisa(StringType codDivisa) {
		this.codDivisa = codDivisa;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodTipoBeneficiario(StringType codTipoBeneficiario) {
		this.codTipoBeneficiario = codTipoBeneficiario;
	}

	public void setCodTipoBeneficiarioMorte(StringType codTipoBeneficiarioMorte) {
		this.codTipoBeneficiarioMorte = codTipoBeneficiarioMorte;
	}

	public void setDataDecorrenza(DateType dataDecorrenza) {
		this.dataDecorrenza = dataDecorrenza;
	}

	public void setDescr1(StringType descr1) {
		this.descr1 = descr1;
	}

	public void setDescrBeneficiario(StringType descrBeneficiario) {
		this.descrBeneficiario = descrBeneficiario;
	}

	public void setDescrBeneficiarioMorte(StringType descrBeneficiarioMorte) {
		this.descrBeneficiarioMorte = descrBeneficiarioMorte;
	}

	public void setDescrPianoPens(StringType descrPianoPens) {
		this.descrPianoPens = descrPianoPens;
	}

	public void setFlag1(StringType flag1) {
		this.flag1 = flag1;
	}

	public void setFlag2(StringType flag2) {
		this.flag2 = flag2;
	}

	public void setFlagBC(StringType flagBC) {
		this.flagBC = flagBC;
	}

	public void setFlagDivisioneImp(StringType flagDivisioneImp) {
		this.flagDivisioneImp = flagDivisioneImp;
	}

	public void setFondo1(StringType fondo1) {
		this.fondo1 = fondo1;
	}

	public void setFondo2(StringType fondo2) {
		this.fondo2 = fondo2;
	}

	public void setFondo3(StringType fondo3) {
		this.fondo3 = fondo3;
	}

	public void setFondoDestinazione1(StringType fondoDestinazione1) {
		this.fondoDestinazione1 = fondoDestinazione1;
	}

	public void setFondoDestinazione7(StringType fondoDestinazione7) {
		this.fondoDestinazione7 = fondoDestinazione7;
	}

	public void setFondoPartenza1(StringType fondoPartenza1) {
		this.fondoPartenza1 = fondoPartenza1;
	}

	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}

	public void setImportoDistr(DoubleType importoDistr) {
		this.importoDistr = importoDistr;
	}

	public void setImportoDistr1(DoubleType importoDistr1) {
		this.importoDistr1 = importoDistr1;
	}

	public void setNumContrPens(StringType numContrPens) {
		this.numContrPens = numContrPens;
	}

	public void setPercentuale1(DoubleType percentuale1) {
		this.percentuale1 = percentuale1;
	}

	public void setPropostaAbbinata1(StringType propostaAbbinata1) {
		this.propostaAbbinata1 = propostaAbbinata1;
	}

	public void setPropostaAbbinata2(StringType propostaAbbinata2) {
		this.propostaAbbinata2 = propostaAbbinata2;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setSocietaPensione(StringType societaPensione) {
		this.societaPensione = societaPensione;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public StringType getFlagIntenz() {
		return flagIntenz;
	}

	public DoubleType getImpIntenz() {
		return impIntenz;
	}

	public void setFlagIntenz(StringType flagIntenz) {
		this.flagIntenz = flagIntenz;
	}

	public void setImpIntenz(DoubleType impIntenz) {
		this.impIntenz = impIntenz;
	}

	public StringType getAnniContribuzione() {
		return anniContribuzione;
	}

	public void setAnniContribuzione(StringType anniContribuzione) {
		this.anniContribuzione = anniContribuzione;
	}

	public DateType getPensioneDataIscrizione() {
		return pensioneDataIscrizione;
	}

	public void setPensioneDataIscrizione(DateType pensioneDataIscrizione) {
		this.pensioneDataIscrizione = pensioneDataIscrizione;
	}

	public DateType getPensioneDataIstituzione() {
		return pensioneDataIstituzione;
	}

	public void setPensioneDataIstituzione(DateType pensioneDataIstituzione) {
		this.pensioneDataIstituzione = pensioneDataIstituzione;
	}

	public StringType getFlagContrAzienda() {
		return flagContrAzienda;
	}

	public void setFlagContrAzienda(StringType flagContrAzienda) {
		this.flagContrAzienda = flagContrAzienda;
	}

	public StringType getFlagGaranziaCompl() {
		return flagGaranziaCompl;
	}

	public void setFlagGaranziaCompl(StringType flagGaranziaCompl) {
		this.flagGaranziaCompl = flagGaranziaCompl;
	}

	public StringType getFlagTfr() {
		return flagTfr;
	}

	public void setFlagTfr(StringType flagTfr) {
		this.flagTfr = flagTfr;
	}

	public StringType getPeriodicitaTfr() {
		return periodicitaTfr;
	}

	public void setPeriodicitaTfr(StringType periodicitaTfr) {
		this.periodicitaTfr = periodicitaTfr;
	}

	public StringType getFondo4() {
		return fondo4;
	}

	public void setFondo4(StringType fondo4) {
		this.fondo4 = fondo4;
	}

	public StringType getFormaPensionistica() {
		return formaPensionistica;
	}

	public void setFormaPensionistica(StringType formaPensionistica) {
		this.formaPensionistica = formaPensionistica;
	}
	
	public StringType getFlagManleva() {
		return flagManleva;
	}

	public void setFlagManleva(StringType flagManleva) {
		this.flagManleva = flagManleva;
	}

}