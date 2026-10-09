package prgm.pdfwebforms.mom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class JsonCallbackDrawer {

	private static final String EXTENDS = "extends";
	private static final String VIRGOLAACAPO = "\",\n";
	private static final String QUADRAVIRGOLAACAPO = "],\n";
	private static final String JSONCODPREFIX = "{\"cod\":\"";
	private static final String JSONTXTPREFIX = "\",\"txt\":\"";
	
	private ClientSessionContext csc;
	private Template template;
	private PdfModel pdf;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public JsonCallbackDrawer(Template template, PdfModel pdf) {
		this.template = template;
		this.csc = template.getUserSessionContext().getClientSessionContext();
		this.pdf = pdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawJsonCallbackObject(){
		
		PdfDataModel pdfData = pdf.getPdfData();
		
		String pdfInstanceId = pdfData.getPdfInstanceId().toString();
		DateType dataSottoscrizione = (DateType)pdfData.read(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
		StringBuilder ndgs = new StringBuilder();
		for(int i=1; i <= DataLoader.MAX_NUM_CLIENTI; i++){
			StringType ndg = (StringType)pdfData.read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+i);
			if(ndg == null || ndg.isNull()){
				ndgs.append(",");
				continue;
			}
			ndgs.append(Tools.fillSx(ndg.toString(), '0', 11)+",");
		}
		if(ndgs.length() > 0)
			ndgs = ndgs.deleteCharAt(ndgs.length()-1);

		Map<String, Properties> errorsPropsMap = pdf.getErrorsPropsMap();
		if(errorsPropsMap == null) {
			errorsPropsMap = loadPropsFiles(pdf.getPdfAnag().getPdfDriverName().toString(), "Errors");
			pdf.setErrorsPropsMap(errorsPropsMap);
		}
		StringBuilder errori = new StringBuilder();
		for(int i=0; i < pdf.getCommandErrors().size(); i++){
			String errore = template.getProperty((CommandError)pdf.getCommandErrors().get(i));
			if(drawJsonCodedErrorWarningObject(pdf.getErrorsCodes(), "er_", errore, errori)) {
				errori.append(",");
				continue;
			}
			errore = replaceChars(errore);
			errori.append(drawJsonErrorWarningObject("er_", errore, errorsPropsMap)+",");
		}
		if(errori.length() > 0)
			errori = errori.deleteCharAt(errori.length()-1);

		StringBuilder warnings = new StringBuilder();
		Map<String, Properties> warningsPropsMap = pdf.getWarningsPropsMap();
		if(warningsPropsMap == null) {
			warningsPropsMap = loadPropsFiles(pdf.getPdfAnag().getPdfDriverName().toString(), "Warnings");
			pdf.setWarningsPropsMap(warningsPropsMap);
		}
		for(int i=0; i < pdf.getCommandWarnings().size(); i++){
			String warning = template.getProperty((CommandWarning)pdf.getCommandWarnings().get(i));
			if(drawJsonCodedErrorWarningObject(pdf.getWarningsCodes(), "", warning, warnings)) {
				warnings.append(",");
				continue;
			}
			warning = replaceChars(warning);
			warnings.append(drawJsonErrorWarningObject("", warning, warningsPropsMap)+",");
		}
		if(warnings.length() > 0)
			warnings = warnings.deleteCharAt(warnings.length()-1);
		
		pdf.getErrorsCodes().clear();
		pdf.getWarningsCodes().clear();
		
		Map<String, Properties> typeErrorsPropsMap = pdf.getTypeErrorsPropsMap();
		if(typeErrorsPropsMap == null) {
			typeErrorsPropsMap = loadPropsFiles(pdf.getPdfAnag().getPdfDriverName().toString(), "TypeErrors");
			pdf.setTypeErrorsPropsMap(typeErrorsPropsMap);
		}
		String typeErrors = drawJsonTypesErrorsObject(template, pdf, typeErrorsPropsMap);
		
		StringBuilder res = new StringBuilder();
		res.append("var data = { \n");
		res.append(	"\"pdfInstanceId\" : \""+pdfInstanceId+VIRGOLAACAPO);
		res.append(	"\"dataSottoscrizione\" : \""+(dataSottoscrizione == null?"":dataSottoscrizione)+VIRGOLAACAPO);
		res.append(	"\"clienti\" : \""+ndgs+VIRGOLAACAPO);
		res.append(	"\"errori\" : ["+errori+QUADRAVIRGOLAACAPO);
		res.append(	"\"warning\" : ["+warnings+QUADRAVIRGOLAACAPO);
		res.append(	"\"erroriCampi\" : ["+typeErrors+QUADRAVIRGOLAACAPO);
		res.append(	"\"esitoConfrontoDoppiaSpunta\" : \""+pdf.getMomEventData().getEsitoConfrontoDoppiaSpunta()+"\"\n");
		res.append("};\n");
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean drawJsonCodedErrorWarningObject(Map<String, String> codesMap, String codPrefix, String msg, StringBuilder msgs){
		String code = codesMap.get(msg);
		if(code != null) {
			msg = replaceChars(msg);
			msgs.append(JSONCODPREFIX+codPrefix+code+JSONTXTPREFIX+msg+"\"}");
			return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String drawJsonErrorWarningObject(String codPrefix, String msg, Map<String, Properties> propsMap){
		StringBuilder res = new StringBuilder();
		for(Map.Entry<String, Properties> propsMapEntry : propsMap.entrySet()) {
			Properties p = propsMapEntry.getValue();
			for(Enumeration<?> en = p.keys(); en.hasMoreElements();) {
				String code = (String) en.nextElement();
				if(code.equals(EXTENDS))
					continue;
				String msgRegex = p.getProperty(code);
				msgRegex = msgRegex.trim();
				msg = msg.trim();
				try {
					Matcher mat = Pattern.compile(msgRegex).matcher(msg);
					if(mat.matches()) { // Trovato
						res.append(JSONCODPREFIX+codPrefix+propsMapEntry.getKey()+code+JSONTXTPREFIX+msg+"\"}");
						return res.toString();
					}
				}catch(Exception e) { /* do nothing: skip */ }
			}
		}
		return "{\"cod\":\"\",\"txt\":\""+msg+"\"}";
	}		
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String drawJsonTypesErrorsObject(Template template, PdfModel pdf, Map<String, Properties> propsMap){
		StringBuilder res = new StringBuilder();
		ArrayList<String> managedFields = new ArrayList<String>();
		PdfInfos pdfInfos = pdf.getPdfData().getPdfInfos();
		PdfDataModel pdfData = pdf.getPdfData();
		for(PdfFieldInfos fi : pdfInfos.getFieldInfos()){
			if(managedFields.contains(fi.pdfFieldName))
				continue;
			managedFields.add(fi.pdfFieldName);
			try {
				AbstractType field = (AbstractType)Tools.getPropertyValue(pdfData, fi.htmlFieldName);
				if(field != null && field.hasTypeErrors())
					res.append(drawJsonFieldTypeErrors(template, field, fi.pdfFieldName, propsMap)+",");
			}catch(Exception e) {/* do nothing*/}
		}
		if(pdf.getPdfData().getCoraFb().hasTypeErrors())
			res.append(drawJsonFieldTypeErrors(template, pdf.getPdfData().getCoraFb(), "coraFb", propsMap)+",");
		if(res.length() > 0)
			res = res.deleteCharAt(res.length()-1);
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String drawJsonFieldTypeErrors(Template template, AbstractType field, String fieldName, Map<String, Properties> propsMap) {
		StringBuilder res = new StringBuilder();
		List<?> errors = field.getTypeErrors();
		for(int i=0;i<errors.size();i++){
			TypeError error = (TypeError)errors.get(i);
			if(drawJsonCodedFieldTypeErrors(error, res)) {
				res.append(",");
				continue;
			}
			String msg = replaceChars(template.getProperty(error));
			res.append(drawJsonFieldTypeError(msg, propsMap)+",");
		}
		if(res.length() > 0)
			res = res.deleteCharAt(res.length()-1);
		return "{\"campo\":\""+fieldName+"\", \"errori\":["+res+"]}";
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean drawJsonCodedFieldTypeErrors(TypeError error, StringBuilder res) {
		if(error.getKey().startsWith(PdfCodedMessage.MESSAGE_CODE_PREFIX)) {
			try {
				StringBuilder grps = new StringBuilder();
				Object[] pars = error.getValues();
				for(int i=0;i<pars.length;i++) {
					if(pars[i] != null)
						grps.append("\""+pars[i]+"\",");
				}
				if(grps.length() > 0)
					grps = grps.deleteCharAt(grps.length()-1);
				String code = error.getKey().substring(PdfCodedMessage.MESSAGE_CODE_PREFIX.length()).replace("_", "");
				String msg = PdfCodedMessage.getMessage(error.getKey(), pars, "TypeError");
				res.append(JSONCODPREFIX+code+JSONTXTPREFIX+replaceChars(msg)+"\",\"grp\":["+grps+"]}");
				return true;
			}catch(Exception e) { /* do nothing */ }
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String drawJsonFieldTypeError(String msg, Map<String, Properties> propsMap) {
		StringBuilder res = new StringBuilder();
		for(Map.Entry<String, Properties> propsMapEntry : propsMap.entrySet()) {
			Properties p = propsMapEntry.getValue();
			for(Enumeration<?> en = p.keys(); en.hasMoreElements();) {
				String code = (String) en.nextElement();
				if(code.equals(EXTENDS))
					continue;
				String msgRegex = p.getProperty(code);
				msgRegex = msgRegex.trim();
				msg = msg.trim();
				try {
					Matcher mat = Pattern.compile(msgRegex).matcher(msg);
					if(mat.matches()) { // Trovato
						String grps = drawJsonFieldTypeErrorGrps(mat);
						res.append(JSONCODPREFIX+propsMapEntry.getKey()+code+JSONTXTPREFIX+msg+"\",\"grp\":["+grps+"]}");
						return res.toString();
					}
				}catch(Exception e) { /* do nothing: skip */ }
			}
		}
		return "{\"cod\":\"\",\"txt\":\""+msg+"\",\"grp\":[]}";
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String drawJsonFieldTypeErrorGrps(Matcher mat) {
		StringBuilder grps = new StringBuilder();
		for(int i=1;i<=mat.groupCount();i++)
			grps.append("\""+mat.group(i)+"\",");
		if(grps.length() > 0)
			grps = grps.deleteCharAt(grps.length()-1);
		return grps.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private Map<String, Properties> loadPropsFiles(String mainFileName, String fileType) {
		Map<String, Properties> propsMap = new LinkedHashMap<String, Properties>();
		String fileName = mainFileName;
		for(;;) {
			Properties p = loadPropFile(fileName, fileType);
			if(p == null)
				break;
			propsMap.put(fileName, p);
			String ext = p.getProperty(EXTENDS); 
			if(ext == null)
				break;
			fileName = ext;
		}
		// Load main pdfwebforms file if not extended by driver file
		String pdfwebformsfileName = "pdfwebforms";
		if(!propsMap.containsKey(pdfwebformsfileName)) {
			Properties p = loadPropFile(pdfwebformsfileName, fileType);
			if(p != null)
				propsMap.put(pdfwebformsfileName, p);
		}
		return propsMap;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private Properties loadPropFile(String fileName, String fileType){
		InputStream inputStream = null;
		try {
			StringType propString = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																				"select CONF_ITEM_PROPERTIES from PDF_MESSAGE_CODES where CONF_ITEM_NAME = '"+fileName+fileType+"'", 
																				null, StringType.class).getSingleResult();
			if(propString != null && !propString.isNull())
				inputStream = new ByteArrayInputStream(propString.toString().getBytes(StandardCharsets.ISO_8859_1));
		}catch(DAOException daoe) {
			// do nothing
		}
		if(inputStream == null) {
			try {
				inputStream = fileName.getClass().getResourceAsStream("/"+fileName+fileType+".properties");
			}catch(Exception e) {
				inputStream = null;
			}
		}
		if(inputStream == null)
			return null;
		try {
			Properties p = new Properties();
			p.load(inputStream);
			return p;
		}catch(Exception e) {
			return null;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String replaceChars(String str) {
		return str.replace("\"", "\\\"").replaceAll("[\\n\\r]"," ");
	}

}
