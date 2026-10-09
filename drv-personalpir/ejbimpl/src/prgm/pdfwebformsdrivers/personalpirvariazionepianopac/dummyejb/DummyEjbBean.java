package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.dummyejb;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;

/**************************************************************************************************
 * @author: Bellegotti
 **************************************************************************************************/
 
/**
 * Session Bean implementation class dummyClass
 */
@Stateless(name = "DummyEjb", mappedName = "DummyEjb")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DummyEjbBean extends ManagerObject implements DummyEjb {    
    @Override
	public boolean check() {
    	return true;
    }
}
