package prgm.pdfwebforms.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ClienteResultModel extends CommandDataModel{
	
	private StringType codTipoSogg 				= new StringType();
	private StringType codSoggOrig 				= new StringType();
	private DoubleType deltaISD 				= new DoubleType();
	private DoubleType deltaScorePortafoglio 	= new DoubleType();
	private ListType   elencoPreferenze 		= new ListType(PreferenzaClienteSostituzioniModel.class);
	
	public StringType getCodTipoSogg() {
		return codTipoSogg;
	}
	public void setCodTipoSogg(StringType codTipoSogg) {
		this.codTipoSogg = codTipoSogg;
	}
	public StringType getCodSoggOrig() {
		return codSoggOrig;
	}
	public void setCodSoggOrig(StringType codSoggOrig) {
		this.codSoggOrig = codSoggOrig;
	}
	public DoubleType getDeltaISD() {
		return deltaISD;
	}
	public void setDeltaISD(DoubleType deltaISD) {
		this.deltaISD = deltaISD;
	}
	public DoubleType getDeltaScorePortafoglio() {
		return deltaScorePortafoglio;
	}
	public void setDeltaScorePortafoglio(DoubleType deltaScorePortafoglio) {
		this.deltaScorePortafoglio = deltaScorePortafoglio;
	}
	public ListType getElencoPreferenze() {
		return elencoPreferenze;
	}
	public void setElencoPreferenze(ListType elencoPreferenze) {
		this.elencoPreferenze = elencoPreferenze;
	}
}
