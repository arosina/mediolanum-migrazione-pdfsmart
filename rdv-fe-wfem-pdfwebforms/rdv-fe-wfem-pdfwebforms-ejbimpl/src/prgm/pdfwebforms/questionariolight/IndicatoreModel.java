package prgm.pdfwebforms.questionariolight;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class IndicatoreModel  extends CommandDataModel {
	
	private IntegerType idIndicatore = new IntegerType();
	private StringType 	nomeIndicatore = new StringType();
	private StringType 	descrizioneIndicatore = new StringType();
	private BooleanType flagQuestionarioLight = new BooleanType();
	private IntegerType idValoreIndicatore = new IntegerType();
	private StringType 	nomeValoreIndicatore = new StringType();
	
	public IntegerType getIdIndicatore() {
		return idIndicatore;
	}
	public void setIdIndicatore(IntegerType idIndicatore) {
		this.idIndicatore = idIndicatore;
	}
	public StringType getNomeIndicatore() {
		return nomeIndicatore;
	}
	public void setNomeIndicatore(StringType nomeIndicatore) {
		this.nomeIndicatore = nomeIndicatore;
	}
	public StringType getDescrizioneIndicatore() {
		return descrizioneIndicatore;
	}
	public void setDescrizioneIndicatore(StringType descrizioneIndicatore) {
		this.descrizioneIndicatore = descrizioneIndicatore;
	}
	public BooleanType getFlagQuestionarioLight() {
		return flagQuestionarioLight;
	}
	public void setFlagQuestionarioLight(BooleanType flagQuestionarioLight) {
		this.flagQuestionarioLight = flagQuestionarioLight;
	}
	public IntegerType getIdValoreIndicatore() {
		return idValoreIndicatore;
	}
	public void setIdValoreIndicatore(IntegerType idValoreIndicatore) {
		this.idValoreIndicatore = idValoreIndicatore;
	}
	public StringType getNomeValoreIndicatore() {
		return nomeValoreIndicatore;
	}
	public void setNomeValoreIndicatore(StringType nomeValoreIndicatore) {
		this.nomeValoreIndicatore = nomeValoreIndicatore;
	}
	
}
