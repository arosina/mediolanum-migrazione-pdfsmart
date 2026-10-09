package prgm.cedacri.adeguatezza.cache;

import java.io.*;
import java.util.*;
import javax.ejb.EJBException;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.*;
import com.atosorigin.wfem.dao.exceptions.*;
import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.util.*;
import prgm.cedacri.adeguatezza.internal.*;
import prgm.cedacri.adeguatezza.model.*;

public class CacheManager  
{
	private static final long serialVersionUID = 0;
	private static CacheManager singleton = null;
	private StringType dataRefresh = new StringType();
	private StringType applicazione = new StringType();

	private IntegerType   codIsti     = new IntegerType();
	private StringType    codAppl     = new StringType();
	private IntegerType   dfinval     = new IntegerType();
	private StringType    canVendRete    = new StringType();
	private StringType    canVendFiliale = new StringType();
	private StringType    seValidDef  = new StringType();
	private IntegerType   datBozz     = new IntegerType();
	private String        nomeDbFile  = new String();
	private StringType    espFinaDef  = new StringType();
	private IntegerType   filOperDef  = new IntegerType();
	private ListType      elencoDomande            = new ListType();
	private ListType      elencoDomandePG          = new ListType();	
	private ListType      elencoPunteggi           = new ListType();
	private ListType      elencoPunteggiPG         = new ListType();
	private ListType      elencoPesi               = new ListType();
	private ListType      elencoPesiPG             = new ListType();
	private ListType      verificaElencoRisposte   = new ListType();
	private ListType      verificaElencoRispostePG = new ListType();
	private ListType      domandeMultiple          = new ListType();
	private ListType      domandeMultiplePG        = new ListType();	
//questionario importo	
	private ListType      domandeImporto           = new ListType();
	private ListType      domandeImportoPG         = new ListType();
	private ListType      releaseCorrente          = new ListType();
	private ListType      releaseCorrentePG        = new ListType();
	private ListType      verificaElencoDomande    = new ListType();
	private ListType      verificaElencoDomandePG  = new ListType();
	private ListType      elenco860 = new ListType();
	private ListType      elenco861 = new ListType();
	private ListType      elenco862 = new ListType();
	private ListType      elenco863 = new ListType();
	private ListType      elenco864 = new ListType();
	private ListType      elenco865 = new ListType();
	private ListType      elenco866 = new ListType();
	private ListType      elenco867 = new ListType();
	private ListType      elenco868 = new ListType();
	private ListType      elenco869 = new ListType();
	
	protected CacheManager()
	{	
	}
	
	public static CacheManager getInstance(ClientSessionContext csc)
	{
		if (singleton == null)
		{
			synchronized (prgm.cedacri.adeguatezza.cache.CacheManager.class)
			{
				if (singleton == null)
				{
					singleton = new CacheManager();
					try
					{
						singleton.loadConfiguration(csc);
					}
					catch (Exception e)
					{
						singleton = null;
					}
				}
			}
		}
		return singleton;
	}
	
	public String refresh(ClientSessionContext csc) 
	{
		try
		{
			loadConfiguration(csc);
		}
		catch (Exception e)
		{
			return e.getMessage();
		}
		return this.getStatus();
	}

	public String getStatus() 
	{
		return "INITIALIZED at " + dataRefresh.getStringValue();
	}
	
