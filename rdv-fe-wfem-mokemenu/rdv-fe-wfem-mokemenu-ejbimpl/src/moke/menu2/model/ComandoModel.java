package moke.menu2.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************
 ***********************************************************************************************/
public class ComandoModel extends CommandDataModel {
	private IntegerType id = new IntegerType();
	private StringType  progetto = new StringType();
	private StringType  attore = new StringType();
	private StringType  descrizione = new StringType();
	private StringType  comando = new StringType();
	
	public StringType getProgetto() {
		return progetto;
	}
	public void setProgetto(StringType progetto) {
		this.progetto = progetto;
	}
	public StringType getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}
	public StringType getComando() {
		return comando;
	}
	public void setComando(StringType comando) {
		this.comando = comando;
	}
	public StringType getAttore() {
		return attore;
	}
	public void setAttore(StringType attore) {
		this.attore = attore;
	}
	public IntegerType getId() {
		return id;
	}
	public void setId(IntegerType id) {
		this.id = id;
	}
}
