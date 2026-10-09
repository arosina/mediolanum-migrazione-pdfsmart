package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.business.KeyInstanceRetriever;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPriipsPage;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Inizia il processo di compilazione di un pdf a prescindere dallo stato
 * **********************************************************************************************/
public class RestorePdf extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = new PdfModel();
			pdf.setPdfData(pdfData);

			if(pdfData.getPdfInstanceId().isNull()){
				pdf.setInitialErrorMsg("Id istanza in input non valorizzata");
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			String err = KeyInstanceRetriever.loadPdfInstanceId(csc, pdfData);
			if(err != null){
				pdf.setInitialErrorMsg(err);
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCarrello");
			
			if(!checkInputPdf(dao, pdf, pdfData)){
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}
			
			String originalPdfInstanceId = pdfData.getPdfInstanceId().toString();
			
			// Read pdf
			pdf = facade.readPdf(csc, pdfData);
			if(pdf.getInitialErrorMsg() != null && pdf.getInitialErrorMsg().length() > 0){
				setNextCommandClass(PdfInitialError.class);
				return pdf;
			}

			pdfData = pdf.getPdfData();
			pdfData.setPdfInstanceId(new StringType());
			pdfData.setPdfStatus(PdfInstanceModel.STATO_BOZZA);
			pdf.clearEndProcessFields();

			DataLoader.loadPersons(csc, pdf);
			
			pdf = facade.savePdf(csc, pdf);

			if(pdf.hasCommandErrors()){
				pdf.setInitialErrorMsg(pdf.getCommandErrors().get(0).toString());
			}
			
			if(pdf.getInitialErrorMsg() != null){
				pdf.setPdfData(pdfData);
				pdf.setFirstDisplayClass(PdfInitialError.class);
			}
			
			if( pdf.mainPdfAnag().getHasPriips().booleanValue()){
				setNextCommandClass(PdfPriipsPage.class);
				return pdf;
			}

			// "Sgancio" il vecchio pdf dal carrello
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(originalPdfInstanceId));
			pdfInstance.setIdCarrello(new IntegerType(pdf.getPdfData().getIdCarrello().intValue() * -1));
			dao.executeTableUpdateAccess("sganciaScadutoDalCarrello", pdfInstance);

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

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean checkInputPdf(DAOObject dao, PdfModel pdf, PdfDataModel pdfData) throws DAOException{
		
		PdfInstanceModel pdfInstance = new PdfInstanceModel();
		pdfInstance.setPdfInstanceId(pdfData.getPdfInstanceId());
		dao.executeTableLoadAccess("datiPdfScaduto", pdfInstance);

		if( !pdfInstance.getPdfStatus().equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE) &&
			!pdfInstance.getPdfStatus().equals(PdfInstanceModel.STATO_RIFIUTATO_DAL_CLIENTE)){
			pdf.setInitialErrorMsg("Il pdf ["+pdf.getPdfData().getPdfInstanceId()+"] non ha uno stato coerente con la riattivazione");
			return false;
		}
		
		if( pdfInstance.getIdCarrello().isNull()){
			pdf.setInitialErrorMsg("Il pdf ["+pdf.getPdfData().getPdfInstanceId()+"] non è collegato ad un carrello");
			return false;
		}

		if( pdfInstance.getIdCarrello().intValue() < 0){
			pdf.setInitialErrorMsg("Il pdf ["+pdf.getPdfData().getPdfInstanceId()+"] è già stato recuperato");
			return false;
		}

		return true;
	}
}
