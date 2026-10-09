package prgm.ita.anagraficaclienti.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AnagraficaCliente extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteModel cliente = (ClienteModel)dataModel;
			cliente.getInfoPersonali().initCombinazioneProvenienzaPatrimonioChecks();
			aggiornaInfoResidenzeFiscali(csc, cliente);
			aggiornaTipoCampoLuogoDocumento(csc, cliente);
			return dataModel;
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void aggiornaInfoResidenzeFiscali(ClientSessionContext csc, ClienteModel cliente) throws DAOException{
		DAOObject dao = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti");				
		
		if(cliente.getCittadinanza().equals(Costanti.COD_NAZIONE_US_UIC) || cliente.getSecondaCittadinanza().equals(Costanti.COD_NAZIONE_US_UIC))
			cliente.getResidenza().setCodNazioneResidenzaFiscale2(new StringType(Costanti.COD_NAZIONE_US_UIC));
		
		if (cliente.getResidenza().getCodNazioneResidenzaFiscale2().isNull()) {
			cliente.getResidenza().setCodFiscaleResidenzaFiscale2(new StringType(""));
			cliente.getResidenza().setCodFiscaleResidenza2Required(new BooleanType(false));
			cliente.getResidenza().setCodFiscaleResidenza2Released(new BooleanType(false));
		}else{	
			dao.executeQueryAccess("loadInfoCodiceFiscale2",cliente);			
			if(!cliente.getResidenza().getCodFiscaleResidenza2Released().booleanValue())
				cliente.getResidenza().setCodFiscaleResidenzaFiscale2(new StringType(""));
		}

		if (cliente.getResidenza().getCodNazioneResidenzaFiscale3().isNull()) {
			cliente.getResidenza().setCodFiscaleResidenzaFiscale3(new StringType(""));
			cliente.getResidenza().setCodFiscaleResidenza3Required(new BooleanType(false));
			cliente.getResidenza().setCodFiscaleResidenza3Released(new BooleanType(false));
		} else {
			dao.executeQueryAccess("loadInfoCodiceFiscale3",cliente);
			if(!cliente.getResidenza().getCodFiscaleResidenza3Released().booleanValue())
				cliente.getResidenza().setCodFiscaleResidenzaFiscale3(new StringType(""));
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void aggiornaTipoCampoLuogoDocumento(ClientSessionContext csc, ClienteModel cliente) {
		cliente.getDocumento().getLuogoRilascio().getCodDescFields().remove("comune");
		if(cliente.getDocumento().getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_CARTA_IDENTITA_ESTERA) ||
		    cliente.getDocumento().getTipoDocumento().equals(Costanti.TIPO_DOCUMENTO_PASSAPORTO_ESTERO)) {
			cliente.getDocumento().getLuogoRilascio().addCodDescField("comune", "NAZIONI_DOCUMENTO_ESTERO");
			new DAOObject(csc, "ItaAnagraficaClienti.AnagraficaClienti").fillCodDesc(cliente.getDocumento().getLuogoRilascio(), false);
		}
	}
}
