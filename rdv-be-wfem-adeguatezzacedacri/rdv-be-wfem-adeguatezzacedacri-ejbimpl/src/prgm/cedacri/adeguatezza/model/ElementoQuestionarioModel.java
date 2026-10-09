package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
/*
 * Elemento della lista che compone le informazioni del questionario 
 */
public class ElementoQuestionarioModel extends CommandDataModel implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento del Questionario
	 * tipElem : tipologia (sezione,commento,domanda,risposta)
	 * numElem : numero domanda nella sezione
	 * numSele : numero risposta della domanda
	 * testEle : testo da visualizzare
	 * selSele : se selezionato e tipo di selezione
	 */
	private StringType  tipElem = new StringType();
	private StringType  testEle = new StringType();
	private IntegerType numElem = new IntegerType();
	private IntegerType numSele = new IntegerType();
	private StringType  selSele = new StringType();
	
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
	public StringType getSelSele() {
		return selSele;
	}
	public void setSelSele(StringType selSele) {
		this.selSele = selSele;
	}
	public StringType getTestEle() {
		return testEle;
	}
	public void setTestEle(StringType testEle) {
		this.testEle = testEle;
	}
	public StringType getTipElem() {
		return tipElem;
	}
	public void setTipElem(StringType tipElem) {
		this.tipElem = tipElem;
	} 
}
