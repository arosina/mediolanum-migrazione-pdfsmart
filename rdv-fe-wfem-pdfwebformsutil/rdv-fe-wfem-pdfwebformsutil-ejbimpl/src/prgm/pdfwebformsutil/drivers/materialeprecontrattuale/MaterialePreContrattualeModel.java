package prgm.pdfwebformsutil.drivers.materialeprecontrattuale;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class MaterialePreContrattualeModel extends CommandDataModel {

	private static final String DESCR = "descr";

	private static final String LINK = "link";

	private static final long serialVersionUID = -5978124444922092708L;
	
	private StringType codiceFb = new StringType();
	private IntegerType idProposta = new IntegerType();
	private IntegerType idOrdine = new IntegerType();
	private StringType lineaDiBusiness = new StringType();	
	
	private StringType codiceProdotto = new StringType();
	private StringType tariffa = new StringType();
	private StringType tipoAdesioneFondo = new StringType();
	private IntegerType numRate = new IntegerType();
	
	private StringType codiceSicav = new StringType();
	private	BooleanType doubleChance = new BooleanType();
	private ListType listaOperazioni = new ListType(OperazioneMaterialePreContrattualeModel.class);
	
	private StringType resultMessage = new StringType();		
	private ListType materialeObbligatorio = new ListType(DocumentoMaterialePreContrattualeModel.class);
	private ListType materialeFacoltativo = new ListType(DocumentoMaterialePreContrattualeModel.class);
	private IntegerType resultCode = new IntegerType();
	
	public StringType getCodiceFb() {
		return codiceFb;
	}
	public void setCodiceFb(StringType codiceFb) {
		this.codiceFb = codiceFb;
	}
	public IntegerType getIdProposta() {
		return idProposta;
	}
	public void setIdProposta(IntegerType idProposta) {
		this.idProposta = idProposta;
	}
	public IntegerType getIdOrdine() {
		return idOrdine;
	}
	public void setIdOrdine(IntegerType idOrdine) {
		this.idOrdine = idOrdine;
	}
	public ListType getMaterialeObbligatorio() {
		return materialeObbligatorio;
	}
	public void setMaterialeObbligatorio(ListType materialeObbligatorio) {
		this.materialeObbligatorio = materialeObbligatorio;
	}
	public ListType getMaterialeFacoltativo() {
		return materialeFacoltativo;
	}
	public void setMaterialeFacoltativo(ListType materialeFacoltativo) {
		this.materialeFacoltativo = materialeFacoltativo;
	}
	public StringType getLineaDiBusiness() {
		return lineaDiBusiness;
	}
	public void setLineaDiBusiness(StringType lineaDiBusiness) {
		this.lineaDiBusiness = lineaDiBusiness;
	}
	public StringType getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(StringType resultMessage) {
		this.resultMessage = resultMessage;
	}

	public IntegerType getResultCode() {
		return resultCode;
	}
	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
	
	public String formattaMaterialePreContrattuale(ListType listMateriale, String field) {
		StringBuilder sb = new StringBuilder();
			
		for(int i = 0; i < listMateriale.size(); i++) {
			DocumentoMaterialePreContrattualeModel docModel = (DocumentoMaterialePreContrattualeModel)listMateriale.get(i);
			
			if(!docModel.getDescrizione().isNull() && !docModel.getLink().isNull()) {
				if (sb.length() > 0) {
					sb.append("|");
				}
				if (field.equals(MaterialePreContrattualeModel.LINK)) {
					sb.append(docModel.getLink()) ;
				}
				else {
					sb.append(docModel.getDescrizione()) ;
				}
			}
		}
		
		return sb.toString();
	}
	
	public String formattaDescrizioneMaterialePreContrattualeObbligatorio() {
		return formattaMaterialePreContrattuale(getMaterialeObbligatorio(), MaterialePreContrattualeModel.DESCR);
	}
	
	public String formattaLinkMaterialePreContrattualeObbligatorio() {
		return formattaMaterialePreContrattuale(getMaterialeObbligatorio(), MaterialePreContrattualeModel.LINK);
	}
	
	public String formattaDescrizioneMaterialePreContrattualeFacoltativo() {
		return formattaMaterialePreContrattuale(getMaterialeFacoltativo(), MaterialePreContrattualeModel.DESCR);
	}
	
	public String formattaLinkMaterialePreContrattualeFacoltativo() {
		return formattaMaterialePreContrattuale(getMaterialeFacoltativo(), MaterialePreContrattualeModel.LINK);
	}
		
	public boolean isDispositivaDaCarrello() {
		return !getIdProposta().isNull() && !getIdOrdine().isNull();
	}
	
	@SuppressWarnings("unchecked")
	public String calcolaHashIsins() {		
		StringBuilder sb = new StringBuilder();
		OperazioneMaterialePreContrattualeModel[] operazioni = new OperazioneMaterialePreContrattualeModel[listaOperazioni.size()]; 
				
		operazioni = (OperazioneMaterialePreContrattualeModel[]) listaOperazioni.getElements().toArray(operazioni);
		
		Arrays.sort(operazioni);
		
		for(OperazioneMaterialePreContrattualeModel operazioneModel : operazioni) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			sb.append(operazioneModel.getIsin());
		}
		
		return DigestUtils.sha256Hex(sb.toString());
	}
	
	public StringType getCodiceProdotto() {
		return codiceProdotto;
	}
	public void setCodiceProdotto(StringType codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}
	public StringType getCodiceSicav() {
		return codiceSicav;
	}
	public void setCodiceSicav(StringType codiceSicav) {
		this.codiceSicav = codiceSicav;
	}
	public BooleanType getDoubleChance() {
		return doubleChance;
	}
	public void setDoubleChance(BooleanType doubleChance) {
		this.doubleChance = doubleChance;
	}
	public ListType getListaOperazioni() {
		return listaOperazioni;
	}
	public void setListaOperazioni(ListType listaOperazioni) {
		this.listaOperazioni = listaOperazioni;
	}
	public StringType getTariffa() {
		return tariffa;
	}
	public void setTariffa(StringType tariffa) {
		this.tariffa = tariffa;
	}
	public StringType getTipoAdesioneFondo() {
		return tipoAdesioneFondo;
	}
	public void setTipoAdesioneFondo(StringType tipoAdesioneFondo) {
		this.tipoAdesioneFondo = tipoAdesioneFondo;
	}
	public IntegerType getNumRate() {
		return numRate;
	}
	public void setNumRate(IntegerType numRate) {
		this.numRate = numRate;
	}
	
}
