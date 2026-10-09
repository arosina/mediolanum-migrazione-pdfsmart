function gridListManageOnScroll(table,propName){
	var list = document.all(propName);
	list.all("headerDiv").scrollLeft=table.scrollLeft;
	if(list.all("divBodyFixColumn")){
		list.all("divBodyFixColumn").scrollTop=table.scrollTop;
	}
}
var pdfCount = 0;
function openGridPdf(title,formname,listPropName,browserInstance){
	if(title == "")
		title = "Acrobat reader";
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=openGridPdf&listPropertyName="+listPropName+"&BrowserInstance="+browserInstance;
	var openWindow = window.open("", ""+pdfCount++, "titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10,width=350,height=350");
	openWindow.document.write("<title>"+title+"</title>")
	openWindow.document.write("<body style='margin:0; padding:0;'>")
	openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>")
	openWindow.document.write("</body>")
	openWindow.document.close();	
}
var calcCount = 0;
function openGridCalc(title,formname,listPropName,browserInstance){
	if(title == "")
		title = "Grid";
	var url = __retrieveGatewayUrl()+"call.wfem?wfemCmd=openGridCalc&listPropertyName="+listPropName+"&BrowserInstance="+browserInstance;
	var openWindow = window.open("", ""+calcCount++, "titlebar=yes,scrollbars=yes,resizable=yes,top=10,left=10,width=350,height=350");
	openWindow.document.write("<title>"+title+"</title>")
	openWindow.document.write("<body style='margin:0; padding:0;'>")
	openWindow.document.write("<iframe style='width=100%; height:100%' src='"+url+"'/>")
	openWindow.document.write("</body>")
	openWindow.document.close();	
}
function firstPage(formname,listPropName){
	submitGtridPageCommand(formname,'firstPage',listPropName);
}
function lastPage(formname,listPropName){
	submitGtridPageCommand(formname,'lastPage',listPropName);
}
function nextPage(formname,listPropName){
	submitGtridPageCommand(formname,'nextPage',listPropName);
}
function previousPage(formname,listPropName){
	submitGtridPageCommand(formname,'previousPage',listPropName);
}
function gotoPage(formname,idx,listPropName){
	submitGtridPageCommand(formname,'gotoPage',listPropName,idx);
}
function submitGtridPageCommand(formname,command,listPropName,idx){
	if(isRequestPending())
		return;
				
	startRequest();
	try{
		eval("document."+listPropName+"Grid.unload();");
	}catch(e){}
	
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
			if(document.getElementById(listPropName+"GridHiddenSubmitContainer"))
				wfemHiddenSubmit(form,listPropName+"GridHiddenSubmitContainer");
			else
				form.submit();
		}
		form.wfemCmd.value = savcmd;
	}catch(e){
		LOG.error(command+': ' + e.message);
		stopRequest();
	} 	
}

