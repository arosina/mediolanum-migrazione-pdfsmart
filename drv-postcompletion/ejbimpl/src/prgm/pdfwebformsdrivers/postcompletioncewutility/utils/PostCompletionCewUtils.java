package prgm.pdfwebformsdrivers.postcompletioncewutility.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfFilenetUtil;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico.InputServizioCopernicoModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.AdeguatezzaClienteModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.AgenteModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.AgevolazioneModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.BancaModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ContoCorrenteModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.PdfInstancePostCompletionModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.riepilogoprimenuove.InputRiepilogoPrimeNuoveModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.InrWriterService;

public abstract class PostCompletionCewUtils {

	/*
	 * Costanti 
	 */
	private static String DAO_NAME = "PdfWebFormDriver.PostCompletionCewUtility.PostCompletionCew";
	public static String FREQUENZA_RATA_PAC_MENSILE = "Mensile";
	public static String FREQUENZA_RATA_PAC_BIMESTRALE = "Bimestrale";	
	public static String FREQUENZA_RATA_PAC_TRIMESTRALE = "Trimestrale";
	public static String FREQUENZA_RATA_PAC_QUADRIMESTRALE= "Quadrimestrale";
	public static String FREQUENZA_RATA_PAC_SEMESTRALE = "Semestrale";
	public static String FREQUENZA_RATA_PAC_ANNUALE = "Annuale";
	
	public static StringType ABI_MEDIOLANUM = new StringType("03062");
	public static StringType CAB_MEDIOLANUM = new StringType("34210");
	
	public static String INI_ITA_MOM_CODE = "FI02";
	public static String AGG_ITA_MOM_CODE = "FI07";
	public static String INI_IRL_TM_MOM_CODE = "FE02";
	public static String INI_IRL_LT_MOM_CODE = "FE03";
	public static String AGG_IRL_MOM_CODE = "FE23";
	public static String DOUBLECHANCE_MOM_CODE = "BA45";
	public static String IIS_MOM_CODE = "FE27";
	public static String IISPLUS_MOM_CODE = "FE28";
	
	/*
	 * Costanti Copernico
	 */
	public static int NUM_GIORNI_VALIDITA_COPERNICO = 3;
	public static String MODALITA_SOTTOSCRIZIONE_COPERNICO = "COPERNICO";
	public static String MSG_ECCEZIONE_GENERICA_COPERNICO = "Impossibile proseguire. Si è verificato un problema tecnico nell'invio a Copernico";
	
	public static String CADENZA_QUINDICINALE = "Quindicinale";
	public static String CADENZA_MENSILE = "Mensile";
	public static String CADENZA_BIMESTRALE = "Bimestrale";
	public static String CADENZA_TRIMESTRALE = "Trimestrale";
	public static String CADENZA_SEMESTRALE = "Semestrale";
	public static String CADENZA_ANNUALE = "Annuale";
	
	
	public static Integer GIORNI_CADENZA_QUINDICINALE = 15;
	public static Integer GIORNI_CADENZA_MENSILE = 30;
	public static Integer GIORNI_CADENZA_BIMESTRALE = 60;
	public static Integer GIORNI_CADENZA_TRIMESTRALE = 90;
	public static Integer GIORNI_CADENZA_SEMESTRALE = 180;
	public static Integer GIORNI_CADENZA_ANNUALE = 360;
	
	/*
	 * Costanti FD
	 */
	public static String MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE = "FD";
	
