package prgm.ita.anagraficaclienti.questionari.html;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class HtmlElementModel extends CommandDataModel {
	
	private StringType  tipElem = new StringType();
	private StringType  testEle = new StringType();
	private IntegerType numElem = new IntegerType();
	private IntegerType numSele = new IntegerType();
	private StringType  selSele = new StringType();
	
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
