package com.atosorigin.wfem.coddesc;

import java.util.Iterator;
import java.util.Vector;

import com.atosorigin.wfem.loggers.ControllerLogger;
import com.atosorigin.wfem.util.RefreshableExpirableCacheElement;

/********************************************************************************/
/********************************************************************************/
public class CodDescDataList extends RefreshableExpirableCacheElement implements java.io.Serializable {

	private int refreshTime = -1;
  	private Vector elements = new Vector();
  
 	/********************************************************************************/
	/********************************************************************************/
	public  CodDescDataList(){}

 	/********************************************************************************/
	/********************************************************************************/
	public  CodDescDataList(CodDescDataList src){
		elements = new Vector(src.elements);
	}
  
	/********************************************************************************/
	/********************************************************************************/
	public void addCodDescData(CodDescData codDescData){
	    elements.add(codDescData);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public CodDescData getCodDesc(int i){
		return (CodDescData)elements.get(i);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public void removeCodDesc(int i){
		elements.remove(i);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public CodDescData getCodDesc(String cod){
		for(int i=0;i<getCodDescCount();i++){	
	          CodDescData data = getCodDesc(i);
	          if(data.getCod().equals(cod))
		          return data;
	    }
	    return null;
	}
	  
	/********************************************************************************/
	/********************************************************************************/
	public void removeCodDesc(String cod){
		for(Iterator it = elements.iterator(); it.hasNext();){
			CodDescData data = (CodDescData) it.next();
			if(data.getCod().equals(cod)) {
				elements.remove(data);
				return;
			}
		}
	}
	  
	/********************************************************************************/
	/********************************************************************************/
	public void removeNotValidElements(){
		for(int i=elements.size()-1;i>=0;i--){
			CodDescData data = (CodDescData)elements.get(i);
			if(!data.isValid()) {
				elements.remove(i);
			}
		}
	}
	  
	/********************************************************************************/
	/********************************************************************************/
	public int getCodDescCount(){
		return elements.size();
	}

	/********************************************************************************/
	/********************************************************************************/
	public int getRefreshTime() {
		return refreshTime;
	}

	/********************************************************************************/
	/********************************************************************************/
	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}

}
