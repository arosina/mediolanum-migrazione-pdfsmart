package prgm.ita.anagraficaclienti.questionari.business;

import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.questionari.display.Patrimonio;
import prgm.ita.anagraficaclienti.questionari.facade.QuestionariFacade;
import prgm.ita.anagraficaclienti.questionari.model.PatrimonioModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;

/*******************************************************************/
/*******************************************************************/
public class NuovoPatrimonio extends BusinessCommand implements MenuCommand{
	
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			if(1==1){ // Con la MIFID3 questa funzione no è più disponibile
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				String html = "<html><table width='100%'><tr><td align='center' style='font-family:Arial;font-size: 8pt;color:#1A458F;'><b>Funzione non più disponibile</b></td></tr></table></html>";
				resp.setContent(html.getBytes());
				resp.setContentType("text/html");				
				setGenericCommandResponse(resp);
				return dataModel;
			}
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteKeyModel clienteKey = (ClienteKeyModel)dataModel;
			BooleanType showBack = clienteKey.getShowBack();
			
			QuestionariFacade facade = (QuestionariFacade)ROF.getFacade(csc,QuestionariFacade.class);
			PatrimonioModel patrimonio = facade.getPatrimonio(csc,clienteKey);
			patrimonio.setModality(Template.READ_MODALITY);
			patrimonio.setShowBack(showBack);
			setNextCommandClass(Patrimonio.class);
			return patrimonio;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}		
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
