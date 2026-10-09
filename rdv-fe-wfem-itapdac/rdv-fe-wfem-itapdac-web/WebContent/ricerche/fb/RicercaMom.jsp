<%@ page import="prgm.ita.p.dac.model.ParamsModel"%>
<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.ricerche.fb.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDACRICERCHE");
	template.setPageName("RicercaDacFb");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelWidth("80");
	template.setJSCombo(false);
	
	RicercaMomModel model = (RicercaMomModel)template.getPageDataModel();	 
%>

<html>

<head>
<%=template.getHeader()%>
<script src="<%=template.getWebApp()%>/resources/javascript/stampa.js"></script>
<script src="<%=template.getWebApp()%>/ricerche/fb/RicercaMom.js"></script>
</head>

<body onload="document.getElementById('parametri_cognomeCliente').focus(); document.getElementById('parametri_cognomeCliente').select();">

<form name="dati" id="dati" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.business.ApriDac.execute">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="idDac" value="">
<%=model.htmlParams("",ParamsModel.showBack)%>
</form>

<table class="text" width="100%" height="100%" >

  <tr>
   <td align="center">
		<table width="80%">
		 <tr>
		  <td align="center">
			<form name="ricerca" method="post" action="call.wfem" style="margin:0px;">
			<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.ricerche.fb.RicercaMom.execute">
			<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
			<input type="hidden" name="doSearch" value="">
			<input type="hidden" name="daoAccessName" value="ricercaStoricoMomFb">
			<fieldset>
			<legend class="text" style="font-weight: bold;">
			    <img src="<%=template.getWebApp()%>/images/section.gif">
				&nbsp;Ricerca Prit&nbsp;
			</legend>
			<table>
			  <tr>
			  	<td>
			  		<table class="text">
			  			<tr>
			  				<td>Tipo ricerca:</td>
			  				<td><input type="radio" name="tipoRicerca" value="MOM" <%="MOM".equals(model.getTipoRicerca().toString())?"checked":""%>>Ricerca Prit (ricerca tutti i dati compresi quelli odierni)</td>
			  			</tr>
			  			<tr>
			  				<td>&nbsp;</td>
			  				<td><input type="radio" name="tipoRicerca" value="storica" <%="MOM".equals(model.getTipoRicerca().toString())?"":"checked"%>>Ricerca storico Prit (ricerca tutti i dati esclusi quelli odierni)</td>
			  			</tr>
			  		</table>
			  	</td>
			  </tr>
			  <tr>
			  	<td align="center">
			  		<table>
			  			<tr>
						     <td valign="top">
						        <table>
						          <tr><td><%=template.field("parametri_stato","style='width:220;'")%></td></tr>
						          <tr><td><%=template.field("parametri_esito","style='width:220;'")%></td></tr>
						        </table>
						     </td>
						     <td valign="top">
						        <table>
						          <tr><td><%=template.field("parametri_dataInizio","labelwidth='60'")%></td></tr>
						          <tr><td><%=template.field("parametri_dataFine","labelwidth='60'")%></td></tr>
						        </table>
						     </td>
						     <td valign="top">
						        <table>
						          <tr><td><%=template.field("parametri_cognomeCliente")%></td></tr>
						          <tr><td><%=template.field("parametri_nomeCliente")%></td></tr>
						          <tr><td><%=template.field("parametri_numeroContratto")%></td></tr>
						        </table>
						     </td>
			  			</tr>
			  		</table>
			  	</td>
			  </tr>
			  <tr>
			     <td style="height: 5"><hr></td>
			  </tr>
			  <tr>
			      <td>
			   		<table width="100%" class="text" style="table-layout:fixed;">
			   			<tr>
				   			<td></td>
				   			<td><%=template.action("ricercaAction")%></td>
				   			<td align="right"><span style="text-decoration:underline;cursor:pointer;" onclick="pulisciParametri();">Pulisci campi di ricerca</span></td>
				   		</tr>
				   	</table>
			   	  </td>
			  </tr>
			</table>
			</fieldset>
			</form>
		  </td>
		 </tr>
		</table>
   </td>
  </tr>

  <tr>
   <td id="toolbarCont">
	     <%if(model.getElencoDac().size() > 0){%>
			<table>
			  <tr>
			     <td>
			       <%=template.action("apriAction","enabled='false' style='width:60'")%>
			     </td>
			     <td>
			       <%=template.action("stampaAction","enabled='false' style='width:60'")%>
			     </td>
			  </tr>
			</table>
	    <%}%>
   </td>
  </tr>
  
  
  <tr>
   <td id="elencoCont" height="100%" align="center">
   		<table width="100%" height="100%" >
			<tr>
			     <td height="100%">   
					<%if(!model.isPrimaAttivazione()){
							String cols = "cols='#flagReplica,idDac,descrStato,descrEsito,dataOraEmissione,dataOraSpunta' ";
							String dim = "colswidths='17%,*,*,15%,15%' ";%>	
			
									<%=template.grid("elencoDac",cols+dim+
																"onclick='selectDac(this);' "+
																"ondblclick='selectDac(this);startRequest();doApriAction();' "+
																"onnewcell='onNewCell(this);' "+
																"selection='single' "+
																"width='100%' height='100%' ")%>
					 <%}%>
			     </td>
			</tr>	
		 </table>
   </td>
  </tr>


</table>

<script>
	try{document.dati.verticalVideoHeight.value = screen.availHeight;}catch(e){}
</script>

<%=template.getFooter()%>
</body>
</html>
<% model.setPrimaAttivazione(false); %>
