package prgm.cedacri.adeguatezza.internal;

import java.io.Serializable;

public class StatoAdeguatezza implements Serializable{
	private static final long serialVersionUID = 0;
	public String esito = new String("000");
	public String descrizione = new String("");
	public String serviceName = new String("");
	public int numElem=-1;
	
	public StatoAdeguatezza(String serviceName)
	{
		this.serviceName = serviceName;
	}
	
	public void setDescr(String descrizione)
	{
		this.descrizione = serviceName + ": " + descrizione;
	}

	public int getNumElem() {
		return numElem;
	}

	public void setNumElem(int numElem) {
		this.numElem = numElem;
	}
}
