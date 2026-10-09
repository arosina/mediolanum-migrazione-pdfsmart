function onNewCell(cell){
	cell.title = '';
	cell.align = 'center';
}

function ripristinaPubblicazione(pdfPublicationId){
	var ret = new Object();		
	ret.pdfPublicationId = pdfPublicationId;
	closeModalPopup(ret);	
}