package prgm.pdfwebforms.aml;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.aml.model.RelazioniModel;
import prgm.pdfwebforms.aml.model.SoggettoRelazioneModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RelazioneOnChangeTipo extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		PdfModel pdf = (PdfModel)dataModel;
		RelazioniModel relazioni = pdf.getAmlModel().getRelazioni();		
		int idxSoggettoSelezionato = relazioni.getIdxSoggettoSelezionato().intValue();
		SoggettoRelazioneModel sogg = (SoggettoRelazioneModel)relazioni.getElencoSoggetti().get(idxSoggettoSelezionato);
		sogg.getDescrTipoRelazioneContraente().resetTypeErrors();
		setForwardDisplay(Integer.valueOf(0));
		return dataModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
