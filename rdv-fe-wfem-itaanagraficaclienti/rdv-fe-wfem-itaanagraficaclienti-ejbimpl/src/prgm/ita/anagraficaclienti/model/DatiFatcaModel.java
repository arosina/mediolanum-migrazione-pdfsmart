package prgm.ita.anagraficaclienti.model;

import prgm.ita.anagraficaclienti.flussofatca.AbstractNavigatore;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiFatcaModel extends CommandDataModel{

	private StringType  	resultingFatcaStatus = new StringType();
	private StringType  	indizioForteNonSuperabile = new StringType();
	private StringType  	indizioForteSuperabile = new StringType();
	private StringType  	indizioDebole = new StringType();
	
	private BooleanType  	isUsPerson = new BooleanType();
	private StringType  	tin = new StringType();
	private BooleanType  	isAppliedFor = new BooleanType();
	private String		  	alertMessage = "";

	private StringType  	moduloFatca = new StringType();
	
	// Dati per flisso popup fatca
	private String			fatcaNavigatorName = null;
	private String			fatcaPopupName = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int pesoIndizio(){
		if(getIndizioForteNonSuperabile().equals("S"))
			return AbstractNavigatore.INDIZI_FORTI_NON_SUPERABILI;
		else if(getIndizioForteSuperabile().equals("S"))
			return AbstractNavigatore.INDIZI_FORTI_SUPERABILI;
		else if(getIndizioDebole().equals("S"))
			return AbstractNavigatore.INDIZI_DEBOLI;
		else
			return AbstractNavigatore.NESSUN_INDIZIO;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean indizioForteNonSuperabile(){
		return getIndizioForteNonSuperabile().equals("S") ? true : false;
	}
	public boolean indizioForteSuperabile(){
		return getIndizioForteSuperabile().equals("S") ? true : false;
	}
	public boolean indizioDebole(){
		return getIndizioDebole().equals("S") ? true : false;
	}

	public StringType getIndizioForteNonSuperabile() {
		return indizioForteNonSuperabile;
	}
	public void setIndizioForteNonSuperabile(StringType indizioForteNonSuperabile) {
		this.indizioForteNonSuperabile = indizioForteNonSuperabile;
	}
	public StringType getIndizioForteSuperabile() {
		return indizioForteSuperabile;
	}
	public void setIndizioForteSuperabile(StringType indizioForteSuperabile) {
		this.indizioForteSuperabile = indizioForteSuperabile;
	}
	public StringType getIndizioDebole() {
		return indizioDebole;
	}
	public void setIndizioDebole(StringType indizioDebole) {
		this.indizioDebole = indizioDebole;
	}

	public StringType getResultingFatcaStatus() {
		return resultingFatcaStatus;
	}

	public void setResultingFatcaStatus(StringType resultingFatcaStatus) {
		this.resultingFatcaStatus = resultingFatcaStatus;
	}

	public StringType getTin() {
		return tin;
	}

	public void setTin(StringType tin) {
		this.tin = tin;
	}

	public BooleanType getIsAppliedFor() {
		return isAppliedFor;
	}

	public void setIsAppliedFor(BooleanType isAppliedFor) {
		this.isAppliedFor = isAppliedFor;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public String getFatcaPopupName() {
		return fatcaPopupName;
	}

	public void setFatcaPopupName(String fatcaPopupName) {
		this.fatcaPopupName = fatcaPopupName;
	}

	public String getFatcaNavigatorName() {
		return fatcaNavigatorName;
	}

	public void setFatcaNavigatorName(String fatcaNavigatorName) {
		this.fatcaNavigatorName = fatcaNavigatorName;
	}

	public BooleanType getIsUsPerson() {
		return isUsPerson;
	}

	public void setIsUsPerson(BooleanType isUsPerson) {
		this.isUsPerson = isUsPerson;
	}
	public StringType getModuloFatca() {
		return moduloFatca;
	}
	public void setModuloFatca(StringType moduloFatca) {
		this.moduloFatca = moduloFatca;
	}

}
