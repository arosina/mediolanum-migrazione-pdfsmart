package prgm.ita.p.dac.model;

import prgm.ita.p.dac.facade.Costanti;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ParamsModel extends CommandDataModel {
	
	private IntegerType verticalVideoHeight = new IntegerType();
	private BooleanType isPritMOM = new BooleanType();

	private StringType	versione 	= new StringType();		// Versione della applicazione
	private IntegerType ufficio 	= new IntegerType(); 	// Codice dell'ufficio dell'utente collegato
	private IntegerType tipoDac 	= new IntegerType();
	private StringType  fnc 		= new StringType();			// Nome della funzione
	private StringType  params 		= new StringType();
	
	// Nomi dei parametri. Devono essere passati in params separati dal carattere underscore '_'
	public static final String smistatore 				= "smistatore"; 		// Letto anche da db
	public static final String mgmPlichi 				= "mgmPlichi"; 			// Letto anche da db
	public static final String ctrlCassette 			= "ctrlCassette"; 		// Letto anche da db
	public static final String noCtrlFirme	 			= "noCtrlFirme"; 		// Letto anche da db
	public static final String noCtrlFirmeAgente		= "noCFAgente"; 		// Letto anche da db
	public static final String autoSpuntata				= "autoSpuntata";
	public static final String servizioFirmePerCC		= "servizioFirmePerCC";
	// Il parametro "speditore" specificato da voce di menù, sovrascrive "smistatore" e "ctrlCassette"
	// sia come parametro da voce di menù che come parametro letto da db.
	// Serve a fare in modo che un uffico definito come "smistatore" possa inviare dac
	// non a partire da documenti spuntati ma normali (ad esempio quando il coding 
	// deve rispedire i resi)
	public static final String speditore 				= "speditore";
	
	// Parametro temporaneo per fare in modo che la gestione del barcode vanga assegnata solo a determinati utenti
	public static final String gestoreBarcode 			= "gestoreBarcode";
	
	// Parametri interni
	public static final String showBack 	= "showBack";
	public static final String storicizzato = "storicizzato";
	public static final String dopoSpunta 	= "dopoSpunta";
	public static final String inSpedizione	= "inSpedizione";
	public static final String inRicezione	= "inRicezione";

	// Parametri specificati nel DB
	private BooleanType isUfficioSmistatore				= new BooleanType();
	private BooleanType isUfficioMgmPlichi				= new BooleanType();
	private BooleanType isUfficioCtrlCassette			= new BooleanType();
	private BooleanType isUfficioCtrlFirmeDisatt		= new BooleanType();
	private BooleanType isUfficioCtrlFirmeAgenteDisatt	= new BooleanType();
	
	// Utili per le ricerche
	private boolean     primaAttivazione = true;
	private StringType  daoAccessName = new StringType();
	private BooleanType	doSearch = new BooleanType();
	private IntegerType tabNum = new IntegerType(0);
	private StringType  tabName = new StringType();
	
	
	/***********************************************************************************************/
	/* Da togliere in futuro quando tutti avranno gli assegni 									   */
	/***********************************************************************************************/
	public boolean isDocAssegniAbilitati(){
		if(getVersione().isNull())
			return false;
		if(Integer.parseInt(getVersione().toString()) >= 2)
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void copyParams(ParamsModel other){
		this.setVersione(other.getVersione());
		this.setVerticalVideoHeight(other.getVerticalVideoHeight());
		this.setParams(other.getParams());
		this.setFnc(other.getFnc());
		this.setTipoDac(other.getTipoDac());
		this.setUfficio(other.getUfficio());
		this.setIsPritMOM(other.getIsPritMOM());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addParam(String param){
		if(getParams().toString().indexOf(param) < 0)
			setParams(new StringType(getParams().toString()+"_"+param));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void removeParam(String param){
		String pars = getParams().toString(); 
		int idx1 = pars.indexOf(param);
		if(idx1 < 0)
			return;
		int idx2 = idx1+param.length();
		String s1 = pars.substring(0,idx1);
		String s2 = pars.substring(idx2);
		String newPars = s1+s2;
		newPars = newPars.replaceAll("__","_");
		if(newPars.endsWith("_"))
			newPars = newPars.substring(0,(newPars.length()-1));
		setParams(new StringType(newPars));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isFaseDiSpunta(){
		if(getFnc().equals(Costanti.FNC_SPUNTA) || isAutoSpuntataParams())
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String htmlParams(String prefix, String extraParams){
		if(prefix != null && prefix.length() > 0)
			prefix = prefix + "_";
		StringBuffer pars = new StringBuffer(getParams().toString());
		String[] exPars = extraParams.split("\\_");
		for(int i=0;i<exPars.length;i++){
			if(pars.indexOf(exPars[i]) >= 0)
				continue;
			pars.append("_"+exPars[i]);
		}
		setParams(new StringType(pars.toString()));
		StringBuffer result = new StringBuffer();
		result.append("<input type='hidden' name='"+prefix+"verticalVideoHeight' value='"+getVerticalVideoHeight()+"'>\n");
		result.append("<input type='hidden' name='"+prefix+"versione' value='"+getVersione()+"'>\n");
		result.append("<input type='hidden' name='"+prefix+"ufficio' value='"+getUfficio()+"'>\n");
		result.append("<input type='hidden' name='"+prefix+"tipoDac' value='"+getTipoDac()+"'>\n");
		result.append("<input type='hidden' name='"+prefix+"fnc' value='"+getFnc()+"'>\n");
		result.append("<input type='hidden' name='"+prefix+"params' value='"+getParams()+"'>\n");
		return result.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isServizioFirmePerContoCorrente() {
		return true;
		//return getParams().toString().indexOf(servizioFirmePerCC) >= 0 ? true : false; 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isMgmPlichi(){
		return getParams().toString().indexOf(mgmPlichi) >= 0 ? true : false; 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSpeditore(){
		return getParams().toString().indexOf(speditore) >= 0 ? true : false; 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSmistatore(){
		if(isSpeditore()) // speditore prevale su smistatore
			return false;
		return getParams().toString().indexOf(smistatore) >= 0 ? true : false; 
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsSmistatore() {
		return new BooleanType(isSmistatore());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isCtrlCassette(){
		// Nel luglio 2009 ci hanno chiesto di eliminare qualunque
		// controllo sulle cassette.
		// Nel caso si volesse in futuro ripristinare sarà sufficiente ripristinare il
		// codice di questo metodo
		return false;
		
//		if(isSpeditore()) // speditore prevale su ctrlCassette
//			return false;
//		return getParams().toString().indexOf(ctrlCassette) >= 0 ? true : false; 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isCtrlFirmeDisattivo(){
		return getParams().toString().indexOf(noCtrlFirme) >= 0 ? true : false; 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isCtrlFirmeAgenteDisattivo(){
		return getParams().toString().indexOf(noCtrlFirmeAgente) >= 0 ? true : false; 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isAutoSpuntataParams(){
		return getParams().toString().indexOf(autoSpuntata) >= 0 ? true : false; 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isShowBack(){
		return getParams().toString().indexOf(showBack) >= 0 ? true : false; 
	}
	
	/***********************************************************************************************/
	/* Non sono parametri di lancio ma vengono impostati dalle ricerche                            */
	/***********************************************************************************************/
	public boolean isStoricizzato(){
		return getParams().toString().indexOf(storicizzato) >= 0 ? true : false; 
	}
	public boolean isDopoSpunta(){
		return getParams().toString().indexOf(dopoSpunta) >= 0 ? true : false; 
	}
	public boolean inSpedizione(){
		return getParams().toString().indexOf(inSpedizione) >= 0 ? true : false; 
	}
	public BooleanType getInSpedizione(){
		return new BooleanType(inSpedizione()); 
	}
	public boolean inRicezione(){
		return getParams().toString().indexOf(inRicezione) >= 0 ? true : false; 
	}
	public BooleanType getInRicezione(){
		return new BooleanType(inRicezione()); 
	}
	/***********************************************************************************************/
	
	/***********************************************************************************************/
	/* Temporaneo 																				   */
	/***********************************************************************************************/
	public boolean isGestoreBarcode(){
		return getParams().toString().indexOf(gestoreBarcode) >= 0 ? true : false; 
	}
	
	public StringType getParams() {
		return params;
	}

	public void setParams(StringType params) {
		this.params = params;
	}

	public StringType getFnc() {
		return fnc;
	}

	public void setFnc(StringType fnc) {
		this.fnc = fnc;
	}

	public IntegerType getUfficio() {
		return ufficio;
	}

	public void setUfficio(IntegerType ufficio) {
		this.ufficio = ufficio;
	}

	public IntegerType getTipoDac() {
		return tipoDac;
	}

	public void setTipoDac(IntegerType tipoDac) {
		this.tipoDac = tipoDac;
	}

	public BooleanType getIsUfficioSmistatore() {
		return isUfficioSmistatore;
	}

	public void setIsUfficioSmistatore(BooleanType isUfficioSmistatore) {
		this.isUfficioSmistatore = isUfficioSmistatore;
	}

	public BooleanType getIsUfficioMgmPlichi() {
		return isUfficioMgmPlichi;
	}

	public void setIsUfficioMgmPlichi(BooleanType isUfficioMgmPlichi) {
		this.isUfficioMgmPlichi = isUfficioMgmPlichi;
	}

	public BooleanType getIsUfficioCtrlCassette() {
		return isUfficioCtrlCassette;
	}

	public void setIsUfficioCtrlCassette(BooleanType isUfficioCtrlCassette) {
		this.isUfficioCtrlCassette = isUfficioCtrlCassette;
	}

	public BooleanType getIsUfficioCtrlFirmeDisatt() {
		return isUfficioCtrlFirmeDisatt;
	}

	public void setIsUfficioCtrlFirmeDisatt(BooleanType isUfficioCtrlFirmeDisatt) {
		this.isUfficioCtrlFirmeDisatt = isUfficioCtrlFirmeDisatt;
	}

	public BooleanType getDoSearch() {
		return doSearch;
	}

	public void setDoSearch(BooleanType doSearch) {
		this.doSearch = doSearch;
	}

	public boolean isPrimaAttivazione() {
		return primaAttivazione;
	}

	public void setPrimaAttivazione(boolean primaAttivazione) {
		this.primaAttivazione = primaAttivazione;
	}

	public StringType getDaoAccessName() {
		return daoAccessName;
	}

	public void setDaoAccessName(StringType daoAccessName) {
		this.daoAccessName = daoAccessName;
	}

	public IntegerType getTabNum() {
		return tabNum;
	}

	public void setTabNum(IntegerType tabNum) {
		this.tabNum = tabNum;
	}

	public IntegerType getVerticalVideoHeight() {
		return verticalVideoHeight;
	}

	public void setVerticalVideoHeight(IntegerType verticalVideoHeight) {
		this.verticalVideoHeight = verticalVideoHeight;
	}

	public StringType getTabName() {
		return tabName;
	}

	public void setTabName(StringType tabName) {
		this.tabName = tabName;
	}

	public StringType getVersione() {
		return versione;
	}

	public void setVersione(StringType versione) {
		this.versione = versione;
	}

	public BooleanType getIsUfficioCtrlFirmeAgenteDisatt() {
		return isUfficioCtrlFirmeAgenteDisatt;
	}

	public void setIsUfficioCtrlFirmeAgenteDisatt(
			BooleanType isUfficioCtrlFirmeAgenteDisatt) {
		this.isUfficioCtrlFirmeAgenteDisatt = isUfficioCtrlFirmeAgenteDisatt;
	}

	public BooleanType getIsPritMOM() {
		return isPritMOM;
	}

	public void setIsPritMOM(BooleanType isPritMOM) {
		this.isPritMOM = isPritMOM;
	}


}
