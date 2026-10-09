package prgm.pdfwebformspolizzeutil.censimentoanagraficocsc;

import java.util.ArrayList;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.model.SoggettoAnagraficoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CensimentoAnagraficoUtils {

	private CensimentoAnagraficoUtils() {
		throw new IllegalStateException("Utility class");
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ArrayList<SoggettoAnagraficoModel> getAnagraficheDaCensire(PdfDataModel pdfDataModel, String prefissoBeneficiario) throws Exception{
		//regole di valorizzazione estero : per la nascita il luogo è obbligatorio sul pdf, ma le regole dicono che va passata la nazione in caso estero		
		
		ArrayList<SoggettoAnagraficoModel> retval = new ArrayList<SoggettoAnagraficoModel>();
		int ordineCensimento = 1;
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario) ;
		
		if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			for (int i = 1;; i++) {
				String suffissoBeneficiario = prefissoBeneficiario + i;
				if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
					break;
				}
	
				StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
	
				if (!isGiaCliente.isNull() 
						&& isGiaCliente.equals("N")) {
					boolean isPersonaFisica = true;
					if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("false")) {
						isPersonaFisica = false;
					}
					
					if (isPersonaFisica) {
						SoggettoAnagraficoModel soggetto = new SoggettoAnagraficoModel();
						
						buildCommonFields(pdfDataModel,soggetto);
						soggetto.setTipologiaPersona(Constants.CA_TIPOLOGIA_PERSONA_FISICA);
						soggetto.setTipoSchedaAnagrafica(Constants.CA_TIPO_SCHEDA_ANAGRAFICA_BENEFICIARIO);
						soggetto.setCodiceClienteProspect((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
						soggetto.setOrdineCensimento(ordineCensimento);
						
						buildDatiAnagraficiPF(pdfDataModel, suffissoBeneficiario, soggetto);
						buildDatiResidenza(pdfDataModel, suffissoBeneficiario, soggetto);
						buildRecapiti(pdfDataModel, suffissoBeneficiario, soggetto);
						
						retval.add(soggetto);
						ordineCensimento++;
						
					} else {
						
						SoggettoAnagraficoModel soggettoPG = new SoggettoAnagraficoModel();
						
						buildCommonFields(pdfDataModel,soggettoPG);
						
						soggettoPG.setTipologiaPersona(Constants.CA_TIPOLOGIA_PERSONA_GIURIDICA);
						soggettoPG.setTipoSchedaAnagrafica(Constants.CA_TIPO_SCHEDA_ANAGRAFICA_BENEFICIARIO);
						soggettoPG.setCodiceClienteProspect((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
						
						buildDatiAnagraficiPG(pdfDataModel, suffissoBeneficiario, soggettoPG);
						buildDatiResidenza(pdfDataModel, suffissoBeneficiario, soggettoPG);
						buildRecapiti(pdfDataModel, suffissoBeneficiario, soggettoPG);
						
						// ciclo sui titolari
						for (int j = 1;; j++) {
							String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;
	
							if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
								break;
							}
	
							StringType isGiaClienteTitolare = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
							
							if (!isGiaClienteTitolare.isNull() 
									&& isGiaClienteTitolare.equals("N")) {
								SoggettoAnagraficoModel soggetto = new SoggettoAnagraficoModel();
								
								buildCommonFields(pdfDataModel,soggetto);

								soggetto.setTipologiaPersona(Constants.CA_TIPOLOGIA_PERSONA_FISICA);
								soggetto.setTipoSchedaAnagrafica(Constants.CA_TIPO_SCHEDA_ANAGRAFICA_TITOLARE);
								soggetto.setOrdineCensimento(ordineCensimento);
								
								StringType codiceClienteProspect = (StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoTitolare);
								if (j == 1) {
									soggetto.setCodiceClienteProspect(codiceClienteProspect);
									soggettoPG.setCodiceProspectTitolare1(codiceClienteProspect);
								}
								if (j == 2) {
									soggetto.setCodiceClienteProspect(codiceClienteProspect);
									soggettoPG.setCodiceProspectTitolare2(codiceClienteProspect);
								}
								
								buildDatiAnagraficiPF(pdfDataModel, suffissoTitolare, soggetto);
								buildDatiResidenza(pdfDataModel, suffissoTitolare, soggetto);
								buildRecapiti(pdfDataModel, suffissoTitolare, soggetto);
								
								retval.add(soggetto);
								ordineCensimento++;
							}
							if (!isGiaClienteTitolare.isNull() && isGiaClienteTitolare.equals("S")) {
								if (j == 1) {
									soggettoPG.setCodiceTitolare1((StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
								}
								if (j == 2) {
									soggettoPG.setCodiceTitolare2((StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
								}
							}
	
						}
						
						
						soggettoPG.setOrdineCensimento(ordineCensimento);
						retval.add(soggettoPG);
						ordineCensimento++;
						
						
					}
				}
	
			}
		}
		return retval;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void buildDatiAnagraficiPF(PdfDataModel pdfDataModel, String suffisso, SoggettoAnagraficoModel soggetto) {
		
		soggetto.setNome((StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setCognome((StringType) pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setCodiceFiscale((StringType) pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffisso));
		
		StringType sesso = (StringType) pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF + suffisso);

		if (sesso.equals("M")) {
			soggetto.setSesso(Constants.CA_SESSO_MASCHIO);
		} else if (sesso.equals("F")) {
			soggetto.setSesso(Constants.CA_SESSO_FEMMINA);
		}
		
		soggetto.setDataNascita((DateType) pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setLuogoNascita((StringType) pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setProvinciaComuneNascita((StringType) pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setNazioneComuneNascita((StringType) pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void buildDatiResidenza(PdfDataModel pdfDataModel, String suffisso, SoggettoAnagraficoModel soggetto) {
		
		soggetto.setTipoVia((StringType) pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setIndirizzo((StringType) pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setNumeroCivicoIndirizzo((StringType) pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setCapComune((StringType) pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setLocalita((StringType) pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setProvinciaComune((StringType) pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setNazioneComune((StringType) pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffisso));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void buildRecapiti(PdfDataModel pdfDataModel, String suffisso, SoggettoAnagraficoModel soggetto) {
		
		StringType tipoTelefono = (StringType) pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffisso);
		
		if (tipoTelefono.equals("F")) {
			soggetto.setPrefissoTelefonoFisso((StringType) pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setTelefonoFisso((StringType) pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffisso));
		} else if (tipoTelefono.equals("C")) {
			soggetto.setPrefissoTelefonoCellulare((StringType) pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setTelefonoCellulare((StringType) pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffisso));
		}

		soggetto.setEmail((StringType) pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffisso));
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void buildCommonFields(PdfDataModel pdfDataModel,  SoggettoAnagraficoModel soggetto) {
		soggetto.setBarcode((StringType) pdfDataModel.read(Constants.BAR_CODE_FIELD_NAME_PDF));
		soggetto.setPdfInstanceId(pdfDataModel.getPdfInstanceId());
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void buildDatiAnagraficiPG(PdfDataModel pdfDataModel, String suffisso, SoggettoAnagraficoModel soggetto) {
		soggetto.setCognome((StringType) pdfDataModel.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setPartitaIVA((StringType) pdfDataModel.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setNumeroIscrizioneREA((StringType) pdfDataModel.read(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setDataIscrizioneREA((DateType) pdfDataModel.read(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffisso));
		soggetto.setProvinciaIscrizioneREA((StringType) pdfDataModel.read(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffisso));
	}
}
