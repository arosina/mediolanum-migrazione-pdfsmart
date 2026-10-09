<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="moke.menu2.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<% 
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setJSCombo(false);
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setLabelAlign("left");
	template.setLabelWidth("100");
	template.setUpperCase(false);
	template.setModality(Template.INSERT_MODALITY);
	
	ElencoComandiModel model = (ElencoComandiModel)template.getPageDataModel();
%>

<%@page import="com.atosorigin.wfem.util.Tools"%>
<html>

<head>
<%=template.getHeader()%>
<script>
function changeProject(){
	startRequest();
	if(document.dati.progetto.value == '')
		document.dati.loadConf.value = 'false';
	document.dati.wfemCmd.value = 'moke.menu2.display.ElencoComandi.execute';
	document.dati.submit();
}

function doNuovoComando(){
	if(document.getElementById("newComandoCont").style.display == 'inline')
		return;

	document.getElementById("opacity").style.display='inline';
	document.getElementById("newComandoContShadow").style.display = 'inline';
	document.getElementById("newComandoCont").style.display = 'inline';
}

function doAnnullaNuovoComando(){
	startRequest();
	document.dati.wfemCmd.value = 'moke.menu2.business.AnnullaNuovoComando.execute';
	document.dati.submit();
}

function doConfermaNuovoComando(){
	document.getElementById("newComandoContShadow").style.display = 'none';
	document.getElementById("newComandoCont").style.display = 'none';
	startRequest();
	document.dati.wfemCmd.value = 'moke.menu2.business.InserisciNuovoComando.execute';
	document.dati.submit();
}

function doCancellaComando(id){
	if(!window.confirm("Confermi la cancellazione del comando ?"))
		return;
	startRequest();
	document.dati.comando_id.value = id;
	document.dati.wfemCmd.value = 'moke.menu2.business.CancellaComando.execute';
	document.dati.submit();
}

function doModificaComando(id){
	document.getElementById("newComandoTitle").innerHTML = "Modifica comando";
	document.getElementById("comando_id").value=id;
	enableField("comando_progetto",false);
	document.getElementById("comando_attore").value=document.getElementById("attore"+id).getAttribute("val");
	document.getElementById("comando_descrizione").value=document.getElementById("descrizione"+id).getAttribute("val");
	document.getElementById("comando_comando").value=document.getElementById("comando"+id).getAttribute("val");

	document.getElementById("opacity").style.display='inline';
	document.getElementById("newComandoContShadow").style.display = 'inline';
	document.getElementById("newComandoCont").style.display = 'inline';
}

function go(cmd){
	startRequest();
	document.startForm.commandName.value = cmd;
	document.startForm.isMultiTaskMode.value = parent.getIsMultiTaskField();
	document.startForm.submit();
}
</script>
</head>

<body>

<form name="startForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="moke.menu2.business.AttivaFunzione.executeProcess">
<input type="hidden" name="commandType" value="COMMAND">
<input type="hidden" name="commandName" value="">
<input type="hidden" name="isMultiTaskMode" value="false">
</form>

<form name="dati" method="post" action="call.wfem" style="margin: 0;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="comando_id" id="comando_id" value="">
<input type="hidden" name="remoteAddr" id="remoteAddr" value="<%=request.getRemoteAddr()%>">
<input type="hidden" name="loadConf" id="loadConf" value="true">

