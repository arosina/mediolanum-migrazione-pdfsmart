<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.DOWN_LABEL);
	template.setJSCombo(false);
	ClienteModel model = (ClienteModel)template.getPageDataModel();
%>

<html>
<head>
<link rel="stylesheet" type="text/css" href="call.wfem?wfemCmd=getCss">
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0" scroll="no">
<table width="100%" height="100%" class="text" style="background-color:azure;" border="1">
<tr>
  <td>
    <table class="text">
      <tr>
        <td align="center" valign="middle" style="padding: 10;">
			Attenzione! Il documento del cliente selezionato<br>
			<%=model.getClienteTitolare().getCognome()%> <%=model.getClienteTitolare().getNome()%><br>
			risulta scaduto.<br>
			E' necessario effettuare la variazione anagrafica prima di censire la ditta/lib. prof.
	    </td>
	  </tr>
	  <tr>
	    <td align="center">
	      <span style="text-decoration:underline;cursor:pointer;" onclick="window.close();">chiudi</span>
	    </td>
	  </tr>
	</table>
  </td>
</tr>
</table>
</body>
</html>
