package prgm.pdfwebforms.publisher.display;

import prgm.pdfwebforms.publisher.model.PdfApplReferenceModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfApplReferences extends DisplayCommand implements GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
		return model;			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		PdfApplReferenceModel ref = (PdfApplReferenceModel)row;
		FieldStyle fs = new FieldStyle();
		if(cellPropertyName.equals("acroformVersion") && ref.getAcroformVersion().isNull()){
			fs.innerHTML = "Attuale";
		}
		cell.setStyle(fs);
	}

}
