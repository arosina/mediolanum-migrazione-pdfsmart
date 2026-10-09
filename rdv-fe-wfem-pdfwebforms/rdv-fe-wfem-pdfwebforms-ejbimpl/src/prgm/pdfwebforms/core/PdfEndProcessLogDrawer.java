package prgm.pdfwebforms.core;

import com.atosorigin.wfem.types.AbstractType;

import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfEndProcessLogDrawer {
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfEndProcessLogDrawer(){
		throw new IllegalStateException("EndProcessLogDrawer class");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String draw(PdfModel pdf, String cmd, String otherPars) {
		if(pdf.isTestMode())
			return "";
		StringBuilder res = new StringBuilder();
		if(pdf.isInBasket()) {
			for(int i=0;i<pdf.getBasket().getBasketElements().size();i++) {
				BasketElement be = pdf.getBasket().getBasketElements().get(i);
				drawOne(be.getDispoPdf(), cmd, otherPars, res);
			}
		}else {
			drawOne(pdf, cmd, otherPars, res);
		}
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void drawOne(PdfModel pdf, String cmd, String otherPars, StringBuilder res) {
		res.append("<iframe  style='display:none;' src='call.wfem?wfemCmd="+cmd+".execute");
		res.append("&readRequest=false");
		res.append(otherPars);
		res.append("&pdfInstanceId="+pdf.getPdfData().getPdfInstanceId());
		res.append("&idBasket="+pdf.getPdfData().getIdCarrello());
		AbstractType ndg = pdf.mainPdfData().read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+"1");
		if(ndg != null && !ndg.isNull())
			res.append("&ndgCliente1="+ndg);
		res.append("&pdfCode="+pdf.mainPdfAnag().getPdfCode());
		res.append("&pdfMomCode="+pdf.mainPdfAnag().getPdfMomCode());
		res.append("&prodCProd="+prodCProd(pdf));
		res.append("&driverName="+pdf.mainPdfAnag().getPdfDriverName());
		res.append("'></iframe>");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String prodCProd(PdfModel pdf){
		StringBuilder prodCProd = new StringBuilder();
		for(int i=0;i<pdf.getPdfAnags().size();i++){
			PdfAnagModel pdfDettAnag = pdf.getPdfAnags().get(i);
			if(pdfDettAnag.getPdfMomCode().isNull())
				continue;
			prodCProd.append(pdfDettAnag.getPdfMomCode()+",");
		}
		if(prodCProd.length() > 0)			
			prodCProd = prodCProd.deleteCharAt(prodCProd.length()-1);
		return prodCProd.toString();
	}
	
}
