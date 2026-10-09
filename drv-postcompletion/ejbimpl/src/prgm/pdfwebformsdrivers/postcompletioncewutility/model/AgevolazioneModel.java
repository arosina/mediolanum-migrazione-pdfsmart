package prgm.pdfwebformsdrivers.postcompletioncewutility.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class AgevolazioneModel extends CommandDataModel {
	
	
	private StringType idIstanza	                    = new StringType();	
	
	//Tipi agevolazione
	private StringType  tipoAgevolazione	            = new StringType();	

	private BooleanType isAgevolazioneCommissionale	    = new BooleanType();	
	private BooleanType isAgevolazionePersonalPremium	= new BooleanType();	
	private BooleanType isAgevolazioneRetention	        = new BooleanType();	
	private BooleanType isAgevolazioneMoser	            = new BooleanType();
	private BooleanType isAgevolazioneDerogaAutomatica   = new BooleanType();
	
	
	private StringType codiceConvenzione	           = new StringType();	
	private StringType codiceAgevolazione	           = new StringType();	
	private StringType percentualeAgevolazione	       = new StringType();	

	
	private StringType pianoVersamentoAgevolazione	   = new StringType();	
	private StringType picProgrammatoAgevolazione	   = new StringType();	
	private StringType doubleChanceAgevolazione	       = new StringType();	
	private DoubleType importoAgevolazione	           = new DoubleType();	
	private StringType agevolazioneCommissionalePac	   = new StringType();
	
	
	
	
	public StringType getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(StringType idIstanza) {
		this.idIstanza = idIstanza;
	}
	public StringType getTipoAgevolazione() {
		return tipoAgevolazione;
	}
	public void setTipoAgevolazione(StringType tipoAgevolazione) {
		this.tipoAgevolazione = tipoAgevolazione;
	}
	public BooleanType getIsAgevolazioneCommissionale() {
		return isAgevolazioneCommissionale;
	}
	public void setIsAgevolazioneCommissionale(BooleanType isAgevolazioneCommissionale) {
		this.isAgevolazioneCommissionale = isAgevolazioneCommissionale;
	}
	public BooleanType getIsAgevolazionePersonalPremium() {
		return isAgevolazionePersonalPremium;
	}
	public void setIsAgevolazionePersonalPremium(BooleanType isAgevolazionePersonalPremium) {
		this.isAgevolazionePersonalPremium = isAgevolazionePersonalPremium;
	}
	public BooleanType getIsAgevolazioneRetention() {
		return isAgevolazioneRetention;
	}
	public void setIsAgevolazioneRetention(BooleanType isAgevolazioneRetention) {
		this.isAgevolazioneRetention = isAgevolazioneRetention;
	}
	public BooleanType getIsAgevolazioneMoser() {
		return isAgevolazioneMoser;
	}
	public void setIsAgevolazioneMoser(BooleanType isAgevolazioneMoser) {
		this.isAgevolazioneMoser = isAgevolazioneMoser;
	}
	public StringType getCodiceConvenzione() {
		return codiceConvenzione;
	}
	public void setCodiceConvenzione(StringType codiceConvenzione) {
		this.codiceConvenzione = codiceConvenzione;
	}
	public StringType getCodiceAgevolazione() {
		return codiceAgevolazione;
	}
	public void setCodiceAgevolazione(StringType codiceAgevolazione) {
		this.codiceAgevolazione = codiceAgevolazione;
	}
	public StringType getPercentualeAgevolazione() {
		return percentualeAgevolazione;
	}
	public void setPercentualeAgevolazione(StringType percentualeAgevolazione) {
		this.percentualeAgevolazione = percentualeAgevolazione;
	}
	public StringType getPianoVersamentoAgevolazione() {
		return pianoVersamentoAgevolazione;
	}
	public void setPianoVersamentoAgevolazione(StringType pianoVersamentoAgevolazione) {
		this.pianoVersamentoAgevolazione = pianoVersamentoAgevolazione;
	}
	public StringType getPicProgrammatoAgevolazione() {
		return picProgrammatoAgevolazione;
	}
	public void setPicProgrammatoAgevolazione(StringType picProgrammatoAgevolazione) {
		this.picProgrammatoAgevolazione = picProgrammatoAgevolazione;
	}
	public StringType getDoubleChanceAgevolazione() {
		return doubleChanceAgevolazione;
	}
	public void setDoubleChanceAgevolazione(StringType doubleChanceAgevolazione) {
		this.doubleChanceAgevolazione = doubleChanceAgevolazione;
	}
	public DoubleType getImportoAgevolazione() {
		return importoAgevolazione;
	}
	public void setImportoAgevolazione(DoubleType importoAgevolazione) {
		this.importoAgevolazione = importoAgevolazione;
	}
	public StringType getAgevolazioneCommissionalePac() {
		return agevolazioneCommissionalePac;
	}
	public void setAgevolazioneCommissionalePac(StringType agevolazioneCommissionalePac) {
		this.agevolazioneCommissionalePac = agevolazioneCommissionalePac;
	}
	public BooleanType getIsAgevolazioneDerogaAutomatica() {
		return isAgevolazioneDerogaAutomatica;
	}
	public void setIsAgevolazioneDerogaAutomatica(BooleanType isAgevolazioneDerogaAutomatica) {
		this.isAgevolazioneDerogaAutomatica = isAgevolazioneDerogaAutomatica;
	}	
}
