<%@page import="com.atosorigin.wfem.layout.Template"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%@page import="prgm.ita.p.dac.estrazioni.model.*"%>
<%@page import="com.atosorigin.wfem.types.*"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%
	template.setWlt(true);
	template.setApplCode("ITAPDAC");
	template.setPageName("EstrazioniPritInviatiInSedeSintesi");;
	
	PritInviatiInSedeModel model = (PritInviatiInSedeModel)template.getPageDataModel();

	DateType data = new DateType(model.getDataDal().dateValue());
	DateType dataAl = new DateType(model.getDataDal().dateValue());
	dataAl.addDays(6);
	
%>
<html>
<!-- PARAMENTRI IMPOSTAZIONE PDF

PAGEORIENTATION = "Horizontal"
MARGINLEFT = "0.4"
MARGINRIGHT = "0.4"

-->
<body>
	<table cellspacing='0' cellpadding='2' border='0'>
		<tr>
		<td align='center'><font size="5"><b>Report di dettaglio</b></font></td>
		</tr>	
		<tr>
		<td><font size="1">Inviati in sede dal: <%=model.getDataDal() %> al: <%=dataAl %></font></td>
		</tr>
		<tr>
		<td align='left'><font size="1">Stampato il <%= Tools.today()%></font></td>
		</tr>
		<tr>
		<td align='left'><font size="1">Tipo: <%=model.getDescValue("tipologiaPrit")%></font></td>
		</tr>
		<tr>
		<td align='left'><font size="1">Stato: <%=model.getDescValue("statoPrit")%></font></td>
		</tr>
	</table>
	<br>
	<table cellspacing='0' cellpadding='2' border='1' width='100%'>
		<tr>
			<td width="6%"  bgcolor="silver"><font size="1" color="#003399"><b>Data invio in sede</b></font></td>
			<td width="6%"  bgcolor="silver"><font size="1" color="#003399"><b>Data spunta</b></font></td>
			<td width="5%"  bgcolor="silver"><font size="1" color="#003399"><b>Barcode</b></font></td>
			<td width="7%" bgcolor="silver"><font size="1" color="#003399"><b>Codice cliente</b></font></td>
			<td width="15%" bgcolor="silver"><font size="1" color="#003399"><b>Cognome e nome cliente</b></font></td>
			<td width="7%" bgcolor="silver"><font size="1" color="#003399"><b>Codice agente</b></font></td>
			<td width="15%" bgcolor="silver"><font size="1" color="#003399"><b>Cognome e nome agente</b></font></td>
			<td width="5%"  bgcolor="silver"><font size="1" color="#003399"><b>Codice prodotto</b></font></td>
			<td width="14%"  bgcolor="silver"><font size="1" color="#003399"><b>Descrizione prodotto</b></font></td>
			<td width="6%"  bgcolor="silver"><font size="1" color="#003399"><b>Codice operazione</b></font></td>
			<td width="14%"  bgcolor="silver"><font size="1" color="#003399"><b>Descrizione operazione</b></font></td>			
		</tr>
<%
		//for (int i=0; i<prova.size(); i++) {
		for (int i=0; i<model.getOutputReportDettaglio().size(); i++) {
			//PritInviatiInSedeOutputModel prova2 = (PritInviatiInSedeOutputModel)prova.get(i);
			DettaglioDocRicevutiDacModel riga = (DettaglioDocRicevutiDacModel)model.getOutputReportDettaglio().get(i);
			
%>
		<tr>
			<td width="7%"><font size="1"><%=riga.getDataInvioInSede()%></font></td>
			<td width="7%"><font size="1"><%=riga.getDataSpunta()%></font></td>
			<td width="7%"><font size="1"><%=riga.getBarCode()%></font></td>
			<td width="7%"><font size="1"><%=riga.getCodCliente()%></font></td>
			<td width="7%"><font size="1"><%=riga.getCognomeCliente()%></font></td>
			<td width="7%"><font size="1"><%=riga.getCodAgente()%></font></td>
			<td width="7%"><font size="1"><%=riga.getNominativoAgente()%></font></td>
			<td width="7%"><font size="1"><%=riga.getCodProdotto()%></font></td>
			<td width="7%"><font size="1"><%=riga.getDescProdotto()%></font></td>
			<td width="7%"><font size="1"><%=riga.getCodOperazione()%></font></td>
			<td width="7%"><font size="1"><%=riga.getDescOperazione()%></font></td>
<%
		}
%>
	</table>
</body>
</html>