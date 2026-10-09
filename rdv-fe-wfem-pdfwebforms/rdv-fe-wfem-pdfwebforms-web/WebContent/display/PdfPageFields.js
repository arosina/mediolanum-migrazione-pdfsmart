if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(obj, start) {
         for (var i = (start || 0), j = this.length; i < j; i++) {
             if (this[i] === obj) { return i; }
         }
         return -1;
    }
}

String.prototype.replaceAt = function(index, replacement) {
    return this.substr(0, index) + replacement + this.substr(index + replacement.length);
}

PdfPageFields = function(){
	this.separator = ".";
	this.decimalChar = ",";
	this.curDatepickerFieldName = null;
	this.dateParser = new DateParser("dd-MM-yyyy",false,"");
	this.onPageLoad = false;
}

PdfPageFields.prototype.setFieldValue = function(fieldName, value){
	
	var jqobj = $("#"+fieldName);

	if(typeof(value) != "string"){
		var datatype = jqobj.attr("datatype");
		if(datatype == 'BooleanType'){
			value = ""+value;
		}else if(datatype == 'IntegerType'){
			value = ""+value;
		}else if(datatype == 'DoubleType'){
			var scale = jqobj.attr("scale");
			var negative = false;
			if(value < 0){
				negative = true;
				value = value*-1;
			}
			var dv = ""+value;
			var idx = dv.lastIndexOf(".");
			var si = dv;
			var di = "";
			if(idx >= 0){
				si = dv.substring(0,idx);
				di = dv.substring(idx+1,idx+1+parseInt(scale,10));
			}
			
			var ssi = addSeparator(si,pdfPageFields.separator);
			for(var i=di.length;i<scale;i++)
				di += "0"
			dv = ssi+pdfPageFields.decimalChar+di;
			if(negative)
				value = "-"+dv;
			else
				value = dv;
		}else if(datatype == 'DateType'){
			var gg = value.getDate(); 		if(gg < 10) gg = "0"+gg;
			var mm = value.getMonth()+1;	if(mm < 10) mm = "0"+mm;
			var aa = value.getFullYear();
			value = gg+"-"+mm+"-"+aa;
		}
	}
	
	var htmltype = jqobj.attr("htmltype");
	var maxlength = 0;
	var maxlengthAttr = jqobj.attr("maxlength");
	if(maxlengthAttr != null && maxlengthAttr != "")
		maxlength = parseInt(maxlengthAttr,10);
	if(maxlength > 0 && value.length > maxlength)
		value = value.substring(0,maxlength); 
	
	if(htmltype == 'radiobutton'){
		$("div[name='"+fieldName+"Radio']").children("img").css("visibility","hidden");
		if($("div[name='"+fieldName+"Cont']").css("visibility") != "hidden")
			$("div[name='"+fieldName+"Radio'][value='"+value+"']").children("img").css("visibility","visible");
		jqobj.val(value);
	}else if(htmltype == 'checkbox'){
		if(value == "true"){
			if($("div[name='"+fieldName+"Cont']").css("visibility") != "hidden")
				$("div[name='"+fieldName+"Check']").children("img").css("visibility","visible");
			jqobj.val("true");
		}else{
			$("div[name='"+fieldName+"Check']").children("img").css("visibility","hidden");
			jqobj.val("false");
		}
	}else if(htmltype == 'text'){
		$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").val(value);
	}else if(htmltype == 'textarea'){
		$("#"+fieldName+",textarea[hasClone='true'][fieldName='"+fieldName+"']").val(value);
	}else if(htmltype == 'combo'){
		$("#"+fieldName+",select[fieldName='"+fieldName+"']").val(value);
	}else{
		$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").val(value);
	}
	if(!this.onPageLoad)
		this.clearFieldErrors(fieldName);
}

PdfPageFields.prototype.getFieldValue = function(fieldName){
	var jqobj = $("#"+fieldName);
	var datatype = jqobj.attr("datatype");

	if(datatype == 'BooleanType')
		return jqobj.val() == "true" ? true : false;
	
	if(datatype == 'IntegerType')
		return parseInt(jqobj.val(),10);
		
	if(datatype == 'DoubleType')
		return toDouble(jqobj.val(),jqobj.attr("scale"));
		
	if(datatype == 'DateType'){
		var date = jqobj.val();
		var gg = parseInt(date.substr(0,2),10);
		var mm = parseInt(date.substr(3,5),10)-1;
		var aa = parseInt(date.substr(6,10),10);
		return new Date(aa,mm,gg);
	}

	return jqobj.val();
}

PdfPageFields.prototype.getFieldValueAsString = function(fieldName){
	return $("#"+fieldName).val();
}

