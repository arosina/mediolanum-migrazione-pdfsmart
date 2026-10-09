<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="prgm.ita.anagraficaclienti.display.FlagPepFrontendStatus"%>
<%@ page import="com.atosorigin.wfem.types.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.UP_LABEL);
	template.setJSCombo(false);
	ClienteModel model = (ClienteModel)template.getPageDataModel();

    boolean modificabile = true;
    if(model.getModality() == template.READ_MODALITY)
    	modificabile = false;
    
	int savModality;
	boolean savModificabile;

	String codFiscaleMultiploMsg = null;
	// Codice fiscale del titolare trovato più volte nel db
	if(model.getClienteTitolare() != null && model.getClienteTitolare().getCodFiscale().hasTypeErrors()){ 
	 	model.getClienteTitolare().getCodFiscale().resetTypeErrors();
		codFiscaleMultiploMsg = "Attenzione: e' stata trovata più di una persona fisica con il codice fiscale della ditta individuale.\\n"+ 
	 	              		    "L'applicazione utilizzerà la prima scheda trovata, ma è opportuno fare istanza al Numero Rosso per segnalare il problema.";
	}
	
	String messaggioPatenteUCO = DocumentoModel.MESSAGGIO_PATENTE_UCO;
%>

<html>
<head>
<%=template.getHeader()%>
<script>
var stampaBozzaFormTargetCount = 0;
var jsPopupMode = <%=model.getPopupMode()%>;
var jsMessaggioPatenteUCO = '<%=messaggioPatenteUCO%>';
</script>

<script src="/ItaAnagraficaClienti/display/Costanti.jsp"></script>
<script src="/ItaAnagraficaClienti/display/StatiPropostaAnagrafica.jsp"></script>
<script src="/ItaAnagraficaClienti/base/header.js"></script>
<script src="/ItaAnagraficaClienti/base/Toolbar.js"></script>
<script src="/ItaAnagraficaClienti/display/AnagraficaCliente.js"></script>

<script src="/ItaAnagraficaClienti/include/DatiGenerali.js"></script>
<script src="/ItaAnagraficaClienti/include/DatiPrivacy.js"></script>
<script src="/ItaAnagraficaClienti/include/Documento.js"></script>
<script src="/ItaAnagraficaClienti/include/Recapiti.js"></script>
<script src="/ItaAnagraficaClienti/include/Residenza.js"></script>
<script src="/ItaAnagraficaClienti/include/Indirizzo.js"></script>
<script src="/ItaAnagraficaClienti/include/InfoPersonali.js"></script>
<script src="/ItaAnagraficaClienti/include/AdempimentiNormativi.js"></script>
<script src="/ItaAnagraficaClienti/include/Variazioni.js"></script>

<script>
var template_getProperty_AnagraficaCliente_partitaIva       					= '<%=template.getProperty("AnagraficaCliente.partitaIva")%>';
var template_getProperty_AnagraficaCliente_dataNascita      					= '<%=template.getProperty("AnagraficaCliente.dataNascita")%>';
var template_getProperty_AnagraficaCliente_dataCostituzione 					= '<%=template.getProperty("AnagraficaCliente.dataCostituzione")%>';
var template_getProperty_AnagraficaCliente_comuneNascita_comune					= '<%=template.getProperty("AnagraficaCliente.comuneNascita_comune")%>';
var template_getProperty_AnagraficaCliente_comuneNascita_comuneCostituzione 	= '<%=template.getProperty("AnagraficaCliente.comuneNascita_comuneCostituzione")%>';
var template_getProperty_AnagraficaCliente_infoPersonali_codAteco		 		= '<%=template.getProperty("AnagraficaCliente.infoPersonali_codAteco")%>';
var template_getProperty_AnagraficaCliente_infoPersonali_codGruppoAttivita 		= '<%=template.getProperty("AnagraficaCliente.infoPersonali_codGruppoAttivita")%>';
var template_getProperty_AnagraficaCliente_infoPersonali_codSottogruppoAttivita = '<%=template.getProperty("AnagraficaCliente.infoPersonali_codSottogruppoAttivita")%>';
var oggi = '<%=Tools.today()%>';
</script>

