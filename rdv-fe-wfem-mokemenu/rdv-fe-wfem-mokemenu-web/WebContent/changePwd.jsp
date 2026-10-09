<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>
<% template.setApplCode("MOKEMENU"); %>

<%@ include file="url.jsi"%>

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
%>

<html>
<head>

<script>
function doChangePwd(){
	if(!top.changePwdEnabled){
   		alert("<%=template.getProperty("status.changePwdWait")%>");
		return;
	}
	
	if(checkPwd()){
		top.enableChangePwd(false);
		document.changePwd.submit();
	}
	return;
}

function cancelChangePwd(){
	document.location.href = '<%=webApp%>/login/cmArea.html<%=param%>';
	return;
}

function checkPwd(){
	var oldpwd = document.changePwd.password.value;
	var newpwd = document.changePwd.newpassword.value;
	var newpwdconf = document.changePwd.newpasswordconf.value;

	if(oldpwd.length == 0){
   		alert("<%=template.getProperty("message.changePwd.noOldPwd")%>");
		document.changePwd.password.focus();
   		return false;
	}
	
	if(oldpwd.length < 5){
   		alert("<%=template.getProperty("message.changePwd.oldPwdTooShort")%>");
		document.changePwd.password.focus();
   		return false;
	}
   	
	if(newpwd.length == 0){
   		alert("<%=template.getProperty("message.changePwd.noNewPwd")%>");
		document.changePwd.newpassword.focus();
   		return false;
	}
   	
	if(newpwd == oldpwd){
   		alert("<%=template.getProperty("message.changePwd.newPwdEqualOldPwd")%>");
		document.changePwd.newpassword.focus();
   		return false;
	}
   	
	if(newpwdconf.length == 0){
   		alert("<%=template.getProperty("message.changePwd.noNewPwdConf")%>");
		document.changePwd.newpasswordconf.focus();
   		return false;
	}
   	
	if(newpwdconf != newpwd){
		alert("<%=template.getProperty("message.changePwd.confPwdNotEqualToNewPwd")%>");
		document.changePwd.newpasswordconf.focus();
		return false;
	}
	
	if(newpwd.length < 5){
    	alert("<%=template.getProperty("message.changePwd.newPwdTooShort")%>");
		document.changePwd.newpassword.focus();
		return false;
	}
		 	
	return true;
}		

function document.onkeypress(){
	if(window.event.keyCode == 13){
	    window.event.keyCode = 0;
	    doChangePwd();
	    return;
	}
	if(window.event.keyCode == 27){
	    window.event.keyCode = 0;
	    cancelChangePwd();
	    return;
	}
}		
</script>

<link rel="stylesheet" type="text/css" href="<%=webApp%>/login/style/login<%=country%><%=channel%>.css">
</head>

<body topmargin='0' leftmargin='0' rightmargin='0'>

<form name="changePwd" action="call.wfem">
<input type="hidden" name="wfemCmd" value="executeChangePwd">

<table width='100%' bgcolor='#ffffff' cellspacing='0' cellpadding='0' border='0'>
  <tr>
	<td class='cellaLine' height='12'></td>
  </tr>
  <tr>
	<td colspan='2'>
		<table width='100%'>
		  <tr>
		    <script>
		    if(top.changePwdError){
			    document.writeln('<td align="left" valign="middle" valign="top" height="40" class="carattereScuro" style="color: red; font-weight: bold;">');
		        document.writeln('&nbsp;&nbsp;<%=template.getProperty("label.changePwdFailed")%>');
		    }else{
			    document.writeln('<td align="left" valign="middle" valign="top" height="40" class="carattereScuro" style="font-weight: bold;">');
		        document.writeln('&nbsp;&nbsp;<%=template.getProperty("label.changePwd.messaggio")%>');		    
		    }
		    </script>
		    </td>
		  </tr>
		</table>
	</td>
  </tr>
  <tr>
	<td  class='cellaLine' height='1' bgcolor='#D2D4D6'></td>
  </tr>
  <tr>
	<td width='600' valign='top' height='300'>
		<table width='600' cellpadding='2' cellspacing='0' border='0'>
		  <tr>
			<td colspan='3' class='cellaLine' height='12'></td>
		  </tr>
		  <tr>
		     <td colspan='3' class='carattereScuro' style='font-size: 10pt; font-weight: bold;'>
			    <img src='<%=webApp%>/login/images/quadsez.jpg'>&nbsp;<%=template.getProperty("label.changePwd.titolo")%>
		     </td>
		  </tr>
		  <tr>
			<td class='cellaLine' height='12' colspan='3'></td>
		  </tr>
		  <tr>
			<td width='150' class='carattereNormale'>
				&nbsp;&nbsp;<%=template.getProperty("label.changePwd.oldpwd")%>
			</td>
			<td align='left' colspan='2'>
				<input name='password' type='password' size='25' maxLength='15' class='campoDiInput'>
			</td>
		  </tr>
		  <tr>
			<td class='carattereNormale'>
				&nbsp;&nbsp;<%=template.getProperty("label.changePwd.newpwd")%>
			</td>
			<td align='left' colspan='2'>
				<input name='newpassword' type='password' size='25' maxLength='15' class='campoDiInput'>
			</td>
		  </tr>
		  <tr>
			<td class='carattereNormale'>
				&nbsp;&nbsp;<%=template.getProperty("label.changePwd.newpwdconf")%>
			</td>
			<td align='left' colspan='2'>
				<input name='newpasswordconf' type='password' size='25' maxLength='15' class='campoDiInput'>
			</td>
		  </tr>
		  <tr>
			<td class='cellaLine' height='12' colspan='3'></td>
		  </tr>
		  <tr>
			<td width='75' align="right" onclick="cancelChangePwd();" class="carattereNormale" style='cursor: hand;'>
				<img src='<%=webApp%>/login/images/freccialink.jpg' border='0'>
				<span><%=template.getProperty("label.changePwdCancel")%></span>
			</td>
			<td width='75' align="right" onclick='doChangePwd();' class='carattereNormale' style='cursor: hand;'>
				<img src='<%=webApp%>/login/images/freccialink.jpg' border='0'>
				<span><%=template.getProperty("label.changePwdDo")%></span>
			</td>
			<td>&nbsp;</td>
		  </tr>
		</table>
	</td>
  </tr>
</table>
<script>
document.changePwd.password.focus();
top.enableChangePwd(true);
top.changePwdError = false;
</script>
</form>

</body>
</html>
