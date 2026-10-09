package prgm.ita.anagraficaclienti.facade;

import java.util.Calendar;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.ComuneModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CalcoloCodiceFiscale {
	
    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	

    private final static String ALFABETO = "abcdefghijklmnopqrstuvwxyz";
    private final static String VOCALI = "aeiou";
    
    private final static char CODICI_MESE[] = {
    											'a', 'b', 'c', 'd', 'e', 'h', 'l', 'm', 'p', 'r', 
		    									's', 't'
    								   		  };
    
    private final static char CARATTERI[] = {
									        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
									        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
									        'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', 
									        '4', '5', '6', '7', '8', '9'
    						 				};
    
    private final static int CODICI_DISPARI[] = {
										        1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 
										        2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 
										        16, 10, 22, 25, 24, 23, 1, 0, 5, 7, 
										        9, 13, 15, 17, 19, 21
    							 				};
    
    private final static int CODICI_PARI[] = 	{
										        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
										        10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
										        20, 21, 22, 23, 24, 25, 0, 1, 2, 3, 
										        4, 5, 6, 7, 8, 9
	    										};
    
    private final static char CODICI_CONTROLLO[] = 	{
											        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
											        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
											        'U', 'V', 'W', 'X', 'Y', 'Z'
    												};
    private static final String ERROR = "";

	/**************************************************************************************************/
	/**************************************************************************************************/
    public static String calcolaCodiceFiscale(ClientSessionContext csc, ClienteModel cliente){
    	
    	try{
	    	String cognome = cliente.getCognome().toString();
	    	String nome = cliente.getNome().toString();
	    	DateType dataDiNascita = cliente.getDataNascita();
	    	String sesso = cliente.getSesso().toString();
	    	String codiceCatasto = getCodiceCatasto(csc,cliente);
	    	if(codiceCatasto.length() == 0)
	    		return ERROR;
	    	
	        cognome = pulisci(cognome.toLowerCase());
	        nome = pulisci(nome.toLowerCase());
	
	        String result = getCodiceCognome(cognome).toUpperCase();
	        result += getCodiceNome(nome).toUpperCase();
	        result += getCodiceData(dataDiNascita,sesso).toUpperCase();
	        result += codiceCatasto.toUpperCase();
	        result += getCodiceControllo(result);
	        return result;
    	}catch(Exception e){
    		return ERROR;
    	}
    }

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getCodiceCatasto(ClientSessionContext csc, ClienteModel model){

		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ComuneModel comune = model.getComuneNascita();
			DAOQueryResultModel qres = 	dao.executeQueryAccess("getCodiceCatasto",comune);
			StringType codiceCatasto = (StringType)qres.getSingleResult();
			if(codiceCatasto == null || codiceCatasto.isNull())
				return "";
			
			return codiceCatasto.toString();
			
		}catch(Exception e){
			LOG.error(e);
			return "";
		}catch(DAOException daoe){
			LOG.error(daoe);
			return "";
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
    private static String pulisci(String s){
        String s1 = "";
        for(int i = 0; i < s.length(); i++){
            switch(s.charAt(i)){
	            case 224: 
	                s1 = s1 + "a";
	                break;
	            case 232: 
	                s1 = s1 + "e";
	                break;
	            case 233: 
	                s1 = s1 + "e";
	                break;
	            case 236: 
	                s1 = s1 + "i";
	                break;
	            case 242: 
	                s1 = s1 + "o";
	                break;
	            case 249: 
	                s1 = s1 + "u";
	                break;
	            case 32: // ' '
	            case 39: // '\''
	                break;
	            default:
	                s1 = s1 + s.charAt(i);
	                break;
            }
    	}
        for(int j = 0; j < s1.length(); j++){
            if(ALFABETO.indexOf(s1.charAt(j)) < 0)
                return "";
        }
        return s1;
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
    private static boolean isVocal(char c){
        return VOCALI.indexOf(c) >= 0;
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceCognome(String s){
        String s1 = "";
        for(int i = 0; i < s.length(); i++){
            if(!isVocal(s.charAt(i)))
                s1 = s1 + s.charAt(i);
        }
        if(s1.length() < 3){
            for(int j = 0; j < s.length(); j++){
                if(isVocal(s.charAt(j)))
                    s1 = s1 + s.charAt(j);
            }
            if(s1.length() < 3)
                s1 = s1 + "xxx";
        }
        return s1.substring(0, 3);
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceNome(String s){
        String s1 = "";
        for(int i = 0; i < s.length(); i++){
            if(!isVocal(s.charAt(i)))
                s1 = s1 + s.charAt(i);
        }
        if(s1.length() > 3)
            return "" + s1.charAt(0) + s1.charAt(2) + s1.charAt(3);
        if(s1.length() == 3)
            return s1;
        for(int j = 0; j < s.length(); j++){
            if(isVocal(s.charAt(j)))
                s1 = s1 + s.charAt(j);
        }
        if(s1.length() < 3)
            s1 = s1 + "xxx";
        return s1.substring(0, 3);
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceData(DateType dataDiNascita, String sesso){
    	int aa = Integer.parseInt(dataDiNascita.getAA().toString());
    	int mm = Integer.parseInt(dataDiNascita.getMM().toString());
    	int gg = Integer.parseInt(dataDiNascita.getGG().toString());
		Calendar calendar = Calendar.getInstance();
		calendar.set(aa,mm-1,gg);
    	
        int i = calendar.get(1);
        int j = calendar.get(2);
        int k = calendar.get(5);
        i %= 100;
        String s;
        if(i < 10)
            s = "0" + String.valueOf(i);
        else
            s = "" + String.valueOf(i);
        if(sesso.equalsIgnoreCase("F"))
            k += 40;
        String s1;
        if(k < 10)
            s1 = "0" + String.valueOf(k);
        else
            s1 = String.valueOf(k);
        if(k < 10){
        	if(i < 10)
                return "0" + String.valueOf(i) + CODICI_MESE[j] + "0" + String.valueOf(k);
            return "" + String.valueOf(i) + CODICI_MESE[j] + "0" + String.valueOf(k);
        }else
            return s + CODICI_MESE[j] + s1;
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
    private static char getCodiceControllo(String s){
        int i = 0;
        int j = 0;
        for(int k = 0; k <= 14; k += 2){
            char c = s.charAt(k);
            for(int i1 = 0; i1 < CARATTERI.length; i1++){
                if(CARATTERI[i1] == c)
                    j += CODICI_DISPARI[i1];
            }
        }
        for(int l = 1; l <= 13; l += 2){
            char c1 = s.charAt(l);
            for(int j1 = 0; j1 < CARATTERI.length; j1++){
                if(CARATTERI[j1] == c1)
                    i += CODICI_PARI[j1];
            }
        }
        return CODICI_CONTROLLO[(i + j) % 26];
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}
}
