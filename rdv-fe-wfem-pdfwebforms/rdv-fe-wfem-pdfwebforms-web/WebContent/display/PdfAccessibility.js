function srSpeak(text, liveType) {
    var el = document.createElement("div");
    var id = "make-a-screen-reader-talk-" + Date.now();
    el.setAttribute("id", id);
    if(!liveType || liveType == null)
    	el.setAttribute("aria-live", "assertive");
    else
    	el.setAttribute("aria-live", liveType);
    el.classList.add("visually-hidden");
    document.body.appendChild(el);
    
    window.setTimeout(function () {
        document.getElementById(id).innerHTML = text;      
    }, 100);
    
    window.setTimeout(function () {
        document.body.removeChild(document.getElementById(id));
    }, 1000);
}

var curFocus = null;
var standardFocusSelector =   '[role="button"]:not([tabindex="-1"]), '+
							  '[role="checkbox"]:not([tabindex="-1"]), '+
					  		  '[role="link"]:not([tabindex="-1"]), '+
					  		  '[tabindex]:not([tabindex="-1"])';
function openMyDialog(dialogId, doCloseOnEcape, okId){
	curFocus = document.activeElement;
	$("#mydialogoverlay").show();
	$("#"+dialogId).show();
	$("#"+dialogId+"Dialog").focus();
	$(document).unbind("keydown."+dialogId).bind("keydown."+dialogId, function(event){
		if(document.activeElement == document.getElementById(dialogId+"Dialog")){
			if(okId && event.keyCode == 13){
				$("#"+okId).click();
				event.preventDefault();
			}else if(doCloseOnEcape && event.keyCode == 27){
				$("#"+dialogId+"myclosebutton").click();
				event.preventDefault();
			}
		}
	});	
	trapFocus($("#"+dialogId+"Dialog"));
}
function closeMyDialog(dialogId){
	$(document).unbind("keydown."+dialogId);
	$("#"+dialogId).hide();
	$("#mydialogoverlay").hide();	
}
function isSomeDialogOpen(){
	return $("#mydialogoverlay").is(":visible");
}

// **********************************************
// Gestione popup jquery (per ora non utilizzata)
function onOpenJQueryAccessiblePopup(dialog, selector, noclose){
	if(!selector || selector == "")
		selector="";
	else
		selector = ", "+selector;
	$(dialog).attr("aria-hidden", "false").focus();	
	$(".ui-dialog-title").attr("aria-hidden","true");
	if(noclose){
		$(".ui-dialog-titlebar-close").hide();
		var page = dialog;
		if(page.length === 0)
			return;
		var $focusableElements = page.find(standardFocusSelector+selector);
		var firstElement = $focusableElements.first();
		var lastElement = $focusableElements.last();
		$(page).unbind('keydown').bind('keydown', function (event) {
		  if (event.keyCode === 9) {
			  if(event.shiftKey && document.activeElement === firstElement.get(0)) {
				event.preventDefault();
				lastElement.focus();
		      }else if(!event.shiftKey && document.activeElement === lastElement.get(0)) {
		    	event.preventDefault();
	    		firstElement.focus();
		      }
		  }
		});
	}else{
		const dialogContainer = $(dialog).closest(".ui-dialog");
		$(document).unbind("keydown.dialog").bind("keydown.dialog", function (event) {
			if(event.keyCode === 9) { // Tasto Tab
			    const focusableElements = dialogContainer.find('a, '+standardFocusSelector+selector).not('.ui-dialog-titlebar-close *');
			    const firstElement = focusableElements[0];
			    const lastElement = focusableElements[focusableElements.length - 1];
				if (event.shiftKey && document.activeElement === firstElement) {
					event.preventDefault();
					$(lastElement).focus();
			    }else if (!event.shiftKey && document.activeElement === lastElement) {
			    	event.preventDefault();
			    	$(firstElement).focus();
			    }
			}
		});
	}
}
function onCloseJQueryAccessiblePopup(dialog){
	$(document).unbind("keydown.dialog");
	$(dialog).attr("aria-hidden", "false");
	$(".ui-dialog-titlebar-close").show();
	trapPageFocus();
}
function openJQueryAccessiblePopup(name){
	curFocus = document.activeElement;
	$('#'+name).dialog('open');
}
function closeJQueryAccessiblePopup(name){
	$(document).unbind("keydown.dialog");
	$('#'+name).dialog('close');
	trapPageFocus();
}
//**********************************************

function trapPageFocus(selector) {
	var page = $(".htmlContainer");
	trapFocus(page, selector);
}

function trapFocus(page, selector) {
	var page = $(page);
	if(page.length === 0)
		return;

	if(!selector || selector == "")
		selector="";
	else
		selector = ", "+selector;
	var $focusableElements = page.find(standardFocusSelector+selector);
	var firstElement = $focusableElements.first();
	var lastElement = $focusableElements.last();
	$(page).unbind('keydown').bind('keydown', function (event) {
	  if (event.keyCode === 9) {
		  if(event.shiftKey && document.activeElement === firstElement.get(0)) {
			event.preventDefault();
			lastElement.focus();
	      }else if(!event.shiftKey && document.activeElement === lastElement.get(0)) {
	    	event.preventDefault();
    		firstElement.focus();
	      }
	  }
	});
}
