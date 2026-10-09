
<html>

<body style="margin:0;">
<%@ include file="../url.jsi"%>
<%
	String country  = (String)request.getParameter("country"); if(country == null)country = new String("");
	String channel  = (String)request.getParameter("channel"); if(channel == null)channel = new String("");
	String imgPath = "/"+webApp+"/login/images/"+country.toUpperCase() + "/" + channel.toUpperCase();
%>

<table width='100%' height="100%" cellspacing="0" cellpadding="0">
<tr>
	<td align="left" valign="middle">
		<img src="<%=imgPath%>/logoSx.gif">
	</td>
	<td align='right' valign="middle">
		<img src="<%=imgPath%>/logoDx.gif">
	</td>
</tr>
<tr>
	<td colspan="2" bgcolor="#CEE1F1" style="height:33;">
		<table width="100%">
		  <tr>
		    <td><img src="/<%=webApp%>/login/images/imgTestata.gif"></td>
		  </tr>
		</table>
	</td>
</tr>
</table>
</body> 
</html>