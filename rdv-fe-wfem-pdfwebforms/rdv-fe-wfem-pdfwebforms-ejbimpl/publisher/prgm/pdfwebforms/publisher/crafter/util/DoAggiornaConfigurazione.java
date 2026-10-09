package prgm.pdfwebforms.publisher.crafter.util;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;

import prgm.pdfwebforms.publisher.crafter.CrafterPdfUtils;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DoAggiornaConfigurazione extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

        ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PopolaCrafterUtilModel model = (PopolaCrafterUtilModel)dataModel;
    	try{
    		try{
    			new DAOObject(csc, "PdfWebForms.PdfCrafterPublisher").executeTableUpdateAccess("configurazioneCrafter", model);
    		}catch(NoRowsAffected nra){
    			new DAOObject(csc, "PdfWebForms.PdfCrafterPublisher").executeTableInsertAccess("configurazioneCrafter", model);    			
    		}
            model.setAreeConfigurate(CrafterPdfUtils.loadAreeCrafter(csc, new PdfConfigurationModel()));
    		setForwardDisplay(new Integer(0));
    		return model;
    	}catch(DAOException daoe){
    		throw new CommandException(daoe.toString());
    	}catch(Exception e){
    		throw new CommandException(e.toString());
		}	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopolaCrafterUtilModel.class;
	}

}
