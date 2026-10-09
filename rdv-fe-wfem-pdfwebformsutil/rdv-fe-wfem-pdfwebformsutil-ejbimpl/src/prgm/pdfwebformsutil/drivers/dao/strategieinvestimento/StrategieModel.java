package prgm.pdfwebformsutil.drivers.dao.strategieinvestimento;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

public class StrategieModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StringType tipoStrategia = new StringType();
	private StringType  codServz = new StringType();
	private StringType contrN = new StringType();
	private StringType  prodC = new StringType(); 
	private BooleanType flgServzAttiv = new BooleanType();
	private StringType  contrNPoliz = new StringType();
	public StringType getTipoStrategia() {
		return tipoStrategia;
	}
	public void setTipoStrategia(StringType tipoStrategia) {
		this.tipoStrategia = tipoStrategia;
	}
	public StringType getCodServz() {
		return codServz;
	}
	public void setCodServz(StringType codServz) {
		this.codServz = codServz;
	}
	public StringType getContrN() {
		return contrN;
	}
	public void setContrN(StringType contrN) {
		this.contrN = contrN;
	}
	public StringType getProdC() {
		return prodC;
	}
	public void setProdC(StringType prodC) {
		this.prodC = prodC;
	}
	public BooleanType getFlgServzAttiv() {
		return flgServzAttiv;
	}
	public void setFlgServzAttiv(BooleanType flgServzAttiv) {
		this.flgServzAttiv = flgServzAttiv;
	}
	public StringType getContrNPoliz() {
		return contrNPoliz;
	}
	public void setContrNPoliz(StringType contrNPoliz) {
		this.contrNPoliz = contrNPoliz;
	}
		
}