<style>
<% if(modificabile){ %>
.mtory{
	color: coral;
	font-weight: normal;
	font-style: italic;
}
.mtoryscoring{
	background-image:url('/ItaAnagraficaClienti/images/pem.gif');
	background-repeat:repeat;
	border:1px solid;
	height: 10;
	width: 10;
}
<% }else{ %>
.mtory{
	display: none;
}
.mtoryscoring{
	display: none;
}
<% } %>

.normalButton{
	background-color: #a3cbe5;
	color: #fff;
	font-family: Segoe UI;
	font-size: 14px;
	text-align: center;
	vertical-align: middle;	
	position: relative;
	cursor: pointer;
	padding-left: 12px;
	padding-right: 12px;
	padding-top: 6px;
	padding-bottom: 6px;
}
.title{
	font-family: Segoe UI;
	font-size: 13px;
	font-weight: normal;
	color: #666666;	
	vertical-align: middle; 
}
</style>

<% if(model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPREVIDENZA)){ %>
<title>Long Life Tool - Censimento anagrafico</title>
<% }else if(model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPROTEZIONE)){ %>
<title>Protection Easy Tool - Censimento anagrafico</title>
<% }else if(model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLDOSSIERTITOLI)){ %>
<title>Bond Analysis Tool - Censimento anagrafico</title>
<% } %>

<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/base/toggle.css'/>
</head>

<body>

<!-- Campi in errore
<%
	java.util.Map errmap = Tools.modelErrorMap(model);
	java.util.Iterator kit = errmap.keySet().iterator();
	while(kit.hasNext()){ %>
		<%=(String)kit.next()%>
<%	} %>
 -->

<form name="clienteKeyForm" method="post" action="call.wfem" style="display:none;">  
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="readRequest" value="true">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="codAgente" value="<%=model.getCodAgente()%>">
<input type="hidden" name="codPotenziale" value="<%=model.getCodPotenziale()%>">
<input type="hidden" name="codMediolanum" value="<%=model.getCodMediolanum()%>">
</form>

<form name="datiread" style="display: none;">
<%=template.hidden("progressivo")%>
<%=template.hidden("codAgente")%>
<%=template.hidden("codPotenziale")%>
<%=template.hidden("codMediolanum")%>
<%=template.hidden("isDitta")%>
<%=template.hidden("datiApplicativi_flagClienteSegnalato")%>
<%=template.hidden("callingAppl")%>
<%=template.hidden("statoProposta")%>
<%=template.hidden("parentBrowserInstance")%>
<input type="hidden" name="refreshable" value="<%=model.getDatiApplicativi().isRefreshable()%>">
</form>

<form name="stampaBozzaForm" method="post" action="call.wfem" style="display:none;">  				
<input type="hidden" name="wfemCmd" value="prgm.ita.anagraficaclienti.business.StampaBozzaExecute.execute">
<input type="hidden" name="resetErrors" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="dati" method="post" action="call.wfem" style="margin:5px;">  				
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="resetErrors" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<input type="hidden" name="isFlussoFatcaTerminato" value="false">
<input type="hidden" name="moduloFatca" value="<%=model.getModuloFatca()%>">
<%=template.hidden("agente_codAgente")%>
<%=template.hidden("agente_codRete")%>
<%=template.hidden("datiApplicativi_nomeTabCorrente")%>
<%=template.hidden("showBack")%>
<%=template.hidden("codMediolanumTitolare")%>
<%=template.hidden("codPotenzialeTitolare")%>
<%=template.hidden("codFiscaleTitolare")%>
<%=template.hidden("infoPersonali_combinazioneProvenienzaPatrimonio")%>

<script>
document.jsModificabile = <%=modificabile%>;
</script>

