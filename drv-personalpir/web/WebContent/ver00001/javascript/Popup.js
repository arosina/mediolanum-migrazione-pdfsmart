/***********************************************************************************************/
/* Gestione Popup Ricerca Fondi */
/***********************************************************************************************/
var suffissoFondi = null;
PdfPageDriver.prototype.openPopupRicercaFondi = function(tipoRicerca, suffisso){
	var tipoSottoscrizione = pdf.getFieldValueAsString("tipoSottoscrizione");
	var tipologiaDoubleChance = "";
	if (pdf.getFieldValueAsString("tipoSottoscrizione") == "DC"){
		tipologiaDoubleChance = pdf.getFieldValueAsString("tipoPicProgrammatoDoubleChance"); 
	}
	var numeroContratto = "";
	numeroContratto = pdf.getFieldValueAsString("numeroContratto");
	var codProdottoPolizza = "";
	codProdottoPolizza = pdf.getFieldValueAsString("codProdottoPolizza");
	var flagTrasformatoPic = "";
	flagTrasformatoPic = pdf.getFieldValueAsString("flagTrasformatoPic");
	suffissoFondi = suffisso; 
	
	openModalPopup("prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup.RicercaFondi",
				   "tipoRicerca="+tipoRicerca+
				   "&tipoSottoscrizione="+tipoSottoscrizione+
				   "&tipologiaDoubleChance="+tipologiaDoubleChance+
				   "&numeroContratto="+numeroContratto+
				   "&codProdottoPolizza="+codProdottoPolizza+
				   "&fondiSelezionati="+getFondiSelezionati(tipoRicerca, suffisso, -1)+
				   "&flagTrasformatoPic="+flagTrasformatoPic,null,openPopupRicercaFondiCallBack,"Ricerca Fondi",600,800,false);
}
/***********************************************************************************************/
/***********************************************************************************************/
function openPopupRicercaFondiCallBack(obj){
	if (obj == null){
		suffissoFondi = null;
		return;
	}
	
	if (suffissoFondi == null){
		return;
	}
	
	var trovato = false;
	var pos = -1;
	
	for (i=0;i<obj.length;i++){//ciclo principale
		trovato = false;
		pos = -1;
		
		for (x=0;;x++){
			if(document.getElementById("codiceFondo"+suffissoFondi+x) == null) //se nullo ho finito il ciclo
				break;
			if (pdf.getFieldValue("codiceFondo"+suffissoFondi+x) == obj[i].codiceFondo){
				trovato = true;
				break;	
			}
			if (pdf.getFieldValue("codiceFondo"+suffissoFondi+x) == "" && pos==-1) //prendo la prima posizione libera
				pos = x;
		}
		
		if (!trovato && pos != -1){
			pdf.setFieldValue("lineaFondo"+suffissoFondi+pos,obj[i].lineaFondo);
			pdf.setFieldValue("codiceFondo"+suffissoFondi+pos,obj[i].codiceFondo);
			pdf.setFieldValue("importoMinimoFondo"+suffissoFondi+pos,obj[i].importoMinimo);
			pdf.setFieldValue("societaFondo"+suffissoFondi+pos,obj[i].societaFondo);
			pdf.setFieldValue("isinFondo"+suffissoFondi+pos,obj[i].isinFondo);
			pdf.setFieldValue("descrizioneFondo"+suffissoFondi+pos,obj[i].descrizioneFondo);
			pdf.setFieldValue("controvaloreFondo"+suffissoFondi+pos,obj[i].controvaloreFondo);
			pdfPageDriver.onChange("isinFondo"+suffissoFondi+pos);
		}
	}
	suffissoFondi = null;
}