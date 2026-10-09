package prgm.pdfwebforms.drivers.io.prit;

import java.util.Date;

import com.atosorigin.wfem.types.DateType;

import prgm.ita.p.dac.service.MezzoPagamento;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class MezzoPagamentoRigaPrit{
	
	public static int ASSEGNO_ITALIA 			= 1;
	public static int BONIFICO_BANCA_MEDIOLANUM = 2;
	public static int BONIFICO_ALTRA_BANCA 		= 3;
	public static int SWITCH_TRA_FONDI_IRLANDA 	= 4;
	public static int SWITCH_DA_FONDI_ITALIA 	= 7;
	public static int DISINVESTIMENTO_PARZIALE 	= 8;
	public static int DISINVESTIMENTO_TOTALE 	= 9;
	public static int DISINVESTIMENTO 			= 10;
	public static int ASSEGNO_ESTERO 			= 11;
	
	private int 		codTipoPagamento;
	private double  	importo;
	private String  	codDivisa = "EUR";
	private String  	numAssegno;
	private String 		banca;
	private String  	luogoEmissione;
	private DateType  	dataEmissione = new DateType();
	private String	 	trasferibile; // S=Si, N=No, null per i non assegni
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public MezzoPagamento transformToDac(){
		MezzoPagamento m = new MezzoPagamento();
		
		m.setCodTipoPagamento(getCodTipoPagamento());
		m.setImporto(getImporto());
		m.setCodDivisa(getCodDivisa());
		m.setNumAssegno(getNumAssegno());
		m.setBanca(getBanca());
		m.setLuogoEmissione(getLuogoEmissione());
		if(!getDataEmissione().isNull())
			m.setDataEmissione(new Date(getDataEmissione().dateValue().getTime()));
		m.setFlagTrasferibile(getTrasferibile());
		return m;
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
	public DateType getDataEmissione() {
		return dataEmissione;
	}
	public void setDataEmissione(DateType dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public String getTrasferibile() {
		return trasferibile;
	}

	public void setTrasferibile(String trasferibile) {
		this.trasferibile = trasferibile;
	}

}
