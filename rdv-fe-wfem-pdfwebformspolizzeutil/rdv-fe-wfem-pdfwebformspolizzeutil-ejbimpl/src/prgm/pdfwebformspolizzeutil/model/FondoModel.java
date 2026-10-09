package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class FondoModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1537262625578255065L;
	/**
	 * 
	 */
	
	private StringType codFondo = new StringType();	
	private DoubleType importo = new DoubleType();
	private DoubleType percentuale = new DoubleType();
	private StringType profilo = new StringType();
	
	public FondoModel()	{
		super();
	}
	
	public FondoModel(StringType codFondo, DoubleType importo, DoubleType percentuale)	{
		this();
		setCodFondo(codFondo);
		setImporto(importo);
		setPercentuale(percentuale);
	}
	
	public FondoModel(StringType codFondo, DoubleType importo, DoubleType percentuale, StringType profilo)	{
		this(codFondo, importo, percentuale);
		setProfilo(profilo);
	}
	
	public StringType getCodFondo() {
		return codFondo;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public DoubleType getPercentuale() {
		return percentuale;
	}
	public void setCodFondo(StringType codFondo) {
		this.codFondo = codFondo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public void setPercentuale(DoubleType percentuale) {
		this.percentuale = percentuale;
	}

	public StringType getProfilo() {
		return profilo;
	}

	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
}
