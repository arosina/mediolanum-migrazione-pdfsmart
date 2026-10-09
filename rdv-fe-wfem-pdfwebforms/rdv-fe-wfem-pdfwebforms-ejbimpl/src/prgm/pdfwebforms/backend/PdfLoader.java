package prgm.pdfwebforms.backend;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/********************************************************************************/
/********************************************************************************/
public class PdfLoader implements Runnable{
	
	private ClientSessionContext 	csc;
	private CountDownLatch 			latch;
	private ArrayList<PdfAnagModel> pdfAnags;
	private int						pdfAnagIndex;
	private PdfModel				pdf;
	private PdfDataModel 			pdfData;
	private boolean 				isOnWork;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfLoader(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, boolean isOnWork){
		this.csc = csc;
		this.pdf = pdf;
		this.pdfData = pdfData;
		this.isOnWork = isOnWork;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfLoader(ClientSessionContext csc, CountDownLatch latch, ArrayList<PdfAnagModel> pdfAnags, 
					  int pdfAnagIndex, PdfModel pdf, PdfDataModel pdfData, boolean isOnWork){
		this.csc = csc;
		this.latch = latch;
		this.pdfAnags = pdfAnags;
		this.pdfAnagIndex = pdfAnagIndex;
		this.pdf = pdf;
		this.pdfData = pdfData;
		this.isOnWork = isOnWork;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void run() {
		try{
			pdfAnags.set(pdfAnagIndex, PdfLoader.loadAndInitPdfData(csc, pdf, pdfData, isOnWork));
		}catch(Throwable t){
			pdfAnags.set(pdfAnagIndex, null);
		}finally{
    		 latch.countDown();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfAnagModel loadAndInitPdfData(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, boolean isOnWork) throws Exception{
		try{
			PdfAnagModel pdfAnag = new PdfAnagModel();
			pdfAnag.setPdfId(new StringType(pdfData.getPdfId().toString()));
			pdfAnag.setPdfCode(new StringType(pdfData.getPdfCode().toString()));
			pdfAnag.setPdfPublicationId(new IntegerType(pdfData.getPdfPublicationId().intValue()));
			pdfAnag.setIsOnWork(new BooleanType(isOnWork));
			new DAOObject(csc,PdfInstanceFacadeBean.DAO_XML_NAME).executeQueryAccess("loadPdfAnagData",pdfAnag);
			new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME).fillCodDesc(pdfAnag, false);
			
			if(pdf.getIsSede().booleanValue()){
				pdfAnag.setTipoPriips(new StringType());
				pdfAnag.setHasDataSottoscrizioneOggi(new BooleanType(false));
			}
			
			// Nel carrello le preferenze non devono essere mai gestite
			if(!pdf.getPdfData().getIdCarrello().isNull()) {
				pdfAnag.setHasPreferenzeInPriips(new BooleanType(false));
			}else if(pdf.getPdfData().getIsSwitch().booleanValue()) { // Mentre nello switch si a meno che non risultino disabilitate
				pdfAnag.setHasPreferenzeInPriips(new BooleanType(false));
				boolean preferenzeSwitchDisabilitato = PdfConfig.getParamAsBool(csc, "SOSTITUZIONI", "DISABILITA_PREFERENZE_IN_SWITCH").booleanValue();
				if(!preferenzeSwitchDisabilitato)
					pdfAnag.setHasPreferenzeInPriips(new BooleanType(true));
			}
			
			// Gestione pilota SOSTITUZIONI
			StringType pilota = PdfConfig.getParamAsString(csc, "SOSTITUZIONI", "CODICI_AGENTE_GRUPPO_PILOTA", true);
			if(pdf.getIsSede().booleanValue() || (csc.getUserCode() != null && !pilota.isNull() && pilota.toString().indexOf(Tools.fillSx(csc.getUserCode(),'0',10)) < 0)) {
				pdfAnag.setHasPreferenzeInPriips(new BooleanType(false));
				pdfAnag.setOnStockProcessBatchQueue(new BooleanType(false));
			}
			
			// Gestione vendita come Bmed, solo sul primo pdf
			if(pdfData.getPdfIndex().intValue() == 0 && !pdfAnag.getPdfMomCode().isNull()) {
				StringType codiciMomVendutiComeBmed = PdfConfig.getParamAsString(csc, "PROCESSO_DI_VENDITA", "CODICI_MOM_VENDUTI_DA_"+pdf.getPdfData().getCodRuoloImpersonato().toString().toUpperCase()+"_COME_BMED", true);
				if(codiciMomVendutiComeBmed.toString().indexOf(pdfAnag.getPdfMomCode().toString()) >= 0)
					pdfAnag.setVenditaComeBmed(true);
			}
			
			pdfData.setPdfInfos(PdfEngine.inspectPdfInfos(csc,pdfData,isOnWork));
			pdfData.initPdfAnag(pdfAnag);

			if(pdfData.getIsInitCalled() == null)
				PdfEngine.initPdfFieldInfosPropertyValues(pdfData, true);
			else
				PdfEngine.initPdfFieldInfosPropertyValues(pdfData, false);
			
			// Se la modalità del dataentry è readonly non possiamo lasciare l'obbligatorietà dei campi
			// perchè tutto viene pilotato dal chiamante
			if(pdf.getPdfData().getReadonlyDataentry().booleanValue())
				removeMandatory(pdfData);
			
			return pdfAnag;
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw new Exception(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void removeMandatory(PdfDataModel pdfData) {
		PdfInfos pdfInfo = pdfData.getPdfInfos();
		if(pdfInfo != null) {
			for(PdfFieldInfos fi : pdfInfo.getFieldInfos())
				fi.mandatory=false;
		}
	}

}
		
