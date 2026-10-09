package prgm.pdfwebforms.drivers.io.prit;

import java.util.ArrayList;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvidePritDataResponse{
	
	private boolean 							includiRigheConfigurate = true;
	private boolean 							nonInserireAlcunaRigaDiPrit = false;
	private String								chiavePritRigaConfigurata = null;
	private ArrayList<RigaPrit> 				righeDiPrit = new ArrayList<RigaPrit>();
	private ArrayList<MezzoPagamentoRigaPrit> 	mezziDiPagamentoRigaConfigurata = new ArrayList<MezzoPagamentoRigaPrit>();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addRigaDiPrit(RigaPrit rigaDiPrit){
		getRigheDiPrit().add(rigaDiPrit);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addMezzoDiPagamentoRigaConfigurata(MezzoPagamentoRigaPrit mezzoPg){
		getMezziDiPagamentoRigaConfigurata().add(mezzoPg);
	}

	public boolean isIncludiRigheConfigurate() {
		return includiRigheConfigurate;
	}

	public void setIncludiRigheConfigurate(boolean includiRigheConfigurate) {
		this.includiRigheConfigurate = includiRigheConfigurate;
	}

	public ArrayList<RigaPrit> getRigheDiPrit() {
		return righeDiPrit;
	}

	public void setRigheDiPrit(ArrayList<RigaPrit> righeDiPrit) {
		this.righeDiPrit = righeDiPrit;
	}

	public ArrayList<MezzoPagamentoRigaPrit> getMezziDiPagamentoRigaConfigurata() {
		return mezziDiPagamentoRigaConfigurata;
	}

	public void setMezziDiPagamentoRigaConfigurata(ArrayList<MezzoPagamentoRigaPrit> mezziDiPagamentoRigaConfigurata) {
		this.mezziDiPagamentoRigaConfigurata = mezziDiPagamentoRigaConfigurata;
	}

	public String getChiavePritRigaConfigurata() {
		return chiavePritRigaConfigurata;
	}

	public void setChiavePritRigaConfigurata(String chiavePritRigaConfigurata) {
		this.chiavePritRigaConfigurata = chiavePritRigaConfigurata;
	}

	public boolean isNonInserireAlcunaRigaDiPrit() {
		return nonInserireAlcunaRigaDiPrit;
	}

	public void setNonInserireAlcunaRigaDiPrit(boolean nonInserireAlcunaRigaDiPrit) {
		this.nonInserireAlcunaRigaDiPrit = nonInserireAlcunaRigaDiPrit;
	}

}
