package com.atosorigin.wfem.util;

import com.atosorigin.wfem.command.*;
import com.atosorigin.wfem.types.*;
import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (11/06/2002 17.25.20)
 * @author: Administrator
 */
public class WebCombo implements java.io.Serializable {
	
	transient private static com.atosorigin.wfem.loggers.UtilLogger LOG = com.atosorigin.wfem.loggers.UtilLogger.getInstance();

	private Vector codes = new Vector();
	private Vector descriptions = new Vector();
/**
 * ComboType constructor comment.
 */
public WebCombo() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (27/08/2002 11.59.11)
 * @param items java.util.Map
 */
public WebCombo(Map items) {

    Iterator it = items.keySet().iterator();
    while (it.hasNext()) {

        Object code = it.next();
        Object description = items.get(code);

        addComboElement((String) code, (String) description);

    }

}
/**
 * Insert the method's description here.
 * Creation date: (11/06/2002 17.27.42)
 */
public void addComboElement(String code, String description) {
	codes.addElement(code);
	descriptions.addElement(description);
}
/**
 * Insert the method's description here.
 * Creation date: (11/06/2002 17.31.14)
 */
public String getComboElementCode(int idx) {
	return (String)codes.get(idx);
}
/**
 * Insert the method's description here.
 * Creation date: (11/06/2002 17.32.17)
 */
public String getComboElementDescription(int idx) {
	return (String)descriptions.get(idx);
}
/**
 * Insert the method's description here.
 * Creation date: (11/06/2002 17.48.46)
 */
public int getNumElements() {
	return codes.size();
}
/**
 * Insert the method's description here.
 * Creation date: (11/06/2002 17.34.13)
 */
public static String make(String propertyName, String comboName, CommandDataModel model) {

	String comboStringIni = "<SELECT name="+propertyName+">";
	String comboStringEnd = "</SELECT>";

	try{
		
		WebCombo comboValue = (WebCombo)Tools.getPropertyValue(model, comboName);
		AbstractType propertyValue = (AbstractType)Tools.getPropertyValue(model, propertyName);

		String propertyCode = propertyValue.toString();
		String options = "";
		
		for(int i=0;i<comboValue.getNumElements();i++){
			String code = comboValue.getComboElementCode(i);
			String desc = comboValue.getComboElementDescription(i);
			
			if(code.equals("%")){

				if(propertyValue == null || propertyValue.isNull())
					options += "<OPTION SELECTED>" + desc;
				else
					options += "<OPTION>" + desc;
				continue;
				
			}
			

			if(propertyCode.equals(code)){
				options += "<OPTION SELECTED value =" + code + ">";
			}else{
				options += "<OPTION value =" + code + ">";
			}
			options += desc;
		}

		return comboStringIni + options + comboStringEnd;
		
	}catch(Exception e){
		String errorMsg = "Exception in generating web combo for property "+propertyName+" from Combo "+comboName+": "+e;
		Exception ne = new Exception(errorMsg);
		LOG.error(ne);
		return comboStringIni + comboStringEnd;
	}
}
}
