package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.mom.CostantiMOM;

/***********************************************************************************************/
/***********************************************************************************************/
public class KeyInstanceRetriever {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isKeyEmpty(PdfDataModel pdfData){
		return 	pdfData.getPdfInstanceId().isNull() && 
				pdfData.getCodDispositivaBMED().isNull() &&
				pdfData.getExternalEntityAppl().isNull() && 
				pdfData.getExternalEntityName().isNull() && 
				pdfData.getExternalEntityKey().isNull();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String loadPdfInstanceId(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException, CommandException{
		StringType pdfInstanceId = null;
		if(!pdfData.getPdfInstanceId().isNull()){
			pdfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
															"select PDF_INSTANCE_ID from PDF_INSTANCE where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
															null, StringType.class).getSingleResult();
			if(pdfInstanceId != null && !pdfInstanceId.isNull()){
				pdfData.setPdfInstanceId(pdfInstanceId);
				return null;
			}
		}

		if(!pdfData.getCodDispositivaBMED().isNull()){
			pdfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
															"select PDF_INSTANCE_ID from PDF_INSTANCE where COD_DISPOSITIVA_BMED = '"+pdfData.getCodDispositivaBMED()+"' and (EXTERNAL_ENTITY_NAME is null or EXTERNAL_ENTITY_NAME not like '"+CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_KEY_ENTITY_NAME_PREFIX+"%')",
															null, StringType.class).getSingleResult();
			if(pdfInstanceId != null && !pdfInstanceId.isNull()){
				pdfData.setPdfInstanceId(pdfInstanceId);
				return null;
			}
		}

		if(!pdfData.getExternalEntityAppl().isNull() && !pdfData.getExternalEntityName().isNull() && !pdfData.getExternalEntityKey().isNull()){
			pdfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
															"select top 1 PDF_INSTANCE_ID from PDF_INSTANCE where"+
																	" EXTERNAL_ENTITY_APPL = '"+pdfData.getExternalEntityAppl()+"' and"+
																	" EXTERNAL_ENTITY_NAME = '"+pdfData.getExternalEntityName()+"' and"+
																	" EXTERNAL_ENTITY_KEY = '"+pdfData.getExternalEntityKey()+"'",
															null, StringType.class).getSingleResult();
			if(pdfInstanceId != null && !pdfInstanceId.isNull()){
				pdfData.setPdfInstanceId(pdfInstanceId);
				return null;
			}
		}
		return "Nessuna istanza di pdf corrisponde alla chiave in input";
	}
	
}
