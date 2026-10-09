package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class SddAttiviValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public SddAttiviValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		if (dataHelper.getNumeroPolizza().isNull())
			return;

		boolean hasSddAttivi = Utility.hasSddAttivi(csc, dataHelper);
		
		StringType isVariazioneDisposizione = dataHelper.getIsVariazioneDisposizioneSDD();
		if(isVariazioneDisposizione.equals("SI") && !hasSddAttivi) {		
			PdfCodedMessage.addTypeError(isVariazioneDisposizione, Costanti.TE_NESSUN_SDD_ATTIVO);			
		}
		StringType isVariazioneConto = dataHelper.getIsVariazioneContoSDD();
		if(isVariazioneConto.equals("SI") && !hasSddAttivi) {				
			PdfCodedMessage.addTypeError(isVariazioneConto, Costanti.TE_NESSUN_SDD_ATTIVO);			
		}

		StringType azione = dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD();
		if(azione.equals("SOSPENSIONE")) {
			if(!hasSddAttivi) {				
				PdfCodedMessage.addTypeError(azione, Costanti.TE_NESSUN_SDD_ATTIVO);				
			}
		}else if(azione.equals("REVOCA")) {
			if(!hasSddAttivi) {
				PdfCodedMessage.addTypeError(azione, Costanti.TE_NESSUN_SDD_ATTIVO);
			}
		}else if(azione.equals("RIATTIVAZIONE") && !Utility.hasSddSospesi(csc, dataHelper)) {				
			PdfCodedMessage.addTypeError(azione, Costanti.TE_NESSUN_SDD_SOSPESO);			
		}
	}
}
