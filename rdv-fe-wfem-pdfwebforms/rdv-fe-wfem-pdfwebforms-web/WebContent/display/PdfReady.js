$( document ).ready(function() {
	
	try{
		$("#pagesCont").bind("scroll",function(){ 
								document.dati.scrollYValue.value = this.scrollTop | 0; 
								document.dati.scrollXValue.value = this.scrollLeft | 0;
								if(pdfPageFields.curDatepickerFieldName != null)
									$("#"+pdfPageFields.curDatepickerFieldName).datepicker("hide");
							}).scrollTop(jsScrollYValue).scrollLeft(jsScrollXValue);
	}catch(e){}
	try{		
		$("#pageBody").css("visibility","visible");
	}catch(e){}
	
	$(".normalButton").attr({"tabindex":"0", "role":"button"});
	$(".normalButtonDisab").attr({"tabindex":"-1"});
	$("img:not([alt])").attr("alt", "");
	
	try{
		$('[role="button"], [role="link"]').bind({
			keypress: function(event){
				if(event.which == 13 || event.which == 32)
					this.click();
				event.preventDefault();					
			}			
		});
	}catch(e){}

	try{
		if(jsInitToCall)
			pdfPageDriver.onInit();
	}catch(e){}
	
	try{
		pdfPageFields.manageAgevolazione();
		pdfPageFields.onPageLoad = true;
	}catch(e){}
	try{
		pdfPageDriver.onLoad();
	}catch(e){}
	try{
		pdfPageFields.onPageLoad = false;
	}catch(e){}
	
	if(document.getElementById("errorsMarkers") != null){
		try{
			showErrMarkers();
		}catch(e){}
	}

	function manageVenditaComeBmed(fieldName, label){
		$("#"+fieldName+",input[fieldName='"+fieldName+"']").attr("disabled","disabled").val(label);
	}
	
	if(jsIsVenditaComeBmed){
		manageVenditaComeBmed("cognomeNomeConsulenteFinanziario","Banca Mediolanum");
		manageVenditaComeBmed("nomeAgente","Banca");
		manageVenditaComeBmed("cognomeAgente","Mediolanum");
		manageVenditaComeBmed("cognomeNomeAgente","Banca Mediolanum");
		manageVenditaComeBmed("nomeCognomeAgente","Banca Mediolanum");
	}
	
});

