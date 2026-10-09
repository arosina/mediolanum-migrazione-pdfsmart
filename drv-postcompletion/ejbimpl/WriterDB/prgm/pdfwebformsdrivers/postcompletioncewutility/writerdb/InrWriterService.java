package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOCallableResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.CepeDispOpInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.DispAdeguatezzaInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispCliInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispContrInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispFondiCliInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispFondiInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispFondiMedboxInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispFondiSottInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispIndInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispMezzoPGInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispOperzInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispProfilInvInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispSquadraInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispTerzoPagatoreInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaBenefInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaCliInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaRefTerzoInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaSocietaInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaTitolariInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.CepeDispOp;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.ContatoreModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.DispAdeguatezza;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InfoProdottoModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDisp;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispCli;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispContr;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispFondi;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispFondiCli;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispFondiMedbox;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispFondiSott;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispInd;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispMezzoPG;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispOperz;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispProfilInv;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispSquadra;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispTerzoPagatore;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVita;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVitaBenef;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVitaCli;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVitaRefTerzo;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVitaSocieta;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model.InrDispVitaTitolari;

public class InrWriterService {

	public static StringType CLIENTE_PRIMARIO  = new StringType("01");
	public static StringType CLIENTE_SECONDARIO  = new StringType("02");
	public static StringType CLIENTE_TERZIARIO  = new StringType("03");		
	
	public static StringType SQUADRA_CLIENTE_PRIMARIO  = new StringType("01");
	public static StringType SQUADRA_CLIENTE_SECONDARIO  = new StringType("02");
	public static StringType SQUADRA_CLIENTE_TERZIARIO  = new StringType("03");		

	public static StringType TIPO_CONTRATTO_GENERALE = new StringType("01");
	public static StringType TIPO_CONTRATTO_COMPARTO = new StringType("02");
	public static StringType TIPO_CONTRATTO_COMPARTO_RIMBORSO = new StringType("03");
	public static StringType TIPO_CONTRATTO_GENERALE_CONTO_DI_RIFERIMENTO = new StringType("03");	
	public static StringType TIPO_CONTRATTO_PIC = new StringType("04");
	public static StringType TIPO_CONTRATTO_PAC = new StringType("05");
	public static StringType TIPO_CONTRATTO_COMPARTO_CONVERSIONI = new StringType("10");
	public static StringType TIPO_CONTRATTO_PROGRAMMATA_CONVERSIONI = new StringType("11");

	
	public static StringType TIPO_OPERAZIONE_ADESIONE = new StringType("10");
	public static StringType TIPO_OPERAZIONE_PROGRAMMATA = new StringType("06");
	public static StringType TIPO_OPERAZIONE_ALIMENTAZIONE = new StringType("09");

	public static StringType MEZZO_PAGAMENTO_ASSEGNO_DA_MEDIOLANUM = new StringType("01");
	public static StringType MEZZO_PAGAMENTO_ASSEGNO_BANCARIO = new StringType("02");
	public static StringType MEZZO_PAGAMENTO_ASSEGNO = new StringType("02");
	public static StringType MEZZO_PAGAMENTO_ASSEGNO_CIRCOLARE = new StringType("03");
	public static StringType MEZZO_PAGAMENTO_BANCA_ESTERNA = new StringType("04");
	public static StringType MEZZO_PAGAMENTO_RIMBORSO_QUOTE_FONDO = new StringType("05");
	public static StringType MEZZO_PAGAMENTO_CONTO_RIFERIMENTO_CC = new StringType("10");
	public static StringType MEZZO_PAGAMENTO_CONTO_RIFERIMENTO_CC_IN_APERTURA = new StringType("20");
	public static StringType MEZZO_PAGAMENTO_BONIFICO = new StringType("11");
	public static StringType MEZZO_PAGAMENTO_ASSEGNO_MEDIOLANUM = new StringType("12");
	public static StringType MEZZO_PAGAMENTO_BONIFICO_MEDIOLANUM 	= new StringType("13");
	public static StringType MEZZO_PAGAMENTO_CONTO_CORRENTE_IN_APERTURA 	= new StringType("20");
	public static StringType MEZZO_PAGAMENTO_ASSEGNI = new StringType("51");
	public static StringType MEZZO_PAGAMENTO_GIROCONTO = new StringType("85");
	public static StringType MEZZO_PAGAMENTO_REINVESTIMENTO_SU_STESSO_FONDO  =  new StringType("60");


	
	public static StringType RIMBORSO_TOTALE_COMPARTO = new StringType("09");
	public static StringType RIMBORSO_PARZIALE_COMPARTO = new StringType("08");


