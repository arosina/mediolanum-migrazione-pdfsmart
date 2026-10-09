package prgm.pdfwebformspolizzeutil.controller;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformspolizzeutil.BeneficiariUtil;
import prgm.pdfwebformspolizzeutil.Constants;

public class Beneficiari {

	private Beneficiari() {
		throw new IllegalStateException("Utility class");
	}

	//*************************************************************************************************
	//*************************************************************************************************
	public static void checkConAggiuntiviBeneficiariCasoVita(ClientSessionContext csc, PdfModel pdf, PdfBaseDriver pdfBasedriver, boolean assicurando, int posizioneAssicurando)throws Exception {
		
		PdfDataModel pdfDataModel = pdf.getPdfData();
		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_VITA;
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.checkAggiuntiviBeneficiariNominativi(csc, pdf, pdfBasedriver, assicurando, posizioneAssicurando, prefissoBeneficiario);
		}
		
		checkBeneficiariCasoVita(csc, pdfDataModel, assicurando, posizioneAssicurando);
	}
	
	public static void checkBeneficiariCasoVita(ClientSessionContext csc, PdfDataModel pdfDataModel,
			boolean assicurando, int posizioneAssicurando) throws Exception {

		checkBeneficiari(csc, pdfDataModel, assicurando, posizioneAssicurando, Constants.PREFISSO_BENEFICIARIO_VITA, Constants.TIPO_CASO_BENEFICARIO_VITA, new DoubleType(0), null);
	}
	
	//*************************************************************************************************
	//*************************************************************************************************
	public static void checkConAggiuntiviBeneficiariCasoVita(ClientSessionContext csc, PdfModel pdf, PdfBaseDriver pdfBasedriver,
			boolean assicurando, int posizioneAssicurando, DoubleType percentualeRiservata, String messaggioErrore) throws Exception {

		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_VITA;
		PdfDataModel pdfDataModel = pdf.getPdfData();
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.checkAggiuntiviBeneficiariNominativi(csc, pdf, pdfBasedriver, assicurando, posizioneAssicurando, prefissoBeneficiario);
		}
		
		checkBeneficiariCasoVita(csc, pdfDataModel, assicurando, posizioneAssicurando, percentualeRiservata, messaggioErrore);		
	}
	
	public static void checkBeneficiariCasoVita(ClientSessionContext csc, PdfDataModel pdfDataModel,
			boolean assicurando, int posizioneAssicurando, DoubleType percentualeRiservata, String messaggioErrore) throws Exception {

		checkBeneficiari(csc, pdfDataModel, assicurando, posizioneAssicurando, Constants.PREFISSO_BENEFICIARIO_VITA, Constants.TIPO_CASO_BENEFICARIO_VITA, percentualeRiservata, messaggioErrore);
	}

	//*************************************************************************************************
	//*************************************************************************************************
	public static void checkConAggiuntiviBeneficiariCasoDecesso(ClientSessionContext csc, PdfModel pdf, PdfBaseDriver pdfBasedriver, boolean assicurando, int posizioneAssicurando)throws Exception {
		
		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO;
		PdfDataModel pdfDataModel = pdf.getPdfData();
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.checkAggiuntiviBeneficiariNominativi(csc, pdf, pdfBasedriver, assicurando, posizioneAssicurando, prefissoBeneficiario);
		}
		
		checkBeneficiariCasoDecesso(csc, pdfDataModel, assicurando, posizioneAssicurando);
	}
	
	public static void checkBeneficiariCasoDecesso(ClientSessionContext csc, PdfDataModel pdfDataModel,boolean assicurando, int posizioneAssicurando) throws Exception {

		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + Constants.PREFISSO_BENEFICIARIO_DECESSO);

		if (tipoBeneficiario.isNull()) {
			tipoBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else {
			checkBeneficiari(csc, pdfDataModel, assicurando,posizioneAssicurando, Constants.PREFISSO_BENEFICIARIO_DECESSO, Constants.TIPO_CASO_BENEFICARIO_DECESSO, new DoubleType(0), null);
		}

	}

	//*************************************************************************************************
	//*************************************************************************************************
	public static void checkConAggiuntiviBeneficiariCasoDecesso(ClientSessionContext csc, PdfModel pdf, PdfBaseDriver pdfBasedriver,
			boolean assicurando, int posizioneAssicurando, DoubleType percentualeRiservata, String messaggioErrore) throws Exception {

		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO;
		PdfDataModel pdfDataModel = pdf.getPdfData();
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.checkAggiuntiviBeneficiariNominativi(csc, pdf, pdfBasedriver, assicurando, posizioneAssicurando, prefissoBeneficiario);
		}
		
		checkBeneficiariCasoDecesso(csc, pdfDataModel, assicurando, posizioneAssicurando, percentualeRiservata, messaggioErrore);		
	}
	
	public static void checkBeneficiariCasoDecesso(ClientSessionContext csc, PdfDataModel pdfDataModel,
			boolean assicurando, int posizioneAssicurando, DoubleType percentualeRiservata, String messaggioErrore) throws Exception {

		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + Constants.PREFISSO_BENEFICIARIO_DECESSO);

		if (tipoBeneficiario.isNull()) {
			tipoBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else {
			checkBeneficiari(csc, pdfDataModel, assicurando,posizioneAssicurando,Constants.PREFISSO_BENEFICIARIO_DECESSO, Constants.TIPO_CASO_BENEFICARIO_DECESSO, percentualeRiservata, messaggioErrore);
		}

	}

	//*************************************************************************************************
	//*************************************************************************************************
	public static void checkBeneficiari(ClientSessionContext csc, PdfDataModel pdfDataModel, boolean assicurando, int posizioneAssicurando,
			String prefissoBeneficiario, String tipoCasoBeneficiario, DoubleType percentualeRiservata, String messaggioErrore) throws Exception {

		StringType tipoBeneficiario = (StringType) pdfDataModel
				.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		Set<StringType> beneficiari = new HashSet<StringType>();

		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			if (isAlmenoUnBeneficiarioCompilato(pdfDataModel, prefissoBeneficiario)) {
				for (int i = 1;; i++) {
					String suffissoBeneficiario = prefissoBeneficiario + i;
					if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
						break;
					}
	
					try {
						BeneficiariUtil.checkBeneficiarioNominativo(csc, pdfDataModel, assicurando, posizioneAssicurando, prefissoBeneficiario, i,
								beneficiari, tipoCasoBeneficiario);
					} catch (DAOException daoe) {
						throw new Exception(daoe);
					}
	
				}
	
				DoubleType percentualeBeneficiario = (DoubleType) pdfDataModel.read(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario+"1");
				if (percentualeBeneficiario != null) {
					//Se non ho la percentuale non devo controllare il totale
					double percentualeTotale = 100;
					
					if (!percentualeRiservata.isNull()
							&& percentualeRiservata.doubleValue() > 0)
						percentualeTotale -= percentualeRiservata.doubleValue();
				
					if (getTotalePercentuali(pdfDataModel, prefissoBeneficiario) != percentualeTotale) {
						if (messaggioErrore != null)
							tipoBeneficiario.addTypeError(messaggioErrore);
						else
							tipoBeneficiario.addTypeError("Il totale delle percentuali indicate non corrisponde al 100%.");
					}
				}
				
			} else {
				tipoBeneficiario.addTypeError("E' necessario compilare almeno un beneficiario.");
			}
		} 

	}

	private static boolean isAlmenoUnBeneficiarioCompilato(PdfDataModel pdfDataModel, String prefissoBeneficiario) {

		int numeroBeneficiariCompilati = 0;

		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			StringType isGiaCliente = (StringType) pdfDataModel
					.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

			if (!isGiaCliente.isNull()) {
				numeroBeneficiariCompilati++;
			}

		}

		return numeroBeneficiariCompilati != 0 ;
	}

	private static double getTotalePercentuali(PdfDataModel pdfDataModel, String prefissoBeneficiario) {

		BigDecimal sommaPerc = BigDecimal.ZERO;
		
		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			StringType isGiaCliente = (StringType) pdfDataModel
					.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

			if (!isGiaCliente.isNull()) {
				DoubleType percentualeBeneficiario = (DoubleType) pdfDataModel
						.read(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				if (!percentualeBeneficiario.isNull()) {
					BigDecimal percentuale = BigDecimal.valueOf(percentualeBeneficiario.doubleValue());
					sommaPerc = sommaPerc.add(percentuale);
				}

			}

		}

		return sommaPerc.doubleValue();
	}

	

}
