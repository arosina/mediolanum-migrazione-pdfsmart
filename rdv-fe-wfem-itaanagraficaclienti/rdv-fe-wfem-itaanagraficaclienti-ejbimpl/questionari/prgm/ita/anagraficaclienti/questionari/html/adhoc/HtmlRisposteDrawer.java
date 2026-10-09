package prgm.ita.anagraficaclienti.questionari.html.adhoc;

import java.util.Vector;

import prgm.ita.anagraficaclienti.questionari.html.HtmlElementModel;
import prgm.ita.anagraficaclienti.questionari.html.RispostaMultipla;
import prgm.ita.anagraficaclienti.questionari.html.RispostaReference;
import prgm.ita.anagraficaclienti.questionari.html.RispostaSemplice;
import prgm.ita.anagraficaclienti.questionari.html.TipiElemento;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.ListType;

/*******************************************************************/
/*******************************************************************/
public class HtmlRisposteDrawer {
	
	Template template;
	QuestionarioModel model;
	String prefix = "";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public HtmlRisposteDrawer(Template template, QuestionarioModel model){
		this.template = template;
		this.model = model;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public HtmlRisposteDrawer(Template template, QuestionarioModel model, String prefix){
		this.template = template;
		this.model = model;
		if(prefix.length() > 0)
			this.prefix = prefix+"_";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String inlineAnchor(){
		return template.inlineAnchor(prefix+"domandaInErrore");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaSemplice(int rispostaIdx, String value){
		return rispostaSemplice(rispostaIdx, value, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaSemplice(int rispostaIdx, String value, String text){
		return rispostaSemplice(rispostaIdx, value, text, false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaSemplice(int rispostaIdx, String value, String text, boolean initiallyDisabled){
		return rispostaSemplice(rispostaIdx, value, text, initiallyDisabled, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaSemplice(int rispostaIdx, String value, String text, boolean initiallyDisabled, String[] replacements){
		rispostaIdx = rispostaIdx-1;
		RispostaReference rispRef = retrieveRispostaReference(rispostaIdx);
		if(rispRef == null)
			return "";
		if(!(rispRef.getRisposta() instanceof RispostaSemplice))
			return "";

		String disabled = "";
		if(model.isSalvato() || initiallyDisabled)
			disabled = " disabled ";
		String checked = "";
		String rispText = "";
		Vector rs = rispRef.getValoriPossibiliRisposta();
		for(int j=0;j<rs.size();j++){
			HtmlElementModel eleRisp = (HtmlElementModel)rs.get(j);
			if(value.equals(eleRisp.getNumSele().toString())){
				rispText = eleRisp.getTestEle().toString();
				if(eleRisp.getSelSele().equals("S"))
					checked = " checked ";
				break;
			}
		}
		
		text = text == null ? rispText : text;
		if(replacements != null){
			for(int i=0;;i+=2){
				if(i >= replacements.length)
					break;
				text = text.replaceAll(replacements[i], replacements[i+1]);
			}
		}
		
		StringBuffer result = new StringBuffer();
		result.append("<table class='rispostaSemplice'><tr>");
		result.append("<td>");
		result.append("<input numElem='"+rispRef.getRisposta().getEle().getNumElem()+"' "+
							  checked+" type='radio' value='"+value+"' "+disabled+"name='"+prefix+"risposte"+rispostaIdx+"_rispostaModel_numSele'>");
		result.append("</td>");
		result.append("<td>"+text+"</td>");
		result.append("</tr></table>");
		return result.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaMultipla(int rispostaIdx){
		return rispostaMultipla(rispostaIdx, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaMultipla(int rispostaIdx, String text){
		return rispostaMultipla(rispostaIdx, text, false);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaMultipla(int rispostaIdx, String text, boolean initiallyDisabled){
		return rispostaMultipla(rispostaIdx, text, initiallyDisabled, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String rispostaMultipla(int rispostaIdx, String text, boolean initiallyDisabled, String[] replacements){
		rispostaIdx = rispostaIdx-1;
		RispostaReference rispRef = retrieveRispostaReference(rispostaIdx);
		if(rispRef == null)
			return "";
		if(!(rispRef.getRisposta() instanceof RispostaMultipla))
			return "";

		String disabled = "";
		if(model.isSalvato() || initiallyDisabled)
			disabled = " disabled ";
		String checked = "";
		String checkValue = "N";
		Vector rs = rispRef.getValoriPossibiliRisposta();
		for(int j=0;j<rs.size();j++){
			HtmlElementModel eleRisp = (HtmlElementModel)rs.get(j);
			if(eleRisp.getSelSele().equals("S")){
				checked = " checked ";
				checkValue = "S";
				break;
			}
		}
		
		text = text == null ? rispRef.getRisposta().getText() : text;
		if(replacements != null){
			for(int i=0;;i+=2){
				if(i >= replacements.length)
					break;
				text = text.replaceAll(replacements[i], replacements[i+1]);
			}
		}
		
		String propName = prefix+"risposte"+rispostaIdx+"_rispostaModel_selSele";
		StringBuffer result = new StringBuffer();
		result.append("<table class='rispostaMultipla'><tr>");
		result.append("<td>");
		result.append("<input numElem='"+rispRef.getRisposta().getEle().getNumElem()+"' numSele='"+rispRef.getRisposta().getEle().getNumSele()+"' "+
								  checked+" type='checkbox' "+disabled+
                                  "name='"+prefix+"risposte"+rispostaIdx+"_rispostaModel_selSeleCheck' "+
                                  "propname='"+propName+"' "+
                                  "rispostaIdx='"+rispostaIdx+"' "+
                                  "onclick='manageRisposteMultiple(this);'>");
		result.append("<input type='hidden' id='"+propName+"' name='"+propName+"' value='"+checkValue+"'>");
		result.append("</td>");
		result.append("<td>"+text+"</td>");
		result.append("</tr></table>");
		return result.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private RispostaReference retrieveRispostaReference(int rispostaIdx){
		ListType risposte = model.getRisposte();
		for(int i=0;i<risposte.size();i++){
			RispostaReference rispRef = (RispostaReference)risposte.get(i);
			if(rispRef.getRispostaIdx() != rispostaIdx)
				continue;
			return rispRef;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String sezione(int numElem){
		ListType el = model.getElementiQuestionario();
		for(int i=0;i<el.size();i++){
			HtmlElementModel e = (HtmlElementModel)el.get(i);
			if(e.getTipElem().equals(TipiElemento.TIPELEM_SEZIONE)){
				if(e.getNumElem().intValue() == numElem){
					return e.getTestEle().toString();
				}
			}
		}
		return "";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String domanda(int numElem){
		ListType el = model.getElementiQuestionario();
		for(int i=0;i<el.size();i++){
			HtmlElementModel e = (HtmlElementModel)el.get(i);
			if(e.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_SEMPLICE) ||
			   e.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_MULTIPLA) ||
			   e.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_IMPORTO)){
				if(e.getNumElem().intValue() == numElem){
					return e.getTestEle().toString();
				}
			}
		}
		return "";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String commento(int numElem, int numSele){
		ListType el = model.getElementiQuestionario();
		for(int i=0;i<el.size();i++){
			HtmlElementModel e = (HtmlElementModel)el.get(i);
			if(e.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO) ||
			   e.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO_PUNTEGGIO)){
				if(e.getNumElem().intValue() == numElem && e.getNumSele().intValue() == numSele){
					return e.getTestEle().toString();
				}
			}
		}
		return "";
	}
}
