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
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************************/
/*******************************************************************************/
public class InserisciNuovoComando extends BusinessCommand {

	private static final String DAO_XML_NAME = "MokeMenu2.MokeMenu2";
	
	/*******************************************************************************/
	/*******************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ElencoComandiModel model = (ElencoComandiModel)dataModel;
			setForwardDisplay(new Integer(0));
			
			ComandoModel c = model.getComando();
			c.resetCommandErrors();
			Tools.resetTypesErrors(c);
			
			boolean err = false;
			if(c.getProgetto().isNull()){
				c.getProgetto().addTypeError("Campo obbligatorio");
				err = true;
			}
			if(c.getAttore().isNull()){
				c.getAttore().addTypeError("Campo obbligatorio");
				err = true;
			}
			if(c.getDescrizione().isNull()){
				c.getDescrizione().addTypeError("Campo obbligatorio");
				err = true;
			}
			if(c.getComando().isNull()){
				c.getComando().addTypeError("Campo obbligatorio");
				err = true;
			}

			if(err){
				c.addCommandError("errori");
				return model;
			}
			
	        DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
	        if(!c.getId().isNull()){
	        	dao.executeTableUpdateAccess("comando",model.getComando());
	        }else{
	        	BooleanType esiste = (BooleanType)dao.executeQueryAccess("comandoEsiste",model.getComando()).getSingleResult();
	        	if(esiste.booleanValue()){
					c.getComando().addTypeError("Il comando esiste già");
					c.addCommandError("errori");
					return model;
	        	}
	        	dao.executeTableInsertAccess("comando",model.getComando());
	        }

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
