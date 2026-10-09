<%@page import="java.math.BigDecimal"%>
<%@page import="com.atosorigin.wfem.util.Tools"%>
<%@page import="com.atosorigin.wfem.command.ClientSessionContext"%>
<%@page import="com.atosorigin.wfem.layout.Template"%>
<%@page import="prgm.pdfwebforms.core.PdfHtmlFieldsDrawer"%>
<%@page import="prgm.pdfwebforms.model.PdfModel"%>
<%@page import="prgm.pdfwebforms.model.PdfAttachModel"%>
<%@page import="prgm.pdfwebforms.drivers.io.attach.Attach"%>

<jsp:useBean id="template" scope="request" class="com.atosorigin.wfem.layout.Template"/>
<%  
	template.setWlt(true);
	template.setIncludedFeatures(Template.FEATURE_ALL);

	PdfModel model = (PdfModel)template.getPageDataModel();
	
	String fileTypes = Attach.DEFAULT_FILE_TYPES;
	String mimeTypes = Attach.DEFAULT_MIME_TYPES;
	String maxdimMeasure = new Attach().getMaxdimMeasure();
   	boolean almenoUnoObbligatorio = false;
   	for(int i=0;i < model.getPdfAttachments().size(); i++){
      PdfAttachModel pdfAttach = (PdfAttachModel)model.getPdfAttachments().get(i);
      if(i==0){
    	  fileTypes = pdfAttach.getDriverAttachRef().getFileTypes();
    	  mimeTypes = pdfAttach.getDriverAttachRef().getMimeTypes();
    	  maxdimMeasure = pdfAttach.getDriverAttachRef().getMaxdimMeasure();
      }
	  if(pdfAttach.getDriverAttachRef().isMandatory()){
		  if(pdfAttach.isImplicitAttach())
      		continue;		  
		  almenoUnoObbligatorio = true;
		  break;
	  }
   	}
%>
<html>
<head>
<%=template.getHeader()%>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Fonts.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Buttons.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/DataEntry.css'/>
<link rel='stylesheet' type='text/css' href='<%=template.getWebApp()%>/style/Dialog.css'/>

<script src="<%=template.getWebApp()%>/jsWait/JsWait.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfAttachments.js"></script>
<script src="<%=template.getWebApp()%>/display/PdfAccessibility.js"></script>
<script>
var numAllegati = <%=model.getPdfAttachments().size()%>;
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
// Override normal load fle wait mgm
var requestPending = false;
function startRequest(){
	requestPending = true;
}
</script>
<style>
body{
	margin: 0;
	padding: 0;
	border: 0;
	height: 100%;
	overflow: hidden;
}
</style>
</head>

<body>
<form name="goForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="">
<input type="hidden" name="scrollYValue" value="0">
<input type="hidden" name="scrollXValue" value="0">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="convertAttachForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.ConvertAttach.execute">
<input type="hidden" name="attachIdx" value="">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<form name="removeAttachForm" method="post" action="call.wfem" style="display:none;">
<input type="hidden" name="wfemCmd" value="prgm.pdfwebforms.business.RemoveAttach.execute">
<input type="hidden" name="attachIdx" value="">
<input type="hidden" name="checkCodDescFields" value="false">
<input type="hidden" name="manageChangedFields" value="false">
<input type="hidden" name="BrowserInstance" value="<%=request.getAttribute("BrowserInstance")%>">
</form>

