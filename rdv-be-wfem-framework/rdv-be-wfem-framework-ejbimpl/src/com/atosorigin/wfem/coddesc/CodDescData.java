package com.atosorigin.wfem.coddesc;

public class CodDescData implements java.io.Serializable{

  private String  cod        = new String();
  private String  shortDescr = new String();
  private String  descr      = new String();
  private boolean valid     = true;

  public CodDescData() {
  }
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:20:42)
 * @return java.lang.String
 */
public java.lang.String getCod() {
	return cod;
}
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:20:42)
 * @return java.lang.String
 */
public java.lang.String getDescr() {
	return descr;
}
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:21:21)
 * @return boolean
 */
public boolean isValid() {
	return valid;
}
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:20:42)
 * @param newCod java.lang.String
 */
public void setCod(java.lang.String newCod) {
	cod = newCod;
}
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:20:42)
 * @param newDescr java.lang.String
 */
public void setDescr(java.lang.String newDescr) {
	descr = newDescr;
}
/**
 * Insert the method's description here.
 * Creation date: (29/08/2003 12:21:21)
 * @param newValid boolean
 */
public void setValid(boolean newValid) {
	valid = newValid;
}
/**
 * Returns the shortDescr.
 * @return String
 */
public String getShortDescr() {
	return shortDescr;
}

/**
 * Sets the shortDescr.
 * @param shortDescr The shortDescr to set
 */
public void setShortDescr(String shortDescr) {
	this.shortDescr = shortDescr;
}

}
