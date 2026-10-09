package com.businessobjects.dsws.session;

import com.businessobjects.dsws.Connection;
import com.businessobjects.dsws.DSWSException;

public class Session {
	public Session(Connection connection) {
	}

	public void login(EnterpriseCredential credential) throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}

	public void logout() throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}

	public String[] getAssociatedServicesURL(String service) throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}
}
