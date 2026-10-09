package prgm.ita.p.dac.model;

import java.util.Vector;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.Esiti;

/***********************************************************************************************/
/***********************************************************************************************/
public class DocumentoModel extends DocumentoKeyModel {
	
	private StringType 		idDac = new StringType();
	
	private StringType		idDocModificato = new StringType(); // Id del documento correntemente in memoria
	
	private StringType 		barcode = new StringType();
	
	private AgenteModel 	agente = new AgenteModel();
	private ClienteModel 	cliente = new ClienteModel();
	
	private StringType 		idReportAdeguatezza = new StringType();
	private StringType 		numeroContratto = new StringType();
	private IntegerType		codProdotto = new IntegerType();
	private IntegerType		codOperazione = new IntegerType();
	private IntegerType		codCassetta = new IntegerType();
	private StringType 		descrContratto = new StringType();
	private DoubleType		spese = new DoubleType();
	private ListType		mezziPagamento = new ListType(MezzoPagamentoModel.class);
	private ListType		mezziPagamentoDaCancellare = new ListType(MezzoPagamentoModel.class);
	
	private TimestampType 	dataOraRicezioneDocumento = new TimestampType();
	private BooleanType 	isInBusta			= new BooleanType(); //True in spedizione, false in chiusura (ricezione)
	private BooleanType 	nonPervenuto 		= new BooleanType();
	private BooleanType 	reso 				= new BooleanType();
	private BooleanType 	aggiuntoInRicezione = new BooleanType();
	private BooleanType 	isErroreSmistamento = new BooleanType();
	private ErroreDocumentoModel	erroreInLettura		= new ErroreDocumentoModel();
	private boolean					warningInLettura	= false; 
	private boolean					visualizzaAlert		= false;

	private StringType 		tipoControlloFirmeCliente = new StringType();
	private StringType 		tipoControlloFirmeAAF = new StringType();
	private StringType 		tipoControlloFirmePF = new StringType();
	
	private StringType 		esitoFirmaCliente = new StringType();
	private StringType 		codUtenteEsitoFirmaCliente = new StringType();
	private TimestampType 	dataOraEsitoFirmaCliente = new TimestampType();

	private StringType 		esitoFirmaAgente = new StringType();
	private StringType 		codUtenteEsitoFirmaAgente = new StringType();
	private TimestampType 	dataOraEsitoFirmaAgente = new TimestampType();
	
	// Per controllo firme agenti
	private IntegerType contatoreFirmeAAF = new IntegerType();
	private IntegerType contatoreFirmePF = new IntegerType();
	private IntegerType numeroEsitiFirmeAgenteInPrit = new IntegerType();
	private IntegerType numeroEsitiFirmeAAFInGiornata = new IntegerType();
	private IntegerType numeroEsitiFirmePFInGiornata = new IntegerType();
	
	// Legame con il mezzo di pagamento in caso di documento assegno
	private MezzoPagamentoModel	datiAssegno = new MezzoPagamentoModel();
	
	// Tecnici
	private StringType 		codInforeteEsterno = new StringType();
	private StringType 		nomeRisorsaEsterna = new StringType();
	private BooleanType 	isFirstInPlico = new BooleanType();
	private BooleanType 	isLastInPlico = new BooleanType();
	private StringType 		idPlico = new StringType();
	private StringType 		contestoAggregatore = new StringType();
	private StringType 		codAggregatore = new StringType();
	private StringType 		contestoAggregatoreOriginale = new StringType();
	private StringType 		codAggregatoreOriginale = new StringType();
	private IntegerType     esitoCorrente = new IntegerType();
	
	// Calcolati join DB
	private StringType 		descrProdotto = new StringType();
	private StringType 		descrOperazione = new StringType();

	// Per layout/gestione interna
	private DoubleType		importo = new DoubleType(); // Solo per totalizzazione Dac
	private boolean			visible = false;
	private IntegerType		absIndexMezzoPgSelezionato = new IntegerType();
	private BooleanType		nuovoOnInserisci = new BooleanType();
	private BooleanType		isSelected = new BooleanType();
	private StringType 		nessunMezzoDiPagamento = new StringType();
	private String 			titolo = "";
	private Vector 			changedProps = new Vector();
	private StringType 		cassetteOperazioniProdotto = new StringType();
	private String 			msgProdottoOperazioneNonPiuValidi = "";
	private boolean			errorsOnDati = false;
	private boolean			errorsOnMezziPg = false;
	
	// Per la gestione della attribuzione degli esiti senza "sporcare" quelli veri del modello
	private IntegerType   esitoNew = new IntegerType();
	private StringType 	  esitoFirmaClienteNew = new StringType();
	private StringType 	  esitoFirmaAgenteNew = new StringType();
	
	// Storia documento
	private ListType	  storiaDocumento = new ListType(DacTestataModel.class);
	private PlicoModel	  plicoDocumento = new PlicoModel();
	
	// Operazione Report adeguatezza
	private String operazioneReportAdeguatezza = null;
	// Operazione Raccomandazione IDD
	private String operazioneRaccomandazioneIdd = null;
	
