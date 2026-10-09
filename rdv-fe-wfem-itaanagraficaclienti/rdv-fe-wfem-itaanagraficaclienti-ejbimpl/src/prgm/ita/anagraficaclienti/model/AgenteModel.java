package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class AgenteModel extends CommandDataModel {

	private StringType codAgente = new StringType();
	private StringType codRete = new StringType();		
	private StringType cognomeAgente = new StringType();
	private StringType nomeAgente = new StringType();
	private StringType areaAgente = new StringType();
	private StringType codiceContrattoAgente = new StringType();
	private StringType codiceArcContrattoAgente = new StringType();
	private StringType serverReplica = new StringType();
	private StringType descrAgenzia = new StringType();
	private StringType codAgenzia = new StringType();
	private StringType codProvincia = new StringType();
	private StringType cicloVitaAgente = new StringType();
	private DateType   dataInizioAssegnazione = new DateType();
	private DateType   dataFineAssegnazione = new DateType();
	private StringType email = new StringType();
	private StringType cellulare = new StringType();
	private StringType telefono = new StringType();
	private ListType   cellulari = new ListType(TelefonoModel.class);
	private StringType codMediolanum = new StringType();
	private DateType   dataAbilitazioneIsvap = new DateType();
	private StringType numeroIscrizioneIsvap = new StringType();
	private StringType codiceFiscale = new StringType();
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getNominativoAgente() {
		return new StringType(cognomeAgente.toString()+" "+nomeAgente.toString());
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getNominativoConCodiceAgente() {
		return new StringType(codAgente.toString()+" - "+cognomeAgente.toString()+" "+nomeAgente.toString());
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getCellulareAgente() {
		if(!getCellulare().isNull())
			return getCellulare();
		
		if(getCellulari().size() == 0)
			return new StringType();
		
		TelefonoModel cell = (TelefonoModel)getCellulari().get(0);
		return cell.getNumeroTelefonoCompleto();
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ListType getCellulari() {
		ListType result = new ListType(TelefonoModel.class);
		if(!getCellulare().isNull()){
			TelefonoModel t = new TelefonoModel();
			t.setNumeroTelefono(getCellulare());
			result.add(t);
		}
		for(int i=0;i<cellulari.size();i++){
			TelefonoModel t = (TelefonoModel)cellulari.get(i);
			if(!t.getNumeroTelefonoCompleto().equals(getCellulare()))
				result.add(t);
		}
		return result;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public boolean isPromotore(){
		if(getCodiceArcContrattoAgente().equals("PROMOTORE FINANZIARIO") ||
		   getCodiceArcContrattoAgente().equals("DIPENDENTI/DIRIGENTI BM - PF"))
			return true;
		return false;
	}
	
	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public StringType getAreaAgente() {
		return areaAgente;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodAgenzia() {
		return codAgenzia;
	}

	public StringType getCodiceContrattoAgente() {
		return codiceContrattoAgente;
	}

	public StringType getCodProvincia() {
		return codProvincia;
	}

	public StringType getCognomeAgente() {
		return cognomeAgente;
	}

	public StringType getDescrAgenzia() {
		return descrAgenzia;
	}

	public StringType getNomeAgente() {
		return nomeAgente;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public void setAreaAgente(StringType areaAgente) {
		this.areaAgente = areaAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodAgenzia(StringType codAgenzia) {
		this.codAgenzia = codAgenzia;
	}

	public void setCodiceContrattoAgente(StringType codiceContrattoAgente) {
		this.codiceContrattoAgente = codiceContrattoAgente;
	}

	public void setCodProvincia(StringType codProvincia) {
		this.codProvincia = codProvincia;
	}

	public void setCognomeAgente(StringType cognomeAgente) {
		this.cognomeAgente = cognomeAgente;
	}

	public void setDescrAgenzia(StringType descrAgenzia) {
		this.descrAgenzia = descrAgenzia;
	}

	public void setNomeAgente(StringType nomeAgente) {
		this.nomeAgente = nomeAgente;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public StringType getCicloVitaAgente() {
		return cicloVitaAgente;
	}

	public void setCicloVitaAgente(StringType cicloVitaAgente) {
		this.cicloVitaAgente = cicloVitaAgente;
	}

	public DateType getDataFineAssegnazione() {
		return dataFineAssegnazione;
	}

	public void setDataFineAssegnazione(DateType dataFineAssegnazione) {
		this.dataFineAssegnazione = dataFineAssegnazione;
	}

	public DateType getDataInizioAssegnazione() {
		return dataInizioAssegnazione;
	}

	public void setDataInizioAssegnazione(DateType dataInizioAssegnazione) {
		this.dataInizioAssegnazione = dataInizioAssegnazione;
	}

	public StringType getEmail() {
		return email;
	}

	public void setEmail(StringType email) {
		this.email = email;
	}

	public void setCellulari(ListType cellulari) {
		this.cellulari = cellulari;
	}

	public StringType getCellulare() {
		return cellulare;
	}

	public void setCellulare(StringType cellulare) {
		this.cellulare = cellulare;
	}

	public StringType getTelefono() {
		return telefono;
	}

	public void setTelefono(StringType telefono) {
		this.telefono = telefono;
	}

	public StringType getCodiceArcContrattoAgente() {
		return codiceArcContrattoAgente;
	}

	public void setCodiceArcContrattoAgente(StringType codiceArcContrattoAgente) {
		this.codiceArcContrattoAgente = codiceArcContrattoAgente;
	}

	public StringType getCodMediolanum() {
		return codMediolanum;
	}

	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}

	public DateType getDataAbilitazioneIsvap() {
		return dataAbilitazioneIsvap;
	}

	public void setDataAbilitazioneIsvap(DateType dataAbilitazioneIsvap) {
		this.dataAbilitazioneIsvap = dataAbilitazioneIsvap;
	}

	public StringType getNumeroIscrizioneIsvap() {
		return numeroIscrizioneIsvap;
	}

	public void setNumeroIscrizioneIsvap(StringType numeroIscrizioneIsvap) {
		this.numeroIscrizioneIsvap = numeroIscrizioneIsvap;
	}

	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	

}
