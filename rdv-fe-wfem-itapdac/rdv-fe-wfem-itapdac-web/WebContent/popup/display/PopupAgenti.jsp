<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.*"%>
<%@ page import="prgm.ita.p.dac.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);

	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACPOPUP");
	template.setPageName("PopupAgenti");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopupAgentiModel model = (PopupAgentiModel)template.getPageDataModel();
	model.getElencoAgenti().setRowsInPage(15);
%>

<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/popup/display/PopupAgenti.js"></script>
<title>Ricerca Family Banker</title>
</head>

<body style="margin:0;" onload="document.getElementById('codAgente').focus();">
<center>
<table height="96%" width="96%">
  
  <tr><td style="height:5;"></td></tr>
  
  <tr>
    <td align="center">
		<form name="dati" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.popup.display.PopupAgenti.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<table>
		  <tr>
		    <td><%=template.field("codAgente","onchange='fieldChanged();'")%></td>
		    <td><%=template.field("cognome","style='width:200;' onchange='fieldChanged();'")%></td>
		  </tr>
		  <tr>
		    <td align="center" colspan="2">
		        <%=template.action("ricercaAction")%>
		    </td>
		  </tr>
		</table>		
		</form>
    </td>
  </tr>
  
  <tr>
    <td style="line-height:1px;background-color:#1A458F;"></td>
  </tr>

  <tr>
  	<td height="100%">
  		<table height="100%" width="100%"><tr>
		    <td height="100%" id="elencoCont" style="padding:10;">
		    <% if(!model.isPrimaVolta()){ %>
		    	<%
		    		String title = "Elenco Family Banker";
					String col = "cols='#codRete,#codMediolanum,codAgente,nominativo' ";
					String dim = "colswidths='20%,*' ";
		    	%>
		    	<%=template.grid("elencoAgenti",col+dim+
				    							  "width='100%' height='100%' "+
				    							  "title='"+title+"' "+
				    							  "onnewcell='onNewCellAgenti(this);' "+
				    							  "selection='single' "+
				    							  "onclick='selAgente(this);' ")%>
		    <% } %>
		    </td>
    	</tr></table>
    </td>
  </tr>
  
  <tr>
    <td class="text" id="legendaCont" align="center">
	  <% if(model.getElencoAgenti().size() > 0){ %>
	    Seleziona il Family Banker desiderato (click sulla riga) 
	  <% } %>
    </td>
  </tr>
  
</table>
</center>

<%=template.getFooter()%>
</body>

</html>

<%model.setPrimaVolta(false);%>
