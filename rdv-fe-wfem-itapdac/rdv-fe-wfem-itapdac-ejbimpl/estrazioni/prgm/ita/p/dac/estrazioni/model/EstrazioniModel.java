package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;


public class EstrazioniModel extends CommandDataModel{
	
	private DateType dataInizio=new DateType();
	private DateType dataFine  =new DateType();
	
	private IntegerType codOperazione=new IntegerType();
	private IntegerType codProdotto=new IntegerType();
	
	//Tot per ogni ufficio
	private StringType service=new StringType();
	private IntegerType totDocumenti=new IntegerType();
	
	private StringType  operazione=new StringType();
	private StringType  prodotto=new StringType();

	//dettagli
	private DettaglioModel dettagli=new DettaglioModel(); 	
	
	public EstrazioniModel(){
		addCodDescField("codOperazione","OperazioniProdotto");
		addCodDescField("codProdotto","Prodotti");
	}
	
	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}
	public DateType getDataInizio() {
		return dataInizio;
	}
	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}
	public DateType getDataFine() {
		return dataFine;
	}
	public void setOperazione(StringType operazione) {
		this.operazione = operazione;
	}
	public StringType getOperazione() {
		return operazione;
	}
	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}
	public StringType getProdotto() {
		return prodotto;
	}

	public void setCodOperazione(IntegerType codOperazione) {
		this.codOperazione = codOperazione;
	}

	public IntegerType getCodOperazione() {
		return codOperazione;
	}

	public void setCodProdotto(IntegerType codProdotto) {
		this.codProdotto = codProdotto;
	}

	public IntegerType getCodProdotto() {
		return codProdotto;
	}

	public StringType getService() {
		return service;
	}

	public void setService(StringType service) {
		this.service = service;
	}

	public IntegerType getTotDocumenti() {
		return totDocumenti;
	}

	public void setTotDocumenti(IntegerType totDocumenti) {
		this.totDocumenti = totDocumenti;
	}

	public void setDettagli(DettaglioModel dettagli) {
		this.dettagli = dettagli;
	}

	public DettaglioModel getDettagli() {
		return dettagli;
	}

	
}
