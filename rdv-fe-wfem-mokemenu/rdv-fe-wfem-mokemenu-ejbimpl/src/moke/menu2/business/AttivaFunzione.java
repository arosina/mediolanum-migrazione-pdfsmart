package moke.menu2.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import moke.menu2.model.Function;

/*******************************************************************************/
/*******************************************************************************/
public class AttivaFunzione extends BusinessCommand {

	private static final String logPrefix = AttivaFunzione.class.getName()+".execute() - ";

	private static final String commandNotFound     = "menu2/commandNotFound.jsp";
	private static final String blankPage           = "menu2/blankPage.jsp";
	
	private static final String showExternalPage    = "menu2/showExternalPage.jsp";
	private static final String startCmdPage        = "menu2/startCmd.jsp";
	
	/*******************************************************************************/
	/*******************************************************************************/
	public AttivaFunzione() {
		super();
	}
	
	/*******************************************************************************/
	/*******************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, 
									 CommandDataModel dataModel) throws CommandException {
		
		try{
			
			Function function = (Function)dataModel;
			if(function.getCommandName() == null ||
			   function.getCommandName().isNull()){
				this.setNextDisplayPage(blankPage);
				return null;
			}
			setNextStep(function);
			return null;		
			
		} catch(Exception e){
	
			throw new CommandException(e);
		}
	}
	
	/*******************************************************************************/
	/*******************************************************************************/
	public Class getInputViewClass() {
		return Function.class;
	}
	
	/*******************************************************************************/
	/*******************************************************************************/
	private void setNextStep(Function function){
		
		String commandName = function.getCommandName().toString();
		String commandType = function.getCommandType().toString();
		
		// La funzione e' di Tipo Comando, verifica se ci sono parametri e se ci sono
		// invoca il comando come una pagina JSP
		if(commandType.equals("COMMAND")){
		
			String commandNameParam = "";
			int firstParamPos = commandName.indexOf("?");
			// Se non trova il carattere '?' cerca il caratter '&'
			if(firstParamPos == -1){
				firstParamPos = commandName.indexOf("&");
			}
			if(firstParamPos != -1){
				commandNameParam = commandName.substring(firstParamPos+1);
				LOG.debug(logPrefix+"I parametri da aggiungere al comando sono: "+commandNameParam);
				commandName = commandName.substring(0,firstParamPos);
			}
			
			if(commandNameParam.length() > 0)
				commandNameParam = "&"+commandNameParam;
		
			// Esegue come Comando solo se non ci sono Parametri,
			// altrimenti lo esegue come un comando Stringa
			String mode="executeProcess";
			if(function.getIsMultiTaskMode().booleanValue())
				mode = "executeProcessOnNewStack";
			String commandNameAndParam = startCmdPage+"?startCmd="+commandName+"."+mode+commandNameParam;
			this.setNextDisplayPage(commandNameAndParam);
						
		}else{ // La funzione non e' di Tipo Comando
	
			if(commandType.equals("EXTERNALURL")){
				LOG.debug(logPrefix+"Il link della funzione esterna da richiamare e' " + commandName );
				this.setNextDisplayPage(showExternalPage+"?url="+commandName);
			}else{
				this.setNextDisplayPage(commandName);
				LOG.debug(logPrefix+"La pagina successiva da richiamare e' " + commandName );
			}
		}
	}
}
