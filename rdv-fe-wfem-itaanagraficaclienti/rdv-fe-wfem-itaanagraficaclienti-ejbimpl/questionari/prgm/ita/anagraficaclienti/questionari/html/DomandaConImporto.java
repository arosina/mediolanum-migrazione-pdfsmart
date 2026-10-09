package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;

/*******************************************************************/
/*******************************************************************/
public class DomandaConImporto extends AbstractDomanda {
	
	RispostaReference rispostaSempliceRef = null;
	
	/*******************************************************************/
	/*******************************************************************/
	public DomandaConImporto(HtmlElementModel ele,Template template, QuestionarioModel questionario, int numSezione, int numDomandaInSezione) {
		super(ele,template,questionario,numSezione,numDomandaInSezione);
	}

	/*******************************************************************/
	/*******************************************************************/
	void addElement(HtmlElement elem) {
		super.addElement(elem);
	}

	public RispostaReference getRispostaSempliceRef() {
		return rispostaSempliceRef;
	}

	public void setRispostaSempliceRef(RispostaReference rispostaSempliceRef) {
		this.rispostaSempliceRef = rispostaSempliceRef;
	}
	
	public String getHtml() {
		StringBuffer result = new StringBuffer();
		
		// Domande 
		result.append("<table width='100%' class='text' style='table-layout:fixed;' id='domanda"+getEle().getNumElem()+"'>\n");
		result.append("<tr>");
		result.append("  <td width='60%' valign='top'>");
		result.append("    <table class='text' width='100%' cellpadding='10'><tr>");
		result.append("      <td>"+getText()+"</td>");
		if(getQuestionario().getDomandaInErrore().intValue() == getEle().getNumElem().intValue())
			result.append("<td>&nbsp;&nbsp;&nbsp;"+getTemplate().inlineAnchor("domandaInErrore")+"</td>");
		result.append("    </tr></table>");
		result.append("  </td>");
		// Risposte
		result.append("  <td width='40%' valign='top'>");
		result.append("    <table class='text' width='100%'  ><tr><td>");
		
		int rows = getElements().size() / cols;
		if((getElements().size() % cols) > 0)
			rows += getElements().size() % cols;
			
		int rispCount = 0;
		result.append("      <table width='100%' class='text' cellspacing='0' cellpadding='0' id='domanda"+getEle().getNumElem()+"'><tr>\n");
		for(int i=0;i<cols;i++){
			result.append("      <td><table width='100%' class='text' cellspacing='0' cellpadding='0'>");
			for(int j=0;j<rows;j++){
				result.append("        <tr>");
				if(rispCount < getElements().size())
					result.append("        <td>"+((HtmlElement)getElements().get(rispCount++)).getHtml()+"</td>");										
				else
					result.append("        <td>&nbsp;</td>");					
				result.append("        </tr>");
			}
			result.append("      </table></td>");
		}
		
		result.append("    </td></tr></table>");
		result.append("  </td>");
		result.append("</tr>");
		result.append("</table>");
		result.append("   </td>\n");
		result.append("</tr>\n");
		result.append("</table>\n");
		return result.toString();
	}

}
