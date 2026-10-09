package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
/*
 * Input servizio CtrlAdeguatezza
 */
public class InputCtrlAdeguatezzaModel extends CommandDataModel  implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDossn   : ndg del cliente
	 * ndgTempn   : ndg temporaneo del cliente
	 * canVend    : canale di vendita
	 * country    : sigla paese 3 caratteri usata per log
	 * username   : sigla utente 10 caratteri usata per log
	 * dataRif    : data sottoscrizione operazione 
	 * segnOrd    : tipo operazione
	 * numOrdg    : contatore operazioni
	 * tipStruStr : tipo strumento
	 * sotTipoStr : sottotipo strumento
	 * obbInveStr : obbiettivo di investimento
	 * riscStrStr : profilo di rischio
	 * obbTempStr : orizzonte temporale
	 * modVersStr : modalità di versamento
	 * compAziStr : componente azionaria
	 * codMercStr : mercato di trattazione
	 * obbInveCli : obbiettivo di investimento
	 * obbTempCli : orizzonte temporale
	 * modVersCli : modalità di versamento
	 * sitFinaCli : percentuale rispetto al patrimonio
	 * ctvlord    : controvalore dell'operazione
	 * divisOt    : divisa del controvalore
	 * posDisiCli : possiblita' disinvestimento cliente 
	 * attCons    : ordine in consulenza
	 * titCons    : titolo consulenziabile
	 */
	public StringType ndgDoss1   = new StringType();
	public StringType ndgTemp1   = new StringType();
	public StringType ndgDoss2   = new StringType();
	public StringType ndgTemp2   = new StringType();
	public StringType ndgDoss3   = new StringType();
	public StringType ndgTemp3   = new StringType();
	public StringType ndgDoss4   = new StringType();
	public StringType ndgTemp4   = new StringType();
	private StringType filDoss   = new StringType();
	private StringType contDoss  = new StringType();
	private StringType progDoss  = new StringType();
	private DateType   dataRif    = new DateType();
	private StringType segnOrd    = new StringType();
	private StringType canVend    = new StringType();
	private IntegerType numOrdg   = new IntegerType();
	private StringType tipStruStr = new StringType(); 
	private StringType sotTipoStr = new StringType(); 
	private StringType obbInveStr = new StringType(); 
	private StringType riscStrStr = new StringType(); 
	private StringType obbTempStr = new StringType(); 
	private StringType modVersStr = new StringType(); 
	private StringType compAziStr = new StringType(); 
	private StringType codMercStr = new StringType(); 
	private StringType obbInveCli = new StringType(); 
	private StringType obbTempCli = new StringType(); 
	private StringType modVersCli = new StringType(); 
	private StringType sitFinaCli = new StringType(); 
	private IntegerType ctvlord   = new IntegerType(); 
	private StringType divisOt    = new StringType();
	private StringType country    = new StringType();
	private StringType username   = new StringType();
	private StringType posDisiCli = new StringType();
	private StringType attCons    = new StringType();
	private StringType titCons    = new StringType();

	public StringType getCanVend() {
		return canVend;
	}
	public void setCanVend(StringType canVend) {
		this.canVend = canVend;
	}
	public StringType getCodMercStr() {
		return codMercStr;
	}
	public void setCodMercStr(StringType codMercStr) {
		this.codMercStr = codMercStr;
	}
	public StringType getCompAziStr() {
		return compAziStr;
	}
	public void setCompAziStr(StringType compAziStr) {
		this.compAziStr = compAziStr;
	}
	public IntegerType getCtvlord() {
		return ctvlord;
	}
	public void setCtvlord(IntegerType ctvlord) {
		this.ctvlord = ctvlord;
	}
	public DateType getDataRif() {
		return dataRif;
	}
	public void setDataRif(DateType dataRif) {
		this.dataRif = dataRif;
	}
	public StringType getDivisOt() {
		return divisOt;
	}
	public void setDivisOt(StringType divisOt) {
		this.divisOt = divisOt;
	}
	public StringType getModVersCli() {
		return modVersCli;
	}
	public void setModVersCli(StringType modVersCli) {
		this.modVersCli = modVersCli;
	}
	public StringType getModVersStr() {
		return modVersStr;
	}
	public void setModVersStr(StringType modVersStr) {
		this.modVersStr = modVersStr;
	}
	public StringType getNdgDoss1() {
		return ndgDoss1;
	}
	public void setNdgDoss1(StringType ndgDoss1) {
		this.ndgDoss1 = ndgDoss1;
	}
	public StringType getNdgDoss2() {
		return ndgDoss2;
	}
	public void setNdgDoss2(StringType ndgDoss2) {
		this.ndgDoss2 = ndgDoss2;
	}
	public StringType getNdgDoss3() {
		return ndgDoss3;
	}
	public void setNdgDoss3(StringType ndgDoss3) {
		this.ndgDoss3 = ndgDoss3;
	}
	public StringType getNdgDoss4() {
		return ndgDoss4;
	}
	public void setNdgDoss4(StringType ndgDoss4) {
		this.ndgDoss4 = ndgDoss4;
	}
	public IntegerType getNumOrdg() {
		return numOrdg;
	}
	public void setNumOrdg(IntegerType numOrdg) {
		this.numOrdg = numOrdg;
	}
	public StringType getObbInveCli() {
		return obbInveCli;
	}
	public void setObbInveCli(StringType obbInveCli) {
		this.obbInveCli = obbInveCli;
	}
	public StringType getObbInveStr() {
		return obbInveStr;
	}
	public void setObbInveStr(StringType obbInveStr) {
		this.obbInveStr = obbInveStr;
	}
	public StringType getObbTempCli() {
		return obbTempCli;
	}
	public void setObbTempCli(StringType obbTempCli) {
		this.obbTempCli = obbTempCli;
	}
	public StringType getObbTempStr() {
		return obbTempStr;
	}
	public void setObbTempStr(StringType obbTempStr) {
		this.obbTempStr = obbTempStr;
	}
	public StringType getRiscStrStr() {
		return riscStrStr;
	}
	public void setRiscStrStr(StringType riscStrStr) {
		this.riscStrStr = riscStrStr;
	}
	public StringType getSegnOrd() {
		return segnOrd;
	}
	public void setSegnOrd(StringType segnOrd) {
		this.segnOrd = segnOrd;
	}
	public StringType getSitFinaCli() {
		return sitFinaCli;
	}
	public void setSitFinaCli(StringType sitFinaCli) {
		this.sitFinaCli = sitFinaCli;
	}
	public StringType getSotTipoStr() {
		return sotTipoStr;
	}
	public void setSotTipoStr(StringType sotTipoStr) {
		this.sotTipoStr = sotTipoStr;
	}
	public StringType getTipStruStr() {
		return tipStruStr;
	}
	public void setTipStruStr(StringType tipStruStr) {
		this.tipStruStr = tipStruStr;
	}
	public StringType getCountry() {
		return country;
	}
	public void setCountry(StringType country) {
		this.country = country;
	}
	public StringType getUsername() {
		return username;
	}
	public void setUsername(StringType username) {
		this.username = username;
	}
	public StringType getNdgTemp1() {
		return ndgTemp1;
	}
	public void setNdgTemp1(StringType ndgTemp1) {
		this.ndgTemp1 = ndgTemp1;
	}
	public StringType getNdgTemp2() {
		return ndgTemp2;
	}
	public void setNdgTemp2(StringType ndgTemp2) {
		this.ndgTemp2 = ndgTemp2;
	}
	public StringType getNdgTemp3() {
		return ndgTemp3;
	}
	public void setNdgTemp3(StringType ndgTemp3) {
		this.ndgTemp3 = ndgTemp3;
	}
	public StringType getNdgTemp4() {
		return ndgTemp4;
	}
	public void setNdgTemp4(StringType ndgTemp4) {
		this.ndgTemp4 = ndgTemp4;
	}
	public StringType getContDoss() {
		return contDoss;
	}
	public StringType getFilDoss() {
		return filDoss;
	}
	public StringType getProgDoss() {
		return progDoss;
	}
	public void setContDoss(StringType contDoss) {
		this.contDoss = contDoss;
	}
	public void setFilDoss(StringType filDoss) {
		this.filDoss = filDoss;
	}
	public void setProgDoss(StringType progDoss) {
		this.progDoss = progDoss;
	}
	public StringType getPosDisiCli() {
		return posDisiCli;
	}
	public void setPosDisiCli(StringType posDisiCli) {
		this.posDisiCli = posDisiCli;
	}
	public StringType getAttCons() {
		return attCons;
	}
	public void setAttCons(StringType attCons) {
		this.attCons = attCons;
	}
	public StringType getTitCons() {
		return titCons;
	}
	public void setTitCons(StringType titCons) {
		this.titCons = titCons;
	}
}