PdfPageFields.prototype.getJqFieldObjects = function(fieldName){
	var jqobj = $("#"+fieldName);
	var htmltype = jqobj.attr("htmltype");
	if(htmltype == 'radiobutton'){
		return $("div[name='"+fieldName+"Radio']");
	}else if(htmltype == 'checkbox'){
		return $("div[name='"+fieldName+"Check']");
	}else if(htmltype == 'text'){
		return $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	}else if(htmltype == 'textarea'){
		return $("#"+fieldName+",textarea[hasClone='true'][fieldName='"+fieldName+"']");
	}else if(htmltype == 'combo'){
		return $("#"+fieldName+",select[fieldName='"+fieldName+"']");
	}else{
		return $("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']");
	}
}

PdfPageFields.prototype.getGroupIds = function(groups){
	var ids = new Array();
	var groupsArray = null;
	if(typeof groups == "string")
		groupsArray = groups.split(",");
	else 
		groupsArray = groups;
	for(var i=0;i<groupsArray.length;i++){
		var groupName = groupsArray[i];
		$("div[name='"+groupName+"Cont'],div[groups~='"+groupName+"']").each(function(index){
			if(ids.indexOf(this.id) < 0)
				ids.push(this.id);
		});
	}
	if(ids.length > 0)
		return ids.toString();
	return "";
}

PdfPageFields.prototype.callHeavyEvent = function(eventName, targets){
	this.callEvent(eventName, targets, true);
}

PdfPageFields.prototype.callEvent = function(eventName, targets, showWait){

	function addHiddenField(form, name, value) {
	    var input = document.createElement("input");
	    input.type = "hidden";
	    input.id = "pdfData_"+name; 
	    input.name = "pdfData_"+name; 
	    input.value = value;
	    form.appendChild(input);
	}	

	function submitEventForm(eventName) {
		var form = document.dati;
		if(useEventFormData)
			form = document.datiEventi;
		
		var ids = "";
		if(targets == "none"){
			;
		}else if(targets == "pdfTitle"){
			;
		}else{
			if(typeof targets != "undefined")
				ids = pdfPageFields.getGroupIds(targets);
			if(ids.length == 0)
				ids = "pagesCont";
			ids = ids+",";
		}
		
		form.eventName.value = locEventName;			
		form.eventArgs.value = locEventArgs;				
		form.reloadPageOnEvent.value = "false";
		form.wfemCmd.value = "prgm.pdfwebforms.business.CallEvent.execute";
		if(targets == "none"){
			wfemHiddenSubmit(form,"none");
		}else{
			var callback = new Object();
			var callbackObj = new Object();
			callbackObj.eventName = eventName;			
			callback.callbackObj = callbackObj;
			callback.callbackFnc = pdfPageFields.callEventCallback;
			if(showWait)
				wait();		
			if(useEventFormData){
				$("#"+form.name+" input[id^='pdfData_']").remove();
				var submitFields = null;
				if(typeof eventName.fields == "string")
					submitFields = eventName.fields.split(",");
				else
					submitFields = eventName.fields;
				for(var i=0;i<submitFields.length;i++){
					var value = pdf.getFieldValueAsString(submitFields[i]);
					if(typeof value != "undefined"){    			
			    		addHiddenField(form, submitFields[i], value);
			    	}
				}
				form.pdfData_editableFields.value = document.dati.pdfData_editableFields.value;
				form.pdfData_uneditableFields.value = document.dati.pdfData_uneditableFields.value;
				form.pdfData_extraMandatoryFields.value = document.dati.pdfData_extraMandatoryFields.value;
				form.pdfData_hidedFields.value = document.dati.pdfData_hidedFields.value;
			}		
			wfemHiddenSubmit(form,ids+"engineGlobalParams,pdfTitle,errorsMarkers",callback);
		}
	}
	
	if(jsPdfPageDriverClass == "")
		return;
	
	var asObjectPars = (typeof eventName == 'object');
	var useEventFormData = asObjectPars && (typeof eventName.fields != 'undefined');
	
	var locEventName = "";
	var locEventArgs = "";
	if(asObjectPars){
		locEventName = eventName.eventName;
		if(typeof eventName.eventArgs != 'undefined')
			locEventArgs = eventName.eventArgs;
	}else{
		locEventName = eventName;
	}
	if(typeof targets == 'undefined' && asObjectPars)
		targets = eventName.targets;
		
	changeRunning = true;
	submitEventForm(eventName);		
}

PdfPageFields.prototype.callEventCallback = function(){
	changeRunning = false;
	try{
		pdfPageDriver.onEventCallback(this.eventName);		
	}catch(e){}
	if(afterChangeFnc != null){
		afterChangeFnc();
		afterChangeFnc = null;
	}
	if(document.getElementById("errorsMarkers") != null){
		try{
			showErrMarkers();
		}catch(e){}
	}	
}

