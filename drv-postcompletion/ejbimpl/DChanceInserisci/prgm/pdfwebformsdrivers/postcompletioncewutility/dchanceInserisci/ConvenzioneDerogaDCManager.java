package prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************************************/
/********************************************************************************************************/
public class ConvenzioneDerogaDCManager {
	
	private static final String DAO_XML = "PdfWebFormDriver.PostCompletionCewUtility.DChanceInserisci.ConvenzioneDerogaDC";
	
	private static final String IDAGEVOLAZIONEFIELD = "idAgevolazione";
	private static final String DATASOTTOSCRIZIONEFIELD = "dataSottoscrizione";
	private static final String CONVENZIONE_PRODOTTO = "prodotto";
	private static final String CONVENZIONE_TIPOLOGIA = "tipologia";
	private static final String CONVENZIONE_DURATA = "durata";
	private static final String TIPOOPZIONEPICDCFIELD = "tipoOpzionePicDc";

	private static final String PICDC = "PIC DC";
	private static final String DCOB = "DCOB";
	private static final String DCAZ = "DCAZ";
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private ConvenzioneDerogaDCManager() {
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static StringType getConvenzioneDerogaDC(ClientSessionContext csc, PdfModel pdf, StringType convenzioneOriginale) throws CommandException{
		try {
			if(pdf.isMultiPdf() && pdf.getPdfAnags().get(1).getPdfDriverName().equals("doublechance")) {
				PdfDataModel pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
				StringType idAgevolazione = (StringType)pdfData.read(IDAGEVOLAZIONEFIELD);
				if(idAgevolazione != null && !idAgevolazione.isNull()) {
															
					AgevolazioneModel agev = new PdfBaseDriver().readAgevolazione(csc, idAgevolazione.toString());
					if(agev == null)
						return convenzioneOriginale;
					
					if( !agev.getModalitaVersamentoAgevolazione().equals(PICDC) && 
						!agev.getCodProdottoDispositiva().toString().startsWith(DCOB) &&
						!agev.getCodProdottoDispositiva().toString().startsWith(DCAZ))
						return convenzioneOriginale;
					
					MapCommandDataModel m = new MapCommandDataModel();
					m.addProperty(DATASOTTOSCRIZIONEFIELD, pdfData.read(DATASOTTOSCRIZIONEFIELD));	
					m.addProperty(CONVENZIONE_PRODOTTO, getProdotto(agev));
					m.addProperty(CONVENZIONE_DURATA, getDurata(agev));	
					m.addProperty(CONVENZIONE_TIPOLOGIA, getTipologia(agev, pdfData));	
					StringType convenzioneDC = (StringType)new DAOObject(csc,DAO_XML).executeQueryAccess("getConvenzioneDerogaDoubleChance", m).getSingleResult();
					if(convenzioneDC != null && !convenzioneDC.isNull())
						return convenzioneDC;
					throw new CommandException("Nessuna convenzione configurata per l'agevolazione con id: "+idAgevolazione);
				}
			}
			return convenzioneOriginale;
		}catch(DAOException daoe) {
			throw new CommandException("Eccezione DAO in getConvenzioneDerogaDC: "+daoe.toString());
		}catch(Exception e) {
			throw new CommandException("Eccezione in getConvenzioneDerogaDC: "+e.toString());
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private static boolean isDerogaDCConMesi(AgevolazioneModel agev) {
		return !getDurata(agev).isNull();
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private static StringType getProdotto(AgevolazioneModel agev) {
		return isDerogaDCConMesi(agev) ? agev.getCodiceAgevolazione() : agev.getModalitaVersamentoAgevolazione();
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private static StringType getTipologia(AgevolazioneModel agev, PdfDataModel pdfData) {
		if(agev.getModalitaVersamentoAgevolazione().equals(PICDC) || isDerogaDCConMesi(agev)) {
			StringType tipoOpzionePicDc = (StringType)pdfData.read(TIPOOPZIONEPICDCFIELD);
			if(tipoOpzionePicDc.equals("AZIONARIO"))
				return new StringType("A");
			else
				return new StringType("B");
		}
		return new StringType();
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private static IntegerType getDurata(AgevolazioneModel agev) {
		try {
			return new IntegerType(Integer.parseInt(agev.getCodProdottoDispositiva().toString().substring(DCOB.length())));
		}catch(Exception e) {
			return new IntegerType();
		}
	}	
}
