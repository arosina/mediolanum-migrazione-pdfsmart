package prgm.cedacri.adeguatezza;

import prgm.cedacri.adeguatezza.cache.*;
import prgm.cedacri.adeguatezza.internal.*;
import prgm.cedacri.adeguatezza.model.*;

import java.util.*;

import com.atosorigin.wfem.dao.*;
import com.atosorigin.wfem.dao.exceptions.*;
import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.util.Logger;

public class AdeguatezzaBusinessUtility 
{
	public static final String ndgDossNull = new String("00000000000");
	public static final String ndgTempNull = new String("0000000000000000");
	
	public static boolean CalcoloProfiloDefault(CacheManager cacheManager, DAOObject dao,
		Hashtable ht,IntegerType eta, StringType titStud, StatoAdeguatezza esito) throws DAOException
	{
		DAOQueryResultModel queryResult = null;
		InputDefaultProfiloModel inputDefault = new InputDefaultProfiloModel();
		inputDefault.setEta(eta);
		inputDefault.setTitStud(titStud);
		
		queryResult = dao.executeQueryAccess("GeneraProfiloDefault",inputDefault);
		if (queryResult.getResult().size() == 0)
		{
			esito.esito = "010";
			esito.setDescr("Valori di default non ammissibili o tabella default non valorizzata");
			return false;				
		}
		OutputDefaultProfiloModel outputDefault = (OutputDefaultProfiloModel)queryResult.getResult().get(0);
		ht.put("OBBINVE",outputDefault.getObbInve().getStringValue());
		ht.put("OBBTEMP",outputDefault.getObbTemp().getStringValue());
		ht.put("SITFINA",outputDefault.getSitFina().getStringValue());
		ht.put("PROFILO",outputDefault.getProfilo().getStringValue());
		ht.put("CLUSTER","        ");
		ht.put("DESCLUSTER","        ");
		ht.put("DFINVAL",outputDefault.getDfinval()); 
		return true; 
	}

	public static boolean CalcoloProfilo(CacheManager cacheManager, DAOObject dao,
		ListType risposte, ListType punteggi, Hashtable ht,IntegerType eta, StatoAdeguatezza esito, StringType pg) throws DAOException
	{
		DAOQueryResultModel profiloResult = null;
		IntegerType punteggioProfilo = new IntegerType("0");
/*v12*/ IntegerType punteggioEsigLiq = new IntegerType("0");
/*v12*/ IntegerType punteggioForzPro = new IntegerType("0");
/*v12*/ IntegerType punteggioProfFor = new IntegerType("0");
/*v12*/ IntegerType punteggioForzObT = new IntegerType("0");
/*141128*/ IntegerType punteggioEspFina = new IntegerType("0");
		int appoggio = 0;
		
// variabili di comodo per determinazione del cluster
		String wsProfilo = null;
/*v12*/ String wsProfiloForz = "   ";
		String wsProfFor = "   ";
		String wsFatTemp = null;
		String wsGesClus = "N";
		String wsChiave  = null;
		String wsRelease = null;
		String wsCluster = null;
		String wsEta = null;
		String wsTotS = null;
		String wsObbTemp = null;
/*v12*/ String wsEsigLiq = " ";
/*v12*/ String wsObbTempForz = " ";
/*v12*/ String wsObbTempOrig = " ";
/*v12*/ String wsClusterOrig = " ";
		/* 20140829 aggiunta Disc */
		String wsObbInve = " ";
		String wsSitFina = " ";
		String wsEspFina = " ";
		/* 20140829 aggiunta Disc */
		
		int wsTot ;
		TW00TBTZModel wsAppoggi = null;
		wsTot = 0;
		
		IntegerType wsReleaseI = new IntegerType();
		
		if (AdeguatezzaBusinessUtility.ReleaseCorrente(cacheManager,dao,wsReleaseI,esito,pg) == false)
		{
			esito.esito = "033";
			esito.setDescr("Release non determinata");
			return false;				
		}
		else
		{
			wsRelease = AdeguatezzaUtility.padLeft(wsReleaseI.getStringValue(),'0',3);
		}

		wsChiave = wsRelease; 
//		 Lettura tabella per determinare se è attiva la gestione del Cluster
		for (int j = 0; j < cacheManager.getElenco869().size(); j++)
		{
			wsAppoggi = (TW00TBTZModel)cacheManager.getElenco869().get(j); 
			if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
			{
				wsGesClus = wsAppoggi.getDati().getStringValue();
				break;
			}
			else
			{
				wsCluster = "        ";
				wsGesClus = "N";
			}
		}				
/*20111025:PG		
		ListType elencoPunteggi = cacheManager.getElencoPunteggi();
//20111025:PG*/
		ListType elencoPunteggi = new ListType();
		
		if (pg.equalsIgnoreCase("PG"))
		{
			elencoPunteggi = cacheManager.getElencoPunteggiPG();
		}
		else
		{
			elencoPunteggi = cacheManager.getElencoPunteggi();
		}
		
		if (elencoPunteggi.size() == 0)
		{
			esito.esito = "003";
			esito.setDescr("Tabella punteggi non valorizzata");
			return false;				
		}
/*20111025:PG
		ListType elencoPesi = cacheManager.getElencoPesi();
//20111025:PG*/
		ListType elencoPesi = new ListType();

		if (pg.equalsIgnoreCase("PG"))
		{
			elencoPesi = cacheManager.getElencoPesiPG();
		}
		else
		{
			elencoPesi = cacheManager.getElencoPesi();
		}
		
		if (elencoPesi.size() == 0)
		{
			esito.esito = "003";
			esito.setDescr("Tabella pesi non valorizzata");
			return false;				
		}
		
		ElementoPesiModel ePesiModel = null;
		ElementoPunteggiModel ePunteggiModelIngresso = null;
		ElementoPunteggiModel ePunteggiModelUscita = null;
		ElementoQuestionarioRisposteModel eRisposteModel = null;
		
		for (int i=0;i<elencoPunteggi.size();i++)
		{
			ePunteggiModelIngresso = (ElementoPunteggiModel)elencoPunteggi.get(i);
			ePunteggiModelUscita = new ElementoPunteggiModel();
			ePunteggiModelUscita.setDominio(ePunteggiModelIngresso.getDominio());
			ePunteggiModelUscita.setValDomi(ePunteggiModelIngresso.getValDomi());
						
			for (int j=0;j<elencoPesi.size();j++)
			{
				ePesiModel = (ElementoPesiModel)elencoPesi.get(j);
				if (ePesiModel.getDominio().equalsIgnoreCase(ePunteggiModelIngresso.getDominio().getStringValue()))
				{
					for(int k=0;k<risposte.size();k++)
					{
						eRisposteModel = (ElementoQuestionarioRisposteModel)risposte.get(k);
						if	((eRisposteModel.getNumElem().intValue() == ePesiModel.getNumDoma().intValue())
							&& (eRisposteModel.getNumSele().intValue() == ePesiModel.getNumRisp().intValue()))
						{
							ePunteggiModelUscita.setValDomi(ePunteggiModelUscita.getValDomi().add(ePesiModel.getPesoRis()));
							break;
						}
					}
				}
			}

/* vecchia gestione			
			if (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("PROFILO"))
			{
				if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("OBBINVE"))
				{
					punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi().multiply(new IntegerType("100")));
					punteggi.add(ePunteggiModelUscita);					
				}
				else
				{
					punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi());
					punteggi.add(ePunteggiModelUscita);
					if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("OBBTEMP"))
					{
						wsObbTemp = punteggioProfilo.intValue();
					}
				}
			}
*/			
//	Se è attiva la nuova gestione del Cluster cambia il calcolo del profilo

			if (wsGesClus.charAt(0) == 'S')
			{
				if ((!ePunteggiModelUscita.getDominio().equalsIgnoreCase("PROFILO"))	
/*v12*/			 && (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("FORZPRO"))
				 && (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("PROFFOR"))
/*v12*/			 && (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("ESIGLIQ"))
/*v12*/			 && (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("FORZOBT"))
				 && (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("ESPFINA")))
				{
					if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("OBBINVE"))
					{
						punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi().multiply(new IntegerType("100")));
						punteggi.add(ePunteggiModelUscita);					
					}
					else
					{
						if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("SITFINA"))
						{
							punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi().multiply(new IntegerType("100")));
							punteggi.add(ePunteggiModelUscita);					
						}
						else
						{
							punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi());
							punteggi.add(ePunteggiModelUscita);
 						}
					}
				}
				else
				{
/*v12*/				if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("FORZPRO"))
/*v12*/				{
/*v12*/					punteggioForzPro = punteggioForzPro.add(ePunteggiModelUscita.getValDomi());
/*v12*/					punteggi.add(ePunteggiModelUscita);
/*v12*/				}
					if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("PROFFOR"))
					{
						punteggioProfFor = punteggioProfFor.add(ePunteggiModelUscita.getValDomi());	
						punteggi.add(ePunteggiModelUscita);
					}
