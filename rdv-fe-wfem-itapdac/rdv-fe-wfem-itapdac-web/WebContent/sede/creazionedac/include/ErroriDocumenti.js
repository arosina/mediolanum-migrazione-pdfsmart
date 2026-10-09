function newCellErrDoc(cell) {
	if (cell.propertyName == "dataIns" ||
			cell.propertyName == "daAutorizzare" ||
			cell.propertyName == "dataAutorizzazione") {

			cell.align="center";
	}
}

function newRowErrDoc(row) {
	if (row.daAutorizzare == "false" || (row.daAutorizzare == "true" && row.dataAutorizzazione != "")) {
		row.style.color = "#1A458F";
		row.style.backgroundColor = "yellowgreen";
	} else {
		row.style.color = "white";
		row.style.backgroundColor = "#FF3333";
	}
}