<% if(model.getDatiApplicativi().isVariazione()){ %>
   <%@ include file="../base/headervar.html"%>
<% }else{ %>
   <%@ include file="../base/header.html"%>
<% } %>
	
<center>
<table id="htmlContainer" width="98%" cellpadding="0" cellspacing="0">

  <tr>
	  <td align="center" style="padding-top:3;">
	  	<% if(model.getIsDitta().booleanValue()){ %>
	  		<%@ include file="../include/DatiGeneraliDitta.html"%>
	  	<% }else{ %>
	    	<%@ include file="../include/DatiGeneraliPersonaFisica.html"%>
	    <% } %>
	  </td>
  </tr>

  <% 
  	String mainTabName; String mainClassName;
  %>
  <tr>
      <td align="center" id="tabLabelsContainer">

        <table width="99%" cellspacing="0"><tr>
        
		  <!-- ******************************** -->          
          <!-- RESIDENZA -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "residenza";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	mainClassName = "htabSelected";
	   	     }else{
             	if(model.getResidenza().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>" id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
          	  onclick="selectTab(this);">
          	  	&nbsp;Indirizzi <span class="mtory">(*)</span>&nbsp;
          	  <% if(model.getCallingAppl().equals(Costanti.CALLING_APPL_MHD) ||
          			model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPREVIDENZA) ||
          			model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPROTEZIONE)){ %>
          	  	<span class="mtoryscoring">&nbsp;</span>&nbsp;
          	  <% } %>
          </td>
          
		  <!-- ******************************** -->          
          <!-- DOCUMENTO -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "documento";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	mainClassName = "htabSelected";
	   	     }else{
             	if(model.getDocumento().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
              onclick="selectTab(this);">
              <% if(model.getIsDitta().booleanValue()){ %>
               &nbsp;Documento titolare ditta/Lib. prof.<span class="mtory">(*)</span>&nbsp;
              <% }else{ %>
               &nbsp;Documento <span class="mtory">(*)</span>&nbsp;
              <% } %>
          </td>
          
		  <!-- ******************************** -->          
          <!-- INFO PERSONALI (+ DITTA) -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "infoPersonali";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	       mainClassName = "htabSelected";
	   	     }else{
             	if(model.getInfoPersonali().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
              onclick="selectTab(this);">
              <% if(model.getIsDitta().booleanValue()){ %>
              	&nbsp;Info ditta <span class="mtory">(*)</span>&nbsp;
              <% }else{ %>
              	&nbsp;Info personali <span class="mtory">(*)</span>&nbsp;
              <% } %>
          	  <% if(model.getCallingAppl().equals(Costanti.CALLING_APPL_MHD) ||
            		model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPREVIDENZA) ||
              		model.getCallingAppl().equals(Costanti.CALLING_APPL_TOOLPROTEZIONE)){ %>
          	  	<span class="mtoryscoring">&nbsp;</span>&nbsp;
          	  <% } %>
          </td>
          
          <!-- ******************************** -->          
	      <!-- PERSONA GIURIDICA -->
		  <!-- ******************************** -->
		  <% if(model.getIsPersonaGiuridica().booleanValue()){ %>
		   	  <% mainTabName = "infoSocietarie"; %>
		   	  <% mainClassName = "htab"; %>
	   	     <% if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	  mainClassName = "htabSelected";
	   	     	} %>
          	  <td nowrap class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
	              onclick="selectTab(this);">
	              &nbsp;Info Societarie&nbsp;
	          </td>          
          <% } %>
          
		  <!-- ******************************** -->          
          <!-- RECAPITI -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "recapiti";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	mainClassName = "htabSelected";
	   	     }else{
             	if(model.getRecapiti().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>" id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
          	  onclick="selectTab(this);">
        	  &nbsp;Recapiti <span class="mtory">(*)</span>&nbsp;
          </td>

		  <!-- ******************************** -->          
          <!-- ADEMPIMENTI NORMATIVI -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "adempimentiNormativi";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	mainClassName = "htabSelected";
	   	     }else{
             	if(model.getAdempimentiNormativi().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
              onclick="selectTab(this);">
              &nbsp;Adempimenti normativi <span class="mtory">(*)</span>&nbsp;
          </td>

		  <!-- ******************************** -->          
          <!-- PRIVACY -->
		  <!-- ******************************** -->          
	   	  <% mainTabName = "datiPrivacy";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName)){
	   	     	mainClassName = "htabSelected";
	   	     }else{
             	if(model.getDatiPrivacy().containErrors(model))
       	   	    	mainClassName = "htabErrors";
	   	     }
          %>
          <td nowrap class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
              onclick="selectTab(this);">
              &nbsp;Privacy <span class="mtory">(*)</span>&nbsp;
          </td>

		  <!-- ******************************** -->          
          <!-- VARIAZIONI -->
		  <!-- ******************************** -->          
		  <% String style= "";
		     if(model.getVariazioni().getElencoVariazioni().size() == 0)
		     	style = "style='display: none;'";
		  %>
	   	  <% mainTabName = "variazioni";
	   	     mainClassName = "htab";
	   	     if(model.getDatiApplicativi().getNomeTabCorrente().equals(mainTabName))
	   	       mainClassName = "htabSelected";
          %>
          <td nowrap <%=style%>
              class="<%=mainClassName%>"  id="<%=mainTabName%>" name="<%=mainTabName%>" valign="bottom"
              onclick="selectTab(this);">
              &nbsp;Variazioni in corso&nbsp;
          </td>

      	  <td nowrap width="100%" style="border-bottom: 1px solid silver;">&nbsp;</td>
      	  
         </tr></table>
         
      </td>
  </tr>
  
  <tr>
	<td align="center" id="tabContainer" valign="top">	  
		<table width="99%" cellpadding="0" cellspacing="0">
		   <tr>
		   
		      <td align="left" class="htabContent" id="sezioneAnagrafica" name="sezioneAnagrafica">
		      <table width="100%"><tr><td style="padding:20px;">
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("residenza")){ %>
				<%@ include file="../include/Residenza.html"%>
		      <% } %>
		      
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("recapiti")){ %>
				<%@ include file="../include/Recapiti.html"%>
		      <% } %>
		      
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("infoPersonali")){ %>
				<%@ include file="../include/InfoPersonali.html"%>
		      <% } %>
		
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("infoSocietarie")){ %>
				<%@ include file="../include/InfoSocietarie.html"%>
		      <% } %>		      
		
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("documento")){ %>
				<%@ include file="../include/Documento.html"%>
		      <% } %>
		
		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("adempimentiNormativi")){ %>
				<%@ include file="../include/AdempimentiNormativi.html"%>
		      <% } %>

		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("datiPrivacy")){ %>
				<%@ include file="../include/DatiPrivacy.html"%>
		      <% } %>

		   	  <% if(model.getDatiApplicativi().getNomeTabCorrente().equals("variazioni")){ %>
				<%@ include file="../include/Variazioni.html"%>
		      <% } %>
			  </td></tr></table>
		      </td>
		
		   </tr>
		</table>

    </td>
  </tr>
  
  <tr>
  	<td>
		<% if(!model.getDatiApplicativi().isVariazione()){ %>
			<%@ include file="../base/footer.html"%>
		<% }else{ %>
			<%@ include file="../base/footervar.html"%>
		<% } %>
	</td>
  </tr>

