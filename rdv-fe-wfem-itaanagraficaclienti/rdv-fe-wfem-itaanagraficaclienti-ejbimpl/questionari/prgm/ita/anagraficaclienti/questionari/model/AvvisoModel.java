package prgm.ita.anagraficaclienti.questionari.model;


import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;


public class AvvisoModel extends CommandDataModel {
	
	private static final long serialVersionUID = 1L;
	
	private StringType sorgenteEsterna = new StringType();
	private StringType idRisorsaEsterna = new StringType();
	private StringType codTipoAvviso = new StringType();
	private StringType codUserDestinatario = new StringType();
	private StringType dataLettura = new StringType();
	private BooleanType portaInPrimoPiano = new BooleanType();
	private StringType nominativoDestinatario = new StringType();
	private StringType oggetto = new StringType();
	
	// TODO verificare, in excel "clob"
	private StringType testo = new StringType();
	
	private StringType dataScadenza = new StringType();
	private BooleanType previstaAttivita = new BooleanType();
	private StringType dataEsecuzioneAttivita = new StringType();
	private StringType codiceAgente = new StringType();
	private StringType nominativoAgente = new StringType();
	private StringType codiceCliente = new StringType();
	private StringType nominativoCliente = new StringType();
	
	private StringType prodotto = new StringType();
	private StringType numeroContratto = new StringType();

	private StringType operazione = new StringType();
	private DoubleType importo = new DoubleType();
	private StringType divisaImporto = new StringType();
	
	private IntegerType resultCode = new IntegerType();
	private StringType resultMessage = new StringType();
	private Long idAvviso = 0L;
	
	public StringType getSorgenteEsterna() {
		return sorgenteEsterna;
	}
	
	public StringType getIdRisorsaEsterna() {
		return idRisorsaEsterna;
	}
	
	public StringType getCodTipoAvviso() {
		return codTipoAvviso;
	}
	
	public StringType getCodUserDestinatario() {
		return codUserDestinatario;
	}
	
	public StringType getDataLettura() {
		return dataLettura;
	}
	
	public BooleanType getPortaInPrimoPiano() {
		return portaInPrimoPiano;
	}
	
	public StringType getNominativoDestinatario() {
		return nominativoDestinatario;
	}
	
	public StringType getOggetto() {
		return oggetto;
	}
	
	public StringType getTesto() {
		return testo;
	}
	
	public StringType getDataScadenza() {
		return dataScadenza;
	}
	
	public BooleanType getPrevistaAttivita() {
		return previstaAttivita;
	}
	
	public StringType getDataEsecuzioneAttivita() {
		return dataEsecuzioneAttivita;
	}
	
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	
	public StringType getNominativoAgente() {
		return nominativoAgente;
	}
	
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	
	public StringType getNominativoCliente() {
		return nominativoCliente;
	}
	
	public StringType getProdotto() {
		return prodotto;
	}
	
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	
	public StringType getOperazione() {
		return operazione;
	}
	
	public DoubleType getImporto() {
		return importo;
	}
	
	public StringType getDivisaImporto() {
		return divisaImporto;
	}
	
	public void setSorgenteEsterna(StringType sorgenteEsterna) {
		this.sorgenteEsterna = sorgenteEsterna;
	}
	
	public void setIdRisorsaEsterna(StringType idRisorsaEsterna) {
		this.idRisorsaEsterna = idRisorsaEsterna;
	}
	
	public void setCodTipoAvviso(StringType codTipoAvviso) {
		this.codTipoAvviso = codTipoAvviso;
	}
	
	public void setCodUserDestinatario(StringType codUserDestinatario) {
		this.codUserDestinatario = codUserDestinatario;
	}
	
	public void setDataLettura(StringType dataLettura) {
		this.dataLettura = dataLettura;
	}
	
	public void setPortaInPrimoPiano(BooleanType portaInPrimoPiano) {
		this.portaInPrimoPiano = portaInPrimoPiano;
	}
	
	public void setNominativoDestinatario(StringType nominativoDestinatario) {
		this.nominativoDestinatario = nominativoDestinatario;
	}
	
	public void setOggetto(StringType oggetto) {
		this.oggetto = oggetto;
	}
	
	public void setTesto(StringType testo) {
		this.testo = testo;
	}
	
	public void setDataScadenza(StringType dataScadenza) {
		this.dataScadenza = dataScadenza;
	}
	
	public void setPrevistaAttivita(BooleanType previstaAttivita) {
		this.previstaAttivita = previstaAttivita;
	}
	
	public void setDataEsecuzioneAttivita(StringType dataEsecuzioneAttivita) {
		this.dataEsecuzioneAttivita = dataEsecuzioneAttivita;
	}
	
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	
	public void setNominativoAgente(StringType nominativoAgente) {
		this.nominativoAgente = nominativoAgente;
	}
	
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	
	public void setNominativoCliente(StringType nominativoCliente) {
		this.nominativoCliente = nominativoCliente;
	}
	
	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}
	
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	
	public void setOperazione(StringType operazione) {
		this.operazione = operazione;
	}
	
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	
	public void setDivisaImporto(StringType divisaImporto) {
		this.divisaImporto = divisaImporto;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public StringType getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(StringType resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Long getIdAvviso() {
		return idAvviso;
	}

	public void setIdAvviso(Long idAvviso) {
		this.idAvviso = idAvviso;
	}
		

}
