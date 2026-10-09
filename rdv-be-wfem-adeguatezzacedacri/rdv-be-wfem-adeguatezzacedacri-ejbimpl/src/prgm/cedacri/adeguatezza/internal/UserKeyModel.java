package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class UserKeyModel extends CommandDataModel
{
	private static final long serialVersionUID = 0;
	private StringType ndgDoss  = new StringType();
	private StringType ndgTemp  = new StringType();
	private TimestampType userKey  = new TimestampType();
	
	public StringType getNdgDoss() {
		return ndgDoss;
	}
	public StringType getNdgTemp() {
		return ndgTemp;
	}
	public TimestampType getUserKey() {
		return userKey;
	}
	public void setNdgDoss(StringType ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public void setNdgTemp(StringType ndgTemp) {
		this.ndgTemp = ndgTemp;
	}
	public void setUserKey(TimestampType userKey) {
		this.userKey = userKey;
	} 

}
