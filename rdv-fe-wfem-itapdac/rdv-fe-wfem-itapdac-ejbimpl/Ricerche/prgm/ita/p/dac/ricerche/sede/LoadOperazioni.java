package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.RicercaDacParamsModel;
import prgm.ita.p.dac.model.RicercaDocParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;

/***********************************************************************************************/
/***********************************************************************************************/
public class LoadOperazioni extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			if(dataModel instanceof AbstractRicercaDacModel){
				AbstractRicercaDacModel ricercaModel = (AbstractRicercaDacModel)dataModel;
				if(ricercaModel.getParametri().getCodProdotto().isNull())
					ricercaModel.getParametri().addCodDescField("codOperazione","Operazioni");
				else
					ricercaModel.getParametri().addCodDescField("codOperazione","OperazioniProdotto");
				ricercaModel.setParametri((RicercaDacParamsModel)facade.fillCodDesc(csc,ricercaModel.getParametri(),false));
				ricercaModel.setDoSearch(new BooleanType(false));
			}else if(dataModel instanceof AbstractRicercaDocModel){
				AbstractRicercaDocModel ricercaModel = (AbstractRicercaDocModel)dataModel;
				if(ricercaModel.getParametri().getCodProdotto().isNull())
					ricercaModel.getParametri().addCodDescField("codOperazione","Operazioni");
				else
					ricercaModel.getParametri().addCodDescField("codOperazione","OperazioniProdotto");
				ricercaModel.setParametri((RicercaDocParamsModel)facade.fillCodDesc(csc,ricercaModel.getParametri(),false));
				ricercaModel.setDoSearch(new BooleanType(false));				
			}
			setForwardDisplay(new Integer(0));
			return dataModel;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return CommandDataModel.class;
	}

}
