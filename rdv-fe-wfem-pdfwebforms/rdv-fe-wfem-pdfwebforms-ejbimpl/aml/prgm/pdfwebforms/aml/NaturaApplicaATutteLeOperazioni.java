package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.aml.model.NaturaModel;
import prgm.pdfwebforms.aml.model.ScopoRapportoModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class NaturaApplicaATutteLeOperazioni extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			PdfModel pdf = (PdfModel)dataModel;
			NaturaModel natura = pdf.getAmlModel().getNatura();			
			int idx = natura.getDispositivaSelezionata().intValue();
			ScopoRapportoModel srCurr = (ScopoRapportoModel)natura.getElencoScopiRapporto().get(idx);
			for(int i=0;i<natura.getElencoScopiRapporto().size();i++) {
				ScopoRapportoModel sr = (ScopoRapportoModel)natura.getElencoScopiRapporto().get(i);
				if(sr != srCurr && !sr.isCodScopoRapportoPreselezionato())
					sr.setCodScopoRapporto(new StringType(srCurr.getCodScopoRapporto().toString()));
			}			
			natura.verify();
			setForwardDisplay(Integer.valueOf(0));
			return dataModel;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