////////////////////////////////////////////////////////////////////////////////
// GridList
////////////////////////////////////////////////////////////////////////////////
function GridList(propName, selectionPolicy, sortable, onclick, ondblclick, onnewrow, onnewcell, 
				  modality, fieldsmodality, onnewheader, containscdata, isHiddenSubmit){
	try{
		this.SCROLLBAR_WIDTH = 18;

		this.isHiddenSubmit = isHiddenSubmit;
	
		var list = document.getElementById(propName);
		var tableModel = list.all("tab");
		var tabDiv = list.all("tabDiv");
		var divBodyFixColumn = list.all("divBodyFixColumn");
		var tabFixColumn = list.all("tabFixColumn");
		var downFixColumn = list.all("downFixColumn");
		var upFixColumn = list.all("upFixColumn");
		var bodyFixColumnZero = list.all("bodyFixColumn0");
		var headerCells = list.all("headerTab").rows[0].cells;	
	
		this.list = list;
		this.tableModel = tableModel;
		this.tabDiv = tabDiv;
		this.divBodyFixColumn = divBodyFixColumn;
		this.tabFixColumn = tabFixColumn;
		this.downFixColumn = downFixColumn;
		this.upFixColumn = upFixColumn;
		this.bodyFixColumnZero = bodyFixColumnZero;
		this.headerCells = headerCells;
		
		this.modality = modality;
		this.fieldsmodality = fieldsmodality;
		this.containscdata = containscdata;		
	
		for(i=0;i<headerCells.length;i++){
			headerCells[i].onselectstart = function() { event.returnValue = false; };			
			headerCells[i].colIndex = i;
			if(sortable == 'true')
				headerCells[i].style.cursor = 'pointer';
			headerCells[i].gridList = this;
			if(onnewheader != ''){
		    	headerCells[i].onNew = function(){eval(onnewheader);};
		    	headerCells[i].onNew();
			}
		}
		
		if(sortable == 'true'){
			this.upFixColumn.style.cursor = 'pointer';
			this.upFixColumn.onclick = function(){headerCells[0].isDesc=false;headerCells[0].onclick();};
			this.tableSort = new GridSort(headerCells,tabFixColumn,tableModel);
		}	
	
		this.loopAllCell(headerCells,tableModel,onnewrow,onnewcell,selectionPolicy,this.modality,this.fieldsmodality);
	
		if(selectionPolicy == 'multi' || selectionPolicy == 'single')
			this.gridListSelection = new GridListSelection(this,tableModel,selectionPolicy,onclick,ondblclick);
				
	} catch (e) {
		LOG.debug("GridList ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
	}
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
		this.divBodyFixColumn = null;
		this.tabFixColumn = null;
		this.downFixColumn = null;
		this.upFixColumn = null;
		this.bodyFixColumnZero = null;
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

GridList.prototype.loopAllCell = function (headerCells,table,onnewrow,onnewcell,selectionPolicy,
										   modality,fieldsmodality){
	var rows = table.rows;
	for(i=0;i<rows.length;i++){
	    var row = rows[i];
		row.isSelectable = true;
		row.selectionPolicy = selectionPolicy;
		row.modality = modality;
		row.fieldsmodality = fieldsmodality;
	    if(onnewrow != ''){
	    	row.onNew = function(){eval(onnewrow);};
	    	row.onNew();
	    }
	    
		var cells = row.cells;
		for(j=0;j<cells.length;j++){
			var cell = cells[j];
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

GridList.prototype.clickRow = function (rowIndex,event) {
	if(rowIndex >= 0 && rowIndex < this.tableModel.rows.length) {
		var row = this.tableModel.rows[rowIndex];
		try{this.showRow(rowIndex);}catch(e){}
		row.click();
	}
}

GridList.prototype.selectRow = function (rowIndex,event) {
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
////////////////////////////////////////////////////////////////////////////////
// End of GridList
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// GridListSelection
////////////////////////////////////////////////////////////////////////////////
function GridListSelection(gridList,tableModel,selectionPolicy,onclick,ondblclick) {

	this.CELL_COLOR = "#1A458F";
	this.CELL_BACKGROUNDCOLOR = "#FFFFFF";

	this.SELECTED_ROW_COLOR = "#DBDBDB";
	this.SELECTED_ROW_BACKGROUNDCOLOR = "#1A458F";

	this.OVERED_ROW_COLOR = "#1A458F";
	this.OVERED_ROW_BACKGROUNDCOLOR = "#DBDBDB";

	this.gridList = gridList;
	
	this.tableModel = tableModel;
	this.selectionPolicy = selectionPolicy;

	this.selectedRows = new Array();
	this.selectedRowsIndexMap = new Array();

	var rows = tableModel.rows;
	for(i=0;i<rows.length;i++){
	    var row = rows[i];
	    row.gridListSelection = this;
	    if(selectionPolicy != 'none'){
	    	if(row.isSelectable){
				row.onclick = function(e) {
					if(!isRequestPending() && this.selectionPolicy != "none"){
						var eventCross =  e || event;
						this.gridListSelection.selectRow(this,eventCross);
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

GridListSelection.prototype.selectRow = function (row, event) {
	if(row.selectionPolicy == 'none')
		return;
	
	if(this.selectionPolicy == 'single'){
		this.unselectRows();
		this.selectedRows = new Array();
		this.selectedRows.push(row);
		this.setSelectedRow(row, true);
		return;
	}

	if(!event || (!event.ctrlKey && !event.shiftKey)) {
		this.unselectRows();
		this.selectedRows = new Array();
		this.selectedRows.push(row);
		this.setSelectedRow(row, true);		
	}else if(event.ctrlKey && !event.shiftKey) {
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
				if(!event.ctrlKey) {
					this.unselectRows();
					this.selectedRows = new Array();
				}
				this.selectedRows.push(row);
				this.setSelectedRow(row, true);
			}else {
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
		}else{
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

GridListSelection.prototype.setSelectedRow = function (row, isSelected) {
	row.selectedRows = this.selectedRows;
	row.selected = isSelected;

	if(isSelected) {
		for(var i=0;i<row.cells.length;i++){
			row.cells[i].style.color = this.SELECTED_ROW_COLOR;
			row.cells[i].style.backgroundColor = this.SELECTED_ROW_BACKGROUNDCOLOR;
		}
		var counter = this.gridList.list.all("bodyFixColumn"+row.getAttribute("absIndex"));
		counter.style.textDecoration = 'underline';
		counter.style.fontWeight = 'bold';
	}else{
		this.resetRow(row);
	}
}


GridListSelection.prototype.resetRow = function (row) {
	if(row.selected) {
		row.style.color = this.SELECTED_ROW_COLOR;
		row.style.backgroundColor = this.SELECTED_ROW_BACKGROUNDCOLOR;
		for(var i=0;i<row.cells.length;i++){
			row.cells[i].style.color = this.SELECTED_ROW_COLOR;
			row.cells[i].style.backgroundColor = this.SELECTED_ROW_BACKGROUNDCOLOR;
		}
	}else{
		for(var i=0;i<row.cells.length;i++){
			if(row.cells[i].cellColor)
				row.cells[i].style.color = row.cells[i].cellColor;
			else if(row.rowColor)
				row.cells[i].style.color = row.cells[i].rowColor;
			else
				row.cells[i].style.color = this.CELL_COLOR;
				
			if(row.cells[i].cellBackgroundColor)
				row.cells[i].style.backgroundColor = row.cells[i].cellBackgroundColor;
			else if(row.rowBackgroundColor)
				row.cells[i].style.backgroundColor = row.rowBackgroundColor;
			else
				row.cells[i].style.backgroundColor = this.CELL_BACKGROUNDCOLOR;
		}

		var counter = this.gridList.list.all("bodyFixColumn"+row.getAttribute("absIndex"));
		counter.style.textDecoration = '';
		counter.style.fontWeight = '';
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
		
	row.style.color = this.OVERED_ROW_COLOR;
	row.style.backgroundColor = this.OVERED_ROW_BACKGROUNDCOLOR;
	for(var i=0;i<row.cells.length;i++){
		row.cells[i].style.color = this.OVERED_ROW_COLOR;
		row.cells[i].style.backgroundColor = this.OVERED_ROW_BACKGROUNDCOLOR;
	}
	var counter = this.gridList.list.all("bodyFixColumn"+row.getAttribute("absIndex"));
	counter.style.textDecoration = '';
	counter.style.fontWeight = 'bold';
}
////////////////////////////////////////////////////////////////////////////////
// End of GridListSelection
////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////
// GridSort
////////////////////////////////////////////////////////////////////////////////
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
										if(this.isDesc)
											this.innerHTML += " " + this.tableSort.UP_POINTING;
										else
											this.innerHTML += " " + this.tableSort.DOWN_POINTING;
									 };
		}
	}

}

GridSort.prototype.sort = function(headerCell) {
	var rows = headerCell.tableSort.tableModel.rows;
	var counterRows = headerCell.tableSort.tabFixColumn.rows;
	
	var sortingRows = new Array();
	for(var i=0;i<rows.length;i++)
		sortingRows[i] = [rows[i].cells[headerCell.colIndex].innerHTML,rows[i],counterRows[i]];

	var sortedRows = new Array();
	var sortFnc = null;
	if(headerCell.colIndex == 0)
		sortFnc = headerCell.tableSort.sortInteger;
	else if(headerCell.abstractType == 'DoubleType')
		sortFnc = headerCell.tableSort.sortDouble;
	else if(headerCell.abstractType == 'IntegerType')
		sortFnc = headerCell.tableSort.sortInteger;
	else if(headerCell.abstractType == 'DateType')
		sortFnc = headerCell.tableSort.sortDate;
	else if(headerCell.abstractType == 'TimestampType')
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
	var counterbody = headerCell.tableSort.tabFixColumn.tBodies[0];
    for(var i=0;i<sortedRows.length;i++){
	    tbody.appendChild(sortedRows[i][1]);
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
	a = formatIT2UK(a);
	b = formatIT2UK(b);
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
	var ddA = parseInt(a.substring(0,2)); var mmA = parseInt(a.substring(3,5))-1; aaA = parseInt(a.substring(6,10));
	var ddB = parseInt(b.substring(0,2)); var mmB = parseInt(b.substring(3,5))-1; aaB = parseInt(b.substring(6,10));
	try{a = new Date(aaA,mmA,ddA).getTime();}catch(e){a = new Date(1900,0,1);}
	try{b = new Date(aaB,mmB,ddB).getTime();}catch(e){b = new Date(1900,0,1);}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
}

GridSort.prototype.sortTime = function(v1,v2) {
	var noTime="01-01-1900 00:00:00";
	var a=v1[0].toString(); var b=v2[0].toString();	a=a.replace(/^\s+|\s+$/g, ''); b=b.replace(/^\s+|\s+$/g, '');
	if(a=="&nbsp;" || a=="" || a.length != 19) a=noTime;
	if(b=="&nbsp;" || b=="" || b.length != 19) b=noTime;
	var ddA = parseInt(a.substring(0,2));   var mmA = parseInt(a.substring(3,5))-1; aaA = parseInt(a.substring(6,10));
	var hhA = parseInt(a.substring(11,13)); var miA = parseInt(a.substring(14,16)); ssA = parseInt(a.substring(17,19));
	var ddB = parseInt(b.substring(0,2));   var mmB = parseInt(b.substring(3,5))-1; aaB = parseInt(b.substring(6,10));
	var hhB = parseInt(b.substring(11,13)); var miB = parseInt(b.substring(14,16)); ssB = parseInt(b.substring(17,19));
	try{a = new Date(aaA,mmA,ddA,hhA,miA,ssA).getTime();}catch(e){a = new Date(1900,0,1);}
	try{b = new Date(aaB,mmB,ddB,hhB,miB,ssB).getTime();}catch(e){b = new Date(1900,0,1);}
    if(a == b) return 0;
    if(a < b) return -1;
    return 1;
}
////////////////////////////////////////////////////////////////////////////////
// End of GridSort
////////////////////////////////////////////////////////////////////////////////
