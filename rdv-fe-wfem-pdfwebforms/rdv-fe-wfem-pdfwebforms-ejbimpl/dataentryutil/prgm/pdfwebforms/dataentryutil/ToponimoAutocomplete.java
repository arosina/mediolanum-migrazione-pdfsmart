package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

/*******************************************************************/
/*******************************************************************/
public class ToponimoAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		ToponimoAutocompleteModel model = (ToponimoAutocompleteModel)dataModel;
		return new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("toponimoAutocomplete",model).getResult();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun toponimo risponde al valore inserito";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ToponimoAutocompleteModel el = (ToponimoAutocompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append("\"descrToponimo\":\""+el.getDescrToponimo()+"\",");
		jsonObj.append("\"codToponimo\":\""+el.getCodToponimo()+"\",");
		jsonObj.append("\"value\":\""+el.getDescrToponimo()+"\",");
		jsonObj.append("\"label\":\""+el.getDescrToponimo()+"\"");
		return jsonObj.toString();
    }

	/********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return ToponimoAutocompleteModel.class;
    }

}