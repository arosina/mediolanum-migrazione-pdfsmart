package com.atosorigin.wfem.command;

import java.util.*;
import com.atosorigin.wfem.types.*;

/**
 * Insert the type's description here.
 * Creation date: (12/05/2002 16.34.08)
 * @author: Administrator
 */
public abstract class ListCommandDataModel extends CommandDataModel {

	private ListType rows = new ListType();
/**
 * Insert the method's description here.
 * Creation date: (23/06/2002 22.31.45)
 */
public ListCommandDataModel() {
}
public ListCommandDataModel(ListType elements) {

	rows = elements;
}
/**
 * ListCommandDataModel constructor comment.
 */
public ListCommandDataModel(ListType elements, int rowsInPage) {

	if(elements.getModelType() != null)
		rows = new ListType(elements.getModelType(),elements.getElements(), rowsInPage);
	else
		rows = new ListType(elements.getElements(), rowsInPage);

}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.01.39)
 * @return int
 */
public int getCurrentPage() {
	return rows.getCurrentPage();
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 18:02:34)
 */
public int getEndIndex() {
	return rows.getEndIndex();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.11.22)
 */
public int getFirstRowNum() {
	return rows.getFirstRowNum();
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 17:28:36)
 */
public ListIterator getIterator() {
	return rows.getIterator();
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 17:28:57)
 */
public ListIterator getIterator(int idx) {
	return rows.getIterator(idx);
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.11.59)
 */
public int getLastRowNum() {
	return rows.getLastRowNum();
}
/**
 * Insert the method's description here.
 * Creation date: (21/10/2002 11:25:37)
 */
public java.lang.Class getModelType() {
	return rows.getModelType();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 17.14.58)
 * @return int
 */
public int getNumRows() {
	return rows.size();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.50.51)
 */
public CommandDataModel getRowAt(int idx) {
	return (CommandDataModel)rows.get(idx);
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.02.48)
 * @return java.util.Vector
 */
public ListType getRows() {
	return rows;
}
/**
 * Insert the method's description here.
 * Creation date: (9/5/2002 10:12:50 PM)
 * @return int
 */
public int getRowsInPage() {
	return rows.getRowsInPage();
}
/**
 * Insert the method's description here.
 * Creation date: (24/09/2002 18:02:55)
 */
public int getStartIndex() {
	return rows.getStartIndex();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.07.43)
 * @return int
 */
public int getTotalPages() {
	return rows.getTotalPages();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.18.47)
 */
public void setNextPage() {
	rows.setNextPage();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 18.20.09)
 */
public void setPreviousPage() {
	rows.setPreviousPage();
}
/**
 * Insert the method's description here.
 * Creation date: (31/10/2002 11.53.46)
 */
public void setRowsInPage(int newRowsInPage) {

	rows.setRowsInPage(newRowsInPage);

}
public void setRows(ListType newRows) {
	this.rows = newRows;
}
}
