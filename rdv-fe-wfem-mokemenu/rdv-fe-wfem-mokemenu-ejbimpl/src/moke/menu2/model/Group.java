package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/****************************************************************/
/****************************************************************/
public class Group extends CommandDataModel {
	private StringType	 code = new StringType();
	private StringType   title = new StringType();
	private BooleanType  isDefault = new BooleanType();
	private ListType     applications = new ListType(Application.class);

	/****************************************************************/
	/****************************************************************/
	public Group(){
	}
	
	/****************************************************************/
	/****************************************************************/
	public Group(String code,String title,boolean isDefault){
		this.code = new StringType(code);
		this.title = new StringType(title);
		this.isDefault = new BooleanType(isDefault);
	}
	
	public StringType getCode() {
		return code;
	}

	public BooleanType getIsDefault() {
		return isDefault;
	}

	public StringType getTitle() {
		return title;
	}

	public void setCode(StringType code) {
		this.code = code;
	}

	public void setIsDefault(BooleanType isDefault) {
		this.isDefault = isDefault;
	}

	public void setTitle(StringType title) {
		this.title = title;
	}

	public ListType getApplications() {
		return applications;
	}

	public void setApplications(ListType applications) {
		this.applications = applications;
	}

}
