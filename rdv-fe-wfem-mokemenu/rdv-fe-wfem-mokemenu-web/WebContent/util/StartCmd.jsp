<%@ page import="java.util.*"%>

<%
String htmlPars = "";
Enumeration pars = request.getParameterNames();
while(pars.hasMoreElements()){

	String par = (String)pars.nextElement();
	
	if(par.equalsIgnoreCase("wfemCmd"))
		continue;
	
	String val = (String)request.getParameter(par);
	if(par.equalsIgnoreCase("startCmd"))
		par = "wfemCmd";
	
	//htmlPars += "<input type='hidden' name='"+par+"' value='"+val+"'>\n";
	htmlPars += "<input type='hidden' name='"+par+"' value=\""+val+"\">\n";
}

String bi = (String)request.getAttribute("BrowserInstance");
if ( bi == null ) bi = "0"; 

%>

<html>
<body>
<form name="startCmd" action="call.wfem">
<input type="hidden" name="BrowserInstance" value="<%=bi%>">
<%=htmlPars%>
</form>
<script>
document.startCmd.submit();
</script>
</body>
</html>
