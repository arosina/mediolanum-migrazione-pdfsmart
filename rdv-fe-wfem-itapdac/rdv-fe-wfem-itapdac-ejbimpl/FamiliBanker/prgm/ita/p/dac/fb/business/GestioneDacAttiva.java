package prgm.ita.p.dac.fb.business;

import prgm.ita.p.dac.display.Dac;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public class GestioneDacAttiva extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			ParamsModel params = (ParamsModel)dataModel;
			if(params.isAutoSpuntataParams())
				throw new CommandException("Il Prit promotore non può nascere già spuntato. Verificare i parametri del menù");
			
			// Gestione dell'altezza video. Non attiva ni quanto tutta la rete vede il docuemnto con in tabbettini
			// indipendentemente dal tipo di pc
			/*
			if(params.getVerticalVideoHeight().isNull()){
				GenericCommandResponseModel resp = DacTools.manageVerticalViedoHeight(this,params);
				setGenericCommandResponse(resp);
				return null;
			}
			*/
			
			params.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
			if(params.getTipoDac().isNull())
				params.setTipoDac(new IntegerType(Costanti.TIPO_DAC_STANDARD));
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			DacModel dac = facade.caricaDacAttiva(csc,params);
			
			if(dac.getDocumenti().size() > 0 && dac.isFaseDiSpunta()){
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(0);
				doc.copyParams(params);
				dac.setDocumento(facade.leggiDocumento(csc,(DocumentoKeyModel)doc));
				dac.getDocumento().copyParams(dac);
				dac.getDocumento().setVisible(true);
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
		return ParamsModel.class;
	}
}
