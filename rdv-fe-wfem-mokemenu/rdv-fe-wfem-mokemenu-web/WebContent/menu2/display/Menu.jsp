<%@ page import="java.util.*" %>
<%@ page import="com.atosorigin.wfem.types.*"%>
<%@ page import="moke.menu2.model.*"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>

<html>
<head>
<%@ include file="../../url.jsi"%>
<%
	int clientAreaHeight = 640;
	int clientAreaWidth  = 965;

	template.setWlt(true);
	template.setApplCode("MOKEMENU2");
	template.setModality(Template.INSERT_MODALITY);

	MenuModel model = (MenuModel)template.getPageDataModel();
    String userCode = model.getUserSessionContext().getUserCode();
	String currentRefAge = model.getUserSessionContext().getCurrentLinkedUserCode();

	String country  = (String)session.getAttribute("country"); if(country == null)country = new String("");
	String channel  = (String)session.getAttribute("channel"); if(channel == null)channel = new String("");
	String language = (String)session.getAttribute("language");if(language == null)language = new String("");
	String starturl = (String)session.getAttribute("starturl");if(starturl == null)starturl = new String("");
	
	String totUrl = "&country="+country+"&channel="+channel+"&language="+language+"&starturl="+starturl;
	String imgPath = "/"+webApp+"/login/images/"+country.toUpperCase() + "/" + channel.toUpperCase();
	
    Calendar cal = Calendar.getInstance();
    int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    String sDayOfWeek = template.getProperty("label.dayOfWeek."+dayOfWeek);
    int day = cal.get(Calendar.DATE);
    int month = cal.get(Calendar.MONTH); month++;
    String sMonth = template.getProperty("label.month."+month);
    int year = cal.get(Calendar.YEAR);
    String data = sDayOfWeek+" "+day+" "+sMonth+" "+year;
    
    int fncDivHeight = 400;
    int fncDivWidth = 250;
%>

<%=template.getHeader()%>

<title><%=titolo%></title>
<link rel="stylesheet" type="text/css" href="/<%=webApp%>/menu2/style/Menu.css">
<script language='JavaScript' src="/<%=webApp%>/menu2/javascript/jquery-1.4.2.min.js"></script>
<script language='JavaScript' src="/<%=webApp%>/menu2/javascript/Menu.js"></script>
<script language='JavaScript' src="/<%=webApp%>/menu2/javascript/Grp.js"></script>
<script language='JavaScript' src="/<%=webApp%>/menu2/javascript/App.js"></script>
<script language='JavaScript' src="/<%=webApp%>/menu2/javascript/Fnc.js"></script>
<script>
var fncDivHeight = '<%=fncDivHeight%>';
var fncDivWidth = '<%=fncDivWidth%>';
var grp;
var app;
var fnc;
var menu = new Menu();
<%
	ListType groups = model.getGroups();
	for(int g=0;g<groups.size();g++){
		Group group = (Group)groups.get(g); 
		ListType applications = group.getApplications(); %>
		grp = new Grp("<%=group.getCode()%>","<%=group.getTitle()%>");
		menu.addGroup(grp);
		<% for(int a=0;a<applications.size();a++){
			Application appl = (Application)applications.get(a); 
			ListType funzs = appl.getFunctions(); %>
			app = new App("<%=appl.getCode()%>","<%=appl.getTitle()%>");
			grp.addApplication(app);
			<% String parentCode = "";
			   for(int f=0;f<funzs.size();f++){
				   Function funz = (Function)funzs.get(f); 
				   if(funz.getMenuLayer().equals("0"))
				   	   parentCode = funz.getCode().toString();%>
				       fnc = new Fnc("<%=funz.getCode()%>","<%=funz.getTitle()%>",
						  	         "<%=funz.getMenuLayer()%>","<%=funz.getCommandName()%>",
							         "<%=funz.getCommandType()%>","<%=funz.getBrowserInstance()%>",
							         "<%=parentCode%>",<%=funz.hasChilds()%>);
				       app.addFunction(fnc);
			 <% } %>
		 <% } %>
<%	} %>
</script>