PdfPageFields.prototype.submitEvent = function(eventName){
	if(jsPdfPageDriverClass == "")
		return;
	document.dati.reloadPageOnEvent.value = "true";
	document.dati.eventName.value = eventName;
	document.dati.wfemCmd.value = "prgm.pdfwebforms.business.CallEvent.execute";
	wait();
	document.dati.submit();
}

PdfPageFields.prototype.enableField = function(fieldName, enabled){
	if(jsIsReadonlyModality)
		return;
	
	var fieldVals = null;
	var fieldNameAndvals = fieldName.split("#");
	if(fieldNameAndvals.length > 1){
		fieldName = fieldNameAndvals[0];
		fieldVals = "|"+fieldNameAndvals[1]+"|";
	}
	
	var jqobj = $("#"+fieldName);
	var htmltype = jqobj.attr("htmltype");
	var datatype = jqobj.attr("datatype");
	
	if(datatype == "DateType"){
		if(enabled){
			$("img[fieldName="+fieldName+"ImgCal]").css("visibility","visible");
			$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"false", "readonly": false, "tabindex": $(this).attr("containerId")});
			});
		}else{
			$("img[fieldName="+fieldName+"ImgCal]").css("visibility","hidden");
			$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"true", "readonly": true, "tabindex": "-1"});
			});
		}
	}else if(htmltype == "radiobutton"){
		if(enabled){
			$("#"+fieldName).attr({"isreadonly":"false"});
			$("div[name='"+fieldName+"Radio']").each(function(index){
				$(this).css("cursor","pointer").attr({"tabindex": $(this).attr("containerId"), "isreadonly":"false"});
				$("div[fid='"+fieldName+$(this).attr("containerId")+"']").removeClass("pdfReadonlyField");
			});
		}else{			
			if(fieldVals != null){
				pdfPageFields.enableField(fieldName, true); // Resettiamo la situazione abilitando tutti i radio
				enabled = true; // Per i settaggi degli editable/uneditable successivi, il campo rimane comunque abilitato
			}else{
				$("#"+fieldName).attr({"isreadonly":"true"});
			}
			$("div[name='"+fieldName+"Radio']").each(function(index){
				if(fieldVals == null || fieldVals.indexOf("|"+$(this).attr("value")+"|") >= 0){
					$(this).css("cursor","default").attr({"tabindex": "-1", "isreadonly":"true"});
					var fva = $("#"+fieldName).val();
					if(fva != "" && fva != $(this).attr("value"))
						$("div[fid='"+fieldName+$(this).attr("containerId")+"']").addClass("pdfReadonlyField").removeClass("pdfFieldHasError");
					else
						$("div[fid='"+fieldName+$(this).attr("containerId")+"']").addClass("pdfReadonlyField");
				}
			});
		}
	}else if(htmltype == "checkbox"){
		if(enabled){
			$("#"+fieldName).attr({"isreadonly":"false"});
			$("div[name='"+fieldName+"Check']").css("cursor","pointer").each(function(index){
				$(this).attr({"tabindex": $(this).attr("containerId")});
			});
		}else{
			$("#"+fieldName).attr({"isreadonly":"true"});
			$("div[name='"+fieldName+"Check']").css("cursor","default").attr({"tabindex": "-1"});
		}
	}else if(htmltype == "combo"){
		if(enabled){
			$("#"+fieldName).attr({"isreadonly":"false"});
			$("select[fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"false", "tabindex": $(this).attr("containerId")});
			});
		}else{
			$("#"+fieldName).attr({"isreadonly":"true"});
			$("select[fieldName='"+fieldName+"']").attr({"isreadonly":"true", "tabindex": "-1"});
		}
	}else if(htmltype == "textarea"){
		if(enabled){
			$("#"+fieldName+",textarea[hasClone='true'][fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"false", "readonly": false, "tabindex": $(this).attr("containerId")});
			});
		}else{
			$("#"+fieldName+",textarea[hasClone='true'][fieldName='"+fieldName+"']").attr({"isreadonly":"true", "readonly": true, "tabindex": "-1"});
		}
	}else{
		if(enabled){
			$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"false", "readonly": false, "tabindex": $(this).attr("containerId")});
			});
		}else{
			$("#"+fieldName+",input[hasClone='true'][fieldName='"+fieldName+"']").each(function(index){
				$(this).attr({"isreadonly":"true", "readonly": true, "tabindex": "-1"});
			});
		}
	}
	
	if(htmltype !== "radiobutton"){
		if(enabled)
			$("div[name='"+fieldName+"ContBorder']").removeClass("pdfReadonlyField");
		else
			$("div[name='"+fieldName+"ContBorder']").addClass("pdfReadonlyField");
	}
	
	if(document.dati.pdfData_editableFields){
		var editableFieldsArray = new Array();
		if(document.dati.pdfData_editableFields.value.length > 0)
			editableFieldsArray = document.dati.pdfData_editableFields.value.split(","); 
		var uneditableFieldsArray = new Array();
		if(document.dati.pdfData_uneditableFields.value.length > 0)
			uneditableFieldsArray = document.dati.pdfData_uneditableFields.value.split(",");
		if(enabled){
			if(editableFieldsArray.indexOf(fieldName) < 0)
				editableFieldsArray.push(fieldName);
			var idx = uneditableFieldsArray.indexOf(fieldName);
			if(idx >= 0){
				var ar1 = uneditableFieldsArray.slice(0,idx);
				var ar2 = uneditableFieldsArray.slice(idx+1);
				uneditableFieldsArray = ar1.concat(ar2)
			}
		}else{
			if(uneditableFieldsArray.indexOf(fieldName) < 0)
				uneditableFieldsArray.push(fieldName);
			var idx = editableFieldsArray.indexOf(fieldName);
			if(idx >= 0){
				var ar1 = editableFieldsArray.slice(0,idx);
				var ar2 = editableFieldsArray.slice(idx+1);
				editableFieldsArray = ar1.concat(ar2)
			}
		}
		document.dati.pdfData_editableFields.value = editableFieldsArray.toString();
		document.dati.pdfData_uneditableFields.value = uneditableFieldsArray.toString();
	}
}

