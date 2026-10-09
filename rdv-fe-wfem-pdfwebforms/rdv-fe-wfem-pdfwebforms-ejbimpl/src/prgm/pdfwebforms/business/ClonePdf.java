package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPriipsPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Inizia il processo di compilazione di un nuovo pdf
 * **********************************************************************************************/
public class ClonePdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       
            
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = null;
			
			if(pdfData.getPdfInstanceId().isNull()){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Id dell'istanza da clonare non valorizzata");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}

			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}

			// Se esiste già un pdf clonato con i dati in inpt lo apriamo
			String sql = "select PDF_INSTANCE_ID from PDF_INSTANCE "+
						 "where ORIGINAL_PDF_INSTANCE_ID='"+pdfData.getPdfInstanceId()+"' ";
			if(!pdfData.getExternalEntityAppl().isNull())
				sql += "and EXTERNAL_ENTITY_APPL='"+pdfData.getExternalEntityAppl()+"' ";
			if(!pdfData.getExternalEntityName().isNull())
				sql += "and EXTERNAL_ENTITY_NAME='"+pdfData.getExternalEntityName()+"' ";
			if(!pdfData.getExternalEntityKey().isNull())
				sql += "and EXTERNAL_ENTITY_KEY='"+pdfData.getExternalEntityKey()+"' ";
			StringType cloneId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", sql, null, StringType.class).getSingleResult();
			if(cloneId != null && !cloneId.isNull()){
				pdfData.setPdfInstanceId(cloneId);
				setNextCommandClass(OpenPdf.class);
				return pdfData;
			}
			
			pdf = facade.clonePdf(csc, pdfData);
			
			if(pdf.getInitialErrorMsg() != null){
				pdf.setPdfData(pdfData);
				pdf.setFirstDisplayClass(PdfInitialError.class);
			}
			
			if( pdf.getFirstDisplayClass().getName().equals(PdfPage.class.getName()) &&
				pdf.mainPdfAnag().getHasPriips().booleanValue()){
				setNextCommandClass(PdfPriipsPage.class);
				return pdf;
			}
			setNextCommandClass(pdf.getFirstDisplayClass());
			return pdf;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

}
