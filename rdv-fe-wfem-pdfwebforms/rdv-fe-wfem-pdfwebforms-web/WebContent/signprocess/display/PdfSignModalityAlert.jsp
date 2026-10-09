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
var originalHeight = 280;
var popupInput = getModalPopupInputParams();
function doOk(){
	var ret = new Object();	
	ret.isFirmaADistanza = "";
	var fs = document.getElementsByName("isFirmaADistanza");
	for(var i=0;i<fs.length;i++){
		if(fs[i].checked){
			ret.isFirmaADistanza = fs[i].value;
			break;
		}
	}
	if(ret.isFirmaADistanza === ""){
		originalHeight = parent.$("#wltPopupContainer").dialog("option","height");
		parent.$("#wltPopupContainer").dialog("option","height",200);
		parent.document.getElementById("ui-dialog-title-wltPopupContainer").innerHTML="Attenzione!";
		document.getElementById("layout1").style.display="none";
		document.getElementById("layout2").style.display="";
		return;
	}
	closeModalPopup(ret);	
}

function doIndietro(){
	parent.$("#wltPopupContainer").dialog("option","height",originalHeight);
	parent.document.getElementById("ui-dialog-title-wltPopupContainer").innerHTML="Attenzione";
	document.getElementById("layout2").style.display="none";
	document.getElementById("layout1").style.display="";
}

function doCancel(){
	closeModalPopup(null);	
}

$(document).ready(function() {
	try{
		
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

		if(popupInput == null || popupInput.msg === "")
			$("#msg").hide();
		else
			$("#msg").html(popupInput.msg);
		
		var tipoDistanzaCollocamento = popupInput.tipoDistanzaCollocamento;
		if(tipoDistanzaCollocamento == null){ // Nessun pdf con il campo
			if(popupInput.mod == 'C')
				$('input:radio[name="isFirmaADistanza"][value="C"]').attr("checked","checked");
		}else{
			$('input:radio[name="isFirmaADistanza"]').attr("disabled",true);
			if(popupInput.mod == 'C')
				$('input:radio[name="isFirmaADistanza"][value="C"]').attr("checked","checked");
			else
				$('input:radio[name="isFirmaADistanza"][value="'+tipoDistanzaCollocamento+'"]').attr("checked","checked");
		}
		
	}catch(e){}
});
</script>
</head>

<body>
<table width="100%" height="100%">
	<tr>
		<td id="layout1">
			<table width="100%" height="100%">
				<tr>
					<td height="100%" valign="top">
						<table class="title">
							<tr><td id="msg" colspan="2"></td></tr>
							<tr>
								<td colspan="2">
								Seleziona la modalità con cui si concluderà la proposta derivante da questa consulenza:				
								</td>
							</tr>
							<tr>
								<td style="padding-top: 10; vertical-align: top;"><input type="radio" name="isFirmaADistanza" value="P"></td>
								<td style="padding-top: 10; vertical-align: top;">In presenza</td>
							</tr>
							<tr>
								<td style="padding-top: 6; vertical-align: top;"><input type="radio" name="isFirmaADistanza" value="C"></td>
								<td style="padding-top: 6; vertical-align: top;">Copernico</td>
							</tr>
							<tr>
								<td style="padding-top: 6; vertical-align: top;"><input type="radio" name="isFirmaADistanza" value="D"></td>
								<td style="padding-top: 6; vertical-align: top;">Firma digitale a distanza (se il collocamento è avvenuto a distanza seguendo le linee guida contenute nel manuale "Comunicazione e collocamento a distanza tramite Microsoft Teams")</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center">
						<table>
							<tr>
								<td><div id="cancelButton" class="normalButton whiteButton" style="width:80px;" onclick="doCancel();">Annulla</div></td>
								<td id="buttonSpace" style="width:200;">&nbsp;</td>
								<td><div id="okButton" class="normalButton" style="width:80px;" onclick="doOk();">Continua</div></td>
							</tr>
						</table>
					</td>
			  	</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td id="layout2" style="display:none;">
			<table width="100%" height="100%">
				<tr>
					<td height="100%" valign="top">
						<table class="title">
							<tr><td>Nessuna preferenza è stata selezionata</td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="right">
						<table>
							<tr>
								<td><div id="indietroButton" class="normalButton" style="width:80px;" onclick="doIndietro();">Indietro</div></td>
							</tr>
						</table>
					</td>
			  	</tr>
			</table>
		</td>
	</tr>
</table>
</body>
</html>
