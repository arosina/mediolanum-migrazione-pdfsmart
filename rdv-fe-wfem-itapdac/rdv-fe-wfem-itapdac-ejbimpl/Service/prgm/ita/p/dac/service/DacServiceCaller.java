package prgm.ita.p.dac.service;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.DacModel;

import com.atosorigin.wfem.backend.AbstractBackendObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacServiceCaller extends AbstractBackendObject{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType inserisciDocumento(ClientSessionContext csc, 
												String codAgente, 
												String nomeRisorsa, 
			 									Contratto contratto, 
			 									Cliente cliente, 
			 									MezzoPagamento[] mezziDiPagamento) throws Exception {

		return inserisciDocumentoInPlico(csc,codAgente,null,null,nomeRisorsa,contratto,cliente,mezziDiPagamento);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType inserisciDocumentoInPlico(ClientSessionContext csc, 
													   String codAgente, 
													   String contestoPlico, String aggregatorePlico,
													   String nomeRisorsa, 
													   Contratto contratto, 
													   Cliente cliente, 
													   MezzoPagamento[] mezziDiPagamento) throws Exception {

		// Controllo correttezza codice aggregatore plichi
		if(aggregatorePlico != null && aggregatorePlico.length() > 0){
			if(aggregatorePlico.length() > 20)
				throw new Exception("Nei plichi il codice aggregatore non può essere più lungo di 20 caratteri");
			if(contestoPlico == null || contestoPlico.length() == 0)
				throw new Exception("Per creare plichi è obbligatorio specificare il contesto di aggregazione");
			if(contestoPlico.length() > 9)
				throw new Exception("Nei plichi il contesto di aggregazione non può essere più lungo di 9 caratteri");
			if(contestoPlico.equalsIgnoreCase(Costanti.CONTESTO_PLICO_DAC) || contestoPlico.equalsIgnoreCase(Costanti.CONTESTO_PLICO_ASS))
				throw new Exception("Il contesto '"+contestoPlico+"' è riservato alla applicazione DAC");
		}
		
		if(contratto.getCodProdotto() == 0)
			throw new Exception("Codice prodotto non valido");
		if(contratto.getCodOperazione() == 0)
			throw new Exception("Codice operazione non valido");
		
		contratto.setNomeRisorsa(nomeRisorsa);
		contratto.setContestoPlico(contestoPlico);
		contratto.setAggregatorePlico(aggregatorePlico);
		
		DacServiceCaller call = new DacServiceCaller();
		return call.innerCall(csc, codAgente, contratto, cliente, mezziDiPagamento);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private StringType innerCall(ClientSessionContext csc, String codAgente, 
								 Contratto contratto, Cliente cliente, 
								 MezzoPagamento[] mezziDiPagamento) throws Exception {
		DacService manager = (DacService)ROF.getManager(csc,DacService.class);
		DacModel dac = manager.preparaInserimento(csc,codAgente,contratto,cliente,mezziDiPagamento);
		return manager.inserisciDocumento(csc,dac);
	}
}
