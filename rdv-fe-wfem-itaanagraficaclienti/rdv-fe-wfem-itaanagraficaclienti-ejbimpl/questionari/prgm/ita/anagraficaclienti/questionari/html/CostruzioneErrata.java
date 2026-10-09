package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class CostruzioneErrata extends HtmlElement {

	String errorMsg;
	
	/*******************************************************************/
	/*******************************************************************/
	public CostruzioneErrata(HtmlElementModel ele,Template template, QuestionarioModel questionario) {
		super(null,template,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public CostruzioneErrata(String errorMsg,Template template, QuestionarioModel questionario) {
		super(null,template,questionario);
		this.errorMsg = errorMsg;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		return "<table><tr><td>"+errorMsg+"</td></tr></table>\n";
	}

}
