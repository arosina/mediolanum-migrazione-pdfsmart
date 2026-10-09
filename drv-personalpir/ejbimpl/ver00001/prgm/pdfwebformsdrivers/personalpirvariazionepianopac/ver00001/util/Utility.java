package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutoCompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutocomplete;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.DatiPianoPacModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoCollocabileModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondiModel;


/***********************************************************************************************/
/***********************************************************************************************/
public class Utility {

	public static final String FIELD_COD_PRODOTTO = "codProdotto";
	public static final String FIELD_NUMERO_CONTRATTO = "numeroContratto";
	public static final String SUFFISSO_IIS = "IIS";
	public static final String SUFFISSO_PREMIO = "Premio";
	public static final String AUT_STEP_OUT_FONDO = "autStepOutFondo";
	public static final String SOCIETA_FONDO = "societaFondo";
	public static final String CONTROVALORE_FONDO = "controvaloreFondo";
	public static final String IMPORTO_FONDO = "importoFondo";
	public static final String PERCENTUALE_FONDO = "percentualeFondo";
	public static final String DESCRIZIONE_FONDO = "descrizioneFondo";
	public static final String IMPORTO_MINIMO_FONDO = "importoMinimoFondo";
	public static final String CODICE_FONDO = "codiceFondo";
	public static final String LINEA_FONDO = "lineaFondo";
	public static final String ISIN_FONDO = "isinFondo";

