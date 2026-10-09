package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.TypeError;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.controller.TerzoPagatore;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class TerzoPagatoreValidator extends AbstractPdfValidator<PdfDriver> {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public TerzoPagatoreValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		TerzoPagatore.checkRelazioneTerzoPagatore(pdfData, "tipoIntestazioneContoSDDBMEDVariazione", "ALTROCLIENTE", "ContraenteTerzoPagatore");
		
		// Gestione extra per mancanza del code nel TypeError delle polizzeutil (CAMPO OBBLIGATORIO)
		overrideError(PdfDataHelper.FieldNames.TIPORELAZIONECONTRAENTETERZOPAGATORE, pdfData);
		overrideError(PdfDataHelper.FieldNames.DESCRIZIONETIPORELAZIONECONTRAENTETERZOPAGATORE, pdfData);
	}

	private void overrideError(String fieldName, PdfDataModel pdfData) throws PdfAcroFieldNotFoundException {
		AbstractType element = pdfData.readProperty(fieldName);
		if (element != null && element.getTypeErrors() != null && element.getTypeErrors().size() > 0) {
			List<TypeError> errors = element.getTypeErrors();
			for(TypeError err: errors) {
				if(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO.equalsIgnoreCase(err.toString())){
					errors.remove(err);
					PdfCodedMessage.addTypeError(pdfData.read(fieldName),Costanti.TE_CAMPO_OBBLIGATORIO);
				}
			}
		}
	}
	
}
