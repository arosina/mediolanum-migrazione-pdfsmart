package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class ComuneAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		ComuneAutocompleteModel model = (ComuneAutocompleteModel)dataModel;
		ListType elements = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("comuneAutocomplete",model).getResult();
		return elements;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun comune risponde al valore inserito";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ComuneAutocompleteModel el = (ComuneAutocompleteModel)dataModel;
		StringBuffer jsonObj = new StringBuffer();
		jsonObj.append("\"nazioneComune\":\""+el.getNazioneComune()+"\",");
		jsonObj.append("\"descrNazioneComune\":\""+el.getDescrNazioneComune()+"\",");
		jsonObj.append("\"provinciaComune\":\""+el.getProvinciaComune()+"\",");
		jsonObj.append("\"capComune\":\""+el.getCapComune()+"\",");
		jsonObj.append("\"comune\":\""+Tools.capitalize(el.getComune().toString())+"\",");
		try{ jsonObj.append("\"value\":\""+Tools.getPropertyValue(el, autocompleteFieldName)+"\","); }catch(Exception e){}
		jsonObj.append("\"label\":\""+el.autocompleteLabel()+"\"");
		return jsonObj.toString();
    }

	/********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return ComuneAutocompleteModel.class;
    }

}