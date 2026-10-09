package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class SezioneFine extends HtmlElement {

	private int numSezione = 1;
	
	/*******************************************************************/
	/*******************************************************************/
	public SezioneFine(HtmlElementModel ele,Template template, QuestionarioModel questionario, int numSezione) {
		super(ele,template,questionario);
		this.numSezione = numSezione;
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		
		StringBuffer result = new StringBuffer();
		
		result.append("</td></tr></table>\n");
		result.append("</fieldset>\n");		
		result.append("</td></tr></table>\n");		
		result.append("<!-- FINE SEZIONE "+numSezione+" -->\n\n\n\n\n\n");
		
		return result.toString();
	}


}
