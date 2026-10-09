package prgm.ita.p.dac.business;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.util.DacTools;
import prgm.ita.p.dac.util.DocumentoAssegnoTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApriDocumento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			DacModel dac = (DacModel)dataModel;
			
			if(dac.getIsPritMOM().booleanValue()){
				DocumentoKeyModel docKey = (DocumentoKeyModel)dac.getDocumento();
				for(int i=0;i<dac.getDocumenti().size();i++){
					DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
					if(doc.getIdDocumento().equals(docKey.getIdDocumento())){
						doc = (DocumentoModel)Tools.cloneObject(doc);
						DocumentoAssegnoTools.impostaDatiDocumentoPadreDocAssegno(dac,doc,true);
						doc.copyParams(docKey);
						doc.setVisible(true);
						dac.setDocumento(doc);
						setForwardDisplay(new Integer(0));
						return dac;
					}
				}
				throw new CommandException("Documento ["+docKey.getIdDocumento()+"] non trovato");
			}
			
			// Salvo il documento corrente se l'utente l'ha modificato senza salvarlo
			dac = DacTools.salvaDocumentoModificato(userSessionContext, dac);
			if(dac.getDocumento().hasCommandErrors()){
				setForwardDisplay(new Integer(0));
				return dac;					
			}
			
			// Imposto nel documento i parametri da menù e/o da ricerca
			dac.getDocumento().copyParams(dac);
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			DocumentoModel doc = facade.leggiDocumento(csc,(DocumentoKeyModel)dac.getDocumento());
			
			if(!doc.getIdDocumento().isNull())
				doc.setVisible(true);
			
			dac.setDocumento(doc);
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
