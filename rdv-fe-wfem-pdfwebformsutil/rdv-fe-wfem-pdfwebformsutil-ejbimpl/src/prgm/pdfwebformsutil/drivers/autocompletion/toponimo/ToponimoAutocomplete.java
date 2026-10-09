package prgm.pdfwebformsutil.drivers.autocompletion.toponimo;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsutil.drivers.util.Util;

/*******************************************************************/
/*******************************************************************/
public class ToponimoAutocomplete extends AbstractAutocompleteCommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8361587393892247979L;

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		return new ToponimoAutocompleteDao(csc).findList((ToponimoAutocompleteModel) dataModel);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun toponimo disponibile";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ToponimoAutocompleteModel el = (ToponimoAutocompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"value\":\"", el.getCodiceToponimo(), "\","));
		jsonObj.append(Util.concat("\"label\":\"", el.getDescrizioneToponimo(), "\""));
		return jsonObj.toString();
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	public Class getInputViewClass() {
		return ToponimoAutocompleteModel.class;
	}

}