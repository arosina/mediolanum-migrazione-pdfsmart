package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.dummyejb;

import javax.ejb.Remote;

import com.atosorigin.wfem.backend.Manager;

/**************************************************************************************************
 * @author: Bellegotti
 **************************************************************************************************/
 
@Remote
public interface DummyEjb extends Manager{
	public boolean check();
}
