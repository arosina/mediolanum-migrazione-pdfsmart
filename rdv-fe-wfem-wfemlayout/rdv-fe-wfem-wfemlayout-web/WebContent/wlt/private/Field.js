/* ************************************************************************************ */
/* Field management */
/* ************************************************************************************ */
FieldIntf = function(){
	
	this.minus = "-";
	this.separator = ".";
	this.decimalChar = ",";
	this.alfaChars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\#$?%&'\"()*+,-./:;<=>?@[]^_`{}~";
	this.intChars  = "0123456789";
	this.stringChars = this.alfaChars + this.intChars; 
	this.textareaChars = this.stringChars + "\n\r";
	this.curOpenedHelper = null;
	
	this.dateParser = new DateParser("dd-MM-yyyy",false,"");
	this.hourParser = new DateParser("HH:mm:ss",false,"");
	this.hourNoSecParser = new DateParser("HH:mm",false,"");
	this.timeParser = new DateParser("dd-MM-yyyy HH:mm:ss",false,"");
	this.timeNoSecParser = new DateParser("dd-MM-yyyy HH:mm",false,"");
}

FieldIntf.prototype.initCodDescType = function(propName,userEvent){
	var prop = $('#'+propName);
	var tp = prop.attr('type') ? prop.attr('type').toLowerCase():"";
	if(tp == 'radio'){ // Radio input type
		prop = $('input[name='+propName+"]");
		prop.bind({
					click: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}else{ // Select input type
		prop.bind({
					change: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}
}

FieldIntf.prototype.initStringType = function(propName,userEvent){
	var field = document.getElementById(propName);
	if(field.getAttribute("enabcopy") == null){
		field.onfocus = function(event){
			try{
				if(!isFieldEnabled(this.id))
					this.blur();
			}catch(e){}
			return;
		}
	}
	var prop = $('#'+propName);
	var tp = prop.attr('type') ? prop.attr('type').toLowerCase():"";
	if(tp == 'radio'){ // Radio input type
		prop = $('input[name='+propName+"]");
		prop.bind({
					click: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}else if(prop.get(0).tagName.toLowerCase() == 'select'){
		prop.bind({
					change: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}else{
		var f = document.getElementById(propName);
		var chars = __fieldIntf.stringChars;
		var extrachars = prop.attr("extrachars") ? prop.attr("extrachars"):"";
		if(extrachars != null && extrachars.length > 0){
			chars += extrachars;
			if(prop.attr("upper") == null || prop.attr("upper") != 'false')
				chars += extrachars.toUpperCase();
		}
		var ctype = prop.attr("okchars");
		if(ctype){
			if(ctype.toLowerCase() == 'all')
				chars = null;
			else if(ctype.toLowerCase() == 'num')
				chars = __fieldIntf.intChars;
		}
		if(f.type == 'textarea' && chars != null)
			chars += "\n\r";
		__fieldIntf.filterChars(propName,chars);
	}
}

FieldIntf.prototype.toUpper = function(field){
	if(field.getAttribute("upper") == 'false')
		return;
	field.value = field.value.toUpperCase();
}

FieldIntf.prototype.filterChars = function(propName,chars){
	
	var prop = $('#'+propName);
	
	var upper = true;
	if(prop.attr("upper") == 'false')
		upper=false;

	if(upper)
		prop.addClass('upperField');
		
	prop.bind({
				keydown: function(event) {
									if(event.which == 69 && event.ctrlKey && event.altKey){ // Don't accept euro
										event.preventDefault();
										return;
									}
								},
								
				keypress: function(event) {
									if(event.which == 0 || event.which == 8 || event.ctrlKey)
										return;
										
									if(!isFieldEnabled(this.id)){
										event.preventDefault();
										return;
									}
									
									if(this.maxlength){
										if(parseInt(this.value.length,10) >= parseInt(this.maxlength,10)){
											event.preventDefault();
											return;
										}
									}
									
									if(chars != null){
										var c = String.fromCharCode(event.which);
										var idx = chars.indexOf(c);
										if(idx < 0){
											event.preventDefault();
											return;
										}
									}
									
									hideHelperAnchor(this.id);
									$(this).removeClass('fieldHasError');
									$(this).addClass('inputField');
									var upper = true;
									if(this.getAttribute("upper") == 'false')
										upper=false;
										
									if(upper)
										$(this).addClass('upperField');
						   		 },
						   		 
				focusout: function(event) {
									if(!isFieldEnabled(this.id))
										return;
					
									if(upper)
										this.value = this.value.toUpperCase();
										
									if(chars != null){
										var newVal = "";
										for(var i=0;i<this.value.length;i++){
											if(this.value.charCodeAt(i) == 8364) 	// Remove euro
												continue;
											var c = this.value.charAt(i);
											if(this.value.charCodeAt(i) == 160) 
												c = ' ';
											var idx = chars.indexOf(c);
											if(idx >= 0)
												newVal += ""+c;
										}
										this.value = newVal;
									}else{				
										var newVal = "";
										for(var i=0;i<this.value.length;i++){
											if(this.value.charCodeAt(i) == 8364) 	// Remove euro
												continue;
											var c = this.value.charAt(i);
											if(this.value.charCodeAt(i) == 160) 
												c = ' ';
											newVal += ""+c;
										}
										this.value = newVal;
									}													
								},
				
				blur: function(event) {
									if(!isFieldEnabled(this.id))
										return;
									
									if(upper)
										this.value = this.value.toUpperCase();

									if(chars != null){
										var newVal = "";
										for(var i=0;i<this.value.length;i++){
											if(this.value.charCodeAt(i) == 8364) 	// Remove euro
												continue;
											var c = this.value.charAt(i);
											if(this.value.charCodeAt(i) == 160) 
												c = ' ';
											var idx = chars.indexOf(c);
											if(idx >= 0)
												newVal += ""+c;
										}
										this.value = newVal;
									}else{				
										var newVal = "";
										for(var i=0;i<this.value.length;i++){
											if(this.value.charCodeAt(i) == 8364) 	// Remove euro
												continue;
											var c = this.value.charAt(i);
											if(this.value.charCodeAt(i) == 160) 
												c = ' ';
											newVal += ""+c;
										}
										this.value = newVal;
									}													
						   		 }
						   		 
	});
}

FieldIntf.prototype.initIntegerType = function(propName){
	var field = document.getElementById(propName);
	if(field.mask)
		return;
	if(field.getAttribute("enabcopy") == null){
		field.onfocus = function(event){
			try{
				if(!isFieldEnabled(this.id))
					this.blur();
			}catch(e){}
			return;
		}
	}
	field.onkeypress = function(event){
		 if (window.event) {
			 event = window.event;
	     }
		 var OTHER = 0; 
		 var BACK = 8; 
		 var CTRL_A = 97;
		 var CTRL_C = 99;
		 var CTRL_V = 118;
		 var CTRL_X = 120;
		 var CTRL_Z = 122;
		 var key = __getKeyEvent(event);
         if(key != OTHER && key != CTRL_A && key != BACK && key != CTRL_C && key != CTRL_V && key != CTRL_X && key != CTRL_Z && (key < 48 || key > 57)){
        	 preventDefault(event);
        	 return;
	     }
	}
	var scale = field.getAttribute("scale");
	var parser = new NumberParser(0, this.decimalChar, this.separator, false);
	var mask = new NumberMask(parser,propName);
	if(field.getAttribute('minus') != null && field.getAttribute('minus') == 'true')
		mask.allowNegative = true;
	else
		mask.allowNegative = false;
    mask.leftToRight = true;
	field.mask = mask;
	mask.keyPressFunction = function(e){
		hideHelperAnchor(this.id);
		$(this).removeClass('fieldHasError');
		$(this).addClass('inputField');
	}
	field.changeFnc = field.onchange;
	field.onchange = null;
	mask.changeFunction = function(e){
		if(field.changeFnc != null)
			field.changeFnc();
		return true;
	}
}

FieldIntf.prototype.initDoubleType = function(propName){
	var field = document.getElementById(propName);
	if(field.mask)
		return;
	if(field.getAttribute("enabcopy") == null){
		field.onfocus = function(event){
			try{
				if(!isFieldEnabled(this.id))
					this.blur();
			}catch(e){}
			return;
		}
	}
	field.onkeypress = function(event){
		 if (window.event) {
			 event = window.event;
	     }
		 var OTHER = 0; 
		 var BACK = 8; 
		 var CTRL_A = 97;
		 var CTRL_C = 99;
		 var CTRL_V = 118;
		 var CTRL_X = 120;
		 var CTRL_Z = 122;
		 var key = __getKeyEvent(event);
         if(key != OTHER && key != CTRL_A && key != BACK && key != CTRL_C && key != CTRL_V && key != CTRL_X && key != CTRL_Z && (key < 48 || key > 57)){
        	 preventDefault(event);
        	 return;
	     }
	}
	field.decimalChar = this.decimalChar;
	field.separator = this.separator;
	var numdig = field.getAttribute("numdig");
	var scale = field.getAttribute("scale");
	var parser = new NumberParser(scale, this.decimalChar, this.separator, true);
	var mask = numdig == null ? new NumberMask(parser,propName) : new NumberMask(parser,propName,numdig);
	if(field.getAttribute('minus') != null && field.getAttribute('minus') == 'true')
		mask.allowNegative = true;
	else
		mask.allowNegative = false;
    mask.leftToRight = true;
	field.mask = mask;
	mask.keyPressFunction = function(e){
		hideHelperAnchor(this.id);
		$(this).removeClass('fieldHasError');
		$(this).addClass('inputField');
	}
	field.changeFnc = field.onchange;
	field.onchange = null;
	mask.changeFunction = function(e){
		if(field.changeFnc != null)
			field.changeFnc();
		return true;
	}
	mask.blurFunction = function(e){
		if(this.decimalChar == null || this.decimalChar == "")
			return true;
		
		if(field.getAttribute('keepzerodec') != 'true'){
			var intPart = this.value.toString();
			var idx = intPart.indexOf(this.decimalChar);
			if(idx >= 0){
				var decPart = parseInt(intPart.substring(idx+1),10);		
				if(decPart == 0)
					this.value = intPart.substring(0,idx);
			}
		}
		return true;
	}
	
}

FieldIntf.prototype.initDateType = function(propName){
	var field = document.getElementById(propName);
	if(field.mask)
		return;
	if(field.getAttribute("enabcopy") == null){
		field.onfocus = function(event){
			try{
				if(!isFieldEnabled(this.id))
					this.blur();
			}catch(e){}
			return;
		}
	}
	var mask = new DateMask(this.dateParser,propName);
	field.mask = mask;	
	mask.keyPressFunction = function(e){
		hideHelperAnchor(this.id);
		$(this).removeClass('fieldHasError');
		$(this).addClass('inputField');
		$('#'+this.id+'Mask').removeClass('fieldHasError');
		$('#'+this.id+'Mask').addClass('inputField');
		return true;
	}
	field.changeFnc = field.onchange;
	field.onchange = null;
	mask.changeFunction = function(e){
		if(field.changeFnc != null)
			field.changeFnc();
		return true;
	}
	
	$('#'+propName+"Mask").bind({
		focus: function(event){
			$('#'+propName).focus();
		}
	});

	$('#'+propName).bind({
							focus: function(event){
								if(!isFieldEnabled(this.id))
									return;
								document.getElementById(this.id+'Mask').value='__-__-____';
							},
							blur: function(event){
								document.getElementById(this.id+'Mask').value='';
							}
						 });
							 
	this.initImgCal(propName);
}

FieldIntf.prototype.initTimestampType = function(propName){
	var field = document.getElementById(propName);
	if(field.mask)
		return;
	if(field.getAttribute("enabcopy") == null){
		field.onfocus = function(event){
			try{
				if(!isFieldEnabled(this.id))
					this.blur();
			}catch(e){}
			return;
		}
	}
	if(field.getAttribute("notime") == 'true') field.notime = true; else field.notime = false;
	if(field.getAttribute("nodate") == 'true') field.nodate = true; else field.nodate = false;
	if(field.getAttribute("noseconds") == 'true') field.noseconds = true; else field.noseconds = false;
		
	var mask = null;
	if(field.notime){
		 mask = new DateMask(this.dateParser,propName);
	}else if(field.nodate){
		if(field.noseconds)
			 mask = new DateMask(this.hourNoSecParser,propName);
		else
			 mask = new DateMask(this.hourParser,propName);
	}else if(field.noseconds){
		 mask = new DateMask(this.timeNoSecParser,propName);
	}else{
		 mask = new DateMask(this.timeParser,propName);
	}
	
	field.mask = mask;	
	mask.keyPressFunction = function(e){
		hideHelperAnchor(this.id);
		$(this).removeClass('fieldHasError');
		$(this).addClass('inputField');
		$('#'+this.id+'Mask').removeClass('fieldHasError');
		$('#'+this.id+'Mask').addClass('inputField');
		return true;
	}
	field.changeFnc = field.onchange;
	field.onchange = null;
	mask.changeFunction = function(e){
		if(field.changeFnc != null)
			field.changeFnc();
		return true;
	}
	
	$('#'+propName).bind({
							focus: function(event){
								if(!isFieldEnabled(this.id))
									return;
								var mask = '';
								if(field.notime){
									 mask = '__-__-____';
								}else if(field.nodate){
									if(field.noseconds)
										 mask = '__:__';
									else
										 mask = '__:__:__';
								}else if(field.noseconds){
									 mask = '__-__-____ __:__';
								}else{
									 mask = '__-__-____ __:__:__';
								}
								document.getElementById(this.id+'Mask').value=mask;
							},
							blur: function(event){
								document.getElementById(this.id+'Mask').value='';
							}
						 });
							 
	this.initImgCal(propName);
}

FieldIntf.prototype.initImgCal = function(propName){
	if(document.getElementById(propName+'ImgCal') == null)
		return;
	$('#'+propName+'ImgCal').bind({
									click: function(event){
										var propName = this.id.substring(0,(this.id.length-"ImgCal".length));
										var dtObj = $('#'+propName); 
										if(!dtObj.hasClass('hasDatepicker')){
											dtObj.datepicker({dateFormat:'dd-mm-yy',
															  showWeek: true, 
															  firstDay: 1,
														  	  showOn: 'button',
														  	  buttonImage: __retrieveWfemLayoutResourceUrl()+'/private/calendar/images/mokeOpenCalendar.gif',
														  	  buttonImageOnly: true,
														  	  beforeShow: function(input, inst) { try{onOpenCalendar(inst);}catch(e){}
														  	  									  try{
															  	  									  var field = document.getElementById(inst.id);
														  	  									  	  field.beforecalvalue = null;
															  	  									  if(field.getAttribute("ftype") == 'TimestampType'){ 
															  	  									  	field.beforecalvalue = field.value.toString();
															  	  									  	if(field.beforecalvalue.length == 0){
																  	  									  	if(field.notime || field.nodate){
																  	  									  		; // Nothing to do	
																  	  									  	}else if(field.noseconds){
																  	  									  		field.beforecalvalue = "xx-xx-xxxx 00:00"
																  	  									  	}else{
																  	  									  		field.beforecalvalue = "xx-xx-xxxx 00:00:00"
																  	  									  	}
															  	  									  	}
															  	  									  }
														  	  									  }catch(e){}
														  	  									},
														  	  onClose: function(dateText, inst) { var field = document.getElementById(inst.id);
														  	  									  try{
															  	  									  if(field.beforecalvalue != null){
															  	  									  	if(field.notime || field.nodate){
															  	  									  		; // Nothing to do	
															  	  									  	}else if(field.noseconds){
															  	  									  		if(field.beforecalvalue.length == 16)
															  	  									  			field.value = dateText+" "+field.beforecalvalue.substring(11);
															  	  									  	}else{
															  	  									  		if(field.beforecalvalue.length == 19)
															  	  									  			field.value = dateText+" "+field.beforecalvalue.substring(11);
															  	  									  	}
															  	  									  }
														  	  									  }catch(e){}
														  	  									  try{onCloseCalendar(inst);}catch(e){}
															  	  								  if(field.value != field.beforecalvalue){
															  	  									  if(field.changeFnc != null)
															  	  										  field.changeFnc();
	                                                                                              }														  	  									  
														  	  									}
														  	});
										}
										dtObj.datepicker('show');
										document.getElementById("ui-datepicker-div").style.zIndex=100;
									}
								 });
}

FieldIntf.prototype.initBooleanType = function(propName,userEvent){
	var prop = $('#'+propName);
	var tp = prop.attr('type') ? prop.attr('type').toLowerCase():"";
	if(tp == 'checkbox'){
		prop.bind({
					click: function(event) {
						var propName = this.id.substring(0,(this.id.length)-('Check'.length));
						if(!isFieldEnabled(propName))
							return;
						var obj = document.getElementById(propName);
						obj.value = this.checked ? "true" : "false";
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}else if(tp == 'radio'){
		prop = $('input[name='+propName+"]");
		prop.bind({
					click: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}else{ // Select input type
		prop.bind({
					change: function(event) {
						if(!isFieldEnabled(this.id))
							return;
						hideHelperAnchor(propName);
						if(userEvent != null && userEvent != '')
							eval(userEvent);
					}
		});
	}
}

FieldIntf.prototype.initFileType = function(propName){
	$("#"+propName).bind({
						keydown: function(event) {
									event.preventDefault();
								 }
						});
}

/* ************************************************************************************ */
/* Helper management */
/* ************************************************************************************ */
FieldIntf.prototype.showMessageHelper = function(anchorName,message){
	if(this.curOpenedHelper != null)
		this.curOpenedHelper.style.display = 'none';
		
	var objTab = document.getElementById("tabMsgHelper");
	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++){
		objTab.deleteRow(1);
	}
	var objRow   = objTab.insertRow(1);
	var objCell  = objRow.insertCell(0);
	objCell.className = 'helperRow';
	objCell.innerHTML = message;
		
	var objDiv = document.getElementById("divMsgHelper");
	this.positionHelper(anchorName,objDiv,"Msg");
}

FieldIntf.prototype.showMsgHelper = function(propName){
	var objDiv = document.getElementById("divMsgHelper");
	var objTab = document.getElementById("tabMsgHelper");
	this.showHelper(propName,objDiv,objTab,"Msg");
}

FieldIntf.prototype.showWarHelper = function(propName){
	var objDiv = document.getElementById("divWarHelper");
	var objTab = document.getElementById("tabWarHelper");
	this.showHelper(propName,objDiv,objTab,"");
}

FieldIntf.prototype.showErrHelper = function(propName){
	var objDiv = document.getElementById("divErrHelper");
	var objTab = document.getElementById("tabErrHelper");
	this.showHelper(propName,objDiv,objTab,"");
}

FieldIntf.prototype.showHelper = function(propName,objDiv,objTab,type){
	
	if(this.curOpenedHelper != null)
		this.curOpenedHelper.style.display = 'none';

	objDiv.propName = propName; // To save prop name for warning management (objDiv is the main DIV object)
	
	var anchorDiv = document.getElementById(propName+"HelperAnchor"+type);

	var messages = anchorDiv.messages;
	var totRow=objTab.rows.length;
	for(var x=0;x<totRow-1;x++)
		objTab.deleteRow(1);
	for(var i=0;i<messages.length;i++){
		var objRow   = objTab.insertRow(i+1);
		var objCell  = objRow.insertCell(0);
		objCell.innerHTML = messages[i];
		objCell.className = 'helperRow';
	}
	
	this.positionHelper(propName+"HelperAnchor"+type,objDiv,type);
}

FieldIntf.prototype.positionHelper = function(anchorName,objDiv,type){
	
	var pos = $('#'+anchorName).offset();
	var top = pos.top;
	var left = pos.left;
	
	if(type == "Msg"){
		top=top+22;
		left=left-10;
	}else{
		top=top+22;
		left=left-30;
	}
	
	objDiv.style.display='';
	if(document.body) { // ********* Force helper position to assuring visibility ********
		try{
			var objDivHeight = objDiv.clientHeight;
			if((top + objDivHeight) > (document.body.clientHeight+document.body.scrollTop)){
				top =  pos.top - objDivHeight - 15;
			}
			var objDivWidth = objDiv.clientWidth;
			if( (left + objDivWidth) > document.body.clientWidth+document.body.scrollLeft) {
				left = pos.left - objDivWidth; 
			}
		}catch(e){}
	}
	objDiv.style.display='none';
	
	objDiv.style.top=top;	
	objDiv.style.left=left;
	
	$('#'+objDiv.id).show('fast');
	
	this.curOpenedHelper = objDiv;	
}

FieldIntf.prototype.closeHelper = function(type){
	$("#div"+type+"Helper").hide('fast');
	this.curOpenedHelper = null;
}

FieldIntf.prototype.warningModify = function(){
	var propName = document.getElementById('divWarHelper').propName;
	var fieldObj = document.getElementById(propName);

	enableField(propName,true);
	hideHelperAnchor(propName);

	try{fieldObj.select();}catch(e){}
	try{fieldObj.focus();}catch(e){}
	
	this.removeSkippable(propName);
	this.closeHelper("War");
}

FieldIntf.prototype.warningIgnore = function(){
	var propName = document.getElementById('divWarHelper').propName;
	this.addSkippable(propName);
	this.closeHelper("War");
	hideHelperAnchor(propName);
}

FieldIntf.prototype.addSkippable = function(propName){
	var skipFields = document.getElementById('skippableFields');
	if(skipFields == null)
		return;
	
	var idx = skipFields.value.indexOf(propName);
	if(idx >= 0)
		return;
		
	skipFields.value = skipFields.value + propName + ",";
}

FieldIntf.prototype.removeSkippable = function(propName){
	var skipFields = document.getElementById('skippableFields');
	if(skipFields == null)
		return;

	var idx = skipFields.value.indexOf(propName);
	if(idx < 0)
		return;

	var s1 = skipFields.value.substr(0,idx);
	var s2 = skipFields.value.substr(idx+propName.length+1);
	skipFields.value = s1 + s2;
}

