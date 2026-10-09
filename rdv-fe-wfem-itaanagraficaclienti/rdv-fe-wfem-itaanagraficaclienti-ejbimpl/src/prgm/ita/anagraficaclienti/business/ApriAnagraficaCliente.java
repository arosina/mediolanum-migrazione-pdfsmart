package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.display.AnagraficaDatoreDiLavoro;
import prgm.ita.anagraficaclienti.display.ErroriOnStartUp;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.facade.StatiPropostaAnagrafica;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/*******************************************************************/
/*******************************************************************/
public class ApriAnagraficaCliente extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		try {
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.leggiCliente(csc, chiave);
			
			if(model.getStato().equals(StatiPropostaAnagrafica.NON_TROVATA)) {
				setNextCommandClass(ErroriOnStartUp.class);
				return model;
			}
			
			model.setParentBrowserInstance(chiave.getParentBrowserInstance());
			model.setModality(Template.UPDATE_MODALITY);
			model.setPopupMode(chiave.getPopupMode());
			model.setShowBack(chiave.getShowBack());
			model.setCallingAppl(chiave.getCallingAppl());
			model.setRuntimeModality(chiave.getRuntimeModality());
			if(model.getIsBozza().booleanValue())
				model.setTipoCarta(new StringType());
			
			if(model.getIsAssegnatoAdAltroAgente().booleanValue())
				model.setModality(Template.READ_MODALITY);
			
			if(model.getCallingAppl().toString().equals(Costanti.CALLING_APPL_MHD)){
				if(!model.getIsBozza().booleanValue() ||
				    model.getIsEffettivo().booleanValue())
					model.setModality(Template.READ_MODALITY);
			}else{
				if( model.getIsPropostaInviataInSede().booleanValue() &&
				   !model.getStatoConfermato().equals(Costanti.VALORE_FLAG_STATO_CONFERMATO))
					model.setModality(Template.READ_MODALITY);
			}
				
			if(model.getIsDatoreDiLavoro().booleanValue()){
				setNextCommandClass(AnagraficaDatoreDiLavoro.class);
				if(!model.getCodMediolanum().isNull())
					model.setModality(Template.READ_MODALITY);
			}else{
				if(model.getIsPersonaGiuridica().booleanValue())
					model.setModality(Template.READ_MODALITY);
				setNextCommandClass(AnagraficaCliente.class);
			}
			return model;
			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'aprire l'anagrafica  codPotenziale=["+chiave.getCodPotenziale()+"] codFiscale=["+chiave.getCodFiscale()+"] partitaIva=["+chiave.getPartitaIva()+"]: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
