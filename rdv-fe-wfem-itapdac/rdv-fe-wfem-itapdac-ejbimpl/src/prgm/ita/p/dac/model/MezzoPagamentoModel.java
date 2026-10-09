package prgm.ita.p.dac.model;

import java.util.Vector;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.p.dac.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class MezzoPagamentoModel extends DocumentoKeyModel implements GridDecorator{
	
	private static final String S_FLAGTRASFERIBILE = "flagTrasferibile";
	
	private BooleanType isEditabile = new BooleanType();
	
	private IntegerType idMezzoPg = new IntegerType();

	private IntegerType codTipoPagamento = new IntegerType();
	private IntegerType codTipoPagamentoSuDB = new IntegerType();
	private DoubleType  importo = new DoubleType();
	private StringType  codDivisa = new StringType();
	private StringType  numAssegno = new StringType();
	private StringType  banca = new StringType();
	private StringType  luogoEmissione = new StringType();
	private DateType  	dataEmissione = new DateType();
	private StringType  flagTrasferibile = new StringType();
	private StringType  beneficiariAssegno = new StringType();
	
	// Legame con il documento in caso di assgeno 
	private StringType  idDocumentoAssegno = new StringType();

	// Esito del documento collegato in caso di assegno (Gli aggiunti si possono editare in spunta) 
	private IntegerType  esitoDocumentoAssegno = new IntegerType();

	// Per layout/gestione interna
	private Vector 			changedProps = new Vector();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public MezzoPagamentoModel(){
		addCodDescField("codTipoPagamento","TipiPagamento");
		
		CodDescDataList dl = new CodDescDataList();
		CodDescData d = null;
		d=new CodDescData(); d.setCod(Costanti.COD_DIVISA_EURO); 		d.setDescr("Euro"); 									dl.addCodDescData(d); 
		d=new CodDescData(); d.setCod(Costanti.COD_DIVISA_LIRE); 		d.setDescr("Lira"); 				d.setValid(false); 	dl.addCodDescData(d);  
		d=new CodDescData(); d.setCod(Costanti.COD_DIVISA_STERLINA); 	d.setDescr("Sterlina Inglese"); 						dl.addCodDescData(d); 
		d=new CodDescData(); d.setCod(Costanti.COD_DIVISA_DOLLARO); 	d.setDescr("Dollaro Statunitense"); 					dl.addCodDescData(d); 
		d=new CodDescData(); d.setCod(Costanti.COD_DIVISA_ALTRO);   	d.setDescr("Altra Valuta"); 							dl.addCodDescData(d); 
		addCodDescField("codDivisa",dl);

		dl = new CodDescDataList();
		d=new CodDescData(); d.setCod("S"); d.setDescr("Si"); dl.addCodDescData(d); 
		d=new CodDescData(); d.setCod("N"); d.setDescr("No"); dl.addCodDescData(d); 
		addCodDescField(S_FLAGTRASFERIBILE,dl);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Vector initChangedProps(MezzoPagamentoModel other){
		changedProps.removeAllElements();
		
		if(!getCodTipoPagamento().toString().equals(other.getCodTipoPagamento().toString()))
			changedProps.add("codTipoPagamento");
		if(!getImporto().toString().equals(other.getImporto().toString()))
			changedProps.add("importo");
		if(!getCodDivisa().toString().equals(other.getCodDivisa().toString()))
			changedProps.add("codDivisa");
		if(!getNumAssegno().toString().equals(other.getNumAssegno().toString()))
			changedProps.add("numAssegno");
		if(!getBanca().toString().equals(other.getBanca().toString()))
			changedProps.add("banca");
		if(!getLuogoEmissione().toString().equals(other.getLuogoEmissione().toString()))
			changedProps.add("luogoEmissione");
		if(!getDataEmissione().toString().equals(other.getDataEmissione().toString()))
			changedProps.add("dataEmissione");
		if(!getFlagTrasferibile().toString().equals(other.getFlagTrasferibile().toString()))
			changedProps.add(S_FLAGTRASFERIBILE);
		return changedProps;		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(MezzoPagamentoModel other){
		if(!getCodTipoPagamento().toString().equals(other.getCodTipoPagamento().toString()))
			return false;
		if(!getImporto().toString().equals(other.getImporto().toString()))
			return false;
		if(!getCodDivisa().toString().equals(other.getCodDivisa().toString()))
			return false;
		if(!getNumAssegno().toString().equals(other.getNumAssegno().toString()))
			return false;
		if(!getBanca().toString().equals(other.getBanca().toString()))
			return false;
		if(!getLuogoEmissione().toString().equals(other.getLuogoEmissione().toString()))
			return false;
		if(!getDataEmissione().toString().equals(other.getDataEmissione().toString()))
			return false;
		if(!getFlagTrasferibile().toString().equals(other.getFlagTrasferibile().toString()))
			return false;
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getChangedProps(){
		String res = "";
		for(int i=0;i<changedProps.size();i++)
			res += changedProps.get(i)+"|";
		return new StringType(res);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,	CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		if(cellPropertyName.equals("codTipoPagamento") || cellPropertyName.equals(S_FLAGTRASFERIBILE)){
			MezzoPagamentoModel mpg = (MezzoPagamentoModel)row;
			FieldStyle fs = new FieldStyle();
			fs.extraParameters = "absIndex="+rowIndex+" originalCodTipoPagamento='"+mpg.getCodTipoPagamento()+"'";
			cell.setStyle(fs);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public IntegerType getCodTipoPagamentoPerMOM(){
		if(getCodTipoPagamento().intValue() == Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO)
			return new IntegerType(Costanti.MEZZO_PAGAMENTO_ASSEGNO);
		return getCodTipoPagamento();
	}
	
	public IntegerType getIdMezzoPg() {
		return idMezzoPg;
	}

	public void setIdMezzoPg(IntegerType idMezzoPg) {
		this.idMezzoPg = idMezzoPg;
	}

	public IntegerType getCodTipoPagamento() {
		return codTipoPagamento;
	}

	public void setCodTipoPagamento(IntegerType codTipoPagamento) {
		this.codTipoPagamento = codTipoPagamento;
	}

	public DoubleType getImporto() {
		return importo;
	}

	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}

	public StringType getCodDivisa() {
		return codDivisa;
	}

	public void setCodDivisa(StringType codDivisa) {
		this.codDivisa = codDivisa;
	}

	public StringType getNumAssegno() {
		return numAssegno;
	}

	public void setNumAssegno(StringType numAssegno) {
		this.numAssegno = numAssegno;
	}

	public StringType getBanca() {
		return banca;
	}

	public void setBanca(StringType banca) {
		this.banca = banca;
	}

	public StringType getIdDocumentoAssegno() {
		return idDocumentoAssegno;
	}

	public void setIdDocumentoAssegno(StringType idDocumentoAssegno) {
		this.idDocumentoAssegno = idDocumentoAssegno;
	}

	public IntegerType getEsitoDocumentoAssegno() {
		return esitoDocumentoAssegno;
	}

	public void setEsitoDocumentoAssegno(IntegerType esitoDocumentoAssegno) {
		this.esitoDocumentoAssegno = esitoDocumentoAssegno;
	}

	public BooleanType getIsEditabile() {
		return isEditabile;
	}

	public void setIsEditabile(BooleanType isEditabile) {
		this.isEditabile = isEditabile;
	}

	public IntegerType getCodTipoPagamentoSuDB() {
		return codTipoPagamentoSuDB;
	}

	public void setCodTipoPagamentoSuDB(IntegerType codTipoPagamentoSuDB) {
		this.codTipoPagamentoSuDB = codTipoPagamentoSuDB;
	}

	public StringType getLuogoEmissione() {
		return luogoEmissione;
	}

	public void setLuogoEmissione(StringType luogoEmissione) {
		this.luogoEmissione = luogoEmissione;
	}

	public DateType getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(DateType dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public StringType getFlagTrasferibile() {
		return flagTrasferibile;
	}

	public void setFlagTrasferibile(StringType flagTrasferibile) {
		this.flagTrasferibile = flagTrasferibile;
	}

	public StringType getBeneficiariAssegno() {
		return beneficiariAssegno;
	}

	public void setBeneficiariAssegno(StringType beneficiariAssegno) {
		this.beneficiariAssegno = beneficiariAssegno;
	}
}
