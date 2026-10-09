package moke.menu2.display;

import moke.menu2.model.ElencoComandiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************
 ***********************************************************************************************/
public class ElencoComandi extends DisplayCommand {

	private static final String DAO_XML_NAME = "MokeMenu2.MokeMenu2";
	
	/***********************************************************************************************
	 ***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ElencoComandiModel model = (ElencoComandiModel)dataModel;
			
	        DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
	        dao.fillCodDesc(model,false);
	        
        	if(model.getLoadConf().booleanValue() && !model.getRemoteAddr().isNull() && model.getProgetto().isNull()){
	        	try{
	        		dao.executeTableLoadAccess("conf",model);
	        	}catch(NoRowsAffected nra){}
        	}
        	
	        if(!model.getProgetto().isNull()){
	        	if(!model.getRemoteAddr().isNull()){
		        	try{
		        		dao.executeTableUpdateAccess("conf",model);
		        	}catch(NoRowsAffected nra){
		        		dao.executeTableInsertAccess("conf",model);
		        	}
	        	}
	        	model.setComandi(dao.executeQueryAccess("elencoComandi",model).getResult());
	        	model.getComando().setProgetto(new StringType(model.getProgetto().toString()));
	        }else{
	        	if(!model.getRemoteAddr().isNull()){
		        	try{
		        		dao.executeTableDeleteAccess("conf",model);
		        	}catch(NoRowsAffected nra){}
	        	}
	        	model.getComandi().clear();
	        }
			return model;
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************
	 ***********************************************************************************************/
	public Class getInputViewClass() {
		return ElencoComandiModel.class;
	}

}
