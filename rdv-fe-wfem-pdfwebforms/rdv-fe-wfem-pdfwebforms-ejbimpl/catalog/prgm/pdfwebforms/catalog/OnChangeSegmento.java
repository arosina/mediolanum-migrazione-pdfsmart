package prgm.pdfwebforms.catalog;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;

/***********************************************************************************************/
/***********************************************************************************************/
public class OnChangeSegmento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;

            if(catalog.getPdfListParams().getCodiceSegmento().isNull())
            	catalog.getPdfListParams().addCodDescField("codiceProdotto", "PRODOTTI");
            else
            	catalog.getPdfListParams().addCodDescField("codiceProdotto", "PRODOTTI_SEGMENTO");
            new DAOObject(csc,"PdfWebForms.PdfCatalog").fillCodDesc(catalog.getPdfListParams(),false);
			
            setForwardDisplay(new Integer(0));
			return catalog;
			
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
