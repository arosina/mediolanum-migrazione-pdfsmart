package prgm.pdfwebformsutil.drivers.rules;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;

/**************************************************************************************************
 * @author: Santoro Luca
 * 
 *          Classe astratta generica per gestire una regola di business del pdf.
 *          Derivare le regole facendo l'override del methodo doProcess.
 *          Effettuando l'override del methodo addInnerRules, aggiungendo le
 *          regole alla lista innerRules può essere utilizzata come container di
 *          regole, da eseguire in sequenza. Il metodo process esegue doProcess
 *          su tutte le innerRules e poi esegue la doProcess della regola
 * 
 **************************************************************************************************/

public abstract class AbstractPdfRule<T extends PdfBaseDriver> {
	private T pdfDriver = null;
	private PdfEventEnum eventSender;

	protected void doProcess(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {
	}

	public AbstractPdfRule(T pdfDriver, PdfEventEnum eventSender) {
		setPdfDriver(pdfDriver);
		setEventSender(eventSender);
	}

	public T getPdfDriver() {
		return pdfDriver;
	}

	private void setPdfDriver(T pdfDriver) {
		this.pdfDriver = pdfDriver;
	}

	public ArrayList<AbstractPdfRule<T>> getInnerRules() {
		return null;
	}

	public PdfEventEnum getEventSender() {
		return eventSender;
	}

	private void setEventSender(PdfEventEnum eventSender) {
		this.eventSender = eventSender;
	}

	public boolean isOnVerifyPdf() {
		return (getEventSender() == PdfEventEnum.VERIFY_PDF);
	}

	public boolean isOnChangeEvent() {
		return (getEventSender() == PdfEventEnum.PAGE_ONCHANGE);
	}

	public void process(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {
		ArrayList<AbstractPdfRule<T>> innerRules = getInnerRules();
		if (innerRules != null && !innerRules.isEmpty()) {
			// processo le innerRules
			for (AbstractPdfRule<T> innerRule : innerRules) {
				innerRule.process(csc, pdfData);
			}
		}

		// processo la rule
		doProcess(csc, pdfData);
	}
}
