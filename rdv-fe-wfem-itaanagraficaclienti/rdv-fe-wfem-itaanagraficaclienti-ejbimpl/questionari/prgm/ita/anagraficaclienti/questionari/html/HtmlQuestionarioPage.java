package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.ListType;

/*******************************************************************/
/*******************************************************************/
public class HtmlQuestionarioPage extends HtmlElement {
	
	/*******************************************************************/
	/*******************************************************************/
	public HtmlQuestionarioPage() {
		super(null,null,null);
	}

	/*******************************************************************/
	/*******************************************************************/
	public HtmlQuestionarioPage(QuestionarioModel model, Template template){
		super(null,template,model);
		try{			
			
			model.getRisposte().clear();
			
			int numElemCorrente = -1;
			int numSezioneCorrente = 1;
			int numDomandaInSezione = 1;
			HtmlElement elemCorrente = this;
			HtmlElement sezioneCorrente = null;		
			HtmlElement domandaCorrente = null;		
			HtmlElement commentoCorrente = null;	
		
			ListType rows = model.getElementiQuestionario(); //inserire mio vettore ordinato come voglio
			for(int i=0;i<rows.size();i++){
				
				HtmlElementModel ele = (HtmlElementModel)rows.get(i);
				
				if(ele.getNumElem().intValue() != numElemCorrente){
					numElemCorrente = ele.getNumElem().intValue();
					elemCorrente = this;
				}
	
				if(!ele.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO) &&
				   !ele.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO_PUNTEGGIO))
					commentoCorrente = null;
				
				if(ele.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO)){
					
					if(commentoCorrente != null){
						commentoCorrente.addText(" "+ele.getTestEle());
					}else{
						Commento comm = new Commento(ele,template,model);
						elemCorrente.addElement(comm);
						commentoCorrente = comm;
					}
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_COMMENTO_PUNTEGGIO)){
					
					if(commentoCorrente != null){
						commentoCorrente.addText(" "+ele.getTestEle());
					}else{
						CommentoPunteggio comm = new CommentoPunteggio(ele,template,model);
						elemCorrente.addElement(comm);
						commentoCorrente = comm;
					}
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_SEZIONE)){
					
					elemCorrente = this;
					if(sezioneCorrente != null){
						elemCorrente.addElement(new SezioneFine(null,template,model,numSezioneCorrente));
						domandaCorrente = null;
						numSezioneCorrente++;
						numDomandaInSezione = 1;
					}
					SezioneInizio s = new SezioneInizio(ele,template,model,numSezioneCorrente);
					elemCorrente.addElement(s);
					elemCorrente = s;
					sezioneCorrente = s;
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_SEMPLICE)){
					
					if(domandaCorrente != null)
						elemCorrente.addElement(new SeparatoreDomande(null,template,model));
					DomandaSemplice ds = new DomandaSemplice(ele,template,model, numSezioneCorrente, numDomandaInSezione);
					numDomandaInSezione++;
					elemCorrente.addElement(ds);
					domandaCorrente = ds;
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_MULTIPLA)){
					
					if(domandaCorrente != null)
						elemCorrente.addElement(new SeparatoreDomande(null,template,model));
					DomandaMultipla dm = new DomandaMultipla(ele,template,model, numSezioneCorrente, numDomandaInSezione);
					numDomandaInSezione++;
					elemCorrente.addElement(dm);
					domandaCorrente = dm;
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_DOMANDA_IMPORTO)){
					
					DomandaConImporto dci = new DomandaConImporto(ele, template, model, numSezioneCorrente, numDomandaInSezione);
					numDomandaInSezione++;
					elemCorrente.addElement(dci);
					domandaCorrente = dci;
					
				}else if(ele.getTipElem().equals(TipiElemento.TIPELEM_RISPOSTA)){
	
					if(domandaCorrente == null){
						
						elemCorrente.addElement(new CostruzioneErrata("Una risposta deve essere definita dopo una domanda.",template,model));
						
					}else if(domandaCorrente instanceof DomandaSemplice){
						
						DomandaSemplice ds = (DomandaSemplice)domandaCorrente;
						RispostaSemplice rs = new RispostaSemplice(ele,template,ds,model);
						ds.addElement(rs);
						
						RispostaReference rispostaRef = ds.getRispostaSempliceRef();
						if(rispostaRef == null){
							rispostaRef = new RispostaReference();
							rispostaRef.setRispostaIdx(model.getRisposte().size());
							ds.setRispostaSempliceRef(rispostaRef);
							model.addRisposta(rispostaRef);
						}
						rispostaRef.getValoriPossibiliRisposta().add(ele);
						
						rispostaRef.setRisposta(rs);
						rs.setRispostaRef(rispostaRef);
						
					}else if(domandaCorrente instanceof DomandaMultipla){
						
						DomandaMultipla dm = (DomandaMultipla)domandaCorrente;
						RispostaMultipla rm = new RispostaMultipla(ele,template,dm,model);
						dm.addElement(rm);
						
						RispostaReference rispostaRef = new RispostaReference();
						rispostaRef.setRispostaIdx(model.getRisposte().size());
						rispostaRef.getValoriPossibiliRisposta().add(ele);
						rispostaRef.setRisposta(rm);
						rm.setRispostaRef(rispostaRef);
						
						model.addRisposta(rispostaRef);
						
					}else if(domandaCorrente instanceof DomandaConImporto){
						
						DomandaConImporto dci = (DomandaConImporto)domandaCorrente;
						RispostaConImporto rci = new RispostaConImporto(ele, template, dci, model);
						dci.addElement(rci);
						
						RispostaReference rispostaRef = new RispostaReference();
						rispostaRef.setRispostaIdx(model.getRisposte().size());
						rispostaRef.getValoriPossibiliRisposta().add(ele);
						rispostaRef.setRisposta(rci);
						rci.setRispostaRef(rispostaRef);
						
						model.addRisposta(rispostaRef);
					
					}else{
						
						elemCorrente.addElement(new CostruzioneErrata("Una risposta deve essere definita dopo una domanda.",template,model));	
						
					}
				}			
			}
			if(sezioneCorrente != null)
				this.addElement(new SezioneFine(null,template,model,numSezioneCorrente));
		}catch(Exception e){
			this.addElement(new CostruzioneErrata("Eccezione: "+e.toString(),template,model));				
		}
		
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		StringBuffer result = new StringBuffer();
		result.append("<table width='100%' class='text'><tr><td>");
		result.append(innerHtml());
		result.append("</td></tr></table>");
		return result.toString();
	}

}
