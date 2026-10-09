package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.business.KeyInstanceRetriever;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/* **********************************************************************************************
 * Inizia/continua la validazione MOM di un pdf
 * **********************************************************************************************/
public class ClonaPdf extends ValidaPdf implements MenuCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = null;
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
            PdfDataModel pdfData = (PdfDataModel)dataModel;
            pdfData.setOperatoreMOM(true);
	
			if(pdfData.getCodDispositivaBMED().isNull() && pdfData.getIdPraticaMOM().isNull()){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Nessuna chiave di riferimento specificata");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			pdfData.setExternalEntityAppl(new StringType(CostantiMOM.MOM_EXTERNAL_KEY_APPL));
			pdfData.setExternalEntityName(new StringType(CostantiMOM.MOM_EXTERNAL_KEY_ENTITY_NAME));
			pdfData.setExternalEntityKey(pdfData.getIdPraticaMOM());

			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){ // Il pdf non esiste con la chiave in input. 
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			// Vedo se il pdf con la chiaveK in input ha già il clone, nel caso lo apro in validazione
			if(existClonedPdfInstanceId(csc, pdfData))
				return super.execute(userSessionContext, dataModel);
			
			pdfData.setPdfEnvironment(new StringType(CostantiMOM.MOM_ENVIRONMENT));
			pdfData.setExternalEntityAppl(new StringType(CostantiMOM.MOM_EXTERNAL_KEY_APPL));

			if(this instanceof ClonaPdfBlank){ // Usato per clonare la carta chimica
				pdfData.setExternalEntityName(new StringType(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_INSERTMENTOMOM_KEY_ENTITY_NAME));
				pdfData.setExternalEntityKey(pdfData.getIdPraticaMOM().isNull() ? pdfData.getPdfInstanceId() : pdfData.getIdPraticaMOM());
				String originalPdfInstanceId = pdfData.getPdfInstanceId().toString();				
				pdf = facade.newPdf(csc, pdfData);
				pdf.setOriginalPdfInstanceId(new StringType(originalPdfInstanceId));
			}else{ 								// Usato per clonare la firma olografa
				pdfData.setExternalEntityName(new StringType(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_VALIDAZIONEMOM_KEY_ENTITY_NAME));
				pdfData.setExternalEntityKey(pdfData.getPdfInstanceId());
				loadOriginalMomVersions(csc, pdfData);
				pdf = facade.clonePdf(csc, pdfData);			
			}
			if(pdf.getInitialErrorMsg() != null){
				pdf.setPdfData(pdfData);
				pdf.setFirstDisplayClass(PdfInitialError.class);
			}

			pdf = facade.savePdfOnMomValidation(csc, pdf);
			
			setNextCommandClass(pdf.getFirstDisplayClass());
			return pdf;
			
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
	@Override
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean existClonedPdfInstanceId(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		StringType clonePfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
														"select PDF_INSTANCE_ID from PDF_INSTANCE "+
														"where ORIGINAL_PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"' "+
														"and EXTERNAL_ENTITY_APPL = '"+CostantiMOM.MOM_EXTERNAL_KEY_APPL+"' "+
														"and EXTERNAL_ENTITY_NAME like '"+CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_KEY_ENTITY_NAME_PREFIX+"%'",
														null, StringType.class).getSingleResult();
		return (clonePfInstanceId != null && !clonePfInstanceId.isNull());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadOriginalMomVersions(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		DAOQueryResultModel qRes = new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeQueryAccess("loadOriginalMomVersions", pdfData);
		if(qRes.getResult().size() == 1) {
			MapCommandDataModel m = (MapCommandDataModel)qRes.getResult().get(0);
			StringType momVersion = (StringType)m.readProperty("momVersion");
			pdfData.setMomVersion(momVersion);
		}else if(qRes.getResult().size() > 1) {
			for(int i=0;i<qRes.getResult().size();i++) {
				MapCommandDataModel m = (MapCommandDataModel)qRes.getResult().get(i);
				StringType momVersion = (StringType)m.readProperty("momVersion");
				PdfDataModel d = new PdfDataModel();
				d.setMomVersion(momVersion);
				pdfData.getPdfs().add(d);
			}
		}
	}
}
