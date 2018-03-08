/**
 * WUI Core Engine
 */

// WUI Core Self Contained Environment

var useSockets = false; // Enable Websockets

(function(){
	
	// Variables
	var elements = {};
	var element_cache = {};
	var timestamp = 0;

	// Get Element Definition
	function getElementDefinition(element_name, callback_done){
		// Get Element Definition
		$.ajax({
			url: window.location.href,
			headers: {'x-wui-request': 'element'},
			data: {element: element_name}
		
		}).done(function(data){
			console.debug("GOT ELEMENT DATA: ", data);
			
			// Register Element
			elements[element_name] = {
					"html": data.html,
					"initialize": new Function("node, data, element, performAction", data['js-initialize']),
					"setData": new Function("node, data, element, performAction", data['js-set-data'])
			}
			
			callback_done();

		});
		// TODO: IF this fails. DO something.
	}
	
	function readChildren(root, data){
		// Iterate Child Nodes
		console.debug("READING CHILDREN", data)
		$.each(data, function(key, value) {
		  console.debug("KEYVALUE: " + key + " :::: ", value);
		  
		  if(typeof root._wuidata.container[key] !== 'undefined'){
			  console.log("Appending Child Nodes to '" + key +"' in node: ", root._wuidata.container[key]);
			  
			  $.each(value, function(index, element) {
				  showElements(root, root._wuidata.container[key], element);
			  });
		  }
		});
	}

	// Show Elements
	function showElements(root, container, data){
		var element = elements[data.element];
		
		if(typeof element === 'undefined'){
			// Download Node
			console.debug("NODE DEFINITITION NOT FOUND. Downloading!!!. NODE ID: " + data.element, elements)
			var $node = $('<div>LOADING</div>');
			
			getElementDefinition(data.element, function(){
				// Replace Node Callback
				instantiateElement(root, container, data, $node[0]);
			});

			console.debug("Appending Element", $node[0])
			$(container).append($node);
		}else{
			console.debug("NODE DEFINITITION FOUND. NODE ID: " + data.element)
			instantiateElement(root, container, data);
		}
	}
	
	// Instantiate Element
	function instantiateElement(root, container, data, replace_node = null){
		
		var element = elements[data.element];
		var $node = $(element.html);
		element.initialize($node[0], data.data, data, performAction);
		element.setData($node[0], data.data, data, performAction);
		
		// Node Methods
		var node = $node[0];
		node._wuimethods = {
			setData: function(data){
				element.setData(node, data.data, data, performAction);
			}
		}
		
		element_cache[data.uuid] = $node[0];
		
		// Append Element
		if(replace_node != null){
			//console.debug("Replacing Element", node)
			console.debug("REPLACING PLACEHOLDER NODE "+ data.element +" WITH NEW NODE", node); //$node[0]
			console.debug("HTML DEFINITION: ",  element)
			$(replace_node).replaceWith(node);
		}else{
			console.debug("Appending Element", node)
			$(container).append(node);
		}
		
		
		// Populate Children
		if(typeof data.children !== 'undefined'){
			// Element has children
			console.debug("Node Has CHildren: ")
			readChildren(node, data.children);
		}
	}
	
	// Prepare Body Node
	document.body._wuidata = {
		container: {
			body: document.body
		}
	}

	// Get Page Content
	function getPageContent(){
		// Get Page Content
		
		doRequest({
			url: window.location.href,
			headers: {'x-wui-request': 'content'},
			data: {'t': timestamp}
			
		}, 'content')
		.onReceive(function(data){
			console.debug("GOT DATA: ", data);
			timestamp = data.timestamp; // Update Timestamp
			
			switch(data.type.toUpperCase()){
			case 'FULL': // Full Page Update
				$(document.body).empty();
				
				readChildren(document.body, data.nodes);
				
				/*
				$.each(data.nodes.body, function(key, value) {
				  console.debug(key, value);
				  
				  var el = showElements(document.body, value);

				  console.debug("Appending Element", el)

				  //$(document.body).append(el);
				  
				});
				*/
				break;
				
			case 'PARTIALDATA': // Partial Data Only Update
				$.each(data.updates, function(key, value) {
				  console.debug(key, value);
				  
				  // Get from cache
				  var element = element_cache[value.uuid]
				  if(typeof element === 'undefined'){
					  // ERROR: Unexpected state. Existing element is being updated but is not found in the DOM. Something went completely wrong.
					  console.error("ERROR: Element with UUID " + value.uuid + " does not exists. Inconsistent state. Requires full page update.");
					  // location.reload(); // TODO: Maybe there is a more elegant way to do this?
					  
				  }else{
					  // Update Element
					  console.debug("UPDATING ELEMENT: ", element);
					  element._wuimethods.setData(value);

				  }
				  
				});

				break;
			
			}			

		}).onClose(function(){
			getPageContent(); // Get Page Update
			
		});

	}

	
	
	
	// Responses
	
	// JQuery Response
	var JQResponse = function(jqrequest){
		this.jqrequest = jqrequest;
	};

	JQResponse.prototype = {
		onReceive: function(callback){
			this.jqrequest.done(callback);
			return this;
		},
		onClose: function(callback){
			this.jqrequest.always(callback);
			return this;
		}
		// TODO: Implement on fail
	}
	
	// WebSocket Response
	var WSResponse = function(callbacks){
		this.callbacks = callbacks;
	}
	
	WSResponse.prototype = {
		onReceive: function(callback){
			this.callbacks.onReceive.push(callback);
			return this;
		},
		onClose: function(callback){
			this.callbacks.onClose.push(callback);
			return this;
		}
		// TODO: Implement on fail
	}
	
	
	// Format Parameters
	function formatParameters(data){
    	var parameters; // Undefined
    	if(data){
    		parameters = {};
    		Object.keys(data).forEach(function(key) {
    			parameters[key] = [data[key]];
    		});
    	}
    	return parameters;
	}
	
	
	var wsConnectionPool = {};

	function doRequest(request, pool, ping_timeout = 0){
		// request.url
		// request.data
		// request.headers

		if (useSockets && window.WebSocket){ // Browser with WebSocket Support
			console.log("BROWSER SUPPORTED");
			
			// Reuse Open Connection
			var connection = wsConnectionPool[pool];
			if(typeof connection != 'undefined' && /*typeof connection.readyState != 'undefined' &&*/ connection.readyState == 1){
				console.debug("REUSING WS CONNECTION", connection);

				// Send Data
				connection.send(JSON.stringify({
					url: window.location.pathname,
					headers: request.headers,
					parameters: formatParameters(request.data)
				}));

				// TODO: Implement Callbacks (And how and when to flush them)
				
				return;
			}

			// Build WebSocket Connection URL
			var loc = window.location;
			var ws_uri =
				(loc.protocol === 'https:' ? 'wss:' : 'ws:') + // Protocol
				'//' + loc.host + // Host
				loc.pathname // Path Name
	
			// Initialize WebSocket Connection
			connection = new WebSocket(ws_uri);
				
			// Callbacks
		    var callbacks = {
	    		onReceive: [],
	    		onClose: []
		    }
		    
		    
		    var ping;
		    
			// Send Data
		    connection.addEventListener('open', function (event) {

		    	connection.send(JSON.stringify({
					url: loc.pathname,
					headers: request.headers,
					parameters: formatParameters(request.data)
				}));
				
		    	if(ping_timeout != 0)
		    		ping = setInterval(function(){ connection.send(""); }, ping_timeout); // Keep Alive Ping (TODO: May not be required by all servers)
			});
			
			// Receive Data	
		    connection.addEventListener('message', function (event) {
				console.log('Message from server', event.data);
				callbacks.onReceive.forEach(function(callback){
					callback(JSON.parse(event.data));
				});
			});
			
		    // Connection Error
		    connection.addEventListener('error', function (event) {
				console.debug("SERVER ERROR OCCURED", event);
			});
		    
			// Connection Closed
		    connection.addEventListener('close', function (event) {
				console.debug("SERVER CLOSED");
				clearInterval(ping);
				callbacks.onClose.forEach(function(callback){
					callback();
				});
			});
		    
		    // Add Connection to Pool
		    wsConnectionPool[pool] = connection;
			
			return new WSResponse(callbacks);

		} else { // Legacy Fallback
		     console.log("BROWSER NOT SUPPORTED");
		     
		     // Do HTTP Request
			var jqrequest = $.ajax({
				url: request.url,
				headers: request.headers,
				data: request.data
			});
			
			return new JQResponse(jqrequest);
		}

		
		//JSON.stringify({url:"/data",headers:{"x-wui-request":"content"},parameters:{"t":[999999999999999]}})
		
	}
	
	
	

	// Perform an Action
	function performAction(uuid){
		doRequest({
			url: window.location.href,
			headers: {'x-wui-request': 'action', 'x-wui-element': uuid}
		}, 'action', 2000);
	}
	
	
	
	
	
	getPageContent(); // Initial Run
})();