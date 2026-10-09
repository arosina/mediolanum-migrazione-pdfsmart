package prgm.pdfwebformsutil.drivers.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.dao.fatcaservices.fatcainformation.FatcaInformationModel;
import prgm.pdfwebformsutil.drivers.service.fatcaservices.fatcainformation.FatcaInformationService;

/**************************************************************************************************
 * @author: Santoro Luca
 **************************************************************************************************/

public class FatcaValidator<T extends PdfBaseDriver> extends AbstractPdfValidator<T> {
	private static final String MSG_ERRORE_PROBLEMA_TECNICO_LETTURA_FATCA = "Problema tecnico nel recuperare i dati Fatca.";
	private static final String MSG_ERRORE_FATCA_COPERNICO = "L' operazione non è consentita. Contatta il tuo Family Banker per maggiori informazioni.";
	private int personIdx;
	private FatcaInformationService<PdfBaseDriver> service = null;
	protected String ndgClienteFieldName;
	protected String cognomeClienteFieldName;
	protected String nomeClienteFieldName;
	protected String codiceFiscalePartitaIvaClienteFieldName;

	public FatcaValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate, int personIdx) {
		super(pdfDriver, eventSender, resetTypeErrorsOnValidate);
		setPersonIdx(personIdx);
	}

	public FatcaValidator(T pdfDriver, PdfEventEnum eventSender, int personIdx) {
		this(pdfDriver, eventSender, true, personIdx);
	}

	public FatcaValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate, int personIdx,
			FatcaInformationService<PdfBaseDriver> fatcaInformationService) {
		this(pdfDriver, eventSender, resetTypeErrorsOnValidate, personIdx);
		this.service = fatcaInformationService;
	}

	public FatcaValidator(T pdfDriver, PdfEventEnum eventSender, int personIdx,
			FatcaInformationService<PdfBaseDriver> service) {
		this(pdfDriver, eventSender, true, personIdx, service);
	}

	public int getPersonIdx() {
		return personIdx;
	}

	public final void setPersonIdx(int personIdx) {
		this.personIdx = personIdx;
		this.ndgClienteFieldName = String.format("ndgCliente%d", personIdx);
		this.cognomeClienteFieldName = String.format("cognomeCliente%d", personIdx);
		this.nomeClienteFieldName = String.format("nomeCliente%d", personIdx);
		this.codiceFiscalePartitaIvaClienteFieldName = String.format("codiceFiscalePartitaIvaCliente%d", personIdx);
	}

	@Override
	protected void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result)
			throws Exception {
		StringType ndgCliente = (StringType) pdfData.read(ndgClienteFieldName);
		StringType tipoSoggetto = new StringType("C");

		if (ndgCliente == null) {
			throw new PdfAcroFieldNotFoundException(String.format("Field [ndgCliente%d] not found", getPersonIdx()));
		}

		if (!ndgCliente.isNull()) {
			if (service == null) {
				service = new FatcaInformationService<PdfBaseDriver>(getPdfDriver());
			}
			try {
				FatcaInformationModel fatcaInfoModel = service.read(csc, pdfData, ndgCliente, tipoSoggetto);

				if (fatcaInfoModel.isInValutazione() || fatcaInfoModel.isUSPerson()) {
					if (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF) {
						addTypeError(
								pdfData, new String[] { ndgClienteFieldName, cognomeClienteFieldName,
										nomeClienteFieldName, codiceFiscalePartitaIvaClienteFieldName },
								MSG_ERRORE_FATCA_COPERNICO);
					}

					if (service.isEnableCache() && fatcaInfoModel.isInValutazione()) {
						service.removeFromCache(pdfData, fatcaInfoModel);
					}
				}
			} catch (Exception e) {
				addTypeError(pdfData, new String[] { ndgClienteFieldName, cognomeClienteFieldName, nomeClienteFieldName,
						codiceFiscalePartitaIvaClienteFieldName }, MSG_ERRORE_PROBLEMA_TECNICO_LETTURA_FATCA);
			}
		}
	}
}