	// MOM
	private StringType tipoAgevolazione = new StringType("");
	private StringType idAttivitaAgevolazione = new StringType();
	private StringType idAgevolazione = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel(){
		
		addCodDescField("stato","StatiDocumento");
		addCodDescField("esito","EsitiDocumento");
		
		addCodDescField("codProdotto","Prodotti");
		addCodDescField("codOperazione","Operazioni");
		if(Configuration.getInstance().isOnlineEnvironment()) {
			addCodDescField("codCassetta","Cassette");
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isDocumentoAssegno(){
		return getDatiAssegno().getIdDocumento().isNull() ? false : true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String assegnoDescrOnPrint(){
		if(!isDocumentoAssegno())
			return "";
		MezzoPagamentoModel datiAssegno = getDatiAssegno();
		String res = "Assegno";
		if(datiAssegno.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
			res += " ESTERO";
		else
			res += " ITALIA";
		if(!datiAssegno.getNumAssegno().isNull())
			res += " n&deg; "+datiAssegno.getNumAssegno();
		if(!datiAssegno.getImporto().isNull())
			res += " - "+datiAssegno.getImporto()+" ("+datiAssegno.getCodDivisa()+")";
		if(!datiAssegno.getLuogoEmissione().isNull())
			res += " emesso a "+datiAssegno.getLuogoEmissione();
		if(!datiAssegno.getDataEmissione().isNull())
			res += " in data "+datiAssegno.getDataEmissione();
		if(datiAssegno.getFlagTrasferibile().equals("S"))
			res += " Trasferibile";
		else if(datiAssegno.getFlagTrasferibile().equals("N"))
			res += " NON Trasferibile";
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEsitoDocumentoMancante(){
		if(getEsito().equals(Costanti.ESITO_DOC_DOCMANCANTE) 		||
		   getEsito().equals(Costanti.ESITO_DOC_DOCMANCANTESEGN) 	||
		   getEsito().equals(Costanti.ESITO_DOC_RIGADOPPIAANNULLATA))
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isTabFirmeClienteVisible(){
		if(inSpedizione() || inRicezione())
			return false;
		
    	if(!getEsitoFirmaCliente().isNull() || getHasControlloFirmeClienteAttivo().booleanValue())
			return true;
		
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isTabFirmeAgenteVisible(){
		if(inSpedizione() || inRicezione())
			return false;
		
    	if(!getEsitoFirmaAgente().isNull() || getHasControlloFirmeAgenteAttivo().booleanValue())
			return true;
		
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String changedStyle(String propName){
		if(changedProps.contains(propName))
			return " style='background-color:coral;' ";
		return "";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Vector initChangedProps(DocumentoModel other){

		changedProps.removeAllElements();
		
		if(isDocumentoAssegno()){
			changedProps.addAll(getDatiAssegno().initChangedProps(other.getDatiAssegno()));
		}else{
			if(!getNumeroContratto().toString().equals(other.getNumeroContratto().toString()))
				changedProps.add("numeroContratto");
	
			if(!getCodProdotto().toString().equals(other.getCodProdotto().toString()))
				changedProps.add("codProdotto");
	
			if(!getCodOperazione().toString().equals(other.getCodOperazione().toString()))
				changedProps.add("codOperazione");
			
			if(getAgente().initChangedProps(other.getAgente()).size() > 0)
				changedProps.add("agente");
			
			if(getCliente().initChangedProps(other.getCliente()).size() > 0)
				changedProps.add("cliente");
			
			ListType ml1 = null;
			ListType ml2 = null;
			if(isDocAssegniAbilitati()){
				ml1 = getMezziPagamentoNonAssegno();
				ml2 = other.getMezziPagamentoNonAssegno();
			}else{
				ml1 = getMezziPagamento();
				ml2 = other.getMezziPagamento();
			}
			boolean changed = false;
			for(int i=0;i<ml1.size();i++){
				boolean trovato = false;
				MezzoPagamentoModel m1 = (MezzoPagamentoModel)ml1.get(i);
				for(int j=0;j<ml2.size();j++){
					MezzoPagamentoModel m2 = (MezzoPagamentoModel)ml2.get(j);
					if(m1.isEqual(m2)){
						trovato = true;
						break;
					}
				}
				if(!trovato){
					changed = true;
					break;
				}
			}
			if(changed || ml1.size() != ml2.size())
				changedProps.add("mezziPagamento");
		
		}
		return changedProps;		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(DocumentoModel other){
		if(isDocumentoAssegno()){
			if(!getDatiAssegno().isEqual(other.getDatiAssegno()))
				return false;
		}else{
			if(!getNumeroContratto().toString().equals(other.getNumeroContratto().toString()))
				return false;

			if(!getCodProdotto().toString().equals(other.getCodProdotto().toString()))
				return false;

			if(!getCodOperazione().toString().equals(other.getCodOperazione().toString()))
				return false;
		
			if(!getAgente().isEqual(other.getAgente()))
				return false;
		
			if(!getCliente().isEqual(other.getCliente()))
				return false;
		
			if(isDocAssegniAbilitati()){
				if(getMezziPagamentoNonAssegno().size() != other.getMezziPagamentoNonAssegno().size())
					return false;
			}else{
				if(getMezziPagamento().size() != other.getMezziPagamento().size())
					return false;
			}

			ListType ml1 = null;
			ListType ml2 = null;
			if(isDocAssegniAbilitati()){
				ml1 = getMezziPagamentoNonAssegno();
				ml2 = other.getMezziPagamentoNonAssegno();
			}else{
				ml1 = getMezziPagamento();
				ml2 = other.getMezziPagamento();
			}
			for(int i=0;i<ml1.size();i++){
				boolean trovato = false;
				MezzoPagamentoModel m1 = (MezzoPagamentoModel)ml1.get(i);
				for(int j=0;j<ml2.size();j++){
					MezzoPagamentoModel m2 = (MezzoPagamentoModel)ml2.get(j);
					if(m1.isEqual(m2)){
						trovato = true;
						break;
					}
				}
				if(!trovato)
					return false;
			}
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ListType getMezziPagamentoAssegno() {
		ListType result = new ListType(MezzoPagamentoModel.class);
		ListType mp = getMezziPagamento();
		for(int i=0;i<mp.size();i++){
			MezzoPagamentoModel m = (MezzoPagamentoModel)mp.get(i);
			if(m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
				result.add(m);
		}
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ListType getMezziPagamentoNonAssegno() {
		ListType result = new ListType(MezzoPagamentoModel.class);
		ListType mp = getMezziPagamento();
		for(int i=0;i<mp.size();i++){
			MezzoPagamentoModel m = (MezzoPagamentoModel)mp.get(i);
			if(!m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) && !m.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
				result.add(m);
		}
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clonaDocumento(DocumentoModel other){
		setNumeroContratto(new StringType(other.getNumeroContratto().toString()));
		setCodProdotto(new IntegerType(other.getCodProdotto().toString()));
		setCodOperazione(new IntegerType(other.getCodOperazione().toString()));
		setCodCassetta(new IntegerType(other.getCodCassetta().toString()));
		setSpese(new DoubleType(other.getSpese().toString()));
		try{setAgente((AgenteModel)Tools.cloneObject(other.getAgente()));}catch(Exception e){}
		try{setCliente((ClienteModel)Tools.cloneObject(other.getCliente()));}catch(Exception e){}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isOperazioneConControlloFirmeCliente(){
		if(getTipoControlloFirmeCliente().equals(Costanti.CONTROLLO_FIRME_NORMALE) ||
	       getTipoControlloFirmeCliente().equals(Costanti.CONTROLLO_FIRME_CONGIUNTO))
			return true;
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isOperazioneConControlloFirmeAgente(){
		if(getAgente().getTipoAgente().equals(Costanti.TIPO_AGENTE_AAF)){
			if(getTipoControlloFirmeAAF().equals(Costanti.CONTROLLO_FIRME_NORMALE))
				return true;
		}
		if(getAgente().getTipoAgente().equals(Costanti.TIPO_AGENTE_PF)){
			if(getTipoControlloFirmePF().equals(Costanti.CONTROLLO_FIRME_NORMALE))
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getHasControlloFirmeClienteAttivo(){
		
		if(!isFaseDiSpunta())
			return new BooleanType(false);
		
		if(!isOperazioneConControlloFirmeCliente())
			return new BooleanType(false);

		if(isCtrlFirmeDisattivo())
			return new BooleanType(false);
		
		if(isEsitoDocumentoMancante())
			return new BooleanType(false);
		
		if(getCliente().getNumContiCorrenti().intValue() == 0)
			return new BooleanType(false);
		
		return new BooleanType(true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getHasControlloFirmeAgenteAttivo(){
		
		if(!isFaseDiSpunta())
			return new BooleanType(false);
		
		if(!isOperazioneConControlloFirmeAgente())
			return new BooleanType(false);

		if(isCtrlFirmeAgenteDisattivo())
			return new BooleanType(false);
		
		if(isEsitoDocumentoMancante())
			return new BooleanType(false);
		
		if(getAgente().getNumContiCorrenti().intValue() == 0)
			return new BooleanType(false);
		
		if(getNumeroEsitiFirmeAgenteInPrit().intValue() > 0)
			return new BooleanType(false);
		
		if(getAgente().getTipoAgente().equals(Costanti.TIPO_AGENTE_AAF) &&
		   getNumeroEsitiFirmeAAFInGiornata().intValue() < getContatoreFirmeAAF().intValue())
				return new BooleanType(true);
		
		if(getAgente().getTipoAgente().equals(Costanti.TIPO_AGENTE_PF) &&
		   getNumeroEsitiFirmePFInGiornata().intValue() < getContatoreFirmePF().intValue())
				return new BooleanType(true);

		return new BooleanType(false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getEsitoFirmaClienteAutomatico(){
		
		if(!isOperazioneConControlloFirmeCliente())
			return null;

		if(getEsito().equals(Costanti.ESITO_DOC_RIGADOPPIAANNULLATA))
			return null;

		if(getEsito().equals(Costanti.ESITO_DOC_DOCMANCANTE) 		||
		   getEsito().equals(Costanti.ESITO_DOC_DOCMANCANTESEGN))
			return Costanti.CF_DOC_MANCANTE;
				
		if(isCtrlFirmeDisattivo() && getEsitoFirmaCliente().isNull())
			return Costanti.CF_NON_ATTIVO;
		
		if(getCliente().getNumContiCorrenti().intValue() == 0)
			return Costanti.CF_CLI_SENZA_CONTI;
		
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initTitolo(DacModel dac) {
		String descrProd = null;
		if(!getCodProdotto().isNull())
			descrProd = getDescValue("codProdotto");
		String descrOper = null;
		if(!getCodOperazione().isNull())
			descrOper = getDescValue("codOperazione");
		initTitolo(dac, descrProd, descrOper);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initTitolo(DacModel dac, String descrProd, String descrOper) {
		if(getIdDocumento().isNull()){
			this.titolo = "Nuovo documento Prit";
		}else{
			this.titolo = ""; 
			if(dac != null){
				ListType documenti = dac.getDocumenti();
				for(int i=0;i<documenti.size();i++){
					DocumentoModel doc = (DocumentoModel)documenti.get(i);
					if(doc.getIdDocumento().equals(getIdDocumento())){
						this.titolo += "("+(i+1)+") ";
						break;
					}
				}
			}
		}
		if(isDocumentoAssegno())
			titolo += "<b>&nbsp;ASSEGNO&nbsp;-&nbsp;</b> ";
		if(descrProd != null && descrProd.length() > 0)
			this.titolo += descrProd;
		if(descrOper != null && descrOper.length() > 0)
			this.titolo += " / "+descrOper;
		if(!getCodInforeteEsterno().isNull())
			this.titolo += " - Prop.: "+getCodInforeteEsterno();
		this.titolo += "<span id='docTitleDescrContr' style='font-size:9;'>";
		if(!getDescrContratto().isNull())
			this.titolo += "&nbsp;&nbsp;&nbsp;&#9658;&nbsp;"+getDescrContratto()+"&nbsp;&#9668;";
		this.titolo += "</span>";
	}
	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getIdPlico() {
		if(idPlico != null)
			idPlico.setEditable(false);
		return idPlico;
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
		String w = "style='width:110;'";
		StringBuffer res = new StringBuffer();

		res.append("<table width='100%'>");
		
		res.append(   "<tr>");
		res.append(     "<td valign='top'>");
		res.append(      "<table>");
    	
		res.append(   		"<tr>");
		res.append(     		"<td id='toolbarDocCont'>");
		res.append(      			"<table>");
	    if(getIdDocumento().isNull()){
	    	res.append(					"<tr><td>");
	    	res.append(						template.action("inserisciDocumentoAction",w));
	    	res.append(					"</td></tr>");
	    }else{
	    	res.append(					"<tr><td>");
	    	res.append(						template.action("salvaDocumentoAction",w));
	    	res.append(					"</td></tr>");
	    	res.append(					"<tr><td>");
	    	res.append(						template.action("cancellaDocumentoAction",w));
	    	res.append(					"</td></tr>");
	    }
		res.append(      			"</table>");
		res.append(     		"</td>");
		res.append(   		"</tr>");
    	// /////////////////////
    	
		// Gestione firme cliente
		res.append(htmlToolbarFirmeCliente(template));
    	
		// Gestione firme agente
		res.append(htmlToolbarFirmeAgente(template));

		res.append(      "</table>");
		res.append(    "</td>");
		res.append(  "</tr>");
		  
		res.append("</table>");
		return res.toString();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String htmlToolbarSpunta(Template template){
		
		String w = "style='width:110;'";
		StringBuffer res = new StringBuffer();
		
		res.append("<table width='100%' height='100%'>");
		
		res.append(   "<tr>");
		res.append(     "<td valign='top' height='100%'>");
		res.append(      "<table>");
    	
		// Gestione spunta
		res.append(   		"<tr>");
		res.append(     		"<td id='toolbarDocCont'>");
		res.append(      			"<table>");
		if(getEsito().equals(Costanti.ESITO_DOC_AGGIUNTO)){
		    if(getIdDocumento().isNull()){
		    	res.append(					"<tr><td>");
		    	res.append(						template.action("inserisciDocumentoAction",w));
		    	res.append(					"</td></tr>");
		    }else{
		    	res.append(		    		"<tr><td>");
	    		res.append(			    		template.action("salvaDocumentoAction",w));
	    		res.append(		    		"</td></tr>");
	    		res.append(		    		"<tr><td>");
	    		res.append(			   			template.action("cancellaDocumentoAction",w));
	    		res.append(		    		"</td></tr>");
		    }
		}else{
			res.append(		    		"<tr><td>");
	    	res.append(						template.action("accettaDocumentoAction","onexecute='esitaDocumento(\\\""+Costanti.ESITO_DOC_ACCETTATO+"\\\");' "+
	    																			 "text='"+Esiti.getDescrEsitoDocumento(Costanti.ESITO_DOC_ACCETTATO)+"' "+w));
	    	res.append(		    		"</td></tr>");
	    	res.append(		    		"<tr><td>");
	    	res.append(						template.action("docMancanteDocumentoAction","onexecute='esitaDocumento(\\\""+Costanti.ESITO_DOC_DOCMANCANTE+"\\\");' "+
	    																			     "text='"+Esiti.getDescrEsitoDocumento(Costanti.ESITO_DOC_DOCMANCANTE)+"' "+w));
	    	res.append(		    		"</td></tr>");
	    	
	    	res.append(		    		"<tr><td>");
	    	res.append(						"<select class='text' id='altreAction' onchange='esitaDocumento(this.value);' "+w+">");
	    	res.append(							"<option value=''>------</option>");
	    	if(!isDocAssegniAbilitati())
	    		res.append(							"<option value='"+Costanti.ESITO_DOC_ASSEGNOMANCANTE+"'>"+Esiti.getDescrEsitoDocumento(Costanti.ESITO_DOC_ASSEGNOMANCANTE)+"</option>");
	    	res.append(							"<option value='"+Costanti.ESITO_DOC_DOCMANCANTESEGN+"'>"+Esiti.getDescrEsitoDocumento(Costanti.ESITO_DOC_DOCMANCANTESEGN)+"</option>");
	    	res.append(							"<option value='"+Costanti.ESITO_DOC_RIGADOPPIAANNULLATA+"'>"+Esiti.getDescrEsitoDocumento(Costanti.ESITO_DOC_RIGADOPPIAANNULLATA)+"</option>");
	    	res.append(						"</select>");
	    	res.append(		    		"</td></tr>");    		
    	}
		res.append(      			"</table>");
		res.append(     		"</td>");
		res.append(   		"</tr>");
    	// /////////////////////
    	
		// Gestione firme cliente
		res.append(htmlToolbarFirmeCliente(template));
    	
		// Gestione firme agente
		res.append(htmlToolbarFirmeAgente(template));

    	res.append(      "</table>");
		res.append(    "</td>");
		res.append(  "</tr>");
		  
		res.append("</table>");
		return res.toString();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String htmlToolbarFirmeCliente(Template template){
		String w = "style='width:110;'";
		String redw = "style='width:110;color:red;'";
		String greenw = "style='width:110;color:green;'";

		StringBuffer res = new StringBuffer();

		res.append(   		"<tr>");
		res.append(     		"<td id='toolbarFirmeClienteCont'>");
		
    	if(getEsito().isNull() || 
    	   getEsitoFirmaCliente().equals(Costanti.CF_CLI_SENZA_CONTI) || 
    	   getEsitoFirmaCliente().equals(Costanti.CF_DOC_MANCANTE)){
    		res.append(     	"</td>");
    		res.append( 	"</tr>");
    		return res.toString();		
    	}
    	
    	if(getHasControlloFirmeClienteAttivo().booleanValue()){
    		res.append(      			"<table>");
	    	res.append(		    			"<tr><td>");
	    	res.append(							template.action("firmaConformeAction","onexecute='esitaFirmaCliente(\\\""+Costanti.CF_CONFORME+"\\\");' "+
	    																			  "text='"+Esiti.getDescrEsitoFirme(Costanti.CF_CONFORME)+"' "+greenw));
	    	res.append(		    			"</td></tr>");
	    	res.append(		    			"<tr><td>");
	    	res.append(							template.action("firmaNonConformeAction","onexecute='esitaFirmaCliente(\\\""+Costanti.CF_NONCONFORME+"\\\");' "+
	    																				 "text='"+Esiti.getDescrEsitoFirme(Costanti.CF_NONCONFORME)+"' "+redw));
	    	res.append(		    			"</td></tr>");
	    	
	    	res.append(		    			"<tr><td>");
	    	res.append(							"<select class='text' id='altreAction' onchange='esitaFirmaCliente(this.value);' "+w+">");
	    	res.append(								"<option value=''>------</option>");
	    	res.append(								"<option value='"+Costanti.CF_FIRMAASSENTE+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_FIRMAASSENTE)+"</option>");
	    	res.append(								"<option value='"+Costanti.CF_FUORIPROCEDURA+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_FUORIPROCEDURA)+"</option>");
	    	res.append(								"<option value='"+Costanti.CF_NONDISPONIBILE+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_NONDISPONIBILE)+"</option>");
	    	res.append(								"<option value='"+Costanti.CF_NONTROVATA+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_NONTROVATA)+"</option>");
	    	res.append(								"<option value='"+Costanti.CF_ERRATA+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_ERRATA)+"</option>");
	    	res.append(							"</select>");
	    	res.append(		    			"</td></tr>");
			res.append(      			"</table>");
    	}
    	
		res.append(     		"</td>");
		res.append(   		"</tr>");
		return res.toString();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String htmlToolbarFirmeAgente(Template template){
		String w = "style='width:110;'";
		String redw = "style='width:110;color:red;'";
		String greenw = "style='width:110;color:green;'";

		StringBuffer res = new StringBuffer();

		res.append(   		"<tr>");
		res.append(     		"<td id='toolbarFirmeAgenteCont'>");
		
    	if(getEsito().isNull()){
    		res.append(     	"</td>");
    		res.append( 	"</tr>");
    		return res.toString();		
    	}
    	
    	if( getHasControlloFirmeAgenteAttivo().booleanValue() ||
    	   (isFaseDiSpunta() && !getEsitoFirmaAgente().isNull())){
    		res.append(      			"<table>");
	    	res.append(		    			"<tr><td>");
	    	res.append(							template.action("firmaConformeAction","onexecute='esitaFirmaAgente(\\\""+Costanti.CF_CONFORME+"\\\");' "+
	    																			  "text='"+Esiti.getDescrEsitoFirme(Costanti.CF_CONFORME)+"' "+greenw));
	    	res.append(		    			"</td></tr>");
	    	res.append(		    			"<tr><td>");
	    	res.append(							template.action("firmaNonConformeAction","onexecute='esitaFirmaAgente(\\\""+Costanti.CF_NONCONFORME+"\\\");' "+
	    																				 "text='"+Esiti.getDescrEsitoFirme(Costanti.CF_NONCONFORME)+"' "+redw));
	    	res.append(		    			"</td></tr>");
	    	
	    	res.append(		    			"<tr><td>");
	    	res.append(							"<select class='text' id='altreAction' onchange='esitaFirmaAgente(this.value);' "+w+">");
	    	res.append(								"<option value=''>------</option>");
	    	res.append(								"<option value='"+Costanti.CF_FIRMAASSENTE+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_FIRMAASSENTE)+"</option>");
	    	res.append(								"<option value='"+Costanti.CF_FIRMAINCOPIA+"'>"+Esiti.getDescrEsitoFirme(Costanti.CF_FIRMAINCOPIA)+"</option>");
	    	res.append(							"</select>");
	    	res.append(		    			"</td></tr>");

	    	
			res.append(      			"</table>");
    	}
    	
		res.append(     		"</td>");
		res.append(   		"</tr>");
		return res.toString();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setEditabilitaMezziPg(boolean isReadonly) throws Exception{
		ListType mezziPg = getMezziPagamento();
		if(isReadonly){ // In modalità readonly imposto a true l'editabilità in modo che le righe non vengano collassate
			for (int i=0;i<mezziPg.size();i++){
				MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
				mezzoPg.setIsEditabile(new BooleanType(true));
			}
			return;
		}
		
		if(!getFnc().equalsIgnoreCase(Costanti.FNC_SPUNTA)){
			for (int i=0;i<mezziPg.size();i++){
				MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
				if (isDocAssegniAbilitati() && 
					(mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO)) && 
					!Tools.containsTypeErrors(mezzoPg))
					mezzoPg.getCodTipoPagamento().setEditable(false);
				mezzoPg.setIsEditabile(new BooleanType(true));
			}
			return;
		}
		
		// Se spunta...
		if(!isDocAssegniAbilitati()){
			for (int i=0;i<mezziPg.size();i++){
				MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
				mezzoPg.setIsEditabile(new BooleanType(true));
			}
			return;
		}

		// Editabilità mezzi pagamento in spunta e con gestione documenti-assegni abilitata
		for (int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			mezzoPg.setIsEditabile(new BooleanType(true));

			// I mezzi di pagamento non assegni sono sempre editabili
			if(!mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) && !mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
				continue;

			// Se sto inputando un valore rispetto a quello letto da db è ovviamente visibile ed editabile
			if(!mezzoPg.getCodTipoPagamento().equals(mezzoPg.getCodTipoPagamentoSuDB()))
				continue;
			
			// Se il promotore ha attiva la gestione dei documenti-assegno posso editare solo gli assegni aggiunti
			if(!mezzoPg.getEsitoDocumentoAssegno().equals(Costanti.ESITO_DOC_AGGIUNTO)){
				mezzoPg.setIsEditabile(new BooleanType(false));
				mezzoPg.getCodTipoPagamento().setEditable(false);
				mezzoPg.getNumAssegno().setEditable(false);
				mezzoPg.getImporto().setEditable(false);
				mezzoPg.getCodDivisa().setEditable(false);
				mezzoPg.getBanca().setEditable(false);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getNumMezziPgEditabili(){
		int count = 0;
		ListType mezziPg = getMezziPagamento();
		for (int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(mezzoPg.getIsEditabile().booleanValue())
				count++;
		}		
		return count;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isDocumentoReportAdeguatezza(){
		if(getOperazioneReportAdeguatezza()==null)
			return false;
		return getOperazioneReportAdeguatezza().indexOf("|"+getCodOperazione().toString()+"|")>=0 ? true : false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isDocumentoRaccomandazioneIdd(){
		if(getOperazioneRaccomandazioneIdd()==null)
			return false;
		return getOperazioneRaccomandazioneIdd().indexOf("|"+getCodOperazione().toString()+"|")>=0 ? true : false;
	}
	
	public String getTitolo(){
		return titolo;
	}
	
	public StringType getDescrProdotto() {
		return descrProdotto;
	}
	public void setDescrProdotto(StringType descrProdotto) {
		this.descrProdotto = descrProdotto;
	}
	public StringType getDescrOperazione() {
		return descrOperazione;
	}
	public void setDescrOperazione(StringType descrOperazione) {
		this.descrOperazione = descrOperazione;
	}
	public ClienteModel getCliente() {
		return cliente;
	}
	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public AgenteModel getAgente() {
		return agente;
	}
	public void setAgente(AgenteModel agente) {
		this.agente = agente;
	}

	public IntegerType getCodProdotto() {
		return codProdotto;
	}

	public void setCodProdotto(IntegerType codProdotto) {
		this.codProdotto = codProdotto;
	}

	public IntegerType getCodOperazione() {
		return codOperazione;
	}

	public void setCodOperazione(IntegerType codOperazione) {
		this.codOperazione = codOperazione;
	}

	public ListType getMezziPagamento() {
		return mezziPagamento;
	}

	public void setMezziPagamento(ListType mezziPagamento) {
		this.mezziPagamento = mezziPagamento;
	}

	public IntegerType getAbsIndexMezzoPgSelezionato() {
		return absIndexMezzoPgSelezionato;
	}

	public void setAbsIndexMezzoPgSelezionato(IntegerType absIndexMezzoPgSelezionato) {
		this.absIndexMezzoPgSelezionato = absIndexMezzoPgSelezionato;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public StringType getIdDac() {
		return idDac;
	}

	public void setIdDac(StringType idDac) {
		this.idDac = idDac;
	}

	public StringType getNomeRisorsaEsterna() {
		return nomeRisorsaEsterna;
	}

	public void setNomeRisorsaEsterna(StringType nomeRisorsaEsterna) {
		this.nomeRisorsaEsterna = nomeRisorsaEsterna;
	}

	public BooleanType getNuovoOnInserisci() {
		return nuovoOnInserisci;
	}

	public void setNuovoOnInserisci(BooleanType nuovoOnInserisci) {
		this.nuovoOnInserisci = nuovoOnInserisci;
	}

	public DoubleType getSpese() {
		return spese;
	}

	public void setSpese(DoubleType spese) {
		this.spese = spese;
	}

	public StringType getCodAggregatore() {
		return codAggregatore;
	}

	public void setCodAggregatore(StringType codAggregatore) {
		this.codAggregatore = codAggregatore;
	}

	public StringType getNessunMezzoDiPagamento() {
		return nessunMezzoDiPagamento;
	}

	public void setNessunMezzoDiPagamento(StringType nessunMezzoDiPagamento) {
		this.nessunMezzoDiPagamento = nessunMezzoDiPagamento;
	}

	public IntegerType getCodCassetta() {
		return codCassetta;
	}

	public void setCodCassetta(IntegerType codCassetta) {
		this.codCassetta = codCassetta;
	}

	public StringType getDescrContratto() {
		return descrContratto;
	}

	public void setDescrContratto(StringType descrContratto) {
		this.descrContratto = descrContratto;
	}

	public StringType getBarcode() {
		return barcode;
	}

	public void setBarcode(StringType barcode) {
		this.barcode = barcode;
	}

	public void setIdPlico(StringType idPlico) {
		this.idPlico = idPlico;
	}

	public BooleanType getIsLastInPlico() {
		return isLastInPlico;
	}

	public void setIsLastInPlico(BooleanType isLastInPlico) {
		this.isLastInPlico = isLastInPlico;
	}

	public BooleanType getIsFirstInPlico() {
		return isFirstInPlico;
	}

	public void setIsFirstInPlico(BooleanType isFirstInPlico) {
		this.isFirstInPlico = isFirstInPlico;
	}

	public BooleanType getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(BooleanType isSelected) {
		this.isSelected = isSelected;
	}

	public BooleanType getNonPervenuto() {
		return nonPervenuto;
	}

	public void setNonPervenuto(BooleanType nonPervenuto) {
		this.nonPervenuto = nonPervenuto;
	}

	public BooleanType getAggiuntoInRicezione() {
		return aggiuntoInRicezione;
	}

	public void setAggiuntoInRicezione(BooleanType aggiuntoInRicezione) {
		this.aggiuntoInRicezione = aggiuntoInRicezione;
	}

	public StringType getIdDocModificato() {
		return idDocModificato;
	}

	public void setIdDocModificato(StringType idDocModificato) {
		this.idDocModificato = idDocModificato;
	}

	public BooleanType getReso() {
		return reso;
	}

	public void setReso(BooleanType reso) {
		this.reso = reso;
	}

	public BooleanType getIsErroreSmistamento() {
		return isErroreSmistamento;
	}

	public void setIsErroreSmistamento(BooleanType isErroreSmistamento) {
		this.isErroreSmistamento = isErroreSmistamento;
	}

	public StringType getTipoControlloFirmeCliente() {
		return tipoControlloFirmeCliente;
	}

	public void setTipoControlloFirmeCliente(StringType tipoControlloFirmeCliente) {
		this.tipoControlloFirmeCliente = tipoControlloFirmeCliente;
	}

	public StringType getEsitoFirmaCliente() {
		return esitoFirmaCliente;
	}

	public void setEsitoFirmaCliente(StringType esitoFirmaCliente) {
		this.esitoFirmaCliente = esitoFirmaCliente;
	}

	public StringType getCodInforeteEsterno() {
		return codInforeteEsterno;
	}

	public void setCodInforeteEsterno(StringType codInforeteEsterno) {
		this.codInforeteEsterno = codInforeteEsterno;
	}

	public StringType getCodUtenteEsitoFirmaCliente() {
		return codUtenteEsitoFirmaCliente;
	}

	public void setCodUtenteEsitoFirmaCliente(StringType codUtenteEsitoFirmaCliente) {
		this.codUtenteEsitoFirmaCliente = codUtenteEsitoFirmaCliente;
	}

	public TimestampType getDataOraEsitoFirmaCliente() {
		return dataOraEsitoFirmaCliente;
	}

	public void setDataOraEsitoFirmaCliente(TimestampType dataOraEsitoFirmaCliente) {
		this.dataOraEsitoFirmaCliente = dataOraEsitoFirmaCliente;
	}

	public IntegerType getEsitoNew() {
		return esitoNew;
	}

	public void setEsitoNew(IntegerType esitoNew) {
		this.esitoNew = esitoNew;
	}

	public StringType getEsitoFirmaClienteNew() {
		return esitoFirmaClienteNew;
	}

	public void setEsitoFirmaClienteNew(StringType esitoFirmaClienteNew) {
		this.esitoFirmaClienteNew = esitoFirmaClienteNew;
	}

	public StringType getContestoAggregatore() {
		return contestoAggregatore;
	}

	public void setContestoAggregatore(StringType contestoAggregatore) {
		this.contestoAggregatore = contestoAggregatore;
	}

	public ListType getStoriaDocumento() {
		return storiaDocumento;
	}

	public void setStoriaDocumento(ListType storiaDocumento) {
		this.storiaDocumento = storiaDocumento;
	}

	public StringType getCassetteOperazioniProdotto() {
		return cassetteOperazioniProdotto;
	}

	public void setCassetteOperazioniProdotto(StringType cassetteOperazioniProdotto) {
		this.cassetteOperazioniProdotto = cassetteOperazioniProdotto;
	}

	public String getMsgProdottoOperazioneNonPiuValidi() {
		return msgProdottoOperazioneNonPiuValidi;
	}

	public void setMsgProdottoOperazioneNonPiuValidi(
			String msgProdottoOperazioneNonPiuValidi) {
		this.msgProdottoOperazioneNonPiuValidi = msgProdottoOperazioneNonPiuValidi;
	}

	public boolean isErrorsOnDati() {
		return errorsOnDati;
	}

	public void setErrorsOnDati(boolean errorsOnDati) {
		this.errorsOnDati = errorsOnDati;
	}

	public boolean isErrorsOnMezziPg() {
		return errorsOnMezziPg;
	}

	public void setErrorsOnMezziPg(boolean errorsOnMezziPg) {
		this.errorsOnMezziPg = errorsOnMezziPg;
	}

	public PlicoModel getPlicoDocumento() {
		return plicoDocumento;
	}

	public void setPlicoDocumento(PlicoModel plicoDocumento) {
		this.plicoDocumento = plicoDocumento;
	}

	public ListType getMezziPagamentoDaCancellare() {
		return mezziPagamentoDaCancellare;
	}

	public void setMezziPagamentoDaCancellare(ListType mezziPagamentoDaCancellare) {
		this.mezziPagamentoDaCancellare = mezziPagamentoDaCancellare;
	}

	public MezzoPagamentoModel getDatiAssegno() {
		return datiAssegno;
	}

	public void setDatiAssegno(MezzoPagamentoModel datiAssegno) {
		this.datiAssegno = datiAssegno;
	}
	
	public BooleanType getIsInBusta() {
		return isInBusta;
	}

	public void setIsInBusta(BooleanType isInBusta) {
		this.isInBusta = isInBusta;
	}

	public StringType getContestoAggregatoreOriginale() {
		return contestoAggregatoreOriginale;
	}

	public void setContestoAggregatoreOriginale(
			StringType contestoAggregatoreOriginale) {
		this.contestoAggregatoreOriginale = contestoAggregatoreOriginale;
	}

	public StringType getCodAggregatoreOriginale() {
		return codAggregatoreOriginale;
	}

	public void setCodAggregatoreOriginale(StringType codAggregatoreOriginale) {
		this.codAggregatoreOriginale = codAggregatoreOriginale;
	}

	public IntegerType getEsitoCorrente() {
		return esitoCorrente;
	}

	public void setEsitoCorrente(IntegerType esitoCorrente) {
		this.esitoCorrente = esitoCorrente;
	}

	public StringType getEsitoFirmaAgente() {
		return esitoFirmaAgente;
	}

	public void setEsitoFirmaAgente(StringType esitoFirmaAgente) {
		this.esitoFirmaAgente = esitoFirmaAgente;
	}

	public StringType getTipoControlloFirmeAAF() {
		return tipoControlloFirmeAAF;
	}

	public void setTipoControlloFirmeAAF(StringType tipoControlloFirmeAAF) {
		this.tipoControlloFirmeAAF = tipoControlloFirmeAAF;
	}

	public StringType getTipoControlloFirmePF() {
		return tipoControlloFirmePF;
	}

	public void setTipoControlloFirmePF(StringType tipoControlloFirmePF) {
		this.tipoControlloFirmePF = tipoControlloFirmePF;
	}

	public StringType getCodUtenteEsitoFirmaAgente() {
		return codUtenteEsitoFirmaAgente;
	}

	public void setCodUtenteEsitoFirmaAgente(StringType codUtenteEsitoFirmaAgente) {
		this.codUtenteEsitoFirmaAgente = codUtenteEsitoFirmaAgente;
	}

	public TimestampType getDataOraEsitoFirmaAgente() {
		return dataOraEsitoFirmaAgente;
	}

	public void setDataOraEsitoFirmaAgente(TimestampType dataOraEsitoFirmaAgente) {
		this.dataOraEsitoFirmaAgente = dataOraEsitoFirmaAgente;
	}

	public StringType getEsitoFirmaAgenteNew() {
		return esitoFirmaAgenteNew;
	}

	public void setEsitoFirmaAgenteNew(StringType esitoFirmaAgenteNew) {
		this.esitoFirmaAgenteNew = esitoFirmaAgenteNew;
	}

	public IntegerType getContatoreFirmeAAF() {
		return contatoreFirmeAAF;
	}

	public void setContatoreFirmeAAF(IntegerType contatoreFirmeAAF) {
		this.contatoreFirmeAAF = contatoreFirmeAAF;
	}

	public IntegerType getContatoreFirmePF() {
		return contatoreFirmePF;
	}

	public void setContatoreFirmePF(IntegerType contatoreFirmePF) {
		this.contatoreFirmePF = contatoreFirmePF;
	}

	public IntegerType getNumeroEsitiFirmeAgenteInPrit() {
		return numeroEsitiFirmeAgenteInPrit;
	}

	public void setNumeroEsitiFirmeAgenteInPrit(
			IntegerType numeroEsitiFirmeAgenteInPrit) {
		this.numeroEsitiFirmeAgenteInPrit = numeroEsitiFirmeAgenteInPrit;
	}

	public IntegerType getNumeroEsitiFirmeAAFInGiornata() {
		return numeroEsitiFirmeAAFInGiornata;
	}

	public void setNumeroEsitiFirmeAAFInGiornata(
			IntegerType numeroEsitiFirmeAAFInGiornata) {
		this.numeroEsitiFirmeAAFInGiornata = numeroEsitiFirmeAAFInGiornata;
	}

	public IntegerType getNumeroEsitiFirmePFInGiornata() {
		return numeroEsitiFirmePFInGiornata;
	}

	public void setNumeroEsitiFirmePFInGiornata(
			IntegerType numeroEsitiFirmePFInGiornata) {
		this.numeroEsitiFirmePFInGiornata = numeroEsitiFirmePFInGiornata;
	}

	public boolean isWarningInLettura() {
		return warningInLettura;
	}

	public void setWarningInLettura(boolean warningInLettura) {
		this.warningInLettura = warningInLettura;
	}

	public ErroreDocumentoModel getErroreInLettura() {
		return erroreInLettura;
	}

	public void setErroreInLettura(ErroreDocumentoModel erroreInLettura) {
		this.erroreInLettura = erroreInLettura;
	}

	public TimestampType getDataOraRicezioneDocumento() {
		return dataOraRicezioneDocumento;
	}

	public void setDataOraRicezioneDocumento(TimestampType dataOraRicezioneDocumento) {
		this.dataOraRicezioneDocumento = dataOraRicezioneDocumento;
	}

	public boolean isVisualizzaAlert() {
		return visualizzaAlert;
	}

	public void setVisualizzaAlert(boolean visualizzaAlert) {
		this.visualizzaAlert = visualizzaAlert;
	}

	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}

	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}

	public String getOperazioneReportAdeguatezza() {
		return operazioneReportAdeguatezza;
	}

	public void setOperazioneReportAdeguatezza(String operazioneReportAdeguatezza) {
		this.operazioneReportAdeguatezza = operazioneReportAdeguatezza;
	}

	public String getOperazioneRaccomandazioneIdd() {
		return operazioneRaccomandazioneIdd;
	}

	public void setOperazioneRaccomandazioneIdd(String operazioneRaccomandazioneIdd) {
		this.operazioneRaccomandazioneIdd = operazioneRaccomandazioneIdd;
	}

	public StringType getIdAttivitaAgevolazione() {
		return idAttivitaAgevolazione;
	}

	public void setIdAttivitaAgevolazione(StringType idAttivitaAgevolazione) {
		this.idAttivitaAgevolazione = idAttivitaAgevolazione;
	}

	public StringType getIdAgevolazione() {
		return idAgevolazione;
	}

	public void setIdAgevolazione(StringType idAgevolazione) {
		this.idAgevolazione = idAgevolazione;
	}

	public StringType getTipoAgevolazione() {
		return tipoAgevolazione;
	}

	public void setTipoAgevolazione(StringType tipoAgevolazione) {
		this.tipoAgevolazione = tipoAgevolazione;
	}



}
