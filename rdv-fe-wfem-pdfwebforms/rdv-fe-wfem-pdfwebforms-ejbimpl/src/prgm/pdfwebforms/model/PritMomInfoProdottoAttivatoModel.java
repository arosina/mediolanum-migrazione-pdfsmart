package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PritMomInfoProdottoAttivatoModel extends CommandDataModel{
	
	private BooleanType isMaster = new BooleanType();
	private StringType codSurrProdServ = null;
	private StringType codTipoOperSuProd = null;
	
	public BooleanType getIsMaster() {
		return isMaster;
	}
	public void setIsMaster(BooleanType isMaster) {
		this.isMaster = isMaster;
	}
	public StringType getCodSurrProdServ() {
		return codSurrProdServ;
	}
	public void setCodSurrProdServ(StringType codSurrProdServ) {
		this.codSurrProdServ = codSurrProdServ;
	}
	public StringType getCodTipoOperSuProd() {
		return codTipoOperSuProd;
	}
	public void setCodTipoOperSuProd(StringType codTipoOperSuProd) {
		this.codTipoOperSuProd = codTipoOperSuProd;
	}
	
}
