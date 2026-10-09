package prgm.cedacri.adeguatezza;

import javax.ejb.Remote;

import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioModel;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface AdeguatezzaManager extends Manager
{
	/*
	 * Servizio 1
	 * Servizio Compilazione Nuovo Questionario
	 */
	public OutputGetNuovoQuestionarioBean  getNuovoQuestionarioWS(InputGetNuovoQuestionarioBean bean);
	public OutputGetNuovoQuestionarioModel getNuovoQuestionario  (ClientSessionContext csc, InputGetNuovoQuestionarioModel model);
	
	/*
	 * Servizio 2
	 * Servizio di controllo congruenza domande/risposte e calcolo del profilo dell'investitore
	 */
	public OutputCalcoloProfiloBean        calcoloProfiloWS      (InputCalcoloProfiloBean bean);
	public OutputCalcoloProfiloModel       calcoloProfilo        (ClientSessionContext csc, InputCalcoloProfiloModel model);
	
	/*
	 * Servizio 4
	 * Servizio per la restituzione dell'elenco questionari
	 */
	public OutputGetElencoQuestionariBean  getElencoQuestionariWS(InputGetElencoQuestionariBean bean);
	public OutputGetElencoQuestionariModel getElencoQuestionari  (ClientSessionContext csc, InputGetElencoQuestionariModel model);
	
	/*
	 * Servizio 5
	 * Servizio Lettura Ultimo Questionario 
	 */
	public OutputGetQuestionarioBean       getQuestionarioWS   (InputGetQuestionarioBean bean);
	public OutputGetQuestionarioModel      getQuestionario     (ClientSessionContext csc, InputGetQuestionarioModel model);
	
	/*
	 * Servizio 6
	 * Servizio di memorizzazione/storicizzazione dei dati
	 */
	public OutputSalvaQuestionarioBean     salvaQuestionarioWS (InputSalvaQuestionarioBean bean);
	public OutputSalvaQuestionarioModel    salvaQuestionario   (ClientSessionContext csc, InputSalvaQuestionarioModel model);
	
	/*
	 * Servizio 7
	 * Servizio di cancellazione questionario in bozza
	 */
	public OutputCancellaQuestionarioBean  cancellaQuestionarioWS(InputCancellaQuestionarioBean bean);
	public OutputCancellaQuestionarioModel cancellaQuestionario  (ClientSessionContext csc, InputCancellaQuestionarioModel model);

	/*
	 * Servizio 9
	 * Servizio Controllo Adeguatezza Operazione
	 */
	public OutputCtrlAdeguatezzaBean  ctrlAdeguatezzaWS(InputCtrlAdeguatezzaBean bean);
	public OutputCtrlAdeguatezzaModel ctrlAdeguatezza  (ClientSessionContext csc, InputCtrlAdeguatezzaModel model);

	/*
	 * Servizio 10
	 * Servizio Lettura Ultimo Profilo 
	 */
	public OutputGetQuestionarioModel getProfilo (ClientSessionContext csc, InputGetQuestionarioModel input);
	public OutputGetQuestionarioBean getProfiloWS (InputGetQuestionarioBean input);

	/*
	 * Servizio 11
	 * Servizio Aggiornamento Patrimonio
	 */
	public OutputAggiornaPatrimonioBean     aggiornaPatrimonioWS (InputAggiornaPatrimonioBean bean);
	public OutputAggiornaPatrimonioModel    aggiornaPatrimonio   (ClientSessionContext csc, InputAggiornaPatrimonioModel model);

}
