/* ************************************************************************************ */
/* Grid interface object */
/* ************************************************************************************ */
GridIntf = function(){
	var pdfCount = 0;
	var calcCount = 0;
}

GridIntf.prototype.gridListManageOnScroll = function(tabDiv){
	if(tabDiv.header != null)
		tabDiv.header.scrollLeft=tabDiv.scrollLeft;
	if(tabDiv.headerFixCol != null)
		tabDiv.headerFixCol.scrollTop=tabDiv.scrollTop;
}

GridIntf.prototype.openGridPdf = function(img,title,formname,listPropName,browserInstance){
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=openGridPdf&listPropertyName="+listPropName+"&BrowserInstance="+browserInstance;
	document.getElementById("utilIFrame").src=url;
}

GridIntf.prototype.openGridCalc = function(img,title,formname,listPropName,browserInstance){
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=openGridCalc&listPropertyName="+listPropName+"&BrowserInstance="+browserInstance;
	document.getElementById("utilIFrame").src=url;
}

GridIntf.prototype.firstPage = function(formname,listPropName){
	this.submitGtridPageCommand(formname,'firstPage',listPropName);
}

GridIntf.prototype.lastPage = function(formname,listPropName){
	this.submitGtridPageCommand(formname,'lastPage',listPropName);
}

GridIntf.prototype.nextPage = function(formname,listPropName){
	this.submitGtridPageCommand(formname,'nextPage',listPropName);
}

GridIntf.prototype.previousPage = function(formname,listPropName){
	this.submitGtridPageCommand(formname,'previousPage',listPropName);
}

GridIntf.prototype.gotoPage = function(formname,idx,listPropName){
	this.submitGtridPageCommand(formname,'gotoPage',listPropName,idx);
}

GridIntf.prototype.submitGtridPageCommand = function(formname,command,listPropName,idx){
	if(isRequestPending())
		return;
				
	startRequest();
	try{ eval("document."+listPropName+"Grid.unload();"); }catch(e){}
	
	try{
		var form = document.forms[formname];
		var savcmd = form.wfemCmd.value; 
		form.wfemCmd.value = command;
		form.listPropertyName.value = listPropName;
		if(command == 'gotoPage')
			form.index.value = idx;
		if(form.onsubmit){
			form.onsubmit();
		}else{
			if(document.getElementById(listPropName+"GridHiddenSubmitContainer")){
				wfemHiddenSubmit(form,listPropName+"GridHiddenSubmitContainer");
			}else{
				form.submit();
			}
		}
		form.wfemCmd.value = savcmd;
	}catch(e){
		alert(command+': ' + e.message);
		stopRequest();
	} 	
}

/* ************************************************************************************ */
/* Grid Object */
/* ************************************************************************************ */
GridList = function(propName, modelPropertyNames, selectionPolicy, sortable, onclick, ondblclick, onnewrow, onnewcell, 
				  	modality, fieldsmodality, onnewheader, containscdata){

		var list = document.getElementById(propName+"Grid");
		var tableModel = document.getElementById(propName+"GridTab");
		var tabDiv = document.getElementById(propName+"GridTabDiv");
		
		var tabFixColumn = document.getElementById(propName+"GridTabFixColumn"); // Counter object
		var upFixColumn = document.getElementById(propName+"GridUpFixColumn");   // Counter object
		var headerFixCol = document.getElementById(propName+"GridDivBodyFixColumn");   // Counter object
		
		var header = document.getElementById(propName+"GridHeaderDiv");
		var headerCells = document.getElementById(propName+"GridHeaderTab").rows[0].cells;	
		
		this.selectionPolicy = selectionPolicy;
		this.list = list;
		this.tableModel = tableModel;
		this.tabDiv = tabDiv;
		this.tabFixColumn = tabFixColumn;
		this.upFixColumn = upFixColumn;
		this.header = header;
		this.headerCells = headerCells;
		
		this.modality = modality;
		this.fieldsmodality = fieldsmodality;
		this.containscdata = containscdata;		

		this.tabDiv.headerFixCol = headerFixCol;
		this.tabDiv.header = header;
	
		for(i=0;i<headerCells.length;i++){
			headerCells[i].onselectstart = function() { preventDefault(event); /* From JavaScriptUtil.js */ };			
			headerCells[i].colIndex = i;
			headerCells[i].abstractType = headerCells[i].getAttribute("abstractType");
			headerCells[i].propertyName = headerCells[i].getAttribute("propertyName");
			if(sortable == 'true')
				headerCells[i].style.cursor = 'pointer';
			headerCells[i].gridList = this;
			if(onnewheader != ''){
		    	headerCells[i].onNew = function(){eval(onnewheader);};
		    	headerCells[i].onNew();
			}
		}
		
		if(sortable == 'true'){
			if(this.upFixColumn != null){
				this.upFixColumn.style.cursor = 'pointer';
				this.upFixColumn.onclick = function(){headerCells[0].isDesc=false;headerCells[0].onclick();};
			}
			this.tableSort = new GridSort(headerCells,tabFixColumn,tableModel);
		}	
	
		this.loopAllCell(headerCells,tableModel,onnewrow,onnewcell,selectionPolicy,this.modality,this.fieldsmodality,modelPropertyNames);
	
		if(selectionPolicy == 'multi' || selectionPolicy == 'single')
			this.gridListSelection = new GridListSelection(this,onclick,ondblclick);
				
}

