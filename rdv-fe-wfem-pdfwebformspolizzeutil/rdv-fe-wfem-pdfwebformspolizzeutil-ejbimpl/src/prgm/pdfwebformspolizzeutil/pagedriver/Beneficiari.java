package prgm.pdfwebformspolizzeutil.pagedriver;

import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.Utils;
import prgm.pdfwebformspolizzeutil.dao.BeneficiariDaoAccess;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;

public class Beneficiari {
	
	private Beneficiari() {
		throw new IllegalStateException("Utility class");
	}

	public static void onChangeCodiceClienteSezioneBeneficiario(ClientSessionContext csc, PageEventInputData input, PdfBaseDriver pdfBaseDriver) throws Exception {
		
		String[] eventArgs = input.getEventArgs().split(",");

		String prefissoBeneficiario = eventArgs[0];
		String indiceBeneficiario = eventArgs[1];

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		StringType codiceCliente = (StringType) input.getPdfData().read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		
		if (!codiceCliente.isNull()) {
			onChangeCodiceClienteSezioneBeneficiario(csc, input);
			pdfBaseDriver.ctrl_isClienteDeceduto(csc, input.getPdfData(), Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		}
	}
	
	public static void onChangeCodiceClienteSezioneBeneficiario(ClientSessionContext csc, PageEventInputData input) throws Exception {
		String[] eventArgs = input.getEventArgs().split(",");

		String prefissoBeneficiario = eventArgs[0];
		String indiceBeneficiario = eventArgs[1];
		boolean isTipoSoggettoPF = eventArgs[2].equals(Constants.TIPO_SOGGETTO_PF);
		boolean isTipoSoggettoPG = eventArgs[2].equals(Constants.TIPO_SOGGETTO_PG);				

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		StringType codiceCliente = (StringType) input.getPdfData().read("codiceCliente" + suffissoBeneficiario);
		StringType codiceAgente = Utils.getCodiceAgente(input.getPdfData());

		BeneficiarioModel beneficiarioModel = new BeneficiarioModel();
		beneficiarioModel.setCodiceCliente(codiceCliente);
		beneficiarioModel.setCodiceAgente(codiceAgente);
		BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc);
		try {
			beneficiarioModel = daoAccess.findBeneficiario(beneficiarioModel);
			String[] fieldNames = null;
			Map<String, String> map = null;
			if (isTipoSoggettoPF) {
				fieldNames = Utils.getFieldNames(Constants.BENEFICIARI_BASE_FIELD_NAME_PDF_PF,
						suffissoBeneficiario);
				map = Utils.getMapFieldNameDbValue(fieldNames,
						Constants.BENEFICIARI_FIELD_DB_MODEL_PF);
			} else if (isTipoSoggettoPG) {
				fieldNames = Utils.getFieldNames(Constants.BENEFICIARI_BASE_FIELD_NAME_PDF_PG,
						suffissoBeneficiario);
				map = Utils.getMapFieldNameDbValue(fieldNames,
						Constants.BENEFICIARI_FIELD_DB_MODEL_PG);
			}

			buildDatiSezione(input.getPdfData(), beneficiarioModel, map);
			
			
			if (isTipoSoggettoPG) {
				for (int i = 1; i <= 2; i++) {

					if (beneficiarioModel.getPropertyValue("codiceClienteTitolare" + i) != null) {
						StringType codiceClienteTitolare = (StringType) beneficiarioModel
								.getPropertyValue("codiceClienteTitolare" + i);

						String suffissoTitolare = Constants.TITOLARE + i + suffissoBeneficiario;
						fieldNames = Utils.getFieldNames(
								Constants.BENEFICIARI_BASE_FIELD_NAME_PDF_TIT, suffissoTitolare);
						map = Utils.getMapFieldNameDbValue(fieldNames,
								Constants.BENEFICIARI_FIELD_DB_MODEL_TIT);

						
						if (codiceClienteTitolare != null && !codiceClienteTitolare.equals("")) {

							BeneficiarioModel titolareModel = new BeneficiarioModel();
							titolareModel.setCodiceCliente(codiceClienteTitolare);

							titolareModel = daoAccess.findBeneficiario(titolareModel);
							
							StringType isGiaCliente = (StringType) input.getPdfData()
									.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
							isGiaCliente.setStringValue("S");

							buildDatiSezione(input.getPdfData(), titolareModel, map);

							
						} else {
							//ripulisco titolare corrente
							StringType isGiaCliente = (StringType) input.getPdfData()
									.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
							isGiaCliente.setStringValue(null);
							
							for (Map.Entry<String, String> entry : map.entrySet()) {
								String fieldPdfName = entry.getKey();

								AbstractType fieldPdfValue = input.getPdfData().read(fieldPdfName);

								if (fieldPdfValue instanceof StringType) {
									((StringType) fieldPdfValue).setStringValue(null);
								} else if (fieldPdfValue instanceof DateType) {
									((DateType) fieldPdfValue).setDateValue(null);
								}
							}
						}
					}
				}
			}
			
			valorizzaDatiDichiarazione(csc, input);

		} catch (DAOException e) {
			throw new Exception(e);
		}
	}

