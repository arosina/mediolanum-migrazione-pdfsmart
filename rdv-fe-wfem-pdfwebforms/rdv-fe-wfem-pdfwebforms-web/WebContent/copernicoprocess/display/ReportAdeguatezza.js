var gblpars = "&checkCodDescFields=false&manageChangedFields=false";
var numPunti = 0;
var numRetryReportAdeguatezza = 0;

function callRefreshReportAdeguatezza(){
	if(numRetryReportAdeguatezza > maxNumRetryReportAdeguatezza){
		var retryError = "La generazione del report di adeguatezza non è stata effettuata nei tempi prestabiliti. Non è possibile proseguire.";
		document.getElementById("linkReportAdeguatezza").setAttribute("statoReportAdeguatezza","ERROR");
		$("#linkReportAdeguatezza").html(retryError);
		showAlertErroreReportAdeguatezza(retryError);
		return;
	}
	numRetryReportAdeguatezza++;
	setTimeout(function(){
		$.ajax({
			url: "call.wfem?wfemCmd=prgm.pdfwebforms.reportadeguatezza.RefreshStatoReportAdeguatezza.execute&BrowserInstance="+__getBrowserInstance()+gblpars,
			dataType: "json",
			async: true,
			success: function(data) {
										var link = document.getElementById("linkReportAdeguatezza");
										link.setAttribute("statoReportAdeguatezza",data.stato);
										if(data.stato == "ERROR"){
											link.msgerr = data.msgerr;
											$("#linkReportAdeguatezza").html(linkReportAdeguatezzaTextERROR);
											showAlertErroreReportAdeguatezza(linkReportAdeguatezzaTextERROR);
											return;
										}else if(data.stato == "WAIT"){
											numPunti = numPunti % 10;
											numPunti++;
											var punti = "";
											if(numPunti == 0){
												punti="...";
											}else{
												for(var i=0;i<numPunti;i++)
													punti+="...";
											}
											$("#linkReportAdeguatezza").html(linkReportAdeguatezzaTextWAIT+punti);
											callRefreshReportAdeguatezza();
										}else if(data.stato == "END"){
											linksReportAdeguatezza = data.linksReportAdeguatezza;
											$("#linkReportAdeguatezza").html(linkReportAdeguatezzaTextEND).css({"text-decoration": "underline", "cursor": "pointer"});
										}
									}
		});	
	 },1000);	
}

function openReportAdeguatezza(link){
	var statoReport = document.getElementById("linkReportAdeguatezza").getAttribute("statoReportAdeguatezza");
	if(statoReport == "ERROR"){
		;
	}else if(statoReport == "END"){
		if(!openReportsAdeguatezza(linksReportAdeguatezza))
			return;
		if(link.getAttribute("isReportAdeguatezzaClicked") != "true"){
			$.ajax({
				url: "call.wfem?wfemCmd=prgm.pdfwebforms.reportadeguatezza.SetReportAdeguatezzaClicked.execute&BrowserInstance="+__getBrowserInstance()+gblpars,
				dataType: "json",
				async: true,
				success: function(data) {
					link.setAttribute("isReportAdeguatezzaClicked", data);
				}
			});	
		}
		
	}
}
