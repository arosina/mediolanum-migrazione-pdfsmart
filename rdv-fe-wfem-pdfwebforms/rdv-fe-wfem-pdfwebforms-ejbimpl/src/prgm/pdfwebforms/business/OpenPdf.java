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
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PdfInstanceContentAsBinary;

/* **********************************************************************************************
 * Continua la compilazione di una bozza di pdf.
 * Se lo stato non è più bozza ritorna una pagina con il pdf memorizzato su db
 * **********************************************************************************************/
public class OpenPdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);      
            
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			// Nella open la mom version, se diversa dalla corrente, è memorizzata nei dati xml
			pdfData.setMomVersion(new StringType());
			for(int i=0;i<pdfData.getPdfs().size();i++)
				((PdfDataModel)pdfData.getPdfs().get(i)).setMomVersion(new StringType());					
			
			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			StringType pdfStatus = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
								"select top 1 STATO from PDF_INSTANCE where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
								null, StringType.class).getSingleResult();
			if(pdfStatus != null && !pdfStatus.equals(PdfInstanceModel.STATO_BOZZA)){
				PdfInstanceModel pdfInstance = new PdfInstanceModel();
				pdfInstance.setPdfInstanceId(pdfData.getPdfInstanceId());
				setNextCommandClass(PdfInstanceContentAsBinary.class);		
				return pdfInstance;
			}
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = facade.readPdf(csc, pdfData);
			if(pdf.getInitialErrorMsg() != null && pdf.getInitialErrorMsg().length() > 0){
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
           pdf.clearEndProcessFields();
			
			if( pdf.mainPdfAnag().getHasPriips().booleanValue()){
				setNextCommandClass(PdfPriipsPage.class);
				return pdf;
			}
			
			setNextCommandClass(PdfPage.class);
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
