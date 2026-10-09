package prgm.pdfwebforms.validation;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfValidationEventDataModel extends CommandDataModel {

	private static final long serialVersionUID = 1L;

	private boolean	onEventCall = false;

	// Input
	private StringType	actionName = new StringType();
	private BooleanType	doValidation = new BooleanType();
	private BooleanType	doAdeguatezza = new BooleanType();

	// Result
	private String isAdeguato = "";
	private String hasErrors = "";
	private String hasWarnings = "";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void clearResult() {
		setIsAdeguato("");
		setHasErrors("");
		setHasWarnings("");
	}

	public boolean isOnEventCall() {
		return onEventCall;
	}
	public void setOnEventCall(boolean onEventCall) {
		this.onEventCall = onEventCall;
	}
	public StringType getActionName() {
		return actionName;
	}
	public void setActionName(StringType actionName) {
		this.actionName = actionName;
	}
	public BooleanType getDoValidation() {
		return doValidation;
	}
	public void setDoValidation(BooleanType doValidation) {
		this.doValidation = doValidation;
	}
	public BooleanType getDoAdeguatezza() {
		return doAdeguatezza;
	}
	public void setDoAdeguatezza(BooleanType doAdeguatezza) {
		this.doAdeguatezza = doAdeguatezza;
	}
	public String getIsAdeguato() {
		return isAdeguato;
	}
	public void setIsAdeguato(String isAdeguato) {
		this.isAdeguato = isAdeguato;
	}
	public String getHasErrors() {
		return hasErrors;
	}
	public void setHasErrors(String hasErrors) {
		this.hasErrors = hasErrors;
	}
	public String getHasWarnings() {
		return hasWarnings;
	}
	public void setHasWarnings(String hasWarnings) {
		this.hasWarnings = hasWarnings;
	}
	

}
