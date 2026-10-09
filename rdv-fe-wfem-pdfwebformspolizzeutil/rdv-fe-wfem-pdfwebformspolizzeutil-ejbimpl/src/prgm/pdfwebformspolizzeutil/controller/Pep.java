package prgm.pdfwebformspolizzeutil.controller;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.PepUtil;

public class Pep {
	private Pep() {
		throw new IllegalStateException("Utility class");
	}
	
	public static void checkPep(PdfDataModel pdfDataModel, String prefissoBeneficiario) {
		PepUtil.checkPep(pdfDataModel, prefissoBeneficiario);
	}
	
	public static void checkPepCasoVita(PdfDataModel pdfDataModel) {
		checkPep(pdfDataModel, Constants.PREFISSO_BENEFICIARIO_VITA);
	}
	
	public static void checkPepCasoDecesso(PdfDataModel pdfDataModel) {
		checkPep(pdfDataModel, Constants.PREFISSO_BENEFICIARIO_DECESSO);
	}
	
	
	
	
}
