package prgm.ita.p.dac.util;

import java.util.Vector;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class GestioneBarcodeImpl implements GestioneBarcode {

	private static Vector users = new Vector();
	static{
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isGestoreBarcode(ClientSessionContext csc) throws Exception {
		String user = Tools.fillSx(csc.getUserCode(),'0',10);
		return users.contains(user);
	}

}
