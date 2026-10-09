package prgm.pdfwebforms.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.atosorigin.wfem.util.RefreshNotifier;
import com.atosorigin.wfem.util.RefreshableCache;
import com.atosorigin.wfem.util.RefreshableCacheContainer;
import com.atosorigin.wfem.util.RefreshableCacheElement;
import com.atosorigin.wfem.util.RefreshableCacheIntf;

import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************
/********************************************************************************/
public class PdfCodedMessageCache extends RefreshableCache implements RefreshableCacheContainer{
	
	private static final int REFRESH_TIME = 60; // Refresh every 60 seconds
	
    private static 		PdfCodedMessageCache singleton = null;
    private transient 	RefreshableCacheIntf propertiesCache = null;
    
	/********************************************************************************
	/********************************************************************************/
    public class PropertyCacheElement extends RefreshableCacheElement{
    	private Properties property;
    	/********************************************************************************
    	/********************************************************************************/
    	public int getRefreshTime() {
    		return REFRESH_TIME;
    	}
    }

	/********************************************************************************
	/********************************************************************************/
	private PdfCodedMessageCache(){
		propertiesCache = new RefreshableCache();
	    RefreshNotifier.addCache(this,propertiesCache,REFRESH_TIME);
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected static synchronized PdfCodedMessageCache getInstance() {
        if (singleton == null)
            singleton = new PdfCodedMessageCache();
		return singleton;
	}

	/********************************************************************************
	/********************************************************************************/
	public static Properties getCodesProperty(String fileName) throws IOException{
		InputStream inputStream = PdfModel.class.getResourceAsStream("/"+fileName);
		Properties p = new Properties();
		p.load(inputStream);
		return p;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public Properties getCachedCodesProperty(String fileName) throws IOException{
		PropertyCacheElement pEl = (PropertyCacheElement)propertiesCache.get(fileName);
		if(pEl == null) {
			Properties p = getCodesProperty(fileName);
			pEl = new PropertyCacheElement();
			pEl.property = p;
			propertiesCache.put(fileName, pEl);
		}
		return pEl.property;
	}

}
