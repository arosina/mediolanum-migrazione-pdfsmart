package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/****************************************************************/
/****************************************************************/
public class PortalPageModel extends CommandDataModel{

	private StringType	 portalPageName = new StringType();
	private StringType	 portalPageParams = new StringType();
	private StringType   wfemCommand = new StringType();
	
	public StringType getPortalPageName() {
		return portalPageName;
	}
	public void setPortalPageName(StringType portalPageName) {
		this.portalPageName = portalPageName;
	}
	public StringType getWfemCommand() {
		return wfemCommand;
	}
	public void setWfemCommand(StringType wfemCommand) {
		this.wfemCommand = wfemCommand;
	}
	public void setPortalPageParams(StringType portalPageParams) {
		this.portalPageParams = portalPageParams;
	}
	public StringType getPortalPageParams() {
		return portalPageParams;
	}
}
