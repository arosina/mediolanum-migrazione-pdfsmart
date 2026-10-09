<%@ page import="java.sql.*"%>

<%
	String query="";
	String xmlText1 = ""; Timestamp xmlText1Upd=null;
	String xmlText2 = ""; Timestamp xmlText2Upd=null;
	
	String dbUrl = request.getParameter("dbUrl");
	if(dbUrl == null) dbUrl = "";
	String dbUser = request.getParameter("dbUser");
	if(dbUser == null) dbUser = "";
	String dbPwd =  request.getParameter("dbPwd");
	if(dbPwd == null) dbPwd = "";
	
	String msg = "";
	String dataora = "";	
	
	String command = request.getParameter("command");
	if(command == null)
		command="";
	
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");

	try{
		
		if(dbUrl.length() > 0 && dbUser.length() > 0 && dbPwd.length() > 0){
		
			Class.forName("com.sybase.jdbc3.jdbc.SybDriver");
			Connection conn = DriverManager.getConnection("jdbc:sybase:Tds:"+dbUrl+"/CEPE", dbUser, dbPwd);
			ResultSet rs = null;
			PreparedStatement ps = null;
		
			// Load data
			query = "select XML,D_PUBBLICAZIONE from CEPE_PAN_XML_PANIERE";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()){
				xmlText1 = rs.getString(1);
				xmlText1Upd = rs.getTimestamp(2);
			}
			if(rs != null){rs.close();rs = null;}
			if(ps != null){ps.close();ps = null;}
			
			query = "select XML,D_PUBBLICAZIONE from CEPE_PAN_XML_SSO";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()){
				xmlText2 = rs.getString(1);
				xmlText2Upd = rs.getTimestamp(2);
			}
			if(rs != null){rs.close();rs = null;}
			if(ps != null){ps.close();ps = null;}
			
			conn.close();
		}
		
	}catch(Exception e){
		msg = e.toString();
	}
%>

<html>
<body>
<script>
function go(command){
	if(document.dati.dbUrl.value == '' || document.dati.dbUser.value == '' || document.dati.dbPwd.value == ''){
		alert('Immettere i parametri di logon al DB');
		return;
	}
	document.all("msg").innerText = "Esecuzione in corso...";
	document.all("dataora").innerHTML = "";
	document.dati.command.value=command;
	document.dati.submit();
}
function reload(){
	document.dati.dbPwd.value = '';
	document.dati.dbUser.value = '';
	document.dati.command.value='';
	document.dati.submit();
}
</script>

<form name="dati" id="dati" method="post" action="PaniereXML.jsp">
<input type="hidden" name="command" value="">

<table>
	<tr>
		<td align="center">XML Paniere</td>
	</tr>
	<tr><td style="height:10;"></td></tr>
	<tr>
	  <td>
		<table>
			<tr>
				<td>DB:
					<select name="dbUrl" onchange="reload();">
						<option value="172.23.27.11:5000">S2</option>
						<option value="172.28.11.28:5000">T2</option>
						<option value="172.16.30.22:5000">P2</option>
					</select>
					<script>
					document.dati.dbUrl.value = "<%=dbUrl%>";
					</script>
				</td>
				<td>User: <input type="text" name="dbUser" value="<%=dbUser%>"></td>
				<td>Password: <input type="password" name="dbPwd" value="<%=dbPwd%>"></td>
			    <td><input type="button" value="Load" onclick="go('load');" style='width:70px;'></td>
			</tr>
		</table>
	  </td>
	</tr>
	<tr>
	  <td>
		<table>
			<tr>
			  <td>Paniere <%if(xmlText1Upd != null){%>- Aggiornato il <%=xmlText1Upd%> <%}%></td>
			  <td>SSO <%if(xmlText2Upd != null){%>- Aggiornato il <%=xmlText2Upd%> <%}%></td>
			</tr>
			<tr>
			  <td><textarea id="xmlText1" name="xmlText1" wrap="off" rows="25" cols="48"><%=xmlText1%></textarea></td>
			  <td><textarea id="xmlText2" name="xmlText2" wrap="off" rows="25" cols="48"><%=xmlText2%></textarea></td>
			</tr>
		</table>
	  </td>
	</tr>
	<tr>
	  <td><textarea id="msg" name="msg" readonly wrap="off" rows="10" cols="150"><%=msg%></textarea></td>
	</tr>
	<tr>
	  <td id="dataora" name="dataora"><%=dataora%></td>
	</tr>
</table>

</form>

</body>
</html>