package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import java.math.BigDecimal;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;
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
public class FondiValidator extends AbstractPdfValidator<PdfDriver> {
	
	public static final int MINIMO_RATA_PAC = 100;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public FondiValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		boolean almenoUnFondo = false;

		BigDecimal importoTotaleInvestito = new BigDecimal(0);
		BigDecimal percentualeTotaleInvestita = new BigDecimal(0);
		double importoTotale = 0;

		for (int i=0;;i++){
			StringType isinFondo = (StringType)pdfData.read("isinFondoPremio"+i);
			DoubleType percentualeFondo = (DoubleType)pdfData.read("percentualeFondoPremio"+i);
			DoubleType importoFondo = (DoubleType)pdfData.read("importoFondoPremio"+i);
			
			if (isinFondo == null)
				break;

			if(isinFondo.isNull() && percentualeFondo.isNull() && importoFondo.isNull())
				continue;

			almenoUnFondo = true;

			if (isinFondo.isNull()){				
				PdfCodedMessage.addTypeError(isinFondo, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
			if (percentualeFondo.isNull()){
				PdfCodedMessage.addTypeError(percentualeFondo, Costanti.TE_INDICARE_PERCENTUALE);
			} else {
				percentualeTotaleInvestita = percentualeTotaleInvestita.add((percentualeFondo.isNull()?new BigDecimal(0):percentualeFondo.bigValue()));
			}
			if (importoFondo.isNull()) {				
				PdfCodedMessage.addTypeError(percentualeFondo, Costanti.TE_INDICARE_IMPORTO);
			} else {
				RipartizionePremioFondoValidator fondoValidator = new RipartizionePremioFondoValidator(getPdfDriver(), getEventSender(), i);
				fondoValidator.validate(csc, pdfData, result);
				importoTotaleInvestito = importoTotaleInvestito.add((importoFondo.isNull()?new BigDecimal(0):importoFondo.bigValue()));
			}
		}

		if (almenoUnFondo){
			DoubleType totalePercentuale = (DoubleType)pdfData.read(PdfDataHelper.FieldNames.TOTALEPERCENTUALE);
			importoTotale = Utility.getImportoRataPianoPac(csc, pdfData);
			if (importoTotale > 0 && importoTotale != importoTotaleInvestito.doubleValue())	{			
				PdfCodedMessage.addError(result, Costanti.E_IMPORTO_NON_CORRISPONDE_AL_PREMIO_UNICO);
			}

			if (percentualeTotaleInvestita.doubleValue() != 100){				
				PdfCodedMessage.addTypeError(totalePercentuale, Costanti.TE_SOMMA_PERCENTUALI_DIVERSA_100);
			}

			// #277629: spostato controllo sui minimi / massimi sulla somma di rate
			double minimoRata = this.calcolaMinimoRataPac();
			if (importoTotaleInvestito.doubleValue() < minimoRata){				
				PdfCodedMessage.addTypeError(totalePercentuale, Costanti.TE_IMPORTO_INFERIORE_MINIMO_PAR, new String[] {Utility.formatDouble(minimoRata)});
			}
			double massimoRata = this.calcolaMassimoRataPac(csc, pdfData);
			if (importoTotaleInvestito.doubleValue() > massimoRata){				
				PdfCodedMessage.addTypeError(totalePercentuale, Costanti.TE_IMPORTO_MAGGIORE_MASSIMO_PAR, new String[] {Utility.formatDouble(massimoRata)});
			}

		}else {
			PdfCodedMessage.addTypeError(pdfData.read("isinFondoPremio0"), Costanti.TE_SELEZIONARE_UN_FONDO);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private double calcolaMinimoRataPac() {
		return FondiValidator.MINIMO_RATA_PAC;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private double calcolaMassimoRataPac(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {
		int frequenza = Utility.getFrequenzaPianoPac(csc, pdfData);
		
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
