package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/******************************************************************************/
/******************************************************************************/
public class FondoDispositivaCarrelloModel extends CommandDataModel{
	
	private IntegerType 	progrCatalogoMifid = new IntegerType();				// COD_PROD_MIFID
	private StringType		codProdOrig = new StringType();						// COD_PROD_ORIG  
	private StringType		codOperzProdOrig = new StringType();				// COD_OPERZ_PROD_ORIG (Concatenazione di codProdotto, mod sott, classe 
	private StringType		divisa = new StringType();							// COD_DIVI
	
	private StringType		segnoOperazione = new StringType();					// COD_SEGNO_IMP "+","-"
	private DoubleType		importoOperazione = new DoubleType();				// IMP

	private StringType		tipoProfiloFondo = new StringType(); 				// COD_CATEG_FONDO AZ=Azionario, OB=Obbligazionario, null=Nessuno
	private BooleanType		isRimborsoTotale = new BooleanType();				// FLG_RIMBO_TOT

	private StringType		destinazioneFondoBigChance = new StringType(); 		// COD_TIPO_FONDO_BIGCH P=Partenza, A=Arrivo
	private DoubleType		importoInvestimentoBigChance = new DoubleType();	// IMP_INVEST_BIGCH

	private StringType		destinazioneFondoIIS = new StringType(); 			// COD_TIPO_FONDO_IIS P=Partenza, A=Arrivo
	private DoubleType		importoInvestimentoIIS = new DoubleType();			// IMP_INVEST_IIS
	
	private DoubleType		importoInvestimentoDoubleChance = new DoubleType();	// IMP_INVEST_DBCHANCE

	private DoubleType		obiettivoInvestimento = new DoubleType();			// IMP_VERSMT_TOT_PIANO
	private DoubleType		versamentoIniziale = new DoubleType();				// IMP_VERSMT_INIZ_PIANO
	private IntegerType		frequenzaVersamenti = new IntegerType();			// COD_FREQ_PIANO
	private IntegerType		durataVersamenti = new IntegerType();				// QTA_RATE_PIANO
	
	private DoubleType		importoOperazioneAggiuntiva = new DoubleType();		// IMP_OPERZ_AGGIU
	private BooleanType		isOperazioneStandAlone = new BooleanType();			// FLG_OPERZ_STANDLN

	private StringType		codProfiloPartenza = new StringType();				// COD_TIPO_PROFL_INVEST_PARTZA
	private StringType		codProfiloDestinazione = new StringType();			// COD_TIPO_PROFL_INVEST_DEST
	private DoubleType		percentualeOperazione = new DoubleType();			// PRC_RIPART_FONDO	
	
	private IntegerType 	codTipoOperazione = new IntegerType();				// COD_OPERZ_PROD_CARL
	private DoubleType		quantita = new DoubleType();						// QTA_OPERZ_PROD_CARL 
	
	public StringType getCodProdOrig() {
		return codProdOrig;
	}

	public void setCodProdOrig(StringType codProdOrig) {
		this.codProdOrig = codProdOrig;
	}

	public StringType getCodOperzProdOrig() {
		return codOperzProdOrig;
	}

	public void setCodOperzProdOrig(StringType codOperzProdOrig) {
		this.codOperzProdOrig = codOperzProdOrig;
	}

	public StringType getDivisa() {
		return divisa;
	}

	public void setDivisa(StringType divisa) {
		this.divisa = divisa;
	}

	public StringType getSegnoOperazione() {
		return segnoOperazione;
	}

	public void setSegnoOperazione(StringType segnoOperazione) {
		this.segnoOperazione = segnoOperazione;
	}

	public DoubleType getImportoOperazione() {
		return importoOperazione;
	}

	public void setImportoOperazione(DoubleType importoOperazione) {
		this.importoOperazione = importoOperazione;
	}

	public StringType getTipoProfiloFondo() {
		return tipoProfiloFondo;
	}

	public void setTipoProfiloFondo(StringType tipoProfiloFondo) {
		this.tipoProfiloFondo = tipoProfiloFondo;
	}

	public BooleanType getIsRimborsoTotale() {
		return isRimborsoTotale;
	}

	public void setIsRimborsoTotale(BooleanType isRimborsoTotale) {
		this.isRimborsoTotale = isRimborsoTotale;
	}

