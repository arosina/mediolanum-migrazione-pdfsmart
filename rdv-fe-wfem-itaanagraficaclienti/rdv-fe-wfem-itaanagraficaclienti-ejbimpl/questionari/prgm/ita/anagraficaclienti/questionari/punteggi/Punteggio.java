package prgm.ita.anagraficaclienti.questionari.punteggi;

/***********************************************************************************************/
/***********************************************************************************************/
public class Punteggio {
	
	private int numElem;
	private int numSele;
	private int punteggio;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Punteggio(int numElem, int numSele, int punteggio){
		this.numElem = numElem;
		this.numSele = numSele;
		this.punteggio = punteggio;
	}

	public int getNumElem() {
		return numElem;
	}

	public int getNumSele() {
		return numSele;
	}

	public int getPunteggio() {
		return punteggio;
	}

}
