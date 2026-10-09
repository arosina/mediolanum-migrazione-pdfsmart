package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class IndirizziModel extends AbstractElencoAttributiModel {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public IndirizziModel(){
		setElencoAttributi( new ListType(IndirizzoModel.class));
	}
}
