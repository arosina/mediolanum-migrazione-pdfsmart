package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.backend.PdfLoader;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractDriver {
	
	private ClientSessionContext csc;
	private PdfModel pdf;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initInstance(ClientSessionContext csc, PdfModel pdf){
		this.csc = csc;
		this.pdf = pdf;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean existOtherPdf(String pdfCodeOrMomCode) throws Exception{
		if(!pdf.isMultiPdf())
			return false;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return true;
			}
		}		
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel getOtherPdfAnag(String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return null;
		
		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return (PdfAnagModel)pdf.getPdfAnags().get(i);
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel getOtherPdfData(String pdfCodeOrMomCode) {
		if(!pdf.isMultiPdf())
			return null;

		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				return pdfDataElement;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean addOtherPdf(String otherPdfCodeOrMomCode, PdfDataModel otherPdfData) throws Exception{
		return addOtherPdf(otherPdfCodeOrMomCode, otherPdfData, -1);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean addOtherPdf(String otherPdfCodeOrMomCode, PdfDataModel otherPdfData, int position) throws Exception{
		return addOtherPdf(otherPdfCodeOrMomCode, otherPdfData, position, false);
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean addOtherPdf(String otherPdfCodeOrMomCode, PdfDataModel otherPdfData, int position, boolean acceptDuplicatedCode) throws Exception{
		if(pdf.isOperatoreMOM())
			return true;

		try{
			
			if(!acceptDuplicatedCode){
				if(existOtherPdf(otherPdfCodeOrMomCode))
					removeOtherPdf(otherPdfCodeOrMomCode);
			}
			
			PdfDataModel pdfData = pdf.getPdfData();
			
			otherPdfData.setPdfCode(new StringType(otherPdfCodeOrMomCode));
			if(!PdfInstanceFacadeBean.loadPdfKey(csc, otherPdfData, pdf))
				return false;

			PdfAnagModel otherPdfAnag = PdfLoader.loadAndInitPdfData(csc, pdf, otherPdfData, false);
			
			if(!pdf.isMultiPdf()){
				PdfDataModel firstPdfData = (PdfDataModel)Tools.cloneObject(pdfData);
				firstPdfData.setPdfIndex(new IntegerType(0));
				pdfData.getPdfs().add(firstPdfData);
				
				otherPdfData.setPdfIndex(new IntegerType(pdfData.getPdfs().size()));
				pdfData.getPdfs().add(otherPdfData);
				pdf.getPdfAnags().add(otherPdfAnag);
			}else{
				if(position == 0 || position > pdfData.getPdfs().size()) // La posizione 0 è sempre del modulo principale. Se posizione è oltre la disponibilità di spazio il pdf viene accodato
					position = -1;
				if(position >= 1){
					otherPdfData.setPdfIndex(new IntegerType(position));
					for(int i=position; i < pdfData.getPdfs().size(); i++){
						PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
						pdfDataElement.setPdfIndex(new IntegerType(i+1));
					}
					pdfData.getPdfs().getElements().add(position, otherPdfData);
					pdf.getPdfAnags().add(position, otherPdfAnag);
				}else{
					otherPdfData.setPdfIndex(new IntegerType(pdfData.getPdfs().size()));
					pdfData.getPdfs().add(otherPdfData);
					pdf.getPdfAnags().add(otherPdfAnag);
				}
			}
			

			otherPdfData.setPdfEnvironment(pdfData.getPdfEnvironment());
			otherPdfData.setSignAll(pdfData.getSignAll());
			otherPdfData.setCompilationModes(pdfData.getCompilationModes());
			otherPdfData.setInputCompilationModes(new StringType(pdfData.getCompilationModes().toString()));
			otherPdfData.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));
			otherPdfData.setCodAgeImpersonato(new StringType(pdfData.getCodAgeImpersonato().toString()));
			otherPdfData.setIdReportAdeguatezza(new StringType(pdfData.getIdReportAdeguatezza().toString()));
			return true;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean removeOtherPdf(String pdfCodeOrMomCode) throws Exception{
		if(pdf.isOperatoreMOM())
			return true;

		if(!pdf.isMultiPdf())
			return false;

		PdfDataModel pdfData = pdf.getPdfData();
		
		boolean found = false;
		int position=0;
		for(;position<pdfData.getPdfs().size();position++){
			PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(position);
			if(pdfDataElement.getPdfCode().equals(pdfCodeOrMomCode) || pdfDataElement.getPdfMomCode().equals(pdfCodeOrMomCode)){
				found = true;
				break;
			}
		}
		
		if(!found)
			return false;
		
		if(position == 0)
			return false;
		
		pdfData.getPdfs().remove(position);
		pdf.getPdfAnags().remove(position);
		
		if(pdfData.getPdfs().size() == 1){
			pdfData.getPdfs().clear();
		}else{
			for(int i=position; i < pdfData.getPdfs().size(); i++){
				PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
				pdfDataElement.setPdfIndex(new IntegerType(i));
			}
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean removeOtherPdf(int position) throws Exception{
		if(pdf.isOperatoreMOM())
			return true;

		if(!pdf.isMultiPdf())
			return false;

		PdfDataModel pdfData = pdf.getPdfData();
	
		if(position == 0)
			return false;
	
		pdfData.getPdfs().remove(position);
		pdf.getPdfAnags().remove(position);
	
		if(pdfData.getPdfs().size() == 1){
			pdfData.getPdfs().clear();
		}else{
			for(int i=position; i < pdfData.getPdfs().size(); i++){
				PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
				pdfDataElement.setPdfIndex(new IntegerType(i));
			}
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getPdfTitle(){
		return pdf.getPdfData().getPdfTitle().isNull()?pdf.getPdfAnag().getPdfCode()+" "+pdf.getPdfAnag().getPdfDescr():pdf.getPdfData().getPdfTitle().toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setPdfTitle(String pdfTitle){
		pdf.getPdfData().setPdfTitle(new StringType(pdfTitle));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected PdfModel getPdf() {
		return pdf;
	}
}
