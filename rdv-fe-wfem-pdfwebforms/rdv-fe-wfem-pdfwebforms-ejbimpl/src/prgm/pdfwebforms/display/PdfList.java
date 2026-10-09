package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.model.PdfListModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfList extends DisplayCommand implements MenuCommand, GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       

            PdfListModel model = (PdfListModel)dataModel;
			model.getParams().setPdfEnvironment(model.getPdfEnvironment());
			if(!model.getAreas().isNull() && model.getParams().getAreas().isNull()){
				String[] areasArray = model.getAreas().toString().split("\\,");
				String searchedAreas = "";
				for(int i=0;i<areasArray.length;i++)
					searchedAreas += "'"+areasArray[i]+"',";
				model.getParams().setAreas(new StringType(searchedAreas.substring(0,searchedAreas.length()-1)));
			}
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfList");
			model.setPdfList(dao.executeQueryAccess("pdfList",model.getParams()).getResult());
			return model;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfListModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		PdfAnagModel pdfAnag = (PdfAnagModel)row;
		FieldStyle fs = new FieldStyle();
		if(cellPropertyName.equals("comandi")){
			fs.innerHTML  = "&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='downloadPdf(\""+pdfAnag.getPdfId()+"\");'>scarica il modulo&nbsp</span>";
			if(!pdfAnag.getPdfPubIdCorrente().isNull()){
				
				if(pdfAnag.getPdfIsStampaEnabled().booleanValue() || 
				   pdfAnag.getPdfIsCartaLiberaEnabled().booleanValue() ||
				   pdfAnag.getPdfIsCartaChimicaEnabled().booleanValue() ||
				   pdfAnag.getPdfIsFirmaDigitaleEnabled().booleanValue())
					fs.innerHTML += "<br><br>&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='compila(\""+pdfAnag.getPdfId()+"\");'>compila&nbsp</span>";
			}
		}
		
		if("pdfDescr".equals(cellPropertyName)){
			String title = pdfAnag.getTitle();
			String descr = Tools.capitalize(pdfAnag.getPdfDescr().toString());
			fs.innerHTML = "<b>"+title+"</b> - "+descr;
		}
		
		cell.setStyle(fs);
		
	}
}
