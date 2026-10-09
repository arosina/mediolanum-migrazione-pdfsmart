package prgm.ita.p.dac.service;

import java.io.Serializable;

import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class Contratto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	protected String 	contestoPlico;
	protected String 	aggregatorePlico;

	protected String	nomeRisorsa;

	private String		codInforete;
	private String		barcode;
	private String		numeroContratto;
	private int 		codProdotto;
	private int 		codOperazione;
	private String 		descrizioneContratto;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void fillApplModel(DocumentoModel doc){
		doc.setNomeRisorsaEsterna(new StringType(getNomeRisorsa()));
		
		doc.setCodInforeteEsterno(new StringType(getCodInforete()));
		doc.setBarcode(new StringType(getBarcode()));
		doc.setNumeroContratto(new StringType(getNumeroContratto()));
		doc.setCodProdotto(new IntegerType(getCodProdotto()));
		doc.setCodOperazione(new IntegerType(getCodOperazione()));
		doc.setDescrContratto(new StringType(getDescrizioneContratto()));
	}

	protected String getContestoPlico() {
		return contestoPlico;
	}

	protected void setContestoPlico(String contestoPlico) {
		this.contestoPlico = contestoPlico;
	}

	protected String getAggregatorePlico() {
		return aggregatorePlico;
	}

	protected void setAggregatorePlico(String aggregatorePlico) {
		this.aggregatorePlico = aggregatorePlico;
	}

	protected String getNomeRisorsa() {
		return nomeRisorsa;
	}

	protected void setNomeRisorsa(String nomeRisorsa) {
		this.nomeRisorsa = nomeRisorsa;
	}

	public String getNumeroContratto() {
		return numeroContratto;
	}

	public void setNumeroContratto(String numeroContratto) {
		this.numeroContratto = numeroContratto;
	}

	public int getCodProdotto() {
		return codProdotto;
	}

	public void setCodProdotto(int codProdotto) {
		this.codProdotto = codProdotto;
	}

	public int getCodOperazione() {
		return codOperazione;
	}

	public void setCodOperazione(int codOperazione) {
		this.codOperazione = codOperazione;
	}

	public String getCodInforete() {
		return codInforete;
	}

	public void setCodInforete(String codInforete) {
		this.codInforete = codInforete;
	}

	public String getDescrizioneContratto() {
		return descrizioneContratto;
	}

	public void setDescrizioneContratto(String descrizioneContratto) {
		this.descrizioneContratto = descrizioneContratto;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
}
