package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class NaturaModel extends AbstractSezioneAmlModel{

	// Uno per ogni dispositiva in elencoDispositive
	private ListType elencoScopiRapporto = new ListType(ScopoRapportoModel.class);

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public String drawIcons() {
		String res = super.drawIcons();
		if(!res.isEmpty())
			return res;

		for(int i=0;i<getElencoScopiRapporto().size();i++) {
			ScopoRapportoModel scopoRapporto = (ScopoRapportoModel)getElencoScopiRapporto().get(i);
			if(scopoRapporto.getCodScopoRapporto().isNull())
				return "";
		}
		return AmlModel.OK_IMG;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean verify() throws Exception {		
		Tools.resetTypesWarningAndErrors(this);		
		for(int i=0;i<getElencoScopiRapporto().size();i++) {
			ScopoRapportoModel scopoRapporto = (ScopoRapportoModel)getElencoScopiRapporto().get(i);
			if(scopoRapporto.getCodScopoRapporto().isNull())
				scopoRapporto.getCodScopoRapporto().addTypeError("Selezionare lo scopo rapporto");
		}
		return !Tools.containsTypeErrors(this);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int numScopiRapportoNonPreselezionati() {
		int res = 0;
		for(int i=0;i<getElencoScopiRapporto().size();i++) {
			ScopoRapportoModel sr = (ScopoRapportoModel)getElencoScopiRapporto().get(i);
			if(!sr.isCodScopoRapportoPreselezionato())
				res++;
		}
		return res;
	}

	public ListType getElencoScopiRapporto() {
		return elencoScopiRapporto;
	}

	public void setElencoScopiRapporto(ListType elencoScopiRapporto) {
		this.elencoScopiRapporto = elencoScopiRapporto;
	}


}
