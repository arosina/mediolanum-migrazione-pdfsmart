package prgm.ita.p.dac.fb.business;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.print.StampaDac;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class StampaUltimaDac extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ParamsModel params = (ParamsModel)dataModel;
			
			params.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
			if(params.getTipoDac().isNull())
				params.setTipoDac(new IntegerType(Costanti.TIPO_DAC_STANDARD));
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc,DacFacade.class); 
			StringType idDac = facade.leggiIdUltimaDac(csc,params);
			if(!idDac.isNull()){
				DacKeyModel dacKey = new DacKeyModel();
				dacKey.copyParams(params);
				dacKey.setIdDac(idDac);
				setNextCommandClass(StampaDac.class);
				return dacKey;
			}
			String out = "<html>"+
								"<table width='100%'>"+
								  "<tr>"+
								    "<td style='font-family:Arial;font-size:12pt;color:#1A458F;background-color:#d0e4ef;'>"+
								       "<b>Stampa ultimo Prit</b>"+
									"</td>"+
								  "</tr>"+
								  "<tr><td height='10'></td></tr>"+
								  "<tr>"+
								    "<td style='padding-left:50;'>"+
								       "<span style='font-family:Arial;font-size:10pt;color:#1A458F;background-color:#F0F0F0;'>"+
								           "<b>Non esistono Prit inviati in sede da lavorare</b>"+
								       "</span>"+
									"</td>"+
								  "</tr>"+
								"</table>"+
						 "</html>";
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("text/html");
			resp.setContentLength(out.length());
			resp.setContent(out.getBytes());
			setGenericCommandResponse(resp);
			return null;				
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ParamsModel.class;
	}
	
}
