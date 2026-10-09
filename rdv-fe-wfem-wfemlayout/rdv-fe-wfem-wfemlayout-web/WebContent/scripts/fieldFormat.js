var alfaChars = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
alfaChars += "!\#$?%&'\"()*+,-./:;<=>?@[]^_`{}~";
var intChars  = "0123456789";
var stringChars = intChars.toString()+alfaChars.toString();
var doubleChars = intChars.toString();

function formatNumber(field,numDec,separator,decimalChar) {	
	try{
		if(field.value == '')
			return;
	
		var num = new NumberFormat();
		num.setInputDecimal(decimalChar);
		num.setNumber(field.value);
		num.setPlaces(numDec);
		num.setCurrencyValue('$');
		num.setCurrency(false);
		num.setCurrencyPosition(num.LEFT_OUTSIDE);
		num.setNegativeFormat(num.LEFT_DASH);
		num.setNegativeRed(false);
		num.setSeparators(true, separator, decimalChar);
		field.value = num.toFormatted();
		
		if(decimalChar != null && decimalChar != ""){
			var intPart = field.value.toString();
			var idx = intPart.indexOf(decimalChar);
			if(idx >= 0){
				var decPart = parseInt(intPart.substring(idx+1),10);		
				if(decPart == 0){
					field.value = intPart.substring(0,idx);
					return;
				}
			}
		}
		return;
	}catch(exec){LOG.debug("formatNumber "+ exec);}
}

function manageAllField(eMoz,field){
	try{
		var eventCross = eMoz || event;
		if(eventCross.which == 0)
			return true;
		var keyCodeCross = eventCross.keyCode||eventCross.which;
		if(keyCodeCross == 8)
			return true;	
		
		if(field.readOnly){
			eventCross.returnValue=false;
			return false;
		}
		
		if(keyCodeCross == 8364){ // euro
			eventCross.returnValue=false;
			return false;
		}
		return true;
	}catch(exec){LOG.debug("manageAllField "+ exec);}
}

function manageField(eMoz,field,type,separator,decimalChar){
	try{
		var eventCross = eMoz || event;
		if(eventCross.which == 0)
			return true;
		var keyCodeCross = eventCross.keyCode||eventCross.which;
		if(keyCodeCross == 8)
			return true;	
		if(field.readOnly){
			eventCross.returnValue=false;
			return false;
		}
	
		var chars;
		if(type == 'string'){
			chars = stringChars.toString();
			if(field.rows) {
				chars += "\n\r";
				if(field.maxlength){
					if(parseInt(field.value.length, 10) >= parseInt(field.maxlength,10)){
						eventCross.returnValue=false;
						return false;
					}
				}
			}
		}else if(type == 'alfa'){
			chars = alfaChars.toString();
		}else if(type == 'int'){
			chars = intChars.toString();
			if(typeof(field.minus) != 'undefined' && field.minus == 'true')
				chars += "-";
		}else if(type == 'double'){
			chars = doubleChars.toString();
			if(typeof(field.minus) != 'undefined' && field.minus == 'true')
				chars += "-";
		}
	
		var idx = chars.indexOf(String.fromCharCode(keyCodeCross));
		if(idx < 0){
			if(type != 'double' || String.fromCharCode(keyCodeCross) != decimalChar){
				eventCross.returnValue = false;
				if(eventCross.preventDefault)
				    eventCross.preventDefault();
				return false;
			}
		}
		
		if(type == 'double'){
			if((field.value.indexOf(decimalChar) >= 0 && String.fromCharCode(keyCodeCross) == decimalChar)){
				eventCross.returnValue = false;
				if(eventCross.preventDefault)
				    eventCross.preventDefault();
				return false;
			}
		}
		
		return true;
	}catch(exec){LOG.debug("manageField "+ exec);}
}

function manageBeforePasteField(field,type){
	try{
		event.returnValue=false;
	}catch(exec){LOG.debug("manageBeforePasteField " +exec);}
}

function manageBeforeActivateField(field,type,separator,decimalChar){
	try{
		if(field.readOnly)
			return;
	
		var re = new RegExp('\\'+separator,'g');
		field.value=field.value.replace(re,'');
	}catch(exec){LOG.debug("manageBeforeActivateField " +exec);}	
}

function manageAllPasteField(field){
	try{
		if(field.readOnly){
			event.returnValue=false;
			return false;
		}
		
		var newValue = window.clipboardData.getData("Text");
		if(newValue == null || newValue == ''){
			event.returnValue=false;
			return false;
		}
		
		for(i=0;i<newValue.length;i++){
			if(newValue.charCodeAt(i) == 8364){  // euro
				event.returnValue=false;
				return false;
			}
		}
		return true;
	}catch(exec){LOG.debug("manageAllPasteField " +exec);}
}

