package prgm.ita.anagraficaclienti.model;

import java.util.ArrayList;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractSectionModel extends CommandDataModel {
	
	protected ArrayList<String> frontendPropName = new ArrayList<String>();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean containErrors(ClienteModel cliente) throws Exception {
		for(String propName : frontendPropName) {
			try {
				AbstractType prop = (AbstractType)Tools.getPropertyValue(cliente,propName);
				if(prop.hasTypeErrors() || prop.hasTypeWarnings())
					return true;
			}catch(Exception e) {
				// continue
			}
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<String> getFrontendPropName() {
		return frontendPropName;
	}

}
