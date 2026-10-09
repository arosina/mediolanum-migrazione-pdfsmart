package prgm.pdfwebforms.dataloader;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataentryutil.PersonAutocompleteModel;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataResponse;
import prgm.pdfwebforms.legalerappresentante.LegaleRappresentanteManager;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.mom.CostantiMOM;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class DataLoader {

	public static final String DAO_XML_NAME = "PdfWebForms.PdfDataLoader";
	public static int MAX_NUM_CLIENTI = 20;
	
	private static final int FB_CUSTOMERS = -1;
	private static final int ALL_CUSTOMERS = 0;
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void initPersons(ClientSessionContext csc, PdfModel pdf) throws Exception{
		loadPersons(csc, pdf, pdf.getPdfData(), true);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void loadPersons(ClientSessionContext csc, PdfModel pdf) throws Exception{
		loadPersons(csc, pdf, pdf.getPdfData(), false);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static void reloadAllPdfsPersons(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(pdf.isMultiPdf()){
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				loadPersons(csc, pdf, pdfDataElement, false);
			}
		}else{
			loadPersons(csc, pdf, pdf.getPdfData(), false);
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static void loadPersons(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData, boolean onInit) throws Exception{
		
		try{
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
	
			// Load empty person only once
			if(pdf.getEmptyAnagraficaSoggetto() == null){
				PdfPersonModel emptyAnagraficaSoggetto = new PdfPersonModel();
				dao.executeQueryAccess("loadEmptyAnagraficaSoggetto",emptyAnagraficaSoggetto);
				pdf.setEmptyAnagraficaSoggetto(emptyAnagraficaSoggetto);
			}
			
			// Init signs
			PdfEngine.initPdfSignFieldInfosPropertyValues(pdfData);	
			
			// Init agente impersonato, se presente,, solo sul pdfData principale
			if(!pdf.getPdfData().getCodAgeImpersonato().isNull() && pdf.getPdfData().getAgenteImpersonato() == null) {
				PdfPersonModel agenteImpersonato = loadAgeImpersonatoPerson(csc, pdfData, pdf, pdf.getPdfData().getCodAgeImpersonato());
				pdf.getPdfData().setAgenteImpersonato(agenteImpersonato);
			}
			
			// Init family banker data
			PdfPersonModel agente = loadAgePerson(csc, pdfData, pdf, false);
			pdfData.setAgente(agente);

			// Init customer data
			loadCustomerPersons(csc, pdfData, pdf, pdfData.getAgente().getCodAgente(), onInit);
			
			// Init pdf fields with the correct pdf datatype overriding customer resultset datatype
			PdfEngine.initPdfFieldInfosPropertyValues(pdfData, onInit);
			return;
			
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}catch(Exception e){
			throw e;
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static PdfPersonModel loadAgePersonOnAutocomplete(ClientSessionContext csc,  String codAgente) throws Exception, DAOException{
		PdfModel pdf = new PdfModel();
		pdf.setEmptyAnagraficaSoggetto(new PdfPersonModel());
		pdf.getPdfData().addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente));
		return loadAgePerson(csc, pdf.getPdfData(), pdf, true);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static PdfPersonModel loadAgePerson(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf, boolean onAutocomplete) throws Exception{
		
		try{
			PdfPersonModel agente = new PdfPersonModel();
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			
			AbstractType codAgente = pdfData.readProperty(PdfPredefinedFields.AGENTE_CODICE);
			if(codAgente == null || codAgente.isNull()){
				if(pdf.getMainCodAgente() != null){
					codAgente = pdf.getMainCodAgente();
				}else{
					agente = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
					dao.executeQueryAccess("loadEmptyDatiAgente",agente);

					pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType());
					agente.fillPdfDataFromAgente(pdf, pdfData, false);
					for(int i=0;i<pdfData.getPdfs().size();i++){
						PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
						pdfDataElement.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType());
						agente.fillPdfDataFromAgente(pdf, pdfDataElement, false);
					}
					pdf.setMainCodAgente(null);
					
					agente.setAgente(true);
					return agente;
				}
			}
			
			codAgente = new StringType(Tools.unFillSx(codAgente.toString(),'0'));
				
			PdfPersonModel key = new PdfPersonModel();
			key.setCodAgente(new StringType(codAgente.toString()));
			dao.executeQueryAccess("loadNdgAgente",key);
			if(key.getNdg().isNull()){
				agente = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
				agente.setCodAgente(new StringType(codAgente.toString()));
			}else{
				agente = loadPerson(csc, dao, pdf, key.getNdg().toString(), codAgente.toString(), true, true, false, ALL_CUSTOMERS);
				if(!onAutocomplete && !pdf.getIsSede().booleanValue() && agente.isNotFound())
					throw new Exception("Il Family Banker con codice ["+codAgente+"] non è stato trovato in anagrafica");
			}
			dao.executeQueryAccess("loadDatiAgente",agente);
			
			pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente.toString()));
			agente.fillPdfDataFromAgente(pdf, pdfData, true);
			for(int i=0;i<pdfData.getPdfs().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
				pdfDataElement.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente.toString()));
				agente.fillPdfDataFromAgente(pdf, pdfDataElement, true);
			}
			pdf.setMainCodAgente(new StringType(Tools.fillSx(codAgente.toString(),'0',10)));
			
			agente.setAgente(true);
			return agente;
			
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static PdfPersonModel loadAgeImpersonatoPerson(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf, StringType codAgente) throws Exception{
		
		try{
			PdfPersonModel agente = null;
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			
			codAgente = new StringType(Tools.unFillSx(codAgente.toString(),'0'));
				
			PdfPersonModel key = new PdfPersonModel();
			key.setCodAgente(new StringType(codAgente.toString()));
			dao.executeQueryAccess("loadNdgAgente",key);
			if(key.getNdg().isNull()){
				agente = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
				agente.setCodAgente(new StringType(codAgente.toString()));
			}else{
				agente = loadPerson(csc, dao, pdf, key.getNdg().toString(), codAgente.toString(), true, true, false, ALL_CUSTOMERS);
				if(!pdf.getIsSede().booleanValue() && agente.isNotFound())
					throw new Exception("Il Family Banker Impersonato con codice ["+codAgente+"] non è stato trovato in anagrafica");
			}
			dao.executeQueryAccess("loadDatiAgente",agente);
			agente.setAgente(true);
			return agente;
			
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}
	}
	
	/*****************************************************************************************************/
	private static final String CAMPI_CLIENTE_PRECARICATI_EDITABILI_SE_VUOTI_DEFAULT=	
			"dataEmissioneDocumento,"+
			"luogoEmissioneDocumento,"+
			"provinciaEmissioneDocumento,"+
			"dataScadenzaDocumento,"+

			"prefissoTelefonoAbitazione,"+
			"numeroTelefonoAbitazione,"+
			"telefonoAbitazione,"+

			"prefissoTelefonoCellulare,"+
			"numeroTelefonoCellulare,"+
			"telefonoCellulare";
	/*****************************************************************************************************/
	public static void loadCustomerPersons(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf, StringType codAgente, boolean onInit) throws Exception{
		
		try{
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			
			if(pdf.getPreloadedPersonFieldsEditableIfNull() == null)
				pdf.setPreloadedPersonFieldsEditableIfNull(PdfConfig.getParamAsStringArray(csc, "DATALOADER", 
																								"CAMPI_CLIENTE_PRECARICATI_EDITABILI_SE_VUOTI", 
																								CAMPI_CLIENTE_PRECARICATI_EDITABILI_SE_VUOTI_DEFAULT, true));
			pdfData.getClienti().clear();
			
			for(int i=1;i<=MAX_NUM_CLIENTI;i++){
				
				AbstractType codCliente = pdfData.readProperty(PdfPredefinedFields.INPUT_COD_CLIENTE_N+i);
				if(codCliente == null || codCliente.isNull()){
					codCliente = pdfData.readProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+i);
					if(codCliente == null || codCliente.isNull()){
						codCliente = pdfData.readProperty(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+i);
						if(codCliente == null || codCliente.isNull()){
							PdfPersonModel nonCliente = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
							nonCliente.fillPdfDataFromEmptyCliente(pdf, pdfData, i, onInit);
							pdfData.getClienti().add(nonCliente);
							continue;
						}
					}
				}
				
				PdfPersonModel cliente = loadPerson(csc, dao, pdf, codCliente.toString(), codAgente.toString(), false, true, onInit, i);
				if(pdf.isInValidazioneMOM() || pdf.isInDoppiaSpuntaMOM()){
					cliente.fillPdfDataFromCliente(pdf, pdfData, i, false); // Non pulisco mai i dati del soggetto già presenti nel pdf
				}else{
					verificaPersonaNonPiuGestita(csc, pdf, cliente, i);
					cliente.fillPdfDataFromCliente(pdf, pdfData, i, onInit);
				}
				pdfData.getClienti().add(cliente);
			}
			
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static boolean verifyPerson(ClientSessionContext csc, PdfModel pdf, 
									   String codCliente, String codAgente, int cliIdx) throws Exception{
		try{
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			PdfPersonModel sogg = loadPerson(csc, dao, pdf, codCliente, pdf.getPdfData().getAgente().getCodAgente().toString(), false, false, false, cliIdx);
			verificaPersonaNonPiuGestita(csc, pdf, sogg, cliIdx);
			if(sogg.getNdg().hasTypeErrors()){
				try{ pdf.getPdfData().readProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+cliIdx).setTypeErrors(sogg.getNdg().getTypeErrors()); }catch(Exception e){}
			}
			if(sogg.isNotFound() || sogg.getNdg().hasTypeErrors())
				return false;
			return true;
			
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static PdfPersonModel loadPersonOnAutocomplete(ClientSessionContext csc,  String codCliente, String codAgente) throws Exception, DAOException{	
		return loadPersonOnAutocomplete(csc, codCliente, codAgente, false);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static PdfPersonModel loadPersonOnAutocomplete(ClientSessionContext csc, String codCliente, String codAgente, boolean escludiVariazioni) throws Exception, DAOException{
		PdfModel pdf = new PdfModel();
		if(escludiVariazioni)
			pdf.getPdfData().setExternalEntityName(new StringType(CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME));
		pdf.setEmptyAnagraficaSoggetto(new PdfPersonModel());
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
		return loadPerson(csc, dao, pdf, codCliente, codAgente, false, true, false, FB_CUSTOMERS);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static PdfPersonModel loadPersonOnDriver(ClientSessionContext csc, String codCliente, String codAgente) throws Exception, DAOException{
		return loadPersonOnDriver(csc, new PdfModel(), codCliente, codAgente);
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static PdfPersonModel loadPersonOnDriver(ClientSessionContext csc, PdfModel pdf, String codCliente, String codAgente) throws Exception, DAOException{
		pdf.setEmptyAnagraficaSoggetto(new PdfPersonModel());
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
		PdfPersonModel result = loadPerson(csc, dao, pdf, codCliente, codAgente, false, true, false, ALL_CUSTOMERS);
		if(result.isNotFound())
			return null;
		return result;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static PdfPersonModel loadAnyPersonOnDriver(ClientSessionContext csc, PdfModel pdf, String codCliente) throws Exception, DAOException{
		pdf.setEmptyAnagraficaSoggetto(new PdfPersonModel());
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
		// L'agente impersonato, se valorizzato, non deve essere considerato. Lo salvo....
		StringType savCodAgeImpersonato = pdf.getPdfData().getCodAgeImpersonato();
		pdf.getPdfData().setCodAgeImpersonato(new StringType());		
		PdfPersonModel result = loadPerson(csc, dao, pdf, codCliente, "", false, true, false, ALL_CUSTOMERS);
		// ...e lo ripristino
		pdf.getPdfData().setCodAgeImpersonato(savCodAgeImpersonato);
		if(result.isNotFound())
			return null;
		return result;
	}
	
	/**
	 * @deprecated
	 */
	@Deprecated
	public static PdfPersonModel loadPersonOnDriverForMom(ClientSessionContext csc, String codCliente, String codAgente) throws Exception, DAOException{
		return loadPersonOnDriverForMom(csc, new PdfModel(), codCliente, codAgente);
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static PdfPersonModel loadPersonOnDriverForMom(ClientSessionContext csc, PdfModel pdf, String codCliente, String codAgente) throws Exception, DAOException{
		pdf.setEmptyAnagraficaSoggetto(new PdfPersonModel());
		pdf.getPdfData().setOperatoreMOM(true);
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
		PdfPersonModel result = loadPerson(csc, dao, pdf, codCliente, codAgente, false, true, false, ALL_CUSTOMERS);
		if(result.isNotFound())
			return null;
		return result;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static PdfPersonModel loadPerson(ClientSessionContext csc, DAOObject dao, PdfModel pdf,
											String codCliente, String codAgente, boolean isAgente, 
											boolean loadDatiAggiuntivi, boolean onInit, int cliIdx) throws Exception, DAOException{

		PdfPersonModel sogg = pdf.getPersonsCache().get(Tools.fillSx(codCliente,'0',20)+isAgente);
		if(sogg != null)
			return (PdfPersonModel)Tools.cloneObject(sogg);
		
		sogg = new PdfPersonModel();
		
		if(codAgente.length() > 0)
			sogg.setCodAgente(new StringType(Tools.fillSx(codAgente,'0',10)));
		if(!pdf.getPdfData().getCodAgeImpersonato().isNull() && !isAgente)
			sogg.setCodAgente(new StringType(Tools.fillSx(pdf.getPdfData().getCodAgeImpersonato().toString(),'0',10)));
		
		if(codCliente.charAt(0) >= '0' && codCliente.charAt(0) <= '9'){	// Cliente effettivo -> ndg
			sogg.setNdg(new StringType(Tools.fillSx(codCliente, '0', 11)));
			loadSoggettoEffettivo(dao, pdf, sogg);
		}else{
			sogg.setIdCensimento(new StringType(codCliente));
			loadSoggettoCensito(dao,sogg,pdf,onInit);
		}
		
		if(sogg.getCodPotenziale().isNull() && sogg.getIdCensimento().isNull()){
			sogg = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
			sogg.setNdg(new StringType(Tools.fillSx(codCliente, '0', 11)));
			sogg.getNdg().addTypeError("Non esiste un cliente con questo codice");
			sogg.setNotFound(true);
			if(onInit && pdf.getErrorOnSomePerson() == PdfPersonModel.NO_ERROR)
				pdf.setErrorOnSomePerson(PdfPersonModel.CLI_NOT_EXIST);
		}else{
			StringType savCodAge = sogg.getCodAgente();
			DAOQueryResultModel qRes = null;
			if(sogg.getIsEffettivo().booleanValue()){
				boolean isCrossFBCustomer = isCrossFbCustomer(csc, pdf, cliIdx);
				if(isAgente || pdf.isOperatoreMOM() || isCrossFBCustomer)
					sogg.setCodAgente(new StringType());
				qRes = dao.executeQueryAccess("loadAnagraficaSoggettoEffettivo",sogg);
				if(qRes.getResult().size() == 0){
					// Non effettivo assegnato. Provo come cointestatario
					sogg.setCodAgente(savCodAge);
					qRes = dao.executeQueryAccess("loadAnagraficaSoggettoCointestatario",sogg);
					if(qRes.getResult().size() == 0){
						sogg = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
						sogg.setNotFound(true);
						sogg.setNdg(new StringType(Tools.fillSx(codCliente, '0', 11)));
						sogg.getNdg().addTypeError("Non esiste un cliente con questo codice");
						if(onInit && pdf.getErrorOnSomePerson() == PdfPersonModel.NO_ERROR)
							pdf.setErrorOnSomePerson(PdfPersonModel.CLI_NOT_EXIST);
					}
				}
				BooleanType isClienteAgente = null;
				if(sogg.readCodiceFiscale().length() > 0){
					isClienteAgente = (BooleanType)dao.executeQueryAccess("loadIsClienteAgenteWithCF", sogg).getSingleResult();
				}else if(!sogg.getNdg().isNull()){
					isClienteAgente = (BooleanType)dao.executeQueryAccess("loadIsClienteAgenteWithNDG", sogg).getSingleResult();
				}
				if(isClienteAgente != null)
					sogg.setClienteAgente(isClienteAgente.booleanValue());
			}else{
				// RFC #287440: nell'onboarding i clienti possono essere in bozza
				sogg.setAcceptDraftCustomer(new BooleanType(pdf.getPdfData().getExternalEntityAppl().equals("ONBOARDINGAZIENDE")));
				qRes = dao.executeQueryAccess("loadAnagraficaSoggettoCensito",sogg);
				if(qRes.getResult().size() == 0){
					BooleanType isAnagraficaSoggettoBozza = (BooleanType)dao.executeQueryAccess("isAnagraficaSoggettoBozza", sogg).getSingleResult();
					sogg = (PdfPersonModel)Tools.cloneObject(pdf.getEmptyAnagraficaSoggetto());
					sogg.setNotFound(true);
					sogg.setBozza(isAnagraficaSoggettoBozza.booleanValue());
					sogg.setNdg(new StringType(Tools.fillSx(codCliente, '0', 11)));
					if(sogg.isBozza()){
						sogg.getNdg().addTypeError("E' necessario completare il censimento anagrafico del cliente");
						if(onInit && pdf.getErrorOnSomePerson() == PdfPersonModel.NO_ERROR)
							pdf.setErrorOnSomePerson(PdfPersonModel.PROSPECT_IS_DRAFT);
					}else{
						sogg.getNdg().addTypeError("Non esiste un cliente con questo codice prospect");
						if(onInit && pdf.getErrorOnSomePerson() == PdfPersonModel.NO_ERROR)
							pdf.setErrorOnSomePerson(PdfPersonModel.PROSPECT_NOT_EXIST);
					}
				}
			}
			sogg.setCodAgente(savCodAge);
		}
		
		if(!sogg.isNotFound()){
			String sesso = sogg.propertyToString("sesso");
			String codiceFiscale = sogg.propertyToString("codiceFiscale");
			String partitaIva = sogg.propertyToString("partitaIva");
			if(sesso.equals("S")){
				sogg.addProperty("codiceFiscalePartitaIva", new StringType(partitaIva));
			}else{
				if(codiceFiscale.length() > 0)
					sogg.addProperty("codiceFiscalePartitaIva", new StringType(codiceFiscale));
				else
					sogg.addProperty("codiceFiscalePartitaIva", new StringType(partitaIva));
			}
			
			// Arricchimento
			if(loadDatiAggiuntivi){
				loadDatiAggiuntiviCliente(dao, sogg);
				StringType luogoEmissioneDocumento = (StringType)sogg.readProperty("luogoEmissioneDocumento");				
				if(luogoEmissioneDocumento != null && !luogoEmissioneDocumento.isNull()){
					MapCommandDataModel comuneModel = new MapCommandDataModel();
					comuneModel.addProperty("comune", luogoEmissioneDocumento);
					DAOQueryResultModel qRes = dao.executeQueryAccess("loadProvinciaFromComune", comuneModel);
					if(qRes.getResult().size() == 1){
						MapCommandDataModel provinciaModel = (MapCommandDataModel)qRes.getResult().get(0);
						sogg.addProperty("provinciaEmissioneDocumento", provinciaModel.readProperty("provincia"));
					}
				}
				pdf.getPersonsCache().put(Tools.fillSx(codCliente,'0',20)+isAgente, sogg);
			}
			
		}
		return sogg;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static boolean isCrossFbCustomer(ClientSessionContext csc, PdfModel pdf, int cliIdx) throws Exception{
		if(pdf.getPdfData().isOnlyPrint())
			return true;
		if(cliIdx == FB_CUSTOMERS)
			return false;
		if(cliIdx == ALL_CUSTOMERS)
			return true;
		// Il legale rappresentante, sempre secondo o terzo cliente, può essere di altri FB se la dispositiva prevede l'integrazione
		if(pdf.isGestioneLegaleRappresentanteAttiva() && (cliIdx == 2 || cliIdx == 3)){ 
			PdfPersonModel p = pdf.mainPdfData().getPerson(1);
			if(p != null && p.isPersonaGiuridica()) {
				if(cliIdx == 2 || LegaleRappresentanteManager.legaleRappresentanteFieldExist(pdf.getPdfData()))
					return true;
			}				
		}
		ProvideCrossFBCustomersDataResponse crossFBCustomersData = PdfDriverCaller.callProvideCrossFBCustomersData(csc, pdf.getPdfData());
		return crossFBCustomersData != null && crossFBCustomersData.getCrossFBCustomerIndexes().contains(""+cliIdx);
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static void loadDatiAggiuntiviCliente(DAOObject dao, PdfPersonModel sogg) throws DAOException{
		dao.executeQueryAccess("loadAnagraficaDatiAggiuntivi",sogg);
		dao.executeQueryAccess("loadAnagraficaDatiAggiuntiviResidenza",sogg);
		dao.executeQueryAccess("loadAnagraficaDatiAggiuntiviTelAbitazione",sogg);
		dao.executeQueryAccess("loadAnagraficaDatiAggiuntiviCellulare",sogg);
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static void verificaPersonaNonPiuGestita(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person, int cliIdx) throws Exception, DAOException{
		if(!csc.isRete() || pdf.getPdfData().isOnlyPrint() || csc.getCurrentLinkedUserCode().equals(person.getCodAgente().toString()) || person.isNotFound())
			return;
		
		// Se il campo ndg è nascosto assumiamo che la cogestione sia eventualmente gestita dal driver
		PdfFieldInfos fi = pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.CLIENTE_NDG_PREFIX+cliIdx);
		if(fi == null || fi.hidden)
			return;

		// Se il cliente non è di nessuno, ossia caricato con la loadAnyPersonOnDriver, oppure è crossFb non lo controlliamo
		if(person.getCodAgente().isNull() || isCrossFbCustomer(csc, pdf, cliIdx))
			return;
		
		PersonAutocompleteModel input = new PersonAutocompleteModel();
		input.setCodAgente(person.getCodAgente());
		input.setCodRuoloImpersonato(pdf.getPdfData().getCodRuoloImpersonato());
		input.setIdCensimento(person.getIdCensimento());
		input.setNdg(person.getNdg());
		input.setPersonIdx(new IntegerType(cliIdx));
		if(!input.getIncludiSoloCogestiti().booleanValue())
			return;
		
		ListType els = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("personAutocomplete",input).getResult();
		if(els.size() == 0)
			person.getNdg().addTypeError("Attenzione, non è possibile procedere al completamento della bozza di contratto: il soggetto non è presente nell'accordo di cogestione");
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static void loadSoggettoEffettivo(DAOObject dao, PdfModel pdf, PdfPersonModel sogg) throws Exception, DAOException{
		dao.executeQueryAccess("loadCodPotenzialeSoggettoEffettivo",sogg);
		if(sogg.getCodPotenziale().isNull())
			return;
		if(!sogg.getCodAgente().isNull() && !pdf.isInInserimentoMOM() && !pdf.getPdfData().getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME))
			dao.executeQueryAccess("loadProgressivoUltimaVariazioneSoggettoEffettivo",sogg);
		sogg.setNdg(new StringType(Tools.fillSx(sogg.getNdg().toString(),'0',11)));
		return;
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static void loadSoggettoCensito(DAOObject dao, PdfPersonModel sogg, PdfModel pdf, boolean onInit) throws Exception, DAOException{
		if(pdf.isInValidazioneMOM() && !sogg.getIdCensimento().isNull()){
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadChiaveNaturaleSoggettoCensito", sogg);
			if(qRes.getResult().size() > 0){
				dao.executeQueryAccess("loadNdgSoggettoCensito",sogg);
				if(!sogg.getNdg().isNull()){ // Il soggetto è presente come effettivo 
					sogg.setIdCensimento(new StringType());
					loadSoggettoEffettivo(dao, pdf, sogg);
					return;
				}
			}
		}
		sogg.setCodPotenziale(new StringType(sogg.getIdCensimento().toString())); // Il potenziale dei censiti è = al codice inforete
		return;
	}
}
