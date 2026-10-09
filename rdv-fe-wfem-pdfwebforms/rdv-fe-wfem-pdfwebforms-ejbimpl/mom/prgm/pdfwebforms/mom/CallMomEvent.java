package prgm.pdfwebforms.mom;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.MomEventOutputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PritMomInfoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CallMomEvent extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
        ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PdfModel pdf = (PdfModel)dataModel;         
		setForwardDisplay(new Integer(0));
		try{
            
           	DataLoader.loadPersons(csc, pdf);
			
           	boolean callSrvDispositiva = true;
           	
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
			
			//Allineamento campi comuni a tutti i pdf (tipo l'rda)
			alignMomCommonToAllPdfDataProperties(pdf);
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			
			// Call verify & verified
			Tools.resetTypesWarningAndErrors(pdf.getPdfData());
			if(pdf.getMomEventData().getValida().equals("true")){
				
				pdf = facade.verifyPdf(csc, pdf);
				List<CommandError>   errOnVerify = new ArrayList<>(pdf.getCommandErrors());	
				List<CommandWarning> warOnVerify = new ArrayList<>(pdf.getCommandWarnings());
				pdf.resetCommandErrors(); 
				pdf.resetCommandWarnings();
				
				facade.pdfVerified(csc, pdf);
				List<CommandError>  errOnVerified = new ArrayList<>(pdf.getCommandErrors()); 
				List<CommandWarning> warOnVerified = new ArrayList<>(pdf.getCommandWarnings());
				pdf.resetCommandErrors(); 
				pdf.resetCommandWarnings();

				pdf.addCommandErrors(errOnVerify); 
				pdf.addCommandErrors(errOnVerified);
				pdf.addCommandWarnings(warOnVerify); 
				pdf.addCommandWarnings(warOnVerified);
				
				if(pdf.hasCommandErrors() || pdf.hasCommandWarnings())
					callSrvDispositiva = false;
			}
			
			// Prepare data to save and/or pass to dispositiva srv
			PritMomInfoModel pritMomInfoModel = new PritMomInfoModel();
			pritMomInfoModel.setCodProdottoPrit(pdf.mainPdfAnag().getPdfCodProdottoPrit());
			pritMomInfoModel.setCodOperazionePrit(pdf.mainPdfAnag().getPdfCodOperazionePrit());
			pritMomInfoModel.initUltimaAzioneMom(pdf.getMomEventData());
			pdf.setPritMomInfoModel(pritMomInfoModel);
			pdf.setMifidCallModel(null);
			pdf.setIddCallModel(null);
			pdf.setRecuperaReportAdeguatezzaCallModel(null);
			
			PdfInstanceModel pdfInstance = null;
			if( pdf.getMomEventData().getSalva().equals("true") ||
				pdf.getMomEventData().getAggiornaDispositiva().equals("true")){
				pdfInstance = loadPdfInstance(csc, pdf);
				if(pdfInstance == null){
					pdf.resetCommandErrors();
					pdf.addCommandError("L'istanza di pdf non è più presente sulla base dati");
					return pdf;
				}
			}
				
			// Call momEvent on driver
			MomEventOutputData eventOutput = PdfDriverCaller.callMomEvent(csc, pdf);
			if(eventOutput != null){
				if(eventOutput.getErrorMessage() != null && eventOutput.getErrorMessage().length() > 0){
					pdf.addCommandError(eventOutput.getErrorMessage());
					callSrvDispositiva = false;
				}
			}
			
			// Save pdf
			if(pdf.getMomEventData().getSalva().equals("true")){
				pdf = facade.savePdfOnMomValidation(csc, pdf);
			}
			
			// Call Dispositiva SRV
			if(callSrvDispositiva && pdf.getMomEventData().getAggiornaDispositiva().equals("true")){
				if(!pdf.getPdfData().getPdfInstanceId().isNull()){
					byte[] pdfContent = PdfEngine.compilePdfFields(csc, pdf, true, false, "MOMVALIDATION");
					String errMsg = SrvDispositivaCaller.callSrvDispositivaFromOperatoreMOM(csc, pdfInstance, pdf, pdfContent);
					if(errMsg != null && errMsg.length() > 0)
						pdf.addCommandError(errMsg);
				}else{
					pdf.addCommandError("E' stato chiesto di richiamare il servizio dispositiva senza prima richiedere il salvataggio del pdf");
				}
			}
			
			if(!pdf.getMomEventData().getGotoPdf().isNull()) { 
				if(!pdf.getMomEventData().getValida().equals("true") || !pdf.hasCommandErrors()){
					StringType codDispositivaBMED = pdf.getMomEventData().getGotoPdf();
					if(!isCurrentPdf(pdf, codDispositivaBMED)) {
						callPdfVerifiedBeforeGoto(csc, pdf);
						for(int i=0;i<pdf.getPdfData().getPdfs().size();i++) {
							pdf = facade.gotoPdf(csc, pdf, i, true);
							callPdfVerifiedBeforeGoto(csc, pdf);
							if(((PdfDataModel)pdf.getPdfData().getPdfs().get(i)).getCodDispositivaBMED().equals(codDispositivaBMED))
								break;
						}
						DataLoader.loadPersons(csc, pdf);
						pdf.setErrorsPropsMap(null);
						pdf.setWarningsPropsMap(null);
						pdf.setTypeErrorsPropsMap(null);
					}
				}
			}

			pdf.getPdfData().setReadonly(new BooleanType(false));
			if( pdf.getMomEventData().getReadonly().equals("true"))
				pdf.getPdfData().setReadonly(new BooleanType(true));
			
		}catch(Exception e){
			pdf.resetCommandErrors();
			pdf.addCommandError("Eccezione grave: "+e.toString());
		}
		
		return pdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void alignMomCommonToAllPdfDataProperties(PdfModel pdf) {
		if(pdf.isInInserimentoMOM() && pdf.isMultiPdf()) {
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++) {
				PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				pdfDataElement.setIdReportAdeguatezza(pdf.getPdfData().getIdReportAdeguatezza());
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isCurrentPdf(PdfModel pdf, StringType codDispositivaBMED) {
		if(!pdf.isMultiPdf())
			return true;
		PdfDataModel pdfData = pdf.getPdfData();
		PdfDataModel pdfDataElement =(PdfDataModel)pdfData.getPdfs().get(pdfData.getPdfIndex().intValue());
		return pdfDataElement.getCodDispositivaBMED().equals(codDispositivaBMED);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void callPdfVerifiedBeforeGoto(ClientSessionContext csc, PdfModel pdf) {
		try {
			PdfDriverCaller.callPdfVerified(csc, pdf);
			Tools.resetTypesWarningAndErrors(pdf.getPdfData());
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
		}catch(Exception e) { /* do nothing */ }
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfInstanceModel loadPdfInstance(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		PdfInstanceModel pdfInstance = new PdfInstanceModel();
		
		// Se l'istanza è nuova è necessariamente un pdf fatto dall'operatore MOM
		if(pdf.getPdfData().getPdfInstanceId().isNull()){
			pdfInstance.setPdfEnvironment(new StringType(CostantiMOM.MOM_ENVIRONMENT));
			pdfInstance.setPdfCreationUserType(new StringType(ClientSessionContext.USER_TYPE_SEDE));
			return pdfInstance;
		}

		try{
			pdfInstance.setPdfInstanceId(new StringType(pdf.getPdfData().getPdfInstanceId().toString()));
			
			DAOObject dao = new DAOObject(csc, PdfInstanceFacadeBean.DAO_XML_NAME);
			DAOTableResultModel tRes = dao.executeTableLoadAccess("pdfInstance", pdfInstance);
			if(tRes.getResult().intValue() == 0)
				return null;			
			dao.executeQueryAccess("loadPdfInstanceCompletedData", pdfInstance);		
			
			// Se inserita dall'operatore MOM non serve leggere la tabella PDF_MOM_INSTANCE
			if( pdfInstance.byOperatoreMom())
				return pdfInstance;
			
			new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeTableLoadAccess("pdfMomInstance", pdfInstance);		
			
			return pdfInstance;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	private static final String VIRGOLAACAPO = "\",\n";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String drawJsonCallbackObject(Template template, PdfModel pdf){
		
		BooleanType oldCodes = new BooleanType(false);
		try {
			oldCodes = PdfConfig.getParamAsBool(template.getUserSessionContext().getClientSessionContext(), "MOP", "BEFORE_MOP_MESSAGE_CODES");
		}catch(Exception e) { /* do nothing */ }
		
		if(!oldCodes.booleanValue())
			return new JsonCallbackDrawer(template, pdf).drawJsonCallbackObject();

		PdfDataModel pdfData = pdf.getPdfData();
		
		String pdfInstanceId = pdfData.getPdfInstanceId().toString();
		DateType dataSottoscrizione = (DateType)pdfData.read(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
		String ndgs = "";
		for(int i=1; i <= DataLoader.MAX_NUM_CLIENTI; i++){
			StringType ndg = (StringType)pdfData.read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+i);
			if(ndg == null || ndg.isNull()){
				ndgs += ",";
				continue;
			}
			ndgs += Tools.fillSx(ndg.toString(), '0', 11)+",";
		}
		if(ndgs.length() > 0)
			ndgs = ndgs.substring(0, ndgs.length()-1);
		
		String errori = "";
		String warnings = "";
		for(int i=0; i < pdf.getCommandErrors().size(); i++){
			String errore = template.getProperty((CommandError)pdf.getCommandErrors().get(i));
			errore = errore.replace("\\\"", "'").replaceAll("[\\n\\r]"," ");
			errori += "\""+errore+"\",";
		}
		if(errori.length() > 0){
			errori = errori.substring(0, errori.length()-1);
		}else{
			for(int i=0; i < pdf.getCommandWarnings().size(); i++){
				String warning = template.getProperty((CommandWarning)pdf.getCommandWarnings().get(i));
				warning = warning.replace("\\\"", "'").replaceAll("[\\n\\r]"," ");
				warnings += "\""+warning+"\",";
			}
			if(warnings.length() > 0)
				warnings = warnings.substring(0, warnings.length()-1);
		}
		
		StringBuilder res = new StringBuilder();
		res.append("var data = { \n");
		res.append( "\"pdfInstanceId\" : \""+pdfInstanceId+VIRGOLAACAPO);
		res.append( "\"dataSottoscrizione\" : \""+(dataSottoscrizione == null?"":dataSottoscrizione)+VIRGOLAACAPO);
		res.append( "\"clienti\" : \""+ndgs+VIRGOLAACAPO);
		res.append( "\"errori\" : ["+errori+"],\n");
		res.append( "\"warning\" : ["+warnings+"]\n");
		res.append("};\n");
		return res.toString();
	}
	
}
