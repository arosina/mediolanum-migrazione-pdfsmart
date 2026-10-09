package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class InfoSocietarieModel extends CommandDataModel  {
	
	private StringType  partitaIVA			= new StringType();
	private StringType  codiceFiscale		= new StringType();
	private StringType  reaPr				= new StringType();
	private IntegerType reaLeg				= new IntegerType();
	private DateType	dataIscrizioneREA	= new DateType();
	private IntegerType nDipendenti			= new IntegerType();
	private StringType  legRapCognome		= new StringType();
	private StringType  legRapNome			= new StringType();
	private StringType  legRapCodiceCliente	= new StringType();	
	private StringType  legCodiceFiscale	= new StringType();

	private StringType  utileUltimoEsercizioNonDisponibile	= new StringType();
	private StringType  utileUltimoEsercizio				= new StringType();
	private StringType  perditaUltimoEsercizio				= new StringType();
	private StringType  utilePenultimoEsercizio				= new StringType();
	private StringType  perditaPenultimoEsercizio			= new StringType();
	private StringType  utileTerzultimoEsercizio			= new StringType();
	private StringType  perditaTerzultimoEsercizio			= new StringType();
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public StringType getNumeroIscrizioneREA() {
		if(!reaPr.isNull()){
			return new StringType(reaPr.toString() + " - "  + String.valueOf( reaLeg.intValue() ) );
		}else{
			return new StringType( String.valueOf( reaLeg.intValue() ) );
		}
	}
	
	public StringType getPartitaIVA() {
		return partitaIVA;
	}
	
	public void setPartitaIVA(StringType partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	
	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public DateType getDataIscrizioneREA() {
		return dataIscrizioneREA;
	}
	
	public void setDataIscrizioneREA(DateType dataIscrizioneREA) {
		this.dataIscrizioneREA = dataIscrizioneREA;
	}
	
	public IntegerType getnDipendenti() {
		return nDipendenti;
	}
	
	public void setnDipendenti(IntegerType nDipendenti) {
		this.nDipendenti = nDipendenti;
	}
	
	public StringType getLegRapCognome() {
		return legRapCognome;
	}
	
	public void setLegRapCognome(StringType legRapCognome) {
		this.legRapCognome = legRapCognome;
	}
	
	public StringType getLegRapNome() {
		return legRapNome;
	}
	
	public void setLegRapNome(StringType legRapNome) {
		this.legRapNome = legRapNome;
	}
	
	public StringType getLegRapCodiceCliente() {
		return legRapCodiceCliente;
	}
	
	public void setLegRapCodiceCliente(StringType legRapCodiceCliente) {
		this.legRapCodiceCliente = legRapCodiceCliente;
	}
	
	public StringType getLegCodiceFiscale() {
		return legCodiceFiscale;
	}
	
	public void setLegCodiceFiscale(StringType legCodiceFiscale) {
		this.legCodiceFiscale = legCodiceFiscale;
	}

	public StringType getReaPr() {
		return reaPr;
	}

	public void setReaPr(StringType reaPr) {
		this.reaPr = reaPr;
	}

	public IntegerType getReaLeg() {
		return reaLeg;
	}

	public void setReaLeg(IntegerType reaLeg) {
		this.reaLeg = reaLeg;
	}

	public StringType getUtileUltimoEsercizioNonDisponibile() {
		return utileUltimoEsercizioNonDisponibile;
	}

	public void setUtileUltimoEsercizioNonDisponibile(StringType utileUltimoEsercizioNonDisponibile) {
		this.utileUltimoEsercizioNonDisponibile = utileUltimoEsercizioNonDisponibile;
	}

	public StringType getUtileUltimoEsercizio() {
		return utileUltimoEsercizio;
	}

	public void setUtileUltimoEsercizio(StringType utileUltimoEsercizio) {
		this.utileUltimoEsercizio = utileUltimoEsercizio;
	}

	public StringType getPerditaUltimoEsercizio() {
		return perditaUltimoEsercizio;
	}

	public void setPerditaUltimoEsercizio(StringType perditaUltimoEsercizio) {
		this.perditaUltimoEsercizio = perditaUltimoEsercizio;
	}

	public StringType getUtilePenultimoEsercizio() {
		return utilePenultimoEsercizio;
	}

	public void setUtilePenultimoEsercizio(StringType utilePenultimoEsercizio) {
		this.utilePenultimoEsercizio = utilePenultimoEsercizio;
	}

	public StringType getPerditaPenultimoEsercizio() {
		return perditaPenultimoEsercizio;
	}

	public void setPerditaPenultimoEsercizio(StringType perditaPenultimoEsercizio) {
		this.perditaPenultimoEsercizio = perditaPenultimoEsercizio;
	}

	public StringType getUtileTerzultimoEsercizio() {
		return utileTerzultimoEsercizio;
	}

	public void setUtileTerzultimoEsercizio(StringType utileTerzultimoEsercizio) {
		this.utileTerzultimoEsercizio = utileTerzultimoEsercizio;
	}

	public StringType getPerditaTerzultimoEsercizio() {
		return perditaTerzultimoEsercizio;
	}

	public void setPerditaTerzultimoEsercizio(StringType perditaTerzultimoEsercizio) {
		this.perditaTerzultimoEsercizio = perditaTerzultimoEsercizio;
	}
	
}
