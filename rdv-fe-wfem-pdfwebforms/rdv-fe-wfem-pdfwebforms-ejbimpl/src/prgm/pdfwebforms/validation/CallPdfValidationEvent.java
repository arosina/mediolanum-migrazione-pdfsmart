package prgm.pdfwebforms.validation;

import java.util.List;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventOutputData;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CallPdfValidationEvent extends BusinessCommand{
	
	private static final long serialVersionUID = 1L;

	private static final String S_FALSE = "false";
	private static final String S_TRUE = "true";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
        ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PdfModel pdf = (PdfModel)dataModel;
        
        PdfValidationEventDataModel validationEventData = pdf.getPdfValidationEventData();
        validationEventData.clearResult();
        validationEventData.setOnEventCall(true);
        
		setForwardDisplay(0);
		
		try{
            
           	DataLoader.loadPersons(csc, pdf);
			
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
			Tools.resetTypesWarningAndErrors(pdf.getPdfData());
			pdf.setErroriAdeguatezza(null);
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			
			// Call validation
			if(validationEventData.getDoValidation().equals(S_TRUE))
				doValidation(csc, pdf, facade);
			
			// Call MIFID & IDD
			if(!pdf.hasCommandErrors() && validationEventData.getDoAdeguatezza().equals(S_TRUE))
				doAdeguatezza(csc, pdf);
			
			// Call validationEvent on driver
			ValidationEventOutputData eventOutput = PdfDriverCaller.callValidationEvent(csc, pdf);
			if(eventOutput != null && eventOutput.getErrorMessage() != null && eventOutput.getErrorMessage().length() > 0)
				pdf.addCommandError(eventOutput.getErrorMessage());
			
			// Se ci sono errori i warning non hanno significato
			if(pdf.hasCommandErrors())
				pdf.resetCommandWarnings();
			
		}catch(Exception e){
			pdf.resetCommandErrors();
			pdf.addCommandError("Eccezione grave: "+e.toString());
		}
		
		return pdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static PdfModel doValidation(ClientSessionContext csc, PdfModel pdf, PdfInstanceFacade facade) throws CommandException {
		try{
			pdf = facade.verifyPdf(csc, pdf);
			
			PdfValidationEventDataModel validationEventData = pdf.getPdfValidationEventData();
			validationEventData.setHasErrors(S_FALSE);
			if(pdf.hasCommandErrors()) {
				validationEventData.setHasErrors(S_TRUE);
			}else{
				validationEventData.setHasWarnings(S_FALSE);
				if(pdf.hasCommandWarnings()) {
					validationEventData.setHasWarnings(S_TRUE);
				}
			}
			return pdf;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void doAdeguatezza(ClientSessionContext csc, PdfModel pdf) throws CommandException {
		try{
			String[] erroriAdeguatezza = MifidCaller.callOnSign(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.getPdfValidationEventData().setIsAdeguato(S_FALSE);
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				String erroriAdeguatezzaMifid = erroriAdeguatezza[0];
				if(erroriAdeguatezzaMifid != null){
					List<String> elencoMsgAdgMifid = MifidCaller.formattaErroriAdeguatezzaMifid(erroriAdeguatezzaMifid);
					for(String mifidMsg : elencoMsgAdgMifid)
						pdf.addCommandError("<b>MIFID:</b><br>"+mifidMsg+"<br>");
				}					
				
				String erroriAdeguatezzaIdd = erroriAdeguatezza[1];
				if(erroriAdeguatezzaIdd != null)
					pdf.addCommandError("<b>IDD:</b><br>"+erroriAdeguatezzaIdd);
				
			}else {
				pdf.getPdfValidationEventData().setIsAdeguato(S_TRUE);
			}
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String drawJsonCallbackObject(Template template, PdfModel pdf){
		
		PdfDataModel pdfData = pdf.getPdfData();
		PdfValidationEventDataModel pdfValidationEventData = pdf.getPdfValidationEventData();
		
		StringBuilder errori = new StringBuilder();
		StringBuilder warnings = new StringBuilder();
		for(int i=0; i < pdf.getCommandErrors().size(); i++){
			String errore = template.getProperty((CommandError)pdf.getCommandErrors().get(i));
			errore = errore.replaceAll("\\\"", "'").replaceAll("[\\n\\r]"," ");
			errori.append("\""+errore+"\",");
		}
		if(errori.length() > 0){
			errori.deleteCharAt(errori.length()-1);
		}else{
			for(int i=0; i < pdf.getCommandWarnings().size(); i++){
				String warning = template.getProperty((CommandWarning)pdf.getCommandWarnings().get(i));
				warning = warning.replaceAll("\\\"", "'").replaceAll("[\\n\\r]"," ");
				warnings.append("\""+warning+"\",");
			}
			if(warnings.length() > 0)
				warnings.deleteCharAt(warnings.length()-1);
		}
		
		String idEsitoMifid = "";
		if(pdf.getMifidCallModel() != null && pdfValidationEventData.getDoAdeguatezza().booleanValue())
			idEsitoMifid = pdf.getMifidCallModel().getIdEsito().toString();
			
		String endString = "\",\n";
		StringBuilder res = new StringBuilder();
		res.append("var data = { \n");
		
		res.append("	\"actionName\" : \""+pdfValidationEventData.getActionName().toString()+endString);
		res.append("	\"doValidation\" : \""+pdfValidationEventData.getDoValidation().toString()+endString);
		res.append("	\"doAdeguatezza\" : \""+pdfValidationEventData.getDoAdeguatezza().toString()+endString);
		
		res.append("	\"pdfInstanceId\" : \""+pdfData.getPdfInstanceId().toString()+endString);
		res.append("	\"isAdeguato\" : \""+pdfValidationEventData.getIsAdeguato()+endString);
		res.append("	\"idEsitoMifid\" : \""+idEsitoMifid+endString);
		res.append("	\"hasErrors\" : \""+pdfValidationEventData.getHasErrors()+endString);
		res.append("	\"hasWarnings\" : \""+pdfValidationEventData.getHasWarnings()+endString);
		res.append("	\"errori\" : ["+errori+"],\n");
		res.append("	\"warning\" : ["+warnings+"]\n");
		res.append("};\n");
		return res.toString();
	}
	
}
