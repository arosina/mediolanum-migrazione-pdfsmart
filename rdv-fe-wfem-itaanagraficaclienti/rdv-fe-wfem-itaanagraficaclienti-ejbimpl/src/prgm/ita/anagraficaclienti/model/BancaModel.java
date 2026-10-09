package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.command.CommandDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class BancaModel extends CommandDataModel {
	
	private StringType abi				= new StringType();
    private StringType cab				= new StringType();
    private StringType ragioneSociale	= new StringType();
    private StringType sportello		= new StringType();
    private StringType indirizzo		= new StringType();
    private StringType localita		= new StringType();
    private StringType provincia		= new StringType();    
	
	public StringType getAbi() {
		return abi;
	}

	public StringType getCab() {
		return cab;
	}

	public StringType getIndirizzo() {
		return indirizzo;
	}

	public StringType getLocalita() {
		return localita;
	}

	public StringType getProvincia() {
		return provincia;
	}

	public StringType getRagioneSociale() {
		return ragioneSociale;
	}

	public StringType getSportello() {
		return sportello;
	}

	public void setAbi(StringType abi) {
		this.abi = abi;
	}

	public void setCab(StringType cab) {
		this.cab = cab;
	}

	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setLocalita(StringType localita) {
		this.localita = localita;
	}

	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}

	public void setRagioneSociale(StringType ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}

	public void setSportello(StringType sportello) {
		this.sportello = sportello;
	}

}
