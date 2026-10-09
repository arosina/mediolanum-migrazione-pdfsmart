package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class ServiceModel extends CommandDataModel{
	
	private StringType service=new StringType();
	private IntegerType totDocumenti=new IntegerType();
	
	public StringType getService() {
		return service;
	}
	public void setService(StringType service) {
		this.service = service;
	}
	public IntegerType getTotDocumenti() {
		return totDocumenti;
	}
	public void setTotDocumenti(IntegerType totDocumenti) {
		this.totDocumenti = totDocumenti;
	}
	
	

}
