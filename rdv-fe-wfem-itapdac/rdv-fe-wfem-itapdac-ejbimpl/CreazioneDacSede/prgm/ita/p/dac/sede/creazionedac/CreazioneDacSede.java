package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.Costanti;
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

/***********************************************************************************************/
/***********************************************************************************************/
public class CreazioneDacSede extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			
			CodDescDataList cdd = new CodDescDataList();
			CodDescData cd = null;
			cd = new CodDescData();
			cd.setCod(Costanti.ORA_SPEDIZIONE_8); cd.setDescr("08.00");
			cdd.addCodDescData(cd);
			cd = new CodDescData();
			cd.setCod(Costanti.ORA_SPEDIZIONE_12); cd.setDescr("12.00");
			cdd.addCodDescData(cd);
			cd = new CodDescData();
			cd.setCod(Costanti.ORA_SPEDIZIONE_14_30); cd.setDescr("14.30");
			cdd.addCodDescData(cd);
			cd = new CodDescData();
			cd.setCod(Costanti.ORA_SPEDIZIONE_16_30); cd.setDescr("16.30");
			cdd.addCodDescData(cd);
			cd = new CodDescData();
			cd.setCod(Costanti.ORA_SPEDIZIONE_19_30); cd.setDescr("19.30");
			cdd.addCodDescData(cd);
			dac.addCodDescField("oraSpedizione", cdd);
			
			dac.addCodDescField("ufficio", "Uffici");
			
			if (dac.isSmistatore()){
				if(dac.getUffDestinatario().isNull())
					dac.addCodDescField("box", "Box");
				else
					dac.addCodDescField("box", "UfficiBox");

				dac.addCodDescField("uffDestinatario", "UfficiDestinatariBox");
			}else{
				dac.addCodDescField("uffDestinatario", "UfficiDestinatariDac");
			}	
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = (DacModel)facade.fillCodDesc(csc,dac,false);
			dac.setDocumento((DocumentoModel)facade.fillCodDesc(csc,dac.getDocumento(),true));
			
			dac.initReadonlySede();
			dac.getDocumento().initTitolo(dac);
			
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

}
