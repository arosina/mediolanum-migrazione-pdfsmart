package prgm.ita.p.dac.sede.facade;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.ErroreDocumentoModel;
import prgm.ita.p.dac.model.PlicoModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;


/***********************************************************************************************/
/***********************************************************************************************/
public class ControlliDacSedeImpl implements ControlliDacSede{

	private static final String DAO_XML_NAME_SEDE = "ItaPDac.DacSede";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaDacInSpedizione(ClientSessionContext csc, DacModel dac) throws Exception {
		
		DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
		boolean docMancante = false;
		
		try{
			dac.resetCommandErrors();
			dac.setPlichiNonCompletiInSpedizione(new ListType());

			//Controllo errori rilevati in lettura documenti
			for (int i=0; i<dac.getErroriDocumento().size(); i++) {
				ErroreDocumentoModel err = (ErroreDocumentoModel)dac.getErroriDocumento().get(i);
				if (err.getDaAutorizzare().booleanValue() && err.getDataAutorizzazione().isNull()) {
					dac.addCommandError("Sono presenti errori sui documenti che devono ancora essere autorizzati");
					return false;
				}
			}
			
			if (!dac.isMgmPlichi())
				return true;

			//Recupero elenco documenti aggregati
			ListType documentiInPlichi = daoSede.executeQueryAccess("loadDocoumentiPlichiMancantiInDac",dac).getResult();
			if (documentiInPlichi.size()==0)
				return true;	//Nessun doc in plico
			
			//Aggrego i documenti per codice aggregatore
			Hashtable plichi = new Hashtable();
			ListType elencoDoc = null;
			Iterator i = documentiInPlichi.getIterator();
			while (i.hasNext()){
				DocumentoModel doc = (DocumentoModel)i.next();
				
				if (doc.getIdDac().isNull())
					docMancante=true;
				
				if (plichi.containsKey(doc.getCodAggregatore())){
					elencoDoc = (ListType)plichi.get(doc.getCodAggregatore());
				}else{
					elencoDoc = new ListType(DocumentoModel.class);
				}
				elencoDoc.add(doc);
				plichi.put(doc.getCodAggregatore(), elencoDoc);	
			}
			
			if (!docMancante)
				return true;	//Tutti i doc in plichi sono in dac
			
			//Creo l'elenco dei plichi
			Set elencoCodAggregatori = plichi.keySet();
			Iterator b = elencoCodAggregatori.iterator();
			while (b.hasNext()){
				StringType codAggregatore = (StringType)b.next();
				
				//Scarto i plichi completi
				Iterator j = documentiInPlichi.getIterator();
				docMancante=false;
				while (j.hasNext()){
					DocumentoModel doc = (DocumentoModel)j.next();
					
					if (doc.getCodAggregatore().equals(codAggregatore) && doc.getIdDac().isNull())
						docMancante=true;						
				}
				if (!docMancante)
					continue;	//Passo al prossimo plico
				
				//Creo il plico
				PlicoModel plico = new PlicoModel();
				plico.setIdPlico(codAggregatore);
				plico.setDocumenti((ListType)plichi.get(codAggregatore));
				dac.getPlichiNonCompletiInSpedizione().add(plico);
			}
			return false;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean controllaDacInChiusura(ClientSessionContext csc, DacModel dac) throws Exception {

		boolean result = true;
		
		dac.resetCommandErrors();
		Tools.resetTypesErrors(dac);
			
		//Controllo errori rilevati in lettura documenti
		for (int i=0; i<dac.getErroriDocumento().size(); i++) {
			ErroreDocumentoModel err = (ErroreDocumentoModel)dac.getErroriDocumento().get(i);
			if (err.getDaAutorizzare().booleanValue() && err.getDataAutorizzazione().isNull()) {
				dac.addCommandError("Sono presenti errori sui documenti che devono ancora essere autorizzati");
				return false;
			}
		}

		for (int i=0; i<dac.getDocumenti().size(); i++){
			
			DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i); 
			if (doc.getIsErroreSmistamento().booleanValue()){
				dac.addCommandError("Uno o più documenti risultano bloccati in altri uffici");
				result=false;
			}

			boolean isMgmPlichi = dac.isMgmPlichi();
			boolean docFiglioPlico = !doc.getCodAggregatore().isNull() && !doc.getIsFirstInPlico().booleanValue();
			boolean docNonPervenuto = doc.getNonPervenuto().booleanValue();
			boolean docRicevuto = doc.getIdDac().equals(Costanti.ID_DAC_X_DOC_FUORI_DAC);
			
			if (docNonPervenuto)
				continue;
			
			if (isMgmPlichi && docFiglioPlico) {
				if (!docRicevuto) {
					dac.addCommandError("Uno o più documenti non risultano essere lavorati");
					result=false;
				}
			} else {
				if (!docRicevuto) {
					doc.getNonPervenuto().addTypeError("Spuntare il campo se il documento cartaceo non è pervenuto");
					result=false;
				}
			}
		}

		if (!result)
			dac.addCommandError(new CommandError("Riscontrati errori nella chiusura della DAC"));
		
		return result;
	}
	
}
