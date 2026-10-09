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
import prgm.pdfwebforms.core.PdfContextUtils;
import prgm.pdfwebforms.model.PdfInstanceListModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfPersonInstanceModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfDraftList extends DisplayCommand implements MenuCommand, GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
            if(csc.isCliente())
            	throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);       

            PdfInstanceListModel pdfList = (PdfInstanceListModel)dataModel;
			PdfContextUtils.initContext(csc, pdfList.getParams());
			if(!pdfList.getPdfEnvironments().isNull() && pdfList.getParams().getPdfEnvironment().isNull()){
				String[] pdfEnvironmentsArray = pdfList.getPdfEnvironments().toString().split("\\,");
				String searchedPdfEnviroments = "";
				for(int i=0;i<pdfEnvironmentsArray.length;i++)
					searchedPdfEnviroments += "'"+pdfEnvironmentsArray[i]+"',";
				pdfList.getParams().setPdfEnvironment(new StringType(searchedPdfEnviroments.substring(0,searchedPdfEnviroments.length()-1)));
			}
			
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfList");
			pdfList.setPdfList(dao.executeQueryAccess("pdfDraftList",pdfList.getParams()).getResult());
			return pdfList;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfInstanceListModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		
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
			fs.innerHTML = "<b>"+title+"</b> - "+descr;
		}
		
		if("comandi".equals(cellPropertyName)){
			fs.innerHTML  = " &nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='apri(\""+pdf.getPdfInstanceId()+"\");'>continua</span>";
			fs.innerHTML  += "<br><br>&nbsp;<span style='text-decoration:underline;cursor:pointer;' onclick='cancella(\""+pdf.getPdfInstanceId()+"\");'>elimina</span>";
		}
		cell.setStyle(fs);
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
