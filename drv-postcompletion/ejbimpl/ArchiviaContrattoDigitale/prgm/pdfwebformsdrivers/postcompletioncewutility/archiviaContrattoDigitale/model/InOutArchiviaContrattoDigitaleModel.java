package prgm.pdfwebformsdrivers.postcompletioncewutility.archiviaContrattoDigitale.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class InOutArchiviaContrattoDigitaleModel extends CommandDataModel {

	// Input
	private StringType pdfInstanceId = new StringType();
	private StringType xmlRequest = new StringType();

	// Output
	private StringType xmlResponse = new StringType();
	private StringType returnCode = new StringType();
	private StringType filenetGuid = new StringType();

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}

	public StringType getXmlRequest() {
		return xmlRequest;
	}

	public void setXmlRequest(StringType xmlRequest) {
		this.xmlRequest = xmlRequest;
	}

	public StringType getXmlResponse() {
		return xmlResponse;
	}

	public void setXmlResponse(StringType xmlResponse) {
		this.xmlResponse = xmlResponse;
	}

	public StringType getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(StringType returnCode) {
		this.returnCode = returnCode;
	}

	public StringType getFilenetGuid() {
		return filenetGuid;
	}

	public void setFilenetGuid(StringType filenetGuid) {
		this.filenetGuid = filenetGuid;
	}
}
