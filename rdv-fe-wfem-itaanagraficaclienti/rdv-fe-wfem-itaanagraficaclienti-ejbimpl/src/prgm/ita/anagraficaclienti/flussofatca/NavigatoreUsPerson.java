package prgm.ita.anagraficaclienti.flussofatca;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiFatcaModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class NavigatoreUsPerson extends AbstractNavigatore {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String init(ClienteModel model){
		DatiFatcaModel datiFatca = model.getDatiFatca();
		int pesoIndizi = datiFatca.pesoIndizio();
		switch(pesoIndizi){
		
			case INDIZI_FORTI_NON_SUPERABILI:
				datiFatca.setFatcaPopupName(NO_POPUP);
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
		
			case INDIZI_FORTI_SUPERABILI:
				if(datiFatca.getIsUsPerson().booleanValue()){
					datiFatca.setFatcaPopupName(FINE);
				}else{
					datiFatca.setAlertMessage(FATCA_ALERT_ALLEGATI);
					datiFatca.setFatcaPopupName(POPUP_CONFERMA_VERFICA);
				}
				break;
				
			case INDIZI_DEBOLI:
				datiFatca.setFatcaPopupName(FINE);
				break;
		}
		
	}

}
