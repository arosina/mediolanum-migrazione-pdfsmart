package prgm.pdfwebforms.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfPageIdxModel;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfPageAnagModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfNasUtil {
	
	private static String SHARE_NAME = "PDFWEBFORMS";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean blobPdfInstanceSuDB(ClientSessionContext csc) throws Exception{
		return PdfConfig.getParamAsBool(csc, "PDF_INSTANCE", "BLOB_SU_DB").booleanValue();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean blobPublisherSuDB(ClientSessionContext csc) throws Exception{
		return PdfConfig.getParamAsBool(csc, "PUBLISHER", "BLOB_SU_DB").booleanValue();
	}
	
	/***********************************************************************************************/
	private static final String PDF_ETX = ".pdf";
	/***********************************************************************************************/
	public static class PDF_INSTANCE{

		private static String PDF_INSTANCE_TAB = "pdf_instance";
		private static String PDF_INSTANCE_BLOB = "pdf_content-";

		public static byte[] readPdfInstanceContent(ClientSessionContext csc, StringType pdfInstanceId) throws Exception{
			File file = pdfInstanceFile(csc, pdfInstanceId);
			return getFileContent(file);
		}
		
		public static void writePdfInstanceContent(ClientSessionContext csc, StringType pdfInstanceId, byte[] pdfContent) throws Exception{
			NasStorage.saveSimpleFile(csc, SHARE_NAME, PDF_INSTANCE_TAB+splittedPath(pdfInstanceId.toString()), PDF_INSTANCE_BLOB+pdfInstanceId+PDF_ETX, new ByteArrayInputStream(pdfContent));
		}
		
		public static void deletePdfInstanceContent(ClientSessionContext csc, StringType pdfInstanceId) throws Exception{
			File file = pdfInstanceFile(csc, pdfInstanceId);
			if(file.exists())
				file.delete();
		}

		public static File pdfInstanceFile(ClientSessionContext csc, StringType pdfInstanceId) throws Exception{
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_INSTANCE_TAB+splittedPath(pdfInstanceId.toString()), PDF_INSTANCE_BLOB+pdfInstanceId+PDF_ETX);
			if(!file.exists())
				file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_INSTANCE_TAB, PDF_INSTANCE_BLOB+pdfInstanceId+PDF_ETX);
			return file;
		}
		
		/***********************************************************************************************/
		/***********************************************************************************************/
		private static String splittedPath(String value) {
			int h = 0;
	        for(int i = 0; i < value.length(); i++) {
	            h = 31 * h + value.charAt(i);
	        }
	        return "_"+Math.abs(h % 100);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class PDF_PUBLICATION{
		
		private static String PDF_PUBLICATION_TAB = "pdf_publication";
		private static String PDF_PUBLICATION_BLOB = "content-";

		public static void loadPdfPublicationContent(ClientSessionContext csc, PdfAnagModel pdf) throws Exception{
			if(!pdf.getPdfContent().isNull())
				return;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_TAB, PDF_PUBLICATION_BLOB+pdf.getPdfId()+"-"+pdf.getPdfPublicationId()+PDF_ETX);
			pdf.getPdfContent().setFileContent(getFileContent(file));
		}
		 
		public static byte[] loadPdfPublicationContent(ClientSessionContext csc, StringType pdfId, IntegerType pdfPublicationId) throws Exception{
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_TAB, PDF_PUBLICATION_BLOB+pdfId+"-"+pdfPublicationId+PDF_ETX);
			return getFileContent(file);
		}
		
		public static boolean writePdfPublicationContent(ClientSessionContext csc, StringType pdfId, IntegerType pdfPublicationId, byte[] pdfContent) throws Exception{
			boolean blobSuDB = blobPublisherSuDB(csc);
			if(!blobSuDB)
				NasStorage.saveSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_TAB, PDF_PUBLICATION_BLOB+pdfId+"-"+pdfPublicationId+PDF_ETX, new ByteArrayInputStream(pdfContent));
			return blobSuDB;
		}
	
		public static boolean deletePdfPublicationContent(ClientSessionContext csc, StringType pdfId, IntegerType pdfPublicationId) throws Exception{
			boolean blobSuDB = blobPublisherSuDB(csc);
			if(blobSuDB)
				return blobSuDB;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_TAB, PDF_PUBLICATION_BLOB+pdfId+"-"+pdfPublicationId+PDF_ETX);
			if(file.exists())
				file.delete();
			return blobSuDB;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class PDF_PUBLICATION_WORK{
		
		private static String PDF_PUBLICATION_WORK_TAB = "pdf_publication_work";
		private static String PDF_PUBLICATION_WORK_BLOB = "content-";
		
		public static void loadPdfPublicationWorkContent(ClientSessionContext csc, PdfAnagModel pdf) throws Exception{
			if(!pdf.getPdfContent().isNull())
				return;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_WORK_TAB, PDF_PUBLICATION_WORK_BLOB+pdf.getPdfId()+PDF_ETX);
			pdf.getPdfContent().setFileContent(getFileContent(file));
		}
		
		public static byte[] loadPdfPublicationWorkContent(ClientSessionContext csc, StringType pdfId) throws Exception{
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_WORK_TAB, PDF_PUBLICATION_WORK_BLOB+pdfId+PDF_ETX);
			return getFileContent(file);
		}
	
		public static boolean writePdfPublicationWorkContent(ClientSessionContext csc, StringType pdfId, byte[] pdfContent) throws Exception{
			boolean blobSuDB = blobPublisherSuDB(csc);
			if(!blobSuDB)
				NasStorage.saveSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_WORK_TAB, PDF_PUBLICATION_WORK_BLOB+pdfId+PDF_ETX, new ByteArrayInputStream(pdfContent));
			return blobSuDB;
		}
	
		public static boolean deletePdfPublicationWorkContent(ClientSessionContext csc, StringType pdfId) throws Exception{
			boolean blobSuDB = blobPublisherSuDB(csc);
			if(blobSuDB)
				return blobSuDB;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PUBLICATION_WORK_TAB, PDF_PUBLICATION_WORK_BLOB+pdfId+PDF_ETX);
			if(file.exists())
				file.delete();
			return blobSuDB;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class PDF_PAGE{
		
		private static String PDF_PAGE_TAB = "pdf_page";
		private static String PDF_PAGE_BLOB = "page_img-";

		public static void loadPdfPageContent(ClientSessionContext csc, PdfPageAnagModel pdfPage) throws Exception{
			if(!pdfPage.getPdfPageImg().isNull())
				return;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_TAB, PDF_PAGE_BLOB+pdfPage.getPdfId()+"-"+pdfPage.getPdfPublicationId()+"-"+pdfPage.getPdfPageNum()+".jpeg");
			pdfPage.setPdfPageImg(new ByteArrayType(getFileContent(file)));
		}
		
		public static ByteArrayType loadPdfPageContent(ClientSessionContext csc, PdfPageIdxModel pdfPageIdx) throws Exception{
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_TAB, PDF_PAGE_BLOB+pdfPageIdx.getPdfId()+"-"+pdfPageIdx.getPdfPublicationId()+"-"+pdfPageIdx.getPageImgIdx()+".jpeg");
			return new ByteArrayType(getFileContent(file));
		}
		
		public static void writePdfPageContent(ClientSessionContext csc, PdfPageAnagModel pdfPage, boolean blobSuDB) throws Exception{
			if(blobSuDB)
				return;
			NasStorage.saveSimpleFile(csc, SHARE_NAME, PDF_PAGE_TAB, PDF_PAGE_BLOB+pdfPage.getPdfId()+"-"+pdfPage.getPdfPublicationId()+"-"+pdfPage.getPdfPageNum()+".jpeg", new ByteArrayInputStream(pdfPage.getPdfPageImg().byteArrayValue()));
			pdfPage.setPdfPageImg(new ByteArrayType());
			return;
		}
		
		public static void deletePdfPagesContent(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey, boolean blobSuDB) throws Exception{
			if(blobSuDB)
				return;
			for(int pdfPageNum=1;;pdfPageNum++){
				File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_TAB, PDF_PAGE_BLOB+pdfAnagKey.getPdfId()+"-"+pdfAnagKey.getPdfPublicationId()+"-"+pdfPageNum+".jpeg");
				if(file == null || !file.exists())
					break;
				file.delete();
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class PDF_PAGE_WORK{
		
		private static String PDF_PAGE_WORK_TAB = "pdf_page_work";
		private static String PDF_PAGE_WORK_BLOB = "page_img-";
		
		public static void loadPdfPageWorkContent(ClientSessionContext csc, PdfPageAnagModel pdfPage) throws Exception{
			if(!pdfPage.getPdfPageImg().isNull())
				return;
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_WORK_TAB, PDF_PAGE_WORK_BLOB+pdfPage.getPdfId()+"-"+pdfPage.getPdfPageNum()+".jpeg");
			pdfPage.setPdfPageImg(new ByteArrayType(getFileContent(file)));
		}
		
		public static ByteArrayType loadPdfPageWorkContent(ClientSessionContext csc, PdfPageIdxModel pdfPageIdx) throws Exception{
			File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_WORK_TAB, PDF_PAGE_WORK_BLOB+pdfPageIdx.getPdfId()+"-"+pdfPageIdx.getPageImgIdx()+".jpeg");
			return new ByteArrayType(getFileContent(file));
		}
		
		public static void writePdfPageWorkContent(ClientSessionContext csc, PdfPageAnagModel pdfPage, boolean blobSuDB) throws Exception{
			if(blobSuDB)
				return;
			NasStorage.saveSimpleFile(csc, SHARE_NAME, PDF_PAGE_WORK_TAB, PDF_PAGE_WORK_BLOB+pdfPage.getPdfId()+"-"+pdfPage.getPdfPageNum()+".jpeg", new ByteArrayInputStream(pdfPage.getPdfPageImg().byteArrayValue()));
			pdfPage.setPdfPageImg(new ByteArrayType());
			return;
		}
		
		public static void deletePdfPagesWorkContent(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey, boolean blobSuDB) throws Exception{
			if(blobSuDB)
				return;
			for(int pdfPageNum=1;;pdfPageNum++){
				File file = NasStorage.readSimpleFile(csc, SHARE_NAME, PDF_PAGE_WORK_TAB, PDF_PAGE_WORK_BLOB+pdfAnagKey.getPdfId()+"-"+pdfPageNum+".jpeg");
				if(file == null || !file.exists())
					break;
				file.delete();
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] getFileContent(File fileReference){
		if(!fileReference.exists())
			return null;
		InputStream fileInputStream = null;
		ByteArrayOutputStream fileOutputStream = null;
		try{
			fileInputStream = new FileInputStream(fileReference);
			fileOutputStream = new ByteArrayOutputStream();
	        byte[] buf = new byte[(16*1024)];
	        int charsRead;
	        while ((charsRead = fileInputStream.read(buf)) != -1) {
	        	fileOutputStream.write(buf, 0, charsRead);
	        	fileOutputStream.flush();
	        }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
	        try{ if(fileInputStream != null){fileInputStream.close();} }catch(Exception e){}
	        try{ if(fileOutputStream != null){fileOutputStream.close();} }catch(Exception e){}
		}
		if(fileOutputStream == null)
			return null;
		else
			return fileOutputStream.toByteArray();
	}
	
}
