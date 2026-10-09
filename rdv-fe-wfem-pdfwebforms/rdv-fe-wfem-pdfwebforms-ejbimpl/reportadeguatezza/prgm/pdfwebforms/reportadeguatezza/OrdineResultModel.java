package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrdineResultModel extends CommandDataModel {

	// Deroga risultante da sostituzione
	private StringType 	derogaResult = new StringType();

	public StringType getDerogaResult() {
		return derogaResult;
	}

	public void setDerogaResult(StringType derogaResult) {
		this.derogaResult = derogaResult;
	}	
}
