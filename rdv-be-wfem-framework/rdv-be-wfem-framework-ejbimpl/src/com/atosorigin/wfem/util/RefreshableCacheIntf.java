package com.atosorigin.wfem.util;

import java.util.Enumeration;

/********************************************************************************/
/********************************************************************************/
public interface RefreshableCacheIntf {
	public Object get(Object key);
	public Object put(Object key, Object value);
	public Object remove(Object key);
	public Enumeration keys();
	public Object getElement(Object key);
	public int setElementCurrentTime(Object key, RefreshableCacheElement element, int scanTime);
	public boolean isScanned();
	public void setScanned(boolean scanned);
	public void setCacheName(String cacheName);
	public String getCacheName();
	public void clear();
}
