package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CampoDispositivaCarrelloModel extends CommandDataModel{

	private StringType	fieldName = new StringType();
	private StringType	fieldValue = new StringType();
	
	public StringType getFieldName() {
		return fieldName;
	}
	public void setFieldName(StringType fieldName) {
		this.fieldName = fieldName;
	}
	public StringType getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(StringType fieldValue) {
		this.fieldValue = fieldValue;
	}

}
