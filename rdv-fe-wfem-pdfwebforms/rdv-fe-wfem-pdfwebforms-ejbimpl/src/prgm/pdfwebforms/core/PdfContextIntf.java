package prgm.pdfwebforms.core;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class PdfContextIntf extends CommandDataModel{
	
	private BooleanType isRete = new BooleanType();
	private BooleanType isSede = new BooleanType();
	private BooleanType isAssistenteFB = new BooleanType();
	private BooleanType isProtectionSpecialistEsterno = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsOtherUser(){
		return new BooleanType(!isRete.booleanValue() && !isSede.booleanValue() && !isAssistenteFB.booleanValue());
	}

	public BooleanType getIsRete() {
		return isRete;
	}

	public void setIsRete(BooleanType isRete) {
		this.isRete = isRete;
	}

	public BooleanType getIsSede() {
		return isSede;
	}

	public void setIsSede(BooleanType isSede) {
		this.isSede = isSede;
	}

	public BooleanType getIsAssistenteFB() {
		return isAssistenteFB;
	}

	public void setIsAssistenteFB(BooleanType isAssistenteFB) {
		this.isAssistenteFB = isAssistenteFB;
	}

	public BooleanType getIsProtectionSpecialistEsterno() {
		return isProtectionSpecialistEsterno;
	}

	public void setIsProtectionSpecialistEsterno(BooleanType isProtectionSpecialistEsterno) {
		this.isProtectionSpecialistEsterno = isProtectionSpecialistEsterno;
	}

}
