package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

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
public class TipoVariazioneValidator extends AbstractPdfValidator<PdfDriver> {
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TipoVariazioneValidator(PdfDriver pdfDriver, PdfEventEnum eventSender) {
		super(pdfDriver, eventSender);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void doValidate(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		if (dataHelper.getNumeroPolizza().isNull())
			return;
		
		StringType isVariazioneDisposizione = dataHelper.getIsVariazioneDisposizioneSDD();
		StringType isVariazioneConto = dataHelper.getIsVariazioneContoSDD();
		StringType tipoSospensioneRevocaRiattivazione = dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD();
		StringType isVariazioneRipartizione = dataHelper.getIsVariazioneRipartizione();
		
		if (isVariazioneDisposizione.isNull() && isVariazioneConto.isNull() && tipoSospensioneRevocaRiattivazione.isNull() && isVariazioneRipartizione.isNull()) {					
			PdfCodedMessage.addTypeError(isVariazioneDisposizione, Costanti.TE_SELEZIONARE_OPERAZIONE);			
			PdfCodedMessage.addTypeError(isVariazioneConto, Costanti.TE_SELEZIONARE_OPERAZIONE);
			PdfCodedMessage.addTypeError(tipoSospensioneRevocaRiattivazione, Costanti.TE_SELEZIONARE_OPERAZIONE);
			PdfCodedMessage.addTypeError(isVariazioneRipartizione, Costanti.TE_SELEZIONARE_OPERAZIONE);			
		}else{
			if(isVariazioneDisposizione.equals("SI")) {
				validateVariazioneSDD(csc, pdfData, result);
			}
	
			if(isVariazioneConto.equals("SI")) {
				validateVariazioneContoSDD(csc, pdfData, result);
			}
	
			if(!tipoSospensioneRevocaRiattivazione.isNull()) {
				validateSospensioneRevocaRiattivazioneSDD(csc, pdfData, result);
			}
					
			if(isVariazioneRipartizione.equals("SI")) {
				validateVariazioneRipartizione(csc, pdfData, result);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void validateVariazioneSDD(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		StringType isVariazioneImporto = dataHelper.getIsVariazioneImportoSDD();
		StringType isVariazioneFrequenza = dataHelper.getIsVariazioneFrequenzaSDD();
		StringType isVariazioneDataPremio = dataHelper.getIsVariazioneDataPremioSDD();

		if(isVariazioneImporto.isNull() && isVariazioneFrequenza.isNull() && isVariazioneDataPremio.isNull()) {			
			PdfCodedMessage.addTypeError(isVariazioneImporto, Costanti.TE_SELEZIONARE_OPZIONE);
			PdfCodedMessage.addTypeError(isVariazioneFrequenza, Costanti.TE_SELEZIONARE_OPZIONE);
			PdfCodedMessage.addTypeError(isVariazioneDataPremio, Costanti.TE_SELEZIONARE_OPZIONE);			
			return;
		}

		if(isVariazioneImporto.equals("SI")) {
			DoubleType importo = dataHelper.getImportoVariazioneSDD();
			if(importo.isNull()) {				
				PdfCodedMessage.addTypeError(importo, Costanti.TE_CAMPO_OBBLIGATORIO);
			}else {
				ImportoVariazioneValidator validator = new ImportoVariazioneValidator(getPdfDriver(), getEventSender());
				validator.validate(csc, pdfData, result);
			}
		}

		if(isVariazioneFrequenza.equals("SI")) {
			StringType freq = dataHelper.getFrequenzaVariazioneSDD();
			if(freq.isNull()) {				
				PdfCodedMessage.addTypeError(freq, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
		}

		if(isVariazioneDataPremio.equals("SI")) {
			StringType giorno = dataHelper.getGiornoValutaVariazioneSDD();
			if(giorno.isNull()) {				
				PdfCodedMessage.addTypeError(giorno, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void validateVariazioneContoSDD(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		StringType intestazione = dataHelper.getTipoIntestazioneContoSDDBMEDVariazione();
		if(intestazione.isNull()) {			
			PdfCodedMessage.addTypeError(intestazione, Costanti.TE_CAMPO_OBBLIGATORIO);
		}else if(intestazione.equals("INTESTATARIO")) {
			StringType tipoConto = dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione();
			if(tipoConto.isNull()) {				
				PdfCodedMessage.addTypeError(tipoConto, Costanti.TE_CAMPO_OBBLIGATORIO);
			} else {
				BonificoBMEDValidator contoValidator = new BonificoBMEDValidator(getPdfDriver(), getEventSender());
				contoValidator.validate(csc, pdfData, result);
			}

		}else if(intestazione.equals("ALTROCLIENTE")) {
			StringType ndg = dataHelper.getNdgALTROCLIENTESDDBMEDVariazione();
			if(ndg.isNull()) {				
				PdfCodedMessage.addTypeError(ndg, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
			StringType cliente = dataHelper.getCognomeNomeALTROCLIENTESDDBMEDVariazione();
			if(cliente.isNull()) {				
				PdfCodedMessage.addTypeError(cliente, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
			AltroClienteValidator validator = new AltroClienteValidator(getPdfDriver(), getEventSender());
			validator.validate(csc, pdfData, result);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void validateSospensioneRevocaRiattivazioneSDD(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		StringType tipo = dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD();
		if(tipo.equals("SOSPENSIONE")) {
			StringType dataDa = dataHelper.getMeseAnnoSospensioneDa();
			if(dataDa.isNull()) {				
				PdfCodedMessage.addTypeError(dataDa, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
			StringType dataA = dataHelper.getMeseAnnoSospensioneA();
			if(dataA.isNull()) {				
				PdfCodedMessage.addTypeError(dataA, Costanti.TE_CAMPO_OBBLIGATORIO);
			}
			DateSospensioneValidator validator = new DateSospensioneValidator(getPdfDriver(), getEventSender());
			validator.validate(csc, pdfData, result);

		}else if(tipo.equals("RIATTIVAZIONE")) {
			if(dataHelper.getIsVariazioneImportoRiattivazioneSDD().equals("SI")) {
				DoubleType importo = dataHelper.getImportoRiattivazioneSDD();
				if(importo.isNull()) {
					PdfCodedMessage.addTypeError(importo, Costanti.TE_CAMPO_OBBLIGATORIO);					
				}
			}
			if(dataHelper.getIsVariazioneFrazionamentoRiattivazioneSDD().equals("SI")) {
				StringType freq = dataHelper.getFrequenzaRiattivazioneSDD();
				if(freq.isNull()) {					
					PdfCodedMessage.addTypeError(freq, Costanti.TE_CAMPO_OBBLIGATORIO);
				}
			}
			if(dataHelper.getIsVariazioneDataPremioRiattivazioneSDD().equals("SI")) {
				StringType giorno = dataHelper.getGiornoValutaRiattivazioneSDD();
				if(giorno.isNull()) {					
					PdfCodedMessage.addTypeError(giorno, Costanti.TE_CAMPO_OBBLIGATORIO);
				}
			}

		}else if(tipo.equals("REVOCA")) {
			if(dataHelper.getIsVariazioneDisposizioneSDD().equals("SI") || dataHelper.getIsVariazioneContoSDD().equals("SI") || dataHelper.getIsVariazioneRipartizione().equals("SI")) {
				PdfCodedMessage.addTypeError(tipo, Costanti.TE_OPERAZIONE_NON_VALIDA_CON_REVOCA);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void validateVariazioneRipartizione(ClientSessionContext csc, PdfDataModel pdfData, AbstractBusinessEventOutputData result) throws Exception {
		RipartizioneMediolanumVsTerziValidator rmtv = new RipartizioneMediolanumVsTerziValidator(getPdfDriver(), getEventSender());
		rmtv.validate(csc, pdfData, result);
		FondiValidator fv = new FondiValidator(getPdfDriver(), getEventSender());
		fv.validate(csc, pdfData, result);
		AbilitazioneFondiValidator afv = new AbilitazioneFondiValidator(getPdfDriver(), getEventSender());
		afv.validate(csc, pdfData, result);
	}
}
