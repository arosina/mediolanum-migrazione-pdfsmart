package com.atosorigin.wfem.pdf;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.Constants;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;
import com.lowagie.text.pdf.*;

/**************************************************************************************************/
/**************************************************************************************************/
public class VerifyPdfFields {

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void main(String[] args) {
		if(args.length != 2){
			System.err.println("usage: java -cp%CLASSPATH% com.atosorigin.wfem.pdf.VerifyPdfFields pdfFileName modelClassName");
			System.err.println("Remeber to set CLASSPATH to wfem and the modelClassName jar");
			System.exit(1);
		}
		
		try{
			Configuration.getInstance();
		}catch(Exception e){
    		System.out.println("Exceptions in wfem.properties file is not a problem");			
		}

		try{

			String pdfFileName = args[0];
			String modelClassName = args[1];
			
    		System.out.println("Opening pdf file ["+pdfFileName+"]...");
		    ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	        PdfReader reader = new PdfReader(pdfFileName);
	        PdfStamper stamp = new PdfStamper(reader,pdfOut);
	        
    		System.out.println("Creating new instance of model ["+modelClassName+"]...");
    		Class c = Class.forName(modelClassName);
    		CommandDataModel commandDataModel = (CommandDataModel)c.newInstance(); 
	        
	        AcroFields form = stamp.getAcroFields();
	    	Iterator fields = form.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		
	    		String fieldName = (String)fields.next();
	    		String propName = fieldName.toString();
	    		if(propName.startsWith("barcode=")){
	    			manageBarcode(propName,commandDataModel);
	    			continue;
	    		}
	    			
	    		System.out.print("Managing form field ["+fieldName+"]...");
	    		
	    		try{
	    			
		    		AbstractType value = (AbstractType)Tools.getPropertyValue(commandDataModel,propName);
		    		if(value == null){
		    			int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
		    			if(idx < 0){
		    	    		System.out.println("not found");
		    				continue;
		    			}
	    				propName = propName.substring(0,idx);
	    				value = (AbstractType)Tools.getPropertyValue(commandDataModel,propName);	    			
		    			if(value == null){
		    	    		System.out.println("not found");
		    				continue;	    				
		    			}
		    		}
		    		System.out.println("found");
		    		
	    		}catch(Exception e){
	    			System.out.println("Exception on field ["+fieldName+"] exception: ["+e.toString()+"]");
	    		}
	    	}
	        stamp.close();
	        
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageBarcode(String fieldName, CommandDataModel model) throws Exception{
		
		System.out.print("Managing barcode field ["+fieldName+"]...");

		String propName = fieldName.toString();
		
		int idx = propName.indexOf("#");
		if(idx < 0){
			idx = propName.indexOf("^");
			if(idx < 0){
				System.err.println("Wrong barcode syntax. Use # or ^ to specify barcode type");
				return;
			}
		}
		
		try{
			propName = propName.substring(idx+1);
			
			AbstractType value = (AbstractType)Tools.getPropertyValue(model,propName);
			if(value == null)
	    		System.out.println("not found");				
			else
	    		System.out.println("found");
		}catch(Exception e){
			System.out.println("Exception on barcode field ["+fieldName+"] exception: ["+e.toString()+"]");			
		}
	}
}
