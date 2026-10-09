package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;

/*******************************************************************/
/*******************************************************************/
public abstract class AbstractDomanda extends HtmlElement {
	
	protected int cols = 1;
	private int curCol = 0;
	private int numSezione = 1;
	private int numDomandaInSezione = 1;
	
	/*******************************************************************/
	/*******************************************************************/
	public AbstractDomanda(HtmlElementModel ele,Template template, QuestionarioModel questionario, int numSezione, int numDomandaInSezione) {
		super(ele,template,questionario);
		this.numSezione = numSezione;
		this.numDomandaInSezione = numDomandaInSezione;
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		
		StringBuffer result = new StringBuffer();
		result.append("\n\n\n   <!-- SEZIONE "+numSezione+" - INIZIO DOMANDA "+numDomandaInSezione+" -->\n");
		result.append("   <table width='100%' style='table-layout:fixed;' id='domanda"+getEle().getNumElem()+"'>\n");
		result.append("     <tr>\n");
		result.append("       <td>\n");
		result.append("         <table class='domanda'><tr>\n");
		result.append("           <td valign='top'><b>"+numDomandaInSezione+")</b></td>\n");
		result.append("           <td valign='top'>\n");
		result.append("            <b>\n");
		result.append("            "+getText()+"\n");
		result.append("            </b>\n");
		result.append("           </td>\n");
		
		if(getQuestionario().getDomandaInErrore().intValue() == getEle().getNumElem().intValue())
			result.append("           <td>&nbsp;&nbsp;&nbsp;"+getTemplate().inlineAnchor("domandaInErrore")+"</td>\n");

		result.append("         </tr></table>\n");
		result.append("       </td>\n");
		result.append("     </tr>\n");

		int rows = getElements().size() / cols;
		if((getElements().size() % cols) > 0)
			rows += getElements().size() % cols;
		
		result.append("     <tr>\n");
		result.append("       <td>\n");
		
		int rispCount = 0;
		result.append("         <table width='100%' class='risposteCont' cellspacing='0' cellpadding='0'>\n");
		result.append("          <tr>\n");
		for(int i=0;i<cols;i++){
			result.append("           <td valign='top'>\n");
			result.append("             <table width='100%' cellspacing='0' cellpadding='0'>\n");
			for(int j=0;j<rows;j++){
				result.append("               <tr>\n");
				if(rispCount < getElements().size()){
					result.append("                 <td>\n");										
					result.append("                   "+((HtmlElement)getElements().get(rispCount++)).getHtml()+"\n");										
					result.append("                 </td>\n");										
				}else
					result.append("                 <td>&nbsp;</td>\n");					
				result.append("               </tr>\n");
			}
			result.append("             </table>\n");
			result.append("           </td>\n");
		}
		result.append("          </tr>\n");
		result.append("         </table>\n");
		
		result.append("       </td>\n");
		result.append("     </tr>\n");

		result.append("   </table>\n");
		result.append("   <!-- SEZIONE "+numSezione+" - FINE DOMANDA "+numDomandaInSezione+" -->\n\n\n");
		return result.toString();
	}

	public int getCurCol() {
		return curCol;
	}

	public void setCurCol(int curCol) {
		this.curCol = curCol;
	}

	public int getCols() {
		return cols;
	}

}
