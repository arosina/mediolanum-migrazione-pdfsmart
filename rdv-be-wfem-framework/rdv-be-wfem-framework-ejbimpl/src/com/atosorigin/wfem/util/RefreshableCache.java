package com.atosorigin.wfem.util;

import java.util.Hashtable;

import com.atosorigin.wfem.loggers.RefresherLogger;

/********************************************************************************/
/********************************************************************************/
public class RefreshableCache extends Hashtable implements RefreshableCacheIntf {

	protected static RefresherLogger LOG = RefresherLogger.getInstance();
	private boolean scanned = false;
	private String cacheName = "";
	private Object o = new Object();

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object get(Object key) {
		Object value = super.get(key);
		if(value != null && value instanceof RefreshableCacheElement && isScanned()){
			if(!(value instanceof RefreshableExpirableCacheElement))
				((RefreshableCacheElement)value).resetElementTime();
		}
		return value;
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object put(Object key, Object value) {
		if(value != null && value instanceof RefreshableCacheElement && isScanned()){
			RefreshableCacheElement element = (RefreshableCacheElement)value;
			if(element.getRefreshTime() == 0) // Refresh time = 0 --> never cached
				return null;
			LOG.debug("Setting start time on key ["+key+"] of cache ["+this+"] to value ["+element.getRefreshTime()+"] (new element)");
			element.setCurrentTime(element.getRefreshTime());
		}
		return super.put(key, value);
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object remove(Object key) {
		return super.remove(key);
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized void clear() {
		super.clear();
	}

	/********************************************************************************/
	/********************************************************************************/
	public boolean isScanned() {
		return scanned;
	}

	/********************************************************************************/
	/********************************************************************************/
	public void setScanned(boolean scanned) {
		this.scanned = scanned;
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object getElement(Object key) {
		return super.get(key);
	}
	
	/********************************************************************************/
	/********************************************************************************/
    public String toString() {
		return getClass().getName() + "@" + o.toString();
    }

	/********************************************************************************/
	/********************************************************************************/
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/********************************************************************************/
	/********************************************************************************/
	public String getCacheName() {
		return this.cacheName;
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized int setElementCurrentTime(Object key, RefreshableCacheElement element, int scanTime) {
		int newTime = element.getCurrentTime() - scanTime;
		LOG.debug("Setting time of object ["+key+"] into cache ["+this+"] to value ["+newTime+"]");
		element.setCurrentTime(newTime);
		return element.getCurrentTime();
	}

}
