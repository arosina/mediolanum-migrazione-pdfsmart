package prgm.pdfwebforms.questionariolight;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.idd.FlagControlloTargetMarketBackupBean;
import prgm.pdfwebforms.idd.IddCallModel;
import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************/
/********************************************************************************/
public class QuestionarioLightUtility {

	private static final String DAO_XML_QLTM = "PdfWebForms.PdfWebFormsQuestionarioLight";	
	private static final String DAO_XML_IDD = "PdfWebForms.PdfWebFormsIdd";	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private QuestionarioLightUtility() {
		throw new IllegalStateException("QuestionarioLightUtility class");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void gestisciVisibilitaLinkIdQLTM(Template t, PdfModel pdf, PdfFieldInfos fi, AbstractType field) {
		
		if(!fi.pdfFieldName.equals(PdfPredefinedFields.LINK_ID_QLTM))
			return;
		
		fi.hidden = false;
		field.setStringValue("");
		try {
			ClientSessionContext csc = t.getUserSessionContext().getClientSessionContext();
			if(!isLinkQuestionarioAttivo(csc, pdf)) {
				field.resetTypeErrors();
				return;
			}
			
			field.setStringValue(fi.defValue.isEmpty() ? "Questionario Light" : fi.defValue);

		}catch(DAOException | Exception e) {
			// Do nothing
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void verificaCompilazioneQuestionarioLight(ClientSessionContext csc, PdfModel pdf) {
		
		if(!pdf.getPdfData().fieldExist(PdfPredefinedFields.LINK_ID_QLTM))
			return;
		
		try {
			if(!isLinkQuestionarioAttivo(csc, pdf)) {
				if(!pdf.isOperatoreMOM() && !pdf.isInBasket()) { // Se siamo rete e non da 5D
					pdf.getPdfData().setIdQLTM(new StringType());
					pdf.mainPdfData().setIdQLTM(new StringType());
				}
				return;			
			}

			AbstractType linkIdQLTMField = pdf.getPdfData().read(PdfPredefinedFields.LINK_ID_QLTM);			
			QuestionarioLightModel ql = leggiQuestionarioLight(csc, pdf.getPdfData().readAsString(PdfPredefinedFields.CLIENTE_NDG_PREFIX+"1"));
			if(ql.getNonCompilabileMsg() != null) {
				linkIdQLTMField.addTypeError(ql.getNonCompilabileMsg());
				return;
			}			
			if(pdf.getPdfData().getIdQLTM().isNull())
				linkIdQLTMField.addTypeError("Attenzione! Non è stato compilato il Questionario Target Market Polizze");

		}catch(DAOException | Exception e) {
			pdf.addCommandError(e.toString());
		}
	}
	
	/***********************************************************************************************/
	private static final String NUMERO_POLIZZA = "numeroPolizza";
	/***********************************************************************************************/
	private static boolean isLinkQuestionarioAttivo(ClientSessionContext csc, PdfModel pdf) throws Exception, DAOException{
		
		if(pdf.isOperatoreMOM() || pdf.isInBasket())
			return false;
		
		AbstractType codCli = pdf.getPdfData().read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+"1");
		if(codCli == null || codCli.isNull())
			return false;
		
		AbstractType numeroContratto = pdf.getPdfData().read(NUMERO_POLIZZA);
		if(numeroContratto == null || numeroContratto.isNull())
			numeroContratto = pdf.getPdfData().read(PdfPredefinedFields.NUMERO_CONTRATTO);		
		if(numeroContratto == null || numeroContratto.isNull())
			return false;

		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, true);
		if(iddData == null || iddData.getTipoDispositiva() == ProvideIddDataResponse.TIPO_DISPOSITIVA_INIZIALE || iddData.getTariffa().isNull())
			return false;

		IddCallModel callModel = new IddCallModel();
		callModel.setUtente(new StringType(csc.getUserCode()));
		callModel.setTariffa(iddData.getTariffa());
		callModel.setCodiceCliente(new StringType(Tools.fillSx(codCli.toString(),'0',11)));
		
		if(pdf.getFlagControlloTargetMarketBackup() != null) {
			FlagControlloTargetMarketBackupBean flagBackup = pdf.getFlagControlloTargetMarketBackup();
			if(flagBackup.hasSameData(callModel))
				return flagBackup.getFlagControlloTMPostvendita().equals("S") || flagBackup.getFlagControlloTMPostvendita().equals("N");
		}
		
		// Chiamo getPolizze per avere flagControlloTMPostvendita
		DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_IDD).executeOSBAccess("readFlagControlloTargetMarket", callModel);
		if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK)
			throw new Exception("Errore dal servizio getPolizze: "+wsRes.getWsCallData().getMessage());

