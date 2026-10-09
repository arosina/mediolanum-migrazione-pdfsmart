////////////////////////////////////////////////////////////////////////////////
///////////////////////   GUI TabbedPane    ////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
function JSTabbedPane(tabbedPaneName, targetFrame, closeThreadTarget) {
	this.cmdPrefix = __retrieveGatewayUrl()+"call.wfem?wfemCmd=";
	this.cmdOnNewThreadPostfix = "OnNewThread";
	this.requestPending = false;
	
	this.name = "UNDEF";
	if(tabbedPaneName) {
		this.name = tabbedPaneName;
	}

	if(targetFrame) {
		this.targetFrame = targetFrame;
		SEMAPHORE.start();
	}
	
	if(closeThreadTarget) {
		this.closeThreadTarget = closeThreadTarget;
	}

	this.TAB_CLASS = "JSTabbedPane_tab";
	this.TAB_SELECTION_CLASS = "JSTabbedPane_tab_selection";
	this.TAB_DISABLE_CLASS = "JSTabbedPane_tab_disable";
	this.TAB_HASERRORS_CLASS = "JSTabbedPane_tab_hasErrors";
	this.TAB_HOVER_BORDER = "#1A458F";

	this.model = new Array();
	this.map = new Array();
	this.selectedIndex = -1;
	this.onTabChanged = null;

	this.tabbedPaneChildNodes = new Array();
	
	var _this = this;
	
	this.keyDownListenerList = new Array();

	document.onkeydown = function(){
	
		for(var i=0; i < _this.keyDownListenerList.length; i++){
			_this.keyDownListenerList[i].onkeydown();
		}

		if(event.keyCode == 39) {
			_this._selectRight();
		}
		else if(event.keyCode == 37) {
			_this._selectLeft();
		}
	};
	
}

JSTabbedPane.prototype.addKeyDownListener = function(keyDownListener) {

	var index = this.keyDownListenerList.length;
	this.keyDownListenerList[index] = keyDownListener;
}

JSTabbedPane.prototype.registerNode = function(tabRef, tabbedPane) {
	return this.registerNodeIndex(this.resolveRef(tabRef), tabbedPane);
}

JSTabbedPane.prototype.registerNodeIndex = function(tabIndex, tabbedPane) {
	if(this.tabbedPaneChildNodes[tabIndex] == null) {
		this.tabbedPaneChildNodes[tabIndex] = tabbedPane;

		LOG.info("JSTabbedPane ["+this.name+"]\n\n Node ["+tabbedPane.name+"] has been REGISTERED in Tab["+tabIndex+"]."); 
		return true;
	}
	LOG.info("JSTabbedPane ["+this.name+"]\n\n Node ["+tabbedPane.name+"] has JUST been REGISTERED in Tab["+tabIndex+"]."); 
	return false;
}

JSTabbedPane.prototype.reloadNode = function(tabRef, tabbedPane) {
	return this.reloadNodeIndex(this.resolveRef(tabRef), tabbedPane);
}

JSTabbedPane.prototype.reloadNodeIndex = function(tabIndex, tabbedPane) {
	var registeredNode = this.tabbedPaneChildNodes[tabIndex];

	var length = tabbedPane.model.length;
	for(var i=0; i<length; i++) {
		tabbedPane.model[i].reload(registeredNode.model[i]);	
	}
	
	LOG.info("JSTabbedPane ["+this.name+"]\n\n Node ["+tabbedPane.name+"] has been RELOADED by Tab["+tabIndex+"]."); 
}

JSTabbedPane.prototype.unload = function() {
	if(!this.closeThreadTarget) {
		LOG.warning("JSTabbedPane ["+this.name+"]\n\n [closeThreadTarget] not defined! TabbedPane cannot close any thread." ); 
		return;
	}

	var length = this.tabbedPaneChildNodes.length;
	for(var i=0; i<length; i++) {
		this._unloadTabbedPane(this.tabbedPaneChildNodes[i], this.closeThreadTarget);
		this.tabbedPaneChildNodes[i]=null;
	}
	this._unloadTabbedPane(this, this.closeThreadTarget);
}

JSTabbedPane.prototype.unloadNode = function(tabRef) {
	return this.unloadNodeIndex(this.resolveRef(tabRef));
}

JSTabbedPane.prototype.unloadNodeIndex = function(tabIndex) {
	this._unloadTabbedPane(this.tabbedPaneChildNodes[tabIndex], this.closeThreadTarget);
	this.tabbedPaneChildNodes[tabIndex] = null;
}

