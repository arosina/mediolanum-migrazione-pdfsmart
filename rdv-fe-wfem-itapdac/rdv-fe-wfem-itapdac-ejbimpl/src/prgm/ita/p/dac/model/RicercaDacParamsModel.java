package prgm.ita.p.dac.model;

import prgm.ita.p.dac.facade.Costanti;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaDacParamsModel extends RicercaDocParamsModel {
	
	private StringType  	idDac = new StringType();
	
	private IntegerType   	tipoDac = new IntegerType();
	private IntegerType   	ubicazione = new IntegerType();
	private IntegerType   	stato = new IntegerType();
	private IntegerType   	esito = new IntegerType();
	
	private DateType   		dataRicezioneDocumenti = new DateType();
	
	private IntegerType   	uffMittente = new IntegerType();
	private StringType 		codUtenteMittente = new StringType();

	private IntegerType   	uffLavorazione = new IntegerType();
	private StringType 		codUtenteLavorazione = new StringType();
	
	private IntegerType   	uffSpunta = new IntegerType();
	private StringType 		codUtenteSpunta = new StringType();
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaDacParamsModel(){
		CodDescData d = null;
		CodDescDataList dl = new CodDescDataList();
		d = new CodDescData(); d.setCod(""+Costanti.TIPO_DAC_STANDARD); 	d.setDescr("Rete"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod(""+Costanti.TIPO_DAC_PERCONTORETE); d.setDescr("Sede per Rete"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod(""+Costanti.TIPO_DAC_RACCOMANDATE); d.setDescr("Raccomandate"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod(""+Costanti.TIPO_DAC_CARTOLINE); 	d.setDescr("Cartoline"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod(""+Costanti.TIPO_DAC_SEDE); 		d.setDescr("D.A.C."); dl.addCodDescData(d);
		addCodDescField("tipoDac",dl);
		
		addCodDescField("ubicazione","Uffici");
		addCodDescField("uffMittente","Uffici");
		addCodDescField("uffLavorazione","UfficiLavorazioneRicercaDac");
		addCodDescField("uffSpunta","UfficiSpuntaRicercaDac");
		addCodDescField("esito","EsitiDac");
	}
	
	public IntegerType getEsito() {
		return esito;
	}
	public void setEsito(IntegerType esito) {
		this.esito = esito;
	}
	public StringType getIdDac() {
		return idDac;
	}
	public void setIdDac(StringType idDac) {
		this.idDac = idDac;
	}
	public IntegerType getStato() {
		return stato;
	}
	public void setStato(IntegerType stato) {
		this.stato = stato;
	}
	public DateType getDataRicezioneDocumenti() {
		return dataRicezioneDocumenti;
	}
	public void setDataRicezioneDocumenti(DateType dataRicezioneDocumenti) {
		this.dataRicezioneDocumenti = dataRicezioneDocumenti;
	}
	public IntegerType getUffLavorazione() {
		return uffLavorazione;
	}
	public void setUffLavorazione(IntegerType uffLavorazione) {
		this.uffLavorazione = uffLavorazione;
	}

	public IntegerType getUffSpunta() {
		return uffSpunta;
	}

	public void setUffSpunta(IntegerType uffSpunta) {
		this.uffSpunta = uffSpunta;
	}

	public IntegerType getUffMittente() {
		return uffMittente;
	}

	public void setUffMittente(IntegerType uffMittente) {
		this.uffMittente = uffMittente;
	}

	public StringType getCodUtenteMittente() {
		return codUtenteMittente;
	}

	public void setCodUtenteMittente(StringType codUtenteMittente) {
		this.codUtenteMittente = codUtenteMittente;
	}

	public StringType getCodUtenteLavorazione() {
		return codUtenteLavorazione;
	}

	public void setCodUtenteLavorazione(StringType codUtenteLavorazione) {
		this.codUtenteLavorazione = codUtenteLavorazione;
	}

	public StringType getCodUtenteSpunta() {
		return codUtenteSpunta;
	}

	public void setCodUtenteSpunta(StringType codUtenteSpunta) {
		this.codUtenteSpunta = codUtenteSpunta;
	}

	public IntegerType getTipoDac() {
		return tipoDac;
	}

	public void setTipoDac(IntegerType tipoDac) {
		this.tipoDac = tipoDac;
	}

	public IntegerType getUbicazione() {
		return ubicazione;
	}

	public void setUbicazione(IntegerType ubicazione) {
		this.ubicazione = ubicazione;
	}

}
