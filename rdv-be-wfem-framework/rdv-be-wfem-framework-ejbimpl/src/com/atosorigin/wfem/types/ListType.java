package com.atosorigin.wfem.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.FieldRenderer;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class ListType implements java.io.Serializable {

    private java.util.List value;
    private Class modelType;

    private int totalPages = 1;
    private int currentPage = 1;
    private int currentIndex = 0;
    private int rowsInPage = -1;
    private int numRows = 0;
    private int lastPageRows = -1;
   	private boolean maxRowsExceeded = false;

   	private String[] columnNames;
   	private String[] columnLabels;
   	private String[] propertyNames;
   	
   	private transient FieldRenderer fieldRenderer = null;
   	
	/****************************************************************/
	/****************************************************************/
	public ListType() {
		this(null,new Vector(),-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(Class modelType) {
		this(modelType,new Vector(),-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(Class modelType, int listRows) {
		this(modelType,new Vector(),-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(Class modelType, List parameterValue) {
		this(modelType,parameterValue,-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(Class modelType, List parameterValue, int rowsInPage) {
		
		this.modelType = modelType;
		this.value = parameterValue;
		this.rowsInPage = rowsInPage;
	
		initPages(parameterValue,rowsInPage);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(List parameterValue) {
		this(null,parameterValue,-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(List parameterValue, int rowsInPage) {
		this(null,parameterValue,rowsInPage);
	}

	/****************************************************************/
	/****************************************************************/
	public ListType(CommandDataModel[] parameterValue) {
		this(null, Arrays.asList(parameterValue),-1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListType(CommandDataModel[] parameterValue, int rowsInPage) {
		this(null,Arrays.asList(parameterValue),rowsInPage);
	}

	
	/****************************************************************/
	/****************************************************************/
	public void recalc(){
		initPages(value,rowsInPage);		
	}
	
	/****************************************************************/
	/****************************************************************/
	public void addRecalc(CommandDataModel dataModel) {
		if(modelType == null)
			modelType = dataModel.getClass();
		value.add(dataModel);
		recalc();
	}

	/****************************************************************/
	/****************************************************************/
	public void add(CommandDataModel dataModel) {
		if(modelType == null)
			modelType = dataModel.getClass();
		value.add(dataModel);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void removeRecalc(int index){
		value.remove(index);
		recalc();
	}

	/****************************************************************/
	/****************************************************************/
	public void remove(int index){
		value.remove(index);
	}

	/****************************************************************/
	/****************************************************************/
	public void clear() {
		value.clear();
	}
	
	/****************************************************************/
	/****************************************************************/
	public CommandDataModel get(int idx) {
		return (CommandDataModel)value.get(idx);
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getAbsIndex(int offset) {
		return (getCurrentPage()-1) * getRowsInPage() + offset;
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getCurrentPage() {
		return currentPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public java.util.List getElements() {
		return value;
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getEndIndex() {
		if(numRows == 0)
			return 0;
	
		return getLastRowNum();
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getFirstRowNum() {
		if(numRows == 0)
			return 0;
			
		return currentIndex+1;
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListIterator getIterator() {
	    return value.listIterator();
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListIterator getIterator(int idx) {
	    return value.listIterator(idx);
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getLastRowNum() {
		if(numRows == 0)
			return 0;
			
		if(currentPage < totalPages)
			return currentIndex + rowsInPage;
		else
			return currentIndex + lastPageRows;
	}
	
	/****************************************************************/
	/****************************************************************/
	public java.lang.Class getModelType() {
		if(modelType != null)
			return modelType;
		if(size() > 0){
			Object el = this.get(0);
			modelType = el.getClass();
		}	
		return modelType;
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListIterator getPageRowsIterator() {
		if(numRows == 0)
			return value.listIterator();
		else
			return value.subList(getFirstRowNum()-1,getLastRowNum()).listIterator();
	}
	
	/****************************************************************/
	/****************************************************************/
	public ListIterator getPageRowsIterator(int idx) {
		if(numRows == 0)
			return value.listIterator(idx);
		else
			return value.subList(getFirstRowNum()-1,getLastRowNum()).listIterator(idx);
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getRowsPerPage() {
		return rowsInPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getRowsInPage() {
		if(rowsInPage < 0)
			return value.size();
		else
			return rowsInPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public static ListType getSizedListInstanceByContentClass(
	    Class contentType,
	    int size)
	    throws Exception {
	
	    List list = new ArrayList(size);
	
	    for (int i = 0; i < size; i++) {
	        list.add(Tools.initObject(contentType.newInstance()));
	    }
	
	    return new ListType(contentType,list);
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getStartIndex() {
		if(numRows == 0)
			return 0;
	
		return currentIndex;
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getTotalPages() {
		return totalPages;
	}
	
	/****************************************************************/
	/****************************************************************/
	private void initPages(List elements, int rowsInPage) {
		
		this.value = elements;
	
		this.rowsInPage = rowsInPage;
		this.numRows = value.size();
	
		if(rowsInPage > 0){
			if(numRows == 0){
				totalPages = 1;
			}else{
				totalPages = numRows / rowsInPage;
				if((numRows % rowsInPage) > 0){
					totalPages++;
					lastPageRows = numRows % rowsInPage;
				}else{
					lastPageRows = rowsInPage;
				}
			}
		}else{
			lastPageRows = numRows;
			totalPages = 1;
			currentPage = 1;
		   	currentIndex = 0;
		}
		
		if(currentPage > totalPages)
			currentPage = totalPages;
		if(currentIndex >= numRows)
			currentIndex = (totalPages-1) * rowsInPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void set(CommandDataModel dataModel, int idx) {
		value.set(idx,dataModel);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setCurrentPage(int newCurrentPage) {
		int curpage = getCurrentPage();
		if(newCurrentPage == curpage)
			return;
		if(newCurrentPage < curpage){
			currentPage = newCurrentPage;
			currentIndex = rowsInPage * (newCurrentPage - 1);
			return;
		}
		
		for(int i=curpage;i<newCurrentPage;i++)
			setNextPage();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setNextPage() {
	
		if(currentPage == totalPages)
			return;
	
		currentPage++;
		currentIndex += rowsInPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setPreviousPage() {
		if(currentPage == 1)
			return;
	
		currentPage--;
		currentIndex -= rowsInPage;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setFirstPage() {
	
		currentPage = 1;
		currentIndex = 0;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setLastPage() {
	
		if(currentPage == totalPages)
			return;
	
		currentPage = totalPages;
		currentIndex = rowsInPage * (totalPages - 1);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setRowsPerPage(int newRowsPerPage) {
		setRowsInPage(newRowsPerPage);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setRowsInPage(int newRowsInPage) {
	
	    this.rowsInPage = newRowsInPage;
	
	    this.initPages(this.value, newRowsInPage);
	
	}
	
	/****************************************************************/
	/****************************************************************/
	public int size() {
		return value.size();
	}
	
	
	/****************************************************************/
	/****************************************************************/
	public void setList(List newValue) {
		
		this.value = newValue;
	
		initPages(newValue,-1);
	}
		
	/*************************************************************************************************/
	/*************************************************************************************************/
	public boolean isMaxRowsExceeded() {
		return maxRowsExceeded;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setMaxRowsExceeded(boolean newMaxRowsExceeded) {
		maxRowsExceeded = newMaxRowsExceeded;
	}

	/****************************************************************/
	/****************************************************************/
	public FieldRenderer getFieldRenderer() {
		return fieldRenderer;
	}

	/****************************************************************/
	/****************************************************************/
	public void setFieldRenderer(FieldRenderer fieldRenderer) {
		this.fieldRenderer = fieldRenderer;
	}

	/****************************************************************/
	/****************************************************************/
	public String[] getColumnNames() {
		return columnNames;
	}

	/****************************************************************/
	/****************************************************************/
	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	/****************************************************************/
	/****************************************************************/
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/****************************************************************/
	/****************************************************************/
	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	/****************************************************************/
	/****************************************************************/
	public String[] getColumnLabels() {
		return columnLabels;
	}

	/****************************************************************/
	/****************************************************************/
	public void setColumnLabels(String[] columnLabels) {
		this.columnLabels = columnLabels;
	}

}
