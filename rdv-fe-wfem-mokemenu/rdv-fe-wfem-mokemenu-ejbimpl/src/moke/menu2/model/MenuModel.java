package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

/****************************************************************/
/****************************************************************/
public class MenuModel extends CommandDataModel {

	private ListType    groups = new ListType(Group.class);
	private BooleanType checkRoles = new BooleanType();
	
	public BooleanType getCheckRoles() {
		return checkRoles;
	}
	public void setCheckRoles(BooleanType checkRoles) {
		this.checkRoles = checkRoles;
	}
	public ListType getGroups() {
		return groups;
	}
	public void setGroups(ListType groups) {
		this.groups = groups;
	}
	
}
