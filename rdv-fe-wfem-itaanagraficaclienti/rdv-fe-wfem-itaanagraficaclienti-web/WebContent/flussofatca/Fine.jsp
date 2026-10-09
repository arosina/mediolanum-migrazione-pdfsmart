<%@ page import="com.atosorigin.wfem.controller.Configuration"%>
<%@ page import="com.atosorigin.wfem.util.Tools"%>
<%@ page import="prgm.ita.anagraficaclienti.facade.*"%>
<%@ page import="prgm.ita.anagraficaclienti.model.*"%>
<%@ page import="com.atosorigin.wfem.types.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);

	template.setApplCode("ITAANAGRAFICACLIENTI");
	template.setPageName("AnagraficaCliente");
	template.setLabelPosition(template.LEFT_LABEL);
	
	ClienteModel model = (ClienteModel)template.getPageDataModel();
%>

<html>
<head>
<%=template.getHeader()%>
</head>

<body>

<script>
closeModalPopup();
parent.inviaInSedeDopoFlussoFatca("<%=model.getDatiFatca().getModuloFatca()%>");
</script>

<%=template.getFooter()%>

</body>
</html>
