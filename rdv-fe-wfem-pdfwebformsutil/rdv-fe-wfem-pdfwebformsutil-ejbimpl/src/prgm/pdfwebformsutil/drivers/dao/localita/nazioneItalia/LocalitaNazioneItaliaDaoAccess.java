package prgm.pdfwebformsutil.drivers.dao.localita.nazioneItalia;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;

public class LocalitaNazioneItaliaDaoAccess extends AbstractDaoAccess{
	
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.LocalitaNazioneItalia";
	private static final String ACCESS_NAME_IS_INDIRIZZO_VALIDO = "isIndirizzoValido";
	private static final String ACCESS_NAME_LOCALITA_CATASTO = "localitaCatastoAccess";
	private static final String ACCESS_NAME_IS_TOPONIMO_INDIRIZZO_VALIDO = "isToponimoValido";

	public LocalitaNazioneItaliaDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	public LocalitaNazioneItaliaDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}
	
	
	/**
	 * @param csc
	 * @param inputDataModel
	 * @return
	 * @throws Exception
	 * @deprecated 
	 */
	@Deprecated
	public static ListType getLocalitaConCatastoNazioneItalia(ClientSessionContext csc, LocalitaNazioneItaliaModel inputDataModel) throws Exception {

		if (inputDataModel.getComune().isNull()
				|| inputDataModel.getProvincia().isNull()) {
			return new ListType(LocalitaNazioneItaliaModel.class);
		}
		
		LocalitaNazioneItaliaDaoAccess daoAccess = new LocalitaNazioneItaliaDaoAccess(csc);

		ListType localitaModel = null;
		try {
			localitaModel = daoAccess.getDaoObj().executeQueryAccess(ACCESS_NAME_LOCALITA_CATASTO, inputDataModel).getResult();
		} catch (DAOException daoe) {
			String errorMsg = "PdfDriver - Eccezione DAO nel recuperare la localita di un indirizzo italiano : " + daoe;
			throw new Exception(errorMsg);
		} catch (Exception e) {
			String errorMsg = "PdfDriver - Eccezione nel nel recuperare la localita di un indirizzo italiano : " + e;
			throw new Exception(errorMsg);
		}

		return localitaModel;
		
	}
	
	/**
	 * @param csc
	 * @param inputDataModel
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	@Deprecated
	public static boolean isIndirizzoValidoNazioneItalia(ClientSessionContext csc, LocalitaNazioneItaliaModel inputDataModel) throws Exception {
		if (inputDataModel.getComune() == null 
				|| inputDataModel.getProvincia().isNull() 
				|| inputDataModel.getCap().isNull()) {
			return false;
		}
		
		boolean result = false;
		LocalitaNazioneItaliaDaoAccess daoAccess = new LocalitaNazioneItaliaDaoAccess(csc);

		
		try {
			DAOQueryResultModel qrResult = daoAccess.getDaoObj().executeQueryAccess(ACCESS_NAME_IS_INDIRIZZO_VALIDO, inputDataModel);
			if (qrResult != null) {
				result = ((BooleanType) qrResult.getSingleResult()).booleanValue();
			}
		} catch (DAOException daoe) {
			String errorMsg = "PdfDriver - Eccezione DAO nel verificare la validita di un indirizzo italiano: " + daoe;
			throw new Exception(errorMsg);
		} catch (Exception e) {
			String errorMsg = "PdfDriver - Eccezione nel verificare la validita di un indirizzo italiano: " + e;
			throw new Exception(errorMsg);
		}
		return result;
		
	}
	
	/**
	 * @param csc
	 * @param toponimo
	 * @return
	 * @throws Exception
	 * @deprecated
	 */
	@Deprecated
	public static boolean isToponimoIndirizzoValidoNazioneItalia(ClientSessionContext csc, StringType toponimo) throws Exception {
		if (toponimo == null || toponimo.isNull()) {
			return false;
		}
		
		boolean result = false;
		LocalitaNazioneItaliaDaoAccess daoAccess = new LocalitaNazioneItaliaDaoAccess(csc);
		MapCommandDataModel inputModel = new MapCommandDataModel();
		inputModel.addProperty("toponimo", toponimo);
		
		try {
			DAOQueryResultModel qrResult = daoAccess.getDaoObj().executeQueryAccess(ACCESS_NAME_IS_TOPONIMO_INDIRIZZO_VALIDO, inputModel);
			if (qrResult != null) {
				result = ((BooleanType) qrResult.getSingleResult()).booleanValue();
			}
		} catch (DAOException daoe) {
			String errorMsg = "PdfDriver - Eccezione DAO nel verificare la validita di un toponimo di un  indirizzo italiano: " + daoe;
			throw new Exception(errorMsg);
		} catch (Exception e) {
			String errorMsg = "PdfDriver - Eccezione nel verificare la validita di un toponimo di un  indirizzo italiano: " + e;
			throw new Exception(errorMsg);
		}
		return result;
		
	}
}
