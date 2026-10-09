package prgm.ita.anagraficaclienti.popup.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClientiModel extends CommandDataModel implements GridDecorator{

	public static final String RICERCA_CLIENTI_PRIMARI = "primari";
	public static final String TD_CENTER = "center";

	private StringType  runtimeModality = new StringType(Costanti.PARAM_RUNTIME_MODALITY_FULL);

	private BooleanType isPrimaVolta = new BooleanType(true);	
	
	private PopupClientiParamsModel params = new PopupClientiParamsModel();
	private ListType elenco = new ListType(ClienteModel.class);
	private ClienteModel clienteSelezionato = new ClienteModel();
	private BooleanType isCampiObbligatori = new BooleanType(false);
	private StringType urlNuovoPcp = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel(){
		setUploadMaxSize(2*1024*1024);//2 MB
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName, CommandDataModel row, 
						  AbstractType cell, int rowIndex, int cellIndex) {
		
		ClienteModel cliente = (ClienteModel)row;
		FieldStyle fs = new FieldStyle();
		String color = "crimson";
		if(cliente.getIsProspect().booleanValue())
			color = "coral";
		else if(cliente.getIsBozza().booleanValue())
		  	color = "antiquewhite";
		else if(cliente.getStatoConfermato().equals(Costanti.VALORE_FLAG_STATO_CONFERMATO))
		  	color = "lavender";
		else if(cliente.getIsPotenziale().booleanValue())
		  	color = "white";
		else if(cliente.getIsAcquisito().booleanValue())
		  	color = "lightgreen";
		else if(cliente.getIsEffettivoPersonale().booleanValue())
		 	color = "lemonchiffon";
		else if(cliente.getIsEffettivoRiassegnato().booleanValue())
		 	color = "khaki";
		else if(cliente.getIsCointestatarioNonAssegnato().booleanValue())
		 	color = "palegreen";
		else if(cliente.getIsAssegnatoAdAltroAgente().booleanValue())
		  	color = "paleturquoise";
		fs.backgroundColor = color;
		
		if(cellPropertyName.equals("isClienteInCogestione") ||
		   cellPropertyName.equals("codPotenziale") ||
		   cellPropertyName.equals("codMediolanum") ||
		   cellPropertyName.equals("codInforete") ||
		   cellPropertyName.equals("codAgente") ||
		   cellPropertyName.equals("partitaIva") ||
		   cellPropertyName.equals("codFiscale") ||
		   cellPropertyName.equals("dataNascita") ||
		   cellPropertyName.equals("numVariazioni"))
		   fs.textAlign = TD_CENTER;
		   
		if(cellPropertyName.equals("numVariazioni")){
			if(cliente.getNumVariazioni().intValue() == 0)
				fs.innerHTML = "&nbsp;";
		}
		
		if(cellPropertyName.equals("naturaGiuridica")){
		    fs.textAlign = TD_CENTER;
			if(cliente.getIsDitta().booleanValue())
				fs.innerHTML = "Ditta/Lib.prof.";
			else if(cliente.getIsPersonaGiuridica().booleanValue())
				fs.innerHTML = "Soc.";
			else
				fs.innerHTML = "&nbsp;";
		}
		
		if(cellPropertyName.equals("indicativoContoDepositoAttivo") && cliente.getTitolareContoDepositoAttivo().booleanValue()){
			fs.innerHTML="<img src='"+row.getTemplate().getWebApp()+"/images/verde.png' title='Intestatario/Cointestatario conto deposito attivo'>";
			fs.textAlign = TD_CENTER;
		}
		
		if(cliente.getIsDitta().booleanValue()){
			if(!cliente.getSecondaIntestazione().isNull()){
				if(cellPropertyName.equals("cognome"))
					fs.innerHTML = cliente.getSecondaIntestazione().toString();
				else if(cellPropertyName.equals("nome"))
					fs.innerHTML = "di "+cliente.getCognome()+" "+cliente.getNome();
			}
		}
		
		if( cellPropertyName.equals("agenteTitolare") &&
		   !cliente.getCogestioneData().getCodAgenteCogestore().isNull()){
			fs.innerHTML = cliente.getCogestioneData().getNominativoAgenteTitolare().toString();
		}
		
		if( cellPropertyName.equals("agenteCogestore") &&
		   !cliente.getCogestioneData().getCodAgenteCogestore().isNull()){			
			fs.innerHTML = cliente.getCogestioneData().getNominativoAgenteCogestore().toString();
		}

		if( cellPropertyName.equals("clienteCogestito")){	
			fs.textAlign = TD_CENTER;
			fs.innerHTML = cliente.getCogestioneData().getCodAgenteCogestore().isNull()?"No":"Si";
		}

		cell.setStyle(fs);
	}

	public ListType getElenco() {
		return elenco;
	}

	public void setElenco(ListType elenco) {
		this.elenco = elenco;
	}

	public PopupClientiParamsModel getParams() {
		return params;
	}

	public void setParams(PopupClientiParamsModel params) {
		this.params = params;
	}

	public StringType getRuntimeModality() {
		return runtimeModality;
	}

	public void setRuntimeModality(StringType runtimeModality) {
		this.runtimeModality = runtimeModality;
	}

	public BooleanType getIsPrimaVolta() {
		return isPrimaVolta;
	}

	public void setIsPrimaVolta(BooleanType isPrimaVolta) {
		this.isPrimaVolta = isPrimaVolta;
	}

	public ClienteModel getClienteSelezionato() {
		return clienteSelezionato;
	}

	public void setClienteSelezionato(ClienteModel clienteSelezionato) {
		this.clienteSelezionato = clienteSelezionato;
	}

	public BooleanType getIsCampiObbligatori() {
		return isCampiObbligatori;
	}

	public void setIsCampiObbligatori(BooleanType isCampiObbligatori) {
		this.isCampiObbligatori = isCampiObbligatori;
	}

	public StringType getUrlNuovoPcp() {
		return urlNuovoPcp;
	}

	public void setUrlNuovoPcp(StringType urlNuovoPcp) {
		this.urlNuovoPcp = urlNuovoPcp;
	}

}