/*v12*/				if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("ESIGLIQ"))
/*v12*/				{
/*v12*/					punteggioEsigLiq = punteggioEsigLiq.add(ePunteggiModelUscita.getValDomi());
/*v12*/					punteggi.add(ePunteggiModelUscita);
/*v12*/				}
/*v12*/				if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("FORZOBT"))
/*v12*/				{
/*v12*/					punteggioForzObT = punteggioForzObT.add(ePunteggiModelUscita.getValDomi());
/*v12*/					punteggi.add(ePunteggiModelUscita);
/*v12*/				}

/*141128*/			if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("ESPFINA"))
/*141128*/			{
/*141128*/				punteggioEspFina = punteggioEspFina.add(ePunteggiModelUscita.getValDomi());
/*141128*/				punteggi.add(ePunteggiModelUscita);
/*141128*/			}

				}	
			}
			else
			{
				if (!ePunteggiModelUscita.getDominio().equalsIgnoreCase("PROFILO"))
				{
					if (ePunteggiModelUscita.getDominio().equalsIgnoreCase("OBBINVE"))
					{
						punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi().multiply(new IntegerType("100")));
						punteggi.add(ePunteggiModelUscita);					
					}
					else
					{
						punteggioProfilo = punteggioProfilo.add(ePunteggiModelUscita.getValDomi());
						punteggi.add(ePunteggiModelUscita);
					}
				}
			}
		}
		
		//Calcolo del PROFILO
		ePunteggiModelIngresso = new ElementoPunteggiModel();
		ePunteggiModelIngresso.setDominio(new StringType("PROFILO"));
		ePunteggiModelIngresso.setValDomi(punteggioProfilo);
		
/*20111025:PG
		profiloResult = dao.executeQueryAccess("GeneraProfilo",ePunteggiModelIngresso);
//20111025:PG */
		
		if (pg.equalsIgnoreCase("PG"))
		{
			profiloResult = dao.executeQueryAccess("GeneraProfiloPG",ePunteggiModelIngresso);
		}
		else
		{
			profiloResult = dao.executeQueryAccess("GeneraProfilo",ePunteggiModelIngresso);
		}
		if (profiloResult.getResult().size() == 0)
		{
			esito.esito = "008";
			esito.setDescr("Tabella profili con errori di valorizzazione");
			return false;				
		}
	
//	salvataggio del Profilo
		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("PROFILO"))
		{
			wsProfilo = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
		}
	
//	salvataggio del Obiettivo temporale
		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("OBBTEMP"))
		{
			wsObbTemp = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/		wsObbTempOrig = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
		}
		
//	salvataggio della Forzatura dell'obiettivo temporale
/*v12*/	if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("FORZOBT"))
/*v12*/	{
/*v12*/		wsObbTempForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/	}
		
//	salvataggio della Esigenza di liquidità

/*v12*/	if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("ESIGLIQ"))
/*v12*/	{
/*v12*/		wsEsigLiq = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/	}

//	salvataggio della forzatura del profilo

/*v12*/	if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("PROFFOR"))
/*v12*/	{
/*v12*/		//	wsProfiloForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
				wsProfFor = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
				appoggio = 1;	
		}

/*v12*/	if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("FORZPRO") && appoggio == 0)
/*v12*/	{
/*v12*/		wsProfiloForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/	}
		
		ht.put(ePunteggiModelIngresso.getDominio().getStringValue(),((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue());
		
		appoggio = 0;
		
		//Calcolo dei valori associati agli altri domini
		for (int i=0;i<punteggi.size();i++)
		{
			ePunteggiModelIngresso = (ElementoPunteggiModel)punteggi.get(i);
        //  System.out.println("Ciclo n. = "+i+" - Dominio = "+ePunteggiModelIngresso.getDominio());
			
/*20111025:PG
			profiloResult = dao.executeQueryAccess("GeneraProfilo",ePunteggiModelIngresso);
//	20111025:PG */
			
			if (pg.equalsIgnoreCase("PG"))
			{
				profiloResult = dao.executeQueryAccess("GeneraProfiloPG",ePunteggiModelIngresso);
			}
			else
			{
				profiloResult = dao.executeQueryAccess("GeneraProfilo",ePunteggiModelIngresso);
			}
			
			if (profiloResult.getResult().size() == 0)
			{
				esito.esito = "008";
				esito.setDescr("Tabella profili con errori di valorizzazione");
				return false;				
			}
			
//	salvataggio del Profilo
			if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("PROFILO"))
			{
				wsProfilo = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
			}

//	salvataggio del Obiettivo temporale
			if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("OBBTEMP"))
			{
				wsObbTemp = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/			wsObbTempOrig = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
			}
			
//	salvataggio della Forzatura dell'obiettivo temporale
/*v12*/		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("FORZOBT"))
/*v12*/		{
/*v12*/			wsObbTempForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/		}

//	salvataggio della Esigenza di liquidità
/*v12*/		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("ESIGLIQ"))
/*v12*/		{
/*v12*/			wsEsigLiq = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
/*v12*/		}

//	salvataggio della forzatura del profilo

/*v12*/		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("PROFFOR"))
/*v12*/		{
/*v12*/		//	wsProfiloForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
				wsProfFor = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
				appoggio = 1;
			//	System.out.println("-----> wsProfFor: "+wsProfFor );
			
			}
