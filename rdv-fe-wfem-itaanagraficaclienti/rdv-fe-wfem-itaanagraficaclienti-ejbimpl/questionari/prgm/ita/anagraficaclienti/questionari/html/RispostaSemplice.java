package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class RispostaSemplice extends AbstractRisposta {
	
	/*******************************************************************/
	/*******************************************************************/
	public RispostaSemplice(HtmlElementModel ele,Template template, DomandaSemplice parent, QuestionarioModel questionario) {
		super(ele,template,parent,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		String disabled = "";
		if(getQuestionario().isSalvato())
			disabled = " disabled ";
		String checked = "";
		if(getEle().getSelSele().equals("S"))
			checked = " checked ";
		
		StringBuffer result = new StringBuffer();
		result.append("<table class='rispostaSemplice'><tr>");
		result.append("<td><input "+checked+" type='radio' value='"+getEle().getNumSele()+"' "+disabled+
										  "name='risposte"+getRispostaRef().getRispostaIdx()+"_rispostaModel_numSele'></td>");
		result.append("<td>"+getText()+"</td>");
		result.append("</tr></table>");
		return result.toString();
	}
	
}
