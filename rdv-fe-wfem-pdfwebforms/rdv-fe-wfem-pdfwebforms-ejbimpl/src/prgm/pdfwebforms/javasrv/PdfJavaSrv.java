package prgm.pdfwebforms.javasrv;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.business.PrintPdf;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.display.PdfPreviewContainer;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class PdfJavaSrv {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class Field{
		public String  name = "";
		public String  value = "";
		
		public Field(String name, String value){
			this.name = name;
			this.value = value;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class Pdf{
		public String  						pdfCode = "";
		public ArrayList<PdfJavaSrv.Field>	fields = new ArrayList<PdfJavaSrv.Field>();
		
		public Pdf(String pdfCode){
			this.pdfCode = pdfCode;
		}
		
		public void addField(Field f){
			fields.add(f);
		}
		
		protected PdfDataModel beanToModel(){
			PdfDataModel result = new PdfDataModel();
			result.setPdfCode(new StringType(pdfCode));
			for(PdfJavaSrv.Field field : fields){
				result.addProperty(field.name, new StringType(field.value));
			}
			return result;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class GeneratePdfInput{
		public String  						pdfTitle = "";
		public String  						codAgente = "";
		public ArrayList<PdfJavaSrv.Pdf>	pdfs = new ArrayList<PdfJavaSrv.Pdf>();
		
		public GeneratePdfInput(String pdfTitle, String codAgente){
			this.pdfTitle = pdfTitle;
			this.codAgente = codAgente;
		}
		
		public void addPdf(Pdf pdf){
			pdfs.add(pdf);
		}
		
		protected PdfDataModel beanToModel(){
			if(pdfs.size() == 1){
				PdfDataModel pdfData = pdfs.get(0).beanToModel();
				pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente));				
				pdfData.setPdfTitle(new StringType(pdfTitle));
				return pdfData;
			}
			
			PdfDataModel mainPdfData = new PdfDataModel();
			mainPdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente));				
			mainPdfData.setPdfTitle(new StringType(pdfTitle));
			for(Pdf pdf : pdfs){
				PdfDataModel pdfData = pdf.beanToModel();
				mainPdfData.getPdfs().add(pdfData);
			}
			return mainPdfData;
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public static byte[] generatePdf(ClientSessionContext csc, GeneratePdfInput input) throws Exception{
		
		UserSessionContext usc = new UserSessionContext();
		csc = (ClientSessionContext)Tools.cloneObject(csc);
		csc.setCurrentLinkedUserCode(input.codAgente);
		usc.setClientSessionContext(csc);
		
		PdfDataModel pdfData = input.beanToModel();
		
		PdfModel pdfModel = (PdfModel)new PrintPdf().execute(usc, pdfData);
		if(!pdfModel.getFirstDisplayClass().getName().equals(PdfPreviewContainer.class.getName()))
			throw new Exception("Errore nella generazione del pdf");

		return PdfEngine.compilePdfFields(usc.getClientSessionContext(), pdfModel, true, false, "PREVIEW");
		
	}
	
}
