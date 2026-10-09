package prgm.pdfwebforms.business;

import java.util.List;
import java.util.Vector;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.aml.AmlErrorPage;
import prgm.pdfwebforms.aml.AmlPage;
import prgm.pdfwebforms.aml.CoraPage;
import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.display.PdfAttachments;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnPreview;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataResponse;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.idd.IddStatiQuestionario;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConfirmPdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;       
            
            if(!Basket.verifyMvcConsistency(this, pdf))
            	return pdf;
            
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
	         
			// Se richiamato per la stampa ciclo sui pdf senza fare null'altro
            if(pdf.getPdfData().isOnlyPrint()) {
    			boolean existOtherPdf = pdf.isMultiPdf() && pdf.getPdfData().getPdfIndex().intValue() < pdf.getPdfData().getPdfs().size()-1;
    			if(existOtherPdf){
    				pdf = facade.nextPdf(csc, pdf);			
    				setNextCommandClass(PdfPage.class);
    				return pdf;
    			}   			
    			if(pdf.isMultiPdf())
    				pdf = facade.gotoPdf(csc, pdf, 0, false);    			
    			setNextCommandClass(PdfPreviewContainer.class);
    			return pdf;
            }

            pdf.clearEndProcessFields();
                      
           	DataLoader.loadPersons(csc, pdf);
			
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
			
			List warOnIdd = new Vector();
			if(pdf.getPdfData().getPdfIndex().intValue() == 0){
				
				String iddErrMsg = IddCaller.callIdd(csc, pdf);
				if(iddErrMsg != null && iddErrMsg.length() > 0){
					pdf.setInitialErrorMsg(iddErrMsg);
	    			setNextCommandClass(PdfInitialError.class);
	    			return pdf;
				}
				
				pdf.initCompilationModes(pdf.getPdfData());
			
				if(pdf.getPdfData().getPdfDriver() != null && !pdf.getPdfData().getPdfDriver().getClass().getName().equals(PdfBaseDriver.class.getName())){
					pdf.getPdfData().setSignAll(new BooleanType(true));
					if(pdf.isMultiPdf()){
						for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
							PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
							pdfElement.setSignAll(new BooleanType(true));
						}
					}
				}
				
				if(pdf.isQuestionarioIddProvvisorioOSospeso()){
					// Se siamo nel carrello e il questionario è provvisorio diamo il messaggio per lo specifico questionario
					if(!pdf.getPdfData().getIdCarrello().isNull() &&
						pdf.getIddCallModel().getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO)){
						warOnIdd.add(new CommandWarning("Il Report di Adeguatezza è riferito al PCA nr "+pdf.getIddCallModel().getIdQuestionarioIdd()+" che risulta sottoscritto in firma olografa e non ancora validato dalla sede, quindi anche il contratto deve essere necessariamente sottoscritto con la medesima modalità di firma."));
					}else{
						warOnIdd.add(new CommandWarning("Nel caso in cui il questionario relativo alle informazioni su richieste ed esigenze assicurative del cliente sia firmato in modalità olografa "+
														"e risulti in stato provvisorio o sospeso, la sottoscrizione tramite firma digitale e copernico non è consentita"));
					}
				}
				
			}

			boolean openWarnings = false;
			
			List warOnVerify = null;
			pdf = facade.verifyPdf(csc, pdf);
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfPage.class);
				return pdf;
			}
			if(pdf.hasCommandWarnings()){
				warOnVerify = new Vector(pdf.getCommandWarnings());
				pdf.resetCommandWarnings();
			}
			
			pdf.resetCommandErrors();
			
			List warOnVerified = null;
			facade.pdfVerified(csc, pdf);
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfPage.class);
				return pdf;
			}		
			// Warning copernico non possibile per cliente=FB
			String warningCopernico = warningCopernicoDaMostrare(csc, pdf);
			if(warningCopernico != null && Basket.isFirstDispoPdf(pdf)){
				pdf.addCommandWarning(warningCopernico);
			}
			if(pdf.hasCommandWarnings()){
				warOnVerified = new Vector(pdf.getCommandWarnings());
				pdf.resetCommandWarnings();
			}

			if(warOnIdd.size() > 0 || warOnVerify != null || warOnVerified != null){
				if(warOnIdd != null)
					pdf.addCommandWarnings(warOnIdd);
				if(warOnVerify != null)
					pdf.addCommandWarnings(warOnVerify);
				if(warOnVerified != null)
					pdf.addCommandWarnings(warOnVerified);
				openWarnings = true;
			}
			
			if(!pdf.getSkipCommandWarnings().booleanValue()){
				if(openWarnings){
					setNextCommandClass(PdfPage.class);
					return pdf;
				}
			}		
			
			pdf.resetCommandErrors();
			pdf.resetCommandWarnings();
			Tools.resetTypesErrors(pdf.getPdfData());
			pdf.setSkipCommandWarnings(new BooleanType(false));
			
			pdf = facade.savePdf(csc, pdf);
			if(pdf.isConcurrencyViolationFounded()) {
				setNextCommandClass(PdfConcurrencyViolation.class);
				return pdf;
			}
			if(pdf.hasCommandErrors()){
				setNextCommandClass(PdfPage.class);
				return pdf;
			}
			
			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			
			boolean existOtherPdf = pdf.isMultiPdf() && pdf.getPdfData().getPdfIndex().intValue() < pdf.getPdfData().getPdfs().size()-1;
			if(existOtherPdf){
				pdf.alignCommonToAllPdfDataProperties();
				pdf = facade.nextPdf(csc, pdf);			
				setNextCommandClass(PdfPage.class);
				return pdf;
			}
			
			ProvideAttachmentsDataResponse attachmentsData = PdfDriverCaller.callProvideAttachmentsData(csc, pdf);
			if(attachmentsData != null && attachmentsData.getErrorMessage() != null){
				pdf.addCommandError(attachmentsData.getErrorMessage());
				setNextCommandClass(PdfPage.class);
				return pdf;
			}
			if(attachmentsData != null && attachmentsData.getAttachments().size() > 0){
				ListType pdfAttachments = new ListType();
				for(int i=0;i<attachmentsData.getAttachments().size();i++){					
					PdfAttachModel attach = new PdfAttachModel();
					attach.setDriverAttachRef(attachmentsData.getAttachments().get(i));
					attach.getFile().setFileTypes(attach.getDriverAttachRef().getFileTypes());
					pdfAttachments.add(attach);
				}
				pdf.setPdfAttachments(pdfAttachments);
				if(!pdf.allImplicitAttach()) {
					setNextCommandClass(PdfAttachments.class);
					return pdf;
				}
			}else{
				pdf.setPdfAttachments(null);
			}			
			
			String[] erroriAdeguatezza = MifidCaller.callOnPreview(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
			
			String errorMsg = PdfDriverCaller.callBeforePreview(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnPreview.class);
    			return pdf;
			}

			if(pdf.isMultiPdf())
				pdf = facade.gotoPdf(csc, pdf, 0, false);
			
			errorMsg = IddCaller.callGeneraRaccomandazioneIdd(csc, pdf);
			if(errorMsg != null && errorMsg.length() > 0){
    			pdf.addCommandError(errorMsg);
    			setNextCommandClass(PdfErrorsOnPreview.class);
    			return pdf;
			}
			
			pdf.alignCommonToAllPdfDataProperties();
			PdfModel nextDispoPdf = Basket.nextDataEntryDispoPdf(userSessionContext, this, pdf);			
			if(nextDispoPdf == null) {
				((AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class)).createAmlAndCoraModel(csc, pdf);
				if(pdf.getAmlModel() != null) {
					if(pdf.getAmlModel().getErrorMsg() != null)
						setNextCommandClass(AmlErrorPage.class);
					else
						setNextCommandClass(AmlPage.class);
				}else if(pdf.getCoraModel() != null)
					setNextCommandClass(CoraPage.class);
				else
					setNextCommandClass(PdfPreviewContainer.class);
				return pdf;
			}
			
			return nextDispoPdf;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String warningCopernicoDaMostrare(ClientSessionContext csc, PdfModel pdf) throws DAOException{
		boolean doWarning =  pdf.getIsRete().booleanValue() &&
							 pdf.getMainCodAgente() != null &&
							!pdf.isQuestionarioIddProvvisorioOSospeso() &&
							 pdf.globalPdfCompilationModes().toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) >= 0 &&
						     pdf.getPdfData().getPdfIndex().intValue() == 0;
		if(doWarning) {
			if(pdf.isPrimoClienteAgente(pdf.getPdfData()))
				return "Il sottoscrittore del contratto è un Family Banker. Non è possibile inviare la proposta con Copernico";
			loadPrimoClienteIsCointestatarioPuro(csc, pdf);
			if(pdf.isPrimoClienteIsCointestatarioPuro())
				return "Il sottoscrittore del contratto è un cointestatario puro. Non è possibile inviare la proposta con Copernico";
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadPrimoClienteIsCointestatarioPuro(ClientSessionContext csc, PdfModel pdf) throws DAOException {
		pdf.setPrimoClienteIsCointestatarioPuro(false);
		PdfPersonModel firstPerson = pdf.getPdfData().getClienti().get(0);
		if(!firstPerson.getIsEffettivo().booleanValue())
			return;
		BooleanType primoClienteIsCointestatarioPuro = (BooleanType)DAOObject.executeDynaQueryAccess(csc, "DBAZ_SOGG", 
					"select case when trim(leading '0' from AGE_C) is null then 'S' else 'N' end " + 
					"from CLL.CLIAGE " + 
					"where GSTD_F_ESIST = 'S' " + 
					"AND CLIAGE_D_FINE = to_date('99991231','yyyymmdd') " + 
					"AND CLI_C = '"+firstPerson.getNdg()+"'", null, BooleanType.class).getSingleResult();
		if(primoClienteIsCointestatarioPuro != null && primoClienteIsCointestatarioPuro.booleanValue())
			pdf.setPrimoClienteIsCointestatarioPuro(true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
