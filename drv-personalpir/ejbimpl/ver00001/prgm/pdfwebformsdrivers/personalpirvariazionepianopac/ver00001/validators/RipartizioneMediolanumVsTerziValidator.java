package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.math.BigDecimal;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;
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
public class RipartizioneMediolanumVsTerziValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RipartizioneMediolanumVsTerziValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		double percentualeFondiMed = 0;		
		for (int i=0;;i++){
			StringType isinFondo = (StringType)pdfData.read("isinFondoPremio"+i);
			StringType lineaFondo = (StringType)pdfData.read("lineaFondoPremio"+i);
			DoubleType percFondo = (DoubleType)pdfData.read("percentualeFondoPremio"+i);
			if (isinFondo == null) {				
				break;
			}
			if (!isinFondo.isNull() && lineaFondo.equals(Costanti.LINEA_FONDO_MED)) {
				percentualeFondiMed += percFondo.doubleValue();							
			}
		}

		double percentualeMinima = 60;
		if (percentualeFondiMed < percentualeMinima){			
			PdfCodedMessage.addError(result, Costanti.E_RANGE_DISTRIBUZIONE_ERRATO, new String[] {Utility.formattaImporto(percentualeMinima), String.valueOf(BigDecimal.valueOf(percentualeFondiMed).setScale(2,BigDecimal.ROUND_DOWN))});
		}
	}
}
