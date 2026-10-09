package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class CommentoPunteggio extends HtmlElement {

	/*******************************************************************/
	/*******************************************************************/
	public CommentoPunteggio(HtmlElementModel ele,Template template, QuestionarioModel questionario) {
		super(ele,template,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		return "<table width='100%' class='text' id='commentopunteggio"+getEle().getNumElem()+"'><tr><td>"+getText()+"</td></tr></table>\n";
	}

}
