package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class PianoAccumuloModel extends CommandDataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1707594087627862463L;
	
	private StringType codProdotto = new StringType();
	private StringType codContratto = new StringType();
	private StringType canale = new StringType();
	private DoubleType importoAggiuntivo = new DoubleType();
		
	private ListType dettaglioFondi = new ListType(FondoModel.class);
	private IntegerType resultCode = new IntegerType();
	
	
	public FondoModel findFondo(String codFondo) {						
		for(int i = 0; i < getDettaglioFondi().size(); i++) {
			FondoModel fondo = (FondoModel) getDettaglioFondi().get(i); 
			if (fondo.getCodFondo().equals(codFondo)){
				return fondo;
			}
		}
		return null;
	}
	
	public IntegerType getResultCode() {
		return resultCode;
	}

	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

	public ListType getDettaglioFondi() {
		return dettaglioFondi;
	}

	public void setDettaglioFondi(ListType dettaglioFondi) {
		this.dettaglioFondi = dettaglioFondi;
	}

	public StringType getCodProdotto() {
		return codProdotto;
	}

	public StringType getCodContratto() {
		return codContratto;
	}

	public StringType getCanale() {
		return canale;
	}

	public DoubleType getImportoAggiuntivo() {
		return importoAggiuntivo;
	}

	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}

	public void setCodContratto(StringType codContratto) {
		this.codContratto = codContratto;
	}

	public void setCanale(StringType canale) {
		this.canale = canale;
	}

	public void setImportoAggiuntivo(DoubleType importoAggiuntivo) {
		this.importoAggiuntivo = importoAggiuntivo;
	}
}
