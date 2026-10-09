<%@ page import="java.sql.*"%>

<%
	int parCount;
	String query="";
	String xmlText1 = ""; String xmlText1Copy = ""; Timestamp xmlText1Upd=null;
	
	String dbUrl = request.getParameter("dbUrl");
	if(dbUrl == null) dbUrl = "";
	String dbUser = request.getParameter("dbUser");
	if(dbUser == null) dbUser = "";
	String dbPwd =  request.getParameter("dbPwd");
	if(dbPwd == null) dbPwd = "";
	String consumerCode = request.getParameter("consumerCode");
	if(consumerCode == null) consumerCode = "";
	
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
			Connection conn = DriverManager.getConnection("jdbc:sybase:Tds:"+dbUrl+"/INR", dbUser, dbPwd);
			ResultSet rs = null;
			PreparedStatement ps = null;
		
			if(command.equals("update")){
				
					dataora = com.atosorigin.wfem.util.Tools.now().toString();	
					
					// Load data
					
					// ******************************************************************************************************** //
			    	if(consumerCode.equals(""))
						query = "select XmlConfiguration from XMLConfigurations where ID = 14 and ConsumerCode is null";
			    	else
						query = "select XmlConfiguration from XMLConfigurations where ID = 14 and ConsumerCode = '"+consumerCode+"'";
					ps = conn.prepareStatement(query);
					rs = ps.executeQuery();
					if(rs.next()){
						xmlText1Copy = rs.getString(1);
						if(rs.wasNull())
							xmlText1Copy = "";
					}
					if(rs != null){rs.close();rs = null;}
					if(ps != null){ps.close();ps = null;}
					// ******************************************************************************************************** //

					xmlText1 = request.getParameter("xmlText1");
									
					// ******************************************************************************************************** //
					if(!xmlText1.equals("") && !xmlText1.equals(xmlText1Copy)){
				    	if(consumerCode.equals(""))
							query = "update XMLConfigurations set XmlConfiguration = ?, UpdatedOn = ? where ID = 14 and ConsumerCode is null";
				    	else
							query = "update XMLConfigurations set XmlConfiguration = ?, UpdatedOn = ? where ID = 14 and ConsumerCode = ?";
						parCount = 1;
						ps = conn.prepareStatement(query);
				    	ps.setString(parCount++,xmlText1);
				    	ps.setTimestamp(parCount++,com.atosorigin.wfem.util.Tools.now().timestampValue());
				    	if(!consumerCode.equals(""))
					    	ps.setString(parCount++,consumerCode);
						int numRows = ps.executeUpdate();
						msg += "L'ID 14 è stato modificato: "+numRows+" righe modificate su DB\n";
						if(rs != null){rs.close();rs = null;}
						if(ps != null){ps.close();ps = null;}
					}else
						msg += "L'ID 14 non è cambiato\n";
					// ******************************************************************************************************** //
					
			}
	
			// Load data
			// ******************************************************************************************************** //
	    	if(consumerCode.equals(""))
				query = "select XmlConfiguration,UpdatedOn from XMLConfigurations where ID = 14 and ConsumerCode is null";
	    	else
				query = "select XmlConfiguration,UpdatedOn from XMLConfigurations where ID = 14 and ConsumerCode = '"+consumerCode+"'";
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			if(rs.next()){
				xmlText1 = rs.getString(1);
				if(rs.wasNull())
					xmlText1 = "";
				xmlText1Upd = rs.getTimestamp(2);
			}
			if(rs != null){rs.close();rs = null;}
			if(ps != null){ps.close();ps = null;}
			// ******************************************************************************************************** //
			
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
	document.dati.consumerCode.value = '';
	document.dati.command.value='';
	document.dati.submit();
}
</script>

<form name="dati" id="dati" method="post" action="PewayXMLConfigurations.jsp">
<input type="hidden" name="command" value="">

<table width="100%">
	<tr>
		<td align="center">XML Peway</td>
	</tr>
	<tr><td style="height:10;"></td></tr>
	<tr>
	  <td>
		<table>
			<tr>
				<td>DB:
					<select name="dbUrl" onchange="reload();">
						<option value="172.23.27.10:5000">S1</option>											
						<option value="172.28.11.27:5000">T1</option>
						<option value="172.16.30.21:5000">P1</option>
					</select>
					<script>
					document.dati.dbUrl.value = "<%=dbUrl%>";
					</script>
				</td>
				<td>User: <input type="text" name="dbUser" value="<%=dbUser%>"></td>
				<td>Password: <input type="password" name="dbPwd" value="<%=dbPwd%>"></td>
				<td>Consumer:
					<select name="consumerCode">
						<option value="">Canali Diretti</option>											
						<option value="6d31f825-dc11-4a4a-aaaa-eceb2e668caf">Tool Arretrato Vita</option>
						<option value="c94ba9ce-8f26-48a6-9a15-48535c65cae4">Tool Previdenza</option>
					</select>
					<script>
					document.dati.consumerCode.value = "<%=consumerCode%>";
					</script>
				</td>
			    <td><input type="button" value="Load" onclick="go('load');" style='width:70px;'></td>
			    <% if(dbUrl.length() > 0 && dbUser.length() > 0 && dbPwd.length() > 0){ %>
			    <td><input type="button" value="Update" onclick="go('update');" style='width:70px;'></td>
			    <% }else{ %>
			    <td><input type="button" value="Update" disabled="disabled" style='width:70px;'></td>
			    <% } %>
			</tr>
		</table>
	  </td>
	</tr>
	<tr>
	  <td>
		<table width="100%" style="table-layout:fixed;">
			<tr>
			  <td>ID 14 <%if(xmlText1Upd != null){%>- Aggiornato il <%=xmlText1Upd%> <%}%></td>
			</tr>
			<tr>
			  <td><textarea id="xmlText1" name="xmlText1" wrap="off" rows="25" style="width:100%;"><%=xmlText1%></textarea></td>
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