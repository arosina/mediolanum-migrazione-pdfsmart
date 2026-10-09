package prgm.pdfwebforms.core;

import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProcessUtils {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadNacUrl(ClientSessionContext csc, PdfModel pdf){
		pdf.setNacUrlDomain("");
		if(pdf.isTestMode())
			return;
    	StringType nacurl = new StringType();
    	try{
    		nacurl = (StringType)DAOObject.executeDynaQueryAccess(csc, "PRGM", "select COMMAND_URL from OPEN_COMMANDS where COMMAND_NAME = 'NAC_POST_MESSAGE'", null, StringType.class).getSingleResult();
			if(nacurl == null) 
				nacurl = new StringType();
    	}catch(DAOException daoe){
    		daoe.printStackTrace();
    	}catch(Throwable t){
    		t.printStackTrace();
   		} 
		pdf.setNacUrlDomain(nacurl.toString());
	}
}
