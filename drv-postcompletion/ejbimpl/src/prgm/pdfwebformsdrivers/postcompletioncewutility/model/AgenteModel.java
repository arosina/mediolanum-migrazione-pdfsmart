package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class AgenteModel  extends CommandDataModel {
	
	private StringType serverCReplica = new StringType();
	private StringType codice = new StringType();
	private StringType cognome = new StringType();
	private StringType nome  = new StringType();
	private StringType email = new StringType();

	public StringType getServerCReplica() {
		return serverCReplica;
	}

	public void setServerCReplica(StringType serverCReplica) {
		this.serverCReplica = serverCReplica;
	}

	public StringType getCodice() {
		return codice;
	}

	public void setCodice(StringType codice) {
		this.codice = codice;
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

	public StringType getEmail() {
		return email;
	}

	public void setEmail(StringType email) {
		this.email = email;
	}
	
	public StringType getNominativo () {
		String returnValue = "";
		if (!getCognome().isNull()) {
			returnValue = getCognome().toString();
			
			if (!getNome().isNull()) {
				returnValue = returnValue + " " + getNome().toString(); 
			}
		}

		return new StringType(returnValue);
	}


	public StringType getCodiceNoFill () {
		if (!getCodice().isNull()) {
			Long codiceNoFill = new Long(getCodice().toString());
			return new StringType(String.valueOf(codiceNoFill));
		}
		
		return new StringType();
	}
}
