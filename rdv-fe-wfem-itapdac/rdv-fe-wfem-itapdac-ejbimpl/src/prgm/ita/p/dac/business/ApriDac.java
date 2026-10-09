package prgm.ita.p.dac.business;

import prgm.ita.p.dac.display.Dac;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApriDac extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacKeyModel dacKey = (DacKeyModel)dataModel;

			DacModel dac = null;
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			
			// In spunta (letto dai parametri) metto la dac in lavorazione
			if(dacKey.isFaseDiSpunta()){
				
				// Leggo la testata e i doc della Dac (senza arricchimenti)
				dac = facade.leggiDacLight(csc,dacKey);
				
				StringType codUtenteLavorazione = dac.getCodUtenteLavorazione();
				String uffLavorazione = dac.getDescValue("uffLavorazione");
				
				// Se è da lavorare, lavoro la Dac. In ogni caso la leggo
				dac = facade.lavoraDac(csc, dac);
				
				if(dac.hasCommandErrors()){
					dac.setMsg("Attenzione!\\nQuesto Prit è stato messo in lavorazione contemporaneamente ad un'altro utente.\\n\\n"+
							   "Codice altro operatore: "+dac.getCodUtenteLavorazione()+" Ufficio: "+dac.getDescValue("uffLavorazione"));
					dac.setShowAlert(true);
					dac.setModality(Template.READ_MODALITY);
					setNextCommandClass(Dac.class);
					return dac;
				}
				
				if(!dac.getStato().equals(Costanti.STATO_LAVORATA) &&
				   (!codUtenteLavorazione.isNull() && !codUtenteLavorazione.equals(Tools.fillSx(csc.getUserCode(),'0',10))) ){
					dac.setMsg("Attenzione!\\nQuesto Prit è stato messo in lavorazione da un altro operatore.\\n\\n"+
							   "Codice altro operatore: "+codUtenteLavorazione+" Ufficio: "+uffLavorazione);
					dac.setShowAlert(true);
				}
					
				// Apro il primo documento da lavorare, se non sono in modalità read
				dac.initReadonly(csc);
				if(dac.getModality() != Template.READ_MODALITY)
					DacTools.prossimoDocDaGestire(csc,this,dac,false,false);
				
			}else{
				
				// Leggo la Dac
				dac = facade.leggiDac(csc,dacKey);
				
			}
			
			setNextCommandClass(Dac.class);
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacKeyModel.class;
	}

}
