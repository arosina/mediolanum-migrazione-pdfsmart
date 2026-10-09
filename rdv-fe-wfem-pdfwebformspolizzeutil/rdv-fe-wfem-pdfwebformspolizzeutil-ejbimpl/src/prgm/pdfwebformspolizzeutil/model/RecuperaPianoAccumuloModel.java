package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class RecuperaPianoAccumuloModel extends PianoAccumuloModel{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 968106822928743205L;
	private PianoAccumuloModel result = new PianoAccumuloModel();

	public RecuperaPianoAccumuloModel() {
		super();
	}
	
	public RecuperaPianoAccumuloModel(StringType codProdotto, StringType codContratto, StringType canale, DoubleType importoAggiuntivo) {
		this();
		setCodProdotto(codProdotto);
		setCodContratto(codContratto);
		setCanale(canale);
		setImportoAggiuntivo(importoAggiuntivo);			
	}
		
	public PianoAccumuloModel getResult() {
		return result;
	}

	public void setResult(PianoAccumuloModel result) {
		this.result = result;
	}	
	
	public FondoModel addFondo(StringType codFondo, DoubleType importo, DoubleType percentuale) {
		FondoModel fondoModel = new prgm.pdfwebformspolizzeutil.model.FondoModel(codFondo, importo, percentuale);
		getDettaglioFondi().add(fondoModel);
		return fondoModel; 
	}
}
