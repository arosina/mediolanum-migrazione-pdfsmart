package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class UniversitaModel extends CommandDataModel {
	private StringType  codUniversita = new StringType();
	private StringType  ateneo = new StringType();
	private StringType  facolta = new StringType();
	private StringType  provincia = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clear() {
		setCodUniversita(new StringType());
		setAteneo(new StringType());
		setFacolta(new StringType());
		setProvincia(new StringType());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getNominativoUniversita() {
		if(codUniversita.isNull())
			return new StringType();
		return new StringType(ateneo.toString()+" - "+facolta.toString()+" ("+provincia.toString()+")");
	}
	
	public StringType getCodUniversita() {
		return codUniversita;
	}
	public void setCodUniversita(StringType codUniversita) {
		this.codUniversita = codUniversita;
	}
	public StringType getFacolta() {
		return facolta;
	}
	public void setFacolta(StringType facolta) {
		this.facolta = facolta;
	}
	public StringType getProvincia() {
		return provincia;
	}
	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}
	public StringType getAteneo() {
		return ateneo;
	}
	public void setAteneo(StringType ateneo) {
		this.ateneo = ateneo;
	}	
}
