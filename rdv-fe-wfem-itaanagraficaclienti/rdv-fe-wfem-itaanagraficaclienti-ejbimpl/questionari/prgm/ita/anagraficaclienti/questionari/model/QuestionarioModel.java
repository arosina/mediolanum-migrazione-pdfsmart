package prgm.ita.anagraficaclienti.questionari.model;

import java.util.ArrayList;
import java.util.Vector;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.cedacri.adeguatezza.model.ElementoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.ElementoQuestionarioRisposteModel;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.questionari.facade.MappaturaTitoliDiStudio;
import prgm.ita.anagraficaclienti.questionari.html.HtmlElementModel;
import prgm.ita.anagraficaclienti.questionari.html.HtmlQuestionarioPage;
import prgm.ita.anagraficaclienti.questionari.html.RispostaConImporto;
import prgm.ita.anagraficaclienti.questionari.html.RispostaReference;
import prgm.ita.anagraficaclienti.questionari.html.RispostaSemplice;
import prgm.ita.anagraficaclienti.questionari.html.TipiElemento;
import prgm.ita.anagraficaclienti.questionari.punteggi.Punteggi;

/*******************************************************************/
/*******************************************************************/
public class QuestionarioModel extends CommandDataModel {
	
	// Numero della posizione della domanda sul Titolo di Studio
	public static final int NUMERO_DOMANDA_ETA 				= 1;
	public static final int NUMERO_RISPOSTA_ETA_00_35		= 1;
	public static final int NUMERO_RISPOSTA_ETA_36_55		= 2;
	public static final int NUMERO_RISPOSTA_ETA_56_75		= 3;
	public static final int NUMERO_RISPOSTA_ETA_76_85		= 4;
	public static final int NUMERO_RISPOSTA_ETA_86_00		= 5;
	
	public static final int NUMERO_DOMANDA_TITOLO_DI_STUDIO = 12;
	
	public static String PROFILO_CON = "CON";
	public static String PROFILO_EQU = "EQU";
	public static String PROFILO_INT = "INT";
	
	public static String ORIZZONTE_BREVE = "BREVE";
	public static String ORIZZONTE_MEDIO = "MEDIO";
	public static String ORIZZONTE_LUNGO = "LUNGO";
	
	public static String ESPFINA_BASSA = "BAS";
	public static String ESPFINA_MEDIA = "MED";
	public static String ESPFINA_ALTA  = "ALT";
	

	private static String ORIZZONTE_CON_B = "CLUSTER9";
	private static String ORIZZONTE_CON_M = "CLUSTER8";
	private static String ORIZZONTE_CON_L = "CLUSTER7";
	
	private static String ORIZZONTE_EQU_B = "CLUSTER6";
	private static String ORIZZONTE_EQU_M = "CLUSTER5";
	private static String ORIZZONTE_EQU_L = "CLUSTER4";
	
	private static String ORIZZONTE_INT_B = "CLUSTER3";
	private static String ORIZZONTE_INT_M = "CLUSTER2";
	private static String ORIZZONTE_INT_L = "CLUSTER1";
	
	// campi tecnici
	private boolean inFirmaDigitale = false;
	private String 	messaggioCentrale = "";
	private boolean salvato = false;
	private String  datiClienteInsufficientiMsg = "";
	
	private IntegerType domandaInErrore = new IntegerType(-1);

	private BooleanType showBack    = new BooleanType();
	private IntegerType scrollPosition = new IntegerType();
	
	private ClienteModel  cliente = new ClienteModel();
	private StringType    numSched = new StringType();    
	private StringType	  codProfiloDiInvestimento = new StringType();
	private StringType    codClusterCedacri 		= new StringType();
	private StringType    descrClusterCedacri 		= new StringType();	
	private IntegerType   dFinValCedacri  			= new IntegerType();
	private BooleanType	  isPrivacyAllegata = new BooleanType();
	private TimestampType dataOraCompilazione = new TimestampType();
	private ListType 	  elementiQuestionario = new ListType(HtmlElementModel.class);
	private ListType 	  risposte = new ListType(RispostaReference.class);
	
	/* MIFID3 CEDACRI - 20140829 aggiunta Disc */
	private StringType obbtemp	   = new StringType();
	private StringType sitfina	   = new StringType();
	private StringType obbinve	   = new StringType();
	private StringType espfina	   = new StringType();
	/* MIFID3 CEDACRI - 20140829 aggiunta Disc */

	private OutputCalcoloProfiloModel 	outputCedacri = null;
	private QuestionarioDatiStampaModel datiStampa = new QuestionarioDatiStampaModel();
	private ArrayList<String>			alertAfterCalcolo = new ArrayList<String>();
	private String 						cmdAfterCalcolo = "";
	private BooleanType 				alertAfterCalcoloViewed = new BooleanType();
	
