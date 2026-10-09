package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfBaseDriver.ModalitaDiSottoscrizione;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldNames;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldValues;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.DatiPianoPacModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class AltroClienteValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AltroClienteValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		StringType ndgALTROCLIENTESDDBMEDVariazione = dataHelper.getNdgALTROCLIENTESDDBMEDVariazione();
		StringType ibanContoCorrenteALTROCLIENTESDDBMEDVariazione = dataHelper.getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione();
		StringType tipoContoALTROCLIENTESDDBMEDVariazione = dataHelper.getTipoContoALTROCLIENTESDDBMEDVariazione();
		StringType ibanContoCorrenteALTROCLIENTESDDEsternaVariazione = dataHelper.getIbanContoCorrenteALTROCLIENTESDDEsternaVariazione();
		
		if (!ndgALTROCLIENTESDDBMEDVariazione.isNull()){
			if (ndgALTROCLIENTESDDBMEDVariazione.equals(dataHelper.getNdgCliente1())){
				PdfCodedMessage.addTypeError(ndgALTROCLIENTESDDBMEDVariazione, Costanti.TE_CLIENTE_COINCIDE_CONTRAENTE);
			}
			MapCommandDataModel model = new MapCommandDataModel();
			model.addProperty("ndg", ndgALTROCLIENTESDDBMEDVariazione);
			try {
				DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadDatiAltroCliente", model);
				if(qRes.getResult().size() < 1){
					PdfCodedMessage.addTypeError(ndgALTROCLIENTESDDBMEDVariazione, Costanti.TE_CLIENTE_INESISTENTE);
				}else {
					StringType cognomeNome = (StringType)((MapCommandDataModel)qRes.getResult().get(0)).readProperty("cognomeNome");
					dataHelper.setCognomeNomeALTROCLIENTESDDBMEDVariazione(cognomeNome);
				}
			} catch (DAOException de) {
				PdfCodedMessage.addTypeError(ndgALTROCLIENTESDDBMEDVariazione, Costanti.TE_PROBLEMA_TECNICO_RECUPERO_SECONDO_CLIENTE);				
			}
		}
		if(tipoContoALTROCLIENTESDDBMEDVariazione.isNull()) {
			PdfCodedMessage.addTypeError(tipoContoALTROCLIENTESDDBMEDVariazione, Costanti.TE_CAMPO_OBBLIGATORIO);
		}else if(tipoContoALTROCLIENTESDDBMEDVariazione.equals(FieldValues.TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE_CC)) {
			if(ibanContoCorrenteALTROCLIENTESDDBMEDVariazione.isNull()) {				
				PdfCodedMessage.addTypeError(ibanContoCorrenteALTROCLIENTESDDBMEDVariazione, Costanti.TE_CAMPO_OBBLIGATORIO);
			} else {
				ContoAutocompleteInput input = new ContoAutocompleteInput(); 
				input.setUseCodAgente(false);
				input.setNdgFieldName(FieldNames.NDGALTROCLIENTESDDBMEDVARIAZIONE);
				input.setTipoConto(Costanti.TIPO_CONTO_CONTO_CORRENTE_SCUDATO);
				input.setRuoliAmmessi("'P', 'I', 'C'");
				input.setFieldName(FieldNames.IBANCONTOCORRENTEALTROCLIENTESDDBMEDVARIAZIONE);
				getPdfDriver().ctrl_esistenzaConto(csc, pdfData, input);
					
				ControlloContoPolizzaScudatiValidator controlloContoPolizzaScudati = new ControlloContoPolizzaScudatiValidator(getPdfDriver(), getEventSender(), input);
				controlloContoPolizzaScudati.validate(csc, pdfData, result);
		
				DatiPianoPacModel pianoPac = Utility.getDatiPianoPac(csc, dataHelper);
				String vecchioConto = pianoPac.getConto().toString();
				if(vecchioConto != null && vecchioConto.length() >= 8 && vecchioConto.substring(vecchioConto.length()-8).equals(dataHelper.getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione().toString().substring(19))) {
					PdfCodedMessage.addTypeError(dataHelper.getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione(), Costanti.TE_NUOVO_CONTO_CORRISPONDE_VECCHIO);					
				}
			}
		}else if(tipoContoALTROCLIENTESDDBMEDVariazione.equals(FieldValues.TIPOCONTOALTROCLIENTESDDBMEDVARIAZIONE_CCESTERNA)){
			if(ibanContoCorrenteALTROCLIENTESDDEsternaVariazione.isNull()) {				
				PdfCodedMessage.addTypeError(ibanContoCorrenteALTROCLIENTESDDEsternaVariazione, Costanti.TE_CAMPO_OBBLIGATORIO);
			} else {
				getPdfDriver().ctrl_formatoIban(csc, ibanContoCorrenteALTROCLIENTESDDEsternaVariazione);
				result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
				PdfCodedMessage.addWarning(result, Costanti.W_DISPOSIZIONE_PERMANENTE_SDD_BANCA_ESTERNA);
			}
		}
	}
}
