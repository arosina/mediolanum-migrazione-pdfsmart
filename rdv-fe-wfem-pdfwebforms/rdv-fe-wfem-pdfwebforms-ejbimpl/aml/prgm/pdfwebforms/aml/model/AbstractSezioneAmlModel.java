package prgm.pdfwebforms.aml.model;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractSezioneAmlModel extends CommandDataModel{

	private IntegerType 	elencoSinistraContScrollPos = new IntegerType(0);
	private IntegerType 	elencoDestraContScrollPos = new IntegerType(0);
	
	private IntegerType		dispositivaSelezionata = new IntegerType(0);
	private transient 		List<DispositivaAmlContainer> elencoDispositive = new ArrayList<DispositivaAmlContainer>();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String drawIcons() {
		try {
			return Tools.containsTypeErrors(this)?AmlModel.ERROR_IMG:"";
		}catch(Exception e) {
			return "";
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel findPdf(PdfModel pdf) {
		for(int i=0;i<getElencoDispositive().size();i++) {
			DispositivaAmlContainer dc = getElencoDispositive().get(i);
			if(dc.getPdf() == pdf) // Se l'input ha l'indirizzo in lista l'abbiamo trovato
				return pdf;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void eliminaDispositiva(int dispoIdx) {
		getElencoDispositive().remove(dispoIdx);
	}
	
	public List<DispositivaAmlContainer> getElencoDispositive() {
		return elencoDispositive;
	}
	public void setElencoDispositive(List<DispositivaAmlContainer> elencoDispositive) {
		this.elencoDispositive = elencoDispositive;
	}
	public IntegerType getDispositivaSelezionata() {
		return dispositivaSelezionata;
	}
	public void setDispositivaSelezionata(IntegerType dispositivaSelezionata) {
		this.dispositivaSelezionata = dispositivaSelezionata;
	}

	public IntegerType getElencoSinistraContScrollPos() {
		return elencoSinistraContScrollPos;
	}

	public void setElencoSinistraContScrollPos(IntegerType elencoSinistraContScrollPos) {
		this.elencoSinistraContScrollPos = elencoSinistraContScrollPos;
	}

	public IntegerType getElencoDestraContScrollPos() {
		return elencoDestraContScrollPos;
	}

	public void setElencoDestraContScrollPos(IntegerType elencoDestraContScrollPos) {
		this.elencoDestraContScrollPos = elencoDestraContScrollPos;
	}

}
