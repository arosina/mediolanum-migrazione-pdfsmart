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
	//ListType prova = model.getOutputReportDettaglio();

	
	DateType data = new DateType(model.getDataDal().dateValue());
	DateType dataAl = new DateType(model.getDataDal().dateValue());
	dataAl.addDays(6);
	
%>
<html>
<body>
	<table cellspacing='0' cellpadding='2' border='0' >
		<tr>
		<td align='center'><font size="5"><b>Report di sintesi</b></font></td>
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
			<td width="14%" bgcolor="silver"><font size="1" color="#003399"><b>Prodotto</b></font></td>
			<td width="23%" bgcolor="silver"><font size="1" color="#003399"><b>Operazione</b></font></td>
			<td width="9%"  bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
			<%data.addDays(1); %>
			<td width="9%" bgcolor="silver"><font size="1" color="#003399"><b><%= data.toString() %></b></font></td>
		</tr>
<%
		for (int i=0; i<model.getOutputReportSintesi().size(); i++) {
			PritInviatiInSedeOutputModel riga = (PritInviatiInSedeOutputModel)model.getOutputReportSintesi().get(i);
%>
		<tr>
			<td width="14%"><font size="1"><%=riga.getDescProdotto()%></font></td>
			<td width="23%"><font size="1"><%=riga.getDescOperazione()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg1().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg2().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg3().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg4().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg5().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg5().toString()%></font></td>
			<td width="9%"><font size="1"><%=riga.getGg7().toString()%></font></td>
		</tr>
<%
		}
%>
	</table>
</body>
</html>