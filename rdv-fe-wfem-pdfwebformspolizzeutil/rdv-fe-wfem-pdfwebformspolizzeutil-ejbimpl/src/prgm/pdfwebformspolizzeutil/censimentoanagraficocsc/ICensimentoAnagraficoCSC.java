package prgm.pdfwebformspolizzeutil.censimentoanagraficocsc;

import java.util.ArrayList;

import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformspolizzeutil.model.SoggettoAnagraficoModel;

public interface ICensimentoAnagraficoCSC {
	
	ArrayList<SoggettoAnagraficoModel> getElencoSoggettiDaCensire(PdfModel pdfModel) throws Exception;

}
