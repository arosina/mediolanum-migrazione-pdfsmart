package prgm.ita.p.dac.business;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OnChangeOperazione extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		DacModel dac = (DacModel)dataModel;
		DocumentoModel doc = dac.getDocumento();		
		String tipoAlertAdeguatezza = "nessuno";
		boolean showAlertBeneficiariNominativi = false;
		
		if(!doc.getCodOperazione().isNull()){
			try{
				StringType tipoAlertReportAdeguatezzaDB = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
														"select PT_PRODOPER_F_ALERT_REP_ADEG from CEPE_PT_RETEP_PRODOPER where PT_PRODOTTO_N_PT_PRODOTTO="+doc.getCodProdotto()+" and PT_OPERAZIONE_N_PT_OPERAZIONE="+doc.getCodOperazione(), null, StringType.class).getSingleResult();
				if(tipoAlertReportAdeguatezzaDB != null){
					if(tipoAlertReportAdeguatezzaDB.equalsIgnoreCase("S") || tipoAlertReportAdeguatezzaDB.equalsIgnoreCase("1")){
						tipoAlertAdeguatezza = "RDA_MIFID";
					}else if(tipoAlertReportAdeguatezzaDB.equalsIgnoreCase("2")){
						tipoAlertAdeguatezza = "RACCOMANDAZIONE_IDD";
					}else if(tipoAlertReportAdeguatezzaDB.equalsIgnoreCase("3")){
						tipoAlertAdeguatezza = "RDA_MIFID_E_RACCOMANDAZIONE_IDD";
					}
				}
				
				StringType momCodeBeneficiariNominativi = (StringType)DAOObject.executeDynaQueryAccess(csc, 
						"CEPE", 
						"select DOMINIO_X_DESCR from INR_CE_DOMINIO where DOMINIO_C_TABELLA = 'BENEFICIARI_NOMINATIVI' and DOMINIO_C_CODICE = 'MOM_CODE' and DOMINIO_F_VALIDITA = 'S'",
						null, 
						StringType.class).getSingleResult();
				
				if(momCodeBeneficiariNominativi != null 
						&& !momCodeBeneficiariNominativi.isNull()) {
					String[] momCodes = momCodeBeneficiariNominativi.toString().split(",");
					String momCodeStr = "";
					for(int i=0;i<momCodes.length;i++) {
						if (momCodeStr.length() > 0)
							momCodeStr += ",";
						momCodeStr += "'"+momCodes[i]+"'";
					}
					
					BooleanType isInAmbitoBeneficiari = (BooleanType)DAOObject.executeDynaQueryAccess(csc, 
							"CEPE", 
							"SELECT CASE WHEN COUNT(*) > 0 THEN 'S' ELSE 'N' END FROM PDF_ANAG WHERE MOM_CODE IN ("+momCodeStr+") AND COD_PRODOTTO_PRIT="+doc.getCodProdotto()+" AND COD_OPERAZIONE_PRIT="+doc.getCodOperazione(),
							null, 
							BooleanType.class).getSingleResult();
					
					if (isInAmbitoBeneficiari != null
							&& isInAmbitoBeneficiari.booleanValue())
						showAlertBeneficiariNominativi = true;
				}
				
				if (!showAlertBeneficiariNominativi) {
					StringType prodOperAltriProdotti = (StringType)DAOObject.executeDynaQueryAccess(csc, 
							"CEPE", 
							"select DOMINIO_X_DESCR from INR_CE_DOMINIO where DOMINIO_C_TABELLA = 'BENEFICIARI_NOMINATIVI' and DOMINIO_C_CODICE = 'ALERT_PRIT_ALTRIPRODOTTI' and DOMINIO_F_VALIDITA = 'S'",
							null, 
							StringType.class).getSingleResult();
					String[] prodoper = prodOperAltriProdotti.toString().split(",");
					for(int i=0;i<prodoper.length;i++) {
						if (prodoper[i].equals(""+doc.getCodProdotto()+"|"+doc.getCodOperazione())) {
							showAlertBeneficiariNominativi = true;
							break;
						}
					}
				}
				
			}catch(DAOException daoe){
				LOG.error(daoe);
			}
		}
			
		String jsonObj = "{ \"tipoAlertAdeguatezza\": \""+tipoAlertAdeguatezza+"\", \"isDocumentoReportAdeguatezza\": \""+doc.isDocumentoReportAdeguatezza()+"\", \"isDocumentoRaccomandazioneIdd\": \""+doc.isDocumentoRaccomandazioneIdd()+"\", \"showAlertBeneficiariNominativi\": \""+showAlertBeneficiariNominativi+"\"  }";
		
		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("application/json");
		try{
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
		}catch(UnsupportedEncodingException uee){
			gcrm.setContent(jsonObj.toString().getBytes());
		}
		gcrm.setContentLength(jsonObj.length());
		setGenericCommandResponse(gcrm);
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacModel.class;
	}

}
