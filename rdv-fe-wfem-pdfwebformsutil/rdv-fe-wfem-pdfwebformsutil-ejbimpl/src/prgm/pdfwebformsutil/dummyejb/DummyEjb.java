package prgm.pdfwebformsutil.dummyejb;
import javax.ejb.Remote;

import com.atosorigin.wfem.backend.Manager;

@Remote
public interface DummyEjb extends Manager{
	public boolean check();
}
