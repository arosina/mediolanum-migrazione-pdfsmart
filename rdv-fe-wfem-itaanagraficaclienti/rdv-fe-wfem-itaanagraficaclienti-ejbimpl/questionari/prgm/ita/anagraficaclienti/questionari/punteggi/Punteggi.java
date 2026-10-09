package prgm.ita.anagraficaclienti.questionari.punteggi;

import java.util.ArrayList;

import prgm.cedacri.adeguatezza.model.ElementoQuestionarioRisposteModel;

import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class Punteggi {

	static ArrayList<Punteggio> punteggi = new ArrayList<Punteggio>();
	static{
		// Sezione A
		punteggi.add(new Punteggio( 1, 1, 4));
		punteggi.add(new Punteggio( 1, 2, 3));
		punteggi.add(new Punteggio( 1, 3, 2));
		punteggi.add(new Punteggio( 1, 4, 1));
		punteggi.add(new Punteggio( 1, 5, 0));
		
		punteggi.add(new Punteggio( 2, 1, 0));
		punteggi.add(new Punteggio( 2, 2, 2));
		punteggi.add(new Punteggio( 2, 3, 6));
		punteggi.add(new Punteggio( 2, 4, 10));
		punteggi.add(new Punteggio( 2, 5, 12));

		punteggi.add(new Punteggio( 3, 1, 3));
		punteggi.add(new Punteggio( 3, 2, 2));
		punteggi.add(new Punteggio( 3, 3, 1));
		punteggi.add(new Punteggio( 3, 4, 0));
		
		// Sezione B
		punteggi.add(new Punteggio( 4, 1, 5));
		punteggi.add(new Punteggio( 4, 2, 3));
		punteggi.add(new Punteggio( 4, 3, 1));
		punteggi.add(new Punteggio( 4, 4, 0));
		
		punteggi.add(new Punteggio( 5, 1, 0));
		punteggi.add(new Punteggio( 5, 2, 2));
		punteggi.add(new Punteggio( 5, 3, 4));
		punteggi.add(new Punteggio( 5, 4, 6));

		punteggi.add(new Punteggio( 6, 1, 0));
		punteggi.add(new Punteggio( 6, 2, 1));
		punteggi.add(new Punteggio( 6, 3, 3));
		
		punteggi.add(new Punteggio( 7, 1, 0));
		punteggi.add(new Punteggio( 7, 2, 1));
		punteggi.add(new Punteggio( 7, 3, 2));
		punteggi.add(new Punteggio( 7, 4, 3));
		punteggi.add(new Punteggio( 7, 5, 4));

		punteggi.add(new Punteggio( 8, 1,10));
		punteggi.add(new Punteggio( 8, 2, 9));
		punteggi.add(new Punteggio( 8, 3, 8));
		punteggi.add(new Punteggio( 8, 4, 7));
		punteggi.add(new Punteggio( 8, 5, 6));
		punteggi.add(new Punteggio( 8, 6, 5));
		punteggi.add(new Punteggio( 8, 7, 4));
		punteggi.add(new Punteggio( 8, 8, 3));
		punteggi.add(new Punteggio( 8, 9, 2));
		punteggi.add(new Punteggio( 8,10, 0));
		
		// Sezione C
		punteggi.add(new Punteggio( 9, 1, 0));
		punteggi.add(new Punteggio( 9, 2, 4));
		punteggi.add(new Punteggio( 9, 3, 6));
		punteggi.add(new Punteggio( 9, 4, 8));
		punteggi.add(new Punteggio( 9, 5,10));

		punteggi.add(new Punteggio(10, 1, 2));
		punteggi.add(new Punteggio(10, 2, 4));
		punteggi.add(new Punteggio(10, 3, 9));
		
		punteggi.add(new Punteggio(11, 1, 0));
		punteggi.add(new Punteggio(11, 2, 2));
		punteggi.add(new Punteggio(11, 3, 5));
		
		// Sezione D
		punteggi.add(new Punteggio(12, 1, 6));
		punteggi.add(new Punteggio(12, 2, 5));
		punteggi.add(new Punteggio(12, 3, 3));
		punteggi.add(new Punteggio(12, 4, 1));
		punteggi.add(new Punteggio(12, 5, 0));
		
		punteggi.add(new Punteggio(13, 1, 4));
		punteggi.add(new Punteggio(13, 2, 2));
		punteggi.add(new Punteggio(13, 3, 0));
		
		punteggi.add(new Punteggio(14, 1, 1));
		punteggi.add(new Punteggio(14, 2, 1));
		punteggi.add(new Punteggio(14, 3, 1));
		punteggi.add(new Punteggio(14, 4, 1));
		punteggi.add(new Punteggio(14, 5, 1));
		punteggi.add(new Punteggio(14, 6, 1));
		punteggi.add(new Punteggio(14, 7, 1));
		punteggi.add(new Punteggio(14, 8, 1));
		punteggi.add(new Punteggio(14, 9, 1));
		punteggi.add(new Punteggio(14,10, 1));
		
		punteggi.add(new Punteggio(15, 1, 3));
		punteggi.add(new Punteggio(15, 2, 2));
		punteggi.add(new Punteggio(15, 3, 1));
		punteggi.add(new Punteggio(15, 4, 0));
		
		punteggi.add(new Punteggio(16, 1, 4));
		punteggi.add(new Punteggio(16, 2, 3));
		punteggi.add(new Punteggio(16, 3, 2));
		punteggi.add(new Punteggio(16, 4, 0));

		punteggi.add(new Punteggio(17, 1, 5));
		punteggi.add(new Punteggio(17, 2, 4));
		punteggi.add(new Punteggio(17, 3, 3));
		punteggi.add(new Punteggio(17, 4, 0));
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioSezioneA(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 1 || r.getNumElem().intValue() == 2 || r.getNumElem().intValue() == 3)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioSezioneB(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 4 || r.getNumElem().intValue() == 5 || r.getNumElem().intValue() == 6 ||
			   r.getNumElem().intValue() == 7  || r.getNumElem().intValue() == 8)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioSezioneC(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 9 || r.getNumElem().intValue() == 10 || r.getNumElem().intValue() == 11)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioSezioneD(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 12 || r.getNumElem().intValue() == 13 || r.getNumElem().intValue() == 14 ||
			   r.getNumElem().intValue() == 15 || r.getNumElem().intValue() == 16 || r.getNumElem().intValue() == 17)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioDomandaD3(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 14)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggioDomandaD4(ListType risposteCedacri){
		int punteggio = 0;
		for(int i=0; i<risposteCedacri.size(); i++){
			ElementoQuestionarioRisposteModel r = (ElementoQuestionarioRisposteModel)risposteCedacri.get(i);
			if(r.getNumElem().intValue() == 15 || r.getNumElem().intValue() == 16 || r.getNumElem().intValue() == 17)
				punteggio +=  getPunteggio(r);
		}
		return punteggio;
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int getPunteggio(ElementoQuestionarioRisposteModel r){
		for(Punteggio p : punteggi){
			if(p.getNumElem() == r.getNumElem().intValue() && p.getNumSele() == r.getNumSele().intValue())
				return p.getPunteggio();
		}
		return 0;
	}
}
