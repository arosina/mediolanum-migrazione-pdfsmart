package prgm.pdfwebforms.publisher.nasutil.moveblob;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.model.PdfInstanceModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DoMoveBlob extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            MoveBlobModel model = (MoveBlobModel)dataModel;
            
            setForwardDisplay(Integer.valueOf(0));
            
            model.setAttivitaInterrotta(false);
            model.setResultMessage(null);
            model.setPdfElaborati(new IntegerType());
            
            if(model.getDataInizio().isNull()) {
            	model.setResultMessage("Specificare la data inizio");
            	return model;
            }
            if(model.getDataFine().isNull()) {
            	model.setResultMessage("Specificare la data fine");
            	return model;
            }
            
        	Thread.sleep(1000);
        	if(model.isAttivitaInterrotta()){
        		model.setResultMessage("Spostamento interrotto. Nessun pdf gestito");
                return model;
        	}
  
        	DAOObject dao = null;
        	try{
        		String msg = null;
            	dao = new DAOObject(csc, "PdfWebForms.PdfNasUtilPublisher");
            	dao.openConnection();
            	if(model.getMoveType().equals("BlobToNas"))
            		msg = doMoveBlobToNas(csc, dao, model);
            	else if(model.getMoveType().equals("NasToBlob"))
            		msg = doMoveNasToBlob(csc, dao, model);
           		model.setResultMessage(msg);
        	}catch(DAOException daoe){
        		model.setResultMessage(daoe.toString());
        	}catch(Exception e){
        		model.setResultMessage(e.toString());
            }finally{
    			if(dao != null) dao.closeConnection();
    		}	
            
            return model;
            
        } catch(Exception e) {
            throw new CommandException(e);
        }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return MoveBlobModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String doMoveBlobToNas(ClientSessionContext csc, DAOObject dao, MoveBlobModel model) throws Exception, DAOException{

		model.setPdfElaborati(new IntegerType(0));
		DAOQueryResultModel qRes = dao.executeFetchableQueryAccess("pdfDaGestireMoveBlobToNas",model);
		for(;;){
			PdfInstanceModel pdf = (PdfInstanceModel)dao.fetchQuery(qRes);
			if(pdf == null || model.isAttivitaInterrotta())
				break;
			
			new DAOObject(csc, "PdfWebForms.PdfNasUtilPublisher").executeQueryAccess("loadPdfContent", pdf);
			if(!pdf.getPdfContent().isNull()) {
				PdfNasUtil.PDF_INSTANCE.writePdfInstanceContent(csc, pdf.getPdfInstanceId(), pdf.getPdfContent().byteArrayValue());
				pdf.setPdfContent(new ByteArrayType());
				dao.executeTableUpdateAccess("updatePdfInstance", pdf);
			}
			
			model.setPdfElaborati(new IntegerType(model.getPdfElaborati().intValue()+1));
		}
		return "Spostamento su NAS "+(model.isAttivitaInterrotta()?"interrotto":"effettuato")+". Elaborati "+model.getPdfElaborati()+" pdf";
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String doMoveNasToBlob(ClientSessionContext csc, DAOObject dao, MoveBlobModel model) throws Exception, DAOException{

		model.setPdfElaborati(new IntegerType(0));
		DAOQueryResultModel qRes = dao.executeFetchableQueryAccess("pdfDaGestireMoveNasToBlob",model);
		for(;;){
			PdfInstanceModel pdf = (PdfInstanceModel)dao.fetchQuery(qRes);
			if(pdf == null || model.isAttivitaInterrotta())
				break;

			byte[] pdfContent = PdfNasUtil.PDF_INSTANCE.readPdfInstanceContent(csc, pdf.getPdfInstanceId());
			if(pdfContent != null) {
				pdf.setPdfContent(new ByteArrayType(pdfContent));
				DAOTableResultModel tRes = dao.executeTableUpdateAccess("updatePdfInstance", pdf);
				if(tRes.getResult().intValue() > 0)
					PdfNasUtil.PDF_INSTANCE.deletePdfInstanceContent(csc, pdf.getPdfInstanceId());
			}
			
			model.setPdfElaborati(new IntegerType(model.getPdfElaborati().intValue()+1));
		}
		return "Spostamento su BLOB "+(model.isAttivitaInterrotta()?"interrotto":"effettuato")+". Elaborati "+model.getPdfElaborati()+" pdf";
	}
}
