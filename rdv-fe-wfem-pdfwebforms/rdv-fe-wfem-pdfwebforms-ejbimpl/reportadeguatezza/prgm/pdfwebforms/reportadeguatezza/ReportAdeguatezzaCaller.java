package prgm.pdfwebforms.reportadeguatezza;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.DispositivaReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProdottoReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.SoggettoReportAdeguatezzaModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ReportAdeguatezzaCaller {

	private static String DAO_RDA_XML = "PdfWebForms.PdfReportAdeguatezza";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callGeneraReportAdeguatezzaOnSend(ClientSessionContext csc, PdfModel pdf) throws Exception{
		return callGeneraReportAdeguatezza(csc, pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callGeneraReportAdeguatezzaOnSign(ClientSessionContext csc, PdfModel pdf) throws Exception{
		return callGeneraReportAdeguatezza(csc, pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callGeneraReportAdeguatezzaOnCopernico(ClientSessionContext csc, PdfModel pdf) throws Exception{
		return callGeneraReportAdeguatezza(csc, pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String callGeneraReportAdeguatezza(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		if( pdf.reportAdeguatezzaPassatoDalChiamate() 	|| 
			pdf.isTestMode()			 				|| 
			pdf.getIsSede().booleanValue() 				|| 
			pdf.getPdfData().getIsVolatile().booleanValue())
			return null;
				
		pdf.setReportAdeguatezzaCallModel(null);

		ProvideReportAdeguatezzaDataResponse reportData = PdfDriverCaller.callProvideReportAdeguatezzaData(csc, pdf);
		if(reportData == null)
			return null;

		try{
			
			// se non viene caricata la listaDispositive aggiungo la dispositiva nella lista
			if (reportData.getListaDispositive().size() == 0) {
				reportData.getListaDispositive().add(reportData.getDispositiva());
			}	
			
			ReportAdeguatezzaCallModel callModel = new ReportAdeguatezzaCallModel();
			callModel.setCodiceAgente(pdf.getMainCodAgente());
			
			// Gestione manleva mifid (rfc #260880)
			impostaManlevaMifid(pdf, callModel);
	
			pdf.setReportAdeguatezzaCallModel(callModel);
			callModel.setInput(reportData);
	
			// Leggo i dati della eventuale agvolazione per impostare il flag che indica i PAC sul primo versamento
			// Se l'agevolazione esiste il flag viene poi messo su tutti gli ordini
			StringType derogaVersamentoIniziale = findDerogaVersamentoIniziale(csc, pdf);
				
			ListType ordini = new ListType(OrdineModel.class);
			int idxPadre = 1;
			for(int i = 0; i < reportData.getListaDispositive().size(); i++) {			
				DispositivaReportAdeguatezzaModel dispositivaModel = (DispositivaReportAdeguatezzaModel) reportData.getListaDispositive().get(i);
				
				String erroriDispositiva = preparaReportAdeguatezzaDispositiva(csc, pdf, reportData, dispositivaModel, callModel);
				if (erroriDispositiva != null && erroriDispositiva.length() > 0) {
					return erroriDispositiva;
				}
				
				// Se non vi sono soggetti non creo il report 
				if(dispositivaModel.getSoggetti().size() == 0){					
					return null;
				}
				
				// Prendo alcuni dati generali dalla prima disposizione 
				if (i == 0) {
					SoggettoReportAdeguatezzaModel clientePrincipale = (SoggettoReportAdeguatezzaModel)dispositivaModel.getSoggetti().get(0);
					callModel.setCodiceCliente(clientePrincipale.getCodice());
				}
				
				ListType ordiniDispo = OrdineModel.fromModelToCall(dispositivaModel, idxPadre);
				for(int j = 0; j < ordiniDispo.size(); j++) {				
					OrdineModel d = (OrdineModel)ordiniDispo.get(j);
					d.setDerogaVersamentoIniziale(derogaVersamentoIniziale);
					ordini.add(d);
				}
				
				if (ordini.size() > 0) {
					OrdineModel ultimoOrdine = (OrdineModel) ordini.get(ordini.size() - 1); 
					idxPadre = Integer.valueOf(ultimoOrdine.getProgr().toString()) + 1;
				}
			}
			
			// Per IDD, imposto l'id della raccomandazione prendendo o quello passato in input o quello generato in precedenza
			String codRaccomandazioneIdd = "";
			if(pdf.getIdRaccomandazioneIdd().length() > 0)
				codRaccomandazioneIdd = pdf.getIdRaccomandazioneIdd();
			else if(pdf.getIddCallModel() != null)
				codRaccomandazioneIdd = pdf.getIddCallModel().getIdRaccomandazioneIdd().toString();
			
			callModel.setCodRaccomandazioneIdd(new StringType(codRaccomandazioneIdd));		
			
			// Per le sostituzioni imposto l'id e le preferenze
			callModel.setIdSostituzione(new StringType(pdf.getIdSostituzione()));
			if(pdf.getSostituzioniCallModel() != null)
				callModel.setElencoPreferenze(pdf.getSostituzioniCallModel().doUnionPreferenzeClientiSostituzioni());
			
			callModel.setOrdini(ordini);
			callModel.setUserId(new StringType(csc.getUserCode()));
		
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_RDA_XML).executeOSBAccess("generaReportAdeguatezza", callModel);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>(Servizio temporaneamente sospeso)</i></span>";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return Costanti.MESSAGGIO_ERRORE;
			}else if(callModel.getIdReportAdeguatezza().isNull()){
				pdf.setXmlSrvSend(wsRes.getWsCallData().getXmlSend());
				pdf.setXmlSrvReceived(wsRes.getWsCallData().getXmlReceive());
				return Costanti.MESSAGGIO_ERRORE; 
			}else{
				if(reportData.getCalcolaSost().booleanValue())
					initDerogaSostituzione(callModel, pdf);
				pdf.setReportAdeguatezzaCallModel(callModel);
				return null;
			}	
		}catch(DAOException daoe){
			return Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>(Errore di sistema)</i></span>";
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void impostaManlevaMifid(PdfModel pdf, ReportAdeguatezzaCallModel callModel) {
		if(pdf.getMifidCallModel() != null) {
			callModel.setIdAdeguatezzaRDA(pdf.getMifidCallModel().getIdEsito());
			callModel.setFlagManlevaKOESG(new StringType("N"));
			if(pdf.getMifidManlevaData() != null && pdf.getMifidManlevaData().getFlagManlevaKOESG().equals("S")) {
				callModel.setIdAdeguatezzaRDA(pdf.getMifidManlevaData().getIdAdeguatezzaPadre());
				callModel.setFlagManlevaKOESG(new StringType("S"));
			}
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void initDerogaSostituzione(ReportAdeguatezzaCallModel callModel, PdfModel pdf){		
		try{
			ListType ordiniResult = callModel.getOrdiniResult();
			for(int i=0;i<ordiniResult.size();i++){
				OrdineResultModel or = (OrdineResultModel)ordiniResult.get(i);
				if(!or.getDerogaResult().isNull()){

					pdf.getPdfData().backupAgevolazione();
					
					pdf.getPdfData().addProperty(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType(PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO));
					pdf.getPdfData().addProperty(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType());
					pdf.getPdfData().addProperty(PdfPredefinedFields.CODICE_AGEVOLAZIONE, or.getDerogaResult());
					pdf.getPdfData().addProperty(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType(AgevolazioneModel.DICITURA_DEROGA_SOSTITUZIONE));
					pdf.getPdfData().addProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, new StringType(AgevolazioneModel.PERCENTUALE_DEROGA_SOSTITUZIONE));
					pdf.getPdfData().addProperty(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, new StringType());
					pdf.getPdfData().addProperty(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, new StringType());
					pdf.getPdfData().addProperty(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, new DoubleType());
					
					if(pdf.isMultiPdf()){
						for(int j=0; j<pdf.getPdfData().getPdfs().size(); j++){
							PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(j);

							pdfElement.backupAgevolazione();
							
							pdfElement.addProperty(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType(PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO));
							pdfElement.addProperty(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType());
							pdfElement.addProperty(PdfPredefinedFields.CODICE_AGEVOLAZIONE, or.getDerogaResult());
							pdfElement.addProperty(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType(AgevolazioneModel.DICITURA_DEROGA_SOSTITUZIONE));
							pdfElement.addProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, new StringType(AgevolazioneModel.PERCENTUALE_DEROGA_SOSTITUZIONE));
							pdfElement.addProperty(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, new StringType());
							pdfElement.addProperty(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, new StringType());
							pdfElement.addProperty(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, new DoubleType());
						}					
					}
					break;
				}
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void clearDerogaSostituzione(PdfModel pdf){		
		try{
			pdf.getPdfData().restoreAgevolazione();			
			if(pdf.isMultiPdf()){
				for(int j=0; j<pdf.getPdfData().getPdfs().size(); j++){
					PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(j);
					pdfElement.restoreAgevolazione();
				}					
			}
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callRecuperaReportAdeguatezza(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		if( pdf.reportAdeguatezzaPassatoDalChiamate()	||
			pdf.isTestMode() 							|| 
			pdf.getIdReportAdeguatezza().length() == 0 	|| 
			pdf.getIsSede().booleanValue())
			return null;
		
		pdf.setRecuperaReportAdeguatezzaCallModel(null);
		
		RecuperaReportAdeguatezzaCallModel callModel = new RecuperaReportAdeguatezzaCallModel();
		callModel.setIdReportAdeguatezza(new StringType(pdf.getIdReportAdeguatezza()));
		callModel.setCodiceAgente(pdf.getMainCodAgente());
		callModel.setUserId(new StringType(csc.getUserCode()));
		
		try{
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_RDA_XML).executeOSBAccess("recuperaReportAdeguatezza", callModel);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di recupero del report adeguatezza disabilitato.";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio di recupero del report adeguatezza: "+wsRes.getWsCallData().getMessage();
			}else if(callModel.getStatoReportAdeguatezza().equals("ERROR")){
				return "Errore nella generazione del Report di Adeguatezza";
			}else{
				pdf.setRecuperaReportAdeguatezzaCallModel(callModel);
				return null;
			}
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio di recupero del report adeguatezza. ID report ["+callModel.getIdReportAdeguatezza()+"]";
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public static String callScriviLegameDispositivaOrdine(ClientSessionContext csc, PdfModel pdf, PdfInstanceModel pdfInstance) throws Exception{	
			
		if( pdf.isTestMode() 							|| 
			pdf.getIdReportAdeguatezza().length() == 0 	|| 
			pdf.getIsSede().booleanValue() 				|| 
			pdf.getPdfData().getIsVolatile().booleanValue())
			return null;
		
		ScriviLegameDispositivaOrdineCallModel callModel = new ScriviLegameDispositivaOrdineCallModel();
		callModel.setCodOperzDispOrig(pdfInstance.getPdfInstanceId());
		if(pdfInstance.getPdfStatus().equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE))
			callModel.setTipoSistemaOrig(new StringType("PC"));
		else
			callModel.setTipoSistemaOrig(new StringType("CE"));
		callModel.setIdReport(new StringType(pdf.getIdReportAdeguatezza()));
		callModel.setOrdini(pdf.recuperaOrdiniRda());	

		callModel.setUserId(new StringType(csc.getUserCode()));
		
		try{
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_RDA_XML).executeOSBAccess("scriviLegameDispositivaOrdine", callModel);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di scrittura legame report adeguatezza/dispositiva disabilitato.";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio di scrittura legame report adeguatezza/dispositiva: "+wsRes.getWsCallData().getMessage();
			}else if(!callModel.getIdReportOut().equals(pdf.getIdReportAdeguatezza())){
				pdf.setXmlSrvSend(wsRes.getWsCallData().getXmlSend());
				pdf.setXmlSrvReceived(wsRes.getWsCallData().getXmlReceive());
				return "Errore dal servizio di scrittura legame report adeguatezza/dispositiva: "+wsRes.getWsCallData().getMessage();
			}else{
				return null;
			}
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio di scrittura legame report adeguatezza/dispositiva.";
		}
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String preparaReportAdeguatezzaDispositiva(ClientSessionContext csc, PdfModel pdf,
			ProvideReportAdeguatezzaDataResponse reportData, DispositivaReportAdeguatezzaModel dispositivaModel, ReportAdeguatezzaCallModel callModel) throws Exception {
		
		// Se il driver non compila i soggetti li compilo con quelli della squadra
		StringBuilder elencoClienti = new StringBuilder();
		if(dispositivaModel.getSoggetti().size() == 0){
			
			ArrayList<PdfPersonModel> clienti = PdfInstanceModel.clientiInstanceFromData(pdf);
			if(clienti.size() == 0){
				return  Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>(Non risultano clienti)</span>";
			}
			
			for(int i=0;i<clienti.size();i++){
				PdfPersonModel cliente = clienti.get(i);
				elencoClienti.append("-ndg:"+cliente.getNdg()+" id:"+cliente.getIdCensimento()+"-");
				SoggettoReportAdeguatezzaModel soggetto = new SoggettoReportAdeguatezzaModel();
				if(cliente.getIsEffettivo().booleanValue()){
					soggetto.setCodice(new StringType(cliente.getNdg().toString()));
					dispositivaModel.getSoggetti().add(soggetto);
				}else if(!cliente.getIdCensimento().isNull()){
					soggetto.setCodice(new StringType(cliente.getIdCensimento().toString()));
					dispositivaModel.getSoggetti().add(soggetto);
				}
			}
		}else{
			for(int i=0;i<dispositivaModel.getSoggetti().size(); i++){
				SoggettoReportAdeguatezzaModel soggetto = (SoggettoReportAdeguatezzaModel)dispositivaModel.getSoggetti().get(i);
				elencoClienti.append("-codice:"+soggetto.getCodice()+"-");
			}
		}
		
		if(elencoClienti.length() > 0) {
			elencoClienti.insert(0, " (");
			elencoClienti.append(")");
		}
				
		// Se non vi sono soggetti non creo il report 
		if(dispositivaModel.getSoggetti().size() == 0){
			callModel.setNoRdaReason(new StringType("Srv RDA non richiamato: non risultano soggetti in dispositiva" + elencoClienti.toString()));
			return null;
		}				
				
		for(int i=1;i<dispositivaModel.getSoggetti().size(); i++){
			SoggettoReportAdeguatezzaModel soggetto = (SoggettoReportAdeguatezzaModel)dispositivaModel.getSoggetti().get(i);
			soggetto.setRuolo(new StringType(SoggettoReportAdeguatezzaModel.Ruoli.COINSTESTATARIO));		
		}

		DAOObject dao = new DAOObject(csc, DAO_RDA_XML);
		try{
			// Transcodifico i codici prodotto da codice fondo/comparto etc. in codice chiave per il servizio
			if(!dispositivaModel.getProdotto().isNull() && dispositivaModel.isDoProdottoDefaultTranslation()){
				StringType codiceChiaveServizio = (StringType)dao.executeQueryAccess("recuperaAnagraficaProdottoServizio", dispositivaModel).getSingleResult();
				if(codiceChiaveServizio == null || codiceChiaveServizio.isNull()){
					return Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>("+dispositivaModel.getProdotto()+" non disponibile)</span>"; 
				}
				dispositivaModel.setProdotto(codiceChiaveServizio);
			}
			for(int i=0;i<dispositivaModel.getProdotti().size();i++){
				ProdottoReportAdeguatezzaModel p = (ProdottoReportAdeguatezzaModel)dispositivaModel.getProdotti().get(i);
				if(!p.getProdotto().isNull() && p.isDoProdottoDefaultTranslation()){
					StringType codiceChiaveServizio = (StringType)dao.executeQueryAccess("recuperaAnagraficaProdottoServizio", p).getSingleResult();
					if(codiceChiaveServizio == null || codiceChiaveServizio.isNull()){
						return Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>("+p.getProdotto()+" non disponibile)</i></span>"; 
					}
					p.setProdotto(codiceChiaveServizio);
				}
			}

			// Transcodifico la deroga
			if(dispositivaModel.isDoDerogaDefaultTranslation()){
				StringType idAgevolazione = findIdAgevolazione(pdf); 
				if(idAgevolazione != null && !idAgevolazione.isNull()){
					StringType percentualeAgevolazione = null; 
					if(idAgevolazione.toString().startsWith("S")){
						percentualeAgevolazione = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																		"select PCT_AGEV_RICHIESTA from CEPE_IST_NUM_ROSSO where ID_ISTANZA = '"+idAgevolazione+"'", 
																		null, StringType.class).getSingleResult();
					}else{
						percentualeAgevolazione = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																		"select PCT_ESENZ from CEPE_CRLEVE_MOV_DEROG_GEST where ID_DEROGA = "+idAgevolazione, 
																		null, StringType.class).getSingleResult();
					}
					if(percentualeAgevolazione != null && !percentualeAgevolazione.isNull()){
						String sperc = percentualeAgevolazione.toString();
						sperc = sperc.replaceAll("\\%", "");
						sperc = Tools.fillSx(sperc, '0', 4);
						dispositivaModel.setDeroga(new StringType(sperc));
					}
				}
			}
			
			return null;
			
		}catch(DAOException daoe){
			return Costanti.MESSAGGIO_ERRORE+"<br><span style='font-size:9;'><i>(Errore di sistema)</i></span>";
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static StringType findDerogaVersamentoIniziale(ClientSessionContext csc, PdfModel pdf) throws DAOException {
		// Leggo i dati della eventuale agvolazione per impostare il flag che indica i PAC sul primo versamento
		// Se l'agevolazione esiste il flag viene poi messo su tutti gli ordini
		StringType derogaVersamentoIniziale = new StringType();
		StringType idAgevolazione = findIdAgevolazione(pdf);			
		if(idAgevolazione != null && !idAgevolazione.isNull()) {
			BooleanType existAgevolazione = (BooleanType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
												"select case when count(*) > 0 then 'S' else 'N' end from CEPE_CRLEVE_MOV_DEROG_GEST where ID_DEROGA = "+idAgevolazione+" "+
												"and upper(MODAL_VERS) = 'PAC' "+
												"and upper(APPL_DEROGA) = 'PRIMO VERSAMENTO'", 
												null, BooleanType.class).getSingleResult();
			if(existAgevolazione != null && existAgevolazione.booleanValue())
				derogaVersamentoIniziale = new StringType("S");
		}
		return derogaVersamentoIniziale;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static StringType findIdAgevolazione(PdfModel pdf) {
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdf.isMultiPdf()){
			pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(0);
			if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfData().getPdfs().size() > 1)
				pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
		}
		return (StringType)pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE);
	}

}
