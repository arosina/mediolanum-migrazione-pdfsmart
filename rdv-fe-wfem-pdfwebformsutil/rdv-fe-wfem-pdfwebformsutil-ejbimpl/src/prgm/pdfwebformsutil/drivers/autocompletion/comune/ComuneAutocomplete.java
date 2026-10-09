package prgm.pdfwebformsutil.drivers.autocompletion.comune;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.dataentryutil.ComuneAutocompleteModel;
import prgm.pdfwebformsutil.drivers.util.Util;

public class ComuneAutocomplete extends prgm.pdfwebforms.dataentryutil.ComuneAutocomplete {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -712718227693338930L;
	public static final String NAZIONEITALIA = "ITALIA";

	/********************************************************************************/
    /********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ComuneAutocompleteModel el = (ComuneAutocompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(Util.concat("\"nazioneComune\":\"", NAZIONEITALIA, "\","));
		jsonObj.append(Util.concat("\"provinciaComune\":\"", el.getProvinciaComune(), "\","));
		jsonObj.append(Util.concat("\"capComune\":\"", el.getCapComune(), "\","));
		jsonObj.append(Util.concat("\"comune\":\"", Tools.capitalize(el.getComune().toString()), "\","));
		try{ 
			jsonObj.append(Util.concat("\"value\":\"", Tools.getPropertyValue(el, autocompleteFieldName), "\",")); 
		}catch(Exception e){			
		}
		jsonObj.append(Util.concat("\"label\":\"", el.autocompleteLabel(), "\""));
		return jsonObj.toString();
	}
}
