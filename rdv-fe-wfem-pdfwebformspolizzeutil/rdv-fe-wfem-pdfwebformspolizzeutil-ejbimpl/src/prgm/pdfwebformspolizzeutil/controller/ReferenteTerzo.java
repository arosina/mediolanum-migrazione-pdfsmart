package prgm.pdfwebformspolizzeutil.controller;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformspolizzeutil.ReferenteTerzoUtil;

public class ReferenteTerzo {
	private ReferenteTerzo() {
		throw new IllegalStateException("Utility class");
	}
	
	public static void checkReferenteTerzo(ClientSessionContext csc, PdfDataModel pdfDataModel, boolean assicurando, int posizioneAssicurando)
			throws Exception {
		try {
			ReferenteTerzoUtil.checkReferenteTerzo(csc, pdfDataModel, assicurando, posizioneAssicurando);
		} catch (DAOException daoe) {
			throw new Exception(daoe);
		}
	}
}
