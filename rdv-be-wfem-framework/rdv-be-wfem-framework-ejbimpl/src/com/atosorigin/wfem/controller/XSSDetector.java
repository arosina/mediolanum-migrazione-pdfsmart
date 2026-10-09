package com.atosorigin.wfem.controller;

import java.util.regex.Pattern;

import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;

public class XSSDetector {
	private static final String XSS_REGEX_SCRIPT_1 = "(.*?)(<script>(.*?)</script>)(.*?)";
	private static final String XSS_REGEX_SRC = "src[\\r\\n]*=[\\r\\n]*(['\"])(.*?)\\1";
	private static final String XSS_REGEX_SCRIPT_2 = "(.*?)</script>(.*?)";
	private static final String XSS_REGEX_SCRIPT_3 = "(.*?)<script(.*?)>(.*?)";	
	private static final String XSS_REGEX_EVAL = ".*eval\\((.*?)\\).*";	
	private static final String XSS_REGEX_EXPRESSION = "expression\\((.*?)\\)\\)?";
	private static final String XSS_REGEX_JAVASCRIPT = ".*javascript:.*";
	private static final String XSS_REGEX_VBSCRIPT = ".*vbscript:.*";
	private static final String XSS_REGEX_ONLOAD = "onload(.*?)=";	
	private static final String XSS_REGEX_ON = "(.*?)<\\s*[a-z]+(.*?)(\\s+on\\S+|/on\\S+)(\\s*)=(.*?)";
	protected static final String XSS_REGEX_FUNCTION_RESTRICT = "(.*?)\\\"(.*?);((.*?)=(.*?)|(.*?)\\((.*?)\\))(.*?)\\/\\/";
	private static final String XSS_REGEX_FUNCTION = "(.*?)\\\"(.*?);(.*?)\\/\\/"; // Se troppo larga utilizzare quella RESTRICT
	private static final String XSS_REGEX_SQL_SELECT = "(.*?)SELECT(.*?)FROM(.*?)";
	private static final String XSS_REGEX_SQL_ORDERBY = "(.*?)ORDER(\\s+)BY(.*?)";	
	private static final String XSS_REGEX_ONLOAD_2 = ".*\\s+onload\\s*=\\s*(['\"]).*?\\1.*";

	
	private static Pattern[] patterns = new Pattern[] {Pattern.compile(XSS_REGEX_SCRIPT_1, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)	    
		    , Pattern.compile(XSS_REGEX_SRC, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
		    , Pattern.compile(XSS_REGEX_SCRIPT_2, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
		    , Pattern.compile(XSS_REGEX_SCRIPT_3, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
		    , Pattern.compile(XSS_REGEX_EVAL, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
		    , Pattern.compile(XSS_REGEX_EXPRESSION, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
		    , Pattern.compile(XSS_REGEX_JAVASCRIPT, Pattern.CASE_INSENSITIVE)	    
		    , Pattern.compile(XSS_REGEX_VBSCRIPT, Pattern.CASE_INSENSITIVE)
		    , Pattern.compile(XSS_REGEX_ONLOAD, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)   	
			, Pattern.compile(XSS_REGEX_ON, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
			, Pattern.compile(XSS_REGEX_FUNCTION, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
			, Pattern.compile(XSS_REGEX_SQL_SELECT, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
			, Pattern.compile(XSS_REGEX_SQL_ORDERBY, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
			, Pattern.compile(XSS_REGEX_ONLOAD_2, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)};
	
	private XSSDetector() {	
	}
	
	public static String canonicalize(String value) {		
        if (value != null && !value.isEmpty()) {
            value = ESAPI.encoder().canonicalize(value).replace("\0", "");
        }
        return value;
    }
	
	public static String stripXSS(String value) {		
        value = canonicalize(value);
        if (value != null && !value.isEmpty()) {
            for(Pattern pattern : patterns) {
            	value = pattern.matcher(value).replaceAll("");
            }            
        }
        return value;
    }

	public static void checXSS(String name, String value) {
		value = canonicalize(value);
		if (value != null && !value.isEmpty()) {
            for(Pattern pattern : patterns) {
	        	if (pattern.matcher(value).matches()) {
	        		// Encode per possibile visualizzazione del messaggio in pagina
	        		String message = ESAPI.encoder().encodeForHTML(String.format("Rilevato blocco di codice malevolo sul parametro %s", name));
	            	throw new IntrusionException(message, message);
	            }
            }
        }    
    }	
}
