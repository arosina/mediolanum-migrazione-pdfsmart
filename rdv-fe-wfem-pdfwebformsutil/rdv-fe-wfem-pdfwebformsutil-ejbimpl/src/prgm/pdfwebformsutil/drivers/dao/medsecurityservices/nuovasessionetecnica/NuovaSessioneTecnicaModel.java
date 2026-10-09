package prgm.pdfwebformsutil.drivers.dao.medsecurityservices.nuovasessionetecnica;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class NuovaSessioneTecnicaModel extends CommandDataModel{
	public static final String KEY_HEADER = "VKEY=";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType applicationId = new StringType();		
	private StringType userId = new StringType();
	private StringType expirationTime = new StringType();
	private StringType userKey = new StringType();
	private IntegerType resultCode = new IntegerType();
    
	public StringType getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(StringType applicationId) {
		this.applicationId = applicationId;
	}
	public StringType getUserId() {
		return userId;
	}
	public void setUserId(StringType userId) {
		this.userId = userId;
	}
	public StringType getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(StringType expirationTime) {
		this.expirationTime = expirationTime;
	}
	public IntegerType getResultCode() {
		return resultCode;
	}
	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
	public StringType getUserKey() {
		return userKey;
	}
	public void setUserKey(StringType userKey) {
		this.userKey = userKey;
	}
	public StringType getWebCookie() {
		if (getUserKey().isNull()) {
			return new StringType();
		}
		else { 
		   return new StringType(KEY_HEADER.concat(getUserKey().toString().split(";")[0]));
		}
	}
}
