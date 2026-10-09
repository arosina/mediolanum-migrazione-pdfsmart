package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.nasstorage.NasFileInfo;
import com.atosorigin.wfem.nasstorage.NasStorage;

/*******************************************************************/
/*******************************************************************/
public class GetPdfReportAdeguatezza extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		setGenericCommandResponse(resp);

		ReportAdeguatezzaCallModel rdaModel = (ReportAdeguatezzaCallModel)dataModel;
		
		if(rdaModel.getIdEcmReportAdeguatezza().isNull()){
			String errResp = "ID Ecm non specificato";
			resp.setContentType("text/html");
			resp.setContentLength(errResp.length());
			resp.setContent(errResp.getBytes());
			return null;
		}

		try {
			NasFileInfo nfi = NasStorage.readFile(csc,rdaModel.getIdEcmReportAdeguatezza().toString());
			if(nfi.fileExists()){
				byte[] result = nfi.getFileContent();
				resp.setContentType("application/pdf");
				resp.setContentLength(result.length);
				resp.setContent(result);
				resp.setSuggestedFileName("Report di Adeguatezza.pdf");
			}else{
				String errResp = "Il file per la raccomandazione con ID Ecm ["+rdaModel.getIdEcmReportAdeguatezza()+"] non esiste";
				resp.setContentType("text/html");
				resp.setContentLength(errResp.length());
				resp.setContent(errResp.getBytes());
			}
		}catch(Exception e){
			String errResp = "Eccezione nel recupero del file: "+e.toString();
			resp.setContentType("text/html");
			resp.setContentLength(errResp.length());
			resp.setContent(errResp.getBytes());
		}
		return null;
			
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ReportAdeguatezzaCallModel.class;
	}

}
