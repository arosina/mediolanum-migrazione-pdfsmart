package com.atosorigin.wfem.bo;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.businessobjects.dsws.reportengine.DiscretePromptValue;
import com.businessobjects.dsws.reportengine.FillPrompt;
import com.businessobjects.dsws.reportengine.FillPrompts;
import com.businessobjects.dsws.reportengine.PromptValue;

/***********************************************************************************************/
/***********************************************************************************************/
public class BoParamsVerifier {
	
	private static final String DB_PRGM = "PRGM";
	public static String THROW_EXCEPTION_PREFIX_MESSAGE = "[throw exception]";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private BoParamsVerifier() {
		throw new IllegalStateException(BoParamsVerifier.class.getName()+" class");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	static void verifyParams(ClientSessionContext csc, String docName, FillPrompts fillPrompts) throws Exception{
		if(csc.isSede())
			return;
		
		FillPrompt[] prompts = fillPrompts.getFillPromptList();
		for(int i=0;i<prompts.length;i++) {
			FillPrompt prompt = prompts[i];
			PromptValue[] dpv = prompt.getValues();
			if(dpv != null && dpv.length > 0) {
				DiscretePromptValue dv = (DiscretePromptValue)dpv[0];
				verifyGerParam(csc, docName, (i+1), dv.getValue());
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void verifyGerParam(ClientSessionContext csc, String docName, int paramCallPosition, String paramValue) throws Exception{
		
		try {
			StringType paramType = (StringType)DAOObject.executeDynaQueryAccess(csc,DB_PRGM,
															"select PARAM_TYPE, 1 from DYNA_REPORT_PARAMS "+
															"where REPORT_CODE = '"+docName+"' "+
															"and PARAM_CALL_POSITION = "+paramCallPosition+" "+
															"union "+
															"select PARAM_TYPE, 2 from DYNA_REPORT_PARAMS_WORK "+
															"where REPORT_CODE = '"+docName+"' "+
															"and PARAM_CALL_POSITION = "+paramCallPosition+" "+
															"order by 2",null,StringType.class).getSingleResult();
			if(paramType == null || paramType.isNull() || !paramType.toString().startsWith("Ger"))
				return;

			StringType agenteCollegato = new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10));
			StringType agenteParametro = new StringType(Tools.fillSx(paramValue,'0',10));
			if(agenteCollegato.equals(agenteParametro))
				return;
			
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc,DB_PRGM,
															"select dbname, ricercaAgenti from DYNA_REPORT_GERCOMM where GERCOMM_TYPE = '"+paramType+"'",
															null,MapCommandDataModel.class);
			if(qRes.getResult().size() <= 0)
				return;
			
			MapCommandDataModel res = (MapCommandDataModel)qRes.getResult().get(0);
			StringType dbname = (StringType)res.readProperty("dbname"); 
			StringType sql = (StringType)res.readProperty("ricercaagenti"); 
			BoAgeVerifierModel input = new BoAgeVerifierModel();
			input.setCodAgenteCollegato(agenteCollegato);
			input.getParams().setCodAgente(agenteParametro);
			qRes = DAOObject.executeDynaQueryAccess(csc,dbname.toString(),sql.toString(),input,MapCommandDataModel.class);
			if(qRes.getResult().size() <= 0)
				throw new Exception(THROW_EXCEPTION_PREFIX_MESSAGE+"\n\nATTENZIONE: il Family Banker selezionato non e' autorizzato alla visualizzazione dei dati.");
			
		}catch(DAOException daoe) {
			// Do nothing
		}
	}

}
