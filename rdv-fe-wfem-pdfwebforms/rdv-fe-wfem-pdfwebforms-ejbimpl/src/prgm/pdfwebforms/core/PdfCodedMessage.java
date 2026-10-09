package prgm.pdfwebforms.core;

import java.util.Properties;

import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.TypeError;

import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;

/**************************************************************************************************/
/**************************************************************************************************/
public class PdfCodedMessage {

	public static String MESSAGE_CODE_PREFIX = "@#@#@";
	
   /**************************************************************************************************/
	/**************************************************************************************************/
	private PdfCodedMessage() {}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public static void addTypeError(AbstractType field, String code) {
		addTypeError(field, code, null);
	}
	public static void addTypeError(AbstractType field, String code, String[] pars) {
		String errCode = MESSAGE_CODE_PREFIX+getPropFileName()+"_"+code;
		TypeError err = new TypeError(errCode);
		if(pars != null) {
			if(pars.length >= 4) err = new TypeError(errCode, pars[0], pars[1], pars[2], pars[3]);
			else if(pars.length == 3) err = new TypeError(errCode, pars[0], pars[1], pars[2]);
			else if(pars.length == 2) err = new TypeError(errCode, pars[0], pars[1]);
			else if(pars.length == 1) err = new TypeError(errCode, pars[0]);
		}
		field.addTypeError(err);
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public static void addError(AbstractBusinessEventOutputData out, String code){
		addError(out, code, null);
	}
	public static void addError(AbstractBusinessEventOutputData out, String code, String[] pars){
		String propFileName = getPropFileName();
		String error = getMessage(MESSAGE_CODE_PREFIX+propFileName+"_"+code, pars, "Error");
		out.addError(MESSAGE_CODE_PREFIX+propFileName+code+"_"+error);
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public static void addWarning(AbstractBusinessEventOutputData out, String code){
		addWarning(out, code, null);
	}
	public static void addWarning(AbstractBusinessEventOutputData out, String code, String[] pars){
		String propFileName = getPropFileName();
		String warning = getMessage(MESSAGE_CODE_PREFIX+propFileName+"_"+code, pars, "Warning");
		out.addWarning(MESSAGE_CODE_PREFIX+propFileName+code+"_"+warning);
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	private static String getPropFileName() {
		StackTraceElement ste = findCallerClass();
		String[] pks = ste.getClassName().split("\\.");
		String fname = pks[2];
		if(!pks[1].equals("pdfwebformsdrivers"))
			fname = pks[1];
		return fname.toLowerCase();
	}

    /**************************************************************************************************/
	/**************************************************************************************************/
	private static StackTraceElement findCallerClass() {
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for(int i=stack.length-1;i>=0;i--) {
			if(stack[i].getClassName().equals(PdfCodedMessage.class.getName()))
				return stack[i+1]; // Caller
		}
		return stack[0]; // Not found
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getMessage(String msgCode, Object[] pars, String propFileSuffix){
		try {
			// formato msgCode: @#@#@pkg%_%code%
			String fileName = msgCode.substring(PdfCodedMessage.MESSAGE_CODE_PREFIX.length(),msgCode.indexOf("_"))+propFileSuffix+"Codes.properties";
			Properties property = PdfCodedMessageCache.getCodesProperty(fileName);
			String msg = property.getProperty(msgCode.substring(msgCode.indexOf("_")+1));
			if(pars != null) {
				if(pars.length >= 4 && pars[3] != null) msg = msg.replace("%4", pars[3].toString());
				if(pars.length >= 3 && pars[2] != null) msg = msg.replace("%3", pars[2].toString());
				if(pars.length >= 2 && pars[1] != null) msg = msg.replace("%2", pars[1].toString());
				if(pars.length >= 1 && pars[0] != null) msg = msg.replace("%1", pars[0].toString());
			}
			return msg;
		}catch(Exception e) {
			return msgCode; // Torno il codice
		}
	}

}
