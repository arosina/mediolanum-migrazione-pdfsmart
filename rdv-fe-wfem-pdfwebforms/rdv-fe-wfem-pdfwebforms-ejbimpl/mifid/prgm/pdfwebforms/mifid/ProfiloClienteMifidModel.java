package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class ProfiloClienteMifidModel extends CommandDataModel {

	private StringType  esito 				= new StringType("OK");
	private StringType  flagProfiloValido 	= new StringType();
	private StringType  statoProfilo 		= new StringType();
	private ListType	controlli			= new ListType(ControlloClienteMifidModel.class);
    private ListType	esitiSecondari 		= new ListType(EsitoBasketMifidModel.class);
    
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public ListType getControlli() {
		return controlli;
	}
	public void setControlli(ListType controlli) {
		this.controlli = controlli;
	}
	public ListType getEsitiSecondari() {
		return esitiSecondari;
	}
	public void setEsitiSecondari(ListType esitiSecondari) {
		this.esitiSecondari = esitiSecondari;
	}
	public StringType getFlagProfiloValido() {
		return flagProfiloValido;
	}
	public void setFlagProfiloValido(StringType flagProfiloValido) {
		this.flagProfiloValido = flagProfiloValido;
	}
	public StringType getStatoProfilo() {
		return statoProfilo;
	}
	public void setStatoProfilo(StringType statoProfilo) {
		this.statoProfilo = statoProfilo;
	}
	
}
