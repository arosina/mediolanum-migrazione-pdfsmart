package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class ServiceResponse extends CommandDataModel {

	public static StringType OK = new StringType("01");
	public static StringType WARNING = new StringType("02");
	public static StringType ERROR = new StringType("03");
	public static StringType FATAL = new StringType("04");
	
	public StringType esito = new StringType();
	public StringType messaggio = new StringType();
	
	public ServiceResponse() {
		this(new StringType(),new StringType());
	}
	
	public ServiceResponse(StringType esito) {
		this(esito,new StringType());
	}

	public ServiceResponse(StringType esito, StringType messaggio) {
		super();
		this.esito = esito;
		this.messaggio = messaggio;
	}
	
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(StringType messaggio) {
		this.messaggio = messaggio;
	}
	
	public boolean isOK() {
		return OK.equals(esito);
	}
	
	public boolean isWARNING() {
		return WARNING.equals(esito);
	}
	
	public boolean isERROR() {
		return ERROR.equals(esito);
	}
	
	public boolean isFATAL() {
		return FATAL.equals(esito);
	}
	
	
	public static ServiceResponse esitoOK() {
		return new ServiceResponse(ServiceResponse.OK);
	}
	
	public static ServiceResponse esitoWARNING(StringType message) {
		return new ServiceResponse(ServiceResponse.WARNING, message);
	}
	
	public static ServiceResponse esitoWARNING(String message) {
		return esitoWARNING(new StringType(message));
	}
	
	public static ServiceResponse esitoERROR(StringType message) {
		return new ServiceResponse(ServiceResponse.ERROR, message);
	}
	
	public static ServiceResponse esitoERROR(String message) {
		return esitoERROR(new StringType(message));
	}
	
	public static ServiceResponse esitoFATAL(StringType message) {
		return new ServiceResponse(ServiceResponse.FATAL, message);
	}
	
	public static ServiceResponse esitoFATAL(String message) {
		return esitoFATAL(new StringType(message));
	}
}
