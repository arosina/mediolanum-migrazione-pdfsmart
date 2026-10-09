package prgm.pdfwebforms.publisher.events;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class OnChangeProdottoPrit extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
			model.getPdfAnag().addCodDescField("pdfCodOperazionePrit","OperazioniProdottoPrit");
			
            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
			model.setPdfAnag((PdfAnagModel)f.fillCodDesc(csc,model.getPdfAnag(),false));
			model.getPdfAnag().setPdfCodOperazionePrit(new IntegerType());
			
			setForwardDisplay(new Integer(0));
			return model;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

}
