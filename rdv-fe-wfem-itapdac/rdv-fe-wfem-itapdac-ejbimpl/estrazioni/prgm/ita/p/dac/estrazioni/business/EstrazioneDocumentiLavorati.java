package prgm.ita.p.dac.estrazioni.business;

import javax.ejb.EJBException;

import prgm.ita.p.dac.estrazioni.model.DettaglioModel;
import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;
import prgm.ita.p.dac.estrazioni.model.ServiceModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.SegmentedResponseCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

public class EstrazioneDocumentiLavorati extends SegmentedResponseCommand{
	private static final String DAO_XML_NAME = "ItaPDac.Estrazioni";
	private DAOObject dao = null;
	private DAOQueryResultModel qRes = null;
	private double loop = 0;
	private EstrazioniModel model=null;
	private ServiceModel serviceModel=null;
	private ListType service=new ListType(ServiceModel.class);
	
	public boolean resourceExist() {
		return true;
	}

	public byte[] getResponseSegment() throws CommandException {
		if(loop==-1)
				return null;
		StringBuffer sb = new StringBuffer();
		if(loop == 0){
			sb.append("<table cellspacing='0' cellpadding='2' border='1' >");
			sb.append("<tr>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='21' ><font color='#FFFFFF'>Estrazione documenti lavorati dai Service</font></th>");
			sb.append("</tr>");	
			sb.append("<tr>");
			sb.append("<td colspan='21'bgcolor='#FFFFFF' ></td>");
			sb.append("</tr>");
		}
		loop++;
		
		if(loop==1){
			for(int j=0;j<service.size();j++){
				ServiceModel sModel=(ServiceModel)service.get(j);
				sb.append("<tr>");
				sb.append("<td colspan='11'bgcolor='#FFFFFF' colspan='2'></td>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='2' ><font color='#FFFFFF'>"+getStringString(new StringType(sModel.getService()+""))+"</font></th>");//
				sb.append("	<td colspan='2'>"+getStringString(new StringType(sModel.getTotDocumenti()+""))+"</td>");//
				sb.append("</tr>");
			}
			sb.append("<tr>");
			sb.append("<td colspan='21'bgcolor='#FFFFFF' ></td>");
			sb.append("</tr>");
			
		}
		loop++;
		DettaglioModel model=null;
		
		if(loop==2){
			//titolo dei vari dettagli
			sb.append("<tr>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Service</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Prit</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Data Spunta</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Barcode</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Cliente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome Cliente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Nome Cliente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Agente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome Agente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Nome Agente</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Agenzia</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Prodotto</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descizione Prodotto</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Operazione</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descrizione Operazione</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Division</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome Division</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Nome Division</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Region</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome Region</th>");
			sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Nome Region</th>");
			sb.append("</tr>");
		}
		
		for(int i=0;i<5;i++){
			try {
				model = (DettaglioModel)dao.fetchQuery(qRes);
			if(model == null){
			sb.append("</table>");
			loop = -1;
			break;
			}
		//stampo dettagli
			sb.append("<tr>");
			sb.append("	<td>"+getStringString(model.getService())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodPrit())+"</td>");
			sb.append("	<td>"+getStringString(new StringType(model.getDataSpunta().toString()))+"</td>");
			sb.append("	<td>"+getStringString(model.getBarcode())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodCliente())+"</td>");
			sb.append("	<td>"+getStringString(model.getCognomeCliente())+"</td>");
			sb.append("	<td>"+getStringString(model.getNomeCliente())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodAgente())+"</td>");
			sb.append("	<td>"+getStringString(model.getCognomeAgente())+"</td>");
			sb.append("	<td>"+getStringString(model.getNomeAgente())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodAgenzia())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodProdotto())+"</td>");
			sb.append("	<td>"+getStringString(model.getDescPrdotto())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodOperazione())+"</td>");
			sb.append("	<td>"+getStringString(model.getDescOperazione())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodDivision())+"</td>");
			sb.append("	<td>"+getStringString(model.getCognomeDivision())+"</td>");
			sb.append("	<td>"+getStringString(model.getNomeDivision())+"</td>");
			sb.append("	<td>"+getStringString(model.getCodRegion())+"</td>");
			sb.append("	<td>"+getStringString(model.getCognomeRegion())+"</td>");
			sb.append("	<td>"+getStringString(model.getNomeRegion())+"</td>");
			sb.append("</tr>");
		
		
		
			} catch (Exception e) {
			throw new CommandException("Eccezione generica durante la creazione del file Excel: " + e.toString());
			} catch (DAOException e) {
			throw new CommandException("Eccezione DAO durante la creazione del file Excel: " + e.toString());
			}
		}
		
		
		return sb.toString().getBytes();
	}

	public String getContentType() {
		return null;
	}

	public String getFileName() {
		return "EstrazioneDocumentiLavorati.xls";
	}

	public int getResponseLength() {
		return 0;
	}

	public void responseTerminated() {
		dao.closeConnection();
	}
	private String getStringString(StringType val){
		return "&nbsp;"+Tools.convertSpecialChars(val.toString());
	}
	
	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			EstrazioniModel model=(EstrazioniModel)dataModel;
			
			
			dao=new DAOObject(csc, DAO_XML_NAME);
		
			//carico il tot Spuntato
			qRes = dao.executeQueryAccess("getTotaleSpuntato", model);
			service=qRes.getResult();
			
			//carico i dettagli
			qRes = dao.executeFetchableQueryAccess("getDettagli", model);
			
		
		} catch (DAOException e) {
			LOG.error(e);
			EJBException ejbEx = new EJBException(" Eccezione durante l'esportazione in excel dei dati: " + e.toString());
			throw ejbEx;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return null;
	}

	public Class getInputViewClass() {
		// TODO Auto-generated method stub
		return EstrazioniModel.class;
	}

}
