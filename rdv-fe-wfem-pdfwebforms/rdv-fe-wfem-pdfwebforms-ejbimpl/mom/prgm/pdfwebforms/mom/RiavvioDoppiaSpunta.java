package prgm.pdfwebforms.mom;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/* **********************************************************************************************
 * Riavvia la doppia spunta riportando come riga principale la seconda e cancellando la prima
 * **********************************************************************************************/
public class RiavvioDoppiaSpunta extends ValidaPdf implements MenuCommand{

	private static final String DAO_XML_NAME = "PdfWebForms.PdfMomRiavvioDoppiaSpunta";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfDataModel pdfData = (PdfDataModel)dataModel;
			
			// Leggo la situazione attuale eventualmente compresa della doppia spunta
			PdfModel pdfClone = (PdfModel)executeValidaPdf(userSessionContext, pdfData);
			if(pdfClone.getInitialErrorMsg() != null){
				pdfClone.setPdfData(pdfData);
				pdfClone.setFirstDisplayClass(PdfInitialError.class);
				return pdfClone;
			}
			
			// Se esiste la doppia spunta cancello la seconda riga
			if(!deleteClonedPdfInstanceId(csc, pdfData))
				return pdfClone;
			
			// Leggo la dispositiva master
			PdfModel pdfMaster = (PdfModel)executeValidaPdf(userSessionContext, pdfData);
			
			// Imposto i dati del clone nella master
			copyPdfClonedData(pdfMaster, pdfClone);
			
			// Salvo la situaizone "mergiata"
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			pdfMaster = facade.savePdfOnMomValidation(csc, pdfMaster);
			
			return pdfMaster;
			
		}catch(Exception | DAOException e){
			throw new CommandException(e.toString());
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean deleteClonedPdfInstanceId(ClientSessionContext csc, PdfDataModel pdfData) throws Exception, DAOException{
		
		if(pdfData.getCodDispositivaBMED().isNull())
			return false;
		
		DAOObject dao = new DAOObject(csc, DAO_XML_NAME);		
		PdfInstanceModel clonedInstance = new PdfInstanceModel();
		clonedInstance.setCodDispositivaBMED(new StringType(pdfData.getCodDispositivaBMED().toString()));
		dao.executeQueryAccess("loadClonedPdfInstanceId", clonedInstance);
		if(clonedInstance.getPdfInstanceId().isNull())
			return false;
		
		dao.executeTableDeleteAccess("deleteClonedPdfInstance", clonedInstance);
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void copyPdfClonedData(PdfModel pdfMaster, PdfModel pdfClone) throws Exception{
		PdfDataModel masterData = pdfMaster.getPdfData();
		PdfDataModel clonedData = pdfClone.getPdfData();
		if(masterData.getPdfs().size() > 0) {
			for(int i=0;i<masterData.getPdfs().size();i++) {
				PdfDataModel masterDataElement = (PdfDataModel)masterData.getPdfs().get(i);
				copyModuleClonedData(masterDataElement, clonedData);
			}
		}
		copyModuleClonedData(masterData, clonedData);
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void copyModuleClonedData(PdfDataModel masterData, PdfDataModel clonedData) throws Exception{	
		if(masterData.getCodDispositivaBMED().isNull())
			return;
		if(clonedData.getPdfs().size() > 0) {
			for(int i=0;i<clonedData.getPdfs().size();i++) {
				PdfDataModel clonedDataElement = (PdfDataModel)clonedData.getPdfs().get(i);
				if(masterData.getCodDispositivaBMED().equals(clonedDataElement.getCodDispositivaBMED())) {
					setMasterProprties(masterData, clonedDataElement);
					break;
				}
			}
		}else {
			if(masterData.getCodDispositivaBMED().equals(clonedData.getCodDispositivaBMED()))
				setMasterProprties(masterData, clonedData);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void setMasterProprties(PdfDataModel masterData, PdfDataModel clonedData) throws Exception{
		AbstractTypePropertyDescriptor[] pds = clonedData.getMappedPropertyDescriptors();
		for(int k=0; k < pds.length; k++){
			AbstractTypePropertyDescriptor pd = pds[k];
			masterData.addProperty(pd.getName(), pd.getValue());
		}
	}
}
