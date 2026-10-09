package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.FileType;

import prgm.pdfwebforms.drivers.io.attach.Attach;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class PdfAttachModel extends CommandDataModel {

	private Attach 		driverAttachRef = null;
	private FileType	file = new FileType();
	private FileType	originalFile = new FileType();

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public boolean isImplicitAttach() {
		return getDriverAttachRef() != null && getDriverAttachRef().getImplicitContent() != null;
	}
	
	public Attach getDriverAttachRef() {
		return driverAttachRef;
	}
	public void setDriverAttachRef(Attach driverAttachRef) {
		this.driverAttachRef = driverAttachRef;
	}
	public FileType getFile() {
		return file;
	}
	public void setFile(FileType file) {
		this.file = file;
	}

	public FileType getOriginalFile() {
		return originalFile;
	}

	public void setOriginalFile(FileType originalFile) {
		this.originalFile = originalFile;
	}

}