</table>
</center>
<%=template.getFooter()%>
</form>

<% if(!model.getDatiApplicativi().isVariazione()){ %>

	<% if(model.hasCommandErrors()){ %>
		<script>
		  document.getElementById('attenzione1').innerHTML = '!';
		  document.getElementById('attenzione2').innerHTML = 'Attenzione';
		  document.getElementById('toolDown').style.visibility = 'visible';
		</script>
	<% } %>
	
	<iframe id="closeThreadIF" name="closeThreadIF" src="<%=template.getWfemLayoutWebApp()%>/blankPage.html" style="display:none;"></iframe>
	
	<!--------  GOTO PAGE FORM --------------->
	<form name="goback" method="post" action="call.wfem" style="display: none;">
		<input type="hidden" name="wfemCmd" value="">
		<input type="hidden" name="forwardDisplay" value="">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	</form>
	<!------------------------------------------->
	
	<!--------  STAMPA FORM --------------->
	<form name="stampaForm" method="post" action="<%=model.getCodPotenziale()%>.wfem" style="display: none;">
		<input type="hidden" name="progressivo"     value="">
		<input type="hidden" name="codAgente"       value="<%=model.getCodAgente()%>">
		<input type="hidden" name="codPotenziale"   value="<%=model.getCodPotenziale()%>">
		<input type="hidden" name="codMediolanum"   value="<%=model.getCodMediolanum()%>">
		<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
		<input type="hidden" name="wfemCmd" value="">
	</form>
	<!------------------------------------------->
	
	<form name="cancellaVariazioneForm" method="post" action="call.wfem" style="display: none;">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" name="codPotenziale" value="">
	<input type="hidden" name="codAgente" value="">
	<input type="hidden" name="progressivo" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
	</form>
<% } %>

