package prgm.pdfwebforms.idd;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class GetIdEcmRaccomandazioneIdd extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		
		PdfModel pdf = (PdfModel)dataModel;

		String idEcmRaccomandazioneIdd = "";
		String errMsg = "";
		try{
			idEcmRaccomandazioneIdd = IddCaller.callRecuperaIdEcmRaccomandazioneIdd(csc, pdf);
			pdf.setRaccomandazioneIddClicked(true);
		}catch(Exception e){
			errMsg = e.toString();
		}

		String jsonObj = "{ \"isRaccomandazioneIddClicked\": \""+pdf.isRaccomandazioneIddClicked()+"\", " +
						   "\"idEcmRaccomandazioneIdd\": \""+idEcmRaccomandazioneIdd+"\", " +
						   "\"errMsg\": \""+errMsg+"\" }";

		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("application/json");
		try{
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
		}catch(UnsupportedEncodingException uee){
			gcrm.setContent(jsonObj.toString().getBytes());
		}
		gcrm.setContentLength(jsonObj.length());
		setGenericCommandResponse(gcrm);
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
