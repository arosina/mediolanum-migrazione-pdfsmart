<%@ page import="com.atosorigin.wfem.login.*" %>
<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>
<% template.setApplCode("MOKEMENU"); %>

<html>
<head>
<%@ include file="./url.jsi"%>
<%
	String country  = (String)session.getAttribute("country"); if(country == null)country = new String("");
	String channel  = (String)session.getAttribute("channel"); if(channel == null)channel = new String("");
	String language = (String)session.getAttribute("language");if(language == null)language = new String("");
	String starturl = (String)session.getAttribute("starturl");if(starturl == null)starturl = new String("");

	String param="?";
	param += "country="+country+"&";
	param += "channel="+channel+"&";
	param += "language="+language+"&";
	param += "starturl="+starturl;
	String failureMessage = (String)request.getAttribute("loginFailure");
	if(failureMessage.startsWith("#"))
		failureMessage = template.getProperty("loginFailed."+failureMessage.substring(1));
%>

<script>
function goHome(){
	document.location.href = '<%=webApp%>/login/cmArea.html<%=param%>';
	return;
}
</script>
<link rel="stylesheet" type="text/css" href="/<%=webApp%>/menu2/style/Menu.css">
</head>

<body style="margin:0;">
<table width="100%" cellspacing="0" cellpadding="0">
  <tr>
    <td style="height: 20pt;" class="text" onclick="goHome();" style="cursor:pointer;">
       &nbsp;&nbsp;
    </td>
  </tr>
  <tr>
	<td class="scuro" height="1"></td>
  </tr>
  <tr>
  	<td style="height: 5pt;">&nbsp;</td>
  </tr>
  <tr>
	<td valign="middle" align="center">
	  <table width="80%" height="280" class="infobox">
	   	<tr>
	   		<td valign="middle" align="center" style="font-size:15pt;font-weight:bold;">
			   Login fallito
	   		</td>
	   </tr>
	   </table>
	</td>
  </tr>
</table>
</body>
</html>
