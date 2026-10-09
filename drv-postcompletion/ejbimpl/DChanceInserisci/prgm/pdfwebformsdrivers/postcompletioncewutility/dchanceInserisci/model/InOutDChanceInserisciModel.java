package prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/******************************************************************************/
/******************************************************************************/
public class InOutDChanceInserisciModel extends CommandDataModel {
	
	// Input
	private StringType utente = new StringType();
	private StringType idRiferimento = new StringType();
	private StringType contoOrigine = new StringType();
	private StringType contoPrenotato = new StringType();
	private StringType dataTimbro = new StringType();
	private StringType convenzione = new StringType();
	private StringType categoria = new StringType();
	private StringType sottocategoria = new StringType();
	private StringType cliCIntestatario1 = new StringType();
	private StringType cliCIntestatario2 = new StringType();
	private StringType cliCIntestatario3 = new StringType();
	private StringType codiceAgente = new StringType();
	private StringType importoGiroconto = new StringType();
	
	// Output
	private StringType esitoRichiestaApertura = new StringType();
	private StringType idCSCRichiestaApertura = new StringType();
	private StringType descrErroreApertura = new StringType();
	private StringType idVendente = new StringType();
	
	public StringType getUtente() {
		return utente;
	}
	public void setUtente(StringType utente) {
		this.utente = utente;
	}
	public StringType getIdRiferimento() {
		return idRiferimento;
	}
	public void setIdRiferimento(StringType idRiferimento) {
		this.idRiferimento = idRiferimento;
	}
	public StringType getContoOrigine() {
		return contoOrigine;
	}
	public void setContoOrigine(StringType contoOrigine) {
		this.contoOrigine = contoOrigine;
	}
	public StringType getContoPrenotato() {
		return contoPrenotato;
	}
	public void setContoPrenotato(StringType contoPrenotato) {
		this.contoPrenotato = contoPrenotato;
	}
	public StringType getDataTimbro() {
		return dataTimbro;
	}
	public void setDataTimbro(StringType dataTimbro) {
		this.dataTimbro = dataTimbro;
	}
	public StringType getConvenzione() {
		return convenzione;
	}
	public void setConvenzione(StringType convenzione) {
		this.convenzione = convenzione;
	}
	public StringType getCategoria() {
		return categoria;
	}
	public void setCategoria(StringType categoria) {
		this.categoria = categoria;
	}
	public StringType getSottocategoria() {
		return sottocategoria;
	}
	public void setSottocategoria(StringType sottocategoria) {
		this.sottocategoria = sottocategoria;
	}
	public StringType getCliCIntestatario1() {
		return cliCIntestatario1;
	}
	public void setCliCIntestatario1(StringType cliCIntestatario1) {
		this.cliCIntestatario1 = cliCIntestatario1;
	}
	public StringType getCliCIntestatario2() {
		return cliCIntestatario2;
	}
	public void setCliCIntestatario2(StringType cliCIntestatario2) {
		this.cliCIntestatario2 = cliCIntestatario2;
	}
	public StringType getCliCIntestatario3() {
		return cliCIntestatario3;
	}
	public void setCliCIntestatario3(StringType cliCIntestatario3) {
		this.cliCIntestatario3 = cliCIntestatario3;
	}
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	public StringType getImportoGiroconto() {
		return importoGiroconto;
	}
	public void setImportoGiroconto(StringType importoGiroconto) {
		this.importoGiroconto = importoGiroconto;
	}
	public StringType getEsitoRichiestaApertura() {
		return esitoRichiestaApertura;
	}
	public void setEsitoRichiestaApertura(StringType esitoRichiestaApertura) {
		this.esitoRichiestaApertura = esitoRichiestaApertura;
	}
	public StringType getIdCSCRichiestaApertura() {
		return idCSCRichiestaApertura;
	}
	public void setIdCSCRichiestaApertura(StringType idCSCRichiestaApertura) {
		this.idCSCRichiestaApertura = idCSCRichiestaApertura;
	}
	public StringType getDescrErroreApertura() {
		return descrErroreApertura;
	}
	public void setDescrErroreApertura(StringType descrErroreApertura) {
		this.descrErroreApertura = descrErroreApertura;
	}
	public StringType getIdVendente() {
		return idVendente;
	}
	public void setIdVendente(StringType idVendente) {
		this.idVendente = idVendente;
	}
	
}
