package com.atosorigin.wfem.pdf;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletContext;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Constants;
import com.atosorigin.wfem.loggers.PdfLogger;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.util.Tools;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**************************************************************************************************/
/**************************************************************************************************/
public class AdobeFormCompiler {

	private static final String thisClassName = "AdobeFormCompiler";
	private static PdfLogger LOG = PdfLogger.getInstance();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static ByteArrayOutputStream deleteAdobeFormFields(InputStream pdf, String[] fieldNames, boolean flattening) throws Exception{
		if(fieldNames == null || fieldNames.length == 0)
			return null;
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	    AcroFields form = stamp.getAcroFields();
	    for(int i=0;i<fieldNames.length;i++)
	    	form.removeField(fieldNames[i]);
		stamp.setFormFlattening(flattening);
	    stamp.close();
	    pdfOut.close();
	    return pdfOut;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static ByteArrayOutputStream fillAdobeForm(InputStream pdf, CommandDataModel commandDataModel) throws Exception{
		return fillAdobeForm(pdf,commandDataModel,true);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static ByteArrayOutputStream fillAdobeForm(InputStream pdf, CommandDataModel commandDataModel, boolean flattening) throws Exception{
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(pdf);
	    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	    AdobeFormCompiler.fillAdobeForm(null,stamp,commandDataModel);
		stamp.setFormFlattening(flattening);
	    stamp.close();
	    pdfOut.close();
	    return pdfOut;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void fillAdobeForm(ServletContext appContext, PdfStamper stamp, CommandDataModel commandDataModel) throws Exception{

		if(commandDataModel == null)
			return;
				
	    try {
	
		    AcroFields form = stamp.getAcroFields();
		    if(form == null){
		    	LOG.debug(thisClassName+".fillAdobeForm: no form founded in document");
		    	return;
		    }
		    
	    	Iterator fields = form.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		
	    		String fieldName = (String)fields.next();
	    		if(fieldName.startsWith("barcode=")){
			    	LOG.debug(thisClassName+".fillAdobeForm: managing barcode");
	    			manageBarcode(stamp,form,fieldName,commandDataModel);
	    			continue;
	    		}
	    		
	    		if(fieldName.startsWith("image=")){
			    	LOG.debug(thisClassName+".fillAdobeForm: managing image");
	    			manageImage(appContext,stamp,form,fieldName,commandDataModel);
	    			continue;
	    		}
	    			
	    		LOG.debug(thisClassName+".fillAdobeForm: Managing form field ["+fieldName+"]");

	    		if(fieldName.equalsIgnoreCase("@today")){
					form.setField(fieldName,Tools.today().toString());
	    			continue;
	    		}
	    		
	    		try{
	    			if(fieldName.length() > 0)
	    				fieldName = fieldName.substring(0,1).toLowerCase()+fieldName.substring(1);
	    			
		    		String propName = fieldName.toString();
		    		AbstractType value = null;
	    			try{
			    		value = (AbstractType)Tools.getPropertyValue(commandDataModel,fieldName);
			    		if(value == null){
				    		LOG.debug(thisClassName+".fillAdobeForm: property ["+propName+"] not found or null. Maybe is a group field");
			    			int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
			    			if(idx < 0){
			    				LOG.warning(thisClassName+".fillAdobeForm: property ["+propName+"] not found or null. Continue");
			    				continue;
			    			}
		    				propName = propName.substring(0,idx);
		    				value = (AbstractType)Tools.getPropertyValue(commandDataModel,propName);	    			
			    			if(value == null){
			    				LOG.warning(thisClassName+".fillAdobeForm: property ["+propName+"] not found or null. Continue");
			    				continue;	    				
			    			}
			    		}
	    			}catch(Exception e){
	    				LOG.warning(thisClassName+".fillAdobeForm: exception on property ["+propName+"]. Continue");
	    				continue;	    				
	    			}

		    		String curModelName = "";
		    		CommandDataModel curModel = commandDataModel;
		    		int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
		    		if(idx >= 0){
		    			curModelName = propName.substring(0,idx);
		    			try{
		    				curModel = (CommandDataModel)Tools.getPropertyValue(commandDataModel,curModelName);
		    			}catch(Exception e){
		    				LOG.warning(thisClassName+".fillAdobeForm: exception getting model ["+curModelName+"]. Continue");
		    				continue;	    						    				
		    			}
		    			propName = propName.substring(idx+1);
		    		}
		    		
		    		LOG.debug(thisClassName+".fillAdobeForm: Managing property ["+propName+"] in model ["+curModel+"]");
	
					boolean codDescField = false;
	                String propValue = null;
	                String propPrintValue = null;
	                
	                if(value instanceof BooleanType){		                
	                    boolean boolValue = ((BooleanType) value).booleanValue();
	                    propPrintValue = "X";
	                    ((AbstractType) value).setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
	                    if (boolValue){
							propValue = "true";
	                    }else{
							propValue = "false";
	                    }	                    
	                }else{	
	                	
						// Field is registered as CodDescField
					    String codDescReferenceName = curModel.getCodDescReference(propName);
					    if(codDescReferenceName != null)
							codDescField = true;
							
	                    propValue = ((AbstractType) value).toString();
	                    propPrintValue = ((AbstractType) value).getPrintableValue();
	                    
	                }
	
	                // If value is null or empty I can skip it
	                if (propPrintValue == null || propPrintValue.equals("") || (!((AbstractType) value).isVisible()) )
	                    continue;
	
	                // Manage the group values. In PDF the field must have the
	                // name of the property plus "_value"
	                // Manage the group values. In PDF the field must have the
	                // name of the property plus "_value"
	                String fdfPropName = curModelName.toString();
	                if(!curModelName.equals(""))
	                	fdfPropName += Constants.NESTED_INDICATOR;
	                int printableType = ((AbstractType) value).getPrintableType();
	                if(printableType == AbstractType.PRINTABLE_GROUP_TYPE)
	                    fdfPropName += propName + Constants.NESTED_INDICATOR + propValue;
	                else
	                    fdfPropName += propName;
	
					if(form.setField(fdfPropName,propPrintValue) && LOG != null)
					    LOG.debug(thisClassName+".fillAdobeForm: Setted field ["+fdfPropName+"] to value ["+propPrintValue+"]");
	                
	                if(codDescField){
						String cod = propValue;
						String descr = curModel.getDescValue(propName);
	                    
						if(form.setField(fdfPropName+"_cod",cod) && LOG != null)
						    LOG.debug(thisClassName+".fillAdobeForm: Setted field ["+fdfPropName+"] to value ["+cod+"]");
		                
						if(form.setField(fdfPropName+"_descr",descr) && LOG != null)
						    LOG.debug(thisClassName+".fillAdobeForm: Setted field ["+fdfPropName+"] to value ["+descr+"]");
	
						if(form.setField(fdfPropName+"_coddescr",cod+" - "+descr) && LOG != null)
						    LOG.debug(thisClassName+".fillAdobeForm: Setted field ["+fdfPropName+"] to value ["+cod+" - "+descr+"]");
					}
	    		}catch(Exception e){
	    			LOG.error(new Exception(thisClassName+".fillAdobeForm: exception in managing field ["+fieldName+"] exception: ["+e.toString()+"]"));	    			
	    		}

	    	}
	        return;
	
	    }catch (Exception e){	
	        String errorMsg = thisClassName+".fillAdobeForm: Exception filling form: " + e;
	        e = new Exception(errorMsg);
	        throw e;
	    }
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageBarcode(PdfStamper stamp, AcroFields form, 
									  String fieldName, CommandDataModel model){
		
		try{
			String propName = fieldName.toString();
			
			int idx = propName.indexOf("#");
			if(idx < 0){
				idx = propName.indexOf("^");
				if(idx < 0){
					LOG.error(new Exception(thisClassName+".manageBarcode: wrong barcode syntax. Use # or ^ to specify barcode type"));
					return;
				}
			}
			
			String barcodeType = propName.substring(idx,idx+1);
			propName = propName.substring(idx+1);

			AbstractType value = null;
			try{
				value = (AbstractType)Tools.getPropertyValue(model,propName);
				if(value == null)
					return;
			}catch(Exception e){
				LOG.warning(thisClassName+".manageBarcode: exception getting barcoded property ["+propName+"]. Continue");
				return;
			}
			
			String code = value.toString();
			if(code.equals(""))
				return;
			
		    Barcode128 barcode = new Barcode128();
		    barcode.setCode(code);
		    barcode.setBarHeight(50);
		    barcode.setX(1.5f);		
		    
		    float[] fp = form.getFieldPositions(fieldName);
		    for(int i=0;i<fp.length;i+=5){
			    
		    	int   pageNum = (int)fp[i+0];
			    float x = fp[i+1];
			    float y = fp[i+2];
			    float h = fp[i+4]-fp[i+2];
			    float w = fp[i+3]-fp[i+1];
			    
			    Image barcodeImage = null;
			    PdfContentByte cb = stamp.getOverContent(pageNum);
			    if(barcodeType.equals("#"))
			    	barcodeImage = barcode.createImageWithBarcode(cb,null,null);
		        else
		        	barcodeImage = barcode.createImageWithBarcode(cb,java.awt.Color.black,java.awt.Color.white);
			    
		        barcodeImage.setAbsolutePosition(x,y);
		        barcodeImage.scaleToFit(w,h);
		        cb.addImage(barcodeImage);
		    }
		    
		}catch(Exception e){
			LOG.error(new Exception(thisClassName+".manageBarcode: exception in creating barcode on field ["+fieldName+"] exception: ["+e.toString()+"]"));
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageImage(ServletContext appContext, PdfStamper stamp, AcroFields form, 
									String fieldName, CommandDataModel model){
		
		try{
			
			String propName = fieldName.substring("image=".length());
			AbstractType value = null;
			try{
				value = (AbstractType)Tools.getPropertyValue(model,propName);
				if(value == null)
					return;
			}catch(Exception e){
				LOG.warning(thisClassName+".manageImage: exception getting image property ["+propName+"]. Continue");
				return;
			}

			String imageName = value.toString();
			Image image = null;				
			try{
				try{
					java.net.URL imageUrl = null;
					if(appContext != null)
						imageUrl = appContext.getResource(imageName);
					else
						imageUrl = new java.net.URL(imageName);
					image = Image.getInstance(imageUrl);					
				}catch(java.net.MalformedURLException mu){					
					image = Image.getInstance(imageName);
				}
			}catch(Exception e){
				image = null;
			}
			if(image == null)
				return;
			
		    float[] fp = form.getFieldPositions(fieldName);
		    for(int i=0;i<fp.length;i+=5){
			    
		    	int   pageNum = (int)fp[i+0];
			    float x = fp[i+1];
			    float y = fp[i+2];
			    float h = fp[i+4]-fp[i+2];
			    float w = fp[i+3]-fp[i+1];
			    
			    PdfContentByte cb = stamp.getOverContent(pageNum);
			    
		        image.setAbsolutePosition(x,y);
		        image.scaleToFit(w,h);
		        cb.addImage(image);
		    }

		}catch(Exception e){
			LOG.error(new Exception(thisClassName+".manageImage: exception in creating image on field ["+fieldName+"] exception: ["+e.toString()+"]"));
		}
	}
}
