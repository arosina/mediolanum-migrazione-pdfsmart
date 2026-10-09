package prgm.ita.p.dac.model;

import java.util.Vector;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class ClienteModel extends CommandDataModel {
	
	private StringType 	codMediolanum = new StringType();
	private StringType 	cognome = new StringType();
	private StringType 	nome = new StringType();
	private IntegerType numContiCorrenti = new IntegerType();
	
	// Per layout/gestione interna
	private Vector 			changedProps = new Vector();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Vector initChangedProps(ClienteModel other){
		changedProps.removeAllElements();
		
		if(!isEqual(other))
			changedProps.add("codMediolanum");
		
		return changedProps;		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(ClienteModel other){
		if(!getCodMediolanum().toString().equals(other.getCodMediolanum().toString()) 	||
		   !getCognome().toString().equals(other.getCognome().toString()) 				||
		   !getNome().toString().equals(other.getNome().toString()))
			return false;
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getNominativo(){
		StringType res = new StringType();
		res.setEditable(false);
		if(getCognome().isNull() && getNome().isNull())
			return res;
		res = new StringType(getCognome()+" "+getNome());
		res.setEditable(false);
		return res;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public StringType getNominativoConCodMediolanum(){
		if(getCodMediolanum().isNull() && getNome().isNull() && getCognome().isNull())
			return new StringType();
		if(getCodMediolanum().isNull())
			return getNominativo();
		return new StringType(getCodMediolanum()+" - "+getNominativo());
	}
	
	public StringType getCognome() {
		return cognome;
	}
	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}

	public StringType getCodMediolanum() {
		return codMediolanum;
	}

	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

	public IntegerType getNumContiCorrenti() {
		return numContiCorrenti;
	}

	public void setNumContiCorrenti(IntegerType numContiCorrenti) {
		this.numContiCorrenti = numContiCorrenti;
	}

}
