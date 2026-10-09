package prgm.ita.p.dac.sede.model;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class SbloccaDacModel extends CommandDataModel {
	
	private StringType 		barcode		= new StringType();
	private DocumentoModel	documento	= new DocumentoModel();
	private ListType 		elencoDac	= new ListType(DacModel.class);
	private StringType  	idDacSelezionata  = new StringType();
	
	//Dati tecnici
	private boolean			primaVolta = true;
	
	public StringType getBarcode() {
		return barcode;
	}
	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}
	public ListType getElencoDac() {
		return elencoDac;
	}
	public void setElencoDac(ListType elencoDac) {
		this.elencoDac = elencoDac;
	}
	public boolean isPrimaVolta() {
		return primaVolta;
	}
	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}
	public DocumentoModel getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoModel documento) {
		this.documento = documento;
	}
	public StringType getIdDacSelezionata() {
		return idDacSelezionata;
	}
	public void setIdDacSelezionata(StringType idDacSelezionata) {
		this.idDacSelezionata = idDacSelezionata;
	}

}
