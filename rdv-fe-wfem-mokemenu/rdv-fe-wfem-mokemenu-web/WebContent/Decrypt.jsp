<%@ page import="com.atosorigin.wfem.util.*"%>

<%
	String errore = "";
	String cpwd = "";
	String pwd = "";
	boolean execute = request.getParameter("execute") != null ? true : false;
	if(execute){
		Crypt crypt = new Crypt();
		cpwd = request.getParameter("cpwd");
		if(cpwd != null && cpwd.length() > 0){
			try{
			pwd = crypt.Decrypt(cpwd.trim());
			}catch(Exception e){
				errore = e.toString();
			}
		}
	}
%>

<html>
<body>
<script>
function go(){
	var l = document.dati.cpwd.value.length;
	if(l == 0){
		alert("Inserire la password");
		return;
	}
	if((l%8) > 0){
		alert("La password criptata deve avere lunghezza multipla di 8");
		return;
	}
	document.dati.submit();
}
</script>

<form name="dati" id="dati" method="post" action="Decrypt.jsp">

<table>
	<tr>
		<td style="font-family:Arial;font-size:10pt;">Inserire la password letta da DB <b>escludendo</b> le parentesi graffe di apertura/chiusura</td>
	</tr>
	<tr>
		<td><input type="text" name="cpwd" value="<%=cpwd%>" size="40"></td>
	</tr>
	<tr>
		<td><input type="text" value="<%=pwd%>" style="background-color:#f0f0f0;" readonly  size="40"></td>
	</tr>
	<tr>
		<td><input type="button" value="Decodifica" onclick="go();"></td>
	</tr>
</table>

<input type="hidden" name="execute" value="true">
</form>

<table><tr><td>
<%=errore%>
</td></tr></table>


</body>
</html>