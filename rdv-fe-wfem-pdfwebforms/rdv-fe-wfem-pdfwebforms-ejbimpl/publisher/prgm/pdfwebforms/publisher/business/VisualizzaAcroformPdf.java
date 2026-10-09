package prgm.pdfwebforms.publisher.business;

import java.util.ArrayList;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.core.PdfActionInfos;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VisualizzaAcroformPdf extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
			PdfAnagModel pdf = (PdfAnagModel)Tools.cloneObject(model.getPdfAnag());
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableLoadAccess("pdfFile_WORK",pdf);
			PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdf);
		
			String html = acroformAsHtml(pdf.getPdfContent().getFileContent());
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("text/html");
			resp.setContent(html.getBytes());
			resp.setContentLength(html.length());
			setGenericCommandResponse(resp);
			return null;
			
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Throwable t){
			CommandException ce = new CommandException(t.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfConfigurationModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String acroformAsHtml(byte[] pdf){
		try{
			String css = ".text{\n"+
							"font-family: Arial;\n"+
							"font-size: 8pt;\n"+
							"color: #1A458F;\n"+
						"}\n";

			StringBuffer res = new StringBuffer("<html>");
			res.append("<head>");
			res.append("<style>");
			res.append(css);
			res.append("</style>");
			res.append("</head>");
			res.append("<body>");

			PdfInfos pdfInfos = PdfEngine.inspectPdfInfos(pdf);
			
			// FIELDS
			ArrayList<PdfFieldInfos> fields = new ArrayList<PdfFieldInfos>();
			for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){
				if(!fields.contains(fi)){
					fields.add(fi);
					if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON || 
						(fi.fieldType == AcroFields.FIELD_TYPE_COMBO || fi.fieldType == AcroFields.FIELD_TYPE_LIST)){
						for(PdfFieldInfos fi2 : pdfInfos.getFieldInfos()){
							if(fi.pdfFieldName.equals(fi2.pdfFieldName) && fi2 != fi)
								fi.expValue += ", "+fi2.expValue;
						}
					}
				}
			}
			res.append("<p class='text'><b>Campi</b></p>");
			res.append("<table width='100%' class='text' border=1>");
			res.append("<tr>");
				res.append("<td style='background-color: #f0f0f0;'>Nome campo pdf</td>");
				res.append("<td style='background-color: #f0f0f0;'>Nome campo dati</td>");
				res.append("<td style='background-color: #f0f0f0;'>Tipo dato</td>");
				res.append("<td style='background-color: #f0f0f0;'>Tipo campo</td>");
				
				res.append("<td style='background-color: #f0f0f0;'>Nascosto</td>");
				res.append("<td style='background-color: #f0f0f0;'>In sola lettura</td>");
				res.append("<td style='background-color: #f0f0f0;'>Obbligatorio</td>");
				res.append("<td style='background-color: #f0f0f0;'>Piu' copie</td>");
				
				res.append("<td style='background-color: #f0f0f0;'>Valore di default</td>");
				res.append("<td style='background-color: #f0f0f0;'>Possibili valori</td>");
			res.append("</tr>");
			for(PdfFieldInfos fi : fields){
				res.append("<tr>");
					res.append("<td valign='top'>"+fi.pdfFieldName+"</td>");
					if(fi.pdfFieldName.equals(fi.htmlFieldName))
						res.append("<td valign='top'>&nbsp;</td>");
					else
						res.append("<td valign='top'>"+fi.htmlFieldName+"</td>");
					res.append("<td valign='top'>"+fi.dataType+"</td>");
					String ftype = "&nbsp;";
					switch(fi.fieldType) {
						case AcroFields.FIELD_TYPE_SIGNATURE:
							ftype="Firma";
							break;
						case AcroFields.FIELD_TYPE_RADIOBUTTON:
							ftype="Radio button";
							break;
						case AcroFields.FIELD_TYPE_CHECKBOX:
							ftype="Checkbox";
							break;
						case AcroFields.FIELD_TYPE_TEXT:
							ftype="Testo"+(fi.multiline?" multilinea":"");
							break;
						case AcroFields.FIELD_TYPE_COMBO:
						case AcroFields.FIELD_TYPE_LIST:
							ftype="Combobox";
							break;
					}
					res.append("<td valign='top'>"+ftype+"</td>");
					
					res.append("<td valign='top'>"+(fi.hidden?"Si":"&nbsp;")+"</td>");
					res.append("<td valign='top'>"+(fi.readonly?"Si":"&nbsp;")+"</td>");
					res.append("<td valign='top'>"+(fi.mandatory?"Si":"&nbsp;")+"</td>");
					res.append("<td valign='top'>"+(fi.hasClone?"Si":"&nbsp;")+"</td>");
									
					res.append("<td valign='top'>"+(fi.fieldType == AcroFields.FIELD_TYPE_TEXT && fi.defValue.length() > 0 ? fi.defValue : "&nbsp;")+"</td>");
					if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON){
						res.append("<td valign='top'>"+fi.expValue+"</td>");
					}else if((fi.fieldType == AcroFields.FIELD_TYPE_COMBO || fi.fieldType == AcroFields.FIELD_TYPE_LIST)){
						CodDescDataList dl = fi.codDescDataList;
						if(dl != null){
							String expV = "";
							for(int i=0;i<dl.getCodDescCount();i++)
								expV += dl.getCodDesc(i).getCod()+", ";
							if(expV.length() > 0)
								expV = expV.substring(0,expV.length()-2);
							else
								expV = "&nbsp;";
							res.append("<td valign='top'>"+expV+"</td>");
						}else{
							res.append("<td valign='top'>&nbsp;</td>");
						}
					}else{
						res.append("<td valign='top'>&nbsp;</td>");
					}
				res.append("</tr>");
			}
			res.append("</table>");
			
			// ACTIONS
			ArrayList<PdfActionInfos> actions = new ArrayList<PdfActionInfos>();
			for(PdfActionInfos ai : pdfInfos.getActionInfos()){
				if(!actions.contains(ai)){
					actions.add(ai);
				}
			}
			res.append("<p class='text'><b>Azioni</b></p>");
			res.append("<table width='100%' class='text' border=1>");
			res.append("<tr>");
				res.append("<td style='background-color: #f0f0f0;'>Nome pdf</td>");
				res.append("<td style='background-color: #f0f0f0;'>Nome html</td>");
			res.append("</tr>");
			for(PdfActionInfos ai : actions){
				res.append("<tr>");
					res.append("<td valign='top'>"+ai.pdfFieldName+"</td>");
					if(ai.pdfFieldName.equals(ai.htmlFieldName))
						res.append("<td valign='top'>&nbsp;</td>");
					else
						res.append("<td valign='top'>"+ai.htmlFieldName+"</td>");
				res.append("</tr>");
			}
			res.append("</table>");
			
			res.append("</body>");
			res.append("</html>");
			return res.toString();
			
		}catch(Throwable t){
			return t.toString();
		}
	}

}
