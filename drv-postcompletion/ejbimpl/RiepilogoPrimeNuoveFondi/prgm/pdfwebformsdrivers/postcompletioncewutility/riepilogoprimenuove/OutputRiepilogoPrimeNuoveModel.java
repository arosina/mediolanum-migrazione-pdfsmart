package prgm.pdfwebformsdrivers.postcompletioncewutility.riepilogoprimenuove;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/******************************************************************************/
/******************************************************************************/
public class OutputRiepilogoPrimeNuoveModel extends CommandDataModel {
	
	private StringType 		idDisposizione 	= new StringType();
	private StringType		idIstanza				= new StringType();
	private StringType      esito       = new StringType();
	private StringType      idRichiesta       = new StringType();

	
	public StringType getIdDisposizione() {
		return idDisposizione;
	}
	public void setIdDisposizione(StringType idDisposizione) {
		this.idDisposizione = idDisposizione;
	}
	public StringType getIdIstanza() {
		return idIstanza;
	}
	public void setIdIstanza(StringType idIstanza) {
		this.idIstanza = idIstanza;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getIdRichiesta() {
		return idRichiesta;
	}
	public void setIdRichiesta(StringType idRichiesta) {
		this.idRichiesta = idRichiesta;
	}
	
}
