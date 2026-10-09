package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
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

public class ResidenzaFisicaPaeseAltoRischioValidator extends AbstractPdfValidator<PdfDriver> {

	public ResidenzaFisicaPaeseAltoRischioValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		StringType ndgCliente1 = dataHelper.getNdgCliente1();
		DAOObject dao = new DAOObject(csc,Costanti.DAO_FILE_XML);
		int gradoRischio = Utility.gradoRischioResidenzaFisica(dao, pdfData);
		if (gradoRischio == Costanti.ALTO_RISCHIO_1) {			
			PdfCodedMessage.addTypeError(ndgCliente1, Costanti.TE_RESIDENZA_CLIENTE_ALTO_RISCHIO);
		}	
	}

}
