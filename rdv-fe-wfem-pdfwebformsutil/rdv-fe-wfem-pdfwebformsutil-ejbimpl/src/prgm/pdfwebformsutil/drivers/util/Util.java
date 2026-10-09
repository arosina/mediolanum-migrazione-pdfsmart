package prgm.pdfwebformsutil.drivers.util;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.nasstorage.SaveFileInfo;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Logger;
import com.atosorigin.wfem.util.Tools;


public class Util {
	private Util() {
	}
	
 	public static String formatDouble(double value, int digit) {
		NumberFormat nf = NumberFormat.getInstance(java.util.Locale.ITALY);
		nf.setMinimumFractionDigits(digit);
		return nf.format(value);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static double roundDouble(double value, int digit) {
		return BigDecimal.valueOf(value).setScale(digit, BigDecimal.ROUND_HALF_EVEN).doubleValue();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static double valoreFromPercentuale(double tot, double perc, int digit) {
		double result = 0;

		if (perc == 0) {
			result = 0;
		} else if (perc == 100.00) {
			result = tot;
		} else {
			result = Util.roundDouble((tot / 100) * perc, digit);
		}

		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static double percentualeFromValore(double tot, double value, int digit) {
		double result = 0;

		if (value == 0) {
			result = 0;
		} else if (value == tot) {
			result = 100;
		} else {
			result = Util.roundDouble((value / tot) * 100, digit);
		}

		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String lZeroPad(String value, int count) {
		String fmt = String.format("%%0%dd", count); 
		if (value.isEmpty()) {
			value = "0";
		}
		return String.format(fmt, Long.valueOf(value.trim()));
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType contoDaIBAN(ClientSessionContext csc, StringType iban) throws DAOException, Exception {
		StringType retval = new StringType();
		if (!iban.isNull()) {
			String contr = iban.toString().substring(iban.toString().length() - 8);
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(csc, "SER_MIFID", String.format(
					"SELECT CONTR_N FROM CLL.CONTR WHERE PROD_C = CAST('BAN01' AS CHAR(11)) AND CONTR_N LIKE '001/%s/%%' AND GSTD_F_ESIST = 'S'",
					contr), null, MapCommandDataModel.class);
			if (qRes.getResult().size() == 1) {
				MapCommandDataModel out = (MapCommandDataModel) qRes.getResult().get(0);
				retval = (StringType) out.getPropertyValue("contrN");
			}
		}
		return retval;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static int getEta(DateType dn) {
		if (dn.isNull()) {
			return -1;
		}

		Date dataNascita = dn.dateValue();

		int age = 0;
		Calendar birthdate = Calendar.getInstance();
		birthdate.setTime(dataNascita);

		Calendar now = Calendar.getInstance();
		age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
		birthdate.add(Calendar.YEAR, age);

		if (now.before(birthdate))
			age--;

		return age;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String saveToNAS(ClientSessionContext csc, String sharedName, String filePath, String fileName,
			byte[] pdfContent) throws Exception {
		SaveFileInfo saveFileInfo = null;
		saveFileInfo = NasStorage.saveFile(csc, sharedName, filePath, fileName, new ByteArrayInputStream(pdfContent));
		return saveFileInfo.getIdFile();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static Date getNextDate(Date dt, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static boolean isDuplicated(ListType list, String methodName, String elem) {
		Class<? extends CommandDataModel> obj = null;
		for (int i = 0; i < list.size(); i++) {
			obj = list.get(i).getClass();
			Method gs1Method = null;
			StringType str1 = null;
			try {
				gs1Method = obj.getMethod(methodName);
				str1 = (StringType) gs1Method.invoke(list.get(i));
				if (str1 != null && str1.equalsIgnoreCase(elem)) {
					return true;
				}
			} catch (Exception e) {
				Logger.getInstance().error(e);
			}
		}
		return false;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static StringType getNomeSicav(ClientSessionContext csc, StringType codSicav) throws Exception {

		DAOObject dao = new DAOObject(csc, "PdfWebFormsDrivers.FondiTerziAggiuntivo.ver00001.FondiTerziAggiuntivo");

		MapCommandDataModel input = new MapCommandDataModel();

		input.addProperty("codSicav", new StringType(codSicav.toString()));
		try {
			DAOQueryResultModel result = dao.executeQueryAccess("getNomeSicav", input);
			if (result.getResult().size() > 0) {
				MapCommandDataModel ret = (MapCommandDataModel) result.getResult().get(0);
				return (StringType) ret.readProperty("nomeSicav");
			} else {
				throw new Exception("Sicav non trovata " + codSicav);
			}
		} catch (DAOException daoe) {
			throw new Exception(daoe);
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String filtraCaratteriSpeciali(String value) {		
		if (value.contains("é")) {
			value = value.replace('é', 'e');					
		}				
		if (value.contains("é")) {
			value = value .replace('é', 'e');				
		}
		if (value.contains("à")) {
			value = value.replace('à', 'a');					
		}
		if (value.contains("ì")) {
			value = value.replace('ì', 'i');					
		}
		if (value.contains("ù")) {
			value = value.replace('ù', 'u');					
		}
		if (value.contains("ò")) {
			value = value.replace('ò', 'o');					
		}				
		value = value.replaceAll("[^A-Za-z0-9 .-]", "");
		return value;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	/**
	 * @param soggetto
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	@Deprecated
	public static boolean checkCoerenzaDatiCodiceFiscale(CalcoloCodiceFiscale.Soggetto soggetto) throws Exception{
		  
		if (soggetto.getCodiceFiscale() == null || soggetto.getCodiceFiscale().length() == 0) {
			throw new Exception("Codice fiscale da controllare assente");
		}
		
		if (soggetto.getNome() == null || soggetto.getNome().length() == 0) {
			throw new Exception("Nome soggetto assente");
		}
		
		if (soggetto.getCognome() == null || soggetto.getCognome().length() == 0) {
			throw new Exception("Cognome soggetto assente");
		}
		
		if (soggetto.getSesso() == null || soggetto.getSesso().length() == 0) {
			throw new Exception("Sesso soggetto assente");
		}
		
		if (soggetto.getDataNascita() == null) {
			throw new Exception("Data nascita soggetto assente");
		}
		
		if (soggetto.getLuogoNascita() == null || soggetto.getLuogoNascita().length() == 0) {
			throw new Exception("Luogo nascita nascita soggetto assente");
		}		
		
		if (soggetto.getCodiceCatastoComuneNascita() == null || soggetto.getCodiceCatastoComuneNascita().length() == 0) {
			throw new Exception("Codice Catasto Comune Nascita assente");
		}
		
		String codiceFiscaleCalcolato = CalcoloCodiceFiscale.calcolaCodiceFiscale(soggetto);
		
		return soggetto.getCodiceFiscale().equalsIgnoreCase(codiceFiscaleCalcolato);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	
	/**
	 * @param soggetto
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	@Deprecated
	public static boolean checkCodiceFiscale(CalcoloCodiceFiscale.Soggetto soggetto) throws Exception{
		  
		if (soggetto.getCodiceFiscale() == null || soggetto.getCodiceFiscale().length() == 0) {
			throw new Exception("Codice fiscale da controllare assente");
		}
		
		if (soggetto.getCodiceFiscale().length() != 16) {
			return false;
		}
		
		StringBuilder regex = new StringBuilder();
		regex.append("^(?i)(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}");
		regex.append("|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])");
		regex.append("|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])");
		regex.append("|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])");
		regex.append("B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$");
		if (!Pattern.matches(regex.toString(), soggetto.getCodiceFiscale().toUpperCase())) {
			return false;
		}
		
		return true;
	}
		
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static boolean checkPartitaIva(String partitaIva) throws Exception{
		
		if (partitaIva == null) {
			throw new Exception("Partita Iva da controllare assente");
		}
		
		return partitaIva.length() == 11 && Pattern.matches("^[0-9]+", partitaIva);
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static boolean checkEmail (String email) throws Exception {
		
		if (email == null || email.length() == 0) {
			throw new Exception("Email da controllare assente");
		}
		
		String regex = "^[\\w\\.\\-]+@\\w+[\\w\\.\\-]*?\\.\\w{2,4}$";
		return Pattern.matches(regex, email);			
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String concat(Object ... args) {
		StringBuilder result = new StringBuilder();
		for(Object item : args) {
			result.append(item);
		}
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String toCamelCase(String str) {
		StringBuilder result = new StringBuilder();		
		String[] words = str.split(" ");
		for(String word : words) {
			if (result.length() > 0) {
				result.append(" ");
			}
			result.append(Tools.capitalize(word));
		}
		return result.toString();
	}
	
	public static MapCommandDataModel fondoCollocabile(ClientSessionContext csc, StringType codProdotto, StringType origine) throws Exception {		
		MapCommandDataModel res =  new MapCommandDataModel();
		try {
			DAOObject dao = new DAOObject(csc,"PdfWebFormsUtil.FondoCollocabile");
			MapCommandDataModel inputModel = new MapCommandDataModel();
			inputModel.addProperty("codFondo", codProdotto);
			inputModel.addProperty("origine", origine);				
			
			DAOQueryResultModel result = dao.executeQueryAccess("isFondoCollocabile", inputModel);
			if (result.getResult().size() > 0) {
				res = (MapCommandDataModel) result.getResult().get(0);								
			}
		} catch(DAOException daoe){
			String errorMsg = "Util.isFondoCollocabile - Eccezione DAO nel recuperare lo stato del fondo: "+daoe;
			throw new Exception(errorMsg);		
		} catch(Exception e){
			String errorMsg = "Util.isFondoCollocabile - Eccezione generica nel recuperare lo stato del fondo: "+e;
			throw new Exception(errorMsg);
		}
		
		return res;
	}
}
