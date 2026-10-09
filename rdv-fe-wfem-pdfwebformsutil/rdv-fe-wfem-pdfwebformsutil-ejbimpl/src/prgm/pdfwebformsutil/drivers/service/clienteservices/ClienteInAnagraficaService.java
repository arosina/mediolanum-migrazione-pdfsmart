package prgm.pdfwebformsutil.drivers.service.clienteservices;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsutil.drivers.dao.cliente.ClienteInAnagraficaDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.cliente.ClienteInAnagraficaInputModel;
import prgm.pdfwebformsutil.drivers.dao.cliente.ClienteType;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;

public class ClienteInAnagraficaService<T extends PdfBaseDriver> extends AbstractCachedService<T, ClienteInAnagraficaInputModel, PdfPersonModel> {

	
	public static final String PERSON_FIELD_CODICE_CLIENTE = "codiceCliente";
	public static final String PERSON_FIELD_CODICE_NDG = "codiceNdg";
	public static final String PERSON_FIELD_ID_CENSIMENTO = "idCensimento";
	public static final String PERSON_FIELD_CODICE_POTENZIALE = "codicePotenziale";
	public static final String PERSON_FIELD_NOME = "nome";
	public static final String PERSON_FIELD_COGNOME = "cognome";
	public static final String PERSON_FIELD_CODICE_FISCALE = "codiceFiscale";
	public static final String PERSON_FIELD_SESSO = "sesso";
	public static final String PERSON_FIELD_DATA_NASCITA = "dataNascita";
	public static final String PERSON_FIELD_COGNOME_NOME = "cognomeNome";
	public static final String PERSON_FIELD_TIPO_CLIENTE = "tipoCliente";
	public static final String PERSON_FIELD_IS_RICONCILIATO = "isRiconcliato";
	
	// Calcolati
	public static final String PERSON_FIELD_IS_GIA_CLIENTE = "isGiaCliente";

	public ClienteInAnagraficaService(T pdfDriver) {
		super(pdfDriver);
	}