	private void loadConfiguration(ClientSessionContext csc)
	{
		String errorMsg;
		EJBException e;
		try
		{
			InputStream inputStream = getClass().getResourceAsStream("/Adeguatezza.properties");
			if (inputStream == null)
			{
				errorMsg = "Adeguatezza configuration file [/Adeguatezza.properties] not found";
				e = new EJBException(errorMsg);
				throw e; 
			}
			else
			{
				Properties configurationProps = new Properties();
				configurationProps.load(inputStream);
				this.applicazione = new StringType(configurationProps.getProperty("APPLICAZIONE"));
				if (this.applicazione.getStringValue().equalsIgnoreCase("")) 
					this.applicazione.setStringValue(" "); 
				this.codIsti     = new IntegerType(configurationProps.getProperty("CODISTI"));
				this.codAppl     = new StringType(configurationProps.getProperty("CODAPPL"));
				if (this.codAppl.getStringValue().equalsIgnoreCase("")) 
					this.codAppl.setStringValue(" "); 
				this.dfinval     = new IntegerType(configurationProps.getProperty("DFINVAL"));
				this.canVendRete = new StringType(configurationProps.getProperty("CANVENDRETE"));
				if (this.canVendRete.getStringValue().equalsIgnoreCase(""))
					this.canVendRete.setStringValue(" "); 
				this.canVendFiliale = new StringType(configurationProps.getProperty("CANVENDFILIALE"));
				if (this.canVendFiliale.getStringValue().equalsIgnoreCase(""))
					this.canVendFiliale.setStringValue(" "); 
				this.seValidDef  = new StringType(configurationProps.getProperty("SEVALIDDEF"));
				if (this.seValidDef.getStringValue().equalsIgnoreCase("")) 
					this.seValidDef.setStringValue(" "); 
				this.datBozz     = new IntegerType(configurationProps.getProperty("DATBOZZ"));
				this.filOperDef  = new IntegerType(configurationProps.getProperty("FILOPERDEF"));
				this.nomeDbFile  = new String(configurationProps.getProperty("NOMEDBFILE"));
				if (this.nomeDbFile.equalsIgnoreCase("")) 
					this.nomeDbFile = " "; 
				this.espFinaDef  = new StringType(configurationProps.getProperty("ESPFINADEF"));
				if (this.espFinaDef.getStringValue().equalsIgnoreCase("")) 
					this.espFinaDef.setStringValue(" "); 

				DAOObject dao = null;
				DAOQueryResultModel queryResult = null;
				try
				{
					dao = new DAOObject(csc,nomeDbFile);
					dao.openConnection();

					queryResult = dao.executeQueryAccess("ElencoDomande",null);
					elencoDomande = queryResult.getResult();

					queryResult = dao.executeQueryAccess("ElencoDomandePG",null);
					elencoDomandePG = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("ElencoPunteggi",null);
					elencoPunteggi = queryResult.getResult();

					queryResult = dao.executeQueryAccess("ElencoPunteggiPG",null);
					elencoPunteggiPG = queryResult.getResult();
					
					ElementoPunteggiModel pModel = new ElementoPunteggiModel();
					pModel.setDominio(new StringType("PROFILO"));
					pModel.setValDomi(new IntegerType(0));
					elencoPunteggi.add(pModel);
					
//					ElementoPunteggiModel pModel = new ElementoPunteggiModel();
					pModel.setDominio(new StringType("PROFILO"));
					pModel.setValDomi(new IntegerType(0));
					elencoPunteggiPG.add(pModel);
					
					queryResult = dao.executeQueryAccess("ElencoPesi",null);
					elencoPesi = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("ElencoPesiPG",null);
					elencoPesiPG = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("VerificaElencoRisposte",null);
					verificaElencoRisposte = queryResult.getResult();

					queryResult = dao.executeQueryAccess("VerificaElencoRispostePG",null);
					verificaElencoRispostePG = queryResult.getResult();

					queryResult = dao.executeQueryAccess("DomandeMultiple",null);
					domandeMultiple = queryResult.getResult();

					queryResult = dao.executeQueryAccess("DomandeMultiplePG",null);
					domandeMultiplePG = queryResult.getResult();

//questionario importo					
					queryResult = dao.executeQueryAccess("DomandeImporto",null);
					domandeImporto = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("DomandeImportoPG",null);
					domandeImportoPG = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("ReleaseCorrente",null);
					releaseCorrente = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("ReleaseCorrentePG",null);
					releaseCorrentePG = queryResult.getResult();
					
					queryResult = dao.executeQueryAccess("VerificaElencoDomande",null);
					verificaElencoDomande = queryResult.getResult();

					queryResult = dao.executeQueryAccess("VerificaElencoDomandePG",null);
					verificaElencoDomandePG = queryResult.getResult();
					
					IntegerModel codtabe = new IntegerModel();
					codtabe.setValore(new IntegerType(860));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco860 = queryResult.getResult();
					
					codtabe.setValore(new IntegerType(861));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco861 = queryResult.getResult();
					
					codtabe.setValore(new IntegerType(862));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco862 = queryResult.getResult();

					codtabe.setValore(new IntegerType(863));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco863 = queryResult.getResult();
					
					codtabe.setValore(new IntegerType(864));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco864 = queryResult.getResult();

					codtabe.setValore(new IntegerType(865));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco865 = queryResult.getResult();

					codtabe.setValore(new IntegerType(866));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco866 = queryResult.getResult();

					codtabe.setValore(new IntegerType(867));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco867 = queryResult.getResult();

					codtabe.setValore(new IntegerType(868));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco868 = queryResult.getResult();

					codtabe.setValore(new IntegerType(869));
					queryResult = dao.executeQueryAccess("AdeguatezzaCache",codtabe);
					elenco869 = queryResult.getResult();
					
				}catch(DAOException daoe){
					errorMsg = "Adeguatezza Db Cache: Eccezione DAO .....: " + daoe;
					throw new EJBException(errorMsg); 
				}catch(Exception ex){
					errorMsg = "Adeguatezza Db Cache: Eccezione generica .....: " + ex;
					throw new EJBException(errorMsg); 
				}finally{
					if(dao != null) dao.closeConnection();
				}
				
				this.dataRefresh = new StringType(Tools.now().getStringValue()); 
			}
		}
		catch (Exception ioe)
		{
			errorMsg = "Adeguatezza configuration file [/Adeguatezza.properties] not found or malformed";
			throw new EJBException(errorMsg); 
		}
	}
	
