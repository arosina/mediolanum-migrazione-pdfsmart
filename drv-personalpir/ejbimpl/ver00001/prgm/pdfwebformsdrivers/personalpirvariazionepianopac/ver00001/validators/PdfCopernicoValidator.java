package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.util.ArrayList;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class PdfCopernicoValidator extends AbstractPdfValidator<PdfDriver> {
	public PdfCopernicoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
		
	@Override
	public ArrayList<AbstractPdfValidator<PdfDriver>> getInnerValidators() {
		ArrayList<AbstractPdfValidator<PdfDriver>> innerValidators = new ArrayList<>();
		innerValidators.add(new CollocazioneFondoValidator(getPdfDriver(), getEventSender()));
		
		
		return innerValidators;
	}
}
