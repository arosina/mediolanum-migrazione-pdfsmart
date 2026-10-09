package prgm.pdfwebforms.copernicoprocess.business;

import java.util.ArrayList;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.copernicoprocess.common.CopernicoUtility;
import prgm.pdfwebforms.copernicoprocess.display.PdfErrorsOnCopernico;
import prgm.pdfwebforms.copernicoprocess.display.PdfNoteFbCopernico;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnCompilationModeSelection;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.model.PdfPersonSignDataModel;
import prgm.pdfwebforms.reportadeguatezza.Costanti;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartCopernicoProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;			
        	pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();
        	
        	if(Basket.isFirstDispoPdf(pdf) && !pdf.isTestMode() &&
        	   (!pdf.isCopernicoAccessibile() || Basket.globalBasketPdfCompilationModes(pdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) < 0)){
    			pdf.setInitialErrorMsg("Attenzione, la modalità di sottoscrizione selezionata non è abilitata, non è possibile procedere");
    			setNextCommandClass(PdfInitialError.class);
    			return pdf;
           	}
        	
			pdf.setReportAdeguatezzaClicked(false);
        	pdf.setRaccomandazioneIddClicked(false);

        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	String noteFBCopernicoConfigurate = "";
        	if(!pdf.mainPdfAnag().getNoteFbCopernico().isNull() && !pdf.getPdfData().getIsSwitch().booleanValue())
        		noteFBCopernicoConfigurate = pdf.mainPdfAnag().getNoteFbCopernico().toString(); 
        	pdf.setPdfNoteFBCopernico(new StringType(noteFBCopernicoConfigurate));
        	if(!pdf.mainPdfAnag().getNoteFbCopernico().isNull() && !pdf.getPdfData().getIsSwitch().booleanValue())
        		pdf.getPdfNoteFBCopernico().setEditable(false);
        	
        	if(!pdf.getSkipCommandWarnings().booleanValue()){
				PdfDriverCaller.callCompilationModeSelection(csc, pdf, PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO);
				if(pdf.hasCommandErrors()){
					setNextCommandClass(PdfErrorsOnCompilationModeSelection.class);
					return pdf;
				}
				if(pdf.hasCommandWarnings()){
					pdf.setSkipWarningCommandName(this.getClass().getName());
	    			setNextCommandClass(PdfErrorsOnCompilationModeSelection.class);
	    			return pdf;
				}
	    	}
	    	pdf.setSkipCommandWarnings(new BooleanType(false));
        	
			String[] erroriAdeguatezza = MifidCaller.callOnCopernico(csc, pdf, null);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
        	
        	// Inizializzo i clienti coinvolti
			ArrayList<PdfPersonModel> fullSendProcessPersons = createFullCopernicoProcessPersons(pdf);
			pdf.setFullProcessPersons(fullSendProcessPersons);

    		if(fullSendProcessPersons.size() == 0){
    			pdf.addCommandError("Nessun soggetto cui inviare la proposta");
    			setNextCommandClass(PdfErrorsOnCopernico.class);
    			return pdf;
    		}
			
        	CopernicoUtility.loadDatiCopernicoPersone(csc,pdf);
        	if(pdf.hasCommandErrors() || pdf.hasCommandWarnings()){
        		setNextCommandClass(PdfErrorsOnCopernico.class);
        		return pdf;
        	}
			
    		// Richiamo la generazione del report adeguatezza, se non già fatto o passato in input
			String erroriReportAdeguatezza = ReportAdeguatezzaCaller.callGeneraReportAdeguatezzaOnCopernico(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(erroriReportAdeguatezza);
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}      	
    		
			// Richiamo il primo tentativo di recupero del report adeguatezza
			erroriReportAdeguatezza = ReportAdeguatezzaCaller.callRecuperaReportAdeguatezza(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(Costanti.MESSAGGIO_ERRORE);
				setNextCommandClass(PdfErrorsOnCopernico.class);
				return pdf;
			}
        	
        	setNextCommandClass(PdfNoteFbCopernico.class);
			return pdf;
			
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
	private ArrayList<PdfPersonModel> createFullCopernicoProcessPersons(PdfModel pdf) throws Exception{
		
		ArrayList<PdfPersonModel> clienti = new ArrayList<PdfPersonModel>();
		if(pdf.isMultiPdf()){
			
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){ // for each pdf
				
				PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				
				for(PdfPersonModel pdfCli : pdfDataElement.getClienti()){ // for each person of pdf
					
					if(pdfCli.isEmty())
						continue;
					
					boolean found = false;
					for(int j=0;j<clienti.size();j++){ // for each person managed
						PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
						if(managedCli.isEqual(pdfCli)){
							found = true;
							break;
						}
					}
					if(!found){
						PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
		        		cli.setSignData(new PdfPersonSignDataModel());
						clienti.add(cli);
						if(!pdf.isProdottoCopernicoSmart())
							break;
					}
					
				}
			}
		}else{
			
			for(PdfPersonModel pdfCli : pdf.getPdfData().getClienti()){
				
				if(pdfCli.isEmty())
					continue;
				
				PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
        		cli.setSignData(new PdfPersonSignDataModel());
				clienti.add(cli);
				if(!pdf.isProdottoCopernicoSmart())
					break;
			}
			
		}
		return clienti;
	}
		
}
