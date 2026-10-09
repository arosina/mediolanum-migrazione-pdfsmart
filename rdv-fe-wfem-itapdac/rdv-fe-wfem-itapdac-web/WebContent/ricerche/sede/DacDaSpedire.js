function onNewCell(cell){
	if(cell.propertyName == 'dataOraIns')
	   cell.align = 'center';
	else if (cell.propertyName == 'reso') {
		if (cell.row.reso == 'true')
			cell.align = "center";
		else
			cell.innerHTML = "&nbsp;";
	}
}

var curDac=null;
function selectDac(dac){
	enableAction("apriAction",true);
	curDac = dac;
}

function doApriAction(){
	document.dati.idDac.value = curDac.idDac;
	document.dati.submit();
	return true;
}