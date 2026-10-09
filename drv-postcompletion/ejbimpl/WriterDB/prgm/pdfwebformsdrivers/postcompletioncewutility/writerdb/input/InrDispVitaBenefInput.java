package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispVitaBenefInput extends CommandDataModel{

    private StringType codDisposizione					= new StringType();
    private StringType codAgente						= new StringType();
    private StringType codRete							= new StringType();
    private StringType codiceFiscale		 			= new StringType();
    private StringType nominativo	 					= new StringType();
    private DoubleType percentualeRipartizione			= new DoubleType();    
    private StringType codiceCliente	 				= new StringType();
    private StringType tipologiaPersona	 				= new StringType();
    private StringType tipologiaBeneficiario	 		= new StringType();
    private StringType invioComunicazione				= new StringType();
    private StringType nome	 							= new StringType();
    private StringType sesso	 						= new StringType();
    private StringType comuneDiNascita	 				= new StringType();
    private DateType   dataNascita 						= new DateType();
    private StringType codiceComuneNascita				= new StringType();
    private StringType provinciaNascita	 				= new StringType();
    private StringType nazioneNascita	 				= new StringType();
    private StringType toponimoIndirizzoResidenza		= new StringType();
    private StringType indirizzoResidenza	 			= new StringType();
    private StringType numeroCivicoResidenza			= new StringType();
    private StringType capResidenza	 					= new StringType();
    private StringType comuneResidenza	 				= new StringType();
    private StringType codiceComuneResidenza			= new StringType();
    private StringType provinciaResidenza	 			= new StringType();
    private StringType nazioneResidenza	 				= new StringType();
    private StringType tipoTelefono						= new StringType();
    private StringType prefissoInternazionaleTelefono	= new StringType();
    private StringType prefissoTelefono	 				= new StringType();
    private StringType numeroTelefono	 				= new StringType();
    private StringType email	 						= new StringType();
    private StringType numeroIscrizioneCCIA	 			= new StringType();
    private DateType   dataIscrizioneCCIA 				= new DateType();
    private StringType provinciaIscrizioneCCIA			= new StringType();
   
    
    private StringType tipoRelazioneBeneficiarioContraente			    = new StringType(); 
    private StringType descrizioneTipoRelazioneBeneficiarioContraente	= new StringType();
    private StringType tipoRelazioneBeneficiarioAssicurando			    = new StringType(); 
    private StringType descrizioneTipoRelazioneBeneficiarioAssicurando	= new StringType();
    
 	
	/*********************************************************************************/
	/*********************************************************************************/

	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public StringType getNominativo() {
		return nominativo;
	}
	public void setNominativo(StringType nominativo) {
		this.nominativo = nominativo;
	}
	public DoubleType getPercentualeRipartizione() {
		return percentualeRipartizione;
	}
	public void setPercentualeRipartizione(DoubleType percentualeRipartizione) {
		this.percentualeRipartizione = percentualeRipartizione;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public StringType getTipologiaPersona() {
		return tipologiaPersona;
	}
	public void setTipologiaPersona(StringType tipologiaPersona) {
		this.tipologiaPersona = tipologiaPersona;
	}
	public StringType getTipologiaBeneficiario() {
		return tipologiaBeneficiario;
	}
	public void setTipologiaBeneficiario(StringType tipologiaBeneficiario) {
		this.tipologiaBeneficiario = tipologiaBeneficiario;
	}
	public StringType getInvioComunicazione() {
		return invioComunicazione;
	}
	public void setInvioComunicazione(StringType invioComunicazione) {
		this.invioComunicazione = invioComunicazione;
	}
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}
	public StringType getSesso() {
		return sesso;
	}
	public void setSesso(StringType sesso) {
		this.sesso = sesso;
	}
	public StringType getComuneDiNascita() {
		return comuneDiNascita;
	}
	public void setComuneDiNascita(StringType comuneDiNascita) {
		this.comuneDiNascita = comuneDiNascita;
	}
	public StringType getCodiceComuneNascita() {
		return codiceComuneNascita;
	}
	public void setCodiceComuneNascita(StringType codiceComuneNascita) {
		this.codiceComuneNascita = codiceComuneNascita;
	}
	public StringType getProvinciaNascita() {
		return provinciaNascita;
	}
	public void setProvinciaNascita(StringType provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}
	public StringType getNazioneNascita() {
		return nazioneNascita;
	}
	public void setNazioneNascita(StringType nazioneNascita) {
		this.nazioneNascita = nazioneNascita;
	}
	public StringType getToponimoIndirizzoResidenza() {
		return toponimoIndirizzoResidenza;
	}
	public void setToponimoIndirizzoResidenza(StringType toponimoIndirizzoResidenza) {
		this.toponimoIndirizzoResidenza = toponimoIndirizzoResidenza;
	}
	public StringType getIndirizzoResidenza() {
		return indirizzoResidenza;
	}
	public void setIndirizzoResidenza(StringType indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public StringType getNumeroCivicoResidenza() {
		return numeroCivicoResidenza;
	}
	public void setNumeroCivicoResidenza(StringType numeroCivicoResidenza) {
		this.numeroCivicoResidenza = numeroCivicoResidenza;
	}
	public StringType getCapResidenza() {
		return capResidenza;
	}
	public void setCapResidenza(StringType capResidenza) {
		this.capResidenza = capResidenza;
	}
	public StringType getComuneResidenza() {
		return comuneResidenza;
	}
	public void setComuneResidenza(StringType comuneResidenza) {
		this.comuneResidenza = comuneResidenza;
	}
	public StringType getCodiceComuneResidenza() {
		return codiceComuneResidenza;
	}
	public void setCodiceComuneResidenza(StringType codiceComuneResidenza) {
		this.codiceComuneResidenza = codiceComuneResidenza;
	}
	public StringType getProvinciaResidenza() {
		return provinciaResidenza;
	}
	public void setProvinciaResidenza(StringType provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	public StringType getNazioneResidenza() {
		return nazioneResidenza;
	}
	public void setNazioneResidenza(StringType nazioneResidenza) {
		this.nazioneResidenza = nazioneResidenza;
	}
	public StringType getTipoTelefono() {
		return tipoTelefono;
	}
	public void setTipoTelefono(StringType tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}
	public StringType getPrefissoInternazionaleTelefono() {
		return prefissoInternazionaleTelefono;
	}
	public void setPrefissoInternazionaleTelefono(StringType prefissoInternazionaleTelefono) {
		this.prefissoInternazionaleTelefono = prefissoInternazionaleTelefono;
	}
	public StringType getPrefissoTelefono() {
		return prefissoTelefono;
	}
	public void setPrefissoTelefono(StringType prefissoTelefono) {
		this.prefissoTelefono = prefissoTelefono;
	}
	public StringType getNumeroTelefono() {
		return numeroTelefono;
	}
	public void setNumeroTelefono(StringType numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}
	public StringType getEmail() {
		return email;
	}
	public void setEmail(StringType email) {
		this.email = email;
	}
	public DateType getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(DateType dataNascita) {
		this.dataNascita = dataNascita;
	}
	public StringType getTipoRelazioneBeneficiarioContraente() {
		return tipoRelazioneBeneficiarioContraente;
	}
	public void setTipoRelazioneBeneficiarioContraente(StringType tipoRelazioneBeneficiarioContraente) {
		this.tipoRelazioneBeneficiarioContraente = tipoRelazioneBeneficiarioContraente;
	}
	public StringType getDescrizioneTipoRelazioneBeneficiarioContraente() {
		return descrizioneTipoRelazioneBeneficiarioContraente;
	}
	public void setDescrizioneTipoRelazioneBeneficiarioContraente(
			StringType descrizioneTipoRelazioneBeneficiarioContraente) {
		this.descrizioneTipoRelazioneBeneficiarioContraente = descrizioneTipoRelazioneBeneficiarioContraente;
	}
	public StringType getTipoRelazioneBeneficiarioAssicurando() {
		return tipoRelazioneBeneficiarioAssicurando;
	}
	public void setTipoRelazioneBeneficiarioAssicurando(StringType tipoRelazioneBeneficiarioAssicurando) {
		this.tipoRelazioneBeneficiarioAssicurando = tipoRelazioneBeneficiarioAssicurando;
	}
	public StringType getDescrizioneTipoRelazioneBeneficiarioAssicurando() {
		return descrizioneTipoRelazioneBeneficiarioAssicurando;
	}
	public void setDescrizioneTipoRelazioneBeneficiarioAssicurando(
			StringType descrizioneTipoRelazioneBeneficiarioAssicurando) {
		this.descrizioneTipoRelazioneBeneficiarioAssicurando = descrizioneTipoRelazioneBeneficiarioAssicurando;
	}
	public StringType getNumeroIscrizioneCCIA() {
		return numeroIscrizioneCCIA;
	}
	public void setNumeroIscrizioneCCIA(StringType numeroIscrizioneCCIA) {
		this.numeroIscrizioneCCIA = numeroIscrizioneCCIA;
	}
	public DateType getDataIscrizioneCCIA() {
		return dataIscrizioneCCIA;
	}
	public void setDataIscrizioneCCIA(DateType dataIscrizioneCCIA) {
		this.dataIscrizioneCCIA = dataIscrizioneCCIA;
	}
	public StringType getProvinciaIscrizioneCCIA() {
		return provinciaIscrizioneCCIA;
	}
	public void setProvinciaIscrizioneCCIA(StringType provinciaIscrizioneCCIA) {
		this.provinciaIscrizioneCCIA = provinciaIscrizioneCCIA;
	}

  
}