package prgm.pdfwebforms.publisher.backend;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfPageAnagModel;
import prgm.pdfwebforms.publisher.model.PdfRuoloUtilizzatoreModel;

@Stateless(name = "PdfAnagManager", mappedName = "PdfAnagManager")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
/********************************************************************************************************/
/********************************************************************************************************/
public class PdfAnagManagerBean extends ManagerObject implements PdfAnagManager{

	/********************************************************************************************************/
	/********************************************************************************************************/
	private void deleteRuoliUtilizatori(DAOObject dao, PdfAnagKeyModel pdfAnagKey) throws DAOException{
		try {
			dao.executeTableDeleteAccess("ruoloUtilizzatore", pdfAnagKey);
		}catch(NoRowsAffected nra) {
			// do nothing
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private void saveRuoliUtilizatori(DAOObject dao, PdfAnagModel pdf) throws DAOException{
		deleteRuoliUtilizatori(dao, pdf);
		for(int i=0;i<pdf.getPdfElencoRuoliUtilizzatori().size();i++) {
			PdfRuoloUtilizzatoreModel r = (PdfRuoloUtilizzatoreModel)pdf.getPdfElencoRuoliUtilizzatori().get(i);
			if(r.getIsSelezionato().booleanValue()) {
				r.setPdfId(pdf.getPdfId());
				dao.executeTableInsertAccess("ruoloUtilizzatore", r);
			}
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public PdfAnagModel savePdfConf(ClientSessionContext csc, PdfAnagModel pdf) throws EJBException {
		try{
			
			pdf.setPdfDriverName(new StringType(pdf.getPdfDriverName().toString().toLowerCase()));
			pdf.setPdfDriverVersion(new StringType(pdf.getPdfDriverVersion().toString().toLowerCase()));
			pdf.initFlagsFromInvioInSede();
			pdf.initNoteConfigurazione(csc);
			
			TimestampType now = Tools.now();
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			if(pdf.getPdfId().isNull()){
				dao.executeCallableAccess("getContatorePdfAnag",pdf);
				pdf.setPdfId(new StringType(Tools.fillSx(pdf.getPdfId().toString(),'0',20)));
			}
			try{
				pdf.setPdfLastModTime(now);
				pdf.setPdfLastModUser(new StringType(csc.getUserCode()));
				dao.executeTableUpdateAccess("pdfAnag",pdf);
			}catch(NoRowsAffected nra){
				pdf.setPdfCreationTime(now);
				pdf.setPdfCreationUser(new StringType(csc.getUserCode()));
				pdf.setPdfLastModTime(new TimestampType());
				pdf.setPdfLastModUser(new StringType());
				dao.executeTableInsertAccess("pdfAnag",pdf);
			}
			saveRuoliUtilizatori(dao, pdf);
			
			pdf.setPdfFileName(new StringType(pdf.getPdfContent().getFileName()));
			byte[] content = pdf.getPdfContent().getFileContent();
			boolean blobSuDB = PdfNasUtil.PDF_PUBLICATION_WORK.writePdfPublicationWorkContent(csc, pdf.getPdfId(), content);
			if(!blobSuDB)
				pdf.getPdfContent().setFileContent(null);
			try{
				dao.executeTableUpdateAccess("pdfPublication_WORK",pdf);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("pdfPublication_WORK",pdf);
			}
			if(!blobSuDB)
				dao.executeTableUpdateAccess("pdfPublication_WORK_setFileName",pdf);
			pdf.getPdfContent().setFileContent(content);
			
			if(pdf.getPagesToSave() != null){
				try{ dao.executeTableDeleteChildsAccess("pdfPage_WORK",pdf); }catch(NoRowsAffected nra){}
				PdfNasUtil.PDF_PAGE_WORK.deletePdfPagesWorkContent(csc, pdf, blobSuDB);
				for(int i=0;i<pdf.getPagesToSave().size();i++){
					PdfPageAnagModel page = (PdfPageAnagModel)pdf.getPagesToSave().get(i);
					if(page.getPdfId().isNull())
						page.setPdfId(new StringType(pdf.getPdfId().toString()));
					PdfNasUtil.PDF_PAGE_WORK.writePdfPageWorkContent(csc, page, blobSuDB);
					dao.executeTableInsertAccess("pdfPage_WORK",page);
				}
			}
			pdf.setPagesToSave(null);
			return pdf;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception saving pdf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception saving pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public PdfAnagModel publishPdfConf(ClientSessionContext csc, PdfAnagModel pdf) throws EJBException {
		try{
			
			pdf.setPdfDriverName(new StringType(pdf.getPdfDriverName().toString().toLowerCase()));
			pdf.setPdfDriverVersion(new StringType(pdf.getPdfDriverVersion().toString().toLowerCase()));
			pdf.initFlagsFromInvioInSede();
			pdf.initNoteConfigurazione(csc);
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableUpdateAccess("pdfAnag",pdf);
			saveRuoliUtilizatori(dao, pdf);

			pdf.setPdfFileName(new StringType(pdf.getPdfContent().getFileName()));
			byte[] content = pdf.getPdfContent().getFileContent();
			boolean blobSuDB = PdfNasUtil.PDF_PUBLICATION.writePdfPublicationContent(csc, pdf.getPdfId(), pdf.getPdfPublicationId(), content);
			if(!blobSuDB)
				pdf.getPdfContent().setFileContent(null);
			try{
				dao.executeTableUpdateAccess("pdfPublication",pdf);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("pdfPublication",pdf);
			}
			if(!blobSuDB)
				dao.executeTableUpdateAccess("pdfPublication_setFileName",pdf);
			pdf.getPdfContent().setFileContent(content);

			try{ dao.executeTableDeleteChildsAccess("pdfPage", pdf); }catch(NoRowsAffected nra){}
			PdfNasUtil.PDF_PAGE.deletePdfPagesContent(csc, pdf, blobSuDB);
			DAOTableResultModel tRes = dao.executeTableLoadChildsAccess("pdfPage_WORK",pdf, PdfPageAnagModel.class);
			for(int i=0;i<tRes.getChilds().size();i++){
				PdfPageAnagModel page = (PdfPageAnagModel)tRes.getChilds().get(i);
				PdfNasUtil.PDF_PAGE_WORK.loadPdfPageWorkContent(csc, page);
				page.setPdfPublicationId(new IntegerType(pdf.getPdfPublicationId().intValue()));
				PdfNasUtil.PDF_PAGE.writePdfPageContent(csc, page, blobSuDB);
				dao.executeTableInsertAccess("pdfPage",page);
			}
			
			deletePdfOnWork(csc,pdf);
			
			pdf.setPdfOriginalCode(new StringType(pdf.getPdfCode().toString()));
			return pdf;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception saving pdf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception saving pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public void deletePdfOnWork(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKeyModel) throws EJBException{
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			try{ dao.executeTableDeleteAccess("pdfPublication_WORK",pdfAnagKeyModel); }catch(NoRowsAffected nra){}
			boolean blobSuDB = PdfNasUtil.PDF_PUBLICATION_WORK.deletePdfPublicationWorkContent(csc, pdfAnagKeyModel.getPdfId());
			try{ dao.executeTableDeleteChildsAccess("pdfPage_WORK", pdfAnagKeyModel); }catch(NoRowsAffected nra){}
			PdfNasUtil.PDF_PAGE_WORK.deletePdfPagesWorkContent(csc, pdfAnagKeyModel, blobSuDB);
			return;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception deleting pdf on work: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf on work: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public void deletePdf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException{
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableDeleteAccess("pdfAnag",pdfAnagKey);
			deleteRuoliUtilizatori(dao, pdfAnagKey);
			deletePdfOnWork(csc, pdfAnagKey);
			return;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception deleting pdf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void editPdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException {
		try{
			
			PdfAnagModel pdf = new PdfAnagModel(pdfAnagKey);
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			
			dao.executeTableLoadAccess("pdfAnag",pdf);
			dao.executeTableLoadAccess("pdfPublication",pdf);
			PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdf);
			dao.executeQueryAccess("loadOtherPdfData",pdf);
			
			pdf.setPdfOriginalPubId(new IntegerType(pdf.getPdfPublicationId().intValue()));
			if(pdf.getPdfNumPubblicazioni().intValue() > 0 && pdf.isPdfPassato()){
				pdf.setPdfPublicationId(new IntegerType(PdfAnagModel.NO_PUBLICATION));
				pdf.setPdfPublicationNotes(new StringType());
			}
			
			try{ dao.executeTableDeleteAccess("pdfPublication_WORK", pdf); }catch(NoRowsAffected nra){}
			
			pdf.setPdfFileName(new StringType(pdf.getPdfContent().getFileName()));
			byte[] content = pdf.getPdfContent().getFileContent();
			boolean blobSuDB = PdfNasUtil.PDF_PUBLICATION_WORK.writePdfPublicationWorkContent(csc, pdf.getPdfId(), content);
			if(!blobSuDB)
				pdf.getPdfContent().setFileContent(null);
			dao.executeTableInsertAccess("pdfPublication_WORK",pdf);
			if(!blobSuDB)
				dao.executeTableUpdateAccess("pdfPublication_WORK_setFileName",pdf);
			pdf.getPdfContent().setFileContent(content);
			
			try{ dao.executeTableDeleteChildsAccess("pdfPage_WORK", pdf); }catch(NoRowsAffected nra){}
			PdfNasUtil.PDF_PAGE_WORK.deletePdfPagesWorkContent(csc, pdf, blobSuDB);
			for(int i=0;i<pdf.getPdfNumPages().intValue();i++){
				PdfPageAnagModel pdfPage = new PdfPageAnagModel();
				pdfPage.setPdfId(new StringType(pdf.getPdfId().toString()));
				pdfPage.setPdfCode(new StringType(pdf.getPdfCode().toString()));
				pdfPage.setPdfPublicationId(new IntegerType(pdf.getPdfOriginalPubId().intValue()));
				pdfPage.setPdfPageNum(new IntegerType(i+1));
				dao.executeTableLoadAccess("pdfPage",pdfPage);
				PdfNasUtil.PDF_PAGE.loadPdfPageContent(csc, pdfPage);
				
				pdfPage.setPdfPublicationId(new IntegerType(pdf.getPdfPublicationId().intValue()));
				PdfNasUtil.PDF_PAGE_WORK.writePdfPageWorkContent(csc, pdfPage, blobSuDB);
				dao.executeTableInsertAccess("pdfPage_WORK",pdfPage);
			}
			
			return;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception editing pdf conf: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception editing pdf conf: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public PdfAnagModel updatePdfAnag(ClientSessionContext csc, PdfAnagModel pdf) throws EJBException {
		try{
			
			pdf.setPdfDriverName(new StringType(pdf.getPdfDriverName().toString().toLowerCase()));
			pdf.setPdfDriverVersion(new StringType(pdf.getPdfDriverVersion().toString().toLowerCase()));
			pdf.initFlagsFromInvioInSede();
			pdf.initNoteConfigurazione(csc);
			
			TimestampType now = Tools.now();
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			pdf.setPdfLastModTime(now);
			pdf.setPdfLastModUser(new StringType(csc.getUserCode()));
			dao.executeTableUpdateAccess("pdfAnag",pdf);
			saveRuoliUtilizatori(dao, pdf);
			return pdf;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception updating pdf anag: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception updating pdf anag: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void deletePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException {
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableDeleteAccess("deletePdfPublication",pdfAnagKey);
			boolean blobSuDB = PdfNasUtil.PDF_PUBLICATION.deletePdfPublicationContent(csc, pdfAnagKey.getPdfId(), pdfAnagKey.getPdfPublicationId());
			try{ dao.executeTableDeleteChildsAccess("pdfPage", pdfAnagKey); }catch(NoRowsAffected nra){}
			PdfNasUtil.PDF_PAGE.deletePdfPagesContent(csc, pdfAnagKey, blobSuDB);
			return;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception deleting pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception deleting pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void archivePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException {
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableUpdateAccess("archivePdfPublication",pdfAnagKey);
			return;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception archiving pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception archiving pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void restorePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws EJBException {
		try{
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableUpdateAccess("restorePdfPublication",pdfAnagKey);
			return;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" DAO exception restoring pdf publication: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Exception restoring pdf publication: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
	}
	
}
