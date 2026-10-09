package prgm.cedacri.adeguatezza.cache;

import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.command.CommandDataModel;

public class PrgmCommands extends CommandDataModel 
{
	private static final long serialVersionUID = 0;

	private IntegerType commandCode        = new IntegerType();
	private StringType  serviceDescription = new StringType("");
	private StringType  commandDescription = new StringType("");
	private StringType  applicationCode    = new StringType("");
	private IntegerType functionCode       = new IntegerType();
	private StringType  flagTraceAccess    = new StringType("");
	private StringType  aType              = new StringType("");
	
	public StringType getApplicationCode() {
		return applicationCode;
	}
	public void setApplicationCode(StringType applicationCode) {
		this.applicationCode = applicationCode;
	}
	public StringType getAType() {
		return aType;
	}
	public void setAType(StringType type) {
		aType = type;
	}
	public IntegerType getCommandCode() {
		return commandCode;
	}
	public void setCommandCode(IntegerType commandCode) {
		this.commandCode = commandCode;
	}
	public StringType getCommandDescription() {
		return commandDescription;
	}
	public void setCommandDescription(StringType commandDescription) {
		this.commandDescription = commandDescription;
	}
	public StringType getFlagTraceAccess() {
		return flagTraceAccess;
	}
	public void setFlagTraceAccess(StringType flagTraceAccess) {
		this.flagTraceAccess = flagTraceAccess;
	}
	public IntegerType getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(IntegerType functionCode) {
		this.functionCode = functionCode;
	}
	public StringType getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(StringType serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
}
