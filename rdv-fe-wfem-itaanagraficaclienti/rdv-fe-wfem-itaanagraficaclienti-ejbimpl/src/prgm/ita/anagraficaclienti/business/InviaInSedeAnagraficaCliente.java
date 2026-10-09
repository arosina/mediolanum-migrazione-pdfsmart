package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.ReminderAddendumAVR;
import prgm.ita.anagraficaclienti.flussofatca.AbstractNavigatore;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiFatcaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class InviaInSedeAnagraficaCliente extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteModel model = (ClienteModel)dataModel;
			
			setForwardDisplay(new Integer(0));
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			
			model = facade.controllaCliente(csc,model);
			if(model.hasCommandErrors())
				return model;

			ReminderAddendumAVR.initReminder(csc, model);
			if(model.hasCommandErrors())
				return model;
			
			if(!model.getIsFlussoFatcaTerminato().booleanValue()){
				
				// Nei cansimenti salvo una bozza per non perdere le modifiche nel caso in cui i servizi fatca abbiano problemi
				if(model.getIsPotenziale().booleanValue()) {
					model = facade.salvaBozzaCliente(csc, model);
					if(model.getConcurrencyViolationFoundedWidth() != null)
						return model;
				}
				
				model.setDatiFatca(new DatiFatcaModel());
				
				DAOOSBResultModel osbRes = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti").executeOSBAccess("getFatcaInformation",model);
				if(osbRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
					model.getDatiApplicativi().setErroreCentrale("Errore ritornato dal servizio per recuperare il FATCA STATUS:<br>"+osbRes.getWsCallData().getMessage());
					return model;
				}
				
				DatiFatcaModel datiFatca = model.getDatiFatca();
				if(datiFatca.getIndizioForteNonSuperabile().isNull() && datiFatca.getIndizioForteSuperabile().isNull() && datiFatca.getIndizioDebole().isNull()){
					model.getDatiApplicativi().setErroreCentrale("Il servizio per recuperare il FATCA STATUS non ha ritornato dati validi");
					return model;
				}
				
				String fatcaNavigatorName = null;
				if(model.getIsPotenziale().booleanValue()){
					fatcaNavigatorName = "ClientePotenziale";
				}else{
					fatcaNavigatorName = (String)AbstractNavigatore.statiFatca.get(datiFatca.getResultingFatcaStatus().isNull()?"O":datiFatca.getResultingFatcaStatus().toString());
				}
				if(fatcaNavigatorName != null){
					datiFatca.setFatcaNavigatorName("Navigatore"+fatcaNavigatorName);
					AbstractNavigatore navig = (AbstractNavigatore)Class.forName("prgm.ita.anagraficaclienti.flussofatca."+datiFatca.getFatcaNavigatorName()).newInstance();
					String firstFatcaPopupName = navig.init(model);
					if(firstFatcaPopupName != null){
						model.setFirstFatcaPopupName(firstFatcaPopupName);
						return model;
					}
				}
			}
			
			model.setIsFlussoFatcaTerminato(new BooleanType(true));
			
			model.setSaltaControlliAllInvioInSede(true);
			model = facade.inviaInSedeCliente(csc,model);
			model.setSaltaControlliAllInvioInSede(false);

			if(!Tools.containsTypeWarningOrErrors(model)) {
				model.getDatiApplicativi().setShowCorniceCentraleBlu(true);
				model.setModality(Template.READ_MODALITY);
			}
				
			return model;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'inviare in sede il cliente: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'inviare in sede il cliente: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
