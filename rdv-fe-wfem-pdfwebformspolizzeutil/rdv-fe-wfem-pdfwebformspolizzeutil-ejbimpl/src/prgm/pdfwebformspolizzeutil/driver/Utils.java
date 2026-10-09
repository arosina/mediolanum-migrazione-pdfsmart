package prgm.pdfwebformspolizzeutil.driver;

import java.util.HashSet;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.prit.RigaPrit;
import prgm.pdfwebforms.drivers.io.srvdispositiva.DispoAggiuntiva;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataResponse;
import prgm.pdfwebforms.drivers.io.srvdispositiva.VincoloDispositiva;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.mom.SoggettoDispositivaCallModel;
import prgm.pdfwebformspolizzeutil.Constants;
import prgm.pdfwebformspolizzeutil.dao.CertificazioneFbDaoAccess;
import prgm.pdfwebformspolizzeutil.model.CertificazioneFbModel;

public class Utils {

	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static ProvideSrvDispositivaDataResponse buildSrvDispositivaData(PdfDataModel pdfDataModel, List<String> prefissoBeneficiario) throws Exception {

		if (!existAlmenoUnBeneficiarioNominativo(pdfDataModel, prefissoBeneficiario)) {
			return null;
		} else {
			ProvideSrvDispositivaDataResponse res = new ProvideSrvDispositivaDataResponse();
			
			HashSet<String> clientiPratica = new HashSet<String>();
			
			// beneficiari dispositiva principale
			for (String prefissoBeneficiarioTemp : prefissoBeneficiario) {
				
				StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiarioTemp);

				if (!tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
					continue;
				} 
				
				for (int i = 1;; i++) {
					String suffissoBeneficiario = prefissoBeneficiarioTemp + i;
					if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
						break;
					}

					StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

					if (!isGiaCliente.isNull()) {

						// Dispositiva principale - Soggetto Beneficiario i-esimo
						SoggettoDispositivaCallModel sogg = new SoggettoDispositivaCallModel();

						boolean isPersonaFisica = true;
						if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("false")) {
							isPersonaFisica = false;
						}
						
						boolean isGiaClienteTemp = isGiaCliente.equals("S");

						if (isPersonaFisica) {
							StringType codiceCliente = (StringType)pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
							StringType codiceProspect = (StringType)pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
							StringType chiaveSoggetto = (!codiceCliente.isNull()?codiceCliente:codiceProspect);
							
							if (!clientiPratica.contains(chiaveSoggetto.toString())) {
								buildSoggettoPF(pdfDataModel, suffissoBeneficiario, isGiaClienteTemp, true, sogg);
								res.getClientiAggiuntiviDispositiva().add(sogg);
								clientiPratica.add(chiaveSoggetto.toString());								
							}
						} else {
							StringType codiceCliente = (StringType)pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
							StringType codiceProspect = (StringType)pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
							StringType chiaveSoggetto = (!codiceCliente.isNull()?codiceCliente:codiceProspect);
							
							if (!clientiPratica.contains(chiaveSoggetto.toString())) {
								buildSoggettoPG(pdfDataModel, suffissoBeneficiario, isGiaClienteTemp, true, sogg);
								res.getClientiAggiuntiviDispositiva().add(sogg);
								clientiPratica.add(chiaveSoggetto.toString());
							}
						}
					}
				}

			}

			// creiamo la pratica aggiuntiva soltanto in presenza di beneficiari da censire
			if (existAlmenoUnBeneficiarioNominativoDaCensire(pdfDataModel, prefissoBeneficiario)) {

				// Dispositiva 1
				DispoAggiuntiva d = new DispoAggiuntiva();
				d.setTipoDispositiva("CENSIMENTO_ANAGRAFICO");
				d.setDescrizione("Censimento Anagrafico");
				d.setBarcode(pdfDataModel.read(Constants.BAR_CODE_FIELD_NAME_PDF).toString());

				// Dispositiva 1 - Codici PRIT (PDF_INFO_PRIT)
				d.getPritRetrieveInfo().setChiave("CENSIMENTO_ANAGRAFICO_BENEFICIARI");
				d.getPritRetrieveInfo().setPdfCode("*");

				clientiPratica = new HashSet<String>();
				
				for (String prefissoBeneficiarioTemp : prefissoBeneficiario) {
					for (int i = 1;; i++) {
						String suffissoBeneficiario = prefissoBeneficiarioTemp + i;
						if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
							break;
						}

						StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

						if (!isGiaCliente.isNull() 
								&& isGiaCliente.equals("N")) {

							// Dispositiva 1 - Soggetto i
							SoggettoDispositivaCallModel sogg = new SoggettoDispositivaCallModel();

							boolean isPersonaFisica = true;
							if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("false")) {
								isPersonaFisica = false;
							}
							
							if (isPersonaFisica) {
								StringType codiceProspect = (StringType)pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
								if (!clientiPratica.contains(codiceProspect.toString())) {
									buildSoggettoPF(pdfDataModel, suffissoBeneficiario, false, true, sogg);
									d.getSoggetti().add(sogg);
									clientiPratica.add(codiceProspect.toString());	
								}

							} else {
								StringType codiceProspect = (StringType)pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
								if (!clientiPratica.contains(codiceProspect.toString())) {
									buildSoggettoPG(pdfDataModel, suffissoBeneficiario, false, true, sogg);
									d.getSoggetti().add(sogg);
									clientiPratica.add(codiceProspect.toString());
								}
							}
						}
					}
				}

				res.getDispoAggiuntive().add(d);

				// Vincoli
				VincoloDispositiva v = new VincoloDispositiva();
				v.setIndiceDispositivaVincolante(0);
				v.setIndiceDispositivaVincolata(VincoloDispositiva.INDICE_DISPOSITIVA_PRINCIPALE);
				res.getVincoliDispositive().add(v);

			}
			return res;

		}

	}
	
	private static void buildSoggettoPF(PdfDataModel pdfDataModel, String suffisso, boolean isGiaCliente, boolean isBeneficiario, SoggettoDispositivaCallModel soggetto) {
		
		soggetto.setCodiceRuoloSoggetto(new StringType(isBeneficiario ? Constants.CODICE_RUOLO_SOGGETTO_BENEFICIARIO : Constants.CODICE_RUOLO_SOGGETTO_TITOLARE));
		soggetto.setCodiceTipoSoggetto(new StringType(isGiaCliente ? Constants.TIPO_SOGGETTO_CLIENTE : Constants.TIPO_SOGGETTO_PROSPECT));

		if (isGiaCliente) {
			soggetto.setCodiceSoggettoOriginale((StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffisso));
		} else {
			soggetto.setCodiceSoggettoOriginale((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setCodiceSoggettoEsterno((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setDenominazioneNomeSoggettoEsterno((StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setDenominazioneCognomeSoggettoEsterno((StringType) pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffisso));

		}
	}
	
	private static void buildSoggettoPG(PdfDataModel pdfDataModel, String suffisso, boolean isGiaCliente, boolean isBeneficiario, SoggettoDispositivaCallModel soggetto) {
		
		soggetto.setCodiceRuoloSoggetto(new StringType(isBeneficiario ? Constants.CODICE_RUOLO_SOGGETTO_BENEFICIARIO : Constants.CODICE_RUOLO_SOGGETTO_TITOLARE));
		soggetto.setCodiceTipoSoggetto(new StringType(isGiaCliente ? Constants.TIPO_SOGGETTO_CLIENTE : Constants.TIPO_SOGGETTO_PROSPECT));

		if (isGiaCliente) {
			soggetto.setCodiceSoggettoOriginale((StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffisso));
		} else {
			soggetto.setCodiceSoggettoOriginale((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setCodiceSoggettoEsterno((StringType) pdfDataModel.read(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffisso));
			soggetto.setDenominazioneCognomeSoggettoEsterno((StringType) pdfDataModel.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffisso));

		}
	}

	private static boolean existAlmenoUnBeneficiarioNominativoDaCensire(PdfDataModel pdfDataModel, List<String> prefissoBeneficiario) {
		boolean retval = false;

		for (String prefissoBeneficiarioTemp : prefissoBeneficiario) {
			if (retval) {
				break;
			}
			StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiarioTemp);

			if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {

				for (int i = 1;; i++) {
					String suffissoBeneficiario = prefissoBeneficiarioTemp + i;
					if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
						break;
					}

					StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

					if (!isGiaCliente.isNull() 
							&& isGiaCliente.equals("N")) {
						retval = true;
						break;
					}
				}
			} else {
				continue;
			}

		}

		return retval;

	}

	private static boolean existAlmenoUnBeneficiarioNominativo(PdfDataModel pdfDataModel,
			List<String> prefissoBeneficiario) {
		boolean retval = false;

		for (String prefissoBeneficiarioTemp : prefissoBeneficiario) {
			if (retval) {
				break;
			}
			StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiarioTemp);

			if (tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
				retval = true;
				break;
			} else {
				continue;
			}

		}

		return retval;

	}

	public static void addRigaPritBeneficiariDaCensire(PdfDataModel pdfDataModel, List<String> prefissoBeneficiario,
			ProvidePritDataResponse result) {

		if (existAlmenoUnBeneficiarioNominativoDaCensire(pdfDataModel, prefissoBeneficiario)) {

			if (result == null) {
				result = new ProvidePritDataResponse();
			}

			RigaPrit rigaPrit = new RigaPrit();
			rigaPrit.setChiavePrit("CENSIMENTO_ANAGRAFICO_BENEFICIARI");
			rigaPrit.setBarcode(pdfDataModel.read(Constants.BAR_CODE_FIELD_NAME_PDF) == null ? "" : pdfDataModel.read(Constants.BAR_CODE_FIELD_NAME_PDF).toString());
			result.addRigaDiPrit(rigaPrit);
		}
	}
	
	public static boolean certificazioneCompletata(ClientSessionContext csc, StringType codFb, StringType certificazione) throws Exception {
		
		CertificazioneFbDaoAccess dao = new CertificazioneFbDaoAccess(csc);
		
		CertificazioneFbModel certificazioneFbModel = dao.recuperaEsitoCertificazione(codFb, certificazione);
		
		return certificazioneFbModel != null;
	
	}
}
