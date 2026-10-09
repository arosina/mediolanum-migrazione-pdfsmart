package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class EsitoBasketMifidModel extends CommandDataModel{
	
    private StringType  esito 				= new StringType("OK");
    private StringType  esitoTecnico 		= new StringType();
    private StringType  descrizioneEsito 	= new StringType();
    private StringType  ndg 				= new StringType();
    private StringType  codPotenziale		= new StringType();
    private DoubleType  ctrvPatrimonioTotale= new DoubleType();
    private ListType	profili		 		= new ListType(ProfiloClienteMifidModel.class);
    
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getDescrizioneEsito() {
		return descrizioneEsito;
	}
	public void setDescrizioneEsito(StringType descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public StringType getEsitoTecnico() {
		return esitoTecnico;
	}
	public void setEsitoTecnico(StringType esitoTecnico) {
		this.esitoTecnico = esitoTecnico;
	}
	public StringType getNdg() {
		return ndg;
	}
	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}
	public StringType getCodPotenziale() {
		return codPotenziale;
	}
	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}
	public ListType getProfili() {
		return profili;
	}
	public void setProfili(ListType profili) {
		this.profili = profili;
	}
	public DoubleType getCtrvPatrimonioTotale() {
		return ctrvPatrimonioTotale;
	}
	public void setCtrvPatrimonioTotale(DoubleType ctrvPatrimonioTotale) {
		this.ctrvPatrimonioTotale = ctrvPatrimonioTotale;
	}
    
}
