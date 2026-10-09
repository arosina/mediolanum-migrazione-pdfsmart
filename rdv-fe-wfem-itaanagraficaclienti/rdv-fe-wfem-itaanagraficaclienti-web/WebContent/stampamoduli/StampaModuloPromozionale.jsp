<%@ page import="prgm.ita.anagraficaclienti.stampamoduli.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("STAMPAMODULI");
	template.setPageName("StampaModuli"); 
	template.setLabelPosition(template.LEFT_LABEL);
	template.setLabelWidth("40%");
	template.setFieldWidth("60%");

	StampaModuloPromozionaleModel model = (StampaModuloPromozionaleModel)template.getPageDataModel();	 
%>

<html>

<%=template.getHeader()%>

<script src="/ItaAnagraficaClienti/stampamoduli/StampaModuloPromozionale.js"></script>

<body>

<form name="moduloForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.stampamoduli.ModuloPromozionale.execute">
<input type="hidden" name="nomeModulo" value="<%=model.getNomeModulo()%>">
<input type="hidden" name="formatoPagina" value="<%=model.getFormatoPagina()%>">
</form>

<form name="ricercaCodicePromozionaleForm" method="post" action="call.wfem" style="margin: 0;">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.stampamoduli.StampaModuloPromozionale.execute">
<center>
<table height="100%" width="90%">
	<tr>
		<td>
		
		  <fieldset class="fieldsGroup">
		    <legend class="text" style="font-weight: bold;">
		      Stampa modulo <%=template.getProperty("nomeModulo."+model.getNomeModulo().toString())%>
		      <img title="Pulisci campi" src="<%=template.getWebApp()%>/images/clear.gif"
		      onclick="pulisciCodicePromozionale();" style="cursor:pointer;">
		    </legend>
		
		    <center>
		    <table width="100%">
		      <tr>
		        <td><%=template.field("codicePromo")%></td>
		      </tr>
		      <tr>
		        <td>
			      	<% if(model.getCodicePromo().isNull()){ %>
			        <div style="position:relative;height: 100%;width: 100%;" id="help">
					<div style="position:absolute;top:0px;left:150px;">
					  <table class="text" cellpadding="0" cellspacing="0" width="160">
						    <tr>
							    <td rowspan="2" style="padding:3pt;background-color:yellow;border: solid 1px gray;">
							    	Inserire il Codice Promozionale e cliccare sul pulsante Stampa
							    </td>
							    <td style="font-size:12pt;" valign="top">&#9658</td>
							</tr>
						    <tr>
							    <td>&nbsp;</td>
							</tr>
					  </table>
					</div>
			        </div>
					<% } %>
		        </td>
		      </tr>
		      <tr>
		        <td><%=template.action("stampaAction")%></td>
		      </tr>
		    </table>
		    </center>
		  </fieldset>
		
		</td>
	</tr>
<% if (!model.getFlagCodicePromoValido().booleanValue() && !model.getCodicePromo().isNull()) { %>
	<tr><td height="5px">&nbsp;</td></tr>
	<tr>
		<td class="text" style="font-weight: bold;" align="center">Attenzione! Codice Promozionale non valido</td>
	</tr>	
<%} %>
	<tr>
		<td height="100%" style="padding-top: 10;">
			<table width="100%" height="100%" cellpadding="0" cellspacing="0">
			  <tr>
				   <td class="text" style="height: 5px;">
				   </td>
			  </tr>
			  <tr>
			  	  <td align="center" id="moduloPrintObject" height="100%">
				  </td>
			  </tr>
			</table>
		</td>
	</tr>
</table>
</center>
</form>
<% if (model.getFlagCodicePromoValido().booleanValue()) { %>	
<script>
	includePdfObject("moduloPrintObject",document.moduloForm);
</script>
<%} %>

<%=template.getFooter()%>
</body>
</html>