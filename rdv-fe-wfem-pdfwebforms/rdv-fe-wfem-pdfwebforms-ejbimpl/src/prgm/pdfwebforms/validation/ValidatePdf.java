package prgm.pdfwebforms.validation;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.business.KeyInstanceRetriever;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.stream.PdfInstanceContentAsBinary;

/* **********************************************************************************************
 * Inizia/continua la validazione MOM di un pdf
 * **********************************************************************************************/
public class ValidatePdf extends BusinessCommand implements MenuCommand{

	private static final long serialVersionUID = 1L;

	private static final String S_NO_DRIVER = "Il modulo prevede dei controlli che per problemi tecnici non sono attivi. Non è possibile proseguire";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = null;
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
	   		if(csc.isCliente())
	   			throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       

	   		PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			if(KeyInstanceRetriever.isKeyEmpty(pdfData)){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Nessuna chiave di riferimento specificata");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){ // Il pdf non esiste con le chiavi ininput. Ne censiamo uno nuovo per l'operatre MOM
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
													"select MODALITA_DI_SOTTOSCRIZIONE "+
													"from PDF_INSTANCE "+
													"where PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"'",
													null, MapCommandDataModel.class);
			if(qRes.getResult().size() == 0){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Il pdf esiste come chiave ma non come dati");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			MapCommandDataModel pdfInfo = (MapCommandDataModel)qRes.getResult().get(0);
			StringType pdfCompilationMode  = (StringType)pdfInfo.readProperty("modalitaDiSottoscrizione");
			if(pdfCompilationMode.isNull()){
				pdf = new PdfModel();
				pdf.setPdfData(pdfData);				
				pdf.setInitialErrorMsg("Lo stato del pdf non risulta congruente con la validazione di sede");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			if( pdfCompilationMode.equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) || 
				pdfCompilationMode.equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO)){
				PdfInstanceModel pdfInstance = new PdfInstanceModel();
				pdfInstance.setPdfInstanceId(pdfData.getPdfInstanceId());
				setNextCommandClass(PdfInstanceContentAsBinary.class);						
				return pdfInstance;
			}

			pdf = facade.readPdf(csc, pdfData);
			if(pdf.getInitialErrorMsg() != null){
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			if(pdf.isSomeDriverNotFount()){
				pdf.setInitialErrorMsg(S_NO_DRIVER);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}					
			
			pdf.setModality(Template.READ_MODALITY);
			pdf.setPdfOnValidation(true);
			setNextCommandClass(PdfPage.class);
			return pdf;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

}
