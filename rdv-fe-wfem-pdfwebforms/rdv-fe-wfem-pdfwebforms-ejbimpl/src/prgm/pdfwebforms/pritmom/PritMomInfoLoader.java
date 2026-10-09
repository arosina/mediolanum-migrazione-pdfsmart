package prgm.pdfwebforms.pritmom;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/********************************************************************************/
/********************************************************************************/
public class PritMomInfoLoader {

	/********************************************************************************/
	/********************************************************************************/
	public static PritMomInfo loadPritMomInfo(ClientSessionContext csc, PdfAnagModel pdfAnag, String chiave){
		
		if(chiave == null || chiave.length() == 0)
			return null;
		
		try{
			ListType infosPrit = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
											"select PRIT_C_PRODOTTO, PRIT_C_OPERAZIONE "+
											"from PDF_INFO_PRIT "+
											"where (PDF_CODE='"+pdfAnag.getPdfCode()+"' or PDF_CODE='"+pdfAnag.getPdfMomCode()+"') "+
											"and CHIAVE='"+chiave+"'", null, MapCommandDataModel.class).getResult();
			if(infosPrit != null && infosPrit.size() > 0){
				MapCommandDataModel infoPrit = (MapCommandDataModel)infosPrit.get(0);
				IntegerType codProd = (IntegerType)infoPrit.readProperty("pritCProdotto");
				IntegerType codOpe = (IntegerType)infoPrit.readProperty("pritCOperazione");
				if(codProd == null || codOpe == null || codProd.intValue() == 0 || codOpe.intValue() == 0)
					return null;
				PritMomInfo pritInfo = new PritMomInfo();
				pritInfo.setCodProdotto(codProd.intValue());
				pritInfo.setCodOperazione(codOpe.intValue());
				return pritInfo;
			}else{
				infosPrit = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
										"select PRIT_C_PRODOTTO, PRIT_C_OPERAZIONE "+
										"from PDF_INFO_PRIT "+
										"where PDF_CODE='*' "+
										"and CHIAVE='"+chiave+"'", null, MapCommandDataModel.class).getResult();
				if(infosPrit != null && infosPrit.size() > 0){
					MapCommandDataModel infoPrit = (MapCommandDataModel)infosPrit.get(0);
					IntegerType codProd = (IntegerType)infoPrit.readProperty("pritCProdotto");
					IntegerType codOpe = (IntegerType)infoPrit.readProperty("pritCOperazione");
					if(codProd == null || codOpe == null || codProd.intValue() == 0 || codOpe.intValue() == 0)
						return null;
					PritMomInfo pritInfo = new PritMomInfo();
					pritInfo.setCodProdotto(codProd.intValue());
					pritInfo.setCodOperazione(codOpe.intValue());
					return pritInfo;
				}else{
					return null;
				}
			}
		}catch(DAOException daoe){
			return null;
		}catch(Throwable t){
			return null;
		}
		
	}
}
