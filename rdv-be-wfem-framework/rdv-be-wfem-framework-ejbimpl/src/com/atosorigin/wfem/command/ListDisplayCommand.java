package com.atosorigin.wfem.command;

/**
 * Insert the type's description here.
 * Creation date: (12/05/2002 16.50.30)
 * @author: Administrator
 */
public abstract class ListDisplayCommand extends DisplayCommand {
/**
 * ListDisplayCommand constructor comment.
 */
public ListDisplayCommand() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 16.51.15)
 */
public void setNextPage(ListCommandDataModel listDataModel) {
	listDataModel.setNextPage();
}
/**
 * Insert the method's description here.
 * Creation date: (12/05/2002 16.52.03)
 */
public void setPreviousPage(ListCommandDataModel listDataModel) {
	listDataModel.setPreviousPage();
}
}