	private boolean etaPrevalorizzata = false;
	private boolean titoloDiStudioPrevalorizzato = false;
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public DateType getDataScadenzaProfiloCedacri(){		
		try{
			String  d = getDFinValCedacri().toString();
			return new DateType(d.substring(6)+"-"+d.substring(4,6)+"-"+d.substring(0,4));
		}catch(Exception e){
			return new DateType();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void appendMessaggioCentrale(String msg){
		messaggioCentrale = messaggioCentrale.concat(msg);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public void initFromElementiQuestionarioCedacri(ClientSessionContext csc, ListType elementiQuestionarioCedacri) throws Exception{
		ClienteModel cliente = getCliente();
		
		int eta = cliente.getEta().intValue();
		int titStudioCedacri = MappaturaTitoliDiStudio.fromMedToCedacri(cliente.getInfoPersonali().getCodTitoloStudio().toString());
		
		getElementiQuestionario().clear();
		for(int i=0;i<elementiQuestionarioCedacri.size();i++){
			ElementoQuestionarioModel rispostaCedacri = (ElementoQuestionarioModel)elementiQuestionarioCedacri.get(i);

			// Imposto la risposta dell'età
			if(rispostaCedacri.getTipElem().equals(TipiElemento.TIPELEM_RISPOSTA) && rispostaCedacri.getNumElem().intValue() == NUMERO_DOMANDA_ETA){
				rispostaCedacri.setSelSele(new StringType("N"));
				if(rispostaCedacri.getNumSele().equals(NUMERO_RISPOSTA_ETA_00_35) && (eta > 0 && eta <= 35)){
					rispostaCedacri.setSelSele(new StringType("S"));
					setEtaPrevalorizzata(true);
				}else if(rispostaCedacri.getNumSele().equals(NUMERO_RISPOSTA_ETA_36_55) && (eta >= 36 && eta <= 55)){
					rispostaCedacri.setSelSele(new StringType("S"));
					setEtaPrevalorizzata(true);
				}else if(rispostaCedacri.getNumSele().equals(NUMERO_RISPOSTA_ETA_56_75) && (eta >= 56 && eta <= 75)){
					rispostaCedacri.setSelSele(new StringType("S"));
					setEtaPrevalorizzata(true);
				}else if(rispostaCedacri.getNumSele().equals(NUMERO_RISPOSTA_ETA_76_85) && (eta >= 76 && eta <= 85)){
					rispostaCedacri.setSelSele(new StringType("S"));
					setEtaPrevalorizzata(true);
				}else if(rispostaCedacri.getNumSele().equals(NUMERO_RISPOSTA_ETA_86_00) && (eta >= 86)){
					rispostaCedacri.setSelSele(new StringType("S"));
					setEtaPrevalorizzata(true);
				}
			}
			
			// Imposto il titolo di studio
			if(rispostaCedacri.getTipElem().equals(TipiElemento.TIPELEM_RISPOSTA) && rispostaCedacri.getNumElem().intValue() == NUMERO_DOMANDA_TITOLO_DI_STUDIO){
				rispostaCedacri.setSelSele(new StringType("N"));
				if(rispostaCedacri.getNumSele().intValue() == titStudioCedacri){
					rispostaCedacri.setSelSele(new StringType("S"));
					setTitoloDiStudioPrevalorizzato(true);
				}
			}			
			
			HtmlElementModel ele = new HtmlElementModel();
			Tools.copyCommandDataModel(rispostaCedacri,ele);
			getElementiQuestionario().add(ele);
		}
		return;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public ListType retrieveRisposteCedacri() throws Exception{
		
		ListType risposte = getRisposte();
		for(int i=0;i<risposte.size();i++){
			RispostaReference rispRef = (RispostaReference)risposte.get(i);
			Vector rs = rispRef.getValoriPossibiliRisposta();
			for(int j=0;j<rs.size();j++){
				HtmlElementModel eleRisp = (HtmlElementModel)rs.get(j);
				if(rispRef.getRisposta() instanceof RispostaSemplice){
					if(rispRef.getRispostaModel().getNumSele().equals(eleRisp.getNumSele()))
						eleRisp.setSelSele(new StringType("S"));
					else
						eleRisp.setSelSele(new StringType("N"));
				}else if(rispRef.getRisposta() instanceof RispostaConImporto){
					if(rispRef.getRispostaModel().getNumSele().isNull())
						eleRisp.setNumSele(new IntegerType(0));
					else
						eleRisp.setNumSele(new IntegerType(rispRef.getRispostaModel().getNumSele().intValue()));	
					if(!rispRef.getRispostaModel().getNumSele().isNull())
						eleRisp.setSelSele(new StringType("S"));
					else
						eleRisp.setSelSele(new StringType("S"));
				}else{
					if(rispRef.getRispostaModel().getSelSele().equals("N"))
						eleRisp.setSelSele(new StringType("N"));
					else if(rispRef.getRispostaModel().getSelSele().equals("S"))
						eleRisp.setSelSele(new StringType("S"));
				}
			}
		}
		
		ListType result = new ListType();
		for(int i=0;i<elementiQuestionario.size();i++){
			HtmlElementModel risposta = (HtmlElementModel)elementiQuestionario.get(i);
			if(!risposta.getSelSele().equals("S"))
				continue;
			ElementoQuestionarioRisposteModel rispostaCedacri = new ElementoQuestionarioRisposteModel();
			Tools.copyCommandDataModel(risposta,rispostaCedacri);
			result.add(rispostaCedacri);
		}
		return result;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public void initDatiPerStampa(){
		try{
			ListType risposteCedacri = retrieveRisposteCedacri();
			for(int i=0;i<risposteCedacri.size();i++){
				ElementoQuestionarioRisposteModel rispostaCedacri = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
				datiStampa.addProperty("r"+rispostaCedacri.getNumElem()+"x"+rispostaCedacri.getNumSele(),new StringType("X"));
			}
			
			datiStampa.addProperty("profiloInvestitore"+getCodProfiloDiInvestimento(),	new StringType("X"));
			datiStampa.addProperty("orizzonteTemporale"+getOrizzonteTemporaleCedacri(),	new StringType("X"));
			datiStampa.addProperty("espfina"+getEspfina(),								new StringType("X"));
			
			datiStampa.addProperty("punteggioSezioneA",	new StringType(""+Punteggi.getPunteggioSezioneA(risposteCedacri)));
			datiStampa.addProperty("punteggioSezioneB",	new StringType(""+Punteggi.getPunteggioSezioneB(risposteCedacri)));
			datiStampa.addProperty("punteggioSezioneC",	new StringType(""+Punteggi.getPunteggioSezioneC(risposteCedacri)));
			datiStampa.addProperty("punteggioSezioneD",	new StringType(""+Punteggi.getPunteggioSezioneD(risposteCedacri)));
			datiStampa.addProperty("punteggioDomandaD3",new StringType(""+Punteggi.getPunteggioDomandaD3(risposteCedacri)));
			datiStampa.addProperty("punteggioDomandaD4",new StringType(""+Punteggi.getPunteggioDomandaD4(risposteCedacri)));
			
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getOrizzonteTemporaleCedacri(){
		String orizzTemp = "";
		if(getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_B) ||
		   getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_B) ||
		   getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_B)){
			orizzTemp = ORIZZONTE_BREVE;
		}else if(getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_M) ||
			getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_M) ||
			getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_M)){
			orizzTemp = ORIZZONTE_MEDIO;
		}else if(getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_L) ||
			getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_L) ||
			getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_L)){
			orizzTemp = ORIZZONTE_LUNGO;
		}
		return orizzTemp;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getOrizzonteTemporaleCedacriCE(){
		String orizzTempCE = "";
		if(getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_B) ||
		   getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_B) ||
		   getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_B)){
			orizzTempCE = ORIZZONTE_BREVE;
		}else if(getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_M) ||
			getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_M) ||
			getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_M)){
			orizzTempCE = ORIZZONTE_MEDIO;
		}else if(getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_CON_L) ||
				getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_EQU_L) ||
				getCliente().getCodClusterCedacri().equals(QuestionarioModel.ORIZZONTE_INT_L)){
			orizzTempCE = ORIZZONTE_LUNGO;
		}
		return orizzTempCE;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public void addRisposta(RispostaReference rispostaRef){
		risposte.add(rispostaRef);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String htmlQuestionario(Template template) {
		HtmlQuestionarioPage htmlQuestionario = new HtmlQuestionarioPage(this,template);
		return htmlQuestionario.getHtml();
	}
		
	public ListType getRisposte() {
		return risposte;
	}

	public void setRisposte(ListType risposte) {
		this.risposte = risposte;
	}

	public ListType getElementiQuestionario() {
		return elementiQuestionario;
	}

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}

	public BooleanType getShowBack() {
		return showBack;
	}

	public void setShowBack(BooleanType showBack) {
		this.showBack = showBack;
	}

	public IntegerType getDomandaInErrore() {
		return domandaInErrore;
	}

	public void setDomandaInErrore(IntegerType domandaInErrore) {
		this.domandaInErrore = domandaInErrore;
	}

	public StringType getCodProfiloDiInvestimento() {
		return codProfiloDiInvestimento;
	}

	public void setCodProfiloDiInvestimento(StringType codProfiloDiInvestimento) {
		this.codProfiloDiInvestimento = codProfiloDiInvestimento;
	}

	public IntegerType getScrollPosition() {
		return scrollPosition;
	}

	public void setScrollPosition(IntegerType scrollPosition) {
		this.scrollPosition = scrollPosition;
	}

	public TimestampType getDataOraCompilazione() {
		return dataOraCompilazione;
	}

	public void setDataOraCompilazione(TimestampType dataOraCompilazione) {
		this.dataOraCompilazione = dataOraCompilazione;
	}

	public boolean isSalvato() {
		return salvato;
	}

	public void setSalvato(boolean salvato) {
		this.salvato = salvato;
	}

	public StringType getNumSched() {
		return numSched;
	}

	public void setNumSched(StringType numSched) {
		this.numSched = numSched;
	}

	public String getDatiClienteInsufficientiMsg() {
		return datiClienteInsufficientiMsg;
	}

	public void setDatiClienteInsufficientiMsg(String datiClienteInsufficientiMsg) {
		this.datiClienteInsufficientiMsg = datiClienteInsufficientiMsg;
	}

	public String getMessaggioCentrale() {
		return messaggioCentrale;
	}

	public void setMessaggioCentrale(String messaggioCentrale) {
		this.messaggioCentrale = messaggioCentrale;
	}

	public BooleanType getIsPrivacyAllegata() {
		return isPrivacyAllegata;
	}

	public void setIsPrivacyAllegata(BooleanType isPrivacyAllegata) {
		this.isPrivacyAllegata = isPrivacyAllegata;
	}

	public StringType getDescrClusterCedacri() {
		return descrClusterCedacri;
	}

	public void setDescrClusterCedacri(StringType descrClusterCedacri) {
		this.descrClusterCedacri = descrClusterCedacri;
	}

	public StringType getCodClusterCedacri() {
		return codClusterCedacri;
	}

	public void setCodClusterCedacri(StringType codClusterCedacri) {
		this.codClusterCedacri = codClusterCedacri;
	}
	public IntegerType getDFinValCedacri() {
		return dFinValCedacri;
	}
	public void setDFinValCedacri(IntegerType finValCedacri) {
		dFinValCedacri = finValCedacri;
	}

	public StringType getObbtemp() {
		return obbtemp;
	}

	public void setObbtemp(StringType obbtemp) {
		this.obbtemp = obbtemp;
	}

	public StringType getSitfina() {
		return sitfina;
	}

	public void setSitfina(StringType sitfina) {
		this.sitfina = sitfina;
	}

	public StringType getObbinve() {
		return obbinve;
	}

	public void setObbinve(StringType obbinve) {
		this.obbinve = obbinve;
	}

	public StringType getEspfina() {
		return espfina;
	}

	public void setEspfina(StringType espfina) {
		this.espfina = espfina;
	}

	public QuestionarioDatiStampaModel getDatiStampa() {
		return datiStampa;
	}

	public void setDatiStampa(QuestionarioDatiStampaModel datiStampa) {
		this.datiStampa = datiStampa;
	}

	public OutputCalcoloProfiloModel getOutputCedacri() {
		return outputCedacri;
	}

	public void setOutputCedacri(OutputCalcoloProfiloModel outputCedacri) {
		this.outputCedacri = outputCedacri;
	}

	public String getCmdAfterCalcolo() {
		return cmdAfterCalcolo;
	}

	public void setCmdAfterCalcolo(String cmdAfterCalcolo) {
		this.cmdAfterCalcolo = cmdAfterCalcolo;
	}

	public BooleanType getAlertAfterCalcoloViewed() {
		return alertAfterCalcoloViewed;
	}

	public void setAlertAfterCalcoloViewed(BooleanType alertAfterCalcoloViewed) {
		this.alertAfterCalcoloViewed = alertAfterCalcoloViewed;
	}

	public ArrayList<String> getAlertAfterCalcolo() {
		return alertAfterCalcolo;
	}

	public void setAlertAfterCalcolo(ArrayList<String> alertAfterCalcolo) {
		this.alertAfterCalcolo = alertAfterCalcolo;
	}

	public boolean isInFirmaDigitale() {
		return inFirmaDigitale;
	}

	public void setInFirmaDigitale(boolean inFirmaDigitale) {
		this.inFirmaDigitale = inFirmaDigitale;
	}

	public boolean isEtaPrevalorizzata() {
		return etaPrevalorizzata;
	}

	public void setEtaPrevalorizzata(boolean etaPrevalorizzata) {
		this.etaPrevalorizzata = etaPrevalorizzata;
	}

	public boolean isTitoloDiStudioPrevalorizzato() {
		return titoloDiStudioPrevalorizzato;
	}

	public void setTitoloDiStudioPrevalorizzato(boolean titoloDiStudioPrevalorizzato) {
		this.titoloDiStudioPrevalorizzato = titoloDiStudioPrevalorizzato;
	}

}
