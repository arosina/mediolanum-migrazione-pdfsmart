package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SoggettoAnagraficoModel extends CommandDataModel {
	//Dati input
	private int ordineCensimento;

	private StringType 	codiceClienteProspect 		= new StringType();
	private StringType 	tipologiaPersona 			= new StringType();
	private StringType 	pdfInstanceId 				= new StringType();
	private StringType 	codiceFiscale 				= new StringType();
	private StringType 	nome 						= new StringType();
	private StringType 	cognome 					= new StringType();
	private StringType 	sesso 						= new StringType();
	private StringType 	partitaIVA 					= new StringType();
	private StringType 	numeroIscrizioneREA 		= new StringType();
	private DateType 	dataIscrizioneREA 			= new DateType();
	private StringType 	provinciaIscrizioneREA 		= new StringType();
	private DateType 	dataNascita 				= new DateType();
	private StringType 	luogoNascita 				= new StringType();
	private StringType 	provinciaComuneNascita 		= new StringType();
	private StringType 	nazioneComuneNascita 		= new StringType();
	private StringType 	tipoVia 					= new StringType(); //toponimo
	private StringType 	indirizzo 					= new StringType();
	private StringType 	numeroCivicoIndirizzo 		= new StringType();
	private StringType 	capComune 					= new StringType();
	private StringType 	localita 					= new StringType();
	private StringType 	provinciaComune 			= new StringType();
	private StringType 	nazioneComune 				= new StringType();
	private StringType 	email 						= new StringType();
	private StringType 	prefissoTelefonoFisso 		= new StringType();
	private StringType 	telefonoFisso 				= new StringType();
	private StringType	prefissoTelefonoCellulare	= new StringType();
	private StringType 	telefonoCellulare 			= new StringType();
	private StringType 	codiceTitolare1 			= new StringType();
	private StringType 	codiceTitolare2 			= new StringType();
	private StringType 	codiceProspectTitolare1 	= new StringType();
	private StringType 	codiceProspectTitolare2 	= new StringType();
	private StringType 	tipoSchedaAnagrafica 		= new StringType();
	private StringType 	canale 						= new StringType();
	private IntegerType numeroAnagraficheCollegate 	= new IntegerType();
	private StringType 	barcode 					= new StringType(); 
	private StringType	sistemaOrigine 				= new StringType();
	private StringType	codiceOperazione 			= new StringType();
	private StringType	idRiferimento 				= new StringType();
	private StringType	codDispositivaBMED 			= new StringType();
	private StringType	modalitaFirma 				= new StringType();

	//Dati output
	private StringType 	codiceEsito 				= new StringType();
	private StringType 	descrizioneEsito 			= new StringType();
	private StringType 	idProgressivoAnagrafica 	= new StringType();
	
	private TimestampType dataUltimaElaborazione	= new TimestampType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getDataNascitaSrv() {
		if (this.dataNascita.isNull())
			return new StringType();
		return new StringType(this.dataNascita.getGG() + "/" + this.dataNascita.getMM() + "/" + this.dataNascita.getAA());
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getDataIscrizioneREASrv() {
		if (this.dataIscrizioneREA.isNull())
			return new StringType();
		return new StringType(this.dataIscrizioneREA.getGG() + "/" + this.dataIscrizioneREA.getMM() + "/" + this.dataIscrizioneREA.getAA());
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	
	public int getOrdineCensimento() {
		return ordineCensimento;
	}

	public void setOrdineCensimento(int ordineCensimento) {
		this.ordineCensimento = ordineCensimento;
	}

	public StringType getCodiceClienteProspect() {
		return codiceClienteProspect;
	}

	public void setCodiceClienteProspect(StringType codiceClienteProspect) {
		this.codiceClienteProspect = codiceClienteProspect;
	}

	public StringType getTipologiaPersona() {
		return tipologiaPersona;
	}

	public void setTipologiaPersona(StringType tipologiaPersona) {
		this.tipologiaPersona = tipologiaPersona;
	}

	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
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

	public StringType getSesso() {
		return sesso;
	}

	public void setSesso(StringType sesso) {
		this.sesso = sesso;
	}

	public StringType getPartitaIVA() {
		return partitaIVA;
	}

	public void setPartitaIVA(StringType partitaIVA) {
		this.partitaIVA = partitaIVA;
	}

	public StringType getNumeroIscrizioneREA() {
		return numeroIscrizioneREA;
	}

	public void setNumeroIscrizioneREA(StringType numeroIscrizioneREA) {
		this.numeroIscrizioneREA = numeroIscrizioneREA;
	}

	public DateType getDataIscrizioneREA() {
		return dataIscrizioneREA;
	}

	public void setDataIscrizioneREA(DateType dataIscrizioneREA) {
		this.dataIscrizioneREA = dataIscrizioneREA;
	}

	public StringType getProvinciaIscrizioneREA() {
		return provinciaIscrizioneREA;
	}

	public void setProvinciaIscrizioneREA(StringType provinciaIscrizioneREA) {
		this.provinciaIscrizioneREA = provinciaIscrizioneREA;
	}

	public DateType getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(DateType dataNascita) {
		this.dataNascita = dataNascita;
	}

	public StringType getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(StringType luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public StringType getProvinciaComuneNascita() {
		return provinciaComuneNascita;
	}

	public void setProvinciaComuneNascita(StringType provinciaComuneNascita) {
		this.provinciaComuneNascita = provinciaComuneNascita;
	}

	public StringType getNazioneComuneNascita() {
		return nazioneComuneNascita;
	}

	public void setNazioneComuneNascita(StringType nazioneComuneNascita) {
		this.nazioneComuneNascita = nazioneComuneNascita;
	}

	public StringType getTipoVia() {
		return tipoVia;
	}

	public void setTipoVia(StringType tipoVia) {
		this.tipoVia = tipoVia;
	}

	public StringType getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}

	public StringType getNumeroCivicoIndirizzo() {
		return numeroCivicoIndirizzo;
	}

	public void setNumeroCivicoIndirizzo(StringType numeroCivicoIndirizzo) {
		this.numeroCivicoIndirizzo = numeroCivicoIndirizzo;
	}

	public StringType getCapComune() {
		return capComune;
	}

	public void setCapComune(StringType capComune) {
		this.capComune = capComune;
	}

	public StringType getLocalita() {
		return localita;
	}

	public void setLocalita(StringType localita) {
		this.localita = localita;
	}

	public StringType getProvinciaComune() {
		return provinciaComune;
	}

	public void setProvinciaComune(StringType provinciaComune) {
		this.provinciaComune = provinciaComune;
	}

	public StringType getNazioneComune() {
		return nazioneComune;
	}

	public void setNazioneComune(StringType nazioneComune) {
		this.nazioneComune = nazioneComune;
	}

	public StringType getEmail() {
		return email;
	}

	public void setEmail(StringType email) {
		this.email = email;
	}

	public StringType getPrefissoTelefonoFisso() {
		return prefissoTelefonoFisso;
	}

	public void setPrefissoTelefonoFisso(StringType prefissoTelefonoFisso) {
		this.prefissoTelefonoFisso = prefissoTelefonoFisso;
	}

	public StringType getTelefonoFisso() {
		return telefonoFisso;
	}

	public void setTelefonoFisso(StringType telefonoFisso) {
		this.telefonoFisso = telefonoFisso;
	}

	public StringType getPrefissoTelefonoCellulare() {
		return prefissoTelefonoCellulare;
	}

	public void setPrefissoTelefonoCellulare(StringType prefissoTelefonoCellulare) {
		this.prefissoTelefonoCellulare = prefissoTelefonoCellulare;
	}

	public StringType getTelefonoCellulare() {
		return telefonoCellulare;
	}

	public void setTelefonoCellulare(StringType telefonoCellulare) {
		this.telefonoCellulare = telefonoCellulare;
	}

	public StringType getCodiceTitolare1() {
		return codiceTitolare1;
	}

	public void setCodiceTitolare1(StringType codiceTitolare1) {
		this.codiceTitolare1 = codiceTitolare1;
	}

	public StringType getCodiceTitolare2() {
		return codiceTitolare2;
	}

	public void setCodiceTitolare2(StringType codiceTitolare2) {
		this.codiceTitolare2 = codiceTitolare2;
	}

	public StringType getCodiceProspectTitolare1() {
		return codiceProspectTitolare1;
	}

	public void setCodiceProspectTitolare1(StringType codiceProspectTitolare1) {
		this.codiceProspectTitolare1 = codiceProspectTitolare1;
	}

	public StringType getCodiceProspectTitolare2() {
		return codiceProspectTitolare2;
	}

	public void setCodiceProspectTitolare2(StringType codiceProspectTitolare2) {
		this.codiceProspectTitolare2 = codiceProspectTitolare2;
	}

	public StringType getTipoSchedaAnagrafica() {
		return tipoSchedaAnagrafica;
	}

	public void setTipoSchedaAnagrafica(StringType tipoSchedaAnagrafica) {
		this.tipoSchedaAnagrafica = tipoSchedaAnagrafica;
	}

	public StringType getCanale() {
		return canale;
	}

	public void setCanale(StringType canale) {
		this.canale = canale;
	}

	public IntegerType getNumeroAnagraficheCollegate() {
		return numeroAnagraficheCollegate;
	}

	public void setNumeroAnagraficheCollegate(IntegerType numeroAnagraficheCollegate) {
		this.numeroAnagraficheCollegate = numeroAnagraficheCollegate;
	}

	public StringType getBarcode() {
		return barcode;
	}

	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}

	public StringType getCodiceEsito() {
		return codiceEsito;
	}

	public void setCodiceEsito(StringType codiceEsito) {
		this.codiceEsito = codiceEsito;
	}

	public StringType getDescrizioneEsito() {
		return descrizioneEsito;
	}

	public void setDescrizioneEsito(StringType descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}
	public StringType getSistemaOrigine() {
		return sistemaOrigine;
	}

	public void setSistemaOrigine(StringType sistemaOrigine) {
		this.sistemaOrigine = sistemaOrigine;
	}
	public StringType getCodDispositivaBMED() {
		return codDispositivaBMED;
	}

	public void setCodDispositivaBMED(StringType codDispositivaBMED) {
		this.codDispositivaBMED = codDispositivaBMED;
	}
	public StringType getIdProgressivoAnagrafica() {
		return idProgressivoAnagrafica;
	}
	public void setIdProgressivoAnagrafica(StringType idProgressivoAnagrafica) {
		this.idProgressivoAnagrafica = idProgressivoAnagrafica;
	}
	public TimestampType getDataUltimaElaborazione() {
		return dataUltimaElaborazione;
	}
	public void setDataUltimaElaborazione(TimestampType dataUltimaElaborazione) {
		this.dataUltimaElaborazione = dataUltimaElaborazione;
	}
	public StringType getCodiceOperazione() {
		return codiceOperazione;
	}
	public void setCodiceOperazione(StringType codiceOperazione) {
		this.codiceOperazione = codiceOperazione;
	}
	public StringType getIdRiferimento() {
		return idRiferimento;
	}
	public void setIdRiferimento(StringType idRiferimento) {
		this.idRiferimento = idRiferimento;
	}
	public StringType getModalitaFirma() {
		return modalitaFirma;
	}
	public void setModalitaFirma(StringType modalitaFirma) {
		this.modalitaFirma = modalitaFirma;
	}
}
