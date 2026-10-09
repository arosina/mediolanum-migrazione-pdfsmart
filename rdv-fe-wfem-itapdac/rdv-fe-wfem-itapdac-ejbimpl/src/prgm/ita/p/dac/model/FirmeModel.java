package prgm.ita.p.dac.model;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class FirmeModel extends ParamsModel {
	private BooleanType 		isReadonly = new BooleanType();
	private StringType 			idDocumento = new StringType();
	private StringType 			codMediolanum = new StringType();
	private StringType 			cognome = new StringType();
	private StringType 			nome = new StringType();
	private StringType 			nominativo = new StringType();
	private ListType			elencoConti = new ListType(ContoCorrenteModel.class);
	private ContoCorrenteModel	contoSelezionato = new ContoCorrenteModel();
	private StringType 			esitoFirma = new StringType();
	
	public StringType getCodMediolanum() {
		return codMediolanum;
	}
	public void setCodMediolanum(StringType codMediolanum) {
		this.codMediolanum = codMediolanum;
	}
	public ContoCorrenteModel getContoSelezionato() {
		return contoSelezionato;
	}
	public void setContoSelezionato(ContoCorrenteModel contoSelezionato) {
		this.contoSelezionato = contoSelezionato;
	}
	public ListType getElencoConti() {
		return elencoConti;
	}
	public void setElencoConti(ListType elencoConti) {
		this.elencoConti = elencoConti;
	}
	public StringType getCognome() {
		return cognome;
	}
	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}
	public StringType getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(StringType idDocumento) {
		this.idDocumento = idDocumento;
	}
	public BooleanType getIsReadonly() {
		return isReadonly;
	}
	public void setIsReadonly(BooleanType isReadonly) {
		this.isReadonly = isReadonly;
	}
	public StringType getEsitoFirma() {
		return esitoFirma;
	}
	public void setEsitoFirma(StringType esitoFirma) {
		this.esitoFirma = esitoFirma;
	}
	public StringType getNominativo() {
		return nominativo;
	}
	public void setNominativo(StringType nominativo) {
		this.nominativo = nominativo;
	}

}
