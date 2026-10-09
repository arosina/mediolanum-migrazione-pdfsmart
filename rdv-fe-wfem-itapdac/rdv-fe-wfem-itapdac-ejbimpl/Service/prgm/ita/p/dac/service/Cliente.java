package prgm.ita.p.dac.service;

import java.io.Serializable;

import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String codMediolanum;
	private String cognome;
	private String nome;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void fillApplModel(prgm.ita.p.dac.model.ClienteModel cli){
		cli.setCodMediolanum(new StringType(getCodMediolanum()));
		cli.setCognome(new StringType(getCognome()));
		cli.setNome(new StringType(getNome()));
	}

	public String getCodMediolanum() {
		return codMediolanum;
	}

	public void setCodMediolanum(String codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