/*v12*/		if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("FORZPRO") && appoggio == 0)
/*v12*/		{
/*v12*/			wsProfiloForz = ((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue();
			//	System.out.println("-----> wsProfiloForz: "+wsProfiloForz);
			}

			/* 20140829 aggiunta Disc */
			if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("OBBINVE")) 
			{
				wsObbInve = ((StringModel) profiloResult.getResult().get(0)).getValore().getStringValue();
			}
			
			if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("SITFINA")) 
			{
				wsSitFina = ((StringModel) profiloResult.getResult().get(0)).getValore().getStringValue();
			}
			
			if (ePunteggiModelIngresso.getDominio().equalsIgnoreCase("ESPFINA")) 
			{
				wsEspFina = ((StringModel) profiloResult.getResult().get(0)).getValore().getStringValue();
/*141128*/		//System.out.println("wsEspFina ------------- "+wsEspFina);
				
			}
			/* 20140829 aggiunta Disc */
			ht.put(ePunteggiModelIngresso.getDominio().getStringValue(),((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue());
		
			//System.out.println("__________________ FINE CICLO "+i);			
		}

/*v12 	se il campo della forzatura del profilo è valorizzato con uno dei valori ammessi, forzo anche il profilo */
/*v12*/	if ((wsProfiloForz.equalsIgnoreCase("CON"))
/*v12*/	  ||(wsProfiloForz.equalsIgnoreCase("EQU"))
/*v12*/	  ||(wsProfiloForz.equalsIgnoreCase("INT")))
/*v12*/	{
/*v12*/		wsProfilo = wsProfiloForz;
/*v12*/		ht.put("PROFILO",wsProfilo);
/*v12*/	}

		if ((wsProfFor.equalsIgnoreCase("CON"))
	      ||(wsProfFor.equalsIgnoreCase("EQU"))
	      ||(wsProfFor.equalsIgnoreCase("INT")))
		{
			wsProfilo = wsProfFor;
			ht.put("PROFILO",wsProfilo);
		}
		

System.out.println(" ------------------------------ Punteggio Tot. = "+punteggioProfilo);
/*System.out.println(" ------------------------------ Punteggio proffor = "+punteggioProfFor);
System.out.println(" ------------------------------ Punteggio forzpro = "+punteggioForzPro);
*/
		
/*v12*/ ht.put("ESIGLIQ",wsEsigLiq);
/*v12*/ ht.put("FORZOBT",wsObbTempForz);
/*v12*/	ht.put("ORIGOBT",wsObbTempOrig);
/*v12*/ ht.put("FORZPRO",wsProfiloForz);
		/* 20140829 aggiunta Disc */
		ht.put("OBBTEMP",wsObbTemp);
		ht.put("OBBINVE",wsObbInve);
		ht.put("SITFINA",wsSitFina);
		ht.put("ESPFINA",wsEspFina);
		/* 20140829 aggiunta Disc */
		
// Ricerca Cluster ------------------------------------------------------------------------------------------------
		
// Se è attiva la gestione del Cluster ricerca tabella 866 per avere il punteggio dell'età
		if (wsGesClus.charAt(0) == 'S')
		{
//			wsEta = eta.toString();
//			wsChiave = wsRelease + AdeguatezzaUtility.padLeft(wsEta,'0',3);
//			for (int j = 0; j < cacheManager.getElenco866().size(); j++)
//			{
//				wsAppoggi = (TW00TBTZModel)cacheManager.getElenco866().get(j); 
//				if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
//				{
//					wsTotS = wsAppoggi.getDati().getStringValue();
//					wsTot = Integer.parseInt(wsTotS);
//					wsTot = wsTot + wsObbTemp ;
//					break;
//				}
//			}
//			if (wsTotS == null)
//			{
//				wsTot = 0 + wsObbTemp ;
//			}
// Lettura della tabella 867 per avere il valore del fattore tempo che è dato dalla somma dell'obbiettivo temporale e dell'età
//			wsTotS = Integer.toString(wsTot); 
//			wsChiave = wsRelease + AdeguatezzaUtility.padLeft(wsTotS,'0',3);
//			for (int j = 0; j < cacheManager.getElenco867().size(); j++)
//			{
//				wsAppoggi = (TW00TBTZModel)cacheManager.getElenco867().get(j); 
//				if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
//				{
//					wsFatTemp = wsAppoggi.getDati().getStringValue();
//					break;
//				}
//			}
			
// Lettura della tabella 868 per avere il valore del Cluster in base al fattore tempo e il profilo del cliente
			wsChiave = wsRelease + wsProfilo + wsObbTemp;

			for (int j = 0; j < cacheManager.getElenco868().size(); j++)
			{
				wsAppoggi = (TW00TBTZModel)cacheManager.getElenco868().get(j); 
				if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
				{
					wsCluster = wsAppoggi.getDati().getStringValue();
					break;
				}
				else
				{
					wsCluster = null;
				}
			}
			if (wsCluster == null)
			{
				esito.esito = "003";
				esito.setDescr("Cluster non determinato");
				return false;				
			}
		}
		
//		ePunteggiModelIngresso = new ElementoPunteggiModel();
//		ePunteggiModelIngresso.setDominio(new StringType("CLUSTER"));
//		ePunteggiModelIngresso.setValDomi(wsCluster.valueOf());

 		ht.put("CLUSTER",wsCluster);
 		ht.put("DESCLUSTER","   ");
 		
/*v12*/ wsClusterOrig = wsCluster;
/*v12*/ ht.put("ORIGCLU",wsClusterOrig);
/*v12*/	ht.put("DESORIGCLU","   ");
 		
 		if (wsCluster.equalsIgnoreCase("CLUSTER1"))
 		{
 			ht.put("DESCLUSTER","Intraprendente Lungo");
/*v12*/ 	ht.put("DESORIGCLU","Intraprendente Lungo");	

 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER2"))
 		{
 			ht.put("DESCLUSTER","Intraprendente Medio");
/*v12*/ 	ht.put("DESORIGCLU","Intraprendente Medio");			
 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER3"))
 		{
 			ht.put("DESCLUSTER","Intraprendente Breve");
/*v12*/ 	ht.put("DESORIGCLU","Intraprendente Breve");
		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER4"))
 		{
 			ht.put("DESCLUSTER","Equilibrato Lungo");
/*v12*/ 	ht.put("DESORIGCLU","Equilibrato Lungo");
 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER5"))
 		{
 			ht.put("DESCLUSTER","Equilibrato Medio");
/*v12*/ 	ht.put("DESORIGCLU","Equilibrato Medio");
		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER6"))
 		{
 			ht.put("DESCLUSTER","Equilibrato Breve");
/*v12*/ 	ht.put("DESORIGCLU","Equilibrato Breve");
 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER7"))
 		{
 			ht.put("DESCLUSTER","Conservatore Lungo");
/*v12*/ 	ht.put("DESORIGCLU","Conservatore Lungo");
 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER8"))
 		{
 			ht.put("DESCLUSTER","Conservatore Medio");
/*v12*/ 	ht.put("DESORIGCLU","Conservatore Medio");
 		}
 		if (wsCluster.equalsIgnoreCase("CLUSTER9"))
 		{
 			ht.put("DESCLUSTER","Conservatore Breve");
/*v12*/ 	ht.put("DESORIGCLU","Conservatore Breve");
 		}
		
/*v12	Se è attiva la gestione del Cluster ed è stato forzato l'obiettivo temporale */
/*v12*/	if ((wsGesClus.charAt(0) == 'S')
/*v12*/ &&  ((wsObbTempForz.equalsIgnoreCase("B"))
/*v12*/   || (wsObbTempForz.equalsIgnoreCase("M"))		
/*v12*/   || (wsObbTempForz.equalsIgnoreCase("L"))))		
/*v12*/	{
            wsObbTemp = wsObbTempForz;	//YC
/*v12*/		ht.put("OBBTEMP",wsObbTempForz);
/*v12 Lettura della tabella 868 per avere il valore del Cluster in base al fattore tempo e il profilo del cliente */
/*v12*/		wsChiave = wsRelease + wsProfilo + wsObbTempForz;
/*v12*/		for (int j = 0; j < cacheManager.getElenco868().size(); j++)
/*v12*/		{
/*v12*/			wsAppoggi = (TW00TBTZModel)cacheManager.getElenco868().get(j); 
/*v12*/			if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
/*v12*/			{
/*v12*/				wsCluster = wsAppoggi.getDati().getStringValue();
/*v12*/				break;
/*v12*/			}
/*v12*/			else
/*v12*/			{
/*v12*/				wsCluster = null;
/*v12*/			}
/*v12*/		}
/*v12*/		if (wsCluster == null)
/*v12*/		{
/*v12*/			esito.esito = "003";
/*v12*/			esito.setDescr("Cluster non determinato");
/*v12*/			return false;				
/*v12*/		}
/*v12*/	}
/*v12*/
/*v12*/	ht.put("CLUSTER",wsCluster);
/*v12*/	ht.put("DESCLUSTER","   ");
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER1"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Intraprendente Lungo");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER2"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Intraprendente Medio");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER3"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Intraprendente Breve");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER4"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Equilibrato Lungo");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER5"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Equilibrato Medio");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER6"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Equilibrato Breve");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER7"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Conservatore Lungo");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER8"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Conservatore Medio");
/*v12*/	}
/*v12*/	if (wsCluster.equalsIgnoreCase("CLUSTER9"))
/*v12*/	{
/*v12*/		ht.put("DESCLUSTER","Conservatore Breve");
/*v12*/	} 		


