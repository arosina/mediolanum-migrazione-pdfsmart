package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;



/*******************************************************************/
/*******************************************************************/
public class DomandaSemplice extends AbstractDomanda {
	
	RispostaReference rispostaSempliceRef = null;
	
	/*******************************************************************/
	/*******************************************************************/
	public DomandaSemplice(HtmlElementModel ele,Template template, QuestionarioModel questionario, int numSezione, int numDomandaInSezione) {
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

}
