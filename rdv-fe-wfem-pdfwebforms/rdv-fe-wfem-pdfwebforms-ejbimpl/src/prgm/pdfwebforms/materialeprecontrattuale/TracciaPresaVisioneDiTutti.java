package prgm.pdfwebforms.materialeprecontrattuale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class TracciaPresaVisioneDiTutti extends BusinessCommand {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfModel pdf = (PdfModel)dataModel;

		String respCont = "Ok";  

		try{
			List<TracciaPresaVisioneModel> elencoInput = new ArrayList<>();
			if(pdf.isInBasket()) {
				Basket basket = pdf.getBasket();
				for(BasketElement be : basket.getBasketElements()) {
					List<String> dispoUrls = new ArrayList<>();
					List<String> dispoLabels = new ArrayList<>();
					addUrlsDispo(be.getDispoPdf(), dispoUrls, dispoLabels);
					addInput(elencoInput, pdf, be.getDispoPdf(), dispoUrls, dispoLabels);
				}
			}else {
				List<String> dispoUrls = new ArrayList<>();
				List<String> dispoLabels = new ArrayList<>();
				addUrlsDispo(pdf, dispoUrls, dispoLabels);
				addInput(elencoInput, pdf, pdf, dispoUrls, dispoLabels);
			}
			
			if(elencoInput.isEmpty()){
				respCont = "Nessun documento";  
			}else{
				if(!doCalls(csc, elencoInput))
					respCont = "Una o più call ko";
			}
			
		}catch(Exception e){
			respCont = "Exception";
		}
				    
		GenericCommandResponseModel resp = new GenericCommandResponseModel();
		resp.setContentType("text/html");
		resp.setContentLength(respCont.length());
		resp.setContent(respCont.getBytes());
		setGenericCommandResponse(resp);
		return null;            			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addUrlsDispo(PdfModel pdf, List<String> dispoUrls, List<String> dispoLabels) {
		
		PdfAnagModel pdfAnag = pdf.mainPdfAnag();
		PdfDataModel pdfData = pdf.mainPdfData();
		
		// Se è uno switch l'anagrafica è del secondo modulo mentre i documenti, quando si è da carrello, vengono caricati sul primo...
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfAnags().size() > 1 && pdf.getPdfData().getPdfs().size() > 1){
			pdfAnag = pdf.getPdfAnags().get(1);	// ...prendo i dati del secondo modulo
			pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
			if(!pdfData.getIdCarrello().isNull()){ 					// nel carrello dobbiamo prendere l'elenco dal primo modulo
				if(pdf.getPdfData().getPdfIndex().intValue() == 0) 	// Se siamo sul primo prendiamo dal pdfData "corrente"
					pdfData = pdf.getPdfData();
				else												 // Altrimenti dal pdfData del primo
					pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(0);
			}
		}

		// URLS
		String url = pdfAnag.getExternalLinkOnSignUrl().toString();
		if(pdfData.getExternalLinkOnSignData() != null){
			if(url.length() > 0)
				url += "|";
			url += pdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrl().toString();
		}
		if(url.length() == 0)
			return;

		// LABELS
		String label = pdfAnag.getExternalLinkOnSignLabel().toString();
		if(pdfData.getExternalLinkOnSignData() != null){
			if(label.length() > 0)
				label += "|";
			label += pdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabel().toString();		
		}
		if(label.length() == 0)
			label = url;	
		
		String[] urls = url.split("\\|");
		String[] labels = label.split("\\|");
		
		if(dispoUrls.size() != dispoLabels.size())
			return;

		dispoUrls.addAll(new ArrayList<>(Arrays.asList(urls)));
		dispoLabels.addAll(new ArrayList<>(Arrays.asList(labels)));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addInput(List<TracciaPresaVisioneModel> elencoInput, PdfModel pdfCorrente, PdfModel pdfDispo, 
						  List<String> dispoUrls, List<String> dispoLabels) {
		for(int i=0;i<dispoUrls.size();i++) {
			if(pdfCorrente.getPersonaCorrente() != null && !pdfCorrente.getPersonaCorrente().getNdg().isNull()) {
				TracciaPresaVisioneModel input = new TracciaPresaVisioneModel();
				input.setClickType(new StringType("presavisione"));
				input.setNdg(new StringType(Tools.fillSx(pdfCorrente.getPersonaCorrente().getNdg().toString(),'0',11)));
				input.setIdCarrello(new StringType(pdfCorrente.getPdfData().getIdCarrello().toString()));
				input.setPdfId(new StringType(pdfDispo.getPdfData().getPdfInstanceId().toString()));
				input.setUrl(new StringType(dispoUrls.get(i)));
				input.setTitle(new StringType(dispoLabels.get(i)));
				elencoInput.add(input);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean doCalls(ClientSessionContext csc, List<TracciaPresaVisioneModel> elencoInput) {
		boolean allOk = true;
		for(TracciaPresaVisioneModel model : elencoInput) {
			try{
				new DAOObject(csc, "PdfWebForms.PdfWebForms").executeOSBAccess("tracciaPresaVisioneMaterialePrecontrattuale", model);
			}catch(DAOException daoe){
				allOk = false;
			}
		}
		return allOk;
    }
	
}
