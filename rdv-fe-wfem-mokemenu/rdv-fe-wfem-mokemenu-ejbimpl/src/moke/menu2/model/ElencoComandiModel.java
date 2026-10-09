package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************
 ***********************************************************************************************/
public class ElencoComandiModel extends CommandDataModel {
	private BooleanType 	loadConf = new BooleanType();
	private StringType 		remoteAddr = new StringType();
	private StringType 		progetto = new StringType();
	private ComandoModel 	comando = new ComandoModel();
	private ListType   		comandi = new ListType(ComandoModel.class);
	
	/***********************************************************************************************
	 ***********************************************************************************************/
	public ElencoComandiModel(){
		addCodDescField("progetto","PROGETTI");
	}
	
	public StringType getProgetto() {
		return progetto;
	}
	public void setProgetto(StringType progetto) {
		this.progetto = progetto;
	}
	public ListType getComandi() {
		return comandi;
	}
	public void setComandi(ListType comandi) {
		this.comandi = comandi;
	}

	public ComandoModel getComando() {
		return comando;
	}

	public void setComando(ComandoModel comando) {
		this.comando = comando;
	}

	public StringType getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(StringType remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public BooleanType getLoadConf() {
		return loadConf;
	}

	public void setLoadConf(BooleanType loadConf) {
		this.loadConf = loadConf;
	}

}
