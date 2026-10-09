package prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/******************************************************************************/
/******************************************************************************/
public class ConvenzioneDCModel extends CommandDataModel {
	
	private StringType codiceProdotto = new StringType();
	private StringType tipologia = new StringType();
	private IntegerType durata = new IntegerType();
	private DoubleType importo = new DoubleType();
	private StringType tasso = new StringType();
	private StringType personaGiuridica = new StringType();
	private DateType dataFirma = new DateType();
	
	public StringType getCodiceProdotto() {
		return codiceProdotto;
	}
	public void setCodiceProdotto(StringType codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}
	public StringType getTipologia() {
		return tipologia;
	}
	public void setTipologia(StringType tipologia) {
		this.tipologia = tipologia;
	}
	public IntegerType getDurata() {
		return durata;
	}
	public void setDurata(IntegerType durata) {
		this.durata = durata;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public StringType getTasso() {
		return tasso;
	}
	public void setTasso(StringType tasso) {
		this.tasso = tasso;
	}
	public StringType getPersonaGiuridica() {
		return personaGiuridica;
	}
	public void setPersonaGiuridica(StringType personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}
	public DateType getDataFirma() {
		return dataFirma;
	}
	public void setDataFirma(DateType dataFirma) {
		this.dataFirma = dataFirma;
	}
}
