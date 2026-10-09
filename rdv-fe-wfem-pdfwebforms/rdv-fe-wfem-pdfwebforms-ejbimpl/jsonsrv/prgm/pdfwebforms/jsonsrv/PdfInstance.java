package prgm.pdfwebforms.jsonsrv;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

import prgm.pdfwebforms.model.PdfInstanceModel;

/*******************************************************************/
/*******************************************************************/
public class PdfInstance extends BusinessCommand {
	
	/********************************************************************************/
	/********************************************************************************/
    public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
    	
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfInstanceModel model = (PdfInstanceModel)dataModel;

			loadPdfInstanceId(csc, model);
			
			DAOTableResultModel tRes = null;
			if(!model.getPdfInstanceId().isNull()){
				tRes = new DAOObject(csc,"PdfWebForms.PdfInstance").executeTableLoadAccess("pdfInstance",model);
				if(tRes.getResult().intValue() == 0)
					model.setPdfInstanceId(new StringType());
			}

			StringBuffer jsonObj = new StringBuffer();

			if(!model.getCallback().isNull())
				jsonObj.append(model.getCallback()+"(");
			
			jsonObj.append("{\n");
			
			jsonObj.append("\"pdfInstanceId\":\""+model.getPdfInstanceId()+"\",\n");
			jsonObj.append("\"pdfStatus\":\""+(model.getPdfInstanceId().isNull()?"":model.getPdfStatus())+"\",\n");
			
			jsonObj.append("\"pdfDescr\":\""+model.getPdfDescr()+"\",\n");

			jsonObj.append("\"pdfBarcode\":\""+model.getPdfBarcode()+"\",\n");
			jsonObj.append("\"pdfCompilationMode\":\""+model.getPdfCompilationMode()+"\",\n");
			jsonObj.append("\"codAgente\":\""+model.getCodAgente()+"\",\n");
			
			jsonObj.append("\"cli1_ndg\":\""+model.getCli1().getNdg()+"\",\n");
			jsonObj.append("\"cli1_idCensimento\":\""+model.getCli1().getIdCensimento()+"\",\n");
			jsonObj.append("\"cli1_codPotenziale\":\""+model.getCli1().getCodPotenziale()+"\",\n");
			jsonObj.append("\"cli1_cognome\":\""+model.getCli1().getCognome()+"\",\n");
			jsonObj.append("\"cli1_nome\":\""+model.getCli1().getNome()+"\",\n");

			jsonObj.append("\"numeroContratto\":\""+model.getNumeroContratto()+"\",\n");
			jsonObj.append("\"numeroProposta\":\""+model.getNumeroProposta()+"\",\n");
			jsonObj.append("\"codDispositivaBMED\":\""+model.getCodDispositivaBMED()+"\",\n");
						
			jsonObj.append("\"pdfCreationUser\":\""+ model.getPdfCreationUser()+"\",\n");
			jsonObj.append("\"pdfCreationTime\":\""+getJsonTime(model.getPdfCreationTime())+"\",\n");
			jsonObj.append("\"pdfLastModUser\":\""+model.getPdfLastModUser()+"\",\n");
			jsonObj.append("\"pdfLastModTime\":\""+getJsonTime(model.getPdfLastModTime())+"\"\n");

			jsonObj.append("}");
			
			if(!model.getCallback().isNull())
				jsonObj.append(");\n");

			GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
			if(!model.getCallback().isNull())
				gcrm.setContentType("text/javascript");
			else
				gcrm.setContentType("application/json");
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
			gcrm.setContentLength(jsonObj.length());
			this.setGenericCommandResponse(gcrm);
			return model;
			
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
    	
    }

    /********************************************************************************/
    /********************************************************************************/
    public Class getInputViewClass() {
        return PdfInstanceModel.class;
    }
    
    /********************************************************************************/
    /********************************************************************************/
    private String getJsonTime(TimestampType time){
    	if(time.isNull())
    		return "";
    	return time.getAA()+"-"+time.getMM()+"-"+time.getGG()+"T"+time.getHH()+":"+time.getMI()+":"+time.getSS()+"Z";
    }

    /***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadPdfInstanceId(ClientSessionContext csc, PdfInstanceModel pdfInstanceKey) throws DAOException{
		if(!pdfInstanceKey.getPdfInstanceId().isNull())
			return;
		StringType pdfInstanceId = null;
		if(!pdfInstanceKey.getExternalEntityAppl().isNull() && !pdfInstanceKey.getExternalEntityName().isNull() && !pdfInstanceKey.getExternalEntityKey().isNull()){
			pdfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
															"select top 1 PDF_INSTANCE_ID from PDF_INSTANCE where"+
																	" EXTERNAL_ENTITY_APPL = '"+pdfInstanceKey.getExternalEntityAppl()+"' and"+
																	" EXTERNAL_ENTITY_NAME = '"+pdfInstanceKey.getExternalEntityName()+"' and"+
																	" EXTERNAL_ENTITY_KEY = '"+pdfInstanceKey.getExternalEntityKey()+"'",
															null, StringType.class).getSingleResult();
		}
		if(pdfInstanceId == null)
			pdfInstanceId = new StringType();
		pdfInstanceKey.setPdfInstanceId(pdfInstanceId);
		return;
	}
    
}
