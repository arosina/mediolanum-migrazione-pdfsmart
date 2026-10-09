package prgm.cedacri.adeguatezza.model;
/*
 * Output servizio CtrlAdeguatezza
 */
public class OutputCtrlAdeguatezzaBean  implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito    : codice di errore, 000 se con successo
	 * DescErr  : Motivo dell'errore
	 * adeNoten : segnalazione aggiuntiva
	 * errProfn : se S manca profilo cliente, se N profilato
	 * adeToggn : se S adeguato per oggetto, se N non adeguato per oggetto
	 * desToggn : descrizione estesa del motivo
	 * adeDimen : se S adeguato per dimensione, se N non adeguato per dimensione
	 * desDimen : descrizione estesa del motivo
	 * adeFreqn : se S adeguato per frequenza, se N non adeguato per frequenza
	 * desFreqn : descrizione estesa del motivo
	 * adeObbin : se S adeguato per obbiettivo di investimento, se N non adeguato per obbiettivo di investimento
	 * desObbin : descrizione estesa del motivo
	 * adeOritn : se S adeguato per orizzonte temporale, se N non adeguato per orizzonte temporale
	 * desOritn : descrizione estesa del motivo
	 * adeModvn : se S adeguato per modalità di versamento, se N non adeguato per modalità di versamento
	 * desModvn : descrizione estesa del motivo
     * adeDisin : adeguatezza disinvestimento
     * desDisin : descrizione adeguatezza disinvestimento
     * adeAttCo : attivita di consulenza
     * adeTitCo : titolo consulenziabile
	 */
	private String descErr = new String();
	private String esito   = new String();
	private String adeNote1 = new String();
	private String errProf1 = new String();
	private String adeTogg1 = new String();
	private String desTogg1 = new String();
	private String adeDime1 = new String();
	private String desDime1 = new String();
	private String adeFreq1 = new String();
	private String desFreq1 = new String();
	private String adeObbi1 = new String();
	private String desObbi1 = new String();
	private String adeOrit1 = new String();
	private String desOrit1 = new String();
	private String adeModv1 = new String();
	private String desModv1 = new String();
	private String adeNote2 = new String();
	private String errProf2 = new String();
	private String adeTogg2 = new String();
	private String desTogg2 = new String();
	private String adeDime2 = new String();
	private String desDime2 = new String();
	private String adeFreq2 = new String();
	private String desFreq2 = new String();
	private String adeObbi2 = new String();
	private String desObbi2 = new String();
	private String adeOrit2 = new String();
	private String desOrit2 = new String();
	private String adeModv2 = new String();
	private String desModv2 = new String();
	private String adeNote3 = new String();
	private String errProf3 = new String();
	private String adeTogg3 = new String();
	private String desTogg3 = new String();
	private String adeDime3 = new String();
	private String desDime3 = new String();
	private String adeFreq3 = new String();
	private String desFreq3 = new String();
	private String adeObbi3 = new String();
	private String desObbi3 = new String();
	private String adeOrit3 = new String();
	private String desOrit3 = new String();
	private String adeModv3 = new String();
	private String desModv3 = new String();
	private String adeNote4 = new String();
	private String errProf4 = new String();
	private String adeTogg4 = new String();
	private String desTogg4 = new String();
	private String adeDime4 = new String();
	private String desDime4 = new String();
	private String adeFreq4 = new String();
	private String desFreq4 = new String();
	private String adeObbi4 = new String();
	private String desObbi4 = new String();
	private String adeOrit4 = new String();
	private String desOrit4 = new String();
	private String adeModv4 = new String();
	private String desModv4 = new String();
	private String adeDisi1 = new String();
	private String adeDisi2 = new String();
	private String adeDisi3 = new String();
	private String adeDisi4 = new String();
	private String desDisi1 = new String();
	private String desDisi2 = new String();
	private String desDisi3 = new String();
	private String desDisi4 = new String();
	private String adeAttCo  = new String(); 
	private String adeTitCo  = new String();
	
	public String getAdeDime1() {
		return adeDime1;
	}
	public void setAdeDime1(String adeDime1) {
		this.adeDime1 = adeDime1;
	}
	public String getAdeDime2() {
		return adeDime2;
	}
	public void setAdeDime2(String adeDime2) {
		this.adeDime2 = adeDime2;
	}
	public String getAdeDime3() {
		return adeDime3;
	}
	public void setAdeDime3(String adeDime3) {
		this.adeDime3 = adeDime3;
	}
	public String getAdeDime4() {
		return adeDime4;
	}
	public void setAdeDime4(String adeDime4) {
		this.adeDime4 = adeDime4;
	}
	public String getAdeFreq1() {
		return adeFreq1;
	}
	public void setAdeFreq1(String adeFreq1) {
		this.adeFreq1 = adeFreq1;
	}
	public String getAdeFreq2() {
		return adeFreq2;
	}
	public void setAdeFreq2(String adeFreq2) {
		this.adeFreq2 = adeFreq2;
	}
	public String getAdeFreq3() {
		return adeFreq3;
	}
	public void setAdeFreq3(String adeFreq3) {
		this.adeFreq3 = adeFreq3;
	}
	public String getAdeFreq4() {
		return adeFreq4;
	}
	public void setAdeFreq4(String adeFreq4) {
		this.adeFreq4 = adeFreq4;
	}
	public String getAdeModv1() {
		return adeModv1;
	}
	public void setAdeModv1(String adeModv1) {
		this.adeModv1 = adeModv1;
	}
	public String getAdeModv2() {
		return adeModv2;
	}
	public void setAdeModv2(String adeModv2) {
		this.adeModv2 = adeModv2;
	}
	public String getAdeModv3() {
		return adeModv3;
	}
	public void setAdeModv3(String adeModv3) {
		this.adeModv3 = adeModv3;
	}
	public String getAdeModv4() {
		return adeModv4;
	}
	public void setAdeModv4(String adeModv4) {
		this.adeModv4 = adeModv4;
	}
	public String getAdeNote1() {
		return adeNote1;
	}
	public void setAdeNote1(String adeNote1) {
		this.adeNote1 = adeNote1;
	}
	public String getAdeNote2() {
		return adeNote2;
	}
	public void setAdeNote2(String adeNote2) {
		this.adeNote2 = adeNote2;
	}
	public String getAdeNote3() {
		return adeNote3;
	}
	public void setAdeNote3(String adeNote3) {
		this.adeNote3 = adeNote3;
	}
	public String getAdeNote4() {
		return adeNote4;
	}
	public void setAdeNote4(String adeNote4) {
		this.adeNote4 = adeNote4;
	}
	public String getAdeObbi1() {
		return adeObbi1;
	}
	public void setAdeObbi1(String adeObbi1) {
		this.adeObbi1 = adeObbi1;
	}
	public String getAdeObbi2() {
		return adeObbi2;
	}
	public void setAdeObbi2(String adeObbi2) {
		this.adeObbi2 = adeObbi2;
	}
	public String getAdeObbi3() {
		return adeObbi3;
	}
	public void setAdeObbi3(String adeObbi3) {
		this.adeObbi3 = adeObbi3;
	}
	public String getAdeObbi4() {
		return adeObbi4;
	}
	public void setAdeObbi4(String adeObbi4) {
		this.adeObbi4 = adeObbi4;
	}
	public String getAdeOrit1() {
		return adeOrit1;
	}
	public void setAdeOrit1(String adeOrit1) {
		this.adeOrit1 = adeOrit1;
	}
	public String getAdeOrit2() {
		return adeOrit2;
	}
	public void setAdeOrit2(String adeOrit2) {
		this.adeOrit2 = adeOrit2;
	}
	public String getAdeOrit3() {
		return adeOrit3;
	}
	public void setAdeOrit3(String adeOrit3) {
		this.adeOrit3 = adeOrit3;
	}
	public String getAdeOrit4() {
		return adeOrit4;
	}
	public void setAdeOrit4(String adeOrit4) {
		this.adeOrit4 = adeOrit4;
	}
	public String getAdeTogg1() {
		return adeTogg1;
	}
	public void setAdeTogg1(String adeTogg1) {
		this.adeTogg1 = adeTogg1;
	}
	public String getAdeTogg2() {
		return adeTogg2;
	}
	public void setAdeTogg2(String adeTogg2) {
		this.adeTogg2 = adeTogg2;
	}
	public String getAdeTogg3() {
		return adeTogg3;
	}
	public void setAdeTogg3(String adeTogg3) {
		this.adeTogg3 = adeTogg3;
	}
	public String getAdeTogg4() {
		return adeTogg4;
	}
	public void setAdeTogg4(String adeTogg4) {
		this.adeTogg4 = adeTogg4;
	}
	public String getDescErr() {
		return descErr;
	}
	public void setDescErr(String descErr) {
		this.descErr = descErr;
	}
	public String getDesDime1() {
		return desDime1;
	}
	public void setDesDime1(String desDime1) {
		this.desDime1 = desDime1;
	}
	public String getDesDime2() {
		return desDime2;
	}
	public void setDesDime2(String desDime2) {
		this.desDime2 = desDime2;
	}
	public String getDesDime3() {
		return desDime3;
	}
	public void setDesDime3(String desDime3) {
		this.desDime3 = desDime3;
	}
	public String getDesDime4() {
		return desDime4;
	}
	public void setDesDime4(String desDime4) {
		this.desDime4 = desDime4;
	}
	public String getDesFreq1() {
		return desFreq1;
	}
	public void setDesFreq1(String desFreq1) {
		this.desFreq1 = desFreq1;
	}
	public String getDesFreq2() {
		return desFreq2;
	}
	public void setDesFreq2(String desFreq2) {
		this.desFreq2 = desFreq2;
	}
	public String getDesFreq3() {
		return desFreq3;
	}
	public void setDesFreq3(String desFreq3) {
		this.desFreq3 = desFreq3;
	}
	public String getDesFreq4() {
		return desFreq4;
	}
	public void setDesFreq4(String desFreq4) {
		this.desFreq4 = desFreq4;
	}
	public String getDesModv1() {
		return desModv1;
	}
	public void setDesModv1(String desModv1) {
		this.desModv1 = desModv1;
	}
	public String getDesModv2() {
		return desModv2;
	}
	public void setDesModv2(String desModv2) {
		this.desModv2 = desModv2;
	}
	public String getDesModv3() {
		return desModv3;
	}
	public void setDesModv3(String desModv3) {
		this.desModv3 = desModv3;
	}
	public String getDesModv4() {
		return desModv4;
	}
	public void setDesModv4(String desModv4) {
		this.desModv4 = desModv4;
	}
	public String getDesObbi1() {
		return desObbi1;
	}
	public void setDesObbi1(String desObbi1) {
		this.desObbi1 = desObbi1;
	}
	public String getDesObbi2() {
		return desObbi2;
	}
	public void setDesObbi2(String desObbi2) {
		this.desObbi2 = desObbi2;
	}
	public String getDesObbi3() {
		return desObbi3;
	}
	public void setDesObbi3(String desObbi3) {
		this.desObbi3 = desObbi3;
	}
	public String getDesObbi4() {
		return desObbi4;
	}
	public void setDesObbi4(String desObbi4) {
		this.desObbi4 = desObbi4;
	}
	public String getDesOrit1() {
		return desOrit1;
	}
	public void setDesOrit1(String desOrit1) {
		this.desOrit1 = desOrit1;
	}
	public String getDesOrit2() {
		return desOrit2;
	}
	public void setDesOrit2(String desOrit2) {
		this.desOrit2 = desOrit2;
	}
	public String getDesOrit3() {
		return desOrit3;
	}
	public void setDesOrit3(String desOrit3) {
		this.desOrit3 = desOrit3;
	}
	public String getDesOrit4() {
		return desOrit4;
	}
	public void setDesOrit4(String desOrit4) {
		this.desOrit4 = desOrit4;
	}
	public String getDesTogg1() {
		return desTogg1;
	}
	public void setDesTogg1(String desTogg1) {
		this.desTogg1 = desTogg1;
	}
	public String getDesTogg2() {
		return desTogg2;
	}
	public void setDesTogg2(String desTogg2) {
		this.desTogg2 = desTogg2;
	}
	public String getDesTogg3() {
		return desTogg3;
	}
	public void setDesTogg3(String desTogg3) {
		this.desTogg3 = desTogg3;
	}
	public String getDesTogg4() {
		return desTogg4;
	}
	public void setDesTogg4(String desTogg4) {
		this.desTogg4 = desTogg4;
	}
	public String getErrProf1() {
		return errProf1;
	}
	public void setErrProf1(String errProf1) {
		this.errProf1 = errProf1;
	}
	public String getErrProf2() {
		return errProf2;
	}
	public void setErrProf2(String errProf2) {
		this.errProf2 = errProf2;
	}
	public String getErrProf3() {
		return errProf3;
	}
	public void setErrProf3(String errProf3) {
		this.errProf3 = errProf3;
	}
	public String getErrProf4() {
		return errProf4;
	}
	public void setErrProf4(String errProf4) {
		this.errProf4 = errProf4;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public void setAdeDisi1(String adeDisi1) {
		this.adeDisi1 = adeDisi1;
	}
	public void setAdeDisi2(String adeDisi2) {
		this.adeDisi2 = adeDisi2;
	}
	public void setAdeDisi3(String adeDisi3) {
		this.adeDisi3 = adeDisi3;
	}
	public void setAdeDisi4(String adeDisi4) {
		this.adeDisi4 = adeDisi4;
	}
	public void setDesDisi1(String desDisi1) {
		this.desDisi1 = desDisi1;
	}
	public void setDesDisi2(String desDisi2) {
		this.desDisi2 = desDisi2;
	}
	public void setDesDisi3(String desDisi3) {
		this.desDisi3 = desDisi3;
	}
	public void setDesDisi4(String desDisi4) {
		this.desDisi4 = desDisi4;
	}
	public void setAdeAttCo(String adeAttCo) {
		this.adeAttCo = adeAttCo;
	}
	public void setAdeTitCo(String adeTitCo) {
		this.adeTitCo = adeTitCo;
	}
	public String getAdeDisi1() {
		return adeDisi1;
	}
	public String getAdeDisi2() {
		return adeDisi2;
	}
	public String getAdeDisi3() {
		return adeDisi3;
	}
	public String getAdeDisi4() {
		return adeDisi4;
	}
	public String getDesDisi1() {
		return desDisi1;
	}
	public String getDesDisi2() {
		return desDisi2;
	}
	public String getDesDisi3() {
		return desDisi3;
	}
	public String getDesDisi4() {
		return desDisi4;
	}
	public String getAdeAttCo() {
		return adeAttCo;
	}
	public String getAdeTitCo() {
		return adeTitCo;
	}
}
