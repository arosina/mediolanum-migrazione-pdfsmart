package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class DomicilioModel extends AbstractSectionModel{

	private IndirizzoModel indirizzo = new IndirizzoModel();
	private TelefonoModel  telefono = new TelefonoModel(); // Deprecato
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DomicilioModel(){
		getIndirizzo().getCodDescFields().remove("tipoIndirizzo");
		getIndirizzo().setTipoIndirizzo(new StringType(Costanti.COD_INDIRIZZO_DOMICILIO));
	}

	/****************************************************************** 
	 * Campi deprecati e non più utilizzati
	 ******************************************************************/	
	@Deprecated
	public TelefonoModel getTelefono() {
		return telefono;
	}
	@Deprecated
	public void setTelefono(TelefonoModel telefono) {
		this.telefono = telefono;
	}
	/********************************************************************/

	
	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

}
