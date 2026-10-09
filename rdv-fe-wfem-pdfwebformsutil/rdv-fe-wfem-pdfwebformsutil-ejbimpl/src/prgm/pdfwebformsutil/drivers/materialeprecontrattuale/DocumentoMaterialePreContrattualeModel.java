package prgm.pdfwebformsutil.drivers.materialeprecontrattuale;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class DocumentoMaterialePreContrattualeModel extends CommandDataModel {

	private static final long serialVersionUID = -3991438229668671330L;
	
	private StringType tipoDocumento = new StringType();
	private StringType link = new StringType();
	private StringType descrizione = new StringType();
		
	public StringType getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(StringType tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public StringType getLink() {
		return link;
	}
	public void setLink(StringType link) {
		this.link = link;
	}
	public StringType getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(StringType descrizione) {
		this.descrizione = descrizione;
	}	
}
