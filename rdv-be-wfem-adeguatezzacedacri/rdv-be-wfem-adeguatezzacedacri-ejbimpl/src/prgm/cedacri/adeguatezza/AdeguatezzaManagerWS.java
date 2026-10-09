package prgm.cedacri.adeguatezza;

import javax.ejb.Remote;

import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioBean;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloBean;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaBean;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariBean;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioBean;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioBean;

import com.atosorigin.wfem.backend.Manager;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface AdeguatezzaManagerWS extends Manager  {
	/*
	 * Servizio 1
	 * Servizio Compilazione Nuovo Questionario
	 */
	public OutputGetNuovoQuestionarioBean  getNuovoQuestionarioWS(InputGetNuovoQuestionarioBean bean);
	
	/*
	 * Servizio 2
	 * Servizio di controllo congruenza domande/risposte e calcolo del profilo dell'investitore
	 */
	public OutputCalcoloProfiloBean        calcoloProfiloWS      (InputCalcoloProfiloBean bean);
	
	/*
	 * Servizio 4
	 * Servizio per la restituzione dell'elenco questionari
	 */
	public OutputGetElencoQuestionariBean  getElencoQuestionariWS(InputGetElencoQuestionariBean bean);
	
	/*
	 * Servizio 5
	 * Servizio Lettura Ultimo Questionario 
	 */
	public OutputGetQuestionarioBean       getQuestionarioWS   (InputGetQuestionarioBean bean);
	
	/*
	 * Servizio 6
	 * Servizio di memorizzazione/storicizzazione dei dati
	 */
	public OutputSalvaQuestionarioBean     salvaQuestionarioWS (InputSalvaQuestionarioBean bean);
	
	/*
	 * Servizio 7
	 * Servizio di cancellazione questionario in bozza
	 */
	public OutputCancellaQuestionarioBean  cancellaQuestionarioWS(InputCancellaQuestionarioBean bean);

	/*
	 * Servizio 9
	 * Servizio Controllo Adeguatezza Operazione
	 */
	public OutputCtrlAdeguatezzaBean  ctrlAdeguatezzaWS(InputCtrlAdeguatezzaBean bean);

	/*
	 * Servizio 10
	 * Servizio Lettura Ultimo Profilo 
	 */
	public OutputGetQuestionarioBean getProfiloWS (InputGetQuestionarioBean input);

	/*
	 * Servizio 11
	 * Servizio Aggiornamento Patrimonio
	 */
	public OutputAggiornaPatrimonioBean     aggiornaPatrimonioWS (InputAggiornaPatrimonioBean bean);
	

}