	private Utility() {}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formattaImporto(double importo) {
		NumberFormat nf = NumberFormat.getInstance(java.util.Locale.ITALY);
		nf.setMinimumFractionDigits(2);
		return nf.format(importo);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static double getImportoRataPianoPac(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		double totale = 0;
		if(dataHelper.getIsVariazioneImportoSDD().equals("SI")) {
			totale = dataHelper.getImportoVariazioneSDD().doubleValue();

		}else if(dataHelper.getIsVariazioneImportoRiattivazioneSDD().equals("SI")) {
			totale = dataHelper.getImportoRiattivazioneSDD().doubleValue();

		}else if(!dataHelper.getImportoPiano().isNull()){
			totale = dataHelper.getImportoPiano().doubleValue();

		}else {			
			totale = ((PdfDriver)pdfData.getPdfDriver()).readResiduoPolizza(csc, dataHelper.getCodProdottoPolizza(), dataHelper.getNumeroContratto()).getImportoRata().doubleValue();
			/* lo salvo in questo campo nascosto del pdf per non rileggerlo ogni volta da db */
			dataHelper.setImportoPiano(new DoubleType(totale));
		}

		return totale;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getFrequenzaPianoPac(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {

		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		int frequenza = 0;
		if(dataHelper.getIsVariazioneFrequenzaSDD().equals("SI")) {
			frequenza = new IntegerType(dataHelper.getFrequenzaVariazioneSDD().toString()).intValue();

		}else if(dataHelper.getIsVariazioneFrazionamentoRiattivazioneSDD().equals("SI")) {
			frequenza = new IntegerType(dataHelper.getFrequenzaRiattivazioneSDD().toString()).intValue();

		}else if(!dataHelper.getFrequenzaPiano().isNull()){
			frequenza = dataHelper.getFrequenzaPiano().intValue();

		}else {
			frequenza = ((PdfDriver)pdfData.getPdfDriver()).readResiduoPolizza(csc, dataHelper.getCodProdottoPolizza(), dataHelper.getNumeroContratto()).getFrazionamento().intValue();
			/* la salvo in questo campo nascosto del pdf per non rileggerla ogni volta da db */
			dataHelper.setFrequenzaPiano(new DoubleType(frequenza));
		}

		return frequenza;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static NumeroPolizzaAutoCompleteModel recuperaPolizza(ClientSessionContext csc,PdfDataModel pdfData,PdfModel pdf) throws Exception {
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		NumeroPolizzaAutoCompleteModel polizza = new NumeroPolizzaAutoCompleteModel();
		polizza.setNdgCliente(dataHelper.getNdgCliente1());
		polizza.setCodiceAgente(pdf.getCodAgeFiltro());
		polizza.setNumeroPolizza(dataHelper.getNumeroPolizza());
		polizza.setCodProdottoPolizza(dataHelper.getCodProdottoPolizza());
		polizza.setNumeroContratto(dataHelper.getNumeroContratto());
		StringType numeroPolizza = dataHelper.getNumeroPolizza();

		try {
			NumeroPolizzaAutocomplete autoComplete = new NumeroPolizzaAutocomplete();
			ListType elencoPolizze = autoComplete.findElements(csc, polizza);
			if (elencoPolizze.size() == 0){				
				return polizza;	

			} else {
				polizza = (NumeroPolizzaAutoCompleteModel)elencoPolizze.get(0);
			}
		} catch (DAOException de) {
			PdfCodedMessage.addTypeError(numeroPolizza, Costanti.TE_PROBLEMA_TECNICO_VERIFICA_POLIZZA);
			return polizza;
		}

		return polizza;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String recuperaAnagraficaProdottoFondoReportAdeguatezza(ClientSessionContext csc, String isin, String tariffa) throws Exception {
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("isin", new StringType(isin));
		input.addProperty("chiaveNaturaPadre", new StringType(tariffa));

		try {		
			DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel result = dao.executeQueryAccess("recuperaAnagraficaProdottoFondo",input);			
			return ((StringType)result.getSingleResult()).toString();

		} catch(DAOException daoe){
			String errorMsg = "Utility.recuperaAnagraficaProdottoFondoReportAdeguatezza - Eccezione DAO nel recuperare il codice prodotto/servizio surrogato: "+daoe;
			throw new Exception(errorMsg);		
		} catch(Exception e){
			String errorMsg = "Utility.recuperaAnagraficaProdottoFondoReportAdeguatezza - Eccezione generica nel recuperare il codice prodotto/servizio surrogato: "+e;
			throw new Exception(errorMsg);
		}	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String recuperaDescrizioneProdottoReportAdeguatezza(ClientSessionContext csc) throws Exception {

		try {
			DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel result = dao.executeQueryAccess("recuperaDescrizioneProdotto", null);
			return ((StringType)result.getSingleResult()).toString();
		} catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel recuperare la descrizione prodotto: "+daoe;
			throw new Exception(errorMsg);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void compattaFondi(PdfDataModel pdfData) {

		String suffisso = Utility.SUFFISSO_PREMIO;
		ArrayList<MapCommandDataModel> fondiSelezionati = new ArrayList<>();
		for (int i=0; ; i++) {
			if(pdfData.read(Utility.ISIN_FONDO+suffisso+i) == null)
				break;
			if(!pdfData.read(Utility.ISIN_FONDO+suffisso+i).isNull()) {
				MapCommandDataModel fondoModel = pdfToModel(pdfData, suffisso, i);
				fondiSelezionati.add(fondoModel);
			}
		}
		for (int i=0; ; i++) {
			if(pdfData.read(Utility.ISIN_FONDO+suffisso+i) == null)
				break;
			if (i<fondiSelezionati.size()) {
				MapCommandDataModel fondoModel = fondiSelezionati.get(i);
				modelToPdf(fondoModel, pdfData, suffisso, i);
			} else {
				clearFondo(pdfData, suffisso, i);
			}
		}

	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static MapCommandDataModel pdfToModel(PdfDataModel pdfData, String suffisso, int fondoIdx) {
		MapCommandDataModel fondoModel = new MapCommandDataModel();

		fondoModel.addProperty(Utility.LINEA_FONDO, pdfData.readProperty(Utility.LINEA_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.CODICE_FONDO, pdfData.readProperty(Utility.CODICE_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.IMPORTO_MINIMO_FONDO, pdfData.readProperty(Utility.IMPORTO_MINIMO_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.ISIN_FONDO, pdfData.readProperty(Utility.ISIN_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.DESCRIZIONE_FONDO, pdfData.readProperty(Utility.DESCRIZIONE_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.PERCENTUALE_FONDO, pdfData.readProperty(Utility.PERCENTUALE_FONDO+suffisso+fondoIdx));
		fondoModel.addProperty(Utility.IMPORTO_FONDO, pdfData.readProperty(Utility.IMPORTO_FONDO+suffisso+fondoIdx));
		if (suffisso.equals(Utility.SUFFISSO_PREMIO)) {
			fondoModel.addProperty(Utility.CONTROVALORE_FONDO, pdfData.readProperty(Utility.CONTROVALORE_FONDO+suffisso+fondoIdx));
			fondoModel.addProperty(Utility.SOCIETA_FONDO, pdfData.readProperty(Utility.SOCIETA_FONDO+suffisso+fondoIdx));
		} else if (suffisso.equals(Utility.SUFFISSO_IIS)) {
			fondoModel.addProperty(Utility.AUT_STEP_OUT_FONDO, pdfData.readProperty(Utility.AUT_STEP_OUT_FONDO+suffisso+fondoIdx));
		}

		return fondoModel;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void modelToPdf(MapCommandDataModel fondoModel, PdfDataModel pdfData, String suffisso, int fondoIdx) {
		pdfData.addProperty(Utility.LINEA_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.LINEA_FONDO));
		pdfData.addProperty(Utility.CODICE_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.CODICE_FONDO));
		pdfData.addProperty(Utility.IMPORTO_MINIMO_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.IMPORTO_MINIMO_FONDO));
		pdfData.addProperty(Utility.ISIN_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.ISIN_FONDO));
		pdfData.addProperty(Utility.DESCRIZIONE_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.DESCRIZIONE_FONDO));
		pdfData.addProperty(Utility.PERCENTUALE_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.PERCENTUALE_FONDO));
		pdfData.addProperty(Utility.IMPORTO_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.IMPORTO_FONDO));
		if (suffisso.equals(Utility.SUFFISSO_PREMIO)) {
			pdfData.addProperty(Utility.CONTROVALORE_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.CONTROVALORE_FONDO));
			pdfData.addProperty(Utility.SOCIETA_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.SOCIETA_FONDO));
		} else if (suffisso.equals(Utility.SUFFISSO_IIS)) {
			pdfData.addProperty(Utility.AUT_STEP_OUT_FONDO+suffisso+fondoIdx, fondoModel.readProperty(Utility.AUT_STEP_OUT_FONDO));
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void clearFondo(PdfDataModel pdfData, String suffisso, int fondoIdx) {
		pdfData.addProperty(Utility.LINEA_FONDO+suffisso+fondoIdx, new StringType());
		pdfData.addProperty(Utility.CODICE_FONDO+suffisso+fondoIdx, new StringType());
		pdfData.addProperty(Utility.IMPORTO_MINIMO_FONDO+suffisso+fondoIdx, new DoubleType());
		pdfData.addProperty(Utility.ISIN_FONDO+suffisso+fondoIdx, new StringType());
		pdfData.addProperty(Utility.DESCRIZIONE_FONDO+suffisso+fondoIdx, new StringType());
		pdfData.addProperty(Utility.PERCENTUALE_FONDO+suffisso+fondoIdx, new DoubleType());
		pdfData.addProperty(Utility.IMPORTO_FONDO+suffisso+fondoIdx, new DoubleType());
		if (suffisso.equals(Utility.SUFFISSO_PREMIO)) {
			pdfData.addProperty(Utility.SOCIETA_FONDO+suffisso+fondoIdx, new StringType());
		} else if (suffisso.equals(Utility.SUFFISSO_IIS)) {
			pdfData.addProperty(Utility.AUT_STEP_OUT_FONDO+suffisso+fondoIdx, new StringType());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isFondoAbilitato(ClientSessionContext csc, String isin) throws Exception {

		try {
			DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
			MapCommandDataModel inputModel = new MapCommandDataModel();
			inputModel.addProperty("isin", new StringType(isin));

			DAOQueryResultModel result = dao.executeQueryAccess("leggiFlagBloccoFondo", inputModel);
			StringType flagBlocco = (StringType)result.getSingleResult();
			if (flagBlocco.isNull())
				throw new Exception("In anagrafica non è presente lo stato del fondo!");
			
			return flagBlocco.equals("D") || flagBlocco.equals("N");

		} catch(DAOException daoe){
			String errorMsg = "Utility.isFondoAbilitato - Eccezione DAO nel recuperare lo stato del fondo: "+daoe;
			throw new Exception(errorMsg);		
		} catch(Exception e){
			String errorMsg = "Utility.isFondoAbilitato - Eccezione generica nel recuperare lo stato del fondo: "+e;
			throw new Exception(errorMsg);
		}	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	/**questo chiamata rimane per recuperare il numero conto che il servizio recupèerapiano non fornisce**/
	public static DatiPianoPacModel getDatiPianoPac(ClientSessionContext csc, PdfDataHelper dataHelper) throws Exception {

		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty(Utility.FIELD_COD_PRODOTTO, dataHelper.getCodProdottoPolizza());
		input.addProperty(Utility.FIELD_NUMERO_CONTRATTO, dataHelper.getNumeroContratto());
		try {
			DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel result = dao.executeQueryAccess("leggiDatiPianoPac", input);
			return (DatiPianoPacModel)(result.getResult().get(0));
		} catch(DAOException daoe){
			String errorMsg = "Utility.getDatiPianoPac - Eccezione DAO: "+daoe;
			throw new Exception(errorMsg);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean hasSddAttivi(ClientSessionContext csc, PdfDataHelper dataHelper) throws Exception {

		MapCommandDataModel sddAttivi = new MapCommandDataModel();
		sddAttivi.addProperty(Utility.FIELD_NUMERO_CONTRATTO, dataHelper.getNumeroContratto());
		try {
			DAOObject dao = new DAOObject(csc, PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("hasSddAttivi", sddAttivi);
			
			return ((BooleanType)qRes.getSingleResult()).booleanValue();
		} catch (DAOException de) {
			throw new Exception(de);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean hasSddSospesi(ClientSessionContext csc, PdfDataHelper dataHelper) throws Exception {

		MapCommandDataModel sddSospesi = new MapCommandDataModel();
		sddSospesi.addProperty(Utility.FIELD_NUMERO_CONTRATTO, dataHelper.getNumeroContratto());
		try {
			DAOObject dao = new DAOObject(csc, PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("hasSddSospesi", sddSospesi);
			return ((BooleanType)qRes.getSingleResult()).booleanValue();			
		} catch (DAOException de) {
			throw new Exception(de);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isSddAttivoBancaEsterna(ClientSessionContext csc, PdfDataHelper dataHelper) throws Exception {

		MapCommandDataModel sddBancaEsterna = new MapCommandDataModel();
		sddBancaEsterna.addProperty(Utility.FIELD_NUMERO_CONTRATTO, dataHelper.getNumeroContratto());
		try {
			DAOObject dao = new DAOObject(csc, PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("isSddAttivoBancaEsterna", sddBancaEsterna);
			return ((BooleanType)qRes.getSingleResult()).booleanValue();
		} catch (DAOException de) {
			throw new Exception(de);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getEtaCliente(PdfPersonModel cliente) throws Exception {

		DateType dn = (DateType)cliente.readProperty("dataNascita");

		if(dn.isNull())
			return -1;

		Date dataNascita = dn.dateValue();
		int age = 0;
		Calendar birthdate = Calendar.getInstance();
		birthdate.setTime(dataNascita);
		Calendar now = Calendar.getInstance();
		age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
		birthdate.add(Calendar.YEAR, age);
		if(now.before(birthdate))
			age--;

		return age;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ListType recuperaElencoFondi(ClientSessionContext csc, StringType numeroContratto) throws Exception, DAOException {
		RicercaFondiModel model = new RicercaFondiModel();		
		model.setCodiceProdotto(new StringType(Costanti.TARIFFA_PIC));
		model.setNumeroContratto(numeroContratto);
		DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
		return dao.executeQueryAccess(Costanti.QUERY_RICERCA_COMPARTI, model).getResult();
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isFondoCollocabile(ClientSessionContext csc, FondoCollocabileModel fondoModel) throws Exception{
		boolean isFondoCollocabile = false;
		try {
			DAOObject dao = new DAOObject(csc,Costanti.DAO_OBJECT_NAME_EF);
			MapCommandDataModel inputModel = new MapCommandDataModel();
			inputModel.addProperty("tariffa", fondoModel.getTariffa());
			inputModel.addProperty("origine", fondoModel.getOrigine());	
			inputModel.addProperty("isin", fondoModel.getIsin());				
			DAOQueryResultModel result = dao.executeQueryAccess(Costanti.QUERY_FONDO_IS_COLLOC, inputModel);
			if (result.getSingleResult() != null && ((IntegerType)result.getSingleResult()).intValue()>=0) {
				isFondoCollocabile = true;
			}
		} catch(DAOException daoe){
			String errorMsg = "Utility.isFondoCollocabile - Eccezione DAO nel recuperare lo stato del fondo: "+daoe;
			throw new Exception(errorMsg);		
		} catch(Exception e){
			String errorMsg = "Utility.isFondoCollocabile - Eccezione generica nel recuperare lo stato del fondo: "+e;
			throw new Exception(errorMsg);
		}
		
		return isFondoCollocabile;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public static boolean isFondoPresentePolizza(ClientSessionContext csc, FondoCollocabileModel fondoModel) throws Exception{
		boolean result = false;
		try {
			RicercaFondiModel model = new RicercaFondiModel();		
			model.setCodiceProdotto(new StringType(Costanti.TARIFFA_PIC));
			model.setNumeroContratto(fondoModel.getPolizzaModel().getNumeroContratto());
			model.setInPortafoglio(new StringType("true"));
			DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
			List<FondoModel> list = dao.executeQueryAccess(Costanti.QUERY_RICERCA_COMPARTI, model).getResult().getElements();
			
			for(FondoModel element: list) {
				if(element.getIsin().equals(fondoModel.getIsin())) {
					result=true;
					break;
				}
			}
		} catch(DAOException daoe){
			String errorMsg = "Utility.isFondoPresentePolizza - " + Costanti.MSG_DAO_EXCEPTION_STATO_FONDO+daoe;
			throw new Exception(errorMsg);	
		}	
		return result;	
	
	}
	
	public static int gradoRischioResidenzaFisica(DAOObject dao, PdfDataModel pdfData) throws Exception {

		StringType cognomeCliente1 = (StringType)pdfData.read(Costanti.COGNOME_CLIENTE_1);
		StringType nomeCliente1 = (StringType)pdfData.read(Costanti.NOME_CLIENTE_1);
		if (!cognomeCliente1.isNull() && !nomeCliente1.isNull()) {
			StringType ndgCliente = (StringType)pdfData.read(Costanti.NDG_CLIENTE_1);
			try {
				MapCommandDataModel inputModel = new MapCommandDataModel();
				String query = null;
				StringType codiceProspect = (StringType)pdfData.read(Costanti.ID_CENSIMENTO_CLIENTE_1);
				
				if (codiceProspect!= null && !codiceProspect.isNull()) {//cliente prospect
					query = "gradoRischioResidenzaFisicaProspect";
					inputModel.addProperty("codProspect",  codiceProspect);
				} else {
					query = "gradoRischioResidenzaFisica";
					inputModel.addProperty(Costanti.NDG_CLIENTE,  new StringType(Tools.fillSx(ndgCliente.toString(), '0', 11)));
				}
				DAOQueryResultModel result = dao.executeQueryAccess(query, inputModel);
				if (result.getSingleResult() != null && !result.getSingleResult().isNull())
					return ((IntegerType)result.getSingleResult()).intValue();
				else {
					return 0; //caso residenza italiana o residenza in paese non a rischio
				}
			} catch (DAOException de) {
				throw new Exception("Errore durante la verifica della residenza fisica del cliente "+ndgCliente+".");
			}
		}
		return 0;
	}

	public static boolean isClienteMinorenne(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {
		return isMinorenne(csc, pdfData, Costanti.ID_CLIENTE);
	}
	
	private static boolean isMinorenne(ClientSessionContext csc, PdfDataModel pdfData, int personIdx) throws Exception {
		PdfPersonModel cliente = pdfData.getPdfDriver().readCliente(csc, pdfData, personIdx);
		if (cliente != null) {
			BooleanType isPersonaFisica = (BooleanType) cliente.readProperty("isPersonaFisica");
			if (isPersonaFisica != null && isPersonaFisica.booleanValue()) {
				int eta = getEtaCliente(cliente);
				if (eta > 0 && eta < 18) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isContoCorrenteScudato(ClientSessionContext csc, PdfDataModel pdfData, ContoAutocompleteInput inputConto) throws Exception {
		ContoAutocompleteModel conto = pdfData.getPdfDriver().readConto(csc, pdfData, inputConto);
		if(conto != null){
			StringType categoriaConto = (StringType)conto.readProperty(Costanti.CATEGORIA_CONTO);
			return categoriaConto != null && categoriaConto.equals(Costanti.CONTR_C_CAT_SCUDATO);
		}
		return false;
	}
	
	public static boolean isPolizzaScudata(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf) throws Exception {
		NumeroPolizzaAutoCompleteModel polizza = recuperaPolizza(csc, pdfData, pdf);
		return !polizza.getScudoFiscale().isNull() && polizza.getScudoFiscale().equals(Costanti.POLIZZA_SCUDATA_S);
	}

	public static String formatDouble(double d) {

		if (d % 1.0 != 0)
			return String.format("%s", d);
		else
			return String.format("%.0f",d);
	}

}
