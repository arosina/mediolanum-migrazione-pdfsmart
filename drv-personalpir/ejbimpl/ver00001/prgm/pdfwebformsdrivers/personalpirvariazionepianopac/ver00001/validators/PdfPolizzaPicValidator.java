package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.util.ArrayList;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class PdfPolizzaPicValidator extends AbstractPdfValidator<PdfDriver> {
	public PdfPolizzaPicValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
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
		innerValidators.add(new ClienteBloccatoValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new ResidenzaFisicaPaeseAltoRischioValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new AgenteAbilitatoAlCollocamentoValidator(getPdfDriver(), getEventSender()));

		innerValidators.add(new RevocaVersamentoPremioAggiuntivoValidator(getPdfDriver(), getEventSender()));
		innerValidators.add(new FirmePolizzaPicValidator(getPdfDriver(), getEventSender()));

		return innerValidators;
	}
}
