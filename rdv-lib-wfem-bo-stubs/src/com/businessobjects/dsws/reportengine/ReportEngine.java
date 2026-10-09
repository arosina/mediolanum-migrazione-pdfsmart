package com.businessobjects.dsws.reportengine;

import com.businessobjects.dsws.DSWSException;
import com.businessobjects.dsws.session.Session;

public class ReportEngine {
	public static ReportEngine getInstance(Session session, String url) throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}

	public DocumentInformation getDocumentInformation(String docId, RetrieveMustFillInfo mustFill, Action[] actions,
			Object unused, RetrieveData data) throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}

	public Image getImage(String docRef, String imageName) throws DSWSException {
		throw new UnsupportedOperationException("stub");
	}
}