	public StringType getDestinazioneFondoBigChance() {
		return destinazioneFondoBigChance;
	}

	public void setDestinazioneFondoBigChance(StringType destinazioneFondoBigChance) {
		this.destinazioneFondoBigChance = destinazioneFondoBigChance;
	}

	public DoubleType getImportoInvestimentoBigChance() {
		return importoInvestimentoBigChance;
	}

	public void setImportoInvestimentoBigChance(DoubleType importoInvestimentoBigChance) {
		this.importoInvestimentoBigChance = importoInvestimentoBigChance;
	}

	public StringType getDestinazioneFondoIIS() {
		return destinazioneFondoIIS;
	}

	public void setDestinazioneFondoIIS(StringType destinazioneFondoIIS) {
		this.destinazioneFondoIIS = destinazioneFondoIIS;
	}

	public DoubleType getImportoInvestimentoIIS() {
		return importoInvestimentoIIS;
	}

	public void setImportoInvestimentoIIS(DoubleType importoInvestimentoIIS) {
		this.importoInvestimentoIIS = importoInvestimentoIIS;
	}

	public DoubleType getImportoInvestimentoDoubleChance() {
		return importoInvestimentoDoubleChance;
	}

	public void setImportoInvestimentoDoubleChance(DoubleType importoInvestimentoDoubleChance) {
		this.importoInvestimentoDoubleChance = importoInvestimentoDoubleChance;
	}

	public DoubleType getObiettivoInvestimento() {
		return obiettivoInvestimento;
	}

	public void setObiettivoInvestimento(DoubleType obiettivoInvestimento) {
		this.obiettivoInvestimento = obiettivoInvestimento;
	}

	public DoubleType getVersamentoIniziale() {
		return versamentoIniziale;
	}

	public void setVersamentoIniziale(DoubleType versamentoIniziale) {
		this.versamentoIniziale = versamentoIniziale;
	}

	public IntegerType getFrequenzaVersamenti() {
		return frequenzaVersamenti;
	}

	public void setFrequenzaVersamenti(IntegerType frequenzaVersamenti) {
		this.frequenzaVersamenti = frequenzaVersamenti;
	}

	public IntegerType getDurataVersamenti() {
		return durataVersamenti;
	}

	public void setDurataVersamenti(IntegerType durataVersamenti) {
		this.durataVersamenti = durataVersamenti;
	}

	public IntegerType getProgrCatalogoMifid() {
		return progrCatalogoMifid;
	}

	public void setProgrCatalogoMifid(IntegerType progrCatalogoMifid) {
		this.progrCatalogoMifid = progrCatalogoMifid;
	}

	public DoubleType getImportoOperazioneAggiuntiva() {
		return importoOperazioneAggiuntiva;
	}

	public void setImportoOperazioneAggiuntiva(DoubleType importoOperazioneAggiuntiva) {
		this.importoOperazioneAggiuntiva = importoOperazioneAggiuntiva;
	}

	public BooleanType getIsOperazioneStandAlone() {
		return isOperazioneStandAlone;
	}

	public void setIsOperazioneStandAlone(BooleanType isOperazioneStandAlone) {
		this.isOperazioneStandAlone = isOperazioneStandAlone;
	}

	public StringType getCodProfiloPartenza() {
		return codProfiloPartenza;
	}

	public void setCodProfiloPartenza(StringType codProfiloPartenza) {
		this.codProfiloPartenza = codProfiloPartenza;
	}

	public StringType getCodProfiloDestinazione() {
		return codProfiloDestinazione;
	}

	public void setCodProfiloDestinazione(StringType codProfiloDestinazione) {
		this.codProfiloDestinazione = codProfiloDestinazione;
	}

	public DoubleType getPercentualeOperazione() {
		return percentualeOperazione;
	}

	public void setPercentualeOperazione(DoubleType percentualeOperazione) {
		this.percentualeOperazione = percentualeOperazione;
	}

	public IntegerType getCodTipoOperazione() {
		return codTipoOperazione;
	}

	public void setCodTipoOperazione(IntegerType codTipoOperazione) {
		this.codTipoOperazione = codTipoOperazione;
	}

	public DoubleType getQuantita() {
		return quantita;
	}

	public void setQuantita(DoubleType quantita) {
		this.quantita = quantita;
	}	
}
