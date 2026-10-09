package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;


/*******************************************************************/
/*******************************************************************/
public class SezioneInizio extends HtmlElement {
	
	private int numSezione = 1;
	
	/*******************************************************************/
	/*******************************************************************/
	public SezioneInizio(HtmlElementModel ele,Template template, QuestionarioModel questionario, int numSezione) {
		super(ele,template,questionario);
		this.numSezione = numSezione;
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		
		StringBuffer result = new StringBuffer();
		result.append("\n\n\n\n\n\n<!-- INIZIO SEZIONE "+numSezione+" -->\n");
		result.append("<table width='100%' id='sezione"+getEle().getNumElem()+"'><tr><td>\n");
		result.append("<fieldset class='sezione'>\n");
		result.append("<legend class='titoloSezione'>\n"+
							"&nbsp;"+getText()+"\n</legend>\n");
		result.append("<table width='100%'><tr><td>\n");
		
		result.append(innerHtml());
		
		return result.toString();
	}


}
