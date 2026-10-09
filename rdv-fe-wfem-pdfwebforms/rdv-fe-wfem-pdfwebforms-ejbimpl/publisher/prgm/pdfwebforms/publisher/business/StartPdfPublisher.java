package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.catalog.PdfCatalogModel;
import prgm.pdfwebforms.publisher.display.PdfPublisher;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartPdfPublisher extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		if(csc.isCliente())
			throw new CommandException(PdfUtil.FUNZIONE_NON_DISPONIBILE);      
		 
		PdfPublisherModel model = (PdfPublisherModel)dataModel;
		if(!model.isProfiloUtenteAmministratore() && model.getArea().isNull())
			model.setArea(new StringType(PdfCatalogModel.AREA_CATALOGO_MODULI));
		
		CodDescDataList dl = new CodDescDataList();
		CodDescData d = null;
		d = new CodDescData(); d.setCod("ISNULL"); d.setDescr("Non attiva"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("D"); d.setDescr("Attiva solo in firma digitale"); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("S"); d.setDescr("Attiva con tutte le modalita' di firma"); dl.addCodDescData(d);
		model.getRicercaPdfParam().addCodDescField("callSrvDispositivaBMED", dl);
		
		setNextCommandClass(PdfPublisher.class);
		return dataModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfPublisherModel.class;
	}

}
