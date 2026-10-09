package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.core.PdfPagination;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.display.PdfPriipsPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Inizia il processo di compilazione di un nuovo pdf
 * **********************************************************************************************/
public class NewPdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = null;
			
			if(!pdfData.getPdfInstanceId().isNull()){
				String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
				if(err != null){
					pdf = new PdfModel();
					pdf.setPdfData(pdfData);
					pdf.setInitialErrorMsg(err);
					setNextCommandClass(PdfInitialError.class);
					return pdf;
				}
				pdf = facade.clonePdf(csc, pdfData);
			}else{
				pdf = facade.newPdf(csc, pdfData);
			}

			if(pdf.getInitialErrorMsg() != null){
				pdf.setPdfData(pdfData);
				pdf.setFirstDisplayClass(PdfInitialError.class);
			}else{
				if(pdfData.isOnlyPrint() || pdfData.getSkipDataentry().booleanValue()){
					for(int maxTry=0;maxTry<100;maxTry++){
						ConfirmPdf cmd = new ConfirmPdf();
						pdf = (PdfModel)cmd.execute(userSessionContext, pdf);
						if(pdf.hasCommandErrors()){
							pdf.setFirstDisplayClass(PdfPage.class);
							break;
						}else{
							if(cmd.getNextCommand().equals(PdfPreviewContainer.class.getName())){
								pdf.setFirstDisplayClass(PdfPreviewContainer.class);
								break;
							}
						}
					}
				}
			}
			PdfPagination.initPdfPagination(pdf);
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
