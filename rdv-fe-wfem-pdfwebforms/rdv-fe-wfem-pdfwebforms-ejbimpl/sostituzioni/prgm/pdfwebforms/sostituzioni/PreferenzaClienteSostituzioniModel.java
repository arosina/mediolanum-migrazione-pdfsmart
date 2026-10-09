package prgm.pdfwebforms.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PreferenzaClienteSostituzioniModel extends CommandDataModel{
	
	private StringType  idPreferenza = new StringType();
	private StringType  descrizione = new StringType();
	private IntegerType progressivo = new IntegerType();
	private StringType  preferenzaWealth = new StringType();
	private StringType  preferenzaAmministrato = new StringType();
	
	private BooleanType isSelezionata = new BooleanType();
	
	public StringType getIdPreferenza() {
		return idPreferenza;
	}
	public void setIdPreferenza(StringType idPreferenza) {
		this.idPreferenza = idPreferenza;
	}
	public StringType getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}
	public IntegerType getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}
	public StringType getPreferenzaWealth() {
		return preferenzaWealth;
	}
	public void setPreferenzaWealth(StringType preferenzaWealth) {
		this.preferenzaWealth = preferenzaWealth;
	}
	public BooleanType getIsSelezionata() {
		return isSelezionata;
	}
	public void setIsSelezionata(BooleanType isSelezionata) {
		this.isSelezionata = isSelezionata;
	}
	public StringType getPreferenzaAmministrato() {
		return preferenzaAmministrato;
	}
	public void setPreferenzaAmministrato(StringType preferenzaAmministrato) {
		this.preferenzaAmministrato = preferenzaAmministrato;
	}

}
