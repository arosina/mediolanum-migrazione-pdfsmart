package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.display.PdfConfiguration;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApriConf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
    		if(csc.isCliente())
    			throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);
    		
            PdfPublisherModel processModel = (PdfPublisherModel)dataModel;
    		PdfAnagKeyModel pdfAnagKey = processModel.getPdfDaGestire();
            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
            PdfConfigurationModel model = f.openPdfConf(csc, pdfAnagKey);
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
