package moke.menu2.login;

import java.io.InputStream;
import java.util.Properties;

import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.command.UserSessionContextFactory;
import com.atosorigin.wfem.util.Tools;

public class UserContextFactory implements UserSessionContextFactory {

	/*******************************************************************************/
	/*******************************************************************************/
	public UserSessionContext createUserSessionContext(UserSessionContext initialUserSessionContext) throws Exception {
		InputStream inputStream = this.getClass().getResourceAsStream("/appsproperties/mokemenu/users");
		Properties p = new Properties();
		p.load(inputStream);
		inputStream.close();

		String userCode = initialUserSessionContext.getUserCode();
		String linkedCode = p.getProperty(userCode.toLowerCase());
		int idx = linkedCode.indexOf(",");
		if(idx >= 0)
			linkedCode = linkedCode.substring(0,idx);

		// Fillo di zeri
		String filledUserCode = Tools.fillSx(userCode,'0',10).toUpperCase();
		String filledLinkedCode = Tools.fillSx(linkedCode,'0',10).toUpperCase();
		
		initialUserSessionContext.setUserCode(filledUserCode);
		initialUserSessionContext.setCurrentLinkedUserCode(filledLinkedCode);
		initialUserSessionContext.getClientSessionContext().setUserCode(filledUserCode);
		initialUserSessionContext.getClientSessionContext().setCurrentLinkedUserCode(filledLinkedCode);
		
//		PrgmUserSessionContext uc = new PrgmUserSessionContext();
		UserSessionContext uc = new UserSessionContext();
		uc.setChannelCode(initialUserSessionContext.getChannelCode());
		uc.setCountryCode(initialUserSessionContext.getCountryCode());
		uc.setLangCode(initialUserSessionContext.getLangCode());
		uc.setStartUrl(initialUserSessionContext.getStartUrl());
		uc.setClientIp(initialUserSessionContext.getClientIp());
		uc.setUserCode(initialUserSessionContext.getUserCode());		
		uc.setCurrentLinkedUserCode(initialUserSessionContext.getCurrentLinkedUserCode());
		uc.setClientSessionContext(initialUserSessionContext.getClientSessionContext());
		
		return uc;
	}

}
