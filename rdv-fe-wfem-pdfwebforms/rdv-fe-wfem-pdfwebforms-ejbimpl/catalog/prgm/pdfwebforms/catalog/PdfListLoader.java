package prgm.pdfwebforms.catalog;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/*******************************************************************/
/*******************************************************************/
public class PdfListLoader implements Runnable{

	private CountDownLatch latch;
	private ArrayList<PdfAnagModel> pdfAnag;
	private ClientSessionContext csc;

	/*******************************************************************/
	/*******************************************************************/
	public PdfListLoader(ClientSessionContext csc, ArrayList<PdfAnagModel> pdfAnag){
		this.pdfAnag = pdfAnag;
		this.csc = csc;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void run() {
		DAOObject dao = null;
		try{
			dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
			dao.openConnection();
			for(int i=0;i<pdfAnag.size();i++)
				pdfAnag.get(i).getPdfModulo().setProdottiModulo(dao.executeQueryAccess("loadProdottiModulo",pdfAnag.get(i)).getResult());
		}catch(Throwable t){
			t.printStackTrace();
		}finally{
			if(dao != null) dao.closeConnection();
    		latch.countDown();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setLatch(CountDownLatch latch) {
		this.latch = latch;
	}

	/*******************************************************************/
	/*******************************************************************/
	public static void loadPdfList(ClientSessionContext csc, PdfCatalogModel catalog, boolean loadElencoProdotti) throws Exception, DAOException{
		
		DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfCatalog");
		catalog.setPdfList(dao.executeQueryAccess("pdfList",catalog.getPdfListParams()).getResult());
		if(catalog.getPdfList().size() == 0 || !loadElencoProdotti)
			return;
		
		ArrayList<PdfListLoader> threads = new ArrayList<PdfListLoader>();
		ArrayList<PdfAnagModel> listInThread = new ArrayList<PdfAnagModel>();
		for(int i=0;i<catalog.getPdfList().size();i++){
			if(threads.size() > 5){
				listInThread.add((PdfAnagModel)catalog.getPdfList().get(i));
				continue;
			}else{
				if(i > 0 && i % 50 == 0){
					threads.add(new PdfListLoader(csc, listInThread));
					listInThread = new ArrayList<PdfAnagModel>();
				}
			}
			listInThread.add((PdfAnagModel)catalog.getPdfList().get(i));
		}
		if(listInThread.size() > 0)
			threads.add(new PdfListLoader(csc, listInThread));
		CountDownLatch latch = new CountDownLatch(threads.size());
		for(PdfListLoader prodottiModuloLoader : threads){
			prodottiModuloLoader.setLatch(latch);
			Thread thread = new Thread(prodottiModuloLoader);
			thread.start();
		}
		latch.await();
	}
}
