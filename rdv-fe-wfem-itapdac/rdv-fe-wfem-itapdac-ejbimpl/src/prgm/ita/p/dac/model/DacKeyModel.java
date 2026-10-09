package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacKeyModel extends BaseModel {
	
	private StringType  idDac = new StringType();
	private BooleanType apriPritAttivo = new BooleanType();

	public StringType getIdDac() {
		return idDac;
	}

	public void setIdDac(StringType idDac) {
		this.idDac = idDac;
	}

	public BooleanType getApriPritAttivo() {
		return apriPritAttivo;
	}

	public void setApriPritAttivo(BooleanType apriPritAttivo) {
		this.apriPritAttivo = apriPritAttivo;
	}


}
