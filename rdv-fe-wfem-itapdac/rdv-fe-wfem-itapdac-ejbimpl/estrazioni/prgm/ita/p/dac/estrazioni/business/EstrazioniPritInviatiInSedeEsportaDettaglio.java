package prgm.ita.p.dac.estrazioni.business;

import java.util.Date;

import prgm.ita.p.dac.estrazioni.model.DettaglioDocRicevutiDacModel;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.SegmentedResponseCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

public class EstrazioniPritInviatiInSedeEsportaDettaglio extends
		SegmentedResponseCommand {

	private static final String DAO_XML_NAME = "ItaPDac.Estrazioni";
	private DAOObject dao = null;
	private DAOQueryResultModel qRes = null;
	private double loop = 0;
	private PritInviatiInSedeModel model=null;
	private DateType dataInvio=null;
	private DateType dataAl=null;
	private StringType statoPrit=null;
	private StringType tipologiaPrit=null; 
	
	
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
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='11'><font color='#FFFFFF'>Report di dettaglio</font></th>");
				sb.append("</tr>");	
				sb.append("<tr>");
				sb.append(" <td colspan='11'bgcolor='#FFFFFF' align='left' >Inviati in sede dal: "+dataInvio+" al: " + dataAl + "</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='11'bgcolor='#FFFFFF' align='left' >Stampato il "+Tools.today()+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='11'bgcolor='#FFFFFF' align='left' >Tipo: "+tipologiaPrit+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='11'bgcolor='#FFFFFF' align='left' >Stato: "+statoPrit+"</td>");
				sb.append("</tr>");
			}
			loop++;
			
			if(loop==1){
				//titolo dei vari dettagli
				sb.append("<tr>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Data invio in sede</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Data spunta</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Barcode</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice cliente</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome e nome cliente</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice agente</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Cognome e nome agente</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice prodotto</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descrizione prodotto</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Codice operazione</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Descrizione operazione</th>");
				sb.append("</tr>");
			}
			
			
			DettaglioDocRicevutiDacModel dettaglio =null;
			
			for(int i=0;i<5;i++){
				try {
					dettaglio = (DettaglioDocRicevutiDacModel)dao.fetchQuery(qRes);
				
					if(dettaglio == null){
						sb.append("</table>");
						loop = -1;
						break;
					}
					sb.append("<tr>");
					sb.append("	<td>"+getStringString(new StringType(dettaglio.getDataInvioInSede().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(dettaglio.getDataSpunta().toString()))+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getBarCode())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodCliente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCognomeCliente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getCodAgente())+"</td>");
					sb.append("	<td>"+getStringString(dettaglio.getNominativoAgente())+"</td>");
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
		return "EstrazioneDettaglio.xls";
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
		model=(PritInviatiInSedeModel)dataModel;
		dao=new DAOObject(csc, DAO_XML_NAME);
		dataInvio=model.getDataDal();
		dataAl = new DateType(model.getDataDal().dateValue());
		dataAl.addDays(6);
		statoPrit = new StringType(model.getDescValue("statoPrit").toString());
		tipologiaPrit= new StringType(model.getDescValue("tipologiaPrit").toString());
		
		//carico tot
		try {
			//carico i dettagli
			qRes = dao.executeFetchableQueryAccess("outputReportDettaglio", model);
		
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Class getInputViewClass() {
		return PritInviatiInSedeModel.class;
	}

}
