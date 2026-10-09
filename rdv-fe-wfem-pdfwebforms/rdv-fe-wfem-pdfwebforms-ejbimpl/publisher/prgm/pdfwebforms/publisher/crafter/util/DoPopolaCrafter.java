package prgm.pdfwebforms.publisher.crafter.util;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.crafter.CrafterPdfUtils;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class DoPopolaCrafter extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
        try {
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
            PopolaCrafterUtilModel model = (PopolaCrafterUtilModel)dataModel;
            
            model.setPopolamentoInterrotto(false);
            model.setResultMessage(null);
            model.setPdfElaborati(new IntegerType());
            
            if(model.getAree().isNull() && model.getIndicatori().isNull()){
            	model.setResultMessage("Nessuna area o indicatore di pdf specificato");
            }else if(!model.getAree().isNull() && !model.getIndicatori().isNull()){
                model.setResultMessage("Specificare o le aree o gli indicatori di pdf, non entrambe");
            }else{
            	
            	Thread.sleep(1000);
            	if(model.isPopolamentoInterrotto()){
            		model.setResultMessage("Popolamento interrotto. Nessun pdf gestito");
            		setForwardDisplay(new Integer(0));
                    return model;
            	}
            	
            	DAOObject dao = null;
            	try{
	            	dao = new DAOObject(csc, "PdfWebForms.PdfCrafterPublisher");
	            	dao.openConnection();
	            	if(!model.getAree().isNull()){
	            		model.setResultMessage(doPopolaAree(csc, dao, model));
	            	}else if(!model.getIndicatori().isNull()){
	            		model.setResultMessage(doPopolaIndicatori(csc, dao, model));
	            	}else{
	            		model.setResultMessage("Nessuna attività svolta");
	            	}
            	}catch(DAOException daoe){
            		model.setResultMessage(daoe.toString());
            	}catch(Exception e){
            		model.setResultMessage(e.toString());
	            }finally{
	    			if(dao != null) dao.closeConnection();
	    		}	
            }
            
            setForwardDisplay(new Integer(0));
            return model;
        } catch(Exception e) {
            throw new CommandException(e);
        }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopolaCrafterUtilModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String doPopolaAree(ClientSessionContext csc, DAOObject dao, PopolaCrafterUtilModel model) throws Exception, DAOException{
		
		String[] aree = model.getAree().toString().split("\\,");
		String filtro = "";
		for(int i=0;i<aree.length;i++){
			String area = aree[i].trim();
			if(area.length() > 0)
				filtro += "'"+area.toUpperCase()+"',";
		}
		if(filtro.length() == 0)
			return "Nessuna area impostata";
		
		model.setFiltro(new StringType(filtro.substring(0,filtro.length()-1)));
		model.setPdfElaborati(new IntegerType(0));
		DAOQueryResultModel qRes = dao.executeFetchableQueryAccess("popolaPerAree",model);
		for(;;){
			PdfAnagKeyModel pdfAnagKey = (PdfAnagKeyModel)dao.fetchQuery(qRes);
			if(pdfAnagKey == null || model.isPopolamentoInterrotto())
				break;
			
            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
            PdfConfigurationModel confModel = f.openPdfConf(csc, pdfAnagKey, true);
			CrafterPdfUtils.moveToCrafter(csc, confModel, false);

			model.setPdfElaborati(new IntegerType(model.getPdfElaborati().intValue()+1));
		}
		return "Popolamento crafter per aree "+(model.isPopolamentoInterrotto()?"interrotto":"effettuato")+". Popolati "+model.getPdfElaborati()+" pdf";
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String doPopolaIndicatori(ClientSessionContext csc, DAOObject dao, PopolaCrafterUtilModel model) throws Exception, DAOException{
		String[] indicatori = model.getIndicatori().toString().trim().replaceAll("[\\s;\\n]+",",").split("\\,");
		String filtro = "";
		for(int i=0;i<indicatori.length;i++){
			String indicatore = indicatori[i].trim();
			if(indicatore.length() > 0)
				filtro += "'"+indicatore.toUpperCase()+"',";
		}
		if(filtro.length() == 0)
			return "Nessun indicatore impostato";
		
		model.setFiltro(new StringType(filtro.substring(0,filtro.length()-1)));
		model.setPdfElaborati(new IntegerType(0));
		DAOQueryResultModel qRes = dao.executeFetchableQueryAccess("popolaPerIndicatori",model);
		for(;;){
			PdfAnagKeyModel pdfAnagKey = (PdfAnagKeyModel)dao.fetchQuery(qRes);
			if(pdfAnagKey == null || model.isPopolamentoInterrotto())
				break;
			
            PdfAnagFacade f = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class);
            PdfConfigurationModel confModel = f.openPdfConf(csc, pdfAnagKey, true);
			CrafterPdfUtils.moveToCrafter(csc, confModel, false);

			model.setPdfElaborati(new IntegerType(model.getPdfElaborati().intValue()+1));
		}
		return "Popolamento crafter per indicatori "+(model.isPopolamentoInterrotto()?"interrotto":"effettuato")+". Popolati "+model.getPdfElaborati()+" pdf";
	}
}
