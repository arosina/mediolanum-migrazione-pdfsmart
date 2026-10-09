package prgm.pdfwebforms.signprocess.backend;

import java.util.UUID;

import javax.ejb.EJBException;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.backend.AbtractPdfProcessFacadeBean;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.CodRuoliImpersonati;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfSignProcessFacadeBean extends AbtractPdfProcessFacadeBean implements PdfSignProcessFacade {
  
	public static final String DAO_XML_NAME = "PdfWebForms.PdfSignProcess";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel signPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws EJBException {

		if(pdf.isTestMode()){
			pdf.setPdfTestContent(pdfContent);
			return pdf;
		}
		
		try{
			
			PdfDataModel pdfData = pdf.getPdfData();
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));
			
			if(!pdf.getPdfData().getIsVolatile().booleanValue()){
				
				super.freezePdfInstance(csc, pdf, pdfInstance, "signPdfInstance", PdfInstanceModel.STATO_COMPLETATO, pdfContent);
				
				if(!pdf.hasCommandErrors()){
					
					// Se tutto ok accodo la milestone
					PdfPersonModel person = pdf.firstFilledPerson();
					if( person != null && !person.getNdg().isNull() &&
					   !pdfInstance.getPdfAnag().getPdfCodProdottoPrit().isNull() && !pdfInstance.getPdfAnag().getPdfCodOperazionePrit().isNull()){
						try{

							DAOObject dao = new DAOObject(csc, DAO_XML_NAME);

							MapCommandDataModel milestone = new MapCommandDataModel();
							milestone.addProperty("pdfCodProdottoPrit", new StringType(pdfInstance.getPdfAnag().getPdfCodProdottoPrit().toString()));
							milestone.addProperty("pdfCodOperazionePrit", new StringType(pdfInstance.getPdfAnag().getPdfCodOperazionePrit().toString()));
							milestone.addProperty("pdfCodProdottoMilestone", new IntegerType());
							milestone.addProperty("pdfCodOperazioneMilestone", new IntegerType());
							milestone.addProperty("tag1", new StringType());
							milestone.addProperty("tag2", new StringType());
							
							String vendente = "Family Banker";
							if(pdf.isProtectionSpecialistInHub())
								vendente = "Family Protection Specialist";
							else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.BC))
								vendente = "Banker Consultant";
							else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.TWP))
								vendente = "Team Wealth Advisor";
							milestone.addProperty("vendente", new StringType(vendente));
							
							DAOQueryResultModel qRes = dao.executeQueryAccess("loadTagMilestone", milestone);
							if(qRes.getResult().size() > 0){
								DoubleType importo = (pdf.mainPdfData().read(PdfPredefinedFields.IMPORTO)==null ? new DoubleType():new DoubleType(pdf.mainPdfData().read(PdfPredefinedFields.IMPORTO).toString()));
								StringType descr = pdf.mainPdfAnag().getPdfDescr();
								if(!pdf.getPdfData().getPdfTitle().isNull())
									descr = pdf.getPdfData().getPdfTitle();
								milestone.addProperty("importo", importo);
								milestone.addProperty("descr", descr);							
								milestone.addProperty("uuid", new StringType(UUID.randomUUID().toString()));
								milestone.addProperty("timestamp", new StringType(""+new java.util.Date().getTime()));
								milestone.addProperty("ndg", pdf.firstFilledPerson().getNdg());
								milestone.addProperty("pdfInstanceId", pdf.getPdfData().getPdfInstanceId());
								dao.executeOSBAccess("accodaMilestone", milestone);						
							}
						}catch(DAOException daoe){
							daoe.printStackTrace();
						}catch(Throwable t){
							t.printStackTrace();
						}
					}
					
				}
			}			
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
}
