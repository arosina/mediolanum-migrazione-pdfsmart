/* PER TESTARE IN CEDACRI ASTERISCARE LE RIGHE CON "CEDACRI" */
/* aggiornamento della versione contenuta nel file "C:\EclipseProjects\Adeguatezza\bin\prgm\moduleVersion" */
/* Creazione del Pacchetto : File - Export - JAR - Selezionare tutto + Zippare tutta la Cartella ADEGUATEZZA */
/* 2.7.1 Tolta obbligatorietà del dossier per i titoli illiquidi, e se non presente non facciamo i controlli di frequenza */
/* 20101120 Controllo consulenza titoli di terzi */
/* 20110223 Controllo consulenza solo per strumenti titoli */
/* 20110611 questionario con importo */
/* 20110725 compatibilità questionario importo */
/* 20111002 passaggio in risposta "sevalid" nelle funzioni "getquestionario", "getquestionariorete", "getquestionariouserkey",
 * utilizzate dai servizi GETQUESTIONARIO e GETELENCOQUESTIONARI. */ 
/* 20111025:PG gestione questionario persone giuridiche */
/*v12: gestione forzatura orizzonte temporale da apposita domanda, doamnda specifica per determinare l'esigenza di liquidità, e la 
 *v12: memorizzazione dei seguenti dati:
 *v12: esigenza di liquidità
 *v12: valore della forzatura dell'obiettivo temporale
 *v12: obiettivo temporale originale
 *v12: cluster originale determinato ante forzatura dell'obiettivo temporale
 *v12*/
/*v13: gestione risposte facoltative da tabella TZ 869 dopo la gestione cluster primo carattere, la durata in anni della scheda 
 *v13: secondo carattere ci sono tre caratteri che identificano il numero della prima risposta facoltativa, i successivi tre caratteri
 *v13: identificano la seconda risposta facoltativa. 
 *v13*/
/*v14: gestione temporanea della possibilità di non rispondere alla domanda 20*/
/*v15: gestione temporanea della possibilità di non rispondere alla domanda 21*/
/*20120924: correzzione richiesta della Banca */
/*121009: gestione domande facoltative in base a tabella 869 con chiave release + domanda */
/*121121: corretto bug su impostazione risposte 20 e 21 su PP versione 007 e 107 */

package prgm.cedacri.adeguatezza;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.ejb.EJBException;

import prgm.cedacri.adeguatezza.cache.CacheManager;
import prgm.cedacri.adeguatezza.cache.PrgmCommands;
import prgm.cedacri.adeguatezza.internal.RisposteCompilateModel;
import prgm.cedacri.adeguatezza.internal.StatoAdeguatezza;
import prgm.cedacri.adeguatezza.internal.TW00TBO2Model;
import prgm.cedacri.adeguatezza.internal.TW00TBQKModel;
import prgm.cedacri.adeguatezza.internal.TW00TBQMModel;
import prgm.cedacri.adeguatezza.internal.TW00TBSKModel;
import prgm.cedacri.adeguatezza.internal.TW00TBTZModel;
import prgm.cedacri.adeguatezza.internal.UserKeyModel;
import prgm.cedacri.adeguatezza.log.AccessLog;
import prgm.cedacri.adeguatezza.model.ElementoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.InputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.InputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.InputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.InputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.InputSalvaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputAggiornaPatrimonioModel;
import prgm.cedacri.adeguatezza.model.OutputCalcoloProfiloModel;
import prgm.cedacri.adeguatezza.model.OutputCancellaQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputCtrlAdeguatezzaModel;
import prgm.cedacri.adeguatezza.model.OutputGetElencoQuestionariModel;
import prgm.cedacri.adeguatezza.model.OutputGetNuovoQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.cedacri.adeguatezza.model.OutputSalvaQuestionarioModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.dao.exceptions.PrimaryKeyViolation;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Logger;
import com.atosorigin.wfem.util.Tools;

public class AdeguatezzaManagerImpl
{
	public static final BooleanType falseType = new BooleanType(false);
	public static final BooleanType trueType = new BooleanType(true);
	public static final String FilDossZero = new String("999");
	public static final String ContDossZero = new String("99999999");
	public static final String ProgDossZero = new String("9999");