/*Inizio nuova gestione per forzatura - YC*/

		try {
			if (wsGesClus != null && wsGesClus.charAt(2) == '1'){
				
				boolean backupOrigCluster = true;
				
				//Downgrade da INT -->  EQU
				if(risposte != null && wsProfilo != null && wsProfilo.equalsIgnoreCase("INT")){
					
					/*INIZIO LOG*/
					/*ElementoQuestionarioRisposteModel rispostaModelLog = null;
					for(int i=0; i<risposte.size(); i++){
						rispostaModelLog = (ElementoQuestionarioRisposteModel)risposte.get(i);
						
						if(rispostaModelLog != null && rispostaModelLog.getNumSele() != null && rispostaModelLog.getNumElem() != null){
							System.out.println("Domanda : " + rispostaModelLog.getNumElem().intValue() + "; Risposta : "+rispostaModelLog.getNumSele().intValue());
						}
					}*/
					/*FINE LOG*/
					
					boolean rispostaB2eqA = false;
					boolean rispostaB4eqAorB = false;
					boolean rispostaB5eqCorD = false;
					
					boolean rispostaB1eqA = false;
					boolean rispostaB3eqA = false;
					
					ElementoQuestionarioRisposteModel rispostaModel = null;
					
					for(int i=0; i<risposte.size(); i++){
						rispostaModel = (ElementoQuestionarioRisposteModel)risposte.get(i);
						
						if(rispostaModel != null && rispostaModel.getNumElem() != null && rispostaModel.getNumSele() != null) {
							
							if(rispostaModel.getNumElem().intValue() == 5 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB2eqA = true;
							}
							
							if(rispostaModel.getNumElem().intValue() == 7 
									&& (rispostaModel.getNumSele().intValue() == 1 || rispostaModel.getNumSele().intValue() == 2)){
								rispostaB4eqAorB = true;
							}	
							
							if(rispostaModel.getNumElem().intValue() == 8 
									&& (rispostaModel.getNumSele().intValue() == 5 || rispostaModel.getNumSele().intValue() == 6
									||rispostaModel.getNumSele().intValue() == 7 || rispostaModel.getNumSele().intValue() == 8
									||rispostaModel.getNumSele().intValue() == 9 || rispostaModel.getNumSele().intValue() == 10)){
								rispostaB5eqCorD = true;
							}	
							
							if(rispostaModel.getNumElem().intValue() == 4 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB1eqA = true;
							}
							
							if(rispostaModel.getNumElem().intValue() == 6 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB3eqA = true;
							}							
							
						}
											
					}
					
					if(rispostaB2eqA && rispostaB4eqAorB && rispostaB5eqCorD){
						
						//backup dei valori precedenti di cluster e descrizione
						ht.put("ORIGCLU",ht.get("CLUSTER"));
						ht.put("DESORIGCLU",ht.get("DESCLUSTER"));
						backupOrigCluster = false;
						
						if(!rispostaB1eqA || rispostaB3eqA){
							wsProfilo = "CON";																								
						} else {													
							wsProfilo = "EQU";																						
						}
						ht.put("PROFILO",wsProfilo); //salvo il profilo							
						


						// Lettura della tabella 868 per avere il valore del Cluster in base al fattore tempo e il profilo del cliente
						wsChiave = wsRelease + wsProfilo + wsObbTemp;

						for (int j = 0; j < cacheManager.getElenco868().size(); j++)
						{
							wsAppoggi = (TW00TBTZModel)cacheManager.getElenco868().get(j); 
							if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
							{
								wsCluster = wsAppoggi.getDati().getStringValue();
								break;
							}
							else
							{
								wsCluster = null;
							}
						}
						if (wsCluster == null)
						{
							esito.esito = "003";
							esito.setDescr("Cluster non determinato");
							return false;				
						}	
						
						ht.put("CLUSTER",wsCluster);
																 
						// Determino la nuova descrizione per il cluster
						if (wsCluster.equalsIgnoreCase("CLUSTER1")){
							ht.put("DESCLUSTER","Intraprendente Lungo");	
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER2")){
							ht.put("DESCLUSTER","Intraprendente Medio");		
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER3")){
							ht.put("DESCLUSTER","Intraprendente Breve");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER4")){
							ht.put("DESCLUSTER","Equilibrato Lungo");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER5")){
							ht.put("DESCLUSTER","Equilibrato Medio");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER6")){
							ht.put("DESCLUSTER","Equilibrato Breve");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER7")){
							ht.put("DESCLUSTER","Conservatore Lungo");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER8")){
							ht.put("DESCLUSTER","Conservatore Medio");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER9")){
							ht.put("DESCLUSTER","Conservatore Breve");
						}					
						
					}
								
				}
				
				// Downgrade da EQU --> CON
				if(risposte != null && wsProfilo != null && wsProfilo.equalsIgnoreCase("EQU")){
					
					/*INIZIO LOG*/
					/*ElementoQuestionarioRisposteModel rispostaModelLog = null;
					for(int i=0; i<risposte.size(); i++){
						rispostaModelLog = (ElementoQuestionarioRisposteModel)risposte.get(i);
						
						if(rispostaModelLog != null && rispostaModelLog.getNumSele() != null && rispostaModelLog.getNumElem() != null){
							System.out.println("Domanda : " + rispostaModelLog.getNumElem().intValue() + "; Risposta : "+rispostaModelLog.getNumSele().intValue());
						}
					}*/
					/*FINE LOG*/
					
					boolean rispostaB1eqA = false;
					boolean rispostaB3eqA = false;
					
					boolean rispostaB2eqA = false;
					boolean rispostaB4eqAorB = false;
					boolean rispostaB5eqCorD = false;
					
					ElementoQuestionarioRisposteModel rispostaModel = null;
					
					for(int i=0; i<risposte.size(); i++){
						rispostaModel = (ElementoQuestionarioRisposteModel)risposte.get(i);
						
						if(rispostaModel != null && rispostaModel.getNumElem() != null && rispostaModel.getNumSele() != null) {
													
							if(rispostaModel.getNumElem().intValue() == 4 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB1eqA = true;
							}		

							if(rispostaModel.getNumElem().intValue() == 6 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB3eqA = true;
							}
							
							if(rispostaModel.getNumElem().intValue() == 5 && rispostaModel.getNumSele().intValue() == 1){
								rispostaB2eqA = true;
							}
							
							if(rispostaModel.getNumElem().intValue() == 7 
									&& (rispostaModel.getNumSele().intValue() == 1 || rispostaModel.getNumSele().intValue() == 2)){
								rispostaB4eqAorB = true;
							}	
							
							if(rispostaModel.getNumElem().intValue() == 8 
									&& (rispostaModel.getNumSele().intValue() == 5 || rispostaModel.getNumSele().intValue() == 6
									||rispostaModel.getNumSele().intValue() == 7 || rispostaModel.getNumSele().intValue() == 8
									||rispostaModel.getNumSele().intValue() == 9 || rispostaModel.getNumSele().intValue() == 10)){
								rispostaB5eqCorD = true;
							}								
							
						}
											
					}
					
					if((rispostaB2eqA && rispostaB4eqAorB && rispostaB5eqCorD) && (!rispostaB1eqA || rispostaB3eqA)){
						
						//backup dei valori precedenti di cluster e descrizione
						if(backupOrigCluster){
							ht.put("ORIGCLU",ht.get("CLUSTER"));
							ht.put("DESORIGCLU",ht.get("DESCLUSTER"));							
						}
												
						wsProfilo = "CON";																
						ht.put("PROFILO",wsProfilo); //salvo il profilo

						// Lettura della tabella 868 per avere il valore del Cluster in base al fattore tempo e il profilo del cliente
						wsChiave = wsRelease + wsProfilo + wsObbTemp;

						for (int j = 0; j < cacheManager.getElenco868().size(); j++)
						{
							wsAppoggi = (TW00TBTZModel)cacheManager.getElenco868().get(j); 
							if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
							{
								wsCluster = wsAppoggi.getDati().getStringValue();
								break;
							}
							else
							{
								wsCluster = null;
							}
						}
						if (wsCluster == null)
						{
							esito.esito = "003";
							esito.setDescr("Cluster non determinato");
							return false;				
						}	
						
						ht.put("CLUSTER",wsCluster);
																 
						// Determino la nuova descrizione per il cluster
						if (wsCluster.equalsIgnoreCase("CLUSTER1")){
							ht.put("DESCLUSTER","Intraprendente Lungo");	
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER2")){
							ht.put("DESCLUSTER","Intraprendente Medio");		
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER3")){
							ht.put("DESCLUSTER","Intraprendente Breve");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER4")){
							ht.put("DESCLUSTER","Equilibrato Lungo");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER5")){
							ht.put("DESCLUSTER","Equilibrato Medio");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER6")){
							ht.put("DESCLUSTER","Equilibrato Breve");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER7")){
							ht.put("DESCLUSTER","Conservatore Lungo");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER8")){
							ht.put("DESCLUSTER","Conservatore Medio");
						}
						if (wsCluster.equalsIgnoreCase("CLUSTER9")){
							ht.put("DESCLUSTER","Conservatore Breve");
						}					
						
					}
								
				}
									
			}
		} 
		catch (StringIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println("StringIndexOutOfBoundsException - Non trovato carattere alla posizione 3 della parte dati della tabella 869");
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Eccezione generica - Forzatura profilo NON eseguita.");
		}

