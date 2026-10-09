package prgm.pdfwebforms.basket;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.FacadeLoader;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.aml.backend.AmlFacade;
import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.display.PdfInitialError;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.display.PdfPriipsPage;
import prgm.pdfwebforms.drivers.PdfBaseDriverUtil;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataResponse;
import prgm.pdfwebforms.drivers.model.SaldoContoModel;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;

/***********************************************************************************************/
/***********************************************************************************************/
public class Basket {
	
	private String idBasket = "";
	private boolean soloFirmaOlografa = false;
	
	private ArrayList<BasketElement> 	basketElements = new ArrayList<BasketElement>();
	private ArrayList<PdfPersonModel> 	personeGestite = new ArrayList<PdfPersonModel>();
	private PdfPersonModel 				personaCorrente;
	private TimestampType 				dataOraUltimaModifica = new TimestampType();
	private StringType 					codUtenteUltimaModifica = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Basket(String idBasket) {
		this.idBasket = idBasket;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String verificaSaldi(ClientSessionContext csc, PdfModel currentPdf) throws Exception{
		
		Basket basket = currentPdf.getBasket();
		
		String msgNotFound = MifidCaller.WARNING_INDICATOR+"Non è stato possibile determinare il saldo del conto selezionato per il pagamento. E' possibile proseguire unicamente in firma olografa.";
		String msgRadar = MifidCaller.WARNING_INDICATOR+"Il conto selezionato per il pagamento non dispone della liquidità necessaria. Se viene selezionata la firma digitale l'addebito del conto sarà ritentato giornalmente nei prossimi 7 giorni di calendario. Al settimo giorno, in assenza di capienza sufficiente, l'operazione di investimento sarà respinta. In alternativa è possibile proseguire in firma olografa.";
		String msgRadarNotFound = MifidCaller.WARNING_INDICATOR+"Non è stato possibile determinare il saldo del conto selezionato per il pagamento. Se viene selezionata la firma digitale l'addebito del conto sarà ritentato giornalmente nei prossimi 7 giorni di calendario. Al settimo giorno, in assenza di capienza sufficiente, l'operazione di investimento sarà respinta. In alternativa è possibile proseguire in firma olografa.";
		String msgSaldo = MifidCaller.WARNING_INDICATOR+"Il conto selezionato per il pagamento non dispone della liquidità necessaria. E' possibile proseguire ma la sottoscrizione tramite Firma Digitale e Copernico non è consentita.";
		
		if(currentPdf.isInAccettazioneCopernico()) {
			msgNotFound = "Non è momentaneamente possibile verificare il saldo del conto indicato. Ti preghiamo di riprovare più tardi.";
			msgRadar = MifidCaller.WARNING_INDICATOR+"Il conto indicato non dispone della liquidità necessaria. Verrà ritentato l'addebito nei prossimi 7 giorni di calendario al termine dei quali, se non vi sarà saldo capiente, l'operazione verrà respinta.";
			msgRadarNotFound = MifidCaller.WARNING_INDICATOR+"Non è momentaneamente possibile verificare il saldo del conto indicato.E' possibile comunque procedere, in quanto verrà ritentato l'addebito nei prossimi 7 giorni di calendario.";
			msgSaldo = "Il conto indicato non dispone nella liquidità necessaria. Per maggiori informazioni rivolgiti al tuo Family Banker.";
		}else{ // Da rete se sia FD che COPERNICO non sono disponibili il messaggio radar non lo mostriamo (rfc #143463: mail Bertuzzi del 23-11-2020 10:01 e mail Geusa 24-11-2020 18:56)
			if( (!currentPdf.isFirmaDigitaleAccessibile() || globalBasketPdfCompilationModes(currentPdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) < 0) &&
				(!currentPdf.isCopernicoAccessibile() || globalBasketPdfCompilationModes(currentPdf).toString().indexOf(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) < 0)) {
				msgRadar = null;
				msgRadarNotFound = null;
			}
		}
		
		int numDispoConControllo = 0;
		ArrayList<BasketContoInfo> contiList = new ArrayList<BasketContoInfo>();
		basket.setSoloFirmaOlografa(false);
		for(BasketElement be : basket.getBasketElements()) {
			ProvideBasketDataResponse dispoBasketData = PdfDriverCaller.callProvideBasketData(csc, be.getDispoPdf());
			if( dispoBasketData != null && 
			   !dispoBasketData.getTipoControlloSaldo().equals(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_NESSUNO) &&
			   !dispoBasketData.getNumeroContoControlloSaldo().isNull() && !dispoBasketData.getImportoControlloSaldo().isNull()) {
				mergeContiVerificaSaldi(contiList, dispoBasketData);
				numDispoConControllo++;
			}
		}
		if(contiList.isEmpty() || numDispoConControllo < 2) // Nessun conto da controllare oppure una sola dispo per cui l'ha già fatto lei (o nessuna)
			return null;
		
		int numRadarNotFound = 0;
		int numRadarNonCapienti = 0;
		for(BasketContoInfo conto : contiList) {
			
			SaldoContoModel saldo = new PdfBaseDriverUtil().readSaldoConto(csc, new StringType(conto.getNumeroContoControlloSaldo()));
			if(saldo.isNotFound()){
				if(conto.getImportoNecessario().doubleValue() > 0) {
					basket.setSoloFirmaOlografa(true);
					return msgNotFound;
				}
				numRadarNotFound++;
				continue;
			}
			
			double saldoConto = saldo.getSaldoDisponibile().doubleValue();
			// Se il saldo del conto non copre le dispositive che vogliono la disponibilità -> "vince" messaggio senza fd/cop
			if(saldoConto < conto.getImportoNecessario().doubleValue()) {
				basket.setSoloFirmaOlografa(true);
				return msgSaldo;
			}
			
			// Se il saldo del conto non copre le dispositive che vogliono la disponibilità radar
			if(saldoConto < conto.getImportoNecessario().doubleValue()+conto.getImportoRadar().doubleValue()) {
				numRadarNonCapienti++;
			}
		}
		if(numRadarNonCapienti > 0)
			return msgRadar;
		if(numRadarNotFound > 0)
			return msgRadarNotFound;
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void mergeContiVerificaSaldi(ArrayList<BasketContoInfo> contiList, ProvideBasketDataResponse dispoBasketData) throws Exception {
		BasketContoInfo foundedConto = null;
		for(BasketContoInfo bci : contiList) {
			if(bci.getNumeroContoControlloSaldo().equals(dispoBasketData.getNumeroContoControlloSaldo().toString())) {
				foundedConto = bci;
				break;
			}
		}
		if(foundedConto == null) {
			foundedConto = new BasketContoInfo();
			foundedConto.setNumeroContoControlloSaldo(dispoBasketData.getNumeroContoControlloSaldo().toString());
			if(dispoBasketData.getTipoControlloSaldo().equals(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_DISPONIBILE))
				foundedConto.setImportoNecessario(new DoubleType(dispoBasketData.getImportoControlloSaldo()));
			else if(dispoBasketData.getTipoControlloSaldo().equals(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_RADAR))
				foundedConto.setImportoRadar(new DoubleType(dispoBasketData.getImportoControlloSaldo()));
			contiList.add(foundedConto);
			return;
		}
		
		if(dispoBasketData.getTipoControlloSaldo().equals(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_DISPONIBILE))
			foundedConto.setImportoNecessario(foundedConto.getImportoNecessario().add(dispoBasketData.getImportoControlloSaldo()));
		else if(dispoBasketData.getTipoControlloSaldo().equals(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_RADAR))
			foundedConto.setImportoRadar(foundedConto.getImportoRadar().add(dispoBasketData.getImportoControlloSaldo()));
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String noteCopernico(ClientSessionContext csc, PdfModel currentPdf) throws Exception{
		Basket basket = currentPdf.getBasket();
		int numRimb = 0;
		int numRisc = 0;
		int numDispo = basket.getBasketElements().size();
		for(BasketElement be : basket.getBasketElements()) {
			ProvideBasketDataResponse dispoBasketData = PdfDriverCaller.callProvideBasketData(csc, be.getDispoPdf());
			if(dispoBasketData != null) { 
				if(dispoBasketData.getTipoOperazione().equals(ProvideBasketDataResponse.TIPO_OPERAZIONE_RIMBORSO))
					numRimb++;
				if(dispoBasketData.getTipoOperazione().equals(ProvideBasketDataResponse.TIPO_OPERAZIONE_RISCATTO))
					numRisc++;
			}
		}

		String il_i= "il";
		String o_i = "o";
		String a_e = "a";
		String e_i = "e";
		if(numDispo > 1) {
			il_i= "i";
			o_i = "i";
			a_e = "e";
			e_i = "i";
		}
		
		String rimbRisc = "rimborso";
		if(numRimb > 0 && numRisc > 0)
			rimbRisc = "rimborso e riscatto";
		else if(numRimb == 0 && numRisc > 0)
			rimbRisc = "riscatto";
		
		int numRimbRisc = numRimb+numRisc;
		if(numRimbRisc > 0 && numRimbRisc == numDispo)
			return "Come da intese, invio "+il_i+" modul"+o_i+", in formato elettronico, per il perfezionamento dell"+a_e+" disposizion"+e_i+" di "+rimbRisc;
		else if(numRimbRisc > 0) {
			return "Come da intese, invio "+il_i+" modul"+o_i+", in formato elettronico, per il perfezionamento dell"+a_e+" disposizion"+e_i+" di "+rimbRisc.replaceAll(" e ", ", ")+" e delle altre operazioni concordate";
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel firstDataEntryDispoPdf(UserSessionContext userSessionContext, BusinessCommand cmd, Basket inputBasketData) throws Exception {
		for(int i=0; i<inputBasketData.getBasketElements().size();i++) {
			PdfModel pdf = initNewBasketElement(userSessionContext, cmd, inputBasketData, i);
			if((Class)cmd.getNextCommandObject() != PdfPage.class && 
			   (Class)cmd.getNextCommandObject() != PdfPriipsPage.class) {
				return pdf;
			}
		}
		BasketElement firstEl = inputBasketData.getBasketElements().get(0);
		cmd.setNextCommandClass((Class)firstEl.getCmd().getNextCommandObject());
		return firstEl.getDispoPdf();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel nextDataEntryDispoPdf(UserSessionContext userSessionContext, BusinessCommand cmd, PdfModel currentPdf) throws Exception {
		
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return null;
		
		int i=0;
		for(;i<basket.getBasketElements().size();i++){
			BasketElement be = basket.getBasketElements().get(i);
			if(be.getDispoPdf() == currentPdf)
				break;
		}
		if(i == basket.getBasketElements().size()-1)
			return null;
		
		BasketElement basketElement = basket.getBasketElements().get(i+1);
		if(basketElement.getDispoPdf() != null){
			if(basketElement.getDispoPdf().mainPdfAnag().getHasPriips().booleanValue())
				cmd.setNextCommandClass(PdfPriipsPage.class);
			else
				cmd.setNextCommandClass(PdfPage.class);
			return basketElement.getDispoPdf();
		}
		
		return initNewBasketElement(userSessionContext, cmd, basket, i+1);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel prevDataEntryDispoPdf(PdfModel currentPdf) {
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return currentPdf;
		
		int i=0;
		for(;i<basket.getBasketElements().size();i++){
			BasketElement be = basket.getBasketElements().get(i);
			if(be.getDispoPdf() == currentPdf)
				break;
		}
		if(i == 0)
			return currentPdf;
		
		BasketElement basketElement = basket.getBasketElements().get(i-1);
		return basketElement.getDispoPdf();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel lastDataEntryDispoPdf(PdfModel currentPdf) {
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return currentPdf;
		
		return basket.getBasketElements().get(basket.getBasketElements().size()-1).getDispoPdf();		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel getFirstDispoPdf(PdfModel currentPdf){
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return currentPdf;
		return basket.getBasketElements().get(0).getDispoPdf();
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel getNextDispoPdf(PdfModel currentPdf){
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return null;
		int i=0;
		for(;i<basket.getBasketElements().size();i++) {
			BasketElement be = basket.getBasketElements().get(i);
			if(be.getDispoPdf() == currentPdf)
				break;
		}
		if(i == basket.getBasketElements().size()-1)
			return null;
		return basket.getBasketElements().get(i+1).getDispoPdf();
	}	
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static PdfModel initNewBasketElement(UserSessionContext userSessionContext, BusinessCommand cmd, Basket basket, int index) throws Exception {
		BasketElement basketElement = basket.getBasketElements().get(index);
		
		PdfDataModel pdfData = basketElement.createPdfDataModel();
		BusinessCommand pdfcmd = basketElement.getCmd();
		PdfModel pdf = (PdfModel)pdfcmd.execute(userSessionContext, pdfData);
		basketElement.setDispoPdf(pdf);
		pdf.setBasket(basket);
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
        ((AmlFacade)FacadeLoader.getFacade(csc, AmlFacade.class)).removePdfAmlHiddenModules(csc, pdf);

		cmd.setNextCommandClass((Class)pdfcmd.getNextCommandObject());
		return pdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isFirstDispoPdf(PdfModel currentPdf) {
		if(!currentPdf.isInBasket())
			return true;
		return currentPdf.getBasket().getBasketElements().get(0).getDispoPdf() == currentPdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isLastDispoPdf(PdfModel currentPdf) {
		if(!currentPdf.isInBasket())
			return true;
		int size = currentPdf.getBasket().getBasketElements().size();
		return currentPdf.getBasket().getBasketElements().get(size-1).getDispoPdf() == currentPdf;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean isLastDispoDaFirmarePersonaCorrente(PdfModel currentPdf){
		if(!currentPdf.isInBasket())
			return true;
		PdfModel pdf = currentPdf;
		for(;;) {
			pdf = Basket.getNextDispoPdf(pdf);
			if(pdf == null)
				return true;
			
			Basket basket = pdf.getBasket();
			for(PdfPersonModel p : pdf.getFullProcessPersons()){
				if(basket.getPersonaCorrente().isEqual(p)) {
					
					for(int i=0;i<p.getPdfPersonsSignProcessInfos().length;i++){
	    				PdfPersonSignProcessInfo pi = p.getPdfPersonsSignProcessInfos()[i];
						if(pi.isHasSignOnPdf()){
							return false;
						}
					}
					
				}
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel findNextDispoToSignCurrentPerson(ClientSessionContext csc, PdfInstanceFacade facade, PdfModel currentPdf) throws Exception{
		PdfModel pdf = currentPdf;
		for(;;) {
			pdf = Basket.getNextDispoPdf(pdf);
			if(pdf == null)
				return null;
			
			Basket basket = pdf.getBasket();
			for(PdfPersonModel p : pdf.getFullProcessPersons()){
				if(basket.getPersonaCorrente().isEqual(p)) {
					
					for(int i=0;i<p.getPdfPersonsSignProcessInfos().length;i++){
	    				PdfPersonSignProcessInfo pi = p.getPdfPersonsSignProcessInfos()[i];
						if(pi.isHasSignOnPdf()){
							p.setSignData(basket.getPersonaCorrente().getSignData());
		   					basket.setPersonaCorrente(p);
							basket.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
		   					pdf = facade.gotoPdf(csc, pdf, i, false);
		   					basket.getPersonaCorrente().getSignData().setStepPinSuperato(true);
							pdf.setPersonaCorrente(basket.getPersonaCorrente());
							return pdf;
						}
					}
					
				}
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static PdfModel findFirstDispoToSignNextPerson(ClientSessionContext csc, PdfInstanceFacade facade, PdfModel currentPdf, boolean findAge) throws Exception{
		Basket basket = currentPdf.getBasket();
		for(BasketElement be : basket.getBasketElements()) {
			PdfModel pdf = be.getDispoPdf();
			for(PdfPersonModel p : pdf.getFullProcessPersons()){
				
				if((findAge && !p.isAgente()) || (!findAge && p.isAgente()))
					continue;

				boolean found = false;
				for(PdfPersonModel bp : basket.getPersoneGestite()) {
					if(p.isEqual(bp)) {
						pdf.setScrollXValue(new IntegerType(0));
						pdf.setScrollYValue(new IntegerType(0));
						found = true;
						break;
					}
				}
				if(!found) {
					for(int i=0;i<p.getPdfPersonsSignProcessInfos().length;i++){
						PdfPersonSignProcessInfo pi = p.getPdfPersonsSignProcessInfos()[i];
						if(pi.isHasSignOnPdf()){
							basket.setPersonaCorrente(p);
							basket.getPersonaCorrente().setIndexInFieldName(pi.getIndexInFieldName());
							pdf = facade.gotoPdf(csc, pdf, i, false);
							pdf.setPersonaCorrente(basket.getPersonaCorrente());
							return pdf;
						}
					}
				}
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getElencoIdDispoOK(PdfModel currentPdf) {
		Basket basket = currentPdf.getBasket();
		if(basket == null)
			return currentPdf.getPdfData().getPdfInstanceId().toString();
		
		StringBuilder res = new StringBuilder();
		for(BasketElement be : basket.getBasketElements()) {
			if(be.hasFreezeError())
				continue;
			res.append(be.getDispoPdf().getPdfData().getPdfInstanceId()+",");
		}
		if(res.length() > 0)
			res.deleteCharAt(res.length()-1);
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType globalBasketPdfCompilationModes(PdfModel pdf) {
		if(!pdf.isInBasket())
			return pdf.globalPdfCompilationModes();
		
		String valid = "";
		String[] all = new String[]{
				PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO,
				PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE,
				PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA,
				PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA,
				PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA};
		Basket basket = pdf.getBasket();
		for(int i=0;i<all.length;i++){
			boolean isValid = true;
			for(BasketElement be : basket.getBasketElements()){
				if(be.getDispoPdf().globalPdfCompilationModes().toString().indexOf(all[i]) < 0){
					isValid = false;
					break;
				}
			}
			if(isValid && valid.indexOf(all[i]) < 0)
				valid += all[i]+",";
		}
		if(valid.length() > 0)
			valid = valid.substring(0, valid.length()-1);
		return new StringType(valid);		
	}
		
	/*******************************************************************/
	/*******************************************************************/
	public static PdfModel clone(PdfModel pdf) throws CloneNotSupportedException {
		if(!pdf.isInBasket())
			return (PdfModel)Tools.cloneObject(pdf);
		Basket inBasket = pdf.getBasket();
		Basket cloneBasket = new Basket(inBasket.getIdBasket());
		for(BasketElement be : inBasket.getBasketElements()) {
			BasketElement cloneBe = new BasketElement();
			PdfModel cloneDispoPdf = (PdfModel)Tools.cloneObject(be.getDispoPdf());
			cloneDispoPdf.setBasket(cloneBasket);
			cloneBe.setDispoPdf(cloneDispoPdf);
			cloneBasket.getBasketElements().add(cloneBe);
		}
		return cloneBasket.getBasketElements().get(0).getDispoPdf();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean verifyMvcConsistency(BusinessCommand cmd, PdfModel pdf) {
		boolean isMvcConsistent = true;
        if( (!pdf.getModelHashCode().isNull() && pdf.getModelHashCode().intValue() != pdf.hashCode()) || 
            (!pdf.getDataHashCode().isNull()  && pdf.getDataHashCode().intValue()  != pdf.getPdfData().getPdfIndex().intValue())) {
			pdf.setInitialErrorMsg("Attenzione! "+
					"Premendo il tasto indietro del browser hai interrotto la compilazione del contratto e alcuni dati potrebbero non essere stati salvati correttamente.<br>"+
					"Torna alla bozza per riavviare il processo di compilazione");
 			cmd.setNextCommandClass(PdfInitialError.class);
 			isMvcConsistent = false;
        }
        pdf.setModelHashCode(new IntegerType());
        pdf.setDataHashCode(new IntegerType());
		return isMvcConsistent;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static List<PdfModel> dispoToSign(PdfModel pdf, PdfPersonModel personaCorrente) {
		ArrayList<PdfModel> res = new ArrayList<PdfModel>();
		if(!pdf.isInBasket()) {
			res.add(pdf);
		}else{
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				for(PdfPersonModel p : pdf.getFullProcessPersons()){
					if(personaCorrente.isEqual(p) && p.isHasSignInSomePdf()) {
						res.add(pdf);
						break;
					}
				}
			}
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ArrayList<BasketElement> getBasketElements() {
		return basketElements;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static TimestampType getDataOraUltimaModifica(PdfModel currentPdf) throws Exception{
		return currentPdf.getBasket() == null ? currentPdf.getDataOraUltimaModifica() :  currentPdf.getBasket().getDataOraUltimaModifica();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static StringType getCodUtenteUltimaModifica(PdfModel currentPdf) throws Exception{
		return currentPdf.getBasket() == null ? currentPdf.getCodUtenteUltimaModifica() : currentPdf.getBasket().getCodUtenteUltimaModifica();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean hasLayerCollocamentoADistanza(PdfModel pdf) throws Exception{
		if(!pdf.isInBasket()) {
			return hasDispoPdfLayerCollocamentoADistanza(pdf);
		}else{
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				if(hasDispoPdfLayerCollocamentoADistanza(pdf))
					return true;
			}
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean hasDispoPdfLayerCollocamentoADistanza(PdfModel pdf) throws Exception{
		if(pdf.getPdfAnags() == null)
			return false;
		for(PdfAnagModel pdfAnag : pdf.getPdfAnags()) {
			if(pdfAnag.getPdfHasLayerCollocamentoADistanza().booleanValue())
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static AbstractType getTipoDistanzaCollocamento(PdfModel pdf) throws Exception{
		if(!pdf.isInBasket()) {
			return getTipoDistanzaCollocamentoInDispo(pdf);
		}else{
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				pdf = be.getDispoPdf();
				AbstractType tipoDistanzaCollocamentoInDispo = getTipoDistanzaCollocamentoInDispo(pdf);
				if(tipoDistanzaCollocamentoInDispo != null)
					return tipoDistanzaCollocamentoInDispo;
			}
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static AbstractType getTipoDistanzaCollocamentoInDispo(PdfModel pdf) throws Exception{
		if(!pdf.isMultiPdf())
			return pdf.getPdfData().read(PdfPredefinedFields.TIPO_DISTANZA_COLLOCAMENTO);
		for(int i=0;i<pdf.getPdfData().getPdfs().size();i++) {
			PdfDataModel pdfDataEl = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			AbstractType tipoDistanzaCollocamentoInDispo = pdfDataEl.read(PdfPredefinedFields.TIPO_DISTANZA_COLLOCAMENTO);
			if(tipoDistanzaCollocamentoInDispo != null)
				return tipoDistanzaCollocamentoInDispo;
		}
		return null;
	}
	
	public String getIdBasket() {
		return idBasket;
	}

	public PdfPersonModel getPersonaCorrente() {
		return personaCorrente;
	}

	public void setPersonaCorrente(PdfPersonModel personaCorrente) {
		this.personaCorrente = personaCorrente;
	}

	public ArrayList<PdfPersonModel> getPersoneGestite() {
		return personeGestite;
	}

	public void setPersoneGestite(ArrayList<PdfPersonModel> personeGestite) {
		this.personeGestite = personeGestite;
	}

	public boolean isSoloFirmaOlografa() {
		return soloFirmaOlografa;
	}

	public void setSoloFirmaOlografa(boolean soloFirmaOlografa) {
		this.soloFirmaOlografa = soloFirmaOlografa;
	}

	public TimestampType getDataOraUltimaModifica() {
		return dataOraUltimaModifica;
	}

	public void setDataOraUltimaModifica(TimestampType dataOraUltimaModifica) {
		this.dataOraUltimaModifica = dataOraUltimaModifica;
	}

	public StringType getCodUtenteUltimaModifica() {
		return codUtenteUltimaModifica;
	}

	public void setCodUtenteUltimaModifica(StringType codUtenteUltimaModifica) {
		this.codUtenteUltimaModifica = codUtenteUltimaModifica;
	}

}
