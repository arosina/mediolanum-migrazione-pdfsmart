package prgm.pdfwebforms.reportadeguatezza;

import java.math.BigDecimal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.io.reportadeguatezza.DispositivaReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProdottoReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.SoggettoReportAdeguatezzaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class OrdineModel extends CommandDataModel {

	// Dati di legame
	private StringType progr = new StringType("1");
	private StringType padre = new StringType();

	// Dati di dispositiva
	private StringType contratto = new StringType();
	private StringType prodotto = new StringType();
	private StringType operazione = new StringType();
	private StringType controvalore = new StringType();
	private StringType variazione = new StringType();
	private StringType deroga = new StringType();
	private StringType descr = new StringType();
	private StringType derogaVersamentoIniziale = new StringType();
	
	// Dati usati da rdv-fe-wfem-pdfwebformsdrivers-reportadeguatezza (rfc #262292)
	private StringType isin = new StringType();
	private StringType valuta = new StringType();
	private StringType piazza = new StringType();

	// Se l'elenco dei soggetti viene lasciato vuoto il motore utilizza i soggetti
	// coinvolti nel pdf
	private ListType soggetti = new ListType(SoggettoReportAdeguatezzaModel.class);
	private ListType costi = new ListType(CostoModel.class);
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ListType fromModelToCall(DispositivaReportAdeguatezzaModel dispo) {
		return fromModelToCall(dispo, 1);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ListType fromModelToCall(DispositivaReportAdeguatezzaModel dispo, int idxPadre) {

		ListType res = new ListType(OrdineModel.class);

		OrdineModel d = new OrdineModel();
		d.setProgr(new StringType(String.valueOf(idxPadre)));
		
		if(dispo.isSwitch()){
			d.setOperazione(new StringType(DispositivaReportAdeguatezzaModel.Operazioni.SWITCH));
			d.setControvalore(new StringType());
			d.setDescr(dispo.getDescr());  //#104596
		}else{
			d.setContratto(dispo.getContratto());
			d.setOperazione(dispo.getOperazione());
			d.setControvalore(new StringType(dispo.getControvalore().isNull() ? "" : "" + dispo.getControvalore().doubleValue()));
			d.setProdotto(dispo.getProdotto());
			d.setDescr(dispo.getDescr());
		}

		d.setDeroga(dispo.getDeroga());
		d.setSoggetti(dispo.getSoggetti());
		
		if (dispo.isAddToReport()) {
			res.add(d);
		}
		
		int progr = idxPadre;
		for (int i = 0; i < dispo.getProdotti().size(); i++){
			ProdottoReportAdeguatezzaModel p = (ProdottoReportAdeguatezzaModel) dispo.getProdotti().get(i);
			progr = addOrdine(res, dispo, p, idxPadre, ++progr);
		}
		
		if(dispo.isSwitch()){
			d.setVariazione(new StringType("0.0"));
		}else{
			if(dispo.getVariazione().isNull()){
				double variazioneTot = 0;
				for (int i = 0; i < res.size(); i++) {
					OrdineModel o = (OrdineModel) res.get(i);
					if (o.getPadre().equals("" + idxPadre) && !o.getVariazione().isNull())
						variazioneTot += new BigDecimal(o.getVariazione().toString()).doubleValue();
				}				
				variazioneTot = BigDecimal.valueOf(variazioneTot).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				d.setVariazione(new StringType("" + variazioneTot));
			}else{
				d.setVariazione(new StringType("" + dispo.getVariazione().doubleValue()));
			}
		}
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static int addOrdine(ListType res, DispositivaReportAdeguatezzaModel dispo,
								 ProdottoReportAdeguatezzaModel p, int idxPadre, int progr) {

		OrdineModel o = new OrdineModel();
		o.setPadre(new StringType("" + idxPadre));
		o.setProgr(new StringType("" + progr));

		if(dispo.isSwitch()){
			if(p.getVariazione().doubleValue() < 0){	// Prodotto di rimborso
				o.setContratto(dispo.getContrattoRimborsoSwitch());
				o.setOperazione(p.getOperazione().isNull() ? dispo.getOperazioneRimborsoSwitch() : p.getOperazione());
			}else{										// Prodotto di versamento
				o.setContratto(dispo.getContratto());
				o.setOperazione(p.getOperazione().isNull() ? dispo.getOperazione() : p.getOperazione());
			}
		}else{
			o.setContratto(dispo.getContratto());
			o.setOperazione(p.getOperazione().isNull() ? dispo.getOperazione() : p.getOperazione());
		}
		
		o.setDeroga(dispo.getDeroga());

		o.setProdotto(p.getProdotto());
		o.setDescr(p.getDescr());

		o.setControvalore(new StringType(p.getControvalore().isNull() ? "" : "" + p.getControvalore().doubleValue()));
		o.setVariazione(new StringType(p.getVariazione().isNull() ? "" : "" + p.getVariazione().doubleValue()));
		o.setSoggetti(dispo.getSoggetti());
		res.add(o);

		idxPadre = progr;
		for (int i = 0; i < p.getProdotti().size(); i++) {
			ProdottoReportAdeguatezzaModel pChild = (ProdottoReportAdeguatezzaModel) p.getProdotti().get(i);
			progr = addOrdine(res, dispo, pChild, idxPadre, ++progr);
		}
		if (p.getVariazione().isNull()) {
			double variazioneTot = 0;
			for (int i = 0; i < res.size(); i++) {
				OrdineModel of = (OrdineModel) res.get(i);
				if (of.getPadre().equals("" + idxPadre) && !of.getVariazione().isNull())
					variazioneTot += new BigDecimal(of.getVariazione().toString()).doubleValue();
			}
			variazioneTot = new BigDecimal(variazioneTot).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			o.setVariazione(new StringType("" + variazioneTot));
		}
		return progr++;
	}

	public StringType getProgr() {
		return progr;
	}

	public void setProgr(StringType progr) {
		this.progr = progr;
	}

	public StringType getPadre() {
		return padre;
	}

	public void setPadre(StringType padre) {
		this.padre = padre;
	}

	public StringType getContratto() {
		return contratto;
	}

	public void setContratto(StringType contratto) {
		this.contratto = contratto;
	}

	public StringType getOperazione() {
		return operazione;
	}

	public void setOperazione(StringType operazione) {
		this.operazione = operazione;
	}

	public StringType getDescr() {
		return descr;
	}

	public void setDescr(StringType descr) {
		this.descr = descr;
	}

	public StringType getProdotto() {
		return prodotto;
	}

	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}

	public StringType getVariazione() {
		return variazione;
	}

	public void setVariazione(StringType variazione) {
		this.variazione = variazione;
	}

	public StringType getDeroga() {
		return deroga;
	}

	public void setDeroga(StringType deroga) {
		this.deroga = deroga;
	}

	public ListType getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(ListType soggetti) {
		this.soggetti = soggetti;
	}

	public ListType getCosti() {
		return costi;
	}

	public void setCosti(ListType costi) {
		this.costi = costi;
	}

	public StringType getControvalore() {
		return controvalore;
	}

	public void setControvalore(StringType controvalore) {
		this.controvalore = controvalore;
	}

	public StringType getDerogaVersamentoIniziale() {
		return derogaVersamentoIniziale;
	}

	public void setDerogaVersamentoIniziale(StringType derogaVersamentoIniziale) {
		this.derogaVersamentoIniziale = derogaVersamentoIniziale;
	}

	public StringType getIsin() {
		return isin;
	}

	public void setIsin(StringType isin) {
		this.isin = isin;
	}

	public StringType getValuta() {
		return valuta;
	}

	public void setValuta(StringType valuta) {
		this.valuta = valuta;
	}

	public StringType getPiazza() {
		return piazza;
	}

	public void setPiazza(StringType piazza) {
		this.piazza = piazza;
	}

}
