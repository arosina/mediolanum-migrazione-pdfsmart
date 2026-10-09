package prgm.pdfwebforms.catalog;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfModuloModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfDettaglioModulo extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			PdfAnagModel pdfAnag = catalog.getDettaglioModulo();
			PdfModuloModel modulo = pdfAnag.getPdfModulo();
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
			dao.executeQueryAccess("loadDettaglioModulo",pdfAnag);
			modulo.setProdottiModulo(dao.executeQueryAccess("loadProdottiModulo",pdfAnag).getResult());
			pdfAnag.setPdfApplReferences(dao.executeQueryAccess("loadPdfApplReferences",pdfAnag).getResult());
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

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}
	
}
