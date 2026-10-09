package prgm.pdfwebformsutil.drivers.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.dao.scaiservices.schedascai.SchedaScaiModel;
import prgm.pdfwebformsutil.drivers.service.scaiservices.schedascai.SchedaScaiService;

/**************************************************************************************************
 * @author: Santoro Luca
 **************************************************************************************************/

public class SchedaScaiValidator<T extends PdfBaseDriver> extends AbstractPdfValidator<T> {
	private static final String MSG_ERRORE_PROBLEMA_TECNICO_LETTURA_SCHEDA_SCAI = "Problema tecnico nel recuperare i dati della scheda scai.";
	
	private static final String MSG_ERRORE_SCHEDA_SCAI_SCADUTA = "La scheda SCAI del cliente %s %s non è aggiornata. Per garantire l'operatività occorre aggiornare l'anagrafica.";	
	private static final String MSG_ERRORE_SCHEDA_SCAI_SCADUTA_COPERNICO = "E' necessario aggiornare i dati anagrafici per proseguire con l'operazione.";		
	private static final String MSG_ERRORE_SCHEDA_SCAI_IN_SCADENZA = "La scheda SCAI del cliente %s %s non è aggiornata. Per garantire l'operatività occorre aggiornare l'anagrafica.";
	private static final String MSG_ERRORE_SCHEDA_SCAI_IN_SCADENZA_COPERNICO = "E' necessario aggiornare i dati anagrafici per proseguire con l'operazione.";	
	private static final String MSG_ERRORE_SCHEDA_SCAI_BLOCCATA = "La scheda SCAI del cliente %s %s non è aggiornata. Per garantire l'operatività occorre aggiornare l'anagrafica.";
	private static final String MSG_ERRORE_SCHEDA_SCAI_BLOCCATA_COPERNICO = "E' necessario aggiornare i dati anagrafici per proseguire con l'operazione.";
	private int personIdx;
	private SchedaScaiService<PdfBaseDriver> service = null;
	protected String ndgClienteFieldName;
	protected String cognomeClienteFieldName;
	protected String nomeClienteFieldName;
	protected String codiceFiscalePartitaIvaClienteFieldName;
	@Deprecated
	protected boolean verificaDataScadenzaDocumento;

	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate,
			int personIdx) {
		super(pdfDriver, eventSender, resetTypeErrorsOnValidate);
		setPersonIdx(personIdx);
	}

	@Deprecated
	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate, int personIdx,
			boolean verificaDataScadenzaDoc) {
		super(pdfDriver, eventSender, resetTypeErrorsOnValidate);
		setPersonIdx(personIdx);
		this.verificaDataScadenzaDocumento = verificaDataScadenzaDoc;
	}

	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, int personIdx) {
		this(pdfDriver, eventSender, true, personIdx);
	}

	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate, int personIdx,
			SchedaScaiService<PdfBaseDriver> service) {
		this(pdfDriver, eventSender, resetTypeErrorsOnValidate, personIdx);
		this.service = service;
	}

	@Deprecated
	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate, int personIdx,
			SchedaScaiService<PdfBaseDriver> service, boolean verificaDataScadenzaDoc) {
		this(pdfDriver, eventSender, resetTypeErrorsOnValidate, personIdx);
		this.service = service;
		this.verificaDataScadenzaDocumento = verificaDataScadenzaDoc;
	}

	public SchedaScaiValidator(T pdfDriver, PdfEventEnum eventSender, int personIdx,
			SchedaScaiService<PdfBaseDriver> service) {
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
		StringType cognomeCliente = (StringType) pdfData.read(cognomeClienteFieldName);
		StringType nomeCliente = (StringType) pdfData.read(nomeClienteFieldName);

		if (ndgCliente == null) {
			throw new PdfAcroFieldNotFoundException(String.format("Field [ndgCliente%d] not found", getPersonIdx()));
		}
		
		if (cognomeCliente == null) {
			throw new PdfAcroFieldNotFoundException(String.format("Field [cognomeCliente%d] not found", getPersonIdx()));
		}
		
		if (nomeCliente == null) {
			throw new PdfAcroFieldNotFoundException(String.format("Field [nomeCliente%d] not found", getPersonIdx()));
		}

		if (!ndgCliente.isNull()) {
			if (service == null) {
				service = new SchedaScaiService<PdfBaseDriver>(getPdfDriver());
			}
			try {
				SchedaScaiModel schedaScaiModel = service.read(csc, pdfData, ndgCliente.toString());

				boolean clearCache = false;
				
				if (schedaScaiModel.isSchedaSCAIScaduta()) {
					addMsg(pdfData, MSG_ERRORE_SCHEDA_SCAI_SCADUTA_COPERNICO, String.format(MSG_ERRORE_SCHEDA_SCAI_SCADUTA, cognomeCliente, nomeCliente), false);
					clearCache = true;
				}
				else if (schedaScaiModel.isSchedaSCAIInScadenza()) {
					addMsg(pdfData, MSG_ERRORE_SCHEDA_SCAI_IN_SCADENZA_COPERNICO, String.format(MSG_ERRORE_SCHEDA_SCAI_IN_SCADENZA, cognomeCliente, nomeCliente), true);
					clearCache = true;
				}
				else if (schedaScaiModel.isSchedaSCAIBloccata()) {
					addMsg(pdfData, MSG_ERRORE_SCHEDA_SCAI_BLOCCATA_COPERNICO, String.format(MSG_ERRORE_SCHEDA_SCAI_BLOCCATA, cognomeCliente, nomeCliente), false);
					clearCache = true;
				}
				
				if (clearCache && service.isEnableCache()) {
					service.removeFromCache(pdfData, ndgCliente.toString());					
				}				
			} catch (Exception e) {
				addTypeError(pdfData, new String[] { ndgClienteFieldName, 
								       cognomeClienteFieldName, 
								       nomeClienteFieldName,
								       codiceFiscalePartitaIvaClienteFieldName }, MSG_ERRORE_PROBLEMA_TECNICO_LETTURA_SCHEDA_SCAI);
			}
		}
	}
	
	private void addMsg(PdfDataModel pdfData, String msgCliente, String msgAgente, boolean isWarning) {
		String msg;
		
		if (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF) {
			msg = msgCliente;			
		} else {
			msg = msgAgente;
		}

		if (isWarning) {
			getWarnings().add(msg);	
		}
		else {
			addTypeError(pdfData, new String[] { ndgClienteFieldName, 
									 cognomeClienteFieldName, 
									 nomeClienteFieldName, 
									 codiceFiscalePartitaIvaClienteFieldName },	msg);
		}	
	}
}
