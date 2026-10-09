package prgm.ita.anagraficaclienti.questionari.print;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.PrintCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioDatiStampaModel;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

/*******************************************************************/
/*******************************************************************/
public class PersonaFisicaPCP extends PrintCommand {

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		QuestionarioModel model = (QuestionarioModel)dataModel;
		
		// Inizializza i campi dinamici delle risposte al questionario
		model.initDatiPerStampa();
		
		model.getDatiStampa().addProperty("profiloInvestitore"+model.getCliente().getCodProfiloDiInvestimento(),	new StringType("X"));
		model.getDatiStampa().addProperty("orizzonteTemporale"+model.getOrizzonteTemporaleCedacriCE(),	new StringType("X"));
		model.getDatiStampa().addProperty("espfina"+model.getCliente().getEspfina(),								new StringType("X"));
		
		QuestionarioDatiStampaModel datiStampa = model.getDatiStampa();
		datiStampa.setDataCompilazione(Tools.today());
		datiStampa.setOraCompilazione(new StringType(Tools.now().getHH()+":"+Tools.now().getMI()));
		
		return model;
	}

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public Class getInputViewClass() {
		return QuestionarioModel.class;
	}

}

