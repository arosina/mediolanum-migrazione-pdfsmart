package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;
import prgm.pdfwebforms.drivers.PdfBaseDriver.ModalitaDiSottoscrizione;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldNames;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.DatiPianoPacModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.validators.AbstractPdfValidator;

/***********************************************************************************************/
/***********************************************************************************************/
public class BonificoBMEDValidator extends AbstractPdfValidator<PdfDriver> {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BonificoBMEDValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		StringType tipoContoBONIFICOBMED = dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione();
		StringType ibanContoCorrenteCCBONIFICOBMED = dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione();
		StringType numeroPropostaCCINAPERTURABONIFICOBMED = dataHelper.getNumeroPropostaCCINAPERTURAINTESTATARIOSDDBMEDVariazione();
		StringType ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione = dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDEsternaVariazione();
		
		if (tipoContoBONIFICOBMED.equals("CC")){
			if (ibanContoCorrenteCCBONIFICOBMED.isNull()){				
				PdfCodedMessage.addTypeError(ibanContoCorrenteCCBONIFICOBMED, Costanti.TE_CAMPO_OBBLIGATORIO);				
			} else {
				// in questo ramo posso mettere il controllo conti scudati
				ContoAutocompleteInput input = new ContoAutocompleteInput();
				input.setNdgFieldName(FieldNames.NDGCLIENTE1);
				input.setTipoConto(Costanti.TIPO_CONTO_CONTO_CORRENTE_SCUDATO);
				input.setFieldName(FieldNames.IBANCONTOCORRENTECCINTESTATARIOSDDBMEDVARIAZIONE);
				getPdfDriver().ctrl_esistenzaConto(csc, pdfData, input);
				
				ControlloContoPolizzaScudatiValidator controlloContoPolizzaScudati = new ControlloContoPolizzaScudatiValidator(getPdfDriver(), getEventSender(), input);
				controlloContoPolizzaScudati.validate(csc, pdfData, result);

				DatiPianoPacModel pianoPac = Utility.getDatiPianoPac(csc, dataHelper);
				String vecchioConto = pianoPac.getConto().toString();
				if(vecchioConto != null && vecchioConto.length() >= 8 && vecchioConto.substring(vecchioConto.length()-8).equals(ibanContoCorrenteCCBONIFICOBMED.toString().substring(19))) {
					PdfCodedMessage.addTypeError(ibanContoCorrenteCCBONIFICOBMED, Costanti.TE_NUOVO_CONTO_CORRISPONDE_VECCHIO);
				}
			}
		} else {
			if (!ibanContoCorrenteCCBONIFICOBMED.isNull()) {				
				PdfCodedMessage.addTypeError(ibanContoCorrenteCCBONIFICOBMED, Costanti.TE_VALORE_NON_AMMESSO);
			}
		}

		if (tipoContoBONIFICOBMED.equals("CCINAPERTURA")){
			if (getPdfDriver().getPdf().isOperatoreMOM()) {//operatore MOP cosi il controllo scatta anche in caso di carta non chimica
				PdfCodedMessage.addTypeError(numeroPropostaCCINAPERTURABONIFICOBMED, Costanti.TE_COMPILA_IBAN);
			} else {
				if(numeroPropostaCCINAPERTURABONIFICOBMED.isNull()) {					
					PdfCodedMessage.addTypeError(numeroPropostaCCINAPERTURABONIFICOBMED, Costanti.TE_CAMPO_OBBLIGATORIO);
				} else {
					result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);					
					PdfCodedMessage.addWarning(result, Costanti.W_CC_IN_APERTURA);
				}
			}
		} else {
			if (!numeroPropostaCCINAPERTURABONIFICOBMED.isNull()) {				
				PdfCodedMessage.addTypeError(numeroPropostaCCINAPERTURABONIFICOBMED, Costanti.TE_VALORE_NON_AMMESSO);
			}
		}
		//Rework MOP
		if (tipoContoBONIFICOBMED.equals("CCESTERNA")){
			if(ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione.isNull()) {
				PdfCodedMessage.addTypeError(ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione, Costanti.TE_CAMPO_OBBLIGATORIO);
			}else {
				getPdfDriver().ctrl_formatoIban(csc, ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione);
				result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
				PdfCodedMessage.addWarning(result, Costanti.W_DISPOSIZIONE_PERMANENTE_SDD_BANCA_ESTERNA);
			}
			
		} else {
			if (!ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione.isNull()) {				
				PdfCodedMessage.addTypeError(ibanContoCorrenteCCINTESTATARIOSDDEsternaVariazione, Costanti.TE_VALORE_NON_AMMESSO);
			}
		}
	}
}
