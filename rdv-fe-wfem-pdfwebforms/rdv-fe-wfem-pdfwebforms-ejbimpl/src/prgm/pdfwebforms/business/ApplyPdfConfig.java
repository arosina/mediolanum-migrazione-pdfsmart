package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;

import prgm.pdfwebforms.model.PdfConfigModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApplyPdfConfig extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			if(!csc.isSede())
				throw new CommandException("Comando non disponibile per l'utente collegato");		
		
			PdfConfigModel model = (PdfConfigModel)dataModel;
			if(!model.isConfigLoaded())
				throw new CommandException("Comando eseguito impropriamente");
			
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfWebForms");
			
			if(model.getPraticheDigitaliDisabilitaCallSrvDispositiva().getValore().equals("S")){
				try{
					dao.executeTableUpdateAccess("pdfConfig", model.getPraticheDigitaliDisabilitaCallSrvDispositiva());
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("pdfConfig", model.getPraticheDigitaliDisabilitaCallSrvDispositiva());
				}
			}else{
				try{
					dao.executeTableDeleteAccess("pdfConfig", model.getPraticheDigitaliDisabilitaCallSrvDispositiva());
				}catch(NoRowsAffected nra){}
			}
			
			if(model.getPraticheDigitaliDisabilitaSemaforoMom().getValore().equals("S")){
				try{
					dao.executeTableUpdateAccess("pdfConfig", model.getPraticheDigitaliDisabilitaSemaforoMom());
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("pdfConfig", model.getPraticheDigitaliDisabilitaSemaforoMom());
				}
			}else{
				try{
					dao.executeTableDeleteAccess("pdfConfig", model.getPraticheDigitaliDisabilitaSemaforoMom());
				}catch(NoRowsAffected nra){}
			}
			
			model.setApplyedMsg("La configurazione e' stata correttamente applicata");
			setForwardDisplay(new Integer(0));
			return model;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigModel.class;
	}

}
