package prgm.pdfwebforms.drivers.io.attach;

import java.util.ArrayList;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideAttachmentsDataResponse{
	
	private String errorMessage = null;
	private ArrayList<Attach> attachments = new ArrayList<Attach>();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addAttach(Attach attach){
		getAttachments().add(attach);
	}

	public ArrayList<Attach> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<Attach> attachments) {
		this.attachments = attachments;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
