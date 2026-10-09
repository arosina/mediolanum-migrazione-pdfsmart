package prgm.pdfwebforms.publisher.business;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.display.PdfConfiguration;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovoPdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfPublisherModel processModel = (PdfPublisherModel)dataModel;
            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
            PdfConfigurationModel model = f.newPdfConf(csc);
            model.getPdfAnag().setPdfArea(new StringType(processModel.getArea().toString()));
            model.setProfiloUtente(processModel.getProfiloUtente().toString());
    		setNextCommandClass(PdfConfiguration.class);
            return model;
        } catch(Exception e) {
            throw new CommandException(e);
        }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfPublisherModel.class;
	}

}
