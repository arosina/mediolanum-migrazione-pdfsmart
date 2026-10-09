package moke.menu2.display;

import moke.menu2.model.PortalPageModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/***********************************************************************************************/
/***********************************************************************************************/
public class OpenPortalPage extends DisplayCommand {

	private static final String DAO_XML_NAME = "MokeMenu2.MokeMenu2";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,	CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PortalPageModel model = (PortalPageModel)dataModel;
			
	        DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
       		dao.executeQueryAccess("loadPortalPageCommand",model);
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
		return PortalPageModel.class;
	}

}
