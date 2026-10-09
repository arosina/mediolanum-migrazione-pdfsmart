package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;

/*******************************************************************/
/*******************************************************************/
public class CodiceAgevolazioneAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		CodiceAgevolazioneAutocompleteModel model = (CodiceAgevolazioneAutocompleteModel)dataModel;
		setModVersForIn(model);
		ListType res = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("codiceAgevolazioneAutocomplete",model).getResult();
		if(!model.getCodDescrAgevolazioneDipendenti().isNull()){
			String cod=model.getCodDescrAgevolazioneDipendenti().toString();
			String descr=ProvideAgevolazioneDipendentiDataResponse.DEFAUT_DESCRIZIONE_AGEVOLAZIONE_DIPENDENTI;
			if(cod.indexOf("|") > 0){
				String[] coddescarray = cod.split("\\|");
				cod = coddescarray[0];
				descr = coddescarray[1];
			}
			CodiceAgevolazioneAutocompleteModel el = new CodiceAgevolazioneAutocompleteModel();
			el.setCodiceAgevolazione(new StringType(cod));
			el.setDescrTipologiaAgevolazione(new StringType(descr));
			el.setPercentualeAgevolazione(new StringType(AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI));
			res.add(el);
		}
		return res;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private void setModVersForIn(CodiceAgevolazioneAutocompleteModel model) {
		if(model.getModalitaVersamentoAgevolazione().isNull())
			return;
		StringBuilder modVers = new StringBuilder();
		String[] modVersArray = model.getModalitaVersamentoAgevolazione().toString().split(",");
		for(int i=0;i<modVersArray.length;i++)
			modVers.append("'"+modVersArray[i]+"',");
		if(modVers.length() > 0)
			modVers.deleteCharAt(modVers.length()-1);
		model.setModalitaVersamentoAgevolazione(new StringType(modVers.toString()));
	}

	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "<span style='white-space:nowrap;'>Nessuna agevolazione disponibile</span>";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
   protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		CodiceAgevolazioneAutocompleteModel el = (CodiceAgevolazioneAutocompleteModel)dataModel;
		StringBuffer jsonObj = new StringBuffer();
		jsonObj.append("\"idAgevolazione\":\""+el.getIdAgevolazione()+"\",");
		jsonObj.append("\"descrizioneAgevolazione\":\""+el.getDescrizioneAgevolazione()+"\",");		
		jsonObj.append("\"tipologiaAgevolazione\":\""+el.getTipologiaAgevolazione()+"\",");
		jsonObj.append("\"codiceAgevolazione\":\""+el.getCodiceAgevolazione()+"\",");
		jsonObj.append("\"modalitaVersamentoAgevolazione\":\""+el.getModalitaVersamentoAgevolazione()+"\",");
		jsonObj.append("\"percentualeAgevolazione\":\""+el.getPercentualeAgevolazione()+"\",");
		jsonObj.append("\"importoAgevolazione\":\""+el.getImportoAgevolazione()+"\",");	
		jsonObj.append("\"label\":\""+el.autocompletionLabel()+"\"");
		return jsonObj.toString();
    }

    /********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return CodiceAgevolazioneAutocompleteModel.class;
    }

}