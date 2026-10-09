package prgm.pdfwebforms.questionariolight;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DomandaModel  extends CommandDataModel {
	
	private IntegerType idDomanda = new IntegerType();
	private StringType 	nomeDomanda = new StringType();
	private StringType 	descrizioneDomanda = new StringType();
	private BooleanType flagQuestionarioLight = new BooleanType();
	private BooleanType flagReadOnly = new BooleanType();
	private BooleanType flagDaValidare = new BooleanType();
	private BooleanType flagHaRisposteSelezionabili = new BooleanType();
	private ListType    risposte = new ListType(RispostaModel.class);
	
	public IntegerType getIdDomanda() {
		return idDomanda;
	}
	public void setIdDomanda(IntegerType idDomanda) {
		this.idDomanda = idDomanda;
	}
	public StringType getNomeDomanda() {
		return nomeDomanda;
	}
	public void setNomeDomanda(StringType nomeDomanda) {
		this.nomeDomanda = nomeDomanda;
	}
	public StringType getDescrizioneDomanda() {
		return descrizioneDomanda;
	}
	public void setDescrizioneDomanda(StringType descrizioneDomanda) {
		this.descrizioneDomanda = descrizioneDomanda;
	}
	public BooleanType getFlagQuestionarioLight() {
		return flagQuestionarioLight;
	}
	public void setFlagQuestionarioLight(BooleanType flagQuestionarioLight) {
		this.flagQuestionarioLight = flagQuestionarioLight;
	}
	public BooleanType getFlagReadOnly() {
		return flagReadOnly;
	}
	public void setFlagReadOnly(BooleanType flagReadOnly) {
		this.flagReadOnly = flagReadOnly;
	}
	public BooleanType getFlagDaValidare() {
		return flagDaValidare;
	}
	public void setFlagDaValidare(BooleanType flagDaValidare) {
		this.flagDaValidare = flagDaValidare;
	}
	public BooleanType getFlagHaRisposteSelezionabili() {
		return flagHaRisposteSelezionabili;
	}
	public void setFlagHaRisposteSelezionabili(BooleanType flagHaRisposteSelezionabili) {
		this.flagHaRisposteSelezionabili = flagHaRisposteSelezionabili;
	}
	public ListType getRisposte() {
		return risposte;
	}
	public void setRisposte(ListType risposte) {
		this.risposte = risposte;
	}
	
	
}
