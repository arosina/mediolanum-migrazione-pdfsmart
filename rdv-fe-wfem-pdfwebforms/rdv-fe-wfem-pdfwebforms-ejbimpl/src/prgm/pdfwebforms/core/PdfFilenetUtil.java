package prgm.pdfwebforms.core;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfFilenetUtil {

	private static final String DAO_XML = "PdfWebForms.PdfAsStream";
	private static final String FILENETGUIDPROPNAME = "pdfFilenetGuid";
	
	/*******************************************************************/
	/*******************************************************************/
	private PdfFilenetUtil() {}
	
	/*******************************************************************/
	/*******************************************************************/
	public static StringType readInstanceFilenetGuid(ClientSessionContext csc, StringType pdfInstanceId) throws DAOException{
		String pdfFilenetGuidProp = FILENETGUIDPROPNAME;
		MapCommandDataModel m = new MapCommandDataModel();
		m.addProperty("pdfInstanceId", pdfInstanceId);
		m.addProperty(pdfFilenetGuidProp, new StringType());
		
		DAOObject dao = new DAOObject(csc,DAO_XML);
		dao.executeQueryAccess("getFilenetGuid", m);
		StringType filenetGuid = (StringType)m.readProperty(pdfFilenetGuidProp);
		if(filenetGuid == null || filenetGuid.isNull())
			return null;
		return filenetGuid;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static GenericCommandResponseModel readFilenetUrlFromGuid(ClientSessionContext csc, StringType filenetGuid) throws Exception{
		try {
			
			String pdfFilenetGuidProp = FILENETGUIDPROPNAME;
			String pdfFilenetUrlProp = "pdfFilenetUrl";
			MapCommandDataModel m = new MapCommandDataModel();
			m.addProperty(pdfFilenetGuidProp, filenetGuid);
			m.addProperty(pdfFilenetUrlProp, new StringType());
			
			new DAOObject(csc,DAO_XML).executeOSBAccess("getUrlFilenetFromGuid", m);				
			String pdfFilenetUrl = m.readProperty(pdfFilenetUrlProp).toString();
			if(pdfFilenetUrl.length() == 0)
				throw new Exception("\n\nIl servizio di recupero delle url Filenet non ha restituito alcun dato");
			
			String respContent = "<script>location.href='"+pdfFilenetUrl+"';</script>";
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("text/html");
			resp.setContentLength(respContent.length());
			resp.setContent(respContent.getBytes());				
			return resp;
		}catch(DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] readFilenetContentFromGuid(ClientSessionContext csc, StringType filenetGuid) throws Exception{
		try {
			
			String pdfFilenetGuidProp = FILENETGUIDPROPNAME;
			String pdfFilenetBase64Prop = "pdfFilenetBase64";
			MapCommandDataModel m = new MapCommandDataModel();
			m.addProperty(pdfFilenetGuidProp, filenetGuid);
			m.addProperty(pdfFilenetBase64Prop, new StringType());
			
			new DAOObject(csc,DAO_XML).executeOSBAccess("getContentFilenetFromGuid", m);				
			String pdfFilenetBase64 = m.readProperty(pdfFilenetBase64Prop).toString();
			if(pdfFilenetBase64.length() == 0)
				throw new Exception("\n\nIl servizio di recupero del content Filenet non ha restituito alcun dato");
					
			return Tools.decodeBase64(pdfFilenetBase64.getBytes());
		}catch(DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}
	
}
