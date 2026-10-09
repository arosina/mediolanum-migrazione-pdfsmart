package prgm.pdfwebforms.signprocess.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.pdf.PdfTools;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.copernicoprocess.accettazione.PdfCopernicoAttachments;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.legalerappresentante.LegaleRappresentanteManager;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.model.PdfPersonSignDataModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SignUtility {
	
	private static String DAO_XML = "PdfWebForms.PdfSignProcess";
	
	public static String CELLULARE_KO_SERVIZIO				= "K";
	public static String CELLULARE_OK 						= "C";

	public static final String 	FIRMA_DIGITALE_ATTIVA	 	= "A";
	public static final String 	FIRMA_DIGITALE_DISATTIVA	= "D";
	public static final String 	FIRMA_DIGITALE_SOSPESA	 	= "S";
	
	private static String ERRORE_GENERICO_COPERNICO_NMOL = "Il servizio di firma digitale non è al momento disponibile";

	private static final String S_IL_CLIENTE = "Il cliente ";
	private static final String S_XML_FIRMATO = "xmlFirmato";
	private static final String S_SRV_FIRMA_PREFIX = "Il servizio di firma digitale non può essere utilizzato da ";

	/***********************************************************************************************/
	/***********************************************************************************************/
	private SignUtility() {}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean mostraOtp(PdfModel pdf){
		if(!pdf.isInBasket() && !pdf.isInAccettazioneCopernico() && pdf.getPdfData().getIsSwitch().booleanValue())
			return true;
		int nextPdf = pdf.getPdfData().getPdfIndex().intValue()+1;
		for(int i=nextPdf;i<pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos().length;i++){
			PdfPersonSignProcessInfo pi = pdf.getPersonaCorrente().getPdfPersonsSignProcessInfos()[i];
			if(pi.isHasSignOnPdf())
				return false;
		}
		return Basket.isLastDispoDaFirmarePersonaCorrente(pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadDatiFirmaDigitalePersone(ClientSessionContext csc, PdfModel pdf){
		
		if(pdf.isTestMode())
			return;
		
		int count = 0;
		boolean inAccettazioneCopernico = pdf.getPdfData().isProcessoAccettazioneCopernico();
		
		for(PdfPersonModel persona : pdf.getFullProcessPersons()){
			persona.resetCommandErrors();
			
			if(persona.isAgente()){
				
				String error = loadDatiFirmaDigitalePersona(csc,persona,false,-1);
				if(error != null)
					pdf.addCommandError(error);
				
			}else{
				
				if(!persona.getIsEffettivo().booleanValue()){
					boolean isProspectEnabled = true;
					try{
						for(int i=0;i<persona.getPdfPersonsSignProcessInfos().length;i++){
							PdfPersonSignProcessInfo psi = persona.getPdfPersonsSignProcessInfos()[i];
							if(!psi.isHasSignOnPdf())
								continue;
							PdfAnagModel pdfAnag = pdf.getPdfAnags().get(i); 
							for(int j=0;j<psi.getIndexInFieldName().size();j++){
								int ifn = psi.getIndexInFieldName().get(j);
								if(ifn > 0 && ifn <= 9) {
									BooleanType isProspectEnabledOnCli = (BooleanType)Tools.getPropertyValue(pdfAnag, "isProspectEnabledOnCli"+ifn);
									if(!isProspectEnabledOnCli.booleanValue()){
										isProspectEnabled = false;
										break;
									}
								}
							}
							if(!isProspectEnabled)
								break;
						}
					}catch(Throwable t){
						isProspectEnabled = false;
					}
					if(!isProspectEnabled) {
						pdf.addCommandError(""+persona.readCognomeNome()+" e' un cliente prospect.");
					}else {
						String error = loadDatiFirmaDigitalePersona(csc,persona,inAccettazioneCopernico,count);
						if(error != null)
							pdf.addCommandError(error);
					}
				}else{
				
					String error = loadDatiFirmaDigitalePersona(csc,persona,inAccettazioneCopernico,count);
					if(error != null)
						pdf.addCommandError(error);
					
				}
				count++;
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String loadDatiFirmaDigitalePersona(ClientSessionContext csc, PdfPersonModel persona, boolean inAccettazioneCopernico, int count){

		String error = null;
		
		try{
			
			if(persona.readCodiceFiscale().length() == 0){
				if(persona.isAgente()){
					String n = persona.readCognomeNome();
					if(n.length() == 0)
						n = "con codice "+persona.getCodAgente().toString();
					error = "Per il Family Banker "+n+" non risulta valorizzato il codice fiscale";
				}else{
					if(inAccettazioneCopernico)
						error = ERRORE_GENERICO_COPERNICO_NMOL;
					else
						error = "Per "+persona.readCognomeNome()+" non risulta valorizzato il codice fiscale";
				}
				return error;
			}
			
			DAOObject dao = new DAOObject(csc,DAO_XML);			
			PdfPersonSignDataModel signData = persona.getSignData();
			
			// Cellulare
			signData.setStatoCelluarePrimario(new StringType());
			signData.setNumeroCelluarePrimario(new StringType());
			signData.setPrefissoCellulareAnag(new StringType());
			signData.setNumeroCellulareAnag(new StringType());
			if(persona.getIsEffettivo().booleanValue()){
				DAOQASResultModel qasRes = dao.executeQASAccess("loadStatoCellulare",persona);
				if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
					signData.setStatoCelluarePrimario(new StringType(CELLULARE_KO_SERVIZIO));
					signData.setNumeroCelluarePrimario(new StringType("NA"));
					if(inAccettazioneCopernico)
						error = ERRORE_GENERICO_COPERNICO_NMOL;
					else
						error = "Errore di sistema nel recuperare la coerenza del cellulare di "+persona.readCognomeNome();
				}else{
					if(!signData.getStatoCelluarePrimario().equals(CELLULARE_OK)){
						signData.setNumeroCelluarePrimario(new StringType("NA"));
						if(inAccettazioneCopernico)
							error = ERRORE_GENERICO_COPERNICO_NMOL;
						else
							error = "Contattare Banking Center e modificare numero di cellulare di "+persona.readCognomeNome();
					}
				}
				if(error == null)
					dao.executeQueryAccess("loadCellulareAnagEffettivo", persona);

			}else{
				dao.executeQueryAccess("loadCellulareAnagCensito", persona);
			}
			
			if(persona.getSignData().getNumeroCelluarePrimario().isNull()){
				if(inAccettazioneCopernico)
					error = ERRORE_GENERICO_COPERNICO_NMOL;
				else
					error = "Per "+persona.readCognomeNome()+" non risulta valorizzato il numero di cellulare";
			}
			
			if(error != null)
				return error;
			
			// Stato firma
			DAOQASResultModel qasRes = dao.executeQASAccess("readStatoFirmaDigitale",persona);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				
				if(inAccettazioneCopernico)
					error = ERRORE_GENERICO_COPERNICO_NMOL;
				else
					error = "Errore di servizio nel recuperare lo stato della firma digitale per "+persona.readCognomeNome()+": "+qasRes.getQasCallData().getMessage();
				
			}else{
				
				if(!"OK".equals(signData.getEsitoChiamataServizio().toString())){
					
					if(inAccettazioneCopernico)
						error = ERRORE_GENERICO_COPERNICO_NMOL;
					else
						error = "KO dal servizio di verifica stato firma digitale per "+persona.readCognomeNome();
					
				}else if(FIRMA_DIGITALE_SOSPESA.equals(signData.getStatoFirmaDigitale().toString())){
					
					if(inAccettazioneCopernico){
						String c1 = Tools.fillSx(persona.getNdg().toString(),'0',11);
						String c2 = Tools.fillSx(csc.getUserCode(),'0',11);
						if(c1.equals(c2))
							error = "La firma digitale risulta sospesa, per riattivarla accedi alla tua <span onclick='sendToNmol(\"gotoAreaPersonale\");' style='cursor:pointer;text-decoration:underline;'>Area Personale</span>.";
						else
							error = S_IL_CLIENTE+persona.readNomeCognome()+" risulta avere la firma digitale sospesa, per riattivarla accedere alla propria area personale.";
					}else{
						error = ""+persona.readCognomeNome()+" ha sospeso la firma digitale";
					}
					
					
				}else if("B".equals(signData.getStatoFirmaDigitale().toString())){
					
					if(inAccettazioneCopernico){
						if(count == 0)
							error = "La firma digitale risulta bloccata, per sbloccarla contatta il Servizio Clienti.";
						else
							error = S_IL_CLIENTE+persona.readNomeCognome()+" risulta avere la firma digitale bloccata, per sbloccarla contattare il Servizio Clienti.";
					}else{
						error = "Per "+persona.readCognomeNome()+" la firma digitale è bloccata";
					}
					
				}else if(FIRMA_DIGITALE_DISATTIVA.equals(signData.getStatoFirmaDigitale().toString())){
					
					if(persona.isAgente()){
						// Controllo se l'agente è cliente di un altro agente e non di se stesso
						DAOQueryResultModel qRes = dao.executeQueryAccess("isAgenteClienteDiAltroAgente",persona);
						BooleanType isAgenteClienteDiAltroAgente = (BooleanType)qRes.getSingleResult();
						if(isAgenteClienteDiAltroAgente.booleanValue()){
							error = "Richiedere la firma digitale tramite il proprio Family Banker";
						}
					}
					
					if(error == null){
						if(inAccettazioneCopernico){
							String c1 = Tools.fillSx(persona.getNdg().toString(),'0',11);
							String c2 = Tools.fillSx(csc.getUserCode(),'0',11);
							if(c1.equals(c2))
								error = "La firma digitale risulta non attiva, per attivarla accedi alla tua <span onclick='sendToNmol(\"gotoAreaPersonale\");' style='cursor:pointer;text-decoration:underline;'>Area Personale</span>.";
							else
								error = S_IL_CLIENTE+persona.readNomeCognome()+" risulta avere la firma digitale non attiva, per attivarla accedere alla propria area personale.";
						}else{
							error = persona.readCognomeNome()+" deve richiedere l'emissione del certificato digitale";
						}
					}
					
				}else if(FIRMA_DIGITALE_ATTIVA.equals(signData.getStatoFirmaDigitale().toString())){
					
					if(signData.getIdCertificationAuthority().isNull()){
						if(inAccettazioneCopernico)
							error = ERRORE_GENERICO_COPERNICO_NMOL;
						else
							error = "Per "+persona.readCognomeNome()+" non risulta l'identificativo della CA";
					}
					
				}
			}
			if(error != null)
				return error;

			// Banca diretta, ossia il soggetto ha i codici
			return impostaBancaDiretta(csc, persona, inAccettazioneCopernico);
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return "Errore DAO nel recuperare le informazioni relative alla firma digitale per "+persona.readCognomeNome()+": "+daoe.toString();
		}
	}

	/***********************************************************************************************************/
	// RFC #284780: se il cliente non ha banca diretta non ha neanche i codici per cui non ha senso controllarli
	// Inoltre impostando il booleano gli permettiamo di utilizzare i codici temporanei come per i prospect
	/***********************************************************************************************************/
	private static String impostaBancaDiretta(ClientSessionContext csc, PdfPersonModel persona, boolean inAccettazioneCopernico) throws DAOException {
		String error = null;
		persona.setHasBancaDiretta(true);
		if(persona.getIsEffettivo().booleanValue()){

			if(!hasSoggettoContiBancaDiretta(csc, persona.getNdg())){
				persona.setHasBancaDiretta(false);
				error = verificaEsistenzaBozzeMyfreedom(csc, persona, inAccettazioneCopernico);
			}else{
				error = verificaCodici(csc, persona.getNdg(), persona.readCognomeNome(), inAccettazioneCopernico);
			}
		
		}else{
			
			StringType ndgFintoProspect = getNdgFintoProspect(csc, persona.readCodiceFiscale());
			if(!ndgFintoProspect.isNull()){
				
				if(!hasSoggettoContiBancaDiretta(csc, ndgFintoProspect)){
					persona.setHasBancaDiretta(false);
					error = verificaEsistenzaBozzeMyfreedom(csc, persona, inAccettazioneCopernico);
				}else{
					// RFC #283062. Solo nel contesto piattaforma conto nessuna verifica codici per i finti prospect
					if(!persona.getPdfEnvironment().equals("PIATTAFORMA_CONTO")) {
						error = verificaCodici(csc, ndgFintoProspect, persona.readCognomeNome(), inAccettazioneCopernico);
					}
				}
				
			}else{
				error = verificaEsistenzaBozzeMyfreedom(csc, persona, inAccettazioneCopernico);
			}
			
		}
		return error;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static void getDigitDaChiedere(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){

		PdfPersonSignDataModel signData = person.getSignData();
		
		signData.setStepPinSuperato(false);
		signData.setDigit1Digitato(new StringType());
		signData.setDigit2Digitato(new StringType());
		
		if(pdf.isTestMode()){
			signData.setDigit1DaChiedere(new IntegerType(2));
			signData.setDigit2DaChiedere(new IntegerType(4));
			return;
		}
		
		try{
			// Richiedo le posizioni da richiedere nel pin
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML).executeQASAccess("digitToTest",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				pdf.addCommandError("Errore di servizio nel recuperare i digit da richiedere per "+person.readCognomeNome()+": "+qasRes.getQasCallData().getMessage());
				return;
			}
			
			// I numeri dei digit da testare non sono in ordine per cui li ordino qui
			// Sommo 1 per rendere congruenti i valori con il servizio di verifica pin 
			int dig1 = signData.getDigit1DaChiedere().intValue()+1;
			int dig2 = signData.getDigit2DaChiedere().intValue()+1;
			if(dig1 > dig2){ // Li inverto
				signData.setDigit1DaChiedere(new IntegerType(dig2));
				signData.setDigit2DaChiedere(new IntegerType(dig1));
			}else{
				signData.setDigit1DaChiedere(new IntegerType(dig1));
				signData.setDigit2DaChiedere(new IntegerType(dig2));
			}
			return;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel recuperare i digit da richiedere per "+person.readCognomeNome()+": "+daoe.toString());
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static void generaNuovoPinProvvisorio(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		
		PdfPersonSignDataModel signData = person.getSignData();	
		signData.setDigit1Digitato(new StringType());
		signData.setDigit2Digitato(new StringType());
		signData.getDigit1DaChiedere().resetTypeErrors();

		if(pdf.isTestMode())
			return;

		try{
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML).executeQASAccess("generaNuovoPinProvvisorio",person);
			if(qasRes.getQasCallData().getStatus() != XmlServiceCallData.STATUS_OK){
				signData.getDigit1DaChiedere().addTypeError("Errore di servizio nel generare il PIN provvisorio per "+person.readCognomeNome()+": "+qasRes.getQasCallData().getMessage());
			}else{
				if(!signData.getEsitoChiamataServizio().equals("OK") && !signData.getEsitoChiamataServizio().equals("-1")){
					signData.getDigit1DaChiedere().addTypeError("Errore dal servizio di generazione PIN provvisorio per "+person.readCognomeNome()+": "+signData.getEsitoChiamataServizio().toString()+" - "+signData.getMessaggioChiamataServizio());
				}
			}
		}catch(DAOException daoe){
			daoe.printStackTrace();
			signData.getDigit1DaChiedere().addTypeError("Errore DAO nel generare il PIN provvisorio per "+person.readCognomeNome()+": "+daoe.toString());
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static boolean verificaPin(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){

		PdfPersonSignDataModel signData = person.getSignData();
		signData.getDigit1DaChiedere().resetTypeErrors();

		if(pdf.isTestMode())
			return true;
		
		try{
			if(signData.getDigit1Digitato().isNull() || signData.getDigit2Digitato().isNull()){
				signData.getDigit1DaChiedere().addTypeError("Digitare le cifre del Codice Segreto");
				return false;
			}
			
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML).executeQASAccess("verificaPin",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				signData.getDigit1DaChiedere().addTypeError("Errore di servizio nel verificare il PIN: "+qasRes.getQasCallData().getMessage());
			}else{
				if(!"OK".equals(signData.getEsitoChiamataServizio().toString()))
					signData.getDigit1DaChiedere().addTypeError(signData.getMessaggioChiamataServizio().toString());
			}
			
			if(signData.getDigit1DaChiedere().hasTypeErrors())
				return false;
			return true;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel verificare il pin per "+person.readCognomeNome()+": "+daoe.toString());
			return true;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static void generaOTP(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		
		PdfPersonSignDataModel signData = person.getSignData();
		signData.setStepOtpSuperato(false);
		signData.setOtpDigitato(new StringType());
		signData.setSavOtpDigitato(new StringType());
		
		if(pdf.isTestMode()){
			if(signData.getOtpGenerato().isNull()){
				signData.setOtpGenerato(new StringType("00001"));
			}else{
				int nnn = Integer.parseInt(signData.getOtpGenerato().toString()) + 1;
				signData.setOtpGenerato(new StringType(Tools.fillSx(""+nnn, '0', 5)));
			}
			return;
		}
		
		try{
			// Richiedo il token
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML).executeQASAccess("generaOTP",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				pdf.addCommandError("Errore di servizio nel generare l'OTP per "+person.readCognomeNome()+": "+qasRes.getQasCallData().getMessage());
			}else{
				if(!"0".equals(signData.getEsitoChiamataServizio().toString()))
					pdf.addCommandError("KO dal servizio di generazione OTP per "+person.readCognomeNome()+": "+signData.getEsitoChiamataServizio().toString()+" - "+signData.getMessaggioChiamataServizio());
			}
			return;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel generare l'OTP per "+person.readCognomeNome()+": "+daoe.toString());
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static boolean verificaOtp(ClientSessionContext csc, PdfModel pdf, PdfPersonModel person){
		
		PdfPersonSignDataModel signData = person.getSignData();
		signData.getOtpDigitato().resetTypeErrors();
		
		if(pdf.isTestMode())
			return true;
		
		try{
			
			if(signData.getOtpDigitato().isNull()){
				signData.getOtpDigitato().addTypeError("Digitare il codice OTP");
				return false;
			}
			
			if(!signData.getSavOtpDigitato().isNull()){
				if(!signData.getOtpDigitato().equals(signData.getSavOtpDigitato())){
					signData.getOtpDigitato().addTypeError("Inserire lo stesso codice OTP ricevuto in precedenza");
					return false;
				}else{
					return true;
				}
			}
			
			signData.setSavOtpDigitato(new StringType());
			DAOQASResultModel qasRes = new DAOObject(csc,DAO_XML).executeQASAccess("verificaOtp",person);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				signData.getOtpDigitato().addTypeError("Errore di servizio nel verificare l'OTP: "+qasRes.getQasCallData().getMessage());
			}else{
				if(!"OK".equals(signData.getEsitoChiamataServizio().toString()))
					signData.getOtpDigitato().addTypeError(signData.getMessaggioChiamataServizio().toString());
			}
			if(signData.getOtpDigitato().hasTypeErrors())
				return false;
			
			signData.setSavOtpDigitato(new StringType(signData.getOtpDigitato().toString()));
			return true;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			pdf.addCommandError("Errore DAO nel verificare l'OTP per "+person.readCognomeNome()+": "+daoe.toString());
			return true;
		}
		
	}

	/*******************************************************************/
	/*******************************************************************/
	private static byte[] generatePdfForSign(ClientSessionContext csc, PdfModel pdf) throws Exception, DAOException{
		
		// Create pdf
		byte[] pdfContent = PdfEngine.compilePdfFields(csc, pdf, false, false, "SIGN");
		
		// Get sign list to remove from pdf
		ArrayList<String> signFieldToRemove = signFieldsToRemove(pdf);
		if(!signFieldToRemove.isEmpty())
			pdfContent = PdfEngine.removePdfFields(csc, pdfContent, signFieldToRemove, false);

		pdfContent = setFlatForSign(pdfContent);
		
		if(pdf.isInAccettazioneCopernico())
			pdfContent = PdfCopernicoAttachments.addAttachments(csc, pdf, pdfContent);
		
		String title = pdf.getPdfData().getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfCode()+" "+pdf.mainPdfAnag().getPdfDescr():pdf.getPdfData().getPdfTitle().toString();
		pdfContent = PdfTools.rendiPdfAccessibile(csc, pdfContent, title, pdf.getPdfData().getPdfInstanceId().toString());
		return pdfContent;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static byte[] firmaPdf(ClientSessionContext csc, PdfModel pdf){
		
		try{

			// Put customer name on selected sign
			putPersonNameOnSigns(pdf);

			// Generate sign pdf
			byte[] pdfContent = generatePdfForSign(csc, pdf);

			String location = "";
			PdfDataModel pdfMainData = pdf.mainPdfData();
			location = pdfMainData.readProperty(PdfPredefinedFields.LUOGO) == null ? "" : pdfMainData.readProperty(PdfPredefinedFields.LUOGO).toString();
			
			MapCommandDataModel dynamicData = new MapCommandDataModel();
			
			DAOObject dao = new DAOObject(csc,DAO_XML);
			dao.executeQueryAccess("loadDatiRAO",dynamicData);
			String dominioCA = dynamicData.readProperty("raoDominio").toString();
			
			ArrayList<PdfPersonModel> fullProccessPersons = pdf.getFullProcessPersons();
			int cliIdx = 1;
			StringBuilder xmlFirmatari = new StringBuilder();
			StringBuilder xmlNdg = new StringBuilder();
			for(int i=0;i<fullProccessPersons.size();i++){
				PdfPersonModel person = (PdfPersonModel)fullProccessPersons.get(i);
				String xmlFirmatario = xmlFirmatario(pdf, person, dominioCA, location); 
				if(!person.isAgente() && xmlFirmatario.length() > 0)
					xmlNdg.append("<NDG"+(i+1)+">"+person.readCognomeNome()+"</NDG"+(i+1)+">");
				xmlFirmatari.append(xmlFirmatario);
				cliIdx++;
			}
			if(xmlFirmatari.toString().length() == 0){
				pdf.addCommandError("Nessun soggetto previsto in firma");
				return null;
			}
			
			if(pdf.isTestMode())
				return pdfContent;
			
			String hashdoc = "";
			try{
				hashdoc = calcolaHash(pdfContent);
			}catch(Exception e){
				if(pdf.isInAccettazioneCopernico())					
					pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
				else
					pdf.addCommandError("Errore nella generazione della HASH del modulo");
				return null;
			}
			
			String xmlDaFirmare = 	"<datifirma>"+
										"<hash_doc>"+hashdoc+"</hash_doc>"+
										xmlNdg+
										"<CODICECLI/>"+
										"<CODICEDOC>"+pdf.getPdfData().getPdfInstanceId()+"</CODICEDOC>"+
										"<DATARIF/>"+
										"<lista_firmatari>"+
											xmlFirmatari+
										"</lista_firmatari>"+
									"</datifirma>";
						
	        xmlDaFirmare = new String(Tools.encodeBase64Chunked(xmlDaFirmare.getBytes()));
			dynamicData.addProperty("xmlDaFirmare",new StringType(xmlDaFirmare));
			dynamicData.addProperty(S_XML_FIRMATO,new StringType());
			DAOOSBResultModel osbRes = dao.executeOSBAccess("firmaWS",dynamicData);
			if(osbRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				if(pdf.isInAccettazioneCopernico())					
					pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
				else
					pdf.addCommandError("Errore nel firmare i dati di contratto: "+osbRes.getWsCallData().getMessage());
				return null;
			}
			StringType xmlFirmato = (StringType)dynamicData.readProperty(S_XML_FIRMATO);
			if(xmlFirmato.isNull()){
				if(pdf.isInAccettazioneCopernico())					
					pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
				else
					pdf.addCommandError("Il servizio di firma dati di contratto non ha restituito dati validi");
				return null;
			}
			
			// Richiamo al vero e proprio servizio di firma
			String pdfContent64Base = new String(Tools.encodeBase64Chunked(pdfContent));
			dynamicData.addProperty(S_XML_FIRMATO,new StringType(xmlFirmato.toString()));
			dynamicData.addProperty("pdfContent64Base",new StringType(pdfContent64Base)); 	// Pdf in input alla chiamata
			dynamicData.addProperty("pdfContent64BaseSigned",new StringType());  			// Pdf in output alla chiamata
			osbRes = dao.executeOSBAccess("firmaContratto",dynamicData);
			if(osbRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				if(pdf.isInAccettazioneCopernico())					
					pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
				else
					pdf.addCommandError("Errore di sistema nel firmare il contratto: "+osbRes.getWsCallData().getMessage());
				return null;
			}
			
			StringType pdfContent64BaseSigned = (StringType)dynamicData.readProperty("pdfContent64BaseSigned");
			if(pdfContent64BaseSigned.isNull()){
				if(pdf.isInAccettazioneCopernico())					
					pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
				else
					pdf.addCommandError("Il servizio di firma del contratto non ha restituito dati validi");
				return null;
			}

			byte[] pdfFirmato = Tools.decodeBase64(pdfContent64BaseSigned.toString().getBytes());
			return pdfFirmato;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			if(pdf.isInAccettazioneCopernico())					
				pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
			else
				pdf.addCommandError("Errore DAO nel firmare il modulo: "+daoe.toString());
			return null;
		}catch(Exception e){
			e.printStackTrace();
			if(pdf.isInAccettazioneCopernico())					
				pdf.addCommandError(ERRORE_GENERICO_COPERNICO_NMOL);
			else
				pdf.addCommandError("Errore nel firmare il modulo: "+e.toString());
			return null;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static byte[] setFlatForSign(byte[] pdfByteArray){
		try{
			InputStream pdf = new ByteArrayInputStream(pdfByteArray);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
			stamp.setFormFlattening(true);
		
		    AcroFields acroForm = stamp.getAcroFields();
			Iterator fields = acroForm.getFields().keySet().iterator();
			while(fields.hasNext()){
				
				String pdfFieldName = (String)fields.next();
				int fieldType = acroForm.getFieldType(pdfFieldName);
				if(fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
					continue;
				stamp.partialFormFlattening(pdfFieldName);
			}
			
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			pdf.close();
			return pdfOut.toByteArray();
			
		}catch(Throwable t){
			return pdfByteArray;
		}
	}
	

	/*******************************************************************/
	/*******************************************************************/
	private static void putPersonNameOnSigns(PdfModel pdf){
		PdfDataModel pdfData = pdf.getPdfData();
		if(!pdf.isMultiPdf()){
			putPersonNameOnSignsForSinglePdf(pdf, pdfData);
		}else{
			for(int i=0;i< pdfData.getPdfs().size();i++)
				putPersonNameOnSignsForSinglePdf(pdf, (PdfDataModel)pdfData.getPdfs().get(i));
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	private static void putPersonNameOnSignsForSinglePdf(PdfModel pdf, PdfDataModel pdfData){
		List<PdfPersonModel> clienti = LegaleRappresentanteManager.elencoClientiFirmatariPG(pdf, pdfData);
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			BooleanType firma = (BooleanType)pdfData.readProperty(fi.htmlFieldName);
			if(firma == null)
				continue;
			if(!firma.booleanValue())
				continue;
			AbstractType nomefirma = pdfData.readProperty("nome"+fi.htmlFieldName);
			if(nomefirma == null)
				continue;
			String nome = "";
			Matcher mat = PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(fi.htmlFieldName);
			if(mat.matches()){
				nome = pdfData.getAgente().readCognomeNome();
			}else{
				mat = PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
				if(mat.matches()){
					try{
						nome = ((PdfPersonModel)clienti.get(Integer.parseInt(mat.group(2))-1)).readCognomeNome();
					}catch(Throwable t){}
				}
			}
			if(nome.length() > 0)
				pdfData.addProperty("nome"+fi.htmlFieldName,new StringType("Firmato digitalmente da\n"+nome));
			else
				pdfData.addProperty("nome"+fi.htmlFieldName,new StringType());
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> signFieldsToRemove(PdfModel pdf) throws Exception{
		PdfDataModel pdfData = pdf.getPdfData();
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		if(!pdf.isMultiPdf()){
			signFieldToRemove = signFieldsToRemoveFromSinglePdf(pdfData,"");
		}else{
			for(int i=0;i< pdfData.getPdfs().size();i++)
				signFieldToRemove.addAll(signFieldsToRemoveFromSinglePdf((PdfDataModel)pdfData.getPdfs().get(i),PdfEngine.multiPdfPrefix(i)));
		}
		return signFieldToRemove;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static ArrayList<String> signFieldsToRemoveFromSinglePdf(PdfDataModel pdfData, String fieldNamePrefix) throws Exception{ 
		ArrayList<String> signFieldToRemove = new ArrayList<String>();
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			BooleanType firma = (BooleanType)pdfData.readProperty(fi.htmlFieldName);
			if(firma == null || !firma.booleanValue()){
				signFieldToRemove.add(fieldNamePrefix+fi.pdfFieldName);
				signFieldToRemove.add(fieldNamePrefix+"nome"+fi.pdfFieldName);
			}
		}
		return signFieldToRemove;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static String xmlFirmatario(PdfModel pdf, PdfPersonModel person, String dominioCA, String location){
		
		StringBuilder listacampi = new StringBuilder();
		
		if(pdf.isMultiPdf()){
			for(int pdfIndex=0; pdfIndex < person.getPdfPersonsSignProcessInfos().length; pdfIndex++){
				PdfPersonSignProcessInfo pi = (PdfPersonSignProcessInfo)person.getPdfPersonsSignProcessInfos()[pdfIndex];
				if(!pi.isHasSignOnPdf())
					continue;
				for(String signFieldName : pi.getSignedFieldNames())
					listacampi.append(xmlCampoFirma(PdfEngine.multiPdfPrefix(pdfIndex)+signFieldName, location));
			}
		}else{
			PdfPersonSignProcessInfo pi = (PdfPersonSignProcessInfo)person.getPdfPersonsSignProcessInfos()[0];
			if(!pi.isHasSignOnPdf())
				return "";
			for(String signFieldName : pi.getSignedFieldNames())
				listacampi.append(xmlCampoFirma(signFieldName, location));
		}

		if(listacampi.length() == 0)
			return "";
		
		StringBuilder result = new StringBuilder();
		result.append("<firmatario>");
			result.append("<alias>"+person.readCodiceFiscale()+"</alias>\n");
			result.append("<dominio>"+dominioCA+"</dominio>");
			result.append("<pin/>");
			result.append("<lista_campi>");
			result.append(listacampi);
			result.append("</lista_campi>");
		result.append("</firmatario>");
		return result.toString();
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static String xmlCampoFirma(String nomeCampoFirma, String location){
		StringBuilder sb = new StringBuilder("<campo>");
		sb.append("<nome>"+nomeCampoFirma+"</nome>");
		sb.append("<reason>Dichiarazione Firma</reason>");
		if(location != null && location.length() > 0)
			sb.append("<location>"+location+"</location>");
		sb.append("</campo>");
		return sb.toString();
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static String calcolaHash(byte[] file) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(file);
		byte[] hashdoc = md.digest();
		hashdoc = Tools.encodeBase64(hashdoc);
		return new String(hashdoc);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static StringType getNdgFintoProspect(ClientSessionContext csc, String codFiscale) throws DAOException{
		
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("codFiscale", new StringType(codFiscale));				
		StringType ndg = (StringType)new DAOObject(csc, DAO_XML).executeQueryAccess("getNdgFintoProspect",input).getSingleResult();
		return ndg == null ? new StringType() : ndg;
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static boolean hasSoggettoContiBancaDiretta(ClientSessionContext csc, StringType ndg) throws DAOException{
		
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("ndg", new StringType(ndg.toString()));	
		BooleanType hasConti = (BooleanType)new DAOObject(csc, DAO_XML).executeQueryAccess("hasSoggettoContiBancaDiretta",input).getSingleResult();
		return hasConti == null ? false : hasConti.booleanValue();
		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static String verificaCodici(ClientSessionContext csc, StringType ndg, String cognomeNome, boolean inAccettazioneCopernico) throws DAOException{
		
		try{
			
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("ndg", new StringType(ndg.toString()));				
			DAOQASResultModel qasRes = new DAOObject(csc, DAO_XML).executeQASAccess("getUserProfile",input);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
				if(inAccettazioneCopernico)
					return ERRORE_GENERICO_COPERNICO_NMOL;
				else
					return "Errore di sistema nel recuperare lo UserProfile: "+qasRes.getQasCallData().getMessage();
			}
			
			MapCommandDataModel srvRes = (MapCommandDataModel)qasRes.getResult();
			String blocked1 = srvRes.readProperty("blocked1") == null ? "" : srvRes.readProperty("blocked1").toString();
			String errNo = srvRes.readProperty("errNo") == null ? "" : srvRes.readProperty("errNo").toString();
			
			if("0".equals(errNo) && "0".equals(blocked1))
				return null;	// Tutto ok
			
			if("15".equals(errNo) && "1".equals(blocked1)){
				if(inAccettazioneCopernico)
					return ERRORE_GENERICO_COPERNICO_NMOL;
				else
					return S_SRV_FIRMA_PREFIX+cognomeNome+" in quanto non attivo il servizio di Banca Diretta.";
			}else{
				if(inAccettazioneCopernico)
					return ERRORE_GENERICO_COPERNICO_NMOL;
				else
					return S_SRV_FIRMA_PREFIX+cognomeNome+" in quanto il suo primo codice segreto è in stato di blocco";
			}
			
		}catch(DAOException daoe){
			if(inAccettazioneCopernico)
				return ERRORE_GENERICO_COPERNICO_NMOL;
			else
				return "Errore DAO nel verificare i codici di "+cognomeNome+" per banca diretta: "+daoe.toString();
		}
	}
	
	/*****************************************************************************************************/
	// Verifico che, se non ha già banca diretta, avrà un conto entro x giorni
	private static final String COD_AGENTE = "codAgente";
	/*****************************************************************************************************/
	private static String verificaEsistenzaBozzeMyfreedom(ClientSessionContext csc, PdfPersonModel persona, boolean inAccettazioneCopernico){
		
		try{
			
			DAOObject dao = new DAOObject(csc, DAO_XML);
			
			IntegerType numGG = null;
			try{ numGG = PdfConfig.getParamAsInt(csc, "SIGN_PROCESS", "GG_BOZZE_MYFREEDOM"); }catch(Exception e){ numGG = null; }
			if(numGG == null || numGG.isNull())
				numGG = new IntegerType(30);
			
			MapCommandDataModel input = new MapCommandDataModel();
			if(persona.getIsEffettivo().booleanValue())
				input.addProperty("codCliente", new StringType(persona.getNdg().toString()));					
			else
				input.addProperty("codCliente", new StringType(persona.getIdCensimento().toString()));
			
			DateType dataDa = Tools.today();
			dataDa.addDays(-numGG.intValue()); 
			input.addProperty("dataDa", dataDa);
			input.addProperty("dataA", Tools.today());
		
			String err = null;
			
			// RFC #283062. In rete chiamo per le bozze dell'FB collegato
			String filledAgeCollegato = "";
			if(csc.isRete()) {
				filledAgeCollegato = Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10);
				input.addProperty(COD_AGENTE, new StringType(filledAgeCollegato));
				err = callVerificaEsistenzaBozzeMyfreedomSrv(dao, input, persona, inAccettazioneCopernico);
				if(err == null) // Per Fb collegato OK -> esco
					return null;
			}
			
			// RFC #283062. Chiamo per le bozze dell'FB titolare del cliente
			String filledAgeTitolare = Tools.fillSx(persona.getCodAgente().toString(),'0',10);
			input.addProperty(COD_AGENTE, new StringType(filledAgeTitolare));
			err = callVerificaEsistenzaBozzeMyfreedomSrv(dao, input, persona, inAccettazioneCopernico);
			if(err == null) // Per Fb titolare OK -> esco
				return null;
			
			// RFC #283062. Se KO con FB collegato e titolare, chiamo ancora una volta con l'agente impersonato.
			// Chiamo solo se è impostata l'impersonificazione e l'agente impersonato è differente dall'FB collegato, impostato solo lato rete, e dal titolare.
			if(!persona.getCodAgeImpersonato().isNull()) {
				String filledAgeImpersonato = Tools.fillSx(persona.getCodAgeImpersonato().toString(),'0',10);
				if(!filledAgeImpersonato.equals(filledAgeCollegato) && !filledAgeImpersonato.equals(filledAgeTitolare)) {
					input.addProperty(COD_AGENTE, new StringType(filledAgeImpersonato));
					err = callVerificaEsistenzaBozzeMyfreedomSrv(dao, input, persona, inAccettazioneCopernico);
				}
			}
			
			return err;
			
		}catch(DAOException daoe){
			if(inAccettazioneCopernico)
				return ERRORE_GENERICO_COPERNICO_NMOL;
			else
				return "Errore di sistema nel verificare se per "+persona.readCognomeNome()+" esistono bozze MyFreedom: "+daoe.toString();
		}
	}	

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static String callVerificaEsistenzaBozzeMyfreedomSrv(DAOObject dao, MapCommandDataModel input, 
															     PdfPersonModel persona, boolean inAccettazioneCopernico) throws DAOException{
		DAOOSBResultModel osbRes = dao.executeOSBAccess("getBozzeMyFreedom",input);
		if(osbRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK)
			return "Errore nel recuperare le bozze MyFreedom: "+osbRes.getWsCallData().getMessage();
		MapCommandDataModel srvRes = (MapCommandDataModel)osbRes.getResult();
		String identvoPrat = srvRes.readProperty("identvoPrat") == null ? "" : srvRes.readProperty("identvoPrat").toString();
		if(identvoPrat.length() == 0){
			if(inAccettazioneCopernico)
				return ERRORE_GENERICO_COPERNICO_NMOL;
			else
				return S_SRV_FIRMA_PREFIX+persona.readCognomeNome()+" in quanto non attivo il servizio di Banca Diretta";
		}	
		return null;
	}
}
