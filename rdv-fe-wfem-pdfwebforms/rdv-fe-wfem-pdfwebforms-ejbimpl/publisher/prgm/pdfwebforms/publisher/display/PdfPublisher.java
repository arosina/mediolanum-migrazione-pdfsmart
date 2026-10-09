package prgm.pdfwebforms.publisher.display;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPublisher extends DisplayCommand implements GridDecorator{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfPublisherModel model = (PdfPublisherModel)dataModel;
			if(model.getEseguiRicerca().booleanValue()){
				PdfAnagFacade facade = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class); 
				model = facade.searchPdf(csc,model);
			}else{
				model.setPdfList(new ListType(PdfAnagModel.class));
			}
			
			if(model.isPrimaAttivazione()){
	            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
				model = (PdfPublisherModel)f.fillCodDesc(csc,model,false);
			}
			
			if(!model.getArea().isNull()){
				CodDescDataList dl = model.getCodDescDataList("area");
				if(dl != null){
					CodDescData d = dl.getCodDesc(model.getArea().toString());
					if(d == null){
						dl = (CodDescDataList)Tools.cloneObject(dl);
						d = new CodDescData(); 
						d.setCod(model.getArea().toString());
						d.setDescr(model.getArea().toString());
						dl.addCodDescData(d);
						model.addCodDescField("area", dl);
					}
				}
			}
			return model;			
		
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfPublisherModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName,
						  CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {
		PdfAnagModel pdf = (PdfAnagModel)row;
		FieldStyle fs = new FieldStyle();
		if(cellPropertyName.equals("pdfNumPubblArch")){
			fs.innerHTML = pdf.getPdfNumPubblicazioni()+" / "+pdf.getPdfNumArchiviazioni();
		}
		if(cellPropertyName.equals("pdfPubIdCorrente")){
			if(pdf.getPdfNumPubblicazioni().intValue() == 0 && pdf.getPdfNumArchiviazioni().intValue() == 0 && pdf.getIsOnWork().booleanValue())
				fs.innerHTML = "0";
		}
		if(cellPropertyName.equals("isModuloScaduto")){
			if(pdf.getIsFromCatalogoModuli().booleanValue()){
				if(!pdf.getPdfModulo().getDataFineValidita().isNull() && pdf.getPdfModulo().getDataFineValidita().compareTo(Tools.today()) < 0)  
					fs.innerHTML = "Si";
			}else{
				if(!pdf.getPdfEndDate().isNull() && pdf.getPdfEndDate().compareTo(Tools.today()) < 0)  
					fs.innerHTML = "Si";
			}
		}
		cell.setStyle(fs);
	}
}
