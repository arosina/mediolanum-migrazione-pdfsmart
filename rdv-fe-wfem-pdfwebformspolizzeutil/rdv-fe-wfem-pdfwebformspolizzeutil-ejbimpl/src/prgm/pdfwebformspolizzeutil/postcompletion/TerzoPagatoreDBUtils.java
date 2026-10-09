package prgm.pdfwebformspolizzeutil.postcompletion;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfPersonInstanceModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.utils.PostCompletionCewUtils;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispTerzoPagatoreInput;

public class TerzoPagatoreDBUtils {

	/*************************************************************************/
	/*************************************************************************/
	public static InrDispTerzoPagatoreInput buildInrDispTerzoPagatoreInput(PdfInstanceModel pdfInstance, PdfDataModel dataModel, String suffissoCampiDaGestire) throws Exception {

		InrDispTerzoPagatoreInput input = new InrDispTerzoPagatoreInput();
		input.setPdfInstanceId(new StringType(dataModel.getPdfInstanceId().toString()));
		input.setCodAgente(new StringType(pdfInstance.getCodAgente().toString()));
		PdfPersonInstanceModel cliente = pdfInstance.getCli1();
		input.setNdgCliente(new StringType(cliente.getNdg().toString()));
		input.setCodPotenzialeCliente(new StringType(cliente.getCodPotenziale().toString()));
		StringType relazione = (StringType)dataModel.read("tipoRelazione"+suffissoCampiDaGestire);
		if(relazione != null) {
			input.setRelazioneContraenteTerzoPagatore(new StringType(relazione.toString()));
		}
		StringType altraRelazione = (StringType)dataModel.read("descrizioneTipoRelazione"+suffissoCampiDaGestire);
		if(altraRelazione != null) {
			input.setAltraRelazione(new StringType(altraRelazione.toString()));
		}

		return input;
	}

	/*************************************************************************/
	/*************************************************************************/
	public static InrDispTerzoPagatoreInput buildInrDispTerzoPagatoreInput(ClientSessionContext csc, PdfDataModel dataModel, String suffissoCampiDaGestire) throws Exception {

		InrDispTerzoPagatoreInput input = new InrDispTerzoPagatoreInput();
		input.setPdfInstanceId(new StringType(dataModel.getPdfInstanceId().toString()));
		StringType codAgente = (StringType)dataModel.read("codiceAgente");
		input.setCodAgente(new StringType(Tools.fillSx(codAgente.toString(), '0', 10)));
		StringType ndgCliente = (StringType)dataModel.read("ndgCliente1");
		if (!ndgCliente.isNull()) {
			input.setNdgCliente(new StringType(Tools.fillSx(ndgCliente.toString(), '0', 11)));
		}
		StringType codPotenziale = (StringType)dataModel.readProperty("idCensimentoCliente1");
		if (codPotenziale == null || codPotenziale.isNull()) {
			codPotenziale = PostCompletionCewUtils.loadCodPotenzialeSoggettoEffettivo(csc, ndgCliente);
		}
		if(codPotenziale != null) {
			input.setCodPotenzialeCliente(new StringType(Tools.fillSx(codPotenziale.toString(), '0', 11)));
		}
		StringType relazione = (StringType)dataModel.read("tipoRelazione"+suffissoCampiDaGestire);
		if(relazione != null) {
			input.setRelazioneContraenteTerzoPagatore(new StringType(relazione.toString()));
		}
		StringType altraRelazione = (StringType)dataModel.read("descrizioneTipoRelazione"+suffissoCampiDaGestire);
		if(altraRelazione != null) {
			input.setAltraRelazione(new StringType(altraRelazione.toString()));
		}

		return input;
	}

	/*************************************************************************/
	/*************************************************************************/
	public static InrDispInput buildInrDispInput(PdfDataModel dataModel, StringType codProdotto) {

		InrDispInput input = new InrDispInput();

		input.setCodProdotto(new StringType(codProdotto.toString()));
		input.setCodDisposizione(new StringType(dataModel.getPdfInstanceId().toString()));
		StringType codAgente = (StringType)dataModel.read("codiceAgente");
		input.setCodAgente(new StringType(Tools.fillSx(codAgente.toString(), '0', 10)));
		input.setCodRete(new StringType("P"));
		input.setTipoStatoDisposizione(new StringType("03"));
		input.setCodDivisa(new StringType("EUR"));
		input.setDispAgenteSplit(new DoubleType(0));
		DateType dataSottoscrizione = (DateType)dataModel.read("dataSottoscrizione");
		input.setDataSottoscrizione(new DateType(dataSottoscrizione.dateValue()));
		StringType numeroPolizza = (StringType)dataModel.read("numeroPolizza");
		input.setOrdine(new StringType(numeroPolizza.toString()));

		return input;
	}
}
