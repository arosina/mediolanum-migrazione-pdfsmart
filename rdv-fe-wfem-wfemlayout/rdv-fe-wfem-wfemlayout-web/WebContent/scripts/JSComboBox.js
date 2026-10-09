////////////////////////////////////////////////////////////////////////////////
/////////////////////        GUI ComboBox      ////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function JSOption(value, text, valid) {

	this.value = value;
	this.text = text;
	this.valid = valid;
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function JSOptions(jsComboBox){
	this.jsComboBox = jsComboBox;
	this.array = new Array();
}

JSOptions.prototype.item = function (index) {
	return this.array[index];
}

JSOptions.prototype.add = function (obj) {

	this.array[this.array.length] = obj;
	this.jsComboBox.addItem(obj);
}

JSOptions.prototype.remove = function (index) {
	this.array.splice(index, 1);
	this.jsComboBox.removeItem(index);
}

JSOptions.prototype.length = function () {
	return this.array.length;
}

////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function JSComboBox(combo, valueHolder, onSelectChangeScript) {

	var oThis = this;
	this.combo = combo;
	this.valueHolder = valueHolder;
	this.valueHolder.selectedIndex = -1;
	this.onSelectChangeScript = onSelectChangeScript;

	if(!combo) {
		var msg = "Exception: Undefined [combo] object!";
		alert(msg);
		throw msg;

	}
	if(!valueHolder) {
		var msg = "Exception: Undefined [valueHolder] object!";
		alert(msg);
		throw msg;
	}

	var options = this.valueHolder.options = new JSOptions(this);

	this.valueHolder.isCombo = true;

	this.valueHolder.setClassName = function(className) {
		if(combo.all.cField) combo.all.cField.className = combo.all.cButton.className = className;
	};

	this.valueHolder.setReadonly = function(isReadonly) {
		if(combo.all.cField) {
			combo.all.cField.readonly = isReadonly;
			combo.all.cButton.disabled = isReadonly;
		}
	};

	this.valueHolder.add = function(cod, desc, valid) {
		var newOption = new JSOption(cod, desc, valid);
		options.add(newOption);
	};

	this.valueHolder.remove = function(index) {
		options.remove(index);
	};

	this.valueHolder.length = function() {
		return options.length();
	};

	this.valueHolder.removeAll = function() {
		while(options.length() > 1) {
			options.remove(options.length()-1);
		}
	};

	this.valueHolder.focus = function() {
		if(combo.all.cField ) {
			combo.all.cField.focus();
		}
	};

	this.valueHolder.onchange = function() {
		if(onSelectChangeScript) eval(onSelectChangeScript);
	};

	this.valueHolder.setSelectedIndex = function(index) {
		var row = combo.all.cTable.rows(index);
		oThis.selectRow(row, true);
	};

	this.onSelectChangeScript = onSelectChangeScript;

	combo.all.cPanel.className = "JSComboBox_panel";
	combo.all.cTable.className = "JSComboBox_table";

	var oThis = this;

	if(combo.all.cButton) {

		combo.all.cButton.className = combo.all.cField.className;
		combo.all.cField.style.borderRight = "none";
		combo.all.cButton.style.backgroundColor = "gainsboro";
		combo.all.cButton.style.width = combo.all.cButton.height = 19;
		combo.all.cButton.style.borderColor = "dimgray";
		combo.all.cButton.style.borderTop = "solid 2px;";
		combo.all.cButton.style.borderLeft = "solid 1px;";


		combo.all.cField.onclick = function () {
			if(combo.all.cButton.disabled) return;
			oThis.click();
			combo.all.cButton.focus();
		};


		combo.all.cButton.ondblclick = function () { oThis.click(); };
		combo.all.cButton.onclick = function () { oThis.click(); };
		combo.all.cButton.onblur = function () { oThis.setVisible(false); };
		combo.all.cPanel.onfocus = function () {
			combo.all.cButton.focus();
			oThis.setVisible(true);
		};

		combo.all.cPanel.style.visibility = "hidden";
		combo.all.cPanel.style.zIndex = "100";
	}
	else {
		combo.all.cPanel.style.position = "relative";
		combo.all.cPanel.style.zIndex = "0";
	}
}