PdfPageFields.prototype.isFieldEnabled = function(field){
	var jqobj = null;
	if(typeof(field) == "string")
		jqobj = $("#"+field);
	else
		jqobj = $(field);
	try{
		if(jqobj.attr("isreadonly") == 'true') 
			return false;
	}catch(e){}
	return true;
}

PdfPageFields.prototype.setMandatoryField = function(fieldName, mandatory){
	if(mandatory)
		$("div[name='"+fieldName+"ContBorder']").addClass("pdfMandatoryField");
	else
		$("div[name='"+fieldName+"ContBorder']").removeClass("pdfMandatoryField");
	
	if(document.dati.pdfData_extraMandatoryFields){
		var extraMandatoryFields = new Array();
		if(document.dati.pdfData_extraMandatoryFields.value.length > 0)
			extraMandatoryFields = document.dati.pdfData_extraMandatoryFields.value.split(",");
		if(mandatory){
			if(extraMandatoryFields.indexOf(fieldName) < 0)
				extraMandatoryFields.push(fieldName);
		}else{
			var idx = extraMandatoryFields.indexOf(fieldName);
			if(idx >= 0)
				extraMandatoryFields.splice(idx, 1);
		}
		document.dati.pdfData_extraMandatoryFields.value = extraMandatoryFields.toString();
	}
}

PdfPageFields.prototype.isFieldMandatory = function(field){
	var jqobj = null;
	if(typeof(field) == "string")
		jqobj = $("div[name='"+field+"ContBorder']");
	else
		jqobj = $("div[name='"+field.id+"ContBorder']");
	try{
		if(jqobj.hasClass("pdfMandatoryField")) 
			return true;
	}catch(e){}
	return false;
}

PdfPageFields.prototype.dispatchChangeAgeEvent = function(field, item){
	try{
		return pdfPageDriver.onChangeAge(field.id, item);
	}catch(e){}
}

PdfPageFields.prototype.dispatchChangePersonEvent = function(field, personIdx, item){
	if(personIdx === 1)
		this.clearAgevolazione();	
	try{
		return pdfPageDriver.onChangePerson(field.id, personIdx, item);
	}catch(e){}
}

PdfPageFields.prototype.dispatchChangeEvent = function(field, item){
	try{
		this.clearFieldErrors(field.id);
		if(field.id === "tipoAgevolazione")
			this.manageAgevolazione();
		pdfPageDriver.onChange(field.id, item);
	}catch(e){}
}

PdfPageFields.prototype.manageAgevolazione = function(){
	try{
		this.enableField("descrizioneAgevolazione", false);
		if(pdf.isAgevolazionePrecaricata())
			return;
		var tipoAgevolazione = this.getFieldValue("tipoAgevolazione");
		if(tipoAgevolazione === "DIPENDENTI"){
			this.enableField("descrizioneAgevolazioneDipendenti",true);
		}else{
			this.enableField("descrizioneAgevolazioneDipendenti",false);
			this.setFieldValue("descrizioneAgevolazioneDipendenti","");
		}
		if(tipoAgevolazione === "ALTRO"){
			this.enableField("codiceAgevolazione",true);
		}else{
			this.enableField("codiceAgevolazione",false);
			pdfPageDataentryUtil.clearAgevolazioneData();
		}		
	}catch(e){}
}

