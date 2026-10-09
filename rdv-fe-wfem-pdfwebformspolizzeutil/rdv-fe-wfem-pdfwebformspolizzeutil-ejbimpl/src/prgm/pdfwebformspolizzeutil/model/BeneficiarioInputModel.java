package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaBenefInput;

public class BeneficiarioInputModel extends CommandDataModel{
	
	private static final long serialVersionUID = 1L;
	
	
	private InrDispVitaBenefInput inrDispVitaBenefInput = null;
	private BooleanType isBeneficiarioPersonaFisica = null;
	private ListType inrDispVitaTitolariInput = null;
	
	/*********************************************************************************/
	/*********************************************************************************/

	
	public InrDispVitaBenefInput getInrDispVitaBenefInput() {
		return inrDispVitaBenefInput;
	}
	public void setInrDispVitaBenefInput(InrDispVitaBenefInput inrDispVitaBenefInput) {
		this.inrDispVitaBenefInput = inrDispVitaBenefInput;
	}
	public BooleanType getIsBeneficiarioPersonaFisica() {
		return isBeneficiarioPersonaFisica;
	}
	public void setIsBeneficiarioPersonaFisica(BooleanType isBeneficiarioPersonaFisica) {
		this.isBeneficiarioPersonaFisica = isBeneficiarioPersonaFisica;
	}
	public ListType getInrDispVitaTitolariInput() {
		return inrDispVitaTitolariInput;
	}
	public void setInrDispVitaTitolariInput(ListType inrDispVitaTitolariInput) {
		this.inrDispVitaTitolariInput = inrDispVitaTitolariInput;
	}
	

		

}
