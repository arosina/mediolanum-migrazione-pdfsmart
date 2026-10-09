package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class AggiornaInfoCodiceFiscale3 extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{

			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteModel cliente = (ClienteModel)dataModel;
			
			if (cliente.getResidenza().getCodNazioneResidenzaFiscale3().isNull()) {
				cliente.getResidenza().setCodFiscaleResidenzaFiscale3(new StringType(""));
				cliente.getResidenza().setCodFiscaleResidenza3Required(new BooleanType(false));
				cliente.getResidenza().setCodFiscaleResidenza3Released(new BooleanType(false));
			} else {
				DAOObject dao = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti");				
				dao.executeQueryAccess("loadInfoCodiceFiscale3",cliente);

				if ( ! cliente.getResidenza().getCodFiscaleResidenza3Released().booleanValue()) {
					cliente.getResidenza().setCodFiscaleResidenzaFiscale3(new StringType(""));
				}				
			}
			
			setForwardDisplay(new Integer(0), false);
			return cliente;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel recupero informazioni codici fiscali: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel recupero informazioni codici fiscali: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}
}
