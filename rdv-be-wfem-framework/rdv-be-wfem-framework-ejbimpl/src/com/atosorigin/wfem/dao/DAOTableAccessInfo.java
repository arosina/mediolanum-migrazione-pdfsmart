package com.atosorigin.wfem.dao;


/*************************************************************************************************/
/*************************************************************************************************/
public class DAOTableAccessInfo extends DAOAccessInfo{
	
	public static final int CREATE_ACCESS_MODE 				= 1;
	public static final int LOAD_ACCESS_MODE   				= 2;
	public static final int UPDATE_ACCESS_MODE 				= 3;
	public static final int DELETE_ACCESS_MODE 				= 4;
	public static final int LOAD_ALL_ACCESS_MODE   			= 5;
	public static final int LOAD_CHILDS_ACCESS_MODE    		= 6;
	public static final int DELETE_CHILDS_ACCESS_MODE   	= 7;
	public static final int DELETE_ALL_ACCESS_MODE 			= 8;

	private String   identityProperty;
	private String 	 sequenceName;
	private String   orderBy;
	private boolean replicable;

	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOTableAccessInfo() {
		super();
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public java.lang.String getIdentityProperty() {
		return identityProperty;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setIdentityProperty(java.lang.String newIdentityProperty) {
		identityProperty = newIdentityProperty;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public boolean isReplicable() {
		return replicable;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setReplicable(boolean replicable) {
		this.replicable = replicable;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

}
