package prgm.pdfwebforms.idd;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.model.CodRuoliImpersonati;

/***********************************************************************************************/
/***********************************************************************************************/
public class IddCallModel extends CommandDataModel {
	
	// Input
	private StringType  			utente = new StringType();
	private StringType				dataRiferimento = new StringType();
	private StringType				codiceCliente = new StringType();
	private StringType				tipoCliente = new StringType();
	private StringType				ruoloAgente = new StringType("1"); // Default 1=FB
	private StringType				pdfInstanceId = new StringType();
	private StringType      		idQuestionarioIddInInput  = new StringType();
	private StringType      		prodottoInInput = new StringType();
	private ProvideIddDataResponse 	input = null;	// Ritornato dalla callback "provideIddData" del driver
	// Input getPolizze per cliente in Target Market o no
	private StringType				tariffa = new StringType();
	
	// Output
	private StringType      flagEsitoAdeguatezzaQuestionario = new StringType();
	private StringType      idQuestionarioIdd  = new StringType();
    private StringType      codStatoQuestionarioIdd = new StringType();
    private StringType      descrStatoQuestionarioIdd = new StringType();
    
	private StringType      flagEsitoAdeguatezzaRaccomandazione = new StringType();
	private StringType      idRaccomandazioneIdd  = new StringType(); // Input/Output in caso si adeguatezza sul questionario
    private StringType      codStatoRaccomandazioneIdd = new StringType();
    private StringType      descrStatoRaccomandazioneIdd = new StringType();
    
    private StringType      idRaccomandazioneIddCtrl = new StringType();
    private StringType      idEcmRaccomandazioneIdd = new StringType();
    private StringType  	idReportAdeguatezza = new StringType();
    
    private StringType      esitoTecnico = new StringType();
    private StringType      esitoValutazione = new StringType();
    private ListType		listaCausali = new ListType(DettaglioEsitoIddModel.class);
    private ListType		listaErroriTecnici = new ListType(DettaglioEsitoIddModel.class);
 
    // Output getPolizze per cliente in Target Market o no
    private StringType  	flagControlloTMSottoscrizione = new StringType();
    private StringType  	flagControlloTMPostvendita = new StringType();
    // Output recupero id questionario light target market per inserimento MOM
    private StringType  	idQLTM = new StringType();
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
	
