package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
/*
 * Elemento della lista che compone le risposte del questionario 
 */
public class ElementoQuestionarioRisposteModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento delle Risposte del Questionario
	 * numElem : numero domanda nella sezione
	 * numSele : numero risposta della domanda
	 */
	private IntegerType numElem = new IntegerType();
	private IntegerType numSele = new IntegerType();
	
	/*
	 * Metodi Set e Get
	 */
	public IntegerType getNumElem() {
		return numElem;
	}
	public void setNumElem(IntegerType numElem) {
		this.numElem = numElem;
	}
	public IntegerType getNumSele() {
		return numSele;
	}
	public void setNumSele(IntegerType numSele) {
		this.numSele = numSele;
	}
}
