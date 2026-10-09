package prgm.pdfwebforms.backend;

import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.ConcurrencyModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************/
/********************************************************************************/
public class PdfUtil {

	public static String FUNZIONE_NON_DISPONIBILE = "\n\nLa funzione non è disponibile per il tipo di utente collegato";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void putError(PdfDataModel pdfData, List<String> fieldNames, String errmsg){
		for(String fieldName : fieldNames)
			putError(pdfData, fieldName, errmsg);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void putError(PdfDataModel pdfData, String fieldName, String errmsg){
		AbstractType field = pdfData.read(fieldName);
		if(field != null)
			field.addTypeError(errmsg);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean testConcorrenzaBozzaIsOk(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		pdf.setConcurrencyViolationFounded(false);
		
		if(pdf.isTestMode() || pdf.getPdfData().getIsVolatile().booleanValue() || pdf.getPdfData().getPdfInstanceId().isNull())
			return true;
		
		try{
			String errMsg = null;
			DAOObject dao = new DAOObject(csc, PdfInstanceFacadeBean.DAO_XML_NAME);
			ConcurrencyModel cm = new ConcurrencyModel();
			if(!pdf.getPdfData().getIdCarrello().isNull() && pdf.getBasket() != null){
				String sempltSuffix = pdf.isInBasketSemplt() ? "Semplt" : "";
				cm.setIdCarrello(pdf.getPdfData().getIdCarrello());
				DAOTableResultModel tRes = dao.executeTableLoadAccess("concurrencyBasketData"+sempltSuffix, cm);
				if(tRes.getResult().intValue() == 0) {
					errMsg = "Attenzione, la proposta è stata cancellata, non è possibile procedere";
				}else if(!pdf.getBasket().getDataOraUltimaModifica().isNull() && !pdf.getBasket().getDataOraUltimaModifica().toString().equals(cm.getDataOraUltimaModifica().toString())) {
					errMsg = "La proposta è stata modificata dal FB "+cm.getCodUtenteUltimaModifica().toString()+" pertanto non è possibile proseguire. E' necessario uscire e riavviare la compilazione";
				}
				if(errMsg != null) {
					pdf.setInitialErrorMsg(errMsg);
					pdf.setConcurrencyViolationFounded(true);
					return false;
				}
			}
			
			cm.setPdfInstanceId(pdf.getPdfData().getPdfInstanceId());
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadConcurrencyData", cm);
			if(qRes.getResult().size() == 0) {
				errMsg = "Attenzione, la bozza è stata cancellata, non è possibile procedere";
			}else if(!cm.getStato().equals(PdfInstanceModel.STATO_BOZZA)){
				errMsg = "Attenzione, la compilazione di questo modulo è già stata completata, non è possibile procedere";
			}else if(!pdf.getDataOraUltimaModifica().isNull() && !pdf.getDataOraUltimaModifica().toString().equals(cm.getDataOraUltimaModifica().toString())) {
				errMsg = "La bozza è stata modificata dal FB "+cm.getCodUtenteUltimaModifica().toString()+" pertanto non è possibile proseguire. E' necessario uscire e riavviare la compilazione";
			}
			if(errMsg != null) {
				pdf.setInitialErrorMsg(errMsg);
				pdf.setConcurrencyViolationFounded(true);
				return false;
			}
			return true;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
		
	}

	/********************************************************************************/
	/********************************************************************************/
	public static boolean testConcorrenzaCopernicoIsOk(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		try{
			StringType stato = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																"select STATO from PDF_INSTANCE where PDF_INSTANCE_ID = '"+pdf.getPdfData().getPdfInstanceId()+"'", 
																null, StringType.class).getSingleResult();
			if(!stato.equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE)){
				pdf.addCommandError("Attenzione, la firma di questo modulo è già stata completata, non è possibile procedere");
				return false;
			}
			return true;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
		
	}
}