	public PrgmCommands getCommand(StringType nomeMetodo)
	{
		return new PrgmCommands();
	}

	public StringType getApplicazione() {
		return applicazione;
	}

	public StringType getCanVendRete() {
		return canVendRete;
	}

	public StringType getCodAppl() {
		return codAppl;
	}

	public IntegerType getCodIsti() {
		return codIsti;
	}

	public StringType getDataRefresh() {
		return dataRefresh;
	}

	public IntegerType getDatBozz() {
		return datBozz;
	}

	public IntegerType getDfinval() {
		return dfinval;
	}

	public ListType getDomandeMultiple() {
		return domandeMultiple;
	}

	public ListType getDomandeMultiplePG() {
		return domandeMultiplePG;
	}

//questionario importo
	public ListType getDomandeImporto() {
		return domandeImporto;
	}

	public ListType getDomandeImportoPG() {
		return domandeImportoPG;
	}

	public ListType getElencoDomande() {
		return elencoDomande;
	}

	public ListType getElencoDomandePG() {
		return elencoDomandePG;
	}

	public ListType getElencoPesi() {
		return elencoPesi;
	}

	public ListType getElencoPesiPG() {
		return elencoPesiPG;
	}

	public ListType getElencoPunteggi() {
		return elencoPunteggi;
	}

	public ListType getElencoPunteggiPG() {
		return elencoPunteggiPG;
	}

	public StringType getEspFinaDef() {
		return espFinaDef;
	}

	public IntegerType getFilOperDef() {
		return filOperDef;
	}

	public String getNomeDbFile() {
		return nomeDbFile;
	}

	public ListType getReleaseCorrente() {
		return releaseCorrente;
	}

	public ListType getReleaseCorrentePG() {
		return releaseCorrentePG;
	}
	public StringType getSeValidDef() {
		return seValidDef;
	}

	public ListType getVerificaElencoRisposte() {
		return verificaElencoRisposte;
	}

	public ListType getVerificaElencoRispostePG() {
		return verificaElencoRispostePG;
	}

	public ListType getVerificaElencoDomande() {
		return verificaElencoDomande;
	}

	public ListType getVerificaElencoDomandePG() {
		return verificaElencoDomandePG;
	}

	public ListType getElenco860() {
		return elenco860;
	}

	public ListType getElenco861() {
		return elenco861;
	}

	public ListType getElenco862() {
		return elenco862;
	}

	public ListType getElenco863() {
		return elenco863;
	}

	public ListType getElenco864() {
		return elenco864;
	}

	public ListType getElenco865() {
		return elenco865;
	}

	public ListType getElenco866() {
		return elenco866;
	}

	public ListType getElenco867() {
		return elenco867;
	}

	public ListType getElenco868() {
		return elenco868;
	}

	public ListType getElenco869() {
		return elenco869;
	}
	
	public StringType getCanVendFiliale() {
		return canVendFiliale;
	}
}
