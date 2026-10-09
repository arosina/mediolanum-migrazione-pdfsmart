package prgm.pdfwebforms.questionariolight;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class QuestionarioLightModel  extends CommandDataModel {

	// Input
	private StringType codiceCliente = new StringType();
	private StringType utente = new StringType();	
    
    // Output
	private StringType	codiceRisposta = new StringType();
	private StringType	messaggioRisposta = new StringType();
	private StringType	codicePCP = new StringType();
    private ListType 	indicatori = new ListType(IndicatoreModel.class);
    private ListType 	domande = new ListType(DomandaModel.class);

    // Se != null quesitonario non compilabile
    private String nonCompilabileMsg = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public StringType getUtente() {
		return utente;
	}

	public void setUtente(StringType utente) {
		this.utente = utente;
	}

	public StringType getCodiceRisposta() {
		return codiceRisposta;
	}

	public void setCodiceRisposta(StringType codiceRisposta) {
		this.codiceRisposta = codiceRisposta;
	}

	public StringType getMessaggioRisposta() {
		return messaggioRisposta;
	}

	public void setMessaggioRisposta(StringType messaggioRisposta) {
		this.messaggioRisposta = messaggioRisposta;
	}

	public StringType getCodicePCP() {
		return codicePCP;
	}

	public void setCodicePCP(StringType codicePCP) {
		this.codicePCP = codicePCP;
	}

	public ListType getIndicatori() {
		return indicatori;
	}

	public void setIndicatori(ListType indicatori) {
		this.indicatori = indicatori;
	}

	public ListType getDomande() {
		return domande;
	}

	public void setDomande(ListType domande) {
		this.domande = domande;
	}

	public String getNonCompilabileMsg() {
		return nonCompilabileMsg;
	}

	public void setNonCompilabileMsg(String nonCompilabileMsg) {
		this.nonCompilabileMsg = nonCompilabileMsg;
	}

}
