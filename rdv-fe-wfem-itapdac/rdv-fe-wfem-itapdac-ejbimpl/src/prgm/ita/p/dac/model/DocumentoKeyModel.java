package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DocumentoKeyModel extends BaseModel {
	
	private StringType 	idDocumento = new StringType();
	
	public StringType getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(StringType idDocumento) {
		this.idDocumento = idDocumento;
	}

}
