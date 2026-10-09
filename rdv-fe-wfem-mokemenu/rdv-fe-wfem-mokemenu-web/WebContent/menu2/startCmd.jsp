<%@ page import="java.util.*"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>

<% 
	template.setIncludedFeatures(Template.FEATURE_ALL & ~Template.FEATURE_AJAX & ~Template.FEATURE_UPLOAD);
	template.setJSCombo(false);

	String htmlPars = "";
	Enumeration pars = request.getParameterNames();
	while(pars.hasMoreElements()){
	
		String par = (String)pars.nextElement();
		
		if(par.equalsIgnoreCase("wfemCmd"))
			continue;
		
		String val = (String)request.getParameter(par);
		if(par.equalsIgnoreCase("startCmd"))
			par = "wfemCmd";
		
		htmlPars += "<input type='hidden' name='"+par+"' value='"+val+"'>\n";
		
	}

%>

<html>
<head>
<%=template.getHeader()%>
</head>

<body>

<form name="startCmd" action="call.wfem">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
<%=htmlPars%>
</form>

<%=template.getFooter()%>

</body>

<script>
startRequest();
document.startCmd.submit();
</script>

</html>
