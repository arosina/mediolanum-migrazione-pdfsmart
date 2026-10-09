package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfModuloModel extends CommandDataModel {
	
	private StringType 		tipoModulo 					= new StringType();
	private StringType 		segmento 					= new StringType();
	private DateType 		dataInizioValidita 			= new DateType();
	private DateType 		dataFineValidita 			= new DateType();
	private DateType 		dataUltimaPubblicazione		= new DateType();
	private StringType 		flagContrElettronico 		= new StringType();
	private StringType 		flagStampaContrElettronico 	= new StringType();
	private StringType 		flagIntranet 				= new StringType();
	private StringType 		flagCartaChimica 			= new StringType();
	private StringType 		noteOperative	 			= new StringType();
	
	private ListType 		prodottiModulo = new ListType(PdfProdottoModuloModel.class);

	/*******************************************************************/
	/*******************************************************************/
	public String htmlProdotti(){
		if(prodottiModulo.size() == 0){
			return "&nbsp;";
		}
		if(prodottiModulo.size() == 1){
			PdfProdottoModuloModel p = (PdfProdottoModuloModel)prodottiModulo.get(0);
			return p.getDescrProdotto().toString();
		}
		StringBuffer res = new StringBuffer();
		for(int i=0;i<prodottiModulo.size();i++){
			PdfProdottoModuloModel p = (PdfProdottoModuloModel)prodottiModulo.get(i);
			res.append(p.getDescrProdotto());
			if(i<prodottiModulo.size()-1)
				res.append(",&nbsp;");
		}
		return res.toString();
	}
	
	public StringType getTipoModulo() {
		return tipoModulo;
	}

	public void setTipoModulo(StringType tipoModulo) {
		this.tipoModulo = tipoModulo;
	}

	public StringType getSegmento() {
		return segmento;
	}

	public void setSegmento(StringType segmento) {
		this.segmento = segmento;
	}

	public DateType getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(DateType dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public StringType getFlagContrElettronico() {
		return flagContrElettronico;
	}

	public void setFlagContrElettronico(StringType flagContrElettronico) {
		this.flagContrElettronico = flagContrElettronico;
	}

	public StringType getFlagStampaContrElettronico() {
		return flagStampaContrElettronico;
	}

	public void setFlagStampaContrElettronico(StringType flagStampaContrElettronico) {
		this.flagStampaContrElettronico = flagStampaContrElettronico;
	}

	public StringType getFlagIntranet() {
		return flagIntranet;
	}

	public void setFlagIntranet(StringType flagIntranet) {
		this.flagIntranet = flagIntranet;
	}

	public StringType getFlagCartaChimica() {
		return flagCartaChimica;
	}

	public void setFlagCartaChimica(StringType flagCartaChimica) {
		this.flagCartaChimica = flagCartaChimica;
	}

	public ListType getProdottiModulo() {
		return prodottiModulo;
	}

	public void setProdottiModulo(ListType prodottiModulo) {
		this.prodottiModulo = prodottiModulo;
	}

	public DateType getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(DateType dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public StringType getNoteOperative() {
		return noteOperative;
	}

	public void setNoteOperative(StringType noteOperative) {
		this.noteOperative = noteOperative;
	}

	public DateType getDataUltimaPubblicazione() {
		return dataUltimaPubblicazione;
	}

	public void setDataUltimaPubblicazione(DateType dataUltimaPubblicazione) {
		this.dataUltimaPubblicazione = dataUltimaPubblicazione;
	}
	
}
