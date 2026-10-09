package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class DispAdeguatezzaInput extends CommandDataModel {

	private StringType 	codDisposizione			= new StringType();
	private StringType 	flagInadg				= new StringType();
    private StringType  codAgente				= new StringType();
    private StringType  codRete 				= new StringType();
    private IntegerType	orizzonteTemporale		= new IntegerType();
    private IntegerType	tolleranzaVolatilita	= new IntegerType();

	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getFlagInadg() {
		return flagInadg;
	}
	public void setFlagInadg(StringType flagInadg) {
		this.flagInadg = flagInadg;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public IntegerType getOrizzonteTemporale() {
		return orizzonteTemporale;
	}
	public void setOrizzonteTemporale(IntegerType orizzonteTemporale) {
		this.orizzonteTemporale = orizzonteTemporale;
	}
	public IntegerType getTolleranzaVolatilita() {
		return tolleranzaVolatilita;
	}
	public void setTolleranzaVolatilita(IntegerType tolleranzaVolatilita) {
		this.tolleranzaVolatilita = tolleranzaVolatilita;
	}
		
}
