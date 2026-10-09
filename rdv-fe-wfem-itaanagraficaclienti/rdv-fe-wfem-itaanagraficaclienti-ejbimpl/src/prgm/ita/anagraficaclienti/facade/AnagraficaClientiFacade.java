package prgm.ita.anagraficaclienti.facade;

import javax.ejb.Remote;

import prgm.ita.anagraficaclienti.agenticlienti.RicercaGlobaleClientiModel;
import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;
import prgm.ita.anagraficaclienti.model.ParametriApplicazioneAnagrafica;
import prgm.ita.anagraficaclienti.model.TelefonoModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface AnagraficaClientiFacade extends Facade {
	public AgenteModel        			leggiAgente(ClientSessionContext csc, StringType codAgente);
	public ClienteModel       			nuovoCliente(ClientSessionContext csc, ParametriApplicazioneAnagrafica params);
	public ClienteModel       			nuovaDitta(ClientSessionContext csc, ParametriApplicazioneAnagrafica params);
	public ClienteModel       			leggiCliente(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public ClienteModel       			leggiClienteLight(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public ClienteModel 				leggiClienteTitolare(ClientSessionContext csc, ClienteKeyModel dittaKey);
	public ClienteKeyModel    			cancellaCliente(ClientSessionContext csc, ClienteKeyModel model);
	public IndirizzoModel     			nuovoIndirizzo(ClientSessionContext csc);
	public TelefonoModel      			nuovoTelefono(ClientSessionContext csc);
	public ClienteModel       			salvaBozzaCliente(ClientSessionContext csc, ClienteModel model);
	public ClienteModel       			confermaPropostaCliente(ClientSessionContext csc, ClienteModel model);
	public ClienteModel       			inviaInSedeCliente(ClientSessionContext csc, ClienteModel model);
	public ClienteModel 				controllaClienteConChiave(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public ClienteModel 				controllaCliente(ClientSessionContext csc, ClienteModel model);
	public ClienteModel       			leggiVariazione(ClientSessionContext csc, VariazioneKeyModel variazioneKey);
	public ClienteModel 	  			cancellaVariazione(ClientSessionContext csc, ClienteModel model);
	public ClienteModel       			calcolaCodiceFiscale(ClientSessionContext csc, ClienteModel model);

	public ClienteModel       			leggiFotografieCliente(ClientSessionContext csc, ClienteModel cliente);
	public ClienteModel       			salvaFotografiaCliente(ClientSessionContext csc, ClienteModel cliente);
	
	public RicercaGlobaleClientiModel 	ricercaGlobaleClienti(ClientSessionContext csc, RicercaGlobaleClientiModel ricercaAgentiClientiModel);
	public RicercaGlobaleClientiModel 	elencoAgentiCliente(ClientSessionContext csc, RicercaGlobaleClientiModel ricercaGlobaleClientiModel);
}
