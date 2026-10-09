function onNewDocPlico(row) {
	if (row.idDac == '') {
		row.style.color = "white";
		row.style.backgroundColor = "red";
	}
}