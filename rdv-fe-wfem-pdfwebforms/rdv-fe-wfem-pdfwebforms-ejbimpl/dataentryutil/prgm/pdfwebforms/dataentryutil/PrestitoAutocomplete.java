package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

/*******************************************************************/
/*******************************************************************/
public class PrestitoAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		PrestitoAutocompleteModel model = (PrestitoAutocompleteModel)dataModel;
		return new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("prestitoAutocomplete",model).getResult();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun prestito disponibile";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		PrestitoAutocompleteModel el = (PrestitoAutocompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append(el.createJsonData(autocompleteFieldName));
		return jsonObj.toString();
    }

	/********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return PrestitoAutocompleteModel.class;
    }

}