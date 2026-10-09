package prgm.ita.p.dac.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.util.DacTools;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractRicercaDac extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			AbstractRicercaDacModel ricercaModel = (AbstractRicercaDacModel)dataModel;
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc,DacFacade.class);
			if(ricercaModel.isPrimaAttivazione()){
				DacTools.loadUfficioUtente(csc, ricercaModel);
				ricercaModel = (AbstractRicercaDacModel)facade.fillCodDesc(csc,ricercaModel,true);
				return ricercaModel;
			}
			if(ricercaModel.getDoSearch().booleanValue())
				return search(userSessionContext,ricercaModel);
			return ricercaModel;
			
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.display.AbstractRicercaDac: Eccezione nella ricerca DAC: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static AbstractRicercaDacModel search(UserSessionContext userSessionContext, AbstractRicercaDacModel ricercaModel) throws CommandException {
		
		try{
			
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			if(ricercaModel.getTipoRicerca().equals("MOM")){
				ricercaModel.getElencoDac().clear();
				ricercaModel.getElencoDac().setMaxRowsExceeded(false);
				if(csc.isAssistenteFB())
					ricercaModel.setUserMOM(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				else
					ricercaModel.setUserMOM(new StringType(Tools.fillSx(csc.getUserCode(),'0',10)));
				new DAOObject(csc,Costanti.DAO_XML_NAME_MOM).executeOSBAccess("ricercaPritMOM",ricercaModel);
				if(ricercaModel.getElencoDac().size() >= 51){
					ricercaModel.getElencoDac().getElements().remove(50);
					ricercaModel.getElencoDac().setMaxRowsExceeded(true);
				}
			}else{
				DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc,DacFacade.class);
				ricercaModel = facade.ricercaDac(csc,ricercaModel);
				ricercaModel.setDoSearch(new BooleanType(false));
			}
			return ricercaModel;
			
		}catch(DAOException daoe){
			String errorMsg = "prgm.ita.p.dac.display.AbstractRicercaDac: Eccezione DAO nella ricerca DAC: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.display.AbstractRicercaDac: Eccezione nella ricerca DAC: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

}
