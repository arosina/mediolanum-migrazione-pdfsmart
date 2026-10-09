package prgm.pdfwebforms.agevolazioni;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;
import prgm.pdfwebforms.model.PdfDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AgevolazioneDataLoader {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static AgevolazioneModel loadAgevolazione(ClientSessionContext csc, PdfDataModel pdfData, String numAgevolazione, String codAgevolazione) throws Exception{
		try{
			
			if(numAgevolazione.length() == 0 && codAgevolazione.length() > 0){ // Deroga sostituzione o dipendenti
				
				String tipoAgevolazionePdf = "";
				String codiceAgevolazionePdf = "";
				String descrizioneAgevolazionePdf = "";
				String percentualeAgevolazionePdf = "";
				
				pdfData.startPdfInitialInputDataInitialization();

				if(codAgevolazione.equalsIgnoreCase("DIP")){
					ArrayList<PdfFieldInfos> radioValues = pdfData.getPdfInfos().getFieldInfos(PdfPredefinedFields.TIPO_AGEVOLAZIONE);
					if(radioValues.size() == 2){

						tipoAgevolazionePdf = PdfPredefinedFields.TIPO_AGEVOLAZIONE_DIPENDENTI;
						percentualeAgevolazionePdf = AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI;
						if(pdfData.read(PdfPredefinedFields.DESCR_AGEVOLAZIONE_DIPENDENTI) != null)
							pdfData.write(PdfPredefinedFields.DESCR_AGEVOLAZIONE_DIPENDENTI, new StringType(AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI+"%"));
						pdfData.setCodAgevolazione(new StringType(codiceAgevolazionePdf));
						
					}else if(radioValues.size() == 1){
						
						ProvideAgevolazioneDipendentiDataResponse agevDipData = PdfDriverCaller.callProvideAgevolazioneDipendentiData(csc, pdfData);
						if(agevDipData != null){
							codiceAgevolazionePdf = agevDipData.getCodiceAgevolazioneDipendenti();
							if( agevDipData.getDescrizioneAgevolazioneDipendenti().length() > 0)
								descrizioneAgevolazionePdf = agevDipData.getDescrizioneAgevolazioneDipendenti();
							else
								descrizioneAgevolazionePdf = ProvideAgevolazioneDipendentiDataResponse.DEFAUT_DESCRIZIONE_AGEVOLAZIONE_DIPENDENTI;
							descrizioneAgevolazionePdf += " - "+AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI+"%";
							percentualeAgevolazionePdf = AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI;
							tipoAgevolazionePdf = PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO;
						}
						pdfData.setCodAgevolazione(new StringType(codiceAgevolazionePdf));
						
					} else {
						pdfData.stopPdfInitialInputDataInitialization();
						return null;
					}
					
					
					
				}else{
					tipoAgevolazionePdf = PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO;
					codiceAgevolazionePdf = codAgevolazione;
					descrizioneAgevolazionePdf = AgevolazioneModel.DICITURA_DEROGA_SOSTITUZIONE;
					percentualeAgevolazionePdf = AgevolazioneModel.PERCENTUALE_DEROGA_SOSTITUZIONE;
				}

				if(pdfData.read(PdfPredefinedFields.TIPO_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType(tipoAgevolazionePdf));
				if(pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType());
				if(pdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.CODICE_AGEVOLAZIONE, new StringType(codiceAgevolazionePdf));
				if(pdfData.read(PdfPredefinedFields.DESCR_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType(descrizioneAgevolazionePdf));
				if(pdfData.read(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, new StringType());
				if(pdfData.read(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, new StringType());
				if(pdfData.read(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE) != null)
					pdfData.write(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, new DoubleType());

				if(pdfData.getPdfInfos().findFieldInfoByHtmlName(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null) {
					//La presenza del campo codiceAgevolazione nell'acroform implica la presenza della sezione agevolazione
					//Il motore aggiunge di default il campo percentualeAgevolazione nel pdfData in modo tale che nel metodo drawHiddenFields venga aggiunto all'html
					//Il campo percentualeAgevolazione viene aggiunto di default in quanto utilizzato nella chiamata al nuovo motore di adeguatezza
					//Questo comportamento di default ha evitato l'aggiunta del campo in tutti i pdf coinvolti nell'adeguamento verso il NMDA
					
					pdfData.write(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, new StringType(percentualeAgevolazionePdf));
				}

				pdfData.stopPdfInitialInputDataInitialization();
				return null;
			}
			
			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfAgevolazioni");
			
			AgevolazioneModel agevolazione = new AgevolazioneModel();
			agevolazione.setIdAgevolazione(new IntegerType(numAgevolazione));
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgevolazione", agevolazione);
			if(qRes.getResult().size() < 1){
				throw new Exception("Agevolazione ["+agevolazione.getIdAgevolazione()+"] non trovata");
			}
			
			if(agevolazione.getCodiceAgevolazione().isNull())
				throw new Exception("Codice Agevolazione non valorizzato per agevolazione ["+agevolazione.getIdAgevolazione()+"]");
			
			pdfData.startPdfInitialInputDataInitialization();

			if(pdfData.read("ndgCliente1") != null)
				pdfData.write("ndgCliente1", agevolazione.getCodCliente());
			if(pdfData.read(PdfPredefinedFields.TIPO_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType(PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO));
			if(pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType(agevolazione.getIdAgevolazione().toString()));
			if(pdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.CODICE_AGEVOLAZIONE, agevolazione.getCodiceAgevolazione());
			if(pdfData.read(PdfPredefinedFields.DESCR_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType(agevolazione.getDescrizioneAgevolazione()));
			if(pdfData.read(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, agevolazione.getTipologiaAgevolazione());
			if(pdfData.read(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, agevolazione.getModalitaVersamentoAgevolazione());
			if(pdfData.read(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE) != null)
				pdfData.write(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, agevolazione.getImportoAgevolazione());

			if(pdfData.getPdfInfos().findFieldInfoByHtmlName(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null) {
				//La presenza del campo codiceAgevolazione nell'acroform implica la presenza della sezione agevolazione
				//Il motore aggiunge di default il campo percentualeAgevolazione nel pdfData in modo tale che nel metodo drawHiddenFields venga aggiunto all'html
				//Il campo percentualeAgevolazione viene aggiunto di default in quanto utilizzato nella chiamata al nuovo motore di adeguatezza
				//Questo comportamento di default ha evitato l'aggiunta del campo in tutti i pdf coinvolti nell'adeguamento verso il NMDA

				pdfData.write(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, agevolazione.getPercentualeAgevolazione());
			}
			
			pdfData.stopPdfInitialInputDataInitialization();
			
			return agevolazione;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void uneditAgevolazione(PdfDataModel pdfData) throws Exception{
		
		pdfData.startPdfInitialInputDataInitialization();

		if(pdfData.read(PdfPredefinedFields.TIPO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.CODICE_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.DESCR_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, new DoubleType());

		pdfData.stopPdfInitialInputDataInitialization();

	}
}
