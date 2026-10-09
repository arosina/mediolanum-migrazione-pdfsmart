package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondiModel;

/*******************************************************************/
/*******************************************************************/
public class RicercaFondoAutocomplete extends AbstractAutocompleteCommand {
	
	public static final String MSG_NESSUN_FONDO_DISPONIBILE = "Nessun fondo disponibile";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1474932912952221267L;
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {

		RicercaFondiModel model = (RicercaFondiModel)dataModel;		
		model.setCodiceProdotto(new StringType("MEDBOX"));
		ListType elencoFondi;
		DAOObject dao = new DAOObject(csc,PdfDriver.DAO_FILE_NAME);
		dao.setQueryMaxResultRows(10);
		elencoFondi = dao.executeQueryAccess("ricercaComparti", model).getResult();

		return elencoFondi;
	}	
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		FondoModel el = (FondoModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append("\"lineaFondo\":\""+el.getLineaFondo()+"\",");
		jsonObj.append("\"codiceFondo\":\""+el.getCodiceFondo()+"\",");
		jsonObj.append("\"importoMinimo\":\""+el.getImportoMinimo()+"\",");
		jsonObj.append("\"societaFondo\":\""+el.getSicav()+"\",");
		jsonObj.append("\"isinFondo\":\""+el.getIsin()+"\",");
		jsonObj.append("\"descrizioneFondo\":\""+el.getDescFondo()+"\",");
		jsonObj.append("\"controvaloreFondo\":\""+el.getControvaloreComparto()+"\",");
		jsonObj.append("\"value\":\""+el.getCodiceFondo()+"\",");
		jsonObj.append("\"label\":\""+el.autocompletionLabel()+"\"");
		return jsonObj.toString();
    }
    
    /********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return RicercaFondiModel.class;
    }
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return RicercaFondoAutocomplete.MSG_NESSUN_FONDO_DISPONIBILE;
	}

}
