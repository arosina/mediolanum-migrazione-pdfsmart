package prgm.pdfwebforms.sostituzioni;

import java.util.ArrayList;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataResponse;

/***********************************************************************************************/
/***********************************************************************************************/
public class SostituzioniCallModel extends CommandDataModel {

	// Input
	private StringType userId = new StringType();
	private StringType operatoreSede = new StringType();	

	private StringType idProposta = new StringType();
	private StringType idAdeguatezza = new StringType();	
	private ProvideSostituzioniDataResponse input = null;	// Ritornato dalla callback "provideSostituzioniData" del driver

	// Output
	private StringType resultCode = new StringType();
	private StringType resultDescription = new StringType();
	private StringType idSostituzione = new StringType();
	private StringType esitoVerificaSostituzioneGlobale = new StringType();
	private ListType   clientiResult = new ListType(ClienteResultModel.class);

    /***********************************************************************************************/
	/***********************************************************************************************/
	public ListType doUnionPreferenzeClientiSostituzioni() {
		ArrayList<String> visitedCod = new ArrayList<String>();
		ListType res = new ListType();
		for(int i=0;i<clientiResult.size();i++) {
			ClienteResultModel cli = (ClienteResultModel)clientiResult.get(i);
			ListType prefCli = cli.getElencoPreferenze();
			for(int j=0; j<prefCli.size();j++) {
				PreferenzaClienteSostituzioniModel p = (PreferenzaClienteSostituzioniModel)prefCli.get(j);
				if(!visitedCod.contains(p.getIdPreferenza().toString())) {
					res.add(p);
					visitedCod.add(p.getIdPreferenza().toString());
				}
			}
		}
		return res;
	}
	
    /***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
	
	public ProvideSostituzioniDataResponse getInput() {
		return input;
	}

	public void setInput(ProvideSostituzioniDataResponse input) {
		this.input = input;
	}

	public StringType getIdSostituzione() {
		return idSostituzione;
	}

	public void setIdSostituzione(StringType idSostituzione) {
		this.idSostituzione = idSostituzione;
	}

	public StringType getIdProposta() {
		return idProposta;
	}

	public void setIdProposta(StringType idProposta) {
		this.idProposta = idProposta;
	}

	public StringType getIdAdeguatezza() {
		return idAdeguatezza;
	}

	public void setIdAdeguatezza(StringType idAdeguatezza) {
		this.idAdeguatezza = idAdeguatezza;
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

	public StringType getEsitoVerificaSostituzioneGlobale() {
		return esitoVerificaSostituzioneGlobale;
	}

	public void setEsitoVerificaSostituzioneGlobale(StringType esitoVerificaSostituzioneGlobale) {
		this.esitoVerificaSostituzioneGlobale = esitoVerificaSostituzioneGlobale;
	}

	public StringType getOperatoreSede() {
		return operatoreSede;
	}

	public void setOperatoreSede(StringType operatoreSede) {
		this.operatoreSede = operatoreSede;
	}

	public ListType getClientiResult() {
		return clientiResult;
	}

	public void setClientiResult(ListType clientiResult) {
		this.clientiResult = clientiResult;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

}
