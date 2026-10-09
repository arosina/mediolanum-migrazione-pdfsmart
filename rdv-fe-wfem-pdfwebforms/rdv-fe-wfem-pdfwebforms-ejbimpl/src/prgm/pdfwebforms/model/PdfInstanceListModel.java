package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceListModel extends CommandDataModel {

	private boolean newProcess = true;
	
	// Input params
	private StringType pdfEnvironments = new StringType();
	private StringType codRuoloImpersonato = new StringType();
	// *******************************************************

	private StringType						idToDelete = new StringType();
	private PdfInstanceSearchParamsModel 	params = new PdfInstanceSearchParamsModel();
	private ListType 						pdfList = new ListType(PdfInstanceModel.class);
	
	public ListType getPdfList() {
		return pdfList;
	}
	public void setPdfList(ListType pdfList) {
		this.pdfList = pdfList;
	}
	public StringType getIdToDelete() {
		return idToDelete;
	}
	public void setIdToDelete(StringType idToDelete) {
		this.idToDelete = idToDelete;
	}
	public boolean isNewProcess() {
		return newProcess;
	}
	public void setNewProcess(boolean newProcess) {
		this.newProcess = newProcess;
	}
	public PdfInstanceSearchParamsModel getParams() {
		return params;
	}
	public void setParams(PdfInstanceSearchParamsModel params) {
		this.params = params;
	}
	public StringType getPdfEnvironments() {
		return pdfEnvironments;
	}
	public void setPdfEnvironments(StringType pdfEnvironments) {
		this.pdfEnvironments = pdfEnvironments;
	}
	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}
	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}
}