GridList.prototype.unload = function(){
	try{
		var headerCells = this.headerCells;
		for(var i=0;i<headerCells.length;i++){
			headerCells[i].tableSort = null;
			headerCells[i].onselectstart = null;			
			headerCells[i].gridList = null;
	    	headerCells[i].onNew = null;
	    	headerCells[i].onclick = null;
		}
		var tableModel = this.tableModel;
		var rows = tableModel.rows;
		for(var i=0;i<rows.length;i++){
		    var row = rows[i];
			var cells = row.cells;
			for(var j=0;j<cells.length;j++){
				var cell = cells[j];
				cell.onclick = null;
				cell.ondblclick = null;
				cell.onmouseover = null;
				cell.onmouseout = null;
		    	cell.onNew = null;
			}
		    row.gridListSelection = null;
			row.onclick = null;
			row.ondblclick = null;
			row.onmouseover = null;
			row.onmouseout = null;
		}
		
		if(this.upFixColumn.onclick)
			this.upFixColumn.onclick = null;
			
		if(this.tableSort){
			this.tableSort.tabFixColumn = null;
			this.tableSort.tableModel = null;
			this.tableSort.headerCells = null;
			
			this.tableSort = null;
		}
		
		if(this.gridListSelection){
			this.gridListSelection.gridList = null;	
			this.gridListSelection.tableModel = null;
			
			this.gridListSelection = null;
		}
			
		this.list = null;
		this.tableModel = null;
		this.tabDiv = null;
		this.tabFixColumn = null;
		this.upFixColumn = null;
		this.headerCells = null;
		
	}catch(e){}
}

GridList.prototype.titleDecode = function(title){
	if(!this.containscdata)
		return title;
	var titleConv=document.createElement("textarea");
	titleConv.innerHTML=title.replace(/</g,"&lt;").replace(/>/g,"&gt;");
  	return titleConv.value;
}

GridList.prototype.loopAllCell = function (headerCells,tableModel,onnewrow,onnewcell,selectionPolicy,
										   modality,fieldsmodality,modelPropertyNames){
	var reApice = new RegExp('\\"','g');
	var reBackslash = new RegExp('\\\\','g');
	var mpnArray = modelPropertyNames.split("|");
	var rows = tableModel.rows;
	for(var i=0;i<rows.length;i++){
	    var row = rows[i];
    	var props = "";
	    for(var k=0;k<mpnArray.length;k++){
	    	var propValue = row.getAttribute("pv"+k).replace(reBackslash,"\\\\").replace(reApice,'\\"');
			props += "try{row"+"."+mpnArray[k]+"=\""+propValue+"\";}catch(e){}";
	    }
	    if(props != "")
    		eval(props);
	    row.rowIndexInPage = row.getAttribute("rowIndexInPage");
	    row.absIndex = row.getAttribute("absIndex");
		row.isSelectable = true;
		row.selectionPolicy = selectionPolicy;
		row.modality = modality;
		row.fieldsmodality = fieldsmodality;

		row.disable = function(){
	    	var cells = row.cells;
			for(var j=0;j<cells.length;j++){
				var cell = cells[j];
				cell.onclick = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.onmouseover = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.onmouseout = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.className = "gridBodyCellDisabled";
			}
	    };

		if(onnewrow != ''){
	    	row.onNew = function(){eval(onnewrow);};
	    	row.onNew();
	    }
	    
		var cells = row.cells;
		for(var j=0;j<cells.length;j++){
			var cell = cells[j];
			cell.disable = function(){
				cell.onclick = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.onmouseover = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.onmouseout = function(event){ if(event) event.stopPropagation(); else window.event.cancelBubble = true; };
				cell.className = "gridBodyCellDisabled";
			};
			cell.editable = cell.getAttribute('editable');
			if(cell.editable == 'false' && cell.innerHTML != '&nbsp;' && cell.innerHTML != '')
				cell.title = this.titleDecode(cell.innerHTML);
			cell.row = row;
			cell.propertyName = headerCells[j].getAttribute("propertyName");
		    if(onnewcell != ''){
		    	cell.onNew = function(){eval(onnewcell);};
		    	cell.onNew(cell);
		    }
			cell.cellColor = cell.style.color;
			cell.cellBackgroundColor = cell.style.backgroundColor;
		}
		row.rowColor = row.style.color;
		row.rowBackgroundColor = row.style.backgroundColor;
		
		if(row.selectionPolicy == 'none')
			row.style.cursor='';
	}
}

