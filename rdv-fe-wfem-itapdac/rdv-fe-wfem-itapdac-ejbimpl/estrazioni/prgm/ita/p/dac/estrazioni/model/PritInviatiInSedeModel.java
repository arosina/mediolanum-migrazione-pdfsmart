package prgm.ita.p.dac.estrazioni.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class PritInviatiInSedeModel extends CommandDataModel{
	
	private DateType dataDal = new DateType();
	private DateType dataAl = new DateType();
	private BooleanType isEsegui = new BooleanType();
	private StringType tipoEstrazione = new StringType();
	private IntegerType statoPrit = new IntegerType();
	private IntegerType tipologiaPrit = new IntegerType();
	private ListType outputReportSintesi = new ListType(PritInviatiInSedeOutputModel.class);
	private ListType outputReportDettaglio = new ListType(DettaglioDocRicevutiDacModel.class);

	public PritInviatiInSedeModel() { 
		
		addCodDescField("statoPrit","StatoPrit");
		addCodDescField("tipologiaPrit","TipologiaPrit");
	}

	public DateType getDataDal() {
		return dataDal;
	}

	public void setDataDal(DateType dataDal) {
		this.dataDal = dataDal;
	}
	
	public DateType getDataAl() {
		return dataAl;
	}

	public void setDataAl(DateType dataAl) {
		this.dataAl = dataAl;
	}
	
	public BooleanType getIsEsegui() {
		return isEsegui;
	}

	public void setIsEsegui(BooleanType isEsegui) {
		this.isEsegui = isEsegui;
	}

	public StringType getTipoEstrazione() {
		return tipoEstrazione;
	}

	public void setTipoEstrazione(StringType tipoEstrazione) {
		this.tipoEstrazione = tipoEstrazione;
	}
	
	public IntegerType getStatoPrit() {
		return statoPrit;
	}

	public void setStatoPrit(IntegerType statoPrit) {
		this.statoPrit = statoPrit;
	}

	public IntegerType getTipologiaPrit() {
		return tipologiaPrit;
	}

	public void setTipologiaPrit(IntegerType tipologiaPrit) {
		this.tipologiaPrit = tipologiaPrit;
	}
	public ListType getOutputReportSintesi() {
		return outputReportSintesi;
	}

	public void setOutputReportSintesi(ListType outputReportSintesi) {
		this.outputReportSintesi = outputReportSintesi;
	}
	
	public ListType getOutputReportDettaglio() {
		return outputReportDettaglio;
	}

	public void setOutputReportDettaglio(ListType outputReportDettaglio) {
		this.outputReportDettaglio = outputReportDettaglio;
	}
}
