package prgm.ita.p.dac.display;

import prgm.ita.p.dac.facade.ControlliDac;
import prgm.ita.p.dac.facade.ControlliDacImpl;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class Dac extends DisplayCommand {

	private static final String DAO_XML_NAME_DAC = "ItaPDac.Dac";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			DocumentoModel doc = dac.getDocumento();
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			
			facade.fillCodDesc(csc,dac,false);
			
			dac.initReadonly(csc);
			
			if(dac.getModality() == Template.INSERT_MODALITY || dac.getModality() == Template.UPDATE_MODALITY){
				doc.addCodDescField("codOperazione","OperazioniProdotto");
				doc.addCodDescField("cassetteOperazioniProdotto","CassetteOperazioniProdotto");
			}
			facade.fillCodDesc(csc,doc,true);
			
			doc.initTitolo(dac);
			
			// GESTIONE PRODOTTO / OPERAZIONE
			// Se non in lettura, gestisco il fatto che la coppia prodotto/operazione potrebbe non essere più valida
			// Escludo da questa gestione i documenti che rappresentano un assegno (prod = 0, oper = 0)
			if(!doc.isDocumentoAssegno() &&
			    (dac.getModality() == Template.INSERT_MODALITY || dac.getModality() == Template.UPDATE_MODALITY)){
				gestioneProdOper(csc,dac,doc);				
			}
			
			// Inizializzazione obbligatorietà campi documento
			// E' codice javascript che genero una volta sola nella Dac
			if(dac.getHtmlJavascriptCampiObbligatori() == null){
				ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
				controlli.initHtmlJavascriptCampiObbligatori(csc, dac);
			}
			
			return dac;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void gestioneProdOper(ClientSessionContext csc, DacModel dac, DocumentoModel doc){
		boolean prodottoValido = true;
		if(!doc.getCodProdotto().isNull()){
			CodDescDataList dl = doc.getCodDescDataList("codProdotto");
			CodDescData d = dl.getCodDesc(doc.getCodProdotto().toString());
			if(!d.isValid()){
				String operazione = null;
				if(!doc.getCodOperazione().isNull()){
					try{
						DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
						DAOQueryResultModel qRes = dao.executeQueryAccess("loadDescrizioneOperazione",doc);
						StringType descrOp = (StringType)qRes.getSingleResult();
						if(descrOp != null && !descrOp.isNull()){
							operazione = descrOp.toString();
						}
					}catch(DAOException daoe){}
				}
				String msg = "Il prodotto <b>&#9658; "+d.getDescr()+" &#9668;</b> specificato nel documento, non è più valido";
				if(operazione != null)
					msg = "Il prodotto <b>&#9658; "+d.getDescr()+" &#9668;</b>, specificato nel documento, non è più valido. L'operazione originale era <b>&#9658;"+operazione+" &#9668;</b>";
				doc.getCodProdotto().addTypeError(msg);
				doc.setMsgProdottoOperazioneNonPiuValidi(msg);
				doc.initTitolo(dac,d.getDescr(),operazione);
				prodottoValido = false;
			}
		}
		if(!prodottoValido)
			return;
		
		if(!doc.getCodOperazione().isNull()){
			CodDescDataList dl = doc.getCodDescDataList("codOperazione");
			if(dl != null){
				CodDescData d = dl.getCodDesc(doc.getCodOperazione().toString());
				if(d == null){
					try{
						DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
						DAOQueryResultModel qRes = dao.executeQueryAccess("loadDescrizioneOperazione",doc);
						StringType descrOp = (StringType)qRes.getSingleResult();
						String msg = ""; String dop = "";
						if(descrOp != null && !descrOp.isNull()){
							dop = descrOp.toString();
							msg = "L'operazione <b>&#9658; "+dop+" &#9668;</b> specificata nel documento, non è più valida";
						}else{
							msg = "L'operazione specificata nel documento, non è più valida";
						}
						doc.getCodOperazione().addTypeError(msg);
						doc.setMsgProdottoOperazioneNonPiuValidi(msg);
						doc.initTitolo(dac,doc.getDescValue("codProdotto"),dop);
					}catch(DAOException daoe){}
				}else if(!d.isValid()){
					String msg = "L'operazione <b>&#9658; "+d.getDescr()+" &#9668;</b> specificata nel documento, non è più valida";
					doc.getCodOperazione().addTypeError(msg);
					doc.setMsgProdottoOperazioneNonPiuValidi(msg);
					doc.initTitolo(dac,doc.getDescValue("codProdotto"),d.getDescr());
				}
			}
		}
	}

}