	/*
	 * Metodi di utilita generici
	 */
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType formatCodAgenete10(StringType codAgente) {
		return left0Fill(codAgente, 10);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType formatCodClientMed11(StringType cod) {
		return left0Fill(cod, 11);
	}	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType left0Fill(StringType value, int size) {
		if(value.isNull()) return value;
		String inString = value.stringValue().trim();
		if(size<=inString.length()) return new StringType(inString);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size-inString.length(); i++) {
			sb.append("0");
		}
		sb.append(inString);
		return new StringType(sb.toString());
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	
	public static DoubleType calcolaPercentuale(DoubleType value, DoubleType tot) throws Exception {
		if(value.isNull() || tot.isNull() || BigDecimal.ZERO.compareTo(tot.bigValue())==0) {
			return new DoubleType();
		} else {
			BigDecimal perc = value.bigValue().divide(tot.bigValue(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			return new DoubleType(perc);
		}
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	
	public static DoubleType calcolaTotaleImporto(PdfDataModel pdfDataModel, String field) throws Exception {
		DoubleType res = new DoubleType(BigDecimal.ZERO);
		for(int i=0;;i++){
			DoubleType importo = (DoubleType)pdfDataModel.readProperty(field+i); 
			if(importo == null || importo.isNull())
				break;	
			if(!importo.isNull()) {
				res = res.add(importo);
			}
		}	
		return res;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	
	public static StringType getCodPeriodoVersato (StringType frequenzaRataPac) {
		StringType retval = new StringType();
		
		if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_MENSILE)) {
			retval = new StringType("12");
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_BIMESTRALE)) {
			retval = new StringType("06");		
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_TRIMESTRALE)) {
			retval = new StringType("04");	
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_QUADRIMESTRALE)) {
			retval = new StringType("03");	
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_SEMESTRALE)) {
			retval = new StringType("02");	
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_SEMESTRALE)) {
			retval = new StringType("02");	
		} else if (frequenzaRataPac.equalsIgnoreCase(PostCompletionCewUtils.FREQUENZA_RATA_PAC_ANNUALE)) {
			retval = new StringType("01");	
		}
		
		
		
		return retval;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	

	/*
	 * Converte un DateType in StringType con formato dd/MM/yyyy
	 */
	public static StringType convertDateTypeToStringType(DateType date) {
		if (!date.isNull()) {
		 return	new StringType(date.getGG()
					+"/"+
					date.getMM()
					+"/"+
					date.getAA());
		} else {
			return null;
		}
	}	
	/**************************************************************************************************/
	/**************************************************************************************************/	
	public static int getNumeroIntestatari(StringType codiceFiscale2, StringType codiceFiscale3) throws Exception{
		int numeroIntestatari = 3;
		if(codiceFiscale2.isNull())
			numeroIntestatari--;
		if(codiceFiscale3.isNull())
			numeroIntestatari--;
		return numeroIntestatari;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType buildContoDaIban(ClientSessionContext csc, StringType iban) throws DAOException, Exception {
		String conto = iban.toString().substring(19, 27);
		return buildContoDaNumeroConto(csc, new StringType(conto));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType buildContoDaNumeroConto(ClientSessionContext csc, StringType conto) throws DAOException, Exception {
		StringType retval = new StringType();
		
		String contoTemp = Tools.fillSx(conto.toString(), '0', 8);

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"SER_MIFID", 
											"SELECT " + 
											"	CONTR_N " + 
											"FROM" + 
											"	CLL.CONTR " + 
											"WHERE " + 
											"	PROD_C = cast('BAN01' as char(11)) " +  //defect 36859
											"	AND GSTD_F_ESIST = 'S' " + 
											"	AND CONTR_N  LIKE '001/' || '"+  contoTemp +"' || '%'" ,
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("contrN");
		} else {
			throw new Exception("Problemi nel recupero del conto corrente con iban " + conto);
		}

		return retval;
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType buildContoTecnico(PdfDataModel pdfDataModel) throws DAOException, Exception {
		StringType numeroContoTecnicoPrenotato = (StringType)pdfDataModel.readProperty("numeroContoTecnicoPrenotato");
		StringType cinNumeroContoTecnicoPrenotato = (StringType)pdfDataModel.readProperty("cinContoTecnicoPrenotato");

		return new StringType("001/"+ Tools.fillSx(numeroContoTecnicoPrenotato.toString(), '0', 8) +"/"+Tools.fillSx(cinNumeroContoTecnicoPrenotato.toString(), '0', 2));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static PdfDataModel getOtherPdfData(PdfModel pdf, String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return null;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return pdfDataElement;
			}
		}
		return null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getCodiceDisposizioneFittizioForSwitch(StringType codiceDisposizione) {
		// il metodo prende in input il codice disposizione AXXXXXXXXXX e deve tornare il medesimo codice che al posto della A ha la B
		
		String codDisp = codiceDisposizione.toString();
		String replaceCod = codDisp.replace('A', 'B');
		StringType replacedCod = new StringType (replaceCod);
		
		return replacedCod;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	
	//defect 37839, 37848
	public static DoubleType approssimaRataDC(DoubleType importoTotale, StringType numeroRate, DoubleType importoSingolaRata, int idx, DoubleType importoComparto) throws Exception {
		int numRateInt = Integer.parseInt(numeroRate.toString());
		double	totaleDaPdf = importoSingolaRata.doubleValue() * numRateInt;
		DoubleType 	primaRata = null;
		if (idx == 0) { 
			if (totaleDaPdf < importoTotale.doubleValue()) {
			 	primaRata = new DoubleType(importoComparto.doubleValue() + (importoTotale.doubleValue() - (importoSingolaRata.doubleValue()* numRateInt))); //defect 37848 - reopen
			}else {
				primaRata = new DoubleType(importoComparto.doubleValue() + ((importoSingolaRata.doubleValue()* numRateInt) - importoTotale.doubleValue())); //defect 37848 - reopen
			}
		} else {
			primaRata = importoComparto;
		}
		return primaRata;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/	
	//defect 37839, 37848
	public static DoubleType approssimaRataDC(DoubleType importoTotale, StringType numeroRate, DoubleType importoSingolaRata) throws Exception {
		int numRateInt = Integer.parseInt(numeroRate.toString());
		double	totaleDaPdf = importoSingolaRata.doubleValue() * numRateInt;
		DoubleType 	retval = null;
		if (totaleDaPdf < importoTotale.doubleValue()) {
		 	retval = new DoubleType(importoSingolaRata.doubleValue() + (importoTotale.doubleValue()- (importoSingolaRata.doubleValue()* numRateInt)));
		} else {
			retval = new DoubleType(importoSingolaRata.doubleValue() + ((importoSingolaRata.doubleValue()* numRateInt) - importoTotale.doubleValue()));
		}
		
		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/*
	 * Attenzione questa query può essere utilizzata solo se si è certi che il cliente abbia un cellulare valido, ovvero se il servizio
	 */
	
	public static StringType getCellularePrimario(ClientSessionContext csc, StringType ndg) throws DAOException, Exception {
		StringType retval = new StringType();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"SER_MIFID", 
											"SELECT " + 
											"	TRIM(RECAPTELCLI_X_PREF_TEL) || TRIM(RECAPTELCLI_X_NUM_TEL) AS cellularePrimario " + 
											"FROM" + 
											"	CLL.V_RECAPTELCLI " + 
											"WHERE " + 
											"	CLI_C = LPAD('"+ndg+"',11, '0')" + 
											"	AND     TIPRECAPTEL_C_TIP_RECAP = '14' "  ,
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("cellulareprimario");
		} else {
			throw new Exception("Problemi nel recupero del cellulare primario del cliente con ndg : " + ndg);
		}

		return retval;
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	/*public static StringType getCodiceComune(ClientSessionContext csc, StringType descrizioneComune) throws DAOException, Exception {
		StringType retval = new StringType();

		
		
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("descrizioneComune", descrizioneComune);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadCodiceComune", mapInput);
		
		if(qRes.getResult().size() == 1 ){
			ComuneModel comuneTemp = (ComuneModel)qRes.getResult().get(0);
			retval = new StringType(comuneTemp.getCodiceComune().intValue() + "");
		} else {
			throw new Exception("Problemi nel recupero del codice comune del comune " + descrizioneComune);
		}
		
		return retval;
	}*/
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static ByteArrayType getPdfContent(ClientSessionContext csc, StringType pdfId) throws DAOException, Exception {
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("pdfId", pdfId);
		ByteArrayType retval = (ByteArrayType)dao.executeQueryAccess("loadPdfContent", mapInput).getSingleResult();
		if(retval == null || retval.isNull()){
			File file = NasStorage.readSimpleFile(csc, "PDFWEBFORMS", "pdf_instance", "pdf_content-"+pdfId+".pdf");
			if(!file.exists()) {
				StringType guid = PdfFilenetUtil.readInstanceFilenetGuid(csc, pdfId);
				if(guid != null)
					retval = new ByteArrayType(PdfFilenetUtil.readFilenetContentFromGuid(csc, guid));					
			}else{
				retval = new ByteArrayType(getFileContent(file));
			}
		}
		return retval;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] getFileContent(File fileReference){
		if(!fileReference.exists())
			return null;
		InputStream fileInputStream = null;
		ByteArrayOutputStream fileOutputStream = null;
		try{
			fileInputStream = new FileInputStream(fileReference);
			fileOutputStream = new ByteArrayOutputStream();
	        byte[] buf = new byte[(16*1024)];
	        int charsRead;
	        while ((charsRead = fileInputStream.read(buf)) != -1) {
	        	fileOutputStream.write(buf, 0, charsRead);
	        	fileOutputStream.flush();
	        }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
	        try{ if(fileInputStream != null){fileInputStream.close();} }catch(Exception e){}
	        try{ if(fileOutputStream != null){fileOutputStream.close();} }catch(Exception e){}
		}
		if(fileOutputStream == null)
			return null;
		else
			return fileOutputStream.toByteArray();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static Map<StringType, StringType> getFondiInPortafoglio(ClientSessionContext csc, StringType numeroContratto) throws DAOException, Exception {
		Map<StringType, StringType> retval = new HashMap<StringType, StringType>();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"SER_MIFID", 
											" SELECT TRIM(PROD_C) AS PROD_C FROM CLL.CONTR WHERE CONTR_N = '"+ numeroContratto.stringValue() + "' AND GSTD_F_ESIST = 'S'", 											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() > 0 ){
			ListType lst = qRes.getResult();
			for(int i=0;i<lst.size() ;i++){
				MapCommandDataModel out = (MapCommandDataModel)lst.get(i);
				StringType prodTemp = (StringType)out.getPropertyValue("prodC");
				retval.put(prodTemp, prodTemp);
			}
		} 

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getCodiceCompartoIIS(ClientSessionContext csc, PdfDataModel pdfDataModel, StringType tipoDisposizione) throws DAOException, Exception {
		
		StringType retval = null;
		
		for(int i=0;;i++){
			StringType codiceFondo = (StringType)pdfDataModel.readProperty("codiceFondo"+i); 
			if(null==codiceFondo) break;
			if(codiceFondo.isNull()) continue;
			
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
					"CEPE", 
					" SELECT " + 
					"	FLAG_SERVIZIO_IIS " + 
					"FROM " + 
					"	INR_DISPCOMPART " + 
					"WHERE " + 
					"	PROD_C_PROD = '"+ codiceFondo + "'" + 
					"	AND DISPCOMPART_C_TIP_DISP = '"+ tipoDisposizione + "'",
					null,
					MapCommandDataModel.class);

			if(qRes.getResult().size() == 1 ){
				MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
				StringType flagIIS = (StringType)out.getPropertyValue("flagServizioIis");
				if (flagIIS.equals("S")) {
					retval = codiceFondo;
					break;
				}
			} else {
				throw new Exception("Problemi nel recupero del flag IIS del comparto " + codiceFondo);
			}
			
		}
		
		if (retval == null) {
			throw new Exception("Non  stato possibile individuare il comparto IIS ");
		}

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getCodiceServizio(ClientSessionContext csc, StringType codComparto, StringType codProdotto) throws DAOException, Exception {
		StringType retval = new StringType();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"CEPE", 
											" SELECT PROD_SERV_KEY 	"	+
											" FROM 	CEPE_INFO_COMPARTI_ITA	"+
											" WHERE	CODICE_COMPARTO = '"+ codComparto.stringValue().toUpperCase() + "' AND  CODICE_PRODOTTO = '"+ codProdotto.stringValue().toUpperCase() + "'",
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("prodServKey");
		} 

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getCodiceFamiglia(ClientSessionContext csc, StringType codComparto, StringType codProdotto) throws DAOException, Exception {
		StringType retval = new StringType();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"CEPE", 
											" SELECT PROD_FAM_KEY 	"	+
											" FROM 	CEPE_INFO_COMPARTI_ITA	"+
											" WHERE	CODICE_COMPARTO = '"+ codComparto.stringValue().toUpperCase() + "' AND  CODICE_PRODOTTO = '"+ codProdotto.stringValue().toUpperCase() + "'",
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("prodFamKey");
		} 

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getTipoVersamento(ClientSessionContext csc, StringType codComparto, StringType codProdotto) throws DAOException, Exception {
		StringType retval = new StringType();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"CEPE", 
											" SELECT TIPO_VERSAMENTO 	"	+
											" FROM 	CEPE_INFO_COMPARTI_ITA	"+
											" WHERE	CODICE_COMPARTO = '"+ codComparto.stringValue().toUpperCase() + "' AND  CODICE_PRODOTTO = '"+ codProdotto.stringValue().toUpperCase() + "'",
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("tipoVersamento");
		} 

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getFlagServizio(ClientSessionContext csc, StringType numeroContratto, StringType codiceComparto) throws DAOException, Exception {
		StringType retval = new StringType();

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"SER_MIFID", 
											" SELECT CONTR_C_TIP 	"	+
											" FROM 	 CLL.CONTR "+
											" WHERE GSTD_F_ESIST = 'S'  AND CONTR_N  = '"+ numeroContratto.stringValue() + "' AND PROD_C = '"  + codiceComparto.stringValue().toUpperCase() + "'",
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType)out.getPropertyValue("contrCTip");
		} 

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static boolean isPersonaGiuridica(ClientSessionContext csc, StringType ndg) throws DAOException, Exception {
		boolean retval = false;

		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, 
											"SER_MIFID", 
											" SELECT CLI_C_PERS_FISICA	"	+
											" FROM 		CLL.CLI	"+
											" WHERE		CLI_C = LPAD(" + ndg + ", 11, '0')",
											null,
											MapCommandDataModel.class);
		
		if(qRes.getResult().size() == 1 ){
			MapCommandDataModel out = (MapCommandDataModel)qRes.getResult().get(0);
			StringType cliCPersFisica =  (StringType)out.getPropertyValue("cliCPersFisica");
			retval = !cliCPersFisica.equals("F");
		} else {
			throw new Exception("Problemi nel recupero dei dati del cliente : " + ndg);
		}

		return retval;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static BancaModel getDatiBancaEsterna(ClientSessionContext csc, StringType codiceAbi, StringType codiceCab) throws Exception, DAOException {
		
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("codiceAbiIn", codiceAbi);
		mapInput.addProperty("codiceCabIn", codiceCab);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadBanca", mapInput);
		
		if(qRes.getResult().size() == 1 ){
			BancaModel retval = (BancaModel)qRes.getResult().get(0);	
			return retval;
		} else {
			throw new Exception("Problemi nel recupero dei dati della banca con abi : " + codiceAbi + "; cab :" + codiceCab);
		}
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static  StringType getCodiceProdottoContrattoProvenienza(ClientSessionContext csc, StringType numeroContrattoProvenienza, StringType ndg) throws DAOException, Exception {
		StringType retval = null;
		StringType ndgTemp = formatCodClientMed11(ndg);
		String sqlQuery = "SELECT " + 
							"	PROD_C " + 
							"FROM " + 
							"	CONTRCLI " + 
							"WHERE " + 
							"	GSTD_F_ESIST = 'S' AND CONTR_N = '" + numeroContrattoProvenienza + "'  AND CLI_C = '" + ndgTemp + "'";
				
		DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc,
						"SER_MIFID", 
						sqlQuery,
						null, 
						MapCommandDataModel.class);
		

		if (qRes.getResult().size() > 0) {
			MapCommandDataModel mapOutput = (MapCommandDataModel)qRes.getResult().get(0);
			retval = (StringType) mapOutput.readProperty("prodC");
		} 
		
		return retval;

	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	//Attenzione : il metodo viene utilizzato sia in fase di scrittura che per le chiamate a FD e Copernico !!!
	//Prima di cambiare la logica verificare .....
	public static AgevolazioneModel getAgevolazione(ClientSessionContext csc, StringType idIstanzaAgevolazione, PdfModel pdfModel, PdfDataModel pdfDataModel,
			boolean gestionePianoPACAgevolazioniCommissionali,
			boolean gestionePianoPACAgevolazioniPersonalPremium,
			boolean gestionePianoPACAgevolazioniMoser) throws DAOException, Exception {
	
		StringType codiceAgevolazione = (StringType)pdfDataModel.read("codiceAgevolazione");
		if(codiceAgevolazione == null)
			codiceAgevolazione = new StringType();
		
		if(pdfDataModel.read("isDerogaSostituzione") != null || 
		   ((idIstanzaAgevolazione == null || idIstanzaAgevolazione.isNull()) && !codiceAgevolazione.isNull())) { // L'agevolazione è di tipo "sostituzione" oppure simile a "dipendenti"
			AgevolazioneModel retval = new AgevolazioneModel();
			retval.setCodiceAgevolazione(codiceAgevolazione);
			retval.setCodiceConvenzione(retval.getCodiceAgevolazione());
			return retval;
		}

		if(idIstanzaAgevolazione == null || idIstanzaAgevolazione.isNull()) { // Con l'id non valorizzato c'è qualcosa che non va perchè i chiamanti verificano che sia stata selezionato il check "altro" prima di chiamare il metodo
			throw new Exception("Agevolazione non trovata.");
		}
		
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("idAgevolazione", idIstanzaAgevolazione);

		
		if (idIstanzaAgevolazione.toString().startsWith("S")) {
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgevolazione", mapInput);
			
			if (qRes.getResult().size() == 1) {
				AgevolazioneModel retval = (AgevolazioneModel)qRes.getResult().get(0);
	
				retval.setIsAgevolazioneCommissionale(new BooleanType("C".equals(retval.getTipoAgevolazione().getStringValue())));
				retval.setIsAgevolazionePersonalPremium(new BooleanType("B".equals(retval.getTipoAgevolazione().getStringValue())));
				retval.setIsAgevolazioneMoser(new BooleanType("M".equals(retval.getTipoAgevolazione().getStringValue())));
				retval.setIsAgevolazioneRetention(new BooleanType("R".equals(retval.getTipoAgevolazione().getStringValue())));
				retval.setIsAgevolazioneDerogaAutomatica(new BooleanType("D".equals(retval.getTipoAgevolazione().getStringValue())));
	
				if (retval.getIsAgevolazioneCommissionale().booleanValue()) {
					// imposto il valire del tipi agevolazione per l'invio del contratto in sede nel casi di ag. commissionale
					// nella chiamata al servizio di invio in sede bisogna passare i codici:
					// 0025 se agev. commissionale al 25%
					// 0050 se agev. commissionale al 50%
					// 0075 se agev. commissionale al 75%
					// 0100 se agev. commissionale al 100%
					boolean trovato = false;
					String perc = retval.getPercentualeAgevolazione().getStringValue();
					perc = perc.substring(0,perc.length() -1 ); // Elimino il simbolo %
					String prefix = "";
					
					if(gestionePianoPACAgevolazioniCommissionali) {
						if("PAC".equals(retval.getPianoVersamentoAgevolazione().getStringValue())
								&& "Tutto il piano".equals(retval.getAgevolazioneCommissionalePac().getStringValue())){
							prefix = "10";
							trovato = true;
						}
					}
		
					if(trovato){
						if ( perc.length() == 3 ) {
							perc = prefix + perc;
						} else {
							perc = prefix + "0" + perc;
						}					
					}else{
						if ( perc.length() == 3 ) {
							perc = "0" + perc;
						} else {
							perc = "00" + perc;
						}					
					}
		
					retval.setCodiceAgevolazione(new StringType(perc));
				} else if (retval.getIsAgevolazionePersonalPremium().booleanValue())  {
					String codiceConvenzione = retval.getCodiceConvenzione().stringValue();
					if(gestionePianoPACAgevolazioniPersonalPremium) {
						if("PAC".equals(retval.getPianoVersamentoAgevolazione().getStringValue())
								&& "Tutto il piano".equals(retval.getAgevolazioneCommissionalePac().getStringValue())){
							codiceConvenzione = "1" + codiceConvenzione;
						}
					}
					retval.setCodiceAgevolazione(new StringType(codiceConvenzione));
				} else if (retval.getIsAgevolazioneMoser().booleanValue())  {
					String codiceConvenzione = retval.getCodiceConvenzione().stringValue();
					if(gestionePianoPACAgevolazioniMoser) {
						if("PAC".equals(retval.getPianoVersamentoAgevolazione().getStringValue())
								&& "Tutto il piano".equals(retval.getAgevolazioneCommissionalePac().getStringValue())){
							codiceConvenzione = "1" + codiceConvenzione;
						}
					}
					retval.setCodiceAgevolazione(new StringType(codiceConvenzione));
				} else if (retval.getIsAgevolazioneRetention().booleanValue())   {
					retval.setCodiceAgevolazione(new StringType("0100"));
				} else {
					retval.setCodiceAgevolazione(new StringType());
				}
				return retval;
			} else {
				//verifico se si tratta di deroga automatica
				mapInput = new MapCommandDataModel();
				mapInput.addProperty("idAgevolazione", idIstanzaAgevolazione);
				qRes = dao.executeQueryAccess("loadAgevolazioneDerogheAutomatiche", mapInput);
				
				if (qRes.getResult().size() > 0) {
					AgevolazioneModel retval = (AgevolazioneModel)qRes.getResult().get(0);
					retval.setIsAgevolazioneDerogaAutomatica(new BooleanType("D".equals(retval.getTipoAgevolazione().getStringValue())));
					retval.setCodiceAgevolazione(new StringType("0100"));
					return retval;
				} else {	
					throw new Exception("Problemi nel recupero dell'agevolazione :" + idIstanzaAgevolazione);
				}
			}
		} else {
			//gestione nuove deroghe
			//#68266
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgevolazioneNuoveDeroghe", mapInput);
			
			if (qRes.getResult().size() == 1) {
				AgevolazioneModel retval = (AgevolazioneModel)qRes.getResult().get(0);
				return retval;
			} else {
				throw new Exception("Errore nel recupero dell'agevolazione numero " + idIstanzaAgevolazione);
			}
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static AgenteModel getDatiAgente(ClientSessionContext csc, StringType codiceAgente) throws Exception, DAOException {
		
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("codAgente", codiceAgente);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadDatiAgente", mapInput);
		
		if(qRes.getResult().size() == 1 ){
			AgenteModel retval = (AgenteModel)qRes.getResult().get(0);	
			return retval;
		} else {
			throw new Exception("Problemi nel recupero dei dati della contratto con codiceAgente :" + codiceAgente);
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static PdfInstancePostCompletionModel getDatiPdf(ClientSessionContext csc, StringType pdfId) throws Exception, DAOException {
		
		DAOObject dao = new DAOObject(csc, DAO_NAME);
		MapCommandDataModel mapInput = new MapCommandDataModel();
		mapInput.addProperty("pdfId", pdfId);
		DAOQueryResultModel qRes = dao.executeQueryAccess("loadPdf", mapInput);
		
		if(qRes.getResult().size() == 1 ){
			PdfInstancePostCompletionModel retval = (PdfInstancePostCompletionModel)qRes.getResult().get(0);	
			return retval;
		} else {
			throw new Exception("Problemi nel recupero dei dati della contratto con pdf id :" + pdfId);
		}
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType buildStringaSerializzataPoliticamenteEsposto(StringType motivazionePoliticamenteEsposto, int numIntestatari, boolean isSwitch, String modalitaSottoscrizione ) {
		String separatore = "|";
		String SI = "S";
		StringBuffer strBf = new StringBuffer();
		strBf.append(separatore + numIntestatari);
		strBf.append(separatore + SI);
		strBf.append(separatore + SI);
		strBf.append(separatore + SI);
		strBf.append(separatore + SI);
		strBf.append(separatore + (!motivazionePoliticamenteEsposto.isNull() ? "S" : "N"));
		if (isSwitch && modalitaSottoscrizione.equals(MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE)) {
			// Se il cliente è politicamente esposto aggiungo anche il motivo
			if (!motivazionePoliticamenteEsposto.isNull())
				strBf.append(separatore + motivazionePoliticamenteEsposto);
		}else {
			// Se il cliente è politicamente esposto aggiungo anche il motivo
			if (!motivazionePoliticamenteEsposto.isNull())
				strBf.append(separatore + motivazionePoliticamenteEsposto);
			else
				strBf.append(separatore);
		}
		strBf.append(separatore + SI);
		strBf.append(separatore + SI + separatore);

		StringType strSerializzata = new StringType(strBf.toString());

		return strSerializzata;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/

	public static String formatAsDoubleWithPoint(Double value) {
		
		NumberFormat df = (NumberFormat) NumberFormat.getInstance(Locale.ENGLISH);		
		return df.format(value);
	}
	/*
	 * Metodi di utilita FD
	 */
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void buildAdeguatezza(InputRiepilogoPrimeNuoveModel inputServizioModel){
		ListType elencoAdeguatezza = new ListType();
		AdeguatezzaClienteModel adeguatezzaClienteModel = new AdeguatezzaClienteModel();
		elencoAdeguatezza.add(adeguatezzaClienteModel);
		inputServizioModel.setElencoAdeguatezza(elencoAdeguatezza);
		inputServizioModel.setIdEsitoAdeguatezza(new StringType(""));
		inputServizioModel.setAdeguatezza(new StringType("S"));
		inputServizioModel.setDescrAdeguatezza(new StringType(""));
		inputServizioModel.setManlevaAdeguatezza(new StringType(""));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void buildDatiRiepilogoPrimeNuove(PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio, boolean mifidII, boolean isRimborso, boolean isSwitch) {		
		
		if (mifidII) {
			inputServizio.setIdSuitability(new StringType(pdfModel.getIdReportAdeguatezza()));
			inputServizio.setPrgSuitability(new StringType("1"));
		}

		inputServizio.setChiaveEK(loadChiaveK(pdfModel.getPdfData().getPdfInstanceId()));
		
		if (!isRimborso && !isSwitch) {
			StringType radarForzato =  (StringType) pdfModel.getPdfData().readProperty("flagRadar");
			if (radarForzato != null) {
				inputServizio.setIsRadarForzato(new StringType(radarForzato.stringValue().trim()));
			}
		} 
		
		inputServizio.setCodDisposizione(pdfModel.getPdfData().getPdfInstanceId()); 

	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static StringType loadChiaveK(StringType pdfInstanceId){
		ClientSessionContext csc = new ClientSessionContext();
		csc.setCountryCode("ITA");
		csc.setChannelCode("P");
		try{
			StringType chiaveK = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
							"select COD_DISPOSITIVA_BMED from PDF_INSTANCE where PDF_INSTANCE_ID='"+pdfInstanceId+"'", null, StringType.class).getSingleResult();
			if(chiaveK != null && !chiaveK.isNull())
				return chiaveK;
			else
				return new StringType();
		}catch(DAOException daoe){
			return new StringType();
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void setIndirizzoEmail(InputRiepilogoPrimeNuoveModel inputServizioModel) {
		inputServizioModel.setRecapitoVia( new StringType("-") );
		inputServizioModel.setRecapitoNum( new StringType("-") );
		inputServizioModel.setRecapitoLoc( new StringType("-") );
		inputServizioModel.setRecapitoCap( new StringType("-") );
		inputServizioModel.setRecapitoPro( new StringType("-") );
		inputServizioModel.setRecapitoNaz( new StringType("-") );
		inputServizioModel.setModalitaComunic(new StringType("EMAIL"));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void setIndirizzoAnagrafico(InputRiepilogoPrimeNuoveModel inputServizioModel) {
		inputServizioModel.setRecapitoVia( new StringType("-") );
		inputServizioModel.setRecapitoNum( new StringType("-") );
		inputServizioModel.setRecapitoLoc( new StringType("-") );
		inputServizioModel.setRecapitoCap( new StringType("-") );
		inputServizioModel.setRecapitoPro( new StringType("-") );
		inputServizioModel.setRecapitoNaz( new StringType("-") );
		inputServizioModel.setModalitaComunic(new StringType("INDIRIZZO_ANAGRAFICA"));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void setIndirizzo(InputRiepilogoPrimeNuoveModel inputServizioModel,StringType via,StringType num,StringType loc,StringType cap,StringType pro,StringType naz) {
		inputServizioModel.setRecapitoVia( via.isNull() ? new StringType("-") : via );
		inputServizioModel.setRecapitoNum( num.isNull() ? new StringType("-") : num );
		inputServizioModel.setRecapitoLoc( loc.isNull() ? new StringType("-") : loc );
		inputServizioModel.setRecapitoCap( cap.isNull() ? new StringType("-") : cap );
		inputServizioModel.setRecapitoPro( pro.isNull() ? new StringType("-") : pro );
		inputServizioModel.setRecapitoNaz( naz.isNull() ? new StringType("-") : naz );
		inputServizioModel.setModalitaComunic(new StringType("NUOVO_INDIRIZZO"));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	/*
	 * Metodi di utilita Copernico
	 */
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void buildAdeguatezza(InputServizioCopernicoModel inputServizio, boolean onlyEsitoAdeguatezza){
		if (onlyEsitoAdeguatezza) {
			inputServizio.setIdEsitoAdeguatezza(new StringType(""));
		} else {
			ListType elencoAdeguatezza = new ListType();
			AdeguatezzaClienteModel adeguatezzaClienteModel = new AdeguatezzaClienteModel();
			elencoAdeguatezza.add(adeguatezzaClienteModel);
			inputServizio.setElencoAdeguatezza(elencoAdeguatezza);
			inputServizio.setIdEsitoAdeguatezza(new StringType(""));
			inputServizio.setAdeguatezza(new StringType("S"));
			inputServizio.setDescrAdeguatezza(new StringType(""));
			inputServizio.setManlevaAdeguatezza(new StringType(""));
		}
		
		
		/*
		 * as is
		 * 		setIdUtenteSecondoCosott(model.getSecondoLegaleRappresentante().getCodMediolanum());
		setIdEsitoAdeguatezza(model.getAdeguatezza().getOutputMifid().getIdEsito());
		setAdeguatezza(model.getAdeguatezza().getIsContrattoAdeguato().booleanValue() ? new StringType("S") : new StringType("N"));
		setDescrAdeguatezza(model.getAdeguatezza().getIsContrattoAdeguato().booleanValue() ? new StringType() : model.getAdeguatezza().getOutputMifid().getDescrizioneEsito());
		setElencoAdeguatezza(buildElencoAdeguatezzaItaliani(model));

		 */
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	public static void buildDatiCopernico(ClientSessionContext csc, InputServizioCopernicoModel inputServizio, PdfModel pdfModel, 
										  boolean mifidII, boolean isRimborso, byte[] pdfContent, boolean isSwitch) throws Exception, DAOException{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, PostCompletionCewUtils.NUM_GIORNI_VALIDITA_COPERNICO);
		c.set(Calendar.AM_PM, Calendar.PM);
		c.set(Calendar.HOUR, 11);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		TimestampType dataFineValidita = new TimestampType(new Timestamp(c.getTime().getTime()));
		inputServizio.setDataFineValidita(new StringType(dataFineValidita.toString())); 

		byte[] pdfByte = Tools.encodeBase64(pdfContent);
		inputServizio.setPdfCopernico(new StringType(new String(pdfByte)));


		if (mifidII) {
			inputServizio.setIdSuitability(new StringType(pdfModel.getIdReportAdeguatezza()));
			inputServizio.setPrgSuitability(new StringType("1"));
			inputServizio.setChiaveEK(new StringType());
		}
		
		if (!isRimborso && !isSwitch) {
			StringType radarForzato = (StringType) pdfModel.getPdfData().readProperty("flagRadar");
			inputServizio.setIsRadarForzato(new StringType(radarForzato.stringValue().trim()));
		}
		
		inputServizio.setCodDisposizione(pdfModel.getPdfData().getPdfInstanceId()); 

	}
	

	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void setIndirizzoEmail(InputServizioCopernicoModel inputServizioModel) {
		inputServizioModel.setRecapitoVia( new StringType("-") );
		inputServizioModel.setRecapitoNum( new StringType("-") );
		inputServizioModel.setRecapitoLoc( new StringType("-") );
		inputServizioModel.setRecapitoCap( new StringType("-") );
		inputServizioModel.setRecapitoPro( new StringType("-") );
		inputServizioModel.setRecapitoNaz( new StringType("-") );
		inputServizioModel.setModalitaComunic(new StringType("EMAIL"));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void  setIndirizzoAnagrafico(InputServizioCopernicoModel inputServizioModel) {
		inputServizioModel.setRecapitoVia( new StringType("-") );
		inputServizioModel.setRecapitoNum( new StringType("-") );
		inputServizioModel.setRecapitoLoc( new StringType("-") );
		inputServizioModel.setRecapitoCap( new StringType("-") );
		inputServizioModel.setRecapitoPro( new StringType("-") );
		inputServizioModel.setRecapitoNaz( new StringType("-") );
		inputServizioModel.setModalitaComunic(new StringType("INDIRIZZO_ANAGRAFICA"));
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void  setIndirizzo(InputServizioCopernicoModel inputServizioModel,StringType via,StringType num,StringType loc,StringType cap,StringType pro,StringType naz) {
		inputServizioModel.setRecapitoVia( via.isNull() ? new StringType("-") : via );
		inputServizioModel.setRecapitoNum( num.isNull() ? new StringType("-") : num );
		inputServizioModel.setRecapitoLoc( loc.isNull() ? new StringType("-") : loc );
		inputServizioModel.setRecapitoCap( cap.isNull() ? new StringType("-") : cap );
		inputServizioModel.setRecapitoPro( pro.isNull() ? new StringType("-") : pro );
		inputServizioModel.setRecapitoNaz( naz.isNull() ? new StringType("-") : naz );
		inputServizioModel.setModalitaComunic(new StringType("NUOVO_INDIRIZZO"));
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static IntegerType calcolaQuantitaRateRimborsoConversioneProgrammato (String cadenza, DateType dataDal, DateType dataAl){
		int giorni = 0;
		Long durata = giorniTraDueDate(dataDal.dateValue(),dataAl.dateValue());
	    int giorniTot = durata.intValue();
	    if (cadenza.equals(CADENZA_QUINDICINALE)) {
	    	//giorni = 15;
	    	giorni = (GIORNI_CADENZA_QUINDICINALE).intValue();
		}else if(cadenza.equals(CADENZA_MENSILE)){
			//giorni = 30;
	    	giorni = (GIORNI_CADENZA_MENSILE).intValue();
	    }else if(cadenza.equals(CADENZA_BIMESTRALE)){
	    	//giorni = 60;
	    	giorni = (GIORNI_CADENZA_BIMESTRALE).intValue();
	    }else if(cadenza.equals(CADENZA_TRIMESTRALE)){
	    	//giorni = 90;
	    	giorni = (GIORNI_CADENZA_TRIMESTRALE).intValue();
	    }else if(cadenza.equals(CADENZA_SEMESTRALE)){
	    	//giorni = 180;
	    	giorni = (GIORNI_CADENZA_SEMESTRALE).intValue(); 
	    }else if(cadenza.equals(CADENZA_ANNUALE)){
	    	//giorni = 360;
	    	giorni = (GIORNI_CADENZA_ANNUALE).intValue();
	    }
	    
	    int numRate = 1;
	    while (true) {
	    	DateType tmp = dataDal.addDaysOnNew(numRate*giorni);
	    	if (tmp.compareTo(dataAl)>0) {
	    		break;
	    	} else {
	    		numRate ++;
	    	}
	    }
	    
	    //#77132 (defect 23043 ) - per la verifica della correttezza del calcolo numRate per le altre cadenze siamo in attesa di risposta da Calzolari
	    if (cadenza.equals(CADENZA_MENSILE))
	    	numRate = numRate + 1;
	    
	    return  new IntegerType(numRate) ;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static long giorniTraDueDate(Date uno, Date due) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(uno);
		c2.setTime(due);

		long giorni = (c2.getTime().getTime() - c1.getTime().getTime())	/ (24 * 3600 * 1000);

		return giorni;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getCodiceTipoFrequenza(StringType frequenza){
		StringType retval = null;
		String cadenza = frequenza.getStringValue();
		if (cadenza.equals(CADENZA_QUINDICINALE)) {
			retval = new StringType("1");
		} else if (cadenza.equals(CADENZA_MENSILE)) {
			retval = new StringType("2");
		} else if (cadenza.equals(CADENZA_BIMESTRALE)) {
			retval = new StringType("3");
		} else if (cadenza.equals(CADENZA_TRIMESTRALE)) {
			retval = new StringType("4");
		} else if (cadenza.equals(CADENZA_SEMESTRALE)) {
			retval = new StringType("6");
		} else if (cadenza.equals(CADENZA_ANNUALE)) {
			retval = new StringType("7");
		}
		return retval;
	}
	
	/*
	 * Metodi di utilita 
	 */
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static ContoCorrenteModel getContoDaIBAN(ClientSessionContext csc, StringType iban) {
		if(null==iban || iban.isNull() || iban.stringValue().trim().length()!=27) return null;
		String ibanS = iban.stringValue().trim();
		String paese= ibanS.substring(0, 2);
		String ibancin = ibanS.substring(2, 4);
		String cin = ibanS.substring(4, 5);
		String abi = ibanS.substring(5, 10);
		String cab = ibanS.substring(10, 15);
		String conto = ibanS.substring(15);
		ContoCorrenteModel res=new ContoCorrenteModel();
		try {
			BancaModel banca = getDatiBancaEsterna(csc, new StringType(abi) , new StringType(cab));
			Tools.copyCommandDataModel(banca, res);
		} catch (Exception e) {
			return null;
		} catch (DAOException e) {
			return null;
		}
		res.setCinIban(new StringType(ibancin));
		res.setCin(new StringType(cin));
		res.setPaese(new StringType(paese));
		res.setNumeroConto(new StringType(conto));
		return res;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/	
	public static StringType getFlagIstat (PdfDataModel pdfDataModel, String nomeCampoFondo, String nomeCampoFlagIstat, String valoreSi, String valoreNo){
		StringType retval = new StringType("");
		boolean trovatoSi = false;
		boolean trovatoNo = false;
		for(int i=0;;i++){
			StringType codiceFondo = (StringType)pdfDataModel.readProperty(nomeCampoFondo + i); 
			if(codiceFondo == null || codiceFondo.isNull())
				break;
			StringType flagIstat = (StringType)pdfDataModel.readProperty(nomeCampoFlagIstat + i); 
			if(flagIstat.equals(valoreSi)){
				trovatoSi = true;
		    	break;		    	
		    } else if (flagIstat.equals(valoreNo)){
				trovatoNo = true;	    	
		    }
		}
		
		if (trovatoSi) {
			retval = new StringType("S");
		}else if (trovatoNo) {
			retval = new StringType("N");
		}
			
		return retval;
	}
	
	public static StringType getPeriodoVersatoForDB(StringType periodoVersato) {
		StringType retval = null;
		if (periodoVersato.equalsIgnoreCase("Annuale")) {
			retval = new StringType("01");
		} else if (periodoVersato.equalsIgnoreCase("Semestrale")) {
			retval = new StringType("02");
		} else if (periodoVersato.equalsIgnoreCase("Trimestrale")) {
			retval = new StringType("04");
		} else if (periodoVersato.equalsIgnoreCase("Bimestrale")) {
			retval = new StringType("06");
		} else if (periodoVersato.equalsIgnoreCase("Mensile")) {
			retval = new StringType("12");
		} else if (periodoVersato.equalsIgnoreCase("Quindicinale")) {
			retval = new StringType("24");
		}
		return retval;
	}
	
	public static StringType getTipoVersamentoFondiIrlanda(StringType codProdotto) {
		if (codProdotto.equals("LT5") || codProdotto.equals("TM5")) {
			return InrWriterService.TIPO_CONTRATTO_PAC;
		}
		return InrWriterService.TIPO_CONTRATTO_PIC;
	}
	
	public static StringType getDescrTipoVersamentoFondiIrlanda(StringType codProdotto) {
		if (codProdotto == null || codProdotto.isNull()) {
			return new StringType("");
		}
		if (codProdotto.equals("LT5") || codProdotto.equals("TM5") || codProdotto.equals("PO5")) {
			return new StringType("PAC");
		}
		return new StringType("PIC");
	}
	
	public static StringType getClasseVersamentoFondiIrlanda(StringType codProdotto) {
		if (codProdotto.equals("LT0") || codProdotto.equals("TM0")) {
			return new StringType("L");
		}else if (codProdotto.equals("LT5") || codProdotto.equals("TM5")) {
			return new StringType("L");
		}else if (codProdotto.equals("L20") || codProdotto.equals("T20")) {
			return new StringType("S");
		}else if (codProdotto.equals("PO0")) {
			return new StringType("S");
		}else if (codProdotto.equals("PA0")) {
			return new StringType("SA");
		}
		return new StringType("");
	}
	
	public static StringType getCodiceFondoFondiIrlanda(StringType tipoSottoscrizione, StringType classeVersamento, StringType codProdotto) {
		if (tipoSottoscrizione.equals("Pac")) {
			if (codProdotto.equals("LT")) {
				return new StringType("LT5");
			} else if (codProdotto.equals("TM")) {
				return new StringType("TM5");
			}
		} else if (tipoSottoscrizione.equals("Pic") || tipoSottoscrizione.equals("Pip")) {//RFC 68764
			if (codProdotto.equals("LT") && classeVersamento.equals("S")) {
				return new StringType("L20");
			} else if (codProdotto.equals("TM") && classeVersamento.equals("S")) {
				return new StringType("T20");
			} else if (codProdotto.equals("LT") && classeVersamento.equals("L")) {
				return new StringType("LT0");
			} else if (codProdotto.equals("TM") && classeVersamento.equals("L")) {
				return new StringType("TM0");
			}
		}
			
		return new StringType("");
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static boolean existOtherPdf(PdfModel pdf, String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return false;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return true;
			}
		}		
		return false;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType loadCodPotenzialeSoggettoEffettivo(ClientSessionContext csc, StringType ndgCliente) throws Exception {
		
		try {
			DAOObject dao = new DAOObject(csc, DAO_NAME);
			MapCommandDataModel inputModel = new MapCommandDataModel();
			inputModel.addProperty("ndg", ndgCliente);
			
			StringType codPotenziale = (StringType)dao.executeQueryAccess("loadCodPotenzialeSoggettoEffettivo", inputModel).getSingleResult();
			if (codPotenziale == null)
				return new StringType();
			
			return codPotenziale;
		} catch (DAOException daoE) {
			throw new Exception(daoE.getMessage());
		}
	}	
}