/*Fine nuova gestione per forzatura - YC*/



 		
		int wsDatFinv;
		IntegerType wsDatComp;
		String wsDurataS = null;
		char wsDurataC;
		int wsDurataI ;			
 		
//		wsGesClus = wsAppoggi.getDati().getStringValue();
		wsDurataC = wsGesClus.charAt(1);
		wsDurataS = String.valueOf(wsDurataC); 
		wsDurataI = Integer.parseInt(wsDurataS);
		wsDatComp = AdeguatezzaUtility.getDateModel();
		wsDatFinv = wsDatComp.intValue() + (wsDurataI * 10000);
		Integer wsDatFinvI = new Integer(wsDatFinv);
		ht.put("DFINVAL",wsDatFinvI);
 		
//		ht.put(ePunteggiModelIngresso.getDominio().getStringValue(),((StringModel)profiloResult.getResult().get(0)).getValore().getStringValue());
		
// Ricerca Cluster ------------------------------------------------------------------------------------------------
		
		return true;
	}
	
	public static boolean VerificaQuestionarioNonRisposto(ListType risposte) throws Exception
	{
		ElementoQuestionarioRisposteModel qModel = null;
		qModel = (ElementoQuestionarioRisposteModel)risposte.get(0);
		if ((risposte.size() == 1) && (qModel.getNumElem().intValue()==0) && (qModel.getNumSele().intValue()==0))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static boolean VerificaQuestionario(CacheManager cacheManager,  
		ListType risposte,StatoAdeguatezza esito,StringType pg) throws DAOException
	{
		if (cacheManager.getVerificaElencoDomande().size() == 0)
		{
			esito.esito = "003";
			esito.setDescr("Tabella domande non valorizzata");
			return false;
		}
		
		ListType verificaElencoDomande = new ListType();
		RisposteVerificaModel rvModelIn = null;
		RisposteVerificaModel rvModelOut = null;
/*v14*/	int wsFacoltativa;
/*v14*/	wsFacoltativa = 20;
/*v14*/	IntegerType wsNonRisposto = new IntegerType("0");
				
/*20111025:PG  
		for (int i=0;i<cacheManager.getVerificaElencoDomande().size();i++)
		{
			rvModelIn = (RisposteVerificaModel)cacheManager.getVerificaElencoDomande().get(i);
			rvModelOut = new RisposteVerificaModel();
			rvModelOut.setNumDoma (new IntegerType(rvModelIn.getNumDoma().intValue()));
			rvModelOut.setRisMult (new StringType (rvModelIn.getRisMult().getStringValue()));
			rvModelOut.setRisposto(new StringType (rvModelIn.getRisposto().getStringValue()));
			verificaElencoDomande.add(rvModelOut);
		}
		ListType verificaElencoRisposte = cacheManager.getVerificaElencoRisposte();
//		20111025:PG */ 

		ListType verificaElencoRisposte = new ListType();
		if (pg.equalsIgnoreCase("PG"))
		{
			for (int i=0;i<cacheManager.getVerificaElencoDomandePG().size();i++)
			{
				rvModelIn = (RisposteVerificaModel)cacheManager.getVerificaElencoDomandePG().get(i);
				rvModelOut = new RisposteVerificaModel();
				rvModelOut.setNumDoma (new IntegerType(rvModelIn.getNumDoma().intValue()));
				rvModelOut.setRisMult (new StringType (rvModelIn.getRisMult().getStringValue()));
				rvModelOut.setRisposto(new StringType (rvModelIn.getRisposto().getStringValue()));
				verificaElencoDomande.add(rvModelOut);
			}
			verificaElencoRisposte = cacheManager.getVerificaElencoRispostePG();
		}
		else
		{
			for (int i=0;i<cacheManager.getVerificaElencoDomande().size();i++)
			{
				rvModelIn = (RisposteVerificaModel)cacheManager.getVerificaElencoDomande().get(i);
				rvModelOut = new RisposteVerificaModel();
				rvModelOut.setNumDoma (new IntegerType(rvModelIn.getNumDoma().intValue()));
				rvModelOut.setRisMult (new StringType (rvModelIn.getRisMult().getStringValue()));
				rvModelOut.setRisposto(new StringType (rvModelIn.getRisposto().getStringValue()));
				verificaElencoDomande.add(rvModelOut);
			}
			verificaElencoRisposte = cacheManager.getVerificaElencoRisposte();
		}
		
/*121009*/ 	String wsReleaseS = null;
/*121009*/ 	String wsRelease  = null;
/*121009*/	String wsChiave   = null;
/*121009*/  ListType releaseLT = new ListType();
/*121009*/  IntegerType releaseI = new IntegerType();

/*121009*/	if (pg.equalsIgnoreCase("PG"))
/*121009*/	{
/*121009*/		releaseLT = cacheManager.getReleaseCorrentePG();
/*121009*/  }
/*121009*/	else
/*121009*/	{
/*121009*/		releaseLT = cacheManager.getReleaseCorrente();
/*121009*/  }
/*121009*/	releaseI.setBigValue(((IntegerModel)releaseLT.get(0)).getValore().bigValue());			
/*121009*/	wsReleaseS = releaseI.toString();
/*121009*/  wsRelease = AdeguatezzaUtility.padLeft(wsReleaseS,'0',3);

		if (verificaElencoRisposte.size() == 0)
		{
			esito.esito = "003";
			esito.setDescr("Tabella risposte non valorizzata");
			return false;
		}

		//Verifica risposte fornite
		ElementoQuestionarioRisposteModel qModelDaVerificare = null;
		ElementoQuestionarioRisposteModel qModel = null;
		RisposteVerificaModel vModel = null;
		boolean trovato = false;

		for (int j=0;j<risposte.size();j++)
		{
			//estraggo la risposta fornita dall'utente
			qModelDaVerificare = (ElementoQuestionarioRisposteModel)risposte.get(j);
			//verifico che esista una risposta con numdomanda/numrisposta pari a quelli forniti
			trovato = false;

			for (int i=0;i<verificaElencoRisposte.size();i++)
			{
				qModel = (ElementoQuestionarioRisposteModel)verificaElencoRisposte.get(i);	
				
				if ((qModel.getNumElem().intValue() == qModelDaVerificare.getNumElem().intValue())
					&& (qModel.getNumSele().intValue() == qModelDaVerificare.getNumSele().intValue()))
				{
					trovato = true;
					break;			
				}
				
//questionario importo
				
			//	vModel = (RisposteVerificaModel)verificaElencoDomande.get(i);
			//	if (vModel.getRisMult().equalsIgnoreCase("I"))
			//	{
			//		trovato = true;
			//		break;
			//	}
/*v13*/		//		if (vModel.getRisMult().equalsIgnoreCase("F"))
/*v13*/		//		{
/*v13*/		//			trovato = true;
/*v13*/		//			break;
/*v13*/		//	 	}
			
/*20141024*/	
	     	   if (verificaElencoDomande.size() > i)
			   {
	     		   	vModel = (RisposteVerificaModel)verificaElencoDomande.get(i);
					if (vModel.getRisMult().equalsIgnoreCase("I"))
					{
						trovato = true;
						break;
					}
					if (vModel.getRisMult().equalsIgnoreCase("F"))
					{
						trovato = true;
						break;
					}
			   }   
/*20141024*/	
			}
  	   
/*121009* 
 *v14* 		if ((trovato == false)
 *v14* 		 && qModelDaVerificare.getNumElem().intValue() == 20)
 *v14* 		{
 *v14* 			trovato = true;
 *v14* 			qModelDaVerificare.setNumSele(wsNonRisposto);
 *v14* 		}
 *v15* 		if ((trovato == false)
 *v15* 		 && qModelDaVerificare.getNumElem().intValue() == 21)
 *v15* 		{
 *v15* 			trovato = true;
 *v15* 			qModelDaVerificare.setNumSele(wsNonRisposto);
 *v15* 		}
 *121009*/

/*121009*/	String wsFacoltativaS = null;			
/*121009*/	if (trovato == false)
/*121009*/	{				
/*121009*/		IntegerType wsNumDomaI = new IntegerType(qModelDaVerificare.getNumElem().intValue());
/*121009*/		wsChiave = wsRelease + AdeguatezzaUtility.padLeft(wsNumDomaI.getStringValue(),'0',2);
/*121009*/		wsFacoltativaS = "N";
/*121009*/		TW00TBTZModel wsAppoggi = null;
/*121009*/		for (int jj = 0; jj < cacheManager.getElenco869().size(); jj++)
/*121009*/		{
/*121009*/			wsAppoggi = (TW00TBTZModel)cacheManager.getElenco869().get(jj); 
/*121009*/			if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
/*121009*/			{
/*121009*/				wsFacoltativaS = wsAppoggi.getDati().getStringValue();
/*121009*/				break;
/*121009*/			}
/*121009*/		}
/*121009*/		if (wsFacoltativaS.trim().equalsIgnoreCase("S"))
/*121009*/		{
/*121009*/			trovato = true;
/*121009*/			qModelDaVerificare.setNumSele(wsNonRisposto);
/*121009*/		}				
/*121009*/	}
			
			if (trovato == false)
			{
				esito.esito = "101";
				esito.numElem = qModelDaVerificare.getNumElem().intValue();
				esito.setDescr("Risposta alla domanda indicata (" + qModelDaVerificare.getNumElem().intValue() + ") non valida/trovata con la release corrente");
				return false;
			}
			
			//verifico che la risposta consenta scelte multiple
			trovato = false;
			for (int i=0;i<verificaElencoDomande.size();i++)
			{
				vModel = (RisposteVerificaModel)verificaElencoDomande.get(i);
				if (vModel.getNumDoma().intValue() == qModelDaVerificare.getNumElem().intValue())
				{
					
//questionario importo
					if (vModel.getRisMult().equalsIgnoreCase("I"))
	 				{
	 					vModel.setRisposto(new StringType("S"));
	 					trovato = true;
	 					break;
	 				}
/*v13*/				if (vModel.getRisMult().equalsIgnoreCase("F"))
/*v13*/				{
/*v13*/	 				vModel.setRisposto(new StringType("S"));
/*v13*/					trovato = true;
/*v13*/ 				break;
/*v13*/				}
					if (vModel.getRisposto().equalsIgnoreCase("S")
						&& vModel.getRisMult().equalsIgnoreCase("N"))
					{
						esito.esito = "104";
						esito.numElem = qModelDaVerificare.getNumElem().intValue();
						esito.setDescr("La domanda indicata (" + qModelDaVerificare.getNumElem().intValue() + ") non consente risposte multiple");
						return false;
					}
					else
					{
						vModel.setRisposto(new StringType("S"));
					}
					trovato = true;
					break;			
				}
			}
			
			if (trovato == false)
			{
				esito.esito = "101";
				esito.numElem = qModelDaVerificare.getNumElem().intValue();
				esito.setDescr("Risposta alla domanda indicata (" + qModelDaVerificare.getNumElem().intValue() + ") non valida/trovata con la release corrente");
				return false;
			}
		}
		
		for (int i=0;i<verificaElencoDomande.size();i++)
		{
			vModel = (RisposteVerificaModel)verificaElencoDomande.get(i);
/*121009*			
 *v14*		if (vModel.getRisposto().equalsIgnoreCase("N")
 *v14* 	 	&& vModel.getNumDoma().intValue() == 20)
 *v14* 		{
 *v14* 			vModel.setRisposto(new StringType("S"));
 *v14* 		}
 *v15* 		if (vModel.getRisposto().equalsIgnoreCase("N")
 *v15* 	 	&& vModel.getNumDoma().intValue() == 21)
 *v15* 		{
 *v15* 			vModel.setRisposto(new StringType("S"));
 *v15* 		}
 *121009*/
  
/*121009*/	IntegerType wsNumDomaI = new IntegerType(vModel.getNumDoma().intValue());
/*121009*/	wsChiave = wsRelease + AdeguatezzaUtility.padLeft(wsNumDomaI.getStringValue(),'0',2);
/*121009*/	String wsFacoltativaS = null;
/*121009*/	wsFacoltativaS = "N";
/*121009*/	TW00TBTZModel wsAppoggi = null;
/*121009*/	for (int jj = 0; jj < cacheManager.getElenco869().size(); jj++)
/*121009*/	{
/*121009*/		wsAppoggi = (TW00TBTZModel)cacheManager.getElenco869().get(jj); 
/*121009*/		if (wsAppoggi.getChiave().equalsIgnoreCase(wsChiave))
/*121009*/		{
/*121009*/			wsFacoltativaS = wsAppoggi.getDati().getStringValue();
/*121009*/			break;
/*121009*/		}
/*121009*/	}
/*121009*/	if (wsFacoltativaS.trim().equalsIgnoreCase("S"))
/*121009*/	{
/*121009*/		vModel.setRisposto(new StringType("S"));
/*121009*/	}

			if (vModel.getRisposto().equalsIgnoreCase("N"))
			{
				esito.esito = "105";
				esito.numElem = vModel.getNumDoma().intValue();
				esito.setDescr("Risposta alla domanda indicata (" + vModel.getNumDoma().intValue() + ") non fornita.");
				return false;
			}
		}
		return true;
	}
	
	public static boolean ReleaseCorrente(CacheManager cacheManager, DAOObject dao, 
		IntegerType release, StatoAdeguatezza esito, StringType pg) throws DAOException
	{
/*20111025:PG 
		ListType releaseCorrente = cacheManager.getReleaseCorrente();
//20111025:PG */
		ListType releaseCorrente = new ListType();
		if (pg.equals("PG"))
		{
			releaseCorrente = cacheManager.getReleaseCorrentePG();
		}
		else
		{
			releaseCorrente = cacheManager.getReleaseCorrente();
		}
		
		if (releaseCorrente.size() == 0)
		{
			esito.esito = "003";
			esito.setDescr("Tabella domande non valorizzata");
			return false;
		}
		release.setBigValue(((IntegerModel)releaseCorrente.get(0)).getValore().bigValue());
		return true;
	}

	public static boolean ComponiRisposte(CacheManager cacheManager, DAOObject dao, 
		ListType risposte, StringType selRisp,StringType[] selRispMult, Integer[] selRispImpo, 
		StatoAdeguatezza esito, StringType pg) throws DAOException
	{
/*20111025:PG		
		ListType domandeMultiple = cacheManager.getDomandeMultiple();
		ListType domandeImporto = cacheManager.getDomandeImporto();
//20111025:PG */
		
		ListType domandeMultiple = new ListType();
		ListType domandeImporto = new ListType();
		
		if (pg.equals("PG"))
		{
			domandeMultiple = cacheManager.getDomandeMultiplePG();
			domandeImporto = cacheManager.getDomandeImportoPG();
		}
		else
		{
			domandeMultiple = cacheManager.getDomandeMultiple();
			domandeImporto = cacheManager.getDomandeImporto();
		}
	
		int ctChar = 0;
		ElementoQuestionarioRisposteModel qModelDaComporre = null;
		ElementoQuestionarioRisposteModel qModel = null;
		boolean trovato = false;
		
		char[] selRispChar = new char[255];
		char[] selRispMultChar = new char[255];

//questionario importo
//		String[] selRispImpoImpo = new String[255];
//		int importo = 0;
		
		for (int i=0;i<risposte.size();i++)
		{
			qModelDaComporre = (ElementoQuestionarioRisposteModel)risposte.get(i);
			ctChar = qModelDaComporre.getNumElem().intValue();
			trovato = false;
			for (int j=0;j<domandeMultiple.size();j++)
			{
				qModel = (ElementoQuestionarioRisposteModel) domandeMultiple.get(j);
				if (qModelDaComporre.getNumElem().intValue() == qModel.getNumElem().intValue())
				{
					selRispChar[ctChar] = 'M';
					if (selRispMult[ctChar].getStringValue().trim().equalsIgnoreCase(""))
						for(int k=0;k<=qModel.getNumSele().intValue();k++)
							if (k==0)
								selRispMult[ctChar].setStringValue(selRispMult[ctChar].getStringValue() + " ");
							else
								selRispMult[ctChar].setStringValue(selRispMult[ctChar].getStringValue() + "N");
					selRispMultChar = selRispMult[ctChar].getStringValue().toCharArray();
					selRispMultChar[qModelDaComporre.getNumSele().intValue()] = 'S';
					selRispMult[ctChar].setStringValue(String.valueOf(selRispMultChar));
					trovato = true;
				}
			}
//questionario importo
			if (trovato == false)
			{
				for (int j=0;j<domandeImporto.size();j++)
				{
					qModel = (ElementoQuestionarioRisposteModel) domandeImporto.get(j);
					if (qModelDaComporre.getNumElem().intValue() == qModel.getNumElem().intValue())
					{
						selRispChar[ctChar] = 'I';
//						selRispImpoImpo[qModel.getNumElem().intValue()] = new Integer(qModel.numSele.intValue());
//						String impoString = null;
//						importo = qModel.getNumSele().intValue();
						selRispImpo[ctChar] = new Integer (qModelDaComporre.getNumSele().intValue());
						trovato = true;
					}
				}
			}
			if (trovato == false)
			{
				/* Conversione risposte >= 10 nei corrispsondenti caratteri alfabetici */
				if (qModelDaComporre.getNumSele().intValue() >= 10)
				{
					switch (qModelDaComporre.getNumSele().intValue()) {
					case 10:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "a".charAt(0);	
						break;
					case 11:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "b".charAt(0);
						break;
					case 12:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "c".charAt(0);
						break;
					case 13:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "d".charAt(0);
						break;
					case 14:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "e".charAt(0);
						break;
					case 15:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "f".charAt(0);
						break;
					case 16:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "g".charAt(0);
						break;
					case 17:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "h".charAt(0);
						break;
					case 18:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "i".charAt(0);
						break;
					case 19:
						selRispChar[qModelDaComporre.getNumElem().intValue()] = "l".charAt(0);
						break;
					}//Fine blocco switch			
				}
				else
					selRispChar[qModelDaComporre.getNumElem().intValue()] = (String.valueOf(qModelDaComporre.getNumSele().intValue())).charAt(0);				
			}
		}
 		selRisp.setStringValue(String.valueOf(selRispChar).trim());
		return true;
	}
	
	public static boolean PreparaInserimento(TW00TBQKModel qk,TW00TBQMModel qm,TW00TBSKModel sk,
			StringType ndgDoss,StringType ndgTemp,StringType canVend,IntegerType codIsti,
			StringType codAppl,IntegerType datFinv,TimestampType datComp,StringType seValid,
			IntegerType filOperDef,IntegerType release,StringType selRisp,
			StringType schedal,StringType profilo,StringType risClie,StringType espFina,
			StringType obbInve,StringType obbTemp, StringType sitFina, StringType flSkCli,
			StringType numSche, StringType cluster, StringType esigLiq, StringType forzObt,
			StringType origObt, StringType origClu)
	{
		qk.setCodisti(codIsti);
		if (ndgDoss == null)
			qk.setNdgdoss(new StringType(ndgDossNull));
		else
			qk.setNdgdoss(ndgDoss);
		if (ndgTemp == null)
			qk.setNdgtemp(new StringType(ndgTempNull));
		else
		{
			if (ndgTemp.equalsIgnoreCase(""))
				qk.setNdgtemp(new StringType(ndgTempNull));
			else
				qk.setNdgtemp(ndgTemp);
		}
				
/*121121*/ // MODIFICA PER GESTIRE LA NON RISPOSTA ALLA DOMANDA 20 ESSENDO FACOLTATIVA
		if (selRisp.getStringValue().length() == 21)
		{
			String selrispold = new String();
			String selrisppre = new String();
			String selrispost = new String();
			String selrispnew = new String();
			String selrispmid = new String();
			int numdomains = 19;
			
			selrispold = selRisp.stringValue();
			selrisppre = selrispold.substring(0,numdomains);
			selrispmid = selrispold.substring(numdomains,numdomains + 1);
			selrispost = selrispold.substring(numdomains + 1,selrispold.length());
			
			if (!(selrispmid.equalsIgnoreCase("0") || selrispmid.equalsIgnoreCase("1") || selrispmid.equalsIgnoreCase("2") || selrispmid.equalsIgnoreCase("3") || selrispmid.equalsIgnoreCase("4"))) 
			{
				selrispmid = "0";
			}

			selrispnew = selrisppre + selrispmid + selrispost;
			selRisp.setStringValue(selrispnew);
		}		
		
/*121121*/ // MODIFICA PER GESTIRE LA NON RISPOSTA ALLA DOMANDA 20 ESSENDO FACOLTATIVA
		qk.setCodappl(codAppl);
		qk.setDfinval(datFinv);
		qk.setDatsche(datComp);
		qk.setFiloper(filOperDef);
		qk.setRelease(release);
		qk.setSelrisp(selRisp);
		qk.setCanvend(canVend);
		
		qm.setCodisti(codIsti);
		if (ndgDoss == null)
			qm.setNdgdoss(new StringType(ndgDossNull));
		else
			qm.setNdgdoss(ndgDoss);
		if (ndgTemp == null)
			qm.setNdgtemp(new StringType(ndgTempNull));
		else
		{
			if (ndgTemp.equalsIgnoreCase(""))
				qm.setNdgtemp(new StringType(ndgTempNull));
			else
				qm.setNdgtemp(ndgTemp);
		}
		
		qm.setCodappl(codAppl);
		qm.setDfinval(datFinv);
		qm.setDatsche(datComp);
		qm.setNumdoma(new IntegerType());
		qm.setSelmult(new StringType());
		
		sk.setCodisti(codIsti);
		sk.setCodappl(codAppl);
		
		if (ndgDoss == null)
			sk.setNdgdoss(new StringType(ndgDossNull));
		else
			sk.setNdgdoss(ndgDoss);
		if (ndgTemp == null)
			sk.setNdgtemp(new StringType(ndgTempNull));
		else
		{
			if (ndgTemp.equalsIgnoreCase(""))
				sk.setNdgtemp(new StringType(ndgTempNull));
			else
				sk.setNdgtemp(ndgTemp);
		}
		
		sk.setDfinval(datFinv);
		sk.setGiosche(new IntegerType(datComp.getAA()+datComp.getMM()+datComp.getGG()));
		sk.setDatsche(datComp);
		sk.setSchedal(schedal);
		sk.setProfilo(profilo);
		sk.setRisclie(risClie);
		sk.setEspfina(espFina);
		sk.setObbinve(obbInve);
		sk.setObbtemp(obbTemp);
		sk.setSitfina(sitFina);			
		sk.setFlskcli(flSkCli);
		sk.setFiloper(filOperDef);
		sk.setCanvend(canVend);
		sk.setSevalid(seValid);
		sk.setRelease(release);
		sk.setNumsche(numSche);
		sk.setCluster(cluster);
/*v12*/ sk.setEsigliq(esigLiq);
/*v12*/ sk.setForzobt(forzObt);
/*v12*/ sk.setOrigobt(origObt);
/*v12*/ sk.setOrigclu(origClu);
		return true;
	}

	public static boolean PreparaCancellazione(TW00TBQKModel qk,TW00TBQMModel qm,TW00TBSKModel sk,
			StringType ndgDoss,StringType ndgTemp,StringType canVend,IntegerType codIsti,
			StringType codAppl,IntegerType datBozza)
	{
		qk.setCodisti(codIsti);
		qk.setNdgdoss(ndgDoss);
		qk.setNdgtemp(ndgTemp);
		qk.setCodappl(codAppl);
		qk.setDfinval(datBozza);
		
		qm.setCodisti(codIsti);
		qm.setNdgdoss(ndgDoss);
		qm.setNdgtemp(ndgTemp);
		qm.setCodappl(codAppl);
		qm.setDfinval(datBozza);
		
		sk.setCodisti(codIsti);
		sk.setNdgdoss(ndgDoss);
		sk.setNdgtemp(ndgTemp);
		sk.setCodappl(codAppl);
		sk.setDfinval(datBozza);
		return true;
	}
}