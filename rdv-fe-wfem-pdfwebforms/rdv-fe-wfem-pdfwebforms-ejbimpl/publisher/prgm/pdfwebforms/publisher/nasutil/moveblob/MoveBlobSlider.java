package prgm.pdfwebforms.publisher.nasutil.moveblob;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;

/********************************************************************************/
/********************************************************************************/
public class MoveBlobSlider  extends BusinessCommand {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		MoveBlobModel model = (MoveBlobModel)dataModel;

		String js = "<script>"+
						"var obj = new Object(); "+
						"obj.pdfElaborati = "+model.getPdfElaborati().intValue()+"; "+
						"parent.updateSlider(obj); "+
					"</script>";
		
		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("text/html");
		gcrm.setContent(js.toString().getBytes());
		gcrm.setContentLength(js.length());
		this.setGenericCommandResponse(gcrm);
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return MoveBlobModel.class;
	}


}