	/***********************************************************************************************/
    /***********************************************************************************************/
    public void initRuoloAgente(StringType codRuoloImpersonato) {
    	if(codRuoloImpersonato.isNull() || codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.FB))
    		setRuoloAgente(new StringType("1"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.CS))
    		setRuoloAgente(new StringType("4"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.FPS))
    		setRuoloAgente(new StringType("8"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.BC))
    		setRuoloAgente(new StringType("9"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.OS))
    		setRuoloAgente(new StringType("10"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.SA))
    		setRuoloAgente(new StringType("11"));
    	else if(codRuoloImpersonato.equalsIgnoreCase(CodRuoliImpersonati.TWP))
    		setRuoloAgente(new StringType("12"));
    }
    
	public StringType getDataRiferimento() {
		return dataRiferimento;
	}
	public void setDataRiferimento(StringType dataRiferimento) {
		this.dataRiferimento = dataRiferimento;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public StringType getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(StringType tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public ProvideIddDataResponse getInput() {
		return input;
	}
	public void setInput(ProvideIddDataResponse input) {
		this.input = input;
	}
	public StringType getFlagEsitoAdeguatezzaQuestionario() {
		return flagEsitoAdeguatezzaQuestionario;
	}
	public void setFlagEsitoAdeguatezzaQuestionario(
			StringType flagEsitoAdeguatezzaQuestionario) {
		this.flagEsitoAdeguatezzaQuestionario = flagEsitoAdeguatezzaQuestionario;
	}
	public StringType getIdQuestionarioIdd() {
		return idQuestionarioIdd;
	}
	public void setIdQuestionarioIdd(StringType idQuestionarioIdd) {
		this.idQuestionarioIdd = idQuestionarioIdd;
	}
	public StringType getCodStatoQuestionarioIdd() {
		return codStatoQuestionarioIdd;
	}
	public void setCodStatoQuestionarioIdd(StringType codStatoQuestionarioIdd) {
		this.codStatoQuestionarioIdd = codStatoQuestionarioIdd;
	}
	public StringType getDescrStatoQuestionarioIdd() {
		return descrStatoQuestionarioIdd;
	}
	public void setDescrStatoQuestionarioIdd(StringType descrStatoQuestionarioIdd) {
		this.descrStatoQuestionarioIdd = descrStatoQuestionarioIdd;
	}
	public StringType getFlagEsitoAdeguatezzaRaccomandazione() {
		return flagEsitoAdeguatezzaRaccomandazione;
	}
	public void setFlagEsitoAdeguatezzaRaccomandazione(
			StringType flagEsitoAdeguatezzaRaccomandazione) {
		this.flagEsitoAdeguatezzaRaccomandazione = flagEsitoAdeguatezzaRaccomandazione;
	}
	public StringType getIdRaccomandazioneIdd() {
		return idRaccomandazioneIdd;
	}
	public void setIdRaccomandazioneIdd(StringType idRaccomandazioneIdd) {
		this.idRaccomandazioneIdd = idRaccomandazioneIdd;
	}
	public StringType getCodStatoRaccomandazioneIdd() {
		return codStatoRaccomandazioneIdd;
	}
	public void setCodStatoRaccomandazioneIdd(StringType codStatoRaccomandazioneIdd) {
		this.codStatoRaccomandazioneIdd = codStatoRaccomandazioneIdd;
	}
	public StringType getDescrStatoRaccomandazioneIdd() {
		return descrStatoRaccomandazioneIdd;
	}
	public void setDescrStatoRaccomandazioneIdd(
			StringType descrStatoRaccomandazioneIdd) {
		this.descrStatoRaccomandazioneIdd = descrStatoRaccomandazioneIdd;
	}
	public StringType getIdRaccomandazioneIddCtrl() {
		return idRaccomandazioneIddCtrl;
	}
	public void setIdRaccomandazioneIddCtrl(StringType idRaccomandazioneIddCtrl) {
		this.idRaccomandazioneIddCtrl = idRaccomandazioneIddCtrl;
	}
	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}
	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	public StringType getIdEcmRaccomandazioneIdd() {
		return idEcmRaccomandazioneIdd;
	}
	public void setIdEcmRaccomandazioneIdd(StringType idEcmRaccomandazioneIdd) {
		this.idEcmRaccomandazioneIdd = idEcmRaccomandazioneIdd;
	}
	public StringType getUtente() {
		return utente;
	}
	public void setUtente(StringType utente) {
		this.utente = utente;
	}
	public StringType getIdQuestionarioIddInInput() {
		return idQuestionarioIddInInput;
	}
	public void setIdQuestionarioIddInInput(StringType idQuestionarioIddInInput) {
		this.idQuestionarioIddInInput = idQuestionarioIddInInput;
	}
	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}
	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}
	public StringType getRuoloAgente() {
		return ruoloAgente;
	}
	public void setRuoloAgente(StringType ruoloAgente) {
		this.ruoloAgente = ruoloAgente;
	}

	public StringType getEsitoTecnico() {
		return esitoTecnico;
	}

	public void setEsitoTecnico(StringType esitoTecnico) {
		this.esitoTecnico = esitoTecnico;
	}

	public StringType getEsitoValutazione() {
		return esitoValutazione;
	}

	public void setEsitoValutazione(StringType esitoValutazione) {
		this.esitoValutazione = esitoValutazione;
	}

	public ListType getListaCausali() {
		return listaCausali;
	}

	public void setListaCausali(ListType listaCausali) {
		this.listaCausali = listaCausali;
	}

	public ListType getListaErroriTecnici() {
		return listaErroriTecnici;
	}

	public void setListaErroriTecnici(ListType listaErroriTecnici) {
		this.listaErroriTecnici = listaErroriTecnici;
	}

	public StringType getProdottoInInput() {
		return prodottoInInput;
	}

	public void setProdottoInInput(StringType prodottoInInput) {
		this.prodottoInInput = prodottoInInput;
	}

	public StringType getFlagControlloTMSottoscrizione() {
		return flagControlloTMSottoscrizione;
	}

	public void setFlagControlloTMSottoscrizione(StringType flagControlloTMSottoscrizione) {
		this.flagControlloTMSottoscrizione = flagControlloTMSottoscrizione;
	}

	public StringType getTariffa() {
		return tariffa;
	}

	public void setTariffa(StringType tariffa) {
		this.tariffa = tariffa;
	}

	public StringType getFlagControlloTMPostvendita() {
		return flagControlloTMPostvendita;
	}

	public void setFlagControlloTMPostvendita(StringType flagControlloTMPostvendita) {
		this.flagControlloTMPostvendita = flagControlloTMPostvendita;
	}

	public StringType getIdQLTM() {
		return idQLTM;
	}

	public void setIdQLTM(StringType idQLTM) {
		this.idQLTM = idQLTM;
	}      
    
}
