package prgm.pdfwebforms.verifiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.squadra.ElementoSquadra;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SchedaScaiVerifier {
	
	private static final String DAO_XML =  "PdfWebForms.PdfVerifiers";
	private static final String CODICE_STATO_SCHEDA_SCAI_FIELD = "codiceStatoSchedaSCAI";

	private Map<String, StringType> schedeClientiElaborate = new HashMap<String, StringType>();
	
	/***********************************************************************************************/
	//	STATO_SCHEDA_SCAI_COMPLETO 		= "1"
	private final static String STATO_SCHEDA_SCAI_INCOMPLETO 	= "2";
	//	STATO_SCHEDA_SCAI_IN_SCADENZA 	= "3"
	private final static String STATO_SCHEDA_SCAI_SCADUTO 		= "4";
	private final static String STATO_SCHEDA_SCAI_ESTINTO 		= "5";
	private final static String STATO_SCHEDA_SCAI_BLOCCATO 		= "6";
	
	private final static String FASE_COMMERCIALE_VENDITA		= "01";
	private final static String FASE_COMMERCIALE_POSTVENDITA	= "02";
	
	private final static String MSG_ERR_TEC_SCAI = "Si sono verificati problemi tecnici nel recuperare lo stato della scheda SCAI";
	private final static String MSG_ERR_NO_SCAI = "La scheda SCAI del cliente non risulta compilata, non è possibile procedere";
	private final static String MSG_ERR_SCAI = "La scheda SCAI del cliente non è aggiornata. Per garantire l'operatività occorre aggiornare l'anagrafica";
	private final static String MSG_ERR_SCAI_COPERNICO = "E' necessario aggiornare i dati anagrafici per proseguire con l'operazione.";
	private final static String MSG_ERR_SERVIZIO_SCAI = "Errore di comunicazione con il servizio di lettura dello stato scheda SCAI";
	/***********************************************************************************************/
	public String controllaSchedeSCAISquadra(ClientSessionContext csc, PdfModel pdf) {
		
		schedeClientiElaborate.clear();
		
		if(!pdf.getPdfAnag().getHasControlloSCAI().booleanValue())
			return null;
		
		DAOObject dao = new DAOObject(csc, DAO_XML);
		boolean someInError = false;
		try {
			PdfDataModel pdfData = pdf.getPdfData();
			List<ElementoSquadra> squadra = PdfDriverCaller.callProvideSquadraData(csc, pdf,
																					new String[] {	ElementoSquadra.Ruoli.SOTTOSCRITTORE,
																									ElementoSquadra.Ruoli.CONTRAENTE,
																									ElementoSquadra.Ruoli.COSOTTOSCRITTORE,
																									ElementoSquadra.Ruoli.LEGALERAPPRESENTANTE });
			for(ElementoSquadra elSq : squadra) {
				StringType codCli = (StringType)pdfData.read(elSq.getNomeCampoNdg());
				if(elSq.getNomiCampoErrore().length() == 0)
					elSq.setNomiCampoErrore(elSq.getNomeCampoNdg());
				if(!controllaSchedaSCAICliente(dao, new StringType(Tools.fillSx(codCli.toString(), '0', 11)), 
											   pdf, new ArrayList<String>(Arrays.asList(elSq.getNomiCampoErrore().split("\\&")))))
					someInError = true;
			}
		}catch(Exception e) {
			pdf.addCommandError(e.getMessage());
			return MSG_ERR_TEC_SCAI;
		}
		// Progetto AML (rfc #170232): hanno deciso solo succesivamente di non fare il controllo su copernico
		// Ormai il metodo era implementato per gestirlo in "StartAccettaPropostaCopernicoProcess" tramite la stringa in output: per ora si lascia così
		return someInError ? MSG_ERR_SCAI_COPERNICO : null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean controllaSchedaSCAICliente(DAOObject dao, StringType codiceCliente,  PdfModel pdf, List<String> putErrorOn){
		
		if(codiceCliente.isNull() || codiceCliente.toString().startsWith("S"))
			return true;
				
		PdfDataModel pdfData = pdf.getPdfData();
		PdfAnagModel pdfAnag = pdf.getPdfAnag();

		try {
			
			MapCommandDataModel inOut = new MapCommandDataModel();
			inOut.addProperty("codiceCliente", codiceCliente);
			BooleanType isPersonaFisica = (BooleanType)dao.executeQueryAccess("isPersonaFisica",inOut).getSingleResult();
			if(isPersonaFisica == null || !isPersonaFisica.booleanValue())
				return true;
			
			StringType codiceStatoSchedaSCAI = schedeClientiElaborate.get(codiceCliente.toString());
			if(codiceStatoSchedaSCAI == null) {
				inOut = new MapCommandDataModel();
				inOut.addProperty("codiceCliente", codiceCliente);
				DAOOSBResultModel wsRes = dao.executeOSBAccess("loadSchedaScaiCliente",inOut);
				if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){
					if(putErrorOn != null)
						PdfUtil.putError(pdfData, putErrorOn, MSG_ERR_SERVIZIO_SCAI);
					else
						codiceCliente.addTypeError(MSG_ERR_SERVIZIO_SCAI);
					return false;
				}
				codiceStatoSchedaSCAI = inOut.readProperty(CODICE_STATO_SCHEDA_SCAI_FIELD) == null ? new StringType() : (StringType)inOut.readProperty(CODICE_STATO_SCHEDA_SCAI_FIELD);
				schedeClientiElaborate.put(codiceCliente.toString(), codiceStatoSchedaSCAI);
			}			
			
			boolean blocca = false;
			String err = MSG_ERR_SCAI;
			if(codiceStatoSchedaSCAI.isNull() && 
				(pdfAnag.getPdfCodFaseCommerciale().equals(FASE_COMMERCIALE_VENDITA) || pdfAnag.getPdfCodFaseCommerciale().equals(FASE_COMMERCIALE_POSTVENDITA))) {
				err = MSG_ERR_NO_SCAI;
				blocca = true;
			}else {
				if(pdfAnag.getPdfCodFaseCommerciale().equals(FASE_COMMERCIALE_VENDITA)) { 			// Vendita
					blocca = codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_SCADUTO) 	|| 
							 codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_BLOCCATO) 	|| 
							 codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_INCOMPLETO) || 
							 codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_ESTINTO);
				}else if(pdfAnag.getPdfCodFaseCommerciale().equals(FASE_COMMERCIALE_POSTVENDITA)) { // Post-vendita
					blocca = codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_BLOCCATO) 	|| 
							 codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_INCOMPLETO) || 
							 codiceStatoSchedaSCAI.equals(STATO_SCHEDA_SCAI_ESTINTO);
				}
			}
			if(blocca) {
				if(putErrorOn != null)
					PdfUtil.putError(pdfData, putErrorOn, err);
				else
					codiceCliente.addTypeError(err);
				return false;
			}
			
			return true;
			
		} catch (DAOException daoe) {
			if(putErrorOn != null)
				PdfUtil.putError(pdfData, putErrorOn, daoe.toString());
			else
				codiceCliente.addTypeError(daoe.toString());
			return false;
		}
	}	
	
}
