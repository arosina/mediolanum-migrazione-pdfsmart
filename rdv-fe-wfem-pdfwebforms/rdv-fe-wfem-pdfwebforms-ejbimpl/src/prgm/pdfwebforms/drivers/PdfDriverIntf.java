package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.drivers.io.AfterSavedPdfInputData;
import prgm.pdfwebforms.drivers.io.AfterSavedPdfOutputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewInputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewOutputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventInputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventOutputData;
import prgm.pdfwebforms.drivers.io.FinalEventInputData;
import prgm.pdfwebforms.drivers.io.FinalEventOutputData;
import prgm.pdfwebforms.drivers.io.FreezeEventInputData;
import prgm.pdfwebforms.drivers.io.FreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.InitPdfInputData;
import prgm.pdfwebforms.drivers.io.InitPdfOutputData;
import prgm.pdfwebforms.drivers.io.LoadPdfInputData;
import prgm.pdfwebforms.drivers.io.LoadPdfOutputData;
import prgm.pdfwebforms.drivers.io.MomEventInputData;
import prgm.pdfwebforms.drivers.io.MomEventOutputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedInputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedOutputData;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesRequest;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesResponse;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataResponse;
import prgm.pdfwebforms.drivers.io.SendToCliEventInputData;
import prgm.pdfwebforms.drivers.io.SendToCliEventOutputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfInputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfOutputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventInputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfOutputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfOutputData;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataRequest;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataResponse;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentInputData;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentOutputData;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataRequest;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataResponse;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataRequest;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataRequest;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataRequest;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataRequest;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataResponse;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataRequest;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataResponse;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataRequest;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataResponse;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventInputData;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventOutputData;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfDriverIntf {
	public InitPdfOutputData 		onInitPdf(ClientSessionContext csc, InitPdfInputData input) throws Exception;
	public LoadPdfOutputData 		onLoadPdf(ClientSessionContext csc, LoadPdfInputData input) throws Exception;
	public SubmitPdfOutputData 		onSubmitPdf(ClientSessionContext csc, SubmitPdfInputData input) throws Exception;
	public AfterSavedPdfOutputData 	onAfterSavedPdf(ClientSessionContext csc, AfterSavedPdfInputData input) throws Exception;
	public VerifyPdfOutputData 		onVerifyPdf(ClientSessionContext csc, VerifyPdfInputData input) throws Exception;
	
	public PdfVerifiedOutputData	onPdfVerified(ClientSessionContext csc, PdfVerifiedInputData input) throws Exception;
	public BeforePreviewOutputData	onPdfPreview(ClientSessionContext csc, BeforePreviewInputData input) throws Exception;
	
	public CompilationModeSelectionEventOutputData	onCompilationModeSelection(ClientSessionContext csc, CompilationModeSelectionEventInputData input) throws Exception;
	public FinalEventOutputData						onPdfCompleted(ClientSessionContext csc, FinalEventInputData input) throws Exception;
	public SendToCliEventOutputData					onSendToCli(ClientSessionContext csc, SendToCliEventInputData input) throws Exception;
	public VerifyCopernicoPdfOutputData				onVerifyCopernicoPdf(ClientSessionContext csc, VerifyCopernicoPdfInputData input) throws Exception;
	public FreezeEventOutputData					onPdfFreeze(ClientSessionContext csc, FreezeEventInputData input) throws Exception;
	public UnfreezeEventOutputData					onPdfUnfreeze(ClientSessionContext csc, UnfreezeEventInputData input) throws Exception;
	
	public MomEventOutputData			onMomEvent(ClientSessionContext csc, MomEventInputData input) throws Exception;
	public ValidationEventOutputData	onValidationEvent(ClientSessionContext csc, ValidationEventInputData input) throws Exception;
	
	public ProvideAttachmentsDataResponse				provideAttachmentsData(ClientSessionContext csc, ProvideAttachmentsDataRequest input) throws Exception;
	public VerifyAttachmentOutputData					onVerifyAttachment(ClientSessionContext csc, VerifyAttachmentInputData input) throws Exception;

	public ProvideMifidDataResponse						provideMifidData(ClientSessionContext csc, ProvideMifidDataRequest input) throws Exception;
	public ProvideSostituzioniDataResponse 				provideSostituzioniData(ClientSessionContext csc, ProvideSostituzioniDataRequest input) throws Exception;
	
	public ProvideCrossFBCustomersDataResponse			provideCrossFBCustomersData(ClientSessionContext csc, ProvideCrossFBCustomersDataRequest input) throws Exception;

	/**************************************************************************************************
	 * @deprecated
	**************************************************************************************************/
	@Deprecated
	public ProvideCompilationModesResponse				provideCompilationModes(ClientSessionContext csc, ProvideCompilationModesRequest input) throws Exception;
	public ProvidePritDataResponse						providePritData(ClientSessionContext csc, ProvidePritDataRequest input) throws Exception;
	public ProvideReportAdeguatezzaDataResponse			provideReportAdeguatezzaData(ClientSessionContext csc, ProvideReportAdeguatezzaDataRequest input) throws Exception;
	public ProvideAgevolazioneDipendentiDataResponse	provideAgevolazioneDipendentiData(ClientSessionContext csc, ProvideAgevolazioneDipendentiDataRequest input);
	public ProvideSrvDispositivaDataResponse			provideSrvDispositivaData(ClientSessionContext csc, ProvideSrvDispositivaDataRequest input) throws Exception;
	public ProvideBasketDataResponse 					provideBasketData(ClientSessionContext csc, ProvideBasketDataRequest input) throws Exception;
	public ProvideSquadraDataResponse 					provideSquadraData(ClientSessionContext csc, ProvideSquadraDataRequest input) throws Exception;
	public ProvideSostituzioniDataResponse 				provideStockData(ClientSessionContext csc, ProvideSostituzioniDataRequest input) throws Exception;
	
}
