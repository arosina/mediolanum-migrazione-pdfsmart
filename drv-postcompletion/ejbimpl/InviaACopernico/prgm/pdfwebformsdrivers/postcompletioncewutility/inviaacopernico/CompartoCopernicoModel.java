package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************/
/*****************************************************************/
public class CompartoCopernicoModel extends CommandDataModel {

	private StringType 	codComparto		 		= new StringType();
	private StringType 	descrizioneComparto  	= new StringType();
	private StringType 	tipoQuotaComparto    	= new StringType();
	private DoubleType 	importoComparto		 	= new DoubleType();
	private DoubleType 	percentualeComparto  	= new DoubleType();
	//PIC
	private StringType 	consolidabile        	= new StringType("");
	//PAC
	private DoubleType 	rataUnitaria         	= new DoubleType();
	
	private StringType 	percentualeConsolida  	= new StringType();
	private StringType 	codMandatoConsolida		= new StringType();
	private StringType 	codCompartoFondoCons	= new StringType();
	private DoubleType 	valoreLoi         		= new DoubleType();
	private StringType 	reinvestimento		= new StringType();
	
	private StringType 	tipoDisinvestimento		= new StringType();
	private StringType 	codProdottoDis			= new StringType();
	private DoubleType 	importoDis         		= new DoubleType();
	private IntegerType	quoteDis				= new IntegerType(0);

	private StringType 	numeroContratto			= new StringType();
	
	private StringType 	disclaimerFondo			= new StringType();

	/*****************************************************************/
	/*****************************************************************/
	public StringType getCodComparto() {
		return codComparto;
	}

	public void setCodComparto(StringType codComparto) {
		this.codComparto = codComparto;
	}

	public StringType getDescrizioneComparto() {
		return descrizioneComparto;
	}

	public void setDescrizioneComparto(StringType descrizioneComparto) {
		this.descrizioneComparto = descrizioneComparto;
	}

	public StringType getTipoQuotaComparto() {
		return tipoQuotaComparto;
	}

	public void setTipoQuotaComparto(StringType tipoQuotaComparto) {
		this.tipoQuotaComparto = tipoQuotaComparto;
	}

	public DoubleType getImportoComparto() {
		return importoComparto;
	}

	public void setImportoComparto(DoubleType importoComparto) {
		this.importoComparto = importoComparto;
	}

	public DoubleType getPercentualeComparto() {
		return percentualeComparto;
	}

	public void setPercentualeComparto(DoubleType percentualeComparto) {
		this.percentualeComparto = percentualeComparto;
	}

	public StringType getConsolidabile() {
		return consolidabile;
	}

	public void setConsolidabile(StringType consolidabile) {
		this.consolidabile = consolidabile;
	}

	public DoubleType getRataUnitaria() {
		return rataUnitaria;
	}

	public void setRataUnitaria(DoubleType rataUnitaria) {
		this.rataUnitaria = rataUnitaria;
	}

	public StringType getPercentualeConsolida() {
		return percentualeConsolida;
	}

	public void setPercentualeConsolida(StringType percentualeConsolida) {
		this.percentualeConsolida = percentualeConsolida;
	}

	public StringType getCodMandatoConsolida() {
		return codMandatoConsolida;
	}

	public void setCodMandatoConsolida(StringType codMandatoConsolida) {
		this.codMandatoConsolida = codMandatoConsolida;
	}

	public StringType getCodCompartoFondoCons() {
		return codCompartoFondoCons;
	}

	public void setCodCompartoFondoCons(StringType codCompartoFondoCons) {
		this.codCompartoFondoCons = codCompartoFondoCons;
	}

	public DoubleType getValoreLoi() {
		return valoreLoi;
	}

	public void setValoreLoi(DoubleType valoreLoi) {
		this.valoreLoi = valoreLoi;
	}


	public StringType getCodProdottoDis() {
		return codProdottoDis;
	}

	public void setCodProdottoDis(StringType codProdottoDis) {
		this.codProdottoDis = codProdottoDis;
	}

	public DoubleType getImportoDis() {
		return importoDis;
	}

	public void setImportoDis(DoubleType importoDis) {
		this.importoDis = importoDis;
	}

	public IntegerType getQuoteDis() {
		return quoteDis;
	}

	public void setQuoteDis(IntegerType quoteDis) {
		this.quoteDis = quoteDis;
	}

	public StringType getNumeroContratto() {
		return numeroContratto;
	}

	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}

	public StringType getTipoDisinvestimento() {
		return tipoDisinvestimento;
	}

	public void setTipoDisinvestimento(StringType tipoDisinvestimento) {
		this.tipoDisinvestimento = tipoDisinvestimento;
	}

	public StringType getReinvestimento() {
		return reinvestimento;
	}

	public void setReinvestimento(StringType reinvestimento) {
		this.reinvestimento = reinvestimento;
	}

	public StringType getDisclaimerFondo() {
		return disclaimerFondo;
	}

	public void setDisclaimerFondo(StringType disclaimerFondo) {
		this.disclaimerFondo = disclaimerFondo;
	}
	

}