package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class RispostaMultipla extends AbstractRisposta {
	
	/*******************************************************************/
	/*******************************************************************/
	public RispostaMultipla(HtmlElementModel ele,Template template, DomandaMultipla parent, QuestionarioModel questionario) {
		super(ele,template,parent,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		String disabled = "";
		if(getQuestionario().isSalvato())
			disabled = " disabled ";
		String checked = "";
		String value = "N";
		if(getEle().getSelSele().equals("S")){
			checked = " checked ";
			value = "S";
		}

		String propName = "risposte"+getRispostaRef().getRispostaIdx()+"_rispostaModel_selSele";
		StringBuffer result = new StringBuffer();

		result.append("<table class='rispostaMultipla'><tr>");
		result.append("<td>");
		result.append("<input "+checked+" type='checkbox' "+disabled+
                                  "name='risposte"+getRispostaRef().getRispostaIdx()+"_rispostaModel_selSeleCheck' "+
                                  "propname='"+propName+"' "+
                                  "rispostaIdx='"+getRispostaRef().getRispostaIdx()+"' "+
                                  "idDomanda='domanda"+getParent().getEle().getNumElem()+"' "+
                                  "onclick='manageRisposteMultiple(this);'>");
		result.append("<input type='hidden' id='"+propName+"' name='"+propName+"' value='"+value+"'>");
		result.append("</td>");
		result.append("<td>"+getText()+"</td>");
		result.append("</tr></table>");
		return result.toString();
	}
}
