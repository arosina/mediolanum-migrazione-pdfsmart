package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/****************************************************************/
/****************************************************************/
public class Function extends CommandDataModel{

	private StringType	 code = new StringType();
	private StringType   title = new StringType();
	private IntegerType  menuLayer = new IntegerType(0);
	private StringType	 commandName = new StringType();
	private StringType	 commandType = new StringType("COMMAND");
	private StringType	 browserInstance = new StringType("0");
	private BooleanType  isDefault = new BooleanType();	
	private BooleanType  isOnline = new BooleanType(true);
	private StringType   roles = new StringType();
	private BooleanType  isMultiTaskMode = new BooleanType();	
	
	private boolean hasChilds = false;

	/****************************************************************/
	/****************************************************************/
	public Function(){
	}
	
	/****************************************************************/
	/****************************************************************/
	public Function(String	 code,
					 String  title,
					 int     menuLayer,
					 String	 commandName,
					 String	 commandType,
					 String	 browserInstance,
					 boolean isDefault,
					 boolean isOnline){
		this.code = new StringType(code);
		this.title = new StringType(title);
		this.menuLayer = new IntegerType(menuLayer);
		this.commandName = new StringType(commandName);
		this.commandType = new StringType(commandType);
		this.browserInstance = new StringType(browserInstance);
		this.isDefault = new BooleanType(isDefault);	
		this.isOnline = new BooleanType(isOnline);
	}
	
	public StringType getBrowserInstance() {
		return browserInstance;
	}

	public StringType getCode() {
		return code;
	}

	public StringType getCommandName() {
		return commandName;
	}

	public StringType getCommandType() {
		return commandType;
	}

	public BooleanType getIsDefault() {
		return isDefault;
	}

	public BooleanType getIsOnline() {
		return isOnline;
	}

	public IntegerType getMenuLayer() {
		return menuLayer;
	}

	public StringType getTitle() {
		return title;
	}

	public void setBrowserInstance(StringType browserInstance) {
		this.browserInstance = browserInstance;
	}

	public void setCode(StringType code) {
		this.code = code;
	}

	public void setCommandName(StringType commandName) {
		this.commandName = commandName;
	}

	public void setCommandType(StringType commandType) {
		this.commandType = commandType;
	}

	public void setIsDefault(BooleanType isDefault) {
		this.isDefault = isDefault;
	}

	public void setIsOnline(BooleanType isOnline) {
		this.isOnline = isOnline;
	}

	public void setMenuLayer(IntegerType menuLayer) {
		this.menuLayer = menuLayer;
	}

	public void setTitle(StringType title) {
		this.title = title;
	}

	public StringType getRoles() {
		return roles;
	}

	public void setRoles(StringType roles) {
		this.roles = roles;
	}

	public boolean hasChilds() {
		return this.hasChilds;
	}

	public void setHasChilds(boolean hasChilds) {
		this.hasChilds = hasChilds;
	}

	public BooleanType getIsMultiTaskMode() {
		return isMultiTaskMode;
	}

	public void setIsMultiTaskMode(BooleanType isMultiTaskMode) {
		this.isMultiTaskMode = isMultiTaskMode;
	}

}
