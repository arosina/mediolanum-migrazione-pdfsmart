package prgm.ita.p.dac.estrazioni.display;

import prgm.ita.p.dac.estrazioni.facade.EstrazioniDocumentiRicevutiFacade;
import prgm.ita.p.dac.estrazioni.model.DocumentiRicevutiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;
/**
 * L'applicazione sostituisce il :
 * 
 * Report conteggio documenti ricevuti nelle DAC
 * 
 * @author pmirabelli
 *
 */
public class EstrazioniDocumentiRicevutiDAC extends DisplayCommand{

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		DocumentiRicevutiModel model=(DocumentiRicevutiModel)dataModel;
		
		try {
			EstrazioniDocumentiRicevutiFacade facade=(EstrazioniDocumentiRicevutiFacade)FacadeLoader.getFacade(csc, EstrazioniDocumentiRicevutiFacade.class);
			model=facade.fillCodDesc(csc, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

	public Class getInputViewClass() {
		return DocumentiRicevutiModel.class;
	}

}
