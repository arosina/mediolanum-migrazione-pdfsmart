package prgm.pdfwebforms.verifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.squadra.ElementoSquadra;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AmlAlertVerifier {
	
	private static final String DAO_XML =  "PdfWebForms.PdfVerifiers";

	private List<String> dispoForAlertAvr = new ArrayList<String>();
	private List<String> dispoForAlertPep = new ArrayList<String>();
	
	private Map<String, StringType> avrClientiElaborati = new HashMap<String, StringType>();
	private Map<String, StringType> pepClientiElaborati = new HashMap<String, StringType>();

	private static final String ALERT_START 		= 	"La presente operazione deve essere sottoposta alla procedura di <b>ADEGUATA VERIFICA RAFFORZATA</b> e alla compilazione dell'apposita modulistica disponibile all'interno del Catalogo Operazioni"; 
	private static final String ALERT_START_BASKET 	= 	"Le seguenti operazioni devono essere sottoposte alla procedura di <b>ADEGUATA VERIFICA RAFFORZATA</b> e alla compilazione dell'apposita modulistica disponibile all'interno del Catalogo Operazioni"; 
	private static final String ALERT_END 			= 	"In caso di assenza o incompleta compilazione della predetta modulistica non si potrà dare esecuzione alle suddette operazioni.";
	private static final String ALERT_PEP_MIDDLE 	= 	"Si rammenta che trattandosi di soggetto PEP è previsto in aggiunta il modulo PEP - Attestazione Origine Fondi Impiegati per ogni singola operazione di investimento, a firma del cliente.";	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void impostaAMLAlert(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		clearAlert(pdf);
		
		if(pdf.getIsSede().booleanValue() || pdf.isTestMode() || pdf.getPdfData().isOnlyPrint())
			return;
				
		if(!pdf.isInBasket()) {
			verifyAlertForPdf(csc, pdf);
		}else{
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				verifyAlertForPdf(csc, pdf);
			}
		}
		
		composeMessageAmlAlert(pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void clearAlert(PdfModel pdf) {
		avrClientiElaborati.clear();
		pepClientiElaborati.clear();
		pdf.setMessaggioPdfAmlAlert(null);
		if(pdf.isInBasket()) {
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				pdf.setMessaggioPdfAmlAlert(null);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void composeMessageAmlAlert(PdfModel pdf) {
		
		StringBuilder msg = new StringBuilder();
		msg.append(composeMessageAlert(pdf, dispoForAlertPep, "PEP"));
		if(dispoForAlertPep.isEmpty())
			msg.append(composeMessageAlert(pdf, dispoForAlertAvr, "AVR"));
		if(msg.length() == 0)
			return;
		
		pdf.setMessaggioPdfAmlAlert(msg.toString());
		if(pdf.isInBasket()) {
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				pdf.setMessaggioPdfAmlAlert(msg.toString());
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String composeMessageAlert(PdfModel pdf, List<String> dispo, String ctrlName) {
		if(dispo.isEmpty())
			return "";
		
		boolean isCtrlPep = ctrlName.equals("PEP"); 
		StringBuilder msg = new StringBuilder();
		msg.append(pdf.isInBasket()?ALERT_START_BASKET:ALERT_START);
		if(!pdf.isInBasket()) {
			msg.append(".<br>");	
		}else{
			msg.append(":<br>");
			msg.append("<ul style='font-size:12px;'>");
			for(String d : dispo)
				msg.append("<li>"+d+"</li>");
			msg.append("</ul>");
		}
		if(isCtrlPep)
			msg.append(ALERT_PEP_MIDDLE+"<br><br>");
		msg.append(ALERT_END);
		return msg.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verifyAlertForPdf(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(!pdf.isMultiPdf()) {
			verifyAlertForSinglePdf(csc, pdf, pdf.getPdfAnag(), pdf.getPdfData());
		}else{
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
				PdfAnagModel pdfAnag = pdf.getPdfAnags().get(i);
				PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				verifyAlertForSinglePdf(csc, pdf, pdfAnag, pdfElement);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verifyAlertForSinglePdf(ClientSessionContext csc, PdfModel pdf, PdfAnagModel pdfAnag, PdfDataModel pdfData) throws Exception{

		if(pdfAnag.getHasControlloAmlAVR().booleanValue())
			doControlloAVR(csc, pdf, pdfAnag, pdfData, new String[] {	ElementoSquadra.Ruoli.SOTTOSCRITTORE,
																		ElementoSquadra.Ruoli.CONTRAENTE,
																		ElementoSquadra.Ruoli.COSOTTOSCRITTORE,
																		ElementoSquadra.Ruoli.LEGALERAPPRESENTANTE,
																		ElementoSquadra.Ruoli.ASSICURATO,
																		ElementoSquadra.Ruoli.TERZOPAGATORE,
																		ElementoSquadra.Ruoli.BENEFICIARIO,
																		ElementoSquadra.Ruoli.TITOLARE_EFFETTIVO_BENEFICIARIO });
		
		if(pdfAnag.getHasControlloAmlPEP().booleanValue())
			doControlloPEP(csc, pdf, pdfData, new String[] {	ElementoSquadra.Ruoli.SOTTOSCRITTORE,
																ElementoSquadra.Ruoli.CONTRAENTE,
																ElementoSquadra.Ruoli.COSOTTOSCRITTORE });
		
	}

	/***********************************************************************************************/
	private static final String LOB_INV_ASSCURATIVI = "01";
	private static final String LOB_FONDI 			= "02";
	private static final String LOB_PREVIDENZA 		= "04";
	private static final String LOB_PROTEZIONE 		= "05";
	/***********************************************************************************************/
	// Aggiungere in fondo , String[] ruoliAml
	private void doControlloAVR(ClientSessionContext csc, PdfModel pdf, PdfAnagModel pdfAnag, PdfDataModel pdfData, String[] ruoliAml) throws Exception{
		
		// Non per lo switch
		if(pdfData.getIsSwitch().booleanValue())
			return;
		boolean doAlert = false;
		DoubleType importo = (DoubleType)pdf.mainPdfData().read(PdfPredefinedFields.IMPORTO);
		if(importo != null && !importo.isNull()) {
			doAlert = (pdfAnag.getPdfCodLineaBusiness().equals(LOB_FONDI) && importo.doubleValue() >= 500000) ||
					  ((pdfAnag.getPdfCodLineaBusiness().equals(LOB_INV_ASSCURATIVI) || 
						pdfAnag.getPdfCodLineaBusiness().equals(LOB_PREVIDENZA) || 
						pdfAnag.getPdfCodLineaBusiness().equals(LOB_PROTEZIONE)) && importo.doubleValue() >= 1000000);
			if(doAlert) {
				String dispoTitle = pdfData.getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfDescr().toString():pdfData.getPdfTitle().toString();
				if(!dispoForAlertAvr.contains(dispoTitle))
					dispoForAlertAvr.add(dispoTitle);
			}
		}
		
		// Se già l'alert è da mostrare per l'importo non vado a verificare i profili di rischio
		if(doAlert)
			return;
		
		List<ElementoSquadra> squadra = PdfDriverCaller.callProvideSquadraData(csc, pdf, pdfData, ruoliAml);
		for(ElementoSquadra elSq : squadra) {
			StringType codCli = (StringType)pdfData.read(elSq.getNomeCampoNdg());
			if(codCli == null || codCli.isNull())
				continue;
			
			boolean showAlert = false;
			try {
				codCli = new StringType(Tools.fillSx(codCli.toString(), '0', 11));
				StringType profiloRischioCalcolato = avrClientiElaborati.get(codCli.toString());
				if(profiloRischioCalcolato == null) {
					MapCommandDataModel inpOut = new MapCommandDataModel();
					inpOut.addProperty("ndg", codCli);
					inpOut.addProperty("profiloRischioCalcolato", new StringType());
					new DAOObject(csc, DAO_XML).executeOSBAccess("loadRischioCliente", inpOut);
					profiloRischioCalcolato = (StringType)inpOut.readProperty("profiloRischioCalcolato");
					avrClientiElaborati.put(codCli.toString(), profiloRischioCalcolato);
				}
				if(profiloRischioCalcolato.equals("ALTA"))
					showAlert = true;
				
			}catch(DAOException daoe) {
				throw new Exception(daoe.toString());
			}
			if(showAlert) {
				String dispoTitle = pdfData.getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfDescr().toString():pdfData.getPdfTitle().toString();
				if(!dispoForAlertAvr.contains(dispoTitle))
					dispoForAlertAvr.add(dispoTitle);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void doControlloPEP(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, String[] ruoliAml) throws Exception{
		
		// Non per lo switch
		if(pdfData.getIsSwitch().booleanValue())
			return;

		List<ElementoSquadra> squadra = PdfDriverCaller.callProvideSquadraData(csc, pdf, pdfData, ruoliAml);
		for(ElementoSquadra elSq : squadra) {
			StringType codCli = (StringType)pdfData.read(elSq.getNomeCampoNdg());
			if(codCli == null || codCli.isNull())
				codCli = (StringType)pdfData.read(elSq.getNomeCampoIdCensimento());
			if(codCli == null || codCli.isNull())
				continue;
			
			boolean showAlert = true;
			try {
				codCli = new StringType(Tools.fillSx(codCli.toString(), '0', 11));
				StringType flagPep = pepClientiElaborati.get(codCli.toString());
				if(flagPep == null) {
					MapCommandDataModel inp = new MapCommandDataModel();
					inp.addProperty("codiceCliente", codCli);
					flagPep = (StringType)new DAOObject(csc, DAO_XML).executeQueryAccess("loadFlagPepCliente", inp).getSingleResult();
					pepClientiElaborati.put(codCli.toString(), flagPep);
				}
				if(flagPep == null || flagPep.isNull() || flagPep.equals("N"))
					showAlert = false;
				
			}catch(DAOException daoe) {
				throw new Exception(daoe.toString());
			}
			if(showAlert) {
				String dispoTitle = pdfData.getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfDescr().toString():pdfData.getPdfTitle().toString();
				if(!dispoForAlertPep.contains(dispoTitle))
					dispoForAlertPep.add(dispoTitle);
			}
		}
	}

}