GridList.prototype.clickRow = function (rowIndex) {
	if(rowIndex >= 0 && rowIndex < this.tableModel.rows.length) {
		var row = this.tableModel.rows[rowIndex];
		try{this.showRow(rowIndex);}catch(e){}
		row.onclick();
	}
}

GridList.prototype.selectRow = function (rowIndex) {
	if(rowIndex >= 0 && rowIndex < this.tableModel.rows.length) {
		var row = this.tableModel.rows[rowIndex];
		this.gridListSelection.selectRow(row,true);
		try{this.showRow(rowIndex);}catch(e){}
	}
}

GridList.prototype.showRow = function (rowIndex) {
	if(rowIndex >= 0 && rowIndex < this.tableModel.rows.length) {
		var row = this.tableModel.rows[rowIndex];
		var rowTop = row.offsetTop;
		var rowHeight = row.clientHeight;
		var viewArea = this.tabDiv;
		if(rowTop < viewArea.scrollTop){
			viewArea.scrollTop = rowTop;
		}else if(rowTop > viewArea.scrollTop+viewArea.clientHeight-rowHeight){
			viewArea.scrollTop = rowTop-viewArea.clientHeight+rowHeight;
		}
	}
}

GridList.prototype.unselectRows = function () {
	this.gridListSelection.unselectRows();
}


/* ************************************************************************************ */
/* Grid Selection Management */
/* ************************************************************************************ */
function GridListSelection(gridList,onclick,ondblclick) {

	this.CELL_COLOR = "#1A458F";
	this.CELL_BACKGROUNDCOLOR = "#FFFFFF";

	this.SELECTED_ROW_COLOR = "#DBDBDB";
	this.SELECTED_ROW_BACKGROUNDCOLOR = "#1A458F";

	this.OVERED_ROW_COLOR = "#1A458F";
	this.OVERED_ROW_BACKGROUNDCOLOR = "#DBDBDB";

	this.gridList = gridList;
	this.tableModel = gridList.tableModel;
	this.tabFixColumn = gridList.tabFixColumn;
	this.selectionPolicy = gridList.selectionPolicy;

	this.selectedRows = new Array();
	this.selectedRowsIndexMap = new Array();

	var rows = this.tableModel.rows;
	for(var i=0;i<rows.length;i++){
	    var row = rows[i];
	    row.gridListSelection = this;
	    if(this.selectionPolicy != 'none'){
	    	if(row.isSelectable){
				row.onclick = function(e) {
					if(!isRequestPending() && this.selectionPolicy != "none"){
						if (!e) 
							e = window.event;
						
						if(e)
							this.gridListSelection.selectRow(this,e.ctrlKey,e.shiftKey);
						else
							this.gridListSelection.selectRow(this,false,false);
						eval(onclick);
					}
				};
				row.ondblclick = function(e) {
					if(row.selectionPolicy != "none")
						eval(ondblclick);
				};
				row.onmouseover = function(e) {
					this.gridListSelection.overflyRow(this);
				};
				row.onmouseout = function(e) {
					this.gridListSelection.resetRow(this);
				};
	    	}
	    }
	}
}

