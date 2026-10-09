package prgm.pdfwebforms.messagecodesupdater;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfMessageCodeModel extends CommandDataModel {

	private boolean primaVolta = true;
	private String resultMessage = null;
	private String origConfItemProperties = null;
	
	private StringType confItemName = new StringType();
	private StringType confItemProperties = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfMessageCodeModel() {
		assignSkippableXssValidationFields("confItemProperties");
		addCodDescField("confItemName","CONF_ITEM_NAMES");
	}

	public StringType getConfItemName() {
		return confItemName;
	}
	public void setConfItemName(StringType confItemName) {
		this.confItemName = confItemName;
	}
	public StringType getConfItemProperties() {
		return confItemProperties;
	}
	public void setConfItemProperties(StringType confItemProperties) {
		this.confItemProperties = confItemProperties;
	}

	public boolean isPrimaVolta() {
		return primaVolta;
	}

	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getOrigConfItemProperties() {
		return origConfItemProperties;
	}

	public void setOrigConfItemProperties(String origConfItemProperties) {
		this.origConfItemProperties = origConfItemProperties;
	}

}
