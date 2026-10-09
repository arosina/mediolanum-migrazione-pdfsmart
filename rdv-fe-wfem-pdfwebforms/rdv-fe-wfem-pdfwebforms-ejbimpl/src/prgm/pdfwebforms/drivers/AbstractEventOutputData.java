package prgm.pdfwebforms.drivers;

import java.util.ArrayList;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractEventOutputData {
		
	private boolean				reloadPersons = false;
	private int 				invalidCompilationModes = 0;
	private boolean				signAll = true;
	
	private boolean				fieldsToRemoveManagedByDriver = false;
	private ArrayList<String>  	fieldsToRemove = new ArrayList<String>();
	
	private boolean				editableFieldsManagedByDriver = false;
	private ArrayList<String>  	editableFields = new ArrayList<String>();
	
	private boolean				uneditableFieldsManagedByDriver = false;
	private ArrayList<String>  	uneditableFields = new ArrayList<String>();
	
	private boolean				extraMandatoryFieldsManagedByDriver = false;
	private ArrayList<String>  	extraMandatoryFields = new ArrayList<String>();

	/**************************************************************************************************
	 * Imposta l'obbligatorietà su tutte le firme del pdf
	 * @Deprecated Se il primo pdf ha un driver le firme devono sempre essere apposte tutte
	 * Quali firmare deve essere impostato dal driver (fieldsToRemove)
	**************************************************************************************************/
	@Deprecated
	public void setSignAll(boolean signAll) {
		this.signAll = signAll;
	}
	/**************************************************************************************************
	 * Elenco dei campi da rimuovere dal pdf
	**************************************************************************************************/
	public ArrayList<String> getFieldsToRemove() {
		return fieldsToRemove;
	}
	/**************************************************************************************************
	 * Elenco dei campi da rendere editabili
	**************************************************************************************************/
	public ArrayList<String> getEditableFields() {
		return editableFields;
	}
	/**************************************************************************************************
	 * Elenco dei campi da rendere non editabili
	**************************************************************************************************/
	public ArrayList<String> getUneditableFields() {
		return uneditableFields;
	}
	/**************************************************************************************************
	 * Imposta il ricaricamento dei dati delle persone
	**************************************************************************************************/
	public void setReloadPersons(boolean reloadPersons) {
		this.reloadPersons = reloadPersons;
	}
	/**************************************************************************************************
	 * Imposta le modalità di compilazione non valide rispetto alla configurazione standard
	 * Da utilizzare nell'onPdfVerified e solo se quest'ultimo non è implementato nell'onVerifyPdf
	**************************************************************************************************/
	public void setInvalidCompilationModes(int invalidCompilationModes) {
		this.invalidCompilationModes = invalidCompilationModes;
	}

	@Deprecated
	public boolean isSignAll() {
		return signAll;
	}
	public void setFieldsToRemove(ArrayList<String> fieldsToRemove) {
		this.fieldsToRemove = fieldsToRemove;
	}
	public void setEditableFields(ArrayList<String> editableFields) {
		this.editableFields = editableFields;
	}
	public void setUneditableFields(ArrayList<String> uneditableFields) {
		this.uneditableFields = uneditableFields;
	}
	public boolean isReloadPersons() {
		return reloadPersons;
	}
	public int getInvalidCompilationModes() {
		return invalidCompilationModes;
	}
	public boolean isFieldsToRemoveManagedByDriver() {
		return fieldsToRemoveManagedByDriver;
	}
	public void setFieldsToRemoveManagedByDriver(boolean fieldsToRemoveManagedByDriver) {
		this.fieldsToRemoveManagedByDriver = fieldsToRemoveManagedByDriver;
	}
	public boolean isEditableFieldsManagedByDriver() {
		return editableFieldsManagedByDriver;
	}
	public void setEditableFieldsManagedByDriver(boolean editableFieldsManagedByDriver) {
		this.editableFieldsManagedByDriver = editableFieldsManagedByDriver;
	}
	public boolean isUneditableFieldsManagedByDriver() {
		return uneditableFieldsManagedByDriver;
	}
	public void setUneditableFieldsManagedByDriver(boolean uneditableFieldsManagedByDriver) {
		this.uneditableFieldsManagedByDriver = uneditableFieldsManagedByDriver;
	}
	public boolean isExtraMandatoryFieldsManagedByDriver() {
		return extraMandatoryFieldsManagedByDriver;
	}
	public void setExtraMandatoryFieldsManagedByDriver(boolean extraMandatoryFieldsManagedByDriver) {
		this.extraMandatoryFieldsManagedByDriver = extraMandatoryFieldsManagedByDriver;
	}
	public ArrayList<String> getExtraMandatoryFields() {
		return extraMandatoryFields;
	}
	public void setExtraMandatoryFields(ArrayList<String> extraMandatoryFields) {
		this.extraMandatoryFields = extraMandatoryFields;
	}
}
