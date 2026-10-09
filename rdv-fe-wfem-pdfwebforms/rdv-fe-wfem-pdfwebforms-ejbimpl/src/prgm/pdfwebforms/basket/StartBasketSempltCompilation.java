package prgm.pdfwebforms.basket;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.business.NewPdf;
import prgm.pdfwebforms.business.OpenPdf;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Inizia il processo di compilazione di un basket semplificato
 * **********************************************************************************************/
public class StartBasketSempltCompilation extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
				
		try{
			
            PdfDataModel inputData = (PdfDataModel)dataModel;
            
            if(inputData.getPdfEnvironment().equalsIgnoreCase("CARRELLO"))
            	throw new CommandException("Basket semplt: l'environment CARRELLO è riservato");
            
			Basket basket = createBasket(userSessionContext.getClientSessionContext(), inputData);
			
			PdfModel basketInput = new PdfModel();
			basketInput.setBasket(basket);
			setNextCommandClass(prgm.pdfwebforms.business.StartBasketCompilation.class);
			return basketInput;
			
		}catch(DAOException | Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfDataModel.class;
	}

	/***********************************************************************************************/
	private static final String MULTI_PDF_PREFIX = "&pdfs";
	/***********************************************************************************************/
	private Basket createBasket(ClientSessionContext csc, PdfDataModel inputData) throws Exception, DAOException{
		
		DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfBasketSemplt");
		ListType elencoDispo = dao.executeQueryAccess("loadDispoBasket", inputData).getResult();
		if(elencoDispo.size() == 0)
			throw new Exception("Basket semplt: nessuna dispositiva configurata per il basket ["+inputData.getIdCarrello()+"]");

		Basket basket = new Basket(inputData.getIdCarrello().toString());
		for(int i=0;i<elencoDispo.size();i++) {
			
			BasketElement basketElem = new BasketElement();
			PdfDataModel dispo = (PdfDataModel)elencoDispo.get(i);
			if(!dispo.getPdfInstanceId().isNull() && !isPdfInBozza(dao, dispo)) // Dispo già completata
				continue;

			StringBuilder par = new StringBuilder();
			par.append("compilationModes="+inputData.getCompilationModes());
			par.append("&callerSelectedCompilationMode="+inputData.getCallerSelectedCompilationMode());			
			par.append("&codAgeImpersonato="+inputData.getCodAgeImpersonato());
			par.append("&codRuoloImpersonato="+inputData.getCodRuoloImpersonato());
			par.append("&gobackLabel="+inputData.getGobackLabel());
			par.append("&gobackUrl="+encodeUrl(inputData.getGobackUrl().toString()));
			par.append("&gobackEndLabel="+inputData.getGobackEndLabel());
			par.append("&gobackEndUrl="+encodeUrl(inputData.getGobackEndUrl().toString()));
			par.append("&gobackEndUrlOnTop="+inputData.getGobackEndUrlOnTop());
			par.append("&hideSaveButton="+inputData.getHideSaveButton());
			par.append("&inviaInSedeButtonLabel="+inputData.getInviaInSedeButtonLabel());
			par.append("&firmaDigitaleButtonLabel="+inputData.getFirmaDigitaleButtonLabel());
			par.append("&copernicoButtonLabel="+inputData.getCopernicoButtonLabel());	
			if(dispo.getPdfInstanceId().isNull()) {
				basketElem.setCmd(new NewPdf());
				par.append("&pdfEnvironment="+inputData.getPdfEnvironment());
				par.append("&idCarrello="+inputData.getIdCarrello());
				par.append("&idDispCarrello="+dispo.getIdDispCarrello());
				par.append("&ordineCompilazioneBasket="+(i+1));
				par.append("&idRaccomandazioneIdd="+dispo.getIdRaccomandazioneIdd());
				par.append("&priipsTipoSupportoMaterialeContrattuale="+dispo.getPriipsTipoSupportoMaterialeContrattuale());
				ListType elencoModuliDispo = dao.executeQueryAccess("loadModuliDispoBasket", dispo).getResult();
				if(elencoModuliDispo.size() == 0)
					throw new Exception("Basket semplt ["+dispo.getIdCarrello()+"]: nessun modulo configurato per la dispositiva ["+dispo.getIdDispCarrello()+"]");
				
				// Nel nuovo carrello esiste una sola dispositiva mentre nel vecchio,per lo switch, ne esistono 2 una per il rimborso e una per l'invesimento
				// Per questo motivo idDispCarrello viene popolato su ciascun singolo modulo e dobbiamo mantenerlo anche se non sarebbe necessario perchè
				// sarà lo stesso valore per tutti i moduli della dispositvia
				if(elencoModuliDispo.size() == 1) {
					PdfDataModel module = (PdfDataModel)elencoModuliDispo.get(0);
					par.append("&pdfCode="+module.getPdfCode());
					par.append("&idDispModuloCarrello="+module.getIdDispModuloCarrello()); 
				}else {
					for(int j=0;j<elencoModuliDispo.size();j++) {
						PdfDataModel module = (PdfDataModel)elencoModuliDispo.get(j);
						par.append(MULTI_PDF_PREFIX+j+"_pdfCode="+module.getPdfCode());
						par.append(MULTI_PDF_PREFIX+j+"_idDispModuloCarrello="+module.getIdDispModuloCarrello()); 
						par.append(MULTI_PDF_PREFIX+j+"_idDispCarrello="+dispo.getIdDispCarrello()); 
						par.append(MULTI_PDF_PREFIX+j+"_priipsTipoSupportoMaterialeContrattuale="+dispo.getPriipsTipoSupportoMaterialeContrattuale());
					}
				}
			}else {
				basketElem.setCmd(new OpenPdf());
				par.append("&pdfInstanceId="+dispo.getPdfInstanceId());
			}
			basketElem.setPar(par.toString());
			basket.getBasketElements().add(basketElem);
		}
		return basket;
	}

    /***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isPdfInBozza(DAOObject dao, PdfDataModel dispoCarrello) throws DAOException{
		PdfInstanceModel pdfIntance = new PdfInstanceModel();
		pdfIntance.setPdfInstanceId(dispoCarrello.getPdfInstanceId());
		dao.executeTableLoadAccess("leggiStatoPdf", pdfIntance);
		return pdfIntance.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String encodeUrl(String url) {
		return url.replace("&","%26").replace("=","%3D");
	}
}
