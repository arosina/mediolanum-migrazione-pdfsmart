package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.core.PdfPagination;
import prgm.pdfwebforms.drivers.io.PageLoadInputData;
import prgm.pdfwebforms.drivers.io.PageLoadOutputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPage extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			if(pdf.getPdfData().getPdfPageDriver() != null && (pdf.getEventName().isNull() || pdf.getReloadPageOnEvent().booleanValue())){
				PageLoadInputData input = new PageLoadInputData(pdf);
				PageLoadOutputData output = pdf.getPdfData().getPdfPageDriver().onLoad(csc, input);
				if(output != null){
					pdf.setGlobalPageDriverJsScript(output.getGlobalJsScripts());
					pdf.setFieldsJsScripts(output.getFieldsJsScripts());
				}
			}
			pdf.setEventName(new StringType());
			pdf.setReloadPageOnEvent(new BooleanType());
			
			if(pdf.isOperatoreMOM()){
				
				// Confronto per doppia spunta (il metodo verifica se è da fare o meno)
				PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
				facade.confrontaDoppiaSpuntaMom(csc, pdf, pdf.getPdfData());

				AmlFacade amlFacade = (AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class);
				amlFacade.createCoraForMom(csc, pdf);

				pdf.setModality(Template.INSERT_MODALITY);
				if(pdf.getPdfData().getReadonly().booleanValue())
					pdf.setModality(Template.READ_MODALITY);				
			}
			PdfPagination.initModulePagination(pdf, pdf.getPdfData(), pdf.getPdfData().getPdfIndex().intValue());
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
