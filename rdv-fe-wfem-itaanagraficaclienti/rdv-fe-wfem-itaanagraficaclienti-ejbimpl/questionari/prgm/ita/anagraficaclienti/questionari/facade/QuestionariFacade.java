package prgm.ita.anagraficaclienti.questionari.facade;

import javax.ejb.Remote;

import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.questionari.model.PatrimonioModel;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface QuestionariFacade extends Facade {
	public PatrimonioModel 		getPatrimonio(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public QuestionarioModel 	getQuestionario(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public StringType 			getProfilo(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public StringType 			getProfiloSede(ClientSessionContext csc, ClienteKeyModel clienteKey);
	public QuestionarioModel 	calcoloProfilo(ClientSessionContext csc, QuestionarioModel questionario);
	public QuestionarioModel 	salvaQuestionario(ClientSessionContext csc, QuestionarioModel questionario);
	public PatrimonioModel 		salvaPatrimonio(ClientSessionContext csc, PatrimonioModel questionario);
	public OutputGetQuestionarioModel getProfiloModel(ClientSessionContext csc, ClienteKeyModel clienteKey);

}
