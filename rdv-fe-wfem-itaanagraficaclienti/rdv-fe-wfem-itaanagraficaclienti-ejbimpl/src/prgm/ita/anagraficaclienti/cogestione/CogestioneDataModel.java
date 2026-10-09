package prgm.ita.anagraficaclienti.cogestione;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class CogestioneDataModel extends CommandDataModel{

	private BooleanType isUtenteCogestore = new BooleanType();
	private BooleanType isUtenteTitolare = new BooleanType();
	private StringType	statoContrattoCogestione = new StringType();
	private StringType	codAgenteTitolare = new StringType();
	private StringType	codAgenteCogestore = new StringType();
	private StringType	nominativoAgenteTitolare = new StringType();
	private StringType	nominativoAgenteCogestore = new StringType();

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static boolean isCensimentoHub(ClienteModel cliente){
		try {
			return Class.forName("prgm.hubspecialistiprotezione.censimentoassicurato.AssicuratoModel").isAssignableFrom(cliente.getClass());
		}catch(ClassNotFoundException cnfe) {
			return false;
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static boolean isCensimentoMhd(ClienteModel cliente){
		return cliente.getCallingAppl().equals(Costanti.CALLING_APPL_MHD);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public boolean isContrattoCogestioneAttivo() {
		return getStatoContrattoCogestione().equals("ATT");
	}
	
	public BooleanType getIsUtenteCogestore() {
		return isUtenteCogestore;
	}

	public void setIsUtenteCogestore(BooleanType isUtenteCogestore) {
		this.isUtenteCogestore = isUtenteCogestore;
	}

	public BooleanType getIsUtenteTitolare() {
		return isUtenteTitolare;
	}

	public void setIsUtenteTitolare(BooleanType isUtenteTitolare) {
		this.isUtenteTitolare = isUtenteTitolare;
	}

	public StringType getStatoContrattoCogestione() {
		return statoContrattoCogestione;
	}

	public void setStatoContrattoCogestione(StringType statoContrattoCogestione) {
		this.statoContrattoCogestione = statoContrattoCogestione;
	}

	public StringType getCodAgenteTitolare() {
		return codAgenteTitolare;
	}

	public void setCodAgenteTitolare(StringType codAgenteTitolare) {
		this.codAgenteTitolare = codAgenteTitolare;
	}

	public StringType getCodAgenteCogestore() {
		return codAgenteCogestore;
	}

	public void setCodAgenteCogestore(StringType codAgenteCogestore) {
		this.codAgenteCogestore = codAgenteCogestore;
	}

	public StringType getNominativoAgenteTitolare() {
		return nominativoAgenteTitolare;
	}

	public void setNominativoAgenteTitolare(StringType nominativoAgenteTitolare) {
		this.nominativoAgenteTitolare = nominativoAgenteTitolare;
	}

	public StringType getNominativoAgenteCogestore() {
		return nominativoAgenteCogestore;
	}

	public void setNominativoAgenteCogestore(StringType nominativoAgenteCogestore) {
		this.nominativoAgenteCogestore = nominativoAgenteCogestore;
	}

}