<script>
function openPortalPage(portalPageName){
	startRequest();
	document.openPortalPageForm.portalPageName.value = portalPageName;
	document.openPortalPageForm.portalPageParams.value = "";
	document.openPortalPageForm.submit();
}
function openPortalPage(portalPageName,portalPageParams){
	startRequest();
	document.openPortalPageForm.portalPageName.value = portalPageName;
	document.openPortalPageForm.portalPageParams.value = portalPageParams;
	document.openPortalPageForm.submit();
}
function logout(){
	var loginUrl = "/<%=webApp%>/call.wfem?wfemCmd=showLogin<%=totUrl%>";
	top.window.document.location.href = loginUrl;
}
function attivaFunzione(fncCode){
	menu.attivaFunzione(fncCode);
}
function goHome(){
	resizeContainer();
	document.getElementById("clientarea").src="call.wfem?wfemCmd=moke.menu2.display.ElencoComandi.execute&loadConf=true&remoteAddr=<%=request.getRemoteAddr()%>&BrowserInstance=0";
}
function __log(msg){
    try{document.getElementById("log").value += msg+"\n";}catch(e){}
}
function ingrandisci(){
	var stato = document.getElementById("maxMinCont").getAttribute("stato");
	if(stato == 'min'){
		document.getElementById("maxMinCont").innerHTML = 'Ripristina';
		document.getElementById("maxMinCont").setAttribute('stato','max');
		document.getElementById("clientarea").frameborder='0';
		document.getElementById("leftCol").style.display='none';
		document.getElementById("rightCol").style.display='none';
		document.getElementById("clientarea").style.width='100%';
		document.getElementById("clientarea").style.height='<%=clientAreaHeight%>px';
	}else{
		document.getElementById("maxMinCont").innerHTML = 'Ingrandisci';
		document.getElementById("maxMinCont").setAttribute('stato','min');
		document.getElementById("clientarea").frameborder='1';
		document.getElementById("leftCol").style.display='';
		document.getElementById("rightCol").style.display='';
		document.getElementById("centerCol").style.width='<%=clientAreaWidth%>px';
		document.getElementById("clientarea").style.height='<%=clientAreaHeight%>px';
	}
}

function mostraValidazioni(){
	if(document.getElementById('momcontainer').style.display === ''){
		document.getElementById('momcontainer').style.display='none';
		document.getElementById('mostraValidazioni').innerHTML='Mostra validazioni';
	}else{
		document.getElementById('momcontainer').style.display='';
		document.getElementById('mostraValidazioni').innerHTML='Nascondi';
	}
}

function resizeContainer(h){
	try{
		if(!h)
			h = <%=clientAreaHeight%>;
		document.getElementById("clientarea").style.height=h+50;
	}catch(e){}
}
function onLoadContent(obj){
	try{
		resizeContainer(obj.height);
	}catch(e){}
}
function setProcessTitle(){
}

//Intagrazione MOM
function callMomEvent(){
	var data = {  
		"read-only"					:document.getElementById("MOM-read-only").value,
		"valida"					:document.getElementById("MOM-valida").value,
		"salva"     				:document.getElementById("MOM-salva").value,
		"azione-mom"				:document.getElementById("MOM-azione-mom").value,
		"aggiorna-dispositiva"		:document.getElementById("MOM-aggiorna-dispositiva").value,
		"goto-pdf"					:document.getElementById("MOM-goto-pdf").value,
		"id-pratica"				:document.getElementById("MOM-id-pratica").value,
		"confronta-doppia-spunta"	:document.getElementById("MOM-confronta-doppia-spunta").value,
		"parametri":[  
           {	"parametro":"nota", 			"valore":null},
           {	"parametro":"versione-modulo",	"valore":"versione modulo MOM catalogo" }
       ]
    };
	
	var chdata = {
		    "msgXchanger": true,
	        "channelMsgName": "PdfWebForms.SalvaEValidaDocumentoMomEvent",
	        "channelMsgData":  data
	};
	var schdata = JSON.stringify(chdata);
	try{
		document.clientarea.postMessage(schdata,"*");
	}catch(e){alert("callMomEvent-> "+e.message);}
}

function callRicercaClienteMomCallbackEvent(){
	var chdata = {
		    "msgXchanger": true,
	        "channelMsgName": "PdfWebForms.RicercaClienteMomCallbackEvent",
	        "channelMsgData":  {
	        	"indiceCliente" : document.getElementById("idxNdgMom").value,
	        	"ndgCliente" : document.getElementById("ndgMom").value
	        }
	};
	var schdata = JSON.stringify(chdata);
	try{
		document.clientarea.postMessage(schdata,"*");
	}catch(e){alert("callRicercaClienteMomCallbackEvent-> "+e.message);}
}

