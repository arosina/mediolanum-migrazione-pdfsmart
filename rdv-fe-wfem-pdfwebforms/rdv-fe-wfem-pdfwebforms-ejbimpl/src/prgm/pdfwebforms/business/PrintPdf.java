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
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PdfInstanceContentAsBinary;
import prgm.pdfwebforms.stream.PrintPdfResult;

/* **********************************************************************************************
 * Se la chiave dell'istanza è vuota produce il pdf con il codice e i dati in request
 * Se la chiave viene valorizzata produce il pdf memorizzato su db (stato non bozza) o generato con i dati (stato bozza) 
 * **********************************************************************************************/
public class PrintPdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

            PdfDataModel pdfData = (PdfDataModel)dataModel;
            
            // Se richiamato dal WS per la stampa senza arricchimenti lascio l'environment già impostato		
            if(!pdfData.getPdfEnvironment().equals(PdfInstanceFacadeBean.ONLY_FLAT_PRINT))
            	pdfData.setPdfEnvironment(new StringType("onlyPrint"));
			pdfData.setOnlyPrint(true);

			PdfModel pdf = null;
			
			if(KeyInstanceRetriever.isKeyEmpty(pdfData)){
				
				NewPdf cmd = new NewPdf();
				pdf = (PdfModel)cmd.execute(userSessionContext, pdfData);
				if(!pdf.getFirstDisplayClass().getName().equals(PdfPreviewContainer.class.getName()))
					setNextCommandClass(pdf.getFirstDisplayClass());
				else
					setNextCommandClass(PrintPdfResult.class);
				
			}else{
				
				String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
				if(err != null){
					pdf = new PdfModel();
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
				pdf = facade.readPdf(csc, pdfData);
				if(pdf.getInitialErrorMsg() != null){
					setNextCommandClass(PdfInitialError.class);
				}else{
					setNextCommandClass(PrintPdfResult.class);
				}
				
			}
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
