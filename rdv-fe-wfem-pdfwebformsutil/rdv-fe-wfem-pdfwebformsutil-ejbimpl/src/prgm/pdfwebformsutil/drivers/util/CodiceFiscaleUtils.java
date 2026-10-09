package prgm.pdfwebformsutil.drivers.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class CodiceFiscaleUtils {
	
	private static final String ALFABETO = "abcdefghijklmnopqrstuvwxyz";
    private static final String VOCALI = "aeiou";
    
    private static final char[] CODICI_MESE = {
    											'a', 'b', 'c', 'd', 'e', 'h', 'l', 'm', 'p', 'r', 
		    									's', 't'
    								   		  };
    
    private static final char[] CARATTERI = {
									        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
									        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
									        'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', 
									        '4', '5', '6', '7', '8', '9'
    						 				};
    
    private static final int[] CODICI_DISPARI = {
										        1, 0, 5, 7, 9, 13, 15, 17, 19, 21, 
										        2, 4, 18, 20, 11, 3, 6, 8, 12, 14, 
										        16, 10, 22, 25, 24, 23, 1, 0, 5, 7, 
										        9, 13, 15, 17, 19, 21
    							 				};
    
    private static final int[] CODICI_PARI = 	{
										        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
										        10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 
										        20, 21, 22, 23, 24, 25, 0, 1, 2, 3, 
										        4, 5, 6, 7, 8, 9
	    										};
    
    private static final char[] CODICI_CONTROLLO = 	{
											        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 
											        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 
											        'U', 'V', 'W', 'X', 'Y', 'Z'
    												};
    private static final String ERROR = "";
    
    public static final String SESSO_FEMMINA = "F";
    public static final String SESSO_MASCHIO = "M";
    public static final StringType COMUNE_NASCITA_ESTERO = new StringType("ESTERO");
    
    /***********************************************************************************************/
    /***********************************************************************************************/
    private CodiceFiscaleUtils() {}
    /**************************************************************************************************/
	/**************************************************************************************************/
    public static boolean checkCodiceFiscale(StringType codiceFiscale) throws Exception{		 
    	if (codiceFiscale == null) {			
			return false;
		} else if (codiceFiscale.isNull()) {
			codiceFiscale.addTypeError("Non è stato possibile verificare il codice fiscale.");
			return false;
		} else if (codiceFiscale.toString().length() != 16) {
			codiceFiscale.addTypeError("Formato errato.");
			return false;
		}
		
		StringBuilder regex = new StringBuilder();
		regex.append("^(?i)(?:[A-Z][AEIOU][AEIOUX]|[AEIOU]X{2}");
		regex.append("|[B-DF-HJ-NP-TV-Z]{2}[A-Z]){2}(?:[\\dLMNP-V]{2}(?:[A-EHLMPR-T](?:[04LQ][1-9MNP-V]|[15MR][\\dLMNP-V]|[26NS][0-8LMNP-U])");
		regex.append("|[DHPS][37PT][0L]|[ACELMRT][37PT][01LM]|[AC-EHLMPR-T][26NS][9V])");
		regex.append("|(?:[02468LNQSU][048LQU]|[13579MPRTV][26NS])");
		regex.append("B[26NS][9V])(?:[A-MZ][1-9MNP-V][\\dLMNP-V]{2}|[A-M][0L](?:[1-9MNP-V][\\dLMNP-V]|[0L][1-9MNP-V]))[A-Z]$");		
		if (!Pattern.matches(regex.toString(), codiceFiscale.toString().toUpperCase())) {
			codiceFiscale.addTypeError("Formato errato.");
			return false;
		}
		
		return true;
	}
    /**************************************************************************************************/
	/**************************************************************************************************/
    public static boolean checkCoerenzaDatiCodiceFiscale(ClientSessionContext csc, StringType cognome, StringType nome, StringType sesso, DateType dataNascita, StringType luogoNascita, StringType provinciaNascita, StringType codiceFiscale) throws Exception {
		StringType codiceComune;
								
		if (cognome.isNull()
				|| nome.isNull()
				|| sesso.isNull()
				|| dataNascita.isNull()
				|| luogoNascita.isNull()
				|| provinciaNascita.isNull()
				|| codiceFiscale.isNull()) {
			codiceFiscale.addTypeError("Non è possibile verificare il codice fiscale in quanto i dati inseriti non sono completi.");
			return false;
		}
		
		MapCommandDataModel inputModel = new MapCommandDataModel();
		inputModel.addProperty("comune", luogoNascita);
		inputModel.addProperty("provincia", provinciaNascita);
		
		try {
			DAOObject dao = new DAOObject(csc, "PdfWebFormsUtil.CodiceFiscale");
			ListType elencoComuni = dao.executeQueryAccess("loadCodiceComune", inputModel).getResult();
			if (elencoComuni.size() != 1) {
				codiceFiscale.addTypeError("Non e' stato possibile identificare il codice comune. Verificare i dati inseriti.");
				return false;
			}
			
			MapCommandDataModel comune = (MapCommandDataModel)elencoComuni.get(0);
			codiceComune = (StringType)comune.readProperty("codiceComune");
			
		} catch (DAOException daoEx) {
			throw new Exception(daoEx.getMessage());
		}
		
		Soggetto soggetto = new Soggetto();
		soggetto.setCognome(cognome.toString().toUpperCase());
		soggetto.setNome(nome.toString().toUpperCase());
		soggetto.setDataNascita(dataNascita.dateValue());
    	soggetto.setSesso(sesso.toString().toUpperCase());
    	soggetto.setCodiceCatastoComuneNascita(codiceComune.toString());

		String codiceFiscaleCalcolato = calcolaCodiceFiscale(soggetto);
		
		if (!codiceFiscaleCalcolato.equalsIgnoreCase(normalizzaCodFiscaleOmocodia(codiceFiscale.toString()))) {			 
			codiceFiscale.addTypeError("Il codice fiscale non è coerente con i dati inseriti.");
			return false;
		}
		return true;
	}
    
    public static String normalizzaCodFiscaleOmocodia(String codiceFiscale) {
    	List<String> omocodia = Arrays.asList(new String[]{"L", "M", "N", "P", "Q", "R", "S", "T", "U", "V"});
    	String comune = codiceFiscale.substring(12, 15);
    	StringBuilder sbComuneNormalizzato = new StringBuilder();
    	    	
    	for (char c : comune.toCharArray())
        {
            if (Character.isDigit(c)) { 
            	sbComuneNormalizzato.append(c);
            }
            else {
            	sbComuneNormalizzato.append(String.valueOf(omocodia.indexOf(String.valueOf(c))));
            }            	
        }   	
    	String result = Util.concat(codiceFiscale.substring(0, 12), sbComuneNormalizzato.toString());
    	
    	return Util.concat(result, getCodiceControllo(result)); 
    }
    
    /**************************************************************************************************/
	/**************************************************************************************************/
    public static String calcolaCodiceFiscale(Soggetto soggetto ) {
    	
    	try{
	    	String cognome = soggetto.getCognome();
	    	String nome = soggetto.getNome();
	    	Date dataDiNascita = soggetto.getDataNascita();
	    	String sesso = soggetto.getSesso();
	    	String codiceCatasto = soggetto.getCodiceCatastoComuneNascita();
	    		    	
	        cognome = pulisci(cognome.toLowerCase());
	        nome = pulisci(nome.toLowerCase());
	
	        String result = String.format("%s%s%s%s", getCodiceCognome(cognome).toUpperCase()
	        								, getCodiceNome(nome).toUpperCase()
	        								, getCodiceData(dataDiNascita,sesso).toUpperCase()
	        								, codiceCatasto.toUpperCase());
	        
	        return result.concat(String.valueOf(getCodiceControllo(result)));
    	}catch(Exception e) {
    		return ERROR;
    	}
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static String pulisci(String s) {
        StringBuilder s1 = new StringBuilder();
        for(int i = 0; i < s.length(); i++) {
            switch(s.charAt(i)) {
	            case 224: 
	                s1.append('a');
	                break;
	            case 232: 
	            	s1.append('e');
	                break;
	            case 233: 
	            	s1.append('e');
	                break;
	            case 236: 
	            	s1.append('i');
	                break;
	            case 242: 
	            	s1.append('o');
	                break;
	            case 249: 
	            	s1.append('u');
	                break;
	            case 32: // ' '
	            case 39: // '\''
	                break;
	            default:
	            	s1.append(s.charAt(i));
	                break;
            }
    	}
        for(int j = 0; j < s1.length(); j++) {
            if(ALFABETO.indexOf(s1.charAt(j)) < 0) {
                return "";
            }
        }
        return s1.toString();
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static boolean isVocal(char c) {
        return VOCALI.indexOf(c) >= 0;
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceCognome(String s) {
    	StringBuilder s1 = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if(!isVocal(s.charAt(i))) {
                s1.append(s.charAt(i));
            }
        }
        if(s1.length() < 3) {
            for(int j = 0; j < s.length(); j++) {
                if(isVocal(s.charAt(j))) {
                    s1.append(s.charAt(j));
                }
            }
            if(s1.length() < 3) {
                s1.append("xxx");
            }
        }
        return s1.substring(0, 3);
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceNome(String s) {
    	StringBuilder s1 = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if(!isVocal(s.charAt(i))) {
                s1.append(s.charAt(i));
            }
        }
        if(s1.length() > 3) {
            return "" + s1.charAt(0) + s1.charAt(2) + s1.charAt(3);
        }
        if(s1.length() == 3) {
            return s1.toString();
        }
        for(int j = 0; j < s.length(); j++){
            if(isVocal(s.charAt(j))) {
                s1.append(s.charAt(j));
            }
        }
        if(s1.length() < 3) {
            s1.append("xxx");
        }
        return s1.substring(0, 3);
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static String getCodiceData(Date dataDiNascita, String sesso) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(dataDiNascita);
    	
        int i = calendar.get(1);
        int j = calendar.get(2);
        int k = calendar.get(5);
        i %= 100;
        String s;
        if(i < 10) {
            s = String.format("0%d", i);
        } else {
            s = String.valueOf(i);
        }
        if(sesso.equalsIgnoreCase(SESSO_FEMMINA)) {
            k += 40;
        }
        String s1;
        if(k < 10) {
            s1 = String.format("0%d", k);
        } else {
            s1 = String.valueOf(k);
        }
        if(k < 10) { 
        	if(i < 10) {
                return String.format("0%d%s0%d", i, CODICI_MESE[j], k);
        	} else {
        		return String.format("%d%s0%d", i, CODICI_MESE[j], k);
        	}
        } else {
            return String.format("%s%s%s", s, CODICI_MESE[j], s1);
        }
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
    private static char getCodiceControllo(String s) {
        int i = 0;
        int j = 0;
        for(int k = 0; k <= 14; k += 2){
            char c = s.charAt(k);
            for(int i1 = 0; i1 < CARATTERI.length; i1++) {
                if(CARATTERI[i1] == c) {
                    j += CODICI_DISPARI[i1];
                }
            }
        }
        for(int l = 1; l <= 13; l += 2) {
            char c1 = s.charAt(l);
            for(int j1 = 0; j1 < CARATTERI.length; j1++) {
                if(CARATTERI[j1] == c1) {
                    i += CODICI_PARI[j1];
                }
            }
        }
        return CODICI_CONTROLLO[(i + j) % 26];
    }
    /**************************************************************************************************/
	/**************************************************************************************************/
	public static class Soggetto {
		private String nome = "";
		private String cognome = "";
		private String sesso = "";
		private Date dataNascita;
		private String codiceCatastoComuneNascita = "";
		private String luogoNascita = "";
		private String codiceFiscale = "";
		
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getCognome() {
			return cognome;
		}
		public void setCognome(String cognome) {
			this.cognome = cognome;
		}
		public String getSesso() {
			return sesso;
		}
		public void setSesso(String sesso) {
			this.sesso = sesso;
		}
		public Date getDataNascita() {
			return dataNascita;
		}
		public void setDataNascita(Date dataNascita) {
			this.dataNascita = dataNascita;
		}
		public String getCodiceCatastoComuneNascita() {
			return codiceCatastoComuneNascita;
		}
		public void setCodiceCatastoComuneNascita(String codiceCatastoComuneNascita) {
			this.codiceCatastoComuneNascita = codiceCatastoComuneNascita;
		}
		public String getLuogoNascita() {
			return luogoNascita;
		}
		public void setLuogoNascita(String luogoNascita) {
			this.luogoNascita = luogoNascita;
		}
		public String getCodiceFiscale() {
			return codiceFiscale;
		}
		public void setCodiceFiscale(String codiceFiscale) {
			this.codiceFiscale = codiceFiscale;
		}		
	}
    
}
