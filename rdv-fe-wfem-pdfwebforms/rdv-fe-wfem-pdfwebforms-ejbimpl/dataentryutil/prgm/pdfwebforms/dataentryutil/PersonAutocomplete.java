package prgm.pdfwebforms.dataentryutil;

import java.util.concurrent.CountDownLatch;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.model.PdfPersonModel;

/*******************************************************************/
/*******************************************************************/
public class PersonAutocomplete extends AbstractAutocompleteCommand {

	/********************************************************************************/
	/********************************************************************************/
	class PersonDataLoader implements Runnable{
		
		private ClientSessionContext csc;
		private CountDownLatch latch;
		private PersonAutocompleteModel inPerson;
		private PdfPersonModel outPerson = new PdfPersonModel();
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public PersonDataLoader(ClientSessionContext csc, CountDownLatch latch, PersonAutocompleteModel inPerson){
			this.csc = csc;
			this.latch = latch;
			this.inPerson = inPerson;
		}
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		public void run() {
			try{
				String codCliente = inPerson.getNdg().isNull() ? inPerson.getIdCensimento().toString() : inPerson.getNdg().toString();
				outPerson = DataLoader.loadPersonOnAutocomplete(csc, codCliente, inPerson.getCodAgente().toString(), inPerson.getEscludiVariazioni().booleanValue());
			}catch(Throwable t){
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
			
			PersonAutocompleteModel model = (PersonAutocompleteModel)dataModel;

			ListType els = new ListType();
			if(!model.getCodAgente().isNull()){
				model.setCodAgente(new StringType(Tools.fillSx(model.getCodAgente().toString(), '0', 10)));
				els = new DAOObject(csc,"PdfWebForms.PdfDataentryUtil").executeQueryAccess("personAutocomplete",model).getResult();
			}
			if(els.size() == 0)
				return els;
			
			CountDownLatch latch = new CountDownLatch(els.size());
			PersonDataLoader[] personDataLoader = new PersonDataLoader[els.size()];
			for(int i=0;i<els.size();i++){
				PersonAutocompleteModel p = (PersonAutocompleteModel)els.get(i);
				p.setEscludiVariazioni(model.getEscludiVariazioni());
				personDataLoader[i] = new PersonDataLoader(csc, latch, p);  
				Thread threadPersonLoader = new Thread(personDataLoader[i]);
				threadPersonLoader.start();
			}
			latch.await();
	
			ListType result = new ListType();
			for(int i=0;i<personDataLoader.length;i++)
				result.add(personDataLoader[i].outPerson);	
			return result;
			
		}catch(Exception e){
			throw new DAOException(e.toString());
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	@Override
	protected String getNoElementsIndicator(String autocompleteFieldName){
		return "Nessun cliente risponde al valore inserito";
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
        return PersonAutocompleteModel.class;
    }
}