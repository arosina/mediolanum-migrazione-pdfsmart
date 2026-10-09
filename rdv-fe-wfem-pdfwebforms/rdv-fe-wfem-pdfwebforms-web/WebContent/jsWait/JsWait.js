function JsWait(webapp,anchorId){
	this.interval = null;
	this.webapp = webapp;
	this.anchorId = anchorId;
	this.loopingGif = true;
}

JsWait.prototype.start = function(){
	if(this.loopingGif)
		return;
	if(this.interval != null)
		return;
	var loop = 0;
	var anchorId = this.anchorId;
	var webapp = this.webapp;
	$("#"+anchorId).attr("src",webapp+"/jsWait/loading_"+loop+".png");
	this.interval = setInterval(function(){
											loop++;
											if(loop >= 16)
												loop=0;
											$("#"+anchorId).attr("src",webapp+"/jsWait/loading_"+loop+".png");
										},80);
}

JsWait.prototype.stop = function(){
	if(this.loopingGif)
		return;
	clearInterval(this.interval);
	this.interval = null;
}

function wait(){
	try{
		$(".waitDiv").show();
		jsWait.start();
		$(".waitDiv[role='status']").attr("aria-hidden", "false");
	}catch(e){}
}

function endWait(){
	try{
		$(".waitDiv").hide();
		jsWait.stop();
		$(".waitDiv[role='status']").attr("aria-hidden", "false");
	}catch(e){}
}

function onWfemHiddenSubmitEnd(){
	endWait();
}

var jsWait = new JsWait(__jsWebApp,'waitAnchor');