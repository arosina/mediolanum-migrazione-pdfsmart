package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.util.Tools;

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
public class DataSottoscrizioneValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DataSottoscrizioneValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		DateType dataSottoscrizione = dataHelper.getDataSottoscrizione();

		if (dataSottoscrizione == null || dataSottoscrizione.isNull())
			return;

		if(dataSottoscrizione.compareTo(Tools.today()) > 0) {
			PdfCodedMessage.addTypeError(dataSottoscrizione, Costanti.TE_DATA_SOTTOSCRIZIONE_FUTURA);
		}
	}
}
