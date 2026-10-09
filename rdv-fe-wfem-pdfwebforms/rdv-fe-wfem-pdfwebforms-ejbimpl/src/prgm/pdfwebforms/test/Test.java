package prgm.pdfwebforms.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Set;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Test {
	
	public static void main(String[] args) {
		try{
			Test t = new Test();

			InputStream pdf = t.getClass().getResourceAsStream("/prgm/pdfwebforms/test/test.pdf");

			PdfReader reader = new PdfReader(pdf);
		    ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    AcroFields acroForm = stamp.getAcroFields();
		    
		    Item itemc = acroForm.getFieldItem("combo");
		    System.out.println("*** COMBO ***");
		    printItem(itemc);
		    String[] ops = acroForm.getListOptionDisplay("combo");
		    for(String op : ops)
		    	System.out.println("op :"+op);
		    ops = acroForm.getListOptionExport("combo");
		    for(String op : ops)
		    	System.out.println("op :"+op);
		    	
		    Item item1 = acroForm.getFieldItem("c1");
		    System.out.println("*** C1 ***");
		    printItem(item1);
		    System.out.println("*********");
		    
		    Item item2 = acroForm.getFieldItem("c2");
		    System.out.println("*** C2 ***");
		    printItem(item2);
		    System.out.println("*********");

		    System.out.println("1 vs 2");
		    manageItem(item1,item2);
		    System.out.println("2 vs 1");
		    manageItem(item2,item1);
			
			if(stamp != null) stamp.close();
		    if(pdfOut != null) pdfOut.close();
		    if(reader != null) reader.close();
		    if(pdf != null) pdf.close();
			
		    System.out.println("Fatto!");
		    
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	private static void printItem(Item item){
	    for(int k = 0; k < item.size(); k++){
            PdfDictionary dict = item.getMerged(k);
            
            Set<PdfName> keys = dict.getKeys();
            for(PdfName pn : keys){
           		System.out.println("  "+pn+": "+dict.get(pn));
            }
        }
	}
	
	private static void manageItem(Item item1, Item item2){
	    for(int k = 0; k < item1.size(); k++){
            PdfDictionary dict1 = item1.getMerged(k);
            PdfDictionary dict2 = null;
            try{dict2 = item2.getMerged(k);}catch(Exception e){dict2 = null;}
            
            Set<PdfName> keys = dict1.getKeys();
            for(PdfName pn : keys){
            	PdfObject o1 = dict1.get(pn);
            	PdfObject o2 = dict2==null?null:dict2.get(pn);
            	String v1 = o1==null?"null":o1.toString();
            	String v2 = o2==null?"null":o2.toString();
            	if(!v1.equals(v2)){
            		System.out.println("  "+pn+": "+v1+" ->> "+v2);
            	}
            	System.out.println("*** "+dict2.getAsNumber(PdfName.FF).intValue());
            }
        }
	}
}
