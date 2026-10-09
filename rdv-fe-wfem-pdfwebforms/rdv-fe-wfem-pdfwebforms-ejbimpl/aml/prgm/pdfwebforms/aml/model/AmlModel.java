package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AmlModel extends CommandDataModel{
	
	public static String ERROR_IMG = "<img src='/wfemlayout/wlt/images/typeError.png'>";
	public static String OK_IMG = "<img src='/wfemlayout/images/debug.png'>";
	
	private StringType tabSelezionato = new StringType();
	
	private OrigineModel 	origine = new OrigineModel();
	private RelazioniModel 	relazioni = new RelazioniModel();
	private NaturaModel 	natura = new NaturaModel();
	
	private String errorMsg = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasAML() {
		return hasOrigine() || hasRelazioni() || hasNatura();
	}
	public boolean hasOrigine() {
		return !getOrigine().getElencoDispositive().isEmpty();
	}
	public boolean hasRelazioni() {
		return !getRelazioni().getElencoDispositive().isEmpty();
	}
	public boolean hasNatura() {
		return !getNatura().getElencoDispositive().isEmpty();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel findPdfInAml(PdfModel pdf) {
		PdfModel res = getNatura().findPdf(pdf);
		if(res != null)
			return res;
		res = getOrigine().findPdf(pdf);
		if(res != null)
			return res;
		res = getRelazioni().findPdf(pdf);
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clearScrollPos() {
		getNatura().setElencoSinistraContScrollPos(new IntegerType(0));
		
		getOrigine().setElencoSinistraContScrollPos(new IntegerType(0));
		getOrigine().setElencoDestraContScrollPos(new IntegerType(0));

		getRelazioni().setElencoSinistraContScrollPos(new IntegerType(0));
		getRelazioni().setElencoDestraContScrollPos(new IntegerType(0));
	}
	
	public StringType getTabSelezionato() {
		return tabSelezionato;
	}
	public void setTabSelezionato(StringType tabSelezionato) {
		this.tabSelezionato = tabSelezionato;
	}
	public OrigineModel getOrigine() {
		return origine;
	}
	public void setOrigine(OrigineModel origine) {
		this.origine = origine;
	}
	public RelazioniModel getRelazioni() {
		return relazioni;
	}
	public void setRelazioni(RelazioniModel relazioni) {
		this.relazioni = relazioni;
	}
	public NaturaModel getNatura() {
		return natura;
	}
	public void setNatura(NaturaModel natura) {
		this.natura = natura;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	

}
