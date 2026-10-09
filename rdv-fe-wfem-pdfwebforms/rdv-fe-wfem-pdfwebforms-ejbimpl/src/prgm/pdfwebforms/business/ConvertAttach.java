package prgm.pdfwebforms.business;

import java.io.ByteArrayOutputStream;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.IntegerType;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentOutputData;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ConvertAttach extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PdfModel pdf = (PdfModel)dataModel;         

            IntegerType attachIdx = pdf.getAttachIdx();
            if(!attachIdx.isNull()){
            	
            	PdfAttachModel pdfAttach = (PdfAttachModel)pdf.getPdfAttachments().get(attachIdx.intValue());
               	pdfAttach.getFile().resetTypeErrors();
               	pdfAttach.setOriginalFile(new FileType());
               	
    			VerifyAttachmentOutputData vod = PdfDriverCaller.callVerifyAttachment(csc, pdf, pdfAttach);
    			if(vod != null && vod.getErrorMessage() != null){
    				pdfAttach.getFile().clear();
    				pdfAttach.getFile().addTypeError(vod.getErrorMessage());
    				setForwardDisplay(Integer.valueOf(0), false);
    				return pdf;
    			}
    			
    			FileType originalFile = pdfAttach.getFile();
                if(pdfAttach.getFile().getContentType().indexOf("application/pdf") < 0){
	            	try{
	            		FileType pdfFile = jpegToPdf(pdfAttach.getFile());
	            		pdfFile.setFileTypes(pdfAttach.getDriverAttachRef().getFileTypes());
	            		pdfAttach.setFile(pdfFile);
	            	}catch(Exception e){
	                	pdfAttach.getFile().clear();
	            		pdfAttach.getFile().addTypeError("Errore nella conversione in formato PDF");
	            	}
            	}else{
	            	try{
	            		new PdfReader(pdfAttach.getFile().getFileContent());
	            	}catch(Throwable t){
	                	pdfAttach.getFile().clear();
	            		pdfAttach.getFile().addTypeError("File PDF non valido, corrotto o protetto");
	            	}
            	}
                if(!pdfAttach.getFile().hasTypeErrors() && pdfAttach.getDriverAttachRef().isKeepOriginal())
                	pdfAttach.setOriginalFile(originalFile);
            }
            
            setForwardDisplay(Integer.valueOf(0), false);
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private FileType jpegToPdf(FileType jpeg) throws Exception{
		
		ByteArrayOutputStream mergedBAOS = new ByteArrayOutputStream();
		Document merged = new Document();
		PdfCopy mergedPdf = new PdfCopy(merged, mergedBAOS);
		merged.open();
		
		float margin = 35;
	    float xmax = PageSize.A4.getWidth() - (margin*2);
    	float ymax = PageSize.A4.getHeight() - (margin*2);
		
		Rectangle pageSize = PageSize.A4;
	    Document jpg2pdf = new Document(pageSize);
	    jpg2pdf.setMargins(35,35,45,45);
	    
	    ByteArrayOutputStream jpg2PdfBAOS = new ByteArrayOutputStream();
	    PdfWriter.getInstance(jpg2pdf, jpg2PdfBAOS);
	    
	    jpg2pdf.open();
	    
	    Image jpgImage = Image.getInstance(jpeg.getFileContent());
	    jpgImage.setAlignment(Image.ALIGN_MIDDLE);
	    
	    if (jpgImage.getWidth() > xmax || jpgImage.getHeight() > ymax) {
	    	jpgImage.scaleToFit(xmax,ymax);
	    }
	    
	    jpg2pdf.add(jpgImage);

	    jpg2pdf.close();
	    
	    mergedPdf.addPage(mergedPdf.getImportedPage(new PdfReader(jpg2PdfBAOS.toByteArray()),1));
	    merged.close();
	    
	    String fileName = jpeg.getFileName().substring(0, jpeg.getFileName().lastIndexOf("."));
	    FileType pdf = new FileType(mergedBAOS.toByteArray(), "application/pdf", fileName+".pdf");
        return pdf;		
		
	}

}
