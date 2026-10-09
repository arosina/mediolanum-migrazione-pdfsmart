package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class InfoDittaModel extends AbstractSectionModel{
	
	private StringType  tabInfoDittaErrorsAnchor = new StringType();
	
	private StringType  numeroIscrizioneREA = new StringType();
	private DateType    dataIscrizione = new DateType();
	private ComuneModel luogoRilascioCCIAA = new ComuneModel();
	
	public DateType getDataIscrizione() {
		return dataIscrizione;
	}
	public void setDataIscrizione(DateType dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}
	public ComuneModel getLuogoRilascioCCIAA() {
		return luogoRilascioCCIAA;
	}
	public void setLuogoRilascioCCIAA(ComuneModel luogoRilascioCCIAA) {
		this.luogoRilascioCCIAA = luogoRilascioCCIAA;
	}
	public StringType getNumeroIscrizioneREA() {
		return numeroIscrizioneREA;
	}
	public void setNumeroIscrizioneREA(StringType numeroIscrizioneREA) {
		this.numeroIscrizioneREA = numeroIscrizioneREA;
	}
	public StringType getTabInfoDittaErrorsAnchor() {
		return tabInfoDittaErrorsAnchor;
	}
	public void setTabInfoDittaErrorsAnchor(StringType tabInfoDittaErrorsAnchor) {
		this.tabInfoDittaErrorsAnchor = tabInfoDittaErrorsAnchor;
	}
	
}
