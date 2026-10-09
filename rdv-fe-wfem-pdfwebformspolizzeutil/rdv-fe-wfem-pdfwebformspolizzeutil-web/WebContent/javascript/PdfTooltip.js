function PdfTooltip(){
	// This is intentional
}

PdfTooltip.prototype.init = function(){
	$("#pdfPagesCont").append("<div id='pdfTooltipDiv' style='position: absolute; left: 0; top: 0; width:300px; z-index:2001; display:none;'>" +
			"<table width='100%' cellpadding='0' cellspacing='0'>" +
			"<tr><td><tr><td>" +
			"<table width='100%' style='background-color: white; border: #0000ff 1px solid; table-layout: fixed;'>" +
			"<col width='15%'><col width='*'><tr><td align='center' valign='top' style='padding-top:5;'>" +
			"<img src='/PdfWebFormsPolizzeUtil/images/info.png'></td><td id='pdfTooltipMessage' class='text' style='padding:3; font-size: 12px; color: #666666;'>" +
			"</td></tr></table></td></tr><tr><td valign='top'>" +
			"<div id='pdfTooltipArrow' style='position:relative;top:-1;color:black;'><img src='/PdfWebFormsPolizzeUtil/images/puntaTooltip.png'></div></td></tr></table></div>");
}

PdfTooltip.prototype.showTooltip = function(objName,msg){
	//per posizione su pagina diversa dalla prima
	var paginaContenitore = $( "div[name='"+objName+"Cont']" ).parent("div[pagenum]" );
	var topPagina = paginaContenitore.height() * (paginaContenitore.attr('pagenum') -1);

	$("#pdfTooltipMessage").html(msg);
	var obj = $( "div[name='"+objName+"Cont']" );
	var wstruct = $("#pdfTooltipDiv");
	var fieldCont = $(obj);
	var pageOffset = 0;
	var fieldPos = fieldCont.position();
	var fMiddle = fieldPos.left + (fieldCont.width() / 2);
	var warLeft = fMiddle - (wstruct.width() / 2);
	var warRight = warLeft+wstruct.width();
	var warTop = topPagina + fieldPos.top - wstruct.height() - 5 + pageOffset;
	$("#pdfTooltipArrow").css({left: (wstruct.width()/2) - 10});
	var page = $("#fieldsCont").position();
	if(warLeft < page.left){
		warLeft = 10;
		$("#pdfTooltipArrow").css({ left: fieldPos.left - 10 });
	}else if(warRight > (page.left+$("#fieldsCont").width())){
		warLeft = fMiddle - wstruct.width() + 20;
		$("#pdfTooltipArrow").css({ left: wstruct.width() - 30 });
	}
	wstruct.css({left: warLeft, top: warTop}).show("slide",{ direction: "down" },300);
}

PdfTooltip.prototype.hideTooltip = function(){
	$("#pdfTooltipDiv").hide();
}

function bindTooltip(objName,msg){
	$("#"+objName).bind({
		mouseout: function(event){
			pdfTooltip.hideTooltip();
		},
		mouseenter: function(event){
			pdfTooltip.showTooltip(objName,msg);
		}
	});
}

var pdfTooltip = new PdfTooltip();
