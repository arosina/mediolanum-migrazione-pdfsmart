package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.BooleanType;

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
public class RevocaVersamentoPremioAggiuntivoValidator extends AbstractPdfValidator<PdfDriver> {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RevocaVersamentoPremioAggiuntivoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		BooleanType revocaVersamentoPremioAggiuntivo = dataHelper.getRevocaVersamentoPremioAggiuntivo();
		if(!revocaVersamentoPremioAggiuntivo.booleanValue())
			PdfCodedMessage.addTypeError(revocaVersamentoPremioAggiuntivo, Costanti.TE_CAMPO_OBBLIGATORIO);
	}
}
