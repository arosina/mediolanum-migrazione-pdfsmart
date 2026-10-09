package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CounterModel extends CommandDataModel {

	private StringType resourceName = new StringType();
	private StringType counter = new StringType();
	
	public StringType getResourceName() {
		return resourceName;
	}
	public void setResourceName(StringType resourceName) {
		this.resourceName = resourceName;
	}
	public StringType getCounter() {
		return counter;
	}
	public void setCounter(StringType counter) {
		this.counter = counter;
	}
}
