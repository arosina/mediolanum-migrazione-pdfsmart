package prgm.pdfwebformsutil.dummyejb;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;

/**
 * Session Bean implementation class dummyClass
 */
@Stateless(name = "DummyEjb", mappedName = "DummyEjb")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class DummyEjbBean extends ManagerObject implements DummyEjb {

    /**
     * Default constructor. 
     */
    public DummyEjbBean() {
        // TODO Auto-generated constructor stub
    }
    
    public boolean check() throws EJBException
    {
    	return true;
    }

}
