package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesResponse;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.verifiers.AmlAlertVerifier;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPreviewContainer extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel model = (PdfModel)dataModel;
			
			model = Basket.getFirstDispoPdf(model);
			if(model.isInBasket()) {
				for(BasketElement be : model.getBasket().getBasketElements()) {
					if(be.getDispoPdf() != null)
						be.getDispoPdf().clearEndProcessFields();
				}
			}
			model.clearEndProcessFields();
						
			model.setFirstSignPage(true);
			model.setReportAdeguatezzaClicked(false);
        	model.setRaccomandazioneIddClicked(false);
        	
			ProvideCompilationModesResponse compilationModesResponse = PdfDriverCaller.callProvideCompilationModes(csc, model);
			if(compilationModesResponse != null && compilationModesResponse.getCompilationModes() > 0){
				
				int compilationModesAsInt = compilationModesResponse.getCompilationModes();
				String compilationModesAsString = "";
				
				if((compilationModesAsInt & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_LIBERA) > 0)
					compilationModesAsString += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA+",";
				
				if((compilationModesAsInt & PdfBaseDriver.ModalitaDiSottoscrizione.CARTA_CHIMICA) > 0)
					compilationModesAsString += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA+",";
				
				if((compilationModesAsInt & PdfBaseDriver.ModalitaDiSottoscrizione.FIRMA_DIGITALE) > 0)
					compilationModesAsString += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE+",";
				
				if((compilationModesAsInt & PdfBaseDriver.ModalitaDiSottoscrizione.COPERNICO) > 0)
					compilationModesAsString += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO+",";
				
				if((compilationModesAsInt & PdfBaseDriver.ModalitaDiSottoscrizione.STAMPA) > 0)
					compilationModesAsString += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA+",";
				
				if(compilationModesAsString.length() > 0)
					compilationModesAsString = compilationModesAsString.substring(0,compilationModesAsString.length()-1);
				else
					compilationModesAsString = PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_NESSUNA;
				
				if(compilationModesAsString.length() > 0)
					model.getPdfData().setCompilationModes(new StringType(compilationModesAsString));
				for(int i=0;i<model.getPdfData().getPdfs().size();i++){
					PdfDataModel pdfDataElement = (PdfDataModel)model.getPdfData().getPdfs().get(i);
					if(compilationModesAsString.length() > 0)
						pdfDataElement.setCompilationModes(new StringType(compilationModesAsString));
				}
			}
			
			new AmlAlertVerifier().impostaAMLAlert(csc, model);

			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isStepCommand() {
		return true;
	}

}