function managePasteField(field,type,separator,decimalChar){
	try{
		if(field.readOnly){
			event.returnValue=false;
			return false;
		}
		
		var chars;
		if(type == 'string'){
			chars = stringChars.toString();
			if(field.rows) {
				chars += "\n\r";
				if(field.maxlength){
					if((parseInt(field.value.length, 10)+parseInt(window.clipboardData.getData("Text").length, 10)) > parseInt(field.maxlength,10)){
						event.returnValue=false;
						return false;
					}
				}
			}
		}else if(type == 'alfa'){
			chars = alfaChars.toString();
		}else if(type == 'int'){
			chars = intChars.toString();
			if(typeof(field.minus) != 'undefined' && field.minus == 'true')
				chars += "-";
		}else if(type == 'double'){
			chars = doubleChars.toString();
			if(typeof(field.minus) != 'undefined' && field.minus == 'true')
				chars += "-";
		}
	
		var newValue = window.clipboardData.getData("Text");
		if(newValue == null || newValue == ''){
			event.returnValue=false;
			return false;
		}
	
		if(typeof(field.uppercase) == 'undefined' || !field.uppercase == 'false'){
			newValue = newValue.toUpperCase();
			window.clipboardData.setData("Text",newValue);
		}
			
		var decimalCharFounded = false;
		for(i=0;i<newValue.length;i++){
			var idx = chars.indexOf(newValue.charAt(i));
			if(idx < 0){
				if(type != 'double' || newValue.charAt(i) != decimalChar){
					event.returnValue=false;
					return false;
				}
				
				if(type == 'double' && decimalCharFounded && newValue.charAt(i) == decimalChar){
					event.returnValue=false;
					return false;
				}
				
				if(newValue.charAt(i) == decimalChar)
					decimalCharFounded = true;
			}
			
			// For double
			if(type == 'double'){
				var selectedTxt = document.selection.createRange().text;
				if(selectedTxt != field.value){
					if((field.value.indexOf(decimalChar) >= 0 && newValue.charAt(i) == decimalChar)){
						event.returnValue=false;
						return false;
					}
				}
			}
		}
	
		return true;
	}catch(exec){LOG.debug("managePasteField " +exec);}
}

function manageAllKeydownField(eMoz,field){
	try{
		var eventCross = eMoz || event;
		if(eventCross.which == 0)
			return true;
		var keyCodeCross = eventCross.keyCode||eventCross.which;
		if(keyCodeCross == 8)
			return true;	
		if(field.readOnly || field.value == '')	
			return false;
		return false;
	}catch(exec){LOG.debug("manageAllKeydownField " +exec);}
}

function manageKeydownField(eMoz,field,type){
	try{
		var eventCross = eMoz || event;
		if(eventCross.which == 0)
			return true;
		var keyCodeCross = eventCross.keyCode||eventCross.which;
		if(keyCodeCross == 8)
			return true;	
		if(field.readOnly || field.value == '')	
			return false;
		return false;
	}catch(exec){LOG.debug("manageKeydownField " +exec);}
}

function toUpper(eMoz,field){
	try{
		var eventCross = eMoz || event;
		if(!eventCross.which){ // For Explorer
		   	if(typeof(field.uppercase) == 'undefined' || !field.uppercase == 'false'){
				event.keyCode = String.fromCharCode(event.keyCode).toUpperCase().charCodeAt(0);
				return true;
			}
			return false;
		}
		
		if(eventCross.which == 0)
			return true;
		var keyCodeCross = eventCross.keyCode||eventCross.which;
		if(keyCodeCross == 8)
			return true;	
		if(field.readOnly)
	   		return false;
		var idx = alfaChars.indexOf(String.fromCharCode(keyCodeCross));
		if(idx < 0)
			return true;	

	   	if(typeof(field.uppercase) == 'undefined' || !field.uppercase == 'false'){
			// Ferma l'evento del keypress solo per NS6 perche' il browser non permette
			// di modificare l'evento quindi va modificato il contenuto del campo a mano		
			if(eventCross.preventDefault)
			    eventCross.preventDefault();
			eventCross.returnValue = false;    
			field.value += String.fromCharCode(keyCodeCross).toUpperCase();
			return true;
		}
		return false;
	}catch(exec){LOG.debug("toUpper "  + exec);}
}


