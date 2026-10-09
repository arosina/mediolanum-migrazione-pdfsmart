package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class ResidenzaModel extends AbstractSectionModel{

	private IndirizzoModel indirizzo = new IndirizzoModel();
	private TelefonoModel  telefono = new TelefonoModel(); // Deprecato
	
	// Con FATCA: 	flagCertificazioneResidenzaEstera non più utilizzato
	//				flagPep e motivazionePep spostati, solo su front-end, nel tab "Info personali"
	private StringType  flagCertificazioneResidenzaEstera = new StringType(); // Deprecato
	private StringType  flagPep = new StringType();	
	private StringType  motivazionePep = new StringType();	
	private StringType  motivazionePepEstesa = new StringType();	
	
	private StringType codNazioneResidenzaFiscale1 = new StringType();
	private StringType codNazioneResidenzaFiscale2 = new StringType();
	private StringType codNazioneResidenzaFiscale3 = new StringType();
	
	private StringType codFiscaleResidenzaFiscale2 = new StringType();
	private StringType codFiscaleResidenzaFiscale3 = new StringType();
	
	// Info di comodo per controlli residenze fiscali
	private BooleanType codFiscaleResidenza2Required = new BooleanType();
	private BooleanType codFiscaleResidenza2Released = new BooleanType();
	private IntegerType codFiscaleResidenza2Digits = new IntegerType(0);
	private BooleanType codFiscaleResidenza3Required = new BooleanType();
	private BooleanType codFiscaleResidenza3Released = new BooleanType();
	private IntegerType codFiscaleResidenza3Digits = new IntegerType(0);

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ResidenzaModel(){
		
		frontendPropName.add("residenza_indirizzo_codNazione");
		frontendPropName.add("residenza_indirizzo_toponimoIndirizzo");
		frontendPropName.add("residenza_indirizzo_descrizioneIndirizzo");
		frontendPropName.add("residenza_indirizzo_numeroCivico");
		frontendPropName.add("residenza_indirizzo_provincia");
		frontendPropName.add("residenza_indirizzo_comune");
		frontendPropName.add("residenza_indirizzo_cap");
		frontendPropName.add("residenza_codNazioneResidenzaFiscale1");
		frontendPropName.add("residenza_codNazioneResidenzaFiscale2");
		frontendPropName.add("residenza_codFiscaleResidenzaFiscale2");
		frontendPropName.add("residenza_codNazioneResidenzaFiscale3");
		frontendPropName.add("residenza_codFiscaleResidenzaFiscale3");
		
		frontendPropName.add("domicilioDiversoDaResidenza");
		
		frontendPropName.add("domicilio_indirizzo_presso");
		frontendPropName.add("domicilio_indirizzo_codNazione");
		frontendPropName.add("domicilio_indirizzo_toponimoIndirizzo");
		frontendPropName.add("domicilio_indirizzo_descrizioneIndirizzo");
		frontendPropName.add("domicilio_indirizzo_numeroCivico");
		frontendPropName.add("domicilio_indirizzo_provincia");
		frontendPropName.add("domicilio_indirizzo_comune");
		frontendPropName.add("domicilio_indirizzo_cap");
				
		getIndirizzo().getCodDescFields().remove("tipoIndirizzo");
		getIndirizzo().setTipoIndirizzo(new StringType(Costanti.COD_INDIRIZZO_RESIDENZA));
		
		addCodDescField("motivazionePep","MOTIVAZIONI_PEP");
		addCodDescField("motivazionePepEstesa","MOTIVAZIONI_PEP_ESTESE");
		addCodDescField("codNazioneResidenzaFiscale1","NAZIONI_RESIDENZA_FISCALE1");
		addCodDescField("codNazioneResidenzaFiscale2","NAZIONI_RESIDENZA_FISCALE2");
		addCodDescField("codNazioneResidenzaFiscale3","NAZIONI_RESIDENZA_FISCALE2");
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
	@Deprecated
	public StringType getFlagCertificazioneResidenzaEstera() {
		return flagCertificazioneResidenzaEstera;
	}
	@Deprecated
	public void setFlagCertificazioneResidenzaEstera(StringType flagCertificazioneResidenzaEstera) {
		this.flagCertificazioneResidenzaEstera = flagCertificazioneResidenzaEstera;
	}
	/********************************************************************/


	public IndirizzoModel getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(IndirizzoModel indirizzo) {
		this.indirizzo = indirizzo;
	}

	public StringType getFlagPep() {
		return flagPep;
	}

	public void setFlagPep(StringType flagPep) {
		this.flagPep = flagPep;
	}

	public StringType getMotivazionePep() {
		return motivazionePep;
	}

	public void setMotivazionePep(StringType motivazionePep) {
		this.motivazionePep = motivazionePep;
	}

	public StringType getMotivazionePepEstesa() {
		return motivazionePepEstesa;
	}

	public void setMotivazionePepEstesa(StringType motivazionePepEstesa) {
		this.motivazionePepEstesa = motivazionePepEstesa;
	}

	public StringType getCodNazioneResidenzaFiscale1() {
		return codNazioneResidenzaFiscale1;
	}

	public void setCodNazioneResidenzaFiscale1(
			StringType codNazioneResidenzaFiscale1) {
		this.codNazioneResidenzaFiscale1 = codNazioneResidenzaFiscale1;
	}

	public StringType getCodNazioneResidenzaFiscale2() {
		return codNazioneResidenzaFiscale2;
	}

	public void setCodNazioneResidenzaFiscale2(
			StringType codNazioneResidenzaFiscale2) {
		this.codNazioneResidenzaFiscale2 = codNazioneResidenzaFiscale2;
	}

	public StringType getCodFiscaleResidenzaFiscale2() {
		return codFiscaleResidenzaFiscale2;
	}

	public void setCodFiscaleResidenzaFiscale2(
			StringType codFiscaleResidenzaFiscale2) {
		this.codFiscaleResidenzaFiscale2 = codFiscaleResidenzaFiscale2;
	}

	public BooleanType getCodFiscaleResidenza2Required() {
		return codFiscaleResidenza2Required;
	}

	public void setCodFiscaleResidenza2Required(
			BooleanType codFiscaleResidenza2Required) {
		this.codFiscaleResidenza2Required = codFiscaleResidenza2Required;
	}

	public IntegerType getCodFiscaleResidenza2Digits() {
		return codFiscaleResidenza2Digits;
	}

	public void setCodFiscaleResidenza2Digits(IntegerType codFiscaleResidenza2Digits) {
		this.codFiscaleResidenza2Digits = codFiscaleResidenza2Digits;
	}

	public StringType getCodNazioneResidenzaFiscale3() {
		return codNazioneResidenzaFiscale3;
	}

	public void setCodNazioneResidenzaFiscale3(
			StringType codNazioneResidenzaFiscale3) {
		this.codNazioneResidenzaFiscale3 = codNazioneResidenzaFiscale3;
	}

	public StringType getCodFiscaleResidenzaFiscale3() {
		return codFiscaleResidenzaFiscale3;
	}

	public void setCodFiscaleResidenzaFiscale3(
			StringType codFiscaleResidenzaFiscale3) {
		this.codFiscaleResidenzaFiscale3 = codFiscaleResidenzaFiscale3;
	}

	public BooleanType getCodFiscaleResidenza3Required() {
		return codFiscaleResidenza3Required;
	}

	public void setCodFiscaleResidenza3Required(
			BooleanType codFiscaleResidenza3Required) {
		this.codFiscaleResidenza3Required = codFiscaleResidenza3Required;
	}

	public IntegerType getCodFiscaleResidenza3Digits() {
		return codFiscaleResidenza3Digits;
	}

	public void setCodFiscaleResidenza3Digits(IntegerType codFiscaleResidenza3Digits) {
		this.codFiscaleResidenza3Digits = codFiscaleResidenza3Digits;
	}

	public BooleanType getCodFiscaleResidenza2Released() {
		return codFiscaleResidenza2Released;
	}

	public void setCodFiscaleResidenza2Released(
			BooleanType codFiscaleResidenza2Released) {
		this.codFiscaleResidenza2Released = codFiscaleResidenza2Released;
	}

	public BooleanType getCodFiscaleResidenza3Released() {
		return codFiscaleResidenza3Released;
	}

	public void setCodFiscaleResidenza3Released(
			BooleanType codFiscaleResidenza3Released) {
		this.codFiscaleResidenza3Released = codFiscaleResidenza3Released;
	}
}