JSTabbedPane.prototype._unloadTabbedPane = function(tabbedPane, closeThreadTarget) {
	var tab;
	if(tabbedPane != null && tabbedPane.model != null) {
		for(var i=0; i<tabbedPane.model.length; i++) {
			tab = tabbedPane.model[i];
			this._unloadTab(tab, closeThreadTarget);
		}
	}
	else {
		LOG.info("JSTabbedPane: _unloadTabbedPane()\n\n expected JSTabbedPane instance is null.");
	}
}
JSTabbedPane.prototype._unloadTab = function(tab, closeThreadTarget) {
	if(tab.isJustExecutedOnNewThread) {
		doGetSubmit(false,
					tab.thread,
					null,
					closeThreadTarget,
					__retrieveGatewayUrl()+"call.wfem?wfemCmd=closeThread");

		LOG.debug("JSTabbedPane ["+this.name+"]\n\n Thread ["+ tab.thread + "] has been closed."); 

		tab.thread = null;
		tab.isJustExecutedOnNewThread = false;
	}
}

JSTabbedPane.prototype.isRequestPending = function() {
	return this.requestPending;
}

JSTabbedPane.prototype.setRequestPending = function(requestPending) {
	this.requestPending = requestPending;
}

JSTabbedPane.prototype.resolveRef = function(ref) {
	var index = -1;
	if( typeof(ref) == "number" ) {
		index = ref;
	}
	else {
		index = this.map[ref];
	}
	return index;
}


JSTabbedPane.prototype.setTabVisible = function(ref, visible) {
	var index =  this.resolveRef(ref);
	var tab = this.model[index];

	if(tab) {
		tab.setVisible(visible);
	}
}

JSTabbedPane.prototype.setTabEnabled = function(ref, enabled) {
	var index =  this.resolveRef(ref);
	var tab = this.model[index];
	
	if(tab) {
		tab.setEnabled(enabled);
	}
}

JSTabbedPane.prototype.setAllTabEnabled = function(enabled) {
	var tab;
	for(var i=0; i<this.model.length; i++) {
		tab = this.model[i];
		tab.setEnabled(enabled);
	}
}

JSTabbedPane.prototype.setAllTabVisible = function(visible) {
	var tab;
	for(var i=0; i<this.model.length; i++) {
		tab = this.model[i];
		tab.setVisible(visible);
	}
}

JSTabbedPane.prototype.addCmdTab = function(tabModel, command, isOnNewthread, cmdBehaviors, nextCmdForm) {
	if(this.targetFrame) {
		var tabCount = this.model.length;
		var newTab = new JSTabComponent(this, tabModel, command, tabCount, true, isOnNewthread, cmdBehaviors, nextCmdForm);
		this.model[tabCount] = newTab;
		this.map[tabModel.id] = tabCount;
	}
}

JSTabbedPane.prototype.addTab = function(tabModel, content) {
	var tabCount = this.model.length;
	var newTab = new JSTabComponent(this, tabModel, content, tabCount, false);
	this.model[tabCount] = newTab;
	this.map[tabModel.id] = tabCount;
	if(this.selectedIndex == -1) this.setSelectedIndex(tabCount);
}

JSTabbedPane.prototype.setSelectedIndex = function(newIndex) {

	var tabCount = this.model.length;
	if(tabCount == 0) return;
	if(!newIndex || newIndex < 0) newIndex = 0;
	else if(newIndex >= tabCount) newIndex = tabCount -1;
	this._selectTab(newIndex);
	this.selectedIndex = newIndex;
}

JSTabbedPane.prototype._selectRight = function() {

	var tabCount = this.model.length;
	if(this.selectedIndex == tabCount-1) return;
	if(!this.model[this.selectedIndex + 1].visible) return;
	this.model[this.selectedIndex + 1].click();
}
JSTabbedPane.prototype._selectLeft = function() {

	var tabCount = this.model.length;
	if(this.selectedIndex == 0) return;
	if(!this.model[this.selectedIndex - 1].visible) return;
	this.model[this.selectedIndex - 1].click();
}


