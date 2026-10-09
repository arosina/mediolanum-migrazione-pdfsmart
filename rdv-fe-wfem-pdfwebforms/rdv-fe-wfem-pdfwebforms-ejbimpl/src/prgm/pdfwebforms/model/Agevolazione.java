package prgm.pdfwebforms.model;

import java.io.Serializable;

import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class Agevolazione implements Serializable{
	
	private StringType 		numAgevolazione = null;
	private StringType 		codAgevolazione = null;

	private AbstractType tipo;
	private AbstractType id;
	private AbstractType codice;
	private AbstractType descr;
	private AbstractType perc;
	private AbstractType tipologia;
	private AbstractType modalitaVersamento;
	private AbstractType importo;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Agevolazione(StringType numAgevolazione, StringType codAgevolazione,
						AbstractType tipo, AbstractType id, AbstractType codice, AbstractType descr, AbstractType perc,
						AbstractType tipologia, AbstractType modalitaVersamento, AbstractType importo){
		
		this.numAgevolazione = numAgevolazione;
		this.codAgevolazione = codAgevolazione;
		
		this.tipo = tipo;
		this.id = id;
		this.codice = codice;
		this.descr = descr;
		this.perc = perc;
	}

	public AbstractType getTipo() {
		return tipo;
	}

	public AbstractType getId() {
		return id;
	}

	public AbstractType getCodice() {
		return codice;
	}

	public AbstractType getDescr() {
		return descr;
	}

	public AbstractType getPerc() {
		return perc;
	}

	public AbstractType getTipologia() {
		return tipologia;
	}

	public AbstractType getModalitaVersamento() {
		return modalitaVersamento;
	}

	public AbstractType getImporto() {
		return importo;
	}

	public StringType getNumAgevolazione() {
		return numAgevolazione;
	}

	public void setNumAgevolazione(StringType numAgevolazione) {
		this.numAgevolazione = numAgevolazione;
	}

	public StringType getCodAgevolazione() {
		return codAgevolazione;
	}

	public void setCodAgevolazione(StringType codAgevolazione) {
		this.codAgevolazione = codAgevolazione;
	}

}
