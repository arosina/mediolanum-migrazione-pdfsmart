package moke.menu2.business;

import moke.menu2.model.ComandoModel;
import moke.menu2.model.ElencoComandiModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************************/
/*******************************************************************************/
public class CancellaComando extends BusinessCommand {

	private static final String DAO_XML_NAME = "MokeMenu2.MokeMenu2";
	
	/*******************************************************************************/
	/*******************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ElencoComandiModel model = (ElencoComandiModel)dataModel;
			ComandoModel c = model.getComando();
			
	        DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
        	dao.executeTableDeleteAccess("comando",model.getComando());
        	String progetto = c.getProgetto().toString();
        	model.setComando(new ComandoModel());
        	model.setProgetto(new StringType(progetto));
        	model.getComando().setProgetto(new StringType(progetto));
			setForwardDisplay(new Integer(0),true);
			return model;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/*******************************************************************************/
	/*******************************************************************************/
	public Class getInputViewClass() {
		return ElencoComandiModel.class;
	}

}
