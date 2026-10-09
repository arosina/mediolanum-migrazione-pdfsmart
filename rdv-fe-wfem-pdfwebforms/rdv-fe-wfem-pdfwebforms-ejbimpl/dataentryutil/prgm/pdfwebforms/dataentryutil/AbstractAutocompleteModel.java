package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class AbstractAutocompleteModel extends MapCommandDataModel {

	private boolean		notFound = false;
	private StringType  noElementsIndicator = new StringType();
	private StringType  autocompleteFieldName = new StringType();
	private StringType  autocompleteTerm = new StringType();

	public StringType getAutocompleteFieldName() {
		return autocompleteFieldName;
	}

	public void setAutocompleteFieldName(StringType autocompleteFieldName) {
		this.autocompleteFieldName = autocompleteFieldName;
	}

	public StringType getAutocompleteTerm() {
		return autocompleteTerm;
	}

	public void setAutocompleteTerm(StringType autocompleteTerm) {
		this.autocompleteTerm = autocompleteTerm;
	}

	public boolean isNotFound() {
		return notFound;
	}

	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}

	public StringType getNoElementsIndicator() {
		return noElementsIndicator;
	}

	public void setNoElementsIndicator(StringType noElementsIndicator) {
		this.noElementsIndicator = noElementsIndicator;
	}

}
