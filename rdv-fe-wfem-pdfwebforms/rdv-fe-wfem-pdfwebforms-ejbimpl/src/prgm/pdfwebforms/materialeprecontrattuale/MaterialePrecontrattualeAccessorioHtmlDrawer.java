package prgm.pdfwebforms.materialeprecontrattuale;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class MaterialePrecontrattualeAccessorioHtmlDrawer {

	private static final String SLASH_DIV = "</div>";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private MaterialePrecontrattualeAccessorioHtmlDrawer(){
		throw new IllegalStateException("HtmlMaterialePrecontrattualeAccessorio class");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String htmlMaterialePrecontrattuale(PdfModel pdf) {
		StringBuilder res = new StringBuilder();
		if(pdf.isInBasket()) {
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements())
				res.append(htmlMaterialePrecontrattualeDispo(be.getDispoPdf()));
		}else {
			res.append(htmlMaterialePrecontrattualeDispo(pdf));
		}
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String htmlMaterialePrecontrattualeDispo(PdfModel pdf) {
		StringBuilder res = new StringBuilder();
		
		PdfAnagModel pdfAnag = pdf.mainPdfAnag();
		PdfDataModel pdfData = pdf.mainPdfData();
		
		 // Se è uno switch...
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfAnags().size() > 1 && pdf.getPdfData().getPdfs().size() > 1){
			pdfAnag = pdf.getPdfAnags().get(1);	// ...prendo i dati del secondo modulo
			pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
			if(!pdfData.getIdCarrello().isNull()){ 				// ...ma nel carrello dobbiamo prendere l'elenco dal primo modulo per poterlo mostrare anche in navigazione
				if(pdf.getPdfData().getPdfIndex().intValue() == 0) // Se siamo sul primo mostriamo dal pdfData "corrente"
					pdfData = pdf.getPdfData();
				else												 // Altrimenti dal pdfData del primo
					pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(0);
			}
		}
		
		// URLS
		String url = pdfAnag.getExternalLinkAggOnSignUrl().toString();
		if(pdfData.getExternalLinkOnSignData() != null){
			if(url.length() > 0)
				url += "|";
			url += pdfData.getExternalLinkOnSignData().getExternalLinkOnSignUrlAggiuntivo().toString();
		}
		if(url.length() == 0)
			return "";
		String[] urls = url.split("\\|");

		// LABELS
		String label = pdfAnag.getExternalLinkAggOnSignLabel().toString();
		if(pdfData.getExternalLinkOnSignData() != null){
			if(label.length() > 0)
				label += "|";
			label += pdfData.getExternalLinkOnSignData().getExternalLinkOnSignLabelAggiuntivo().toString();		
		}
		if(label.length() == 0)
			label = url;	
		String[] labels = label.split("\\|");
		
		if(urls.length != labels.length)
			return "<div class='gruppoMaterialePrecontrattuale'>Errore composizione link</div>";

		String title = pdf.getPdfData().getPdfTitle().isNull()?pdfAnag.getPdfDescr().toString() : pdf.getPdfData().getPdfTitle().toString();
		res.append("<div class='gruppoMaterialePrecontrattuale'>"+title+SLASH_DIV);
		res.append("<div class='gruppoMaterialePrecontrattualeCont'>");
		for(int i=0;i<urls.length;i++) {
			res.append("<div role='link' tabindex='0' class='elementoMaterialePrecontrattuale' tabindex='0' onclick=\""+getOnClick(urls[i])+"\">"+
						 "<div><img src='/PdfWebForms/images/visualizza_pdf.png'></div>"+
						 "<div>"+labels[i]+SLASH_DIV+
						 "<div><img src='/PdfWebForms/images/freccia_avanti.png'></div>"+
					   SLASH_DIV);
			if(i < urls.length-1)
				res.append("<div class='elementoMaterialePrecontrattualeSep'></div>");
		}
		res.append(SLASH_DIV);
		return res.toString();	
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getOnClick(String url) {
		return "openMaterialePrecontrattualeAccessorio('"+url+"');";
	}
}
