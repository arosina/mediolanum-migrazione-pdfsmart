package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class ComuneModel extends AbstractSectionModel{

	private BooleanType isIscrittoAlCatasto = new BooleanType();

	private StringType codNazione   = new StringType(Costanti.COD_NAZIONE_ITALIA);
	private StringType comune		= new StringType(); // Questo campo rappresenta in realtà la località, dato richiesto a front-end 
    private StringType comuneEstero	= new StringType();
	private StringType cap			= new StringType();
	private StringType provincia	= new StringType();	
	private StringType codComune	= new StringType();
	private DateType   scadenzaProvincia 	= new DateType();
	private DateType   scadenzaComune 		= new DateType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ComuneModel(){
		
		addCodDescField("codNazione","NAZIONI");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setCodNazione(StringType codNazione) {
		if(codNazione != null && (codNazione.isNull() || 
								  codNazione.equals("ITA") || 
								  codNazione.toString().startsWith("*")))
			codNazione = new StringType(Costanti.COD_NAZIONE_ITALIA);
		this.codNazione = codNazione;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodNazione() {
		if(this.codNazione != null && (this.codNazione.isNull() || 
				  					   this.codNazione.equals("ITA") || 
				  					   this.codNazione.toString().startsWith("*")))
			this.codNazione = new StringType(Costanti.COD_NAZIONE_ITALIA);
		return this.codNazione;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getDescrComune(){
		if(getCodNazione().equals(Costanti.COD_NAZIONE_ITALIA))
			return getComune();
		else
			return getComuneEstero();
	}
	
	/********************************************************************
	 * Il campo "comune" rappresenta in realtà la "località", dato richiesto a front-end 
	 */
	public StringType getComune() {
		return comune;
	}
	public void setComune(StringType comune) {
		this.comune = comune;
	}
	/********************************************************************/


	public StringType getCap() {
		return cap;
	}

	public StringType getCodComune() {
		return codComune;
	}

	public StringType getProvincia() {
		return provincia;
	}

	public void setCap(StringType cap) {
		this.cap = cap;
	}

	public void setCodComune(StringType codComune) {
		this.codComune = codComune;
	}

	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}

	public DateType getScadenzaProvincia() {
		return scadenzaProvincia;
	}

	public void setScadenzaProvincia(DateType scadenzaProvincia) {
		this.scadenzaProvincia = scadenzaProvincia;
	}

	public StringType getComuneEstero() {
		return comuneEstero;
	}

	public void setComuneEstero(StringType comuneEstero) {
		this.comuneEstero = comuneEstero;
	}

	public BooleanType getIsIscrittoAlCatasto() {
		return isIscrittoAlCatasto;
	}

	public void setIsIscrittoAlCatasto(BooleanType isIscrittoAlCatasto) {
		this.isIscrittoAlCatasto = isIscrittoAlCatasto;
	}

	public DateType getScadenzaComune() {
		return scadenzaComune;
	}

	public void setScadenzaComune(DateType scadenzaComune) {
		this.scadenzaComune = scadenzaComune;
	}



}
