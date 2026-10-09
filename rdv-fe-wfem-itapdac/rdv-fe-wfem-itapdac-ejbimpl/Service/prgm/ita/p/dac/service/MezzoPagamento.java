package prgm.ita.p.dac.service;

import java.io.Serializable;
import java.util.Date;

import prgm.ita.p.dac.facade.Costanti;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class MezzoPagamento implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static String COD_DIVISA_EURO		= Costanti.COD_DIVISA_EURO;
	public static String COD_DIVISA_LIRE		= Costanti.COD_DIVISA_LIRE;
	public static String COD_DIVISA_STERLINA	= Costanti.COD_DIVISA_STERLINA;
	public static String COD_DIVISA_DOLLARO		= Costanti.COD_DIVISA_DOLLARO;
	public static String COD_DIVISA_ALTRO		= Costanti.COD_DIVISA_ALTRO;

	public static int COD_TIPO_PAGAMENTO_ASSEGNO = Costanti.MEZZO_PAGAMENTO_ASSEGNO; 
	public static int COD_TIPO_PAGAMENTO_ASSEGNO_ESTERO = Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO; 
	
	private int 	codTipoPagamento;
	private double  importo;
	private String  codDivisa = "EUR";
	private String  numAssegno;
	private String  banca;
	private String  luogoEmissione;
	private Date  	dataEmissione;
	private String  flagTrasferibile;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void fillDocModel(prgm.ita.p.dac.model.MezzoPagamentoModel mezzoPg){
		mezzoPg.setCodTipoPagamento(new IntegerType(getCodTipoPagamento()));
		mezzoPg.setImporto(new DoubleType(getImporto()));
		mezzoPg.setCodDivisa(new StringType(getCodDivisa()));
		mezzoPg.setNumAssegno(new StringType(getNumAssegno()));
		mezzoPg.setBanca(new StringType(getBanca()));
		mezzoPg.setLuogoEmissione(new StringType(getLuogoEmissione()));
		mezzoPg.setDataEmissione(new DateType(getDataEmissione()));
		mezzoPg.setFlagTrasferibile(new StringType(getFlagTrasferibile()));
	}

	public int getCodTipoPagamento() {
		return codTipoPagamento;
	}

	public void setCodTipoPagamento(int codTipoPagamento) {
		this.codTipoPagamento = codTipoPagamento;
	}

	public double getImporto() {
		return importo;
	}

	public void setImporto(double importo) {
		this.importo = importo;
	}

	public String getCodDivisa() {
		return codDivisa;
	}

	public void setCodDivisa(String codDivisa) {
		this.codDivisa = codDivisa;
	}

	public String getNumAssegno() {
		return numAssegno;
	}

	public void setNumAssegno(String numAssegno) {
		this.numAssegno = numAssegno;
	}

	public String getBanca() {
		return banca;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public String getLuogoEmissione() {
		return luogoEmissione;
	}

	public void setLuogoEmissione(String luogoEmissione) {
		this.luogoEmissione = luogoEmissione;
	}

	public Date getDataEmissione() {
		return dataEmissione;
	}

	public void setDataEmissione(Date dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public String getFlagTrasferibile() {
		return flagTrasferibile;
	}

	public void setFlagTrasferibile(String flagTrasferibile) {
		this.flagTrasferibile = flagTrasferibile;
	}
	
}