	public static OutputGetNuovoQuestionarioModel getNuovoQuestionario (ClientSessionContext csc, InputGetNuovoQuestionarioModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException 
	{
		Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputGetNuovoQuestionarioModel output = null;
		InputGetQuestionarioModel internalInput = null;
		OutputGetQuestionarioModel internalOutput = null; 
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		//Inizializzazione variabili
		try
		{
			output = new OutputGetNuovoQuestionarioModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA[ Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}

 		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI" 	*/
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
				{
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					Logger.getInstance().debug(esito.descrizione);
					return output;
				}
/*           "CEDACRI" */
		}
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		try
		{
			internalInput = new InputGetQuestionarioModel();
			internalInput.setCanVend(input.getCanVend());
			internalInput.setNdgDoss(input.getNdgDoss());
			internalInput.setNdgTemp(input.getNdgTemp());
			internalInput.setUserKey(new StringType());
			internalInput.setCountry(input.getCountry());
			internalInput.setUsername(input.getUsername());
			internalInput.setPG(input.getPG());
			internalOutput = getQuestionario(csc,internalInput,nomeMetodo,AdeguatezzaManagerImpl.trueType);
			output.setEsito(internalOutput.getEsito());
			output.setDescErr(internalOutput.getDescErr());
			if (internalOutput.getEsito().equalsIgnoreCase("000"))
			{
				output.setDatComp(internalOutput.getDatComp());
				output.setOraComp(internalOutput.getOraComp());
				output.setProfilo(internalOutput.getProfilo());
				output.setCluster(internalOutput.getCluster());
				output.setQuestionario(internalOutput.getQuestionario());
				output.setRelease(internalOutput.getRelease());
				output.setSeCompi(internalOutput.getSeCompi());
				output.setCluster(internalOutput.getCluster());
				output.setDesCluster(internalOutput.getDesCluster());
/*v12*/			output.setEsigLiq(internalOutput.getEsigLiq());
/*v12*/			output.setForzObT(internalOutput.getForzObT());
/*v12*/			output.setOrigObT(internalOutput.getOrigObT());
/*v12*/			output.setOrigClu(internalOutput.getOrigClu());
/*v12*/			output.setDesOrigClu(internalOutput.getDesOrigClu());
				output.setDfinval(internalOutput.getDfinval());
				/* 20140829 aggiunta Disc */
				output.setObbtemp(internalOutput.getObbtemp());
				output.setSitfina(internalOutput.getSitfina());
				output.setObbinve(internalOutput.getObbinve());
				output.setEspfina(internalOutput.getEspfina());
				/* 20140829 aggiunta Disc */
			}
			Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}
		return output;
	}

	public static OutputCalcoloProfiloModel calcoloProfilo (ClientSessionContext csc, InputCalcoloProfiloModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputCalcoloProfiloModel output = null;
//		InputGetElencoQuestionariModel inputSeCompiModel = null;
//		OutputGetElencoQuestionariModel outputSeCompiModel = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		//Inizializzazione variabili
		try
		{
			output = new OutputCalcoloProfiloModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"     */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
/*       "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp(null);
			}catch (Exception e){}
			if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
			  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				esito.esito = "004";
				esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp() == null) && (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getRisposte() == null) || (input.getRisposte().size() == 0))
			{
				esito.esito = "004";
				esito.setDescr("Campo risposte non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();
			
			//Verifica questionario già compilato
/*
 			inputSeCompiModel = new InputGetElencoQuestionariModel();
			inputSeCompiModel.setCanVend(input.getCanVend());
			inputSeCompiModel.setNdgDoss(input.getNdgDoss());
			inputSeCompiModel.setNdgTemp(input.getNdgTemp());
			queryResult = dao.executeQueryAccess("SeQuestionarioCompilato",inputSeCompiModel);
			if (queryResult.getResult().size() == 0)
			{
				output.setSeCompi(new StringType("N"));				
			} 
			else 
			{
				outputSeCompiModel = (OutputGetElencoQuestionariModel)queryResult.getResult().get(0);
				output.setSeCompi(outputSeCompiModel.getSeCompi());
			}
*/
			if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
				queryResult = dao.executeQueryAccess("GetQuestionarioRete",input);
			else
				queryResult = dao.executeQueryAccess("GetQuestionario",input);
	
			if (queryResult.getResult().size() > 0)
			{
				output.setSeCompi(new StringType("S"));
			} 
			else
			{
				output.setSeCompi(new StringType("N"));				
			}
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Compilato " + output.getSeCompi().getStringValue());

			Hashtable ht = new Hashtable();
			ListType punteggi = new ListType();

			if (AdeguatezzaBusinessUtility.VerificaQuestionarioNonRisposto(input.getRisposte()) == true)
			{
				output.setRifiuto(new StringType("S"));
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Non Risposto " + output.getRifiuto().getStringValue());
				if (input.getEta().bigValue() == null)
				{
					esito.esito = "014";
					esito.setDescr("Campo Eta non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getTitStud().getStringValue() == null) || (input.getTitStud().equalsIgnoreCase("")))
				{
					esito.esito = "013";
					esito.setDescr("Campo Titolo di Studio non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				input.setTitStud(new StringType(AdeguatezzaUtility.padLeft(input.getTitStud().getStringValue(),'0',3)));
				output.setSeInCon(new StringType("N"));	
				//Calcolo profilo default
				if (AdeguatezzaBusinessUtility.CalcoloProfiloDefault(cacheManager,dao,ht,input.getEta(),input.getTitStud(),esito) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Profilo di Default " + ht.get("PROFILO").toString() + ": Cluster di Default " + ht.get("CLUSTER").toString());
			}
			else
			{
				output.setRifiuto(new StringType("N"));	
				if (AdeguatezzaBusinessUtility.VerificaQuestionario(cacheManager,input.getRisposte(),esito,input.getPG()) == false)
				{
					output.setSeInCon(new StringType("S"));
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					output.setNumElem(new IntegerType(esito.numElem));
					Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Incompleto");
					return output;
				}
				else
				{
					output.setSeInCon(new StringType("N"));
					Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Completo");
				}
			
				//Calcolo profilo
				if (AdeguatezzaBusinessUtility.CalcoloProfilo(cacheManager,dao,input.getRisposte(),punteggi,ht,input.getEta(),esito,input.getPG()) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Profilo Calcolato " + ht.get("PROFILO").toString() + ": Cluster Calcolato " + ht.get("CLUSTER").toString());
			}
			output.setProfilo(new StringType(ht.get("PROFILO").toString()));
			output.setCluster(new StringType(ht.get("CLUSTER").toString()));
/*v12*/		output.setEsigLiq(new StringType(ht.get("ESIGLIQ").toString()));
/*v12*/		output.setForzObT(new StringType(ht.get("FORZOBT").toString()));
/*v12*/		output.setOrigObT(new StringType(ht.get("ORIGOBT").toString()));
/*v12*/		output.setOrigClu(new StringType(ht.get("ORIGCLU").toString()));
			
//			output.setDfinval(new IntegerType("0"));
			output.setDfinval(new IntegerType(ht.get("DFINVAL").toString()));
			
			/* 20140829 aggiunta Disc */
			output.setObbtemp(new StringType(ht.get("OBBTEMP").toString()));
			output.setSitfina(new StringType(ht.get("SITFINA").toString()));
			output.setObbinve(new StringType(ht.get("OBBINVE").toString()));
			output.setEspfina(new StringType(ht.get("ESPFINA").toString()));
			/* 20140829 aggiunta Disc */

			output.setDesCluster(new StringType("    "));
						
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER1"))
	 		{
	 			output.setDesCluster(new StringType("Intraprendente Lungo"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER2"))
	 		{
	 			output.setDesCluster(new StringType("Intraprendente Medio"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER3"))
	 		{
	 			output.setDesCluster(new StringType("Intraprendente Breve"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER4"))
	 		{
	 			output.setDesCluster(new StringType("Equilibrato Lungo"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER5"))
	 		{
	 			output.setDesCluster(new StringType("Equilibrato Medio"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER6"))
	 		{
	 			output.setDesCluster(new StringType("Equilibrato Breve"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER7"))
	 		{
	 			output.setDesCluster(new StringType("Conservatore Lungo"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER8"))
	 		{
	 			output.setDesCluster(new StringType("Conservatore Medio"));
	 		}
	 		if (ht.get("CLUSTER").toString().equalsIgnoreCase("CLUSTER9"))
	 		{
	 			output.setDesCluster(new StringType("Conservatore Breve"));
	 		}

/*v12*/		output.setDesOrigClu(new StringType("    "));
			
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER1"))
/*v12*/		{
/*v12*/ 		output.setDesOrigClu(new StringType("Intraprendente Lungo"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER2"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Intraprendente Medio"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER3"))
/*v12*/ 	{
/*v12*/			output.setDesOrigClu(new StringType("Intraprendente Breve"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER4"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Equilibrato Lungo"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER5"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Equilibrato Medio"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER6"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Equilibrato Breve"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER7"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Conservatore Lungo"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER8"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Conservatore Medio"));
/*v12*/		}
/*v12*/		if (ht.get("ORIGCLU").toString().equalsIgnoreCase("CLUSTER9"))
/*v12*/		{
/*v12*/			output.setDesOrigClu(new StringType("Conservatore Breve"));
/*v12*/		}
	 		
			output.setPunteggi(punteggi);
			output.setRisposte(input.getRisposte());
	        Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}

	public static OutputGetElencoQuestionariModel getElencoQuestionari (ClientSessionContext csc, InputGetElencoQuestionariModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputGetElencoQuestionariModel output = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		//Inizializzazione variabili
		try
		{
			output = new OutputGetElencoQuestionariModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"     */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
			cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
			{
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
/*       "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp(null);
			}catch (Exception e){}
			if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
			  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				esito.esito = "004";
				esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp() == null) && (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();

			if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
				queryResult = dao.executeQueryAccess("GetQuestionarioRete",input);
			else
				queryResult = dao.executeQueryAccess("GetQuestionario",input);
	
			if (queryResult.getResult().size() == 0)
			{
				output.setSeCompi(new StringType("N"));
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Compilato " + output.getSeCompi().getStringValue());
			} 
			else
			{
				output.setSeCompi(new StringType("S"));				
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Compilato " + output.getSeCompi().getStringValue());
/*
 			queryResult = dao.executeQueryAccess("SeQuestionarioCompilato",input);
			
			if (queryResult.getResult().size() == 0)
			{
				output.setSeCompi(new StringType("N"));				
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Compilato " + output.getSeCompi().getStringValue());
			} 
			else 
			{
				output = (OutputGetElencoQuestionariModel)queryResult.getResult().get(0);
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Compilato " + output.getSeCompi().getStringValue());
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
*/			
				queryResult = dao.executeQueryAccess("ElencoStorico",input);
				if (queryResult.getResult().size() == 0)
				{
					esito.esito = "009";
					esito.setDescr("Impossibile reperire lo storico");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Lettura Storico");
				
				if (queryResult.getResult().size() == 0)
					output.setStorico(new ListType());
				else
					output.setStorico(queryResult.getResult());
			}
	        Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}
	
	public static OutputGetQuestionarioModel getQuestionario (ClientSessionContext csc, InputGetQuestionarioModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		}
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputGetQuestionarioModel output = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		}
		//Inizializzazione variabili
		try
		{
			output = new OutputGetQuestionarioModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
/* "CEDACRI"    */
			if (internalCall.booleanValue() == false)
			{
				if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
					cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
/*      "CEDACRI" */
		//Conversione classe input
		try
		{
			if (internalCall.booleanValue() == false)
			{
				try 
				{ 
					if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgDoss(null);
				}catch (Exception e){}
				try 
				{ 
					if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgTemp(null);
				}catch (Exception e){}
				if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
				  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
				{
					esito.esito = "004";
					esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			else
			{
				input.setNdgDoss(null);
			} 				
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			else
			{
				input.setNdgTemp(null);
			}
				
			if (!((input.getNdgDoss() == null) && (input.getNdgTemp() == null)))
			{
				if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getNdgTemp() == null) && (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		DAOQueryResultModel risposteResult = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		}
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();
			if (!((input.getNdgDoss() == null) && (input.getNdgTemp() == null)))
			{
				if (input.getUserKey().getStringValue().trim().equalsIgnoreCase(""))
					if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
						queryResult = dao.executeQueryAccess("GetQuestionarioRete",input);
					else
						queryResult = dao.executeQueryAccess("GetQuestionario",input);
				else
				{
					UserKeyModel ukModel = new UserKeyModel();
					ukModel.setNdgDoss(input.getNdgDoss());
					ukModel.setNdgTemp(input.getNdgTemp());
					ukModel.setUserKey(AdeguatezzaUtility.convertTimestampString(input.getUserKey().getStringValue()));

					queryResult = dao.executeQueryAccess("GetQuestionarioUserKey",ukModel);
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Lettura Questionario");
			
				if (queryResult.getResult().size() > 0)
				{
					TW00TBSKModel sk = (TW00TBSKModel)queryResult.getResult().get(0);
					output.setProfilo(sk.getProfilo());
					output.setDatComp(AdeguatezzaUtility.convertStringDate(sk.getDatsche()));
					output.setOraComp(AdeguatezzaUtility.convertStringTime(sk.getDatsche()));
					output.setRelease(sk.getRelease());
					output.setSeCompi(new StringType("S"));
					output.setSeBanca(sk.getFlskcli());
//20111002:seValid
					output.setSeValid(sk.getSevalid());
					
					output.setCluster(sk.getCluster());	
					
/*v12*/				output.setEsigLiq(sk.getEsigliq());
/*v12*/				output.setForzObT(sk.getForzobt());
/*v12*/				output.setOrigObT(sk.getOrigobt());
/*v12*/				output.setOrigClu(sk.getOrigclu());

					output.setDfinval(sk.getDfinval());
					
					/* 20140829 aggiunta Disc */
					output.setObbtemp(sk.getObbtemp());
					output.setSitfina(sk.getSitfina());
					output.setObbinve(sk.getObbinve());
					output.setEspfina(sk.getEspfina());
					/* 20140829 aggiunta Disc */
					
					
					output.setDesCluster(new StringType("    "));
								
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER1"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER2"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER3"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Breve"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER4"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER5"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER6"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Breve"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER7"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase( "CLUSTER8"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER9"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Breve"));
			 		}

/*v12*/				output.setDesOrigClu(new StringType("    "));
					
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER1"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER2"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER3"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Breve"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER4"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER5"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER6"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Breve"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER7"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase( "CLUSTER8"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER9"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Breve"));
/*v12*/		 		}

//					if (input.getUserKey().getStringValue().trim().equalsIgnoreCase(""))
						risposteResult = dao.executeQueryAccess("GetQuestionarioRisposte",sk);
//					else
//						risposteResult = dao.executeQueryAccess("GetQuestionarioRisposteUserKey",input);
					Logger.getInstance().info(nomeMetodo.getStringValue() + ": Lettura Risposte Questionario");
				} 
				else 
				{
					output.setDatComp(new StringType());
					output.setOraComp(new StringType());
					output.setProfilo(new StringType());
					output.setRelease(new IntegerType());
					output.setSeCompi(new StringType("N"));
					output.setSeBanca(new StringType());
					output.setCluster(new StringType());
/*v12*/				output.setEsigLiq(new StringType());					
/*v12*/				output.setForzObT(new StringType());
/*v12*/				output.setOrigObT(new StringType());
/*v12*/				output.setOrigClu(new StringType());
//20111002:seValid
					output.setSeValid(new StringType());
					
					/* 20140829 aggiunta Disc */
					output.setObbtemp(new StringType());
					output.setSitfina(new StringType());
					output.setObbinve(new StringType());
					output.setEspfina(new StringType());
					/* 20140829 aggiunta Disc */
				}
			}
			else
			{
				output.setDatComp(new StringType());
				output.setOraComp(new StringType());
				output.setProfilo(new StringType());
				output.setRelease(new IntegerType());
				output.setSeCompi(new StringType("N"));				
				output.setSeBanca(new StringType());
				output.setCluster(new StringType());
/*v12*/			output.setEsigLiq(new StringType());					
/*v12*/			output.setForzObT(new StringType());
/*v12*/			output.setOrigObT(new StringType());
/*v12*/			output.setOrigClu(new StringType());

//20111002:seValid
				output.setSeValid(new StringType());
				/* 20140829 aggiunta Disc */
				output.setObbtemp(new StringType());
				output.setSitfina(new StringType());
				output.setObbinve(new StringType());
				output.setEspfina(new StringType());
				/* 20140829 aggiunta Disc */
			}				

/*20111025:PG
			ListType elencoDomande = cacheManager.getElencoDomande();
20111025:PG*/

//20111025:PG
			ListType elencoDomande = cacheManager.getElencoDomande();
			if (input.getPG().getStringValue().equalsIgnoreCase("PG"))
			{
				elencoDomande = cacheManager.getElencoDomandePG();
			}
			
			if (elencoDomande.size() == 0) 
			{
				esito.esito = "003";
				esito.setDescr("Tabella domande non valorizzata");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}

/*121009
 			String wsRelease = null;
 			String wsChiave  = null;
 			IntegerType wsReleaseI = new IntegerType();
 			if (AdeguatezzaBusinessUtility.ReleaseCorrente(cacheManager,dao,wsReleaseI,esito,input.getPG()) == false)
 			{
 				esito.esito = "033";
 			    esito.setDescr("Release non determinata");
 				output.setEsito(new StringType(esito.esito));
 				output.setDescErr(new StringType(esito.descrizione));
 				return output;
 			}
 			else
 			{
 				wsRelease = AdeguatezzaUtility.padLeft(wsReleaseI.getStringValue(),'0',3);
 			}
 			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Versione Questionario " + wsRelease);
 *121009*/

			int numeroDomanda = 0;
			int numeroRisposta = 0;
//questionario importo
			int rispostaImporto = 0;
			String risposta = ""; 
			ListType questionario = new ListType();
			ElementoQuestionarioModel qModelIngresso = null;
			ElementoQuestionarioModel qModelUscita = null;
			RisposteCompilateModel rModel = null;
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Composizione Domande/Risposte");
			for(int i=0;i<elencoDomande.size();i++)
			{
				qModelIngresso = (ElementoQuestionarioModel)elencoDomande.get(i);
				qModelUscita = new ElementoQuestionarioModel();
				numeroDomanda = qModelIngresso.getNumElem().intValue();
//questionario importo
				if (qModelIngresso.getTipElem().getStringValue().trim().equalsIgnoreCase("I"))
				{
					rispostaImporto = numeroDomanda;
				}
				else
				{
					if  (!(rispostaImporto == numeroDomanda))
					{
						rispostaImporto = 0;
					}
				}
				qModelUscita.setNumElem(qModelIngresso.getNumElem());
				qModelUscita.setNumSele(qModelIngresso.getNumSele());
				qModelUscita.setTestEle(qModelIngresso.getTestEle());
				qModelUscita.setTipElem(qModelIngresso.getTipElem());
				if (!qModelIngresso.getTipElem().getStringValue().trim().equalsIgnoreCase("R"))
					qModelUscita.setSelSele(new StringType());
				else
				{
					if ((risposteResult == null) || (risposteResult.getResult().size() == 0))
					{
						qModelUscita.setSelSele(qModelIngresso.getSelSele());
					}
					else
					{
						for (int j=0;j<risposteResult.getResult().size();j++)
						{
							rModel = (RisposteCompilateModel)risposteResult.getResult().get(j);
							risposta = rModel.getSelRisp().getStringValue();
							numeroRisposta = rModel.getNumDoma().intValue();

							if ((numeroRisposta == 0) && (risposta.trim().equalsIgnoreCase("0")))
							{
								qModelUscita.setSelSele(qModelIngresso.getSelSele());
								break;
							}
//questionario importo
							if (numeroDomanda == rispostaImporto)
							{
								if (numeroDomanda == numeroRisposta)
								{
									qModelUscita.setSelSele(new StringType(risposta));
									break;
								}
							}
							if (numeroDomanda == numeroRisposta)
							{
								qModelUscita.setSelSele(new StringType(String.valueOf(risposta.charAt(qModelIngresso.getNumSele().intValue()-1))));
								break;
							}
							if (numeroRisposta == 0)
							{
								Logger.getInstance().info(numeroDomanda + ": NUMERO DOMANDA ");

								
/*v14*/							if (numeroDomanda == 20)			
/*v14*/							{
/*v14 121121						qModelUscita.setSelSele(new StringType("N")); */
/*v14 121121						break; */	
/*121121*/							int lung_risposta = risposta.length(); 
/*121121*/							if (!(lung_risposta > (numeroDomanda-1)))
/*121121*/							{
/*121121*/								qModelUscita.setSelSele(new StringType("N"));
/*121121*/								break;
/*121121*/							}
/*v14*/							}
/*v14*/							if (numeroDomanda == 21)
/*v14*/							{
/*v14 121121						qModelUscita.setSelSele(new StringType("N")); */
/*v14 121121						break; */
/*121121*/							int lung_risposta = risposta.length(); 
/*121121*/							if (!(lung_risposta > (numeroDomanda-1)))
/*121121*/							{
/*121121*/								qModelUscita.setSelSele(new StringType("N"));
/*121121*/								break;
/*121121*/							}
/*v14*/							}
 
/* 20110725 compatibilità questionario importo */
/* 20110725 */					if (numeroDomanda == rispostaImporto)
/* 20110725 */					{
/* 20110725 */						int lung_risposta = risposta.length(); 
/* 20110725 */						if (!(lung_risposta > (numeroDomanda-1)))
/* 20110725 */						{
/* 20110725 */							qModelUscita.setSelSele(new StringType("0"));
/* 20110725 */							break;
/* 20110725 */						}
/* 20110725 */					}

/*121009
        						IntegerType wsNumDomaI = new IntegerType(numeroDomanda);
        						wsChiave = wsRelease + AdeguatezzaUtility.padLeft(wsNumDomaI.getStringValue(),'0',2);
        						String wsFacoltativa = null;
        						wsFacoltativa = "N";
        						TW00TBTZModel wsAppoggi = null;
        						for (int jj = 0; jj < cacheManager.getElenco869().size(); jj++)
        						{
        							wsAppoggi = (TW00TBTZModel)cacheManager.getElenco869().get(jj); 
        							if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
        							{
        							wsFacoltativa = wsAppoggi.getDati().getStringValue();
        							break;
        							}
        						}
        						if (wsFacoltativa.trim().equalsIgnoreCase("S"))
        						{
        							qModelUscita.setSelSele(new StringType("N"));
									break;
								}						
 *121009*/
								if (String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase(String.valueOf(qModelIngresso.getNumSele().intValue()))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("10") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("a"))
								    ||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("11") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("b"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("12") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("c"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("13") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("d"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("14") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("e"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("15") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("f"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("16") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("g"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("17") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("h"))
								    ||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("18") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("i"))
									||(String.valueOf(qModelIngresso.getNumSele().intValue()).equalsIgnoreCase("19") && 
									 String.valueOf(risposta.charAt(numeroDomanda-1)).equalsIgnoreCase("l")))
										qModelUscita.setSelSele(new StringType("S"));
								else
									qModelUscita.setSelSele(new StringType("N"));
								break;
							}
						}
					}
				}
				questionario.add(qModelUscita);								
			}
			output.setQuestionario(questionario);
			if (internalCall.booleanValue() == false)
			{
				Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
			}
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}
	
	public static OutputSalvaQuestionarioModel salvaQuestionario (ClientSessionContext csc, InputSalvaQuestionarioModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		String ndgTempInserimento = new String();
        Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputSalvaQuestionarioModel output = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		//Inizializzazione variabili
		try
		{
			ndgTempInserimento = input.getNdgTemp().getStringValue();
			output = new OutputSalvaQuestionarioModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			output.setDfinval(new IntegerType("0"));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			output.setDfinval(new IntegerType("0"));
			return output;
		}
		output.setDfinval(new IntegerType("0"));
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"      */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
/*      "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp(null);
			}catch (Exception e){}
			if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
			  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				esito.esito = "004";
				esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (input.getNdgTemp() == null)
			{
				if (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
			  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				esito.esito = "007";
				esito.setDescr("Campo NdgDoss/NdgTemp non validi");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}

			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getBozza().getStringValue() == null) || (input.getBozza().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Bozza non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendFiliale().getStringValue()))
			{
				if ((input.getProfilo().getStringValue() == null) || (input.getProfilo().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo Profilo non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if ((input.getRisposte() == null) || (input.getRisposte().size() == 0))
			{
				esito.esito = "004";
				esito.setDescr("Campo risposte non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (input.getDataOraComp().isNull())
			{
				esito.esito = "004";
				esito.setDescr("Campo DataOraComp non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}			
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();
			IntegerType release = new IntegerType();
			if (AdeguatezzaBusinessUtility.ReleaseCorrente(cacheManager,dao,release,esito,input.getPG()) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Versione " + release.getStringValue());
			
			Hashtable ht = new Hashtable();
			ListType punteggi = new ListType();
			StringType schedal = null;
			StringType flSkCli = null;

			if (AdeguatezzaBusinessUtility.VerificaQuestionarioNonRisposto(input.getRisposte()) == true)
			{
				schedal = new StringType("S");
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Non Risposto " + schedal.getStringValue());
				if (input.getEta().bigValue() == null)
				{
					esito.esito = "014";
					esito.setDescr("Campo Eta non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getTitStud().getStringValue() == null) || (input.getTitStud().equalsIgnoreCase("")))
				{
					esito.esito = "013";
					esito.setDescr("Campo Titolo di Studio non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				input.setTitStud(new StringType(AdeguatezzaUtility.padLeft(input.getTitStud().getStringValue(),'0',3)));
				flSkCli = new StringType("B");
				//Calcolo profilo default
				if (AdeguatezzaBusinessUtility.CalcoloProfiloDefault(cacheManager,dao,ht,input.getEta(),input.getTitStud(),esito) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Profilo di Default " + ht.get("PROFILO").toString() + ": Cluster di Default " + ht.get("CLUSTER").toString());
			}
			else
			{
				schedal = new StringType("N");
				flSkCli = new StringType("C");
				if (AdeguatezzaBusinessUtility.VerificaQuestionario(cacheManager,input.getRisposte(),esito,input.getPG()) == false)
				{
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					output.setNumElem(new IntegerType(esito.numElem));
					Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Incompleto");
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Questionario Completo");
			
				//Calcolo profilo
				if (AdeguatezzaBusinessUtility.CalcoloProfilo(cacheManager,dao,input.getRisposte(),punteggi,ht,input.getEta(),esito,input.getPG()) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Profilo Calcolato " + ht.get("PROFILO").toString() + ": Cluster Calcolato " + ht.get("CLUSTER").toString());
			}

			StringType profilo = new StringType(ht.get("PROFILO").toString());
			StringType obbInve = new StringType(ht.get("OBBINVE").toString());
			StringType obbTemp = new StringType(ht.get("OBBTEMP").toString());
			StringType sitFina = new StringType(ht.get("SITFINA").toString());		
			StringType cluster = new StringType(ht.get("CLUSTER").toString());
/*v12*/		StringType esigLiq = new StringType(ht.get("ESIGLIQ").toString());			
/*v12*/		StringType forzObt = new StringType(ht.get("FORZOBT").toString());
/*v12*/		StringType origObt = new StringType(ht.get("ORIGOBT").toString());
/*v12*/		StringType origClu = new StringType(ht.get("ORIGCLU").toString());
			/* 20140829 aggiunta Disc */
			StringType espFina = new StringType(ht.get("ESPFINA").toString());
			/* 20140829 aggiunta Disc */
			
			if (!input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendFiliale().getStringValue()))
			{
				if (!input.getProfilo().equalsIgnoreCase(profilo.getStringValue()))
				{
					esito.esito = "103";
					esito.setDescr("Il profilo inserito non e' congruente con le risposte fornite");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;				
				}
			}
			
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Preparazione dati per inserimento");
			//Calcolo risposte
			StringType selRisp = new StringType();
			StringType[] selRispMult = new StringType[255];
			for(int i=0;i<255;i++) selRispMult[i] = new StringType();
//questionario importo
			Integer[] selRispImpo = new Integer[255];
			for(int i=0;i<255;i++) selRispImpo[i] = null;
			if (AdeguatezzaBusinessUtility.ComponiRisposte(cacheManager,dao,input.getRisposte(),selRisp,selRispMult,selRispImpo,esito,input.getPG()) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((selRisp == null) || (selRisp.equalsIgnoreCase("")))
			{
				esito.esito = "005";
				esito.setDescr("Risposte non compatibili con la logica del componente");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}
			if (selRispMult == null)
			{
				esito.esito = "005";
				esito.setDescr("Risposte non compatibili con la logica del componente");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}
			
			//Logica di canale
			TimestampType datComp = input.getDataOraComp();
			IntegerType datFinv = cacheManager.getDfinval();

			if (input.getBozza().equalsIgnoreCase("S"))
			{
				datFinv = cacheManager.getDatBozz();
			}
			StringType seValid = cacheManager.getSeValidDef();
			if (input.getCanVend().equals(cacheManager.getCanVendRete()) || input.getBozza().equalsIgnoreCase("S"))
			{
				seValid = new StringType(" ");
			}
			
			//Cancellazione bozze
			InputCancellaQuestionarioModel inputCancellaModel = new InputCancellaQuestionarioModel();
			inputCancellaModel.setCanVend (input.getCanVend());
			inputCancellaModel.setNdgDoss (input.getNdgDoss());
			inputCancellaModel.setNdgTemp (input.getNdgTemp());
			inputCancellaModel.setCountry (input.getCountry());
			inputCancellaModel.setUsername(input.getUsername());
			OutputCancellaQuestionarioModel outputCancellaModel = cancellaQuestionario(csc,inputCancellaModel,nomeMetodo,AdeguatezzaManagerImpl.trueType);
			if (!outputCancellaModel.getEsito().equalsIgnoreCase("000"))
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}
			
//			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Allineamento chiave primaria");
			//Salvataggio dati
			TW00TBQKModel qk = new TW00TBQKModel(); 
			TW00TBQMModel qm = new TW00TBQMModel();
			TW00TBSKModel sk = new TW00TBSKModel();
			DAOTableResultModel tableResult = null;
/*
			if (!input.getBozza().equalsIgnoreCase("S"))
			{
				if (AdeguatezzaBusinessUtility.PreparaCancellazione(qk,qm,sk,
					input.getNdgDoss(),input.getNdgTemp(),null,cacheManager.getCodIsti(),
					cacheManager.getCodAppl(),datComp)==false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;				
				}
				if (input.getNdgDoss() == null)
				{
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioQKTemp",qk); }catch(DAOException die){}
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioQMTemp",qm); }catch(DAOException die){}
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioSKTemp",sk); }catch(DAOException die){}
				}else{
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioQK",qk); }catch(DAOException die){}
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioQM",qm); }catch(DAOException die){}
					try{ dao.executeTableUpdateAccess("UpdateQuestionarioSK",sk); }catch(DAOException die){}					
				}
			}
*/
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inserimento");
            
			input.setNdgTemp(new StringType(ndgTempInserimento));
			
//	Determinazione della data di scadenza della scheda			
			String wsGesClus = "N";
			String wsChiave  = null;
			String wsRelease = null;
			String wsCluster = null;
			TW00TBTZModel wsAppoggi = null;
			int wsDatFinv;
			String wsDatComp = null;
			String wsDurataS = null;
			char wsDurataC;
			int wsDurataI ;			
			
			IntegerType wsReleaseI = new IntegerType();
			
			if (AdeguatezzaBusinessUtility.ReleaseCorrente(cacheManager,dao,wsReleaseI,esito,input.getPG()) == false)
			{
				esito.esito = "033";
				esito.setDescr("Release non determinata");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}
			else
			{
				wsRelease = AdeguatezzaUtility.padLeft(wsReleaseI.getStringValue(),'0',3);
			}
			
			wsChiave = wsRelease; 
//	Lettura tabella per determinare se è attiva la gestione della data scadenza
			for (int j = 0; j < cacheManager.getElenco869().size(); j++)
			{
				wsAppoggi = (TW00TBTZModel)cacheManager.getElenco869().get(j); 
				if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
				{
					wsGesClus = wsAppoggi.getDati().getStringValue();
					wsDurataC = wsGesClus.charAt(1);
					wsDurataS = String.valueOf(wsDurataC); 
					wsDurataI = Integer.parseInt(wsDurataS);
					wsDatComp = datComp.getAA() + datComp.getMM() + datComp.getGG();
					wsDatFinv = Integer.parseInt(wsDatComp) + (wsDurataI * 10000);
					Integer wsDatFinvI = new Integer(wsDatFinv);					
					datFinv = new IntegerType(wsDatFinvI);
					break;
				}
			}		
			// 20141203 gestione anomalia espfina nullo
			output.setDfinval(datFinv);
			if (AdeguatezzaBusinessUtility.PreparaInserimento(qk,qm,sk,
				input.getNdgDoss(),input.getNdgTemp(),input.getCanVend(),cacheManager.getCodIsti(),
				cacheManager.getCodAppl(),datFinv,datComp,seValid,cacheManager.getFilOperDef(),
				release,selRisp,schedal,profilo,profilo,espFina,obbInve,
				obbTemp,sitFina,flSkCli,input.getNumSched(),cluster,
				esigLiq,forzObt,origObt,origClu)==false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}

			boolean inserisci = true;
			boolean modifica = false;
			
			// Per la filiale, in caso di presenza del questionario si può decidere per l'aggiornamento
			if (input.getCanVend().equals(cacheManager.getCanVendFiliale()))
			{
				UserKeyModel ukModel = new UserKeyModel();
				ukModel.setNdgDoss(input.getNdgDoss());
				ukModel.setNdgTemp(input.getNdgTemp());
				ukModel.setUserKey(input.getDataOraComp());

				queryResult = dao.executeQueryAccess("GetQuestionarioUserKey",ukModel);
		
				if (queryResult.getResult().size() > 0)
				{
					inserisci = false;
					modifica = true;
				}
			}
						
			if (inserisci)
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Attivata modalità inserimento");
			if (modifica)
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Attivata modalità modifica");

			try
			{
				if (inserisci)
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioSK",sk);
				if (modifica)
					tableResult = dao.executeTableUpdateAccess("InsertQuestionarioSK",sk);
			}
			catch (PrimaryKeyViolation pkv)
			{
				StringWriter sw = new StringWriter();
				pkv.printStackTrace(new PrintWriter(sw));
				esito.esito = "106";
				esito.setDescr("<![CDATA[" + "Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
				Logger.getInstance().error(pkv);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;								
			}
			if (tableResult.getResult().intValue() != 1)
			{
				esito.esito = "006";
				esito.setDescr("Inserimento non riuscito nella tabella SK");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;								
			}
			try
			{
				if (inserisci)
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQK",qk);
				if (modifica)
					tableResult = dao.executeTableUpdateAccess("InsertQuestionarioQK",qk);
			}
			catch (PrimaryKeyViolation pkv)
			{
				StringWriter sw = new StringWriter();
				pkv.printStackTrace(new PrintWriter(sw));
				esito.esito = "106";
				esito.setDescr("<![CDATA[" + "Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
				Logger.getInstance().error(pkv);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;								
			}
			if (tableResult.getResult().intValue() != 1)
			{
				esito.esito = "006";
				esito.setDescr("Inserimento non riuscito nella tabella QK");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;								
			}
			for(int i=0; i<selRisp.getStringValue().length();i++)
			{
				if (selRisp.getStringValue().charAt(i) == 'M')
				{
					if (selRispMult[i+1].getStringValue().trim().equalsIgnoreCase(""))
					{
						esito.esito = "105";
						esito.numElem = i+1;
						esito.setDescr("Risposta alla domanda indicata (" + esito.numElem + ") non fornita");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						output.setNumElem(new IntegerType(esito.numElem));
						return output;								
					}
					else
					{
						qm.setNumdoma(new IntegerType(i+1));
						qm.setSelmult(new StringType(selRispMult[i+1].getStringValue().trim()));
						if (inserisci)
							tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
						if (modifica)
							tableResult = dao.executeTableUpdateAccess("InsertQuestionarioQM",qm);
						if (tableResult.getResult().intValue() != 1)
						{
							esito.esito = "006";
							esito.setDescr("Inserimento non riuscito nella tabella QM");
							Logger.getInstance().debug(esito.descrizione);
							output.setEsito(new StringType(esito.esito));
							output.setDescErr(new StringType(esito.descrizione));
							return output;								
						}
					}
				}
//questionario importi
				if ((selRisp.getStringValue().charAt(i) == 'I')
					&& !(selRispImpo == null)) 
				{
					if (!(selRispImpo[i+1] == null))
					{
						qm.setNumdoma(new IntegerType(i+1));
						String impoString = null;
						impoString = AdeguatezzaUtility.padLeft(selRispImpo[i+1].toString(),'0',10);
						qm.setSelmult(new StringType(impoString));
						if (inserisci)
							tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
						if (modifica)
							tableResult = dao.executeTableUpdateAccess("InsertQuestionarioQM",qm);
						if (tableResult.getResult().intValue() != 1)
						{
							esito.esito = "006";
							esito.setDescr("Inserimento non riuscito nella tabella QM");
							Logger.getInstance().debug(esito.descrizione);
							output.setEsito(new StringType(esito.esito));
							output.setDescErr(new StringType(esito.descrizione));
							return output;								
						}
					}
				}
			}
						
            Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");		
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA[" + "Errore SQL " + sw.toString() + "]]>");
			Logger.getInstance().error(daoe); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}
	
	public static OutputCancellaQuestionarioModel cancellaQuestionario (ClientSessionContext csc, InputCancellaQuestionarioModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		}
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputCancellaQuestionarioModel output = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		}
		//Inizializzazione variabili
		try
		{
			output = new OutputCancellaQuestionarioModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"     */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
/*      "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp(null);
			}catch (Exception e){}
			if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
			  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				esito.esito = "004";
				esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp() == null) && (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		}
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();

			//Cancellazione dati
			TW00TBQKModel qk = new TW00TBQKModel();
			TW00TBQMModel qm = new TW00TBQMModel();
			TW00TBSKModel sk = new TW00TBSKModel();
			
			if (AdeguatezzaBusinessUtility.PreparaCancellazione(qk,qm,sk,
				input.getNdgDoss(),input.getNdgTemp(),input.getCanVend(),cacheManager.getCodIsti(),
				cacheManager.getCodAppl(),cacheManager.getDatBozz())==false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;				
			}
			if (input.getNdgDoss() == null)
			{
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioQKTemp",qk); }catch (NoRowsAffected nra){}
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioQMTemp",qm); }catch (NoRowsAffected nra){}
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioSKTemp",sk); }catch (NoRowsAffected nra){}
			}else{
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioQK",qk); }catch (NoRowsAffected nra){}
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioQM",qm); }catch (NoRowsAffected nra){}
				try{ dao.executeTableDeleteAccess("DeleteQuestionarioSK",sk); }catch (NoRowsAffected nra){}
			}
			if (internalCall.booleanValue() == false)
			{
				Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
			}
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL  " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}	

	public static OutputCtrlAdeguatezzaModel ctrlAdeguatezza (ClientSessionContext csc, InputCtrlAdeguatezzaModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
        Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputCtrlAdeguatezzaModel output = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		//Inizializzazione variabili
		try
		{
			output = new OutputCtrlAdeguatezzaModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		 }catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"     */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
/*       "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			//Almeno un ndg è obbligatorio
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss1().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss1(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp1().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp1(null);
			}catch (Exception e){}
			if (((input.getNdgDoss1() == null) || (input.getNdgDoss1().getStringValue() == null) || (input.getNdgDoss1().equalsIgnoreCase("")))
			  && ((input.getNdgTemp1() == null) || (input.getNdgTemp1().getStringValue() == null) || (input.getNdgTemp1().equalsIgnoreCase(""))))
			{
				esito.esito = "004";
				esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (!((input.getNdgDoss1() == null) || (input.getNdgDoss1().getStringValue() == null) || (input.getNdgDoss1().equalsIgnoreCase(""))))
			{
				input.setNdgTemp1(null);
			}
			if (!((input.getNdgTemp1() == null) || (input.getNdgTemp1().getStringValue() == null) || (input.getNdgTemp1().equalsIgnoreCase(""))))
			{
				input.setNdgDoss1(null);
			} 
			if ((input.getNdgDoss1() == null) && (input.getNdgTemp1().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp1() == null) && (input.getNdgDoss1().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			// Secondo NDG facoltativo
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss2().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss2(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp2().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp2(null);
			}catch (Exception e){}
			if ((input.getNdgDoss2() == null) || (input.getNdgDoss2().getStringValue() == null) || (input.getNdgDoss2().equalsIgnoreCase("")))
			{
				input.setNdgDoss2(null);
			}
			if ((input.getNdgTemp2() == null) || (input.getNdgTemp2().getStringValue() == null) || (input.getNdgTemp2().equalsIgnoreCase("")))
			{
				input.setNdgTemp2(null);
			}
			if (!((input.getNdgDoss2() == null) || (input.getNdgDoss2().getStringValue() == null) || (input.getNdgDoss2().equalsIgnoreCase(""))))
			{
				input.setNdgTemp2(null);
			}
			if (!((input.getNdgTemp2() == null) || (input.getNdgTemp2().getStringValue() == null) || (input.getNdgTemp2().equalsIgnoreCase(""))))
			{
				input.setNdgDoss2(null);
			} 
			if ((input.getNdgDoss2() == null) && (input.getNdgTemp2() != null) && (input.getNdgTemp2().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp2() == null) && (input.getNdgDoss2() != null) && (input.getNdgDoss2().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			//Terzo NDG facoltativo
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss3().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss3(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp3().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp3(null);
			}catch (Exception e){}
			if ((input.getNdgDoss3() == null) || (input.getNdgDoss3().getStringValue() == null) || (input.getNdgDoss3().equalsIgnoreCase("")))
			{
				input.setNdgDoss3(null);
			}
			if ((input.getNdgTemp3() == null) || (input.getNdgTemp3().getStringValue() == null) || (input.getNdgTemp3().equalsIgnoreCase("")))
			{
				input.setNdgTemp3(null);
			}
			if (!((input.getNdgDoss3() == null) || (input.getNdgDoss3().getStringValue() == null) || (input.getNdgDoss3().equalsIgnoreCase(""))))
			{
				input.setNdgTemp3(null);
			}
			if (!((input.getNdgTemp3() == null) || (input.getNdgTemp3().getStringValue() == null) || (input.getNdgTemp3().equalsIgnoreCase(""))))
			{
				input.setNdgDoss3(null);
			} 
			if ((input.getNdgDoss3() == null) && (input.getNdgTemp3() != null) && (input.getNdgTemp3().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp3() == null) && (input.getNdgDoss3() != null) && (input.getNdgDoss3().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			//Quarto NDG facoltativo
			try 
			{ 
				if (Long.parseLong(input.getNdgDoss4().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgDoss4(null);
			}catch (Exception e){}
			try 
			{ 
				if (Long.parseLong(input.getNdgTemp4().getStringValue().trim().replaceAll(" ","")) == 0)
					input.setNdgTemp4(null);
			}catch (Exception e){}
			if ((input.getNdgDoss4() == null) || (input.getNdgDoss4().getStringValue() == null) || (input.getNdgDoss4().equalsIgnoreCase("")))
			{
				input.setNdgDoss4(null);
			}
			if ((input.getNdgTemp4() == null) || (input.getNdgTemp4().getStringValue() == null) || (input.getNdgTemp4().equalsIgnoreCase("")))
			{
				input.setNdgTemp4(null);
			}
			if (!((input.getNdgDoss4() == null) || (input.getNdgDoss4().getStringValue() == null) || (input.getNdgDoss4().equalsIgnoreCase(""))))
			{
				input.setNdgTemp4(null);
			}
			if (!((input.getNdgTemp4() == null) || (input.getNdgTemp4().getStringValue() == null) || (input.getNdgTemp4().equalsIgnoreCase(""))))
			{
				input.setNdgDoss4(null);
			} 
			if ((input.getNdgDoss4() == null) && (input.getNdgTemp4() != null) && (input.getNdgTemp4().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getNdgTemp4() == null) && (input.getNdgDoss4() != null) && (input.getNdgDoss4().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
			{
				esito.esito = "007";
				esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getSegnOrd().getStringValue() == null) || (input.getSegnOrd().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo SegnOrd non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			// TipStruStr valori ammessi:
			// P=Polizze
			// F=Fondi
			// T=Titoli
			// G=Gestioni
			// C=Collocamento
			// X=PCT
			// I=Illiquidi non Titoli
			// Z=Titoli Illiquidi
			if ((input.getTipStruStr().getStringValue() == null) || (input.getTipStruStr().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo TipStruStr non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}

			if ((input.getTipStruStr().getStringValue() == "C") || (input.getTipStruStr().getStringValue() == "I"))
			{
				if ((input.getPosDisiCli().getStringValue() == null) || (input.getPosDisiCli().equalsIgnoreCase("")))
				{	
				esito.esito = "004";
				esito.setDescr("Campo PosDisiCli non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
				}				
			}
			//Controllo del valore ammesso dai parametri
			if (!((input.getSegnOrd() == null) || (input.getSegnOrd().getStringValue() == null) || (input.getSegnOrd().equalsIgnoreCase(""))))
			{
				if (input.getSegnOrd().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo SegnOrd");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("IASPCAV".indexOf(input.getSegnOrd().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo SegnOrd");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getCanVend() == null) || (input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase(""))))
			{
				if (input.getCanVend().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo CanVend");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("PFIC".indexOf(input.getCanVend().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo CanVend");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getTipStruStr() == null) || (input.getTipStruStr().getStringValue() == null) || (input.getTipStruStr().equalsIgnoreCase(""))))
			{
				if (input.getTipStruStr().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo TipStruStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("PFTGCXIZ".indexOf(input.getTipStruStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo TipStruStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("I") || input.getTipStruStr().equalsIgnoreCase("X") || input.getTipStruStr().equalsIgnoreCase("Z"))
				{
					if (!(input.getSegnOrd().equalsIgnoreCase("A") || input.getSegnOrd().equalsIgnoreCase("V")))
					{
						esito.esito = "012";
						esito.setDescr("Valore non ammesso per il campo SegnOrd");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
			}
			if (!((input.getObbInveStr() == null) || (input.getObbInveStr().getStringValue() == null) || (input.getObbInveStr().equalsIgnoreCase(""))))
			{
				if (input.getObbInveStr().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ObbInveStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("IPRNS".indexOf(input.getObbInveStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ObbInveStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getRiscStrStr() == null) || (input.getRiscStrStr().getStringValue() == null) || (input.getRiscStrStr().equalsIgnoreCase(""))))
			{
				if (input.getRiscStrStr().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo RiscStrStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("AMB".indexOf(input.getRiscStrStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo RiscStrStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getObbTempStr() == null) || (input.getObbTempStr().getStringValue() == null) || (input.getObbTempStr().equalsIgnoreCase(""))))
			{
				if (input.getObbTempStr().getStringValue().equalsIgnoreCase("1"))
					input.getObbTempStr().setStringValue("L");
				if (input.getObbTempStr().getStringValue().equalsIgnoreCase("2"))
					input.getObbTempStr().setStringValue("M");
				if (input.getObbTempStr().getStringValue().equalsIgnoreCase("3"))
					input.getObbTempStr().setStringValue("B");
				if (input.getObbTempStr().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ObbTempStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("LMB".indexOf(input.getObbTempStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ObbTempStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getModVersStr() == null) || (input.getModVersStr().getStringValue() == null) || (input.getModVersStr().equalsIgnoreCase(""))))
			{
				if (input.getModVersStr().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ModVersStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("UP".indexOf(input.getModVersStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ModVersStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getCompAziStr() == null) || (input.getCompAziStr().getStringValue() == null) || (input.getCompAziStr().equalsIgnoreCase(""))))
			{
				if (input.getCompAziStr().getStringValue().equalsIgnoreCase("1"))
					input.getCompAziStr().setStringValue("050");
				if (input.getCompAziStr().getStringValue().equalsIgnoreCase("2"))
					input.getCompAziStr().setStringValue("100");
				if ((input.getCompAziStr().getStringValue().length() != 3) && (input.getCompAziStr().getStringValue().length() != 1)) 
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo CompAziStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("050§100".indexOf(input.getCompAziStr().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo CompAziStr");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getObbInveCli() == null) || (input.getObbInveCli().getStringValue() == null) || (input.getObbInveCli().equalsIgnoreCase(""))))
			{
				if (input.getObbInveCli().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ObbInveCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("IPRNS".indexOf(input.getObbInveCli().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ObbInveCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getObbTempCli() == null) || (input.getObbTempCli().getStringValue() == null) || (input.getObbTempCli().equalsIgnoreCase(""))))
			{
				if (input.getObbTempCli().getStringValue().equalsIgnoreCase("1"))
					input.getObbTempCli().setStringValue("L");
				if (input.getObbTempCli().getStringValue().equalsIgnoreCase("2"))
					input.getObbTempCli().setStringValue("M");
				if (input.getObbTempCli().getStringValue().equalsIgnoreCase("3"))
					input.getObbTempCli().setStringValue("B");
				if (input.getObbTempCli().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ObbTempCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("LMB".indexOf(input.getObbTempCli().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ObbTempCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getModVersCli() == null) || (input.getModVersCli().getStringValue() == null) || (input.getModVersCli().equalsIgnoreCase(""))))
			{
				if (input.getModVersCli().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo ModVersCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("UPE".indexOf(input.getModVersCli().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo ModVersCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getSitFinaCli() == null) || (input.getSitFinaCli().getStringValue() == null) || (input.getSitFinaCli().equalsIgnoreCase(""))))
			{
				if (input.getSitFinaCli().getStringValue().length() != 3)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo SitFinaCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("010§040§100§999".indexOf(input.getSitFinaCli().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo SitFinaCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getPosDisiCli() == null) || (input.getPosDisiCli().getStringValue() == null) || (input.getPosDisiCli().equalsIgnoreCase(""))))
			{
				if (input.getPosDisiCli().getStringValue().equalsIgnoreCase("1"))
					input.getPosDisiCli().setStringValue("A");
				if (input.getPosDisiCli().getStringValue().equalsIgnoreCase("2"))
					input.getPosDisiCli().setStringValue("M");
				if (input.getPosDisiCli().getStringValue().equalsIgnoreCase("3"))
					input.getPosDisiCli().setStringValue("B");
				if (input.getPosDisiCli().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo PosDisiCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("AMB".indexOf(input.getPosDisiCli().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo PosDisiCli");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}

			//20110223: il campo AttCons è significativo solamente per gli strumenti titoli "T", altrimenti forza "N"				
			if (!input.getTipStruStr().equalsIgnoreCase("T"))
			{
				input.getAttCons().setStringValue("N") ;
			}
			
			// 20101110: gestione default AttCons per canale di Vendita
			if ((input.getAttCons().getStringValue() == null) || (input.getAttCons().equalsIgnoreCase("")))
			{
				if (input.getCanVend().equalsIgnoreCase("C"))
				{	
					esito.esito = "011";
					esito.setDescr("Per canale C obbligatorio campo AttCons");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (input.getCanVend().equalsIgnoreCase("P"))
				{
					input.getAttCons().setStringValue("S") ;
				}
				else
				{
					input.getAttCons().setStringValue("N") ;
				}
				//20110223: il campo AttCons è significativo solamente per gli strumenti titoli "T", altrimenti forza "N"				
				if (!input.getTipStruStr().equalsIgnoreCase("T"))
				{
					input.getAttCons().setStringValue("N") ;
				}
			}

			// 20101110: controllo del valore AttCons
			if ("SN".indexOf(input.getAttCons().getStringValue())<0)
			{
				esito.esito = "012";
				esito.setDescr("Valore non ammesso per il campo AttCons");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}

			// 20101110: gestione default TitCons = N 			
			if ((input.getTitCons().getStringValue() == null) || (input.getTitCons().equalsIgnoreCase("")))
			{
					input.getTitCons().setStringValue("N") ;
			}

			// 20101110: controllo del valore TitCons
			if ("SN".indexOf(input.getTitCons().getStringValue())<0)
			{
				esito.esito = "012";
				esito.setDescr("Valore non ammesso per il campo TitCons");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}

			// 20101110: esportazione dei valori calcolati AttCons
			if (input.getAttCons().equalsIgnoreCase("S"))
				output.getClass().getDeclaredField("adeAttCo").set(output,new StringType("S"));
			else
				output.getClass().getDeclaredField("adeAttCo").set(output,new StringType("N"));
			// 20101110: esportazione dei valori calcolati TitCons
			if (input.getTitCons().equalsIgnoreCase("S"))
				output.getClass().getDeclaredField("adeTitCo").set(output,new StringType("S"));
			else
				output.getClass().getDeclaredField("adeTitCo").set(output,new StringType("N"));

			//Controllo del valore ammesso dai parametri
			if (!((input.getSegnOrd() == null) || (input.getSegnOrd().getStringValue() == null) || (input.getSegnOrd().equalsIgnoreCase(""))))
			{
				if (input.getSegnOrd().getStringValue().length() != 1)
				{	
					esito.esito = "011";
					esito.setDescr("Dimensione errata del campo SegnOrd");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ("IASPCAV".indexOf(input.getSegnOrd().getStringValue())<0)
				{
					esito.esito = "012";
					esito.setDescr("Valore non ammesso per il campo SegnOrd");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();

			boolean nonAdeguato = false; 
			boolean seCtrlAdeguatezza = false;
			boolean nonProfilato = false; 
			output.setTuttoAdeguato();
			TW00TBO2Model[] frequenzain = new TW00TBO2Model[5]; 
			TW00TBO2Model[] frequenzaout = new TW00TBO2Model[5]; 
			
			//Lettura dalla 860 per sapere quali controlli fare
			String chiaveControlli = new String(input.getTipStruStr().getStringValue().trim() + "  " + input.getSegnOrd().getStringValue().trim());
			String controlli = null;
			TW00TBTZModel appoggio = null;
			for (int i = 0; i < cacheManager.getElenco860().size(); i++)
			{
				appoggio = (TW00TBTZModel)cacheManager.getElenco860().get(i); 
				if (appoggio.getChiave().equalsIgnoreCase(chiaveControlli))
				{
					controlli = appoggio.getDati().getStringValue();
					break;
				}
			}
			if (controlli == null)
			{
				controlli = AdeguatezzaUtility.padLeft("",'N',6);
			}

			int caso = 0;
			if (controlli.indexOf('S')>=0)
			{
				if (AdeguatezzaUtility.convertDateString(controlli.substring(7,15)).compareTo(input.getDataRif()) > 0)
				{
					caso = 1;
				}
				else if (AdeguatezzaUtility.convertDateString(controlli.substring(15,23)).compareTo(input.getDataRif()) >= 0)
				{
					caso = 2;
					if (input.getNumOrdg().bigValue() == null)
					{
						esito.esito = "004";
						esito.setDescr("Campo NumOrdg non compilato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}				
				}
				else
				{
					caso = 3;
				}
			}
			if ((caso == 2) || (controlli.charAt(2) == 'S'))
			{
				if ((input.getTipStruStr().equalsIgnoreCase("C"))
				&& ((input.getFilDoss() == null) || (input.getFilDoss().getStringValue() == null) || (input.getFilDoss().equalsIgnoreCase("")))
				&& ((input.getContDoss() == null) || (input.getContDoss().getStringValue() == null) || (input.getContDoss().equalsIgnoreCase("")))
				&& ((input.getProgDoss() == null) || (input.getProgDoss().getStringValue() == null) || (input.getProgDoss().equalsIgnoreCase(""))))
				{
					input.setFilDoss(new StringType(FilDossZero));
					input.setContDoss(new StringType(ContDossZero));
					input.setProgDoss(new StringType(ProgDossZero));
				}
				for (int i = 1; i < 5; i++) 
				{
					frequenzain[i] = new TW00TBO2Model();
					frequenzaout[i] = null;
					if (input.getClass().getDeclaredField("ndgDoss" + i).get(input) != null || input.getClass().getDeclaredField("ndgTemp"+i).get(input) != null)
					{
						if (input.getClass().getDeclaredField("ndgDoss" + i).get(input) == null)
						{
							frequenzain[i].setNdgDoss(null);	
							frequenzaout[i] = new TW00TBO2Model();
							frequenzaout[i].setZero();
						}
						else
						{
							if (input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("X") || input.getTipStruStr().equalsIgnoreCase("Z"))
							{
								if ((input.getFilDoss() == null) || (input.getFilDoss().getStringValue() == null) || (input.getFilDoss().equalsIgnoreCase("")))
								{
									esito.esito = "004";
									esito.setDescr("Campo FilDoss non compilato");
									Logger.getInstance().debug(esito.descrizione);
									output.setEsito(new StringType(esito.esito));
									output.setDescErr(new StringType(esito.descrizione));
									return output;
								}
								if ((input.getContDoss() == null) || (input.getContDoss().getStringValue() == null) || (input.getContDoss().equalsIgnoreCase("")))
								{
									esito.esito = "004";
									esito.setDescr("Campo ContDoss non compilato");
									Logger.getInstance().debug(esito.descrizione);
									output.setEsito(new StringType(esito.esito));
									output.setDescErr(new StringType(esito.descrizione));
									return output;
								}								
								if ((input.getProgDoss() == null) || (input.getProgDoss().getStringValue() == null) || (input.getProgDoss().equalsIgnoreCase("")))
								{
									esito.esito = "004";
									esito.setDescr("Campo ProgDoss non compilato");
									Logger.getInstance().debug(esito.descrizione);
									output.setEsito(new StringType(esito.esito));
									output.setDescErr(new StringType(esito.descrizione));
									return output;
								}
								input.setFilDoss(new StringType(Tools.fillSx(input.getFilDoss().getStringValue(),'0',3)));
								input.setContDoss(new StringType(Tools.fillSx(input.getContDoss().getStringValue(),'0',8)));
								input.setProgDoss(new StringType(Tools.fillSx(input.getProgDoss().getStringValue(),'0',4)));
								try 
								{ 
									if (Long.parseLong(
											input.getFilDoss().getStringValue().trim().replaceAll(" ","") + input.getContDoss().getStringValue().trim().replaceAll(" ","") + input.getProgDoss().getStringValue().trim().replaceAll(" ","")  
										) == 0)
									{
										esito.esito = "007";
										esito.setDescr("Campo FilDoss/ContDoss/ProgDoss contengono valori non ammissibili");
										Logger.getInstance().debug(esito.descrizione);
										output.setEsito(new StringType(esito.esito));
										output.setDescErr(new StringType(esito.descrizione));
										return output;
									}
								}catch (Exception e){}
								//frequenzain[i].setNdgTemp(new StringType(input.getFilDoss().getStringValue() + input.getContDoss().getStringValue() + input.getProgDoss().getStringValue() + "0"));
								frequenzain[i].setNdgTemp(new StringType(input.getFilDoss().getStringValue() + input.getContDoss().getStringValue() + "00000"));
							}
							else
							{
								frequenzain[i].setNdgDoss(new StringType(input.getClass().getDeclaredField("ndgDoss" + i).get(input).toString()));
								frequenzain[i].setNdgTemp(null);
							}
							queryResult = dao.executeQueryAccess("ControlloFrequenza",frequenzain[i]);
							if (queryResult.getResult().size() > 0)
							{
								frequenzaout[i] = (TW00TBO2Model)queryResult.getResult().get(0);
								if (frequenzaout[i].getTotGiorass().equals(0))
									frequenzaout[i].setTotGiorass(new IntegerType(1));
								if (frequenzaout[i].getTotGiorfon().equals(0))
									frequenzaout[i].setTotGiorfon(new IntegerType(1));
								if (frequenzaout[i].getTotGiortit().equals(0))
									frequenzaout[i].setTotGiortit(new IntegerType(1));
							}
							else
							{
								frequenzaout[i] = new TW00TBO2Model();
								frequenzaout[i].setZero();
							}
						}
					}
				}
			}
			InputGetQuestionarioModel inputProfilato = null;
			TW00TBSKModel[] profili = new TW00TBSKModel[5]; 
			
			for (int i = 1; i < 5; i++)
			{
				profili[i] = null;
				if (input.getClass().getDeclaredField("ndgDoss" + i).get(input) != null || input.getClass().getDeclaredField("ndgTemp"+i).get(input) != null)
				{
					inputProfilato = new InputGetQuestionarioModel();
					if (input.getClass().getDeclaredField("ndgDoss" + i).get(input) == null)
						inputProfilato.setNdgDoss(null);
					else
						inputProfilato.setNdgDoss(new StringType(input.getClass().getDeclaredField("ndgDoss" + i).get(input).toString()));
					if (input.getClass().getDeclaredField("ndgTemp" + i).get(input) == null)
						inputProfilato.setNdgTemp(null);
					else
						inputProfilato.setNdgTemp(new StringType(input.getClass().getDeclaredField("ndgTemp" + i).get(input).toString()));
					inputProfilato.setCanVend(input.getCanVend());
					if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
						queryResult = dao.executeQueryAccess("GetQuestionarioRete",inputProfilato);
					else
						queryResult = dao.executeQueryAccess("GetQuestionario",inputProfilato);
					
					if (queryResult.getResult().size() > 0)
					{
						profili[i] = (TW00TBSKModel)queryResult.getResult().get(0);
						output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("N"));
					}
					else
					{
						output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("S"));
						output.getClass().getDeclaredField("adeNote" + i).set(output,new StringType("Manca il profilo"));
						if (caso == 1)
						{
							output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeDime" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desDime" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Manca il profilo"));

						}
						if (caso == 2)
						{
							int totaleOrdini = input.getNumOrdg().intValue();
							int frequenza = Integer.parseInt(controlli.substring(23,29));
							if (input.getTipStruStr().equalsIgnoreCase("P") || input.getTipStruStr().equalsIgnoreCase("I"))
							{
								totaleOrdini = totaleOrdini + frequenzaout[i].getOrdTranass().intValue();
							}
							if (input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("X") || input.getTipStruStr().equalsIgnoreCase("Z"))
							{
								totaleOrdini = totaleOrdini + frequenzaout[i].getOrdTrantit().intValue();
							}
							if (input.getTipStruStr().equalsIgnoreCase("F"))
							{
								totaleOrdini = totaleOrdini + frequenzaout[i].getOrdTranfon().intValue();
							}
							if (totaleOrdini > frequenza)
							{
								output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("E"));
								output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType("E"));
								output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeDime" + i).set(output,new StringType("E"));
								output.getClass().getDeclaredField("desDime" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("E"));
								output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType("E"));
								output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Manca il profilo"));
								nonProfilato = true;
							}
							else
							{
								output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("W"));
								output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType("W"));
								output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeDime" + i).set(output,new StringType("W"));
								output.getClass().getDeclaredField("desDime" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("W"));
								output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Manca il profilo"));
								output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType("W"));
								output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Manca il profilo"));

							}
						}
						if (caso == 3)
						{
							output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("E"));
							output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType("E"));
							output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeDime" + i).set(output,new StringType("E"));
							output.getClass().getDeclaredField("desDime" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Manca il profilo"));
							output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType("W"));
							output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Manca il profilo"));
							nonProfilato = true;
						}
					}
				}
				else
				{
					output.setAdeguatoIndex(i,false);
					output.getClass().getDeclaredField("errProf" + i).set(output,new StringType("S"));
				}
			}
						
			String chiaveSingoloControllo = null;
			boolean trovatoSingoloControllo = true;
			//Controllo per Tipologia/Oggetto 861 (standard) / 862 (eccezioni 861)
			if (controlli.charAt(0) == 'S')
			{
				if ((input.getRiscStrStr().getStringValue() == null) || (input.getRiscStrStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo RiscStrStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (!(input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("I") || input.getTipStruStr().equalsIgnoreCase("X")|| input.getTipStruStr().equalsIgnoreCase("Z")))
				{
					if ((input.getModVersStr().getStringValue() == null) || (input.getModVersStr().equalsIgnoreCase("")))
					{
						esito.esito = "004";
						esito.setDescr("Campo ModVersStr non compilato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
					if ((input.getObbTempCli().getStringValue() == null) || (input.getObbTempCli().equalsIgnoreCase("")))
					{
						esito.esito = "004";
						esito.setDescr("Campo ObbTempCli non compilato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
					if ((input.getCompAziStr().getStringValue() == null) || (input.getCompAziStr().equalsIgnoreCase("")))
					{
						esito.esito = "004";
						esito.setDescr("Campo CompAziStr non compilato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
				if ((input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("I")))
				{
					if ((input.getObbTempCli().getStringValue() == null) || (input.getObbTempCli().equalsIgnoreCase("")))
					{
						esito.esito = "004";
						esito.setDescr("Campo ObbTempCli non compilato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
				for (int i = 1;i < profili.length; i++)
				{
					if (profili[i] != null)
					{
						trovatoSingoloControllo = true;
						if (!(input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("I") || input.getTipStruStr().equalsIgnoreCase("X") || input.getTipStruStr().equalsIgnoreCase("Z")))
						{
							chiaveSingoloControllo = chiaveControlli;
							chiaveSingoloControllo = chiaveSingoloControllo + input.getRiscStrStr().getStringValue().trim();
							chiaveSingoloControllo = chiaveSingoloControllo + "  ";
							chiaveSingoloControllo = chiaveSingoloControllo + ((TW00TBSKModel)profili[i]).getProfilo().getStringValue().trim();
							chiaveSingoloControllo = chiaveSingoloControllo + input.getModVersStr().getStringValue().trim();
							chiaveSingoloControllo = chiaveSingoloControllo + "  ";
							chiaveSingoloControllo = chiaveSingoloControllo + input.getObbTempCli().getStringValue().trim();
							chiaveSingoloControllo = chiaveSingoloControllo + "  ";
							chiaveSingoloControllo = chiaveSingoloControllo + input.getCompAziStr().getStringValue().trim();
							for (int j = 0; j < cacheManager.getElenco862().size(); j++)
							{
								appoggio = (TW00TBTZModel)cacheManager.getElenco862().get(j); 
								if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
								{
									trovatoSingoloControllo = false;
									output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
									if (appoggio.getDati().getStringValue().equalsIgnoreCase("N"))
									{
										output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Non adeguato per Tipologia/Oggetto"));
										nonAdeguato = true;
									}
									break;
								}
							}
						}
						if (trovatoSingoloControllo)
						{	
							chiaveSingoloControllo = chiaveControlli;
							chiaveSingoloControllo = chiaveSingoloControllo + input.getRiscStrStr().getStringValue().trim();
							chiaveSingoloControllo = chiaveSingoloControllo + "  ";
							chiaveSingoloControllo = chiaveSingoloControllo + ((TW00TBSKModel)profili[i]).getProfilo().getStringValue().trim();
							for (int j = 0; j < cacheManager.getElenco861().size(); j++)
							{
								appoggio = (TW00TBTZModel)cacheManager.getElenco861().get(j); 
								if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
								{
									output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
									if (appoggio.getDati().getStringValue().equalsIgnoreCase("N"))
									{
										output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Non adeguato per Tipologia/Oggetto"));
										nonAdeguato = true;
									}
									break;
								}
							}
						}
					}
				}
			}
			//Controllo per Dimensione
			if (controlli.charAt(1) == 'S')
			{
				if ((input.getRiscStrStr().getStringValue() == null) || (input.getRiscStrStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo RiscStrStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getSitFinaCli().getStringValue() == null) || (input.getSitFinaCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo SitFinaCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				for (int i = 1;i < profili.length; i++)
				{
					if (profili[i] != null)
					{
						chiaveSingoloControllo = chiaveControlli;
						chiaveSingoloControllo = chiaveSingoloControllo + input.getRiscStrStr().getStringValue().trim();
						chiaveSingoloControllo = chiaveSingoloControllo + "  ";
						chiaveSingoloControllo = chiaveSingoloControllo + ((TW00TBSKModel)profili[i]).getProfilo().getStringValue().trim();
						chiaveSingoloControllo = chiaveSingoloControllo + input.getSitFinaCli().getStringValue().trim();
						for (int j = 0; j < cacheManager.getElenco863().size(); j++)
						{
							appoggio = (TW00TBTZModel)cacheManager.getElenco863().get(j); 
							if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
							{
								output.getClass().getDeclaredField("adeDime" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
								if (appoggio.getDati().getStringValue().equalsIgnoreCase("N"))
								{
									output.getClass().getDeclaredField("desDime" + i).set(output,new StringType("Non adeguato per Dimensione"));
									nonAdeguato = true;
									if (input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("I"))
									{	
										output.getClass().getDeclaredField("adeTogg" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
										output.getClass().getDeclaredField("desTogg" + i).set(output,new StringType("Non adeguato per Tipologia/Oggetto"));
									}
								}
								break;
							}		
						}
					}
				}				
			}
			//Controllo per Frequenza
			if (controlli.charAt(2) == 'S')
			{
				if ((input.getRiscStrStr().getStringValue() == null) || (input.getRiscStrStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo RiscStrStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (input.getNumOrdg().bigValue() == null)
				{
					esito.esito = "004";
					esito.setDescr("Campo NumOrdg non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				for (int i = 1;i < profili.length; i++)
				{
					if (profili[i] != null)
					{
						chiaveSingoloControllo = chiaveControlli;
						chiaveSingoloControllo = chiaveSingoloControllo + profili[i].getProfilo().getStringValue();
						chiaveSingoloControllo = chiaveSingoloControllo + input.getRiscStrStr().getStringValue();
						for (int j = 0; j < cacheManager.getElenco864().size(); j++)
						{
							appoggio = (TW00TBTZModel)cacheManager.getElenco864().get(j); 
							if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
							{
								int totaleOrdini = input.getNumOrdg().intValue();
								int frequenza = Integer.parseInt(appoggio.getDati().getStringValue().substring(0,8));
								int media = Integer.MAX_VALUE;
								if (input.getTipStruStr().equalsIgnoreCase("P") || input.getTipStruStr().equalsIgnoreCase("I"))
								{
									totaleOrdini = totaleOrdini + frequenzaout[i].getNumOrdiass().intValue();
									media = frequenzaout[i].getTotOrdiass().intValue() / frequenzaout[i].getTotGiorass().intValue(); 
								}
								if (input.getTipStruStr().equalsIgnoreCase("T") || input.getTipStruStr().equalsIgnoreCase("C") || input.getTipStruStr().equalsIgnoreCase("X") || input.getTipStruStr().equalsIgnoreCase("Z"))
								{
									totaleOrdini = totaleOrdini + frequenzaout[i].getNumOrditit().intValue();
									media = frequenzaout[i].getTotOrditit().intValue() / frequenzaout[i].getTotGiortit().intValue(); 
								}
								if (input.getTipStruStr().equalsIgnoreCase("F"))
								{
									totaleOrdini = totaleOrdini + frequenzaout[i].getNumOrdifon().intValue();
									media = frequenzaout[i].getTotOrdifon().intValue() / frequenzaout[i].getTotGiorfon().intValue(); 
								}
								if (totaleOrdini > frequenza)
								{
									if (appoggio.getDati().getStringValue().charAt(8) == 'S')
									{
										if (totaleOrdini > media)
										{
											output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("N"));
											output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Non adeguato per Frequenza/Media"));
											nonAdeguato = true;
										}
									}
									else
									{
										output.getClass().getDeclaredField("adeFreq" + i).set(output,new StringType("N"));
										output.getClass().getDeclaredField("desFreq" + i).set(output,new StringType("Non adeguato per Frequenza"));
										nonAdeguato = true;
									}
								}
								break;
							}
						}
					}
				}
			}
			//Controllo per Obbiettivo di Investimento
			if (controlli.charAt(3) == 'S')
			{
				if ((input.getObbInveStr().getStringValue() == null) || (input.getObbInveStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbInveStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getObbInveCli().getStringValue() == null) || (input.getObbInveCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbInveCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (!input.getObbInveCli().equalsIgnoreCase(input.getObbInveStr().getStringValue()))
				{
					for (int i = 1;i < profili.length; i++)
					{
						if (profili[i] != null)
						{
							output.getClass().getDeclaredField("adeObbi" + i).set(output,new StringType("N"));
							output.getClass().getDeclaredField("desObbi" + i).set(output,new StringType("Non adeguato per Obbiettivo Investimento"));
							nonAdeguato = true;
						}
					}
				}				
			}
			//Controllo per Orizzonte Temporale
			if (controlli.charAt(4) == 'S')
			{
				if ((input.getObbTempStr().getStringValue() == null) || (input.getObbTempStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbTempStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getObbTempCli().getStringValue() == null) || (input.getObbTempCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbTempCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				int obbTempCliInt = 0;
				int obbTempStrInt = 0;
				switch (input.getObbTempCli().getStringValue().charAt(0))
				{
					case 'L':
						obbTempCliInt=3;
						break;
					case 'M':
						obbTempCliInt=2;
						break;
					case 'B':
						obbTempCliInt=1;
						break;
				}
				switch (input.getObbTempStr().getStringValue().charAt(0))
				{
					case 'L':
						obbTempStrInt=3;
						break;
					case 'M':
						obbTempStrInt=2;
						break;
					case 'B':
						obbTempStrInt=1;
						break;
				}
//				if (!input.getObbTempCli().equalsIgnoreCase(input.getObbTempStr().getStringValue()))
				if (obbTempCliInt < obbTempStrInt)
				{
					for (int i = 1;i < profili.length; i++)
					{
						if (profili[i] != null)
						{
							output.getClass().getDeclaredField("adeOrit" + i).set(output,new StringType("N"));
							output.getClass().getDeclaredField("desOrit" + i).set(output,new StringType("Non adeguato per Orizzonte Temporale"));
							nonAdeguato = true;
						}
					}
				}				
			}
			//Controllo per Orizzonte Temporale Uguaglianza
			if (controlli.charAt(4) == 'E')
			{
				if ((input.getObbTempStr().getStringValue() == null) || (input.getObbTempStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbTempStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getObbTempCli().getStringValue() == null) || (input.getObbTempCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbTempCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				int obbTempCliInt = 0;
				int obbTempStrInt = 0;
				switch (input.getObbTempCli().getStringValue().charAt(0))
				{
					case 'L':
						obbTempCliInt=3;
						break;
					case 'M':
						obbTempCliInt=2;
						break;
					case 'B':
						obbTempCliInt=1;
						break;
				}
				switch (input.getObbTempStr().getStringValue().charAt(0))
				{
					case 'L':
						obbTempStrInt=3;
						break;
					case 'M':
						obbTempStrInt=2;
						break;
					case 'B':
						obbTempStrInt=1;
						break;
				}
//				if (!input.getObbTempCli().equalsIgnoreCase(input.getObbTempStr().getStringValue()))
				if (obbTempCliInt != obbTempStrInt)
				{
					for (int i = 1;i < profili.length; i++)
					{
						if (profili[i] != null)
						{
							output.getClass().getDeclaredField("adeOrit" + i).set(output,new StringType("N"));
							output.getClass().getDeclaredField("desOrit" + i).set(output,new StringType("Non adeguato per Orizzonte Temporale"));
							nonAdeguato = true;
						}
					}
				}				
			}
			//Controllo per Modalità di Versamento
			if (controlli.charAt(5) == 'S')
			{
				if ((input.getModVersStr().getStringValue() == null) || (input.getModVersStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ModVersStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getModVersCli().getStringValue() == null) || (input.getModVersCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ModVersCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (!(
						(input.getModVersCli().equalsIgnoreCase("E")) 
						||
						(input.getModVersCli().equalsIgnoreCase(input.getModVersStr().getStringValue()))
				))
				{
					for (int i = 1;i < profili.length; i++)
					{
						if (profili[i] != null)
						{
							output.getClass().getDeclaredField("adeModv" + i).set(output,new StringType("N"));
							output.getClass().getDeclaredField("desModv" + i).set(output,new StringType("Non adeguato per Modalita' Versamento"));
							nonAdeguato = true;
						}				
					}				
				}				
			}
			seCtrlAdeguatezza = false;
			// 20101110: Controllo Bloccante per Titoli ILLIQUIDI
			if (controlli.charAt(30) == 'E')
			{
				esito.esito = "210";
				esito.setDescr("TITOLO ILLIQUIDO ACQUISTO NON POSSIBILE");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;					
			}
			
			// 20101110: Controllo Consulenza Per canale di vendita Promotore
			if ((controlli.charAt(30) == 'S') && (input.getCanVend().equalsIgnoreCase("P")))
			{
				// se ordine in consulenza e titolo consulenziabile ADEGUATEZZA
				if ((input.getAttCons().equalsIgnoreCase("S")) && (input.getTitCons().equalsIgnoreCase("S")))
				{
					seCtrlAdeguatezza = true;
				}
				// se ordine NON in consulenza e titolo consulenziabile ERRORE
				if ((input.getAttCons().equalsIgnoreCase("N")) && (input.getTitCons().equalsIgnoreCase("S")))
				{
					esito.esito = "211";
					esito.setDescr("Ordine non in consulenza e Titolo Consulenziabile");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;					
				}
				// se ordine in consulenza e titolo NON consulenziabile ERRORE
				if ((input.getAttCons().equalsIgnoreCase("S")) && (input.getTitCons().equalsIgnoreCase("N")))
				{
					esito.esito = "212";
					esito.setDescr("Ordine in consulenza e Titolo NON Consulenziabile");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;					
				}
				// se ordine NON in consulenza e titolo NON consulenziabile APPROPRIATEZZA
				if ((input.getAttCons().equalsIgnoreCase("N")) && (input.getTitCons().equalsIgnoreCase("N")))
				{
					seCtrlAdeguatezza = false;
				}
			}

			// 20101110: Controllo Consulenza Per canale di vendita diverso da Promotore
			if ((controlli.charAt(30) == 'S') && !(input.getCanVend().equalsIgnoreCase("P")))
			{
				// se ordine in consulenza e titolo consulenziabile ADEGUATEZZA
				if ((input.getAttCons().equalsIgnoreCase("S")) && (input.getTitCons().equalsIgnoreCase("S")))
				{
					seCtrlAdeguatezza = true;
				}
				// se ordine in consulenza e titolo NON consulenziabile ERRORE
				if ((input.getAttCons().equalsIgnoreCase("S")) && (input.getTitCons().equalsIgnoreCase("N")))
				{
					esito.esito = "212";
					esito.setDescr("Ordine in consulenza e Titolo NON Consulenziabile");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;					
				}
				// se ordine NON in consulenza e titolo NON consulenziabile APPROPRIATEZZA
				if ((input.getAttCons().equalsIgnoreCase("N")) && (input.getTitCons().equalsIgnoreCase("N")))
				{
					seCtrlAdeguatezza = false;
				}
			}

			
			//Controllo per Disinvestimento
			if (controlli.charAt(29) == 'S')
			{
				if ((input.getObbTempStr().getStringValue() == null) || (input.getObbTempStr().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo ObbTempStr non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getSitFinaCli().getStringValue() == null) || (input.getSitFinaCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo SitFinaCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getPosDisiCli().getStringValue() == null) || (input.getPosDisiCli().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo PosDisiCli non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				//Controllo per Disinvestimento primo giro senza utilizzare la dimensione
				for (int i = 1;i < profili.length; i++)
				{
					if (profili[i] != null)
					{
						chiaveSingoloControllo = chiaveControlli;
						chiaveSingoloControllo = chiaveSingoloControllo + input.getObbTempStr().getStringValue().trim();
						chiaveSingoloControllo = chiaveSingoloControllo + "  ";
						chiaveSingoloControllo = chiaveSingoloControllo + input.getPosDisiCli().getStringValue().trim();
						for (int j = 0; j < cacheManager.getElenco865().size(); j++)
						{
							appoggio = (TW00TBTZModel)cacheManager.getElenco865().get(j); 
							if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
							{
								if (appoggio.getDati().getStringValue().equalsIgnoreCase("N"))
								{
									output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
									output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Non adeguato per Disinvestimento"));
									nonAdeguato = true;
								}
								break;
							}		
						}
					}
				}
				//Controllo per Disinvestimento secondo giro utilizzando la dimensione solo nel caso di adeguatezza per Disinvestimento
				for (int i = 1;i < profili.length; i++)
				{
					if (profili[i] != null)
					{
						chiaveSingoloControllo = chiaveControlli;
						chiaveSingoloControllo = chiaveSingoloControllo + input.getObbTempStr().getStringValue().trim();
						chiaveSingoloControllo = chiaveSingoloControllo + "  ";
						chiaveSingoloControllo = chiaveSingoloControllo + input.getPosDisiCli().getStringValue().trim();
						chiaveSingoloControllo = chiaveSingoloControllo + input.getSitFinaCli().getStringValue().trim();
						for (int j = 0; j < cacheManager.getElenco865().size(); j++)
						{
							appoggio = (TW00TBTZModel)cacheManager.getElenco865().get(j); 
							if (appoggio.getChiave().equalsIgnoreCase(chiaveSingoloControllo))
							{
								if (appoggio.getDati().getStringValue().equalsIgnoreCase("N"))
								{
									output.getClass().getDeclaredField("adeDisi" + i).set(output,new StringType(appoggio.getDati().getStringValue()));
									output.getClass().getDeclaredField("desDisi" + i).set(output,new StringType("Non adeguato per Disinvestimento"));
									nonAdeguato = true;
								}
								break;
							}		
						}
					}
				}								
			}

			if (esito.esito.equalsIgnoreCase("000"))
			{
				if (nonAdeguato == true)
				{
					esito.esito = "200";
					esito.setDescr("Trovato un valore di non adeguatezza");
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					
					// 20101110: se devo fare il controllo di adeguatezza cambio esito.
					if (seCtrlAdeguatezza == true)
					{
						esito.esito = "220";
						esito.setDescr("Trovato un valore di non adeguatezza (Consulenza)");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
				if (nonProfilato == true)
				{
					esito.esito = "201";
					esito.setDescr("Obbligo di profilazione non rispettato da uno o piu clienti");
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));

					// 20101110: se devo fare il controllo di adeguatezza cambio esito.
					if (seCtrlAdeguatezza == true)
					{
						esito.esito = "221";
						esito.setDescr("Obbligo di profilazione non rispettato da uno o piu clienti (Consulenza)");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
			}
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL  " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e); 
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}

	public static OutputGetQuestionarioModel getProfilo (ClientSessionContext csc, InputGetQuestionarioModel input, 
		StringType nomeMetodo, BooleanType internalCall) throws EJBException
	{
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
		}
		StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
		CacheManager cacheManager = null;
		PrgmCommands command = null;
		OutputGetQuestionarioModel output = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
		}
		//Inizializzazione variabili
		try
		{
			output = new OutputGetQuestionarioModel();
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			cacheManager = CacheManager.getInstance(csc);
			command = cacheManager.getCommand(nomeMetodo);
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "001";
			esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		if (internalCall.booleanValue() == false)
		{
/* "CEDACRI"     */
			if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
				cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
			{
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
/*        "CEDACRI" */
		}
		//Conversione classe input
		try
		{
			if (internalCall.booleanValue() == false)
			{
				try 
				{ 
					if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgDoss(null);
				}catch (Exception e){}
				try 
				{ 
					if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgTemp(null);
				}catch (Exception e){}
				if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
				  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
				{
					esito.esito = "004";
					esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
			{
				input.setNdgTemp(null);
			}
			else
			{
				input.setNdgDoss(null);
			} 				
			if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
			{
				input.setNdgDoss(null);
			} 
			else
			{
				input.setNdgTemp(null);
			}
				
			if (!((input.getNdgDoss() == null) && (input.getNdgTemp() == null)))
			{
				if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getNdgTemp() == null) && (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull)))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
			}
			if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo CanVend non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Username non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
			{
				esito.esito = "004";
				esito.setDescr("Campo Country non compilato");
				Logger.getInstance().debug(esito.descrizione);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "002";
			esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
			return output;
		}
		//Lettura da database
		DAOObject dao = null;
		DAOQueryResultModel queryResult = null;
		if (internalCall.booleanValue() == false)
		{
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
		}
		try
		{
			dao = new DAOObject(csc,cacheManager.getNomeDbFile());
			dao.openConnection();
			if (!((input.getNdgDoss() == null) && (input.getNdgTemp() == null)))
			{
				if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
					queryResult = dao.executeQueryAccess("GetQuestionarioRete",input);
				else
					queryResult = dao.executeQueryAccess("GetQuestionario",input);
			
				if (queryResult.getResult().size() > 0)
				{
					TW00TBSKModel sk = (TW00TBSKModel)queryResult.getResult().get(0);
					output.setProfilo(sk.getProfilo());
					output.setDatComp(AdeguatezzaUtility.convertStringDate(sk.getDatsche()));
					output.setOraComp(AdeguatezzaUtility.convertStringTime(sk.getDatsche()));
					output.setRelease(sk.getRelease());
					output.setSeCompi(new StringType("S"));
//20111002:seValid
					output.setSeValid(sk.getSevalid());
					
					output.setCluster(sk.getCluster());	
					
/*v12*/				output.setEsigLiq(sk.getEsigliq());
/*v12*/				output.setForzObT(sk.getForzobt());
/*v12*/				output.setOrigObT(sk.getOrigobt());
/*v12*/				output.setOrigClu(sk.getOrigclu());
					
					output.setDfinval(sk.getDfinval());
					
					/* 20140829 aggiunta Disc */
					output.setObbtemp(sk.getObbtemp());
					output.setSitfina(sk.getSitfina());
					output.setObbinve(sk.getObbinve());
					output.setEspfina(sk.getEspfina());
					/* 20140829 aggiunta Disc */
					
					
					output.setDesCluster(new StringType("    "));
								
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER1"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER2"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER3"))
			 		{
			 			output.setDesCluster(new StringType("Intraprendente Breve"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER4"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER5"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER6"))
			 		{
			 			output.setDesCluster(new StringType("Equilibrato Breve"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER7"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Lungo"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER8"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Medio"));
			 		}
			 		if (sk.getCluster().toString().equalsIgnoreCase("CLUSTER9"))
			 		{
			 			output.setDesCluster(new StringType("Conservatore Breve"));
			 		}

/*v12*/				output.setDesOrigClu(new StringType("    "));
					
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER1"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER2"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER3"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Intraprendente Breve"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER4"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER5"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER6"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Equilibrato Breve"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER7"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Lungo"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER8"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Medio"));
/*v12*/		 		}
/*v12*/		 		if (sk.getOrigclu().toString().equalsIgnoreCase("CLUSTER9"))
/*v12*/		 		{
/*v12*/		 			output.setDesOrigClu(new StringType("Conservatore Breve"));
/*v12*/		 		}
				} 
				else 
				{
					output.setDatComp(new StringType());
					output.setOraComp(new StringType());
					output.setProfilo(new StringType());
					output.setRelease(new IntegerType());
					output.setSeCompi(new StringType("N"));
//20111002:seValid
					output.setSeValid(new StringType());

					output.setCluster(new StringType());
					
/*v12*/				output.setEsigLiq(new StringType());
/*v12*/				output.setForzObT(new StringType());
/*v12*/				output.setOrigObT(new StringType());
/*v12*/				output.setOrigClu(new StringType());
					/* 20140829 aggiunta Disc */
					output.setObbtemp(new StringType());
					output.setSitfina(new StringType());
					output.setObbinve(new StringType());
					output.setEspfina(new StringType());
					/* 20140829 aggiunta Disc */
				}
			}
			else
			{
				output.setDatComp(new StringType());
				output.setOraComp(new StringType());
				output.setProfilo(new StringType());
				output.setRelease(new IntegerType());
				output.setSeCompi(new StringType("N"));
//20111002:seValid
				output.setSeValid(new StringType());

				output.setCluster(new StringType());
/*v12*/			output.setEsigLiq(new StringType());
/*v12*/			output.setForzObT(new StringType());
/*v12*/			output.setOrigObT(new StringType());
/*v12*/			output.setOrigClu(new StringType());
				/* 20140829 aggiunta Disc */
				output.setObbtemp(new StringType());
				output.setSitfina(new StringType());
				output.setObbinve(new StringType());
				output.setEspfina(new StringType());
				/* 20140829 aggiunta Disc */
			}				
				
			if (internalCall.booleanValue() == false)
			{
				Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");
			}
		}catch(DAOException daoe){
			StringWriter sw = new StringWriter();
			daoe.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore SQL " + sw.toString()+"]]>");
			Logger.getInstance().error(daoe);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			esito.esito = "999";
			esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
			Logger.getInstance().error(e);
			output.setEsito(new StringType(esito.esito));
			output.setDescErr(new StringType(esito.descrizione));
		}finally{
			if(dao != null) dao.closeConnection();
		}
		return output;
	}
	
	public static OutputAggiornaPatrimonioModel aggiornaPatrimonio (ClientSessionContext csc, InputAggiornaPatrimonioModel input, 
			StringType nomeMetodo, BooleanType internalCall) throws EJBException
		{
			String ndgTempInserimento = new String();
	        Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Inizio");
			StatoAdeguatezza esito = new StatoAdeguatezza(nomeMetodo.getStringValue());
			CacheManager cacheManager = null;
			PrgmCommands command = null;
			OutputAggiornaPatrimonioModel output = null;
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inizializzazione variabili.");
			//Inizializzazione variabili
			try
			{
				ndgTempInserimento = input.getNdgTemp().getStringValue();
				output = new OutputAggiornaPatrimonioModel();
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				cacheManager = CacheManager.getInstance(csc);
				command = cacheManager.getCommand(nomeMetodo);
			}catch(Exception e){
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				esito.esito = "001";
				esito.setDescr("<![CDATA["+"Errore inizializzazione variabili " + sw.toString()+"]]>");
				Logger.getInstance().error(e);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			if (internalCall.booleanValue() == false)
			{
/* "CEDACRI"      */
				if (AccessLog.callTraceFunction(csc,input.getUsername(),input.getCountry(),input.getCanVend(),
					cacheManager.getApplicazione(),command,nomeMetodo,cacheManager.getNomeDbFile(),esito) == false)
				{
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
/*	       "CEDACRI" */
			}
			//Conversione classe input
			try
			{
				try 
				{ 
					if (Long.parseLong(input.getNdgDoss().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgDoss(null);
				}catch (Exception e){}
				try 
				{ 
					if (Long.parseLong(input.getNdgTemp().getStringValue().trim().replaceAll(" ","")) == 0)
						input.setNdgTemp(null);
				}catch (Exception e){}
				if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
				  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
				{
					esito.esito = "004";
					esito.setDescr("Campo NdgDoss/NdgTemp non compilati");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (!((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase(""))))
				{
					input.setNdgTemp(null);
				}
				if (!((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
				{
					input.setNdgDoss(null);
				} 
				if ((input.getNdgDoss() == null) && (input.getNdgTemp().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgTempNull)))
				{
					esito.esito = "007";
					esito.setDescr("Operazione richiesta non valida: ndgTemp " + AdeguatezzaBusinessUtility.ndgTempNull + " e' un valore riservato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if (input.getNdgTemp() == null)
				{
					if (input.getNdgDoss().equalsIgnoreCase(AdeguatezzaBusinessUtility.ndgDossNull))
					{
						esito.esito = "007";
						esito.setDescr("Operazione richiesta non valida: ndgDoss " + AdeguatezzaBusinessUtility.ndgDossNull + " e' un valore riservato");
						Logger.getInstance().debug(esito.descrizione);
						output.setEsito(new StringType(esito.esito));
						output.setDescErr(new StringType(esito.descrizione));
						return output;
					}
				}
				if (((input.getNdgDoss() == null) || (input.getNdgDoss().getStringValue() == null) || (input.getNdgDoss().equalsIgnoreCase("")))
				  && ((input.getNdgTemp() == null) || (input.getNdgTemp().getStringValue() == null) || (input.getNdgTemp().equalsIgnoreCase(""))))
				{
					esito.esito = "007";
					esito.setDescr("Campo NdgDoss/NdgTemp non validi");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}

				if ((input.getCanVend().getStringValue() == null) || (input.getCanVend().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo CanVend non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getUsername().getStringValue() == null) || (input.getUsername().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo Username non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getCountry().getStringValue() == null) || (input.getCountry().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo Country non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto1() == null) || (input.getImporto1().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo1 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto2() == null) || (input.getImporto2().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo2 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto3() == null) || (input.getImporto3().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo3 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto4() == null) || (input.getImporto4().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo4 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto5() == null) || (input.getImporto5().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo5 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto6() == null) || (input.getImporto6().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo6 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto7() == null) || (input.getImporto7().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo7 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto8() == null) || (input.getImporto8().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo8 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}
				if ((input.getImporto9() == null) || (input.getImporto9().equalsIgnoreCase("")))
				{
					esito.esito = "004";
					esito.setDescr("Campo importo9 non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}

				if (input.getDataOraComp().isNull())
				{
					esito.esito = "004";
					esito.setDescr("Campo DataOraComp non compilato");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}			
			}catch(Exception e){
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				esito.esito = "002";
				esito.setDescr("<![CDATA["+"Controllo parametri obbligatori " + sw.toString()+"]]>");
				Logger.getInstance().error(e);
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
				return output;
			}
			//Lettura da database
			DAOObject dao = null;
			DAOQueryResultModel queryResult = null;
			Logger.getInstance().info(nomeMetodo.getStringValue() + ": Logica");
			try
			{
				dao = new DAOObject(csc,cacheManager.getNomeDbFile());
				dao.openConnection();
				
				InputGetQuestionarioModel inputProfilato = new InputGetQuestionarioModel();
				TW00TBSKModel skold = new TW00TBSKModel();
				TW00TBQKModel qkold = new TW00TBQKModel();
				TW00TBQMModel qmold = new TW00TBQMModel();

				if (input.getNdgDoss() == null)
					inputProfilato.setNdgDoss(null);
				else
					inputProfilato.setNdgDoss(new StringType(input.getNdgDoss().toString()));
				if (input.getNdgTemp() == null)
					inputProfilato.setNdgTemp(null);
				else
					inputProfilato.setNdgTemp(new StringType(input.getNdgTemp().toString()));

				inputProfilato.setCanVend(input.getCanVend());

/*20120924:il questionario cercato deve essere non scaduto e già validato, quindi usa la query "GetQuestionario"
				if (input.getCanVend().equalsIgnoreCase(cacheManager.getCanVendRete().getStringValue()))
					queryResult = dao.executeQueryAccess("GetQuestionarioRete",inputProfilato);
				else
					queryResult = dao.executeQueryAccess("GetQuestionario",inputProfilato);
*/				
				queryResult = dao.executeQueryAccess("GetQuestionario",inputProfilato);
				if (queryResult.getResult().size() > 0)
				{
					skold = (TW00TBSKModel)queryResult.getResult().get(0);
				}
				else
				{
					esito.esito = "300";
					esito.setDescr("NDG non trovato tabella TW00TBSK");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}				
				IntegerType release = new IntegerType();
				release = skold.getRelease();
				
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Versione " + release.getStringValue());
				
				//Lettura tabella QK
				UserKeyModel ukModel = new UserKeyModel();
				ukModel.setNdgDoss(input.getNdgDoss());
				ukModel.setNdgTemp(input.getNdgTemp());
				ukModel.setUserKey(skold.getDatsche());

				queryResult = dao.executeQueryAccess("GetTW00TBQK",ukModel);
				
				if (queryResult.getResult().size() > 0)
				{
					qkold = (TW00TBQKModel)queryResult.getResult().get(0);
				}
				else
				{
					esito.esito = "301";
					esito.setDescr("NDG non trovato tabella TW00TBQK");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}		
				
				//Lettura tabella QM
				queryResult = dao.executeQueryAccess("GetTW00TBQM",ukModel);
				
				if (queryResult.getResult().size() > 0)
				{
					qmold = (TW00TBQMModel)queryResult.getResult().get(0);
				}
				else
				{
					esito.esito = "302";
					esito.setDescr("NDG non trovato tabella TW00TBQM");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;
				}	
				
				//Logica di canale
				TimestampType datComp = input.getDataOraComp();
				
//				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Allineamento chiave primaria");
				//Salvataggio dati
				TW00TBSKModel sk = new TW00TBSKModel();
				TW00TBQKModel qk = new TW00TBQKModel(); 
				TW00TBQMModel qm = new TW00TBQMModel();

				sk = skold;
				qk = qkold;
				qm = qmold;
				
//20120924: il canale di vendita viene sempre forzato a P.
				sk.setCanvend(new StringType("P"));
				qk.setCanvend(new StringType("P"));

				sk.setGiosche(new IntegerType(datComp.getAA()+datComp.getMM()+datComp.getGG()));
				sk.setDatsche(datComp);
//forzatura dei campi non valorizzati a spazio
				if ((skold.getEspfina() == null) || (skold.getEspfina().equalsIgnoreCase("")))
				{
					sk.setEspfina(new StringType("   "));
				}
				if ((skold.getObbinve() == null) || (skold.getObbinve().equalsIgnoreCase("")))
				{
					sk.setObbinve(new StringType("   "));
				}
				if ((skold.getObbtemp() == null) || (skold.getObbtemp().equalsIgnoreCase("")))
				{
					sk.setObbtemp(new StringType(" "));
				}
				if ((skold.getSitfina() == null) || (skold.getSitfina().equalsIgnoreCase("")))
				{
					sk.setSitfina(new StringType("   "));
				}
				if ((skold.getFlskcli() == null) || (skold.getFlskcli().equalsIgnoreCase("")))
				{
					sk.setFlskcli(new StringType("   "));
				}
//20120924: impostato il nuovo questionario sempre a non validato
//				if ((skold.getSevalid() == null) || (skold.getSevalid().equalsIgnoreCase("")))
//				{
//					sk.setSevalid(new StringType(" "));
//				}
				sk.setSevalid(new StringType(" "));
		
				if ((skold.getCluster() == null) || (skold.getCluster().equalsIgnoreCase("")))
				{
					sk.setCluster(new StringType("        "));
				}
//20120924: tolta forzatura a spazi dei campi per esigenze di Mediolanum di lasciarli a null.
//				if ((skold.getEsigliq() == null) || (skold.getEsigliq().equalsIgnoreCase("")))
//				{
//					sk.setEsigliq(new StringType(" "));
//				}
//				if ((skold.getForzobt() == null) || (skold.getForzobt().equalsIgnoreCase("")))
//				{
//					sk.setForzobt(new StringType(" "));
//				}
//				if ((skold.getOrigobt() == null) || (skold.getOrigobt().equalsIgnoreCase("")))
//				{
//					sk.setOrigobt(new StringType(" "));
//				}
//				if ((skold.getOrigclu() == null) || (skold.getOrigclu().equalsIgnoreCase("")))
//				{
//					sk.setOrigclu(new StringType("        "));
//				}
//20120924: tolta forzatura a spazi dei campi per esigenze di Mediolanum di lasciarli a null.
				
				qk.setDatsche(datComp);
				qm.setDatsche(datComp);

				String impoString = null;
				int numdomains = 0;
				//determinazione prima domanda importo in base alla release
				if (release.intValue() > 2)
				{
					numdomains = 11;
				}
				else
				{
					numdomains = 9;
				}						
				
				String selrispold = new String();
				String selrisppre = new String();
				String selrispost = new String();
				String selrispnew = new String();
				
				selrispold = qk.getSelrisp().stringValue();
				selrisppre = selrispold.substring(0,(numdomains - 1));
				
				if (selrispold.length() > numdomains)
				{
					selrispost = selrispold.substring(numdomains - 1 + 9);
				}
					
				selrispnew = selrisppre + "IIIIIIIII" + selrispost;
				
				qk.getSelrisp().setStringValue(selrispnew);
								
				DAOTableResultModel tableResult = null;

				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Inserimento");
	            
				Logger.getInstance().info(nomeMetodo.getStringValue() + ": Attivata modalità inserimento");
				//Scrittura tabella SK
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioSK",sk);
				}				
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "SK Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella SK");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				//Scrittura tabella QK
				try
				{
					tableResult = dao.executeTableInsertAccess("SalvaQuestionario",qk);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QK Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QK");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				//Scrittura tabella QM
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}

				//Scrittura tabella QM Importo 1
				impoString = AdeguatezzaUtility.padLeft(input.getImporto1().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM1 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM1");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 2
				impoString = AdeguatezzaUtility.padLeft(input.getImporto2().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM2 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM2");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}

				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 3
				impoString = AdeguatezzaUtility.padLeft(input.getImporto3().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM3 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM3");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 4
				impoString = AdeguatezzaUtility.padLeft(input.getImporto4().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM4 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM4");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 5
				impoString = AdeguatezzaUtility.padLeft(input.getImporto5().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM5 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM5");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 6
				impoString = AdeguatezzaUtility.padLeft(input.getImporto6().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM6 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM6");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 7
				impoString = AdeguatezzaUtility.padLeft(input.getImporto7().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM7 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM7");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 8
				impoString = AdeguatezzaUtility.padLeft(input.getImporto8().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM8 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM8");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
				numdomains = numdomains + 1;
				//Scrittura tabella QM Importo 9
				impoString = AdeguatezzaUtility.padLeft(input.getImporto9().toString(),'0',10);
				qm.setSelmult(new StringType(impoString));
				qm.setNumdoma(new IntegerType(numdomains));
				try
				{
					tableResult = dao.executeTableInsertAccess("InsertQuestionarioQM",qm);
				}
				catch (PrimaryKeyViolation pkv)
				{
					StringWriter sw = new StringWriter();
					pkv.printStackTrace(new PrintWriter(sw));
					esito.esito = "106";
					esito.setDescr("<![CDATA[" + "QM9 Questionario gia' inserito per la data/ora corrente: " + sw.toString() + "]]>");
					Logger.getInstance().error(pkv);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				if (tableResult.getResult().intValue() != 1)
				{
					esito.esito = "006";
					esito.setDescr("Inserimento non riuscito nella tabella QM9");
					Logger.getInstance().debug(esito.descrizione);
					output.setEsito(new StringType(esito.esito));
					output.setDescErr(new StringType(esito.descrizione));
					return output;								
				}
				
	            Logger.getInstance().debug(nomeMetodo.getStringValue() + ": Fine");		
			}catch(DAOException daoe){
				StringWriter sw = new StringWriter();
				daoe.printStackTrace(new PrintWriter(sw));
				esito.esito = "999";
				esito.setDescr("<![CDATA[" + "Errore SQL " + sw.toString() + "]]>");
				Logger.getInstance().error(daoe); 
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
			}catch(Exception e){
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
				esito.esito = "999";
				esito.setDescr("<![CDATA["+"Errore Generico " + sw.toString()+"]]>");
				Logger.getInstance().error(e); 
				output.setEsito(new StringType(esito.esito));
				output.setDescErr(new StringType(esito.descrizione));
			}finally{
				if(dao != null) dao.closeConnection();
			}
			return output;
		}
			
}
