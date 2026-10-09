package prgm.ita.anagraficaclienti.questionari.html;

import java.util.Vector;

import com.atosorigin.wfem.command.CommandDataModel;

/*******************************************************************/
/*******************************************************************/
public class RispostaReference extends CommandDataModel{
	
	Vector 				valoriPossibiliRisposta = new Vector();
	HtmlElementModel 	rispostaModel = new HtmlElementModel();
	HtmlElement			risposta;
	int rispostaIdx;
	
	public int getRispostaIdx() {
		return rispostaIdx;
	}
	public void setRispostaIdx(int rispostaIdx) {
		this.rispostaIdx = rispostaIdx;
	}
	public HtmlElementModel getRispostaModel() {
		return rispostaModel;
	}
	public void setRispostaModel(HtmlElementModel rispostaModel) {
		this.rispostaModel = rispostaModel;
	}
	public HtmlElement getRisposta() {
		return risposta;
	}
	public void setRisposta(HtmlElement risposta) {
		this.risposta = risposta;
	}
	public Vector getValoriPossibiliRisposta() {
		return valoriPossibiliRisposta;
	}
	public void setValoriPossibiliRisposta(Vector valoriPossibiliRisposta) {
		this.valoriPossibiliRisposta = valoriPossibiliRisposta;
	}

}
