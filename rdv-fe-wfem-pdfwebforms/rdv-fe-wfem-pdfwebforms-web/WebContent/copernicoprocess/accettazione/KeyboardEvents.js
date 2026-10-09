$( document ).ready(function() {
    try{
        $('[role="button"], [role="link"]').bind({
            keypress: function(event){
            	if(event.which == 13 || event.which == 32) {
                    this.click();
                }
                event.preventDefault();
            }
        });
    }catch(e){}
});

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

var standardFocusSelector = '[role="button"]:not([tabindex="-1"]), '+
  '[role="link"]:not([tabindex="-1"]), '+
  '[role="checkbox"]:not([tabindex="-1"]), '+
  '[data-role="input"]:not([tabindex="-1"]), '+
  '[tabindex]:not([tabindex="-1"])';

function trapPageFocus(selector) {
	var page = $(".tabArea");
	trapFocus(page, selector);
}

function trapFocus(page, selector) {
	var page = $(page);
	if(page.length === 0)
		return;

	var $focusableElements = page.find(selector || standardFocusSelector).not('.ui-dialog *');
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

