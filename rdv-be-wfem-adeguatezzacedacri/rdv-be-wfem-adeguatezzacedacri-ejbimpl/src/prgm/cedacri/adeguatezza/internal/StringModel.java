package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class StringModel extends CommandDataModel {
	public static final long serialVersionUID = 0;
	public StringType  valore = new StringType("");
	public StringModel()
	{
		super();
	}
	public StringModel(String init)
	{
		super();
		setValore(new StringType(init));
	}
	public StringModel(StringType init)
	{
		super();
		setValore(init);
	}
	public StringType getValore() {
		return valore;
	}
	public void setValore(StringType valore) {
		this.valore = valore;
	}
	public void concat(String addon)
	{
		setValore(new StringType(getValore().getStringValue() + addon));
	}
}
