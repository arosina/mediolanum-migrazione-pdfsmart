package prgm.pdfwebforms.core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.dataentryutil.AutoCompleteBinder;
import prgm.pdfwebforms.legalerappresentante.LegaleRappresentanteManager;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.questionariolight.QuestionarioLightUtility;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfHtmlFieldsDrawer{

	private static final String VISIBILITYHIDDEN = "visibility:hidden;";
	private static final String TOPATTRIBUTE = "' top=";
	private static final String NAMEATTRIBUTE = "' name='";
	private static final String CONTATTRIBUTE= "Cont'";
	
	private static final String PX_BOTTOM = "px;bottom:";
	private static final String PX_HEIGHT = "px;height:";
	private static final String PX_MAGGIORE = "px;'>";
	private static final String DIV_ACAPO = "</div>\n";
	private static final String PX_WIDTH = "px;width:";
	private static final String STYLE_LEFT = "style='left:";
	
	/*******************************************************************/
	/*******************************************************************/
	public static class Rect{
		
		/*******************************************************************/
		/*******************************************************************/
		public Rect(PdfModel pdf, PdfFieldInfos fi){
			double scale = pdf.getScaleFactor().doubleValue();
			float bottom = fi.bottom; 
			bottom *= scale;
			this.ibottom = Math.round(bottom);
			float left = fi.left; 			
			left *= scale;
			this.ileft = Math.round(left);
			float w = fi.width; 
			w *= scale;
			this.iw = Math.round(w);
			float h = fi.height; 
			h *= scale;
			this.ih = Math.round(h);
		}
		
		/*******************************************************************/
		/*******************************************************************/
		public Rect(PdfModel pdf, PdfActionInfos ai){
			double scale = pdf.getScaleFactor().doubleValue();
			float bottom = ai.bottom; 
			bottom *= scale;
			this.ibottom = Math.round(bottom);
			float left = ai.left; 			
			left *= scale;
			this.ileft = Math.round(left);
			float w = ai.width; 
			w *= scale;
			this.iw = Math.round(w);
			float h = ai.height; 
			h *= scale;
			this.ih = Math.round(h);
		}
		
		/*******************************************************************/
		/*******************************************************************/
		public Rect(PdfModel pdf, Rectangle r){
			double scale = pdf.getScaleFactor().doubleValue();
			float bottom = r.getBottom(); 
			bottom *= scale;
			this.ibottom = Math.round(bottom);
			float left = r.getLeft(); 			
			left *= scale;
			this.ileft = Math.round(left);
			float w = r.getWidth(); 
			w *= scale;
			this.iw = Math.round(w);
			float h = r.getHeight(); 
			h *= scale;
			this.ih = Math.round(h);
		}
		
		int ibottom;
		int ileft;
		int iw;
		int ih;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static int drawPersonSignFields(Writer out, Template t, PdfModel pdf, int pageNum, int pageOffset, int pageHeight, PdfPersonModel person, int containerId) throws IOException{
		
		PdfHtmlFieldDrawer fieldDrawer = new PdfHtmlFieldDrawer();
		
		PdfDataModel pdfData = pdf.getPdfData();
		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove)); 
		String[] hidedFields = pdfData.getHidedFields().toString().split("\\,");
		ArrayList<String> hidedFieldsAsArray = new ArrayList<String>(Arrays.asList(hidedFields)); 

		t.savePrefix();
		t.setPrefix("pdfData");
		t.setNoPadding(true);
		
		ArrayList<String> drawedSign = new ArrayList<String>();
		List<PdfFieldInfos> personSignFields = pdf.getPersonSignFieldsInPage(person,pageNum,-1);
		for(PdfFieldInfos fi : personSignFields){
			
			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName))
				continue;
			
			AbstractType field = (AbstractType)pdfData.readProperty(fi.htmlFieldName);
			if(field == null)
				continue;

			PdfFieldInfos nomefi = pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName("nome"+fi.htmlFieldName);
			
			Rect r = new Rect(pdf,fi);
			if(nomefi != null){
				Rect nomer = new Rect(pdf,nomefi);
				r.ih = nomer.ih;
				r.iw = r.iw+nomer.iw;
			}
			if(r.ih < 35){
				r.ih = 35;
			}
			
			drawedSign.add(fi.htmlFieldName);
			out.write("<div isFieldToSign='true' class='pdfSignField' id='fcont"+containerId+NAMEATTRIBUTE+fi.htmlFieldName+"Cont' fid='"+fi.htmlFieldName+TOPATTRIBUTE+(pageOffset+(pageHeight-r.ibottom-r.ih))+" "+
							STYLE_LEFT+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+PX_MAGGIORE);
			out.write(PdfHtmlFieldDrawer.drawSignField(t, pdf, fi, field, 20, 20, false, containerId,r.iw,"Dichiaro e approvo:<br>"+person.readCognomeNome()));
			out.write(DIV_ACAPO);
			containerId++;
		}
		
		List<PdfPersonModel> cliInPdfData = LegaleRappresentanteManager.elencoClientiFirmatariPG(pdf, pdfData);
		for(int i=0;i<cliInPdfData.size();i++){
			
			PdfPersonModel curp = cliInPdfData.get(i);
			List<PdfFieldInfos> curpersonSignFields = pdf.getPersonSignFieldsInPage(curp,pageNum, i+1);
			for(PdfFieldInfos fi : curpersonSignFields){
				
				if(drawedSign.contains(fi.htmlFieldName))
					continue;

				AbstractType field = (AbstractType)pdfData.readProperty(fi.htmlFieldName);
				if(field == null || !"true".equals(field.toString()))
					continue;
	
				PdfFieldInfos nomefi = pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName("nome"+fi.htmlFieldName);
				
				Rect r = new Rect(pdf,fi);
				if(nomefi != null){
					Rect nomer = new Rect(pdf,nomefi);
					r.ih = nomer.ih;
					r.iw = r.iw+nomer.iw;
				}
				if(r.ih < 35){
					r.ih = 35;
				}
				
				out.write("<div class='pdfSignField pdfReadonlySignField' id='fcont"+containerId+NAMEATTRIBUTE+fi.htmlFieldName+CONTATTRIBUTE+
								STYLE_LEFT+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+PX_MAGGIORE);
				out.write(PdfHtmlFieldDrawer.drawSignField(t, pdf, fi, field, 20, 20, true, containerId,r.iw,"In firma per:<br>"+curp.readCognomeNome()));
				out.write(DIV_ACAPO);
				containerId++;
			}
		}
		
		for(PdfFieldInfos fi : pdf.getPdfData().getPdfInfos().getFieldInfos()){
			
			if(fi.page != pageNum)
				continue;
			
			if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
				continue;

			if(fieldsToRemoveAsArray.contains(fi.htmlFieldName) || hidedFieldsAsArray.contains(fi.htmlFieldName))
				continue;
			
			if(PdfPredefinedFields.AGENTE_NOME_FIRMA_N_PATTERN.matcher(fi.htmlFieldName).matches() ||
			   PdfPredefinedFields.CLIENTE_NOME_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName).matches())
				continue;
			
			if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA)) {
				Rect r = new Rect(pdf,fi);
				out.write("<div class='pdfField pdfReadonlyField' style='left:"+r.ileft+PX_BOTTOM+r.ibottom+PX_HEIGHT+r.ih+PX_MAGGIORE);
				out.write(PdfHtmlFieldDrawer.drawLabelField(t, pdf, fi, new StringType(pdf.getIdReportAdeguatezza()), r.iw, r.ih));
				out.write(DIV_ACAPO);
				continue;
			}
			
			AbstractType field = (AbstractType)pdfData.readProperty(fi.htmlFieldName);
			if(field == null)
				continue;

			String className = "pdfField pdfReadonlyField";

			boolean isClause = false;
			Matcher mat = PdfPredefinedFields.AGENTE_CLAUSOLA_N_PATTERN.matcher(fi.htmlFieldName);
			if(mat.matches()){
				isClause = true;
				className =  "pdfSignField";
			}else{
				mat = PdfPredefinedFields.CLIENTE_CLAUSOLA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
				if(mat.matches()){
					if(person.getIndexInFieldName().contains(Integer.parseInt(mat.group(2)))){
						isClause = true;
						className =  "pdfSignField";
					}else{
						continue;
					}
				}
			}
			
			Rect r = new Rect(pdf,fi);
			if(isClause){
				out.write("<div isFieldToSign='true' class='"+className+"' style='left:"+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+"px;"+(fi.hidden?VISIBILITYHIDDEN:"")+"' fid='"+fi.htmlFieldName+TOPATTRIBUTE+(pageOffset+(pageHeight-r.ibottom-r.ih))+">");
				out.write(PdfHtmlFieldDrawer.drawClauseField(t, pdf, fi, field, r.iw, r.ih, containerId));
			}else{
				out.write("<div class='"+className+"' style='left:"+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+"px;"+(fi.hidden?VISIBILITYHIDDEN:"")+"'>");
				out.write(drawField(t, pdf, fi, "", fieldDrawer, field, r.iw, r.ih, true, -1, true));
			}
			out.write(DIV_ACAPO);

		}
		
		t.restorePrefix();
		return containerId;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static int drawFields(Writer out, Template t, PdfModel pdf, int pageNum, int pageOffset, int pageHeight, int containerId, boolean readonlyPage) throws IOException{
		
		PdfHtmlFieldDrawer fieldDrawer = new PdfHtmlFieldDrawer();
		
		PdfInfos pdfInfos = pdf.getPdfData().getPdfInfos();
		PdfDataModel pdfData = pdf.getPdfData();
		PdfAnagModel pdfAnag = pdf.getPdfAnag();
		
		String[] hidedFields = pdfData.getHidedFields().toString().split("\\,");
		ArrayList<String> hidedFieldsAsArray = new ArrayList<String>(Arrays.asList(hidedFields)); 

		String[] fieldsToRemove = pdfData.getFieldsToRemove().toString().split("\\,");
		ArrayList<String> fieldsToRemoveAsArray = new ArrayList<String>(Arrays.asList(fieldsToRemove)); 
		
		String[] editableFields = pdfData.getEditableFields().toString().split("\\,");
		ArrayList<String> editableFieldsAsArray = new ArrayList<String>(Arrays.asList(editableFields)); 

		String[] uneditableFields = pdfData.getUneditableFields().toString().split("\\,");
		ArrayList<String> uneditableFieldsAsArray = new ArrayList<String>(Arrays.asList(uneditableFields)); 
		Map<String, ArrayList<String>> uneditableVals = new HashMap<String, ArrayList<String>>();
		for(int i=0; i < uneditableFieldsAsArray.size(); i++){
			String fname = uneditableFieldsAsArray.get(i);
			int sep = fname.indexOf("#");
			if(sep > 0){
				String vals = fname.substring(sep+1);
				String[] valsArray = vals.split("\\|");
				fname = fname.substring(0,sep);
				uneditableVals.put(fname, new ArrayList<String>(Arrays.asList(valsArray)));
			}
		}
		
		String[] extraMandatoryFields = pdfData.getExtraMandatoryFields().toString().split("\\,");
		ArrayList<String> extraMandatoryFieldsAsArray = new ArrayList<String>(Arrays.asList(extraMandatoryFields)); 

		t.savePrefix();
		t.setPrefix("pdfData");
		t.setNoPadding(true);
		
		for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){
			
			if(fi.page != pageNum)
				continue;
			
			if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			
			if(PdfPredefinedFields.AGENTE_CLAUSOLA_N_PATTERN.matcher(fi.htmlFieldName).matches() ||
			   PdfPredefinedFields.CLIENTE_CLAUSOLA_N_DI_M_PATTERN.matcher(fi.htmlFieldName).matches())
				continue;
			
			if(fieldsToRemoveAsArray.contains(fi.pdfFieldName) || fieldsToRemoveAsArray.contains(fi.htmlFieldName)){
				AbstractType prop = pdfData.readProperty(fi.htmlFieldName);
				if(prop != null)
					prop.setStringValue("");
				continue;
			}
				
			if(PdfPredefinedFields.AGENTE_NOME_FIRMA_N_PATTERN.matcher(fi.htmlFieldName).matches() ||
			   PdfPredefinedFields.CLIENTE_NOME_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName).matches())
				continue;

			if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_IMAGE) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_VALUE) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.NUMERO_ISTANZA_MODULO))
				continue;
			
			AbstractType field = null;
			
			if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.CODICE_MODULO) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.DESCRIZIONE_MODULO) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_QUESTIONARIO_IDD) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA) ||
			   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD)){
				AbstractType value = new StringType();
				if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.CODICE_MODULO))
					value = pdfAnag.getPdfCode(); 
				else if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.DESCRIZIONE_MODULO))
					value = pdfAnag.getPdfDescr();
				else if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_QUESTIONARIO_IDD))
					value = new StringType(pdf.getIdQuestionarioIdd().toString());
				else if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA)){
					if(pdf.isOperatoreMOM()){
						if(pdf.isInInserimentoMOM()) {
							fi.readonly=false;
							uneditableFieldsAsArray.remove(fi.pdfFieldName);
							field = pdfData.getIdReportAdeguatezza();
						}else {
							value = new StringType(pdf.getIdReportAdeguatezza());
						}
					}else if(pdf.reportAdeguatezzaPassatoDalChiamate() || pdf.isPdfOnValidation()) {
						value = new StringType(pdf.getIdReportAdeguatezza());
					}else {
						value = new StringType();
					}
				}else if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD)) {
					if(pdf.isInInserimentoMOM()) {
						fi.readonly=false;
						uneditableFieldsAsArray.remove(fi.pdfFieldName);
						field = pdfData.getIdRaccomandazioneIdd();
					}else if(pdf.getIddCallModel() != null && !pdf.getIddCallModel().getIdRaccomandazioneIdd().isNull()) {
						value = pdf.getIddCallModel().getIdRaccomandazioneIdd();
					}else {
						value = new StringType(pdf.getIdRaccomandazioneIdd().toString());
					}
				}
				if(field == null) { // field != null: non va gestito come label
					Rect r = new Rect(pdf,fi);
					out.write("<div class='pdfField pdfReadonlyField' style='left:"+r.ileft+PX_BOTTOM+r.ibottom+PX_HEIGHT+r.ih+PX_MAGGIORE);
					out.write(PdfHtmlFieldDrawer.drawLabelField(t, pdf, fi, value, r.iw, r.ih));
					out.write(DIV_ACAPO);
					continue;
				}
			}

			if(field == null)
				field = pdfData.readProperty(fi.htmlFieldName);
			if(field == null)
				continue;
			
			Rect r = new Rect(pdf,fi);
 
			boolean readonly = readonlyPage;
			if(!readonly){
				readonly = fi.readonly;
				ArrayList<String> uvals = uneditableVals.get(fi.htmlFieldName);
				if(uvals != null && uvals.contains(fi.expValue)){
					continue;
				}
				if(editableFieldsAsArray.contains(fi.pdfFieldName) || editableFieldsAsArray.contains(fi.htmlFieldName)){
					readonly = false;
				}else if(uneditableFieldsAsArray.contains(fi.pdfFieldName) || uneditableFieldsAsArray.contains(fi.htmlFieldName)){
					readonly = true;
				}else{
					if(!pdf.isOperatoreMOM() && pdf.getPdfData().getPdfInitialInputDataArray().contains(fi.pdfFieldName))
						readonly = true;
				}
				if(pdf.isOperatoreMOM()){
					if(fi.htmlFieldName.equals(PdfPredefinedFields.AGENTE_CODICE)){
						readonly = false;
					}else{
						Matcher mat = PdfPredefinedFields.CLIENTE_NDG_PATTERN.matcher(fi.htmlFieldName);
						if(mat.matches()){
							readonly = false;
						}
					}
				}
			}
			
			String title = "";
			if(fi.helpText.length() > 0)
				title = "title=\""+fi.helpText.replaceAll("\\\"","'")+"\"";
			
			boolean mandatory = fi.mandatory;
			if(extraMandatoryFieldsAsArray.contains(fi.pdfFieldName) || extraMandatoryFieldsAsArray.contains(fi.htmlFieldName))
				mandatory = true;
			
    		if(fi.pdfFieldName.equals(PdfPredefinedFields.NUMERO_CARTA_CHIMICA)){
        		if(pdfData.getCompilationModes().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)){
        			mandatory = true;
        		}else if(!pdf.getPdfAnag().getPdfIsCartaChimicaEnabled().booleanValue()){
        			field.setStringValue("");
        			continue;
        		}
    		}

    		if(fi.pdfFieldName.equals(PdfPredefinedFields.DATA_SOTTOSCRIZIONE) &&
    		   pdf.hasDataSottoscrizioneOggi()){
    			field.setStringValue(Tools.today().toString());
    			readonly = true;
    		}
    		
    		if(fi.pdfFieldName.equals(PdfPredefinedFields.ORA_SOTTOSCRIZIONE) &&
    		   pdf.hasDataSottoscrizioneOggi()){
    			field.setStringValue("");
    			readonly = true;
    		}    		

   			QuestionarioLightUtility.gestisciVisibilitaLinkIdQLTM(t, pdf, fi, field);
    		
			String className = "pdfNormalField";
			if(mandatory)
				className += " pdfMandatoryField";
			if(field.hasTypeErrors()){
				if(fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON){
					if(field.isNull() || fi.expValue.equals(field.toString()))
						className += " pdfFieldHasError";
				}else{
					className += " pdfFieldHasError";
				}
			}
			if(readonly)
				className += " pdfReadonlyField";
			
			String fieldGroups = "";
			if(pdfData.getPdfPageDriver() != null){
				fieldGroups = pdfData.getPdfPageDriver().fieldGroups(fi.htmlFieldName);
				if(fieldGroups.length() > 0)
					fieldGroups = " groups='"+fieldGroups+"'";
			}
			
			String fname = fi.htmlFieldName;
			String fid = fname;
			if(fi.mainField != null || fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON || fi.fieldType == AcroFields.FIELD_TYPE_CHECKBOX)
				fid = fname+containerId;
			
			String hidedFieldStyle = "";
			if(hidedFieldsAsArray.contains(fi.pdfFieldName) || hidedFieldsAsArray.contains(fi.htmlFieldName)) {
				hidedFieldStyle = "display:none;";
				field.resetTypeErrors();
			}
				
			out.write("<div class='pdfField' id='fcont"+containerId+NAMEATTRIBUTE+fi.htmlFieldName+CONTATTRIBUTE+fieldGroups+" pageOffset='"+pageOffset+"' "+
							STYLE_LEFT+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+"px;"+(fi.hidden?VISIBILITYHIDDEN:"")+hidedFieldStyle+"' "+
							title+">");
			out.write("<div name='"+fi.htmlFieldName+"ContBorder' class='"+className+"' id='fcontborder"+containerId+"' page="+fi.page+" fid='"+fid+TOPATTRIBUTE+(pageOffset+(pageHeight-r.ibottom-r.ih))+">");
			out.write(drawField(t, pdf, fi, fid, fieldDrawer, field, r.iw, r.ih, readonly, containerId, false));
			out.write("</div>");
			out.write(DIV_ACAPO);
			containerId++;
		}
		t.restorePrefix();
		return containerId;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static void drawActions(Writer out, Template t, PdfModel pdf, int pageNum, int pageOffset) throws IOException{
		
		PdfInfos pdfInfos = pdf.getPdfData().getPdfInfos();
		PdfDataModel pdfData = pdf.getPdfData();
		int count = 1;
		for(PdfActionInfos ai : pdfInfos.getActionInfos()){
			
			if(ai.page != pageNum)
				continue;
			
			if(ai.html == null || ai.html.length() == 0)
				continue;
			
			Rect r = new Rect(pdf,ai);
			 
			String title = "";
			if(ai.helpText != null && ai.helpText.length() > 0)
				title = " title=\""+ai.helpText.replaceAll("\\\"","'")+"\"";
			
			String fieldGroups = "";
			if(pdfData.getPdfPageDriver() != null){
				fieldGroups = pdfData.getPdfPageDriver().fieldGroups(ai.htmlFieldName);
				if(fieldGroups.length() > 0)
					fieldGroups = " groups='"+fieldGroups+"'";
			}

			out.write("<div class='pdfAction' id='"+ai.htmlFieldName+pageNum+count+NAMEATTRIBUTE+ai.htmlFieldName+CONTATTRIBUTE+fieldGroups+
							STYLE_LEFT+r.ileft+PX_BOTTOM+r.ibottom+PX_WIDTH+r.iw+PX_HEIGHT+r.ih+"px;'"+title+">");
			out.write(ai.html);
			out.write(DIV_ACAPO);
			count++;
		}
		return;
	}

	/***********************************************************************************************/
	private static final String S_TYPE_HIDDEN = "<input type='hidden' id='";
	private static final String S_NAME = "' name='pdfData_";
	private static final String S_VALUE = "' value='";
	/***********************************************************************************************/
	protected static void drawHiddenFields(Writer out, Template t, PdfModel pdf) throws IOException{
		PdfFieldInfos fi = null;
		
		PdfPersonModel person = pdf.getPdfData().getAgente();
		fi = pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(PdfPredefinedFields.AGENTE_CODICE);
		if(fi == null)
			out.write(S_TYPE_HIDDEN+PdfPredefinedFields.AGENTE_CODICE+S_NAME+PdfPredefinedFields.AGENTE_CODICE+S_VALUE+person.getCodAgente()+"'>");
		
		for(int i=0;i<pdf.getPdfData().getClienti().size();i++){
			person = pdf.getPdfData().getClienti().get(i);			
			drawHiddenPersonField(out, pdf, "ndg", i, person.getNdg().toString(), false);			
			drawHiddenPersonField(out, pdf, "idCensimento", i, person.getIdCensimento().toString(), false);
			drawHiddenPersonField(out, pdf, "isPersonaFisica", i, person.propertyToString("isPersonaFisica"), true);
			drawHiddenPersonField(out, pdf, "isPersonaGiuridica", i, person.propertyToString("isPersonaGiuridica"), true);
			drawHiddenPersonField(out, pdf, "isDittaIndividuale", i, person.propertyToString("isDittaIndividuale"), true);
			drawHiddenPersonField(out, pdf, "isLiberoProfessionista", i, person.propertyToString("isLiberoProfessionista"), true);
		}
		
		// Creazione campi agevolazione se non presenti, solo se esiste il campo codice
		if(pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null &&
			pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE) == null) {
			AbstractType percentualeAgevolazione = (AbstractType)pdf.getPdfData().read(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE);
			if(percentualeAgevolazione == null)
				percentualeAgevolazione = new StringType();
			else {
				//Con la rfc 185853 siamo andati in produzione settando erroneamente percentualeAgevolazione come DoubleType anzichè StringType
				if (percentualeAgevolazione instanceof DoubleType) {
					percentualeAgevolazione = new StringType(percentualeAgevolazione.toString());
				}
			}
			out.write(S_TYPE_HIDDEN+PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE+S_NAME+PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE+S_VALUE+percentualeAgevolazione+"'>");
			pdf.getPdfData().addProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, percentualeAgevolazione);
		}
		
	}

	/***********************************************************************************************/
	private static final String S_CLIENTE = "Cliente";
	/***********************************************************************************************/
	private static void drawHiddenPersonField(Writer out, PdfModel pdf, String fname, int idx, String fvalue, boolean asBool) throws IOException{
		String fullFname = fname+S_CLIENTE+(idx+1);
		if(!pdf.getPdfData().fieldExist(fullFname)){
			out.write(S_TYPE_HIDDEN+fullFname+S_NAME+fullFname+S_VALUE+fvalue+"'>");
			pdf.getPdfData().addProperty(fullFname, asBool ? new BooleanType(fvalue) : new StringType(fvalue));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String drawField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid, PdfHtmlFieldDrawer fieldDrawer,
									AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		if(fi.mainField != null && fi.fieldType == AcroFields.FIELD_TYPE_RADIOBUTTON)
			return PdfHtmlFieldDrawer.drawSlaveField(t, pdfModel, fi, field, w, h);
		
		CodDescDataList dl = null;
		StringBuffer res = new StringBuffer();

		switch(fi.fieldType){
	        case AcroFields.FIELD_TYPE_RADIOBUTTON:
	        	res.append(fieldDrawer.drawRadiobuttonField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_CHECKBOX:
        		res.append(fieldDrawer.drawCheckboxField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_TEXT:
	        	dl = pdfModel.getPdfData().getCodDescDataList(fi.htmlFieldName);
	        	if(dl != null){
        			fi.codDescDataList = dl; 
		        	res.append(fieldDrawer.drawComboField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
	        	}else{
		        	if(fi.dataType.equals("IntegerType"))
		        		res.append(PdfHtmlFieldDrawer.drawIntegerField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
		        	else if(fi.dataType.equals("DoubleType"))
		        		res.append(PdfHtmlFieldDrawer.drawDoubleField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
		        	else if(fi.dataType.equals("DateType"))
		        		res.append(PdfHtmlFieldDrawer.drawDateField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
		        	else if(!fi.pdfFieldName.startsWith("testofirma"))
		        		res.append(PdfHtmlFieldDrawer.drawTextField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
	        	}
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_COMBO:
	        case AcroFields.FIELD_TYPE_LIST:
	        	dl = pdfModel.getPdfData().getCodDescDataList(fi.htmlFieldName);
	        	if(dl != null)
        			fi.codDescDataList = dl;
	        	res.append(fieldDrawer.drawComboField(t, pdfModel, fi, fid, field, w, h, readonly, containerId, onSign));
        		break;
		}    		
		
		if(!onSign){
			String fieldJs = null;
			Map<String, String> fieldsJsScript = pdfModel.getFieldsJsScript();
			if(fieldsJsScript != null)
				fieldJs = fieldsJsScript.get(fi.pdfFieldName);
			if(fieldJs != null && fieldJs.length() > 0){
				res.append("<script>");
				res.append(fieldJs);
				res.append("</script>\n");
			}else{
				if(fi.htmlFieldName.toLowerCase().startsWith(PdfPredefinedFields.LUOGO)){
					res.append(AutoCompleteBinder.luogoAutocomplete(fi.htmlFieldName));
				}else if(fi.htmlFieldName.toLowerCase().startsWith(PdfPredefinedFields.DESCR_TOPONIMO.toLowerCase())){
					res.append(AutoCompleteBinder.descrToponimoAutocomplete(fi.htmlFieldName));
				}else if(fi.htmlFieldName.equals(PdfPredefinedFields.CODICE_AGEVOLAZIONE)){
					res.append(AutoCompleteBinder.agevolazioneAutocomplete());
				}else{
					Matcher mat = PdfPredefinedFields.AGENTE_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.agePersonAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
					mat = PdfPredefinedFields.SPLIT_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.splitPersonAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
					mat = PdfPredefinedFields.CLIENTE_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.personAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
					mat = PdfPredefinedFields.CLIENTE_CONTOCORRENTE_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.contoCorrenteAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
					mat = PdfPredefinedFields.CLIENTE_CONTO_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.contoAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
					mat = PdfPredefinedFields.CLIENTE_PRESTITO_PATTERN.matcher(fi.htmlFieldName);
					if(mat.matches()){
						res.append(AutoCompleteBinder.prestitoAutocomplete(mat, fi.htmlFieldName));
						return res.toString();
					}
				}
			}
		}
		return res.toString();
	}
	
}
