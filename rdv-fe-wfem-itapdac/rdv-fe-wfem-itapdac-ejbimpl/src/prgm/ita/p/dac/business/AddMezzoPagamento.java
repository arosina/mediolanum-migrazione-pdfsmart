package prgm.ita.p.dac.business;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public class AddMezzoPagamento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			MezzoPagamentoModel mezzoPg = new MezzoPagamentoModel();
			if(dac.isFaseDiSpunta())
				mezzoPg.setEsitoDocumentoAssegno(new IntegerType(Costanti.ESITO_DOC_AGGIUNTO));
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			facade.fillCodDesc(csc,mezzoPg,false);
			dac.getDocumento().getMezziPagamento().getElements().add(0,mezzoPg);
			setForwardDisplay(new Integer(0));
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
