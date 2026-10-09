package prgm.pdfwebforms.dataentryutil;

import java.util.regex.Matcher;

/*******************************************************************/
/*******************************************************************/
public class AutoCompleteBinder {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String agePersonAutocomplete(Matcher mat, String fieldName){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDriver.bindAgePersonAutocomplete({personFieldName: '"+mat.group(1)+"'});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String splitPersonAutocomplete(Matcher mat, String fieldName){
		StringBuilder res = new StringBuilder();
		res.append("<script>");
		res.append("pdfPageDataentryUtil.bindSplitPersonAutocomplete({personFieldName: '"+mat.group(1)+"'});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String personAutocomplete(Matcher mat, String fieldName){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDriver.bindPersonAutocomplete({ personFieldName: '"+mat.group(1)+"', "+
														  "personIdx: "+mat.group(2)+","+
														  "fieldName: '"+mat.group(1)+"Cliente"+mat.group(2)+"'});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String contoCorrenteAutocomplete(Matcher mat, String fieldName){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDriver.bindContoAutocomplete({useCodAgente: 		true, "+
														"ndgFieldName: 		'ndgCliente"+mat.group(3)+"', "+
														"tipoConto: 		'CONTO_CORRENTE', "+
														"ruoliAmmessi:		\"'P', 'I', 'C', 'D'\","+
														"contoFieldName: 	'"+mat.group(1)+"', "+
														"fieldName: 		'"+fieldName+"'});");
		res.append("</script>\n");
		return res.toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String contoAutocomplete(Matcher mat, String fieldName){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDriver.bindContoAutocomplete({useCodAgente: 		true, "+
														"ndgFieldName: 		'ndgCliente"+mat.group(3)+"', "+
														"tipoConto: 		'', "+
														"contoFieldName: 	'"+mat.group(1)+"', "+
														"fieldName: 		'"+fieldName+"'});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String luogoAutocomplete(String fieldName){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDriver.bindLuogoAutocomplete({fieldName: \""+fieldName+"\"});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String descrToponimoAutocomplete(String fieldName){
		StringBuilder res = new StringBuilder();
		res.append("<script>");
		res.append("pdfPageDriver.bindDescrToponimoAutocomplete({fieldName: \""+fieldName+"\"});");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String agevolazioneAutocomplete(){
		StringBuffer res = new StringBuffer();
		res.append("<script>");
		res.append("pdfPageDataentryUtil.bindCodiceAgevolazioneAutocomplete();");
		res.append("</script>\n");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String prestitoAutocomplete(Matcher mat, String fieldName){
		StringBuilder res = new StringBuilder();
		res.append("<script>");
		res.append("pdfPageDataentryUtil.bindPrestitoAutocomplete({ ndgFieldName: 	'ndgCliente"+mat.group(3)+"', "+
																	"prestitoFieldName:'"+mat.group(1)+"', "+
																	"fieldName: 	'"+fieldName+"'});");
		res.append("</script>\n");
		return res.toString();
	}
	
}
