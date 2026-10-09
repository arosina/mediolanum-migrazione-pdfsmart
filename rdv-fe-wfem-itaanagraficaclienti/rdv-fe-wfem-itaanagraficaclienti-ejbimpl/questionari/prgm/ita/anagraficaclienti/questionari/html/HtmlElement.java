package prgm.ita.anagraficaclienti.questionari.html;

import java.io.Serializable;
import java.util.Vector;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;

/*******************************************************************/
/*******************************************************************/
public abstract class HtmlElement implements Serializable{
	private transient Template template;
	private Vector elements = new Vector();
	private HtmlElementModel ele;
	private String text = "";
	private QuestionarioModel questionario;
	
	/*******************************************************************/
	/*******************************************************************/
	public HtmlElement(HtmlElementModel ele, Template template, QuestionarioModel questionario){
		this.ele = ele;
		this.template = template;
		this.questionario = questionario;
		if(ele != null)
			this.text = ele.getTestEle().toString();
	}
	
	/*******************************************************************/
	/*******************************************************************/
	void addElement(HtmlElement elem){
		elements.add(elem);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	String innerHtml(){
		StringBuffer result = new StringBuffer();
		for(int i=0;i<elements.size();i++){
			HtmlElement ele = (HtmlElement)elements.get(i);
			result.append(ele.getHtml());
		}
		return result.toString();
	}

	/*******************************************************************/
	/*******************************************************************/
	public abstract String getHtml();

	/*******************************************************************/
	/*******************************************************************/
	public HtmlElementModel getEle() {
		return ele;
	}

	/*******************************************************************/
	/*******************************************************************/
	public void addText(String text) {
		this.text += text.toString();
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getText() {
		return text;
	}

	public Template getTemplate() {
		return template;
	}

	public QuestionarioModel getQuestionario() {
		return questionario;
	}

	public Vector getElements() {
		return elements;
	}

}
