package com.atosorigin.wfem.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/****************************************************************/
/****************************************************************/
public class MailAuthenticator extends Authenticator {
    
    private String user = "";
    private String password = "";

	/****************************************************************/
	/****************************************************************/
	protected PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(getUser(), getPassword());
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
