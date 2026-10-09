package prgm.pdfwebforms.aml.backend;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.aml.model.DispositivaAmlContainer;
import prgm.pdfwebforms.aml.model.RelazioniModel;
import prgm.pdfwebforms.aml.model.SoggettoRelazioneModel;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RelazioniFacadeBean extends FacadeObject implements RelazioniFacade{

	private static final String FNAME_COD_TIPO_RELAZIONE_CONTR 	= "codTipoRelazioneContraente";
	private static final String FNAME_COD_TIPO_RELAZIONE_ASSIC  = "codTipoRelazioneAssicurando";
	
	private static final String FNAME_IS_GIA_CLIENTE 		= "isGiaCliente";
	private static final String FNAME_CODICE_CLIENTE 		= "codiceCliente";
	private static final String FNAME_NOME_CLIENTE 			= "nome";
	private static final String FNAME_COGNOME_CLIENTE 		= "cognome";
	private static final String FNAME_RAGSOC_CLIENTE 		= "ragioneSociale";
	private static final String FNAME_CF_CLIENTE 			= "codiceFiscale";
	private static final String FNAME_CFPIVA_CLIENTE 		= "codiceFiscalePartitaIva";
	
	private static final String BENEFICIARIO_PREFIX 		= "Beneficiario";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static class CodiciTipoRelazioni{
		private static CodDescDataList codScopoRelazioneDl = new CodDescDataList();
		static {
			CodDescData d = new CodDescData(); d.setCod("001"); d.setDescr("1 - Coniuge, rapporto di parentela o affinità"); codScopoRelazioneDl.addCodDescData(d);
						d = new CodDescData(); d.setCod("002"); d.setDescr("2 - Rapporto affettivo (convivente more uxorio)"); codScopoRelazioneDl.addCodDescData(d);
						d = new CodDescData(); d.setCod(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_AZIENDALE); 	d.setDescr("3 - Rapporto aziendale o professionale"); codScopoRelazioneDl.addCodDescData(d);
						d = new CodDescData(); d.setCod(SoggettoRelazioneModel.COD_TIPO_RELAZIONE_ALTRO); 		d.setDescr("4 - Altro"); codScopoRelazioneDl.addCodDescData(d);
		}
		private static CodDescDataList getCodTipoRelazioneDl() {
			return codScopoRelazioneDl;
		}
	}

	/***********************************************************************************************/
	// Usato dal driver del modulo AML adeguataverificainvestimento
	/***********************************************************************************************/
	public RelazioniModel relazioniDispositiva(ClientSessionContext csc, PdfModel pdf) throws EJBException {		
		try {
			RelazioniModel relazioni = new RelazioniModel();
			relazioni.getElencoDispositive().add(new DispositivaAmlContainer(pdf));
			innerInit(csc, pdf, relazioni, false);
			return relazioni;
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void init(ClientSessionContext csc, PdfModel pdf, RelazioniModel relazioni) throws EJBException {
		innerInit(csc, pdf, relazioni, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void innerInit(ClientSessionContext csc, PdfModel pdf, RelazioniModel relazioni, boolean fromAmlPage) throws EJBException {
		try {
			StringType nomiCampoNdgTerzoPagatore = PdfConfig.getParamAsString(csc, "AML", "NOMI_CAMPO_NDG_TERZO_PAGATORE");
			for(int dispoIdx=relazioni.getElencoDispositive().size()-1;dispoIdx>=0;dispoIdx--) {
				
				DispositivaAmlContainer dc = relazioni.getElencoDispositive().get(dispoIdx);
				PdfDataModel pdfData = AmlFacadeBean.pdfDataContratto(dc.getPdf());
				
				int numSoggPerDispo = 0;

				// Primo co-sottoscrittore
				if(!isPolizza(pdfData) && AmlFacadeBean.isDispoIniziale(pdfData)) // Nelle polizze e negli aggiuntivi il secondo cliente è sempre il legale rappresentante
					numSoggPerDispo += (aggiungiCosottoscrittore(pdfData, relazioni, 2) != null ? 1 : 0);
				// Secondo co-sottoscrittore o assicurato polizza
				SoggettoRelazioneModel assicurato = aggiungiCosottoscrittore(pdfData, relazioni, 3);
				if(assicurato != null) {
					numSoggPerDispo++;
					if(!assicurato.getIsAssicurato().booleanValue())
						assicurato = null;
				}				
				// Terzo co-sottoscrittore
				numSoggPerDispo += (aggiungiCosottoscrittore(pdfData, relazioni, 4) != null ? 1 : 0);			
				
				numSoggPerDispo += aggiungiTerzoPagatore(csc, dc.getPdf(), pdfData, relazioni, nomiCampoNdgTerzoPagatore.toString());
				
				for(int beneficiarioIdx=1;beneficiarioIdx<=8;beneficiarioIdx++)
					numSoggPerDispo += aggiungiBeneficiario(pdfData, relazioni, "", beneficiarioIdx, assicurato);
					
				for(int beneficiarioIdx=1;beneficiarioIdx<=2;beneficiarioIdx++)
					numSoggPerDispo += aggiungiBeneficiario(pdfData, relazioni, "Vita", beneficiarioIdx, assicurato);
				
				if(assicurato != null)
					ordinaElencoSoggetti(assicurato.getElencoBeneficiariAssicurato());
					
				// Se la dispositiva non ha soggetti con relazioni la eliminiamo dall'elenco
				if(numSoggPerDispo == 0)
					relazioni.eliminaDispositiva(dispoIdx);
			}		
			
			// Se non ci sono soggetti svuoto l'elenco dispositive così da non far apparire il tab
			if(relazioni.getMappaSoggetti().isEmpty()) {
				relazioni.getElencoDispositive().clear();
				return;
			}
			
			// Genero e sorto l'elenco dei soggetti
			generaElencoSoggetti(relazioni, fromAmlPage);
			
		}catch(Exception e) {
			throw new EJBException(e);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SoggettoRelazioneModel aggiungiCosottoscrittore(PdfDataModel pdfData, RelazioniModel relazioni, int personIdx) {
		
		PdfPersonModel p = pdfData.getPerson(personIdx);
		if(p == null || p.isEmty() || p.isNotFound())
			return null;

		// Se il cliente è un legale rappresentante non lo consideriamo
		String legRappr = AmlFacadeBean.findFieldValue(pdfData, "tipoCliente"+personIdx+
																",isCliente"+personIdx+"LegaleRappresentante"+
																",legaleRappresentante"+personIdx);
		if(legRappr.equals("true") || legRappr.equals("LEGALERAPP"))
			return null;
	
		SoggettoRelazioneModel sogg = aggiungiPdfPerson(p, relazioni);
		if(sogg != null) {
			if(isAssicuratoPolizza(pdfData, personIdx)) { // Nelle polizze il terzo cliente è sempre l'asicurato oppure non esiste
				sogg.setIsAssicurato(new BooleanType(true));
				sogg.impostaCodTipoRelazioneContraente(relazioni, pdfData.readAsString("tipoRelazioneContraenteAssicurando"),
					    										  pdfData.readAsString("descrizioneTipoRelazioneContraenteAssicurando"));					
			}else {
				sogg.setIsCosottoscrittore(new BooleanType(true));
			}
			return sogg;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static int aggiungiTerzoPagatore(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, 
											  RelazioniModel relazioni, String nomiCampoNdgTerzoPagatore){
		
		String ndgTerzoPagatore = AmlFacadeBean.findFieldValue(pdfData, nomiCampoNdgTerzoPagatore);
		if(!ndgTerzoPagatore.isEmpty()) {
			try {
				StringType savCodAgeImpersonato = pdf.getPdfData().getCodAgeImpersonato();
				pdf.getPdfData().setCodAgeImpersonato(new StringType());
				PdfPersonModel p = DataLoader.loadPersonOnDriver(csc, pdf, ndgTerzoPagatore, "");
				pdf.getPdfData().setCodAgeImpersonato(savCodAgeImpersonato);
				SoggettoRelazioneModel sogg = aggiungiPdfPerson(p, relazioni);
				if(sogg != null) {
					sogg.setIsTerzoPagatore(new BooleanType(true));
					sogg.impostaCodTipoRelazioneContraente(relazioni, pdfData.readAsString("tipoRelazioneContraenteTerzoPagatore"),
														    		  pdfData.readAsString("descrizioneTipoRelazioneContraenteTerzoPagatore"));
					return 1;
				}
			}catch(Exception | DAOException e) {
				// do nothing
			}
		}
		return 0;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static int aggiungiBeneficiario(PdfDataModel pdfData, RelazioniModel relazioni, String suffix, int idxSoggetto, 
											SoggettoRelazioneModel assicurato) throws Exception {

		String beneficiarioSuffix = BENEFICIARIO_PREFIX+suffix+idxSoggetto;
		
		String isPersonaFisicaBeneficiario = pdfData.readAsString("isPersonaFisica"+beneficiarioSuffix);
		if(isPersonaFisicaBeneficiario.isEmpty())
			return 0;
		
		if(!isPersonaFisicaBeneficiario.equals("true")) {
			return aggiungiBeneficiarioPG(pdfData, relazioni, suffix, idxSoggetto, assicurato);
		}
		
		String codiceFiscaleBeneficiario = pdfData.readAsString(FNAME_CF_CLIENTE+beneficiarioSuffix);
		if(codiceFiscaleBeneficiario.isEmpty())
			return 0;

		String ndg = pdfData.readAsString(FNAME_CODICE_CLIENTE+beneficiarioSuffix);
		String chiaveCli = ndg.isEmpty()?codiceFiscaleBeneficiario:ndg;
		SoggettoRelazioneModel sogg = relazioni.getMappaSoggetti().get(chiaveCli);
		if(sogg == null) {
			sogg = new SoggettoRelazioneModel();
			sogg.setCodiceFiscalePartitaIva(new StringType(codiceFiscaleBeneficiario));
			sogg.setNdg(new StringType(ndg));
			sogg.setNome(new StringType(pdfData.readAsString(FNAME_NOME_CLIENTE+beneficiarioSuffix)));
			sogg.setCognome(new StringType(pdfData.readAsString(FNAME_COGNOME_CLIENTE+beneficiarioSuffix)));
			sogg.setIsGiaCliente(new StringType(pdfData.readAsString(FNAME_IS_GIA_CLIENTE+beneficiarioSuffix)));
			sogg.setIsPersonaFisica(new BooleanType(isPersonaFisicaBeneficiario));
			relazioni.getMappaSoggetti().put(chiaveCli, sogg);			
		}
		sogg.setIsBeneficiario(new BooleanType(true));	
		
		sogg.impostaCodTipoRelazioneContraente(relazioni, pdfData.readAsString("tipoRelazioneContraente"+beneficiarioSuffix),
											    		  pdfData.readAsString("descrizioneTipoRelazioneContraente"+beneficiarioSuffix));
		// Per quando stiamo gestendo la specifica dispositiva dal driver impostiamo comunque i 2 campi prendendoli dal contratto
		// Successivamente li impostiamo sulla specifica istanza di soggetto creata per gestire le enne occorrenze del beneficiario nella pagina AML
		sogg.impostaCodTipoRelazioneAssicurando(relazioni, pdfData.readAsString("tipoRelazioneAssicurando"+beneficiarioSuffix),
														   pdfData.readAsString("descrizioneTipoRelazioneAssicurando"+beneficiarioSuffix));		

		if(assicurato != null)
			aggiungiBeneficiarioAssicurato(assicurato, sogg, relazioni, pdfData, beneficiarioSuffix);
		
		return 1;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static int aggiungiBeneficiarioPG(PdfDataModel pdfData, RelazioniModel relazioni, String suffix, int idxSoggetto, 
											  SoggettoRelazioneModel assicurato) throws Exception{

		String beneficiarioSuffix = BENEFICIARIO_PREFIX+suffix+idxSoggetto;
		
		String codiceFiscaleBeneficiario = pdfData.readAsString(FNAME_CFPIVA_CLIENTE+beneficiarioSuffix);
		if(codiceFiscaleBeneficiario.isEmpty())
			return 0;
		
		String ndg = pdfData.readAsString(FNAME_CODICE_CLIENTE+beneficiarioSuffix);
		String chiaveCli = ndg.isEmpty()?codiceFiscaleBeneficiario:ndg;
		SoggettoRelazioneModel soggPG = relazioni.getMappaSoggetti().get(chiaveCli);
		if(soggPG == null) {
			soggPG = new SoggettoRelazioneModel();
			soggPG.setCodiceFiscalePartitaIva(new StringType(codiceFiscaleBeneficiario));
			soggPG.setNdg(new StringType(ndg));
			soggPG.setCognome(new StringType(pdfData.readAsString(FNAME_RAGSOC_CLIENTE+beneficiarioSuffix)));
			soggPG.setIsGiaCliente(new StringType(pdfData.readAsString(FNAME_IS_GIA_CLIENTE+beneficiarioSuffix)));
			soggPG.setIsPersonaFisica(new BooleanType(false));
			relazioni.getMappaSoggetti().put(chiaveCli, soggPG);
		}		
		soggPG.setIsBeneficiarioPG(new BooleanType(true));
		
		aggiungiTitolareBeneficiarioPG(soggPG, pdfData, relazioni, suffix, idxSoggetto, 1, assicurato);
		aggiungiTitolareBeneficiarioPG(soggPG, pdfData, relazioni, suffix, idxSoggetto, 2, assicurato);
		return 1;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void aggiungiTitolareBeneficiarioPG(SoggettoRelazioneModel soggPG, PdfDataModel pdfData,
														RelazioniModel relazioni, String suffix, 
													   	int idxSoggetto, int idxTitolare, SoggettoRelazioneModel assicurato) throws Exception{
		
		String titolareSuffix = "Titolare"+idxTitolare+BENEFICIARIO_PREFIX+suffix+idxSoggetto;
		
		String codiceFiscaleTitolare = pdfData.readAsString(FNAME_CF_CLIENTE+titolareSuffix);
		if(codiceFiscaleTitolare.isEmpty())
			return;
		
		String ndg = pdfData.readAsString(FNAME_CODICE_CLIENTE+titolareSuffix);
		String chiaveCli = ndg.isEmpty()?codiceFiscaleTitolare:ndg;
		SoggettoRelazioneModel sogg = relazioni.getMappaSoggetti().get(chiaveCli);
		if(sogg == null) {
			sogg = new SoggettoRelazioneModel();
			sogg.setCodiceFiscalePartitaIva(new StringType(codiceFiscaleTitolare));
			sogg.setNdg(new StringType(ndg));
			sogg.setNome(new StringType(pdfData.readAsString(FNAME_NOME_CLIENTE+titolareSuffix)));
			sogg.setCognome(new StringType(pdfData.readAsString(FNAME_COGNOME_CLIENTE+titolareSuffix)));
			sogg.setIsGiaCliente(new StringType(pdfData.readAsString(FNAME_IS_GIA_CLIENTE+titolareSuffix)));
			sogg.setIsPersonaFisica(new BooleanType(true));
			relazioni.getMappaSoggetti().put(chiaveCli, sogg);
		}
		sogg.setIsTitolareBeneficiarioPG(new BooleanType(true));	
		
		sogg.impostaCodTipoRelazioneContraente(relazioni, pdfData.readAsString("tipoRelazioneContraente"+titolareSuffix),
											    		  pdfData.readAsString("descrizioneTipoRelazioneContraente"+titolareSuffix));		
		// Per quando stiamo gestendo la specifica dispositiva dal driver impostiamo comunque i 2 campi prendendoli dal contratto
		// Successivamente li impostiamo sulla specifica istanza di soggetto creata per gestire le enne occorrenze del beneficiario nella pagina AML
		sogg.impostaCodTipoRelazioneAssicurando(relazioni, pdfData.readAsString("tipoRelazioneAssicurando"+titolareSuffix),
														   pdfData.readAsString("descrizioneTipoRelazioneAssicurando"+titolareSuffix));		
		
		soggPG.getTitolariBeneficiarioPG().add(sogg);
		
		if(assicurato != null)
			aggiungiBeneficiarioAssicurato(assicurato, sogg, relazioni, pdfData, titolareSuffix);
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void aggiungiBeneficiarioAssicurato(SoggettoRelazioneModel assicurato, SoggettoRelazioneModel beneficiario, 
													   RelazioniModel relazioni, PdfDataModel pdfData, String suffix) throws Exception{
		SoggettoRelazioneModel beneficiarioAssicurato = assicurato.findBeneficiarioAssicurato(beneficiario);
		if(beneficiarioAssicurato == null) {
			beneficiarioAssicurato = (SoggettoRelazioneModel)Tools.cloneObject(beneficiario);
			beneficiarioAssicurato.addCodDescField(FNAME_COD_TIPO_RELAZIONE_ASSIC, CodiciTipoRelazioni.getCodTipoRelazioneDl());
			beneficiarioAssicurato.impostaCodTipoRelazioneAssicurando(relazioni, pdfData.readAsString("tipoRelazioneAssicurando"+suffix),
															   		  pdfData.readAsString("descrizioneTipoRelazioneAssicurando"+suffix));		
			assicurato.getElencoBeneficiariAssicurato().add(beneficiarioAssicurato);
		}else{
			String codTipoRelazione = pdfData.readAsString("tipoRelazioneAssicurando"+suffix);
			if( !codTipoRelazione.isEmpty() && 
				!beneficiarioAssicurato.getCodTipoRelazioneAssicurando().isNull() && 
				!codTipoRelazione.equals(beneficiarioAssicurato.getCodTipoRelazioneAssicurando().toString()))
				relazioni.setRelazioniContrastanti(true);				
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SoggettoRelazioneModel aggiungiPdfPerson(PdfPersonModel p, RelazioniModel relazioni) {
		
		if(p == null || p.isEmty() || p.isNotFound())
			return null;

		String codiceFiscalePartitaIva = p.propertyToString(FNAME_CFPIVA_CLIENTE);
		if(codiceFiscalePartitaIva.isEmpty())
			return null;
		
		StringType ndg = p.getNdg();
		String chiaveCli = ndg.isNull()?codiceFiscalePartitaIva:ndg.toString();
		SoggettoRelazioneModel sogg = relazioni.getMappaSoggetti().get(chiaveCli);
		if(sogg == null) {
			sogg = new SoggettoRelazioneModel();
			sogg.setCodiceFiscalePartitaIva(new StringType(codiceFiscalePartitaIva));
			sogg.setIdCensimento(p.getIdCensimento());
			sogg.setNdg(ndg);
			sogg.setNome(new StringType(p.propertyToString(FNAME_NOME_CLIENTE)));
			sogg.setCognome(new StringType(p.propertyToString(FNAME_COGNOME_CLIENTE)));
			sogg.setIsGiaCliente(new StringType(p.propertyToString(FNAME_IS_GIA_CLIENTE).equals("true")?"S":"N"));
			sogg.setIsPersonaFisica(new BooleanType(p.propertyToString("isPersonaFisica")));
			relazioni.getMappaSoggetti().put(chiaveCli, sogg);
		}
		return sogg;
	}
	
	/***********************************************************************************************/
	private static final String TIPO_PERSONA_FISICA = "F";
	private static final String TIPO_PERSONA_GIURIDICA = "G";
	private static final String ACCORDION_CHIUSO = "0";
	private static final String ACCORDION_APERTO = "1";
	/***********************************************************************************************/
	private static void generaElencoSoggetti(RelazioniModel relazioni, boolean fromAmlPage) {
		Map<String, SoggettoRelazioneModel> mappaSoggetti = relazioni.getMappaSoggetti();
		ListType elencoSoggetti = relazioni.getElencoSoggetti();
	    Iterator it = mappaSoggetti.keySet().iterator();
	    while(it.hasNext()){
	    	String cliKey = (String)it.next();
	    	SoggettoRelazioneModel sogg = mappaSoggetti.get(cliKey);
	    	sogg.addCodDescField(FNAME_COD_TIPO_RELAZIONE_CONTR, CodiciTipoRelazioni.getCodTipoRelazioneDl());
	    	elencoSoggetti.add(sogg);
	    }		
	    if(fromAmlPage)
	    	ordinaElencoSoggetti(elencoSoggetti);
	    
	    // Predispongo la stringa con i caratteri 0/1 per lo stato degli accordion dove aperto è solo il primo
	    StringBuilder globalSoggAccordionStatus = new StringBuilder();
	    for(int i=0;i<relazioni.getElencoSoggetti().size();i++){ 
			SoggettoRelazioneModel sogg = (SoggettoRelazioneModel)relazioni.getElencoSoggetti().get(i);
			globalSoggAccordionStatus.append(ACCORDION_CHIUSO);
			if(sogg.getIsAssicurato().booleanValue() && sogg.getElencoBeneficiariAssicurato().size() > 0) {
			    for(int j=0;j<relazioni.getElencoSoggetti().size();j++)
			    	globalSoggAccordionStatus.append(ACCORDION_CHIUSO);
			}
	    }
	    if(globalSoggAccordionStatus.length() >  0)
	    	relazioni.setGlobalSoggAccordionStatus(new StringType(ACCORDION_APERTO+globalSoggAccordionStatus.substring(1)));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void ordinaElencoSoggetti(ListType elencoSoggetti) {
		List<SoggettoRelazioneModel> elencoSoggettiList = elencoSoggetti.getElements();
		Collections.sort(elencoSoggettiList, new java.util.Comparator<SoggettoRelazioneModel>(){
			public int compare(SoggettoRelazioneModel sogg1, SoggettoRelazioneModel sogg2) {
				String mainCriteriaSogg1 = sogg1.getIsPersonaFisica().booleanValue() ? TIPO_PERSONA_FISICA : TIPO_PERSONA_GIURIDICA;
				String mainCriteriaSogg2 = sogg2.getIsPersonaFisica().booleanValue() ? TIPO_PERSONA_FISICA : TIPO_PERSONA_GIURIDICA;
				String cmp1 = mainCriteriaSogg1+sogg1.getCognome()+sogg1.getNome();
				String cmp2 = mainCriteriaSogg2+sogg2.getCognome()+sogg2.getNome();
				return cmp1.compareTo(cmp2);
			}
		});	    
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean isPolizza(PdfDataModel pdfData) {
		return pdfData.fieldExist("codiceClienteBeneficiario1") || pdfData.fieldExist("codiceClienteBeneficiarioVita1");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean isAssicuratoPolizza(PdfDataModel pdfData, int soggIdx) {
		return soggIdx == 3 && isPolizza(pdfData) && pdfData.fieldExist("ndgCliente3");
	}
	
}
