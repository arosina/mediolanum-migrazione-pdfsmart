<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<% 
	template.setWlt(true);
%>

<html>
<head>
<%=template.getHeader()%>
<style>
.text{
	font-family: Arial; 
	font-size: 8pt;
	color: #1A458F;
	font-weight: bold;
}
</style>
<script>
function esci(ret){
	closeModalPopup(ret);
}
</script>
<title>Avviso</title>
</head>

<body>

<table class="text" height="100%" width="100%">
  <tr>
    <td id="msg" align="center"></td>
  </tr>
  <tr>
    <td id="buttons" align="center"></td>
  </tr>
</table>

<script>
var par = getModalPopupInputParams();
document.getElementById("msg").innerHTML = par.msg;
var butt = "<table><tr>";
if(par.type == 'ok'){
  butt += "<td><input type='button' value='Ok' class='text' style='width:50;cursor:pointer;' onclick='esci(\"ok\");'></td>";
}else if(par.type == 'yesno'){
  butt += "<td><input type='button' value='Si' class='text' style='width:50;cursor:pointer;' onclick='esci(\"yes\");'></td>"+
  		 "<td style='width:5;'></td>"+
  		 "<td><input type='button' value='No' class='text' style='width:50;cursor:pointer;' onclick='esci(\"no\");'></td>";
}else if(par.type == 'annullaprosegui'){
	butt += "<td><input type='button' value='Annulla' class='text' style='width:70;cursor:pointer;' onclick='esci(\"no\");'></td>"+
	 "<td style='width:5;'></td>"+
	 "<td><input type='button' value='Prosegui' class='text' style='width:70;cursor:pointer;' onclick='esci(\"yes\");'></td>";
}else if(par.type == 'yesnocancel'){
  butt += "<td><input type='button' value='Si' class='text' style='width:50;cursor:pointer;' onclick='esci(\"yes\");'></td>"+
  		 "<td style='width:5;'></td>"+
  		 "<td><input type='button' value='No' class='text' style='width:50;cursor:pointer;' onclick='esci(\"no\");'></td>"+
  		 "<td style='width:5;'></td>"+
  		 "<td><input type='button' value='Annulla' class='text' style='width:60;cursor:pointer;' onclick='esci(\"cancel\");'></td>";
}
butt += "</tr></table>";
document.getElementById("buttons").innerHTML = butt;
</script>

<%=template.getFooter()%>
</body>
</html>