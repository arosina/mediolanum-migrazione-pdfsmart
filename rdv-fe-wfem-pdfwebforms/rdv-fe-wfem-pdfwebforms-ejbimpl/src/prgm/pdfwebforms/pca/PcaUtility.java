package prgm.pdfwebforms.pca;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************/
/********************************************************************************/
public class PcaUtility {

	private static final String DAO_XML = "PdfWebForms.PdfWebFormsPca";	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PcaUtility() {
		throw new IllegalStateException("PcaUtility class");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void callCollegaPcaAReportAdeguatezza(ClientSessionContext csc, PdfModel pdf, String idReportAdeguatezza) throws Exception, DAOException{
		
		if( pdf.isTestMode() || 
			pdf.getIsSede().booleanValue() ||
			idReportAdeguatezza.isEmpty() ||
			pdf.mainPdfData().getIdPCA().isNull())
			return;

		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
		if(iddData == null || 
		   iddData.getTipoVerfica() != ProvideIddDataResponse.VERIFICA_IDD_RAMO_III || 
		   iddData.getTipoDispositiva() != ProvideIddDataResponse.TIPO_DISPOSITIVA_INIZIALE)
			return;
		
		MapCommandDataModel m = new MapCommandDataModel();
		m.addProperty("now", Tools.now());
		m.addProperty("utente", new StringType(csc.getUserCode()));
		m.addProperty("idPCA", pdf.mainPdfData().getIdPCA());
		m.addProperty("idRDA", new StringType(idReportAdeguatezza));
		new DAOObject(csc, DAO_XML).executeOSBAccess("collegaPcaAReportAdeguatezza", m);
	}	

}
