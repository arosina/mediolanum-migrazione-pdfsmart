package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;

/*******************************************************************/
/*******************************************************************/
public abstract class AbstractRisposta extends HtmlElement {
	
	RispostaReference rispostaRef;
	private AbstractDomanda parent;
	

	/*******************************************************************/
	/*******************************************************************/
	public AbstractRisposta(HtmlElementModel ele,Template template, AbstractDomanda parent, QuestionarioModel questionario) {
		super(ele,template,questionario);
		this.parent = parent;
	}

	public void setRispostaRef(RispostaReference rispostaRef) {
		this.rispostaRef = rispostaRef;
	}

	public RispostaReference getRispostaRef() {
		return rispostaRef;
	}

	public AbstractDomanda getParent() {
		return parent;
	}

}
