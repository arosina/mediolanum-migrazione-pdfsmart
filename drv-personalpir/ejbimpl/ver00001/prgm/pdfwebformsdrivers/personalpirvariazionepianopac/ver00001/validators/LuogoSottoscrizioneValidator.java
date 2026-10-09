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
public class LuogoSottoscrizioneValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public LuogoSottoscrizioneValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		
		if (getPdfDriver().ctrl_luogo(csc, pdfData, Costanti.LUOGO)) {
			StringType luogo = dataHelper.getLuogo();
			if (luogo.getStringValue().toUpperCase().contains(Costanti.SAN_MARINO)) {
				PdfCodedMessage.addTypeError(luogo, Costanti.TE_LUOGO_NON_ITALIANO);
			} 
		}
	}
}
