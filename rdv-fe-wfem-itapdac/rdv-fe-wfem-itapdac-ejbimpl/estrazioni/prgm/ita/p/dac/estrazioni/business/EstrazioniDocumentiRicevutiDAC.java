package prgm.ita.p.dac.estrazioni.business;

import prgm.ita.p.dac.estrazioni.model.ConteggioModel;
import prgm.ita.p.dac.estrazioni.model.DettaglioDocRicevutiDacModel;
import prgm.ita.p.dac.estrazioni.model.DocumentiRicevutiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.SegmentedResponseCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

public class EstrazioniDocumentiRicevutiDAC extends SegmentedResponseCommand {
	
	private static final String DAO_XML_NAME = "ItaPDac.Estrazioni";
	private DAOObject dao = null;
	private DAOQueryResultModel qRes = null;
	private double loop = 0;
	private DocumentiRicevutiModel model=null;
	private ConteggioModel conteggio=new ConteggioModel();
	private DateType dataInizio=null;
	private DateType dataFine=null;
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
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='14' ><font color='#FFFFFF'>Report conteggio documenti ricevuti nelle DAC</font></th>");
				sb.append("</tr>");	
				sb.append("<tr >");
				sb.append("<td colspan='14'bgcolor='#FFFFFF' align='center' >Conteggio dei documenti ricevuti dal giorno "+dataInizio+" al giorno "+dataFine+"</td>");
				sb.append("</tr>");
			}
			loop++;
	
			if(loop==1){
				sb.append("<tr>");
				sb.append("<td bgcolor='#FFFFFF' colspan='2'></td>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='2' ><font color='#FFFFFF'>Numero Dac</font></th>");//
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='2' ><font color='#FFFFFF'>Numero Doc</font></th>");//
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append("	<td colspan='2'></td>");//
				sb.append("	<td colspan='2'>"+conteggio.getNumeroDAC()+"</td>");//
				sb.append("	<td colspan='2'>"+conteggio.getNumeroDoc()+"</td>");//
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append("<td colspan='14'bgcolor='#FFFFFF' ></td>");
				sb.append("</tr>");					
			}
					
			loop++;
			DettaglioDocRicevutiDacModel dettaglio =null;
				
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
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Nominativo Agente</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Agenzia</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Prodotto</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descrizione Prodotto</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice Operazione</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descrizione Operazione</th>");
				sb.append("</tr>");
			}
				
			for(int i=0;i<5;i++){
				try {
					dettaglio = (DettaglioDocRicevutiDacModel)dao.fetchQuery(qRes);
				
					if(dettaglio == null){
						sb.append("</table>");
						loop = -1;
						break;
					}
					sb.append("<tr>");
					sb.append("	<td>"+getStringString(dettaglio.getService())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodPrit())+"</td>");
					sb.append("	<td>"+getStringString(new StringType(dettaglio.getDataSpunta().toString()))+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getBarCode())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodCliente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCognomeCliente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getNomeCliente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodAgente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getNominativoAgente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodAgenzia())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodProdotto())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getDescProdotto())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodOperazione())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getDescOperazione())+"</td>");
						
					sb.append("</tr>");
				} catch (Exception e) {
					throw new CommandException("Eccezione generica durante la creazione del file Excel: " + e.toString());
				} catch (DAOException e) {
					throw new CommandException("Eccezione DAO durante la creazione del file Excel: " + e.toString());
				}
			}//for
	
		return sb.toString().getBytes();
	}

	public String getContentType() {
		return null;
	}

	public String getFileName() {
		return "EstrazioniDocumentiRicevutiDAC.xls";
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
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		
		model=(DocumentiRicevutiModel)dataModel;
		dao=new DAOObject(csc, DAO_XML_NAME);
		dataInizio=model.getDataInizio();
		dataFine=model.getDataFine();
		//carico tot
		try {
			qRes = dao.executeQueryAccess("conteggio", model);
			
			conteggio=(ConteggioModel)qRes.getResult().get(0);
			//carico i dettagli
			
			qRes = dao.executeFetchableQueryAccess("getDettagliConteggioDocumentiRicDAC", model);
			//conteggio=(ConteggioModel)qRes.getResult().get(0);
		
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public Class getInputViewClass() {
		return DocumentiRicevutiModel.class;
	}

}
