<%@ page import="com.atosorigin.wfem.util.Tools" %>
<%@ page import="com.atosorigin.wfem.command.CommandException" %>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"></jsp:useBean>
<% template.setApplCode("MOKEMENU"); %>

<%@ include file="url.jsi"%>
<%
	CommandException commandException = (CommandException)request.getAttribute("CommandException");
	
	String country  = (String)session.getAttribute("country"); if(country == null)country = new String("");
	String channel  = (String)session.getAttribute("channel"); if(channel == null)channel = new String("");
	String language = (String)session.getAttribute("language");if(language == null)language = new String("");
	String starturl = (String)session.getAttribute("starturl");if(starturl == null)starturl = new String("");
	
	String exmsg = commandException == null ? "Errore generico" : commandException.getMessage();
	String jsexmsg = exmsg.replaceAll("\\\"","'").replaceAll("\n"," ").replaceAll("\r"," ");
%>

<html>
<head>
<script src="/wfemlayout/dtagentApi.js"></script>
<script>
function onPageLoad(){
	try{
		dynaTrace.reportError("WFEM: <%=jsexmsg%>");
	}catch(e){}
}
</script>
</head>

<body topmargin='0' leftmargin='0' rightmargin='0' onload='onPageLoad();'>

<table width='100%' bgcolor='#ffffff' cellspacing='0' cellpadding='0' border='0'>
  <tr>
	<td class='cellaLine' height='12'>&nbsp;</td>
  </tr>
  <tr>
	<td>
		<table width='100%'>
		  <tr>

		    <td align='left' valign='middle' valign='top' height='40' class='carattereScuro' style='font-weight: bold;'>
		       &nbsp;&nbsp;Attenzione !!! Si è verificato un errore non recuperabile. Contattare l'help desk
		    </td>
		  </tr>
		</table>
	</td>
  </tr>
  <tr>
	<td class='cellaLine' bgcolor='#D2D4D6'>
	   &nbsp;
	</td>
  </tr>
  <tr>
  	<td class='cellaLine' height='12'></td>
  </tr>
  <tr>
	<td valign='middle' align='center' height='250'>
	  <table cellpadding='0' cellspacing='0' border='0' height='280' width='500'>
	   <tr>
	   		<td colspan='3' height='5' class='cellaLine'></td>
	   	</tr>
	   	<tr>
	   		<td width='5' class='cellaLine'></td>
	   		<td valign='middle' align='center'>
		   		<textarea rows="20" cols="100" readonly class="contornoTabellaErrore" style="font-size: 9pt;"><%=exmsg%>
	   			</textarea>
	   		</td>
	   		<td width='5' class='cellaLine'></td>
	    </tr>
	   </table>
	</td>
  </tr>
  <tr>
    <td class='carattereNormale' align='center'>
       <%=Tools.now()%>
    </td>
  </tr>
</table>

</body>
</html>
