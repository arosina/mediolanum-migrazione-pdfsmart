package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class MailAttachmentsModel extends CommandDataModel {
	private IntegerType		mailId		= new IntegerType();
	private StringType		fileName	= new StringType();
	
	public IntegerType getMailId() {
		return mailId;
	}
	public void setMailId(IntegerType mailId) {
		this.mailId = mailId;
	}
	public StringType getFileName() {
		return fileName;
	}
	public void setFileName(StringType fileName) {
		this.fileName = fileName;
	}
}
