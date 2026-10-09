package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;

/*******************************************************************/
/*******************************************************************/
public class NumeroPolizzaAutocomplete extends AbstractAutocompleteCommand {
	
	public static final String MSG_IL_CLIENTE_NON_HA_POLIZZE_IN_PORTAFOGLIO = "Il cliente non ha polizze in portafoglio";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1740877566135119949L;

	/********************************************************************************/
	/********************************************************************************/
	@Override
	public ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		NumeroPolizzaAutoCompleteModel model = (NumeroPolizzaAutoCompleteModel)dataModel;
		ListType elements = new ListType();
		
		if(!model.getCodiceAgente().isNull() && !model.getNdgCliente().isNull())
			elements = new DAOObject(csc,PdfDriver.DAO_FILE_NAME).executeQueryAccess("numeroPolizzaAutocomplete",model).getResult();
		return elements;
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		NumeroPolizzaAutoCompleteModel el = (NumeroPolizzaAutoCompleteModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append("\"numeroContratto\":\""+el.getNumeroContratto()+"\",");
		jsonObj.append("\"codProdottoPolizza\":\""+el.getCodProdottoPolizza()+"\",");
		jsonObj.append("\"numeroPolizza\":\""+el.getNumeroPolizza()+"\",");
		jsonObj.append("\"flagTrasformatoPic\":\""+el.getFlagTrasformatoPic()+"\",");
		jsonObj.append("\"formaContrattuale\":\""+el.getFormaContrattuale()+"\",");
		jsonObj.append("\"value\":\""+el.autocompletionValue()+"\",");
		jsonObj.append("\"label\":\""+el.autocompletionLabel()+"\"");
		return jsonObj.toString();
    }

    /********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return NumeroPolizzaAutoCompleteModel.class;
    }

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return NumeroPolizzaAutocomplete.MSG_IL_CLIENTE_NON_HA_POLIZZE_IN_PORTAFOGLIO;
	}
}
