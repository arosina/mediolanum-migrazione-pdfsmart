package prgm.pdfwebforms.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfConfigModel extends CommandDataModel {

	private static final long serialVersionUID = 1L;

	private boolean configLoaded = false;
	private String applyedMsg = null;
	
	private PdfConfigParamModel praticheDigitaliDisabilitaCallSrvDispositiva = new PdfConfigParamModel("PRATICHE_DIGITALI",
																										"DISABILITA_CALL_SRV_DISPOSITIVA_PROC_MOM",
																										"Disattiva l'integrazione con il processa dispositiva per i pdf con processo orizzontale (default false)");
	private PdfConfigParamModel praticheDigitaliDisabilitaSemaforoMom = new PdfConfigParamModel("PRATICHE_DIGITALI",
																										"DISABILITA_SEMAFORO_MOM_INVIO_FABBRICA",
																										"Disattiva la verifica del semaforo MOM di attesa dell'ok alla lavorazione verso le fabbriche (default false)");

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfConfigModel(){
		CodDescDataList dl = new CodDescDataList();
		CodDescData d = new CodDescData();
		d.setCod(""); 
		d.setDescr("No");
		dl.addCodDescData(d);
		
		d = new CodDescData();
		d.setCod("S"); 
		d.setDescr("Si");
		dl.addCodDescData(d);

		getPraticheDigitaliDisabilitaCallSrvDispositiva().addCodDescField("valore", dl);
		getPraticheDigitaliDisabilitaSemaforoMom().addCodDescField("valore", dl);
	}
	
	public PdfConfigParamModel getPraticheDigitaliDisabilitaCallSrvDispositiva() {
		return praticheDigitaliDisabilitaCallSrvDispositiva;
	}

	public void setPraticheDigitaliDisabilitaCallSrvDispositiva(PdfConfigParamModel praticheDigitaliDisabilitaCallSrvDispositiva) {
		this.praticheDigitaliDisabilitaCallSrvDispositiva = praticheDigitaliDisabilitaCallSrvDispositiva;
	}

	public boolean isConfigLoaded() {
		return configLoaded;
	}

	public void setConfigLoaded(boolean configLoaded) {
		this.configLoaded = configLoaded;
	}

	public String getApplyedMsg() {
		return applyedMsg;
	}

	public void setApplyedMsg(String applyedMsg) {
		this.applyedMsg = applyedMsg;
	}

	public PdfConfigParamModel getPraticheDigitaliDisabilitaSemaforoMom() {
		return praticheDigitaliDisabilitaSemaforoMom;
	}

	public void setPraticheDigitaliDisabilitaSemaforoMom(PdfConfigParamModel praticheDigitaliDisabilitaSemaforoMom) {
		this.praticheDigitaliDisabilitaSemaforoMom = praticheDigitaliDisabilitaSemaforoMom;
	}
}
