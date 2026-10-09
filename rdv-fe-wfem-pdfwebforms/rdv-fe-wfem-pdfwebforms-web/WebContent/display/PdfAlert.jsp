<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Fonts.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<script>
var input = getModalPopupInputParams();
function doOk(){
	var ret = new Object();		
	closeModalPopup(ret);	
}
function doCancel(){
	closeModalPopup(null);	
}
function onLoad(){
	if(input.onlyCancel){
		$("#okButton").hide();
		$("#buttonSpace").hide();
		$("#cancelButton").html("Ok").removeClass("whiteButton");
	}
	$("#msg").html(input.msg);
}

$( document ).ready(function() {
	
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	
	try{
		$("[onclick]").bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32){
					this.click();
				}
			}			
		});
	}catch(e){}

});
</script>
</head>

<body onload="onLoad();">

<table width="100%" height="100%">
	<tr>
		<td height="100%" valign="top">
			<table>
				<tr>
					<td class="testo" id="msg"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td align="center">
			<table>
				<tr>
					<td><div id="cancelButton" class="normalButton whiteButton" style="width:80px;" onclick="doCancel();">Annulla</div></td>
					<td id="buttonSpace">&nbsp;&nbsp;</td>
					<td><div id="okButton" class="normalButton" style="width:80px;" onclick="doOk();">Ok</div></td>
				</tr>
			</table>
		</td>
  	</tr>
</table>
</body>
</html>
