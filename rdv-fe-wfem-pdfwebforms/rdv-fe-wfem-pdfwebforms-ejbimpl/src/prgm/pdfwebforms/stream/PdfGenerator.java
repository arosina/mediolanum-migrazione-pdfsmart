package prgm.pdfwebforms.stream;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.pdf.PdfTools;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class PdfGenerator extends BusinessCommand {

	String caller = "PDFGENERATOR";
	boolean onlyCurrentBasketDispo = false;
	
	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return innerExecute(userSessionContext, dataModel);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	protected CommandDataModel innerExecute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel processPdfModel = (PdfModel)dataModel;			
			PdfModel pdf = Basket.clone(processPdfModel);
			
			boolean doMultipleCopies = false;
			if(pdf.getPdfData().isOnlyPrint())
				doMultipleCopies = true;
			if(doMultipleCopies && pdf.getPdfData().getDoMultipleCopiesOnPrintPdf().equals("false"))
				doMultipleCopies = false;
			
			String titolo = "Modulo";
			byte[] pdfOut = null;
			if(processPdfModel.isInBasket()){
				int currentDispoIndex = getCurrentDispoIndex(processPdfModel);
				if(onlyCurrentBasketDispo)
					removeOtherBasketDispo(pdf, currentDispoIndex);
				pdfOut = PdfEngine.compileBasketPreviewPdf(csc, pdf.getBasket(), doMultipleCopies, caller);
			}else {
				titolo = pdf.getPdfData().getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfCode()+" "+pdf.mainPdfAnag().getPdfDescr():pdf.getPdfData().getPdfTitle().toString();
				pdfOut = PdfEngine.compilePdfFields(csc, pdf, true, doMultipleCopies, caller);
			}
			
			boolean asFacsimile = false;
			StringType facSimileLabelOnPrint = pdf.getPdfData().getFacSimileLabelOnPrintPdf();
			if(!facSimileLabelOnPrint.isNull() ||
			    (caller.equals("PREVIEW") && (pdf.mainPdfAnag().getFacSimileOnPreview().booleanValue() || 
			    							  pdf.getPdfData().getFacSimileOnPreview().booleanValue())))
				asFacsimile = true;
			if(asFacsimile && !pdf.isInAccettazioneCopernico())
				pdfOut = PdfEngine.generateAsFacsimile(pdfOut, facSimileLabelOnPrint.isNull()?"anteprima":facSimileLabelOnPrint.toString());
			else
				pdfOut = PdfTools.rendiPdfAccessibile(csc, pdfOut, titolo, pdf.getPdfData().getPdfInstanceId().toString());
			
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			resp.setContentType("application/pdf");
			resp.setContentLength(pdfOut.length);
			resp.setContent(pdfOut);
			String title = pdf.getPdfData().getPdfTitle().isNull()?pdf.mainPdfAnag().getPdfCode()+" "+pdf.mainPdfAnag().getPdfDescr():pdf.getPdfData().getPdfTitle().toString();
			String fileName = title.replaceAll("[\\\\/:*?\"<>|]","-");
			if(!fileName.toUpperCase().endsWith(".PDF"))
				fileName += ".pdf";
			resp.setSuggestedFileName(fileName);
			setGenericCommandResponse(resp);
			return processPdfModel;
			
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	private int getCurrentDispoIndex(PdfModel processPdfModel) {
		Basket basket = processPdfModel.getBasket();
		for(int currentDispoIndex=0;currentDispoIndex<basket.getBasketElements().size();currentDispoIndex++) {
			BasketElement be = basket.getBasketElements().get(currentDispoIndex);
			if(be.getDispoPdf() == processPdfModel)
				return currentDispoIndex;
		}
		return -1;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private void removeOtherBasketDispo(PdfModel pdf, int currentDispoIndex) {
		Basket basket = pdf.getBasket();
		for(int i=basket.getBasketElements().size()-1;i>=0;i--) {
			if(i != currentDispoIndex)
				basket.getBasketElements().remove(i);
		}
	}	
}
