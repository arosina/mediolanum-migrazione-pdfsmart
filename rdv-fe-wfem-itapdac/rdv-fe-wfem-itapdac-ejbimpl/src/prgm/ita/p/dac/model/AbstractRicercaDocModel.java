package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractRicercaDocModel extends ParamsModel {
	
	private StringType tipoRicerca = new StringType();
	private RicercaDocParamsModel parametri = new RicercaDocParamsModel();
	private ListType documenti = new ListType(DocumentoModel.class);
	private DocumentoModel documento = new DocumentoModel();
	
	public RicercaDocParamsModel getParametri() {
		return parametri;
	}
	public void setParametri(RicercaDocParamsModel parametri) {
		this.parametri = parametri;
	}
	public DocumentoModel getDocumento() {
		return documento;
	}
	public void setDocumento(DocumentoModel documento) {
		this.documento = documento;
	}
	public ListType getDocumenti() {
		return documenti;
	}
	public void setDocumenti(ListType documenti) {
		this.documenti = documenti;
	}
	public StringType getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	
}
