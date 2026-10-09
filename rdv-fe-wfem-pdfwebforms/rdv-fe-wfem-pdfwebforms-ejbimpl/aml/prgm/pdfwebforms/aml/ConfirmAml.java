package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConfirmAml extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
	        PdfModel pdf = (PdfModel)dataModel;
	        
            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;

            AmlFacade facade = (AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class);
            if(!facade.verifyAml(pdf.getAmlModel())) {
            	setNextCommandClass(AmlPage.class);
            	return pdf;
            }
            
            synchronized (pdf) {
            	facade.addAmlHiddenModulesToBasket(csc, pdf);
			}
        	
            pdf.getAmlModel().clearScrollPos();
            
	        if(pdf.getCoraModel() != null)
	        	setNextCommandClass(CoraPage.class);
	        else
	        	setNextCommandClass(PdfPreviewContainer.class);
			return pdf;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
