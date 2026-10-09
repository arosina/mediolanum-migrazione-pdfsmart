package moke.menu2.login;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.login.LoginFailureInfo;
import com.atosorigin.wfem.login.LoginManager;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************************/
/*******************************************************************************/
public class LoginManagerImpl implements LoginManager {
	
    private com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	        
    
    private static final int UTENTE_SCONOSCIUTO = 10;

	/*******************************************************************************************************************/
	/*******************************************************************************************************************/
	public int executeChangePwd(String channel, String user, String password, String newPassword) throws Exception {
		LOG.info("Executing change password for user ["+user+"]");
		InputStream inputStream = this.getClass().getResourceAsStream("/passwd");
		Properties p = new Properties();
		p.load(inputStream);
		inputStream.close();
		DateType nuovaScadenza = Tools.today();
		nuovaScadenza.addMonths(1);
		p.setProperty(user.toLowerCase(),newPassword+","+nuovaScadenza.toString());
		p.store(new FileOutputStream("/WsadProjects/MokeMenuWeb/Web Content/WEB-INF/classes/passwd"),"");
		return LoginManager.CHANGE_PWD_CORRECT;
	}

	/*******************************************************************************************************************/
	/*******************************************************************************************************************/
	public int executeLogin(UserSessionContext userSessionContext,String password) throws Exception {
		InputStream inputStream = this.getClass().getResourceAsStream("/appsproperties/mokemenu/users");
		Properties p = new Properties();
		p.load(inputStream);
		inputStream.close();
		String linkedCode = p.getProperty(userSessionContext.getUserCode().toLowerCase());
		if(linkedCode == null)
			return UTENTE_SCONOSCIUTO;
		
		inputStream = this.getClass().getResourceAsStream("/passwd");
		if(inputStream == null)
			return LoginManager.LOGIN_CORRECT;
		
		p = new Properties();
		p.load(inputStream);
		inputStream.close();
		String pwd = p.getProperty(userSessionContext.getUserCode().toLowerCase());
		if(pwd == null)
			return LoginManager.LOGIN_CORRECT;
		DateType expirationDate = null;
		if(pwd != null){
			int idx = pwd.indexOf(",");
			if(idx >= 0){
				expirationDate = new DateType(pwd.substring(idx+1));				
				pwd = pwd.substring(0,idx);
			}
			
			if(password.equals(pwd)){
				if(expirationDate != null &&
					expirationDate.compareTo(Tools.today()) <= 0){
						return LoginManager.LOGIN_PWD_EXPIRED;
				}			
				return LoginManager.LOGIN_CORRECT;
			}else
			    return LoginManager.LOGIN_FAILED;
		}else
		    return LoginManager.LOGIN_FAILED;
	}

	public LoginFailureInfo getLoginFailureInfo() {
		return null;
	}

}
