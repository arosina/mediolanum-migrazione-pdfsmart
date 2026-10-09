package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.util.Calendar;
import java.util.Date;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutoCompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutocomplete;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class FirmeValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FirmeValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		result.setFieldsToRemoveManagedByDriver(true);

		StringType isVariazioneDisposizione = dataHelper.getIsVariazioneDisposizioneSDD();
		StringType tipoSospensioneRevocaRiattivazione = dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD();
		StringType isVariazioneRipartizione = dataHelper.getIsVariazioneRipartizione();
		if (isVariazioneDisposizione.isNull() && tipoSospensioneRevocaRiattivazione.isNull() && isVariazioneRipartizione.isNull()) {
			result.getFieldsToRemove().add("firma3Cliente1");
		}

		StringType isVariazioneConto = dataHelper.getIsVariazioneContoSDD();
		if (isVariazioneConto.isNull()) {
			result.getFieldsToRemove().add("firma2Cliente1");
			result.getFieldsToRemove().add("firma1Cliente1");
		}

		
		NumeroPolizzaAutoCompleteModel polizza = new NumeroPolizzaAutoCompleteModel();
		polizza.setNdgCliente(dataHelper.getNdgCliente1());
		polizza.setCodiceAgente(getPdfDriver().getPdf().getCodAgeFiltro());
		polizza.setNumeroPolizza(dataHelper.getNumeroPolizza());
		polizza.setCodProdottoPolizza(dataHelper.getCodProdottoPolizza());
		polizza.setNumeroContratto(dataHelper.getNumeroContratto());
		StringType numeroPolizza = dataHelper.getNumeroPolizza();
		try {
			NumeroPolizzaAutocomplete autoComplete = new NumeroPolizzaAutocomplete();
			ListType elencoPolizze = autoComplete.findElements(csc, polizza);
			if (elencoPolizze.size() == 0) {
				PdfCodedMessage.addTypeError(numeroPolizza, Costanti.TE_PROBLEMA_TECNICO_VERIFICA_POLIZZA);
			} else {
				polizza = (NumeroPolizzaAutoCompleteModel) elencoPolizze.get(0);
				DateType dataEmissione = polizza.getDataEmissione();
				if (dataEmissione.compareTo(dateObbligoFirmaDigitale()) > 0) {
					result.getFieldsToRemove().add("firma4Cliente1");
				}
			}
		} catch (DAOException de) {
			PdfCodedMessage.addTypeError(numeroPolizza, Costanti.TE_PROBLEMA_TECNICO_VERIFICA_POLIZZA);
		}		
	}

	private static DateType dateObbligoFirmaDigitale() {
		Calendar cDigital = Calendar.getInstance();
		cDigital.set(2018, 5, 18);
		Date dDate = new Date(cDigital.getTime().getTime());
		DateType dt = new DateType(dDate);
		return dt;
	}
}
