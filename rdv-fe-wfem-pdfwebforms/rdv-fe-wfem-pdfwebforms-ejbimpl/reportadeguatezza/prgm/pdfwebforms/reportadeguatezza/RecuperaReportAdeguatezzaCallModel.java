package prgm.pdfwebforms.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class RecuperaReportAdeguatezzaCallModel extends CommandDataModel {
	
	// Input
	private StringType  userId = new StringType();
    private StringType  idReportAdeguatezza = new StringType();
	private StringType  codiceAgente = new StringType();
	
	// Output
	private StringType  stato = new StringType();
	private ListType 	elencoLink = new ListType(LinkReportAdeguatezzaModel.class);

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getStatoReportAdeguatezza(){
		
		String STATO_BOZZA 		= "1";
		String STATO_AVVIATO 	= "2";
		String STATO_ERRORE 	= "6";
		
		if(getStato().equals(STATO_ERRORE))
			return "ERROR";
		else if(getStato().equals(STATO_BOZZA) || 
				getStato().equals(STATO_AVVIATO))
			return "WAIT";
		else
			return "END";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String writeLinksReportAdeguatezza(){
		if(!getStatoReportAdeguatezza().equals("END") || getElencoLink().size() == 0)
			return "[]";
		StringBuffer result = new StringBuffer();
		for(int i=0;i<getElencoLink().size();i++){
			LinkReportAdeguatezzaModel link = (LinkReportAdeguatezzaModel)getElencoLink().get(i);
			result.append("{ \"link\": \""+link.getLink()+"\", \"guid\": \""+link.getGuid()+"\", \"idECM\": \""+link.getIdECM()+"\" },");
		}
		return "["+result.toString().substring(0, result.length()-1)+"]";
	}	
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}

	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}

	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}

	public StringType getCodiceAgente() {
		return codiceAgente;
	}

	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	public ListType getElencoLink() {
		return elencoLink;
	}

	public void setElencoLink(ListType elencoLink) {
		this.elencoLink = elencoLink;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public StringType getStato() {
		return stato;
	}

	public void setStato(StringType stato) {
		this.stato = stato;
	}
	
}
