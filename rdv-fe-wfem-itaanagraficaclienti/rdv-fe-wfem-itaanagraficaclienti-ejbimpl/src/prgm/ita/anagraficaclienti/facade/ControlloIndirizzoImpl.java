package prgm.ita.anagraficaclienti.facade;

import prgm.ita.anagraficaclienti.model.IndirizzoModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlloIndirizzoImpl implements ControlloIndirizzoIntf{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, IndirizzoModel indirizzo,	boolean indirizzoObbligatorio){
		
		boolean indirizzoCompilato = true;
		StringType tipoIndirizzo = indirizzo.getTipoIndirizzo();
		if(IndirizzoModel.isIndirizzoSpecifico(tipoIndirizzo))
			tipoIndirizzo = new StringType();
		 
		if(tipoIndirizzo.isNull() &&
		   indirizzo.getToponimoIndirizzo().isNull() &&
		   indirizzo.getDescrizioneIndirizzo().isNull() &&
		   indirizzo.getNumeroCivico().isNull() &&
		   indirizzo.getCap().isNull() &&
		   indirizzo.getComune().isNull())
			indirizzoCompilato = false;
		
		if(indirizzoObbligatorio)
			indirizzoCompilato = true;
		
		if(indirizzo.getCodNazione().equals(Costanti.COD_NAZIONE_ITALIA)){
			// Toponimo
			if(indirizzoCompilato &&
			   indirizzo.getToponimoIndirizzo().isNull())
			   indirizzo.getToponimoIndirizzo().addTypeError("err.toponimoIndirizzoObbl");
			// Descrizione indirizzo
			if(indirizzoCompilato &&
			   indirizzo.getDescrizioneIndirizzo().isNull())
			   indirizzo.getDescrizioneIndirizzo().addTypeError("err.indirizzoObbl");
			// Numero civico
			if(indirizzoCompilato &&
			   indirizzo.getNumeroCivico().isNull())
			   indirizzo.getNumeroCivico().addTypeError("err.numeroCivicoIndirizzoObbl");
			// CAP
			if(indirizzoCompilato &&
			   indirizzo.getCap().isNull())
			   indirizzo.getCap().addTypeError("err.capIndirizzoObbl");
			// Comune
			if(indirizzoCompilato &&
			   indirizzo.getComune().isNull())
			   indirizzo.getComune().addTypeError("err.comuneIndirizzoObbl");
		}else{
			// Descrizione indirizzo
			if(indirizzoCompilato &&
			   indirizzo.getDescrizioneIndirizzo().isNull())
				indirizzo.getDescrizioneIndirizzo().addTypeError("err.indirizzoObbl");			
			if(indirizzoCompilato &&
			   indirizzo.getComuneEstero().isNull())
				indirizzo.getComuneEstero().addTypeError("err.comuneEsteroIndirizzoObbl");
				
		}
		return;
		
	}
	
}
