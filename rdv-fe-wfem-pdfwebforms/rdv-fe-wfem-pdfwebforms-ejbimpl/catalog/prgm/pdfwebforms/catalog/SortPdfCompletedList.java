package prgm.pdfwebforms.catalog;

import java.util.Collections;
import java.util.Vector;

import prgm.pdfwebforms.model.PdfInstanceModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class SortPdfCompletedList extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			
			Vector list = (Vector)catalog.getPdfCompletedList().getElements();
			if(catalog.getPdfCompletedListParams().getOrderField().equals("pdfInstanceId")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						return c1.getPdfInstanceId().toString().compareTo(c2.getPdfInstanceId().toString());
					}
				});
			}else if(catalog.getPdfCompletedListParams().getOrderField().equals("pdfAnag_pdfDescr")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						return c1.getPdfAnag().getPdfCode().toString().compareTo(c2.getPdfAnag().getPdfCode().toString());
					}
				});
			}else if(catalog.getPdfCompletedListParams().getOrderField().equals("agente")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						return c1.getNominativoAgente().toString().compareTo(c2.getNominativoAgente().toString());
					}
				});
			}else if(catalog.getPdfCompletedListParams().getOrderField().equals("clienti")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						String s1 = c1.getCli1().getCognome().toString()+c1.getCli1().getNome().toString();
						String s2 = c2.getCli1().getCognome().toString()+c2.getCli1().getNome().toString();
						return s1.compareTo(s2);
					}
				});
			}else if(catalog.getPdfCompletedListParams().getOrderField().equals("pdfCompilationModeDescr")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						return c1.getPdfCompilationMode().compareTo(c2.getPdfCompilationMode());
					}
				});
			}else if(catalog.getPdfCompletedListParams().getOrderField().equals("pdfCompletionTime")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfInstanceModel c1 = (PdfInstanceModel)o1;
						PdfInstanceModel c2 = (PdfInstanceModel)o2;
						return c1.getPdfCompletionTime().compareTo(c2.getPdfCompletionTime());
					}
				});
			}
			
			if(catalog.getPdfCompletedListParams().getOrderType().equals("desc"))
				Collections.reverse(list);
			
			setNextCommandClass(PdfCatalog.class);
			return catalog;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfCatalogModel.class;
	}

}
