package prgm.cedacri.adeguatezza.log;

import javax.ejb.EJBException;

import prgm.cedacri.adeguatezza.cache.PrgmCommands;
import prgm.cedacri.adeguatezza.internal.StatoAdeguatezza;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

public class AccessLog 
{
    private static com.atosorigin.wfem.util.BkRemoteObjectFactory ROF = com.atosorigin.wfem.util.BkRemoteObjectFactory.getInstance();
    
	private static final long serialVersionUID = 0;
	private static final int AT_WS_EXTERNAL_COMMAND = 10;
	private static final int AT_EJB_EXTERNAL_COMMAND = 11;

	public static boolean callTraceFunction(ClientSessionContext csc, 
											StringType userCode, StringType country, StringType channel, 
											StringType applicationCode, PrgmCommands command, 
											StringType nomeMetodo, String nomeDbFile, StatoAdeguatezza esito){
		try{
			return traceFunctionAccess(csc,userCode,country,channel,applicationCode,command,nomeMetodo,nomeDbFile,esito); 			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	 
	public static boolean traceFunctionAccess(ClientSessionContext csc, 
		StringType userCode, StringType country, StringType channel, 
		StringType applicationCode, PrgmCommands command, 
		StringType nomeMetodo, String nomeDbFile, StatoAdeguatezza esito) throws EJBException 
	{
		if (userCode.equalsIgnoreCase(""))
		{
			esito.esito = "004";
			esito.setDescr("Campo Username non compilato");
			return false;			
		}
		if (country.equalsIgnoreCase(""))
		{
			esito.esito = "004";
			esito.setDescr("Campo Country non compilato");
			return false;			
		}
		if (channel.equalsIgnoreCase(""))
		{
			esito.esito = "004";
			esito.setDescr("Campo CanVend non compilato");
			return false;			
		}

		try 
		{
			UserAccessLog record = new UserAccessLog(); 

			record.setUserCode(userCode);
			record.setAccessTime(Tools.now());
			if (nomeMetodo.getStringValue().endsWith("WS"))
				record.setAccessType(new IntegerType(AT_WS_EXTERNAL_COMMAND));
			else
				record.setAccessType(new IntegerType(AT_EJB_EXTERNAL_COMMAND));
			record.setDescription(new StringType("ACCESS TO COMMAND: " + nomeMetodo.getStringValue()));
			record.setApplicationCode(applicationCode);
			record.setFunctionCode(new IntegerType(command.getFunctionCode()));
			record.setCommandCode(new IntegerType(command.getCommandCode()));
			record.setCountryCode(country);
			record.setChannelCode(channel);
			UserAccessLogManager userAccesLogFacade = (UserAccessLogManager)ROF.getManager(csc,UserAccessLogManager.class);
			userAccesLogFacade.traceAccess(csc,record);
		}catch(Exception e){
			esito.esito = "999";
			esito.setDescr("" + e);
			return false;
		}
		return true;
	}
}
