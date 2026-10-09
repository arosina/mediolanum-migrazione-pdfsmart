package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.display.AnagraficaDatoreDiLavoro;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/*******************************************************************/
/*******************************************************************/
public class VisualizzaAnagraficaCliente extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		try {
			
			if(chiave.getCodAgente().isNull())
				chiave.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.leggiCliente(csc, chiave);
			model.setModality(Template.READ_MODALITY);
			
			model.setPopupMode(chiave.getPopupMode());
			model.setShowBack(chiave.getShowBack());
			model.setCallingAppl(chiave.getCallingAppl());
			model.setRuntimeModality(chiave.getRuntimeModality());
			
			if(model.getIsDatoreDiLavoro().booleanValue())
				setNextCommandClass(AnagraficaDatoreDiLavoro.class);
			else
				setNextCommandClass(AnagraficaCliente.class);
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