JSTabbedPane.prototype.executeCommand = function(ref, form, slaveCaller, forcedBrowserInstance, cmdBehaviors) {

	var newIndex = this.resolveRef(ref);
	var selectedTab = this.model[newIndex];
	
	if(!selectedTab.enabled) {
		LOG.info("JSTabbedPane ["+this.name+"]\n\n Tab ["+ref+"] not enabled!"); 
		return;
	}

	if(!selectedTab.visible) {
		selectedTab.setVisible(true);
	}

	if(this.isRequestPending()) return;
	this.setRequestPending(true);


	if(slaveCaller) {
		this.currentSlaveCaller = slaveCaller;
		this.currentSlaveCaller.startTabRequestPending = function () {
			if(!this.tabRequestPending) {
				this.tabRequestPending = true;
			}
			else {
				return false;
			}
			var waitObj=this.document.all("waitObject");
			if(waitObj != null){
				waitObj.style.visibility = 'visible';
				return true;
			}
		}
		this.currentSlaveCaller.startTabRequestPending();
	}

	selectedTab.getTabModel().style.cursor = "wait";

	this.setSelectedIndex(newIndex);

	var postFix = "";
	var browserIstance = null;
	if(forcedBrowserInstance == null) {
		browserIstance = (selectedTab.isJustExecutedOnNewThread) ? selectedTab.thread : null;
		postFix = (selectedTab.isOnNewThread && !selectedTab.isJustExecutedOnNewThread)? this.cmdOnNewThreadPostfix : "";
		LOG.debug("JSTabbedPane ["+this.name+"]\n\n ExecuteCommand ["+ref+"] in BrowserIstance ["+ browserIstance +"]"); 
	}
	else {
		browserIstance = forcedBrowserInstance;
		LOG.debug("JSTabbedPane ["+this.name+"]\n\n ExecuteCommand ["+ref+"] in FORCED BrowserIstance ["+ browserIstance+"]"); 
	}
	var action = this.cmdPrefix + selectedTab.getContent() + postFix;


	doGetSubmit(false,
				browserIstance,
				form,
				this.targetFrame,
				action + (cmdBehaviors != null ? cmdBehaviors : "") );
				
	if( postFix == this.cmdOnNewThreadPostfix) {
		selectedTab.isJustExecutedOnNewThread = true;
	}
}

JSTabbedPane.prototype.commandExecuted = function() {


	var body = this.targetFrame.document.body;
	if(body.noCommandPage) return; 

	LOG.debug("JSTabbedPane ["+this.name+"]\n\n CommandExecuted."); 

	if(!this.isRequestPending()) return;
	this.setRequestPending(false);

	if(this.currentSlaveCaller) {

		this.currentSlaveCaller.stopTabRequestPending = function () {
			this.tabRequestPending = false;
			var waitObj=this.document.all("waitObject");
			if(waitObj != null){
				waitObj.style.visibility = 'hidden';
				return true;
			}
		}
		this.currentSlaveCaller.stopTabRequestPending();


		this.currentSlaveCaller = null;
	}
	
	var selectedTab = this.model[this.selectedIndex];
	selectedTab.getTabModel().style.cursor = "default";

	var targetForms = this.targetFrame.document.forms;
	var targetForm;
	var hasBrowserInstance = false;
	if(targetForms) {	
		for(var i=0; i<targetForms.length; i++) {
			targetForm = targetForms[i];
			if(targetForm.BrowserInstance) {
				selectedTab.thread = targetForm.BrowserInstance.value;
				LOG.debug("JSTabbedPane ["+this.name+"]\n\n Assigned Thread > ["+selectedTab.thread+"]"); 
				hasBrowserInstance = true;
				break;
			}
		}
	}
	if(!hasBrowserInstance) 
		LOG.debug("JSTabbedPane ["+this.name+"]\n\n [BrowserInstance] parameter not found in any forms."); 
}


JSTabbedPane.prototype._selectTab = function(newIndex) {
	var oldIndex = this.selectedIndex;
	if(oldIndex > -1) {
		this.model[oldIndex].moveDown();
	}
	this.model[newIndex].moveUp();
	this.fireTabChangedEvent(oldIndex, newIndex);
}

JSTabbedPane.prototype.getSelectedTab = function() {
	return this.model[this.selectedIndex];
}

JSTabbedPane.prototype.fireTabChangedEvent = function(oldTabIndex, newTabIndex) {
	if(this.onTabChanged != null)
		eval(this.onTabChanged);
}

