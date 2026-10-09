package prgm.pdfwebforms.dataentryutil;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfConfig;

/*******************************************************************/
/*******************************************************************/
public class ContoAutocomplete extends AbstractAutocompleteCommand {
	
	/********************************************************************************/
	/********************************************************************************/
	class ContoDataLoader implements Runnable{
		
		private ClientSessionContext csc;
		private CountDownLatch latch;
		private ContoAutocompleteModel conto;
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public ContoDataLoader(ClientSessionContext csc, CountDownLatch latch, ContoAutocompleteModel conto){
			this.csc = csc;
			this.latch = latch;
			this.conto = conto;
		}
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public void run() {
			try{
				conto.setNominativiConto(new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("nominativiContoAutocomplete",conto).getResult());
			}catch(Throwable t){
			}finally{
	    		 latch.countDown();
			}
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected ListType findElements(ClientSessionContext csc, CommandDataModel dataModel) throws DAOException {
		try{
			ListType contiCliente = new ListType();
			ContoAutocompleteModel model = (ContoAutocompleteModel)dataModel;
			if(!model.getNdgCliente().isNull()){
				// Se il tipo conto è impostato ne carico la configurazione della "where condition" prima di lanciare la query(default sono i conti correnti)
				model.setWhereTipoConto(new StringType());
				if(!model.getTipoConto().isNull()) 
					model.setWhereTipoConto(PdfConfig.getParamAsString(csc, "WHERE_CONDITION_TIPO_CONTO", model.getTipoConto().toString()));
				contiCliente = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("contoAutocomplete",model).getResult();
				if(contiCliente.size() > 0){
					CountDownLatch latch = new CountDownLatch(contiCliente.size());
					ContoDataLoader[] contoDataLoader = new ContoDataLoader[contiCliente.size()];
					for(int i=0;i<contiCliente.size();i++){
						contoDataLoader[i] = new ContoDataLoader(csc, latch, (ContoAutocompleteModel)contiCliente.get(i));  
						Thread threadContoLoader = new Thread(contoDataLoader[i]);
						threadContoLoader.start();
					}
					latch.await();
				}
			}
			return contiCliente;
		}catch(Exception e){
			throw new DAOException(e.toString());
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun conto disponibile";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
    protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		ContoAutocompleteModel el = (ContoAutocompleteModel)dataModel;
		StringBuffer jsonObj = new StringBuffer();
		jsonObj.append("\"numeroConto\":\""+el.propertyToString("numeroConto")+"\",");
		jsonObj.append("\"numeroEstesoConto\":\""+el.propertyToString("numeroEstesoConto")+"\",");
		jsonObj.append("\"ibanConto\":\""+el.propertyToString("ibanConto")+"\",");
		jsonObj.append("\"dataAperturaConto\":\""+el.propertyToString("dataAperturaConto")+"\",");
		jsonObj.append("\"annoAperturaConto\":\""+el.propertyToString("annoAperturaConto")+"\",");
		jsonObj.append("\"paeseIbanConto\":\""+el.propertyToString("paeseIbanConto")+"\",");
		jsonObj.append("\"cinEuropeoIbanConto\":\""+el.propertyToString("cinEuropeoIbanConto")+"\",");
		jsonObj.append("\"cinControlloIbanConto\":\""+el.propertyToString("cinControlloIbanConto")+"\",");
		jsonObj.append("\"abiIbanConto\":\""+el.propertyToString("abiIbanConto")+"\",");
		jsonObj.append("\"cabIbanConto\":\""+el.propertyToString("cabIbanConto")+"\",");
		jsonObj.append("\"numeroIbanConto\":\""+el.propertyToString("numeroIbanConto")+"\",");
		jsonObj.append("\"codiceRuoloConto\":\""+el.propertyToString("codiceRuoloConto")+"\",");
		jsonObj.append("\"ruoloConto\":\""+el.propertyToString("ruoloConto")+"\",");
		
		try{ jsonObj.append("\"value\":\""+el.propertyToString(autocompleteFieldName)+"\","); }catch(Exception e){}
		jsonObj.append("\"label\":\""+el.label()+"\"");
		return jsonObj.toString();
    }

    /********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return ContoAutocompleteModel.class;
    }
	
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	@Deprecated
	public ArrayList<ContoAutocompleteModel> getResult() {
		return null;
	}
	
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	// Per coesistenza con Amex. DA TOGLERE APPENA POSSIBILE!!!!
	@Deprecated
	public CommandDataModel setJsonResponse(ArrayList<ContoAutocompleteModel> elements){
		return null;
	}
	
}