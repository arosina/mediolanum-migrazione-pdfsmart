////////////////////////////////////////////////////////////////////////////////
///////////////////////////////   GUI TableList   //////////////////////////////
////////////////////////////////////////////////////////////////////////////////
//
//	 Per ogni FieldFactory.tableField("nomeProprieta") viene istanziato un oggetto
//	di tipo JSTableList	assegnato ad una variabile di nome [nomeProprietaJSTableField].
//  Questo oggetto controlla la table HTML creata ed espone dei metodi:
//
//	-	setOnClick(script, index) -> assegna all'evento onclick della riga di indice index
//			l'esecuzione di script; per index non indicato assegna lo script a tutte le righe.

//	-	setOnDblClick(script, index) -> assegna all'evento ondblclick della riga di indice index
//			l'esecuzione di script; per index non indicato assegna lo script a tutte le righe.
//
//	 Script assegnati all'evento onclick/ondblclick sono eseguiti
//	in un contesto cos? valorizzato:
//
//	-	this 			-> referenza all'oggetto <tr> che ha lanciato l'evento.
//	-	this.rowIndex 	-> indice del <tr> relativo alla pagina.
//	-	this.absIndex 	-> indice assoluto della riga nel modello lista.
//	-	this.form		-> oggetto <form> contenente i dati della riga selezionata
//			sigolarmente. In caso di selezione multipla rimane valorizzata con i
//			dati dell'eventuale ultima riga selezioneta singolarmente.
//
//	-	this.selectedRows	-> array aggiornato ad ogni selezione/deselezione singola/multipla
//			contente una o pi? righe effettivamente selezionate.
//
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function JSTableList(view
				, tableModel
				, headerModel
				, rowForm
				, currentPage
				, totalPages
				, rowsInPage
				,  onNewRowScript
				, onNewCellScript
				, selectionPolicy
				, sortable) {

	this.model = tableModel;
	this.rowForm = rowForm;

	this.selectionPolicy = selectionPolicy==null ? "multiple": selectionPolicy;

	if(this.selectionPolicy != "none") {
	  this.tableListSelection = new TableListSelection(tableModel, rowForm);
	}

	this.currentPage = currentPage;
	this.totalPages = totalPages;
	this.rowsInPage = rowsInPage;

	var _this = this;

	var currentRow;
	var currentRowFirstCell;
	var currentRowAction;
    for (var i=0; i < tableModel.rows.length; i++) {
        currentRow = tableModel.rows[i];

		currentRow.className = "tableRow";

		currentRowFirstCell = currentRow.cells(0);
		currentRowFirstCell.style.borderWidth="1px";
		currentRowFirstCell.style.borderRightStyle = "solid";
		currentRowFirstCell.style.borderBottomStyle = "solid";

		currentRowAction = currentRowFirstCell.firstChild;
		currentRowAction.className = "JSTableList_action";
		currentRowAction.style.width="100%";
		currentRowAction.style.borderStyle= "none";

		if(this.selectionPolicy != "none") {

			currentRowAction.onclick = function() {
				_this.selectRow(this.parentNode.parentNode);
			};
			currentRow.onmousedown = function() {
				_this.tableListSelection.selectRow(this, event);
			};
			currentRow.onmouseover = function() {
				_this.tableListSelection.overflyRow(this);
			};
			currentRow.onmouseout = function() {
				_this.tableListSelection.resetRow(this);
			};
			currentRowAction.onfocus = function() {
				this.parentNode.parentNode.onmouseover();
			};
			currentRowAction.onblur = function() {
				this.parentNode.parentNode.onmouseout();
			};
		}

		if(onNewRowScript != null) {
			currentRow.onNew = function() { eval(onNewRowScript); };
			currentRow.onNew();
		}
		var headerRow = headerModel.rows[0];
		currentRow.rowColor = currentRow.style.backgroundColor;

        for (var j=0; j < currentRow.cells.length; j++) {
            currentCell = currentRow.cells(j);
            currentCell.noWrap = true;
            if(j==0) continue;
 			currentCell.style.paddingLeft = 3;
            currentCell.propertyName = headerRow.cells[j].propertyName;
			currentCell.className = "JSTableList_cell";
			currentCell.onselectstart = function() { event.returnValue = false; };

			if(onNewCellScript != null) {
				currentCell.row = currentRow;
				currentCell.onNew = function() { eval(onNewCellScript); };
				currentCell.onNew();
			}
         }
	}
	
	var	current;
	this.headerCtrls = new Array();
	var lenght = headerModel.rows(0).cells.length;
    for (var k=0; k < lenght; k++) {
    	current = headerModel.rows(0).cells(k);
		this.headerCtrls[k] = new ColumnController(current, k, tableModel, sortable);
		this.headerCtrls[k].setListener(this);
	}
	this.selectedHeaderIndex = -1;
	
	view.style.visibility  = "visible";
}

