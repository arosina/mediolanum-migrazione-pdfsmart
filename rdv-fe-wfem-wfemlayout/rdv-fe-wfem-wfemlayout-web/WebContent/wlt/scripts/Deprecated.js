////////////////////////////////////////////////////////////////////////
/////////////////////  Gestione checkbox    ////////////////////////////
////////////////////////////////////////////////////////////////////////
function setBoolField(boolPropName){
	var boolObj = document.getElementById(boolPropName);
	var checkBoolObj = document.getElementById(boolPropName+'Check');
	boolObj.value = checkBoolObj.checked ? "true" : "false";
}

function clearBoolField(boolPropName){
	var boolObj = document.getElementById(boolPropName);
	var checkBoolObj = document.getElementById(boolPropName+'Check');
	boolObj.value = "false";
	checkBoolObj.checked = false;
}

function checkBoolField(boolPropName){
	var boolObj = document.getElementById(boolPropName);
	var checkBoolObj = document.getElementById(boolPropName+'Check');
	boolObj.value = "true";
	checkBoolObj.checked = true;
}

////////////////////////////////////////////////////////////////////////
/////////////////////  Gestione date    ////////////////////////////
////////////////////////////////////////////////////////////////////////
function setHtmlDateField(datePropName,value){
	setPropertyValue(datePropName,value);
}

function clearDateField(datePropName){
	setPropertyValue(datePropName,"");
}

function isDate(day,month,year) {
    var today = new Date();

    if (!day) return false

    if (!month) return false
    else month = month - 1;
    
    if (!year) return false

    var test = new Date(year,month,day);
    if ( (y2k(test.getYear()) == year) &&
         (month == test.getMonth()) &&
         (day == test.getDate()) )
        return true;
    else
        return false
}

////////////////////////////////////////////////////////////////////////
/////////////////////  Utility    ////////////////////////////
////////////////////////////////////////////////////////////////////////
function y2k(number) {
	return (number < 1000) ? number + 1900 : number;
}

function getChecked(radio){
	if(radio){
		for(i=0;i<radio.length;i++){
			if(radio[i].checked)
				return radio[i].value;
		}
	}	
	return "";
}

function setChecked(name,value,doClick){
	var i = 0;
	var radio = document.getElementsByName(name);
	while(radio[i]){
		if(radio[i].value == value){
			radio[i].checked = true;
			if(doClick)
				radio[i].click();
		}else{
			radio[i].checked = false;
		}
		i++;
	}
}

function clearIfZeroes(obj){
	var num = new Number(obj.value);
	if(isNaN(num) || num == 0)
		obj.value = '';
}
