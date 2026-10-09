package prgm.pdfwebformspolizzeutil.controller;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformspolizzeutil.BeneficiariUtil;

public class TerzoPagatore {

	public static final String TIPORELAZIONE_FIELDNAME = "tipoRelazione";
	public static final String DESCRIZIONETIPORELAZIONE_FIELDNAME = "descrizioneTipoRelazione";

	private TerzoPagatore() {
		throw new IllegalStateException("Utility class");
	}

	public static void checkRelazioneTerzoPagatore(PdfDataModel dataModel, String nomeCampoTipoConto,
													String valoreTerzoPagatore, String suffissoCampiDaGestire) throws Exception {

		StringType tipoConto = (StringType)dataModel.read(nomeCampoTipoConto);
		if(tipoConto != null && tipoConto.equals(valoreTerzoPagatore)) {
			BeneficiariUtil.checkRelazione((StringType)dataModel.read(TIPORELAZIONE_FIELDNAME+suffissoCampiDaGestire),
											(StringType)dataModel.read(DESCRIZIONETIPORELAZIONE_FIELDNAME+suffissoCampiDaGestire));
		}
	}
}
