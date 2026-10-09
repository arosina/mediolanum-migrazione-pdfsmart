package prgm.ita.anagraficaclienti.facade;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class ControlloPartitaIva implements Controlli{

    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eseguiControllo(ClientSessionContext csc, ClienteModel model){
		
		if(model.getPartitaIva().isNull() || model.getPartitaIva().isSkippable())
			return;
	    
		try{
			String filledPIva = Tools.fillSx(model.getPartitaIva().toString(),'0',11);
			if(Integer.parseInt(filledPIva) == 0){
				model.getPartitaIva().addTypeError("err.partitaIvaErrata");
				return;
			}
		}catch(Exception e){}
		
		if(model.getComuneNascita().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA))
			controllaPartitaIva(csc,model);
		else
			controllaPartitaIvaEstero(csc,model);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaPartitaIvaEstero(ClientSessionContext csc, ClienteModel model){
		
		try{
			
			if(model.getPartitaIva().isNull())
				return;
	
			if(model.getPartitaIva().toString().length() != Costanti.LUNGHEZZA_PARTITA_IVA_ESTERA){
				model.getPartitaIva().addTypeError("err.lunghezzaPIva");
				return;
			}
			return;
			
		}catch(Exception e){
			LOG.error(e);
			return;
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void controllaPartitaIva(ClientSessionContext csc, ClienteModel model){
		
		try{
			
			if(model.getPartitaIva().isNull())
				return;
	
			if(model.getPartitaIva().toString().length() != Costanti.LUNGHEZZA_PARTITA_IVA_ITALIANA){
				model.getPartitaIva().addTypeError("err.lunghezzaPIva");
				return;
			}
	
			String pi = model.getPartitaIva().toString();
		    int i, c, s;
		    for(i=0;i<11;i++ ){
		        if( pi.charAt(i) < '0' || pi.charAt(i) > '9' ){
					model.getPartitaIva().addTypeError("err.partitaIvaErrata");
					return;
		        }
		    }
		    s = 0;
		    for(i=0;i<=9;i+=2)
		        s += pi.charAt(i) - '0';
		    for(i=1;i<=9;i+=2){
		        c = 2*(pi.charAt(i) - '0');
		        if( c > 9 )  
		        	c = c - 9;
		        s += c;
		    }
	
		    if((10 - s%10)%10 != pi.charAt(10) - '0'){
				model.getPartitaIva().addTypeWarning("war.pIvaNonConforme");
				return;
		    }
			return;
			
		}catch(Exception e){
			LOG.error(e);
			return;
		}
	}
	
}
