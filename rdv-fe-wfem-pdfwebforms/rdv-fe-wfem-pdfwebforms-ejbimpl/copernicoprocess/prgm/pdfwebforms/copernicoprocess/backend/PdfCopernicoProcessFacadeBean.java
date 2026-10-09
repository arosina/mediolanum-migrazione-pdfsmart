package prgm.pdfwebforms.copernicoprocess.backend;

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
import com.atosorigin.wfem.util.SmsSender;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.AbtractPdfProcessFacadeBean;
import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.CodRuoliImpersonati;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfCopernicoProcessFacadeBean extends AbtractPdfProcessFacadeBean implements PdfCopernicoProcessFacade {
  
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel sendToCliPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws EJBException {

		if(pdf.isTestMode()){
			pdf.setPdfTestContent(pdfContent);
			return pdf;
		}
		
		try{
			
			PdfDataModel pdfData = pdf.getPdfData();
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));
			
			if(!pdf.getPdfData().getIsVolatile().booleanValue()){
				
				super.freezePdfInstance(csc, pdf, pdfInstance, "sendToCliPdfInstance", PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE, pdfContent);
				if(pdf.hasCommandErrors())
					return pdf;
				
				DAOObject dao = new DAOObject(csc,PdfInstanceFacadeBean.DAO_XML_NAME);
				if(Basket.isLastDispoPdf(pdf)){
					if(pdf.isInBasket())
						dao.executeTableUpdateAccess("freezeSendToCliBasket", pdfInstance);
					else
						dao.executeTableUpdateAccess("freezeSendToCliPdfInstance", pdfInstance);
				}
			}
			
			if(pdf.isProdottoCopernicoSmart() && Basket.isLastDispoPdf(pdf)){
				PdfPersonModel firstPerson = Basket.getFirstDispoPdf(pdf).firstFilledPerson();
				inviaNotificaSms(csc, pdf, firstPerson);
				inviaNotificaApp(csc, pdf, firstPerson); // Ripristinato con rfc #161385
			}
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inviaNotificaSms(ClientSessionContext csc, PdfModel pdf, PdfPersonModel firstPerson) {
		try{
			if(firstPerson != null && firstPerson.getSignData() != null && !firstPerson.getSignData().getNumeroCelluarePrimario().isNull()){
				String numeroCell = firstPerson.getSignData().getNumeroCelluarePrimario().toString().replace("_", "");
				String smsMessage = "Il tuo Family Banker ti ha inviato una proposta. Accedi al sito www.bmedonline.it per scoprirla subito.";
				if(pdf.isProtectionSpecialistInHub())
					smsMessage = "Il tuo Family Protection Specialist, in accordo con il tuo Family Banker, ti ha inviato una proposta. Accedi al sito www.bmedonline.it per scoprirla subito.";
				else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.BC))
					smsMessage = "Il tuo Banker Consultant, in accordo con il tuo Family Banker, ti ha inviato una proposta. Accedi al sito www.bmedonline.it per scoprirla subito.";
				else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.TWP))
					smsMessage = "Il tuo Team Wealth Advisor ti ha inviato una proposta. Accedi al sito www.bmedonline.it per scoprirla subito.";
				new SmsSender().sendSMS(numeroCell,smsMessage);
			}
		}catch(Exception e) {
			// Do nothing
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inviaNotificaApp(ClientSessionContext csc, PdfModel pdf, PdfPersonModel firstPerson) {
		try {
			if(firstPerson != null && !firstPerson.getNdg().isNull()){
				MapCommandDataModel input = new MapCommandDataModel();
				String corpo = "Il tuo Family Banker ti ha inviato una proposta di contratto. Accedi all'app per visualizzarla e accettarla."; 
				if(pdf.isProtectionSpecialistInHub())
					corpo = "Il tuo Family Protection Specialist, in accordo con il tuo Family Banker, ti ha inviato una proposta di contratto. Accedi all'app per visualizzarla e accettarla."; 
				else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.BC))
					corpo = "Il tuo Banker Consultant, in accordo con il tuo Family Banker, ti ha inviato una proposta. Accedi all'app per visualizzarla e accettarla.";
				else if(pdf.getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.TWP))
					corpo = "Il tuo Team Wealth Advisor ti ha inviato una proposta di contratto. Accedi all'app per visualizzarla e accettarla.";
				input.addProperty("codiceCliente", new StringType(Tools.fillSx(firstPerson.getNdg().toString(), '0', 11)));
				input.addProperty("corpo", new StringType(corpo));
				new DAOObject(csc,"PdfWebForms.PdfCopernicoProcess").executeOSBAccess("inviaNotificaApp", input);
			}
		}catch(DAOException daoe) {
			// Do nothing
		}catch(Exception e) {
			// Do nothing
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel acceptPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws EJBException {

		try{

			PdfDataModel pdfData = pdf.getPdfData();
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));

			
			super.freezePdfInstance(csc, pdf, pdfInstance, "acceptPdfInstance", PdfInstanceModel.STATO_COMPLETATO, pdfContent);

			if(!pdf.hasCommandErrors()){
				
				// Se tutto ok accodo la milestone
				PdfPersonModel person = pdf.firstFilledPerson();
				if( person != null && !person.getNdg().isNull() &&
				   !pdfInstance.getPdfAnag().getPdfCodProdottoPrit().isNull() && !pdfInstance.getPdfAnag().getPdfCodOperazionePrit().isNull()){
					try{

						DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfCopernicoProcess");

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
			
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
}
