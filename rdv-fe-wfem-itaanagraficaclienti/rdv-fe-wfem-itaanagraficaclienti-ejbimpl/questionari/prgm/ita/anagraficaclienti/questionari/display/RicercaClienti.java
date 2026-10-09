package prgm.ita.anagraficaclienti.questionari.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiParamsModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaClienti extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			PopupClientiParamsModel params = popupClientiModel.getParams();
					
			if(popupClientiModel.getIsPrimaVolta().booleanValue()) {
				CogestioneDataModel cogestioneData = popupClientiModel.getClienteSelezionato().getCogestioneData();
				CogestioneDataManager.initContrattoCogestione(csc, cogestioneData);
				if(cogestioneData.getIsUtenteCogestore().booleanValue()) {
					params.setTipoInclusioneCogestiti(new StringType("T"));
					params.setRuoloCogestione(new StringType("BC"));
					params.setTipoOrdinamentoCogestiti(new StringType("C"));
				}else if(cogestioneData.getIsUtenteTitolare().booleanValue()) {
					params.setTipoInclusioneCogestiti(new StringType("T"));
					params.setRuoloCogestione(new StringType("BC"));
					params.setTipoOrdinamentoCogestiti(new StringType("P"));
				}
				params.setTipoRicerca(new StringType("primaricensiti"));
				loadNuovoPcpUrl(csc, popupClientiModel);
			}
			
			PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);		
			popupClientiModel = popupFacade.getElencoClientiAgente(csc,popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'elenco clienti: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco clienti: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupClientiModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadNuovoPcpUrl(ClientSessionContext csc, PopupClientiModel popupClientiModel){
    	BooleanType isInPilota = new BooleanType(true);
    	try{
    		isInPilota = (BooleanType)DAOObject.executeDynaQueryAccess(csc, "DBAZ_SOGG", "select case when count(*) > 0 then 'S' else 'N' end "+
																"from cll.dett_profl_pers_n5d "+
																"where COD_PERS in ('"+Tools.fillSx(csc.getUserCode(),'0',10)+"','*') and "+
																"COD_PROFL_APPL='NUOVO_PCP' and "+
																"COD_TIPO_PERS='1' and "+
																"GSTD_F_ESIST='S' and "+
																"sysdate &gt;= DAT_INIZ_VALID and "+
																"sysdate &lt;= DAT_FINE_VALID", null, BooleanType.class).getSingleResult();
    	}catch(DAOException daoe){
    		// do nothing
   		}
    	if(!isInPilota.booleanValue()) {
    		popupClientiModel.setUrlNuovoPcp(new StringType());
    		return;
    	}
    		
    	
    	StringType urlNuovoPcp = new StringType();
    	try{
    		urlNuovoPcp = (StringType)DAOObject.executeDynaQueryAccess(csc, "PRGM", "select COMMAND_URL from OPEN_COMMANDS where COMMAND_NAME = 'NUOVO_PCP'", null, StringType.class).getSingleResult();
    	}catch(DAOException daoe){
    		// do nothing
   		}
    	
		if(urlNuovoPcp == null) 
			urlNuovoPcp = new StringType();
		
    	if(urlNuovoPcp.equals("WFEM_APPLICATION")) {
    		urlNuovoPcp = new StringType();
    	}else if(urlNuovoPcp.isNull()) {
			urlNuovoPcp = new StringType("lr/applicazioni-cliente/questionario-pcp?params=");
    	}
    	
		popupClientiModel.setUrlNuovoPcp(urlNuovoPcp);
	}

}
