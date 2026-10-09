package prgm.ita.p.dac.facade;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class FacadeLoader {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static Facade getFacade(ClientSessionContext csc, Class facadeClass) throws Exception{
		Class bean = Class.forName(facadeClass.getName()+"Bean");
		Facade facade = (Facade)bean.newInstance();
		return facade;
	}
	
}
