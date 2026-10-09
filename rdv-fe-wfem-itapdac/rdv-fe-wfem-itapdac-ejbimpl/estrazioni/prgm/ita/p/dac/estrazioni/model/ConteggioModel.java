package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;

public class ConteggioModel extends CommandDataModel{
 private IntegerType numeroDAC=new IntegerType();
 private IntegerType numeroDoc=new IntegerType();
 
public void setNumeroDAC(IntegerType numeroDAC) {
	this.numeroDAC = numeroDAC;
}
public IntegerType getNumeroDAC() {
	return numeroDAC;
}
public void setNumeroDoc(IntegerType numeroDoc) {
	this.numeroDoc = numeroDoc;
}
public IntegerType getNumeroDoc() {
	return numeroDoc;
}
 
 
}
