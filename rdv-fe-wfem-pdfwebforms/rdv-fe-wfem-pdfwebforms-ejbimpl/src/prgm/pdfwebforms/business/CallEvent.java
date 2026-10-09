package prgm.pdfwebforms.business;

import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.PdfPageDriverIntf;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.drivers.io.PageEventOutputData;
import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class CallEvent extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			if(pdf.getEventName().isNull()){
				setForwardDisplay(new Integer(0));
				return pdf;
			}
			
			PdfPageDriverIntf pdfPageDriver = pdf.getPdfData().getPdfPageDriver();
			if(pdfPageDriver == null){
				setForwardDisplay(new Integer(0));
				return pdf;
			}
			
			PageEventInputData input = new PageEventInputData(pdf); 
			PageEventOutputData output = pdfPageDriver.onEvent(csc, pdf.getEventName().toString(), input);
			if(output != null){
				PdfDriverCaller.manageEventResult(output, pdf, false);
				if(output.isReloadPersons())
					DataLoader.loadPersons(csc, pdf);
			}
			setForwardDisplay(new Integer(0));
			return pdf;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return PdfModel.class;
	}
	
}
