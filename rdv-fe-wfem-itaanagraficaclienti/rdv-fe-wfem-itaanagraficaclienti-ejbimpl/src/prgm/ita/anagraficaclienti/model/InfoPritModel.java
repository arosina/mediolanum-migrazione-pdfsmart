package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class InfoPritModel extends CommandDataModel {

	//Parametri di input
	private StringType codiceProdottoDispo 	= new StringType("ANAGRAFICA");
	private StringType chiave 				= new StringType();
	//Output
	private StringType pritCodProdotto = new StringType();
	private StringType pritCodOperazione = new StringType();
	private StringType nomeProprietaImporto	= new StringType();
		
	public StringType getCodiceProdottoDispo() {
		return codiceProdottoDispo;
	}

	public void setCodiceProdottoDispo(StringType codiceProdottoDispo) {
		this.codiceProdottoDispo = codiceProdottoDispo;
	}

	public StringType getChiave() {
		return chiave;
	}

	public void setChiave(StringType chiave) {
		this.chiave = chiave;
	}

	public StringType getNomeProprietaImporto() {
		return nomeProprietaImporto;
	}

	public void setNomeProprietaImporto(StringType nomeProprietaImporto) {
		this.nomeProprietaImporto = nomeProprietaImporto;
	}

	public StringType getPritCodProdotto() {
		return pritCodProdotto;
	}

	public void setPritCodProdotto(StringType pritCodProdotto) {
		this.pritCodProdotto = pritCodProdotto;
	}

	public StringType getPritCodOperazione() {
		return pritCodOperazione;
	}

	public void setPritCodOperazione(StringType pritCodOperazione) {
		this.pritCodOperazione = pritCodOperazione;
	}

}
