package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsutil.drivers.util.Util;

/*******************************************************************/
/*******************************************************************/
public class AbiBancaAutocomplete extends AbstractAutocompleteCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4592939189379469587L;

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		return new AbiBancaAutocompleteDao(csc).findList((AbiBancaAutocompleteModel) dataModel);
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName) {
		AbiBancaAutocompleteModel el = (AbiBancaAutocompleteModel) dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"descrizioneBanca\":\"", el.descrizioneBanca().replaceAll("\"", "'"), "\","));
		jsonObj.append(Util.concat("\"value\":\"", el.propertyToString("abi"), "\","));
		jsonObj.append(Util.concat("\"label\":\"", el.autocompletionLabel().replaceAll("\"", "&quot;"), "\""));
		return jsonObj.toString();
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	public Class getInputViewClass() {
		return AbiBancaAutocompleteModel.class;
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName) {
		return "Nessuna banca disponibile";
	}
}