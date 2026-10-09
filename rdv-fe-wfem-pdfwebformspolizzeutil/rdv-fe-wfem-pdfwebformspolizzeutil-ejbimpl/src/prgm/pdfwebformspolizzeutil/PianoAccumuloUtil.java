package prgm.pdfwebformspolizzeutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebformspolizzeutil.dao.RecuperaPianoAccumuloDaoAccess;
import prgm.pdfwebformspolizzeutil.model.PianoAccumuloModel;
import prgm.pdfwebformspolizzeutil.model.RecuperaPianoAccumuloModel;

public class PianoAccumuloUtil {
	private PianoAccumuloUtil() {		
	}
	
	public static PianoAccumuloModel recuperaSituazioniPianiAccumulo(ClientSessionContext csc, RecuperaPianoAccumuloModel model) throws Exception {
		RecuperaPianoAccumuloDaoAccess dao = new RecuperaPianoAccumuloDaoAccess(csc, model);
		try {
			dao.executeSync();
			return model.getResult();
		} catch (DAOException e) {
			throw new Exception(e);
		}						
	}
}
