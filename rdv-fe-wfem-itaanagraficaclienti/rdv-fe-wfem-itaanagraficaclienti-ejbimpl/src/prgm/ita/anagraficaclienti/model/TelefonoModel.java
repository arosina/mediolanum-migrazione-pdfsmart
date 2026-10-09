package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class TelefonoModel extends AbstractSectionModel{

	private boolean existOnDB = false;
	
	private StringType  	codAgente   	= new StringType();
	private StringType  	codPotenziale 	= new StringType();
	private StringType 		codRete			= new StringType();
	private StringType  	stato 			= new StringType();
	private StringType  	statoProposta	= new StringType();
	private IntegerType 	progressivo 	= new IntegerType();
	private StringType  	serverReplica	= new StringType();    
	private TimestampType 	dataVariazione  = new TimestampType();

	private IntegerType	progressivoProposta = new IntegerType();

	private StringType 	tipoTelefono = new StringType();
	private StringType 	prefissoInternazionale = new StringType();
	private StringType 	prefisso = new StringType();
	private StringType 	numeroTelefono = new StringType();
	private StringType 	descrizioneTelefono = new StringType();
	private StringType 	reperibileOraDa = new StringType();
	private StringType 	reperibileOraA = new StringType();
	private DateType 	reperibileGiornoDa = new DateType();
	private DateType 	reperibileGiornoA = new DateType();
	private StringType 	note = new StringType();
	private StringType 	noteNonReperibilita = new StringType();

	private TimestampType 	reperibileOraDaDB = new TimestampType();
	private TimestampType 	reperibileOraADB = new TimestampType();
	
	private StringType  	statoConfermato = new StringType();
	
	private DatiApplicativiModel datiApplicativi = new DatiApplicativiModel();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefonoModel(){
		addCodDescField("tipoTelefono","TIPI_TELEFONO");
		
		CodDescDataList ore = new CodDescDataList();
		CodDescData ora = null;
		for(int i=0;i<Costanti.ORE.length;i++){
			ora = new CodDescData();
			ora.setCod(Costanti.ORE[i]); ora.setDescr(Costanti.ORE[i]);
			ore.addCodDescData(ora);
		}
		addCodDescField("reperibileOraDa",ore);
		addCodDescField("reperibileOraA",ore);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isTelefonoCellulare() {
		if( tipoTelefono.equals(Costanti.COD_TELEFONO_CELLULARE) )
			return true;
		
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isTelefonoFax() {
		if( tipoTelefono.equals(Costanti.COD_TELEFONO_FAX) )
			return true;
		
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isTelefonoSpecifico(StringType tipoTelefono){
		if(tipoTelefono.equals(Costanti.COD_TELEFONO_RESIDENZA) ||
		   tipoTelefono.equals(Costanti.COD_TELEFONO_DOMICILIO) ||
		   tipoTelefono.equals(Costanti.COD_TELEFONO_FAX) ||
		   tipoTelefono.equals(Costanti.COD_TELEFONO_CELLULARE))
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getNumeroTelefonoCompleto() {
		if(getNumeroTelefono().isNull())
			return new StringType();
		String result = "";
		if(!getPrefissoInternazionale().isNull())
			result+=getPrefissoInternazionale().toString();
		if(!getPrefisso().isNull())
			result+=getPrefisso().toString();
		result+=getNumeroTelefono().toString();
		return new StringType(result);
	}
	
	public StringType getTipoTelefono() {
		return tipoTelefono;
	}

	public void setTipoTelefono(StringType tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}

	public StringType getNumeroTelefono() {
		return numeroTelefono;
	}

	public void setNumeroTelefono(StringType numeroTelefono) {
		this.numeroTelefono = numeroTelefono;
	}

	public IntegerType getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}

	public IntegerType getProgressivoProposta() {
		return progressivoProposta;
	}

	public void setProgressivoProposta(IntegerType progressivoProposta) {
		this.progressivoProposta = progressivoProposta;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getStato() {
		return stato;
	}

	public StringType getStatoProposta() {
		return statoProposta;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setStato(StringType stato) {
		this.stato = stato;
	}

	public void setStatoProposta(StringType statoProposta) {
		this.statoProposta = statoProposta;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}

	public TimestampType getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(TimestampType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public StringType getDescrizioneTelefono() {
		return descrizioneTelefono;
	}

	public StringType getNote() {
		return note;
	}

	public StringType getNoteNonReperibilita() {
		return noteNonReperibilita;
	}

	public StringType getPrefisso() {
		return prefisso;
	}

	public StringType getPrefissoInternazionale() {
		return prefissoInternazionale;
	}

	public DateType getReperibileGiornoA() {
		return reperibileGiornoA;
	}

	public DateType getReperibileGiornoDa() {
		return reperibileGiornoDa;
	}

	public void setDescrizioneTelefono(StringType descrizioneTelefono) {
		this.descrizioneTelefono = descrizioneTelefono;
	}

	public void setNote(StringType note) {
		this.note = note;
	}

	public void setNoteNonReperibilita(StringType noteNonReperibilita) {
		this.noteNonReperibilita = noteNonReperibilita;
	}

	public void setPrefisso(StringType prefisso) {
		this.prefisso = prefisso;
	}

	public void setPrefissoInternazionale(StringType prefissoInternazionale) {
		this.prefissoInternazionale = prefissoInternazionale;
	}

	public void setReperibileGiornoA(DateType reperibileGiornoA) {
		this.reperibileGiornoA = reperibileGiornoA;
	}

	public void setReperibileGiornoDa(DateType reperibileGiornoDa) {
		this.reperibileGiornoDa = reperibileGiornoDa;
	}

	public StringType getReperibileOraA() {
		return reperibileOraA;
	}

	public TimestampType getReperibileOraADB() {
		return reperibileOraADB;
	}

	public StringType getReperibileOraDa() {
		return reperibileOraDa;
	}

	public TimestampType getReperibileOraDaDB() {
		return reperibileOraDaDB;
	}

	public void setReperibileOraA(StringType reperibileOraA) {
		this.reperibileOraA = reperibileOraA;
	}

	public void setReperibileOraADB(TimestampType reperibileOraADB) {
		this.reperibileOraADB = reperibileOraADB;
	}

	public void setReperibileOraDa(StringType reperibileOraDa) {
		this.reperibileOraDa = reperibileOraDa;
	}

	public void setReperibileOraDaDB(TimestampType reperibileOraDaDB) {
		this.reperibileOraDaDB = reperibileOraDaDB;
	}

	public DatiApplicativiModel getDatiApplicativi() {
		return datiApplicativi;
	}

	public void setDatiApplicativi(DatiApplicativiModel datiApplicativi) {
		this.datiApplicativi = datiApplicativi;
	}

	public StringType getStatoConfermato() {
		return statoConfermato;
	}

	public void setStatoConfermato(StringType statoConfermato) {
		this.statoConfermato = statoConfermato;
	}

	public boolean isExistOnDB() {
		return existOnDB;
	}

	public void setExistOnDB(boolean existOnDB) {
		this.existOnDB = existOnDB;
	}
}
