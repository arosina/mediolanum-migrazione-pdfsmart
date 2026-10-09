package prgm.ita.anagraficaclienti.questionari.model;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class QuestionarioDatiStampaModel extends MapCommandDataModel {
	
	private DateType			dataCompilazione 	= new DateType();
	private StringType			oraCompilazione 	= new StringType();
	
	public DateType getDataCompilazione() {
		return dataCompilazione;
	}
	public void setDataCompilazione(DateType dataCompilazione) {
		this.dataCompilazione = dataCompilazione;
	}
	public StringType getOraCompilazione() {
		return oraCompilazione;
	}
	public void setOraCompilazione(StringType oraCompilazione) {
		this.oraCompilazione = oraCompilazione;
	}
}