JSTabbedPane.prototype.setHasCommandErrors = function(ref, hasErrors) {
	var newIndex = this.resolveRef(ref);
	var selectedTab = this.model[newIndex];
	selectedTab.setHasErrors(hasErrors);
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function JSTabComponent(tabbedPane, tabModel, content, index, isCmdContent, isOnNewThread, cmdBehaviors, nextCmdForm) {

	var oThis = this;

	this.index = index;
	this.enabled = true;
	this.selected = false;
	this.visible = true;
	this.tabbedPane = tabbedPane;
	
	this.hasErrors = false;

	this.setTabModel(tabModel);

	this.content = content;
	this.isCmdContent = isCmdContent;
	
	this.isOnNewThread = isOnNewThread;
	this.cmdBehaviors = cmdBehaviors;
	this.nextCmdForm = nextCmdForm;
	
	this.isJustExecutedOnNewThread = false;
	this.thread = null;

	this.getTabModel().className = tabbedPane.TAB_CLASS;
	this.getTabModel().onselectstart = function() { event.returnValue = false; };
	this.tabbedPane = tabbedPane;


	this.getTabModel().onmouseover = function() {
		if(!oThis.enabled || this.hasFocus) return;
		this.style.borderBottomColor = oThis.tabbedPane.TAB_HOVER_BORDER;
		this.style.cursor = "pointer";
	};
	
	this.getTabModel().onmouseout = function() {
		if(!oThis.enabled) return;
		this.style.borderBottomColor = this.style.borderColor;
		this.style.cursor = "default";
	};

	this.moveDown();
	
	this.click = function () { oThis.getTabModel().onclick(true); };

	this.getTabModel().onclick = function () {

		if(!oThis.enabled || oThis.index == tabbedPane.selectedIndex ) return;


		if(oThis.isCmdContent==true) {
			var forceBrowserInstance = false;
			var prevTab = tabbedPane.getSelectedTab();
			
			if(!this.isOnNewThread && prevTab!=null && prevTab.isOnNewThread) {
				forceBrowserInstance = true;
			}
//********************		
			var executionCommand = 
				new ExecutionCommand(index, forceBrowserInstance, prevTab, oThis.cmdBehaviors, tabbedPane, prevTab.nextCmdForm);
			SEMAPHORE.addCommand(executionCommand);
//********************		
		}
		else {
			tabbedPane.setSelectedIndex(index);
		}
	};
}

JSTabComponent.prototype.setHasErrors = function(hasErrors){
	this.hasErrors = hasErrors;
	if(this.enabled && !this.selected) {
		this.getTabModel().className = this.hasErrors ? this.tabbedPane.TAB_HASERRORS_CLASS : this.tabbedPane.TAB_CLASS;
	}
}

JSTabComponent.prototype.getId = function(){
	return this.getTabModel().id;
}

JSTabComponent.prototype.reload = function(src) {

	this.isOnNewThread = src.isOnNewThread;
	this.isJustExecutedOnNewThread = src.isJustExecutedOnNewThread;
	this.thread = src.thread;
}

JSTabComponent.prototype.setVisible = function(visible) {


	this.visible = visible;

	if(visible) {
		this.getTabModel().style.display = "inline";
	}
	else {
		this.getTabModel().style.display = "none";
	}
}


JSTabComponent.prototype.setEnabled = function(enabled) {

	if(this.selected) {

		if(this.isCmdContent) {
			var targetBody = this.tabbedPane.targetFrame.document.body;
			if(targetBody) { 
				targetBody.disabled = !enabled;
			}			
		}
		return;
	}

	this.enabled = enabled;
	if(enabled) {
	//	this.getTabModel().style.borderBottomColor = this.tabbedPane.TAB_BORDER;
		this.getTabModel().className = this.tabbedPane.TAB_CLASS;
	}
	else {
	//	this.getTabModel().style.borderBottomColor = this.tabbedPane.TAB_DISABLE_BORDER;
		this.getTabModel().className = this.tabbedPane.TAB_DISABLE_CLASS;
	}
}

JSTabComponent.prototype.moveUp = function() {

	this.selected = true;
	//this.getTabModel().style.borderBottomColor = this.tabbedPane.TAB_HOVER_BORDER;
	this.getTabModel().className = this.tabbedPane.TAB_SELECTION_CLASS;
	this.getTabModel().hasFocus = true;

	if(!this.isCmdContent) {
		this.getContent().style.display = "inline";
	}
}

JSTabComponent.prototype.moveDown = function() {

	this.selected = false;
	
//**********
	this.getTabModel().className = this.hasErrors ? this.tabbedPane.TAB_HASERRORS_CLASS : this.tabbedPane.TAB_CLASS;
//**********

	if(!this.isCmdContent) {
		this.getContent().style.display = "none";
	}
	this.getTabModel().hasFocus = false;
}

JSTabComponent.prototype.getTabModel = function() {
	return this.tabModel;
}
JSTabComponent.prototype.setTabModel = function(tabModel) {
	this.tabModel = tabModel;
}

JSTabComponent.prototype.getContent = function() {
	return this.content;
}
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////

function ExecutionCommand(ref, forceBrowserInstance, prevTab, cmdBehaviors, tabbedPane, nextCmdForm) {
	this.ref = ref;
	this.forceBrowserInstance = forceBrowserInstance;
	this.prevTab = prevTab;
	this.cmdBehaviors = cmdBehaviors;
	this.tabbedPane = tabbedPane;
	this.nextCmdForm = nextCmdForm;
}

ExecutionCommand.prototype.execute = function() {

	if(this.tabbedPane.targetFrame.document.readyState == 'complete' && !this.tabbedPane.targetFrame.isRequestPending()) {
		this.tabbedPane.executeCommand(this.ref, eval(this.nextCmdForm), null, (this.forceBrowserInstance ? this.prevTab.thread : null), this.cmdBehaviors);
		return true;
	}
	return false;
}
