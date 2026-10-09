<%@page import="java.util.*"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.ita.p.dac.facade.Costanti"%>
<%@page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<html>

<% 	
	template.setApplCode("ITAPDAC");
	template.setPageName("Documento");
	template.setLabelCodePrefix("Documento.");
	template.setLabelPosition(Template.LEFT_LABEL);
	template.setJSCombo(false);
	
	DocumentoModel documento = (DocumentoModel)template.getPageDataModel();
%>

<%@ include file="../documento/DefinizioneVariabili.html"%>

<head>
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Labels.css" type="text/css">
<link rel="stylesheet" href="<%=template.getWebApp()%>/resources/css/Tab.css" type="text/css">
<%=template.getHeader()%>
</head>

<body scroll="no">

<%  boolean tabMezzi=false; %>
<%@ include file="../documento/include/Dati.html"%>

<%=template.getFooter()%>
</body>
</html>
