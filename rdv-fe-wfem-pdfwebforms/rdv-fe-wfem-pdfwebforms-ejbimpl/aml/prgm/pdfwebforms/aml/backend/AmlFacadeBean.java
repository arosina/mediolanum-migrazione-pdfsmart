package prgm.pdfwebforms.aml.backend;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.aml.model.AmlModel;
import prgm.pdfwebforms.aml.model.CoraModel;
import prgm.pdfwebforms.aml.model.DispositivaAmlContainer;
import prgm.pdfwebforms.aml.model.NaturaModel;
import prgm.pdfwebforms.aml.model.OrigineModel;
import prgm.pdfwebforms.aml.model.RelazioniModel;
import prgm.pdfwebforms.aml.model.ScopoRapportoModel;
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfModuliAggiuntiviManager;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AmlFacadeBean extends PdfInstanceFacadeBean implements AmlFacade {
  
	private static final String SEZIONE_AML = "AML";
	private static final String MODULI_CON_QUESTIONARIO_AML = "MODULI_CON_QUESTIONARIO_AML_";
	
	private DoubleType	sogliaMinimaPerAggiuntivi = new DoubleType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void createAmlAndCoraModel(ClientSessionContext csc, PdfModel pdf) throws EJBException{
		try {

			// Nel processo tattico la pagina AML non esiste mai
			BooleanType processoTattico = PdfModuliAggiuntiviManager.isProcessoTattico(csc);
			if(processoTattico.booleanValue())
				return;
			
			List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);

			// Se il modulo è un AML stand-alone non faccio nulla
			if(!pdf.isMultiPdf()) {
				for(int i=0;i<elencoCodiciModuloAML.size();i++) { 
					if(pdf.mainPdfAnag().getPdfMomCode().equals(elencoCodiciModuloAML.get(i)))
						return;
				}
			}
			
			AmlModel amlModel = createAmlModel(csc, pdf, elencoCodiciModuloAML);
			CoraModel coraModel = createCoraModel(csc, amlModel, pdf, elencoCodiciModuloAML);
			
			pdf.setAmlModel(amlModel);
			pdf.setCoraModel(coraModel);
			if(pdf.isInBasket()) {
				Basket basket = pdf.getBasket();
				for(BasketElement be : basket.getBasketElements()){
					be.getDispoPdf().setAmlModel(amlModel);
					be.getDispoPdf().setCoraModel(coraModel);
				}
			}
			
		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public AmlModel createAmlModel(ClientSessionContext csc, PdfModel pdf, List<String> elencoCodiciModuloAML) throws EJBException{
		try {
		
			// La pagina AML la mostriamo solo quando da basket, altrimenti il tutto è gestito da prgm.pdfwebforms.drivers.PdfModuliAggiuntiviManager
			if(!pdf.isInBasket())
				return null;
			
			// Soglia mimima nei controlli sugli aggiuntivi
			sogliaMinimaPerAggiuntivi = PdfConfig.getParamAsDouble(csc, SEZIONE_AML, "SOGLIA_MIMINA_AGGIUNTIVI");
			if(sogliaMinimaPerAggiuntivi.isNull())
				sogliaMinimaPerAggiuntivi = new DoubleType(5000); // Non configurata: default
			
			AmlModel amlModel = new AmlModel();				
			
			for(int i=0;i<elencoCodiciModuloAML.size();i++) {
				String codiceModuloAML = elencoCodiciModuloAML.get(i);
				List<String> elencoCodiciModuloConAML = PdfConfig.getParamAsStringArray(csc, SEZIONE_AML, MODULI_CON_QUESTIONARIO_AML+codiceModuloAML,"",true);
				if(!elencoCodiciModuloConAML.isEmpty()) {
					Basket basket = pdf.getBasket();
					for(BasketElement be : basket.getBasketElements())
						addDispoToAml(csc, be.getDispoPdf(), amlModel, elencoCodiciModuloConAML, codiceModuloAML);
				}
			}
	
			if(amlModel.hasNatura())
				((NaturaFacade)FacadeLoader.getFacade(csc, NaturaFacade.class)).init(csc, pdf, amlModel.getNatura());
			
			if(amlModel.hasOrigine())
				((OrigineFacade)FacadeLoader.getFacade(csc, OrigineFacade.class)).init(csc, pdf, amlModel.getOrigine());
			
			if(amlModel.hasRelazioni()) {
				((RelazioniFacade)FacadeLoader.getFacade(csc, RelazioniFacade.class)).init(csc, pdf, amlModel.getRelazioni());
				if(amlModel.getRelazioni().isRelazioniContrastanti())
					amlModel.setErrorMsg("Sono state indicate relazioni differenti per uno stesso soggetto terzo che compare più volte nei moduli di sottoscrizione. "+
									     "Prima di procedere è necessario uniformare la scelta e indicare una sola relazione per lo stesso soggetto");
			}
			
			// Se qualche pdf non è rimasto presente nell'elenco delle dispositive cui aggiungere il modulo AML ne annulliamo il codice mom
			// cosicchè non venga aggiunto. Nelle "init" delle singole sezioni infatti la dispositiva potrebbe essere stata rimossa
			for(BasketElement be : pdf.getBasket().getBasketElements()) {
				if(amlModel.findPdfInAml(be.getDispoPdf()) == null)
					be.getDispoPdf().setCodiceModuloAmlHidden(null);
			}
			
			// Se negli elenchi delle dispo è presente almeno una dispositiva allora aml c'è, altrimenti no 
			return amlModel.hasAML() ? amlModel : null;
			
		}catch(DAOException | Exception e) {
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private CoraModel createCoraModel(ClientSessionContext csc, AmlModel amlModel, PdfModel pdf, List<String> elencoCodiciModuloAML) throws Exception{
		
		List<String> elencoModuliConCORA = PdfConfig.getParamAsStringArray(csc, SEZIONE_AML, "MODULI_CON_CORA","",true);
		if(!elencoModuliConCORA.isEmpty()) {
			CoraModel coraModel = new CoraModel();
			if(pdf.isInBasket()) {
				for(BasketElement be : pdf.getBasket().getBasketElements())
					addDispoToCora(elencoModuliConCORA, be.getDispoPdf(), coraModel);				
			}else {
				addDispoToCora(elencoModuliConCORA, pdf, coraModel);	
			}
			if(!coraModel.getElencoDispositive().isEmpty())
				return coraModel;
		}
		
		if(pdf.isInBasket()) {
			if(amlModel != null) { // Se esiste il layer aml allora esiste anche il layer cora
				CoraModel coraModel = new CoraModel();
				Basket basket = pdf.getBasket();
				for(BasketElement be : basket.getBasketElements())
					addDispoToCora(be.getDispoPdf(), coraModel, amlModel);
				return coraModel;
			}				
		}else {
			// Se la dispositiva ha aggiunto un modulo AML con la gestione non in basket (prgm.pdfwebforms.drivers.PdfModuliAggiuntiviManager) 
			// aggiungiamo anche il cora
			for(PdfAnagModel pdfAnag : pdf.getPdfAnags()) {
				String amlMomCode = pdfAnag.getPdfMomCode().toString();
				if(!amlMomCode.isEmpty() && elencoCodiciModuloAML.contains(amlMomCode)) {
					CoraModel coraModel = new CoraModel();
					DispositivaAmlContainer dc = new DispositivaAmlContainer(pdf);
					coraModel.getElencoDispositive().add(dc);
					return coraModel;
				}
			}
		}		
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addDispoToAml(ClientSessionContext csc, PdfModel pdf, AmlModel amlModel, 
							   List<String> elencoCodiciModuloConAML, String codiceModuloAML) throws DAOException, Exception{

		OrigineModel origine = amlModel.getOrigine();
		NaturaModel natura = amlModel.getNatura();
		RelazioniModel relazioni = amlModel.getRelazioni();

		PdfAnagModel pdfAnagContratto = pdfAnagContratto(pdf);
		PdfDataModel pdfDataContratto = pdfDataContratto(pdf);
		
		if(hasDispoAml(csc, pdf, pdfAnagContratto, pdfDataContratto, elencoCodiciModuloConAML)){
			pdf.setCodiceModuloAmlHidden(codiceModuloAML);
			DispositivaAmlContainer dc = new DispositivaAmlContainer(pdf);
			if(isDispoIniziale(pdfDataContratto)) {
				
				natura.getElencoDispositive().add(dc);
				natura.getElencoScopiRapporto().add(new ScopoRapportoModel());
				
				relazioni.getElencoDispositive().add(dc);
			}
			
			origine.getElencoDispositive().add(dc); // L'origine della provvista l'hanno tutte le dispo con AML
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addDispoToCora(List<String> elencoModuliConCORA, PdfModel pdf, CoraModel coraModel) {
		for(PdfAnagModel pdfAnag : pdf.getPdfAnags()) {
			if(hasDispoCora(pdfAnag, elencoModuliConCORA)) {
				DispositivaAmlContainer dc = new DispositivaAmlContainer(pdf);
				coraModel.getElencoDispositive().add(dc);
				return;
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addDispoToCora(PdfModel pdf, CoraModel coraModel, AmlModel amlModel) {
		// Se il pdf esiste in almeno uno degli elenchi AML allora ha anche il cora
		if(pdf == amlModel.findPdfInAml(pdf)) { 
			DispositivaAmlContainer dc = new DispositivaAmlContainer(pdf);
			coraModel.getElencoDispositive().add(dc);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean hasDispoAml(ClientSessionContext csc, PdfModel pdf, PdfAnagModel pdfAnag, PdfDataModel pdfData, 
							    List<String> elencoCodiciModuloConAML) throws DAOException, Exception{
		
		// Lo switch aggiuntivo non ha mai aml
		if(isSwitchAggiuntivo(pdf, pdfData))
			return false;
		
		String momCode = pdfAnag.getPdfMomCode().toString();
		String driverName = pdfAnag.getPdfDriverName().toString();
		boolean hasAml = (!momCode.isEmpty() && elencoCodiciModuloConAML.contains(momCode)) || 
						 (!driverName.isEmpty() && elencoCodiciModuloConAML.contains(driverName));
		if(!hasAml)
			return false;

		if(!isDispoIniziale(pdfData)) {
			
			// RFC #263223: negli aggiutivi 
			// Se c'è un PEP persona fisica, aml lo lasciamo sempre
			if(existOnePep(csc, pdf, pdfData))
				return true;
			
			//  Se esiste un soggetto residente in paese ad alto rischio, aml lo lasciamo sempre
			if(existOneGradoRischioResidenza(csc, pdf, pdfData))
				return true;
			
			// Altrimenti se l'importo è minore di 5.000 aml non c'è
			OrigineModel origineDispo = ((OrigineFacade)FacadeLoader.getFacade(csc, OrigineFacade.class)).importiOrigineDispositiva(csc, pdf);
			if(origineDispo.getImportoContestualeTotale().doubleValue() <= sogliaMinimaPerAggiuntivi.doubleValue())
				return false;
		}
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isSwitchAggiuntivo(PdfModel pdf, PdfDataModel pdfData) {
		StringType numeroContratto = (StringType)pdfData.read(PdfPredefinedFields.NUMERO_CONTRATTO);
		return pdf.isMultiPdf() && pdfData.getIsSwitch().booleanValue() && numeroContratto != null && !numeroContratto.isNull();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean existOnePep(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData)  throws DAOException, Exception{
		StringBuilder codClienti = new StringBuilder();
		PdfPersonModel p = pdfData.getPerson(1);
		if(p != null && !p.isEmty() && p.propertyToString("isPersonaFisica").equals("true"))
			codClienti.append("'"+(p.getNdg().isNull()?p.getIdCensimento():p.getNdg())+"',");
		if(codClienti.length() > 0) {
			MapCommandDataModel m = new MapCommandDataModel();
			codClienti.deleteCharAt(codClienti.length()-1);
			m.addProperty("codClienti", new StringType(codClienti.toString()));
			BooleanType existOnePep = (BooleanType)new DAOObject(csc, OrigineFacadeBean.DAO_XML_AML_NAME).executeQueryAccess("existOnePep", m).getSingleResult();
			if(existOnePep.booleanValue())
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean existOneGradoRischioResidenza(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData)  throws DAOException, Exception{
		DAOObject dao = new DAOObject(csc, OrigineFacadeBean.DAO_XML_AML_NAME);
		PdfPersonModel p = pdfData.getPerson(1);
		if(p != null && !p.isEmty()) {
			if(hasClinteGradoDiRischioResidenzaAlto(dao, p.getNdg(), p.getIdCensimento()))
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfPersonModel personaFirmatariaAggiuntivi(PdfDataModel pdfData) {
		PdfPersonModel p = pdfData.getPerson(1);
		if(p == null || p.isEmty())
			return null;
		// Persona fisica
		if(p.propertyToString("isPersonaFisica").equals("true"))
			return p;
		
		// Legale Rappresentante (nei post-vendita sempre il secondo cliente)
		p = pdfData.getPerson(2);
		if(p == null || p.isEmty())
			return null;
		return p;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean hasClinteGradoDiRischioResidenzaAlto(DAOObject dao, StringType ndg, StringType idCensimento) throws DAOException{
		MapCommandDataModel m = new MapCommandDataModel();
		m.addProperty("codCliente", ndg.isNull() ? idCensimento : ndg);
		IntegerType gradoRischio = (IntegerType)dao.executeQueryAccess("gradoRischioResidenza", m).getSingleResult();
		if(gradoRischio == null)
			gradoRischio = new IntegerType(0);
		return gradoRischio.intValue() == 1;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean hasDispoCora(PdfAnagModel pdfAnag, List<String> elencoCodiciModuloConCora) {
		String momCode = pdfAnag.getPdfMomCode().toString();
		String driverName = pdfAnag.getPdfDriverName().toString();
		return (!momCode.isEmpty() && elencoCodiciModuloConCora.contains(momCode)) || 
			   (!driverName.isEmpty() && elencoCodiciModuloConCora.contains(driverName));
	}

	/***********************************************************************************************/
	private static final String NUMERO_POLIZZA = "numeroPolizza";
	/***********************************************************************************************/
	static boolean isDispoIniziale(PdfDataModel pdfData) {
		boolean iniziale = false;
		StringType numeroContratto = (StringType)pdfData.read(NUMERO_POLIZZA);
		
		if(numeroContratto == null || numeroContratto.isNull())
			numeroContratto = (StringType)pdfData.read(PdfPredefinedFields.NUMERO_CONTRATTO);
		
		if(numeroContratto == null || numeroContratto.isNull())
			iniziale = true;
		
		return iniziale;
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public boolean verifyAml(AmlModel amlModel) throws EJBException {
		
		try {
			boolean naturaOk = amlModel.getNatura().verify();
			boolean origineOk = amlModel.getOrigine().verify();
			boolean relazioniOk = amlModel.getRelazioni().verify();
			return naturaOk && origineOk && relazioniOk;

		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void addAmlHiddenModulesToBasket(ClientSessionContext csc, PdfModel pdf) throws EJBException{
		
		try {			
			removeAllAmlHiddenModulesFromBasket(csc, pdf);

			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements())
				addAmlHiddenModule(csc, be.getDispoPdf());

		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void addAmlHiddenModule(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		// Se la dispo non ha impostato il codice modulo aml non va gestita
		if(pdf.getCodiceModuloAmlHidden() == null)
			return;
		
		PdfBaseDriver baseDrv = new PdfBaseDriver();
		baseDrv.initInstance(csc, pdf);
		
		PdfDataModel avPdfData = new PdfDataModel();
		StringType gcm = Basket.globalBasketPdfCompilationModes(pdf);
		baseDrv.addOtherPdf(pdf.getCodiceModuloAmlHidden(), avPdfData);
		avPdfData.setCompilationModes(gcm);
		
		gotoPdf(csc, pdf, pdf.getPdfData().getPdfs().size()-1, true);
		pdf.getPdfData().initPdfsFromData();
		gotoPdf(csc, pdf, 0, false);
		pdf.getPdfData().initPdfsFromData();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void removeAllAmlHiddenModulesFromBasket(ClientSessionContext csc, PdfModel pdf) throws EJBException{
		
		try {			
			// Nel processo tattico la pagina AML non esiste e ci pensa "PdfModuliAggiuntiviManager" a gestire i moduli anche nei basket
			BooleanType processoTattico = PdfModuliAggiuntiviManager.isProcessoTattico(csc);
			if(processoTattico.booleanValue())
				return;
			
			if(!pdf.isInBasket())
				return;
			
			List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);
			for(int i=0;i<elencoCodiciModuloAML.size();i++) {
				String codiceModuloAML = elencoCodiciModuloAML.get(i);			
				Basket basket = pdf.getBasket();
				for(BasketElement be : basket.getBasketElements()){
					PdfBaseDriver baseDrv = new PdfBaseDriver();
					baseDrv.initInstance(csc, be.getDispoPdf());
					baseDrv.removeOtherPdf(codiceModuloAML); 					
				}
			}
		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public void removePdfAmlHiddenModules(ClientSessionContext csc, PdfModel pdf) throws EJBException{
		try {			
			// Nel processo tattico la pagina AML non esiste e ci pensa "PdfModuliAggiuntiviManager" a gestire i moduli anche nei basket
			BooleanType processoTattico = PdfModuliAggiuntiviManager.isProcessoTattico(csc);
			if(processoTattico.booleanValue())
				return;

			if(!pdf.isInBasket())
				return;
			
			PdfBaseDriver baseDrv = new PdfBaseDriver();
			baseDrv.initInstance(csc, pdf);
			List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);
			for(int i=0;i<elencoCodiciModuloAML.size();i++) {
				String codiceModuloAML = elencoCodiciModuloAML.get(i);			
				baseDrv.removeOtherPdf(codiceModuloAML); 					
			}
		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfAnagModel pdfAnagContratto(PdfModel pdf) {
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.isMultiPdf())
			return pdf.getPdfAnags().get(1);
		return pdf.mainPdfAnag();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfDataModel pdfDataContratto(PdfModel pdf) {
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.isMultiPdf())
			return (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
		return pdf.mainPdfData();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String findFieldValue(PdfDataModel pdfData, String fieldsNamesAsString) {
		String res = "";
		String[] fieldsName = fieldsNamesAsString.split(",");
		for(String fn : fieldsName) {
			AbstractType f = pdfData.read(fn);
			if(f != null && !f.isNull())
				return f.toString();
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType stringField(PdfDataModel pdfData, String fieldName) {
		StringType res = (StringType)pdfData.read(fieldName);
		return res == null ? new StringType() : res;
	}

	/***********************************************************************************************/
	/* ************************************** MOM ************************************************ */
	/***********************************************************************************************/
	@Override
	public void createCoraForMom(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		try {
			if(!pdf.isInInserimentoMOM())
				return;
			CodDescDataList dl = new CodDescDataList();
			List<String> elencoCodiciModuloConCORA = prgm.pdfwebforms.core.PdfConfig.getParamAsStringArray(csc, SEZIONE_AML, "MODULI_CON_CORA","",true);
			List<String> elencoCodiciModuloConAML = getElencoCodiciModuloAMLForMom(csc);
			elencoCodiciModuloConCORA.addAll(elencoCodiciModuloConAML);			
			if(hasDispoCora(pdf.getPdfAnag(), elencoCodiciModuloConCORA)) {
				CodDescData d = new CodDescData(); d.setCod("1"); d.setDescr("1 - Tipologia A"); dl.addCodDescData(d);
				d = new CodDescData(); d.setCod("2"); d.setDescr("2 - Tipologia B"); dl.addCodDescData(d);
			}else {
				pdf.getPdfData().setCoraFb(new StringType());
			}
			pdf.getPdfData().addCodDescField("coraFb", dl); // Aggiungo comunque la tendina: se esiste e ha elementi allora il modulo ha il co.ra.
		}catch(Exception e) {
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private List<String> getElencoCodiciModuloAMLForMom(ClientSessionContext csc) throws Exception{
		List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);
		List<String> elencoCodiciModuloConAML = new ArrayList();
		for(int i=0;i<elencoCodiciModuloAML.size();i++) {
			String codiceModuloAML = elencoCodiciModuloAML.get(i);
			elencoCodiciModuloConAML = prgm.pdfwebforms.core.PdfConfig.getParamAsStringArray(csc, SEZIONE_AML, MODULI_CON_QUESTIONARIO_AML+codiceModuloAML,"",true);
			if(!elencoCodiciModuloAML.isEmpty())
				break;
		}
		return elencoCodiciModuloConAML;
	}

}