<table width="100%" height="100%">
   	<tr>
   		<td align="center" class="toolbar">
   		  <table style="table-layout:fixed;">
   		  	<col width="15%"><col width="*"><col width="15%">
   		    <tr>
	   		    <td>&nbsp;</td>
	   		    <td align="center">
			   		<%=template.field("progetto","labelcode='<b>Progetto</b>' labelalign='right' onchange=changeProject();")%>
	   		    </td>
	   		    <td align="right">
		   			<table><tr><td style="padding-right:20;"><%=template.action("nuovoComando","text='Nuovo comando'")%></td></tr></table>
	   		    </td>
   		    </tr>
   		  </table>
   		</td>
   	</tr>
   	<tr>
   	  <td style="height:15;"></td>
   	</tr>
   	<tr>
	   	<td align="center">
	   	  <table width="100%" class="text" style="table-layout:fixed;">
	   	  <col width="4%">
	   	  <col width="16%">
	   	  <col width="40%">
	   	  <col width="40%">
	   	  <col width="16px">
	   	  <thead>
		   	  <tr>
		   	      <td class="gridHeader">&nbsp;</td>
		   	      <td class="gridHeader">&nbsp;Attore</td>
		   	      <td class="gridHeader">&nbsp;Descrizione</td>
		   	      <td class="gridHeader">&nbsp;Comando</td>
		   	      <td></td>
		   	  </tr>
	   	  </thead>
	   	  <tbody>
	   	  </tbody>
	   	  </table>
		</td>
	</tr>
   	<tr>
	   	<td align="center" height="100%" valign="top">
	   	<div style="overflow-y:scroll;overflow-x:auto;height: 100%;">
	   	  <table width="100%" class="text" style="table-layout:fixed;">
	   	  <col width="4%">
	   	  <col width="16%">
	   	  <col width="40%">
	   	  <col width="40%">
	   	  <thead>
	   	  </thead>
	   	  <tbody>
		   	  <%
		   	  	for(int i=0;i<model.getComandi().size();i++){
		   	  		ComandoModel c = (ComandoModel)model.getComandi().get(i);
		   	  		String cmd = Tools.stringToHTMLString(c.getComando().toString());
		   	  %>
		   	  <tr <%=(i%2!=0?"style='background-color:azure;'":"")%>>
		   	      <td valign="middle" align="center" nowrap="nowrap">
		   	      	<img title="Modifica comando" src="<%=template.getWebApp()%>/menu2/images/edit.png" style="cursor:pointer;" onclick="doModificaComando('<%=c.getId()%>');">
		   	      	<img title="Cancella comando" src="<%=template.getWebApp()%>/menu2/images/delete.png" style="cursor:pointer;" onclick="doCancellaComando('<%=c.getId()%>');">
		   	      </td>
		   	      <td valign="top">
		   	      	<span id="attore<%=c.getId()%>" val="<%=c.getAttore().toString()%>" style="text-decoration:underline;cursor:pointer;" onclick="go('<%=cmd%>');"><%=Tools.stringToHTMLString(c.getAttore().toString())%></span>
		   	      </td>
		   	      <td valign="top">
		   	      	<span id="descrizione<%=c.getId()%>" val="<%=c.getDescrizione().toString()%>" style="text-decoration:underline;cursor:pointer;" onclick="go('<%=cmd%>');"><%=Tools.stringToHTMLString(c.getDescrizione().toString())%></span>
		   	      </td>
		   	      <td valign="top">
		   	      	<span id="comando<%=c.getId()%>" val="<%=c.getComando().toString()%>" style="text-decoration:underline;cursor:pointer;" onclick="go('<%=cmd%>');"><%=Tools.stringToHTMLString(c.getComando().toString())%></span>
		   	      </td>
		   	  </tr>
		   	  <% } %>
	   	  </tbody>
	   	  </table>
	   	</div>
		</td>
	</tr>
   	<tr style="height:10;">
   		<td align="center" class="gridHeader">
   		
   		</td>
   	</tr>
</table>

<div id="newComandoCont" style="z-index:500;position:absolute;left:9%;top:25%;width:800;height:300;<%=(model.getComando().hasCommandErrors()?"":"display:none;")%>">
<table width="100%" height="100%" style="background-color: #ECF0F2; border: solid 1px black;"><tr>
	<td align="center" valign="middle">
		<table class="text">
		   <tr style="height:30;"><td colspan="2"></td></tr>
		   <tr><td colspan="2" align="center" style="font-weight:bold;" id="newComandoTitle">Nuovo comando</td></tr>
		   <tr style="height:30;"><td colspan="2"></td></tr>
		   <tr style="height:30;">
		   		<td colspan="2" align="center">
		   		  <table>
		   		   <tr><td><%=template.field("comando_progetto","labelcode='Progetto' size='120'")%></td></tr>
		   		   <tr><td><%=template.field("comando_attore","labelcode='Attore' size='120'")%></td></tr>
		   		   <tr><td><%=template.field("comando_descrizione","labelcode='Descrizione' size='120'")%></td></tr>
		   		   <tr><td><%=template.field("comando_comando","labelcode='Comando' size='120'")%></td></tr>
		   		  </table>
		   		</td>
		   	</tr>
		   <tr style="height:30;"><td colspan="2"></td></tr>
		   <tr>
		       <td>	         
		       	   <%=template.action("confermaNuovoComando","text='Conferma'")%>
		       </td>
		       <td>	         
		       	   <%=template.action("annullaNuovoComando","text='Annulla'")%>
		       </td>
		   </tr>
		   <tr style="height:30;"><td colspan="2"></td></tr>
		</table>
	</td>
</tr></table>
</div>
<div id="newComandoContShadow" 
	 style="filter: alpha(opacity=60);z-index:400;background-color:silver;position:absolute;left:10%;top:28%;width:800;height:300;<%=(model.getComando().hasCommandErrors()?"":"display:none;")%>">
</div>
<div id="opacity" class="divwaitOpacityCoverStyle" 
	 style="z-index:10;<%=(model.getComando().hasCommandErrors()?"":"display:none;")%>">
</div>

</form>

<%=template.getFooter()%>

</body>
</html>