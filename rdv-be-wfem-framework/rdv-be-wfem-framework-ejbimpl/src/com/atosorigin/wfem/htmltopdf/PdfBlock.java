package com.atosorigin.wfem.htmltopdf;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PdfBlock {
	
    int totalPages;

    public PdfBlock(){
	    totalPages = 0;
    }
    
    public int getTotalPages(){
	    return totalPages;
    }

    public void setTotalPages(int totalPages){
	    this.totalPages = totalPages;
    }

}
