package prgm.ita.p.dac.business;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class SalvaDocumento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			DocumentoModel doc = dac.getDocumento();
			
			boolean nuovoOnInserisci = doc.getNuovoOnInserisci().booleanValue();
			boolean inClonazione = dac.isInClonazione();

			// Potrei arrivare qui per salvare un nuovo documento modificato e non ancora salvato
			if(doc.getIdDocumento().equals("NEW"))
				doc.setIdDocumento(new StringType());
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = facade.salvaDocumento(csc,dac);

			DacTools.prossimoDocDaGestire(csc,this,dac,nuovoOnInserisci,inClonazione);
			return dac;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacModel.class;
	}

}
