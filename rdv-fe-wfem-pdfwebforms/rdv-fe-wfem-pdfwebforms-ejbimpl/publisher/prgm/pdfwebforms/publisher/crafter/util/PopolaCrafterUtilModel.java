package prgm.pdfwebforms.publisher.crafter.util;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopolaCrafterUtilModel extends CommandDataModel{

	private StringType areeConfigurate = new StringType();

	private StringType aree = new StringType();
	private StringType indicatori = new StringType();
	
	private StringType  filtro = new StringType();
	private IntegerType pdfElaborati = new IntegerType();

	private String resultMessage = null;
	private boolean popolamentoInterrotto = false;
	
	public StringType getAree() {
		return aree;
	}
	public void setAree(StringType aree) {
		this.aree = aree;
	}
	public StringType getIndicatori() {
		return indicatori;
	}
	public void setIndicatori(StringType indicatori) {
		this.indicatori = indicatori;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public StringType getFiltro() {
		return filtro;
	}
	public void setFiltro(StringType filtro) {
		this.filtro = filtro;
	}
	public IntegerType getPdfElaborati() {
		return pdfElaborati;
	}
	public void setPdfElaborati(IntegerType pdfElaborati) {
		this.pdfElaborati = pdfElaborati;
	}
	public boolean isPopolamentoInterrotto() {
		return popolamentoInterrotto;
	}
	public void setPopolamentoInterrotto(boolean popolamentoInterrotto) {
		this.popolamentoInterrotto = popolamentoInterrotto;
	}
	public StringType getAreeConfigurate() {
		return areeConfigurate;
	}
	public void setAreeConfigurate(StringType areeConfigurate) {
		this.areeConfigurate = areeConfigurate;
	}
	
}
