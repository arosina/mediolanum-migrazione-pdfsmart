package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.utils;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.model.InviaMailInputModel;

public class InviaMailUtils {
	public static void buildTestoEOggettoEmailFondiItaliaEIrlanda(InviaMailInputModel input)  {
		
		String testo  = "";	
		String oggetto  = "";
		StringType codiceProdotto = input.getCodiceProdotto();
		StringType nomeCliente = input.getNomeCliente();
		StringType cognomeCliente = input.getCognomeCliente();
		DateType dataSottoscrizione = input.getDataSottoscrizione();
		
		if (input.getIsSwitch().equals("S")) {
			oggetto ="Copia contratto Switch Fondi";
			testo = "Si invia in allegato il contratto di Switch da Fondi " + input.getSwitchFrom() + " a Fondi " + input.getSwitchTo() + ", sottoscritto tramite il servizio di firma digitale " +
			"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
			
		} else {
		
			if(codiceProdotto.equals("TM") || codiceProdotto.equals("ATM") ) {
				testo  = "Si invia in allegato il contratto Mediolanum Best Brand, sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Mediolanum Best Brands";
			}else if (codiceProdotto.equals("PF") || codiceProdotto.equals("APF")) {
				testo  = "Si invia in allegato il contratto Mediolanum Portfolio Fund, sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Mediolanum Portfolio Fund";
			}else if (codiceProdotto.equals("LT") || codiceProdotto.equals("ALT")) {
				testo  = "Si invia in allegato il contratto Challenge Funds, sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Challenge Funds";
			}else if (codiceProdotto.equals("FI") || codiceProdotto.equals("AFI")) {
				testo  = "Si invia in allegato il contratto Fondi Italia, sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Fondi Italia";
			}else if (codiceProdotto.equals("TMDC") ) {
				testo  = "Si invia in allegato il contratto Mediolanum Best Brands con contestuale attivazione del servizio Double Chance," +
						" sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Mediolanum Best Brands con attivazione Double Chance";
			}else if (codiceProdotto.equals("DCTM") ) {
				testo  = "Si invia in allegato il contratto del servizio Double Chance, sottoscritto tramite il servizio di firma digitale " +
				"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Double Chance per Mediolanum Best Brands";
			}else if (codiceProdotto.equals("LTDC")) {
				testo  = "Si invia in allegato il contratto Challenge Funds con contestuale attivazione del servizio Double Chance," +
						" sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Challenge Funds con attivazione Double Chance";
			}else if (codiceProdotto.equals("DCLT") ) {
				testo  = "Si invia in allegato il contratto del servizio Double Chance, sottoscritto tramite il servizio di firma digitale " +
				"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Double Chance per Challenge Funds ";					
			}else if (codiceProdotto.equals("TMDCBE") ) {
				testo  = "Si invia in allegato il contratto Mediolanum Best Brands con contestuale attivazione del servizio Double Chance," +
						" sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Mediolanum Best Brands con attivazione Double Chance";
			}else if (codiceProdotto.equals("DCBETM") ) {
				testo  = "Si invia in allegato il contratto del servizio Double Chance, sottoscritto tramite il servizio di firma digitale " +
				"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Double Chance per Mediolanum Best Brands ";						
			}else if (codiceProdotto.equals("FIDC")  ) {
				testo  = "Si invia in allegato il contratto Fondi Italia con contestuale attivazione del servizio Double Chance," +
						" sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Fondi Italia con attivazione Double Chance";
			}else if (codiceProdotto.equals("DCFI") ) {
				testo  = "Si invia in allegato il contratto del servizio Double Chance, sottoscritto tramite il servizio di firma digitale " +
				"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Double Chance per Fondi Italia ";							
			}else if (codiceProdotto.equals("FIDCBE") ) {
				testo  = "Si invia in allegato il contratto Fondi Italia con contestuale attivazione del servizio Double Chance," +
						" sottoscritto tramite il servizio di firma digitale " +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Fondi Italia con attivazione Double Chance";
			}else if (codiceProdotto.equals("DCBEFI") ) {
				testo  = "Si invia in allegato il contratto del servizio Double Chance, sottoscritto tramite il servizio di firma digitale " +
				"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia contratto Double Chance per Fondi Italia ";						
			}else if (codiceProdotto.equals("RTM")) {
				testo = "Si invia in allegato il modulo di rimborso del Fondo Mediolanum Best Brand, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo rimborso Mediolanum Best Brands";
			}else if (codiceProdotto.equals("RLT")) {
				testo = "Si invia in allegato il modulo di rimborso del Fondo Challenge Funds, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo rimborso Challenge Funds";
			}else if (codiceProdotto.equals("RPF")) {
				testo = "Si invia in allegato il modulo di rimborso del Fondo Mediolanum Portfolio Fund, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo rimborso Mediolanum Portfolio Fund";
			}else if (codiceProdotto.equals("RFI")) {
				testo = "Si invia in allegato il modulo di rimborso del Fondo Italiano, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo rimborso Fondo Italiano";
			}else if (codiceProdotto.equals("CTM")) {
				testo = "Si invia in allegato il modulo di conversione del Fondo Mediolanum Best Brand, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo conversione Mediolanum Best Brands";
			}else if (codiceProdotto.equals("CLT")) {
				testo = "Si invia in allegato il modulo di conversione del Fondo Challenge Funds, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo conversione Challenge Funds";
			}else if (codiceProdotto.equals("CPF")) {
				testo = "Si invia in allegato il modulo di conversione del Fondo Mediolanum Portfolio Fund, sottoscritto tramite il servizio di firma digitale" +
						"dal Cliente " + nomeCliente + " " + cognomeCliente + " in data " + dataSottoscrizione + ".";
				oggetto = "Copia modulo conversione Mediolanum Portfolio Fund";
			}
		}
		
		input.setTestoEmail(new StringType(testo));
		input.setOggettoEmail(new StringType(oggetto));
	}
}
