package prgm.pdfwebforms.idd;

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
public class GetPdfRaccomandazioneIdd extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		setGenericCommandResponse(resp);

		IddCallModel iddModel = (IddCallModel)dataModel;
		
		if(iddModel.getIdEcmRaccomandazioneIdd().isNull()){
			String errResp = "ID Ecm non specificato";
			resp.setContentType("text/html");
			resp.setContentLength(errResp.length());
			resp.setContent(errResp.getBytes());
			return null;
		}

		try {
			NasFileInfo nfi = NasStorage.readFile(csc, iddModel.getIdEcmRaccomandazioneIdd().toString());
			if(nfi.fileExists()){
				byte[] result = nfi.getFileContent();
				resp.setContentType("application/pdf");
				resp.setContentLength(result.length);
				resp.setContent(result);
				resp.setSuggestedFileName("Raccomandazione Personalizzata.pdf");
			}else{
				String errResp = "Il file per la raccomandazione con ID Ecm ["+iddModel.getIdEcmRaccomandazioneIdd()+"] non esiste";
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
		return IddCallModel.class;
	}

}
