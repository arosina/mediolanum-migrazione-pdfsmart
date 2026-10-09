package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ScopoRapportoModel extends CommandDataModel{

	private StringType 		codScopoRapporto = new StringType();
	private boolean			codScopoRapportoPreselezionato;
	private CodDescDataList codScopoRapportoDataList = new CodDescDataList();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawIcons() {
		if(getCodScopoRapporto().hasTypeErrors())
			return AmlModel.ERROR_IMG;
		if(!getCodScopoRapporto().isNull())
			return AmlModel.OK_IMG;
		return "";
	}

	public StringType getCodScopoRapporto() {
		return codScopoRapporto;
	}

	public void setCodScopoRapporto(StringType codScopoRapporto) {
		this.codScopoRapporto = codScopoRapporto;
	}

	public CodDescDataList getCodScopoRapportoDataList() {
		return codScopoRapportoDataList;
	}

	public void setCodScopoRapportoDataList(CodDescDataList codScopoRapportoDataList) {
		this.codScopoRapportoDataList = codScopoRapportoDataList;
	}

	public boolean isCodScopoRapportoPreselezionato() {
		return codScopoRapportoPreselezionato;
	}

	public void setCodScopoRapportoPreselezionato(boolean codScopoRapportoPreselezionato) {
		this.codScopoRapportoPreselezionato = codScopoRapportoPreselezionato;
	}
}
