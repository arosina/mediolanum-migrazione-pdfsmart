package prgm.ita.anagraficaclienti.flussofatca;

import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiFatcaModel;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class NavigatoreValutazioneInCorso extends AbstractNavigatore {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String init(ClienteModel model){
		DatiFatcaModel datiFatca = model.getDatiFatca();
		int pesoIndizi = datiFatca.pesoIndizio();
		switch(pesoIndizi){
		
			case INDIZI_FORTI_NON_SUPERABILI:
				datiFatca.setFatcaPopupName(POPUP_RACCOLTA_TIN);
				break;
				
			case INDIZI_FORTI_SUPERABILI:
				datiFatca.setFatcaPopupName(POPUP_DICHIARAZIONE_US_PERSON);
				break;
				
			case INDIZI_DEBOLI:
				datiFatca.setFatcaPopupName(POPUP_DICHIARAZIONE_US_PERSON);
				break;
				
			case NESSUN_INDIZIO:
				datiFatca.setFatcaPopupName(NO_POPUP);
				break;
		}
		return datiFatca.getFatcaPopupName();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void doStep(ClienteModel model){
		DatiFatcaModel datiFatca = model.getDatiFatca();
		
		if(datiFatca.getFatcaPopupName().equals(POPUP_CONFERMA_VERFICA)){
			datiFatca.setFatcaPopupName(FINE);
			return;
		}		
		
		int pesoIndizi = datiFatca.pesoIndizio();
		switch(pesoIndizi){
		
			case INDIZI_FORTI_NON_SUPERABILI:
				datiFatca.setAlertMessage(FATCA_ALERT_W9);
				datiFatca.setModuloFatca(new StringType(Costanti.MODULO_W9_FATCA));
				datiFatca.setFatcaPopupName(POPUP_CONFERMA_VERFICA);
				break;
				
			case INDIZI_FORTI_SUPERABILI:
				if(datiFatca.getFatcaPopupName().equals(POPUP_DICHIARAZIONE_US_PERSON)){
					
					if(datiFatca.getIsUsPerson().booleanValue()){
						datiFatca.setFatcaPopupName(POPUP_RACCOLTA_TIN);
					}else{
						datiFatca.setAlertMessage(FATCA_ALERT_ALLEGATI);
						datiFatca.setFatcaPopupName(POPUP_CONFERMA_VERFICA);
					}
					
				}else if(datiFatca.getFatcaPopupName().equals(POPUP_RACCOLTA_TIN)){
					
					datiFatca.setAlertMessage(FATCA_ALERT_SI_DICHIARA_US);
					datiFatca.setModuloFatca(new StringType(Costanti.MODULO_W9_FATCA));
					datiFatca.setFatcaPopupName(POPUP_CONFERMA_VERFICA);
					
				}
				break;
				
			case INDIZI_DEBOLI:
				if(datiFatca.getFatcaPopupName().equals(POPUP_DICHIARAZIONE_US_PERSON)){

					if(datiFatca.getIsUsPerson().booleanValue()){
						datiFatca.setFatcaPopupName(POPUP_RACCOLTA_TIN);
					}else{
						datiFatca.setFatcaPopupName(FINE);
					}
					
				}else if(datiFatca.getFatcaPopupName().equals(POPUP_RACCOLTA_TIN)){
					
					datiFatca.setAlertMessage(FATCA_ALERT_SI_DICHIARA_US);
					datiFatca.setModuloFatca(new StringType(Costanti.MODULO_W9_FATCA));
					datiFatca.setFatcaPopupName(POPUP_CONFERMA_VERFICA);
					
				}
				break;
				
		}
	}

}