JSComboBox.prototype.addItem = function(newOption) {
	var combo = this.combo;
	var oThis = this;
	var options = this.valueHolder.options;

	var newRow;
	var newCell;

	newRow = combo.all.cTable.insertRow();
	newCell = newRow.insertCell();
	newCell.innerText = newOption.text;
	newCell.className = "JSComboBox_td";

	newRow.option = newOption;
	newRow.option.selected = false;

	if(this.valueHolder.value == newOption.value ) {
		this.selectRow(newRow);
	}
	if(newOption.valid==false) {
		newCell.className = "JSComboBox_item_notValid";
	}

	newRow.onmouseover = function() { newRow.className="JSComboBox_tr_hOver"; };

	newRow.onmouseout = function() { oThis.rebootRow(newRow); };

	newRow.onmousedown = function() { oThis.selectRow(newRow, true); };
}

JSComboBox.prototype.removeItem = function (index){
	var combo = this.combo;
	combo.all.cTable.deleteRow(index);

	if(this.valueHolder.length() > 0) {
		this.valueHolder.setSelectedIndex(0);
	}
	else  {
		this.valueHolder.selectedIndex = -1;
	}
}

JSComboBox.prototype.selectRow = function(row, isArmed) {

	if(this.currentSelectedRow && this.currentSelectedRow.rowIndex == row.rowIndex) return;

	var combo = this.combo;
	var valueHolder = this.valueHolder;
	valueHolder.value = row.option.value;

	if(combo.all.cField) {
		combo.all.cField.value = row.option.text;
		combo.all.cField.title = row.option.text;
		if(!row.option.valid) {
			combo.all.cField.className = "fieldHasError";
		}
		else {
			if(!combo.all.cButton.disabled)
				combo.all.cField.className = "inputField";
		}

	}

	row.className = "JSComboBox_tr_selection";

	row.option.selected = true;

	if(this.currentSelectedRow) {
		this.currentSelectedRow.option.selected = false;
		this.rebootRow(this.currentSelectedRow);
	}
	this.currentSelectedRow = row;
	valueHolder.selectedIndex = row.rowIndex;

	if(isArmed && this.onSelectChangeScript) valueHolder.onchange();
}

JSComboBox.prototype.rebootRow = function(row) {

	if(this.valueHolder.value == row.option.value) {
		row.className = "JSComboBox_tr_selection";
	}
	else if(row.option.className) {
			row.className = row.option.className;
		}
		else {
			row.className = "JSComboBox_tr_hOut";
		}
}

JSComboBox.prototype.setWidth = function(width) {

	var combo = this.combo;

	if(!combo.all.cField ) {
		combo.style.width = width;
		combo.all.cPanel.style.width = width;
	}
	else {
		combo.all.cField.style.width = width - 20;
	}
}

JSComboBox.prototype.setHeightByRows = function(rows) {

	var combo = this.combo;
	var height = rows * 20;

	combo.style.height = height;
	combo.all.cPanel.style.height = height;
}

JSComboBox.prototype.click = function () {
	this.setVisible(!this.visible);
}

JSComboBox.prototype.setVisible = function (isVisible) {
	this.visible = isVisible;
	if(this.visible) {
		this._show();
	}
	else {
		this._hide();
	}
}

JSComboBox.prototype._show = function() {

	var combo = this.combo;

	var cFieldWidth = combo.all.cField.getBoundingClientRect().right
					- combo.all.cField.getBoundingClientRect().left;
	var cButtonWidth = combo.all.cButton.getBoundingClientRect().right
					- combo.all.cButton.getBoundingClientRect().left;

	var cTableHeight = combo.all.cTable.getBoundingClientRect().bottom
					 - combo.all.cTable.getBoundingClientRect().top;

	cTableHeight = cTableHeight > 200 ? 200 : cTableHeight;

	combo.all.cPanel.style.visibility = "visible";
	combo.all.cPanel.style.width = cFieldWidth+cButtonWidth;
	combo.all.cPanel.style.height = cTableHeight+5;
	combo.all.cPanel.style.top = combo.all.cField.getBoundingClientRect().bottom;
	combo.all.cPanel.style.left = combo.all.cField.getBoundingClientRect().left;
}

JSComboBox.prototype._hide = function() {
	var combo = this.combo;
	combo.all.cPanel.style.visibility = "hidden";
}

function boldAdvise(src) {
	src.title="this is bold text";
	return;
}
