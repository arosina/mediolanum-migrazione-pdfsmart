package prgm.pdfwebforms.publisher.display;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfArchivedList extends DisplayCommand implements GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
			model.setPdfArchivedList(new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME).executeQueryAccess("pdfArchivedList",model.getPdfAnag()).getResult());
			return model;			
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}	
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
		PdfAnagModel pdf = (PdfAnagModel)row;
		FieldStyle fs = new FieldStyle();
		
		if(cellPropertyName.equals("comandi")){
			fs.innerHTML =  "<span style='text-decoration:underline;cursor:pointer;' onclick='ripristinaPubblicazione("+pdf.getPdfPublicationId()+");'>ripristina</span>";
		}
		
		cell.setStyle(fs);
	}
	
}
