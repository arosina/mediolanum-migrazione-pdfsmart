package prgm.pdfwebforms.drivers.io.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OperazioneMifidModel extends CommandDataModel{

	private static final long serialVersionUID = 1L;

	private StringType 	codTipoOperazione = null;
	private StringType 	numeroContratto = new StringType();
	private StringType flagSwitch = new StringType();	//S oppure N
	private ListType 	comparti = new ListType(CompartoMifidModel.class);  // Elenco dei singoli comparti acquistati/disinvestiti. In caso di prodotti senza comparti conterrà un solo elemento

    /***********************************************************************************************/
    /***********************************************************************************************/
    public void addComparto(CompartoMifidModel comparto){
    	getComparti().add(comparto);
    }

    public ListType getComparti() {
		return comparti;
	}

	public void setComparti(ListType comparti) {
		this.comparti = comparti;
	}

	public StringType getNumeroContratto() {
		return numeroContratto;
	}

	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}

	public StringType getCodTipoOperazione() {
		return codTipoOperazione;
	}

	public void setCodTipoOperazione(StringType codTipoOperazione) {
		this.codTipoOperazione = codTipoOperazione;
	}

	public StringType getFlagSwitch() {
		return flagSwitch;
	}

	public void setFlagSwitch(StringType flagSwitch) {
		this.flagSwitch = flagSwitch;
	}


}
