package prgm.ita.p.dac.model;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacModel extends DacTestataModel {

	// Campo string contenente codice javascript per gestire l'evidenziazione eei campi obbligatori
	// del documento in funzione dell'operazione selezionata
	private String htmlJavascriptCampiObbligatori = null;

	private DateType		dataRicezioneDocumenti = new DateType();
	
	private IntegerType		box = new IntegerType();
	private IntegerType   	codTipoSpedizione	= new IntegerType();
	private StringType	  	oraSpedizione		= new StringType();
	private BooleanType		reso				= new BooleanType();
	private BooleanType		isAutoSpuntata		= new BooleanType();
	
	// Tecnici
	private boolean 		forceLoadPlichiBusta = false;
	private IntegerType   	cassettaBox = new IntegerType();
	private String 			msg = new String();
	private StringType	  	barcode = new StringType();
	private boolean 		showAlert = false;
	private boolean 		nuovaDacDopoInvia = false;
	private boolean 		inClonazione = false;
	
	// Calcolati join DB
	private StringType	  	descrBox = new StringType();
	
	private ListType 		documenti	= new ListType(DocumentoModel.class);
	private ListType 		erroriDocumento	= new ListType(ErroreDocumentoModel.class);	
	private ListType 		plichiNonCompletiInSpedizione = new ListType(PlicoModel.class);
	
	private DocumentoModel  		documento		= new DocumentoModel();
	private ErroreDocumentoModel  	erroreDocumento = new ErroreDocumentoModel();
	
	// Dati MOM
	private StringType indirizzoAgenziaMOM = new StringType();
	
	// Cogestione
	private ListType elencoBC = new ListType(AgenteModel.class);
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel(){
		
		addCodDescField("codTipoSpedizione","TipiSpedizione");
		setCodTipoSpedizione(new IntegerType(Costanti.MEZZO_SPEDIZIONE_CORRIERE));
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setDatRicDocAsCombo(){
		CodDescDataList dl = new CodDescDataList();
		CodDescData d = null;
		DateType day = Tools.today();
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		day.addDays(-1);
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		day.addDays(-1);
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		day.addDays(-1);
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		day.addDays(-1);
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		day.addDays(-1);
		d = new CodDescData(); d.setCod(day.toString()); d.setDescr(d.getCod()); dl.addCodDescData(d); 
		addCodDescField("dataRicezioneDocumenti",dl);		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String htmlTitoloGrigliaDocumenti(){
		if(getModality() == READ_MODALITY || !isMgmPlichi()){
			String tit = "Distinta documenti";
			if(getTipoDac().equals(Costanti.TIPO_DAC_CARTOLINE))
				tit += "&nbsp;&nbsp;&nbsp;<span style=\"color:red;\">Attenzione !</span> Vengono visualizzati solo i primi 50 documenti";
			return tit;
		}
		StringBuffer res = new StringBuffer();
		res.append("<table class=\"text\" cellspacing=\"0\" cellpadding=\"0\"><tr>"+
						"<td>[<span style=\"color: silver;\" id=\"pinzaLabel\"><b>Pinza</b></span>]</td>"+
						"<td>&nbsp;&nbsp;[<span style=\"color: silver;\" id=\"spinzaLabel\"><b>Spinza</b></span>]</td>"+
					"</tr></table>");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String htmlToolbar(Template template){
		if(getFnc().isNull())
			return "";
		if(getFnc().equalsIgnoreCase(Costanti.FNC_GESTIONE))
			return htmlToolbarGestione(template);
		if(getFnc().equalsIgnoreCase(Costanti.FNC_SPUNTA))
			return htmlToolbarSpunta(template);
		return "";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String htmlToolbarGestione(Template template){
		StringBuffer res = new StringBuffer();
	    String enab = "true";
	    if(getDocumenti().size() == 0){
	    	enab = "false";
	    }else{
		    if(getIsAutoSpuntata().booleanValue() && !isSpuntabile())
		    	enab = "false";
	    }
	    
        res.append("<table><tr>");
        res.append("<td>"+template.action("inviaInSedeDac","enabled='"+enab+"' style='width:100;'")+"</td>");
        res.append("</tr></table>");
		
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String htmlToolbarSpunta(Template template){
		StringBuffer res = new StringBuffer();
		String enab = "true";
	    if(!isSpuntabile())
			enab = "false";
	    	
        res.append("<table><tr>");
        res.append("<td>"+template.action("spuntaDac","enabled='"+enab+"' style='width:100;'")+"</td>");
        res.append("</tr></table>");
		
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSpuntabile() {
		ListType documenti = getDocumenti();
		for(int i=0;i<documenti.size();i++){
			DocumentoModel doc = (DocumentoModel)documenti.get(i);
			if(doc.getEsito().isNull())
				return false;
			if(doc.getHasControlloFirmeClienteAttivo().booleanValue() && doc.getEsitoFirmaCliente().isNull())
				return false;
			if(doc.getHasControlloFirmeAgenteAttivo().booleanValue() && doc.getEsitoFirmaAgente().isNull())
				return false;
			if(isGestoreBarcode() && doc.getBarcode().isNull() && !doc.isEsitoDocumentoMancante())
				return false;
		}
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initReadonlySede(){
		if (getIdDac().isNull()) {
			setModality(Template.INSERT_MODALITY);
		} else {
			if (getStato().equals(Costanti.STATO_INCORSO) || getStato().equals(Costanti.STATO_APERTA)) {
				setModality(Template.UPDATE_MODALITY);
			} else {
				setModality(Template.READ_MODALITY);
			}
		}
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initReadonly(ClientSessionContext csc){
		if(getModality() == Template.READ_MODALITY) // Non la cambio se è già stata impostata a READ
			return;
		
		if(getFnc().equalsIgnoreCase(Costanti.FNC_GESTIONE)){
			boolean insMod = getCodUtenteMittente().equals(csc.getUserCode());
			if(csc.isAssistenteFB())
				insMod = getCodUtenteMittente().equals(csc.getCurrentLinkedUserCode()); 
			if(getStato().equals(Costanti.STATO_INCORSO) && getUfficio().equals(getUffMittente()) && insMod){
				if(getIdDac().isNull())
					setModality(Template.INSERT_MODALITY);
				else
					setModality(Template.UPDATE_MODALITY);
			}else{
				setModality(Template.READ_MODALITY);
			}
			return;
		}
		
		if(getFnc().equalsIgnoreCase(Costanti.FNC_SPUNTA)){
			if((getStato().equals(Costanti.STATO_SPEDITA) || getStato().equals(Costanti.STATO_APERTA)) &&
			   (getUbicazione().equals(Costanti.UFFICIO_CODING_SPUNTA) || getUbicazione().equals(Costanti.UFFICIO_COMDATA_SPUNTA) || getUbicazione().equals(Costanti.UFFICIO_C_GLOBAL_SPUNTA))){
				setModality(Template.UPDATE_MODALITY);
			}else{
				setModality(Template.READ_MODALITY);
			}
			return;
		}
		
		setModality(Template.READ_MODALITY);
		return;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DoubleType getImporto(){
		double res=0;
		for(int i=0;i<documenti.size();i++){
			DocumentoModel doc = (DocumentoModel)documenti.get(i);
			res += doc.getImporto().doubleValue();
		}
		return new DoubleType(res);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void removeDoc(StringType idDocumento){
		ListType documenti = getDocumenti();
		for(int i=0;i<documenti.size();i++){
			DocumentoModel doc = (DocumentoModel)documenti.get(i);
			if(doc.getIdDocumento().equals(idDocumento)) {
				getDocumenti().remove(i);
				return;
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PlicoModel getPlico(DocumentoModel documento, boolean inPinzatura){
		PlicoModel result = new PlicoModel();
		ListType docs = getDocumenti();
		for(int i=0;i<docs.size();i++){
			DocumentoModel doc = (DocumentoModel)docs.get(i);
			if(inPinzatura){
				if((!doc.getCodAggregatore().isNull() && doc.getCodAggregatore().toString().equals(documento.getCodAggregatore().toString()) ||
					 doc.getIsSelected().booleanValue()))
					result.getDocumenti().add(doc);
			}else{
				if(!doc.getCodAggregatore().isNull() && doc.getCodAggregatore().toString().equals(documento.getCodAggregatore().toString()))
					result.getDocumenti().add(doc);
			}
		}
		if(result.getDocumenti().size() <= 1)
			return new PlicoModel();
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getConsideraPlichiBusta(){
		if(isForceLoadPlichiBusta())
			return new BooleanType(true);
		if(getUfficio().equals(Costanti.UFFICIO_RETE)){ // Promotori
			if(getStato().equals(Costanti.STATO_INCORSO))
				return new BooleanType(false);
		}else if(getTipoDac().equals(Costanti.TIPO_DAC_SEDE)){ // Per le DAC
			if(inRicezione() || inSpedizione())
				return new BooleanType(false);
		}else{ // In sede sui Prit (non dac)
			if(getStato().equals(Costanti.STATO_INCORSO))
				return new BooleanType(false);
			if(isFaseDiSpunta() && !getStato().equals(Costanti.STATO_LAVORATA))
				return new BooleanType(false);
		}
		return new BooleanType(true);
	}
	
	public IntegerType getCodTipoSpedizione() {
		return codTipoSpedizione;
	}

	public void setCodTipoSpedizione(IntegerType codTipoSpedizione) {
		this.codTipoSpedizione = codTipoSpedizione;
	}

	public ListType getDocumenti() {
		return documenti;
	}

	public void setDocumenti(ListType documenti) {
		this.documenti = documenti;
	}

	public DocumentoModel getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoModel documento) {
		this.documento = documento;
	}

	public DateType getDataRicezioneDocumenti() {
		return dataRicezioneDocumenti;
	}

	public void setDataRicezioneDocumenti(DateType dataRicezioneDocumenti) {
		this.dataRicezioneDocumenti = dataRicezioneDocumenti;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public StringType getBarcode() {
		return barcode;
	}

	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}

	public StringType getOraSpedizione() {
		return oraSpedizione;
	}

	public void setOraSpedizione(StringType oraSpedizione) {
		this.oraSpedizione = oraSpedizione;
	}

	public BooleanType getReso() {
		return reso;
	}

	public void setReso(BooleanType reso) {
		this.reso = reso;
	}

	public BooleanType getIsAutoSpuntata() {
		return isAutoSpuntata;
	}

	public void setIsAutoSpuntata(BooleanType isAutoSpuntata) {
		this.isAutoSpuntata = isAutoSpuntata;
	}

	public boolean isShowAlert() {
		return showAlert;
	}

	public void setShowAlert(boolean showAlert) {
		this.showAlert = showAlert;
	}

	public boolean isNuovaDacDopoInvia() {
		return nuovaDacDopoInvia;
	}

	public void setNuovaDacDopoInvia(boolean nuovaDacDopoInvia) {
		this.nuovaDacDopoInvia = nuovaDacDopoInvia;
	}

	public boolean isInClonazione() {
		return inClonazione;
	}

	public void setInClonazione(boolean inClonazione) {
		this.inClonazione = inClonazione;
	}

	public ListType getPlichiNonCompletiInSpedizione() {
		return plichiNonCompletiInSpedizione;
	}

	public void setPlichiNonCompletiInSpedizione(ListType plichiNonCompletiInSpedizione) {
		this.plichiNonCompletiInSpedizione = plichiNonCompletiInSpedizione;
	}

	public IntegerType getBox() {
		return box;
	}

	public void setBox(IntegerType box) {
		this.box = box;
	}

	public IntegerType getCassettaBox() {
		return cassettaBox;
	}

	public void setCassettaBox(IntegerType cassettaBox) {
		this.cassettaBox = cassettaBox;
	}

	public StringType getDescrBox() {
		return descrBox;
	}

	public void setDescrBox(StringType descrBox) {
		this.descrBox = descrBox;
	}

	public ListType getErroriDocumento() {
		return erroriDocumento;
	}

	public void setErroriDocumento(ListType erroriDocumento) {
		this.erroriDocumento = erroriDocumento;
	}

	public ErroreDocumentoModel getErroreDocumento() {
		return erroreDocumento;
	}

	public void setErroreDocumento(ErroreDocumentoModel erroreDocumento) {
		this.erroreDocumento = erroreDocumento;
	}

	public boolean isForceLoadPlichiBusta() {
		return forceLoadPlichiBusta;
	}

	public void setForceLoadPlichiBusta(boolean forceLoadPlichiBusta) {
		this.forceLoadPlichiBusta = forceLoadPlichiBusta;
	}

	public String getHtmlJavascriptCampiObbligatori() {
		return htmlJavascriptCampiObbligatori;
	}

	public void setHtmlJavascriptCampiObbligatori(
			String htmlJavascriptCampiObbligatori) {
		this.htmlJavascriptCampiObbligatori = htmlJavascriptCampiObbligatori;
	}

	public StringType getIndirizzoAgenziaMOM() {
		return indirizzoAgenziaMOM;
	}

	public void setIndirizzoAgenziaMOM(StringType indirizzoAgenziaMOM) {
		this.indirizzoAgenziaMOM = indirizzoAgenziaMOM;
	}

	public ListType getElencoBC() {
		return elencoBC;
	}

	public void setElencoBC(ListType elencoBC) {
		this.elencoBC = elencoBC;
	}

}
