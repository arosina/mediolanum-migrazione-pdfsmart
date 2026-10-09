package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class MomEventDataModel extends CommandDataModel {

	public static String CONFRONTA_DOPPIA_SPUNTA_SENZA_EVIDENZE = "S";
	public static String CONFRONTA_DOPPIA_SPUNTA_CON_EVIDENZE 	= "V";
	
	private StringType	azioneMom = new StringType();
	private StringType	readonly = new StringType();
	private StringType	valida = new StringType();
	private StringType	salva = new StringType();
	private StringType	aggiornaDispositiva = new StringType();
	private StringType	gotoPdf = new StringType();
	private StringType	idPratica = new StringType();
	private StringType	confrontaDoppiaSpunta = new StringType();
	private StringType	esitoConfrontoDoppiaSpunta = new StringType();
	private ListType	parametri = new ListType(MomEventParamModel.class);

	public StringType getAzioneMom() {
		return azioneMom;
	}

	public void setAzioneMom(StringType azioneMom) {
		this.azioneMom = azioneMom;
	}

	public ListType getParametri() {
		return parametri;
	}

	public void setParametri(ListType parametri) {
		this.parametri = parametri;
	}

	public StringType getReadonly() {
		return readonly;
	}

	public void setReadonly(StringType readonly) {
		this.readonly = readonly;
	}

	public StringType getValida() {
		return valida;
	}

	public void setValida(StringType valida) {
		this.valida = valida;
	}

	public StringType getSalva() {
		return salva;
	}

	public void setSalva(StringType salva) {
		this.salva = salva;
	}

	public StringType getAggiornaDispositiva() {
		return aggiornaDispositiva;
	}

	public void setAggiornaDispositiva(StringType aggiornaDispositiva) {
		this.aggiornaDispositiva = aggiornaDispositiva;
	}

	public StringType getGotoPdf() {
		return gotoPdf;
	}

	public void setGotoPdf(StringType gotoPdf) {
		this.gotoPdf = gotoPdf;
	}

	public StringType getIdPratica() {
		return idPratica;
	}

	public void setIdPratica(StringType idPratica) {
		this.idPratica = idPratica;
	}

	public StringType getConfrontaDoppiaSpunta() {
		return confrontaDoppiaSpunta;
	}

	public void setConfrontaDoppiaSpunta(StringType confrontaDoppiaSpunta) {
		this.confrontaDoppiaSpunta = confrontaDoppiaSpunta;
	}

	public StringType getEsitoConfrontoDoppiaSpunta() {
		return esitoConfrontoDoppiaSpunta;
	}

	public void setEsitoConfrontoDoppiaSpunta(StringType esitoConfrontoDoppiaSpunta) {
		this.esitoConfrontoDoppiaSpunta = esitoConfrontoDoppiaSpunta;
	}

}
