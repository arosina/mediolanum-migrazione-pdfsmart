package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.model.SaldoContoModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
/** Richiamato solo in fase di accettazione proposta Copernico**/
public class SaldoValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public SaldoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		DoubleType importoInvestito = new DoubleType();
		StringType iban = new StringType();

		if (dataHelper.getTipoIntestazioneContoSDDBMEDVariazione().equals("INTESTATARIO") &&
			dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione().equals("CC")){
			iban = dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione();
			importoInvestito = dataHelper.getImportoVariazioneSDD();
		}

		SaldoContoModel saldo = getPdfDriver().readSaldoConto(csc, iban);
		if (saldo.isNotFound()) {			
			PdfCodedMessage.addError(result, Costanti.E_PROBLEMI_NEL_DETERMINARE_SALDO_CONTO);
		} else {
			if (saldo.getSaldoDisponibile().doubleValue() < importoInvestito.doubleValue()) {				
				PdfCodedMessage.addError(result, Costanti.E_IMPORTO_MAGGIORE_SALDO);
			}
		}
	}
}