//Integrazione Validazione
function callPdfValidationEvent(){
	var data = {  
		"actionName"			:document.getElementById("VALIDATION-actionName").value,
		"doValidation"			:document.getElementById("VALIDATION-doValidation").value,
		"doAdeguatezza"			:document.getElementById("VALIDATION-doAdeguatezza").value
    };
	
	var chdata = {
		    "msgXchanger": true,
	        "channelMsgName": "PdfWebForms.CallPdfValidationEvent",
	        "channelMsgData":  data
	};
	var schdata = JSON.stringify(chdata);
	try{
		document.clientarea.postMessage(schdata,"*");
	}catch(e){alert("callPdfValidationEvent-> "+e.message);}
}

// Ricezione eventi json
function receivePdfEvent(event){
	try{
		var chdata = JSON.parse(event.data);
		var evdata = chdata.channelMsgData;
		if(chdata.channelMsgName == "PdfWebForms.SalvaEValidaDocumentoMomCallbackEvent"){
			var msg = "";
			var err = "";
			for(var i=0;i<evdata.errori.length;i++){
				err += evdata.errori[i]+"\n\n";
			}
			var war = "";
			for(var i=0;i<evdata.warning.length;i++){
				war += evdata.warning[i]+"\n\n";
			}
			if(err.length > 0 || war.length > 0){
				if(err.length > 0)
					msg += "ERRORI\n"+err+"\n\n";
				if(war.length > 0)
					msg += "WARNING\n"+war+"\n\n";
			}
			alert(msg);
		}else if(chdata.channelMsgName == "PdfWebForms.CallPdfValidationEventCallback"){
			var msg = "Event callback\n";
			msg += "Input\n"+" actionName: "+evdata.actionName+"\n doValidation: "+evdata.doValidation+"\n doAdeguatezza: "+evdata.doAdeguatezza+"\n";
			msg += "Result\n"+" hasErrors: "+evdata.hasErrors+"\n hasWarnings: "+evdata.hasWarnings+"\n isAdeguato: "+evdata.isAdeguato+"\n idEsitoMifid: "+evdata.idEsitoMifid+"\n";
			/*
			var err = "";
			for(var i=0;i<evdata.errori.length;i++){
				err += evdata.errori[i]+"\n\n";
			}
			var war = "";
			for(var i=0;i<evdata.warning.length;i++){
				war += evdata.warning[i]+"\n\n";
			}
			if(err.length > 0 || war.length > 0){
				if(err.length > 0)
					msg += "ERRORI\n"+err+"\n\n";
				if(war.length > 0)
					msg += "WARNING\n"+war+"\n\n";
			}
			*/
			alert(msg);
		}else if(chdata.channelMsgName == "PdfWebForms.RicercaClienteMomEvent"){
			alert("RicercaClienteMomEvent received: "+evdata.indiceCliente);
		}else if(chdata.channelMsgName == "PdfWebForms.PopolamentoAgenteMomEvent"){
			alert("PopolamentoAgenteMomEvent received: ["+evdata.codiceAgente+"]");
		}else if(chdata.channelMsgName == "PdfWebForms.PopolamentoClienteMomEvent"){
			alert("PopolamentoClienteMomEvent received: "+evdata.ndgCliente+" at "+evdata.indiceCliente);
		}
	}catch(e){}
}

if(window.addEventListener){
	addEventListener("message", receivePdfEvent, false);
}else{
	attachEvent("onmessage", receivePdfEvent);	
}

function getIsMultiTaskField(){
	return document.getElementById("isMultiTaskField").value;
}
</script>
<style>
body{
  margin: 0;
  padding: 0;
  border: 0;
}
</style>
</head>

<body>

<form name="openPortalPageForm" method="post" action="call.wfem" style="display:none;" target="clientarea">
<input type="hidden" name="wfemCmd" value="moke.menu2.display.OpenPortalPage.execute">
<input type="hidden" name="portalPageName" value="">
<input type="hidden" name="portalPageParams" value="">
<input type="hidden" name="browserInstance" value="">
</form>

<div style="position: absolute; left: 0; top:0; z-index:10;display:none;">
<textarea id="log" rows=30></textarea>
</div>

