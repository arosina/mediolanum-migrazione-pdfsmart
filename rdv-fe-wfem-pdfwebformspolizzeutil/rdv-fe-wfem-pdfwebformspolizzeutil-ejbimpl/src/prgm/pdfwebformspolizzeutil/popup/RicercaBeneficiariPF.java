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
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;

public class RicercaBeneficiariPF extends DisplayCommand implements GridDecorator {

	public void onNewCell(String listPropertyName, String cellPropertyName, CommandDataModel row, AbstractType cell,
			int rowIndex, int cellIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)
			throws CommandException {
		try {
			RicercaBeneficiariModel model = (RicercaBeneficiariModel) dataModel;
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			if (!model.isPrimaAttivazione()) {
				// eseguo query
				BeneficiarioModel beneficiarioModel = new BeneficiarioModel();
				beneficiarioModel.setCodiceFiscale(model.getCodiceFiscale());
				beneficiarioModel.setCodiceCliente(model.getCodiceCliente());
				beneficiarioModel.setTipoRicerca(new StringType("PF"));
				beneficiarioModel.setCodiceAgente(model.getCodiceAgente());
				beneficiarioModel.setCognome(model.getCognome());
				beneficiarioModel.setNome(model.getNome());
				beneficiarioModel.setCodAgeImpersonato(model.getCodAgeImpersonato());
				beneficiarioModel.setCodRuoloImpersonato(model.getCodRuoloImpersonato());
				
				BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc);
				ListType result = daoAccess.findList(beneficiarioModel);
				model.setElencoBeneficiari(result);
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
		return RicercaBeneficiariModel.class;
	}

}
