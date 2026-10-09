package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ClearIndirizzoDomicilio extends AbstractElencoAttributiBusinessCommand {

	private static final long serialVersionUID = 1L;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
			
		ClienteModel model = (ClienteModel)dataModel;
		
		IndirizzoModel domicilio = model.getDomicilio().getIndirizzo();
		domicilio.setCodNazione(new StringType(Costanti.COD_NAZIONE_ITALIA));
		domicilio.setCap(new StringType());
		domicilio.setComune(new StringType());
		domicilio.setComuneEstero(new StringType());
		domicilio.setProvincia(new StringType());
		domicilio.setCodComune(new StringType());
		domicilio.setToponimoIndirizzo(new StringType());
		domicilio.setDescrizioneIndirizzo(new StringType());
		domicilio.setNumeroCivico(new StringType());
		domicilio.setPresso(new StringType());

		setForwardDisplay(0,false);
		return model;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