	private static void valorizzaDatiDichiarazione(ClientSessionContext csc, PageEventInputData input) throws Exception  {
		
		PdfDataModel pdfData = input.getPdfData();
		String[] eventArgs = input.getEventArgs().split(",");
		String suffissoBeneficiario = eventArgs[0] + eventArgs[1];
		boolean isTipoSoggettoPF = eventArgs[2].equals(Constants.TIPO_SOGGETTO_PF);
		boolean isTipoSoggettoPG = eventArgs[2].equals(Constants.TIPO_SOGGETTO_PG);				

		String[] fieldsNameDichiarazioneBeneficiari = null;
		String[] fieldsNameBeneficiari = null;
		
		if (isTipoSoggettoPF) {
			fieldsNameDichiarazioneBeneficiari = Utils.getFieldNames(Constants.DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PF, "Dichiarazione" + suffissoBeneficiario);
			fieldsNameBeneficiari = Utils.getFieldNames(Constants.DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PF,	suffissoBeneficiario);
		} else if (isTipoSoggettoPG) {
			fieldsNameDichiarazioneBeneficiari = Utils.getFieldNames(Constants.DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PG, "Dichiarazione" + suffissoBeneficiario);
			fieldsNameBeneficiari = Utils.getFieldNames(Constants.DICHIARAZIONI_BENEFICIARI_BASE_FIELD_PG,	suffissoBeneficiario);
		}
		
		if (fieldsNameDichiarazioneBeneficiari == null || pdfData.read(fieldsNameDichiarazioneBeneficiari[0]) == null)
			return;
		
		for (int i = 0; i < fieldsNameDichiarazioneBeneficiari.length; i++) {
			pdfData.write(fieldsNameDichiarazioneBeneficiari[i], pdfData.read(fieldsNameBeneficiari[i]));
		}
		prgm.pdfwebformspolizzeutil.Utils.recuperaDatiLegaleRapprProcuratore(csc, input);
	}

	public static void onChangeCodiceClienteSezioneTitolareBeneficiario(ClientSessionContext csc, PageEventInputData input, PdfBaseDriver pdfBaseDriver) throws Exception  {
		String[] eventArgs = input.getEventArgs().split(",");

		String prefissoBeneficiario = eventArgs[0];
		String indiceBeneficiario = eventArgs[1];
		String indiceTitolare = eventArgs[2];

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		StringType codiceCliente = (StringType) input.getPdfData().read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
		
		if (!codiceCliente.isNull()) {
			onChangeCodiceClienteSezioneTitolareBeneficiario(csc, input);
			pdfBaseDriver.ctrl_isClienteDeceduto(csc, input.getPdfData(), Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
		}
	}

	public static void onChangeCodiceClienteSezioneTitolareBeneficiario(ClientSessionContext csc,
			PageEventInputData input) throws Exception  {
		String[] eventArgs = input.getEventArgs().split(",");

		String prefissoBeneficiario = eventArgs[0];
		String indiceBeneficiario = eventArgs[1];
		String indiceTitolare = eventArgs[2];

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		StringType codiceCliente = (StringType) input.getPdfData().read("codiceCliente" + suffissoTitolare);

		BeneficiarioModel beneficiarioModel = new BeneficiarioModel();
		beneficiarioModel.setCodiceCliente(codiceCliente);

		BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc);
		try {
			beneficiarioModel = daoAccess.findBeneficiario(beneficiarioModel);
			String[] fieldNames = null;
			Map<String, String> map = null;

			fieldNames = Utils.getFieldNames(Constants.BENEFICIARI_BASE_FIELD_NAME_PDF_TIT,
					suffissoTitolare);
			map = Utils.getMapFieldNameDbValue(fieldNames, Constants.BENEFICIARI_FIELD_DB_MODEL_TIT);

			buildDatiSezione(input.getPdfData(), beneficiarioModel, map);

			StringType isGiaCliente = (StringType) input.getPdfData().read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
			isGiaCliente.setStringValue("S");

		} catch (DAOException e) {
			throw new Exception(e);
		}
	}
	
	
	static void buildDatiSezione(PdfDataModel pdfDataMdel, BeneficiarioModel beneficiarioModel,  Map<String, String> map) throws Exception {
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String fieldPdfName = entry.getKey();
			String fieldDbName = entry.getValue();

			AbstractType fieldPdfValue = pdfDataMdel.read(fieldPdfName);

			if (fieldPdfValue instanceof StringType) {
				if (beneficiarioModel.getPropertyValue(fieldDbName) != null) {
					pdfDataMdel.write(fieldPdfName, (StringType) beneficiarioModel.getPropertyValue(fieldDbName));
				} else {
					pdfDataMdel.write(fieldPdfName, null);
				}
			} else if (fieldPdfValue instanceof DateType) {
				if (beneficiarioModel.getPropertyValue(fieldDbName) != null) {
					pdfDataMdel.write(fieldPdfName, (DateType) beneficiarioModel.getPropertyValue(fieldDbName));

				} else {
					pdfDataMdel.write(fieldPdfName, null);

				}
			}

		}

	}

	
}
