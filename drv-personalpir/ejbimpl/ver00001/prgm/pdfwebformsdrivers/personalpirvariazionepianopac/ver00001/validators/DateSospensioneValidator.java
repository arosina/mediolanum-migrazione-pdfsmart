package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.FieldFormatException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
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
public class DateSospensioneValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DateSospensioneValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		StringType strDataDa = dataHelper.getMeseAnnoSospensioneDa();
		DateType dataDa = new DateType();
		if(!strDataDa.isNull()) {
			dataDa = controllaMeseAnno(strDataDa);
		}

		StringType strDataA = dataHelper.getMeseAnnoSospensioneA();
		DateType dataA = new DateType();
		if(!strDataA.isNull()) {
			dataA = controllaMeseAnno(strDataA);
		}

		if(!strDataDa.hasTypeErrors() && !strDataA.hasTypeErrors()) {
			if (dataDa.compareTo(dataA) > 0) {
				PdfCodedMessage.addTypeError(strDataA, Costanti.TE_VALORE_NON_AMMESSO);
			}
			if (dataDa.addMonthsOnNew(12).compareTo(dataA) <= 0) {
				PdfCodedMessage.addTypeError(strDataDa, Costanti.TE_SOSPENSIONE_MAGGIORE_12_MESI);
				PdfCodedMessage.addTypeError(strDataA, Costanti.TE_SOSPENSIONE_MAGGIORE_12_MESI);				
			}
		}
	}

	/*******************************************************************************************/
	/*******************************************************************************************/
	private DateType controllaMeseAnno(StringType meseAnno) {

		DateType d = new DateType();
		
		// RFC #257299: si chiede di non accettare più il formato MM/AA ma solo MM/AAAA
		if(meseAnno.toString().length() != 7) {
			PdfCodedMessage.addTypeError(meseAnno, Costanti.TE_FORMATO_ERRATO);
			return d;
		}
		
		try{
			d = new DateType("01/" + meseAnno);

			if(d.compareTo(Tools.today()) <= 0 ){
				PdfCodedMessage.addTypeError(meseAnno, Costanti.TE_VALORE_NON_AMMESSO);
			}
		}catch(FieldFormatException | NumberFormatException | IndexOutOfBoundsException ffe ){			
			PdfCodedMessage.addTypeError(meseAnno, Costanti.TE_FORMATO_ERRATO);
		}
		return d;
	}
}
