package prgm.pdfwebformsutil.drivers.autocompletion.nazione;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsutil.drivers.util.Util;

/*******************************************************************/
/*******************************************************************/
public class NazioneAutocomplete extends AbstractAutocompleteCommand {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8793681613513745518L;

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		return new NazioneAutocompleteDao(csc).findList((NazioneAutocompleteModel) dataModel);
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName) {
		NazioneAutocompleteModel el = (NazioneAutocompleteModel) dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"value\":\"", el.getNazione(), "\","));
		jsonObj.append(Util.concat("\"label\":\"", el.getNazione().toString().replaceAll("\"", "&quot;"), "\""));
		return jsonObj.toString();
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	public Class getInputViewClass() {
		return NazioneAutocompleteModel.class;
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName) {
		return "Nessuna nazione disponibile";
	}
}