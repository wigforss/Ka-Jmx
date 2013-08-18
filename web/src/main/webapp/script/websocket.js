 var org = {};
 org.kasource = {};
 org.kasource.Websocket = {};

 org.kasource.Websocket.socket = null;
 
 org.kasource.Websocket.connect = (function(host) {
     if ('WebSocket' in window) {
    	 org.kasource.Websocket.socket = new WebSocket(host);
     } else if ('MozWebSocket' in window) {
    	 org.kasource.Websocket.socket = new MozWebSocket(host);
     } else {
         console.log('Error: WebSocket is not supported by this browser.');
         return;
     }
    
     org.kasource.Websocket.socket.onopen = function () {
         console.log('Info: WebSocket connection opened.');
     };

     org.kasource.Websocket.socket.onclose = function () {
         console.log('Info: WebSocket closed.');
     };

     org.kasource.Websocket.socket.onmessage = function (message) {
    	 org.kasource.Websocket.invokeListeners(message);
         //console.log(message);
     };
 });
 
 org.kasource.Websocket.initialize = function() {
     if (window.location.protocol == 'http:') {
    	 org.kasource.Websocket.connect('ws://' + window.location.host + '/ka-jmx/push');
     } else {
    	 org.kasource.Websocket.connect('wss://' + window.location.host + '/ka-jmx/push');
     }
 };
 /*
 org.kasource.Websocket.notifyListeners = function(message) {
	 var attributeValue = JSON.parse(message);
	 alert(message);
	 var objectName = attributeValue.key.name;
	 var attributeName = attributeValue.key.attributeName;
	 var key = objectName + "." + attributeName;
	 var attributeCallbacks = org.kasource.Websocket.listeners.attribute[key];
	 for (var i = 0; i < attributeCallbacks.length; i++) {
		 	attributeCallbacks[key](attributeValue);
		}
 };
 */
 org.kasource.Websocket.listeners= {};
 
 function getKeySize(obj) {
	 var count = 0;
	 for (var key in obj) {
		    if (obj.hasOwnProperty(key)) {
		        count = count + 1;
		    }
		}
	 return count;
 }
 
 function isEmpty(obj) {
	 return getKeySize() == 0;
 }
 
 org.kasource.Websocket.subscribe=function(objectName, attribute, id, callback) {
	 
	
	 var key = objectName + "." + attribute;
	 var attributeListeners = org.kasource.Websocket.listeners[key];
	 if(!attributeListeners) {
		 attributeListeners = {};
		 org.kasource.Websocket.listeners[key]=attributeListeners;
	 }
	 attributeListeners[id]=callback;
	 var jmxAttribute = {
			 "name": new String(objectName).replace('"','\"'),
			 "attributeName": attribute
	 };
	 var message = {
			 "key": jmxAttribute,
			 "subscribe": true
	 };
	
	 org.kasource.Websocket.socket.send(JSON.stringify(message));
 }; 
 
 
 
 org.kasource.Websocket.unsubscribe=function(objectName, attribute, id) {
	 var key = objectName + "." + attribute;
	 var attributeListeners = org.kasource.Websocket.listeners[key];
	 if(attributeListeners) {
		 delete attributeListeners[id];
		 if(isEmpty(attributeListeners)) {
			 delete org.kasource.Websocket.listeners[key];
			 var jmxAttribute = {
					 "name": new String(objectName).replace('"','\"'),
					 "attributeName": attribute
			 };
			 var message = {
					 "key": jmxAttribute,
					 "subscribe": false
			 };
			
			 org.kasource.Websocket.socket.send(JSON.stringify(message));
		 }
	 }
 }
 
 org.kasource.Websocket.invokeListeners=function(message) {
	 var jmxValue = JSON.parse(message.data);
	 if(org.kasource.Websocket.listeners){
		 var attributeKey = jmxValue.key.name + "." + jmxValue.key.attributeName;
		 var attributeListeners = org.kasource.Websocket.listeners[attributeKey];
		 for (var key in attributeListeners) {
			    if (attributeListeners.hasOwnProperty(key)) {
			    	var callback = attributeListeners[key];
			    	if(callback){
			    		callback(jmxValue);
			    	}
			    }
			}
	 }
 }
 
function logValue(jmxValue) {
	
	console.log("ObjectName: " + jmxValue.key.name+" Attribute: " + jmxValue.key.attributeName + " Value: " + jmxValue.value);
}




       

       