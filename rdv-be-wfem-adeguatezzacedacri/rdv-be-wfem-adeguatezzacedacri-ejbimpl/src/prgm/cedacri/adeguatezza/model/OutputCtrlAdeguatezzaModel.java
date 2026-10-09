package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
/*
 * Output servizio CtrlAdeguatezza
 */
public class OutputCtrlAdeguatezzaModel extends CommandDataModel  implements java.io.Serializable
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
	private StringType descErr = new StringType();
	private StringType esito   = new StringType();
	private StringType adeNote1 = new StringType();
	private StringType errProf1 = new StringType();
	private StringType adeTogg1 = new StringType();
	private StringType desTogg1 = new StringType();
	private StringType adeDime1 = new StringType();
	private StringType desDime1 = new StringType();
	private StringType adeFreq1 = new StringType();
	private StringType desFreq1 = new StringType();
	private StringType adeObbi1 = new StringType();
	private StringType desObbi1 = new StringType();
	private StringType adeOrit1 = new StringType();
	private StringType desOrit1 = new StringType();
	private StringType adeModv1 = new StringType();
	private StringType desModv1 = new StringType();
	private StringType adeNote2 = new StringType();
	private StringType errProf2 = new StringType();
	private StringType adeTogg2 = new StringType();
	private StringType desTogg2 = new StringType();
	private StringType adeDime2 = new StringType();
	private StringType desDime2 = new StringType();
	private StringType adeFreq2 = new StringType();
	private StringType desFreq2 = new StringType();
	private StringType adeObbi2 = new StringType();
	private StringType desObbi2 = new StringType();
	private StringType adeOrit2 = new StringType();
	private StringType desOrit2 = new StringType();
	private StringType adeModv2 = new StringType();
	private StringType desModv2 = new StringType();
	private StringType adeNote3 = new StringType();
	private StringType errProf3 = new StringType();
	private StringType adeTogg3 = new StringType();
	private StringType desTogg3 = new StringType();
	private StringType adeDime3 = new StringType();
	private StringType desDime3 = new StringType();
	private StringType adeFreq3 = new StringType();
	private StringType desFreq3 = new StringType();
	private StringType adeObbi3 = new StringType();
	private StringType desObbi3 = new StringType();
	private StringType adeOrit3 = new StringType();
	private StringType desOrit3 = new StringType();
	private StringType adeModv3 = new StringType();
	private StringType desModv3 = new StringType();
	private StringType adeNote4 = new StringType();
	private StringType errProf4 = new StringType();
	private StringType adeTogg4 = new StringType();
	private StringType desTogg4 = new StringType();
	private StringType adeDime4 = new StringType();
	private StringType desDime4 = new StringType();
	private StringType adeFreq4 = new StringType();
	private StringType desFreq4 = new StringType();
	private StringType adeObbi4 = new StringType();
	private StringType desObbi4 = new StringType();
	private StringType adeOrit4 = new StringType();
	private StringType desOrit4 = new StringType();
	private StringType adeModv4 = new StringType();
	private StringType desModv4 = new StringType();
	private StringType adeDisi1 = new StringType();
	private StringType adeDisi2 = new StringType();
	private StringType adeDisi3 = new StringType();
	private StringType adeDisi4 = new StringType();
	private StringType desDisi1 = new StringType();	
	private StringType desDisi2 = new StringType();
	private StringType desDisi3 = new StringType();
	private StringType desDisi4 = new StringType();
	private StringType adeAttCo = new StringType();
	private StringType adeTitCo = new StringType();
	
	public void setTuttoAdeguato()
	{
		setAdeguatoIndex(1,true);
		setAdeguatoIndex(2,true);
		setAdeguatoIndex(3,true);
		setAdeguatoIndex(4,true);
	}
	public void setTuttoNonAdeguato()
	{
		setAdeguatoIndex(1,false);
		setAdeguatoIndex(2,false);
		setAdeguatoIndex(3,false);
		setAdeguatoIndex(4,false);
	}
	
	public void setAdeguatoIndex(int i,boolean tipo)
	{
		if (i == 1)
		{
			if (tipo == true)
			{
				adeTogg1.setStringValue("S");
				adeDime1.setStringValue("S");
				adeFreq1.setStringValue("S");
				adeObbi1.setStringValue("S");
				adeOrit1.setStringValue("S");
				adeModv1.setStringValue("S");
				adeDisi1.setStringValue("S");
			}else{
				adeTogg1.setStringValue("N");
				adeDime1.setStringValue("N");
				adeFreq1.setStringValue("N");
				adeObbi1.setStringValue("N");
				adeOrit1.setStringValue("N");
				adeModv1.setStringValue("N");
				adeDisi1.setStringValue("N");				
			}
		}
		if (i == 2)
		{
			if (tipo == true)
			{
				adeTogg2.setStringValue("S");
				adeDime2.setStringValue("S");
				adeFreq2.setStringValue("S");
				adeObbi2.setStringValue("S");
				adeOrit2.setStringValue("S");
				adeModv2.setStringValue("S");
				adeDisi2.setStringValue("S");
			}else{
				adeTogg2.setStringValue("N");
				adeDime2.setStringValue("N");
				adeFreq2.setStringValue("N");
				adeObbi2.setStringValue("N");
				adeOrit2.setStringValue("N");
				adeModv2.setStringValue("N");
				adeDisi2.setStringValue("N");
			}
		}
		if (i == 3)
		{
			if (tipo == true)
			{
				adeTogg3.setStringValue("S");
				adeDime3.setStringValue("S");
				adeFreq3.setStringValue("S");
				adeObbi3.setStringValue("S");
				adeOrit3.setStringValue("S");
				adeModv3.setStringValue("S");
				adeDisi3.setStringValue("S");
			}else{
				adeTogg3.setStringValue("N");
				adeDime3.setStringValue("N");
				adeFreq3.setStringValue("N");
				adeObbi3.setStringValue("N");
				adeOrit3.setStringValue("N");
				adeModv3.setStringValue("N");
				adeDisi3.setStringValue("N");
			}
		}
		if (i == 4)
		{
			if (tipo == true)
			{
				adeTogg4.setStringValue("S");
				adeDime4.setStringValue("S");
				adeFreq4.setStringValue("S");
				adeObbi4.setStringValue("S");
				adeOrit4.setStringValue("S");
				adeModv4.setStringValue("S");
				adeDisi4.setStringValue("S");
			}else{
				adeTogg4.setStringValue("N");
				adeDime4.setStringValue("N");
				adeFreq4.setStringValue("N");
				adeObbi4.setStringValue("N");
				adeOrit4.setStringValue("N");
				adeModv4.setStringValue("N");				
				adeDisi4.setStringValue("N");
			}
		}
	}
	
	public StringType getAdeDime1() {
		return adeDime1;
	}
	public void setAdeDime1(StringType adeDime1) {
		this.adeDime1 = adeDime1;
	}
	public StringType getAdeDime2() {
		return adeDime2;
	}
	public void setAdeDime2(StringType adeDime2) {
		this.adeDime2 = adeDime2;
	}
	public StringType getAdeDime3() {
		return adeDime3;
	}
	public void setAdeDime3(StringType adeDime3) {
		this.adeDime3 = adeDime3;
	}
	public StringType getAdeDime4() {
		return adeDime4;
	}
	public void setAdeDime4(StringType adeDime4) {
		this.adeDime4 = adeDime4;
	}
	public StringType getAdeFreq1() {
		return adeFreq1;
	}
	public void setAdeFreq1(StringType adeFreq1) {
		this.adeFreq1 = adeFreq1;
	}
	public StringType getAdeFreq2() {
		return adeFreq2;
	}
	public void setAdeFreq2(StringType adeFreq2) {
		this.adeFreq2 = adeFreq2;
	}
	public StringType getAdeFreq3() {
		return adeFreq3;
	}
	public void setAdeFreq3(StringType adeFreq3) {
		this.adeFreq3 = adeFreq3;
	}
	public StringType getAdeFreq4() {
		return adeFreq4;
	}
	public void setAdeFreq4(StringType adeFreq4) {
		this.adeFreq4 = adeFreq4;
	}
	public StringType getAdeModv1() {
		return adeModv1;
	}
	public void setAdeModv1(StringType adeModv1) {
		this.adeModv1 = adeModv1;
	}
	public StringType getAdeModv2() {
		return adeModv2;
	}
	public void setAdeModv2(StringType adeModv2) {
		this.adeModv2 = adeModv2;
	}
	public StringType getAdeModv3() {
		return adeModv3;
	}
	public void setAdeModv3(StringType adeModv3) {
		this.adeModv3 = adeModv3;
	}
	public StringType getAdeModv4() {
		return adeModv4;
	}
	public void setAdeModv4(StringType adeModv4) {
		this.adeModv4 = adeModv4;
	}
	public StringType getAdeNote1() {
		return adeNote1;
	}
	public void setAdeNote1(StringType adeNote1) {
		this.adeNote1 = adeNote1;
	}
	public StringType getAdeNote2() {
		return adeNote2;
	}
	public void setAdeNote2(StringType adeNote2) {
		this.adeNote2 = adeNote2;
	}
	public StringType getAdeNote3() {
		return adeNote3;
	}
	public void setAdeNote3(StringType adeNote3) {
		this.adeNote3 = adeNote3;
	}
	public StringType getAdeNote4() {
		return adeNote4;
	}
	public void setAdeNote4(StringType adeNote4) {
		this.adeNote4 = adeNote4;
	}
	public StringType getAdeObbi1() {
		return adeObbi1;
	}
	public void setAdeObbi1(StringType adeObbi1) {
		this.adeObbi1 = adeObbi1;
	}
	public StringType getAdeObbi2() {
		return adeObbi2;
	}
	public void setAdeObbi2(StringType adeObbi2) {
		this.adeObbi2 = adeObbi2;
	}
	public StringType getAdeObbi3() {
		return adeObbi3;
	}
	public void setAdeObbi3(StringType adeObbi3) {
		this.adeObbi3 = adeObbi3;
	}
	public StringType getAdeObbi4() {
		return adeObbi4;
	}
	public void setAdeObbi4(StringType adeObbi4) {
		this.adeObbi4 = adeObbi4;
	}
	public StringType getAdeOrit1() {
		return adeOrit1;
	}
	public void setAdeOrit1(StringType adeOrit1) {
		this.adeOrit1 = adeOrit1;
	}
	public StringType getAdeOrit2() {
		return adeOrit2;
	}
	public void setAdeOrit2(StringType adeOrit2) {
		this.adeOrit2 = adeOrit2;
	}
	public StringType getAdeOrit3() {
		return adeOrit3;
	}
	public void setAdeOrit3(StringType adeOrit3) {
		this.adeOrit3 = adeOrit3;
	}
	public StringType getAdeOrit4() {
		return adeOrit4;
	}
	public void setAdeOrit4(StringType adeOrit4) {
		this.adeOrit4 = adeOrit4;
	}
	public StringType getAdeTogg1() {
		return adeTogg1;
	}
	public void setAdeTogg1(StringType adeTogg1) {
		this.adeTogg1 = adeTogg1;
	}
	public StringType getAdeTogg2() {
		return adeTogg2;
	}
	public void setAdeTogg2(StringType adeTogg2) {
		this.adeTogg2 = adeTogg2;
	}
	public StringType getAdeTogg3() {
		return adeTogg3;
	}
	public void setAdeTogg3(StringType adeTogg3) {
		this.adeTogg3 = adeTogg3;
	}
	public StringType getAdeTogg4() {
		return adeTogg4;
	}
	public void setAdeTogg4(StringType adeTogg4) {
		this.adeTogg4 = adeTogg4;
	}
	public StringType getDescErr() {
		return descErr;
	}
	public void setDescErr(StringType descErr) {
		this.descErr = descErr;
	}
	public StringType getDesDime1() {
		return desDime1;
	}
	public void setDesDime1(StringType desDime1) {
		this.desDime1 = desDime1;
	}
	public StringType getDesDime2() {
		return desDime2;
	}
	public void setDesDime2(StringType desDime2) {
		this.desDime2 = desDime2;
	}
	public StringType getDesDime3() {
		return desDime3;
	}
	public void setDesDime3(StringType desDime3) {
		this.desDime3 = desDime3;
	}
	public StringType getDesDime4() {
		return desDime4;
	}
	public void setDesDime4(StringType desDime4) {
		this.desDime4 = desDime4;
	}
	public StringType getDesFreq1() {
		return desFreq1;
	}
	public void setDesFreq1(StringType desFreq1) {
		this.desFreq1 = desFreq1;
	}
	public StringType getDesFreq2() {
		return desFreq2;
	}
	public void setDesFreq2(StringType desFreq2) {
		this.desFreq2 = desFreq2;
	}
	public StringType getDesFreq3() {
		return desFreq3;
	}
	public void setDesFreq3(StringType desFreq3) {
		this.desFreq3 = desFreq3;
	}
	public StringType getDesFreq4() {
		return desFreq4;
	}
	public void setDesFreq4(StringType desFreq4) {
		this.desFreq4 = desFreq4;
	}
	public StringType getDesModv1() {
		return desModv1;
	}
	public void setDesModv1(StringType desModv1) {
		this.desModv1 = desModv1;
	}
	public StringType getDesModv2() {
		return desModv2;
	}
	public void setDesModv2(StringType desModv2) {
		this.desModv2 = desModv2;
	}
	public StringType getDesModv3() {
		return desModv3;
	}
	public void setDesModv3(StringType desModv3) {
		this.desModv3 = desModv3;
	}
	public StringType getDesModv4() {
		return desModv4;
	}
	public void setDesModv4(StringType desModv4) {
		this.desModv4 = desModv4;
	}
	public StringType getDesObbi1() {
		return desObbi1;
	}
	public void setDesObbi1(StringType desObbi1) {
		this.desObbi1 = desObbi1;
	}
	public StringType getDesObbi2() {
		return desObbi2;
	}
	public void setDesObbi2(StringType desObbi2) {
		this.desObbi2 = desObbi2;
	}
	public StringType getDesObbi3() {
		return desObbi3;
	}
	public void setDesObbi3(StringType desObbi3) {
		this.desObbi3 = desObbi3;
	}
	public StringType getDesObbi4() {
		return desObbi4;
	}
	public void setDesObbi4(StringType desObbi4) {
		this.desObbi4 = desObbi4;
	}
	public StringType getDesOrit1() {
		return desOrit1;
	}
	public void setDesOrit1(StringType desOrit1) {
		this.desOrit1 = desOrit1;
	}
	public StringType getDesOrit2() {
		return desOrit2;
	}
	public void setDesOrit2(StringType desOrit2) {
		this.desOrit2 = desOrit2;
	}
	public StringType getDesOrit3() {
		return desOrit3;
	}
	public void setDesOrit3(StringType desOrit3) {
		this.desOrit3 = desOrit3;
	}
	public StringType getDesOrit4() {
		return desOrit4;
	}
	public void setDesOrit4(StringType desOrit4) {
		this.desOrit4 = desOrit4;
	}
	public StringType getDesTogg1() {
		return desTogg1;
	}
	public void setDesTogg1(StringType desTogg1) {
		this.desTogg1 = desTogg1;
	}
	public StringType getDesTogg2() {
		return desTogg2;
	}
	public void setDesTogg2(StringType desTogg2) {
		this.desTogg2 = desTogg2;
	}
	public StringType getDesTogg3() {
		return desTogg3;
	}
	public void setDesTogg3(StringType desTogg3) {
		this.desTogg3 = desTogg3;
	}
	public StringType getDesTogg4() {
		return desTogg4;
	}
	public void setDesTogg4(StringType desTogg4) {
		this.desTogg4 = desTogg4;
	}
	public StringType getErrProf1() {
		return errProf1;
	}
	public void setErrProf1(StringType errProf1) {
		this.errProf1 = errProf1;
	}
	public StringType getErrProf2() {
		return errProf2;
	}
	public void setErrProf2(StringType errProf2) {
		this.errProf2 = errProf2;
	}
	public StringType getErrProf3() {
		return errProf3;
	}
	public void setErrProf3(StringType errProf3) {
		this.errProf3 = errProf3;
	}
	public StringType getErrProf4() {
		return errProf4;
	}
	public void setErrProf4(StringType errProf4) {
		this.errProf4 = errProf4;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public void setAdeDisi1(StringType adeDisi1) {
		this.adeDisi1 = adeDisi1;
	}
	public void setAdeDisi2(StringType adeDisi2) {
		this.adeDisi2 = adeDisi2;
	}
	public void setAdeDisi3(StringType adeDisi3) {
		this.adeDisi3 = adeDisi3;
	}
	public void setAdeDisi4(StringType adeDisi4) {
		this.adeDisi4 = adeDisi4;
	}
	public void setDesDisi1(StringType desDisi1) {
		this.desDisi1 = desDisi1;
	}
	public void setDesDisi2(StringType desDisi2) {
		this.desDisi2 = desDisi2;
	}
	public void setDesDisi3(StringType desDisi3) {
		this.desDisi3 = desDisi3;
	}
	public void setDesDisi4(StringType desDisi4) {
		this.desDisi4 = desDisi4;
	}
	public void setAdeAttCo(StringType adeAttCo) {
		this.adeAttCo = adeAttCo;
	}
	public void setAdeTitCo(StringType adeTitCo) {
		this.adeTitCo = adeTitCo;
	}

	public StringType getAdeDisi1() {
		return adeDisi1;
	}
	public StringType getAdeDisi2() {
		return adeDisi2;
	}
	public StringType getAdeDisi3() {
		return adeDisi3;
	}
	public StringType getAdeDisi4() {
		return adeDisi4;
	}
	public StringType getDesDisi1() {
		return desDisi1;
	}
	public StringType getDesDisi2() {
		return desDisi2;
	}
	public StringType getDesDisi3() {
		return desDisi3;
	}
	public StringType getDesDisi4() {
		return desDisi4;
	}
	public StringType getAdeAttCo() {
		return adeAttCo;
	}
	public StringType getAdeTitCo() {
		return adeTitCo;
	}
}
