package prgm.ita.p.dac.display;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;

/***********************************************************************************************/
/***********************************************************************************************/
public class QuickViewDoc extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DocumentoKeyModel docKey = (DocumentoKeyModel)dataModel;

			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			DocumentoModel doc = facade.leggiDocumento(csc, docKey);
			
			facade.fillCodDesc(csc,doc,true);
			doc.setModality(Template.READ_MODALITY);
			return doc;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DocumentoKeyModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

}
