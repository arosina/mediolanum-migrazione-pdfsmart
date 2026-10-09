package com.atosorigin.wfem.util;

import java.util.Enumeration;

import com.atosorigin.wfem.loggers.LogPrinter;
import com.atosorigin.wfem.loggers.RefresherLogger;

/********************************************************************************/
/********************************************************************************/
public class RefreshNotifier implements Runnable {

	private static RefresherLogger LOG = RefresherLogger.getInstance();

	private RefreshableCacheContainer cacheContainer;
	private RefreshableCacheIntf cache;
	private RefreshEventListener listener;
	private int scanTime = 0;
	
	/********************************************************************/
	/********************************************************************/
	private RefreshNotifier(RefreshableCacheContainer cacheContainer,
						     RefreshableCacheIntf cache, int scanTime){
		this.cacheContainer = cacheContainer;
		this.cache = cache;
		this.scanTime = scanTime;
		this.cache.setScanned(true);
	}
	
	/********************************************************************/
	/********************************************************************/
	private RefreshNotifier(RefreshEventListener listener,
						   int scanTime){
		this.listener = listener;
		this.scanTime = scanTime;
	}
	
	/********************************************************************/
	/********************************************************************/
	public void run() {

		while(true){
			try{
				Thread.currentThread().sleep(scanTime*1000);
				try{
					if(listener != null){
						
						listener.refresh();
						
					}else{

						synchronized(cache){
							
							LOG.debug("Scanning cache ["+cache+"] after ["+scanTime+"] seconds");

							Enumeration cacheKeys = cache.keys();
							while(cacheKeys.hasMoreElements()){
								
								Object key = cacheKeys.nextElement();
								RefreshableCacheElement element = (RefreshableCacheElement)cache.getElement(key);
								int refreshTime = element.getRefreshTime();
								if(refreshTime == 0){ // 0 refresh: expire after a scan (strange case: see put in RefreshableCache)
									
									LOG.debug("Removing element for key ["+key+"] from cache ["+cache+"]");
									Object o = cache.remove(key);
									o = null;
										
								}else if(refreshTime < 0){ // Negative refresh:
															// - If refreshed expire after a scan,
														    // - If serialized remain in cache
									
									if(!(cache instanceof RefreshableSerializedCache)){
										LOG.debug("Removing element for key ["+key+"] from cache ["+cache+"]");
										Object o = cache.remove(key);
										o = null;
									}
									
								}else if(refreshTime > 0){ // Positive refresh = expire after period
									
									if(cache.setElementCurrentTime(key,element,scanTime) <= 0){
										LOG.debug("Removing element for key ["+key+"] from cache ["+cache+"]");
										Object o = cache.remove(key);
										o = null;
									}
										
								}
								
							}
						}
					}
				}catch(Exception e){
					String errorMsg = getClass()+" Exception ["+e+"] doing scan";
					e = new Exception(errorMsg);
					e.printStackTrace();
				}
			}catch(Exception e){
				LogPrinter.println(getClass()+" Cache scanning terminated for reason ["+e+"]");
				break;
			}
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public static void addCache(RefreshableCacheContainer cacheContainer,
								  RefreshableCacheIntf cache, int threadScanTime){
		if(threadScanTime <= 0){
			LOG.debug("Cache ["+cache+"] is addeded with negative scan time. No thread is started");
			cache.setCacheName(cache.toString());
			return;
		}
		RefreshNotifier refresher = new RefreshNotifier(cacheContainer,cache,threadScanTime);
		Thread refresherThread = new Thread(refresher);
		cache.setCacheName(cache.toString());
		refresherThread.setDaemon(true);
		refresherThread.start();
	}
	
	/********************************************************************/
	/********************************************************************/
	public static void addListener(RefreshEventListener listener,
								     int threadScanTime){
		if(threadScanTime <= 0){
			LOG.debug("Listener ["+listener+"] is addeded with negative scan time. No thread is started");
			return;
		}
		RefreshNotifier refresher = new RefreshNotifier(listener,threadScanTime);
		Thread refresherThread = new Thread(refresher);
		refresherThread.setDaemon(true);
		refresherThread.start();
	}
}
