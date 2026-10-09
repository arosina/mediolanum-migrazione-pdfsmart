package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class DocumentiRicevutiModel extends CommandDataModel{
	
	private StringType ufficio	 =new StringType();
	private DateType   dataInizio=new DateType();
	private DateType   dataFine	 =new  DateType();
	private IntegerType codOperazione=new IntegerType();
	private IntegerType codProdotto =new IntegerType();
	
	
	public DocumentiRicevutiModel(){
		addCodDescField("codOperazione","OperazioniProdotto");
		addCodDescField("codProdotto","Prodotti");
		addCodDescField("ufficio","UfficiLavorazioneRicercaDac");
	}
	
	public StringType getUfficio() {
		return ufficio;
	}
	public void setUfficio(StringType ufficio) {
		this.ufficio = ufficio;
	}
	public DateType getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}
	public DateType getDataFine() {
		return dataFine;
	}
	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}
	public IntegerType getCodOperazione() {
		return codOperazione;
	}
	public void setCodOperazione(IntegerType codOperazione) {
		this.codOperazione = codOperazione;
	}
	public IntegerType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(IntegerType codProdotto) {
		this.codProdotto = codProdotto;
	}
	
	

}
