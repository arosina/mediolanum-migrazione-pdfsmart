package prgm.pdfwebforms.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class PreferenzeClienteSostituzioniModel extends CommandDataModel{
	
	private StringType 	userId = new StringType();
    private StringType 	idProposta = new StringType();
	private StringType 	resultCode = new StringType();
	private StringType 	resultDescription = new StringType();
	private ListType 	elencoPreferenze = new ListType(PreferenzaClienteSostituzioniModel.class);
	
    /***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ListType getElencoPreferenzeSelezionate() {
		ListType res = new ListType();
		for(int i=0;i<getElencoPreferenze().size();i++) {
			PreferenzaClienteSostituzioniModel p = (PreferenzaClienteSostituzioniModel)getElencoPreferenze().get(i);
			if(p.getIsSelezionata().booleanValue())
				res.add(p);
		}
		return res;
	}
	
	public StringType getIdProposta() {
		return idProposta;
	}
	public void setIdProposta(StringType idProposta) {
		this.idProposta = idProposta;
	}
	public ListType getElencoPreferenze() {
		return elencoPreferenze;
	}
	public void setElencoPreferenze(ListType elencoPreferenze) {
		this.elencoPreferenze = elencoPreferenze;
	}
	public StringType getResultCode() {
		return resultCode;
	}
	public void setResultCode(StringType resultCode) {
		this.resultCode = resultCode;
	}
	public StringType getResultDescription() {
		return resultDescription;
	}
	public void setResultDescription(StringType resultDescription) {
		this.resultDescription = resultDescription;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

}
