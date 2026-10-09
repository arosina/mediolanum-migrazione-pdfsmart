<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="com.atosorigin.wfem.coddesc.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	ClienteModel model = (ClienteModel)template.getPageDataModel();
	CodDescDataList elenco = model.getInfoPersonali().getCodDescDataList("codStatoCivile");
%>
<html>
<title>Elenco stati civili</title>

<body>
<table style="font-family: Arial;font-size: 8pt;color:#1A458F;" width="100%">
  <tr><td><b>Stato civile.</b>&nbsp;&nbsp;&nbsp;Utilizza la funzione di ricerca del browser (Ctrl + F) per trovare la voce che ti interessa.</td></tr>
  <tr><td style="height:10px;"></td></tr>
</table>
<table style="font-family: Arial;font-size: 8pt;color: #1A458F;font-weight: normal;" border="1" width="100%">
<% for(int i=0;i<elenco.getCodDescCount();i++){ 
	CodDescData e = elenco.getCodDesc(i);
	if(!e.isValid())
		continue;
   %>
	<tr><td><%=e.getCod()%></td><td><%=e.getDescr()%></td></tr>
<% } %>
</table>
<body>

</html>