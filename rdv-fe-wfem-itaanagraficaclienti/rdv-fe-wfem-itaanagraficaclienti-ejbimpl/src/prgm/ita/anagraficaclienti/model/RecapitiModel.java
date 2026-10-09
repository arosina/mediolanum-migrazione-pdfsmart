package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class RecapitiModel extends AbstractSectionModel{
	private StringType    email = new StringType();
	private StringType    emailPec = new StringType();
	private TelefonoModel telefonoResidenza = new TelefonoModel();
	private TelefonoModel telefonoDomicilio = new TelefonoModel();
	private TelefonoModel telefonoFax = new TelefonoModel();
	private TelefonoModel telefonoCellulare = new TelefonoModel();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public RecapitiModel(){
		
		frontendPropName.add("recapiti_email");
		frontendPropName.add("recapiti_telefonoCellulare_prefissoInternazionale");
		frontendPropName.add("recapiti_telefonoCellulare_prefisso");
		frontendPropName.add("recapiti_telefonoCellulare_numeroTelefono");
		frontendPropName.add("recapiti_telefonoResidenza_prefissoInternazionale");
		frontendPropName.add("recapiti_telefonoResidenza_prefisso");
		frontendPropName.add("recapiti_telefonoResidenza_numeroTelefono");
		frontendPropName.add("recapiti_telefonoFax_prefissoInternazionale");
		frontendPropName.add("recapiti_telefonoFax_prefisso");
		frontendPropName.add("recapiti_telefonoFax_numeroTelefono");
		frontendPropName.add("recapiti_telefonoResidenza_reperibileOraDa");
		frontendPropName.add("recapiti_telefonoResidenza_reperibileOraA");
		frontendPropName.add("recapiti_telefonoResidenza_note");
		frontendPropName.add("recapiti_telefonoResidenza_noteNonReperibilita");
		
		getTelefonoResidenza().getCodDescFields().remove("tipoTelefono");
		getTelefonoResidenza().setTipoTelefono(new StringType(Costanti.COD_TELEFONO_RESIDENZA));
		getTelefonoDomicilio().getCodDescFields().remove("tipoTelefono");
		getTelefonoDomicilio().setTipoTelefono(new StringType(Costanti.COD_TELEFONO_DOMICILIO));
		getTelefonoFax().getCodDescFields().remove("tipoTelefono");
		getTelefonoFax().setTipoTelefono(new StringType(Costanti.COD_TELEFONO_FAX));
		getTelefonoFax().setPrefissoInternazionale(new StringType(Costanti.PREFIX_INTERNAZIONALE_ITALIA));
		getTelefonoCellulare().getCodDescFields().remove("tipoTelefono");
		getTelefonoCellulare().setTipoTelefono(new StringType(Costanti.COD_TELEFONO_CELLULARE));
		getTelefonoCellulare().setPrefissoInternazionale(new StringType(Costanti.PREFIX_INTERNAZIONALE_ITALIA));
	}
	
	public StringType getEmail() {
		return email;
	}

	public void setEmail(StringType email) {
		this.email = email;
	}

	public TelefonoModel getTelefonoCellulare() {
		return telefonoCellulare;
	}

	public void setTelefonoCellulare(TelefonoModel telefonoCellulare) {
		this.telefonoCellulare = telefonoCellulare;
	}

	public TelefonoModel getTelefonoFax() {
		return telefonoFax;
	}

	public void setTelefonoFax(TelefonoModel telefonoFax) {
		this.telefonoFax = telefonoFax;
	}

	public TelefonoModel getTelefonoResidenza() {
		return telefonoResidenza;
	}

	public void setTelefonoResidenza(TelefonoModel telefonoResidenza) {
		this.telefonoResidenza = telefonoResidenza;
	}

	public TelefonoModel getTelefonoDomicilio() {
		return telefonoDomicilio;
	}

	public void setTelefonoDomicilio(TelefonoModel telefonoDomicilio) {
		this.telefonoDomicilio = telefonoDomicilio;
	}

	public StringType getEmailPec() {
		return emailPec;
	}

	public void setEmailPec(StringType emailPec) {
		this.emailPec = emailPec;
	}
	
}
