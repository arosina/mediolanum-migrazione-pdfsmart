package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideAgevolazioneDipendentiDataResponse{
	
	public static String DEFAUT_DESCRIZIONE_AGEVOLAZIONE_DIPENDENTI = "Agevolazioni dipendenti, promotori e Societa' del Gruppo";
	
    private String codiceAgevolazioneDipendenti = "";
    private String descrizioneAgevolazioneDipendenti = "";
    
    /**************************************************************************************************
    **************************************************************************************************/
	public String getCodDescrAgevolazioneDipendenti() {
		String res = getCodiceAgevolazioneDipendenti();
		if(res.length() > 0){
			if(getDescrizioneAgevolazioneDipendenti().length() > 0)
				res += "|"+getDescrizioneAgevolazioneDipendenti();
		}
		return res;
	}

	public String getCodiceAgevolazioneDipendenti() {
		return codiceAgevolazioneDipendenti;
	}
	public void setCodiceAgevolazioneDipendenti(String codiceAgevolazioneDipendenti) {
		this.codiceAgevolazioneDipendenti = codiceAgevolazioneDipendenti;
	}
	public void setDescrizioneAgevolazioneDipendenti(String descrizioneAgevolazioneDipendenti) {
		this.descrizioneAgevolazioneDipendenti = descrizioneAgevolazioneDipendenti;
	}
 	public String getDescrizioneAgevolazioneDipendenti() {
		return descrizioneAgevolazioneDipendenti;
	}

}