<%
	boolean showCorniceCentraleBlu = model.getDatiApplicativi().isShowCorniceCentraleBlu();
	boolean isVariazioneInviata = model.getIsPropostaInviataInSede().booleanValue() && !model.getCodMediolanum().isNull();
	boolean showMessaggioCentrale = model.getDatiApplicativi().getMessaggioCentrale().length() > 0 && !model.getDatiApplicativi().getMessaggioCentrale().equals("###");
	boolean showAlertAddendumAVR = model.getDatiApplicativi().isShowAlertAddendumAVR();
%>
<%if(showCorniceCentraleBlu){%>
		<iframe name='divMessaggiIF' id='divMessaggiIF' src='<%=template.getWfemLayoutWebApp()%>/blankPage.html' scrolling='no' frameborder='0' style='position:absolute;left:20%;top:20%;height:45%;width:65%;'></iframe>
		<div name='divMessaggi' id='divMessaggi' style='z-index:100;position:absolute;left:20%;top:20%;height:45%;width:65%;background-color:white;'>
		<center>
		<table width='100%' height='100%' cellpadding='10' cellspacing='0' style='background-color:#587bc4;'>
		 <tr>
		   <td align='center'>
				<table width='100%' height='100%' cellpadding='2' cellspacing='0' style='background-color:white;'>
					<tr><td>
						<table>
							<tr><td align='right' style='height:20px;'><table><tr><td class='text' style='font-size:12px;cursor:pointer;text-decoration:underline;' title='Chiudi' onclick='closeMessages();'>Chiudi</td></tr></table></td></tr>
							<tr>
								<td align='center' class='text'><b>
									<%=isVariazioneInviata?"Variazione effettuata.":"Operazione conclusa correttamente."%> 
									Attenzione ai seguenti punti:
								</b></td>
							</tr>
							<tr><td style='height:20px;'>&nbsp;</td></tr>
							<tr><td align='center' class='text' style='height:20px;'><b>si ricorda che &egrave; necessaria la firma del cliente sulla scheda di variazione cartacea in caso di modifica dei dati nelle sezioni: dati anagrafici, indirizzo di residenza, documento identificativo e informazioni personali obbligatorie. In assenza della firma del cliente, la variazione non potr&agrave; essere processata.</b></td></tr>
							<% if(showAlertAddendumAVR){%>
								<tr><td style='height:20px;'>&nbsp;</td></tr>
								<tr>
									<td><table><tr>
										<td><img src="<%=template.getWebApp()%>/images/alertAddendumAVR.png"></td>
										<td class='text' style='font-size:10px;'><%=prgm.ita.anagraficaclienti.facade.ReminderAddendumAVR.REMINDER_MESSAGE%></td>
									</tr></table></td>
								</tr>
							<% } %>
							<% if(showMessaggioCentrale){ %>
								<tr><td style='height:20px;'>&nbsp;</td></tr>
								<tr><td align='center'>
									<table><tr><td class='text' style='font-size:12px;'>
									<%=template.getProperty(model.getDatiApplicativi().getMessaggioCentrale().toString())%>
								    </td></tr></table>
								</td></tr>
							<% } %>
							<% if(isVariazioneInviata){%>
								<tr><td style='height:20px;'>&nbsp;</td></tr>
								<tr><td align='center' style='height:20px;'><%=template.action("stampaVariazioneAction")%></td></tr>
							<% } %>
							<tr><td style='height:20px;'>&nbsp;</td></tr>
						</table>
					</td></tr>
				</table>
		   </td>
		 </tr>
		</table>
		</center>
		</div>
		<%
			model.getDatiApplicativi().setMessaggioCentrale("###");
			model.getDatiApplicativi().setShowCorniceCentraleBlu(false);
		%>
<%}%>