JSTableList.prototype.selectRow = function (row) {
	if(row.disabled) return;
	this.tableListSelection.selectRow(row);
}
JSTableList.prototype.selectRowIndex = function (rowIndex) {
	if(rowIndex >= 0 && rowIndex < this.model.rows.length) {
		this.selectRow(this.model.rows[rowIndex]);
	}
}
JSTableList.prototype.selectRowAbsIndex = function (absIndex) {
	var rowIndex = absIndex % this.rowsInPage;
	this.selectRowIndex(rowIndex);
}

JSTableList.prototype.clickRow = function (row) {
	if(row.disabled) return;
	this.selectRow(row);
	try {
		row.onclick();
	}
	catch (e) {
		row.ondblclick();
	}
}
JSTableList.prototype.clickRowIndex = function (rowIndex) {
	if(rowIndex >= 0 && rowIndex < this.model.rows.length) {
		this.clickRow(this.model.rows[rowIndex]);
	}
}
JSTableList.prototype.clickRowAbsIndex = function (absIndex) {
	var rowIndex = absIndex % this.rowsInPage;
	this.clickRowIndex(rowIndex);
}

JSTableList.prototype.size = function() {
	return this.model.rows.length;
}
JSTableList.prototype.hasRows = function() {
	return this.size() > 0;
}

JSTableList.prototype.getCurrentPage = function() {
	return this.currentPage;
}

JSTableList.prototype.getTotalPages = function() {
	return this.totalPages;
}

JSTableList.prototype.onSortChanged = function (index) {
	var length =  this.headerCtrls.length;
	if(this.selectedHeaderIndex == index || index >= length) return;
	this.selectedHeaderIndex = index;
	var	current;
    for (var k=0; k < length; k++) {
    	if(k == index) continue;
    	current = this.headerCtrls[k];
		current.action.setState("none");
    }
}

JSTableList.prototype.setOnClick = function (script, index) {
	if(this.selectionPolicy == "none") {
		return;
	}

	var _this = this;
	var tableModel = this.model;
	var rowForm = this.rowForm;
	var selectedRows = this.tableListSelection.selectedRows;

	if(index) {
        currentRow = tableModel.rows(index);
        if(currentRow){
			currentRow.onclick = function() {
				eval(script);
			};
		}
		return;
	}

	var currentRow;
    for (var i=0; i < tableModel.rows.length; i++) {
        currentRow = tableModel.rows[i];
		currentRow.onclick = function() {
			eval(script);
		};/*
		currentRow.cells(0).firstChild.onclick = function() {
			_this.clickRow(this.parentNode.parentNode);
		};*/
    }
}

