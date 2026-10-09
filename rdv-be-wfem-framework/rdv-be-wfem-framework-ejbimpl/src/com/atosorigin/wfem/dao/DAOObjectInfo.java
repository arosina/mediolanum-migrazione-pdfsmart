package com.atosorigin.wfem.dao;

import java.util.*;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.util.RefreshableCacheElement;

/********************************************************************************/
/********************************************************************************/
public class DAOObjectInfo extends RefreshableCacheElement{
	
	private String  daoObjectName;
	private String  dataSourceReferenceName;
	private HashMap daoAccessInfos = new HashMap();
	private HashMap daoCodDescLoadInfos = new HashMap();
	
	/********************************************************************************/
	/********************************************************************************/
	public DAOObjectInfo() {
		super();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public int getRefreshTime() {
		return Configuration.getInstance().getDaoObjectsLifeTime();
	}

	/********************************************************************************/
	/********************************************************************************/
	public void addDaoAccessInfo(DAOAccessInfo newDaoAccessInfo) {
		String daoAccessInfoName = newDaoAccessInfo.getDaoAccessName();
		daoAccessInfos.put(daoAccessInfoName,newDaoAccessInfo);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public void addDaoCodDescLoadInfo(DAOCodDescLoadInfo newDaoCodDescLoadInfo) {
		String codDescName = newDaoCodDescLoadInfo.getCodDescName();
		daoCodDescLoadInfos.put(codDescName,newDaoCodDescLoadInfo);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public DAOAccessInfo getDaoAccessInfo(String daoAccessInfoName) {
		return (DAOAccessInfo)daoAccessInfos.get(daoAccessInfoName);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public DAOCodDescLoadInfo getDaoCodDescLoadInfo(String daoCodDescName) {
		return (DAOCodDescLoadInfo)daoCodDescLoadInfos.get(daoCodDescName);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public java.lang.String getDataSourceReferenceName() {
		return dataSourceReferenceName;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public void setDataSourceReferenceName(java.lang.String newDataSourceReferenceName) {
		dataSourceReferenceName = newDataSourceReferenceName;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public String getDaoObjectName() {
		return daoObjectName;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public void setDaoObjectName(String daoObjectName) {
		this.daoObjectName = daoObjectName;
	}

}
