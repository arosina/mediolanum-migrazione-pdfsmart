package prgm.pdfwebforms.aml.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class TipoImportoOrigineModel extends CommandDataModel{
	
	private StringType 	codTipoImporto = new StringType();
	private boolean		codTipoImportoPreselezionatoSwitchFondi;
	private DoubleType 	importo = new DoubleType();
	private DoubleType 	importoOriginale = new DoubleType();

	public StringType getCodTipoImporto() {
		return codTipoImporto;
	}
	public void setCodTipoImporto(StringType codTipoImporto) {
		this.codTipoImporto = codTipoImporto;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public DoubleType getImportoOriginale() {
		return importoOriginale;
	}
	public void setImportoOriginale(DoubleType importoOriginale) {
		this.importoOriginale = importoOriginale;
	}
	public boolean isCodTipoImportoPreselezionatoSwitchFondi() {
		return codTipoImportoPreselezionatoSwitchFondi;
	}
	public void setCodTipoImportoPreselezionatoSwitchFondi(boolean codTipoImportoPreselezionatoSwitchFondi) {
		this.codTipoImportoPreselezionatoSwitchFondi = codTipoImportoPreselezionatoSwitchFondi;
	}

}
