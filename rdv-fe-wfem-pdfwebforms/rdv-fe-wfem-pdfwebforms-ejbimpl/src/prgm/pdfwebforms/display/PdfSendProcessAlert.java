package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfSendProcessAlert extends DisplayCommand {

	private static final String S_ALERT_MODULO_INVIATO_IN_SEDE = "Il modulo verrà inviato in sede e non sarà più modificabile.";
	private static final String S_ALERT_BASKET_INVIATO_IN_SEDE = "I moduli verranno inviati in sede e non saranno più modificabili.";
	private static final String S_EVITARE_RESPINTI_PREFIX1 = "Per evitare sospesi o respinti presta la massima attenzione alla corretta compilazione.<br>";
	private static final String S_EVITARE_RESPINTI_PREFIX2 = "Per evitare sospesi o respinti presta la massima attenzione alla corretta compilazione prima della stampa.<br>";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		if(!(dataModel instanceof PdfModel))
			return dataModel;
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfModel pdf = (PdfModel)dataModel;
			
			pdf.setListaCodiciOperazionePritPerMultioperazione(new ListType());
			
			String messaggioAlert = null;
			if((!pdf.getIsSede().booleanValue() && !pdf.getPdfData().getIsVolatile().booleanValue()) || pdf.isTestMode()){
				int numPdf = pdf.getPdfAnags().size();

				for(int i=0;i<pdf.getPdfAnags().size();i++){
					PdfAnagModel anag = pdf.getPdfAnags().get(i);
					MapCommandDataModel m = new MapCommandDataModel();
					m.addProperty("pdfCodProdottoPrit", anag.getPdfCodProdottoPrit());
					m.addProperty("pdfCodOperazionePrit", anag.getPdfCodOperazionePrit());
					m.addCodDescField("pdfCodOperazionePrit","OperazioniProdottoPrit");
					new DAOObject(csc,"PdfWebForms.PdfPublisher").fillCodDesc(m,false);
					pdf.getListaCodiciOperazionePritPerMultioperazione().add(m);
				}

				if(pdf.getPdfData().getPdfEnvironment().equals(PdfDataModel.ENVIRONMENT_CATALOGO_MODULI) || pdf.getPdfData().getPdfEnvironment().equals(PdfDataModel.ENVIRONMENT_CATALOGO_OPERAZIONI)){
					
					if(pdf.mainPdfAnag().getPdfCodProdottoPrit().isNull() || pdf.mainPdfAnag().getPdfCodOperazionePrit().isNull()){
						if(pdf.pdfIsInCartaChimica()){
							if(numPdf == 1){
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX1+
												 "Ricordati di inserire la riga di prit e spedire il modulo firmato.";
							}else{
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX1+
												 "Ricordati di inserire le righe di prit e spedire i moduli firmati.";
							}
						}else{
							if(numPdf == 1){
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX2+
												 "Ricordati di stampare il modulo, farlo firmare al cliente, inserire la riga di prit e spedirlo in sede.";
								
							}else{
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX2+
												 "Ricordati di stampare i moduli, farli firmare al cliente, inserire le righe di prit e spedirli in sede.";
							}
						}
					}else{
						if(pdf.pdfIsInCartaChimica()){
							if(numPdf == 1){
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX1+
												 "Ricordati di spedire il modulo firmato.";
							}else{
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX1+
												 "Ricordati di spedire i moduli firmati.";
							}
						}else{
							if(numPdf == 1){
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX2+ 
												 "Ricordati di stampare il modulo, farlo firmare al cliente e spedirlo in sede.";
							}else{
								messaggioAlert = S_EVITARE_RESPINTI_PREFIX2+ 
												 "Ricordati di stampare i moduli, farli firmare al cliente e spedirli in sede.";
							}
						}
					}
					
				}else{
					messaggioAlert = pdf.isInBasket() ? S_ALERT_BASKET_INVIATO_IN_SEDE : S_ALERT_MODULO_INVIATO_IN_SEDE;
				}
			}else{
				messaggioAlert = pdf.isInBasket() ? S_ALERT_BASKET_INVIATO_IN_SEDE : S_ALERT_MODULO_INVIATO_IN_SEDE;
			}
			pdf.setMessaggioPdfAlert(messaggioAlert);
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return CommandDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public boolean isNoSubmitCommand() {
		return true;
	}

}
