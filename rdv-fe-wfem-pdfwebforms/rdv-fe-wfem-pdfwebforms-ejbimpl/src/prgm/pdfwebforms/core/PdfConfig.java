package prgm.pdfwebforms.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfConfig {

	public static List<String> getParamAsStringArray(ClientSessionContext csc, String sezione, String parametro, String valoreDefault) throws Exception{
		return getParamAsStringArray(csc, sezione, parametro, valoreDefault, false);
	}

	public static List<String> getParamAsStringArray(ClientSessionContext csc, String sezione, String parametro, String valoreDefault, boolean inLike) throws Exception{
		StringType val = PdfConfig.getParamAsString(csc, sezione, parametro, inLike);
		if(val.isNull())
			val = new StringType(valoreDefault);
		String[] valArr = val.isNull() ? new String[]{} : val.toString().split("\\,");
		return new ArrayList<String>((Arrays.asList(valArr)));
	}
	
	public static StringType getParamAsString(ClientSessionContext csc, String sezione, String parametro) throws Exception{
		return executeQuery(csc, sezione, parametro, false);
	}

	public static StringType getParamAsString(ClientSessionContext csc, String sezione, String parametro, boolean inLike) throws Exception{
		return executeQuery(csc, sezione, parametro, inLike);
	}

	public static IntegerType getParamAsInt(ClientSessionContext csc, String sezione, String parametro) throws Exception{
		return new IntegerType(executeQuery(csc, sezione, parametro, false).toString());
	}

	public static DoubleType getParamAsDouble(ClientSessionContext csc, String sezione, String parametro) throws Exception{
		return new DoubleType(executeQuery(csc, sezione, parametro, false).toString());
	}

	public static BooleanType getParamAsBool(ClientSessionContext csc, String sezione, String parametro) throws Exception{
		return new BooleanType(executeQuery(csc, sezione, parametro, false).equals("S") ? true : false);
	}

	private static StringType executeQuery(ClientSessionContext csc, String sezione, String parametro, boolean inLike) throws Exception{
		try{
			StringType res = null;
			if(inLike){
				String v = "";
				String sql = "select VALORE from PDF_CONFIG where SEZIONE='"+sezione+"' and PARAMETRO like '"+parametro+"%' and ATTIVO='S'";
				ListType els = DAOObject.executeDynaQueryAccess(csc, "CEPE", sql, null, MapCommandDataModel.class).getResult();
				if(els != null && els.size() > 0){
					for(int i=0;i<els.size();i++){
						MapCommandDataModel m = (MapCommandDataModel)els.get(i);
						StringType p = (StringType)m.readProperty("valore");
						if(p != null && !p.isNull())
							v += p.toString()+",";
					}
				}
				if(v.endsWith(","))
					v = v.substring(0,v.length()-1);
				res = new StringType(v);
			}else{
				String sql = "select VALORE from PDF_CONFIG where SEZIONE='"+sezione+"' and PARAMETRO='"+parametro+"' and ATTIVO='S'";
				res = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", sql, null, StringType.class).getSingleResult();
			}
			if(res == null)
				res = new StringType();
			return res;
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	public static StringType getExtendedValue(ClientSessionContext csc, String sezione, String parametro) throws Exception{
		try{
			String sql = "select VALORE_ESTESO from PDF_CONFIG where SEZIONE='"+sezione+"' and PARAMETRO='"+parametro+"' and ATTIVO='S'";
			StringType res = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", sql, null, StringType.class).getSingleResult();
			if(res == null)
				res = new StringType();
			return res;
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}
	
}
