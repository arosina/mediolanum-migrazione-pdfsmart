package prgm.pdfwebforms.dataentryutil;

import java.util.concurrent.CountDownLatch;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.model.PdfPersonModel;

/*******************************************************************/
/*******************************************************************/
public class AgePersonAutocomplete extends AbstractAutocompleteCommand {

	/********************************************************************************/
	/********************************************************************************/
	class AgePersonDataLoader implements Runnable{
		
		private ClientSessionContext csc;
		private CountDownLatch latch;
		private AgePersonAutocompleteModel inPerson;
		private PdfPersonModel outPerson = new PdfPersonModel();
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public AgePersonDataLoader(ClientSessionContext csc, CountDownLatch latch, AgePersonAutocompleteModel inPerson){
			this.csc = csc;
			this.latch = latch;
			this.inPerson = inPerson;
		}
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public void run() {
			try{
				String codAgente = inPerson.getCodAgente().toString();
				outPerson = DataLoader.loadAgePersonOnAutocomplete(csc, codAgente);
				if(outPerson.isNotFound())
					outPerson.setCodAgente(new StringType(codAgente));
			}catch(Throwable t){
				t.printStackTrace();
				outPerson.setNotFound(true);
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
			AgePersonAutocompleteModel model = (AgePersonAutocompleteModel)dataModel;
			
			ListType els = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("agePersonAutocomplete",model).getResult();
			if(els.size() == 0)
				return els;
			
			CountDownLatch latch = new CountDownLatch(els.size());
			AgePersonDataLoader[] ageDataLoader = new AgePersonDataLoader[els.size()];
			for(int i=0;i<els.size();i++){
				ageDataLoader[i] = new AgePersonDataLoader(csc, latch, (AgePersonAutocompleteModel)els.get(i));  
				Thread threadPersonLoader = new Thread(ageDataLoader[i]);
				threadPersonLoader.start();
			}
			latch.await();
	
			ListType result = new ListType();
			for(int i=0;i<ageDataLoader.length;i++)
				result.add(ageDataLoader[i].outPerson);	
			return result;
			
		}catch(Exception e){
			throw new DAOException(e.toString());
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun Family Banker risponde al valore inserito";
	}
	
    /********************************************************************************/
    /********************************************************************************/
	@Override
    protected String getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName){
		PdfPersonModel el = (PdfPersonModel)dataModel;
		StringBuffer jsonObj = new StringBuffer();
		jsonObj.append(el.createJsonData(autocompleteFieldName));
		return jsonObj.toString();
    }
 
	/********************************************************************************/
    /********************************************************************************/
	@Override
    public Class getInputViewClass() {
        return AgePersonAutocompleteModel.class;
    }

}