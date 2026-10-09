package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/****************************************************************/
/****************************************************************/
public class Application extends CommandDataModel{

	private StringType	 code = new StringType();
	private StringType   title = new StringType();
	private StringType	 description = new StringType();
	private StringType	 image = new StringType();
	private StringType	 version = new StringType();
	private BooleanType  isDefault = new BooleanType();
	private BooleanType  isOnline = new BooleanType();

	private ListType     functions = new ListType(Function.class);
	
	/****************************************************************/
	/****************************************************************/
	public Application(){
	}
	
	/****************************************************************/
	/****************************************************************/
	public Application(String code,
						String  title,
						String	 description,
						String	 image,
						String	 version,
						boolean isDefault,
						boolean isOnline){
		this.code = new StringType(code);
		this.title = new StringType(title);
		this.description = new StringType(description);
		this.image = new StringType(image);
		this.version = new StringType(version);
		this.isDefault = new BooleanType(isDefault);
		this.isOnline = new BooleanType(isOnline);
	}
	
	public StringType getCode() {
		return code;
	}

	public StringType getDescription() {
		return description;
	}

	public StringType getImage() {
		return image;
	}

	public BooleanType getIsDefault() {
		return isDefault;
	}

	public BooleanType getIsOnline() {
		return isOnline;
	}

	public StringType getTitle() {
		return title;
	}

	public StringType getVersion() {
		return version;
	}

	public void setCode(StringType code) {
		this.code = code;
	}

	public void setDescription(StringType description) {
		this.description = description;
	}

	public void setImage(StringType image) {
		this.image = image;
	}

	public void setIsDefault(BooleanType isDefault) {
		this.isDefault = isDefault;
	}

	public void setIsOnline(BooleanType isOnline) {
		this.isOnline = isOnline;
	}

	public void setTitle(StringType title) {
		this.title = title;
	}

	public void setVersion(StringType version) {
		this.version = version;
	}

	public ListType getFunctions() {
		return functions;
	}

	public void setFunctions(ListType functions) {
		this.functions = functions;
	}

}
