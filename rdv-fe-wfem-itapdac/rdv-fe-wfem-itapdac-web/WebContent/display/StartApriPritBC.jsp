<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@ page import="prgm.ita.p.dac.model.*"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%
	DacKeyModel model = (DacKeyModel)template.getPageDataModel();
%>
<html>
<head>
<title>Prit</title>
</head>
<body onload="apriPritBcForm.submit();">
<form name="apriPritBcForm" method="post" action="call.wfem" style="display:none;">
	<input type="hidden" name="wfemCmd" value="prgm.ita.p.dac.business.ApriDac.execute">
	<input type="hidden" name="apriPritAttivo" value="true">
	<input type="hidden" name="idDac" value="<%=model.getIdDac()%>">
	<input type="hidden" name="ufficio" value="1">
	<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>
<table width="100%" height="100%">
	<tr><td align="center" valign="middle"><img src="<%=template.getWebApp()%>/images/waitAnimatedCircle.gif"></td></tr>
</table>
</body>
</html>
