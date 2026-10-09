package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.FieldStyle;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;

/********************************************************************************/
/********************************************************************************/
public class RicercaFondi extends DisplayCommand implements GridDecorator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2777261749986527323L;
	/********************************************************************************/
    /********************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)
			throws CommandException {
		try {
			RicercaFondiModel model = (RicercaFondiModel)dataModel;
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			if (model.getCodProdottoPolizza().equals(Costanti.TARIFFA_PAC) && model.getFlagTrasformatoPic().equals("S")){
				model.setCodiceProdotto(new StringType(Costanti.TARIFFA_PIC));
		 	}else{
		 		model.setCodiceProdotto(model.getCodProdottoPolizza());
		 	}
			if (model.isPrimaAttivazione()){
				model.addCodDescField("tipologiaAppoggio", caricaCombo(csc, "loadTipologiaFondo", model));
				model.addCodDescField("societaAppoggio", caricaCombo(csc, "loadSicav", model));
				// combo ricerca per in portafoglio
				CodDescDataList dataList = new CodDescDataList();
				CodDescData cd = new CodDescData();
				cd.setCod("true");
				cd.setDescr("Si");
				dataList.addCodDescData(cd);
				cd = new CodDescData();
				cd.setCod("false");
				cd.setDescr("No");
				dataList.addCodDescData(cd);
				model.addCodDescField("inPortafoglioAppoggio", dataList);
			} else {
                if (model.getTipoSottoscrizione().isNull()) {
                    model.setElencoFondi(new ListType());
                    return model;
                }

				DAOObject dao = new DAOObject(csc, PdfDriver.DAO_FILE_NAME);
				DAOQueryResultModel result = dao.executeQueryAccess("ricercaComparti", model);
				model.setElencoFondi(result.getResult());
			}
			return model;
		} catch (DAOException daoE) {
			LOG.error(daoE);			
			throw new CommandException(daoE.toString());
		} catch (Exception e) {
			LOG.error(e);			
			throw new CommandException(e.toString());
		}
	}
	/********************************************************************************/
    /********************************************************************************/
	@Override
	public Class getInputViewClass() {
		return RicercaFondiModel.class;
	}
	/********************************************************************************/
    /********************************************************************************/
	private CodDescDataList caricaCombo(ClientSessionContext csc, String queryAccess, RicercaFondiModel model) throws CommandException{
		DAOObject dao = null;
		try {		
			dao = new DAOObject(csc, PdfDriver.DAO_FILE_NAME);
			DAOQueryResultModel result = dao.executeQueryAccess(queryAccess, model);
			ListType tipologie = result.getResult();

			CodDescDataList dataList = new CodDescDataList();
			CodDescData cd = null;
			for (int i = 0; i <tipologie.size(); i++) {
				MapCommandDataModel tipologia = (MapCommandDataModel)tipologie.get(i);
				cd = new CodDescData();
				cd.setCod(((StringType)tipologia.getPropertyValue("cod")).toString());
				cd.setDescr(((StringType)tipologia.getPropertyValue("descr")).toString());
				dataList.addCodDescData(cd);
			}
			return dataList;
		}catch (DAOException daoe) {
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;

		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}
	/********************************************************************************/
    /********************************************************************************/
	public void onNewCell(String listPropertyName, String cellPropertyName, CommandDataModel row, AbstractType cell, int rowIndex, int cellIndex) {

		FieldStyle fs = new FieldStyle();
		if(cellPropertyName.equals("tipologia")){
			if (cell.equals("A"))
				fs.innerHTML = "Azionario";
			else if (cell.equals("B"))
				fs.innerHTML = "Bilanciato";
			else if (cell.equals("M"))
				fs.innerHTML = "Monetario";
			else if (cell.equals("O"))
				fs.innerHTML = "Obbligazionario";
			else if (cell.equals("F"))
				fs.innerHTML = "Flessibile Azionario";
			else if (cell.equals("K"))
				fs.innerHTML = "Flessibile Obbligazionario";
		}
		if (cellPropertyName.equals("lineaFondo")
				|| cellPropertyName.equals("isin")
				|| cellPropertyName.equals("sicav")
				|| cellPropertyName.equals("descFondo")
				|| cellPropertyName.equals("classe")
				|| cellPropertyName.equals("flagConsolidaAz")
				|| cellPropertyName.equals("controvaloreComparto")
				|| cellPropertyName.equals("tipologia")){
			cell.setEditable(false);
		}

		cell.setStyle(fs);
	}

}