JSTableList.prototype.setOnDblClick = function (script, index) {
	if(this.selectionPolicy == "none") {
		return;
	}

	var tableModel = this.model;
	var rowForm = this.rowForm;
	var selectedRows = this.tableListSelection.selectedRows;

	if(index) {
        currentRow = tableModel.rows(index);
        if(currentRow){
			currentRow.ondblclick = function() {
				eval(script);
			};/*
			currentRow.cells(0).firstChild.onclick = function() {
				this.parentNode.parentNode.ondblclick();
			};*/
		}
		return;
	}

	var currentRow;
    for (var i=0; i < tableModel.rows.length; i++) {
        currentRow = tableModel.rows[i];
		currentRow.ondblclick = function() {
			eval(script);
		};
    }
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function TableListSelection(tableModel, rowForm) {

	this.SELECTED_ROW = "#1A458F";
	this.SELECTED_ROW_CLASS = "tableSelectedRow";

	this.NO_SELECTED_ROW = "white";
	this.NO_SELECTED_ROW_CLASS = "tableRow";

	this.OVERED_ROW = "#DBDBDB";
	this.OVERED_ROW_CLASS = "tableOverRow";

	this.tableModel = tableModel;
	this.rowForm = rowForm;

	this.selectedRows = new Array();
	this.selectedRowsIndexMap = new Array();

}

TableListSelection.prototype.selectRow = function (row, event) {

	/*** ricalibra la scrollbar in selezione ***/
	var rowTop = row.offsetTop;
	var rowHeight = row.clientHeight
	var viewArea = row.offsetParent.offsetParent
	if(rowTop < viewArea.scrollTop ){
		viewArea.scrollTop = rowTop;
	} 
 	else if(rowTop > viewArea.scrollTop+viewArea.clientHeight-rowHeight ) {
		viewArea.scrollTop = rowTop-viewArea.clientHeight+rowHeight;
	}
	/*******************************************/
	
	if(!event || (!event.ctrlKey && !event.shiftKey)) {
		this.unselectRows();
		this.selectedRows = new Array();
		this.loadRowForm(row);
		this.selectedRows.push(row);
		this.setSelectedRow(row, true);
	}
	else if(event.ctrlKey && !event.shiftKey) {
		if(row.selected) {
			var newSelectedRows = new Array();
			var currentRow;
			for(var i=0; i<this.selectedRows.length; i++) {
				currentRow = this.selectedRows[i];
				if(currentRow.absIndex == row.absIndex) continue;
				newSelectedRows.push(currentRow);
			}
			this.selectedRows = newSelectedRows;
			this.setSelectedRow(row, false);
		}
		else {
			this.selectedRows.push(row);
			this.setSelectedRow(row, true);
		}
	}
	else  {
		var findForward = -1;
		var findBackward = -1;
		var currentRow;
		for(var i=row.rowIndex; i < this.tableModel.rows.length; i++) {
			currentRow = this.tableModel.rows[i];
			if(currentRow.selected) {
				findForward = currentRow.rowIndex;
				break;
			}
		}
		if(findForward == -1) {
			for(var i=row.rowIndex; i >= 0; i--) {
				currentRow = this.tableModel.rows[i];
				if(currentRow.selected) {
					findBackward = currentRow.rowIndex;
					break;
				}
			}
			if(findBackward == -1) {
			// first selected
				if(!event.ctrlKey) {
					this.unselectRows();
					this.selectedRows = new Array();
				}
				this.selectedRows.push(row);
				this.setSelectedRow(row, true);
			}
			else {
			// findBackward
				if(!event.ctrlKey) {
					this.unselectRows();
					this.selectedRows = new Array();
					findBackward--;
				}
				for(var i=row.rowIndex; i > findBackward; i--) {
					currentRow = this.tableModel.rows[i];
					if(	!currentRow.disabled ) {
						this.selectedRows.push(currentRow);
						this.setSelectedRow(currentRow, true);
					}
				}
			}
		}
		else {
		// findForward
			if(!event.ctrlKey) {
				this.unselectRows();
				this.selectedRows = new Array();
				findForward++;
			}
			for(var i=row.rowIndex; i < findForward; i++) {
				currentRow = this.tableModel.rows[i];
				if(	!currentRow.disabled ) {
					this.selectedRows.push(currentRow);
					this.setSelectedRow(currentRow, true);
				}
			}
		}
	}
}

TableListSelection.prototype.setSelectedRow = function (row, isSelected) {
	row.form = this.rowForm;
	row.selectedRows = this.selectedRows;
	row.selected = isSelected;

	if(isSelected) {
		row.style.backgroundColor = this.SELECTED_ROW;
		row.className = this.SELECTED_ROW_CLASS;

		rowAction = row.cells(0).firstChild;
		rowAction.className = "JSTableList_selectedAction";
	}
	else {
		this.resetRow(row);
	}
}


TableListSelection.prototype.resetRow = function (row) {
	if(row.selected) {
		row.style.backgroundColor = this.SELECTED_ROW;
		row.className = this.SELECTED_ROW_CLASS;
	}
	else {
		rowAction = row.cells(0).firstChild;
		rowAction.className = "JSTableList_action";

		if(row.rowColor) {
			row.style.backgroundColor = row.rowColor;
			row.className = this.NO_SELECTED_ROW_CLASS;
		}
		else {
			row.style.backgroundColor = this.NO_SELECTED_ROW;
			row.className = this.NO_SELECTED_ROW_CLASS;
		}
	}
}

TableListSelection.prototype.overflyRow = function (row) {
	if(row.selected) return;
	row.style.backgroundColor = this.OVERED_ROW;
	row.className = this.OVERED_ROW_CLASS;
}

TableListSelection.prototype.unselectRows = function () {
	var length = this.selectedRows.length;
	var currentRow;
	for(var i=0; i<length; i++) {
		currentRow = this.selectedRows[i];
		currentRow.selected = false;
		this.resetRow(currentRow);
	}
}

TableListSelection.prototype.loadRowForm = function (row) {
	var length = this.rowForm.all.length;
	var id;
	var value;
	for(var i=0; i<length; i++) {
		id = this.rowForm.all[i].id;
		if(id != "") {
			try {
				value = eval("row."+id);
				if(value) {
					this.rowForm.all[i].value = value;
				}
			} catch (e) {}
		}
	}
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function ColumnController(colModel, index, tableModel, sortable) {
	var UP_POINTING = String.fromCharCode(9650);
	var DOWN_POINTING = String.fromCharCode(9660);

	var _this = this;

	colModel.style.borderWidth = "1px";
	colModel.style.borderRightStyle = "solid";
	colModel.style.borderBottomStyle = "solid";

	var action = colModel.firstChild;
	action.className = "JSTableList_action";
	action.style.width="100%";
	action.style.borderStyle= "none";
	
	if(sortable) {
		action.state = "none";
		action.originalLabel  = action.value;
		action.headerIndex = index;
		action.tableSort = new TableSort(tableModel, colModel, index);
		action.setState = function(state) {
				this.state = state;
				if(state == "asc") this.innerText = this.originalLabel + UP_POINTING;
				else if(state == "desc") this.innerText = this.originalLabel + DOWN_POINTING;
				else this.innerText = this.originalLabel;
			};
		action.onclick = function() {
				if(this.state == "none" || this.state == "desc") {
					this.setState("asc");
					this.tableSort.setDesc(false);
				}
				else {
					this.setState("desc");
					this.tableSort.setDesc(true);
				}
				this.tableSort.sort();
				this.style.cursor = "hand";
	
				_this.fireEvent(this.headerIndex);
			};
		action.onmousedown = function() {
				this.style.cursor = "wait";
			};
	
		action.onmouseover = function() {
				this.style.textDecoration = "underline";
			};
		action.onmouseout = function() {
				this.style.textDecoration = "none";
			};
	}
	else {
		action.style.cursor = "default";
	}
	this.action = action;
}

ColumnController.prototype.fireEvent = function(index) {
	this.listener.onSortChanged(index);
}

ColumnController.prototype.setListener = function(listener) {
	this.listener = listener;
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function JSPageSelector(view, pageSelectorModel, pageSelectorForm, currentPage, totalPages) {
	this.pageSelectorForm = pageSelectorForm;

	this._initModel(pageSelectorModel, currentPage, totalPages);
	view.style.visibility  = "visible";
}

JSPageSelector.prototype._initModel = function(model, currentPage, totalPages){

	var _this = this;

	var cells = model.rows(0).cells;
	var firstPageCell = cells(0);
	var previousPageCell = cells(2);
	var nextPageCell = cells(cells.length-3);
	var lastPageCell = cells(cells.length-1);

	for(var j=0; j<cells.length; j++) {
		cells(j).style.backgroundColor = "white";
	}

	if(currentPage==1) {
		firstPageCell.className = "JSTableList_disabledAction";
		previousPageCell.className = "JSTableList_disabledAction";
		firstPageCell.onclick = null;
		previousPageCell.onclick = null;
	}
	else {
		firstPageCell.className = "JSTableList_action";
		firstPageCell.onclick = function () {_this.firstPage();};
	    firstPageCell.onmouseover = function () {this.className="JSTableList_selectedAction";};
    	firstPageCell.onmouseout = function () {this.className="JSTableList_action";};
		previousPageCell.className = "JSTableList_action";
		previousPageCell.onclick = function () {_this.previousPage();};
	    previousPageCell.onmouseover = function () {this.className="JSTableList_selectedAction";};
    	previousPageCell.onmouseout = function () {this.className="JSTableList_action";};
	}

	if(currentPage==totalPages) {
		nextPageCell.className = "JSTableList_disabledAction";
		lastPageCell.className = "JSTableList_disabledAction";
		nextPageCell.onclick = null;
		lastPageCell.onclick = null;
	}
	else {
		nextPageCell.className = "JSTableList_action";
		nextPageCell.onclick = function () {_this.nextPage();};
	    nextPageCell.onmouseover = function () {this.className="JSTableList_selectedAction";};
    	nextPageCell.onmouseout = function () {this.className="JSTableList_action";};
		lastPageCell.className = "JSTableList_action";
		lastPageCell.onclick = function () {_this.lastPage();};
	    lastPageCell.onmouseover = function () {this.className="JSTableList_selectedAction";};
    	lastPageCell.onmouseout = function () {this.className="JSTableList_action";};
	}

	var max = (totalPages*2) + 4;
	var count = 1;
	for(var k=4; k< max; k+=2) {
		if(count == currentPage) {
			cells(k).style.backgroundColor = "silver";
			cells(k).style.fontFamily = "Arial";
			cells(k).style.fontSize = "10pt";
			cells(k).onclick = null;
		}
		else {
			cells(k).pageIndex = count;
			cells(k).className = "JSTableList_action";
			cells(k).onclick = function () {_this.gotoPage(this.pageIndex);};
			cells(k).onmouseover = function () {this.className="JSTableList_selectedAction";};
			cells(k).onmouseout = function () {this.className="JSTableList_action";};
		}
		count++;
	}
}

JSPageSelector.prototype.firstPage = function(){
	if(isRequestPending())
		return;
	startRequest();

	this.pageSelectorForm.wfemCmd.value = 'firstPage';
	this.pageSelectorForm.submit();
	return true;
}

JSPageSelector.prototype.lastPage = function(){
	if(isRequestPending())
		return;
	startRequest();

	this.pageSelectorForm.wfemCmd.value = 'lastPage';
	this.pageSelectorForm.submit();
	return true;
}

JSPageSelector.prototype.nextPage = function(){
	if(isRequestPending())
		return;
	startRequest();

	this.pageSelectorForm.wfemCmd.value = 'nextPage';
	this.pageSelectorForm.submit();
	return true;
}

JSPageSelector.prototype.previousPage = function(){
	if(isRequestPending())
		return;
	startRequest();

	this.pageSelectorForm.wfemCmd.value = 'previousPage';
	this.pageSelectorForm.submit();
	return true;
}

JSPageSelector.prototype.gotoPage = function(idx){
	if(isRequestPending())
		return;
	startRequest();

	this.pageSelectorForm.wfemCmd.value = 'gotoPage';
	this.pageSelectorForm.index.value = idx;
	this.pageSelectorForm.submit();
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function TableSort(tableModel, colModel, colIndex, isDesc) {

	this.isDesc = isDesc ? true : false;
	this.tableModel = tableModel;
	this.colIndex = colIndex;

	if(colModel.abstractType == "com.atosorigin.wfem.types.IntegerType" ||
			this.abstractType == "com.atosorigin.wfem.types.DoubleType") {
		this.numberType = true;
	}
	else
	if(colModel.abstractType == "com.atosorigin.wfem.types.DateType") {
		this.dateType = true;
	}
}

TableSort.prototype.sort = function (begin, end) {
	if(!begin) begin=0;
	if(!end) end=this.tableModel.rows.length;

	var pivot = begin;
	var left = begin + 1;
	var right = end;

	while(left < right) {
		if (this.compare(left, pivot) <= 0) {
			left++;
		} else {
			right--;
			this.exchange(left, right);
		}
	}
	left--;
	this.exchange(begin, left);

	if(left-begin > 1) this.sort(begin, left);
	if(end-right > 1) this.sort(right, end);
}

TableSort.prototype.compare = function (indexA, indexB) {

	var objA = this.tableModel.rows(indexA).cells(this.colIndex).title;
	var objB = this.tableModel.rows(indexB).cells(this.colIndex).title;
	if(this.numberType) {
		objA = new Number(objA).valueOf();
		objB = new Number(objB).valueOf();
	}
	else
	if(this.dateType) {
		objA = new Date(objA).valueOf();
		objB = new Date(objB).valueOf();
	}
	else
	{
		objA = new String(objA).valueOf();
		objB = new String(objB).valueOf();
	}

	if(objA > objB) {
		ret = 1;
	}
	else if(objA == objB) {
		ret = 0;
	}
	else {
		ret = -1;
	}

	return this.isDesc ? ret*(-1) : ret;
}

TableSort.prototype.exchange = function (indexA, indexB) {
	rowA = this.tableModel.rows(indexA);
	rowB = this.tableModel.rows(indexB);
	rowA.swapNode(rowB);
}

TableSort.prototype.setDesc = function(isDesc) {
	this.isDesc = isDesc;
}