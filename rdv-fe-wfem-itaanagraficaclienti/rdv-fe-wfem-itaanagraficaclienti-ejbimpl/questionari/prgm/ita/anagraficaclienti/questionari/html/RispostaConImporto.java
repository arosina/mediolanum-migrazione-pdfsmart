package prgm.ita.anagraficaclienti.questionari.html;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;


/*******************************************************************/
/*******************************************************************/
public class RispostaConImporto extends AbstractRisposta {
	
	/*******************************************************************/
	/*******************************************************************/
	public RispostaConImporto(HtmlElementModel ele,Template template, DomandaConImporto parent, QuestionarioModel questionario) {
		super(ele,template,parent,questionario);
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getHtml() {
		String modality = "insert";
		if(getQuestionario().isSalvato())
			modality = " read ";
		
		StringBuffer result = new StringBuffer();
		result.append("<table class='text' width='100%' cellspacing='0' cellpadding='0' ><tr>");
		result.append("<td width='50%'>"+getText()+"</td>");
		result.append("<td width='30%'>");
		
		IntegerType importo = new IntegerType(0);
		if(getEle().getSelSele().equals("S")){
			importo = getEle().getNumSele();
			if (importo.intValue() == 0 )
				importo = new IntegerType();			
			ListType risp = getQuestionario().getRisposte(); 
			RispostaReference rr = (RispostaReference)risp.get(getRispostaRef().getRispostaIdx());
			rr.getRispostaModel().setNumSele(importo);
		}	
		
		result.append(getTemplate().field("risposte"+getRispostaRef().getRispostaIdx()+"_rispostaModel_numSele", "type='num' size='15' maxlength='9' labelposition='nolabel' style='text-align:right;' modality='"+modality+"' "));
		result.append("</td>");
		result.append("<td width='20%'>,00 &euro; </td>");
		result.append("</tr></table>");
		return result.toString();
	}
	
}