<%if( model.getDatiApplicativi().getErroreCentrale().length() > 0){ %>
	<iframe name='divMessaggiIF' id='divMessaggiIF' src='<%=template.getWfemLayoutWebApp()%>/blankPage.html' scrolling='no' frameborder='0' style='position:absolute;left:20%;top:20%;height:45%;width:65%;'></iframe>
	<div name='divMessaggi' id='divMessaggi' style='z-index:100;position:absolute;left:20%;top:20%;height:45%;width:65%;background-color:white;'>
	<center>
	<table width='100%' height='100%' cellpadding='10' cellspacing='0' style='background-color:red;'>
	 <tr>
	   <td align='center'>
			<table width='100%' height='100%' cellpadding='2' cellspacing='0' style='background-color:white;'>
			<tr><td align='right' style='height:20px;'><table><tr><td class='text' style='font-size:12px;cursor:pointer;text-decoration:underline;' title='Chiudi' onclick='closeMessages();'>Chiudi</td></tr></table></td></tr>
			<tr><td align='center'>
				<table><tr><td class='text' style='font-size:12px;padding: 20px;'>
				<%=template.getProperty(model.getDatiApplicativi().getErroreCentrale().toString())%><%model.getDatiApplicativi().setErroreCentrale("");%>
			    </td></tr></table>
			</td></tr>
			</table>
	   </td>
	 </tr>
	</table>
	</center>
	</div>
<%}%>

<div id="alertMessage" style="display:none;">
	<table height="100%" width="100%">
		<tr>
			<td valign="top" height="100%" style="padding: 10;">
				<table>
					<tr>
						<td class="title" id="alertMessageText"></td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td align="center">
				<table><tr>
					<td><div class="normalButton" onclick="$('#alertMessage').dialog('destroy');">Ok</div></td>
				</tr></table>
			</td>
		</tr>
	</table>
</div>

<div id="globalScriptCont" style="display:none">
<table><tr><td>
</td></tr></table>
</div>

<% 	if(model.getFirstFatcaPopupName() != null){ %>
		<script>openModalPopup("prgm.ita.anagraficaclienti.flussofatca.<%=model.getDatiFatca().getFatcaPopupName()%>","BrowserInstance=<%=request.getAttribute("BrowserInstance")%>",null,null,"Verifica FATCA",300,500,true);</script>
<% 		model.setFirstFatcaPopupName(null);
	} %>

<% if(model.isOnStampaBozzaAction()){ %>
<script>
document.stampaBozzaForm.target = "pop"+stampaBozzaFormTargetCount;
window.open("","pop"+stampaBozzaFormTargetCount,"titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10");
document.stampaBozzaForm.submit();
stampaBozzaFormTargetCount++;
</script>
<% } %>
<% model.setOnStampaBozzaAction(false); %>
</body>
</html>
