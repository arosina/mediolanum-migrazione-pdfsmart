package prgm.pdfwebforms.catalog;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SearchCompleted extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			
			catalog.getPdfCompletedListParams().setOrderField(new StringType("pdfCompletionTime"));
			catalog.getPdfCompletedListParams().setOrderType(new StringType("desc"));
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
			catalog.setPdfCompletedList(dao.executeQueryAccess("pdfCompletedList",catalog.getPdfCompletedListParams()).getResult());
			
			setNextCommandClass(PdfCatalog.class);
			return catalog;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfCatalogModel.class;
	}

}