		FlagControlloTargetMarketBackupBean flagBacup = new FlagControlloTargetMarketBackupBean(callModel);
		pdf.setFlagControlloTargetMarketBackup(flagBacup);
		
		return callModel.getFlagControlloTMPostvendita().equals("S") || callModel.getFlagControlloTMPostvendita().equals("N");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static QuestionarioLightModel leggiQuestionarioLight(ClientSessionContext csc, String codiceCliente) {
		
		QuestionarioLightModel ql = new QuestionarioLightModel();
		ql.setCodiceCliente(new StringType(Tools.fillSx(codiceCliente, '0', 11)));
		ql.setUtente(new StringType(csc.getUserCode()));		
		
		try {
			// Il questionario light viene construito a partire dal questionario PCP
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_QLTM).executeOSBAccess("getInfoPCPPerTMPolizze", ql);
			if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
				ql.setNonCompilabileMsg("Errore dal servizio getInfoPCPPerTMPolizze: "+wsRes.getWsCallData().getMessage());
				return ql;
			}
			
			if(!ql.getCodiceRisposta().equals("0")) {
				 ql.setNonCompilabileMsg(ql.getMessaggioRisposta().isNull()?"getInfoPCPPerTMPolizze: Codice risposta "+ql.getCodiceRisposta():ql.getMessaggioRisposta().toString());
				 return ql;
			}
			
			for(int i=0;i<ql.getDomande().size();i++) { 
				 DomandaModel domanda=(DomandaModel)ql.getDomande().get(i);
				 if(!domanda.getFlagHaRisposteSelezionabili().booleanValue()) {
					 ql.setNonCompilabileMsg("Attenzione! Ti invitiamo a verificare gli obiettivi di investimento dichiarati nel PCP. "+
							 				 "Sulla base di quanto fornito, gli obiettivi di investimento dichiarati nel PCP non sono in linea "+
							 				 "con il prodotto su cui si desidera operare. Non è possibile, pertanto, procedere con l'operazione.");
					 break;
				 }
			}		
		}catch(DAOException daoe) {
			ql.setNonCompilabileMsg("Eccezione nel richiamo del servizio getInfoPCPPerTMPolizze: "+daoe.toString());
		}
		return ql;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void callCollegaQuestionarioPostvendita(ClientSessionContext csc, PdfModel pdf) throws DAOException, Exception {
		
		if( pdf.isTestMode() || 
			pdf.getIsSede().booleanValue() ||
			pdf.getPdfData().getPdfInstanceId().isNull() ||
			pdf.isInBasket() ||
			pdf.mainPdfData().getIdQLTM().isNull())
			return;
		
		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
		if(iddData == null || 
		   iddData.getTipoVerfica() != ProvideIddDataResponse.VERIFICA_IDD_RAMO_III || 
		   iddData.getTipoDispositiva() == ProvideIddDataResponse.TIPO_DISPOSITIVA_INIZIALE)
			return;

		IddCallModel callModel = new IddCallModel();
		callModel.setUtente(new StringType(csc.getUserCode()));
		callModel.setIdQuestionarioIdd(pdf.mainPdfData().getIdQLTM());
		callModel.setPdfInstanceId(pdf.getPdfData().getPdfInstanceId());
		new DAOObject(csc, DAO_XML_QLTM).executeOSBAccess("aggiornaQuestionarioPostvendita", callModel);
	}
}
