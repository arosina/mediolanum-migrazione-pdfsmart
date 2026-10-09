package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.display.SceltaTitolareDitta;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiException;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DocumentoModel;

/*******************************************************************/
/*******************************************************************/
public class SelezionaTitolare extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClienteModel cliente = (ClienteModel)dataModel;
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			cliente.getDatiApplicativi().setSceltaTipologiaCensimentoDittaEffettuata(true);
			cliente.getCodFiscaleTitolare().resetTypeErrors();
			
			ClienteKeyModel refKey = new ClienteKeyModel();
			refKey.setCodAgente(cliente.getCodAgente());
			refKey.setCodPotenziale(cliente.getCodPotenzialeTitolare());
			refKey.setCodMediolanum(cliente.getCodMediolanumTitolare());
			refKey.setCodFiscale(cliente.getCodFiscaleTitolare());
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel titolare = facade.leggiClienteTitolare(csc,refKey);
			cliente.setClienteTitolare(titolare);
			
			DocumentoModel documento = titolare.getDocumento();
			DateType oggi = Tools.today();
		   	if(!documento.getDataScadenza().isNull() &&
			   	documento.getDataScadenza().compareTo(oggi) < 0){
			   	cliente.getCodFiscaleTitolare().addTypeError("err.documentoTitolareScaduto");
				setNextCommandClass(SceltaTitolareDitta.class);
				return cliente;
		   	}
			
		   	cliente.initDatiDittaFromTitolare(titolare);
			
		   	eseguiControlliPreventivi(csc, facade, cliente);
		   	
			cliente.setModality(Template.UPDATE_MODALITY);
			setNextCommandClass(AnagraficaCliente.class);
			return cliente;
			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel selezionare il titolare: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

	/********************************************************************************/
	/********************************************************************************/
	private void eseguiControlliPreventivi(ClientSessionContext csc, AnagraficaClientiFacade facade, ClienteModel cliente) throws AnagraficaClientiException{
		try {
			ClienteModel clone = (ClienteModel)Tools.cloneObject(cliente);
			clone = facade.controllaCliente(csc,clone);
			
			if(Tools.containsTypeErrors(clone.getComuneNascita())){
				cliente.getComuneNascita().getCodNazione().setTypeErrors(clone.getComuneNascita().getCodNazione().getTypeErrors());
				cliente.getComuneNascita().getComune().setTypeErrors(clone.getComuneNascita().getComune().getTypeErrors());
				cliente.getComuneNascita().getComuneEstero().setTypeErrors(clone.getComuneNascita().getComuneEstero().getTypeErrors());
				cliente.getComuneNascita().getProvincia().setTypeErrors(clone.getComuneNascita().getProvincia().getTypeErrors());
				cliente.getDynamicData().addProperty("errorsOnComuneDiNascitaClienteTitolare", new BooleanType(true));
			}
			
		}catch(Exception e){
			throw new AnagraficaClientiException(e.toString());
		}
	}	
}
