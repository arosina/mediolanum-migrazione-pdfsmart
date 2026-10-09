package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;

import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.model.ConcurrencyModel;
import prgm.pdfwebforms.model.PdfModel;

/* *********************************************************************************************
 * Inizia il processo di compilazione di un basket
 * **********************************************************************************************/
public class StartBasketCompilation extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
				
		try{
			
            PdfModel basketInput = (PdfModel)dataModel;
			Basket basket = basketInput.getBasket();
			
			if(basket.getBasketElements().size() == 0)
				throw new CommandException("\n\nIl basket ["+basket.getIdBasket()+"] non contiene dispositive\n\n");	
			
			PdfModel firstPdf = Basket.firstDataEntryDispoPdf(userSessionContext, this, basket);
			ConcurrencyModel cm = new ConcurrencyModel();
			cm.setIdCarrello(new IntegerType(basket.getIdBasket()));
			new DAOObject(userSessionContext.getClientSessionContext(), PdfInstanceFacadeBean.DAO_XML_NAME).executeTableLoadAccess("concurrencyBasketData", cm);
			basket.setDataOraUltimaModifica(cm.getDataOraUltimaModifica());
			basket.setCodUtenteUltimaModifica(cm.getCodUtenteUltimaModifica());
			return firstPdf;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

}
