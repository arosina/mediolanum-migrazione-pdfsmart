package prgm.ita.p.dac.business;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class RemoveMezzoPagamento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			DacModel dac = (DacModel)dataModel;
			int absIndex = dac.getDocumento().getAbsIndexMezzoPgSelezionato().intValue();
			MezzoPagamentoModel mp = (MezzoPagamentoModel)dac.getDocumento().getMezziPagamento().get(absIndex);
			dac.getDocumento().getMezziPagamento().removeRecalc(absIndex);
			dac.getDocumento().getMezziPagamentoDaCancellare().add(mp);
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
