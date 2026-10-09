package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class Commento extends HtmlElement {

	/*******************************************************************/
	/*******************************************************************/
	public Commento(HtmlElementModel ele,Template template, QuestionarioModel questionario) {
		super(ele,template,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		return "<table width='100%' class='commento' id='commento"+getEle().getNumElem()+"'><tr><td>"+getText()+"</td></tr></table>\n";
	}

}
