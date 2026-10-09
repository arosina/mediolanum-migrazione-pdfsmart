package prgm.ita.p.dac.estrazioni.business;

import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeOutputModel;

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

public class EstrazioniPritInviatiInSedeEsportaSintesi extends
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
	private DateType data = null;
	
	public boolean resourceExist() {
		return true;
	}

	public byte[] getResponseSegment() throws CommandException {
		
		data = new DateType(model.getDataDal().dateValue());
		
		if(loop==-1)
			return null;
		StringBuffer sb = new StringBuffer();
			if(loop == 0){
				sb.append("<table cellspacing='0' cellpadding='2' border='1' >");
				sb.append("<tr>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4' colspan='9'><font color='#FFFFFF'>Report di sintesi</font></th>");
				sb.append("</tr>");	
				sb.append("<tr>");
				sb.append(" <td colspan='9'bgcolor='#FFFFFF' align='left' >Inviati in sede dal: "+dataInvio+" al: " + dataAl + "</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='9'bgcolor='#FFFFFF' align='left' >Stampato il "+Tools.today()+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='9'bgcolor='#FFFFFF' align='left' >Tipo: "+tipologiaPrit+"</td>");
				sb.append("</tr>");
				sb.append("<tr>");
				sb.append(" <td colspan='9'bgcolor='#FFFFFF' align='left' >Stato: "+statoPrit+"</td>");
				sb.append("</tr>");
			}
			loop++;
			
			if(loop==1){
				//titolo della sintesi
				sb.append("<tr>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Prodotto</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>Operazione</th>");
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				data.addDays(1);
				sb.append("	<th bordercolor='#000000' bgcolor='#6687C4'>"+data.toString()+"</th>");
				sb.append("</tr>");
			}
			
			PritInviatiInSedeOutputModel sintesi =null;
			
			for(int i=0;i<5;i++){
				try {
					sintesi = (PritInviatiInSedeOutputModel)dao.fetchQuery(qRes);
				
					if(sintesi == null){
						sb.append("</table>");
						loop = -1;
						break;
					}
					sb.append("<tr>");
					sb.append("	<td>"+getStringString(sintesi.getDescProdotto())+"</td>");
					sb.append("	<td>"+getStringString(sintesi.getDescOperazione())+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg1().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg2().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg3().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg4().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg5().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg6().toString()))+"</td>");
					sb.append("	<td>"+getStringString(new StringType(sintesi.getGg7().toString()))+"</td>");
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
		return "EstrazioneSintesi.xls";
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
			//carico la sintesi
			qRes = dao.executeFetchableQueryAccess("outputReportSintesi", model);
		
		} catch (DAOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public Class getInputViewClass() {
		return PritInviatiInSedeModel.class;
	}

}
