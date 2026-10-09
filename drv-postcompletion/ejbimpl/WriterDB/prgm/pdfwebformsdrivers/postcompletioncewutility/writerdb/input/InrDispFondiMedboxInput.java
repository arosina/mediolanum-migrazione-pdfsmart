package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispFondiMedboxInput extends CommandDataModel{

	public static final String TIPOFONDO_PREMIO_UNICO		= "PU";
	public static final String TIPOFONDO_PIC_PROGRAMMATO	= "PP";
	public static final String TIPOFONDO_PAC				= "PC";
	public static final String TIPOFONDO_BIG_CHANCE			= "BC";
	public static final String TIPOFONDO_IIS				= "IS";
	public static final String TIPOFONDO_PARTENZA_SWITCH	= "FP";
	public static final String TIPOFONDO_DESTINAZIONE_SWITCH = "FD";
	
	private StringType codDisposizione		= new StringType();
	private StringType codAgente			= new StringType();
	private StringType codRete				= new StringType();
	private StringType tipoFondo            = new StringType();
	private StringType isinFondo   		    = new StringType();
	private DoubleType percentuale			= new DoubleType();
	private DoubleType importo				= new DoubleType();
	private StringType isinFondoConsRend	= new StringType();
	private DoubleType percIncrQuota		= new DoubleType();
	private StringType flagReinvSuCali		= new StringType();
	private DoubleType quote				= new DoubleType();
	private StringType tipoOperazione		= new StringType();
	
	
	/*********************************************************************************/
	/*********************************************************************************/
	public StringType getTipoOperazione() {
		return tipoOperazione;
	}
	public void setTipoOperazione(StringType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getTipoFondo() {
		return tipoFondo;
	}
	public void setTipoFondo(StringType tipoFondo) {
		this.tipoFondo = tipoFondo;
	}
	public DoubleType getPercentuale() {
		return percentuale;
	}
	public void setPercentuale(DoubleType percentuale) {
		this.percentuale = percentuale;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public DoubleType getPercIncrQuota() {
		return percIncrQuota;
	}
	public void setPercIncrQuota(DoubleType percIncrQuota) {
		this.percIncrQuota = percIncrQuota;
	}
	public StringType getFlagReinvSuCali() {
		return flagReinvSuCali;
	}
	public void setFlagReinvSuCali(StringType flagReinvSuCali) {
		this.flagReinvSuCali = flagReinvSuCali;
	}
	public StringType getIsinFondo() {
		return isinFondo;
	}
	public void setIsinFondo(StringType isinFondo) {
		this.isinFondo = isinFondo;
	}
	public StringType getIsinFondoConsRend() {
		return isinFondoConsRend;
	}
	public void setIsinFondoConsRend(StringType isinFondoConsRend) {
		this.isinFondoConsRend = isinFondoConsRend;
	}
	public DoubleType getQuote() {
		return quote;
	}
	public void setQuote(DoubleType quote) {
		this.quote = quote;
	}
}