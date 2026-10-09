package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsutil.drivers.util.Util;

/*******************************************************************/
/*******************************************************************/
public class CabFilialeAutocomplete extends AbstractAutocompleteCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8561837715126534289L;

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		return new CabFilialeAutocompleteDao(csc).findList((CabFilialeAutocompleteModel) dataModel);
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName) {
		CabFilialeAutocompleteModel el = (CabFilialeAutocompleteModel) dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"abi\":\"", el.propertyToString("abi"), "\","));
		jsonObj.append(Util.concat("\"descrizioneFiliale\":\"", el.descrizioneFiliale(), "\","));
		jsonObj.append(Util.concat("\"value\":\"", el.propertyToString("cab"), "\","));
		jsonObj.append(Util.concat("\"label\":\"", el.autocompletionLabel(), "\""));
		return jsonObj.toString();
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	public Class getInputViewClass() {
		return CabFilialeAutocompleteModel.class;
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName) {
		return "Nessuna filiale disponibile";
	}
}