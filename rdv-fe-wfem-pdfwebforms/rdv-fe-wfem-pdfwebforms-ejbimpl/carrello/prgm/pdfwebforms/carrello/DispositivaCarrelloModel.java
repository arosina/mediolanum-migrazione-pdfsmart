package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DispositivaCarrelloModel extends CommandDataModel{

	private IntegerType 	idCarrello = new IntegerType();
	private IntegerType 	idDispCarrello = new IntegerType();
	private IntegerType 	idDispCollegataCarrello = new IntegerType();

	private StringType		codProdottoDispositiva = new StringType(); 		// PROD_C_PROD
	private StringType		numeroContratto = new StringType(); 			// CONTRATTO
	private StringType		tipoAdesione = new StringType(); 				// COD_TIPO_ADES_FONDO "PIC", "PAC"
	
	private BooleanType		isRimborsoTotale = new BooleanType();			// FLG_RIMBO_TOT
	private BooleanType		isRimborsoProgrammato= new BooleanType();		// FLG_RIMBO_PROG_FONDO

	private DoubleType		obiettivoInvestimentoPac = new DoubleType();	// IMP_PIANO_PAC
	private DoubleType		versamentoInizialePac	= new DoubleType();		// IMP_PREM_INIZ_PAC
	private IntegerType		frequenzaVersamentiPac	= new IntegerType();	// COD_FREQ_PREMI_SUCVI
	private IntegerType		durataVersamentiPac	= new IntegerType();		// QTA_DURA_PREMI_SUCVI
	
	private IntegerType 	codPianoPacFondiDiTerzi = new IntegerType();	// COD_PIANO_PAC_ISIN

	private DoubleType		importoRata	= new DoubleType();					// IMP_RATA
	private IntegerType 	numeroRate = new IntegerType();					// QTA_RATE
	
	private IntegerType 	codTipoDispositiva = new IntegerType();			// COD_OPERZ_ORD_CARL
	
	private BooleanType		isRipartizioneDaContratto = new BooleanType();	// FLG_RIPART_CONTR
	private BooleanType		isRiallocazioneCapitaleMaturato = new BooleanType();	//FLG_RIALLOC_CAPIT_MATUR
	
	private BooleanType		isVariazioneDataValuta = new BooleanType();			//FLG_VARZ_DAT_VALU
	private BooleanType		isVariazioneCC = new BooleanType();					//FLG_VARZ_CC
	private BooleanType		isModificaRivalutazioneISTAT = new BooleanType();	//FLG_MODIF_RIVA_ISTAT	
	private StringType		tipoVariazioneStatoSDD = new StringType(); 			// COD_TIPO_VARZ_STATO_SDD
	private IntegerType 	numAnniNuovaDurata = new IntegerType();				// NUM_ANNI_NUOVA_DURA_CONTR
	private DoubleType		importoAmpliamento	= new DoubleType();				// IMP_AMPL_PREM_ANNUO
	private DoubleType		importoVersamento	= new DoubleType();				// IMP_VERSMT_AGGIU
	private StringType		tipoAmpliamento = new StringType(); 				// COD_TIPO_AMPL
	private StringType		codDataValutaAddebito = new StringType(); 			// COD_DAT_VALU_ADDEB
	
	private ListType		sottoscrittori = new ListType(SottoscrittoreCarrelloModel.class);
	private	ListType		fondi = new ListType(FondoDispositivaCarrelloModel.class);
	
	private DispositivaCarrelloModel dispositivaCarrelloCollegata = null;
	
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public StringType getTipoAdesione() {
		return tipoAdesione;
	}
	public void setTipoAdesione(StringType tipoAdesione) {
		this.tipoAdesione = tipoAdesione;
	}
	public BooleanType getIsRimborsoTotale() {
		return isRimborsoTotale;
	}
	public void setIsRimborsoTotale(BooleanType isRimborsoTotale) {
		this.isRimborsoTotale = isRimborsoTotale;
	}
	public BooleanType getIsRimborsoProgrammato() {
		return isRimborsoProgrammato;
	}
	public void setIsRimborsoProgrammato(BooleanType isRimborsoProgrammato) {
		this.isRimborsoProgrammato = isRimborsoProgrammato;
	}
	public DoubleType getObiettivoInvestimentoPac() {
		return obiettivoInvestimentoPac;
	}
	public void setObiettivoInvestimentoPac(DoubleType obiettivoInvestimentoPac) {
		this.obiettivoInvestimentoPac = obiettivoInvestimentoPac;
	}
	public DoubleType getVersamentoInizialePac() {
		return versamentoInizialePac;
	}
	public void setVersamentoInizialePac(DoubleType versamentoInizialePac) {
		this.versamentoInizialePac = versamentoInizialePac;
	}
	public IntegerType getFrequenzaVersamentiPac() {
		return frequenzaVersamentiPac;
	}
	public void setFrequenzaVersamentiPac(IntegerType frequenzaVersamentiPac) {
		this.frequenzaVersamentiPac = frequenzaVersamentiPac;
	}
	public IntegerType getDurataVersamentiPac() {
		return durataVersamentiPac;
	}
	public void setDurataVersamentiPac(IntegerType durataVersamentiPac) {
		this.durataVersamentiPac = durataVersamentiPac;
	}
	public IntegerType getCodPianoPacFondiDiTerzi() {
		return codPianoPacFondiDiTerzi;
	}
	public void setCodPianoPacFondiDiTerzi(IntegerType codPianoPacFondiDiTerzi) {
		this.codPianoPacFondiDiTerzi = codPianoPacFondiDiTerzi;
	}
	public DoubleType getImportoRata() {
		return importoRata;
	}
	public void setImportoRata(DoubleType importoRata) {
		this.importoRata = importoRata;
	}
	public IntegerType getNumeroRate() {
		return numeroRate;
	}
	public void setNumeroRate(IntegerType numeroRate) {
		this.numeroRate = numeroRate;
	}

	public ListType getFondi() {
		return fondi;
	}
	public void setFondi(ListType fondi) {
		this.fondi = fondi;
	}
	public IntegerType getIdCarrello() {
		return idCarrello;
	}
	public void setIdCarrello(IntegerType idCarrello) {
		this.idCarrello = idCarrello;
	}
	public IntegerType getIdDispCarrello() {
		return idDispCarrello;
	}
	public void setIdDispCarrello(IntegerType idDispCarrello) {
		this.idDispCarrello = idDispCarrello;
	}
	public ListType getSottoscrittori() {
		return sottoscrittori;
	}
	public void setSottoscrittori(ListType sottoscrittori) {
		this.sottoscrittori = sottoscrittori;
	}
	public StringType getCodProdottoDispositiva() {
		return codProdottoDispositiva;
	}
	public void setCodProdottoDispositiva(StringType codProdottoDispositiva) {
		this.codProdottoDispositiva = codProdottoDispositiva;
	}
	public DispositivaCarrelloModel getDispositivaCarrelloCollegata() {
		return dispositivaCarrelloCollegata;
	}
	public void setDispositivaCarrelloCollegata(DispositivaCarrelloModel dispositivaCarrelloCollegata) {
		this.dispositivaCarrelloCollegata = dispositivaCarrelloCollegata;
	}
	public IntegerType getIdDispCollegataCarrello() {
		return idDispCollegataCarrello;
	}
	public void setIdDispCollegataCarrello(IntegerType idDispCollegataCarrello) {
		this.idDispCollegataCarrello = idDispCollegataCarrello;
	}
	public IntegerType getCodTipoDispositiva() {
		return codTipoDispositiva;
	}
	public void setCodTipoDispositiva(IntegerType codTipoDispositiva) {
		this.codTipoDispositiva = codTipoDispositiva;
	}
	public BooleanType getIsRipartizioneDaContratto() {
		return isRipartizioneDaContratto;
	}
	public void setIsRipartizioneDaContratto(BooleanType isRipartizioneDaContratto) {
		this.isRipartizioneDaContratto = isRipartizioneDaContratto;
	}
	public BooleanType getIsRiallocazioneCapitaleMaturato() {
		return isRiallocazioneCapitaleMaturato;
	}
	public void setIsRiallocazioneCapitaleMaturato(BooleanType isRiallocazioneCapitaleMaturato) {
		this.isRiallocazioneCapitaleMaturato = isRiallocazioneCapitaleMaturato;
	}
	public BooleanType getIsVariazioneDataValuta() {
		return isVariazioneDataValuta;
	}
	public void setIsVariazioneDataValuta(BooleanType isVariazioneDataValuta) {
		this.isVariazioneDataValuta = isVariazioneDataValuta;
	}
	public BooleanType getIsVariazioneCC() {
		return isVariazioneCC;
	}
	public void setIsVariazioneCC(BooleanType isVariazioneCC) {
		this.isVariazioneCC = isVariazioneCC;
	}
	public BooleanType getIsModificaRivalutazioneISTAT() {
		return isModificaRivalutazioneISTAT;
	}
	public void setIsModificaRivalutazioneISTAT(BooleanType isModificaRivalutazioneISTAT) {
		this.isModificaRivalutazioneISTAT = isModificaRivalutazioneISTAT;
	}
	public StringType getTipoVariazioneStatoSDD() {
		return tipoVariazioneStatoSDD;
	}
	public void setTipoVariazioneStatoSDD(StringType tipoVariazioneStatoSDD) {
		this.tipoVariazioneStatoSDD = tipoVariazioneStatoSDD;
	}
	public IntegerType getNumAnniNuovaDurata() {
		return numAnniNuovaDurata;
	}
	public void setNumAnniNuovaDurata(IntegerType numAnniNuovaDurata) {
		this.numAnniNuovaDurata = numAnniNuovaDurata;
	}
	public DoubleType getImportoAmpliamento() {
		return importoAmpliamento;
	}
	public void setImportoAmpliamento(DoubleType importoAmpliamento) {
		this.importoAmpliamento = importoAmpliamento;
	}
	public DoubleType getImportoVersamento() {
		return importoVersamento;
	}
	public void setImportoVersamento(DoubleType importoVersamento) {
		this.importoVersamento = importoVersamento;
	}
	public StringType getTipoAmpliamento() {
		return tipoAmpliamento;
	}
	public void setTipoAmpliamento(StringType tipoAmpliamento) {
		this.tipoAmpliamento = tipoAmpliamento;
	}
	public StringType getCodDataValutaAddebito() {
		return codDataValutaAddebito;
	}
	public void setCodDataValutaAddebito(StringType codDataValutaAddebito) {
		this.codDataValutaAddebito = codDataValutaAddebito;
	}
}
