package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class TelefoniModel extends AbstractElencoAttributiModel {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefoniModel(){
		setElencoAttributi( new ListType(TelefonoModel.class));
	}
}
