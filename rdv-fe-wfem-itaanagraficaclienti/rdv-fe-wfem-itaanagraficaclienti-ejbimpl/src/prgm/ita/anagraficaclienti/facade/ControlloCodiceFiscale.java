package prgm.ita.anagraficaclienti.facade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlloCodiceFiscale implements Controlli{

    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, ClienteModel model){
	    
		if(model.getCodFiscale().isNull() || model.getCodFiscale().isSkippable())
			return;
		
		if(model.getComuneNascita().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA))
			controllaCodiceFiscale(csc,model);
		else
			controllaCodiceFiscaleEstero(csc,model);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaCodiceFiscaleEstero(ClientSessionContext csc, ClienteModel model){
		try{
			
			if(model.getCodFiscale().isNull())
				return;
			
			if(model.getCodFiscale().toString().length() != Costanti.LUNGHEZZA_CODICE_FISCALE_ESTERO){
				model.getCodFiscale().addTypeError("err.lunghezzaCodFisc");
				return;
			}
			return;
			
		}catch(Exception e){
			LOG.error(e);
			return;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaCodiceFiscale(ClientSessionContext csc, ClienteModel cliente){
		
		// #65636 - In variazione non si controlla più il codice fiscale START
		if(!cliente.getIsPotenziale().booleanValue()){
			return;
		}
		// #65636 - In variazione non si controlla più il codice fiscale END
		
		if(cliente.getCodFiscale().isNull())
			return;
		
		if(cliente.getComuneNascita().getComune().hasTypeErrors())
			return;
		
		if(cliente.getCodFiscale().toString().length() != Costanti.LUNGHEZZA_CODICE_FISCALE_ITALIANO){
			cliente.getCodFiscale().addTypeError("err.lunghezzaCodFisc");
			return;
		}

		if(cliente.getCognome().isNull())
			return;
		if(cliente.getNome().isNull())
			return;
		if(cliente.getDataNascita().isNull())
			return;
		if(cliente.getComuneNascita().getComune().isNull())
			return;
			
		String codFiscale = CalcoloCodiceFiscale.calcolaCodiceFiscale(csc,cliente);
	
		
		StringType nomeOriginale = null;
		StringType cognomeOriginale = null;
		DateType dataNascitaOriginale = null;
		StringType luogoNascitaOriginale = null;
		StringType codiceFiscaleOriginale = null;

		if(cliente.getDatiApplicativi().getClienteOriginale()!= null){
			
			if(cliente.getDatiApplicativi().getClienteOriginale().getNome() != null)			
				nomeOriginale=cliente.getDatiApplicativi().getClienteOriginale().getNome();
			if(cliente.getDatiApplicativi().getClienteOriginale().getCognome() != null)
				cognomeOriginale=cliente.getDatiApplicativi().getClienteOriginale().getCognome();
			if(cliente.getDatiApplicativi().getClienteOriginale().getDataNascita() != null)
				dataNascitaOriginale=cliente.getDatiApplicativi().getClienteOriginale().getDataNascita();
			if(cliente.getDatiApplicativi().getClienteOriginale().getComuneNascita().getComune() != null)
				luogoNascitaOriginale=cliente.getDatiApplicativi().getClienteOriginale().getComuneNascita().getComune();
			if(cliente.getDatiApplicativi().getClienteOriginale().getCodFiscale() != null)
				codiceFiscaleOriginale = cliente.getDatiApplicativi().getClienteOriginale().getCodFiscale();
		}		
			
			
		if(( nomeOriginale  == null || !nomeOriginale.equals(cliente.getNome()))
				|| (cognomeOriginale  == null || !cognomeOriginale.equals(cliente.getCognome())) 
				|| (dataNascitaOriginale  == null || !dataNascitaOriginale.equals(cliente.getDataNascita()))
				|| (luogoNascitaOriginale  == null || !luogoNascitaOriginale.equals(cliente.getComuneNascita().getComune()))
				|| (codiceFiscaleOriginale == null || !codiceFiscaleOriginale.equals(cliente.getCodFiscale()))){
			
			
			if(!cliente.getCodFiscale().equalsIgnoreCase(codFiscale)){
				if(codFiscale.equals(""))
					cliente.getCodFiscale().addTypeWarning("war.controlloCodFiscDatiInsuff");
				else
					cliente.getCodFiscale().addTypeWarning("war.codFiscNonConforme");
				
			}			

		}
		
	}

}