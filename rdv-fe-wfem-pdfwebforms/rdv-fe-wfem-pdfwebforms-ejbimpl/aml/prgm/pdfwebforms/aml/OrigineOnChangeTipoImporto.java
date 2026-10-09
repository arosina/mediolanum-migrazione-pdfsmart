package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.aml.model.OrigineModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrigineOnChangeTipoImporto extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = (PdfModel)dataModel;
		OrigineModel origine = pdf.getAmlModel().getOrigine();		
		origine.aggiornaTendineTipiImporto();		
		setForwardDisplay(Integer.valueOf(0));
		return dataModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
