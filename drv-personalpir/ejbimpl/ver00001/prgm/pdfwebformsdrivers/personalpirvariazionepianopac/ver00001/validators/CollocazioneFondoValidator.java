package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;


import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldNames;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutoCompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoCollocabileModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

public class CollocazioneFondoValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CollocazioneFondoValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		doValidateFondi(csc, pdfData, result);
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public void doValidateFondi(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		NumeroPolizzaAutoCompleteModel polizza = Utility.recuperaPolizza(csc, pdfData, getPdfDriver().getPdf());	
		FondoCollocabileModel fondoModel = new FondoCollocabileModel();
		fondoModel.setPolizzaModel(polizza);
		
		int i=0;
		while((StringType)pdfData.readProperty(FieldNames.ISINFONDOPREMIO+i)!= null) {
			StringType isinFondo = (StringType)pdfData.readProperty(FieldNames.ISINFONDOPREMIO+i);
			if (!isinFondo.isNull()) {
				fondoModel.setIsin(isinFondo);
				fondoModel.setFondoPartenza(true);
				sottoScrivibilitaFondo(csc, fondoModel, result);
			}
			i++;
		}
	}

	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void sottoScrivibilitaFondo(ClientSessionContext csc, FondoCollocabileModel fondoModel, AbstractBusinessEventOutputData result) throws Exception {
		fondoModel.setTariffa(new StringType(Costanti.TARIFFA_PIC));
		
		if(getPdfDriver().isOperatoreMOM()) {
			fondoModel.setOrigine(new StringType(Costanti.ORIGINE_MOP));
		}else {
			fondoModel.setOrigine(new StringType(Costanti.ORIGINE_RDV));
		}
		
		if(!Utility.isFondoPresentePolizza(csc, fondoModel) && (!Utility.isFondoCollocabile(csc, fondoModel))) {				
			if (getEventSender() == PdfEventEnum.VERIFY_COPERNICO_PDF && result != null) {
				PdfCodedMessage.addError(result, Costanti.E_OPERAZIONE_NON_SOTTOSCRIVIBILE);
			}
			else {
				PdfCodedMessage.addTypeError(fondoModel.getIsin(), Costanti.TE_PRODOTTO_NON_SOTTOSCRIVIBILE);
			}	
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void doValidateSingoloFondo(ClientSessionContext csc, PdfDataModel pdfData, FondoCollocabileModel fondoModel, AbstractBusinessEventOutputData result) throws Exception {
		NumeroPolizzaAutoCompleteModel polizza = Utility.recuperaPolizza(csc, pdfData, getPdfDriver().getPdf());
		fondoModel.setPolizzaModel(polizza);
		sottoScrivibilitaFondo(csc, fondoModel, result);
	}	
	
}
