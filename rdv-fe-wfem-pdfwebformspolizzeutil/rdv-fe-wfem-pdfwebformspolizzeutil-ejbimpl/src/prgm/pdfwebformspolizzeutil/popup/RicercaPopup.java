package prgm.pdfwebformspolizzeutil.popup;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformspolizzeutil.dao.BeneficiariDaoAccess;
import prgm.pdfwebformspolizzeutil.dao.RicercaDaoAccess;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;

public class RicercaPopup extends DisplayCommand implements GridDecorator {

	

	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)
			throws CommandException {
		try {
			RicercaPopupModel model = (RicercaPopupModel) dataModel;
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			if (!model.isPrimaAttivazione())  {
				// eseguo query
				RicercaPopupModel ricercaPopupModel = new RicercaPopupModel();
				ricercaPopupModel.setCodiceFiscale(model.getCodiceFiscale());
				ricercaPopupModel.setCodiceCliente(model.getCodiceCliente());				
				ricercaPopupModel.setCodiceAgente(model.getCodiceAgente());
				ricercaPopupModel.setRagioneSociale(model.getRagioneSociale());
				ricercaPopupModel.setCodAgeImpersonato(model.getCodAgeImpersonato());
				ricercaPopupModel.setCodRuoloImpersonato(model.getCodRuoloImpersonato());
				ricercaPopupModel.setEscludiXxx(model.getEscludiXxx());
				RicercaDaoAccess daoAccess = new RicercaDaoAccess(csc);
				ListType result = daoAccess.findList(ricercaPopupModel);
				model.setElencoRicerca(result);
			}
			return model;

		} catch (DAOException daoE) {
			LOG.error(daoE);
			throw new CommandException(daoE.toString());
		} catch (Exception e) {
			LOG.error(e);
			throw new CommandException(e.toString());
		}
	}

	@Override
	public Class getInputViewClass() {
		return RicercaPopupModel.class;
	}

	public void onNewCell(String listPropertyName, String cellPropertyName, CommandDataModel row, AbstractType cell,
			int rowIndex, int cellIndex) {
		// TODO Auto-generated method stub
		
	}

}