	public static StringType ESECUZIONE_INIZIALE  	= new StringType("I");
	public static StringType ESECUZIONE_AGGIUNTIVA	= new StringType("A");
	
	

	public static StringType REINVESTIMENTO_ITALIA = new StringType("I");
	public static StringType REINVESTIMENTO_ESTERI = new StringType("S");

	public static  StringType RID_MEDIOLANUM            						=  new StringType("41");
	public static  StringType RID_ALTRA_BANCA		       						=  new StringType("42");
	public static  StringType RID_CONTO_CORRENTE_IN_APERTURA					=  new StringType("43");
	public static  StringType RID_CONTO_CORRENTE_IN_APERTURA_CON_IMPORTO		=  new StringType("45");
	public static StringType RID_MEDIOLANUM_PERIODICITA   = new StringType("44");
	public static StringType  RID_ALTRA_BANCA_CON_IMPORTO		   =  new StringType("04");
	
	public static String DAO_XML_NAME = "PdfWebFormDriver.PostCompletionCewUtility.WriterDB.WriterDB";
	
	public static DAOObject getDao(ClientSessionContext csc) {
		return new DAOObject(csc,DAO_XML_NAME);
	}
	
	/********************************************************************************************************/
	/**	INR_DISP **/
	/********************************************************************************************************/		
	public static void writeInrDisp(ClientSessionContext csc,DAOObject dao,InrDispInput inrDispInput) throws Exception {
		try {
			InrDisp inrDisp = new InrDisp();
			Tools.copyCommandDataModel(inrDispInput, inrDisp);
			InfoProdottoModel infoProdottoModel = getInfoProdotto(csc, dao, inrDisp.getCodProdotto());
			if(inrDispInput.getOnLine().booleanValue())
				inrDisp.setTipoDisposizione(infoProdottoModel.getTipoDisposizioneOnline()); 
			else if (inrDispInput.getInSwitch().booleanValue()) 
				inrDisp.setTipoDisposizione(infoProdottoModel.getTipoDisposizioneSwitch());
			else
				inrDisp.setTipoDisposizione(infoProdottoModel.getTipoDisposizione()); 
			inrDisp.setSocProdotto(infoProdottoModel.getCodSocietaProdotto());
			inrDisp.setTipoProdotto(infoProdottoModel.getTipoProdotto());		
			inrDisp.setVersioneDisposizione(infoProdottoModel.getVersioneProdotto());
			dao.executeTableInsertAccess("InrDisp",inrDisp);
			
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISP "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISP"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPSQUADRA **/
	/********************************************************************************************************/		
	public static void writeInrDispSquadra(ClientSessionContext csc,DAOObject dao,InrDispSquadraInput inrDispSquadraInput) throws Exception {
		try {
			InrDispSquadra inrDispSquadra = new InrDispSquadra();
			Tools.copyCommandDataModel(inrDispSquadraInput, inrDispSquadra);			
			dao.executeTableInsertAccess("InrDispSquadra",inrDispSquadra);

			
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISP "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISP"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}

	/********************************************************************************************************/
	/**	INR_DISPFONDICLI **/
	/********************************************************************************************************/	
	public static void writeInrDispFondiCli(ClientSessionContext csc,DAOObject dao,InrDispFondiCliInput inrDispFondiCliInput) throws Exception {
		try {
			InrDispFondiCli inrDispFondiCli = new InrDispFondiCli();
			Tools.copyCommandDataModel(inrDispFondiCliInput, inrDispFondiCli);
			dao.executeTableInsertAccess("InrDispFondiCli",inrDispFondiCli);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPFONDICLI "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPFONDICLI"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPIND **/
	/********************************************************************************************************/	
	public static void writeInrDispInd(ClientSessionContext csc,DAOObject dao,InrDispIndInput inrDispIndInput) throws Exception {
		try {
			InrDispInd inrDispInd = new InrDispInd();
			Tools.copyCommandDataModel(inrDispIndInput, inrDispInd);
			dao.executeTableInsertAccess("InrDispInd",inrDispInd);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPIND "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPIND"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPFONDI **/
	/********************************************************************************************************/	
	public static void writeInrDispFondi(ClientSessionContext csc,DAOObject dao,InrDispFondiInput inrDispFondiInput) throws Exception {
		try {
			InrDispFondi inrDispFondi = new InrDispFondi();
			Tools.copyCommandDataModel(inrDispFondiInput, inrDispFondi);
			dao.executeTableInsertAccess("InrDispFondi",inrDispFondi);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPFONDI "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPFONDI"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}	
	
	/********************************************************************************************************/
	/**	INR_DISPFONDISOTT **/
	/********************************************************************************************************/	
	public static void writeInrDispFondiSott(ClientSessionContext csc,DAOObject dao,InrDispFondiSottInput inrDispFondiSottInput) throws Exception {
		try {
			InrDispFondiSott inrDispFondiSott = new InrDispFondiSott();
			Tools.copyCommandDataModel(inrDispFondiSottInput, inrDispFondiSott);
			dao.executeTableInsertAccess("InrDispFondiSott",inrDispFondiSott);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPFONDISOTT "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPFONDISOTT"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}		
	
	/********************************************************************************************************/
	/**	INR_DISPCONTR **/
	/********************************************************************************************************/	
	public static void writeInrDispContr(ClientSessionContext csc,DAOObject dao,InrDispContrInput inrDispContrInput) throws Exception {
		try {
			InrDispContr InrDispContr = new InrDispContr();
			Tools.copyCommandDataModel(inrDispContrInput, InrDispContr);
			dao.executeTableInsertAccess("InrDispContr",InrDispContr);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPCONTR "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPCONTR"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}	
	
	/********************************************************************************************************/
	/**	INR_DISPOPERZ **/
	/********************************************************************************************************/	
	public static void writeInrDispOperz(ClientSessionContext csc,DAOObject dao,InrDispOperzInput inrDispOperzInput) throws Exception {
		try {
			InrDispOperz InrDispOperz = new InrDispOperz();
			Tools.copyCommandDataModel(inrDispOperzInput, InrDispOperz);
			dao.executeTableInsertAccess("InrDispOperz",InrDispOperz);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPOPERZ "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPOPERZ"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}		
	
	/********************************************************************************************************/
	/**	INR_DISPMEZZOPG **/
	/********************************************************************************************************/	
	public static void writeInrDispMezzoPG(ClientSessionContext csc,DAOObject dao,InrDispMezzoPGInput inrDispMezzoPGInput) throws Exception {
		try {
			InrDispMezzoPG inrDispMezzoPG = new InrDispMezzoPG();
			Tools.copyCommandDataModel(inrDispMezzoPGInput, inrDispMezzoPG);
			dao.executeTableInsertAccess("InrDispMezzoPG",inrDispMezzoPG);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPMEZZOPG "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPMEZZOPG"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}	
	
	/********************************************************************************************************/
	/**    INR_DISPVITACLI **/
	/********************************************************************************************************/    
	public static void writeInrDispVitaCli(ClientSessionContext csc,DAOObject dao,InrDispVitaCliInput inrDispVitaCliInput) throws Exception {
		try {
			InrDispVitaCli inrDispVitaCli = new InrDispVitaCli();
			Tools.copyCommandDataModel(inrDispVitaCliInput, inrDispVitaCli);
			dao.executeTableInsertAccess("InrDispVitaCli",inrDispVitaCli);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITACLI "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITACLI"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}

	/********************************************************************************************************/
	/**    INR_DISPVITA **/
	/********************************************************************************************************/    
	public static void writeInrDispVita(ClientSessionContext csc,DAOObject dao,InrDispVitaInput inrDispVitaInput) throws Exception {
		try {
			InrDispVita inrDispVita = new InrDispVita();
			Tools.copyCommandDataModel(inrDispVitaInput, inrDispVita);
			dao.executeTableInsertAccess("InrDispVita",inrDispVita);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITA "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITA"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}

	/********************************************************************************************************/
	/**    INR_DISPVITABENEF **/
	/********************************************************************************************************/    
	public static void writeInrDispVitaBenef(ClientSessionContext csc,DAOObject dao,InrDispVitaBenefInput inrDispVitaBenefInput) throws Exception {
		try {
			InrDispVitaBenef inrDispVitaBenef = new InrDispVitaBenef();
			Tools.copyCommandDataModel(inrDispVitaBenefInput, inrDispVitaBenef);
			if (inrDispVitaBenef.getTipologiaBeneficiario().isNull())
				inrDispVitaBenef.setTipologiaBeneficiario(new StringType("DEC"));
			dao.executeTableInsertAccess("InrDispVitaBenef",inrDispVitaBenef);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITABENEF "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITABENEF "+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**    INR_DISPFONDIMEDBOX **/
	/********************************************************************************************************/    
	public static void writeInrDispFondiMedBox(ClientSessionContext csc,DAOObject dao,InrDispFondiMedboxInput inrDispFondiMedboxInput) throws Exception {
		try {
			InrDispFondiMedbox inrDispFondiMedbox = new InrDispFondiMedbox();
			Tools.copyCommandDataModel(inrDispFondiMedboxInput, inrDispFondiMedbox);
			dao.executeTableInsertAccess("InrDispFondiMedbox",inrDispFondiMedbox);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPFONDIMEDBOX "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPFONDIMEDBOX "+e;
			e = new Exception(errorMsg);
			throw e;
		}	
	}	
	
	/********************************************************************************************************/
	/**    INR_DISPPROFILIINV **/
	/********************************************************************************************************/    
	public static void writeInrDispProfilInv(ClientSessionContext csc,DAOObject dao,InrDispProfilInvInput inrDispProfilInvInput) throws Exception {
		try {
			InrDispProfilInv inrDispProfilInv = new InrDispProfilInv();
			Tools.copyCommandDataModel(inrDispProfilInvInput, inrDispProfilInv);
			dao.executeTableInsertAccess("InrDispProfilInv",inrDispProfilInv);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPPROFILINV "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPPROFILINV"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	
	/********************************************************************************************************/
	/**    INR_DISPVITAREFTERZO **/
	/********************************************************************************************************/    
	public static void writeInrDispVitaRefTerzo(ClientSessionContext csc,DAOObject dao,InrDispVitaRefTerzoInput inrDispVitaRefTerzoInput) throws Exception {
		try {
			InrDispVitaRefTerzo inrDispVitaRefTerzo = new InrDispVitaRefTerzo();
			Tools.copyCommandDataModel(inrDispVitaRefTerzoInput, inrDispVitaRefTerzo);
			dao.executeTableInsertAccess("InrDispVitaRefTerzo",inrDispVitaRefTerzo);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITAREFTERZO "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITAREFTERZO "+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static InfoProdottoModel getInfoProdotto(ClientSessionContext csc,DAOObject dao,
													  StringType codProdotto) throws Exception{													    	
		try{
			DAOObject  dao2 = new DAOObject(csc,DAO_XML_NAME);
			//dao.openConnection();

			InfoProdottoModel infoProdotto = new InfoProdottoModel();			
			infoProdotto.setCodProdotto(codProdotto);
			dao2.executeQueryAccess("loadInfoProdotto",infoProdotto);
			return infoProdotto;
		}catch(DAOException daoe){
			String errorMsg = "DisposizioneTools - Eccezione DAO nel leggere le informazioni relative al prodotto ["+codProdotto+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "DisposizioneTools -  Eccezione  nel leggere le informazioni relative al prodotto ["+codProdotto+"]: "+e;
			e = new Exception(errorMsg);
			throw e;
		}		
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static StringType getContatoreTabella(ClientSessionContext csc, String codAgente, String nomeTabella) throws Exception{

		try{

			DAOObject dao2 = new DAOObject(csc,DAO_XML_NAME);
			//dao.openConnection();

			String filledUserCode = Tools.fillSx(codAgente.toUpperCase(),'0',10);

			ContatoreModel model = new ContatoreModel();
			model.setNomeRisorsa(new StringType(nomeTabella));
			model.setUtente(new StringType(filledUserCode));
			model.setIncremento(new IntegerType(1));
			DAOCallableResultModel callRes = dao2.executeCallableAccess("getContatore",model);
			if(callRes.getResult() != 0)
				throw new Exception("Errore ["+callRes.getResult()+"] nel prendere il contatore  per la tabella [" + nomeTabella + "]: ");
			return model.getProgressivo();

		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel prendere il contatore per la tabella [" + nomeTabella + "]: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel prendere il contatore per la tabella [" + nomeTabella + "]: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/********************************************************************************************************/
	/**	CEPE_DISP_ADEGUATEZZA **/
	/********************************************************************************************************/		
	public static void writeDispAdeguatezza(ClientSessionContext csc,DAOObject dao,DispAdeguatezzaInput dispAdeguatezzaInput) throws Exception {
		try {
			DispAdeguatezza dispAdeguatezzaModel = new DispAdeguatezza();
			Tools.copyCommandDataModel(dispAdeguatezzaInput, dispAdeguatezzaModel);
			dao.executeTableInsertAccess("DispAdeguatezza",dispAdeguatezzaModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPPROFILINV "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPPROFILINV"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPVITATITOLARI **/
	/********************************************************************************************************/		
	public static void writeInrDispVitaTitolari(ClientSessionContext csc,DAOObject dao,InrDispVitaTitolariInput inrDispVitaTitolariInput) throws Exception {
		try {
			InrDispVitaTitolari inrDispVitaTitolariModel = new InrDispVitaTitolari();
			Tools.copyCommandDataModel(inrDispVitaTitolariInput, inrDispVitaTitolariModel);
			dao.executeTableInsertAccess("InrDispVitaTitolari",inrDispVitaTitolariModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITATITOLARI "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITATITOLARI"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPVITASOCIETA **/
	/********************************************************************************************************/		
	public static void writeInrDispVitaSocieta(ClientSessionContext csc,DAOObject dao,InrDispVitaSocietaInput inrDispVitaSocietaInput) throws Exception {
		try {
			InrDispVitaSocieta inrDispVitaSocietaModel = new InrDispVitaSocieta();
			Tools.copyCommandDataModel(inrDispVitaSocietaInput, inrDispVitaSocietaModel);
			dao.executeTableInsertAccess("InrDispVitaSocieta",inrDispVitaSocietaModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPVITASOCIETA "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPVITASOCIETA"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	CEPE_DISPOP **/
	/********************************************************************************************************/		
	public static void writeCepeDispOp(ClientSessionContext csc,DAOObject dao,CepeDispOpInput cepeDispOpInput) throws Exception {
		try {
			CepeDispOp cepeDispOpModel = new CepeDispOp();
			Tools.copyCommandDataModel(cepeDispOpInput, cepeDispOpModel);
			dao.executeTableInsertAccess("CepeDispOp",cepeDispOpModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su CEPE_DISPOP "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su CEPE_DISPOP"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}
	
	/********************************************************************************************************/
	/**	INR_DISPCLI **/
	/********************************************************************************************************/		
	public static void writeInrDispCli(ClientSessionContext csc,DAOObject dao,InrDispCliInput inrDispCliInput) throws Exception {
		try {
			InrDispCli inrDispCliModel = new InrDispCli();
			Tools.copyCommandDataModel(inrDispCliInput, inrDispCliModel);
			dao.executeTableInsertAccess("InrDispCli",inrDispCliModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPCLI "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPCLI"+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
	}

	/********************************************************************************************************/
	/**	INR_DISPTERZOPAGATORE **/
	/********************************************************************************************************/		
	public static void writeInrDispTerzoPagatore(ClientSessionContext csc, DAOObject dao, InrDispTerzoPagatoreInput inrDispTerzoPagatoreInput) throws Exception {

		if(inrDispTerzoPagatoreInput.getRelazioneContraenteTerzoPagatore()==null || inrDispTerzoPagatoreInput.getRelazioneContraenteTerzoPagatore().isNull()) {
			return;
		}

		try {
			InrDispTerzoPagatore inrDispTerzoPagatoreModel = new InrDispTerzoPagatore();
			Tools.copyCommandDataModel(inrDispTerzoPagatoreInput, inrDispTerzoPagatoreModel);
			dao.executeTableInsertAccess("InrDispTerzoPagatore", inrDispTerzoPagatoreModel);
		}catch(DAOException daoe){
			String errorMsg = InrWriterService.class+ " Eccezione DAO nel salvare la disposizione su INR_DISPTERZOPAGATORE "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = InrWriterService.class+" Eccezione  nel salvare la disposizione su INR_DISPTERZOPAGATORE"+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
}
