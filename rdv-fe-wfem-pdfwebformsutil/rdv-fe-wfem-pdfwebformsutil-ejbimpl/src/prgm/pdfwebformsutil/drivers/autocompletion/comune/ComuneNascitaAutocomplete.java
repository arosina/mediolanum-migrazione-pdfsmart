package prgm.pdfwebformsutil.drivers.autocompletion.comune;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsutil.drivers.util.Util;

public class ComuneNascitaAutocomplete extends AbstractAutocompleteCommand {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1528653663224085035L;
	public static final String NAZIONEITALIA = "ITALIA";

	/********************************************************************************/
    /********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ComuneNascitaAutocompleteModel el = (ComuneNascitaAutocompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"nazioneComune\":\"", NAZIONEITALIA, "\","));
		jsonObj.append(Util.concat("\"provinciaComune\":\"", el.getProvinciaComune(), "\","));
		jsonObj.append(Util.concat("\"comune\":\"", Tools.capitalize(el.getComune().toString()), "\","));
		try{ 
			jsonObj.append(Util.concat("\"value\":\"", Tools.getPropertyValue(el, autocompleteFieldName), "\",")); 
		}catch(Exception e){	
			// Nessun errore deve bloccare 
		}
		jsonObj.append(Util.concat("\"label\":\"", el.autocompleteLabel(), "\""));
		return jsonObj.toString();
	}

	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		return new ComuneNascitaAutocompleteDao(csc).findList((ComuneNascitaAutocompleteModel) dataModel);
	}

	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName) {
		return "Nessun comune risponde al valore inserito";
	}

	@Override
	public Class getInputViewClass() {
		return ComuneNascitaAutocompleteModel.class;
	}
}
