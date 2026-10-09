package prgm.ita.anagraficaclienti.questionari.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***********************************************************************************************/
/***********************************************************************************************/
public class MappaturaTitoliDiStudio {

	static Map<String, Integer> titoliStudio = new HashMap<String, Integer>();
	static{
		titoliStudio.put("D",1);
		
		titoliStudio.put("5",2);
		titoliStudio.put("B",2);
		titoliStudio.put("C",2);		
		titoliStudio.put("E",2);
		titoliStudio.put("F",2);
		titoliStudio.put("G",2);
		titoliStudio.put("H",2);
		
		titoliStudio.put("4",3);
		titoliStudio.put("7",3);
		titoliStudio.put("8",3);
		titoliStudio.put("9",3);
		titoliStudio.put("A",3);

		titoliStudio.put("3",4);
		
		titoliStudio.put("1",5);
		titoliStudio.put("2",5);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int fromMedToCedacri(String inputCodMed){
		Integer result = titoliStudio.get(inputCodMed);
		return result == null ? -1 : result.intValue();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ArrayList<String> fromCedacriToMed(int inputCodCedacri){
		ArrayList<String> result = new ArrayList<String>();
		for(String codMed : titoliStudio.keySet()){
			Integer codCedacri = titoliStudio.get(codMed);
			if(codCedacri != null && codCedacri == inputCodCedacri)
				result.add(codMed);
		}		
		return result;
	}
}