PdfPageFields.prototype.clearAgevolazione = function(){
	try{
		if(pdf.isAgevolazionePrecaricata())
			return;
		this.setFieldValue("descrizioneAgevolazioneDipendenti","");
		pdfPageDataentryUtil.clearAgevolazioneData();
	}catch(e){}
}

PdfPageFields.prototype.initRadiobutton = function(fieldName){
	$("div[name='"+fieldName+"Radio']").unbind().bind({
		click: function(event){
				event.stopPropagation();
				var fieldName = $(this).attr("fieldName");
				if(!pdfPageFields.isFieldEnabled(fieldName) || $(this).attr("isreadonly") === "true")
					return;
				var jqobj = $("#"+fieldName);
				var value = $(this).attr("value");
				var curValue = jqobj.val();
				$("div[name='"+fieldName+"Radio']").children("img").css("visibility","hidden");
				if(value == curValue){
					jqobj.val("");
				}else{
					jqobj.val(value);
					$(this).children("img").css("visibility","visible");
				}
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
				
		mouseout: function(event){ 
				pdfPageFields.hideWarning(); 
		},
		
		mouseenter: function(event){ 				
				pdfPageFields.showWarning(this); 
		},
		
		focusout: function(event){ 
				pdfPageFields.hideWarning(); 
		},
		
		focusin: function(event){ 
				pdfPageFields.showWarning(this); 
		},
		
		keypress: function(event){
				if(event.which == 32){
					event.stopPropagation();
					event.preventDefault();
					if(pdfPageFields.isFieldEnabled($(this).attr("fieldName")) && $(this).attr("isreadonly") !== "true") 
						this.click();
				}
		}
		
	}).css("cursor",pdfPageFields.isFieldEnabled(fieldName)?"pointer":"default");
}

PdfPageFields.prototype.initCheckbox = function(fieldName){
	$("div[name='"+fieldName+"Check']").unbind().bind({
		click: function(event){
				event.stopPropagation();
				var fieldName = $(this).attr("fieldName");
				if(!pdfPageFields.isFieldEnabled(fieldName))
					return;
				var jqobj = $("#"+fieldName);
				var checked = false;
				if("true" == jqobj.val())
					checked = true;
				checked = !checked;
				jqobj.val(checked ? "true" : "false");
				if(checked)
					$("div[name='"+fieldName+"Check']").children("img").css("visibility","visible");
				else
					$("div[name='"+fieldName+"Check']").children("img").css("visibility","hidden");
				pdfPageFields.dispatchChangeEvent(document.getElementById(fieldName));
		},
		
		mouseout: function(event){ 
				pdfPageFields.hideWarning(); 
		},
		
		mouseenter: function(event){ 
				pdfPageFields.showWarning(this); 
		},
		
		focusout: function(event){ 
				pdfPageFields.hideWarning(); 
		},
		
		focusin: function(event){ 
				pdfPageFields.showWarning(this); 
		}, 
		
		keypress: function(event){ 
				if(event.which == 32){
					event.stopPropagation();
					event.preventDefault();
					if(pdfPageFields.isFieldEnabled($(this).attr("fieldName"))) 
						this.click();
				}
		}
	
	}).css("cursor",pdfPageFields.isFieldEnabled(fieldName)?"pointer":"default");
}

PdfPageFields.prototype.initSignCheckbox = function(fid){
	$("#"+fid+"Check").unbind().bind({
		click: function(event){
				event.stopPropagation();
				event.preventDefault();
				apponiFirma($(this));
		},
		keypress: function(event){ 
			if(event.which == 13 || event.which == 32){
				event.stopPropagation();
				event.preventDefault();
				apponiFirma($(this));
			}
		},
		focus: function(event){
			var fieldName = $(this).attr("fieldName");
			var jqobj = $("#"+fieldName);
			if("true" == jqobj.val()){
				var ariaLabel = $("#"+fieldName+"AriaLabelSigned").html();
				$(this).attr({"aria-describedby":"", "aria-label": ariaLabel});
			}
		}
		
	}).css("cursor","pointer");
}

function apponiFirma(signCheck){
	var fieldName = signCheck.attr("fieldName");
	var jqobj = $("#"+fieldName);
	
	if("true" == jqobj.val()){
		srSpeak($("#"+fieldName+"SignDescr").html());
		return;
	}
	
	jqobj.val("true");
	signCheck.attr({"aria-checked":"true"});
	$("div[name='"+fieldName+"Check']").children("img").css("visibility","visible");	

	var speak = "Firma apposta.";
	var allSigned = true;
	$(".signFieldValue").each(function(index){
		if(this.value != "true"){
			allSigned = false;
			return false;
		}
	});
	if(allSigned){
		var presaVisioneCont = document.getElementById("presaVisioneCont");
		if(presaVisioneCont != null){
			presaVisioneCont.style.display='';
			speak = speak+" "+presaVisioneCont.innerHTML;
		}
	}
	srSpeak(speak);	
}

PdfPageFields.prototype.initText = function(fieldId){
	$("#"+fieldId).unbind().bind({
		
		change:	function(event) {
			var newVal = "";
			for(var i=0;i<this.value.length;i++){
				var cc = this.value.charCodeAt(i);
				if(cc == 8220 || cc == 8221)
					 this.value = this.value.replaceAt(i,'"');
				if(cc == 8216 || cc == 8217)
					 this.value = this.value.replaceAt(i,"'");
				cc = this.value.charCodeAt(i);
				if(cc > 255)
					continue;
				var c = this.value.charAt(i);
				if(this.value.charCodeAt(i) == 160) 
					c = ' ';
				newVal += ""+c;
			}
			if(this.type == 'textarea')
				$("textarea[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val(newVal); 
			else
				$("input[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val(newVal); 
			pdfPageFields.dispatchChangeEvent(document.getElementById($(this).attr("fieldName")));
		},
		
		keydown: function(event) {
				if(event.which == 69 && event.ctrlKey && event.altKey){ // Don't accept euro
					event.preventDefault();
					return;
				}
		},
		
		keypress: function(event) {
				if(event.which == 0 || event.which == 8 || event.ctrlKey)
					return;
					
				if(!pdfPageFields.isFieldEnabled($(this).attr("fieldName"))){
					event.preventDefault();
					return;
				}
				
				if(this.maxlength){
					if(parseInt(this.value.length,10) >= parseInt(this.maxlength,10)){
						event.preventDefault();
						return;
					}
				}
				
				if($(this).attr("onlynum") == "true"){
					var c = String.fromCharCode(event.which);
					var idx = "0123456789".indexOf(c);
					if(idx < 0){
						event.preventDefault();
						return;
					}
				}
		},
		   		 
		focusout: function(event) {
				pdfPageFields.hideWarning();
				
				if(!pdfPageFields.isFieldEnabled($(this).attr("fieldName")))
					return;
				
				// Se siamo sull'ndg ed e' impostato con l'id censimento, causa ricaricamento dati cliente, non pulisco al focusout
				var fname = $(this).attr("fieldName");
				if( fname == "ndgCliente1" || fname == "ndgCliente2" || fname == "ndgCliente3" ||
					fname == "ndgCliente4" || fname == "ndgCliente5" || fname == "ndgCliente6" ||
					fname == "ndgCliente7" || fname == "ndgCliente8" || fname == "ndgCliente9"){
					if(this.value.length == 16 && this.value.charAt(0) == 'S')
						return;
				}				
				
				var newVal = "";
				for(var i=0;i<this.value.length;i++){
					var cc = this.value.charCodeAt(i);
					if(cc == 8220 || cc == 8221)
						 this.value = this.value.replaceAt(i,'"');
					if(cc == 8216 || cc == 8217)
						 this.value = this.value.replaceAt(i,"'");
					cc = this.value.charCodeAt(i);
					if(cc > 255)
						continue;
					var c = this.value.charAt(i);
					if($(this).attr("onlynum") == "true" && (c < '0' || c > '9')){
						newVal = '';
						break;
					}
					if(this.value.charCodeAt(i) == 160) 
						c = ' ';
					newVal += ""+c;
				}
				this.value = newVal;
		},
		
		focusin: function(event){
			pdfPageFields.showWarning(this); 
		}
	});
}

PdfPageFields.prototype.initDate = function(fieldId){
	var field = document.getElementById(fieldId);
	if(field.mask)
		return;
	
	$(field).bind({
		
		focusout: function(event){ 
			$("#"+this.id+'Mask').val('');
			pdfPageFields.hideWarning(); 
		},
 
		focusin: function(event){
			if(pdfPageFields.isFieldEnabled($(this).attr("fieldName")))
				$("#"+this.id+'Mask').val('__-__-____');
			pdfPageFields.showWarning(this); 
		},
		
		focus: function(event){
			if(pdfPageFields.isFieldEnabled($(this).attr("fieldName")))
				$("#"+this.id+'Mask').val('__-__-____');
		}
	});
	
	var mask = new DateMask(this.dateParser,fieldId);
	field.mask = mask;
	mask.changeFunction = function(event){
		$("input[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val($(this).val()); 
		pdfPageFields.dispatchChangeEvent(document.getElementById($(field).attr("fieldName")));
		return true;
	}
	
	$('#'+fieldId+"Mask").bind({
		focus: function(event){
			$('#'+fieldId).focus();
		}
	});
	this.initImgCal(fieldId);
}

PdfPageFields.prototype.initImgCal = function(fieldId){
	if(document.getElementById(fieldId+'ImgCal') == null)
		return;
	$('#'+fieldId+'ImgCal').bind({
		click: function(event){
			var propId = this.id.substring(0,(this.id.length-"ImgCal".length));
			var dtObj = $('#'+propId); 
			if(!dtObj.hasClass('hasDatepicker')){
				dtObj.datepicker({dateFormat:'dd-mm-yy',
								  showWeek: true, 
								  firstDay: 1,
							  	  showOn: 'button',
							  	  buttonImage: __retrieveWfemLayoutResourceUrl()+'/private/calendar/images/mokeOpenCalendar.gif',
							  	  buttonImageOnly: true,
							  	  beforeShow: function(input, inst) { 	
							  		  pdfPageFields.curDatepickerFieldName = inst.id;
							  		  var field = document.getElementById(inst.id);
									  field.beforecalvalue = field.value;
							  	  },
							  	  onClose: function(dateText, inst) { 
							  		  pdfPageFields.curDatepickerFieldName = null;
	  								  var field = document.getElementById(inst.id);
  									  if(field.value != field.beforecalvalue){
  										$("input[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val($(this).val()); 
  										pdfPageFields.dispatchChangeEvent(document.getElementById($(field).attr("fieldName")));
  									  }
							  	  }
							  	});
			}
			dtObj.datepicker('show');
			document.getElementById("ui-datepicker-div").style.zIndex=100;
		}
	 });
}

PdfPageFields.prototype.initDouble = function(fieldId){
	
	var field = document.getElementById(fieldId);
	if(field.mask)
		return;
	
	$(field).bind({
		
		keypress: function(event){
			 if(window.event)
				 event = window.event;
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
		},
		
		focusout: function(event){
			pdfPageFields.hideWarning();
		},
		
		focusin: function(event){
			pdfPageFields.showWarning(this);
		}
	});

	var scale = field.getAttribute("scale");
	var parser = new NumberParser(scale, pdfPageFields.decimalChar, pdfPageFields.separator, true);
	var mask = new NumberMask(parser,fieldId);
	mask.allowNegative = true;
    mask.leftToRight = true;
	field.mask = mask;
	mask.changeFunction = function(event){
		$("input[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val($(this).val()); 
		pdfPageFields.dispatchChangeEvent(document.getElementById($(field).attr("fieldName")));
		return true;
	}
}

PdfPageFields.prototype.initCombo = function(fieldId){
	
	$("#"+fieldId+"Combo").unbind().bind({
		
		change: function(event){
			var value = $(this).val();
			$("select[fieldName='"+$(this).attr("fieldName")+"']").not(document.getElementById(this.id)).val(value);
			$("#"+$(this).attr("fieldName")).val(value);
			pdfPageFields.dispatchChangeEvent(document.getElementById($(this).attr("fieldName")));
		},
		
		focusout: function(event){
			pdfPageFields.hideWarning();
		},
		
		focusin: function(event){
			pdfPageFields.showWarning(this);
		},
		
		mouseout: function(event){
			pdfPageFields.hideWarning();
		},
		
		mouseenter: function(event){
			pdfPageFields.showWarning(this);
		},
		
		mousedown: function(event){
			if(this.getAttribute("isreadonly") === "true")
				event.preventDefault();
		}
	});	
}

PdfPageFields.prototype.hideWarning = function(){
	$("#warningsStruct").hide();
}

PdfPageFields.prototype.showWarning = function(obj){

	if(!$("#fcontborder"+$(obj).attr("containerid")).hasClass("pdfFieldHasError"))
		return;
		
	var msgName = $(obj).attr("fieldName");
	if(msgName == null)
		return;
	
	var messages = eval("document."+msgName+"messages");
	if(messages == null || messages.length == 0)
		return;
	
	var html = "<ul style='margin-left:15;padding-left:5;'>";
	for(var i=0;i<messages.length;i++)
		html += "<li>"+messages[i]+"</li>";
	html += "</ul>";
	$("#warningText").html(html);

	var wstruct = $("#warningsStruct");
	var fieldCont = $("#fcont"+$(obj).attr("containerId"));
	var pageOffset = parseInt(fieldCont.attr("pageOffset"),10);

	var fieldPos = fieldCont.position();
	var fMiddle = fieldPos.left + (fieldCont.width() / 2);
	
	var warLeft = fMiddle - (wstruct.width() / 2);
	var warRight = warLeft+wstruct.width();
	var warTop = fieldPos.top - wstruct.height() - 5 + pageOffset;
	
	$("#warningArrow").css({left: (wstruct.width()/2) - 5});
	var page = $("#fieldsCont").position();

	if(warLeft < page.left){
		warLeft = 10;
		$("#warningArrow").css({ left: fieldPos.left - 5 });
	}else if(warRight > (page.left+$("#fieldsCont").width())){
		warLeft = fMiddle - wstruct.width() + 20;
		$("#warningArrow").css({ left: wstruct.width() - 25 });
	}
	
	wstruct.css({left: warLeft, top: warTop}).show("slide",{ direction: "down" },300);
}

PdfPageFields.prototype.clearFieldErrors = function(fieldName){
	
	if(document.dati.momEventData_confrontaDoppiaSpunta != null && 
	   document.dati.momEventData_confrontaDoppiaSpunta.value === "V")
		return;
	
	$("div[name='"+fieldName+"ContBorder']").removeClass("pdfFieldHasError");
	try{
		$("div[name='"+fieldName+"ErrMarker']").html("").removeClass("errMarker");
		var numErrs = $(".errMarker").length;
		if(numErrs === 0){
			$("#errorsMarkers").css("border-left","none");
			$("#errorsMarkersTitle").hide();
			$("#numErrorsMarkers").html("");
			$("#navigateErrorsMarkers").hide();
		}else{
			var totErrs = $(".totErrMarker").length;
			if(numErrs > totErrs)
				totErrs = numErrs;
			$("#numErrorsMarkers").html("Correzione errori ("+numErrs+"/"+totErrs+" errori)").show();
			$("#navigateErrorsMarkers").show();
		}
	}catch(e){}
	try{eval("document."+fieldName+"messages = new Array();");}catch(e){}
}

PdfPageFields.prototype.hasFieldErrors = function(fieldName){
	try{
		if(!$("div[name='"+fieldName+"ContBorder']").hasClass("pdfFieldHasError"))
			return false;	
		var messages = eval("document."+fieldName+"messages");
		if(messages != null && messages.length > 0 && !messages[0].startsWith("Valore precedente: "))
			return true;
	}catch(e){}
	return false;
}

PdfPageFields.prototype.scrollToField = function(fieldName, relativePos){
	try{
		var pdfFieldTop = $("div[fid='"+fieldName+"']").attr("top");
		if(typeof relativePos == "undefined")
			relativePos = 50;
		var pos = parseInt(pdfFieldTop,10)-relativePos;
		$("#pagesCont").animate({scrollTop:pos},'50','swing');
	}catch(e){}
}

PdfPageFields.prototype.hideField = function(fieldName){
	var jqObj = $("div[name='"+fieldName+"Cont']");
	if(jqObj.css("display") === "none")
		return;
	jqObj.css("display","none");
	if(!this.onPageLoad)
		this.clearFieldErrors(fieldName);
	var hidedFieldsArray = new Array();
	if(document.dati.pdfData_hidedFields.value.length > 0)
		hidedFieldsArray = document.dati.pdfData_hidedFields.value.split(","); 
	if(hidedFieldsArray.indexOf(fieldName) < 0)
		hidedFieldsArray.push(fieldName);
	document.dati.pdfData_hidedFields.value = hidedFieldsArray.toString();
}

PdfPageFields.prototype.showField = function(fieldName){
	var jqObj = $("div[name='"+fieldName+"Cont']");
	if(jqObj.css("display") === "")
		return;
	jqObj.css("display","");
	if(!this.onPageLoad)
		this.clearFieldErrors(fieldName);
	var hidedFieldsArray = new Array();
	if(document.dati.pdfData_hidedFields.value.length > 0)
		hidedFieldsArray = document.dati.pdfData_hidedFields.value.split(","); 
	var idx = hidedFieldsArray.indexOf(fieldName);
	if(idx >= 0){
		var ar1 = hidedFieldsArray.slice(0,idx);
		var ar2 = hidedFieldsArray.slice(idx+1);
		hidedFieldsArray = ar1.concat(ar2)
	}
	document.dati.pdfData_hidedFields.value = hidedFieldsArray.toString();
}

PdfPageFields.prototype.isFieldHided = function(fieldName){
	var jqObj = $("div[name='"+fieldName+"Cont']");
	return jqObj.css("display") === "none";
}

var pdfPageFields = new PdfPageFields();
