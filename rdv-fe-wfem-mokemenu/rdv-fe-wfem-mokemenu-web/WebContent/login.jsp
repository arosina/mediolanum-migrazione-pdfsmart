<%@ page import="com.atosorigin.wfem.layout.Template" %>

<%
	String country  = (String)request.getParameter("country"); if(country == null)country = new String("");
	String channel  = (String)request.getParameter("channel"); if(channel == null)channel = new String("");
	String language = (String)request.getParameter("language");if(language == null)language = new String("");
	String starturl = (String)request.getParameter("starturl");if(starturl == null)starturl = new String("");
	
	String param="?";
	param += "country="+country+"&";
	param += "channel="+channel+"&";
	param += "language="+language+"&";
	param += "starturl="+starturl;

	Template template = new Template(language,null,false,request);
	template.setApplCode("MOKEMENU2");
%>
<html>
<head>
<%@ include file="./url.jsi"%>
<title><%=titolo%></title>
</head>

<frameset rows="160,80%*" framespacing="0" frameborder="0"> 
	<frame src="login/loginTestata.jsp<%=param%>" scrolling="no" noresize="noresize">
	<frameset cols="211,80%*" framespacing="0" > 
		<frame name="loginData" src="login/loginData.jsp<%=param%>" scrolling="no" noresize="noresize">
		<frame src="login/cmArea.html" name="cmArea" scrolling="auto" noresize="noresize"> 
	</frameset>
</frameset> 
</html>