GridListSelection.prototype.selectRow = function (row, ctrlKey, shiftKey) {
	if(row.selectionPolicy == 'none')
		return;
	
	if(this.selectionPolicy == 'single'){
		this.unselectRows();
		this.selectedRows = new Array();
		this.selectedRows.push(row);
		this.setSelectedRow(row, true);
		return;
	}

	if(!ctrlKey && !shiftKey) {
		this.unselectRows();
		this.selectedRows = new Array();
		this.selectedRows.push(row);
		this.setSelectedRow(row, true);		
	}else if(ctrlKey && !shiftKey) {
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
		}else {
			this.selectedRows.push(row);
			this.setSelectedRow(row, true);
		}
	}else{
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
				if(!ctrlKey) {
					this.unselectRows();
					this.selectedRows = new Array();
				}
				this.selectedRows.push(row);
				this.setSelectedRow(row, true);
			}else {
			    // findBackward
				if(!ctrlKey) {
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
		}else{
		    // findForward
			if(!ctrlKey) {
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

GridListSelection.prototype.setSelectedRow = function (row, isSelected) {
	row.selectedRows = this.selectedRows;
	row.selected = isSelected;

	if(isSelected) {
		for(var i=0;i<row.cells.length;i++)
			$(row.cells[i]).removeClass("gridOveredRow").addClass("gridSelectedRow");
		
		if(this.tabFixColumn != null){
			var counter = this.tabFixColumn.rows[row.rowIndexInPage].cells[0];
			$(counter).addClass("gridSelectedCounter");
		}
	}else{
		this.resetRow(row);
	}
}


GridListSelection.prototype.resetRow = function (row) {
	if(row.selected)
		return;
		
	for(var i=0;i<row.cells.length;i++)
		$(row.cells[i]).removeClass("gridSelectedRow gridOveredRow");
	
	if(this.tabFixColumn != null){
		var counter = this.tabFixColumn.rows[row.rowIndexInPage].cells[0];
		$(counter).removeClass("gridOveredCounter gridSelectedCounter");
	}
}

GridListSelection.prototype.unselectRows = function () {
	var length = this.selectedRows.length;
	var currentRow;
	for(var i=0; i<length; i++) {
		currentRow = this.selectedRows[i];
		currentRow.selected = false;
		this.resetRow(currentRow);
	}
}

GridListSelection.prototype.overflyRow = function (row) {

	if(isRequestPending() || row.selectionPolicy == 'none')
		return;
		
	if(row.selected) 
		return;

	for(var i=0;i<row.cells.length;i++)
		$(row.cells[i]).addClass("gridOveredRow");
	
	if(this.tabFixColumn != null){
		var counter = this.tabFixColumn.rows[row.rowIndexInPage].cells[0];
		$(counter).addClass("gridOveredCounter");
	}
}

/* ************************************************************************************ */
/* Grid Sorting Management */
/* ************************************************************************************ */
function GridSort(headerCells, tabFixColumn, tableModel) {
	this.UP_POINTING = String.fromCharCode(9650);
	this.DOWN_POINTING = String.fromCharCode(9660);

	this.originalHeaderText = new Array();
	this.tabFixColumn = tabFixColumn;
	this.tableModel = tableModel;
	this.headerCells = headerCells;
	
	for(i=0;i<headerCells.length;i++){
		this.originalHeaderText[i] = headerCells[i].innerHTML;
		headerCells[i].tableSort = this;
		headerCells[i].isDesc = null;
		if(headerCells[i].onclick == null || headerCells[i].onclick == ''){
			headerCells[i].onclick = function(){
										this.tableSort.sort(this);
										this.isDesc = !this.isDesc;
										for(i=0;i<this.tableSort.headerCells.length;i++)
											this.tableSort.headerCells[i].innerHTML = this.tableSort.originalHeaderText[i];
										if(this.getAttribute("propertyName") != "absIndex"){
											if(this.isDesc)
												this.innerHTML += " " + this.tableSort.UP_POINTING;
											else
												this.innerHTML += " " + this.tableSort.DOWN_POINTING;
										}
									 };
		}
	}

}

GridSort.prototype.sort = function(headerCell) {
	var rows = headerCell.tableSort.tableModel.rows;
	var counterRows = null;
	if(headerCell.tableSort.tabFixColumn != null)
		counterRows = headerCell.tableSort.tabFixColumn.rows;
	
	var sortingRows = new Array();
	for(var i=0;i<rows.length;i++){
		var value = rows[i].cells[headerCell.colIndex].innerHTML;
		if(rows[i].cells[headerCell.colIndex].sortValue)
			value = rows[i].cells[headerCell.colIndex].sortValue;
		if(headerCell.colIndex == 0)
			value = rows[i].cells[0].getAttribute("rowIndex");
		sortingRows[i] = [value,rows[i],(counterRows == null ? "" : counterRows[i])];
	}

	var sortedRows = new Array();
	var sortFnc = null;
	var type = headerCell.getAttribute("abstractType");
	if(headerCell.colIndex == 0)
		sortFnc = headerCell.tableSort.sortInteger;
	else if(type == 'DoubleType'){
		sortFnc = headerCell.tableSort.sortDouble;
	}else if(type == 'IntegerType')
		sortFnc = headerCell.tableSort.sortInteger;
	else if(type == 'DateType')
		sortFnc = headerCell.tableSort.sortDate;
	else if(type == 'TimestampType')
		sortFnc = headerCell.tableSort.sortTime;
	else
		sortFnc = headerCell.tableSort.sortString;
		
	if(headerCell.isDesc == null){
		sortedRows = sortingRows.sort(sortFnc);		
	}else{
		if(!headerCell.isDesc)
			sortedRows = sortingRows.sort(sortFnc);
		else
			sortedRows = sortingRows.reverse(sortFnc);
	}
	
	var tbody = headerCell.tableSort.tableModel.tBodies[0];
	var counterbody = null;
	if(counterRows != null)
		counterbody = headerCell.tableSort.tabFixColumn.tBodies[0];
    for(var i=0;i<sortedRows.length;i++){
	    tbody.appendChild(sortedRows[i][1]);
	    if(counterbody != null)
	    	counterbody.appendChild(sortedRows[i][2]);
    }
}

GridSort.prototype.sortString = function(v1,v2) {
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;") a="";
	if(b=="&nbsp;") b="";
	if(a == b) return 0;
	if(a < b) return -1;
	return 1;
}

GridSort.prototype.sortInteger = function(v1,v2) {
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;" || a=="") a="0";
	if(b=="&nbsp;" || b=="") b="0";
	try{a = new Number(a);}catch(e){a=new Number("0");}		
	try{b = new Number(b);}catch(e){b=new Number("0");}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
}

GridSort.prototype.sortDouble = function(v1,v2) {
	try{
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;" || a=="") a="0";
	if(b=="&nbsp;" || b=="") b="0";
	a = toDouble(a);
	b = toDouble(b);
	try{a = new Number(a);}catch(e){a = new Number("0");}		
	try{b = new Number(b);}catch(e){b = new Number("0");}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
	}catch(e){alert("a: ["+a+"] b: ["+b+"]");}
}

GridSort.prototype.sortDate = function(v1,v2) {
	var noDate = "01-01-1900";
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;" || a=="" || a.length != 10) a=noDate; 
	if(b=="&nbsp;" || b=="" || b.length != 10) b=noDate; 
	var ddA = parseInt(a.substring(0,2),10); var mmA = parseInt(a.substring(3,5),10)-1; aaA = parseInt(a.substring(6,10),10);
	var ddB = parseInt(b.substring(0,2),10); var mmB = parseInt(b.substring(3,5),10)-1; aaB = parseInt(b.substring(6,10),10);
	try{a = new Date(aaA,mmA,ddA);}catch(e){a = new Date(1900,0,1);}
	try{b = new Date(aaB,mmB,ddB);}catch(e){b = new Date(1900,0,1);}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
}

GridSort.prototype.sortTime = function(v1,v2) {
	var noTime="01-01-1900 00:00:00";
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;" || a=="" || a.length != 19) a=noTime;
	if(b=="&nbsp;" || b=="" || b.length != 19) b=noTime;
	var ddA = parseInt(a.substring(0,2),10);   var mmA = parseInt(a.substring(3,5),10)-1; aaA = parseInt(a.substring(6,10),10);
	var hhA = parseInt(a.substring(11,13),10); var miA = parseInt(a.substring(14,16),10); ssA = parseInt(a.substring(17,19),10);
	var ddB = parseInt(b.substring(0,2),10);   var mmB = parseInt(b.substring(3,5),10)-1; aaB = parseInt(b.substring(6,10),10);
	var hhB = parseInt(b.substring(11,13),10); var miB = parseInt(b.substring(14,16),10); ssB = parseInt(b.substring(17,19),10);
	try{a = new Date(aaA,mmA,ddA,hhA,miA,ssA);}catch(e){a = new Date(1900,0,1);}
	try{b = new Date(aaB,mmB,ddB,hhB,miB,ssB);}catch(e){b = new Date(1900,0,1);}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
}
