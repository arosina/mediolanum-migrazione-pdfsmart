package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.util.ArrayList;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class PdfValidator extends AbstractPdfValidator<PdfDriver> {
	public PdfValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
		
	@Override
	public ArrayList<AbstractPdfValidator<PdfDriver>> getInnerValidators() {

		ArrayList<AbstractPdfValidator<PdfDriver>> innerValidators = new ArrayList<>();
		innerValidators.add(new AgenteValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new LuogoSottoscrizioneValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new DataSottoscrizioneValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new ContraenteValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new PolizzaValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new TipoVariazioneValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new SddAttiviValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new FirmeValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new TerzoPagatoreValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new ClienteBloccatoValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new ResidenzaFisicaPaeseAltoRischioValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new AgenteAbilitatoAlCollocamentoValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new CollocazioneFondoValidator(getPdfDriver(), getEventSender()));

		return innerValidators;
	}
}
