<%@ page import="java.io.*"%>
<%@ page import="java.sql.*"%>

<%
	String codDisposizione = "";
	String dbUrl = "";
	String dbName = "";
	String dbUser = "";
	String dbPwd = "";
	
	String msg = "";
	String dataora = "";	
	
	boolean execute = request.getParameter("execute") != null ? true : false;
	
	if(execute){
		try{
			dataora = com.atosorigin.wfem.util.Tools.now().toString();	
			
			dbUrl = request.getParameter("dbUrl");
			dbName = request.getParameter("dbName");
			dbUser = request.getParameter("dbUser");
			dbPwd = request.getParameter("dbPwd");
			codDisposizione = request.getParameter("codDisposizione");
							
			response.setHeader("Cache-Control","no-cache");
			response.setHeader("Pragma","no-cache");
		
			String query = "select DISP_XML from CEPE_CONTRATTI_ELETTRONICI where DISP_C_DISP = ?";
	
			Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
			Connection conn = DriverManager.getConnection("jdbc:sybase:Tds:"+dbUrl+"/"+dbName, dbUser, dbPwd);
	
			ResultSet rs = null;
			PreparedStatement ps = null;
		
			int parCount = 1;
			ps = conn.prepareStatement(query);
		
		    ps.setString(parCount++,codDisposizione);
			rs = ps.executeQuery();
			if(!rs.next()){
				msg += "Disposizione non trovata";
			}else{
				String xml = rs.getString(1);
				msg += xml;
				
				FileWriter fw = new FileWriter("c:/"+codDisposizione+".xml");
				fw.write(xml);
				fw.flush();
				fw.close();
				
			}
			
			if(rs != null){rs.close();rs = null;}
			if(ps != null){ps.close();ps = null;}
	
			conn.close();
			
		}catch(Exception e){
			msg = e.toString();
		}
	}
%>

<html>
<body>
<script>
function go(){
	document.all("msg").innerText = "Esecuzione in corso...";
	document.all("dataora").innerHTML = "";
	document.dati.submit();
}
</script>

<form name="dati" id="dati" method="post" action="XmlDisp.jsp">

<table>
	<tr>
		<td align="center">Lettura XML Disposizioni</td>
	</tr>
	<tr>
	  <td>
		<table>
			<tr>
				<td>DB:
					<select name="dbUrl">
						<option value="157.28.114.23:5000">S2</option>
						<option value="172.16.30.25:5000">T2</option>
						<option value="172.16.30.22:5000">P2</option>
					</select>
					<select name="dbName">
						<option value="CEPE">CEPE</option>
					</select>
					<script>
					document.dati.dbUrl.value = "<%=dbUrl%>";
					document.dati.dbName.value = "<%=dbName%>";
					</script>
				</td>
				<td>User: <input type="text" name="dbUser" value="<%=dbUser%>"></td>
				<td>Password: <input type="password" name="dbPwd" value="<%=dbPwd%>"></td>
			</tr>
			<tr>
			</tr>
			<tr>
				<td colspan=2>Codice disposizione: <input size=40 type="text" id="codDisposizione" name="codDisposizione" value="<%=codDisposizione%>"></td>
				<td align="center"><input type="button" value="Esegui" onclick="go();"></td>
			</tr>
		</table>
	  </td>
	</tr>
	<tr>
	  <td><textarea id="msg" name="msg" readonly wrap="off" rows="25" cols="100"><%=msg%></textarea></td>
	</tr>
	<tr>
	  <td id="dataora" name="dataora"><%=dataora%></td>
	</tr>
</table>

<input type="hidden" name="execute" value="true">
</form>

</body>
</html>