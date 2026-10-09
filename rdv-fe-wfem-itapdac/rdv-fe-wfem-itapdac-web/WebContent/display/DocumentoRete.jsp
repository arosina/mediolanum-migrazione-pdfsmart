<%@ page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.facade.Costanti"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<%  
	template.setWlt(true);

	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setApplCode("ITAPDAC");
	template.setPageName("Documento");
	template.setLabelCodePrefix("Documento.");
	template.setLabelPosition(Template.LEFT_LABEL);  
	template.setLabelAlign("right");	
	template.setLabelWidth("100");
	template.setJSCombo(false);
	
	DocumentoModel documento = (DocumentoModel)template.getPageDataModel();
%>

<%@ include file="../documento/DefinizioneVariabili.html"%>
<% showNote = false; %>

<html>
<head>
<%=template.getHeader()%>

<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
<script src="<%=template.getWebApp()%>/documento/Documento.js"></script>
<title>Documento originale</title>
</head>

<body>
<% if(documento.getIdDocumento().isNull()){ %>
	<table class="text" width="100%"><tr><td align="center"><b>I dati inseriti dal Family Banker non sono disponibili</b></td></tr></table>
<% }else{ %>
	<table width="98%" height="100%" cellpadding="0" cellspacing="0" style="background-color:white;">
	  <tr>
	      <td height="100%" align="center" valign="top">
			  <%  boolean tabMezzi=false; %>
		      <%@ include file="../documento/include/Dati.html"%>
	  	  </td>
	  </tr>		
	</table>
<% } %>

<%=template.getFooter()%>
</body>
</html>