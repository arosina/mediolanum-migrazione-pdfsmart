package prgm.ita.p.dac.model;

import java.util.Vector;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class AgenteModel extends ClienteModel {
	
	private StringType  codAgente = new StringType();
	private StringType  codRete = new StringType();
	private StringType  nominativo = new StringType();
	private StringType  serverReplica = new StringType();
	
	private StringType  codArea = new StringType();
	private StringType  codAgenzia = new StringType();
	private StringType  indirizzoAgenzia = new StringType();
	private StringType  comuneAgenzia = new StringType();
	private StringType  capAgenzia = new StringType();
	private StringType  provAgenzia = new StringType();
	private StringType  telAgenzia = new StringType();
	private StringType  faxAgenzia = new StringType();
	private StringType  contrattoAgente = new StringType();
	private BooleanType canMakeForOtherFb = new BooleanType(true);
	private StringType  tipoAgente = new StringType();
	private BooleanType isIscrittoOAM = new BooleanType();
	
	// Per layout/gestione interna
	private Vector 		changedProps = new Vector();
	private String		msgAgenteNonIscrittoAlboOAM = "";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Vector initChangedProps(AgenteModel other){
		changedProps.removeAllElements();
		
		if(!isEqual(other))
			changedProps.add("codAgente");
		
		return changedProps;		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(AgenteModel other){
		if(!getCodAgente().toString().equals(other.getCodAgente().toString()))
			return false;
		return true;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public StringType getDatiAgenzia(){
		StringBuffer res = new StringBuffer();
		res.append(getIndirizzoAgenzia()+" - "+getCapAgenzia()+" "+getComuneAgenzia()+" ("+getProvAgenzia()+") - Tel. "+getTelAgenzia()+" - Fax. "+getFaxAgenzia());
		return new StringType(res.toString());
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public StringType getNominativoConCodice(){
		if(getCodAgente().isNull())
			return new StringType();
		return new StringType(getCodAgente()+" - "+getNominativo());
	}
	
	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public StringType getCodArea() {
		return codArea;
	}

	public void setCodArea(StringType codArea) {
		this.codArea = codArea;
	}

	public StringType getCodAgenzia() {
		return codAgenzia;
	}

	public void setCodAgenzia(StringType codAgenzia) {
		this.codAgenzia = codAgenzia;
	}

	public StringType getNominativo() {
		return nominativo;
	}

	public void setNominativo(StringType nominativo) {
		this.nominativo = nominativo;
	}

	public StringType getIndirizzoAgenzia() {
		return indirizzoAgenzia;
	}

	public void setIndirizzoAgenzia(StringType indirizzoAgenzia) {
		this.indirizzoAgenzia = indirizzoAgenzia;
	}

	public StringType getComuneAgenzia() {
		return comuneAgenzia;
	}

	public void setComuneAgenzia(StringType comuneAgenzia) {
		this.comuneAgenzia = comuneAgenzia;
	}

	public StringType getCapAgenzia() {
		return capAgenzia;
	}

	public void setCapAgenzia(StringType capAgenzia) {
		this.capAgenzia = capAgenzia;
	}

	public StringType getProvAgenzia() {
		return provAgenzia;
	}

	public void setProvAgenzia(StringType provAgenzia) {
		this.provAgenzia = provAgenzia;
	}

	public StringType getTelAgenzia() {
		return telAgenzia;
	}

	public void setTelAgenzia(StringType telAgenzia) {
		this.telAgenzia = telAgenzia;
	}

	public StringType getFaxAgenzia() {
		return faxAgenzia;
	}

	public void setFaxAgenzia(StringType faxAgenzia) {
		this.faxAgenzia = faxAgenzia;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public BooleanType getCanMakeForOtherFb() {
		return canMakeForOtherFb;
	}

	public void setCanMakeForOtherFb(BooleanType canMakeForOtherFb) {
		this.canMakeForOtherFb = canMakeForOtherFb;
	}

	public StringType getContrattoAgente() {
		return contrattoAgente;
	}

	public void setContrattoAgente(StringType contrattoAgente) {
		this.contrattoAgente = contrattoAgente;
	}

	public StringType getTipoAgente() {
		return tipoAgente;
	}

	public void setTipoAgente(StringType tipoAgente) {
		this.tipoAgente = tipoAgente;
	}

	public BooleanType getIsIscrittoOAM() {
		return isIscrittoOAM;
	}

	public void setIsIscrittoOAM(BooleanType isIscrittoOAM) {
		this.isIscrittoOAM = isIscrittoOAM;
	}

	public String getMsgAgenteNonIscrittoAlboOAM() {
		return msgAgenteNonIscrittoAlboOAM;
	}

	public void setMsgAgenteNonIscrittoAlboOAM(String msgAgenteNonIscrittoAlboOAM) {
		this.msgAgenteNonIscrittoAlboOAM = msgAgenteNonIscrittoAlboOAM;
	}

}
