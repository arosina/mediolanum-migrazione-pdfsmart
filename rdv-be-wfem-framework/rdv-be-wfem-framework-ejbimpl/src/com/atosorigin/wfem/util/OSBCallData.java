package com.atosorigin.wfem.util;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OSBCallData extends XmlServiceCallData implements Serializable {

	/********************************************************************************/
	/********************************************************************************/
	public int getServiceType() {
		return OSB_SERVICE_TYPE;
	}
	
}
