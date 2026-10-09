<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACPOPUP");
	template.setPageName("PopupContratti");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopupContrattiModel model = (PopupContrattiModel)template.getPageDataModel();	
%>

<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/popup/display/Contratti.js"></script>
<script src="<%=template.getWebApp()%>/popup/display/PopupContratti.js"></script>
<title>Ricerca contratti</title>
</head>

<body  style="margin:0;" onload="document.getElementById('numeroContratto').focus();">
<center>
<table height="96%" width="96%">

  <tr><td style="height:5;"></td></tr>
  
  <tr>
    <td align="center">
		<form name="dati" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.popup.display.PopupContratti.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="tipoRicerca" value="">
		<table>
		  <tr>
		    <td><%=template.field("numeroContratto","onchange='fieldChanged();'")%></td>
		  </tr>
		  <tr>
		    <td align="center">
		      <table><tr>
		        <% if(Configuration.getInstance().isOnlineEnvironment()){ %>
		        	<% if(model.getUfficio().equals(Costanti.UFFICIO_RETE)){ %>
				        <td><%=template.action("ricercaMieiAction","text='Cerca'")%></td>
		        	<% }else{ %>
			        	<td><%=template.action("ricercaTuttiAction","text='Cerca'")%></td>
		        	<% }  %>
		        <% }else{ %>
			        <td><%=template.action("ricercaMieiAction","text='Cerca'")%></td>
		        <% } %>
		      </tr></table>
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
			    	String legendaChiusi = "";
			    	if(model.esisteContrattoChiuso())
				    	legendaChiusi = "<span style=\"background-color:coral;\">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Contratti chiusi";
				    	
		    		String title = null;
					String col = null;
					String dim = null;
		    		if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_TUTTI_CLIENTI){
		        		title = "Elenco contratti";
			    		col = "cols='#isChiuso,desAgente,tipoProdotto,numeroContratto,numeroPolizza,codProdotto,descrContratto,desCliente,"+
			    					"#agente_codAgente,#agente_codRete,#agente_nominativo,#agente_codMediolanum,"+
			    					"#cliente_codMediolanum,#cliente_cognome,#cliente_nome' ";
			    		dim = "colswidths='*,15%,12%,12%,10%,*,*' ";
		    		}else if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_MIEI_CLIENTI){
		        		title = "Contratti dei miei clienti";
			    		col = "cols='#isChiuso,tipoProdotto,numeroContratto,numeroPolizza,codProdotto,descrContratto,desCliente,"+
			    					"#agente_codAgente,#agente_codRete,#agente_nominativo,#agente_codMediolanum,"+
			    					"#cliente_codMediolanum,#cliente_cognome,#cliente_nome' ";
			    		dim = "colswidths='15%,12%,12%,8%,*,*' ";
		    		}else if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_NON_MIEI_CLIENTI){
		        		title = "Contratti di clienti di altri Family Banker";
			    		col = "cols='#isChiuso,desAgente,tipoProdotto,numeroContratto,numeroPolizza,codProdotto,descrContratto,desCliente,"+
			    					"#agente_codAgente,#agente_codRete,#agente_nominativo,#agente_codMediolanum,"+
			    					"#cliente_codMediolanum,#cliente_cognome,#cliente_nome' ";
			    		dim = "colswidths='*,15%,12%,12%,10%,*,*' ";
		    		}
		    	%>
		    	<%=template.grid("elencoContratti",col+dim+
				    							  "width='100%' height='100%' "+
				    							  "title='<table class=\"text\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"+
			  							  			"<tr>"+
			  							  			   "<td><b>"+title+"</b></td>"+
			  							  			   "<td align=\"right\">"+
			  							  			   		legendaChiusi+
			  							  			   "</td>"+
			  							  			"</tr>"+
			  							  		 "</table>' "+
				    							  "cellsnowrap='false' "+
				    							  "cellheight='50' "+
					   							  "onnewrow='onNewRowContratti(this);' "+
				    							  "onnewcell='onNewCellContratti(this);' "+
				    							  "selection='single' "+
				    							  "onclick='selContratto(this);' ")%>
		    <% } %>
		    </td>
    	</tr></table>
    </td>
  </tr>
  
  <tr>
    <td class="text" id="legendaCont" align="center">
	  <% if(model.getElencoContratti().size() > 0){ %>
	    Seleziona il contratto desiderato (click sulla riga) 
	  <% } %>
    </td>
  </tr>
  
</table>
</center>

<%=template.getFooter()%>
</body>

</html>

<%model.setPrimaVolta(false);%>