<table width=100%" height="100%" cellspacing="0" cellpadding="0">
 <tr>
  <td> 
	<table width="100%" cellpadding="0" cellspacing="0"> <!-- Loghi, utente e path -->
	  <tr>
	    <td colspan="3" align="right">
	      <table width="100%" cellspacing="0" cellpadding="0" style="table-layout: fixed;">
	        <tr>
                <td align="left" style="font-family: verdana, san-serif; font-size: 11px; color: #3C6C9C;">
                 <label style=" font-weight: bold;"><%=country%>-<%=channel%>-<%=language%>-<%=userCode%></label>
                 - Cod Rif.:
                 <label style="font-weight: bold;" name="agenteRif" id="agenteRif"><%=currentRefAge%></label>
                </td>
                <td align="center">
                 <table><tr>
	                <td class="text" align="right"><span id="maxMinCont" stato="min" style="text-decoration:underline;cursor:pointer;font-weight:bold;" onclick="ingrandisci();">Ingrandisci</span></td>
	                <td>&nbsp;&nbsp;&nbsp;</td>
	                <td class="text" align="right"><span style="text-decoration:underline;cursor:pointer;font-weight:bold;" onclick="goHome();">Home</span></td>
					<td style="padding-left:10;">
	                	<select name="isMultiTaskField" id="isMultiTaskField" style="font-family: verdana, san-serif; font-size: 11px; color: #3C6C9C;">
		                	<option value="false">Mono task</option>
		                	<option value="true">Multi task</option>
	                	</select>
					</td>
                 </tr></table>
                </td>
                <td align="right" id="pathLabel" height="28" style="font-family:Verdana;font-size:8pt;color:#3C6C9C;padding-right:10;">
                </td>
	        </tr>
	      </table>
	    </td>
	  </tr>
	  <tr>
	    <td id="gruppi" style="height:25px;"> <!-- Gruppi -->
	    </td>
	    <td width="100%" valign="bottom">
	      <table width="100%" style="height:1px;" cellpadding="0" cellspacing="0">
	         <tr>
	           <td class="grp"></td>
	           <td align="right">&nbsp;<span class="text" style="font-size:10pt;cursor:pointer;text-decoration:underline;" onclick="logout();">Logout</span>&nbsp;</td>
	         </tr>
	      </table>
	    </td>
	  </tr>
	  <tr>
	    <td colspan="3" id="applicazioni" class='app' style="height:25px;"> <!-- Applicazioni -->
	    </td>
	  </tr>
	  <tr>
	  	<td colspan="3" width="100%" style="height:1px;" class="grp"></td>
	  </tr>
	</table>
  </td>
 </tr>
 <tr>
  <td height="100%" valign="top">  <!-- Funzioni -->
    <table height="100%" width=100%" cellspacing="0" cellpadding="0">
      <tr>
      	<td id="leftCol" valign="top" style="background-color:#f0f0f0;width:160px;">
			<table class="text">
				<tr>
     				<td style="cursor:pointer;text-decoration:underline;" id="mostraValidazioni" onclick="mostraValidazioni();">Mostra validazioni</td>
				</tr>
				<tr>
					<td>
			      		<table id="momcontainer" class="text" cellspacing="0" cellpadding="0" style="display:none;">
			      			<tr>
			      				<td>
						      		<table class="text">
			 			      			<tr><td style="color:green;"><b>VALIDAZIONE MOM</b></td></tr>
						      			<tr>
						      				<td>
						      					<table class="text">
									       			<tr>
									       				<td>Azione</td>
									       				<td>
										      				<select id="MOM-azione-mom" class="text" style="width:80px;">
										      					<option value="NESSUNA">-----</option>
										      					<option value="SCARTA">SCARTA</option>
										      					<option value="METTI_IN_ATTESA">METTI_IN_ATTESA</option>
										      					<option value="MODIFICA_DATI">MODIFICA_DATI</option>
										      					<option value="PROCEDI">PROCEDI</option>
										      					<option value="CONFERMA">CONFERMA</option>
										      					<option value="SOSPENDI">SOSPENDI</option>
										      					<option value="RESPINGI">RESPINGI</option>
										      					<option value="FORZA">FORZA</option>
										      				</select>
									      				</td>
									      			</tr>
									      			<tr>
									      				<td>Pratica</td>
									      				<td><input type="text" id="MOM-id-pratica" value="" class="text" size="9"></td>
										      		</tr>
									      			<tr>
									      				<td>Prat dig.</td>
									      				<td>
										      				<select id="MOM-aggiorna-dispositiva" class="text" style="width:50px;">
										      					<option value="false">No</option>
										      					<option value="true">Si</option>
										      				</select>
									      				</td>
									      			</tr>
									      			<tr>
									      				<td>Readonly</td>
									      				<td>
										      				<select id="MOM-read-only" class="text" style="width:50px;">
										      					<option value="false">No</option>
										      					<option value="true">Si</option>
										      				</select>
										      			</td>
										      		</tr>
									      			<tr>
									      				<td>Salva</td>
									      				<td>
										      				<select id="MOM-salva" class="text" style="width:50px;">
										      					<option value="false">No</option>
										      					<option value="true">Si</option>
										      				</select>
										      			</td>
										      		</tr>
									      			<tr>
									      				<td>Valida</td>
									      				<td>
										      				<select id="MOM-valida" class="text" style="width:50px;">
										      					<option value="false">No</option>
										      					<option value="true">Si</option>
										      				</select>
										      			</td>
										      		</tr>
									      			<tr>
									      				<td>Confronta</td>
									      				<td>
										      				<select id="MOM-confronta-doppia-spunta" class="text" style="width:50px;">
										      					<option value="">No</option>
										      					<option value="S">Si</option>
										      					<option value="V">Diff.</option>
										      				</select>
										      			</td>
										      		</tr>
									      			<tr>
									      				<td>Vai a</td>
									      				<td><input type="text" id="MOM-goto-pdf" value="" class="text" size="2"></td>
										      		</tr>
						      					</table>
						      				</td>
						      			</tr>
						      			<tr><td><span style="cursor:pointer;text-decoration:underline;" onclick="callMomEvent();">Call Pdf</span></td></tr>
						      			<tr><td>&nbsp;</td></tr>
						      			<tr><td>Ricerca cli. callback</td></tr>
						      			<tr><td><input type="text" id="idxNdgMom" value="indice cliente" class="text"></td></tr>
						      			<tr><td><input type="text" id="ndgMom" value="ndg cliente" class="text"></td></tr>
						      			<tr><td>
						      				<span style="cursor:pointer;text-decoration:underline;" onclick="callRicercaClienteMomCallbackEvent();">do callback</span>
						      			</td></tr>
						      		</table>
			      				</td>
			      			</tr>
			      			<tr><td>&nbsp;</td></tr>
			      			<tr><td>&nbsp;</td></tr>
			      			<tr>
			      				<td>
						      		<table class="text">
						      			<tr><td colspan="2" style="color:green;"><b>VALIDAZIONE CERTIFICATES</b></td></tr>
						      			<tr><td colspan="2"><input type="text" id="VALIDATION-actionName" value="actionName" class="text"></td></tr>
						      			<tr>
						      				<td>Valida</td>
						      				<td>
							      				<select id="VALIDATION-doValidation" class="text">
							      					<option value="false">No</option>
							      					<option value="true">Si</option>
							      				</select>
							      			</td>
							      		</tr>
						      			<tr>
						      				<td>Adeguatezza</td>
						      				<td>
							      				<select id="VALIDATION-doAdeguatezza" class="text">
							      					<option value="false">No</option>
							      					<option value="true">Si</option>
							      				</select>
							      			</td>
							      		</tr>
						      			<tr>
						      				<td colspan="2">
						      					<span style="cursor:pointer;text-decoration:underline;" onclick="callPdfValidationEvent();">Call Pdf</span>
						      				</td>
						      			</tr>
						      		</table>
			      				</td>
			      			</tr>
			      		</table>
					</td>
				</tr>
			</table>      		
      	
      	
      	</td>
        <td id="centerCol" align="center" valign="top" style="width:<%=clientAreaWidth%>px;">
           <iframe frameborder="1" id="clientarea" name="clientarea" 
          		  style="width:100%;height:<%=clientAreaHeight%>px;background-color:ffffff;" 
          		  src="call.wfem?wfemCmd=moke.menu2.display.ElencoComandi.execute&loadConf=true&remoteAddr=<%=request.getRemoteAddr()%>&BrowserInstance=0"></iframe>
        </td>
        <td id="rightCol" style="background-color:#f0f0f0;"></td>
      </tr>
    </table>
  </td>
 </tr>
</table>

<form name="attivaFunzioneForm" method="post" target="clientarea" action="call.wfem" style="display: none;">
<input type="hidden" name="wfemCmd" value="moke.menu2.business.AttivaFunzione.executeProcess">
<input type="hidden" name="readRequest" value="false">
<input type="hidden" name="code" value="">
<input type="hidden" name="commandName" value="">
<input type="hidden" name="commandType" value="">
<input type="hidden" name="browserInstance" value="">
<input type="hidden" name="isMultiTaskMode" value="false">
</form>

<iframe id="fncIFrame" frameborder="0" style="display:none;position:absolute;height:<%=fncDivHeight%>;width:<%=fncDivWidth%>;z-index:1000;" 
		src="/<%=webApp%>/menu2/display/FncSfondo.html" onmouseout="menu.clearFunzioni();"></iframe>

<script>
menu.showGruppi();
</script>

<%=template.getFooter()%>
</body>
</html>

