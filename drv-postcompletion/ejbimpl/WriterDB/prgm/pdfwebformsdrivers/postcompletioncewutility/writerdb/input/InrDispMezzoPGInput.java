package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispMezzoPGInput extends CommandDataModel
{

  private StringType codDisposizione			= new StringType();
  private StringType codDispMezzoPG				= new StringType();
  private StringType codAgente					= new StringType();
  private StringType codRete					= new StringType();
  private StringType tipoStatoDisposizione		= new StringType();
  private StringType codAbi						= new StringType();
  private StringType codCab						= new StringType();
  private StringType codDivisa					= new StringType();
  private DoubleType importoDivisa				= new DoubleType();
  private DateType dataValuta					= new DateType();
  private StringType descrIntestatario			= new StringType();
  private StringType codCC						= new StringType();
  private StringType numAssgn					= new StringType();
  private StringType nota						= new StringType();
  private DateType dataVariazione				= new DateType();
  private StringType serverReplica				= new StringType();
  private StringType descrBanca					= new StringType();
  private StringType codOperazione				= new StringType();
  private StringType codTipoMezzoPG				= new StringType();
  private StringType flagCCApertura				= new StringType();
  private StringType codPotenziale				= new StringType();
  private StringType cognomeIntestatario		= new StringType();
  private StringType nomeIntestatario			= new StringType();
  private StringType filiale					= new StringType();
  private StringType citta						= new StringType();
  private StringType descrFondo					= new StringType();
  private StringType codFondo					= new StringType();
  private StringType codCC1						= new StringType();
  private DoubleType piTranche					= new DoubleType();
  private IntegerType numeroRate				= new IntegerType();
  private DoubleType importoRata				= new DoubleType();
  private DoubleType numeroQuote				= new DoubleType();
  private DateType dataSospensione				= new DateType();
  private StringType tipoModalitaPagamento		= new StringType();
  private StringType tipoAzioneModalitaPagamento	= new StringType();
  private StringType codDisposizioneCCApertura		= new StringType();
  private BooleanType flagAccreditoStipendio		= new BooleanType();
  private StringType prefissoIban 					= new StringType();

  /*********************************************************************************/
  /*********************************************************************************/
  public StringType getPaese(){
	if (prefissoIban == null || prefissoIban.isNull())
		return new StringType();
    return new StringType(prefissoIban.toString().substring(0,2));
  }
  /*********************************************************************************/
  /*********************************************************************************/
 /* public StringType getCin(){
	if (prefissoIban == null || prefissoIban.isNull())
		return new StringType();
	return new StringType(prefissoIban.toString().substring(4,5));
  }*/
  /*********************************************************************************/
  /*********************************************************************************/
/*  public StringType getCinIban(){
	if (prefissoIban == null || prefissoIban.isNull())
		return new StringType();
	return new StringType(prefissoIban.toString().substring(2,4));
  }  */

  public StringType getCitta()
  {
    return citta;
  }

  public StringType getCodAbi()
  {
    return codAbi;
  }

  public StringType getCodAgente()
  {
    return codAgente;
  }

  public StringType getCodCab()
  {
    return codCab;
  }

  public StringType getCodCC()
  {
    return codCC;
  }

  public StringType getCodCC1()
  {
    return codCC1;
  }

  public StringType getCodDispMezzoPG()
  {
    return codDispMezzoPG;
  }

  public StringType getCodDisposizione()
  {
    return codDisposizione;
  }

  public StringType getCodDisposizioneCCApertura()
  {
    return codDisposizioneCCApertura;
  }

  public StringType getCodDivisa()
  {
    return codDivisa;
  }

  public StringType getCodFondo()
  {
    return codFondo;
  }

  public StringType getCodOperazione()
  {
    return codOperazione;
  }

  public StringType getCodPotenziale()
  {
    return codPotenziale;
  }

  public StringType getCodRete()
  {
    return codRete;
  }

  public StringType getCodTipoMezzoPG()
  {
    return codTipoMezzoPG;
  }

  public StringType getCognomeIntestatario()
  {
    return cognomeIntestatario;
  }

  public DateType getDataSospensione()
  {
    return dataSospensione;
  }

  public DateType getDataValuta()
  {
    return dataValuta;
  }

  public DateType getDataVariazione()
  {
    return dataVariazione;
  }

  public StringType getDescrBanca()
  {
    return descrBanca;
  }

  public StringType getDescrFondo()
  {
    return descrFondo;
  }

  public StringType getDescrIntestatario()
  {
    return descrIntestatario;
  }

  public StringType getFiliale()
  {
    return filiale;
  }

  public StringType getFlagCCApertura()
  {
    return flagCCApertura;
  }

  public DoubleType getImportoDivisa()
  {
    return importoDivisa;
  }

  public DoubleType getImportoRata()
  {
    return importoRata;
  }

  public StringType getNomeIntestatario()
  {
    return nomeIntestatario;
  }

  public StringType getNota()
  {
    return nota;
  }

  public StringType getNumAssgn()
  {
    return numAssgn;
  }

  public DoubleType getNumeroQuote()
  {
    return numeroQuote;
  }

  public IntegerType getNumeroRate()
  {
    return numeroRate;
  }

  public DoubleType getPiTranche()
  {
    return piTranche;
  }

  public StringType getServerReplica()
  {
    return serverReplica;
  }

  public StringType getTipoAzioneModalitaPagamento()
  {
    return tipoAzioneModalitaPagamento;
  }

  public StringType getTipoModalitaPagamento()
  {
    return tipoModalitaPagamento;
  }

  public StringType getTipoStatoDisposizione()
  {
    return tipoStatoDisposizione;
  }

  public void setCitta(StringType citta)
  {
    this.citta = citta;
  }

  public void setCodAbi(StringType codAbi)
  {
    this.codAbi = codAbi;
  }

  public void setCodAgente(StringType codAgente)
  {
    this.codAgente = codAgente;
  }

  public void setCodCab(StringType codCab)
  {
    this.codCab = codCab;
  }

  public void setCodCC(StringType codCC)
  {
    this.codCC = codCC;
  }

  public void setCodCC1(StringType codCC1)
  {
    this.codCC1 = codCC1;
  }

  public void setCodDispMezzoPG(StringType codDispMezzoPG)
  {
    this.codDispMezzoPG = codDispMezzoPG;
  }

  public void setCodDisposizione(StringType codDisposizione)
  {
    this.codDisposizione = codDisposizione;
  }

  public void setCodDisposizioneCCApertura(StringType codDisposizioneCCApertura)
  {
    this.codDisposizioneCCApertura = codDisposizioneCCApertura;
  }

  public void setCodDivisa(StringType codDivisa)
  {
    this.codDivisa = codDivisa;
  }

  public void setCodFondo(StringType codFondo)
  {
    this.codFondo = codFondo;
  }

  public void setCodOperazione(StringType codOperazione)
  {
    this.codOperazione = codOperazione;
  }

  public void setCodPotenziale(StringType codPotenziale)
  {
    this.codPotenziale = codPotenziale;
  }

  public void setCodRete(StringType codRete)
  {
    this.codRete = codRete;
  }

  public void setCodTipoMezzoPG(StringType codTipoMezzoPG)
  {
    this.codTipoMezzoPG = codTipoMezzoPG;
  }

  public void setCognomeIntestatario(StringType cognomeIntestatario)
  {
    this.cognomeIntestatario = cognomeIntestatario;
  }

  public void setDataSospensione(DateType dataSospensione)
  {
    this.dataSospensione = dataSospensione;
  }

  public void setDataValuta(DateType dataValuta)
  {
    this.dataValuta = dataValuta;
  }

  public void setDataVariazione(DateType dataVariazione)
  {
    this.dataVariazione = dataVariazione;
  }

  public void setDescrBanca(StringType descrBanca)
  {
    this.descrBanca = descrBanca;
  }

  public void setDescrFondo(StringType descrFondo)
  {
    this.descrFondo = descrFondo;
  }

  public void setDescrIntestatario(StringType descrIntestatario)
  {
    this.descrIntestatario = descrIntestatario;
  }

  public void setFiliale(StringType filiale)
  {
    this.filiale = filiale;
  }

  public void setFlagCCApertura(StringType flagCCApertura)
  {
    this.flagCCApertura = flagCCApertura;
  }

  public void setImportoDivisa(DoubleType importoDivisa)
  {
    this.importoDivisa = importoDivisa;
  }

  public void setImportoRata(DoubleType importoRata)
  {
    this.importoRata = importoRata;
  }

  public void setNomeIntestatario(StringType nomeIntestatario)
  {
    this.nomeIntestatario = nomeIntestatario;
  }

  public void setNota(StringType nota)
  {
    this.nota = nota;
  }

  public void setNumAssgn(StringType numAssgn)
  {
    this.numAssgn = numAssgn;
  }

  public void setNumeroQuote(DoubleType numeroQuote)
  {
    this.numeroQuote = numeroQuote;
  }

  public void setNumeroRate(IntegerType numeroRate)
  {
    this.numeroRate = numeroRate;
  }

  public void setPiTranche(DoubleType piTranche)
  {
    this.piTranche = piTranche;
  }

  public void setServerReplica(StringType serverReplica)
  {
    this.serverReplica = serverReplica;
  }

  public void setTipoAzioneModalitaPagamento(StringType tipoAzioneModalitaPagamento)
  {
    this.tipoAzioneModalitaPagamento = tipoAzioneModalitaPagamento;
  }

  public void setTipoModalitaPagamento(StringType tipoModalitaPagamento)
  {
    this.tipoModalitaPagamento = tipoModalitaPagamento;
  }

  public void setTipoStatoDisposizione(StringType tipoStatoDisposizione)
  {
    this.tipoStatoDisposizione = tipoStatoDisposizione;
  }

  public BooleanType getFlagAccreditoStipendio()
  {
    return flagAccreditoStipendio;
  }

  public void setFlagAccreditoStipendio(BooleanType flagAccreditoStipendio)
  {
    this.flagAccreditoStipendio = flagAccreditoStipendio;
  }

public StringType getPrefissoIban() {
	return prefissoIban;
}

public void setPrefissoIban(StringType prefissoIban) {
	this.prefissoIban = prefissoIban;
}

}
