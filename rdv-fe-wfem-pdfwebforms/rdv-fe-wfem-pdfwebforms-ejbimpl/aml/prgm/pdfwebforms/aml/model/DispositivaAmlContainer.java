package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DispositivaAmlContainer {
	
	private PdfModel	pdf = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String title() {
		PdfDataModel pdfData = getPdf().getPdfData();
		return pdfData.getPdfTitle().isNull() ? getPdf().mainPdfAnag().getPdfDescr().toString() : pdfData.getPdfTitle().toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String numPropostaContratto() {
		PdfDataModel pdfData = getPdf().getPdfData();
		StringType nProp = (StringType)pdfData.read(PdfPredefinedFields.NUMERO_PROPOSTA);	
		if(nProp != null && !nProp.isNull())
			return "Proposta num: "+nProp;
		StringType nCont = (StringType)pdfData.read(PdfPredefinedFields.NUMERO_CONTRATTO);
		if(nCont != null && !nCont.isNull())
			return "Contratto num: "+nCont;
		return "";
	}

	public DispositivaAmlContainer(PdfModel pdf){
		this.pdf = pdf;
	}
	
	public PdfModel getPdf() {
		return pdf;
	}

}
