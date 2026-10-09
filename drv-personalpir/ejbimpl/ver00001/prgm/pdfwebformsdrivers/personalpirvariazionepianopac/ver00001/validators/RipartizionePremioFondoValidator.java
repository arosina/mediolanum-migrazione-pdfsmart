package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class RipartizionePremioFondoValidator extends AbstractPdfValidator<PdfDriver> {
	private int fondoIdx = -1; 
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RipartizionePremioFondoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RipartizionePremioFondoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender, int fondoIdx) {
		super(pdfDriver, eventSender);
		this.setFondoIdx(fondoIdx);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		
		DoubleType importoFondo = (DoubleType)pdfData.read("importoFondoPremio"+getFondoIdx());
		if(!importoFondo.isNull() && importoFondo.doubleValue() <= 0) {				
			PdfCodedMessage.addTypeError(importoFondo, Costanti.TE_IMPORTO_NON_VALIDO );			
		}

		DoubleType percentualeFondo = (DoubleType)pdfData.read("percentualeFondoPremio"+getFondoIdx());
		if(!percentualeFondo.isNull()) {
			if (percentualeFondo.doubleValue() <= 0  || percentualeFondo.doubleValue() > 100) {
				PdfCodedMessage.addTypeError(percentualeFondo, Costanti.TE_PERCENTUALE_NON_VALIDA);
			}
						
			// controllo dei 500 euro attendere risposta sul versato sui singoli fondi ed eventualmente integrare 
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getFondoIdx() {
		return fondoIdx;
	}
	public void setFondoIdx(int fondoIdx) {
		this.fondoIdx = fondoIdx;
	}
}
