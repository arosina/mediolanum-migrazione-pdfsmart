package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class ControlloContoPolizzaScudatiValidator extends AbstractPdfValidator<PdfDriver>{
	
	private ContoAutocompleteInput inputConto = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ControlloContoPolizzaScudatiValidator(PdfDriver pdfDriver, PdfEventEnum eventSender, ContoAutocompleteInput inputConto) {
		super(pdfDriver, eventSender);
		this.setInputConto(inputConto);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		StringType ibanContoCorrente = (StringType)pdfData.read(inputConto.getFieldName());
		if (Utility.isContoCorrenteScudato(csc, pdfData, inputConto) && 
				!Utility.isPolizzaScudata(csc, pdfData, getPdfDriver().getPdf())) {			
			PdfCodedMessage.addTypeError(ibanContoCorrente, Costanti.TE_INCOERENZA_SCUDO_FISCALE_POLIZZA);			
		}
		
		if (!Utility.isContoCorrenteScudato(csc, pdfData, inputConto) && 
				Utility.isPolizzaScudata(csc, pdfData, getPdfDriver().getPdf())) {
			PdfCodedMessage.addTypeError(ibanContoCorrente, Costanti.TE_INCOERENZA_SCUDO_FISCALE_POLIZZA);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public ContoAutocompleteInput getInputConto() {
		return inputConto;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public void setInputConto(ContoAutocompleteInput inputConto) {
		this.inputConto = inputConto;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/	

}
