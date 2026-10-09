package prgm.pdfwebforms.signprocess.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.display.PdfConcurrencyViolation;
import prgm.pdfwebforms.display.PdfErrorsOnCompilationModeSelection;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfMifidResultPage;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.legalerappresentante.LegaleRappresentanteManager;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.model.PdfPersonSignDataModel;
import prgm.pdfwebforms.reportadeguatezza.Costanti;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;
import prgm.pdfwebforms.signprocess.common.SignUtility;
import prgm.pdfwebforms.signprocess.display.PdfErrorsOnSign;
import prgm.pdfwebforms.signprocess.display.PdfPersonSign;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartSignProcess extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;			
			pdf.resetCommandErrors();
        	pdf.resetCommandWarnings();
        	
        	if(Basket.isFirstDispoPdf(pdf) && !pdf.isTestMode() &&
        	   (!pdf.isFirmaDigitaleAccessibile() || Basket.globalBasketPdfCompilationModes(pdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) < 0)){
    			pdf.setInitialErrorMsg("Attenzione, la modalità di sottoscrizione selezionata non è abilitata, non è possibile procedere");
    			setNextCommandClass(PdfInitialError.class);
    			return pdf;
        	}
        	
			pdf.setReportAdeguatezzaClicked(false);
        	pdf.setRaccomandazioneIddClicked(false);
        	pdf.setMaterialePrecontrattualeClicked(new BooleanType());
        	pdf.setMaterialePrecontrattualeAccessorioClicked(new BooleanType());
        	pdf.setPresaVisioneMaterialePrecontrattuale(new BooleanType());
        	
        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)){
    			setNextCommandClass(PdfConcurrencyViolation.class);
    			return pdf;
    		}
        	
        	DataLoader.reloadAllPdfsPersons(csc, pdf);
        	
        	if(!pdf.getSkipCommandWarnings().booleanValue()){
				PdfDriverCaller.callCompilationModeSelection(csc, pdf, PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE);
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
        	
			String[] erroriAdeguatezza = MifidCaller.callOnSign(csc, pdf);
			if(erroriAdeguatezza != null){
				pdf.setErroriAdeguatezza(erroriAdeguatezza);
				setNextCommandClass(PdfMifidResultPage.class);
				return pdf;
			}
        	
        	// Inizializzo i clienti che dovranno firmare
			ArrayList<PdfPersonModel> fullSignProcessPersonsSign = createFullSignProcessPersons(pdf);
    		if(fullSignProcessPersonsSign.isEmpty()){
				pdf.addCommandError("Nessun soggetto previsto in firma");
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
    		}
    		
    		// Imposto i dti di contesto utili ai controlli abilitazione firma
    		impostaDatiContestoSuiFirmatari(pdf, fullSignProcessPersonsSign);
    		
    		// Imposto i firmatari
    		pdf.setFullProcessPersons(fullSignProcessPersonsSign);
    		
    		// Carico i dati FD dei soggetti
        	SignUtility.loadDatiFirmaDigitalePersone(csc,pdf);
        	if(pdf.hasCommandErrors()){
        		setNextCommandClass(PdfErrorsOnSign.class);
        		return pdf;
        	}

			// Cerco il primo cliente che deve firmare
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdf.setPersonaCorrente(null);
			for(PdfPersonModel personaCorrente : pdf.getFullProcessPersons()){
				
				for(int i=0;i<personaCorrente.getPdfPersonsSignProcessInfos().length;i++){
    				PdfPersonSignProcessInfo pi = personaCorrente.getPdfPersonsSignProcessInfos()[i];
					if(pi.isHasSignOnPdf()){
	    	       		personaCorrente.setIndexInFieldName(pi.getIndexInFieldName());
	   					pdf = facade.gotoPdf(csc, pdf, i, false);
						pdf.setPersonaCorrente(personaCorrente);
						break;
					}
				}
				if(pdf.getPersonaCorrente() != null)
					break;
			}
			
    		if(pdf.getPersonaCorrente() == null){ // Non dovrebbe mai accadere!
				pdf.addCommandError("Nessun pdf per il soggetto previsto in firma");
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
    		}
    		
    		if(!pdf.isInBasket()) {
    			
	    		SignUtility.getDigitDaChiedere(csc, pdf, pdf.getPersonaCorrente());
	    		if(pdf.hasCommandErrors()){
	    			setNextCommandClass(PdfErrorsOnSign.class);
	        		return pdf;            			
	    		}
	    		
    		}

    		// Richiamo la generazione del report adeguatezza, se non già fatto o passato in input
			String erroriReportAdeguatezza = ReportAdeguatezzaCaller.callGeneraReportAdeguatezzaOnSign(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(erroriReportAdeguatezza);
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
			}      	
    		
			// Richiamo il primo tentativo di recupero del report adeguatezza
			erroriReportAdeguatezza = ReportAdeguatezzaCaller.callRecuperaReportAdeguatezza(csc, pdf);
			if(erroriReportAdeguatezza != null){
				pdf.addCommandError(Costanti.MESSAGGIO_ERRORE);
				setNextCommandClass(PdfErrorsOnSign.class);
				return pdf;
			}
			
			pdf.saveAndSetDataSottoscrizione();
        	
			setNextCommandClass(PdfPersonSign.class);
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
	public static ArrayList<PdfPersonModel> createFullSignProcessPersons(PdfModel pdf) throws Exception{
		
		int numPdf = pdf.isMultiPdf() ? pdf.getPdfData().getPdfs().size() : 1; 
		
		ArrayList<PdfPersonModel> clienti = new ArrayList<PdfPersonModel>();
		
		// Aggiungo i clienti
		for(int pdfIndex=0;pdfIndex<numPdf;pdfIndex++){ // for each pdf
			
			PdfDataModel pdfDataElement = pdf.isMultiPdf() ? (PdfDataModel)pdf.getPdfData().getPdfs().get(pdfIndex) : pdf.getPdfData();
			
			// Gestione legale rappresentante
			List<PdfPersonModel> cliInPdfData = LegaleRappresentanteManager.elencoClientiFirmatariPG(pdf, pdfDataElement);
			for(int cliIndex=1;cliIndex<=cliInPdfData.size();cliIndex++){ // for each person of pdf
				
				PdfPersonModel pdfDataElementCli = cliInPdfData.get(cliIndex-1);
				
				if(pdfDataElementCli.isEmty())
					continue;
				
				boolean hasSignInPdf = hasPersonSignInPdf(pdfDataElementCli, pdfDataElement, cliIndex);
					
				boolean found = false;
				for(int j=0;j<clienti.size();j++){ // for each person managed
					
					PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
					if(managedCli.isEqual(pdfDataElementCli)){
						
						PdfPersonSignProcessInfo pdfPersonSignInfo = managedCli.getPdfPersonsSignProcessInfos()[pdfIndex];
		        		if(hasSignInPdf){
			        		pdfPersonSignInfo.setHasSignOnPdf(true);
			        		managedCli.setHasSignInSomePdf(true);
			        		pdfPersonSignInfo.getIndexInFieldName().add(cliIndex);
		        		}
		        		found = true;
						break;
						
					}
				}
				if(found)
					continue;
				
				PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfDataElementCli); 
        		cli.setSignData(new PdfPersonSignDataModel());
        		cli.setPdfPersonsSignProcessInfos(new PdfPersonSignProcessInfo[numPdf]);
        		for(int k=0;k<numPdf;k++)
        			cli.getPdfPersonsSignProcessInfos()[k] = new PdfPersonSignProcessInfo();

				PdfPersonSignProcessInfo pdfPersonSignInfo = cli.getPdfPersonsSignProcessInfos()[pdfIndex];
        		if(hasSignInPdf){
	        		pdfPersonSignInfo.setHasSignOnPdf(true);
        			cli.setHasSignInSomePdf(true);
            		pdfPersonSignInfo.getIndexInFieldName().add(cliIndex);
        		}
 				clienti.add(cli);
				
			}
			
		}
		
		// Aggiungo l'agente in fondo (se non siamo in accettazione copernico e non è vendita come Bmed)
		if(!pdf.isInAccettazioneCopernico() && !pdf.isVenditaComeBmed()){
			for(int pdfIndex=0;pdfIndex<numPdf;pdfIndex++){ // for each pdf
				
				PdfDataModel pdfDataElement = pdf.isMultiPdf() ? (PdfDataModel)pdf.getPdfData().getPdfs().get(pdfIndex) : pdf.getPdfData();
				
				PdfPersonModel pdfDataElementAge = pdfDataElement.getAgente();

				boolean hasSignInPdf = hasPersonSignInPdf(pdfDataElementAge, pdfDataElement, -1);

				boolean found = false;
				for(int j=0;j<clienti.size();j++){ // for each person managed
					
					PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
					if(managedCli.isEqual(pdfDataElementAge)){
							
						PdfPersonSignProcessInfo pdfPersonSignInfo = managedCli.getPdfPersonsSignProcessInfos()[pdfIndex];
		        		if(hasSignInPdf){
			        		pdfPersonSignInfo.setHasSignOnPdf(true);
			        		managedCli.setHasSignInSomePdf(true);
		        		}
		        		found = true;
						break;
					}
				}
				if(found)
					continue;
				
				PdfPersonModel age = (PdfPersonModel)Tools.cloneObject(pdfDataElementAge); 
	    		age.setSignData(new PdfPersonSignDataModel());
	    		age.setPdfPersonsSignProcessInfos(new PdfPersonSignProcessInfo[numPdf]);
	    		for(int k=0;k<numPdf;k++)
	    			age.getPdfPersonsSignProcessInfos()[k] = new PdfPersonSignProcessInfo();

				PdfPersonSignProcessInfo pdfPersonSignInfo = age.getPdfPersonsSignProcessInfos()[pdfIndex];
	    		if(hasSignInPdf){
	        		pdfPersonSignInfo.setHasSignOnPdf(true);
	    			age.setHasSignInSomePdf(true);
	    		}
				clienti.add(age);				
				
			}
		}
		
		// Elimino i clienti che non hanno nemmeno una firma da apporre in nessuno dei pdf
		for(int i=clienti.size()-1;i>=0;i--){
			PdfPersonModel p = (PdfPersonModel)clienti.get(i);
			if(!p.isHasSignInSomePdf())
				clienti.remove(i);
		}
		
		return clienti;
				
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean hasPersonSignInPdf(PdfPersonModel person, PdfDataModel pdfData, int cliIndex){
		
		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove)); 

		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){

			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			
			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName))
				continue;

			Matcher mat = null;
			if(person.isAgente())
				mat = PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(fi.htmlFieldName);
			else
				mat = PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
			if(!mat.matches())
				continue;
			
			String propertyName = null;
			if(person.isAgente()){
				propertyName = "firma"+mat.group(1)+"Agente";
			}else{
				if(Integer.parseInt(mat.group(2)) != cliIndex)
					continue;
				propertyName = "firma"+mat.group(1)+"Cliente"+mat.group(2);
			}
			AbstractType field = (AbstractType)pdfData.readProperty(propertyName);
			if(field == null)
				continue;
			
			return true;
		}		
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void impostaDatiContestoSuiFirmatari(PdfModel pdf, ArrayList<PdfPersonModel> fullSignProcessPersonsSign) {
		// Imposto l'eventuale agente impersonato
		for(int i=0;i<fullSignProcessPersonsSign.size();i++) {
			PdfPersonModel p = fullSignProcessPersonsSign.get(i);
			p.setCodAgeImpersonato(pdf.getPdfData().getCodAgeImpersonato());
			p.setPdfEnvironment(pdf.getPdfData().getPdfEnvironment());
		}
	}

}
