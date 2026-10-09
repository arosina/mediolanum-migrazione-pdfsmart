package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class ErroreDocumentoModel extends CommandDataModel {

	private IntegerType progressivo = new IntegerType();
	private StringType 	idDac = new StringType();
	private StringType 	barcode = new StringType();
	private IntegerType	codErrore = new IntegerType();
	private BooleanType	daAutorizzare = new BooleanType();
	private TimestampType	dataAutorizzazione = new TimestampType();
	private BooleanType	inSpedizione = new BooleanType();
	
	private StringType 	parametroUno = new StringType();
	
	//Caricati da join
	private StringType 	erroreDescr = new StringType();
	
	//Dati tecnici
	private TimestampType dataIns = new TimestampType();
	
	public ErroreDocumentoModel() {
		if(Configuration.getInstance().isOnlineEnvironment()) {
			addCodDescField("codErrore","Errori");
		}
	}

	public StringType getBarcode() {
		return barcode;
	}
	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}
	public StringType getIdDac() {
		return idDac;
	}
	public void setIdDac(StringType idDac) {
		this.idDac = idDac;
	}
	public IntegerType getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}
	public TimestampType getDataIns() {
		return dataIns;
	}
	public void setDataIns(TimestampType dataIns) {
		this.dataIns = dataIns;
	}
	public BooleanType getDaAutorizzare() {
		return daAutorizzare;
	}
	public void setDaAutorizzare(BooleanType daAutorizzare) {
		this.daAutorizzare = daAutorizzare;
	}
	public BooleanType getInSpedizione() {
		return inSpedizione;
	}
	public void setInSpedizione(BooleanType inSpedizione) {
		this.inSpedizione = inSpedizione;
	}
	public StringType getErroreDescr() {
		return erroreDescr;
	}
	public void setErroreDescr(StringType erroreDescr) {
		this.erroreDescr = erroreDescr;
	}
	public TimestampType getDataAutorizzazione() {
		return dataAutorizzazione;
	}
	public void setDataAutorizzazione(TimestampType dataAutorizzazione) {
		this.dataAutorizzazione = dataAutorizzazione;
	}
	public StringType getParametroUno() {
		return parametroUno;
	}
	public void setParametroUno(StringType parametroUno) {
		this.parametroUno = parametroUno;
	}

	public IntegerType getCodErrore() {
		return codErrore;
	}

	public void setCodErrore(IntegerType codErrore) {
		this.codErrore = codErrore;
	}
	
}
