<%@ page import="com.atosorigin.wfem.layout.Template" %>

<%
	String country  = (String)request.getParameter("country"); if(country == null)country = new String("");
	String channel  = (String)request.getParameter("channel"); if(channel == null)channel = new String("");
	String language = (String)request.getParameter("language");if(language == null)language = new String("");
	String starturl = (String)request.getParameter("starturl");if(starturl == null)starturl = new String("");
	Template template = new Template(language,null,false,request);
	template.setApplCode("MOKEMENU2");
%>

<html>		
<head>
<style>
.text {
	font-family: Arial;
	font-size: 9pt;
}
.userPwdInput {
	font-family: Arial;
	font-size: 8pt;
	color: #1A458F;
	text-transform: uppercase;
}
</style>

<script>
function doLogin(){
	if(document.login.user.value.length == 0){
   		alert("<%=template.getProperty("Specificare la User ID")%>");
		return;
	}
	document.login.submit();
}

document.onkeypress = function(e){
	var eventCross = e || event;
	if(eventCross.keyCode == 13)
	   doLogin();
}
</script>
</head>

<body leftmargin="0">

<form id="invalidateSession" name="invalidateSession" method='post' action='call.wfem'>
<input type='hidden' name='wfemCmd' value='invalidateSession'>
</form>

<form name="login" method="post" action="call.wfem" target="cmArea">

<input type="hidden" name="wfemCmd" value="executeLogin">
<input type="hidden" name="BrowserInstance" value="0">
<input type="hidden" name="starturl" value="<%=starturl%>">
<input type="hidden" name="password" value="">

<table class="text" width="100%" height="350" valign="top">
<tr>
	<td style="line-height:1px;height:1;"></td>
</tr>
<tr>
	<td width="100%" height="80" style="background-color:#C9D6EA;">
		<table class="text" style="color:#1A458F;" width="100%">
			<tr>
				<td>&nbsp;&nbsp;<b>Env.</b></td>
				<td align="left">
				 <table><tr>
				   <td>
					<select class="userPwdInput" id="country" name="country">
					  <option value="ITA" <%=(country.equals("ITA")?"selected":"")%>>ITA</option>
					  <option value="ESP" <%=(country.equals("ESP")?"selected":"")%>>ESP</option>
					  <option value="GER" <%=(country.equals("GER")?"selected":"")%>>GER</option>
					</select>
				   </td>
				   <td>
					<select class="userPwdInput" id="channel" name="channel">
					  <option value="P" <%=(channel.equals("P")?"selected":"")%>>P</option>
					  <option value="F" <%=(channel.equals("F")?"selected":"")%>>F</option>
					  <option value="T" <%=(channel.equals("T")?"selected":"")%>>T</option>
					  <option value="L" <%=(channel.equals("L")?"selected":"")%>>L</option>
					</select>
				   </td>
				   <td>
					<select class="userPwdInput" id="language" name="language">
					  <option value="IT" <%=(language.equals("IT")?"selected":"")%>>IT</option>
					  <option value="EN" <%=(language.equals("EN")?"selected":"")%>>EN</option>
					  <option value="SP" <%=(language.equals("SP")?"selected":"")%>>SP</option>
					</select>
				   </td>
				 </tr></table>
				</td>
			</tr>
			<tr>
				<td>&nbsp;&nbsp;<b>User</b></td>
				<td align="left"><input class="userPwdInput" id="user" name="user" value="" size="20" maxLength="10"></td>
			</tr>
			<tr>
			    <td>&nbsp;</td>
				<td align="right"><span style="cursor:pointer;" onclick="doLogin();"><b>Vai...</b><span>&nbsp;&nbsp;&nbsp;</td>
			</tr>
		</table>
	</td>
</tr> 
<tr>
	<td height="350" valign="top" style="background-color:#f0f0f0;"></td>
</tr>
<tr>
	<td style="line-height:1px;height:1;"></td>
</tr>
</table>

<script>
document.login.user.focus();
</script>
</form>

</body>
</html>

<%session.invalidate();%>