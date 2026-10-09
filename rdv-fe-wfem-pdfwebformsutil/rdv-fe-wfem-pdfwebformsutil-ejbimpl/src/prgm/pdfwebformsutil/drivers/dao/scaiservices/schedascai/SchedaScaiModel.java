package prgm.pdfwebformsutil.drivers.dao.scaiservices.schedascai;

import java.util.Calendar;
import java.util.Date;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class SchedaScaiModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final static String STATO_COMPLETO = "1";
	public final static String STATO_INCOMPLETO = "2";
	public final static String STATO_IN_SCADENZA = "3";
	public final static String STATO_SCADUTO = "4";
	public final static String STATO_ESTINTO = "5";
	public final static String STATO_BLOCCATO = "6";
	
	private IntegerType resultCode = new IntegerType();
	private StringType codiceCliente = new StringType();
	private StringType nomeCliente = new StringType();
	private StringType cognomeCliente = new StringType();
	private StringType codiceAgente = new StringType();
	private DateType dataCopiaDocumento = new DateType();
	private DateType dataPrimoContattoUtile = new DateType();
	private DateType dataProfiloRischio = new DateType();
	private DateType dataScadenzaDocumento = new DateType();
	private DateType dataVariazioneAnagrafica = new DateType();
	private StringType flagControlloCliAttivoAntiriciclaggio = new StringType();
	private StringType flagControlloCopiaDocumento = new StringType();
	private StringType flagControlloDataEmissioneDoc = new StringType();
	private StringType flagControlloLuogoEmissioneDoc = new StringType();
	private StringType flagControlloLuogoSvolgimentoProf = new StringType();
	private StringType flagControlloNumeroDocumento = new StringType();
	private StringType flagControlloProfessione = new StringType();
	private StringType flagControlloTAE = new StringType();
	private StringType flagControlloTipoDocumento = new StringType();
	private StringType flgControlloVariazioneOnline = new StringType();
	private DoubleType qtaMesiVariazioneProfiloRischio = new DoubleType();
	private StringType codiceFascioProfiloRischio = new StringType();
	private StringType descrizioneFascioProfiloRischio = new StringType();
	private DateType dataInizioValiditaFascioProfiloRischio = new DateType();
	private StringType flagControlloAnagrafica = new StringType();
	private DateType dataVariazioneOnLine = new DateType();
	private StringType telefonoFissoCliente = new StringType();
	private StringType telefonoCellulareCliente = new StringType();
	private DoubleType qtaPol = new DoubleType();
	private DateType datScadPol = new DateType();
	private StringType flgPolInLiqzne = new StringType();
	private StringType flgCliAttivAdv = new StringType();
	private StringType indirizzoEmail = new StringType();
	private StringType codiceStatoSchedaSCAI = new StringType();
	private StringType descrizioneStatoSchedaSCAI = new StringType();
	private DateType dataVariazioneStatoSchedaScai = new DateType();
	private DateType dataScadenzaSchedaScai = new DateType();
	private StringType flagClienteInGestioneScadenziere = new StringType();
	private DateType dataCalcoloMotore = new DateType();
	private DateType dataFineCountdown = new DateType();
	private DateType dataFineProcedura = new DateType();
	private StringType codiceAzioneInputMotore = new StringType();
	private DoubleType progressivoVersioneStepPratica = new DoubleType();

	public boolean isSchedaSCAIUpdated() {
		if (!getDataScadenzaSchedaScai().isNull()) {
			Date date = getDataScadenzaSchedaScai().dateValue();
			Date oggi = Calendar.getInstance().getTime();
			return oggi.before(date);
		} else {
			return true;
		}
	}

	public boolean isDocumentoScaduto() {
		if (!getDataScadenzaDocumento().isNull()) {
			Date date = getDataScadenzaDocumento().dateValue();
			Date oggi = Calendar.getInstance().getTime();
			return oggi.after(date);
		} else {
			return false;
		}
	}

			
	public boolean isSchedaSCAIScaduta() {
		return getCodiceStatoSchedaSCAI() != null && getCodiceStatoSchedaSCAI().equals(STATO_SCADUTO);
	}
	
	public boolean isSchedaSCAIInScadenza() {
		return getCodiceStatoSchedaSCAI() != null && getCodiceStatoSchedaSCAI().equals(STATO_IN_SCADENZA);
	}
	
	public boolean isSchedaSCAIBloccata() {
		return getCodiceStatoSchedaSCAI() != null && getCodiceStatoSchedaSCAI().equals(STATO_BLOCCATO);
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public StringType getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(StringType nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public StringType getCognomeCliente() {
		return cognomeCliente;
	}

	public void setCognomeCliente(StringType cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}

	public StringType getCodiceAgente() {
		return codiceAgente;
	}

	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	public DateType getDataCopiaDocumento() {
		return dataCopiaDocumento;
	}

	public void setDataCopiaDocumento(DateType dataCopiaDocumento) {
		this.dataCopiaDocumento = dataCopiaDocumento;
	}

	public DateType getDataPrimoContattoUtile() {
		return dataPrimoContattoUtile;
	}

	public void setDataPrimoContattoUtile(DateType dataPrimoContattoUtile) {
		this.dataPrimoContattoUtile = dataPrimoContattoUtile;
	}

	public DateType getDataProfiloRischio() {
		return dataProfiloRischio;
	}

	public void setDataProfiloRischio(DateType dataProfiloRischio) {
		this.dataProfiloRischio = dataProfiloRischio;
	}

	public DateType getDataScadenzaDocumento() {
		return dataScadenzaDocumento;
	}

	public void setDataScadenzaDocumento(DateType dataScadenzaDocumento) {
		this.dataScadenzaDocumento = dataScadenzaDocumento;
	}

	public DateType getDataVariazioneAnagrafica() {
		return dataVariazioneAnagrafica;
	}

	public void setDataVariazioneAnagrafica(DateType dataVariazioneAnagrafica) {
		this.dataVariazioneAnagrafica = dataVariazioneAnagrafica;
	}

	public StringType getFlagControlloCliAttivoAntiriciclaggio() {
		return flagControlloCliAttivoAntiriciclaggio;
	}

	public void setFlagControlloCliAttivoAntiriciclaggio(StringType flagControlloCliAttivoAntiriciclaggio) {
		this.flagControlloCliAttivoAntiriciclaggio = flagControlloCliAttivoAntiriciclaggio;
	}

	public StringType getFlagControlloCopiaDocumento() {
		return flagControlloCopiaDocumento;
	}

	public void setFlagControlloCopiaDocumento(StringType flagControlloCopiaDocumento) {
		this.flagControlloCopiaDocumento = flagControlloCopiaDocumento;
	}

	public StringType getFlagControlloDataEmissioneDoc() {
		return flagControlloDataEmissioneDoc;
	}

	public void setFlagControlloDataEmissioneDoc(StringType flagControlloDataEmissioneDoc) {
		this.flagControlloDataEmissioneDoc = flagControlloDataEmissioneDoc;
	}

	public StringType getFlagControlloLuogoEmissioneDoc() {
		return flagControlloLuogoEmissioneDoc;
	}

	public void setFlagControlloLuogoEmissioneDoc(StringType flagControlloLuogoEmissioneDoc) {
		this.flagControlloLuogoEmissioneDoc = flagControlloLuogoEmissioneDoc;
	}

	public StringType getFlagControlloLuogoSvolgimentoProf() {
		return flagControlloLuogoSvolgimentoProf;
	}

	public void setFlagControlloLuogoSvolgimentoProf(StringType flagControlloLuogoSvolgimentoProf) {
		this.flagControlloLuogoSvolgimentoProf = flagControlloLuogoSvolgimentoProf;
	}

	public StringType getFlagControlloNumeroDocumento() {
		return flagControlloNumeroDocumento;
	}

	public void setFlagControlloNumeroDocumento(StringType flagControlloNumeroDocumento) {
		this.flagControlloNumeroDocumento = flagControlloNumeroDocumento;
	}

	public StringType getFlagControlloProfessione() {
		return flagControlloProfessione;
	}

	public void setFlagControlloProfessione(StringType flagControlloProfessione) {
		this.flagControlloProfessione = flagControlloProfessione;
	}

	public StringType getFlagControlloTAE() {
		return flagControlloTAE;
	}

	public void setFlagControlloTAE(StringType flagControlloTAE) {
		this.flagControlloTAE = flagControlloTAE;
	}

	public StringType getFlagControlloTipoDocumento() {
		return flagControlloTipoDocumento;
	}

	public void setFlagControlloTipoDocumento(StringType flagControlloTipoDocumento) {
		this.flagControlloTipoDocumento = flagControlloTipoDocumento;
	}

	public StringType getFlgControlloVariazioneOnline() {
		return flgControlloVariazioneOnline;
	}

	public void setFlgControlloVariazioneOnline(StringType flgControlloVariazioneOnline) {
		this.flgControlloVariazioneOnline = flgControlloVariazioneOnline;
	}

	public DoubleType getQtaMesiVariazioneProfiloRischio() {
		return qtaMesiVariazioneProfiloRischio;
	}

	public void setQtaMesiVariazioneProfiloRischio(DoubleType qtaMesiVariazioneProfiloRischio) {
		this.qtaMesiVariazioneProfiloRischio = qtaMesiVariazioneProfiloRischio;
	}

	public StringType getCodiceFascioProfiloRischio() {
		return codiceFascioProfiloRischio;
	}

	public void setCodiceFascioProfiloRischio(StringType codiceFascioProfiloRischio) {
		this.codiceFascioProfiloRischio = codiceFascioProfiloRischio;
	}

	public StringType getDescrizioneFascioProfiloRischio() {
		return descrizioneFascioProfiloRischio;
	}

	public void setDescrizioneFascioProfiloRischio(StringType descrizioneFascioProfiloRischio) {
		this.descrizioneFascioProfiloRischio = descrizioneFascioProfiloRischio;
	}

	public DateType getDataInizioValiditaFascioProfiloRischio() {
		return dataInizioValiditaFascioProfiloRischio;
	}

	public void setDataInizioValiditaFascioProfiloRischio(DateType dataInizioValiditaFascioProfiloRischio) {
		this.dataInizioValiditaFascioProfiloRischio = dataInizioValiditaFascioProfiloRischio;
	}

	public StringType getFlagControlloAnagrafica() {
		return flagControlloAnagrafica;
	}

	public void setFlagControlloAnagrafica(StringType flagControlloAnagrafica) {
		this.flagControlloAnagrafica = flagControlloAnagrafica;
	}

	public DateType getDataVariazioneOnLine() {
		return dataVariazioneOnLine;
	}

	public void setDataVariazioneOnLine(DateType dataVariazioneOnLine) {
		this.dataVariazioneOnLine = dataVariazioneOnLine;
	}

	public StringType getTelefonoFissoCliente() {
		return telefonoFissoCliente;
	}

	public void setTelefonoFissoCliente(StringType telefonoFissoCliente) {
		this.telefonoFissoCliente = telefonoFissoCliente;
	}

	public StringType getTelefonoCellulareCliente() {
		return telefonoCellulareCliente;
	}

	public void setTelefonoCellulareCliente(StringType telefonoCellulareCliente) {
		this.telefonoCellulareCliente = telefonoCellulareCliente;
	}

	public DoubleType getQtaPol() {
		return qtaPol;
	}

	public void setQtaPol(DoubleType qtaPol) {
		this.qtaPol = qtaPol;
	}

	public DateType getDatScadPol() {
		return datScadPol;
	}

	public void setDatScadPol(DateType datScadPol) {
		this.datScadPol = datScadPol;
	}

	public StringType getFlgPolInLiqzne() {
		return flgPolInLiqzne;
	}

	public void setFlgPolInLiqzne(StringType flgPolInLiqzne) {
		this.flgPolInLiqzne = flgPolInLiqzne;
	}

	public StringType getFlgCliAttivAdv() {
		return flgCliAttivAdv;
	}

	public void setFlgCliAttivAdv(StringType flgCliAttivAdv) {
		this.flgCliAttivAdv = flgCliAttivAdv;
	}

	public StringType getIndirizzoEmail() {
		return indirizzoEmail;
	}

	public void setIndirizzoEmail(StringType indirizzoEmail) {
		this.indirizzoEmail = indirizzoEmail;
	}

	public StringType getCodiceStatoSchedaSCAI() {
		return codiceStatoSchedaSCAI;
	}

	public void setCodiceStatoSchedaSCAI(StringType codiceStatoSchedaSCAI) {
		this.codiceStatoSchedaSCAI = codiceStatoSchedaSCAI;
	}

	public StringType getDescrizioneStatoSchedaSCAI() {
		return descrizioneStatoSchedaSCAI;
	}

	public void setDescrizioneStatoSchedaSCAI(StringType descrizioneStatoSchedaSCAI) {
		this.descrizioneStatoSchedaSCAI = descrizioneStatoSchedaSCAI;
	}

	public DateType getDataVariazioneStatoSchedaScai() {
		return dataVariazioneStatoSchedaScai;
	}

	public void setDataVariazioneStatoSchedaScai(DateType dataVariazioneStatoSchedaScai) {
		this.dataVariazioneStatoSchedaScai = dataVariazioneStatoSchedaScai;
	}

	public DateType getDataScadenzaSchedaScai() {
		return dataScadenzaSchedaScai;
	}

	public void setDataScadenzaSchedaScai(DateType dataScadenzaSchedaScai) {
		this.dataScadenzaSchedaScai = dataScadenzaSchedaScai;
	}

	public StringType getFlagClienteInGestioneScadenziere() {
		return flagClienteInGestioneScadenziere;
	}

	public void setFlagClienteInGestioneScadenziere(StringType flagClienteInGestioneScadenziere) {
		this.flagClienteInGestioneScadenziere = flagClienteInGestioneScadenziere;
	}

	public DateType getDataCalcoloMotore() {
		return dataCalcoloMotore;
	}

	public void setDataCalcoloMotore(DateType dataCalcoloMotore) {
		this.dataCalcoloMotore = dataCalcoloMotore;
	}

	public DateType getDataFineCountdown() {
		return dataFineCountdown;
	}

	public void setDataFineCountdown(DateType dataFineCountdown) {
		this.dataFineCountdown = dataFineCountdown;
	}

	public DateType getDataFineProcedura() {
		return dataFineProcedura;
	}

	public void setDataFineProcedura(DateType dataFineProcedura) {
		this.dataFineProcedura = dataFineProcedura;
	}

	public StringType getCodiceAzioneInputMotore() {
		return codiceAzioneInputMotore;
	}

	public void setCodiceAzioneInputMotore(StringType codiceAzioneInputMotore) {
		this.codiceAzioneInputMotore = codiceAzioneInputMotore;
	}

	public DoubleType getProgressivoVersioneStepPratica() {
		return progressivoVersioneStepPratica;
	}

	public void setProgressivoVersioneStepPratica(DoubleType progressivoVersioneStepPratica) {
		this.progressivoVersioneStepPratica = progressivoVersioneStepPratica;
	}

}