	public ClienteInAnagraficaService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}
	
	@Override
	protected PdfPersonModel readData(ClientSessionContext csc, PdfDataModel pdfData, ClienteInAnagraficaInputModel key, boolean async)
			throws Exception, DAOException {

		PdfPersonModel personModel = null; 

		try {
			// Check base input
			if(key == null || key.getCodiceCliente() == null) {
				return null;
			}

			// Search Censito
			if(!key.getCodiceCliente().isEmpty()) {
				// Search Censito any 
				if(key.getCodiceAgente() == null || key.getCodiceAgente().isEmpty()) {
					personModel = getPdfDriver().readAnyCliente(csc, Tools.fillSx(key.getCodiceCliente(), '0', 11));
				}
				else {
					pdfData.setCodAgeImpersonato(new StringType(key.getCodiceAgente()));
					personModel = getPdfDriver().readCliente(csc, pdfData, Tools.fillSx(key.getCodiceCliente(), '0', 11));
				}
				
				if(personModel != null) {
					if(personModel.getIsEffettivo().booleanValue()) {
						personModel.writeProperty(PERSON_FIELD_TIPO_CLIENTE, new StringType(ClienteType.EFFETTIVO.name()));
					}
					else {
						personModel.writeProperty(PERSON_FIELD_TIPO_CLIENTE, new StringType(ClienteType.PROSPECT_FULL.name()));
					}
					personModel.writeProperty(PERSON_FIELD_IS_RICONCILIATO, new BooleanType(false));
				}
			}

			// Search Bozza
			if(personModel == null && key.getParamsBozza() != null && isCodicePotenziale(key.getCodiceCliente())) {
				key.getParamsBozza().setCodMediolanum(new StringType(key.getCodiceCliente()));
				
				ClienteInAnagraficaDaoAccess daoAccess = new ClienteInAnagraficaDaoAccess(csc);
				ClienteModel bozza = daoAccess.getClienteBozza(key.getParamsBozza());
				if(bozza != null) {
					personModel = new PdfPersonModel();
					personModel.setIdCensimento(new StringType(key.getCodiceCliente()));
					personModel.setNdg(new StringType());
					personModel.writeProperty(PERSON_FIELD_ID_CENSIMENTO, new StringType(key.getCodiceCliente()));
					personModel.writeProperty(PERSON_FIELD_NOME, bozza.getNome());
					personModel.writeProperty(PERSON_FIELD_COGNOME, bozza.getCognome());
					personModel.writeProperty(PERSON_FIELD_CODICE_FISCALE, bozza.getCodFiscale());
					personModel.writeProperty(PERSON_FIELD_SESSO, bozza.getSesso());
					personModel.writeProperty(PERSON_FIELD_DATA_NASCITA, bozza.getDataNascita());	
					personModel.writeProperty(PERSON_FIELD_COGNOME_NOME, new StringType(bozza.getCognome().getStringValue() + " " + bozza.getNome().getStringValue()));
					personModel.writeProperty(PERSON_FIELD_TIPO_CLIENTE, new StringType(ClienteType.PROSPECT_LIGHT.name()));
					personModel.writeProperty(PERSON_FIELD_IS_RICONCILIATO, new BooleanType(false));
					personModel.writeProperty(PERSON_FIELD_IS_GIA_CLIENTE, new StringType("false"));

					// DATI AGGIUNTIVI
					daoAccess.getClienteBozzaDatiAggiuntivi(personModel);
				}
			}

			// Search Riconciliazione
			if(personModel == null && key.isDoRiconciliazione()) {
				ClienteInAnagraficaDaoAccess daoAccess = new ClienteInAnagraficaDaoAccess(csc);
				MapCommandDataModel riconciliato = null;
				MapCommandDataModel input = null; 
				
				String codiceNdg = null;

				// search by codice potenziale
				if(isCodicePotenziale(key.getCodiceCliente())) {
					input = new MapCommandDataModel();
					input.addProperty(PERSON_FIELD_CODICE_CLIENTE, new StringType(key.getCodiceCliente()));
					riconciliato = daoAccess.getClienteRiconciliato(input);
					if(riconciliato != null) {
						codiceNdg = riconciliato.readProperty(PERSON_FIELD_CODICE_NDG).getStringValue();
					}
				}

				// search by codice fiscale
				if(codiceNdg == null && key.getCodiceFiscale() != null && !key.getCodiceFiscale().trim().isEmpty()) {
					input = new MapCommandDataModel();
					input.addProperty(PERSON_FIELD_CODICE_FISCALE, new StringType(key.getCodiceFiscale()));
					riconciliato = daoAccess.getClienteRiconciliato(input);
					if(riconciliato != null) {
						codiceNdg = riconciliato.readProperty(PERSON_FIELD_CODICE_NDG).getStringValue();
					}
				}

				// read cliente
				if(codiceNdg != null) {
					if(key.getCodiceAgente() == null || key.getCodiceAgente().isEmpty()) {
						personModel = getPdfDriver().readAnyCliente(csc, Tools.fillSx(codiceNdg, '0', 11));
					}
					else {
						pdfData.setCodAgeImpersonato(new StringType(key.getCodiceAgente()));
						personModel = getPdfDriver().readCliente(csc, pdfData, Tools.fillSx(codiceNdg, '0', 11));
					}
					
					if(personModel != null) {
						if(personModel.getIsEffettivo().booleanValue()) {
							personModel.writeProperty(PERSON_FIELD_TIPO_CLIENTE, new StringType(ClienteType.EFFETTIVO.name()));
						}
						else {
							personModel.writeProperty(PERSON_FIELD_TIPO_CLIENTE, new StringType(ClienteType.PROSPECT_FULL.name()));
						}
						personModel.writeProperty(PERSON_FIELD_IS_RICONCILIATO, new BooleanType(true));
					}
				}
			}
			
			onExecutionSuccess(key.getCodiceCliente(), personModel);
			return personModel;		
		} catch (Exception e) {
			onExecutionError(this, pdfData, key, e);
			throw e;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, ClienteInAnagraficaInputModel key) {
		return key.getCodiceCliente();
	}

	public void onExecutionSuccess(String codCliente, PdfPersonModel obj) {
		setHolderValueAndReleaseLock(null, codCliente, obj);
	}

	public void onExecutionError(String codCliente, PdfPersonModel obj) {
		setHolderValueAndReleaseLock(null, codCliente, obj);
	}
	
	private static boolean isCodicePotenziale(String codice) {
		return codice.toUpperCase().startsWith("S");	
	}
	
}
