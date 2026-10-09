package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class AbilitazioneFondiValidator extends AbstractPdfValidator<PdfDriver> {	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbilitazioneFondiValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		for (int i=0;;i++){
			StringType isinFondo = (StringType)pdfData.read("isinFondoPremio"+i);

			if (isinFondo == null)
				break;
			if (isinFondo.isNull())
				continue;

			if (!Utility.isFondoAbilitato(csc, isinFondo.toString())) {
				if (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF) {
					PdfCodedMessage.addError(result, Costanti.E_OICR_OGGETTO_SALVAGUARDIA_E_MONITORAGGIO);
				}
				else {
					PdfCodedMessage.addTypeError(isinFondo, Costanti.TE_OICR_OGGETTO_SALVAGUARDIA_E_MONITORAGGIO);
				}				
			}
		}
	}
}
