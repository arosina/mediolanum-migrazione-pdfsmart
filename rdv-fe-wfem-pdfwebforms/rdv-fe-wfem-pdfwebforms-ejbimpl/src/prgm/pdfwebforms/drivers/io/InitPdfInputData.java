package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.carrello.DispositivaCarrelloModel;
import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class InitPdfInputData extends AbstractEventInputData{
	
	private DispositivaCarrelloModel 	dispositivaCarrello = null;
	private AgevolazioneModel 			agevolazione = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public InitPdfInputData(PdfModel pdf){
		super(pdf);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public InitPdfInputData(PdfModel pdf, DispositivaCarrelloModel dispositivaCarrello, AgevolazioneModel agevolazione){
		super(pdf);
		this.dispositivaCarrello = dispositivaCarrello;
		this.agevolazione = agevolazione;
	}

	public DispositivaCarrelloModel getDispositivaCarrello() {
		return dispositivaCarrello;
	}

	public AgevolazioneModel getAgevolazione() {
		return agevolazione;
	}
}
