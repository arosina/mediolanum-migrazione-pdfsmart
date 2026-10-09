package prgm.pdfwebforms.sendprocess.backend;

import java.util.List;

import javax.ejb.EJBException;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.p.dac.service.Cliente;
import prgm.ita.p.dac.service.Contratto;
import prgm.ita.p.dac.service.DacServiceCaller;
import prgm.ita.p.dac.service.MezzoPagamento;
import prgm.pdfwebforms.backend.AbtractPdfProcessFacadeBean;
import prgm.pdfwebforms.catalog.PdfCatalogModel;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.PdfModuliAggiuntiviManager;
import prgm.pdfwebforms.drivers.io.prit.MezzoPagamentoRigaPrit;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.prit.RigaPrit;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.pritmom.PritMomInfo;
import prgm.pdfwebforms.pritmom.PritMomInfoLoader;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfSendProcessFacadeBean extends AbtractPdfProcessFacadeBean implements PdfSendProcessFacade {
  
	private static final String DAO_XML_NAME = "PdfWebForms.PdfSendProcess";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel sendPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws EJBException {

		if(pdf.isTestMode()){
			pdf.setPdfTestContent(pdfContent);
			return pdf;
		}
		
		try{
			
			PdfDataModel pdfData = pdf.getPdfData();
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));
			
        	// Inizializzo i codici operazione prit per le multioperazione
			if(!pdf.getCodiciOperazionePritPerMultioperazione().isNull()){
				String[] codici = pdf.getCodiciOperazionePritPerMultioperazione().toString().split("\\,");
				if(codici.length == pdf.getPdfAnags().size()){
					for(int i=0;i<pdf.getPdfAnags().size();i++){
						if(!codici[i].equals("asis"))
							pdf.getPdfAnags().get(i).setPdfCodOperazionePrit(new IntegerType(codici[i]));
					}
				}
			}
			pdf.setCodiciOperazionePritPerMultioperazione(new StringType());
			
			if(pdf.getPdfData().getIsVolatile().booleanValue())
				return pdf;

			super.freezePdfInstance(csc, pdf, pdfInstance, "sendPdfInstance", PdfInstanceModel.STATO_COMPLETATO, pdfContent);
			if(pdf.hasCommandErrors())
				return pdf;

			PdfPersonModel clientePdf = null;
			for(int i=0;i<pdf.getFullProcessPersons().size();i++){
				PdfPersonModel p = pdf.getFullProcessPersons().get(i);
				if(p.isEmty())
					continue;
				clientePdf = p;
				break;
			}
			
			// Inserimento PRIT (non per la sede)
			int numPritDaInserire = 0;
			if(!pdf.getIsSede().booleanValue()){
				
				ProvidePritDataResponse mainPritData = PdfDriverCaller.callProvidePritData(csc, pdf);
				if(mainPritData != null && mainPritData.isNonInserireAlcunaRigaDiPrit())
					return pdf;
				
	        	// Istanzio il cliente prit
				Cliente clientePrit = new Cliente();
				if(clientePdf != null){
					if(clientePdf.getNdg() != null)
						clientePrit.setCodMediolanum(clientePdf.getNdg().toString());
					clientePrit.setCognome(clientePdf.readProperty("cognome")==null?"":clientePdf.readProperty("cognome").toString());
					clientePrit.setNome(clientePdf.readProperty("nome")==null?"":clientePdf.readProperty("nome").toString());
				}
				
	        	// inserisco le righe di prit
				if(pdf.isTipoProcessoSedeMOM2())
					numPritDaInserire = inserisciPritMom2(csc, pdf, clientePrit);
				else
					numPritDaInserire = inserisciPritMom1(csc, mainPritData, pdf, clientePrit);
			}

			if(numPritDaInserire == 0 && !pdfInstance.getPdfInstanceId().isNull()){
				try{
					pdfInstance.setPdfCompilationMode(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE));
					pdfInstance.setPutOnSignedProcessBatchQueue(new StringType("N"));
					new DAOObject(csc,DAO_XML_NAME).executeTableUpdateAccess("updateSendDigitalPdfInstanceCompilationMode", pdfInstance);
				}catch(DAOException daoe){ /* do nothing */ }
			}
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private int inserisciPritMom1(ClientSessionContext csc, ProvidePritDataResponse pritData, PdfModel pdf, Cliente clientePrit) {
		int numPritDaInserire = 0;
		try{

			boolean inserimentoPritOk = true;
			int numPdf = 0;

			boolean includiRigheConfigurate = true;
			if(pritData != null)
				includiRigheConfigurate = pritData.isIncludiRigheConfigurate();
			
			for(PdfAnagModel pdfAnag : pdf.getPdfAnags()){
				numPdf++;
				if(includiRigheConfigurate){
					if( !pdfAnag.getPdfCodProdottoPrit().isNull() && 
						!pdfAnag.getPdfCodOperazionePrit().isNull() && 
						pdfAnag.getPdfCodOperazionePrit().intValue() != PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE){
						// Inserimento PRIT
						numPritDaInserire++;
						if(!inserisciRigaPritConfigurata(csc, pdf, pdfAnag, clientePrit, pritData, numPdf, false))
							inserimentoPritOk = false;
					}
				}
			}
			
			if(pritData != null){
				PdfAnagModel mainPdfAnag = pdf.mainPdfAnag(); 	// Nel vecchio modo, mom1, viene usato sempre mainAnag per
																// recuperare i codici prit dalla pdf_prit_config perchè il motore "parlava" solo con il driver principale
				for(RigaPrit rigaPrit : pritData.getRigheDiPrit()){
					// Inserimento PRIT
					numPritDaInserire++;
					if(!inserisciRigaPritDaDriver(csc, pdf, mainPdfAnag, clientePrit, rigaPrit))
						inserimentoPritOk = false;
				}
			}
			
			// Moduli aggiuntivi automatici, se il driver non derisce all'inserimento standard 
			if(!includiRigheConfigurate) {
				List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);
				for(PdfAnagModel pdfAnag : pdf.getPdfAnags()){
					for(String codiceModuloAML : elencoCodiciModuloAML) {
						if(!pdfAnag.getPdfMomCode().isNull() && pdfAnag.getPdfMomCode().equals(codiceModuloAML)) {
							if(!inserisciRigaPritConfigurata(csc, pdf, pdfAnag, clientePrit, null, numPdf, false))
								inserimentoPritOk = false;
						}
					}
				}
			}
			
			impostaMessaggioFinale(pdf, inserimentoPritOk, numPritDaInserire);

		}catch(Throwable t){
			pdf.setMessaggioFineOperazione("<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore di sistema nell'inserimento Prit. Aggiungere i documenti mancanti tramite l'applicazione Prit Promotore");
		}
		return numPritDaInserire;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private int inserisciPritMom2(ClientSessionContext csc, PdfModel pdf, Cliente clientePrit) {
		int numPritDaInserire = 0;
		try{
			
			boolean inserimentoPritOk = true;

			for(int i=0;i<pdf.getPdfAnags().size();i++){
				
				PdfAnagModel pdfAnag = pdf.getPdfAnags().get(i);
				if( pdfAnag.getPdfCodProdottoPrit().isNull() || 
					pdfAnag.getPdfCodOperazionePrit().isNull() || 
					pdfAnag.getPdfCodOperazionePrit().intValue() == PdfAnagFacadeBean.PRIT_MILTIOPERAZIONE_CODE)
					continue;
				
				// Inserimento PRIT
				numPritDaInserire++;
				PdfDataModel pdfData = (PdfDataModel)(pdf.isMultiPdf() ? pdf.getPdfData().getPdfs().get(i) : pdf.getPdfData());
				ProvidePritDataResponse pritData = PdfDriverCaller.callProvideMom2PritData(csc, pdf, pdfData);
				
				if(!inserisciRigaPritConfigurata(csc, pdf, pdfAnag, clientePrit, pritData, 1, true))
					inserimentoPritOk = false;
				
				if(pritData != null){
					for(RigaPrit rigaPrit : pritData.getRigheDiPrit()){
						// Inserimento PRIT
						numPritDaInserire++;
						if(!inserisciRigaPritDaDriver(csc, pdf, pdfAnag, clientePrit, rigaPrit))
							inserimentoPritOk = false;
					}
				}
			}
			
			impostaMessaggioFinale(pdf, inserimentoPritOk, numPritDaInserire);
			
		}catch(Throwable t){
			pdf.setMessaggioFineOperazione("<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore di sistema nell'inserimento Prit. Aggiungere i documenti mancanti tramite l'applicazione Prit Promotore");
		}
		return numPritDaInserire;
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean inserisciRigaPritConfigurata(ClientSessionContext csc, PdfModel pdf, 
												 PdfAnagModel pdfAnag, Cliente clientePrit, 
												 ProvidePritDataResponse pritData, int numPdf, boolean isMom2) {
		try{
			AbstractType numeroContrattoPdf = pdf.mainPdfData().read(PdfPredefinedFields.NUMERO_CONTRATTO);
			String codAgente = pdf.getMainCodAgente().toString();
			Contratto contrattoPrit = new Contratto();
			contrattoPrit.setCodInforete(pdf.getPdfData().getPdfInstanceId().toString());
			contrattoPrit.setCodProdotto(pdfAnag.getPdfCodProdottoPrit().intValue());
			contrattoPrit.setCodOperazione(pdfAnag.getPdfCodOperazionePrit().intValue());
			if(isMom2 && pritData != null) {
				PritMomInfo pritInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfAnag, pritData.getChiavePritRigaConfigurata());
				if(pritInfo != null){
					contrattoPrit.setCodProdotto(pritInfo.getCodProdotto());
					contrattoPrit.setCodOperazione(pritInfo.getCodOperazione());
				}
			}
			contrattoPrit.setBarcode(pdfAnag.getPritBarcode());
			if(numeroContrattoPdf != null && !numeroContrattoPdf.isNull())
				contrattoPrit.setNumeroContratto(numeroContrattoPdf.toString());
			MezzoPagamento[] mezziPgPrit = null;
			if(numPdf <= 1 && pritData != null && !pritData.getMezziDiPagamentoRigaConfigurata().isEmpty()){
				int count = 0;
				mezziPgPrit = new MezzoPagamento[pritData.getMezziDiPagamentoRigaConfigurata().size()];
				for(MezzoPagamentoRigaPrit mezzoPg : pritData.getMezziDiPagamentoRigaConfigurata()){
					MezzoPagamento mezzoPgPrit = mezzoPg.transformToDac();
					mezziPgPrit[count++] = mezzoPgPrit;
				}
			}
			DacServiceCaller.inserisciDocumento(csc, codAgente,"PDF_INSTANCE",contrattoPrit,clientePrit,mezziPgPrit);
			return true;
		}catch(Throwable tr){
			return false;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean inserisciRigaPritDaDriver(ClientSessionContext csc, PdfModel pdf, PdfAnagModel pdfAnag,
											  Cliente clientePrit, RigaPrit rigaPrit) {
		try{
			AbstractType numeroContrattoPdf = pdf.mainPdfData().read(PdfPredefinedFields.NUMERO_CONTRATTO);
			String codAgente = pdf.getMainCodAgente().toString();
			Contratto contrattoPrit = new Contratto();
			String numeroContratto = rigaPrit.getNumeroContratto();
			if(numeroContratto == null){
				if(numeroContrattoPdf != null && !numeroContrattoPdf.isNull())
					numeroContratto = numeroContrattoPdf.toString();
			}
			contrattoPrit.setBarcode(rigaPrit.getBarcode());
			contrattoPrit.setNumeroContratto(numeroContratto);
			contrattoPrit.setCodInforete(pdf.getPdfData().getPdfInstanceId().toString());
			PritMomInfo pritInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfAnag, rigaPrit.getChiavePrit());
			if(pritInfo != null){
				rigaPrit.setCodProdotto(pritInfo.getCodProdotto());
				rigaPrit.setCodOperazione(pritInfo.getCodOperazione());
			}
			contrattoPrit.setCodProdotto(rigaPrit.getCodProdotto());
			contrattoPrit.setCodOperazione(rigaPrit.getCodOperazione());
			MezzoPagamento[] mezziPgPrit = null;
			if(!rigaPrit.getMezziDiPagamento().isEmpty()){
				int count = 0;
				mezziPgPrit = new MezzoPagamento[rigaPrit.getMezziDiPagamento().size()];
				for(MezzoPagamentoRigaPrit mezzoPg : rigaPrit.getMezziDiPagamento()){
					MezzoPagamento mezzoPgPrit = mezzoPg.transformToDac();
					mezziPgPrit[count++] = mezzoPgPrit;
				}
			}
			DacServiceCaller.inserisciDocumento(csc, codAgente,"PDF_INSTANCE",contrattoPrit,clientePrit,mezziPgPrit);
			return true;
		}catch(Throwable tr){
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaMessaggioFinale(PdfModel pdf, boolean inserimentoPritOk, int numPritDaInserire) {
		
		String messaggioFinale = null;
		if(pdf.mainPdfAnag().getPdfArea().isNull() || pdf.mainPdfAnag().getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_MODULI) || pdf.mainPdfAnag().getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_OPERAZIONI)){
			
			if(numPritDaInserire > 0){ // Nessun prit da inserire

				if(inserimentoPritOk){ // Tutte le righe di prit correttamente inserite
					
					if(pdf.pdfIsInCartaChimica()){
						if(numPritDaInserire == 1)
							messaggioFinale = "E' stata creata la riga di prit ed i dati inseriti sono stati inviati in sede.<br>Per perfezionare la pratica ricordati far firmare il modulo al cliente e spedirlo in sede.";
						else
							messaggioFinale = "Sono state create le righe di prit ed i dati inseriti sono stati inviati in sede.<br>Per perfezionare la pratica ricordati far firmare i moduli al cliente e spedirli in sede.";
					}else{
						if(numPritDaInserire == 1)
							messaggioFinale = "E' stata creata la riga di prit ed i dati inseriti sono stati inviati in sede.<br>Per perfezionare la pratica ricordati di stampare il modulo, farlo firmare al cliente e spedirlo in sede.";
						else
							messaggioFinale = "Sono state create le righe di prit ed i dati inseriti sono stati inviati in sede.<br>Per perfezionare la pratica ricordati di stampare i moduli, farli firmare al cliente e spedirli in sede.";
					}
					
				}else{		 // Qualche riga di prit non inserita
					
					if(numPritDaInserire == 1)
						messaggioFinale = "<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore nell'inserimento Prit. Aggiungere il documento mancante tramite l'applicazione Prit Promotore";
					else
						messaggioFinale = "<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore nell'inserimento Prit. Aggiungere i documenti mancanti tramite l'applicazione Prit Promotore";
				
				}
			}
		
		}else{
			
			if(numPritDaInserire > 0){ // Nessun prit configurato

				if(inserimentoPritOk){ 	// Tutte le righe di prit correttamente inserite
					if(numPritDaInserire == 1)
						messaggioFinale = "E' stata creata la riga di prit ed i dati inseriti sono stati correttamente inviati in sede.";
					else
						messaggioFinale = "Sono state create le righe di prit ed i dati inseriti sono stati correttamente inviati in sede.";
				}else{					 // Qualche riga di prit non inserita
					if(numPritDaInserire == 1)
						messaggioFinale = "<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore nell'inserimento Prit. Aggiungere il documento mancante tramite l'applicazione Prit Promotore";
					else
						messaggioFinale = "<span style='color:red;'><b>Attenzione</b></span>: si &egrave verificato un errore nell'inserimento Prit. Aggiungere i documenti mancanti tramite l'applicazione Prit Promotore";
				}
				
			}
		}
		pdf.setMessaggioFineOperazione(messaggioFinale);		
	}
}
