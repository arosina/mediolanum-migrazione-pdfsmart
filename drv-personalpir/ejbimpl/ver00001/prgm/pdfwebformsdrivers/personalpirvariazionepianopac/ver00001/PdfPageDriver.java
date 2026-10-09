package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001;

import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBasePageDriver;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.drivers.io.PageEventOutputData;
import prgm.pdfwebforms.drivers.io.PageLoadInputData;
import prgm.pdfwebforms.drivers.io.PageLoadOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldNames;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoCollocabileModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.CollocazioneFondoValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.ContraenteValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.DataSottoscrizioneValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.ImportoVariazioneValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.RipartizionePremioFondoValidator;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;

public class PdfPageDriver extends PdfBasePageDriver {

	public static final String FIELD_TOTALE_PERCENTUALE = "totalePercentuale";
	public static final String FIELD_TOTALE_IMPORTO = "totaleImporto";
	public static final String PREFIX_FIELD_IMPORTO_FONDO_PREMIO = "importoFondoPremio";
	public static final String PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO = "percentualeFondoPremio";
	public static final String PREFIX_FIELD_DESCRIZIONE_FONDO_PREMIO = "descrizioneFondoPremio";
	public static final String PREFIX_FIELD_ISIN_FONDO_PREMIO = "isinFondoPremio";

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public String drawHeader(PdfModel pdf, PdfDataModel pdfData, PdfAnagModel pdfAnag) {

		StringBuilder result = new StringBuilder();
		result.append(super.drawHeader(pdf, pdfData, pdfAnag));
		prgm.pdfwebformspolizzeutil.pagedriver.Utils.buildDrawHeaderTerzoPagatore(result);
		return result.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public PageLoadOutputData onLoad(ClientSessionContext csc, PageLoadInputData input) throws Exception {
		PageLoadOutputData output = new PageLoadOutputData();
		
		output.setGlobalJsScripts(new String[]{"ver00001/autocompletion/RicercaFondoAutocomplete.js",
												"ver00001/javascript/Popup.js",
												"ver00001/autocompletion/NumeroPolizzaAutocomplete.js"});

		Map<String, String> fieldsJsScripts = new HashMap<String, String>();
		fieldsJsScripts.put("numeroPolizza", "bindNumeroPolizzaAutocomplete();");

		fieldsJsScripts.put("ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione", 
							"pdfPageDriver.bindContoAutocomplete({" +
									"useCodAgente: true, "+
									"ndgFieldName: 'ndgCliente1', "+
									Costanti.TIPO_CONTO+": '"+Costanti.TIPO_CONTO_CONTO_CORRENTE_SCUDATO+"', "+
									Costanti.STRINGA_RUOLI_AMMESSI+
									"contoFieldName: 'ibanConto', "+
									"fieldName: 'ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione'});");

		fieldsJsScripts.put("ibanContoCorrenteALTROCLIENTESDDBMEDVariazione", 
							"pdfPageDriver.bindContoAutocomplete({" +
							"useCodAgente: false, "+
							"ndgFieldName: 'ndgALTROCLIENTESDDBMEDVariazione', "+
							Costanti.TIPO_CONTO+": '"+Costanti.TIPO_CONTO_CONTO_CORRENTE_SCUDATO+"', "+
							"ruoliAmmessi: \"'P', 'I', 'C'\", "+
							"contoFieldName: 'ibanConto', "+
							"fieldName: 'ibanContoCorrenteALTROCLIENTESDDBMEDVariazione'});");

		//Ripartizione fondi Premio Unico / PAC
		for(int i=0;;i++){
			AbstractType isinFondo = input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i);
			if(isinFondo == null)
				break;
			fieldsJsScripts.put(PdfPageDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i, "bindRicercaFondoAutocomplete('isinFondoPremio"+i+"', '"+i+"', 'Premio', 3);");
			fieldsJsScripts.put(PdfPageDriver.PREFIX_FIELD_DESCRIZIONE_FONDO_PREMIO+i, "bindRicercaFondoAutocomplete('descrizioneFondoPremio"+i+"', '"+i+"', 'Premio', 3);");
		}

		output.setFieldsJsScripts(fieldsJsScripts);

		if(input.getPdfData().getIdCarrello().isNull()){
			initAction("apriPopupRicercaFondiAction", "<img id='apriPopupRicercaFondiAction' src='"+getWebApp()+"/images/search.gif' onclick='pdfPageDriver.openPopupRicercaFondi(\"picpac\",\"Premio\");' style='cursor:pointer;'></img>");
		}

		return output;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected PageEventOutputData onChangeDataSottoscrizione(ClientSessionContext csc, PageEventInputData input) throws Exception {
		DataSottoscrizioneValidator validator = new DataSottoscrizioneValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE);
		validator.validate(csc, input.getPdfData());
		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected PageEventOutputData onChangeContraente(ClientSessionContext csc, PageEventInputData input) throws Exception {
		ContraenteValidator validator = new ContraenteValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE);
		validator.validate(csc, input.getPdfData());
		getPdfDriver().ctrl_isClienteBloccato(csc, input.getPdfData(), FieldNames.NDGCLIENTE1);
		try {
			getPdfDriver().ctrlNdgClienteOperativaEstero(csc, input.getPdfData(), FieldNames.NDGCLIENTE1);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected PageEventOutputData onChangeImportoVariazioneSDD(ClientSessionContext csc, PageEventInputData input) throws Exception {
		ImportoVariazioneValidator validator = new ImportoVariazioneValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE);
		validator.validate(csc, input.getPdfData());
		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected synchronized PageEventOutputData onChangePercentualeFondoPremio(ClientSessionContext csc, PageEventInputData input) throws Exception {

		int idx = Integer.parseInt(input.getEventArgs());

		if(input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idx).isNull()) {
			input.getPdfData().addProperty(PdfPageDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idx, new DoubleType());
		}else {
			double percentualeFondo = ((DoubleType)input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idx)).doubleValue();
			double importoFondo = 0;
			double importoTotale = 0;
	
			importoTotale = Utility.getImportoRataPianoPac(csc, input.getPdfData());
	
			if (importoTotale > 0){
				if (percentualeFondo == 100.00)
					importoFondo = importoTotale;
				else {
					importoFondo = (importoTotale / 100) * percentualeFondo;
					importoFondo = Math.round(importoFondo * 100)/(double)100;
				}
				input.getPdfData().addProperty(PdfPageDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idx, new DoubleType(importoFondo));
			}
		}

		RipartizionePremioFondoValidator fondoValidator = new RipartizionePremioFondoValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE, idx);
		fondoValidator.validate(csc, input.getPdfData());
		aggiornaTotali(input);

		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected synchronized PageEventOutputData onChangeImportoFondoPremio(ClientSessionContext csc, PageEventInputData input) throws Exception {

		int idx = Integer.parseInt(input.getEventArgs());

		if(input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idx).isNull()) {
			input.getPdfData().addProperty(PdfPageDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idx, new DoubleType());
		}else {
			double importoFondo = ((DoubleType)input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idx)).doubleValue();
			double percentualeFondo = 0;
			double importoTotale = 0;
	
