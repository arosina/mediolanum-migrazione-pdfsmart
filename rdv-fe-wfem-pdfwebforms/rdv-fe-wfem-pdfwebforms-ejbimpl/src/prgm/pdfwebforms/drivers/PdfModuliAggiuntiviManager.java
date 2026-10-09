package prgm.pdfwebforms.drivers;

import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.aml.model.AmlModel;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.io.PdfVerifiedInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfModuliAggiuntiviManager{

	private static final String AML_SECTION = "AML";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static List<String> elencoCodiciModuloAggiuntivi(ClientSessionContext csc) throws Exception{
		return PdfConfig.getParamAsStringArray(csc, AML_SECTION, "MODULI_QUESTIONARIO_AML","BAN9,AN43,AN44");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static BooleanType isProcessoTattico(ClientSessionContext csc)  throws Exception{
		return PdfConfig.getParamAsBool(csc, AML_SECTION, "PROCESSO_TATTICO");
	}
	
	/***********************************************************************************************/
	// RFC #280359: Tenere presente che su mom i moduli non vengono mai ne aggiunti ne eliminati
	// (vedere AbstractDriver)
	/***********************************************************************************************/
	public PdfDataModel addQuestionarioAML(ClientSessionContext csc, PdfBaseDriver pdfDriver, PdfVerifiedInputData input) throws Exception{
		if(input.getPdf().isTestMode())
			return null;
		
		// Per l'operatore mom lasciamo tutto com'è perchè il modulo lo ha eventualmnente agganciato lui
		if(input.getPdf().isOperatoreMOM())
			return null;
			
		BooleanType processoTattico = isProcessoTattico(csc);
		if(!processoTattico.booleanValue() && input.getPdf().isInBasket())  // Nel processo AML target in caso di basket i moduli vengono aggiunti a valle del dataentry.
			return null;
		
		// Configurazione aggancio modulo, solo per moduli con momcode valorizzato
		StringType codiceMomAttuale = input.getPdfAnag().getPdfMomCode();
		if(codiceMomAttuale.isNull())
			return null;
		
		// Se non siamo sul pdf principale di una multimodulo non faccio nulla
		if(input.getPdf().isMultiPdf()) {
			int masterModuleIndex = 0;
			if(input.getPdfData().getIsSwitch().booleanValue())
				masterModuleIndex = 1;
			if(input.getPdfData().getPdfIndex().intValue() != masterModuleIndex)
				return null;
		}
		
		List<String> elencoCodiciModuloAML = elencoCodiciModuloAggiuntivi(csc);
		
		// Se il modulo attuale è un AML non faccio nulla
		for(int i=0;i<elencoCodiciModuloAML.size();i++) { 
			if(codiceMomAttuale.equals(elencoCodiciModuloAML.get(i)))
				return null;
		}
		
		// Codice modulo da aggiungere. Se rimane null non va aggiunto
		String codiceModuloDaAggiungere = null;
		StringType driverAttuale = input.getPdfAnag().getPdfDriverName();
		for(int i=0;i<elencoCodiciModuloAML.size();i++) {
			String codiceModuloAML = elencoCodiciModuloAML.get(i);
			List<String> elencoCodiciModuloConAML = PdfConfig.getParamAsStringArray(csc, AML_SECTION, "MODULI_CON_QUESTIONARIO_AML_"+codiceModuloAML,"",true);
			if( (elencoCodiciModuloConAML.contains(codiceMomAttuale.toString())) || 
				(!driverAttuale.isNull() && elencoCodiciModuloConAML.contains(driverAttuale.toString()))) {
				codiceModuloDaAggiungere = codiceModuloAML;
				break;
			}
		}

		// Nel processo tattico i driver che non specificavano l'importo non devono prevedere il modulo AV
		if(processoTattico.booleanValue()) {
			AbstractType importo = input.getPdfData().read(PdfPredefinedFields.IMPORTO);
			if(importo == null || importo.isNull())
				codiceModuloDaAggiungere = null;
		}
		
		// Se il modulo non prevede, o non prevede più l'aggancio elimino tutti i moduli eventualmente 
		// agganciati quando lo prevedeva ed esco
		if(codiceModuloDaAggiungere == null) {
			for(int i=0;i<elencoCodiciModuloAML.size();i++)
				pdfDriver.removeOtherPdf(elencoCodiciModuloAML.get(i));
			return null;
		}

		// Se il modulo prevede l'aggancio elimino tutti gli eventuali moduli aggiuntivi differenti rispetto al corrente
		if(codiceModuloDaAggiungere != null) {
			for(int i=0;i<elencoCodiciModuloAML.size();i++) {
				String codModAml = elencoCodiciModuloAML.get(i);
				if(!codModAml.equals(codiceModuloDaAggiungere))
					pdfDriver.removeOtherPdf(codModAml);
			}
		}

		PdfDataModel avPdfData = new PdfDataModel(); 
		if(pdfDriver.existOtherPdf(codiceModuloDaAggiungere)) // Se il modulo esiste già ne prendo i dati così da non perdere il dataentry fatto
			avPdfData = pdfDriver.getOtherPdfData(codiceModuloDaAggiungere);

		// l'LR nei moduli AML non c'è, ndgCliente2 è il terzo pagatore
		avPdfData.setIndiceLegaleRappresentante(new IntegerType(-1));

		pdfDriver.removeOtherPdf(codiceModuloDaAggiungere); // Lo rimuovo, così da riappenderlo sempre in coda
		if(hasAml(csc, input.getPdf(), elencoCodiciModuloAML)) // Se c'è aml...
			pdfDriver.addOtherPdf(codiceModuloDaAggiungere, avPdfData); // ...lo riappendo
		return avPdfData;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasAml(ClientSessionContext csc, PdfModel pdf, List<String> elencoCodiciModuloAML) throws Exception {
		
		// Simuliamo di essere in un basket. La facade AML lo prevede
		Basket basket = new Basket("aml");
		BasketElement basketElement = new BasketElement();
		basketElement.setDispoPdf(pdf);
		basket.getBasketElements().add(basketElement);
		pdf.setBasket(basket);
		
		// Se la dispositiva non risulta a vere AML lo verifichiamo utilizzando la stessa logica del basket ma con solo questa dispositiva
		AmlModel amlModel = ((AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class)).createAmlModel(csc, pdf, elencoCodiciModuloAML);
		
		pdf.setBasket(null);
		return amlModel != null;
	}
	
}
