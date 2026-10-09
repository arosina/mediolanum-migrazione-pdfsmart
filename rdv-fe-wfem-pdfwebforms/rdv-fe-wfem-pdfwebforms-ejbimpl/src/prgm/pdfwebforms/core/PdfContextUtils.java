package prgm.pdfwebforms.core;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfContextUtils {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initContext(ClientSessionContext csc, PdfContextIntf model) throws DAOException{
		model.setIsRete(new BooleanType(csc.isRete()));
		model.setIsSede(new BooleanType(csc.isSede()));
		model.setIsAssistenteFB(new BooleanType(csc.isAssistenteFB()));
		if(model.getIsProtectionSpecialistEsterno() == null) {
			model.setIsProtectionSpecialistEsterno(new BooleanType());
			if(csc.isRete() && csc.getUserCode() != null && csc.getUserCode().length() > 0){
				MapCommandDataModel m = new MapCommandDataModel();
				m.addProperty("codAgenteCollegato", new StringType(csc.getUserCode()));
				BooleanType isProtectionSpecialistEsterno = (BooleanType)new DAOObject(csc, "PdfWebForms.PdfWebForms").executeQueryAccess("isProtectionSpecialistEsterno", m).getSingleResult();
				if(isProtectionSpecialistEsterno != null)
					model.setIsProtectionSpecialistEsterno(isProtectionSpecialistEsterno);
			}
		}
	}
}
