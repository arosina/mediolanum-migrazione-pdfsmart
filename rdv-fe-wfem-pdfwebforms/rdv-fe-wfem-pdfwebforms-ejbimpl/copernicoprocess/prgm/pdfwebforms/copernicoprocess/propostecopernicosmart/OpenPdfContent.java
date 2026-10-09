package prgm.pdfwebforms.copernicoprocess.propostecopernicosmart;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfFilenetUtil;
import prgm.pdfwebforms.core.PdfNasUtil;

/*******************************************************************/
/*******************************************************************/
public class OpenPdfContent extends BusinessCommand implements MenuCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
             
			PropostaCopernicoKeyModel key = (PropostaCopernicoKeyModel)dataModel;
			if(key.getSorgente().isNull() || key.getChiave().isNull()){
				setGenericCommandResponse(htmlResp("La sorgente o la chiave non risulta specificata"));
				return null;
			}
			
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfCopernicoProcess");
			ByteArrayType pdf = null;
			if(key.getSorgente().equals("ANAGRAFICACLIENTI")){
				
				pdf = (ByteArrayType)dao.executeQueryAccess("loadPdfAnagrafica", key).getSingleResult();
				
			}else if(key.getSorgente().equals("PDFCOMPILABILI")){
				
				if(!verificaProprietaPdf(csc, dao, key)) {
					setGenericCommandResponse(htmlResp("Risorsa non recuperabile"));
					return null;
				}

				StringType filenetGuid = PdfFilenetUtil.readInstanceFilenetGuid(csc, key.getChiave());
				if(filenetGuid != null){
					GenericCommandResponseModel filenetResp = PdfFilenetUtil.readFilenetUrlFromGuid(csc, filenetGuid);
					if(filenetResp != null) {
						setGenericCommandResponse(filenetResp);
						return null;
					}
				}
				
				pdf = (ByteArrayType)dao.executeQueryAccess("loadPdfCompilabile", key).getSingleResult();
				if(pdf.isNull())
					pdf = new ByteArrayType(PdfNasUtil.PDF_INSTANCE.readPdfInstanceContent(csc, key.getChiave()));
				
			}else if(key.getSorgente().equals("COMUNICAZIONI")){
				
				key.setChiaveAsNum(new IntegerType(key.getChiave().toString()));
				pdf = (ByteArrayType)dao.executeQueryAccess("loadPdfComunicazione", key).getSingleResult();
				
			}else{
				
				setGenericCommandResponse(htmlResp("La sorgente ["+key.getSorgente()+"] non è gestita"));
				return null;
				
			}
			
			if(pdf == null || pdf.isNull()){
				setGenericCommandResponse(htmlResp("Il pdf non è recuperabile"));
				return null;
			}

			byte[] result = pdf.byteArrayValue();
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("application/pdf");
			resp.setContentLength(result.length);
			resp.setContent(result);
			setGenericCommandResponse(resp);
			return null;
			
		}catch(DAOException daoe){
			setGenericCommandResponse(htmlResp(daoe.toString()));
			return null;
		}catch(Exception e){
			setGenericCommandResponse(htmlResp(e.toString()));
			return null;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PropostaCopernicoKeyModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	private GenericCommandResponseModel htmlResp(String text){
		String errResp = "<center><span style='font-family: Segoe UI;color:red;font-size:13px;'>"+text+"</span></center>";
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		resp.setContentType("text/html");
		resp.setContentLength(errResp.length());
		resp.setContent(errResp.getBytes());
		return resp;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private boolean verificaProprietaPdf(ClientSessionContext csc, DAOObject dao, PropostaCopernicoKeyModel key) throws DAOException{
		if(!csc.isCliente())
			return true;
		BooleanType diPropieta = (BooleanType)dao.executeQueryAccess("verificaProprietaPdf", key).getSingleResult();
		return diPropieta.booleanValue(); 
	}
	
}
