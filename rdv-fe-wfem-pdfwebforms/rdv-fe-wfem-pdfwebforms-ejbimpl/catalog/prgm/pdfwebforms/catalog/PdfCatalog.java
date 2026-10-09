package prgm.pdfwebforms.catalog;

import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfPersonInstanceModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfCatalog extends DisplayCommand implements GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			if(catalog.getRefreshDraft().booleanValue()){
				catalog.setRefreshDraft(new BooleanType());
				DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
				catalog.setPdfDraftList(dao.executeQueryAccess("pdfDraftList",catalog.getPdfDraftListParams()).getResult());
			}
			return dataModel;
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfCatalogModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		
		if(listPropertyName.equals("pdfList")){					/* PDF LIST */
			
			PdfAnagModel pdfAnag = (PdfAnagModel)row;
	
			FieldStyle fs = new FieldStyle();
			
			if(cellPropertyName.equals("elencoProdotti")){
				fs.innerHTML = pdfAnag.getPdfModulo().htmlProdotti();
			}

			String imgDim = "height='28px' width='28px'";
			String tdImgWidth = "28px";
			
			if(cellPropertyName.equals("comandi")){
				fs.innerHTML = "<table><tr>";
				if(!pdfAnag.getPdfPubIdCorrente().isNull()){
					
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'>";
					if(pdfAnag.getPdfIsCopernicoEnabled().booleanValue())
						fs.innerHTML += "<img title='Copernico' "+imgDim+" src='/PdfWebForms/catalog/images/modSottCopernico.png' style='cursor:pointer;' onclick='pdfList.compila(\""+pdfAnag.getPdfId()+"\");'>";
					fs.innerHTML += "</td>";
					
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'>";
					if(pdfAnag.getPdfIsFirmaDigitaleEnabled().booleanValue())
						fs.innerHTML += "<img title='Firma Digitale' "+imgDim+" src='/PdfWebForms/catalog/images/modSottFirmaDigitale.png' style='cursor:pointer;' onclick='pdfList.compila(\""+pdfAnag.getPdfId()+"\");'>";
					fs.innerHTML += "</td>";
					
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'>";
					if(pdfAnag.getPdfIsCartaLiberaEnabled().booleanValue() || pdfAnag.getPdfIsCartaChimicaEnabled().booleanValue())
						fs.innerHTML += "<img title='Invia in sede e stampa' "+imgDim+" src='/PdfWebForms/catalog/images/modSottInvioInSede.png' style='cursor:pointer;' onclick='pdfList.compila(\""+pdfAnag.getPdfId()+"\");'>";
					fs.innerHTML += "</td>";
					
				}else{
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'></td>";
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'></td>";
					fs.innerHTML += "<td style='width:"+tdImgWidth+";'></td>";
				}
				fs.innerHTML += "<td style='width:"+tdImgWidth+";'>";
				fs.innerHTML += 	"<img title='Scarica pdf' "+imgDim+" src='/PdfWebForms/catalog/images/downloadPdf.jpg' style='cursor:pointer;' onclick='pdfList.downloadPdf(\""+pdfAnag.getPdfId()+"\");'>";
				fs.innerHTML += "</td>";
				fs.innerHTML += "</tr></table>";
			}

			if(cellPropertyName.equals("esempioCompilazione")){
				if(!pdfAnag.getPdfCompilationExampleFileType().isNull()){
					fs.innerHTML = "<table><tr>";
					fs.innerHTML += 	"<td>";
					fs.innerHTML += 		"<img title='Esempi di compilazione' "+imgDim+" src='/PdfWebForms/catalog/images/esempioCompilazione.jpg' style='cursor:pointer;' contentType='"+pdfAnag.getPdfCompilationExampleFileType()+"' onclick='pdfList.openCompilationExample(this,\""+pdfAnag.getPdfId()+"\",\"\");'>";
					fs.innerHTML += 	"</td>";
					fs.innerHTML += "</tr></table>";
				}
			}
			
			if("pdfDescr".equals(cellPropertyName)){
				String title = pdfAnag.getTitle();
				String descr = Tools.capitalize(pdfAnag.getPdfDescr().toString());
				String hasNoteOperative = pdfAnag.getPdfModulo().getNoteOperative().isNull() ? "false" : "true";
				fs.innerHTML  = "<span style='cursor:pointer;' onclick='pdfList.dettaglioModulo(\""+pdfAnag.getPdfId()+"\",\""+title.replaceAll("\\'","\\\\x27")+"\","+hasNoteOperative+");'><b><font color='#a3cbe5'>"+title+"</font></b><br>"+descr+"</span>";
			}
			
			cell.setStyle(fs);
			
			
		}else if(listPropertyName.equals("pdfDraftList")){		/* DRAFT LIST */
			
			PdfInstanceModel pdf = (PdfInstanceModel)row;
			FieldStyle fs = new FieldStyle();
			if("agente".equals(cellPropertyName)){
				fs.innerHTML = pdf.getCodAgente().toString();
				if(!pdf.getNominativoAgente().isNull()){
					if(fs.innerHTML.length() > 0)
						fs.innerHTML += " - ";
					fs.innerHTML += Tools.capitalize(pdf.getNominativoAgente().toString());
				}
			}
			
			if("clienti".equals(cellPropertyName)){
				
				String cli1 = cognomeNome(pdf.getCli1());
				String cli2 = cognomeNome(pdf.getCli2());
				String cli3 = cognomeNome(pdf.getCli3());
				String cli4 = cognomeNome(pdf.getCli4());
				String cli5 = cognomeNome(pdf.getCli5());
				String cli6 = cognomeNome(pdf.getCli6());
				String cli7 = cognomeNome(pdf.getCli7());
				String cli8 = cognomeNome(pdf.getCli8());
				String cli9 = cognomeNome(pdf.getCli9());
				
				String clienti = cli1;
				if(clienti.length() > 0 && cli2.length() > 0)
					clienti += ", ";
				clienti += cli2;
				
				if(clienti.length() > 0 && cli3.length() > 0)
					clienti += ", ";
				clienti += cli3;
				
				if(clienti.length() > 0 && cli4.length() > 0)
					clienti += ", ";
				clienti += cli4;
				
				if(clienti.length() > 0 && cli5.length() > 0)
					clienti += ", ";
				clienti += cli5;
				
				if(clienti.length() > 0 && cli6.length() > 0)
					clienti += ", ";
				clienti += cli6;
				
				if(clienti.length() > 0 && cli7.length() > 0)
					clienti += ", ";
				clienti += cli7;
				
				if(clienti.length() > 0 && cli8.length() > 0)
					clienti += ", ";
				clienti += cli8;
				
				if(clienti.length() > 0 && cli9.length() > 0)
					clienti += ", ";
				clienti += cli9;
				
				if(clienti.length() > 0)
					fs.innerHTML = Tools.capitalize(clienti);
			}
			
			if("pdfAnag_pdfDescr".equals(cellPropertyName)){
				String title = pdf.getPdfAnag().getTitle();
				String descr = Tools.capitalize(pdf.getPdfAnag().getPdfDescr().toString());
				fs.innerHTML = "<b><font color='#a3cbe5'>"+title+"</font></b><br>"+descr;
			}
			
			if("comandi".equals(cellPropertyName)){
				fs.innerHTML   = "<p style='margin: 5px'><span style='text-decoration:underline;cursor:pointer;' onclick='pdfDraftList.apri(\""+pdf.getPdfInstanceId()+"\");'>continua</span></p>";
				fs.innerHTML  += "<p style='margin: 5px'><span style='text-decoration:underline;cursor:pointer;' onclick='pdfDraftList.cancella(\""+pdf.getPdfInstanceId()+"\");'>elimina</span></p>";
			}
			cell.setStyle(fs);
			
		}else if(listPropertyName.equals("pdfCompletedList")){		/* COMPLETED LIST */
			
			PdfInstanceModel pdf = (PdfInstanceModel)row;
			FieldStyle fs = new FieldStyle();
			if("agente".equals(cellPropertyName)){
				fs.innerHTML = pdf.getCodAgente().toString();
				if(!pdf.getNominativoAgente().isNull()){
					if(fs.innerHTML.length() > 0)
						fs.innerHTML += " - ";
					fs.innerHTML += Tools.capitalize(pdf.getNominativoAgente().toString());
				}
			}

			if("clienti".equals(cellPropertyName)){
				
				String cli1 = cognomeNome(pdf.getCli1());
				String cli2 = cognomeNome(pdf.getCli2());
				String cli3 = cognomeNome(pdf.getCli3());
				String cli4 = cognomeNome(pdf.getCli4());
				String cli5 = cognomeNome(pdf.getCli5());
				String cli6 = cognomeNome(pdf.getCli6());
				String cli7 = cognomeNome(pdf.getCli7());
				String cli8 = cognomeNome(pdf.getCli8());
				String cli9 = cognomeNome(pdf.getCli9());
				
				String clienti = cli1;
				if(clienti.length() > 0 && cli2.length() > 0)
					clienti += ", ";
				clienti += cli2;
				
				if(clienti.length() > 0 && cli3.length() > 0)
					clienti += ", ";
				clienti += cli3;
				
				if(clienti.length() > 0 && cli4.length() > 0)
					clienti += ", ";
				clienti += cli4;
				
				if(clienti.length() > 0 && cli5.length() > 0)
					clienti += ", ";
				clienti += cli5;
				
				if(clienti.length() > 0 && cli6.length() > 0)
					clienti += ", ";
				clienti += cli6;
				
				if(clienti.length() > 0 && cli7.length() > 0)
					clienti += ", ";
				clienti += cli7;
				
				if(clienti.length() > 0 && cli8.length() > 0)
					clienti += ", ";
				clienti += cli8;
				
				if(clienti.length() > 0 && cli9.length() > 0)
					clienti += ", ";
				clienti += cli9;
							
				if(clienti.length() > 0)
					fs.innerHTML = Tools.capitalize(clienti);
			}
			
			if("pdfAnag_pdfDescr".equals(cellPropertyName)){
				String title = pdf.getPdfAnag().getTitle();
				String descr = Tools.capitalize(pdf.getPdfAnag().getPdfDescr().toString());
				fs.innerHTML = "<b><font color='#a3cbe5'>"+title+"</font></b><br>"+descr;
			}
			
			cell.setStyle(fs);
			
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String cognomeNome(PdfPersonInstanceModel person){
		String res = "";
		if(!person.getCognome().isNull())
			res += person.getCognome().toString();
		if(!person.getNome().isNull())
			res += " "+person.getNome().toString();
		return Tools.capitalize(res);
	}

}
