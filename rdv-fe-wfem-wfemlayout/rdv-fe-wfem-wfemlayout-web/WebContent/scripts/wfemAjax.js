/* Nel IE viene fornito in automatico l'attributo readyState del Document. Il valore di questo attributo e' un
 * un intero e indica in che stato si trova il caricamento del file XML.
 *
 * Code String	        Description
 *  0	uninitialized	The DOM Document has been created, but the load() method has not yet been called.
 *  1	loading	        The load() method has been called and is executing.
 *  2	loaded	        Loading is complete, the DOM Document is now parsing the file.
 *  3	interactive	    Some of the XML data has been parsed, the DOM Document is now available, but is incomplete and read-only.
 *  4	completed	    The XML data has been fully parsed and the DOM Document is available in its entirety.
 *
 * Libreria fatta per la comunicazione in XMLHTTP (protocollo microsft) quindi non multipiattaforma e utilizzabile solo su
 * browser micrososft Explorer.
 */

/*************************************************************************/
/***************************** WFEM *************************************/
/*************************************************************************/
 
var Wfem = {
	Version: '0.1.1',
  	rootXml: './model',

	//-- Possibili librerie per il protocollo IXMLHTTPRequest --------------//
	getTransport: function() {
		return Try.these(
		function() {return new ActiveXObject("Msxml2.XMLHTTP.3.0")},      
		function() {return new ActiveXObject("Msxml2.XMLHTTP")},
		function() {return new ActiveXObject("Microsoft.XMLHTTP")},
		function() {return new XMLHttpRequest()}
		) || false;
	},
	//-- Possibili librerie per il protocollo IXMLDOMDocument/DOMDocument --//
	getXMLDocument: function() {
		return Try.these(
		function() {return new ActiveXObject("MSXML4.DOMDocument")},
		function() {return new ActiveXObject("MSXML3.DOMDocument")},
		function() {return new ActiveXObject("MSXML2.DOMDocument")},      
		function() {return new ActiveXObject("MSXML.DOMDocument")},            
		function() {return new ActiveXObject("Microsoft.XmlDom")},
		function() {return new XMLHttpRequest()}
		) || false;
	},
	//-- XML2Model --//
	parseXML : function(node,jsModel){
		try{
			if(node.nodeName != '#cdata-section' && node.nodeName != '#text' && node.getAttribute("type") == "list") {
				Wfem.parseXMLListNode(node, jsModel);
			}
			else {
				for (var i=0; i < node.childNodes.length; i++) {
					var nodeItem = node.childNodes[i];
					if (nodeItem.nodeName == '#cdata-section'){	//Foglia piena
						try{
							//Rimuovo gli apici singoli all'interno della stringa perche' nell'istruzione dopo
							//Faccio l'eval racchiudendo la stringa con gli apici singoli.						
							nodeItem.text = nodeItem.text.replace(/\'/g,"");
							eval("jsModel.value = \'" + (nodeItem.text)+"\'");
						} catch (e) {
							LOG.debug("ParseFoglia ----> nodeItem.nodeName:" +nodeItem.nodeName + " nodeItem.text:"+ nodeItem.text +" message: " + e.message + " descr: " + e.description);
						}
					}
					else if (nodeItem.nodeName == '#text'){	/* Caso di nodo <a></a> */ 
						try{
							if(nodeItem.text != null && nodeItem.text != "")
								eval("jsModel.value = \'" + (nodeItem.text)+"\'");						
						} catch (e) {
							LOG.debug("ParseFoglia ----> nodeItem.nodeName:" +nodeItem.nodeName + " nodeItem.text:"+ nodeItem.text +" message: " + e.message + " descr: " + e.description);
						}
					}
					else{
						try{
							eval("jsModel." + nodeItem.nodeName + "= new Object()");
						} catch (e) {
							LOG.debug("ParseNode ----> nodeItem.nodeName:" +nodeItem.nodeName + "nodeItem.text:"+ nodeItem.text +" message: " + e.message + " descr: " + e.description);
						}
						Wfem.parseXML(nodeItem,eval('jsModel.' + nodeItem.nodeName));
					}
				}
			}
			return jsModel;	
		} catch (e) {
			LOG.debug("Parse ----> nodeItem.nodeName:" +nodeItem.nodeName + "name:" +e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}	
	},
	parseXMLListNode : function(node,jsModel){
		try{
			var listNodeName = node.nodeName;
			eval("jsModel." + listNodeName + " = new Array();");
			eval("jsModel." + listNodeName + ".maxRowsExceeded = " + (node.getAttribute("maxRowsExceeded") == "true") + ";");
			for (var i=0; i < node.childNodes.length; i++) {
				var nodeItem = node.childNodes[i];
				var elementObj = new Object();
				elementObj = Wfem.parseXML(nodeItem, elementObj);
				try{
					eval("jsModel." + listNodeName + "["+i+"] = elementObj;");
				} catch (e) {
					LOG.debug("ParseNode ----> nodeItem.nodeName:" +nodeItem.nodeName + "nodeItem.text:"+ nodeItem.text +" message: " + e.message + " descr: " + e.description);
				}
			}
			return jsModel;	
		} catch (e) {
			LOG.debug("parseXMLListNode ----> nodeItem.nodeName:" +nodeItem.nodeName + "name:" +e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}	
	}
	
}

/*************************************************************************/
/***************************** BASE **************************************/
/*************************************************************************/
Wfem.Base = function() {};
Wfem.Base.prototype = (new Ajax.Base()).extend(
{
	//----------------------------------------------------------//
	setOptions: function(options) {
		try{
	    this.options = {method:'get',
	    				asynchronous: true,
	    				parameters:   '',
	    				parametersOutput: null,
	    				jsModel: null,
	    				xmlparsingEnabled: true}.extend(options || {});
	    } catch (e) {
			LOG.debug("Wfem.Base.setOptions ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}					
	},
	jsObject: function(root){
		try{
			//per leggere l'XML in response:
			LOG.debug("response XML ["+this.transport.responseXML.xml+"]"); 	
			if(this.transport.responseXML.xml == null || this.transport.responseXML.xml == "") {
				return new Object();
			}
			this.XMLDocument.loadXML(this.transport.responseXML.xml);
			if (this.XMLDocument.parseError.errorCode == 0){
				var rootNode = this.XMLDocument.documentElement.selectSingleNode(root!=null?root:Wfem.rootXml);
				if(!rootNode) {
					return new Object();
				}
				var jsModel = Wfem.parseXML(rootNode, new Object());
				return jsModel;
			}else{
				LOG.debug("errore Parse :"+this.XMLDocument.parseError.reason + " pos:" + this.XMLDocument.parseError.filepos + "\n"); 
				LOG.debug(this.transport.responseXML.xml);
			}
			return new Object();
		} catch (e) {
			LOG.debug("Wfem.Base.jsObject ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}		
	}
});
/***************************************************************************/
/***************************** REQUEST *************************************/
/***************************************************************************/
Wfem.Request = Class.create();
// Gestione Polimorfismo ( se mi ricordo bene Luca);
Wfem.Request.prototype.extend(Ajax.Request.prototype);
Wfem.Request.prototype.extend(Wfem.Base.prototype).extend(
{
	//----------------------------------------------------------//
	initialize: function(url, options) {
		try{
			this.transport = Wfem.getTransport();
			this.XMLDocument = Wfem.getXMLDocument();
		    this.setOptions(options);		 
			this.request(url);
		} catch (e) {
			LOG.debug("Wfem.Request.initialize ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	},
	//----------------------------------------------------------//	  
	request: function(url) {
		try {
			var parameters = this.options.parameters || '';

			if ((this.options.method == 'get')&&(parameters.length > 0)) url += '?' + parameters;
			
			this.transport.onreadystatechange = this.onStateChange.bind(this);
			
			this.transport.open(this.options.method,url,this.options.asynchronous);	
			
			if (this.options.asynchronous) 
				setTimeout((function() {this.respondToReadyState(1)}).bind(this), 10);
	
			this.setRequestHeaders();
	
			var body = this.options.postBody ? this.options.postBody : parameters;
			this.transport.send(this.options.method == 'post' ? body : null);
			 
		} catch (e) {
			LOG.debug("Wfem.Request.request ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	},
	//----------------------------------------------------------//	  
	respondToReadyState: function(readyState) {
		try{
		    var event = Ajax.Request.Events[readyState];
			//Viene chamato dinamicamente l'onSuccess onFailure o 
			//on['Uninitialized', 'Loading', 'Loaded', 'Interactive', 'Complete'] con parametro l'oggetto xmlhttp
			
			//Caso di complete
			
			if (event == 'Complete') {
				if(this.options.xmlparsingEnabled) {
					var rootXml = this.options.rootXml;
					this.options.jsModel = this.jsObject(rootXml!=null?rootXml:null);	
				}
				(this.options['on' + this.transport.status]
				|| this.options['on' + (this.responseIsSuccess() ? 'Success' : 'Failure')]
				|| Prototype.emptyFunction)(this.transport,this.options);
			}	
		
			//Caso in cui qualcosa sia andato male
			(this.options['on' + event] || Prototype.emptyFunction)(this.transport,this.options);
		
			/* Avoid memory leak in MSIE: clean up the oncomplete event handler */
			if (event == 'Complete') this.transport.onreadystatechange = Prototype.emptyFunction;
		} catch (e) {
			LOG.debug("Wfem.Request.respondToReadyState ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	}
});

/***************************************************************************/
/***************************** UPDATER *************************************/
/***************************************************************************/
/* Questo metodo Update sui chiama in questo modo:
 * var myAjax = new Wfem.Updater(
 *	 					Container,
 *						url, 
 *						{method: 'get', parameters: parametri, asynchronous:false}
 *						);
 *
 * Contaniner e' l'ID del campo da valorizzare tramite la chiamata XML. 
 * L'URL e'la stringa formata dal comando piu tutti i dati che servono al comando 
 * di display. I dati con cui riempire la combo si trovano in un 
 * nuovo campo dell'Xml chiamato listComboXML. Nel caso debbano essere valorizzate
 * piu combo, i dati si troveranno tutte all'interno di questo tag. Il listComboToBuild sull'URL
 * per indicare al comando di display si quali combo fare Xml. E' inutile fare XML di tutte
 * le combo, l'XML diventerebbe enorme, rendendo la comunicazione via XMLHTTP lenta e
 * inutile.
*/
Wfem.Updater = Class.create();
Wfem.Updater.prototype.extend(Wfem.Request.prototype).extend(
{
	//----------------------------------------------------------//
	initialize: function(objToUpdate, url, options) {
		try{
			this.containers = document.getElementById(objToUpdate);
			this.containers.name = objToUpdate;
			
			this.transport = Wfem.getTransport();
			this.XMLDocument = Wfem.getXMLDocument();
			this.setOptions(options);
					
			var onComplete = this.options.onComplete || Prototype.emptyFunction;
			//Faccio il bind tra la funzione agganciata dalla chiamata dell'utente e quella interna 
			//che fa l'update automatica del campo 
			this.options.onComplete =(
										function() {
											this.updateContent();
											onComplete(this.transport);
										}
									).bind(this);
			//Controllo sul tipo del campo da fare l'update
			if(this.containers.type.indexOf("select") != -1){
				//aggiunta del campo da fare refresh
				if(this.options.parameters.substr(this.options.parameters.length) == '?')
					this.options.parameters += "listComboToBuild=" + objToUpdate;
				else	
					this.options.parameters += "&listComboToBuild=" + objToUpdate;
			}		
			
			this.request(url);
		} catch (e) {
			LOG.debug("Wfem.Updater.initialize ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	},
	//----------------------------------------------------------//
	updateContent: function() {
		try{
		    if (this.responseIsSuccess()) {
		    	var typeT = new String(this.containers.type);
		    	//Controllo sul tipo del campo da fare l'update
		    	if(typeT.indexOf("select") != -1){
		    		//clear della combo.
			    	this.containers.options.length = 0;
			    	//update della combo
			    	//Viene passato solo il nodo che contiene tutte le combo da Updatare		
		    		this.updateSelect(this.options.jsModel.listComboXML.value);		    		
		    	}
		    }
	    } catch (e) {
			LOG.debug("Wfem.Updater.updateContent ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	},
	//----------------------------------------------------------//
	updateSelect:function(selectXML) {
		try{
			var selectDOM = Wfem.getXMLDocument();
			selectDOM.loadXML(selectXML);
			if(selectDOM.parseError.errorCode != 0){
				LOG.debug("errore Parse :"+selectDOM.parseError.reason + " pos:" + selectDOM.parseError.filepos + "\n Stringa in errore:[" + selectXML.substring(selectDOM.parseError.filepos)+"]"); 
				LOG.debug(selectXML);
				return;
			}
	   		//Controllo che il nodo contenga dei dati
	   		if(selectDOM.hasChildNodes){
				for (var i=0; i < selectDOM.childNodes.length; i++) {
					var nodeItem = selectDOM.childNodes[i];
					// Cerco il campo per fare il refresh
					if (nodeItem.nodeName == this.containers.name){
						for (var i=0; i < nodeItem.childNodes.length; i++){
							var optionItem = nodeItem.childNodes[i];
							//Costruisco l'elemento option
							var oOption = document.createElement("OPTION");
							this.containers.options.add(oOption);
							oOption.innerText = optionItem.text;
							oOption.value = optionItem.getAttribute("value");
							// valorizzo l'attributo selected
							if(optionItem.getAttribute("selected")){
								this.containers.selectedIndex = i;
							}
							// aggiungo lo style
							if(optionItem.getAttribute("style")){
								this.containers.style = optionItem.getAttribute("style");
							}
						}
					}
				}	
			}else{
			//Creo una combo vuota 
			LOG.debug("combo vuota");
			}	   		
		} catch (e) {
			LOG.debug("Wfem.Updater.updateSelect ----> name:" + e.name + " message: " + e.message + " n: "+ e.number + " descr: " + e.description);
		}
	}
});