<center>
<table class="htmlContainer"><tr><td height="100%">
<div style="height:100%;width:100%;position:relative;overflow:auto;">
	<div style="position:absolute;left:0;top:0;">
	<table class="testo" width="100%" cellpadding="0" celspacing="0">
	<tr>
		<td style="padding-left:10;">
			<table width="100%">
				<tr>
					<td class="titolo" style="padding-top:20;padding-bottom:5px;">
						<span><b>Caricamento allegati</b></span>
					</td>
				</tr>
				<tr>
					<td>
						<span>File ammessi: <b><%=fileTypes.replaceAll(",",",&nbsp;")%></b>; dimensione massima file: <b><%=maxdimMeasure%></b></span>
					</td>
				</tr>
				<% if(almenoUnoObbligatorio){ %>
				<tr>
					<td>
						<span style="font-size: 12;">Gli allegati marcati con (*) sono obbligatori</span>
					</td>
				</tr>
				<% } %>
				<tr>
					<td style="padding-top: 10;">
						<table class="testo" width="100%" style="table-layout: fixed;">
						<col width="30%"><col width="*">
						<% 
						   for(int i=0;i < model.getPdfAttachments().size(); i++){
						      PdfAttachModel pdfAttach = (PdfAttachModel)model.getPdfAttachments().get(i);
							  if(pdfAttach.isImplicitAttach())
						      		continue;		  
						%>
							<tr>
								<td><%=pdfAttach.getDriverAttachRef().getDescr()%><%=(pdfAttach.getDriverAttachRef().isMandatory()?"&nbsp;(*)":"")%><%=(pdfAttach.getDriverAttachRef().getDescrSuffix().isEmpty()?"":""+"<br>"+pdfAttach.getDriverAttachRef().getDescrSuffix())%></td>
								<td id="attach<%=i%>">
									<table class="testo">
										<tr>
											<td style="display:none;"><%=template.field("pdfAttachments"+i+"_file","maxdim='"+pdfAttach.getDriverAttachRef().getMaxdim()+"'")%></td>
											<td style="width:100px;" id="chooseAttachButtonDiv<%=i%>" align="center">
												<div class="normalButton" onclick="onSelectFile('<%=i%>');">
													<% if(pdfAttach.getFile().isNull()){ %>
														Scegli&nbsp;file
													<% }else{ %>
														Cambia&nbsp;file
													<% } %>
												</div>
											</td>									
											<td id="fileNameCont<%=i%>" style="width:300px;padding-left:20px;">
												<% if(pdfAttach.getFile().hasTypeErrors()){ %>
													<span style="white-space:nowrap;color:red;"><%=pdfAttach.getFile().getTypeErrors().get(0).toString()%></span>
												<% }else{ %>
													<% if(!pdfAttach.getFile().isNull()){ %>
														<span id="fileName<%=i%>" title="Apri allegato" onclick="openAttach('<%=i%>');" style="text-decoration: underline;cursor: pointer;"><%=pdfAttach.getFile().getFileName()%></span>
													<% } %>
												<% } %>
											</td>
											<td style="width:80px;padding-left:20px;" id="removeAttachButtonDiv<%=i%>" align="left">												
												<% if(!pdfAttach.getFile().hasTypeErrors() && !pdfAttach.getFile().isNull()){ %>
													<img style="height:16px;cursor:pointer;" title="Annulla" src="<%=template.getWebApp()%>/images/checkX.png" onclick="removeAttch('<%=i%>');">
												<% } %>
											</td>									
											<td>
												<script>
												$("#pdfAttachments<%=i%>_file").attr("accept","<%=mimeTypes%>");
												$("#pdfAttachments<%=i%>_fileIFrame").load(function() {
													onLoadAttch("<%=i%>");
												});
												requestPending = false;
												</script>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						<% } %>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr><td style="padding-top:10;"><div class="line">&nbsp;</div></td></tr>
	
	<tr>
		<td>
			<table width="100%" cellpadding="0" cellspacing="0">
				<tr>
					<td style="padding:10;padding-top:3;">
						<table>
							<tr>
			    				<td><div class="normalButton whiteButton" onclick="doGoPrevPdf();">Indietro</div></td>
							</tr>
						</table>
					</td>				
					<td align="right" style="padding:10;padding-top:3;">
						<table>
							<tr>
				    			<td><div class="normalButton" onclick="doGoOn();">Avanti</div></td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
	</div>
</div>
</table>
</center>

<div class='waitDiv divwaitOpacityCoverStyle' style="display:none; z-index: 10000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;"></div>
<div class='waitDiv' style="display:none; z-index: 20000; position: absolute; left: 0; top: 0; height: 100%; width: 100%;">
	<table style="height: 100%; width: 100%;"><tr><td align="center" valign="middle"><img id="waitAnchor" src="<%=template.getWebApp()%>/jsWait/loading_0.gif"></td></tr></table>
</div>

<%=template.getFooter()%>
</body>
</html>