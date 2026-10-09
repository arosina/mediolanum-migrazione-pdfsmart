package prgm.pdfwebforms.catalog;

import java.util.Collections;
import java.util.Vector;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class SortPdfList extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			
			Vector list = (Vector)catalog.getPdfList().getElements();
			if(catalog.getPdfListParams().getOrderField().equals("pdfDescr")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfAnagModel c1 = (PdfAnagModel)o1;
						PdfAnagModel c2 = (PdfAnagModel)o2;
						return c1.getPdfCode().toString().compareTo(c2.getPdfCode().toString());
					}
				});
			}else if(catalog.getPdfListParams().getOrderField().equals("pdfModulo_tipoModulo")){
				Collections.sort(list,	new java.util.Comparator(){
					public int compare(Object o1, Object o2) {
						PdfAnagModel c1 = (PdfAnagModel)o1;
						PdfAnagModel c2 = (PdfAnagModel)o2;
						return c1.getPdfModulo().getTipoModulo().toString().compareTo(c2.getPdfModulo().getTipoModulo().toString());
					}
				});
			}
			
			if(catalog.getPdfListParams().getOrderType().equals("desc"))
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
