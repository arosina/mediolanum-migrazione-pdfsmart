package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class LuogoAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		LuogoAutocompleteModel model = (LuogoAutocompleteModel)dataModel;
		ListType elements = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("luogoAutocomplete",model).getResult();
		return elements;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun luogo risponde al valore inserito";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		LuogoAutocompleteModel el = (LuogoAutocompleteModel)dataModel;
		return "\"value\":\""+Tools.capitalize(el.getLuogo().toString())+"\"";
    }

	/********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return LuogoAutocompleteModel.class;
    }

}