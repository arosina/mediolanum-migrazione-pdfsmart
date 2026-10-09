package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Gestisce l'apertura/creazione di un pdf partendo dalla tripletta
 * **********************************************************************************************/
public class ManageExternalPdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       
            
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			boolean replaceExternalInstance = pdfData.getReplaceExternalPdfInstance().booleanValue();
			pdfData.setReplaceExternalPdfInstance(new BooleanType());
			PdfDataModel originalInputData = (PdfDataModel)Tools.cloneObject(pdfData);
			
			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){ // Il pdf non esiste con le chiavi in input. Ne censiamo uno nuovo
				setNextCommandClass(NewPdf.class);
				return pdfData;
			}

			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																		"select STATO "+
																		"from PDF_INSTANCE "+
																		"where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
																		null, MapCommandDataModel.class);
			if(qRes.getResult().size() == 0){
				PdfModel pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Il pdf esiste come chiave ma non come dati");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			MapCommandDataModel pdfInfo = (MapCommandDataModel)qRes.getResult().get(0);
			StringType pdfStatus = (StringType)pdfInfo.readProperty("stato");
			if(pdfStatus.equals(PdfInstanceModel.STATO_BOZZA)){
				if(replaceExternalInstance)
					replaceExternalPdfInstance(userSessionContext, pdfData, originalInputData);					
				setNextCommandClass(OpenPdf.class);
			}else{
				setNextCommandClass(OpenCompletedPdf.class);
			}
			return pdfData;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void replaceExternalPdfInstance(UserSessionContext userSessionContext, PdfDataModel pdfData, 
											PdfDataModel originalInputData) throws DAOException, CommandException {
		new DAOObject(userSessionContext.getClientSessionContext(), PdfInstanceFacadeBean.DAO_XML_NAME).executeTableDeleteAccess("pdfInstance", pdfData);
		PdfModel newPdf = (PdfModel)new NewPdf().execute(userSessionContext, originalInputData);
		newPdf.getPdfData().setPdfInstanceId(pdfData.getPdfInstanceId());
		newPdf.getPdfData().setReplaceExternalPdfInstance(new BooleanType(true));
		new SavePdf().execute(userSessionContext, newPdf);	
	}
}
