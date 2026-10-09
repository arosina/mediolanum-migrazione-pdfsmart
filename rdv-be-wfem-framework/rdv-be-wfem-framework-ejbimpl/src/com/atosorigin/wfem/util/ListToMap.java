package com.atosorigin.wfem.util;

import java.util.*;
/**
 * Insert the type's description here.
 * Creation date: (27/09/2002 11.52.28)
 * @author: Administrator
 */
public class ListToMap extends java.util.HashMap {
/**
 * ListToMap constructor comment.
 */
public ListToMap() {
	super();
}
/**
 * ListToMap constructor comment.
 * @param initialCapacity int
 */
public ListToMap(int initialCapacity) {
	super(initialCapacity);
}
/**
 * ListToMap constructor comment.
 * @param initialCapacity int
 * @param loadFactor float
 */
public ListToMap(int initialCapacity, float loadFactor) {
	super(initialCapacity, loadFactor);
}
/**
 * ListToMap constructor comment.
 * @param t java.util.Map
 */
public ListToMap(java.util.Map t) {
	super(t);
}
/**
 * Insert the method's description here.
 * Creation date: (30/09/2002 16.48.28)
 * @return java.util.Map
 * @param keyPropertyName java.lang.String
 * @param source java.util.List
 */
public static Map getFromList(String keyPropertyName, List source)
    throws Exception {
	    
    ListToMap map = new ListToMap();
    map.setList(keyPropertyName, source);
    return map;
}
/**
 * Insert the method's description here.
 * Creation date: (27/09/2002 11.52.35)
 */
public void setList(String keyPropertyName, List source) throws Exception {
	clear();
	Iterator it = source.iterator();
	while(it.hasNext()){
		Object obj = it.next();
		put(Tools.getPropertyValue(obj,keyPropertyName),obj);
	}
}
}
