package prgm.pdfwebformsutil.drivers.validators;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.AbstractType;

import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;

/**************************************************************************************************
 * @author: Santoro Luca
 * 
 *          Classe astratta generica per gestire un controllo di validazione del
 *          pdf. Derivare i validator facendo l'override del methodo doValidate.
 *          Effettuando l'override del methodo addInnerValidators, aggiungendo i
 *          validator alla lista innerValidators può essere utilizzata come
 *          container di validatori da eseguire in sequenza. Il metodo validate
 *          esegue doValidate su tutti gli innerValidators e poi esegue la
 *          doValidate della regola
 * 
 **************************************************************************************************/

public abstract class AbstractPdfValidator<T extends PdfBaseDriver> {
	private T pdfDriver = null;
	private PdfModel pdfModel;
	private ArrayList<String> warnings = new ArrayList<String>();
	private ArrayList<String> errors = new ArrayList<String>();
	private boolean resetTypeErrorsOnValidate = true;
	private PdfEventEnum eventSender;

	protected void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result)
			throws Exception {
	}

	public AbstractPdfValidator(T pdfDriver, PdfModel pdfModel, PdfEventEnum eventSender,
			boolean resetTypeErrorsOnValidate) {
		this.pdfDriver = pdfDriver;
		this.pdfModel = pdfModel;
		this.eventSender = eventSender;
		this.resetTypeErrorsOnValidate = resetTypeErrorsOnValidate;
	}

	public AbstractPdfValidator(T pdfDriver, PdfEventEnum eventSender) {
		this(pdfDriver, null, eventSender, true);
	}

	public AbstractPdfValidator(T pdfDriver, PdfModel pdfModel, PdfEventEnum eventSender) {
		this(pdfDriver, pdfModel, eventSender, true);
	}

	public AbstractPdfValidator(T pdfDriver, PdfEventEnum eventSender, boolean resetTypeErrorsOnValidate) {
		this(pdfDriver, null, eventSender, resetTypeErrorsOnValidate);
	}

	public PdfEventEnum getEventSender() {
		return eventSender;
	}

	public ArrayList<String> getWarnings() {
		return warnings;
	}

	public ArrayList<String> getErrors() {
		return errors;
	}

	public ArrayList<AbstractPdfValidator<T>> getInnerValidators() {
		return null;
	}

	public T getPdfDriver() {
		return pdfDriver;
	}
	
	public PdfModel getPdfModel() {
		return pdfModel;
	}

	public void setPdfModel(PdfModel pdfModel) {
		this.pdfModel = pdfModel;
	}

	public boolean isResetTypeErrorsOnValidate() {
		return resetTypeErrorsOnValidate;
	}

	public void setResetTypeErrorsOnValidate(boolean resetTypeErrorsOnValidate) {
		this.resetTypeErrorsOnValidate = resetTypeErrorsOnValidate;
	}

	public boolean isOnVerifyPdf() {
		return (getEventSender() == PdfEventEnum.VERIFY_PDF);
	}

	public boolean isOnVerifyCopernicoPdf() {
		return (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF);
	}

	public boolean isOnChangeEvent() {
		return (getEventSender() == PdfEventEnum.PAGE_ONCHANGE);
	}

	public void validate(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {
		this.validate(csc, pdfData, null);
	}

	public void validate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result)
			throws Exception {
		getWarnings().clear();
		getErrors().clear();

		ArrayList<AbstractPdfValidator<T>> innerValidators = getInnerValidators();
		if (innerValidators != null && !innerValidators.isEmpty()) {
			// Valido gli innerValidators
			for (AbstractPdfValidator<T> innerValidator : innerValidators) {
				validate(innerValidator, csc, pdfData, result);
			}
		}
		// Valido il validator
		doValidate(csc, pdfData, result);
	}

	public void validate(AbstractPdfValidator<T> validator, ClientSessionContext csc, PdfDataModel pdfData,
			AbstractBusinessEventOutputData result) throws Exception {
		validator.validate(csc, pdfData, result);
		getWarnings().addAll(validator.getWarnings());
		getErrors().addAll(validator.getErrors());
	}

	protected void addTypeError(PdfDataModel pdfData, AbstractType field, String errorMsg) {
		if (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF) {
			getErrors().add(errorMsg);
		} else {
			if (field != null) {
				if (isResetTypeErrorsOnValidate()) {
					field.resetTypeErrors();
				}
				field.addTypeError(errorMsg);
			}
		}
	}

	protected void addTypeError(PdfDataModel pdfData, String fieldName, String errorMsg) {
		AbstractType field = pdfData.read(fieldName);
		addTypeError(pdfData, field, errorMsg);
	}

	protected void addTypeError(PdfDataModel pdfData, ArrayList<String> fieldNames, String errorMsg) {
		addTypeError(pdfData, fieldNames.toArray(new String[fieldNames.size()]), errorMsg);
	}

	protected void addTypeError(PdfDataModel pdfData, String[] fieldNames, String errorMsg) {
		for (String fieldName : fieldNames) {
			addTypeError(pdfData, fieldName, errorMsg);
		}
	}
}
