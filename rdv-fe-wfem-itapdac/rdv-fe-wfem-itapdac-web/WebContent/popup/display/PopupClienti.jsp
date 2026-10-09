<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.*"%>
<%@ page import="prgm.ita.p.dac.popup.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACPOPUP");
	template.setPageName("PopupClienti");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");
	template.setJSCombo(false);
	
	PopupClientiModel model = (PopupClientiModel)template.getPageDataModel();
	ClienteModel clienteSelezionato = model.getClienteSelezionato();
	model.getElencoClienti().setRowsInPage(15);

	String title = null;
	String col = null;
	String dim = null;
%>

<html>

<head>
<%=template.getHeader()%>

<script src="<%=template.getWebApp()%>/popup/display/Contratti.js"></script>
<script src="<%=template.getWebApp()%>/popup/display/PopupClienti.js"></script>
<title>Ricerca clienti</title>
</head>

<body style="margin:0;" onload="document.getElementById('cognome').focus();">
<center>
<table height="96%" width="96%">

  <tr><td style="height:5;"></td></tr>

  <tr>
    <td align="center">
		<form name="dati" method="post" action="call.wfem" style="margin:0;">
		<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.popup.display.PopupClienti.execute">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="clienteSelezionato_codMediolanum" value="">
		<input type="hidden" name="tipoRicerca" value="">
		<table>
		  <tr>
		    <td align="center">
		      <table><tr>
			    <td><%=template.field("cognome","style='width:140;' onchange='fieldChanged();'")%></td>
			    <td><%=template.field("nome","style='width:140;' onchange='fieldChanged();'")%></td>
			    <td><%=template.field("codMediolanum","size='10' maxlength='11' onchange='fieldChanged();'")%></td>
			    <% if(Configuration.getInstance().isOnlineEnvironment() && model.getAgenteCollegato().getCanMakeForOtherFb().booleanValue()){  %>
			    	<td><%=template.field("codAgente","size='8' maxlength='10' onchange='fieldChanged();'")%></td>
			    <% }else{ %>
					<td><input type="hidden" name="codAgente" value=""></td>
			    <% } %>
		      </tr></table>
		    </td>
		  </tr>
		  <tr>
		    <td align="center">
		      <table><tr>
		        <% if(Configuration.getInstance().isOnlineEnvironment()){ %>
		        	<% if(model.getUfficio().equals(Costanti.UFFICIO_RETE)){ %>
				        <% if(model.getAgenteCollegato().getCanMakeForOtherFb().booleanValue()){ %>
					        <td><%=template.action("ricercaMieiAction")%></td>
			        		<td><%=template.action("ricercaNonMieiAction")%></td>
			        	<% }else{ %>
					        <td><%=template.action("ricercaMieiAction","text='Cerca'")%></td>
			        	<% } %>
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
			<table height="100%" width="100%">
			<!--  ELENCO CLIENTI -->
			<tr>
				<td height="100%" id="elencoCont" style="padding:10;">
				<% if(!model.isPrimaVolta()){ %>
			    	<%
			    		if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_TUTTI_CLIENTI){
			       			title = "Elenco clienti";
			    			col = "cols='visContr,codMediolanum,cognome,nome,dataNascita,codFiscale,partitaIva,agente_codAgente,#agente_codRete,"+
										"#agente_nominativo,#agente_codMediolanum' ";
							dim = "colswidths='3%,*,*,*,*,*,*,*' ";
			    		}else if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_MIEI_CLIENTI){
			    			title = "Miei clienti";
				    		col = "cols='visContr,codMediolanum,cognome,nome,dataNascita,codFiscale,codCluster' ";
				    		dim = "colswidths='3%,*,*,*,*,*,*' ";
			    		}else if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_NON_MIEI_CLIENTI){
			        		title = "Clienti di altri Family Banker";
			    			col = "cols='codMediolanum,cognome,nome,dataNascita,codFiscale,partitaIva,#agente_codAgente,#agente_codRete,"+
										"#agente_nominativo,#agente_codMediolanum' ";
							dim = "colswidths='*,*,*,*,*,*' ";
			    		}
			    	%>
			    	<%=template.grid("elencoClienti",col+dim+
					    							  "width='100%' height='100%' "+
					    							  "title='"+title+"' "+
					    							  "cellsnowrap='false' "+
					    							  "cellheight='30' "+
					    							  "selection='single' "+
					    							  "onnewcell='onNewCellClienti(this);' "+
					    							  "onclick='selCliente(this);' ")%>
				<% } %>
			    </td>
			</tr>
			<!--  HELP -->
			<tr>
				<td class="text" id="legendaCont" align="center">
				  <% if(model.getElencoClienti().size() > 0 && model.getTipoRicerca().intValue() != PopupClientiModel.RICERCA_NON_MIEI_CLIENTI){ %>
				    	Puoi selezionare solamente il cliente (click sulla riga) oppure visualizzare i suoi contratti 
				    	(click sull'immagine <img style="vertical-align:middle;" src="<%=template.getWebApp()%>/images/contratti.png">)
				  <% } %>
			    </td>
			</tr>
			<!-- ELENCO CONTRATTI -->
			<tr>
				<td height="180px" id="elencoContrattiCont" style="padding:10;">
					<form name="contrattiClienteForm" method="post" action="call.wfem">
					<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.popup.display.PopupClienti.execute">
					<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
					<input type="hidden" name="clienteSelezionato_codMediolanum" value="">
			    
				    <% if(!model.getClienteSelezionato().getCodMediolanum().isNull()){ %>
					    <%
					    	String legendaChiusi = "";
					    	if(model.esisteContrattoChiuso())
						    	legendaChiusi = "<span style=\"background-color:coral;\">&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;Contratti chiusi";
						    	
				    		title = "Contratti di "+clienteSelezionato.getNominativo();
				    		if(model.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_MIEI_CLIENTI){
					    		col = "cols='#isChiuso,tipoProdotto,numeroContratto,numeroPolizza,codProdotto,descrContratto,#desCliente,"+
					    					"#agente_codAgente,#agente_codRete,#agente_nominativo,#agente_codMediolanum,"+
					    					"#cliente_codMediolanum,#cliente_cognome,#cliente_nome' ";
					    		dim = "colswidths='15%,12%,12%,10%,*' ";
				    		}else{
					    		col = "cols='#isChiuso,#desAgente,tipoProdotto,numeroContratto,numeroPolizza,codProdotto,descrContratto,#desCliente,"+
					    					"#agente_codAgente,#agente_codRete,#agente_nominativo,#agente_codMediolanum,"+
					    					"#cliente_codMediolanum,#cliente_cognome,#cliente_nome' ";
					    		dim = "colswidths='15%,12%,12%,10%,*' ";
				    		}
							
					    %>
						<%=template.grid("elencoContrattiCliente",col+dim+
						   							  "width='100%' height='180px' "+
						   							  "norowsmsg='Per il cliente "+clienteSelezionato.getNominativo()+" non risultano contratti in portafoglio' "+
					    							  "title='<table class=\"text\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"+
					    							  			"<tr>"+
					    							  			   "<td><b>"+title.replaceAll("\\'","&rsquo;")+"</b></td>"+
					    							  			   "<td align=\"right\">"+
					    							  			   		legendaChiusi+
					    							  			   "</td>"+
					    							  			"</tr>"+
					    							  		 "</table>' "+
						   							  "onnewrow='onNewRowContratti(this);' "+
						   							  "onnewcell='onNewCellContratti(this);' "+
						   							  "selection='single' "+
						   							  "onclick='selContratto(this);' ")%>
					<% } %>
					</form>
				</td>
			</tr>
			<!--  HELP -->
			<tr>
			    <td class="text" id="legendaContrCont" align="center">
				  <% if(model.getElencoContrattiCliente().size() > 0){ %>
				    Seleziona il contratto desiderato (click sulla riga) 
				  <% } %>
			    </td>
			</tr>
			</table>
		</td>
	</tr>
  
</table>
</center>

<%=template.getFooter()%>
</body>
</html>

<%model.setPrimaVolta(false);%>