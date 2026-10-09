package prgm.pdfwebforms.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class MifidCallModel extends CommandDataModel {
	
	// Input
	private StringType 					userId = new StringType();
	private StringType 					applicazioneChiamante = new StringType();
	private StringType					codDispoSorgente = new StringType();
	private StringType					flagCheckRiprofilatura = new StringType("S");
	private StringType					flagRDA = new StringType();
	private StringType  				idPCA = new StringType();	// RFC #292576: Id PCA polizze. Passato dal 5D o recuperato da IDD
	private StringType  				idQLTM = new StringType();	// RFC #292576: Id questionario light Target Market. Passato dal 5D o generato dai driver
	private MifidManlevaDataModel		mifidManlevaData = new MifidManlevaDataModel();
	private ProvideMifidDataResponse 	input = null;	// Ritornato dalla callback "provideMifidData" del driver
	private InputBasketMifidModel		inputBasket = null; // Utilizzato come input nella chiamata a basket
	
	// Output
    private IntegerType		err = new IntegerType();
	private StringType      esito = new StringType("OK");
	private IntegerType		stato = new IntegerType();
    private StringType      idEsito = new StringType();
    private StringType      descrizioneEsito = new StringType();
    private StringType      descrizioneErrore = new StringType();
    private ListType		esitiBasket = new ListType(EsitoBasketMifidModel.class);
    
    // RFC #281217: Controllo concentrazione FIA data
    private ControlliConcentrazioneFiaDataModel	controlliConcentrazioneFiaData = new ControlliConcentrazioneFiaDataModel();
   
    // Tecnici
    private boolean soloFirmaOlografa = false;
    
    /***********************************************************************************************/
	/***********************************************************************************************/
	public TimestampType getNow() {
		return Tools.now();
	}
    
    /***********************************************************************************************/
    /***********************************************************************************************/
    public static MifidCallModel modelForXmlFormat(MifidCallModel callModel) throws Exception {
    	
    	if(callModel.getInput() == null && callModel.getInputBasket() == null)
    		return null;
    	
		MifidCallModel cloneCallModel = (MifidCallModel)Tools.cloneObject(callModel); 

		if(cloneCallModel.getInput() != null)
			fromModelToXmlCliListFormat(cloneCallModel.getInput().getClienti());
		
		if(cloneCallModel.getInputBasket() != null) {
			ListType acquisti = cloneCallModel.getInputBasket().getAcquisti();
			for(int i=0;i<acquisti.size();i++) {
				OperazioneBasketMifidModel o = (OperazioneBasketMifidModel)acquisti.get(i);
				fromModelToXmlCliListFormat(o.getClienti());
			}
			ListType disinvestimenti = cloneCallModel.getInputBasket().getDisinvestimenti();
			for(int i=0;i<disinvestimenti.size();i++) {
				OperazioneBasketMifidModel o = (OperazioneBasketMifidModel)disinvestimenti.get(i);
				fromModelToXmlCliListFormat(o.getClienti());
			}
		}
		cloneCallModel.setMifidManlevaData(null);
		return cloneCallModel;
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    private static void fromModelToXmlCliListFormat(ListType cliList) {
    	for(int i=0;i<cliList.size();i++){
    		MapCommandDataModel cli = (MapCommandDataModel)cliList.get(i);
    		if(cli.readProperty("ndg") != null) { // Già gestito
    			cliList.getElements().set(i, cli);
    			continue;
    		}
    		PdfPersonModel person = (PdfPersonModel)cli;
			MapCommandDataModel cliKey = new MapCommandDataModel();
			cliKey.addProperty("ndg", person.getNdg());
			cliKey.addProperty("codPotenziale", person.getCodPotenziale());
			cliList.getElements().set(i, cliKey);
    	}
    }
    
    public StringType getApplicazioneChiamante() {
		return applicazioneChiamante;
	}
	public void setApplicazioneChiamante(StringType applicazioneChiamante) {
		this.applicazioneChiamante = applicazioneChiamante;
	}
	public ProvideMifidDataResponse getInput() {
		return input;
	}
	public void setInput(ProvideMifidDataResponse input) {
		this.input = input;
	}
	public IntegerType getErr() {
		return err;
	}
	public void setErr(IntegerType err) {
		this.err = err;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getIdEsito() {
		return idEsito;
	}
	public void setIdEsito(StringType idEsito) {
		this.idEsito = idEsito;
	}
	public StringType getDescrizioneEsito() {
		return descrizioneEsito;
	}
	public void setDescrizioneEsito(StringType descrizioneEsito) {
		this.descrizioneEsito = descrizioneEsito;
	}
	public InputBasketMifidModel getInputBasket() {
		return inputBasket;
	}
	public void setInputBasket(InputBasketMifidModel inputBasket) {
		this.inputBasket = inputBasket;
	}
	public ListType getEsitiBasket() {
		return esitiBasket;
	}

	public void setEsitiBasket(ListType esitiBasket) {
		this.esitiBasket = esitiBasket;
	}

	public StringType getUserId() {
		return userId;
	}

	public void setUserId(StringType userId) {
		this.userId = userId;
	}

	public StringType getCodDispoSorgente() {
		return codDispoSorgente;
	}

	public void setCodDispoSorgente(StringType codDispoSorgente) {
		this.codDispoSorgente = codDispoSorgente;
	}

	public StringType getDescrizioneErrore() {
		return descrizioneErrore;
	}

	public void setDescrizioneErrore(StringType descrizioneErrore) {
		this.descrizioneErrore = descrizioneErrore;
	}	

	public StringType getFlagCheckRiprofilatura() {
		return flagCheckRiprofilatura;
	}

	public void setFlagCheckRiprofilatura(StringType flagCheckRiprofilatura) {
		this.flagCheckRiprofilatura = flagCheckRiprofilatura;
	}

	public StringType getFlagRDA() {
		return flagRDA;
	}

	public void setFlagRDA(StringType flagRDA) {
		this.flagRDA = flagRDA;
	}

	public MifidManlevaDataModel getMifidManlevaData() {
		return mifidManlevaData;
	}

	public void setMifidManlevaData(MifidManlevaDataModel mifidManlevaData) {
		this.mifidManlevaData = mifidManlevaData;
	}

	public IntegerType getStato() {
		return stato;
	}

	public void setStato(IntegerType stato) {
		this.stato = stato;
	}

	public boolean isSoloFirmaOlografa() {
		return soloFirmaOlografa;
	}

	public void setSoloFirmaOlografa(boolean soloFirmaOlografa) {
		this.soloFirmaOlografa = soloFirmaOlografa;
	}

	public ControlliConcentrazioneFiaDataModel getControlliConcentrazioneFiaData() {
		return controlliConcentrazioneFiaData;
	}

	public void setControlliConcentrazioneFiaData(ControlliConcentrazioneFiaDataModel controlliConcentrazioneFiaData) {
		this.controlliConcentrazioneFiaData = controlliConcentrazioneFiaData;
	}

	public StringType getIdPCA() {
		return idPCA;
	}

	public void setIdPCA(StringType idPCA) {
		this.idPCA = idPCA;
	}

	public StringType getIdQLTM() {
		return idQLTM;
	}

	public void setIdQLTM(StringType idQLTM) {
		this.idQLTM = idQLTM;
	}

}
