package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;

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
public class ImportoVariazioneValidator extends AbstractPdfValidator<PdfDriver> {

	public static final int IMPORTO_MINIMO_RATA = 100;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ImportoVariazioneValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		//recupero frequenza
		int frequenza = Utility.getFrequenzaPianoPac(csc, pdfData);

		if(frequenza > 0) {
			//recupero minimo 
			double minimo = getImportoMinimoRata();
	
			//recupero massimo in base  frequenza
			double massimo = getImportoMassimoRata(frequenza);
	
			//controllo
			DoubleType importo = dataHelper.getImportoVariazioneSDD(); 
			if(importo.doubleValue() < minimo) {				
				PdfCodedMessage.addTypeError(importo, Costanti.TE_IMPORTO_INFERIORE_MINIMO);
			}
			
			if(importo.doubleValue() > massimo) {				
				PdfCodedMessage.addTypeError(importo, Costanti.TE_IMPORTO_MAGGIORE_MASSIMO);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private double getImportoMinimoRata() {
		return ImportoVariazioneValidator.IMPORTO_MINIMO_RATA;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private double getImportoMassimoRata(int frequenza) {				
		if (frequenza == 12){
			return 3333.33;
		} else if (frequenza == 4){
			return 10000;
		} else if (frequenza == 2){
			return 20000;
		} else if (frequenza == 1){
			return 40000;
		}
		return 0;
	}
}
