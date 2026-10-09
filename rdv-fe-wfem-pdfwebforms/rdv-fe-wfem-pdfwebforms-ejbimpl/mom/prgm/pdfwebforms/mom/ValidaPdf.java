package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.business.KeyInstanceRetriever;
import prgm.pdfwebforms.business.NewPdf;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PdfInstanceContentAsBinary;
import prgm.pdfwebforms.stream.PdfPreview;

/* **********************************************************************************************
 * Inizia/continua la validazione MOM di un pdf
 * **********************************************************************************************/
public class ValidaPdf extends BusinessCommand implements MenuCommand{

	protected boolean calledForImport = false; 

	/***********************************************************************************************/	
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return executeValidaPdf(userSessionContext, dataModel);
	}
	
	/***********************************************************************************************/
	// Per poter testare con junit l'execute delle classi figlie
	/***********************************************************************************************/
	protected CommandDataModel executeValidaPdf(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = null;
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
	   		if(csc.isCliente())
	   			throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       

	   		PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			pdfData.setOperatoreMOM(true);
			
			pdfData.setExternalEntityKey(new StringType(pdfData.getIdPraticaMOM().toString()));

			if(KeyInstanceRetriever.isKeyEmpty(pdfData)){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Nessuna chiave di riferimento specificata");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			pdfData.setExternalEntityAppl(new StringType(CostantiMOM.MOM_EXTERNAL_KEY_APPL));
			pdfData.setExternalEntityName(new StringType(calledForImport ? CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME : CostantiMOM.MOM_EXTERNAL_KEY_ENTITY_NAME));

			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){ // Il pdf non esiste con le chiavi ininput. Ne censiamo uno nuovo per l'operatre MOM

				if(this instanceof CreaPdf){
					
					pdfData.setPdfEnvironment(new StringType(CostantiMOM.MOM_ENVIRONMENT));
					
					NewPdf cmd = new NewPdf();
					pdf = (PdfModel)cmd.execute(userSessionContext, pdfData);
					setNextCommandClass(pdf.getFirstDisplayClass());
					return pdf;
					
				}else{
					
					pdf = new PdfModel();
					pdf.setPdfData(pdfData);				
					pdf.setInitialErrorMsg(err);
					setNextCommandClass(PdfInitialError.class);
					return pdf;
					
				}
				
			}
			
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
													"select ENVIRONMENT, STATO, MODALITA_DI_SOTTOSCRIZIONE, CREATION_USERTYPE "+
													"from PDF_INSTANCE "+
													"where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
													null, MapCommandDataModel.class);
			if(qRes.getResult().size() == 0){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Il pdf esiste come chiave ma non come dati");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			MapCommandDataModel pdfInfo = (MapCommandDataModel)qRes.getResult().get(0);
			StringType pdfEnvironment = (StringType)pdfInfo.readProperty("environment");
			StringType pdfStatus = (StringType)pdfInfo.readProperty("stato");
			StringType pdfCompilationMode  = (StringType)pdfInfo.readProperty("modalitaDiSottoscrizione");
			StringType userType  = (StringType)pdfInfo.readProperty("creationUsertype");
			
			if(pdfEnvironment.equals(CostantiMOM.MOM_ENVIRONMENT) && userType.equals(ClientSessionContext.USER_TYPE_SEDE)){	// Il pdf è stato inserito dall'operatore MOM
				
				if(pdfStatus.equals(PdfInstanceModel.STATO_BOZZA)){ // E' in bozza: procediamo con l'editazione
					
					pdf = facade.readPdfOnMomValidation(csc, pdfData);
					if(pdf.getInitialErrorMsg() != null){
						setNextCommandClass(PdfInitialError.class);
					}else{
						setNextCommandClass(PdfPage.class);
					}
					return pdf;
					
				}else{ 												// E' completato. Visualizziamo il pdf memorizzato a db
					
					PdfInstanceModel pdfInstance = new PdfInstanceModel();
					pdfInstance.setPdfInstanceId(pdfData.getPdfInstanceId());
					setNextCommandClass(PdfInstanceContentAsBinary.class);		
					return pdfInstance;
				}
				
			}else{													// Il pdf è stato inserito dalla rete. Procedo con la validazione 
				
				if(pdfCompilationMode.isNull()){
					pdf = new PdfModel();
					pdf.setPdfData(pdfData);				
					pdf.setInitialErrorMsg("Lo stato del pdf non risulta congruente con la validazione di sede");
					setNextCommandClass(PdfInitialError.class);
					return pdf;
				}
				
				StringType statoValidazione =  (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
														"select STATO from PDF_MOM_INSTANCE where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
														null, StringType.class).getSingleResult();
				if(statoValidazione == null || statoValidazione.equals(PdfInstanceModel.STATO_BOZZA)){
					
					pdf = facade.readPdfOnMomValidation(csc, pdfData);
					if(pdf.getInitialErrorMsg() != null){
						setNextCommandClass(PdfInitialError.class);
					}else{
						if(pdfCompilationMode.equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) || 
						   pdfCompilationMode.equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO)) {
							pdf.setModality(Template.READ_MODALITY);
						}
						setNextCommandClass(PdfPage.class);
					}
					return pdf;
					
				}else{
					
					pdfData.setPdfEnvironment(new StringType("onlyPrint"));
					pdfData.setOnlyPrint(true);
					
					pdf = facade.readPdfOnMomValidation(csc, pdfData);
					if(pdf.getInitialErrorMsg() != null){
						setNextCommandClass(PdfInitialError.class);
					}else{
						setNextCommandClass(PdfPreview.class);
					}
					return pdf;
					
				}
				
			}
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}finally{
			if(pdf != null)
				pdf.getPdfData().setOperatoreMOM(true);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

	/***********************************************************************************************/
	/* Usato da WS generaIstanzaMom */ 
	/***********************************************************************************************/
	public void setCalledForImport(boolean calledForImport) {
		this.calledForImport = calledForImport;
	}

}
