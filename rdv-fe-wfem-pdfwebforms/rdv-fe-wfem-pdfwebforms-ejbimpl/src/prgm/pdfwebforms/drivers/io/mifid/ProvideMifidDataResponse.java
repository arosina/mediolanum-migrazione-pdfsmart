package prgm.pdfwebforms.drivers.io.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfPersonModel;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideMifidDataResponse extends CommandDataModel{
	
    private StringType      contoDiAddebito = new StringType();       					// Se specificato permette di identificare i clienti su cui considerare l'addebito
    private StringType      flagAdeguatezza = new StringType("S");         				// Impostare a "S" se si vuole calcolare l'adeguatezza. A "N" per l'appropriatezza
    private IntegerType     contatoreOperazioni = new IntegerType("1");					// Sempre a 1 tranne per i Titoli dove si avrà il numero operazioni
    private ListType		clienti = new ListType(PdfPersonModel.class);				// Elenco dei clienti di riferiemnto delle operazioni
    private ListType    	acquisti = new ListType(OperazioneMifidModel.class);		// Elenco delle operazioni di acquisto
    private ListType	    disinvestimenti = new ListType(OperazioneMifidModel.class);	// Elenco delle operazioni di disinvestimento contestuali agli acquisti
    
    // RFC #281217: Elenco degli elementi per il controllo sulla concentrazione FIA
    private ListType		elementiControlloConcentrazioneFia = new ListType(ElementoControlliConcentrazioneFiaModel.class);
    
    /***********************************************************************************************/
    /***********************************************************************************************/
    public void addCliente(PdfPersonModel cliente){
    	getClienti().add(cliente);
    }
    
    /***********************************************************************************************/
    /***********************************************************************************************/
    public void addAcquisto(OperazioneMifidModel operazione){
    	getAcquisti().add(operazione);
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    public void addDisinvestimento(OperazioneMifidModel operazione){
    	getDisinvestimenti().add(operazione);
    }

    /***********************************************************************************************/
    /***********************************************************************************************/
    public void addElementoControlloConcentrazioneFia(ElementoControlliConcentrazioneFiaModel elementoControlloConcentrazioneFia){
    	getElementiControlloConcentrazioneFia().add(elementoControlloConcentrazioneFia);
    }

    public StringType getContoDiAddebito() {
		return contoDiAddebito;
	}
	public void setContoDiAddebito(StringType contoDiAddebito) {
		this.contoDiAddebito = contoDiAddebito;
	}
	public StringType getFlagAdeguatezza() {
		return flagAdeguatezza;
	}
	public void setFlagAdeguatezza(StringType flagAdeguatezza) {
		this.flagAdeguatezza = flagAdeguatezza;
	}
	public IntegerType getContatoreOperazioni() {
		return contatoreOperazioni;
	}
	public void setContatoreOperazioni(IntegerType contatoreOperazioni) {
		this.contatoreOperazioni = contatoreOperazioni;
	}
	public ListType getClienti() {
		return clienti;
	}
	public void setClienti(ListType clienti) {
		this.clienti = clienti;
	}
	public ListType getAcquisti() {
		return acquisti;
	}
	public void setAcquisti(ListType acquisti) {
		this.acquisti = acquisti;
	}
	public ListType getDisinvestimenti() {
		return disinvestimenti;
	}
	public void setDisinvestimenti(ListType disinvestimenti) {
		this.disinvestimenti = disinvestimenti;
	}

	public ListType getElementiControlloConcentrazioneFia() {
		return elementiControlloConcentrazioneFia;
	}

	public void setElementiControlloConcentrazioneFia(ListType elementiControlloConcentrazioneFia) {
		this.elementiControlloConcentrazioneFia = elementiControlloConcentrazioneFia;
	}

}
