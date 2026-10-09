package com.atosorigin.wfem.util;

import java.io.*;
import java.util.*;

import com.atosorigin.wfem.controller.Configuration;

/********************************************************************************/
/********************************************************************************/
public class RefreshableSerializedCache extends RefreshableCache {
	
	private String CACHE_DIR = Configuration.getInstance().getSerializedCacheDir();
	private String PATH_SEP = File.separator;
	private boolean dirCreated = false;

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object get(Object key) {
		Object value = readSerializedObject(key);
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
		return writeSerializedObject(key, value);
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object remove(Object key) {
		return removeSerializedObject(key);
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized Enumeration keys(){
		
		Vector vf = new Vector();
		
		File f = new File(CACHE_DIR+this.getCacheName());
		if(!f.isDirectory())
			return vf.elements();
		File[] fs = f.listFiles();
		if(fs == null || fs.length == 0)
			return vf.elements();
			
		for(int i=0;i<fs.length;i++){
			String fname = fs[i].getName();
			vf.add(fname);
		}
		return vf.elements();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public synchronized Object getElement(Object key) {
		return readSerializedObject(key);
	}

	/********************************************************************************/
	/********************************************************************************/
	public synchronized int setElementCurrentTime(Object key, RefreshableCacheElement element, int scanTime) {
		int newTime = element.getCurrentTime() - scanTime;
		LOG.debug("Setting time of object ["+key+"] into cache ["+this+"] to value ["+newTime+"]");
		element.setCurrentTime(newTime);
		writeSerializedObject(key, element);
		return element.getCurrentTime();
	}

	/********************************************************************************/
	/********************************************************************************/
	private Object readSerializedObject(Object key){
		try{
			checkDir();
			FileInputStream fin = new FileInputStream(CACHE_DIR+this.getCacheName()+PATH_SEP+key.toString());
			ObjectInputStream in = new ObjectInputStream(fin);
			Object result = in.readObject();	
			in.close();
			fin.close();
			return result;
		}catch(FileNotFoundException fnf){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	private Object writeSerializedObject(Object key, Object value){
		try{
			checkDir();
			FileOutputStream fout = new FileOutputStream(CACHE_DIR+this.getCacheName()+PATH_SEP+key.toString());
			ObjectOutputStream out = new ObjectOutputStream(fout);
			out.writeObject(value);
			out.close();
			fout.close();
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.toString());
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	private Object removeSerializedObject(Object key){
		checkDir();
		File f = new File(CACHE_DIR+this.getCacheName()+PATH_SEP+key.toString());
		f.delete();
		return null;
	}

	/********************************************************************************/
	/********************************************************************************/
	private void checkDir(){
		if(dirCreated)
			return;
			
		File f = new File(CACHE_DIR+this.getCacheName());
		if(!f.exists())
			f.mkdir();
		dirCreated = true;
	}
}
