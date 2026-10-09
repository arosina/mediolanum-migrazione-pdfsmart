<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
	template.setIncludedFeatures(template.FEATURE_ALL & ~template.FEATURE_AJAX & ~template.FEATURE_UPLOAD);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.UP_LABEL);
	template.setLabelAlign("left");
	template.setJSCombo(false);
	ClienteModel model = (ClienteModel)template.getPageDataModel();

	boolean modificabile = true;
    if(model.getModality() == template.READ_MODALITY)
    	modificabile = false;
%>

<html>
<head>
<%=template.getHeader()%>
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
<script src="/ItaAnagraficaClienti/include/InfoPersonali.js"></script
<script src="/ItaAnagraficaClienti/include/Variazioni.js"></script>

</head>

<body topmargin="0" scroll="no">

<style>
<% if(modificabile){ %>
.mtory{
	color: coral;
	font-weight: normal;
	font-style: italic;
}
<% }else{ %>
.mtory{
	display: none;
}
<% } %>
</style>

<!--------  GOTO PAGE FORM --------------->
<form name="goback" method="post" action="call.wfem" style="display: none;">
	<input type="hidden" name="wfemCmd" value="">
	<input type="hidden" name="forwardDisplay" value="">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>
<!------------------------------------------->

<form name="datiread" style="display: none;">
<%=template.hidden("progressivo")%>
<%=template.hidden("codAgente")%>
<%=template.hidden("codPotenziale")%>
<%=template.hidden("codMediolanum")%>
<%=template.hidden("isDitta")%>
<%=template.hidden("datiApplicativi_flagClienteSegnalato")%>
<%=template.hidden("callingAppl")%>
<input type="hidden" name="refreshable" value="<%=model.getDatiApplicativi().isRefreshable()%>">
</form>

<form name="dati" method="post" action="call.wfem" style="margin: 5px;">  
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="resetErrors" value="false">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<%=template.hidden("agente_codAgente")%>
<%=template.hidden("agente_codRete")%>
<%=template.hidden("showBack")%>

<center>

<%@ include file="../base/header.html"%>

<table class="text" width="100%" cellspacing="0" cellpadding="0">
  <tr><td style="height:10px;"></td></tr>
  <tr>
  	<td>
  		<table width="100%" class="text"><tr>
  			<td><hr style="line-height:1px;width:5;color:#1A458F;"></td>
  			<td style="white-space: nowrap;font-weight:bold;">&nbsp;<img src="<%=template.getWebApp()%>/images/section.gif">
			   &nbsp;Dati del datore di lavoro&nbsp;
			   <%if(model.getIsEffettivo().booleanValue() ||
			        (model.getIsPotenziale().booleanValue() && !model.getCodInforete().isNull())){%>
				   &nbsp;Cod Inforete: <%=model.getCodInforete()%>&nbsp;
			   <%}%>
			   <%if(model.getIsEffettivo().booleanValue()){ %>
				   &nbsp;Cod Potenziale: <%=model.getCodPotenziale()%>&nbsp;
			   <%}%>
  			</td>
  			<td width="100%"><hr style="height:1px;width:100%;color:#1A458F;"></td>
  		</tr></table>
  	</td>
  </tr>
  <tr>
     <td style="padding-left: 25px;">
       <table>
       	<tr>
		  <td style="<%=model.fch("cognome")%>"><%=template.field("cognome","labelcode='Ragione sociale <span class=\"mtory\">(*)</span>' size='77' maxlength='80'")%></td>
		  <td style="<%=model.fch("partitaIva")%>"><%=template.field("partitaIva","labelcode='Partita Iva <span class=\"mtory\">(*)</span>' type='num' size='15' maxlength='11'")%></td>
       	</tr>
       </table>
     </td>
  </tr>
  
  <tr><td style="height:10px;"></td></tr>
  
  <tr>
  	<td>
  		<table width="100%" class="text"><tr>
  			<td><hr style="line-height:1px;width:5;color:#1A458F;"></td>
  			<td style="white-space: nowrap;font-weight:bold;">&nbsp;<img src="<%=template.getWebApp()%>/images/section.gif">
			   &nbsp;Sede legale&nbsp;
  			</td>
  			<td width="100%"><hr style="height:1px;width:100%;color:#1A458F;"></td>
  		</tr></table>
  	</td>
  </tr>
  <tr>
     <td>
      <%
      	template.setPrefix("residenza_indirizzo");
      %>
	  <%@ include file="../include/Indirizzo.html"%>
     </td>
  </tr>
  <tr>
     <td style="padding-left: 25px;">
       <table>
       	<tr>
	     <%template.setPrefix("recapiti_telefonoResidenza");%>
	     <td style="<%=model.fch(template.getPrefix(),"prefisso")%>"><%=template.field("prefisso","type='num' maxlength='8' size='9'")%></td>
		 <td style="<%=model.fch(template.getPrefix(),"numeroTelefono")%>"><%=template.field("numeroTelefono","type='num' maxlength='12' size='15' labelcode='DatoreDiLavoro_numeroTelefono'")%></td>
		 <td style="width:20px;"></td>
	     <%template.setPrefix("recapiti");%>
	     <td style="<%=model.fch(template.getPrefix(),"email")%>">
		       <table>
		         <tr>
		           <td><%=template.field("email","uppercase='false' size='50' labelcode='DatoreDiLavoro_email'")%></td>
		           <% if(modificabile){ %>
		           <td valign="bottom">
		              <input type="button" value="@" class="action" onclick="aggingiChiocciola();"></td>
		           </td>
		           <td valign="bottom" class="text">Usa questo pulsante, o la tastiera, per inserire il carattere @ nell'indirizzo e-mail</td>
		           <% } %>
		         </tr>
		       </table>
		  </td>
		</tr>
	   </table>
     </td>
  </tr>
  <tr><td style="height:20px;"></td></tr>
</table>

<%=template.getFooter()%>
</form>

<%@ include file="../base/footer.html"%>

</center>

</body>
</html>
