package prgm.ita.anagraficaclienti.pcp;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class ProfiloPcpClienteSrvInputModel extends CommandDataModel {

	private StringType 		idConversazione = new StringType();
	private TimestampType 	timeStamp = new TimestampType();
	private StringType 		userId = new StringType();
	private StringType 		idCliente = new StringType();
	private StringType 		flagProvvisorio = new StringType();
	
	public StringType getIdConversazione() {
		return idConversazione;
	}
	public void setIdConversazione(StringType idConversazione) {
		this.idConversazione = idConversazione;
	}
	public TimestampType getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(TimestampType timeStamp) {
		this.timeStamp = timeStamp;
	}
	public StringType getUserId() {
		return userId;
	}
	public void setUserId(StringType userId) {
		this.userId = userId;
	}
	public StringType getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(StringType idCliente) {
		this.idCliente = idCliente;
	}
	public StringType getFlagProvvisorio() {
		return flagProvvisorio;
	}
	public void setFlagProvvisorio(StringType flagProvvisorio) {
		this.flagProvvisorio = flagProvvisorio;
	}

}
