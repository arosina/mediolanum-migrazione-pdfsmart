package prgm.pdfwebforms.catalog;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfContextUtils;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartCatalog extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel catalog = (PdfCatalogModel)dataModel;
			
			PdfContextUtils.initContext(csc, catalog.getPdfListParams());
			PdfContextUtils.initContext(csc, catalog.getPdfDraftListParams());
			PdfContextUtils.initContext(csc, catalog.getPdfCompletedListParams());
			
			if(!catalog.getPdfListParams().getCodiceProdotto().isNull()){
				catalog.setFiltered(true);
				catalog.getPdfDraftListParams().setCodiceProdotto(new IntegerType(catalog.getPdfListParams().getCodiceProdotto().intValue()));
				catalog.getPdfCompletedListParams().setCodiceProdotto(new IntegerType(catalog.getPdfListParams().getCodiceProdotto().intValue()));
			}
			
			catalog.getPdfListParams().setOrderField(new StringType("pdfDescr"));
			catalog.getPdfListParams().setOrderType(new StringType("asc"));
			
			catalog.getPdfDraftListParams().setOrderField(new StringType("pdfCreationTime"));
			catalog.getPdfDraftListParams().setOrderType(new StringType("desc"));

			catalog.getPdfCompletedListParams().setOrderField(new StringType("pdfCompletionTime"));
			catalog.getPdfCompletedListParams().setOrderType(new StringType("desc"));

			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
			PdfListLoader.loadPdfList(csc, catalog, true);
			catalog.setPdfDraftList(dao.executeQueryAccess("pdfDraftList",catalog.getPdfDraftListParams()).getResult());
			
            if(catalog.getPdfListParams().getCodiceSegmento().isNull())
            	catalog.getPdfListParams().addCodDescField("codiceProdotto", "PRODOTTI");
            else
            	catalog.getPdfListParams().addCodDescField("codiceProdotto", "PRODOTTI_SEGMENTO");
			dao.fillCodDesc(catalog.getPdfListParams(),false);
			
			if(catalog.isFiltered()){
				try{
					String filterTitle = catalog.getPdfListParams().getCodDescDataList("codiceProdotto").getCodDesc(catalog.getPdfListParams().getCodiceProdotto().toString()).getDescr();
					catalog.setFilterTitle(filterTitle);
				}catch(Throwable t){
					catalog.setFilterTitle("Filtro non applicabile");
				}
			}
			
			setNextCommandClass(PdfCatalog.class);
			return catalog;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
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
