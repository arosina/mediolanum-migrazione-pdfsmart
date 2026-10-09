package prgm.pdfwebformspolizzeutil.postcompletion;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaBenefInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaRefTerzoInput;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispVitaTitolariInput;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.Utils;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioInputModel;

public class WriteDBUtils {
	private WriteDBUtils() {
		throw new IllegalStateException("Utility class");
	}
	
	
	public static InrDispVitaRefTerzoInput builReferenteTerzo(ClientSessionContext csc, PdfDataModel pdfDataModel, StringType codDisposizione, StringType codAgente, StringType codRete) throws Exception {
		InrDispVitaRefTerzoInput res = new InrDispVitaRefTerzoInput();
		res.setCodDisposizione(codDisposizione);
		res.setCodAgente(new StringType(Tools.fillSx(codAgente.toString(), '0', 10)));
		res.setCodRete(codRete); 
		res.setCodiceFiscale((StringType)pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
		res.setCognome((StringType)pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
		res.setNome((StringType)pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
		res.setToponimoIndirizzoResidenza((StringType)pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setIndirizzoResidenza((StringType)pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setNumeroCivicoResidenza((StringType)pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setCapResidenza((StringType)pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setComuneResidenza((StringType)pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setProvinciaResidenza((StringType)pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setPrefissoInternazionaleTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setPrefissoTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setNumeroTelefono((StringType)pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    res.setEmail((StringType)pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO));
	    
	    StringType nazioneResidenza = (StringType)pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO);
	    StringType codiceNazione = Utils.decodeDatoAnagrafico(csc, "NAZIONE", nazioneResidenza);
	    res.setNazioneResidenza(codiceNazione);
	    
	    if (!nazioneResidenza.equals(Constants.NAZIONE_ITALIA)) {
	    	res.setProvinciaResidenza(new StringType("EE"));
	    	res.setCapResidenza(new StringType("00000"));
	    }

	    return res;
	}
	
	
	public static BeneficiarioInputModel builBeneficiario(PdfDataModel pdfDataModel, StringType codDisposizione, StringType codAgente, StringType codRete, boolean assicurando,String prefissoBeneficiario, int indiceBeneficiario) {
		
		BeneficiarioInputModel res = new BeneficiarioInputModel();
		
		InrDispVitaBenefInput inrDispVitaBenefInput = new InrDispVitaBenefInput();
		
		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
		
		boolean isPersonaFisica = true;
		StringType isPersonaFisicaTemp = (StringType)pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF +suffissoBeneficiario);
		if (isPersonaFisicaTemp.equals("false")){
			isPersonaFisica = false;
		}
		
		StringType isBeneficiarioGiaCliente = (StringType)pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario); 
		
		//dati generali
		inrDispVitaBenefInput.setCodiceCliente((StringType)pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		inrDispVitaBenefInput.setTipologiaPersona(new StringType(isPersonaFisica ? "PF" : "PG"));
		
		inrDispVitaBenefInput.setCodDisposizione(codDisposizione);
		inrDispVitaBenefInput.setCodAgente(new StringType(Tools.fillSx(codAgente.toString(), '0', 10)));
		inrDispVitaBenefInput.setCodRete(codRete); 

		if (prefissoBeneficiario.equals(Constants.PREFISSO_BENEFICIARIO_DECESSO)) {
			inrDispVitaBenefInput.setTipologiaBeneficiario(new StringType("DEC"));
		} else if (prefissoBeneficiario.equals(Constants.PREFISSO_BENEFICIARIO_VITA)) {
			inrDispVitaBenefInput.setTipologiaBeneficiario(new StringType("VIT"));
			inrDispVitaBenefInput.setInvioComunicazione((StringType)pdfDataModel.read(Constants.INVIO_COMUNICAZIONE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		}
		
		
		//Recapiti
		inrDispVitaBenefInput.setTipoTelefono((StringType)pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		inrDispVitaBenefInput.setPrefissoInternazionaleTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		inrDispVitaBenefInput.setPrefissoTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		inrDispVitaBenefInput.setNumeroTelefono((StringType)pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		inrDispVitaBenefInput.setEmail((StringType)pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

		//indirizzo
		//CR29, rfc 217491: per i già clienti abbiamo sempre scritto il codice del toponimo, mentre per i nuovi clienti la descrizione
		//Questa gestione è per andare in continuità col pregresso e per gestire il pregresso
		StringType codToponimoBeneficiario = (StringType)pdfDataModel.read(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoBeneficiario);
		if (codToponimoBeneficiario != null && isBeneficiarioGiaCliente.equals("S"))
			inrDispVitaBenefInput.setToponimoIndirizzoResidenza(codToponimoBeneficiario);
		else
			inrDispVitaBenefInput.setToponimoIndirizzoResidenza((StringType)pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

		inrDispVitaBenefInput.setIndirizzoResidenza((StringType)pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
		inrDispVitaBenefInput.setNumeroCivicoResidenza((StringType)pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
		inrDispVitaBenefInput.setCapResidenza((StringType)pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
		inrDispVitaBenefInput.setComuneResidenza((StringType)pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
		inrDispVitaBenefInput.setProvinciaResidenza((StringType)pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
		inrDispVitaBenefInput.setNazioneResidenza((StringType)pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

		
		//percentuale
		inrDispVitaBenefInput.setPercentualeRipartizione((DoubleType)pdfDataModel.read(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

		ListType elencoTitolari = new ListType();
		
		if (isPersonaFisica) {
			inrDispVitaBenefInput.setCodiceFiscale((StringType)pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setNominativo((StringType)pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setNome((StringType)pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setSesso((StringType)pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setDataNascita((DateType)pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setComuneDiNascita((StringType)pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setProvinciaNascita((StringType)pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setNazioneNascita((StringType)pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setTipoRelazioneBeneficiarioContraente((StringType)pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setDescrizioneTipoRelazioneBeneficiarioContraente((StringType)pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

			if (assicurando) {
				inrDispVitaBenefInput.setTipoRelazioneBeneficiarioAssicurando((StringType)pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
				inrDispVitaBenefInput.setDescrizioneTipoRelazioneBeneficiarioAssicurando((StringType)pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			}
		} else {
			inrDispVitaBenefInput.setCodiceFiscale((StringType)pdfDataModel.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setNominativo((StringType)pdfDataModel.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

			inrDispVitaBenefInput.setNumeroIscrizioneCCIA((StringType)pdfDataModel.read(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setDataIscrizioneCCIA((DateType)pdfDataModel.read(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));
			inrDispVitaBenefInput.setProvinciaIscrizioneCCIA((StringType)pdfDataModel.read(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF+suffissoBeneficiario));

			
			
			
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;
				StringType isGiaCliente = (StringType)pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare); 

				if (isGiaCliente == null) {
					break;
				}
				
				if (isGiaCliente.isNull()) {
					continue;
				}
				
				InrDispVitaTitolariInput inrDispVitaTitolariInput = new InrDispVitaTitolariInput();
				
				inrDispVitaTitolariInput.setCodDisposizione(codDisposizione);
				inrDispVitaTitolariInput.setCodAgente(new StringType(Tools.fillSx(codAgente.toString(), '0', 10)));
				inrDispVitaTitolariInput.setCodRete(codRete); 

				//dati anagrafici
				inrDispVitaTitolariInput.setCodiceFiscaleBeneficiario(inrDispVitaBenefInput.getCodiceFiscale());
				inrDispVitaTitolariInput.setTipologiaBeneficiario(inrDispVitaBenefInput.getTipologiaBeneficiario());
				inrDispVitaTitolariInput.setCodiceCliente((StringType)pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				inrDispVitaTitolariInput.setCodiceFiscale((StringType)pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setCognome((StringType)pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setNome((StringType)pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setSesso((StringType)pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setDataNascita((DateType)pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setComuneDiNascita((StringType)pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setProvinciaNascita((StringType)pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setNazioneNascita((StringType)pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF+suffissoTitolare));
				
				
				//Recapiti
				inrDispVitaTitolariInput.setTipoTelefono((StringType)pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				inrDispVitaTitolariInput.setPrefissoInternazionaleTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				inrDispVitaTitolariInput.setPrefissoTelefono((StringType)pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				inrDispVitaTitolariInput.setNumeroTelefono((StringType)pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				inrDispVitaTitolariInput.setEmail((StringType)pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare));

				//indirizzo
				//CR29, rfc 217491: per i già clienti abbiamo sempre scritto il codice del toponimo, mentre per i nuovi clienti la descrizione
				//Questa gestione è per andare in continuità col pregresso e per gestire il pregresso
				StringType codToponimoTitolare = (StringType)pdfDataModel.read(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoTitolare);
				if (codToponimoTitolare != null && isGiaCliente.equals("S"))
					inrDispVitaTitolariInput.setToponimoIndirizzoResidenza(codToponimoTitolare);
				else
					inrDispVitaTitolariInput.setToponimoIndirizzoResidenza((StringType)pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setIndirizzoResidenza((StringType)pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setNumeroCivicoResidenza((StringType)pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setCapResidenza((StringType)pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setComuneResidenza((StringType)pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setProvinciaResidenza((StringType)pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setNazioneResidenza((StringType)pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF+suffissoTitolare));


				
				//relazioni
				inrDispVitaTitolariInput.setTipoRelazioneTitolareContraente((StringType)pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF+suffissoTitolare));
				inrDispVitaTitolariInput.setDescrizioneTipoRelazioneTitolareContraente((StringType)pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF+suffissoTitolare));

				if (assicurando) {
					inrDispVitaTitolariInput.setTipoRelazioneTitolareAssicurando((StringType)pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF+suffissoTitolare));
					inrDispVitaTitolariInput.setDescrizioneTipoRelazioneTitolareAssicurando((StringType)pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF+suffissoTitolare));
				}
				
				elencoTitolari.add(inrDispVitaTitolariInput);

				
			}
			

		}
		
		
		res.setInrDispVitaBenefInput(inrDispVitaBenefInput);
		res.setIsBeneficiarioPersonaFisica(new BooleanType(isPersonaFisica));
		res.setInrDispVitaTitolariInput(elencoTitolari);
		
		return res;
		
	}
}
