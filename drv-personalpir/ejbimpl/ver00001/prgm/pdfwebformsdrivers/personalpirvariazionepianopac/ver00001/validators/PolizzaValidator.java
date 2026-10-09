package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class PolizzaValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PolizzaValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
				
		StringType numeroPolizza = dataHelper.getNumeroPolizza();
		if (numeroPolizza.isNull()){			
			PdfCodedMessage.addTypeError(numeroPolizza, Costanti.TE_SELEZIONARE_POLIZZA);
		}else if(pdfData.fieldExist(PdfDataHelper.FieldNames.FORMACONTRATTUALE) && dataHelper.getFormaContrattuale().isNull()) {
			// rfc #257299 - Se esiste la forma contrattuale allora è il nuovo modulo e la forma contrattuale deve essere impostata
			// o nell'onLoad o nell'autocomplete
			PdfCodedMessage.addTypeError(numeroPolizza, Costanti.TE_NO_FORMA_CONTR);
		}
	}
}
