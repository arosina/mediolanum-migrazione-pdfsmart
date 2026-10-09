package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.aml.model.AmlModel;
import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class AmlPage extends DisplayCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		PdfModel pdf = (PdfModel)dataModel;
		AmlModel amlModel = pdf.getAmlModel();
		if(amlModel.getTabSelezionato().isNull()) {
			if(amlModel.hasNatura())
				amlModel.setTabSelezionato(new StringType("natura"));
			else if(amlModel.hasOrigine())
				amlModel.setTabSelezionato(new StringType("origine"));
			else if(amlModel.hasRelazioni())
				amlModel.setTabSelezionato(new StringType("relazioni"));
		}
		return pdf;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public boolean isStepCommand() {
		return true;
	}
}
