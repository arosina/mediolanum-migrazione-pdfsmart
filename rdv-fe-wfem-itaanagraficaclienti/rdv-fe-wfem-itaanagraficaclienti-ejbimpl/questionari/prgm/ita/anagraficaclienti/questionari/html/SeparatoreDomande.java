package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;



/*******************************************************************/
/*******************************************************************/
public class SeparatoreDomande extends HtmlElement {
	
	/*******************************************************************/
	/*******************************************************************/
	public SeparatoreDomande(HtmlElementModel ele,Template template, QuestionarioModel questionario) {
		super(ele,template,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		StringBuffer result = new StringBuffer();
		result.append("<table width='100%'><tr><td class='risposteSeparator'></td></tr></table>\n");
		return result.toString();
	}
}
