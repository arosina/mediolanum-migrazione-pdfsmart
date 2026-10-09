package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.publisher.common.CostantiPublisher;

/*******************************************************************/
/*******************************************************************/
public class PdfPublisherModel extends CommandDataModel {

	private boolean 		primaAttivazione = true;
	
	// Input params
	private StringType  	profiloUtente = new StringType();
	private StringType  	area = new StringType();
	// *******************************************************

	private BooleanType 	eseguiRicerca = new BooleanType();
	private BooleanType 	soloAttivi = new BooleanType();
	private PdfAnagModel 	ricercaPdfParam = new PdfAnagModel();
	
	private ListType 		pdfList = new ListType(PdfAnagModel.class);
	private PdfAnagKeyModel pdfDaGestire = new PdfAnagKeyModel();
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfPublisherModel(){
		addCodDescField("area","Aree");
	}

	/*******************************************************************/
	/*******************************************************************/
	public boolean isProfiloUtenteSviluppo(){
		return  getProfiloUtente().equalsIgnoreCase(CostantiPublisher.ProfiliUtente.SVILUPPO);
	}

	/*******************************************************************/
	/*******************************************************************/
	public boolean isProfiloUtenteAmministratore(){
		return  getProfiloUtente().equalsIgnoreCase(CostantiPublisher.ProfiliUtente.SVILUPPO) ||
				getProfiloUtente().equalsIgnoreCase(CostantiPublisher.ProfiliUtente.AMMINISTRATORE);
	}

	/*******************************************************************/
	/*******************************************************************/
	public BooleanType getIsRicercaOnAnag() {
		return new BooleanType(!getRicercaPdfParam().getPdfMomCode().isNull() || 
							   !getRicercaPdfParam().getCallSrvDispositivaBMED().isNull() ||
							   !getRicercaPdfParam().getPdfDriverName().isNull());
	}
	
	public boolean isPrimaAttivazione() {
		return primaAttivazione;
	}
	public void setPrimaAttivazione(boolean primaAttivazione) {
		this.primaAttivazione = primaAttivazione;
	}
	public BooleanType getEseguiRicerca() {
		return eseguiRicerca;
	}
	public void setEseguiRicerca(BooleanType eseguiRicerca) {
		this.eseguiRicerca = eseguiRicerca;
	}
	public PdfAnagModel getRicercaPdfParam() {
		return ricercaPdfParam;
	}
	public void setRicercaPdfParam(PdfAnagModel ricercaPdfParam) {
		this.ricercaPdfParam = ricercaPdfParam;
	}
	public ListType getPdfList() {
		return pdfList;
	}
	public void setPdfList(ListType pdfList) {
		this.pdfList = pdfList;
	}
	public PdfAnagKeyModel getPdfDaGestire() {
		return pdfDaGestire;
	}
	public void setPdfDaGestire(PdfAnagKeyModel pdfDaGestire) {
		this.pdfDaGestire = pdfDaGestire;
	}
	public BooleanType getSoloAttivi() {
		return soloAttivi;
	}
	public void setSoloAttivi(BooleanType soloAttivi) {
		this.soloAttivi = soloAttivi;
	}
	public StringType getArea() {
		return area;
	}
	public void setArea(StringType area) {
		this.area = area;
	}

	public StringType getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(StringType profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

}
