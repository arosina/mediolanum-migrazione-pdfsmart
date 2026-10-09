package prgm.ita.anagraficaclienti.print;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.PrintCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.AdempimentiNormativiModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiPrivacyModel;
import prgm.ita.anagraficaclienti.model.DatiStampa;
import prgm.ita.anagraficaclienti.model.InfoPersonaliModel;

/*******************************************************************/
/*******************************************************************/
public abstract class AbstractScheda extends PrintCommand {

	private static final long serialVersionUID = 1L;
	
	private static final String COD_NAZIONE = "codNazione";

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return prepareStampaScheda(userSessionContext, (ClienteModel)dataModel);
	}

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	public static ClienteModel prepareStampaScheda(UserSessionContext usc, ClienteModel model) throws CommandException {
		
		ClientSessionContext csc = usc.getClientSessionContext();
		ClienteModel cliente = null;
		try {
			cliente = (ClienteModel)Tools.cloneObject(model);
		}catch(CloneNotSupportedException cns) {
			throw new CommandException(cns.toString());
		}

		try {
			DAOObject dao = new DAOObject(csc, "ItaAnagraficaClienti.AnagraficaClienti");
			
			// Aggiorno le informazioni sulle nazioni fiscali (non sono salvate con il cliente)
			cliente.getInfoPersonali().initCombinazioneProvenienzaPatrimonioChecks();
			AnagraficaCliente.aggiornaInfoResidenzeFiscali(csc, cliente);
					
			DatiStampa ds = new DatiStampa();
			ds.setData(Tools.today());
			
			ds.addProperty("nomeCognome",new StringType(cliente.getNome()+" "+cliente.getCognome()));
			
			String cityStateAndZipCode =  cliente.getResidenza().getIndirizzo().getDescrComune()+", "+
		    							  cliente.getResidenza().getIndirizzo().getDescValue(COD_NAZIONE);
			if(!cliente.getResidenza().getIndirizzo().getCap().isNull())
				cityStateAndZipCode += ", "+cliente.getResidenza().getIndirizzo().getCap();
			ds.addProperty("cityStateAndZipCode",new StringType(cityStateAndZipCode));
			
			// Fatca
			prepareStampaFatca(cliente, ds);
			
			// Codice cliente per variazioni
			if(cliente.getIsEffettivo().booleanValue())
				ds.addProperty("codiceVariazione",new StringType(cliente.getCodMediolanum()+"-"+cliente.getProgressivo()));
			
			// Domicilio - Pulisco "italia" se è vuoto perchè la nazione è sempre valorizzata a "I"
			ds.addProperty("nazioneDomicilio",new StringType(cliente.getDomicilio().getIndirizzo().getDescValue(COD_NAZIONE)));
			if(cliente.getDomicilio().getIndirizzo().isEmpty())
				ds.addProperty("nazioneDomicilio",new StringType());
			
			// Residenze fiscali
			prepareStampaResidenzaFiscale2(cliente, ds);
			prepareStampaResidenzaFiscale3(cliente, ds);
			
			// Info personali (PEP)
			prepareStampaPep(dao, cliente, ds);
			
			if(cliente.getIsDitta().booleanValue()){
				if(cliente.getTipoDitta().equals(Costanti.TIPO_DITTA_DITTA))
					ds.addProperty("tipoDittaDitta",new StringType("X"));
				else if(cliente.getTipoDitta().equals(Costanti.TIPO_DITTA_LIBPROF))
					ds.addProperty("tipoDittaLibProf",new StringType("X"));
			}
			
			// Info personali
			InfoPersonaliModel infoPersonali = cliente.getInfoPersonali();
			infoPersonali.getHaFigli().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			infoPersonali.getAttualeAbitazione().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			infoPersonali.getFlagMutuo().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			
			// Scheda privacy
			ds.setCodFiscPartIva(cliente.getCodFiscale());
			if(ds.getCodFiscPartIva().isNull())
				ds.setCodFiscPartIva(cliente.getPartitaIva());
			DatiPrivacyModel datiprivacy = cliente.getDatiPrivacy();
			datiprivacy.getFlagCarte().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			datiprivacy.getFlagLiberatoria().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			datiprivacy.getFlagExtraUE().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			datiprivacy.getFlagProfilazione().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			
			// Adempimenti normativi
			AdempimentiNormativiModel adempimenti = cliente.getAdempimentiNormativi();
			if(!adempimenti.getHaCarichePubbliche().isNull() && !adempimenti.getHaCarichePubbliche().equals("S"))
				adempimenti.setCaricaPubblicaRicoperta(new StringType(Costanti.CODICE_CARICA_PUBBLICA_RICOPERTA_NO));
			adempimenti.getCaricaPubblicaRicoperta().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			adempimenti.getDettaglioCaricaPubblicaRicoperta().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);
			if(!adempimenti.getHaLegamiAffariDiversiDaAttivitaPrincipale().isNull() && !adempimenti.getHaLegamiAffariDiversiDaAttivitaPrincipale().equals("S"))
				adempimenti.setTipologiaLegameAffariDiversoDaAttivitaPrincipale(new StringType(Costanti.CODICE_TIPOLOGIA_LEGAME_AFFARI_DIVERSO_ATTIVITA_PRINCIPALE_NO));
			adempimenti.getTipologiaLegameAffariDiversoDaAttivitaPrincipale().setPrintableType(AbstractType.PRINTABLE_GROUP_TYPE);		
	
			ds.addProperty("fasciaPatrimonioComplessivo"+infoPersonali.getFasciaPatrimonioComplessivo(),new StringType("X"));
			ds.addProperty("fasciaRedditoAnnuale"+infoPersonali.getFasciaRedditoAnnuale(),new StringType("X"));
			
			// Inclusione modulo allegati in stampa
			gestisciInclusioneModuloAllegatiExtraUE(dao, cliente, ds);
			
			cliente.setDatiStampa(ds);
			return cliente;
		}catch(DAOException daoe) {
			throw new CommandException(daoe.toString());
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static void prepareStampaFatca(ClienteModel cliente, DatiStampa ds) {
		String appliedFor = "";
		String tinA = "";
		String tinB = "";
		String tinC = "";
		if(cliente.getDatiFatca().getIsAppliedFor().booleanValue()){
			appliedFor = " APPLIED   FOR";
		}else if(!cliente.getDatiFatca().getTin().isNull() && cliente.getDatiFatca().getTin().toString().length() > 5){
			tinA = cliente.getDatiFatca().getTin().toString().substring(0,3);
			tinB = cliente.getDatiFatca().getTin().toString().substring(3,5);
			tinC = cliente.getDatiFatca().getTin().toString().substring(5);
		}
		ds.addProperty("appliedFor",new StringType(appliedFor));
		ds.addProperty("tinA",new StringType(tinA));
		ds.addProperty("tinB",new StringType(tinB));
		ds.addProperty("tinC",new StringType(tinC));
		
		ds.addProperty("nonUsPersonAltroRecapito",new StringType(cliente.getRecapiti().getTelefonoResidenza().getNumeroTelefonoCompleto().toString()));
	}

	/*******************************************************************/
	/*******************************************************************/
	private static void prepareStampaResidenzaFiscale2(ClienteModel cliente, DatiStampa ds) throws DAOException {
		if(!cliente.getResidenza().getCodNazioneResidenzaFiscale2().isNull()){
			if(cliente.getResidenza().getCodNazioneResidenzaFiscale2().equals(Costanti.COD_NAZIONE_US_UIC)){
				ds.addProperty("codFiscaleResidenzaFiscale2", cliente.getDatiFatca().getTin());
			}else{
				if(cliente.getResidenza().getCodFiscaleResidenza2Released().booleanValue()){
					ds.addProperty("codFiscaleResidenzaFiscale2", cliente.getResidenza().getCodFiscaleResidenzaFiscale2());
				}else{
					// X se la nazione non prevede cf
					ds.addProperty("codFiscaleResidenzaFiscale2NonRilasciato", new StringType("X"));
				}
			}
		}
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private static void prepareStampaResidenzaFiscale3(ClienteModel cliente, DatiStampa ds) throws DAOException {
		if(!cliente.getResidenza().getCodNazioneResidenzaFiscale3().isNull()){
			if(cliente.getResidenza().getCodNazioneResidenzaFiscale3().equals(Costanti.COD_NAZIONE_US_UIC)){
				ds.addProperty("codFiscaleResidenzaFiscale3", cliente.getDatiFatca().getTin());
			} else{
				if(cliente.getResidenza().getCodFiscaleResidenza3Released().booleanValue()){
					ds.addProperty("codFiscaleResidenzaFiscale3", cliente.getResidenza().getCodFiscaleResidenzaFiscale3());
				}else{
					// X se la nazione non prevede cf
					ds.addProperty("codFiscaleResidenzaFiscale3NonRilasciato", new StringType("X"));
				}
			}
		}		
	}

	/*******************************************************************/
	/*******************************************************************/
	private static void prepareStampaPep(DAOObject dao, ClienteModel cliente, DatiStampa ds) throws DAOException {
		if(cliente.getResidenza().getFlagPep().isNull() || cliente.getResidenza().getFlagPep().equals("N"))
			ds.addProperty("flagPepNo",new StringType("X"));
		else
			ds.addProperty("flagPepSi",new StringType("X"));
		
		ds.addProperty("motivazionePep",cliente.getResidenza().getMotivazionePep());
		StringType codiceMotivazionePepStampa = (StringType)dao.executeQueryAccess("loadCodiceMotivazionePepStampa", cliente).getSingleResult();
		if(codiceMotivazionePepStampa == null)
			codiceMotivazionePepStampa = new StringType();
		ds.addProperty("codiceMotivazionePep",codiceMotivazionePepStampa);

		if(cliente.getAdempimentiNormativi().getHaLegamiParentelaConPep().isNull() || cliente.getAdempimentiNormativi().getHaLegamiParentelaConPep().equals("N"))
			ds.addProperty("haLegamiParentelaConPepNo",new StringType("X"));
		else
			ds.addProperty("haLegamiParentelaConPepSi",new StringType("X"));
		
		if(cliente.getAdempimentiNormativi().getHaLegamiAffariConPep().isNull() || cliente.getAdempimentiNormativi().getHaLegamiAffariConPep().equals("N"))
			ds.addProperty("haLegamiAffariConPepNo",new StringType("X"));
		else
			ds.addProperty("haLegamiAffariConPepSi",new StringType("X"));
	}


	/*******************************************************************/
	/*******************************************************************/
	private static void gestisciInclusioneModuloAllegatiExtraUE(DAOObject dao, ClienteModel cliente, DatiStampa ds) throws DAOException {
		ds.addProperty("includiModuloAllegatiExtraUE", new BooleanType());
		if(cliente.getResidenza().getIndirizzo().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)) {
			MapCommandDataModel input = new MapCommandDataModel();
			
			input.addProperty(COD_NAZIONE, cliente.getCittadinanza());
			BooleanType isCitt1ExtraUE = (BooleanType)dao.executeQueryAccess("isNazioneExtraUE", input).getSingleResult();
			
			BooleanType isCitt2ExtraUE = new BooleanType(true);
			if(!cliente.getSecondaCittadinanza().isNull()) {
				input.addProperty(COD_NAZIONE, cliente.getSecondaCittadinanza());
				isCitt2ExtraUE = (BooleanType)dao.executeQueryAccess("isNazioneExtraUE", input).getSingleResult();
			}
			
			if(isCitt1ExtraUE.booleanValue() && isCitt2ExtraUE.booleanValue())
				ds.addProperty("includiModuloAllegatiExtraUE", new BooleanType(true));
		}
	}
}
