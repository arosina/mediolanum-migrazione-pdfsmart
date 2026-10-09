package prgm.pdfwebforms.questionariolight;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RispostaModel  extends CommandDataModel {
	
	private IntegerType idRisposta = new IntegerType();
	private StringType 	valoreRisposta = new StringType();
	private StringType 	descrizioneRisposta = new StringType();
	private BooleanType flagSelezionabile = new BooleanType();
	
	public IntegerType getIdRisposta() {
		return idRisposta;
	}
	public void setIdRisposta(IntegerType idRisposta) {
		this.idRisposta = idRisposta;
	}
	public StringType getValoreRisposta() {
		return valoreRisposta;
	}
	public void setValoreRisposta(StringType valoreRisposta) {
		this.valoreRisposta = valoreRisposta;
	}
	public StringType getDescrizioneRisposta() {
		return descrizioneRisposta;
	}
	public void setDescrizioneRisposta(StringType descrizioneRisposta) {
		this.descrizioneRisposta = descrizioneRisposta;
	}
	public BooleanType getFlagSelezionabile() {
		return flagSelezionabile;
	}
	public void setFlagSelezionabile(BooleanType flagSelezionabile) {
		this.flagSelezionabile = flagSelezionabile;
	}
	
	
}
