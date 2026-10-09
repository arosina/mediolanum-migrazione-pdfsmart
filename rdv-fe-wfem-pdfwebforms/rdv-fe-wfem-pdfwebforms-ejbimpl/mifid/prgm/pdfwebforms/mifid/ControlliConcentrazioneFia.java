package prgm.pdfwebforms.mifid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.drivers.io.mifid.ElementoControlliConcentrazioneFiaModel;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliConcentrazioneFia {
	
	public static String ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR = "#CONTROLLICONCENTRAZIONEFIA#";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private ControlliConcentrazioneFia() {
		throw new IllegalStateException("ControlliConcentrazioneFia class");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void addElementiDispo(ProvideMifidDataResponse mifidData, MifidCallModel callModel) {
		for(int i=0;i<mifidData.getElementiControlloConcentrazioneFia().size();i++) {
			ElementoControlliConcentrazioneFiaModel el = (ElementoControlliConcentrazioneFiaModel)mifidData.getElementiControlloConcentrazioneFia().get(i);
			callModel.getControlliConcentrazioneFiaData().getElementiControlloConcentrazioneFia().add(el);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String eseguiControlli(ClientSessionContext csc, MifidCallModel callModel) throws Exception{
		ControlliConcentrazioneFiaDataModel controlliConcentrazioneFiaData = callModel.getControlliConcentrazioneFiaData();
		ListType elementiControlloConcentrazioneFia = controlliConcentrazioneFiaData.getElementiControlloConcentrazioneFia();
		if(elementiControlloConcentrazioneFia.size() == 0)
			return null;

		List<ElementoControlliConcentrazioneFiaModel> distinctFamiglie = distinctElementiControlloConcentrazioneFia(elementiControlloConcentrazioneFia, false);
		impostaPercentualiControlloConcentrazioneFia(csc, distinctFamiglie, false);

		List<ElementoControlliConcentrazioneFiaModel> distinctIsin = distinctElementiControlloConcentrazioneFia(elementiControlloConcentrazioneFia, true);
		impostaPercentualiControlloConcentrazioneFia(csc, distinctIsin, true);
		
		// Per ogni cliente controllo le famiglie e gli isin
		StringBuilder err = new StringBuilder();
		for(int i=0;i<callModel.getEsitiBasket().size();i++) {
			EsitoBasketMifidModel cli = (EsitoBasketMifidModel)callModel.getEsitiBasket().get(i);
			String errFam = eseguiControlloConcentrazioneFia(cli, distinctFamiglie, false);
			String errIsin = eseguiControlloConcentrazioneFia(cli, distinctIsin, true);
			if(!errFam.isEmpty() || !errIsin.isEmpty())
				err.append("<br><b>"+MifidCaller.searchNominativoClienteMifid(cli, callModel)+"</b><br>"+errFam+errIsin);
		}
		return err.length() == 0 ? null : ERRORE_CONTROLLI_CONCENTRAZIONE_FIA_INDICATOR+err.toString();
	}

    /***********************************************************************************************/
    /***********************************************************************************************/
    private static List<ElementoControlliConcentrazioneFiaModel> distinctElementiControlloConcentrazioneFia(ListType elementiControlloConcentrazioneFia,
    																										boolean forIsin) throws Exception{  	
    	// Creo l'elenco delle differenti elementi per famiglia o isin
    	ArrayList<ElementoControlliConcentrazioneFiaModel> res = new ArrayList<>();
    	for(int i=0;i<elementiControlloConcentrazioneFia.size();i++) {
    		ElementoControlliConcentrazioneFiaModel el = (ElementoControlliConcentrazioneFiaModel)elementiControlloConcentrazioneFia.get(i);
    		boolean found = false;
    		for(ElementoControlliConcentrazioneFiaModel resel : res) {
    			if( ( forIsin && resel.getIsin().equals(el.getIsin())) ||
    				(!forIsin && resel.getFamiglia().equals(el.getFamiglia())) ) {
    				resel.setImporto(resel.getImporto().add(el.getImporto()));
    				found = true;
    				break;
    			}
    		}
    		if(!found)
    			res.add((ElementoControlliConcentrazioneFiaModel)Tools.cloneObject(el));
    	}
    	return res;
    }
	
	/***********************************************************************************************/
	private static final String PRECENTUALE_DEFAULT_FAMIGLIE 	= "20";
	private static final String PRECENTUALE_DEFAULT_ISIN 		= "10";
	/***********************************************************************************************/
	private static void impostaPercentualiControlloConcentrazioneFia(ClientSessionContext csc, 
																	 List<ElementoControlliConcentrazioneFiaModel> elencoDistinctElementi, 
																	 boolean forIsin) throws Exception{
	   	// Imposto le percentuali. Se configurata prendo quella altrimenti il default
    	List<String> percentualiConf = PdfConfig.getParamAsStringArray(csc, "MIFID", forIsin ? "CONTROLLO_CONCENTRAZIONE_FIA_PERCENTUALI_ISIN" : "CONTROLLO_CONCENTRAZIONE_FIA_PERCENTUALI_FAMIGLIE","");
		for(ElementoControlliConcentrazioneFiaModel el : elencoDistinctElementi) {
			boolean found = false;
			for(String propConf : percentualiConf) {
				String[] propPercSplit = propConf.split("\\=");
				String propNameConf = propPercSplit[0];
				String percConf = propPercSplit[1];
				if( ( forIsin && el.getIsin().toString().equals(propNameConf)) ||
					(!forIsin && el.getFamiglia().toString().equals(propNameConf)) ) {
					el.setPercentuale(new DoubleType(percConf));
					found = true;
					break;
				}
			}
			if(!found)
				el.setPercentuale(new DoubleType(forIsin ? PRECENTUALE_DEFAULT_ISIN : PRECENTUALE_DEFAULT_FAMIGLIE));
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String eseguiControlloConcentrazioneFia(EsitoBasketMifidModel cli, 
														   List<ElementoControlliConcentrazioneFiaModel> elencoDistinctElementi, 
														   boolean forIsin) {
		StringBuilder sb = new StringBuilder();
		for(ElementoControlliConcentrazioneFiaModel el : elencoDistinctElementi) {
			BigDecimal importoPercPatr = importoPercentualePatrimonio(cli.getCtrvPatrimonioTotale().bigValue(), el.getPercentuale().bigValue());
			if(el.getImporto().bigValue().compareTo(importoPercPatr) > 0) {
				if(forIsin)
					sb.append("L'importo dell'investimento per l'isin "+el.getIsin()+" supera il "+el.getPercentuale()+"% del totale del patrimonio MiFid<br>");
				else
					sb.append("L'importo dell'investimento per la famiglia "+el.getFamiglia()+" supera il "+el.getPercentuale()+"% del totale del patrimonio MiFid<br>");
			}
		}
		return sb.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static BigDecimal importoPercentualePatrimonio(BigDecimal patrimonio, BigDecimal percentuale) {
		return patrimonio.multiply(percentuale).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
	}
	
}
