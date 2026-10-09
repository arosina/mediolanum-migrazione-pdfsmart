package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/* **********************************************************************************************
 * Inizia/continua la validazione MOM di un pdf
 * **********************************************************************************************/
public class CreaPdf extends ValidaPdf implements MenuCommand{
	
	private static final String DAO_CREAPDF_XML_NAME = "PdfWebForms.PdfMomCreaPdf";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			 if(pdfData.getPdfs().size() > 0) {
				 PdfDataModel pdfDataEl = (PdfDataModel)pdfData.getPdfs().get(0);
				 pdfData.setCodDispositivaBMED(pdfDataEl.getCodDispositivaBMED());
			 }
			
			deleteIfExist(csc, pdfData);
			
			PdfModel pdf = (PdfModel)super.execute(userSessionContext, dataModel);
			if(!calledForImport)
				pdf = saveMomPdf(csc, pdf);
			return pdf;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel saveMomPdf(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(pdf.getInitialErrorMsg() == null && pdf.getPdfData().getPdfInstanceId().isNull()) {
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdf = facade.savePdfOnMomValidation(csc, pdf);
		}
		return pdf;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void deleteIfExist(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		
		if(pdfData.getIdPraticaMOM().isNull())
			return;
		
		DAOObject dao = new DAOObject(csc, DAO_CREAPDF_XML_NAME);
		StringType pdfInstanceId = (StringType)dao.executeQueryAccess("loadPdfInstanceIdPraticaMOM", pdfData).getSingleResult();
		if(pdfInstanceId == null || pdfInstanceId.isNull())
			return;
		
		StringType clonedPdfInstanceId = getClonedPdfInstanceId(dao, pdfInstanceId);
		
		boolean committed = false;
		dao = new DAOObject(csc, PdfInstanceFacadeBean.DAO_XML_NAME);
		try {
			dao.beginTransaction();
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(pdfInstanceId);
			try{ dao.executeTableDeleteChildsAccess("pdfInstanceDett", pdfInstance); }catch(NoRowsAffected nra){ /* do nothing */}
			try{ dao.executeTableDeleteAccess("deletePdfMomInstance", pdfInstance); }catch(NoRowsAffected nra){ /* do nothing */}
			dao.executeTableDeleteAccess("pdfInstance", pdfInstance);
			if(!clonedPdfInstanceId.isNull()) {
				pdfInstance.setPdfInstanceId(clonedPdfInstanceId);
				try{ dao.executeTableDeleteAccess("pdfInstance", pdfInstance); }catch(NoRowsAffected nra){ /* do nothing */}
			}
			dao.commitTransaction();
			committed = true;
		}finally {
			if(!committed)
				dao.rollbackTransaction();			
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private StringType getClonedPdfInstanceId(DAOObject dao, StringType pdfInstanceId) throws DAOException{
		PdfInstanceModel pdfInstance = new PdfInstanceModel();
		pdfInstance.setPdfInstanceId(pdfInstanceId);
		StringType clonedPdfInstanceId = (StringType)dao.executeQueryAccess("getClonedPdfInstanceId", pdfInstance).getSingleResult();
		return clonedPdfInstanceId == null ? new StringType() : clonedPdfInstanceId;
	}

}
