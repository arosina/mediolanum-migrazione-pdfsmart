package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.model.PdfInstanceListModel;
import prgm.pdfwebforms.model.PdfInstanceModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DeletePdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfInstanceListModel pdfInstanceList = (PdfInstanceListModel)dataModel;
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfInstanceList.getIdToDelete().toString()));
			pdfInstance.setCodRuoloImpersonato(pdfInstanceList.getCodRuoloImpersonato());
            
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			facade.deletePdf(csc, pdfInstance);
			
			setForwardDisplay(new Integer(0));
			return pdfInstanceList;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfInstanceListModel.class;
	}

}
