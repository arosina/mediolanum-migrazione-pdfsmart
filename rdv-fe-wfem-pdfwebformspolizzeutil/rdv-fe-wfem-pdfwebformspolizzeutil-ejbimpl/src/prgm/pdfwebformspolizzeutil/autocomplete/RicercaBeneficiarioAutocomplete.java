package prgm.pdfwebformspolizzeutil.autocomplete;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteCommand;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.dao.BeneficiariDaoAccess;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;
import prgm.pdfwebformspolizzeutil.popup.RicercaBeneficiariModel;

/*******************************************************************/
/*******************************************************************/
public class RicercaBeneficiarioAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {

		RicercaBeneficiariModel model = (RicercaBeneficiariModel)dataModel;
				
		DAOObject dao = new DAOObject(csc, BeneficiariDaoAccess.DAO_FILE_NAME);
		dao.setQueryMaxResultRows(10);
		BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc, dao);
		BeneficiarioModel beneficiarioModel = new BeneficiarioModel();
		
		beneficiarioModel.setCodiceAgente(model.getCodiceAgente());
		
		if (model.getAutocompleteFieldName().equals(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF)) {
			beneficiarioModel.setCodiceCliente(model.getCodiceCliente());
		}
		
		if (model.getAutocompleteFieldName().equals(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF)) {
			beneficiarioModel.setCodiceFiscale(model.getCodiceFiscale());
		}


		if (model.getTipoRicerca().equals("PG")){
			beneficiarioModel.setTipoRicerca(new StringType("PG"));
			if (model.getAutocompleteFieldName().equals(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF)) {
				beneficiarioModel.setRagioneSociale(model.getRagioneSociale());
			}
		}else {
			//beneficiri fisici e titolari
			beneficiarioModel.setTipoRicerca(new StringType("PF"));
			if (model.getAutocompleteFieldName().equals(Constants.NOME_BASE_FIELD_NAME_PDF)) {
				beneficiarioModel.setNome(model.getNome());
			}
			if (model.getAutocompleteFieldName().equals(Constants.COGNOME_BASE_FIELD_NAME_PDF)) {
				beneficiarioModel.setCognome(model.getCognome());
			}
		}
		
		

		
		return daoAccess.findList(beneficiarioModel);
	}	
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
	protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		BeneficiarioModel el = (BeneficiarioModel)dataModel;
		StringBuilder jsonObj = new StringBuilder();
		jsonObj.append("\"value\":\""+el.getCodiceCliente()+"\",");
		jsonObj.append("\"label\":\""+el.getCodiceCliente()+" - "+el.getCognome()+" "+el.getNome()+" - CF/PIVA : "+el.getCodiceFiscale()+"\"");
		return jsonObj.toString();
    }
    
    /********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return RicercaBeneficiariModel.class;
    }

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun beneficiario disponibile";
	}

}