			importoTotale = Utility.getImportoRataPianoPac(csc, input.getPdfData());
	
			if (importoTotale > 0){
				if (importoFondo == importoTotale)
					percentualeFondo = 100;
				else {
					percentualeFondo = (importoFondo / importoTotale) * 100;
					percentualeFondo = Math.round(percentualeFondo * 100)/(double)100;
				}
				input.getPdfData().addProperty(PdfPageDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idx, new DoubleType(percentualeFondo));
			}
		}

		RipartizionePremioFondoValidator fondoValidator = new RipartizionePremioFondoValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE, idx);
		fondoValidator.validate(csc, input.getPdfData());

		aggiornaTotali(input);

		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public PdfDriver getPdfDriver() {
		return (PdfDriver)super.getPdfDriver();		
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void aggiornaTotali(PageEventInputData input) {

		double totalePerc = 0;
		double totaleImporto = 0;

		for(int i=0;;i++) {
			if(input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i) == null)
				break;

			DoubleType importo = (DoubleType)input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+i);
			totaleImporto += importo.doubleValue();
			DoubleType perc = (DoubleType)input.getPdfData().read(PdfPageDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+i);
			totalePerc += perc.doubleValue();
		}
		input.getPdfData().addProperty(PdfPageDriver.FIELD_TOTALE_IMPORTO, new DoubleType(totaleImporto));
		input.getPdfData().addProperty(PdfPageDriver.FIELD_TOTALE_PERCENTUALE, new DoubleType(totalePerc));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected synchronized PageEventOutputData onChangeFondoPremio(ClientSessionContext csc, PageEventInputData input) throws Exception {
		int idx = Integer.parseInt(input.getEventArgs());
		PdfDataModel pdfData = input.getPdfData();
		
		FondoCollocabileModel fondoModel = new FondoCollocabileModel();
		fondoModel.setIsin((StringType)input.getPdfData().read(FieldNames.ISINFONDOPREMIO+idx));
		fondoModel.setFondoPartenza(true);
		CollocazioneFondoValidator collocazioneFondoValidator = new CollocazioneFondoValidator(getPdfDriver(), PdfEventEnum.PAGE_ONCHANGE);
		collocazioneFondoValidator.doValidateSingoloFondo(csc, pdfData, fondoModel, null);
		
		return null;
	}
}
